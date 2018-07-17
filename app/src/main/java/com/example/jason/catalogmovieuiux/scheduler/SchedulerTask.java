package com.example.jason.catalogmovieuiux.scheduler;

import android.content.Context;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

public class SchedulerTask {
    private GcmNetworkManager mGcmNetworkManager;

    public SchedulerTask(Context context) {
        this.mGcmNetworkManager = GcmNetworkManager.getInstance(context);
    }

    public void createPeriodicTask(){
        Task periodicTask = new PeriodicTask.Builder()
                .setService(SchedulerService.class)
                .setPeriod(3 * 60 * 1000)
                .setFlex(20)
                .setTag(SchedulerService.TAG_UPCOMING)
                .setPersisted(true)
                .build();

        mGcmNetworkManager.schedule(periodicTask);
    }

    public void cancelPeriodicTask(){
        if(mGcmNetworkManager!=null){
            mGcmNetworkManager.cancelTask(SchedulerService.TAG_UPCOMING, SchedulerService.class);
        }
    }
}
