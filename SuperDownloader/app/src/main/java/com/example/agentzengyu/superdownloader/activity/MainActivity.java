package com.example.agentzengyu.superdownloader.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.app.SuperDownloaderApp;
import com.example.agentzengyu.superdownloader.fragment.CurrentTaskFragment;
import com.example.agentzengyu.superdownloader.fragment.HistoryTaskFragment;
import com.example.agentzengyu.superdownloader.fragment.NewTaskFragment;
import com.example.agentzengyu.superdownloader.fragment.SettingFragment;
import com.example.agentzengyu.superdownloader.service.DownloadService;

/**
 * 主活动
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private SuperDownloaderApp superDownloaderApp = null;
    protected DownloadService.ServiceBinder binder;
    private ServiceConnection serviceConnection;
    private FragmentManager fragmentManager;

    private NewTaskFragment newTaskFragment;
    private CurrentTaskFragment currentTaskFragment;
    private HistoryTaskFragment historyTaskFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFragment();
        setVariable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    /**
     * 初始化布局
     */
    private void initView() {
        findViewById(R.id.btnNew).setOnClickListener(this);
        findViewById(R.id.btnCurrent).setOnClickListener(this);
        findViewById(R.id.btnHistory).setOnClickListener(this);
        findViewById(R.id.btnSetting).setOnClickListener(this);
    }

    private void initFragment() {
        newTaskFragment = new NewTaskFragment();
        currentTaskFragment = new CurrentTaskFragment();
        historyTaskFragment = new HistoryTaskFragment();
        settingFragment = new SettingFragment();
    }

    /**
     * 设置变量
     */
    private void setVariable() {
        superDownloaderApp = (SuperDownloaderApp) getApplication();
        superDownloaderApp.addActivityToList(this);
        setService();
        fragmentManager = getSupportFragmentManager();
    }

    /**
     * 设置服务
     */
    public void setService() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (DownloadService.ServiceBinder) service;  //获取其实例
                Toast.makeText(MainActivity.this, "Service has started.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        Intent intent = new Intent(MainActivity.this, DownloadService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.btnNew:
                fragmentTransaction.replace(R.id.container, newTaskFragment);
                fragmentTransaction.commit();
                break;
            case R.id.btnCurrent:
                fragmentTransaction.replace(R.id.container, currentTaskFragment);
                fragmentTransaction.commit();
                break;
            case R.id.btnHistory:
                fragmentTransaction.replace(R.id.container, historyTaskFragment);
                fragmentTransaction.commit();
                break;
            case R.id.btnSetting:
                fragmentTransaction.replace(R.id.container, settingFragment);
                fragmentTransaction.commit();
                break;
            default:
                break;
        }
    }
}
