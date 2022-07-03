package com.diktapk.followroute.model.room.config;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.diktapk.followroute.model.clases.Direcciones;
import com.diktapk.followroute.model.room.daos.DireccionesDao;

@Database(entities = {Direcciones.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DireccionesDao direccionesDao();
}