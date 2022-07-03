package com.diktapk.followroute.utils;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Map;

public class Permission {

    public static final String TAG = "Permission";


    public static void requestPermission(Fragment fragment, String permission ,ActivityResultCallback<Boolean> result){
        fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), result).launch(permission);
    }


    public static void requestPermissions(Fragment fragment, OnGrantedPermissions listener, String... permissions){
        fragment.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                result -> verifyPermissions(result,listener)).launch(permissions);
    }

    public static void requestPermissions(Fragment fragment, String[] permissions, OnGrantedPermissions listener){
        fragment.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                result -> verifyPermissions(result,listener)).launch(permissions);
    }

    public static void requestPermissions(Fragment fragment, List<String> permissions, OnGrantedPermissions listener){
        fragment.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                result -> verifyPermissions(result,listener)).launch(permissions.toArray(new String[permissions.size()]));
    }


    private static void verifyPermissions(Map<String, Boolean> result, OnGrantedPermissions listener){
        for(Boolean granted: result.values())
            if(!granted){
                listener.status(false);
                return;
            }

        listener.status(true);
    }

    public interface OnGrantedPermissions{
        void status(boolean granted);
    }

}
