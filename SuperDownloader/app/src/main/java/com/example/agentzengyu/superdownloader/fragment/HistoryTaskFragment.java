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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private DownloadManager downloadManager;
    private Handler handler;
    private Runnable runnable;
    private ArrayList<HistoryDownloadItem> selectList= new ArrayList<>();

    public HistoryTaskFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_task, null);
        superDownloaderApp = (SuperDownloaderApp) getActivity().getApplication();
        initView(view);
        superDownloaderApp.addFragmentToList(this);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                historyItemAdapter.notifyDataSetChanged();
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
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        historyItemAdapter = new HistoryItemAdapter(superDownloaderApp.getService().getHistoryDownloadItems());
        historyItemAdapter.setItemClickListener(new HistoryItemAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, HistoryDownloadItem historyDownloadItem) {
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll);
                ImageView imageView = (ImageView)linearLayout.findViewById(R.id.ivSelect);
                if(((ColorDrawable)imageView.getBackground()).getColor()==Color.parseColor("#d3d3d3")){
                    ((ColorDrawable)imageView.getBackground()).setColor(Color.parseColor("#ff0000"));
                    selectList.add(historyDownloadItem);
                }else{
                    ((ColorDrawable)imageView.getBackground()).setColor(Color.parseColor("#d3d3d3"));
                    selectList.remove(historyDownloadItem);
                }
            }
        });
        recyclerView.setAdapter(historyItemAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void updateList() {
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAll:
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) recyclerView.getChildAt(i);
                    ImageView imageView = (ImageView)linearLayout.findViewById(R.id.ivSelect);
                    imageView.setBackgroundColor(Color.parseColor("#ff0000"));
                }
                break;
            case R.id.btnDelete:
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) recyclerView.getChildAt(i);
                    ImageView imageView = (ImageView)linearLayout.findViewById(R.id.ivSelect);
                    if(((ColorDrawable)imageView.getBackground()).getColor()==Color.parseColor("#ff0000")){
                        TextView textView = (TextView) linearLayout.findViewById(R.id.tvID);
                        downloadManager.remove(Long.getLong(textView.getText().toString()));
                        imageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                    }
                }
                break;
            case R.id.btnCancel:
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) recyclerView.getChildAt(i);
                    ImageView imageView = (ImageView)linearLayout.findViewById(R.id.ivSelect);
                    imageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                }
                break;
            default:
                break;
        }
    }
}
