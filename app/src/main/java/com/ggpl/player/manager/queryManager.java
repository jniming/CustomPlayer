package com.ggpl.player.manager;


import com.ggpl.player.model.bean.FileDirBean;
import com.ggpl.player.model.bean.ModuleBean;
import com.ggpl.player.model.bean.Videobean;
import com.ggpl.player.model.entitiy.FileDirEntity;

import java.util.List;

/**
 * Created by zhangxiaoming on 2017/2/16.
 * 查询管理接口
 */

public interface queryManager {

    /**
     * 添加多个video
     *
     * @param list
     */
    void addAllvideo(List<Videobean> list);   //添加多个video

    /**
     * 添加一个个video
     *
     * @param bean
     */
    void addOnevideo(Videobean bean);   //添加一个个video

    /**
     * 查询所有的video
     *
     * @return
     */
    List<Videobean> queryAllVideo();  //查询所有的video

    /**
     * 条件查询 视频
     *
     * @param sql
     * @return
     */
    List<Videobean> queryAllVideo(String sql);   //条件查询

    /**
     * /添加一个目录
     *
     * @param dirBean
     */
    void addFileDir(FileDirBean dirBean);  //添加一个目录

    /**
     * 查询所有的目录
     *
     * @return
     */
    List<FileDirEntity> queryAllFileDir();    //查询所有的目录

    /**
     * 条件查询某个目录
     *
     * @return
     */
    List<FileDirEntity> queryFileDir(String sql);

    /**
     * 更新目录状态
     *
     * @param bean
     */
    void updateFiledirHiden(FileDirBean bean);

    /**
     * 清空视频列表
     */
    void clearVideoTable();


    /**
     * 添加一个模块
     * @param ben
     */
    void addModule(List<ModuleBean> ben);

    /**
     * 获取所有模块,只获取可显示的模块
     * @return
     */
    List<ModuleBean> getModule();
}
