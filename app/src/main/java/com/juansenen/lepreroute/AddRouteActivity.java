package com.juansenen.lepreroute;

import static com.juansenen.lepreroute.database.Constans.DATABASE_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.juansenen.lepreroute.database.AppDataBase;
import com.juansenen.lepreroute.domain.Route;

public class AddRouteActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
    }

    //Opciones de menu en la action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.but_menu_back) {
            // Intent regresar
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    public void butAddRoute(View view){

        //Recuperamos los elementos
        EditText ed_tipo = findViewById(R.id.edtxt_type);
        EditText ed_valoracion= findViewById(R.id.edtxt_rating);
        EditText ed_longitud = findViewById(R.id.edtxt_longitude);
        EditText ed_latitud = findViewById(R.id.edtxt_latitude);
        EditText ed_code = findViewById(R.id.edtxt_code);
        CheckBox chkDone = findViewById(R.id.checkBox_addroute);
        EditText ed_fecha = findViewById(R.id.edtxt_date);

        //Recuperamos los datos de los campos
        String tipo = ed_tipo.getText().toString();
        int valora = Integer.parseInt(ed_valoracion.getText().toString());
        float longi = Float.parseFloat(ed_longitud.getText().toString());
        float lati = Float.parseFloat(ed_latitud.getText().toString());
        String code = ed_code.getText().toString();
        String fecha = ed_fecha.getText().toString();
        boolean done = chkDone.isChecked();
        String imagen = "";

        //Creamos el objeto con los datos
        Route route = new Route(code,lati,longi,tipo,valora,fecha,done,imagen);

        //Insertamos los datos en la BD
        final AppDataBase db = Room.databaseBuilder(this, AppDataBase.class, DATABASE_NAME)
                .allowMainThreadQueries().build();
        db.routeDAO().insert(route);

        //Elemento emergente que indica se ha añadido
        Toast.makeText(this, R.string.Added, Toast.LENGTH_LONG).show();
        //Ponemos los campos vacios
        ed_tipo.setText("");
        ed_valoracion.setText("");
        ed_fecha.setText("");
        ed_code.setText("");
        ed_latitud.setText("");
        ed_longitud.setText("");
        chkDone.setChecked(false);

        //Levamos el foco al campo code
        ed_code.requestFocus();


    }
}