package com.juansenen.lepreroute;

import static com.juansenen.lepreroute.database.Constans.DATABASE_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.juansenen.lepreroute.adapter.RouteAdapter;
import com.juansenen.lepreroute.database.AppDataBase;
import com.juansenen.lepreroute.domain.Route;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Route> routeList;
    private RouteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        routeList = new ArrayList<>();

        //Recuperamos el recyclerview del layout
        RecyclerView recyclerView = findViewById(R.id.rcview_main);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //Construimos el adapter del recyclerview
        adapter = new RouteAdapter(this,routeList);
        recyclerView.setAdapter(adapter);

        registerForContextMenu(recyclerView);
    }

    //Al volver a la Activity principal, recuperamos los datos de la BD
    @Override
    protected void onResume() {
        super.onResume();

        final AppDataBase db = Room.databaseBuilder(this,AppDataBase.class,DATABASE_NAME)
                .allowMainThreadQueries().build();

        //Limpiamos la lista de rutas
        routeList.clear();
        //Añadimos los rutas de la BD
        routeList.addAll(db.routeDAO().getAll());
        //Notificamos cambios al adapter
        adapter.notifyDataSetChanged();

    }

    //Opciones de menu en la action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.but_actionbar_add) {
            // Intent para añadir nueva ruta
            Intent intent = new Intent(this, AddRouteActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}