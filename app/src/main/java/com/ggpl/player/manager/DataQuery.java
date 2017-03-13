package com.ggpl.player.manager;

import com.ggpl.player.model.bean.FileDirBean;
import com.ggpl.player.model.bean.ModuleBean;
import com.ggpl.player.model.bean.Videobean;
import com.ggpl.player.model.entitiy.FileDirEntity;

import java.util.List;

/**
 * Created by zhangxiaoming on 2017/2/16.
 * sd卡扫描接口
 */

public interface DataQuery {

    /**
     * 扫描sdk文件
     */
    void ScanerSDk();


    /**
     * 获取文件所在的所有目录
     * @return
     */
    List<FileDirEntity> getFileDir();


    /**
     * 获取所有的的音频文件
     * @return
     */
    List<Videobean> getVideoList();


    /**
     * 更新目录隐藏状态
     * @param bean
     */
    void updateFileDirHiden(FileDirBean bean);

    /**
     * 获取所有目录列表
     * @return
     */
    List<FileDirEntity> getFileListDir();


    /**
     * 根据关键字搜索视频
     */
    List<Videobean> searchVideo(String hint);


    /**
     * 添加模块
     * @param ben
     */
    void addModule(List<ModuleBean> ben);


}
