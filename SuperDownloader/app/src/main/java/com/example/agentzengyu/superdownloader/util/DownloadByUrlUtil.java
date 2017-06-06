package com.example.agentzengyu.superdownloader.util;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.example.agentzengyu.superdownloader.app.SuperDownloaderApp;
import com.example.agentzengyu.superdownloader.entity.CurrentDownloadItem;
import com.example.agentzengyu.superdownloader.entity.HistoryDownloadItem;
import com.example.agentzengyu.superdownloader.fragment.CurrentTaskFragment;
import com.example.agentzengyu.superdownloader.fragment.HistoryTaskFragment;

import java.io.File;

/**
 * Created by ZengYu on 2017/6/5.
 */

public class DownloadByUrlUtil {
    private SuperDownloaderApp superDownloaderApp = null;
    private DownloadManager downloadManager;
    private Context context;
    private long downloadID;
    private BroadcastReceiver broadcastReceiver;
    private CurrentDownloadItem currentDownloadItem;

    public DownloadByUrlUtil(Context context) {
        this.context = context;
        superDownloaderApp = (SuperDownloaderApp) context.getApplicationContext();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkStatus();
            }
        };
    }

    public void download(String url) {
        String objectName = url.substring(url.lastIndexOf(File.separator) + 1);
        try {
            Request request = new Request(Uri.parse(url));
            //移动网络情况下是否允许漫游
            request.setAllowedOverRoaming(false);
            //在通知栏中显示，默认就是显示的
            request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
            request.setTitle(objectName);
            request.setDescription("SuperDownload");
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().getAbsolutePath(), objectName);
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            downloadID = downloadManager.enqueue(request);
            currentDownloadItem = new CurrentDownloadItem();
            currentDownloadItem.setName("'");
            currentDownloadItem.setMsg1("'");
            currentDownloadItem.setMsg2("'");
            currentDownloadItem.setMsg3("'");
            currentDownloadItem.setMsg4("'");
            ((CurrentTaskFragment) superDownloaderApp.getFragment(CurrentTaskFragment.class)).getCurrentItemAdpter().addItem(0, currentDownloadItem);
            context.registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } catch (IllegalArgumentException e) {
            Toast.makeText(context, "Wrong url.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkStatus() {
        Query query = new Query();
        query.setFilterById(downloadID);
        Cursor cursor = downloadManager.query(query);
        int currentSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
        int total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
        int progress = currentSize * 100 / total;
        ((CurrentTaskFragment) superDownloaderApp.getFragment(CurrentTaskFragment.class)).getCurrentItemAdpter().updateProgress(currentDownloadItem, progress);
        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    Toast.makeText(context, "Success to download.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, "Fail to download.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}
