package com.example.agentzengyu.superdownloader.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private EditText metUrl, metLink, metTorrent, metWebsite;
    private WebView webView;

    public NewTaskFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_task, null);
        superDownloaderApp = (SuperDownloaderApp) getActivity().getApplication();
        initView(view);
        setVariable();
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
        view.findViewById(R.id.btnPasteLink).setOnClickListener(this);
        view.findViewById(R.id.btnChooseTorrent).setOnClickListener(this);
        view.findViewById(R.id.btnBack).setOnClickListener(this);
        view.findViewById(R.id.btnEnter).setOnClickListener(this);
        metUrl = (EditText) view.findViewById(R.id.etUrl);
        metLink = (EditText) view.findViewById(R.id.etLink);
        metTorrent = (EditText) view.findViewById(R.id.etTorrent);
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
            Toast.makeText(getActivity(), "Nothing to download.", Toast.LENGTH_SHORT).show();
        } else if (!"".equals(stringUrl) && "".equals(stringLink) && "".equals(stringTorrent)) {
            superDownloaderApp.getService().startNewTaskByUrl(stringUrl);
        } else if ("".equals(stringUrl) && !"".equals(stringLink) && "".equals(stringTorrent)) {
            superDownloaderApp.getService().startNewTaskByLink(stringLink);
        } else if ("".equals(stringUrl) && "".equals(stringLink) && !"".equals(stringTorrent)) {
            if (!stringTorrent.toLowerCase().endsWith(".torrent")) {
                Toast.makeText(getActivity(), "Wrong file type!", Toast.LENGTH_SHORT).show();
            }
            superDownloaderApp.getService().startNewTaskByTorrent(Uri.parse(stringTorrent));
        } else {
            Toast.makeText(getActivity(), "Only one task allowed to execute.", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

                break;
            case R.id.btnBack:
                webView.goBack();
                break;
            case R.id.btnEnter:
                String website = metWebsite.getText().toString().trim();
                if ("".equals(website))
                    return;
                if (website.toLowerCase().indexOf("http://") != 0)
                    website = "http://" + website;
                webView.loadUrl(website);
                break;
            default:
                break;
        }
    }
}
