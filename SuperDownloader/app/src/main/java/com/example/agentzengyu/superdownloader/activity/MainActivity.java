package com.example.agentzengyu.superdownloader.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.app.SuperDownloaderApp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SuperDownloaderApp superDownloaderApp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setVariable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        findViewById(R.id.btnNew).setOnClickListener(this);
        findViewById(R.id.btnCurrent).setOnClickListener(this);
        findViewById(R.id.btnHistory).setOnClickListener(this);
        findViewById(R.id.btnExit).setOnClickListener(this);
    }

    private void setVariable() {
        superDownloaderApp = (SuperDownloaderApp) getApplication();
        superDownloaderApp.addActivityToList(this);
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
