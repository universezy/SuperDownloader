package com.example.agentzengyu.superdownloader.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import com.example.agentzengyu.superdownloader.app.SuperDownloaderApp;
import com.example.agentzengyu.superdownloader.util.DownloadByUrlUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 下载服务，在后台操作
 */
public class DownloadService extends Service {
    private SuperDownloaderApp superDownloaderApp = null;

    @Override
    public void onCreate() {
        super.onCreate();
        superDownloaderApp = (SuperDownloaderApp) getApplication();
        superDownloaderApp.setService(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    public class ServiceBinder extends Binder {
    }

    /**
     * 通过URL下载
     * @param url
     */
    public void startNewTaskByUrl(String url){
        DownloadByUrlUtil downloadByUrlUtil = new DownloadByUrlUtil(getApplicationContext());
        downloadByUrlUtil.download(url);
    }

    /**
     * 通过磁力链接下载
     * @param link
     */
    public void startNewTaskByLink(String link){

    }

    /**
     * 通过torrent文件下载
     * @param uri
     */
    public void startNewTaskByTorrent(Uri uri){
        ContentResolver contentResolver = this.getContentResolver();
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);


            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

