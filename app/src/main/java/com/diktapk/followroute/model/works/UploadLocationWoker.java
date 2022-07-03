package com.diktapk.followroute.model.works;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkQuery;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.diktapk.followroute.model.location.ManagerLocation;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.vavr.control.Try;

public class UploadLocationWoker extends Worker {

    public static final String TAG = "UploadLocationWoker";
    public static final String NAME = "update_location";

    public UploadLocationWoker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    public static void initWork(Context context){

        Log.d(TAG, " init work -> ");

        PeriodicWorkRequest periodicWorkRequest =
                new PeriodicWorkRequest.Builder(UploadLocationWoker.class, 1, TimeUnit.MINUTES)
                        .addTag(NAME)
                        .build();


        WorkManager
                .getInstance(context)
                .enqueueUniquePeriodicWork(
                        NAME,
                        ExistingPeriodicWorkPolicy.KEEP,
                        periodicWorkRequest);

        Log.d(TAG, " <- end init work ");

    }

    public static void infoWork(Context context, LifecycleOwner lifecycleOwner){
        WorkQuery workQuery = WorkQuery.Builder
                .fromTags(Arrays.asList(NAME))
                .addStates(Arrays.asList(WorkInfo.State.SUCCEEDED, WorkInfo.State.CANCELLED))
                .addUniqueWorkNames(Arrays.asList(NAME)
                )
                .build();

        //ListenableFuture<List<WorkInfo>> workInfos = WorkManager.getInstance(context).getWorkInfos(workQuery);


        WorkManager.getInstance(context).getWorkInfosLiveData(workQuery).observe(lifecycleOwner,workInfos -> {
            Try.of(() -> workInfos.stream()
                    .filter(workInfo -> workInfo != null)
                    .filter(workInfo -> workInfo.getState() != null)
                    .peek(workInfo -> Log.d(TAG, " work info -> "+workInfo))
                    .collect(Collectors.toList()))
                    .orElse(Try.success(new ArrayList<>()))
                    .onSuccess(workInfos1 -> {

                    })
                    .onFailure(throwable -> Log.e(TAG, "work infor error = "+throwable));
        });

    }


    public static void stopWork(Context context){
        WorkManager.getInstance(context).cancelUniqueWork(NAME);
    }


    @Override
    public Result doWork() {

        ManagerLocation.getInstance().getLocationAddress(address -> {
            if(address != null)
                Log.d(TAG, " direccion actualziada -> "+address);
            else
                Log.d(TAG, " no se encontro direccion");
        });

        return Result.success();
    }
}
