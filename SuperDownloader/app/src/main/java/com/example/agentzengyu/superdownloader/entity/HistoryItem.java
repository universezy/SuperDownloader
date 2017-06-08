package com.example.agentzengyu.superdownloader.entity;

/**
 * Created by ZengYu on 2017/6/6.
 */

/**
 * 历史任务类
 */
public class HistoryItem {
    private String name;
    private long size = 0;
    private long ID = 0;
    private String path = "";

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

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setPath(String path) {
        this.path = path;
    }
}