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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.adapter.CurrentItemAdpter;
import com.example.agentzengyu.superdownloader.app.Config;
import com.example.agentzengyu.superdownloader.app.SuperDownloaderApp;
import com.example.agentzengyu.superdownloader.entity.CurrentItem;

/**
 * 当前任务
 */
public class CurrentFragment extends Fragment implements View.OnClickListener {
    private SuperDownloaderApp app = null;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CurrentItemAdpter itemAdpter;
    private CurrentReceiver receiver;

    public CurrentFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current, null);
        app = (SuperDownloaderApp) getActivity().getApplication();
        initView(view);
        app.addFragment(this);
        receiver = new CurrentReceiver();
        IntentFilter filter = new IntentFilter(Config.SERVICE);
        getActivity().registerReceiver(receiver, filter);
        return view;
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
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
        itemAdpter = new CurrentItemAdpter(app.getService().getCurrentItems());
        itemAdpter.setItemClickListener(new CurrentItemAdpter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, CurrentItem currentItem) {
                ImageView imageView = (ImageView) view.findViewById(R.id.ivSelect);
                if (((ColorDrawable) imageView.getBackground()).getColor() == Color.parseColor("#d3d3d3")) {
                    ((ColorDrawable) imageView.getBackground()).setColor(Color.parseColor("#ff0000"));
                } else {
                    ((ColorDrawable) imageView.getBackground()).setColor(Color.parseColor("#d3d3d3"));
                }
            }
        });
        recyclerView.setAdapter(itemAdpter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                        app.getService().removeCurrentItem(id);
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
                itemAdpter.notifyDataSetChanged();
            }
        }
    }
}
