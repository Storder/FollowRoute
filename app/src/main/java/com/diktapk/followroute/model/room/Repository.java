package com.diktapk.followroute.model.room;

import android.content.Context;

import androidx.room.Room;

import com.diktapk.followroute.model.room.config.AppDatabase;
import com.diktapk.followroute.model.room.daos.DireccionesDao;

public class Repository {

    public static final String NAME_DATABASE = "FollowRouteDataBase";

    private static Repository repository;
    private AppDatabase database;


    public Repository(Context context) {
        database = Room.databaseBuilder(context,
                AppDatabase.class, NAME_DATABASE).build();
    }


    public static Repository init(Context context){
        if(repository == null)
            repository = new Repository(context);
        return repository;
    }

    public static Repository getInstance(){
        return repository;
    }


    public DireccionesDao getDireccionesDao(){
        return database.direccionesDao();
    }


}
