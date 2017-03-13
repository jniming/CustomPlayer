package com.ggpl.player.model.entitiy;

import com.ggpl.player.model.bean.FileDirBean;
import com.ggpl.player.model.bean.ModuleBean;
import com.ggpl.player.model.bean.Videobean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxiaoming on 2017/2/16.
 */

public class FileDirEntity implements Serializable{

    private FileDirBean dirBean;

    private List<Videobean> videoList;

    public ModuleBean moduleBean;   //模块

    private int type;    //0表示显示原item,1显示模块item


    public FileDirBean getDirBean() {
        return dirBean;
    }

    public void setDirBean(FileDirBean dirBean) {
        this.dirBean = dirBean;
    }

    public List<Videobean> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Videobean> videoList) {
        this.videoList = videoList;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ModuleBean getModuleBean() {
        return moduleBean;
    }

    public void setModuleBean(ModuleBean moduleBean) {
        this.moduleBean = moduleBean;
    }
}
