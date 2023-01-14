package com.juansenen.lepreroute;

import static com.juansenen.lepreroute.database.Constans.DATABASE_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.juansenen.lepreroute.database.AppDataBase;
import com.juansenen.lepreroute.domain.Route;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;

public class ModifyActivity extends AppCompatActivity {

    private String coderoute;
    private EditText txtCode;
    private EditText txtType;
    private EditText txtRating;
    private EditText txtDate;
    private CheckBox checkDone;
    private Route route;
    private MapView mapView;
    private Point point;
    private PointAnnotationManager pointAnnotationManager;
    private double longitudePoint;
    private double latitudePoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        //Recuperamos la ruta por el atributo code
        Intent intent = getIntent();
        coderoute = intent.getStringExtra("code");
        if (coderoute == null)
            return;
        //Obtenemos datos de la BD
        final AppDataBase db = Room.databaseBuilder(this,AppDataBase.class,DATABASE_NAME)
                .allowMainThreadQueries().build();
        route = db.routeDAO().getByCode(coderoute);

        //Recuperamos el mapa
        mapView = findViewById(R.id.mapModify);

        //Obtenemos los elementos del layout de la Activity
        txtCode = findViewById(R.id.mod_code);
        txtType = findViewById(R.id.mod_type);
        txtRating = findViewById(R.id.mod_rating);
        txtDate = findViewById(R.id.mod_date);
        checkDone = findViewById(R.id.mod_checkBox_addroute);


        //Colocamos los datos de la base en los campos
        txtCode.setText(coderoute);
        txtRating.setText(String.valueOf(route.getRaiting()));
        txtType.setText(route.getType());
        txtDate.setText(route.getDate());
        checkDone.setChecked(route.isCompleted());

        latitudePoint = route.getLatitude();
        longitudePoint = route.getLongitude();

        //Metodos para colocar el marcador en el mapa segun los campos
        initializePointManager();
        addMarker(latitudePoint,longitudePoint);

    }
    private void initializePointManager() {
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        AnnotationConfig annotationConfig = new AnnotationConfig();
        pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, annotationConfig);

    }
    //Añadimos marcador en la ultima posicion
    private void addMarker(double latitude, double longitude) {
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(Point.fromLngLat(longitude, latitude))
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.mipmap.red_map_market_foreground));
        pointAnnotationManager.create(pointAnnotationOptions);
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
            // Intent para regresar
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    //Metodo al pulsar boton actualizar ruta
    public void butModdRoute(View view){
        route.setCode(txtCode.getText().toString());
        route.setType(txtType.getText().toString());
        route.setDate(txtDate.getText().toString());
        route.setCompleted(checkDone.isChecked());
        route.setLatitude(latitudePoint);
        route.setLongitude(longitudePoint);

        //Dialogo asegurar modificación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.wantupdate)
                .setTitle(R.string.Update)
                .setPositiveButton(R.string.Update, (dialog, id) -> {
                    final AppDataBase db = Room.databaseBuilder(this, AppDataBase.class, DATABASE_NAME)
                            .allowMainThreadQueries().build();
                    db.routeDAO().update(route);
                    Snackbar.make(txtCode, R.string.Updated,Snackbar.LENGTH_LONG).show();
                    Intent intent= new Intent (this, MainActivity.class);
                    startActivity(intent);

                })
                .setNegativeButton((R.string.Cancel), (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}