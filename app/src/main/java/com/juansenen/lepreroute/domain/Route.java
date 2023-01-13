package com.juansenen.lepreroute.domain;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rutas")
public class Route {

    //Establecemos llave primaria
    @PrimaryKey
    @NonNull
    public String code;
    @ColumnInfo
    public float latitude;
    @ColumnInfo
    public float longitude;
    @ColumnInfo
    public String type;
    @ColumnInfo
    public int raiting;
    @ColumnInfo
    public String date;
    @ColumnInfo
    public boolean isCompleted;
    @ColumnInfo
    public String img = "";

    public Route (){

    }

    public Route(@NonNull String code, float latitude, float longitude, String type, int raiting, String date, boolean isCompleted, String img) {
        this.code = code;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.raiting = raiting;
        this.date = date;
        this.isCompleted = isCompleted;
        this.img = img;
    }

    @NonNull
    public String getCode() {
        return code;
    }

    public void setCode(@NonNull String code) {
        this.code = code;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRaiting() {
        return raiting;
    }

    public void setRaiting(int raiting) {
        this.raiting = raiting;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
