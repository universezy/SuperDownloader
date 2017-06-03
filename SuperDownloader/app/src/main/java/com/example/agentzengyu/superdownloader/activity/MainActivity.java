package com.example.agentzengyu.superdownloader.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.app.SuperDownloaderApp;
import com.example.agentzengyu.superdownloader.service.DownloadService;

/**
 * 主活动
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SuperDownloaderApp superDownloaderApp = null;
    protected DownloadService.ServiceBinder binder;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setVariable();
        setService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        findViewById(R.id.btnNew).setOnClickListener(this);
        findViewById(R.id.btnCurrent).setOnClickListener(this);
        findViewById(R.id.btnHistory).setOnClickListener(this);
        findViewById(R.id.btnExit).setOnClickListener(this);
    }

    /**
     * 设置变量
     */
    private void setVariable() {
        superDownloaderApp = (SuperDownloaderApp) getApplication();
        superDownloaderApp.addActivityToList(this);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (DownloadService.ServiceBinder) service;  //获取其实例
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
    }

    /**
     * 设置服务
     */
    public void setService(){
        Intent intent = new Intent(MainActivity.this, DownloadService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNew:
                Intent intentNew = new Intent(this, NewTaskActivity.class);
                startActivity(intentNew);
                break;
            case R.id.btnCurrent:
                Intent intentCurrent = new Intent(this, CurrentTaskActivity.class);
                startActivity(intentCurrent);
                break;
            case R.id.btnHistory:
                Intent intentHistory = new Intent(this, HistoryTaskActivity.class);
                startActivity(intentHistory);
                break;
            case R.id.btnExit:
                superDownloaderApp.destroyService();
                superDownloaderApp.destroyAllActivitiesFromList();
                break;
            default:
                break;
        }
    }
}
