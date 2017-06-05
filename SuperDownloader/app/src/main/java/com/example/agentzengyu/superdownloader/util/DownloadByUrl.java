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

import java.io.File;

/**
 * Created by ZengYu on 2017/6/5.
 */

public class DownloadByUrl {
    private DownloadManager downloadManager;
    private Context context;
    private long downloadID;
    private BroadcastReceiver broadcastReceiver;

    public DownloadByUrl(Context context) {
        this.context = context;
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
            context.registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }catch (IllegalArgumentException e){
            Toast.makeText(context,"Wrong url.",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkStatus() {
        Query query = new Query();
        query.setFilterById(downloadID);
        Cursor cursor = downloadManager.query(query);
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
