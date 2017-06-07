package com.example.agentzengyu.superdownloader.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.example.agentzengyu.superdownloader.app.SuperDownloaderApp;
import com.example.agentzengyu.superdownloader.entity.CurrentDownloadItem;
import com.example.agentzengyu.superdownloader.entity.HistoryDownloadItem;
import com.example.agentzengyu.superdownloader.fragment.CurrentTaskFragment;
import com.example.agentzengyu.superdownloader.fragment.HistoryTaskFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 下载服务，在后台操作
 */
public class DownloadService extends Service {
    private SuperDownloaderApp superDownloaderApp = null;
    private DownloadManager downloadManager;
    private BroadcastReceiver broadcastReceiver;
    private Map<Long, CurrentDownloadItem> map = new HashMap<>();
    private ArrayList<Long> arrayList = new ArrayList<>();
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        superDownloaderApp = (SuperDownloaderApp) getApplication();
        superDownloaderApp.setService(this);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkStatus();
            }
        };
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void download(String url) {
        String objectName = url.substring(url.lastIndexOf(File.separator) + 1);
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            //移动网络情况下是否允许漫游
            request.setAllowedOverRoaming(false);
            request.setAllowedOverMetered(false);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            //在通知栏中显示，默认就是显示的
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setTitle(objectName);
            request.setDescription("SuperDownload");
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().getAbsolutePath(), objectName);
            getApplicationContext().registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            downloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
            long downloadID = downloadManager.enqueue(request);
            Toast.makeText(getApplicationContext(), "Start to download: " + objectName, Toast.LENGTH_SHORT).show();
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadID);
            Cursor cursor = downloadManager.query(query);
            CurrentDownloadItem currentDownloadItem = new CurrentDownloadItem();
            int indexTitle = cursor.getColumnIndex(DownloadManager.COLUMN_TITLE);
            if (indexTitle >= 0) {
                if (cursor.moveToFirst())
                    currentDownloadItem.setName("" + cursor.getString(indexTitle));
            }
            int indexTotal = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            if (indexTotal >= 0) {
                if (cursor.moveToFirst())
                    currentDownloadItem.setMsg1("" + cursor.getLong(indexTotal));
            }
            if(cursor!=null){
                cursor.close();
            }
            currentDownloadItem.setMsg2("");
            currentDownloadItem.setMsg3("");
            currentDownloadItem.setMsg4("");
            map.put(downloadID, currentDownloadItem);
            arrayList.add(downloadID);
            ((CurrentTaskFragment) superDownloaderApp.getFragment(CurrentTaskFragment.class)).getCurrentItemAdpter().addItem(0, currentDownloadItem);
            getProgress(downloadID);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "Wrong url.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkStatus() {
        for (Long id : arrayList) {
            CurrentDownloadItem currentDownloadItem = map.get(id);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            Cursor cursor = downloadManager.query(query);
            if (cursor.moveToFirst()) {
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    //下载暂停
                    case DownloadManager.STATUS_PAUSED:
                        ((CurrentTaskFragment) superDownloaderApp.getFragment(CurrentTaskFragment.class)).getCurrentItemAdpter().setDownloadStatus(currentDownloadItem, false);
                        break;
                    //下载延迟
                    case DownloadManager.STATUS_PENDING:
                        break;
                    //正在下载
                    case DownloadManager.STATUS_RUNNING:
                        break;
                    //下载完成
                    case DownloadManager.STATUS_SUCCESSFUL:
                        Toast.makeText(getApplicationContext(), "Success to download.", Toast.LENGTH_SHORT).show();
                        ((CurrentTaskFragment) superDownloaderApp.getFragment(CurrentTaskFragment.class)).getCurrentItemAdpter().removeItem(currentDownloadItem);
                        HistoryDownloadItem historyDownloadItem = new HistoryDownloadItem();
                        historyDownloadItem.setName(currentDownloadItem.getName());
                        historyDownloadItem.setMsg1(currentDownloadItem.getMsg1());
                        historyDownloadItem.setMsg2(currentDownloadItem.getMsg2());
                        historyDownloadItem.setMsg3(currentDownloadItem.getMsg3());
                        historyDownloadItem.setMsg4(currentDownloadItem.getMsg4());
                        ((HistoryTaskFragment) superDownloaderApp.getFragment(HistoryTaskFragment.class)).getHistoryItemAdater().addItem(0, historyDownloadItem);
                        break;
                    //下载失败
                    case DownloadManager.STATUS_FAILED:
                        Toast.makeText(getApplicationContext(), "Fail to download.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void getProgress(final long downloadID) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadID);
        final Cursor cursor = downloadManager.query(query);
        handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentSize = 1, total = 1;
                if (cursor.moveToFirst())
                    currentSize =
                            cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                if (cursor.moveToFirst())
                    total = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                int progress = currentSize * 100 / total;
                ((CurrentTaskFragment) superDownloaderApp.getFragment(CurrentTaskFragment.class)).getCurrentItemAdpter().updateProgress(map.get(downloadID), progress);
                if(cursor!=null){
                    cursor.close();
                }
                getProgress(downloadID);
            }
        };
        handler.postDelayed(runnable, 1000);
    }
}

