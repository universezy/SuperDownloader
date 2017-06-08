package com.example.agentzengyu.superdownloader.entity;

/**
 * Created by ZengYu on 2017/6/6.
 */

/**
 * 当前任务类
 */
public class CurrentItem {
    private String name;
    private long size = 0;
    private long ID = 0;
    private String path = "";
    private int progress = 0;
    private boolean downloading = false;

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public long getID() {
        return ID;
    }

    public String getPath() {
        return path;
    }

    public int getProgress() {
        return progress;
    }

    public boolean getDownloading() {
        return downloading;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
