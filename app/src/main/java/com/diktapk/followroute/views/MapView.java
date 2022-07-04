package com.diktapk.followroute.views;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;


import com.diktapk.followroute.model.clases.Direcciones;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import at.bluesource.choicesdk.maps.common.CameraUpdate;
import at.bluesource.choicesdk.maps.common.CameraUpdateFactory;
import at.bluesource.choicesdk.maps.common.LatLng;
import at.bluesource.choicesdk.maps.common.Map;
import at.bluesource.choicesdk.maps.common.MapFragment;
import at.bluesource.choicesdk.maps.common.Marker;
import at.bluesource.choicesdk.maps.common.UiSettings;
import at.bluesource.choicesdk.maps.common.options.MarkerOptions;
import at.bluesource.choicesdk.maps.common.shape.PolylineOptions;
import io.reactivex.rxjava3.observers.DisposableObserver;
import lombok.Getter;
import lombok.Setter;

public class MapView extends FrameLayout {

    public static final String TAG = "MapView";
    public static final float ZOOM = 18f;
    public static final int TIME_MOVE_CAM = 2000;

    private MapFragment mapFragment;
    @Getter
    private Map map;
    @Setter
    private OnMap onMap;
    @Setter
    private Marker marker;
    private List<Marker> markers;


    public MapView(@NonNull Context context) {
        super(context);
        setColor();
    }

    public MapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setColor();
    }

    public MapView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setColor();
    }

    public MapView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setColor();
    }

    private void setColor(){
        this.setBackgroundColor(Color.parseColor("#8A8A8A"));
    }


    public void init(FragmentManager fm){

        mapFragment = MapFragment.newInstance();
        fm.beginTransaction().add(getId(),mapFragment).commit();

        mapFragment.getMapObservable().subscribeWith(createDisposableMap(mapa ->{
            UiSettings uiSettings = map.getUiSettings();
            //uiSettings.setCompassEnabled(true);
            uiSettings.setMyLocationButtonEnabled(true);
            uiSettings.setRotateGesturesEnabled(false);
            map.setMyLocationEnabled(true);
            map.setMapType(Map.MAP_TYPE_SATELLITE);
        }));

    }

    public void addMarker(LatLng latlng){
        addMarker(MarkerOptions.Factory.create()
                .defaultIcon()
                .position(latlng));
    }

    public void addMarker(double latitude, double longitude){
        LatLng latlng = new LatLng(latitude,longitude);
        addMarker(MarkerOptions.Factory.create()
                .defaultIcon()
                .position(latlng));
    }

    public void addMarker(Location location){
        LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
        addMarker(MarkerOptions.Factory.create()
                .defaultIcon()
                .position(latlng));
    }

    public Marker addMarker(MarkerOptions markerOptions){

        if(marker != null)
            marker.remove();

        if(map != null){
            this.marker = map.addMarker(markerOptions);
            return marker;
        }

        this.marker = null;
        return null;
    }

    public void addMultiMarkerFromDirecciones(List<Direcciones> direcciones){
        markers = new ArrayList<>();
        for(Direcciones direccion: direcciones){
            LatLng latlng = new LatLng(direccion.getLatitude(),direccion.getLongitude());
            markers.add(
                    map.addMarker(
                        MarkerOptions.Factory.create()
                            .defaultIcon()
                            .position(latlng)
                    )
            );
        }
    }

    public void addLines(List<Direcciones> locations){
        addLine(locations.stream()
                .map(direcciones -> new LatLng(direcciones.getLatitude(),direcciones.getLongitude()))
                .collect(Collectors.toList()));
    }

    public void addLine(List<LatLng> locations){
        map.addPolyline(
                new PolylineOptions()
                        .addAll(locations)
                        .width(15f)
                        .color(Color.RED)
                        //.startCap(Cap.RoundCap())
                        //.endCap(Cap.SquareCap())
                        .zIndex(2f)
        );
    }


    public void moveCamera(LatLng latlng){
        if(map != null){
            CameraUpdate cameraUpdate = CameraUpdateFactory.get().newLatLngZoom(latlng, ZOOM);
            map.animateCamera(cameraUpdate, TIME_MOVE_CAM, null);
        }
    }

    public void moveCamera(double latitude, double longitude){
        LatLng latlng = new LatLng(latitude,longitude);
        if(map != null){
            CameraUpdate cameraUpdate = CameraUpdateFactory.get().newLatLngZoom(latlng, ZOOM);
            map.animateCamera(cameraUpdate, TIME_MOVE_CAM, null);
        }
    }

    public void moveCamera(Location location){
        LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
        if(map != null){
            CameraUpdate cameraUpdate = CameraUpdateFactory.get().newLatLngZoom(latlng, ZOOM);
            map.animateCamera(cameraUpdate, TIME_MOVE_CAM, null);
        }
    }


    private DisposableObserver<Map> createDisposableMap(@NonNull OnMap onMap){
        return new DisposableObserver<Map>() {
            @Override public void onNext(@io.reactivex.rxjava3.annotations.NonNull Map map) {
                MapView.this.map = map;
                if(MapView.this.onMap != null)
                    MapView.this.onMap.readyMap(map);
                onMap.readyMap(map);
                Log.i(TAG," init succesfull map");
            }
            @Override public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                Log.e(TAG,"error init map "+e);
            }
            @Override public void onComplete() { }
        };
    }



    public interface OnMap{
        void readyMap(Map map);
    }
}
