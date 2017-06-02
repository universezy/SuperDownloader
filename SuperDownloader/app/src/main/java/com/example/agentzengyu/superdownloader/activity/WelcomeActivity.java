package com.example.agentzengyu.superdownloader.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.agentzengyu.superdownloader.R;

public class WelcomeActivity extends AppCompatActivity {
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initView();
        initVariable();
        setAction();
    }

    private void initView(){

    }

    private void initVariable(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intentMainActivity = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intentMainActivity);
                finish();
            }
        };
    }

    private void setAction(){
        handler.postDelayed(runnable,2000);
    }
}
