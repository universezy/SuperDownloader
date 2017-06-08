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
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.example.agentzengyu.superdownloader.app.Config;
import com.example.agentzengyu.superdownloader.app.SuperDownloaderApp;
import com.example.agentzengyu.superdownloader.entity.CurrentItem;
import com.example.agentzengyu.superdownloader.entity.HistoryItem;

import java.io.File;
import java.util.ArrayList;

/**
 * 下载服务，在后台操作
 */
public class DownloadService extends Service {
    private SuperDownloaderApp app = null;
    private DownloadManager manager;
    private BroadcastReceiver receiver;
    private ArrayList<CurrentItem> currentItems = new ArrayList<>();
    private ArrayList<HistoryItem> historyItems = new ArrayList<>();
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        app = (SuperDownloaderApp) getApplication();
        app.setService(this);
        handler = new Handler();
        manager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkStatus();
            }
        };
        getApplicationContext().registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
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
            request.setDestinationInExternalPublicDir("/superdownload/",objectName);
            long downloadID = manager.enqueue(request);
            Toast.makeText(getApplicationContext(), "Start to download: " + objectName, Toast.LENGTH_SHORT).show();

            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadID);
            cursor = manager.query(query);
            CurrentItem currentItem = new CurrentItem();
            currentItem.setID(downloadID);
            currentItem.setPath(cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
            int indexTitle;
            if ((indexTitle = cursor.getColumnIndex(DownloadManager.COLUMN_TITLE)) >= 0) {
                String title = "", temp;
                if (cursor.moveToFirst() && !"".equals(temp = cursor.getString(indexTitle)))
                    title = temp;
                currentItem.setName(title);
            }
            addCurrentItem(currentItem);
            getProgress(query, currentItem, true, true);
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
        for (CurrentItem currentItem : currentItems) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(currentItem.getID());
            Cursor cursor = manager.query(query);
            if (cursor.moveToFirst()) {
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    //下载暂停
                    case DownloadManager.STATUS_PAUSED:
                        setStatus(currentItem, false);
                        break;
                    //下载延迟
                    case DownloadManager.STATUS_PENDING:
                        break;
                    //正在下载
                    case DownloadManager.STATUS_RUNNING:
                        setStatus(currentItem, true);
                        break;
                    //下载完成
                    case DownloadManager.STATUS_SUCCESSFUL:
                        Toast.makeText(getApplicationContext(), "Success to download.", Toast.LENGTH_SHORT).show();
                        currentItems.remove(currentItem);
                        HistoryItem historyItem = new HistoryItem();
                        historyItem.setName(currentItem.getName());
                        historyItem.setSize(currentItem.getSize());
                        historyItem.setID(currentItem.getID());
                        historyItem.setPath(currentItem.getPath());
                        addHistoryItem(historyItem);
                        break;
                    //下载失败
                    case DownloadManager.STATUS_FAILED:
                        Toast.makeText(getApplicationContext(), "Fail to download.", Toast.LENGTH_SHORT).show();
                        setStatus(currentItem, false);
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
     * @param item 下载任务
     */
    public void getProgress(final DownloadManager.Query query, final CurrentItem item, final boolean checkCurrentSize, final boolean checkTotalSize) {
        query.setFilterById(item.getID());
        final Cursor cursor = manager.query(query);
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
                        setSize(item, total);
                        nextCheckTotalSize = false;
                    }
                } else {
                    nextCheckTotalSize = false;
                }
                if (checkCurrentSize && currentSize >= 0) {
                    if (progress >= 0 && progress < 100) {
                        setProgress(item, progress);
                    } else if (progress >= 100) {
                        setProgress(item, 100);
                        return;
                    }
                }
                getProgress(query, item, nextCheckCurrentSize, nextCheckTotalSize);
                if (cursor != null) {
                    cursor.close();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public void setSize(CurrentItem item, long size) {
        int index = currentItems.indexOf(item);
        if (index >= 0) {
            currentItems.get(index).setSize(size);
        }
    }

    public void setProgress(CurrentItem item, int progress) {
        int index = currentItems.indexOf(item);
        if (index >= 0) {
            currentItems.get(index).setProgress(progress);
        }
    }

    public void setStatus(CurrentItem item, boolean downloading) {
        int index = currentItems.indexOf(item);
        if (index >= 0) {
            currentItems.get(index).setDownloading(downloading);
        }
    }

    public ArrayList<CurrentItem> getCurrentItems() {
        return this.currentItems;
    }

    public ArrayList<HistoryItem> getHistoryItems() {
        return this.historyItems;
    }

    public void addCurrentItem(CurrentItem item) {
        currentItems.add(item);
        Intent intent = new Intent(Config.SERVICE);
        intent.putExtra(Config.SUPERDOWNLOAD, Config.CURRENT);
        sendBroadcast(intent);
    }

    public void addHistoryItem(HistoryItem item) {
        historyItems.add(item);
        Intent intent = new Intent(Config.SERVICE);
        intent.putExtra(Config.SUPERDOWNLOAD, Config.HISTORY);
        sendBroadcast(intent);
    }

    public void removeCurrentItem(long downloadID) {
        manager.remove(downloadID);
        for (CurrentItem item : currentItems) {
            if (item.getID() == downloadID) {
                currentItems.remove(item);
                break;
            }
        }
        Intent intent = new Intent(Config.SERVICE);
        intent.putExtra(Config.SUPERDOWNLOAD, Config.CURRENT);
        sendBroadcast(intent);
    }

    public void removeHistoryItem(long downloadID) {
        manager.remove(downloadID);
        for (HistoryItem item : historyItems) {
            if (item.getID() == downloadID) {
                historyItems.remove(item);
                break;
            }
        }
        Intent intent = new Intent(Config.SERVICE);
        intent.putExtra(Config.SUPERDOWNLOAD, Config.HISTORY);
        sendBroadcast(intent);
    }
}

