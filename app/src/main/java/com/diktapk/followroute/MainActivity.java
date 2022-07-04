package com.diktapk.followroute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.diktapk.followroute.model.location.ManagerLocation;
import com.diktapk.followroute.model.service.UploadLocationService;

import at.bluesource.choicesdk.core.ChoiceSdk;
import at.bluesource.choicesdk.maps.common.MapFragment;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ManagerLocation.init(this);
        //Log.d(TAG ," disponible = "+ChoiceSdk.);
        //startForegroundService(new Intent(this, UploadLocationService.class));

    }

}