package com.juansenen.lepreroute;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mapbox.maps.MapView;

public class MapActivity extends AppCompatActivity {

    private MapView mapview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Recuperamos elementos del layout
        mapview = findViewById(R.id.mapAllRoutes);


    }
    //Menu en la action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Al pulsar en menu action bar regresamos a la pantalla principal
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }

}