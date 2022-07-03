package com.diktapk.followroute.model.location;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.io.IOException;

import java.util.Locale;

import io.vavr.control.Try;
import lombok.NonNull;

public class ManagerLocation {

    public static final String TAG = "ManagerLocation";
    /**(puede mandar acatualizaciones mas rapido del tiempo establecido)**/
    public static final int INTERVAL_UPDATE_LOCATION = 30000; //milisegundos
    /**(si manda actualizaciones mas rapido que el intervalo de INTERVAL_UPDATE_LOCATION define el tiempo maximo que si debe respetar cada llamada)**/
    public static final int WAIT_FOR_REQUEST_UPDATE_LOCATION = 10000; //milisegundos

    public static MutableLiveData<Address> address = new MutableLiveData<>();
    private static ManagerLocation managerLocation;
    public static boolean activeLocationUpdates = false;

    private FusedLocationProviderClient fusedLocationClient;
    private Activity activity;
    private LocationCallback locationCallback;

    public ManagerLocation(Activity activity) {
        this.activity = activity;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public static ManagerLocation init(Activity activity){
        managerLocation = new ManagerLocation(activity);
        return managerLocation;
    }

    public static ManagerLocation getInstance(){
        return managerLocation;
    }


    public void getLocation(@NonNull OnLocation listener) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(activity, location -> listener.location(location));
        }

    }

    public void getLocationAddress(@NonNull OnLocationAddress listener) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(activity,
                    location ->
                        Try.of(() -> getAddress(location))
                                .onSuccess(listener::location)
                                .onFailure(throwable -> listener.location(null)));

        }else
            listener.location(null);
    }


    private Address getAddress(Location location) throws IOException {
        if(location == null)
            return null;

        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        return geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1)
                .stream()
                .filter(address -> address != null)
                .findFirst()
                .orElse(null);
    }


    public void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(INTERVAL_UPDATE_LOCATION);
        locationRequest.setFastestInterval(WAIT_FOR_REQUEST_UPDATE_LOCATION);
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            activeLocationUpdates = true;
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    createLocationCallBack(),
                    Looper.getMainLooper());
        }
    }

    public LocationCallback createLocationCallBack(){
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null)
                    return;

                Try.of(() -> locationResult.getLocations()
                            .stream()
                            .findFirst())
                        .onSuccess(location -> saveLocationUpdateToAddress(location.get()))
                        .onFailure(throwable -> Log.e(TAG, " error get result locations "+throwable));

            }
        };
        return locationCallback;
    }

    private void saveLocationUpdateToAddress(Location location){
        Try.of(() -> getAddress(location))
                .onSuccess(address -> ManagerLocation.address.postValue(address))
                .onFailure(throwable -> Log.e(TAG, " error save location update "+throwable));
    }

    public void stopLocationUpate(){
        if(locationCallback != null){
            activeLocationUpdates = false;
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }


    public interface OnLocation{
        void location(Location location);
    }
    public interface OnLocationAddress{
        void location(Address address);
    }
}
