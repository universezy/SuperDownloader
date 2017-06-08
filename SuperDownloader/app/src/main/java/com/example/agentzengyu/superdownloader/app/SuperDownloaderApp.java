package com.example.agentzengyu.superdownloader.app;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

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
    private static List<Fragment> fragmentList = new ArrayList<>();
    private DownloadService service = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 加入一个活动到列表
     *
     * @param activity 活动实例
     */
    public void addActivity(Activity activity) {
        if (!activityList.contains(activity))
            activityList.add(activity);
    }

    /**
     * 从列表销毁一个活动
     *
     * @param activity 活动实例
     */
    public void destroyActivity(Activity activity) {
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
    public void destroyAllActivities() {
        for (Activity activity : activityList) {
            if (activity != null && !(activity instanceof MainActivity))
                activity.finish();
        }
        if (activityList.size() == 1 && activityList.get(0) instanceof MainActivity)
            destroyActivity(activityList.get(0));
        activityList.clear();
    }

    /**
     * 绑定服务
     *
     * @param service
     */
    public void setService(DownloadService service) {
        this.service = service;
    }

    /**
     * 获取服务
     *
     * @return
     */
    public DownloadService getService() {
        return this.service;
    }

    /**
     * 加入一个碎片到列表
     *
     * @param fragment
     */
    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }

    /**
     * 获取碎片
     *
     * @param fragment
     * @return
     */
    public Fragment getFragment(Class fragment) {
        for (Fragment f : fragmentList) {
            if (f.getClass().equals(fragment)) {
                return f;
            }
        }
        return null;
    }
}
