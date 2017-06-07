package com.example.agentzengyu.superdownloader.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
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
public class NewTaskFragment extends Fragment implements View.OnClickListener {
    private SuperDownloaderApp superDownloaderApp = null;

    private EditText metUrl, metWebsite;
    private WebView webView;

    public NewTaskFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_task, null);
        superDownloaderApp = (SuperDownloaderApp) getActivity().getApplication();
        initView(view);
        setVariable();
        superDownloaderApp.addFragmentToList(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化布局
     */
    private void initView(View view) {
        view.findViewById(R.id.btnDownload).setOnClickListener(this);
        view.findViewById(R.id.btnPasteUrl).setOnClickListener(this);
        view.findViewById(R.id.btnBack).setOnClickListener(this);
        view.findViewById(R.id.btnEnter).setOnClickListener(this);
        metUrl = (EditText) view.findViewById(R.id.etUrl);
        metWebsite = (EditText) view.findViewById(R.id.etWebsite);
        webView = (WebView) view.findViewById(R.id.webview);
    }

    /**
     * 设置变量
     */
    private void setVariable() {
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
                metWebsite.setText(url + "");
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        webView.setDownloadListener(new SuperDownloadListener());
        webView.setInitialScale(200);
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
//        webView.loadUrl("file:///android_asset/www/default.html");
        webView.loadUrl("https://www.baidu.com/");
    }

    /**
     * 下载入口
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void download() {
        String stringUrl = metUrl.getText().toString().trim();
        if ("".equals(stringUrl)) {
            Toast.makeText(getActivity(), "Nothing to download.", Toast.LENGTH_SHORT).show();
            return;
        }
        superDownloaderApp.getService().download(stringUrl);
    }

    /**
     * 获取剪贴板信息
     *
     * @return 剪贴板内容
     */
    private String getClipBoardContext() {
        String context = "";
        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager.hasPrimaryClip())
            context = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
        return context;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDownload:
                download();
                break;
            case R.id.btnPasteUrl:
                metUrl.setText(getClipBoardContext());
                break;
            case R.id.btnBack:
                webView.goBack();
                break;
            case R.id.btnEnter:
                String website = metWebsite.getText().toString().trim();
                if ("".equals(website))
                    return;
                if (website.toLowerCase().indexOf("http://") != 0 && website.toLowerCase().indexOf("https://") != 0)
                    website = "http://" + website;
                webView.loadUrl(website);
                break;
            default:
                break;
        }
    }

    class SuperDownloadListener implements DownloadListener {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            superDownloaderApp.getService().download(url);
        }
    }
}
