package com.example.agentzengyu.superdownloader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.app.SuperDownloaderApp;

public class NewTaskActivity extends AppCompatActivity implements View.OnClickListener {
    private SuperDownloaderApp superDownloaderApp = null;

    private EditText metUrl, metLink, metFile;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        initView();
        setVariable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        findViewById(R.id.btnReturn).setOnClickListener(this);
        findViewById(R.id.btnDownload).setOnClickListener(this);
        findViewById(R.id.btnPasteUrl).setOnClickListener(this);
        findViewById(R.id.btnPasteLink).setOnClickListener(this);
        findViewById(R.id.btnChooseFile).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.btnEnter).setOnClickListener(this);
        (metUrl = (EditText) findViewById(R.id.etUrl)).setOnClickListener(this);
        (metLink = (EditText) findViewById(R.id.etLink)).setOnClickListener(this);
        (metFile = (EditText) findViewById(R.id.etFile)).setOnClickListener(this);
        webView = (WebView) findViewById(R.id.webview);
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
            case R.id.btnDownload:

                break;
            case R.id.btnPasteUrl:

                break;
            case R.id.btnPasteLink:

                break;
            case R.id.btnChooseFile:

                break;
            case R.id.btnBack:

                break;
            case R.id.btnEnter:

                break;
            case R.id.etUrl:

                break;
            case R.id.etLink:

                break;
            case R.id.etFile:

                break;
            default:
                break;
        }
    }
}
