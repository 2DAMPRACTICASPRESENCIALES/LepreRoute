package com.juansenen.lepreroute.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.juansenen.lepreroute.domain.Route;

//Indicamos las clases para la base de datos y la version de la base.
//Aumentamos la version segun modifiquemos aspectos de la base

@Database(entities = {Route.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    //Un DAO por cada clase
    public abstract Route carsDAO();
}
