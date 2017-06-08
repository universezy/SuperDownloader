package com.example.agentzengyu.superdownloader.entity;

/**
 * Created by ZengYu on 2017/6/6.
 */

/**
 * 当前任务类
 */
public class CurrentDownloadItem {
    private String name;
    private int progress = 0;
    private long ID = 0;
    private boolean downloading = false;
    private long size = 0;

    public String getName() {
        return name;
    }

    public int getProgress() {
        return progress;
    }

    public boolean getDownloading() {
        return downloading;
    }

    public long getSize() {
        return size;
    }

    public long getID() {
        return ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setID(long ID) {
        this.ID = ID;
    }
}
