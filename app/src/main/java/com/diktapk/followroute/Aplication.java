package com.diktapk.followroute;

import android.app.Application;

import at.bluesource.choicesdk.core.ChoiceSdk;
import pub.devrel.easypermissions.AfterPermissionGranted;

public class Aplication  extends Application{


    @Override
    public void onCreate() {
        super.onCreate();
        ChoiceSdk.init(this);
    }


}
