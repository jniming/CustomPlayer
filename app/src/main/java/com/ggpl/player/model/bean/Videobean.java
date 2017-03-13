package com.ggpl.player.model.bean;

import java.io.Serializable;

/**
 * Created by zhangxiaoming on 2017/2/15.
 */

public class Videobean implements Serializable{


    private  String path;   //视频地址

    private  String wh;   //视频宽高

    private  String title;   //标题

    private String  size;  //文件大小

    private  String time ;   //时长

    private String img;   //缩略图

    private String dir;  //文件所属目录

    private String logtime;


    public String getLogtime() {
        return logtime;
    }

    public void setLogtime(String logtime) {
        this.logtime = logtime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getWh() {
        return wh;
    }

    public void setWh(String wh) {
        this.wh = wh;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
