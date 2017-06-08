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
import com.example.agentzengyu.superdownloader.fragment.CurrentFragment;
import com.example.agentzengyu.superdownloader.fragment.HistoryFragment;
import com.example.agentzengyu.superdownloader.fragment.NewFragment;
import com.example.agentzengyu.superdownloader.fragment.SettingFragment;
import com.example.agentzengyu.superdownloader.service.DownloadService;

/**
 * 主活动
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private SuperDownloaderApp app = null;
    protected DownloadService.ServiceBinder binder;
    private ServiceConnection connection;
    private FragmentManager fragmentManager;

    private NewFragment newFragment;
    private CurrentFragment currentFragment;
    private HistoryFragment historyFragment;
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
        unbindService(connection);
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

    /**
     * 初始化碎片
     */
    private void initFragment() {
        newFragment = new NewFragment();
        currentFragment = new CurrentFragment();
        historyFragment = new HistoryFragment();
        settingFragment = new SettingFragment();
    }

    /**
     * 设置变量
     */
    private void setVariable() {
        app = (SuperDownloaderApp) getApplication();
        app.addActivity(this);
        setService();
        fragmentManager = getSupportFragmentManager();
    }

    /**
     * 设置服务
     */
    public void setService() {
        connection = new ServiceConnection() {
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
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.btnNew:
                transaction.replace(R.id.container, newFragment);
                transaction.commit();
                break;
            case R.id.btnCurrent:
                transaction.replace(R.id.container, currentFragment);
                transaction.commit();
                break;
            case R.id.btnHistory:
                transaction.replace(R.id.container, historyFragment);
                transaction.commit();
                break;
            case R.id.btnSetting:
                transaction.replace(R.id.container, settingFragment);
                transaction.commit();
                break;
            default:
                break;
        }
    }
}
