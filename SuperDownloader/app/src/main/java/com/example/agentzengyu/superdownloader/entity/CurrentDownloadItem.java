package com.example.agentzengyu.superdownloader.entity;

/**
 * Created by Agent ZengYu on 2017/6/6.
 */

public class CurrentDownloadItem {
    private String name;
    private String msg1;
    private String msg2;
    private String msg3;
    private String msg4;
    private int progress = 0;

    public String getMsg1() {
        return msg1;
    }

    public String getMsg2() {
        return msg2;
    }

    public String getMsg3() {
        return msg3;
    }

    public String getMsg4() {
        return msg4;
    }

    public String getName() {
        return name;
    }

    public int getProgress() {
        return progress;
    }

    public void setMsg1(String msg1) {
        this.msg1 = msg1;
    }

    public void setMsg2(String msg2) {
        this.msg2 = msg2;
    }

    public void setMsg3(String msg3) {
        this.msg3 = msg3;
    }

    public void setMsg4(String msg4) {
        this.msg4 = msg4;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
