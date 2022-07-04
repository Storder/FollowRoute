package com.diktapk.followroute.model.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diktapk.followroute.model.clases.Direcciones;
import com.diktapk.followroute.model.room.Repository;

import java.util.List;
import java.util.logging.Handler;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapsViewModel {

    public static final String TAG = "MapsViewModel";

    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<List<Direcciones>> direcciones = new MutableLiveData<>();


    public void getDireccionesDb(){
        disposable.add(Repository.getInstance().getDireccionesDao().getAll()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableSingleObserver<List<Direcciones>>() {
                            @Override
                            public void onSuccess(@NonNull List<Direcciones> direcciones) {
                                Log.d(TAG, "se obtivieron direcciones en db...");
                                MapsViewModel.this.direcciones.postValue(direcciones);
                                for(Direcciones dir : direcciones)
                                    Log.d(TAG , dir.toString());
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d(TAG, " error al obtener direcciones de db "+e);
                            }
                        }));
    }

}
