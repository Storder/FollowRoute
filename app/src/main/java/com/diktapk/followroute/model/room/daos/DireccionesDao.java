package com.diktapk.followroute.model.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.diktapk.followroute.model.clases.Direcciones;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface DireccionesDao {

    @Query("SELECT * FROM direcciones")
    Single<List<Direcciones>> getAll();

    @Insert
    void insertAll(Direcciones... direccioness);

    @Delete
    void delete(Direcciones direcciones);


}
