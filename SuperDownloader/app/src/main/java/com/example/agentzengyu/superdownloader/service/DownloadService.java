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

import java.io.File;
import java.util.ArrayList;

/**
 * 下载服务，在后台操作
 */
public class DownloadService extends Service {
    private SuperDownloaderApp superDownloaderApp = null;
    private DownloadManager downloadManager;
    private BroadcastReceiver broadcastReceiver;
    private ArrayList<CurrentDownloadItem> currentDownloadItems = new ArrayList<>();
    private ArrayList<HistoryDownloadItem> historyDownloadItems = new ArrayList<>();
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        superDownloaderApp = (SuperDownloaderApp) getApplication();
        superDownloaderApp.setService(this);
        handler = new Handler();
        downloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkStatus();
            }
        };
        getApplicationContext().registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
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
     * 下载入口
     *
     * @param url 下载链接
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void download(String url) {
        String objectName = url.substring(url.lastIndexOf(File.separator) + 1);
        Cursor cursor = null;
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
            Log.e("getAbsolutePath", Environment.getExternalStorageDirectory().getAbsolutePath());
            long downloadID = downloadManager.enqueue(request);
            Toast.makeText(getApplicationContext(), "Start to download: " + objectName, Toast.LENGTH_SHORT).show();

            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadID);
            cursor = downloadManager.query(query);
            CurrentDownloadItem currentDownloadItem = new CurrentDownloadItem();
            currentDownloadItem.setID(downloadID);
            int indexTitle;
            if ((indexTitle = cursor.getColumnIndex(DownloadManager.COLUMN_TITLE)) >= 0) {
                String title = "", temp;
                if (cursor.moveToFirst() && !"".equals(temp = cursor.getString(indexTitle)))
                    title = temp;
                currentDownloadItem.setName(title);
            }
            addItemToCurrentDownloadItems(currentDownloadItem);
            getProgress(query, currentDownloadItem, true, true);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "Wrong url.", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 检查状态
     */
    private void checkStatus() {
        for (CurrentDownloadItem currentDownloadItem : currentDownloadItems) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(currentDownloadItem.getID());
            Cursor cursor = downloadManager.query(query);
            if (cursor.moveToFirst()) {
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    //下载暂停
                    case DownloadManager.STATUS_PAUSED:
                        setStatus(currentDownloadItem, false);
                        break;
                    //下载延迟
                    case DownloadManager.STATUS_PENDING:
                        break;
                    //正在下载
                    case DownloadManager.STATUS_RUNNING:
                        setStatus(currentDownloadItem, true);
                        break;
                    //下载完成
                    case DownloadManager.STATUS_SUCCESSFUL:
                        Toast.makeText(getApplicationContext(), "Success to download.", Toast.LENGTH_SHORT).show();
                        currentDownloadItems.remove(currentDownloadItem);
                        HistoryDownloadItem historyDownloadItem = new HistoryDownloadItem();
                        historyDownloadItem.setName(currentDownloadItem.getName());
                        historyDownloadItem.setSize(currentDownloadItem.getSize());
                        addItemToHistoryDownloadItems(historyDownloadItem);
                        break;
                    //下载失败
                    case DownloadManager.STATUS_FAILED:
                        Toast.makeText(getApplicationContext(), "Fail to download.", Toast.LENGTH_SHORT).show();
                        setStatus(currentDownloadItem, false);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 下载进度
     *
     * @param currentDownloadItem 下载任务
     */
    public void getProgress(final DownloadManager.Query query, final CurrentDownloadItem currentDownloadItem, final boolean checkCurrentSize, final boolean checkTotalSize) {
        query.setFilterById(currentDownloadItem.getID());
        final Cursor cursor = downloadManager.query(query);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long currentSize = 0, total = -1;
                if (cursor.moveToFirst()) {
                    currentSize = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    total = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                }
                int progress = (int) (currentSize * 100 / total);
                boolean nextCheckCurrentSize = true, nextCheckTotalSize = true;
                if (checkTotalSize) {
                    if (total > 0) {
                        setSize(currentDownloadItem, total);
                        nextCheckTotalSize = false;
                    }
                } else {
                    nextCheckTotalSize = false;
                }
                if (checkCurrentSize && currentSize >= 0) {
                    if (progress >= 0 && progress < 100) {
                        setProgress(currentDownloadItem, progress);
                    } else if (progress >= 100) {
                        setProgress(currentDownloadItem, 100);
                        return;
                    }
                }
                getProgress(query, currentDownloadItem, nextCheckCurrentSize, nextCheckTotalSize);
                if (cursor != null) {
                    cursor.close();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public void setSize(CurrentDownloadItem currentDownloadItem, long size) {
        int index = currentDownloadItems.indexOf(currentDownloadItem);
        if (index >= 0) {
            currentDownloadItems.get(index).setSize(size);
        }
    }

    public void setProgress(CurrentDownloadItem currentDownloadItem, int progress) {
        int index = currentDownloadItems.indexOf(currentDownloadItem);
        if (index >= 0) {
            currentDownloadItems.get(index).setProgress(progress);
        }
    }

    public void setStatus(CurrentDownloadItem currentDownloadItem, boolean downloading) {
        int index = currentDownloadItems.indexOf(currentDownloadItem);
        if (index >= 0) {
            currentDownloadItems.get(index).setDownloading(downloading);
        }
    }

    public ArrayList<CurrentDownloadItem> getCurrentDownloadItems() {
        return this.currentDownloadItems;
    }

    public ArrayList<HistoryDownloadItem> getHistoryDownloadItems() {
        return this.historyDownloadItems;
    }

    public void addItemToCurrentDownloadItems(CurrentDownloadItem currentDownloadItem) {
        currentDownloadItems.add(currentDownloadItem);
    }

    public void addItemToHistoryDownloadItems(HistoryDownloadItem historyDownloadItem) {
        historyDownloadItems.add(historyDownloadItem);
    }
}

