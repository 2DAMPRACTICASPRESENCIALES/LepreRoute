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

import java.util.List;

public class MapActivity extends AppCompatActivity {

    private MapView mapview;
    private Route route;
    private Point point;
    private PointAnnotationManager pointAnnotationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Recuperamos elementos del layout
        mapview = findViewById(R.id.mapAllRoutes);

        //Recuperamos datos de la base
        final AppDataBase db = Room.databaseBuilder(this, AppDataBase.class, DATABASE_NAME)
                .allowMainThreadQueries().build();
        //Obtenemos los datos de la BD
        List<Route> routeList = db.routeDAO().getAll();

        initializePointManager();
        addAllMarker(routeList);

    }

    public void addAllMarker(List<Route> routeList){
        for (Route route: routeList
             ) {
            Point point = Point.fromLngLat(route.getLongitude(), route.getLatitude());
            addMarketPoint(point);

        }

    }
    private void initializePointManager() {
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapview);
        AnnotationConfig annotationConfig = new AnnotationConfig();
        pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, annotationConfig);

    }
    //Metodo para añadir marcador
    private void addMarketPoint(Point point) {
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.mipmap.red_map_market_foreground));
        pointAnnotationManager.create(pointAnnotationOptions);

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