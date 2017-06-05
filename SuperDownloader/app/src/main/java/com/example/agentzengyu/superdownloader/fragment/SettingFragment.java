package com.example.agentzengyu.superdownloader.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.app.SuperDownloaderApp;


public class SettingFragment extends Fragment implements View.OnClickListener{
    private SuperDownloaderApp superDownloaderApp = null;

    public SettingFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);
        superDownloaderApp = (SuperDownloaderApp) getActivity().getApplication();
        initView(view);
        return view;
    }

    /**
     * 初始化布局
     */
    private void initView(View view ) {
        view.findViewById(R.id.btnExit).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnExit:
                superDownloaderApp.destroyAllActivitiesFromList();
                break;
            default:
                break;
        }
    }
}
