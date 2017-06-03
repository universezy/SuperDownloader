package com.example.agentzengyu.superdownloader.app;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.example.agentzengyu.superdownloader.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agent ZengYu on 2017/6/2.
 */

public class SuperDownloaderApp extends Application {
    private static List<Activity> activityList = new ArrayList<>();
    private Service service = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void addActivityToList(Activity activity) {
        if (!activityList.contains(activity))
            activityList.add(activity);
    }

    public void destroyActivityFromList(Activity activity) {
        if (activity == null) {
            return;
        }
        if (activityList.contains(activity)) {
            activityList.remove(activity);
            activity.finish();
        }
    }

    public void destroyAllActivitiesFromList() {

        for (Activity activity : activityList) {
            if (activity != null && !(activity instanceof MainActivity)) {
                activity.finish();
            }
        }
        if (activityList.size() == 1 && activityList.get(0) instanceof MainActivity)
            destroyActivityFromList(activityList.get(0));
        activityList.clear();
    }

    public void addService(Service service) {
        this.service = service;
    }

    public void destroyService() {
        if (this.service != null) {
            this.service.stopSelf();
            this.service = null;
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
