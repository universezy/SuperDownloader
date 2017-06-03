package com.example.agentzengyu.superdownloader.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.agentzengyu.superdownloader.R;

/**
 * 欢迎页面
 */
public class WelcomeActivity extends AppCompatActivity {
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initView();
        setVariable();
        setAction();
    }

    /**
     * 初始化布局
     */
    private void initView() {

    }

    /**
     * 初始化变量
     */
    private void setVariable() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intentMainActivity = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intentMainActivity);
                finish();
            }
        };
    }

    /**
     * 设置动作
     */
    private void setAction() {
        handler.postDelayed(runnable, 2000);
    }
}
