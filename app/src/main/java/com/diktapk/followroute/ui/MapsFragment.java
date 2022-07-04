package com.diktapk.followroute.ui;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.diktapk.followroute.databinding.FragmentMapsBinding;
import com.diktapk.followroute.model.clases.Direcciones;
import com.diktapk.followroute.model.location.ManagerLocation;
import com.diktapk.followroute.model.room.Repository;
import com.diktapk.followroute.model.service.UploadLocationService;
import com.diktapk.followroute.model.viewmodel.MapsViewModel;
import com.diktapk.followroute.model.works.UploadLocationWoker;
import com.diktapk.followroute.utils.Permission;
import com.diktapk.followroute.views.MapView;

import java.util.List;

import at.bluesource.choicesdk.maps.common.Map;
import io.reactivex.Single;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;

public class MapsFragment extends Fragment  implements MapView.OnMap{

    public static final String TAG = "MapsFragment";
    private FragmentMapsBinding binding;
    private MapsViewModel mapsViewModel = new MapsViewModel();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Permission.requestPermissions(this, this::verifyPermissions, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        binding.fabPrintRoutes.setOnClickListener(v -> {
            mapsViewModel.getDireccionesDb();
        });

        mapsViewModel.getDirecciones().observe(getViewLifecycleOwner(),direcciones -> {
            if(direcciones != null){
                Log.d(TAG, " se pintaran las direcciones en el mapa");
                binding.mapView.addLines(direcciones);
            }
        });

    }


    public void verifyPermissions(Boolean granted){
        Log.d(TAG, granted ? "permisos concedidos" : " permisos denegados");
        if(granted){
            binding.mapView.init(getChildFragmentManager());
            binding.mapView.setOnMap(this);
        }
    }


    @Override
    public void readyMap(Map map) {
        Log.d(TAG, " ready map");
        testUpdateLocations();
    }


    public void testUpdateLocations(){
        /*ManagerLocation.getInstance().startLocationUpdates();
        ManagerLocation.address.observe(getViewLifecycleOwner(),address -> {
            Log.d(TAG, " se actualizo las ubicaciones -> "+address);
            if(address != null){
                binding.mapView.addMarker(address.getLatitude(), address.getLongitude());
                binding.mapView.moveCamera(address.getLatitude(), address.getLongitude());
            }
        });*/


        /*ManagerLocation.getInstance().getLocation(location -> {
            if(location != null){
                binding.mapView.addMarker(location);
                binding.mapView.moveCamera(location);
            }
        });*/

        //UploadLocationWoker.initWork(requireContext());
        //UploadLocationWoker.infoWork(requireContext(),getViewLifecycleOwner());



        //requireActivity().startForegroundService(new Intent(requireContext(), UploadLocationService.class));
    }

}