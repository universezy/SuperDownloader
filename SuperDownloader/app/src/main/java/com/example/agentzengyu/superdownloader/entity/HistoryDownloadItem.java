package com.example.agentzengyu.superdownloader.entity;

/**
 * Created by ZengYu on 2017/6/6.
 */

/**
 * 历史任务类
 */
public class HistoryDownloadItem {
    private String name;
    private long size = 0;

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(long size) {
        this.size = size;
    }
}