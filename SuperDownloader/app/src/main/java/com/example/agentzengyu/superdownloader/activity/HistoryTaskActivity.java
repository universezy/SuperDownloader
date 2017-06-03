package com.example.agentzengyu.superdownloader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.app.SuperDownloaderApp;

public class HistoryTaskActivity extends AppCompatActivity implements View.OnClickListener {
    private SuperDownloaderApp superDownloaderApp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_task);

        initView();
        setVariable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        findViewById(R.id.btnReturn).setOnClickListener(this);
    }

    private void setVariable() {
        superDownloaderApp = (SuperDownloaderApp) getApplication();
        superDownloaderApp.addActivityToList(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReturn:
                Intent intentReturn = new Intent(this, MainActivity.class);
                startActivity(intentReturn);
                break;
            default:
                break;
        }
    }
}
