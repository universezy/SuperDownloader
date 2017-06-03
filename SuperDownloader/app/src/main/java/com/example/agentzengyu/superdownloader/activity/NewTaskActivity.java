package com.example.agentzengyu.superdownloader.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.app.SuperDownloaderApp;

/**
 * 新建任务
 */
public class NewTaskActivity extends AppCompatActivity implements View.OnClickListener {
    private SuperDownloaderApp superDownloaderApp = null;

    private EditText metUrl, metLink, metTorrent, metWebsite;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        initView();
        setVariable();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            metTorrent.setText(data.getData().toString());
        }
        super.onActivityReenter(resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        findViewById(R.id.btnReturn).setOnClickListener(this);
        findViewById(R.id.btnDownload).setOnClickListener(this);
        findViewById(R.id.btnPasteUrl).setOnClickListener(this);
        findViewById(R.id.btnPasteLink).setOnClickListener(this);
        findViewById(R.id.btnChooseTorrent).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.btnEnter).setOnClickListener(this);
        metUrl = (EditText) findViewById(R.id.etUrl);
        metLink = (EditText) findViewById(R.id.etLink);
        metTorrent = (EditText) findViewById(R.id.etTorrent);
        metWebsite = (EditText) findViewById(R.id.etWebsite);
        webView = (WebView) findViewById(R.id.webview);
    }

    /**
     * 设置变量
     */
    private void setVariable() {
        superDownloaderApp = (SuperDownloaderApp) getApplication();
        superDownloaderApp.addActivityToList(this);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        webView.setInitialScale(200);
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        //webView.loadUrl("file:///android_asset/www/default.html");
        webView.loadUrl("https://www.baidu.com/");
    }

    /**
     * 下载入口
     */
    private void download() {
        String stringUrl = metUrl.getText().toString().trim();
        String stringLink = metLink.getText().toString().trim();
        String stringTorrent = metTorrent.getText().toString().trim();
        if ("".equals(stringUrl) && "".equals(stringLink) && "".equals(stringTorrent)) {
            Toast.makeText(this, "Nothing to download.", Toast.LENGTH_SHORT);
        } else if (!"".equals(stringUrl) && "".equals(stringLink) && "".equals(stringTorrent)) {
            superDownloaderApp.getService().startNewTaskByUrl(stringUrl);
        } else if ("".equals(stringUrl) && !"".equals(stringLink) && "".equals(stringTorrent)) {
            superDownloaderApp.getService().startNewTaskByLink(stringLink);
        } else if ("".equals(stringUrl) && "".equals(stringLink) && !"".equals(stringTorrent)) {
            if (!stringTorrent.toLowerCase().endsWith(".torrent")) {
                Toast.makeText(this, "Wrong file type!", Toast.LENGTH_SHORT);
            }
            superDownloaderApp.getService().startNewTaskByTorrent(Uri.parse(stringTorrent));
        } else {
            Toast.makeText(this, "Only one task allowed to execute.", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 获取剪贴板信息
     *
     * @return 剪贴板内容
     */
    private String getClipBoardContext() {
        String context = "";
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager.hasPrimaryClip())
            context = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
        return context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReturn:
                Intent intentReturn = new Intent(this, MainActivity.class);
                startActivity(intentReturn);
                break;
            case R.id.btnDownload:
                download();
                break;
            case R.id.btnPasteUrl:
                metUrl.setText(getClipBoardContext());
                break;
            case R.id.btnPasteLink:
                metLink.setText(getClipBoardContext());
                break;
            case R.id.btnChooseTorrent:
                Intent intentChooser = new Intent(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentChooser, 1);
                break;
            case R.id.btnBack:
                webView.goBack();
                break;
            case R.id.btnEnter:
                String website = metWebsite.getText().toString().trim();
                if ("".equals(website))
                    return;
                webView.loadUrl(website);
                break;
            default:
                break;
        }
    }
}
