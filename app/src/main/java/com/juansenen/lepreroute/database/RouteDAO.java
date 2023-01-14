package com.juansenen.lepreroute.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.juansenen.lepreroute.domain.Route;

import java.util.List;

@Dao
public interface RouteDAO {
        //Obtener todos
        @Query("SELECT * FROM rutas")
        List<Route> getAll();
        //Obtener por codigo
        @Query("SELECT * FROM rutas WHERE code = :coderoute")
        Route getByCode(String coderoute);
        //Añadir
        @Insert
        void insert(Route routes);
        //Borrar
        @Delete
        void delete(Route routes);
        //Actualizar
        @Update
        void update(Route routes);


}
