package com.juansenen.lepreroute;

import static com.juansenen.lepreroute.database.Constans.DATABASE_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Intent;
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

public class ModifyActivity extends AppCompatActivity {

    private String coderoute;
    private EditText txtCode;
    private EditText txtType;
    private EditText txtRating;
    private EditText txtDate;
    private EditText txtLongitude;
    private EditText txtLatitude;
    private CheckBox checkDone;
    private Route route;

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

        //Obtenemos los elementos del layout de la Activity
        txtCode = findViewById(R.id.mod_code);
        txtType = findViewById(R.id.mod_type);
        txtRating = findViewById(R.id.mod_rating);
        txtDate = findViewById(R.id.mod_date);
        txtLatitude = findViewById(R.id.mod_latitude);
        txtLongitude = findViewById(R.id.mod_longitude);
        checkDone = findViewById(R.id.mod_checkBox_addroute);

        //Colocamos los datos de la base en los campos
        txtCode.setText(coderoute);
        txtRating.setText(String.valueOf(route.getRaiting()));
        txtType.setText(route.getType());
        txtDate.setText(route.getDate());
        txtLatitude.setText(String.valueOf(route.getLatitude()));
        txtLongitude.setText(String.valueOf(route.getLongitude()));
        checkDone.setChecked(route.isCompleted());
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

    //Metodo al pulsar boton actualizar coche
    public void butModdRoute(View view){
        route.setCode(txtCode.getText().toString());
        route.setType(txtType.getText().toString());
        route.setLatitude(Double.parseDouble(txtLatitude.getText().toString()));
        route.setLongitude(Double.parseDouble(txtLongitude.getText().toString()));
        route.setDate(txtDate.getText().toString());
        route.setCompleted(checkDone.isChecked());

        //Dialogo asegurar modiicación
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