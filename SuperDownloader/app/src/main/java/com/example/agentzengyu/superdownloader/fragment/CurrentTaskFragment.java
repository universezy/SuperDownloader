package com.example.agentzengyu.superdownloader.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.adapter.ItemAdpter;
import com.example.agentzengyu.superdownloader.app.SuperDownloaderApp;
import com.example.agentzengyu.superdownloader.entity.DownloadItem;

import java.util.ArrayList;

/**
 * 当前任务
 */
public class CurrentTaskFragment extends Fragment implements View.OnClickListener {
    private SuperDownloaderApp superDownloaderApp = null;

    private Button mbtnTest;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ItemAdpter itemAdpter;
    private ArrayList<DownloadItem> downloadItems = new ArrayList<>();

    public CurrentTaskFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_task, null);
        superDownloaderApp = (SuperDownloaderApp) getActivity().getApplication();
        initView(view);
        return view;
    }

    /**
     * 初始化布局
     */
    private void initView(View view) {
        mbtnTest = (Button) view.findViewById(R.id.btnTest);
        mbtnTest.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        itemAdpter = new ItemAdpter(downloadItems);
        itemAdpter.setItemClickListener(new ItemAdpter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, DownloadItem downloadItem) {

            }
        });
        recyclerView.setAdapter(itemAdpter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTest:
                DownloadItem downloadItem = new DownloadItem();
                downloadItem.setMsg1("111");
                downloadItem.setMsg2("222");
                downloadItem.setMsg3("333");
                downloadItem.setMsg4("444");
                downloadItem.setName("555");
                itemAdpter.addItem(0,downloadItem);
                layoutManager.scrollToPosition(0);
                break;
            default:
                break;
        }
    }
}
