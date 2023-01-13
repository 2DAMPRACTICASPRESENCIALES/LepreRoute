package com.juansenen.lepreroute.adapter;

//Adapter del RecyclerView de la MainActivity

import static com.juansenen.lepreroute.database.Constans.DATABASE_NAME;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.juansenen.lepreroute.R;
import com.juansenen.lepreroute.database.AppDataBase;
import com.juansenen.lepreroute.domain.Route;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteHolder> {

    private List<Route> routeList;
    private Context contex;

    public RouteAdapter(Context contex, List<Route> routeList) {
        this.contex = contex;
        this.routeList = routeList;

    }
    //Creamos ViewHolder e inicializamos campos del RecyclerView
    @Override
    public RouteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rcview_main_layout, parent,false);
        return new RouteHolder(view);
    }

    //Establecemos los datos sobre un Item del Recycler
    @Override
    public void onBindViewHolder(RouteHolder holder, int position) {
        holder.txtType.setText(routeList.get(position).getType());
        holder.txtRating.setText(String.valueOf(routeList.get(position).getRaiting()));
        holder.txtDate.setText(routeList.get(position).getDate());
        holder.checkIs.setChecked(routeList.get(position).isCompleted());

        //Si ruta imagen no esta cargada, se carga imagen por defecto
        if ((routeList.get(position).getImg()) == null){
            holder.imgRoute.setImageResource(R.drawable.notimagen);
        }else{
            holder.imgRoute.setImageURI(Uri.parse(routeList.get(position).getImg()));
        }
    }
    //Obtenemos el tamaño del listado
    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class RouteHolder extends RecyclerView.ViewHolder {

        public ImageView imgRoute;
        public ImageButton imgModRoute;
        public ImageButton imgDelRoute;
        public TextView txtType;
        public TextView txtRating;
        public TextView txtDate;
        public CheckBox checkIs;

        public View parentview;

        public RouteHolder(View view) {
            super(view);
            parentview = view;

            //Recuperamos los elementos del layout
            imgRoute = view.findViewById(R.id.imageView);
            imgDelRoute = view.findViewById(R.id.but_delete_route);
            imgModRoute = view.findViewById(R.id.but_modi_route);
            txtType = view.findViewById(R.id.rcview_txttype);
            txtRating = view.findViewById(R.id.rcview_rating);
            txtDate = view.findViewById(R.id.rcview_date);
            checkIs = view.findViewById(R.id.rcview_checkbox);

            //Boton con imagen de añadir ruta Establecemos click listener y cogemos posicion
            //en el recycler para saber que coche es
            imgModRoute = view.findViewById(R.id.but_modi_route);


            //Boton con imagen para eliminar ruta. Establecemos clisk listener y cogemos posicion
            //en el recycler para saber que coche es
            imgDelRoute = view.findViewById(R.id.but_delete_route);
            imgDelRoute.setOnClickListener(view1 -> deleteRoute(getAdapterPosition()));


        }
        //Metodo para borrar vehiculo
        public void deleteRoute(int position) {

            //Creamos dialogo de alerta con opciones
            AlertDialog.Builder builder = new AlertDialog.Builder(contex);
            builder.setMessage("Desea eliminar")
                    .setTitle("ELIMINAR")
                    .setPositiveButton("Si", (dialog, id) -> {
                        //Al pulsar en OK eliminamos vehiculo de la base de datos
                        final AppDataBase db = Room.databaseBuilder(contex, AppDataBase.class, DATABASE_NAME)
                                .allowMainThreadQueries().build();
                        Route car = routeList.get(position);
                        db.routeDAO().delete(car);

                        routeList.remove(position);
                        //Notificamos el cambio
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton(("Cancelar"), (dialog, id) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}