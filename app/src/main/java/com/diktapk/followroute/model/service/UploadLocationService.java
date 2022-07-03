package com.diktapk.followroute.model.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.diktapk.followroute.R;
import com.diktapk.followroute.model.clases.Direcciones;
import com.diktapk.followroute.model.location.ManagerLocation;
import com.diktapk.followroute.model.room.Repository;

import io.reactivex.rxjava3.core.Single;
import io.vavr.control.Try;

public class UploadLocationService extends Service {

    private static final String TAG = "UploadLocationService";
    // ya que este servicio accede a la ubicacion en el manifest declarar tipo de servicio location
    //si no es declarado la ubicacion va hacer null cada vez que se requiera consultar

    @Override
    public void onCreate() {
        //se crea un canal de notificacion del servicio que se ejecutara en segundo plano
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "locationupdateID";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Canal update locations",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Actualizacion ubicaciones")
                    .setContentText("se actualizaran las ubicaciones cada 3seg").build();

            startForeground(1, notification);
        }



        new Thread(() -> {
            while(true){
                ManagerLocation.getInstance().getLocationAddress(address -> {
                    if(address != null){
                        Log.d(TAG, " direccion actualzada -> "+address);
                        Try.of(() -> saveDirections(address))
                                .onSuccess(successfull -> Log.d(TAG, "se guardaron las rutas en base de datos"))
                                .onFailure(throwable -> Log.e(TAG, "error al guardar los datos en base de datos "+throwable));
                    } else
                        Log.d(TAG, " no se encontro direccion");
                });
                Try.of(this::sleep);
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "el servicio se inicio", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "el serivicio se inicio");




        return START_STICKY;
    }

    private boolean sleep() throws Exception{
        Thread.sleep(3000);
        return true;
    }

    private Single<Boolean> saveDirections(Address address){
        return Single.create(emitter -> {
            Repository.getInstance().getDireccionesDao().insertAll(Direcciones.transfor(address));
            emitter.onSuccess(Boolean.TRUE);
        });
    }


    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service se destruira", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "el serivicio se destruira");
    }

}
