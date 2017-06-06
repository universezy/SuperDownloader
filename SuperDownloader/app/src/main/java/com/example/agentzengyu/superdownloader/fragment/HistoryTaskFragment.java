package com.example.agentzengyu.superdownloader.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.adapter.HistoryItemAdapter;
import com.example.agentzengyu.superdownloader.app.SuperDownloaderApp;
import com.example.agentzengyu.superdownloader.entity.HistoryDownloadItem;

import java.util.ArrayList;

/**
 * 历史任务
 */
public class HistoryTaskFragment extends Fragment implements View.OnClickListener {
    private SuperDownloaderApp superDownloaderApp = null;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HistoryItemAdapter historyItemAdapter;
    private ArrayList<HistoryDownloadItem> historyDownloadItems = new ArrayList<>();

    public HistoryTaskFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_task, null);
        superDownloaderApp = (SuperDownloaderApp) getActivity().getApplication();
        initView(view);
        superDownloaderApp.addFragmentToList(this);
        return view;
    }

    /**
     * 初始化布局
     */
    private void initView(View view ) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        historyItemAdapter = new HistoryItemAdapter(historyDownloadItems);
        historyItemAdapter.setItemClickListener(new HistoryItemAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, HistoryDownloadItem historyDownloadItem) {
                LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.ll);
                if (((ColorDrawable)linearLayout.getBackground()).getColor() == Color.parseColor("#87ceeb")) {
                    linearLayout.setBackgroundColor(Color.parseColor("#a9a9a9"));
                } else if (((ColorDrawable)linearLayout.getBackground()).getColor() == Color.parseColor("#a9a9a9")) {
                    linearLayout.setBackgroundColor(Color.parseColor("#87ceeb"));
                }
            }
        });
        recyclerView.setAdapter(historyItemAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public HistoryItemAdapter getHistoryItemAdater(){
        return this.historyItemAdapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAll:

                break;
            case R.id.tvDelete:

                break;
            case R.id.tvCancel:

                break;
            default:
                break;
        }
    }
}
