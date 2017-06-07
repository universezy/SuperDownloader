package com.example.agentzengyu.superdownloader.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.adapter.CurrentItemAdpter;
import com.example.agentzengyu.superdownloader.app.SuperDownloaderApp;
import com.example.agentzengyu.superdownloader.entity.CurrentDownloadItem;

/**
 * 当前任务
 */
public class CurrentTaskFragment extends Fragment implements View.OnClickListener {
    private SuperDownloaderApp superDownloaderApp = null;

    private Button mbtnTest;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CurrentItemAdpter currentItemAdpter;
    private DownloadManager downloadManager;
    private Handler handler;
    private Runnable runnable;

    public CurrentTaskFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_task, null);
        superDownloaderApp = (SuperDownloaderApp) getActivity().getApplication();
        initView(view);
        superDownloaderApp.addFragmentToList(this);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                currentItemAdpter.notifyDataSetChanged();
                updateList();
            }
        };
        downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        updateList();
        return view;
    }

    /**
     * 初始化布局
     */
    private void initView(View view) {
        view.findViewById(R.id.btnAll).setOnClickListener(this);
        view.findViewById(R.id.btnDelete).setOnClickListener(this);
        view.findViewById(R.id.btnCancel).setOnClickListener(this);
        mbtnTest = (Button) view.findViewById(R.id.btnTest);
        mbtnTest.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        currentItemAdpter = new CurrentItemAdpter(superDownloaderApp.getService().getCurrentDownloadItems());
        currentItemAdpter.setItemClickListener(new CurrentItemAdpter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, CurrentDownloadItem currentDownloadItem) {
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll);
                if (((ColorDrawable) linearLayout.getBackground()).getColor() == Color.parseColor("#87ceeb")) {
                    linearLayout.setBackgroundColor(Color.parseColor("#a9a9a9"));
                } else if (((ColorDrawable) linearLayout.getBackground()).getColor() == Color.parseColor("#a9a9a9")) {
                    linearLayout.setBackgroundColor(Color.parseColor("#87ceeb"));
                }
            }
        });
        recyclerView.setAdapter(currentItemAdpter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void updateList(){
        handler.postDelayed(runnable,1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTest:
                CurrentDownloadItem currentDownloadItem = new CurrentDownloadItem();
                currentDownloadItem.setName("555");
                currentDownloadItem.setSize(100000);
                superDownloaderApp.getService().addItemToCurrentDownloadItems(currentDownloadItem);
                currentItemAdpter.notifyDataSetChanged();
                layoutManager.scrollToPosition(0);
                break;
            case R.id.btnAll:

                break;
            case R.id.btnCancel:

                break;
            default:
                break;
        }
    }
}
