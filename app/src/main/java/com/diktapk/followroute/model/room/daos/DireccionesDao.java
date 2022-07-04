package com.diktapk.followroute.model.room.daos;

import static androidx.room.OnConflictStrategy.REPLACE;

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
    Single<List<Long>> insertAll(Direcciones... direccioness);

    @Insert(onConflict = REPLACE)
    Single<Long> insert(Direcciones direccion);

    @Delete
    void delete(Direcciones direcciones);


}
