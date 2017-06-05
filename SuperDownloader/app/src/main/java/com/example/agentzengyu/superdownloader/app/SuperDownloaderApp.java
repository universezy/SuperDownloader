package com.example.agentzengyu.superdownloader.app;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.example.agentzengyu.superdownloader.activity.MainActivity;
import com.example.agentzengyu.superdownloader.service.DownloadService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZengYu on 2017/6/2.
 */

/**
 * 下载应用
 */
public class SuperDownloaderApp extends Application {
    private static List<Activity> activityList = new ArrayList<>();
    private DownloadService service =null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 加入一个活动到列表
     *
     * @param activity 活动实例
     */
    public void addActivityToList(Activity activity) {
        if (!activityList.contains(activity))
            activityList.add(activity);
    }

    /**
     * 从列表销毁一个活动
     *
     * @param activity 活动实例
     */
    public void destroyActivityFromList(Activity activity) {
        if (activity == null)
            return;
        if (activityList.contains(activity)) {
            activityList.remove(activity);
            activity.finish();
        }
    }

    /**
     * 销毁所有活动
     */
    public void destroyAllActivitiesFromList() {
        for (Activity activity : activityList) {
            if (activity != null && !(activity instanceof MainActivity))
                activity.finish();
        }
        if (activityList.size() == 1 && activityList.get(0) instanceof MainActivity)
            destroyActivityFromList(activityList.get(0));
        activityList.clear();
    }

    public void setService(DownloadService service){
        this.service = service;
    }

    public DownloadService getService(){
        return this.service;
    }
}
