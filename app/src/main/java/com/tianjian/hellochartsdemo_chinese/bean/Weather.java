package com.tianjian.hellochartsdemo_chinese.bean;

/**
 * Created by Ye on 2016/12/28.
 */

public class Weather {
    private String desc;
    private int status;
    private Data data;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
