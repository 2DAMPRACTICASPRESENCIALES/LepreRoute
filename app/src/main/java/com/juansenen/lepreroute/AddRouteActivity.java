package com.juansenen.lepreroute;

import static com.juansenen.lepreroute.database.Constans.DATABASE_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.juansenen.lepreroute.database.AppDataBase;
import com.juansenen.lepreroute.domain.Route;
import com.mapbox.geojson.Point;
import com.mapbox.maps.MapView;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.gestures.GesturesUtils;

public class AddRouteActivity extends AppCompatActivity {


    private MapView mapView;
    private Point point;
    private PointAnnotationManager pointAnnotationManager;
    private double latitudeMap;
    private double longitudeMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);

        //Recuperamos los elementos del layout
        mapView = findViewById(R.id.mapActivity);

        //Metodo para controlar el mapa y añadir marcador al pulsar sobre una posicion
        GesturesPlugin gesturesPlugin = GesturesUtils.getGestures(mapView);
        gesturesPlugin.addOnMapClickListener(point -> {
            delAllMakers();
            this.point = point;
            addMarketPoint(point);
            return true;
        });
        initializePointManager();
    }

    //Opciones de menu en la action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;

    }
    //Metodo para añadir marcador
    private void addMarketPoint(Point point) {
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.mipmap.red_map_market_foreground));
        pointAnnotationManager.create(pointAnnotationOptions);

    }
    //Borrar todos los marcadores
    private void delAllMakers() {
        pointAnnotationManager.deleteAll();
    }
    private void initializePointManager() {
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        AnnotationConfig annotationConfig = new AnnotationConfig();
        pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, annotationConfig);

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
        EditText ed_code = findViewById(R.id.edtxt_code);
        CheckBox chkDone = findViewById(R.id.checkBox_addroute);
        EditText ed_fecha = findViewById(R.id.edtxt_date);

        //Recuperamos los datos de los campos
        String tipo = ed_tipo.getText().toString();
        int valora = Integer.parseInt(ed_valoracion.getText().toString());

        String code = ed_code.getText().toString();
        String fecha = ed_fecha.getText().toString();
        boolean done = chkDone.isChecked();
        String imagen = null;

        //Creamos el objeto con los datos
        Route route = new Route(code,point.latitude(), point.longitude(), tipo,valora,fecha,done,imagen);

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
        chkDone.setChecked(false);

        //Levamos el foco al campo code
        ed_code.requestFocus();


    }
}