package com.ggpl.player.model.bean;

import java.io.Serializable;

/**
 * Created by zhangxiaoming on 2017/2/17.
 */

public class ModuleBean implements Serializable {

    private String imgurl;   //图片地址,这里是指下载好了的本地地址

    private String h5url;  //模块网址

    private String ishiden;  //0显示,1隐藏

    private String name;  //模块名

    private String desc; //模块描述

    private String logtime;

    public ModuleBean(String imgurl, String h5url, String ishiden) {
        this.imgurl = imgurl;
        this.h5url = h5url;
        this.ishiden = ishiden;
    }

    public ModuleBean() {
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getH5url() {
        return h5url;
    }

    public void setH5url(String h5url) {
        this.h5url = h5url;
    }

    public String getIshiden() {
        return ishiden;
    }

    public void setIshiden(String ishiden) {
        this.ishiden = ishiden;
    }

    public ModuleBean(String imgurl, String h5url, String ishiden, String name, String desc) {
        this.imgurl = imgurl;
        this.h5url = h5url;
        this.ishiden = ishiden;
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLogtime() {
        return logtime;
    }

    public void setLogtime(String logtime) {
        this.logtime = logtime;
    }
}
