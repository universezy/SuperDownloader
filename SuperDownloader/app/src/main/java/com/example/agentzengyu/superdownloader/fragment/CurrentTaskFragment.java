package com.example.agentzengyu.superdownloader.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.adapter.CurrentItemAdpter;
import com.example.agentzengyu.superdownloader.app.Config;
import com.example.agentzengyu.superdownloader.app.SuperDownloaderApp;
import com.example.agentzengyu.superdownloader.entity.CurrentDownloadItem;

import java.util.Random;

/**
 * 当前任务
 */
public class CurrentTaskFragment extends Fragment implements View.OnClickListener {
    private SuperDownloaderApp superDownloaderApp = null;

    private Button mbtnTest;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CurrentItemAdpter currentItemAdpter;
    private CurrentReceiver currentReceiver;

    public CurrentTaskFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_task, null);
        superDownloaderApp = (SuperDownloaderApp) getActivity().getApplication();
        initView(view);
        superDownloaderApp.addFragmentToList(this);
        currentReceiver = new CurrentReceiver();
        IntentFilter intentFilter = new IntentFilter(Config.SERVICE);
        getActivity().registerReceiver(currentReceiver, intentFilter);
        return view;
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(currentReceiver);
        super.onDestroy();
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
                ImageView imageView = (ImageView) view.findViewById(R.id.ivSelect);
                if (((ColorDrawable) imageView.getBackground()).getColor() == Color.parseColor("#d3d3d3")) {
                    ((ColorDrawable) imageView.getBackground()).setColor(Color.parseColor("#ff0000"));
                } else {
                    ((ColorDrawable) imageView.getBackground()).setColor(Color.parseColor("#d3d3d3"));
                }
            }
        });
        recyclerView.setAdapter(currentItemAdpter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTest:
                CurrentDownloadItem currentDownloadItem = new CurrentDownloadItem();
                currentDownloadItem.setName(System.currentTimeMillis() + "");
                currentDownloadItem.setSize(new Random().nextInt(100000));
                currentDownloadItem.setID(System.currentTimeMillis());
                currentDownloadItem.setProgress(new Random().nextInt(100));
                superDownloaderApp.getService().addItemToCurrentDownloadItems(currentDownloadItem);
                currentItemAdpter.notifyDataSetChanged();
                layoutManager.scrollToPosition(0);
                break;
            case R.id.btnAll:
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) recyclerView.getChildAt(i);
                    ImageView imageView = (ImageView) linearLayout.findViewById(R.id.ivSelect);
                    imageView.setBackgroundColor(Color.parseColor("#ff0000"));
                }
                break;
            case R.id.btnDelete:
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) recyclerView.getChildAt(i);
                    ImageView imageView = (ImageView) linearLayout.findViewById(R.id.ivSelect);
                    if (((ColorDrawable) imageView.getBackground()).getColor() == Color.parseColor("#ff0000")) {
                        TextView textView = (TextView) linearLayout.findViewById(R.id.tvID);
                        long id = Long.parseLong(textView.getText().toString());
                        superDownloaderApp.getService().removeItemFromCurrentDownloadItems(id);
                        imageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                    }
                }
                break;
            case R.id.btnCancel:
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) recyclerView.getChildAt(i);
                    ImageView imageView = (ImageView) linearLayout.findViewById(R.id.ivSelect);
                    imageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                }
                break;
            default:
                break;
        }
    }

    public class CurrentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String state = intent.getStringExtra(Config.SUPERDOWNLOAD);
            Log.e("state",state);
            if (state.equals(Config.CURRENT)) {
                currentItemAdpter.notifyDataSetChanged();
            }
        }
    }
}
