package com.ggpl.player.model.bean;

import java.io.Serializable;

/**
 * Created by zhangxiaoming on 2017/2/16.
 */

public class FileDirBean implements Serializable{

    private String dirname;

    private String ishiden;  //是否隐藏,0是隐藏,1是显示

    private String logtime;


    public String getDirname() {
        return dirname;
    }

    public void setDirname(String dirname) {
        this.dirname = dirname;
    }

    public String getIshiden() {
        return ishiden;
    }

    public void setIshiden(String ishiden) {
        this.ishiden = ishiden;
    }

    public String getLogtime() {
        return logtime;
    }

    public void setLogtime(String logtime) {
        this.logtime = logtime;
    }
}
