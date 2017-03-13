package com.ggpl.player.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.ggpl.player.R;
import com.ggpl.player.common.AppConfig;
import com.ggpl.player.common.DebugLog;
import com.ggpl.player.model.bean.FileDirBean;
import com.ggpl.player.model.bean.ModuleBean;
import com.ggpl.player.model.bean.Videobean;
import com.ggpl.player.model.entitiy.FileDirEntity;
import com.ggpl.player.sql.SqlHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhangxiaoming on 2017/2/15.
 * <p>
 * 视频管理类
 */

public class DataManager implements DataQuery {


    private Context mcontext;

    private DataBaseManager dataBaseManager;

    private static DataManager dataManager;


    /**
     * 单利
     *
     * @param context
     * @return
     */
    public static DataManager getIntence(Context context) {

        if (dataManager == null) {
            dataManager = new DataManager(context);
        }
        return dataManager;

    }


    public DataManager(Context mcontext) {

        this.mcontext = mcontext;
        dataBaseManager = new DataBaseManager(mcontext);
        //扫描前先删除本地表中数据,防止数据错乱
        dataBaseManager.clearVideoTable();

    }


    @Override
    public void ScanerSDk() {


        List<Videobean> videoList = new ArrayList<>();
        ContentResolver contentResolver = mcontext.getContentResolver();

        String[] projection = new String[]{MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE, MediaStore
                .Video.Media.SIZE, MediaStore.Video.Media.WIDTH, MediaStore.Video.Media.HEIGHT, MediaStore.Video
                .Media.DURATION,MediaStore.Video.Thumbnails.DATA};
        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        int fileNum = cursor.getCount();
        Boolean isfite = AppConfig.getAppConfig(mcontext).GetFileConfig().isFitleSize(); //是否过滤大于1m文件


        FileDirBean dirBean2 = new FileDirBean();
        dirBean2.setIshiden("0");
        dirBean2.setDirname(mcontext.getResources().getString(R.string.all_filedir));
        dataBaseManager.addFileDir(dirBean2);

        for (int counter = 0; counter < fileNum; counter++) {

            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
            String width = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.WIDTH));
            String height = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT));

            double size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE)) / 1024 / 1024;
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));

            String wd = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));

            DebugLog.d("wd--"+wd);



            BigDecimal bg = new BigDecimal(size);
            size = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

            String[] dirar = path.split("/");
            String dir = dirar[dirar.length - 2];
            title = dirar[dirar.length - 1];


            DebugLog.d("扫描的时长-->" + duration);


            if (isfite) {
                if (size > 2) {
                    Videobean videobean = new Videobean();
                    videobean.setPath(path);
                    videobean.setSize(size + "M");
                    videobean.setTitle(title);
                    videobean.setTime(duration + "");

                        videobean.setWh((width == null ? 0 : width) + "x" + (height == null ? 0 : height));

                    videobean.setDir(dir + "");
                    videoList.add(videobean);
                    FileDirBean dirBean = new FileDirBean();
                    dirBean.setDirname(dir);
                    dirBean.setIshiden("0");


                    dataBaseManager.addFileDir(dirBean);
                    dataBaseManager.addOnevideo(videobean);
                }
            } else {
                Videobean videobean = new Videobean();
                videobean.setPath(path);
                videobean.setSize(size + "M");
                videobean.setTitle(title);
                videobean.setTime(duration + "");

                    videobean.setWh((width == null ? 0 : width) + "x" + (height == null ? 0 : height));

                videobean.setDir(dir + "");
                videoList.add(videobean);

                FileDirBean dirBean = new FileDirBean();
                dirBean.setDirname(dir);
                dirBean.setIshiden("0");


                dataBaseManager.addFileDir(dirBean);
                dataBaseManager.addOnevideo(videobean);
            }


            cursor.moveToNext();
        }
        cursor.close();
    }







    @Override
    public List<FileDirEntity> getFileDir() {
//这里需要添加模块,
        List<FileDirEntity> list = dataBaseManager.queryAllFileDir();

        //获取模块数据
        List<ModuleBean> moduleBeanList = dataBaseManager.getModule();
        if (moduleBeanList != null && moduleBeanList.size() != 0) {
            switch (AppConfig.getAppConfig(mcontext).GetSortType()) {
                case -1:

                    break;
                case 0:
                    List<FileDirEntity> mu = new ArrayList<>();
                    for (ModuleBean mb : moduleBeanList) {
                        FileDirEntity dirEntity = new FileDirEntity();
                        dirEntity.setType(1);
                        dirEntity.setModuleBean(mb);
                        mu.add(dirEntity);
                    }

                    list.addAll(0, mu);
                    break;
                case 1:
                    int size = list.size();
                    int index = 0;
                    if (size >= 3) {
                        index = new Random().nextInt(size);

                        List<FileDirEntity> mu1 = new ArrayList<>();
                        for (ModuleBean mb : moduleBeanList) {
                            FileDirEntity dirEntity = new FileDirEntity();
                            dirEntity.setType(1);
                            dirEntity.setModuleBean(mb);
                            mu1.add(dirEntity);
                        }


                        list.addAll(index, mu1);
                    } else {
                        List<FileDirEntity> mu2 = new ArrayList<>();
                        for (ModuleBean mb : moduleBeanList) {
                            FileDirEntity dirEntity = new FileDirEntity();
                            dirEntity.setType(1);
                            dirEntity.setModuleBean(mb);
                            mu2.add(dirEntity);
                        }
                        list.addAll(0, mu2);

                    }

                    break;
                case 2:
                    List<FileDirEntity> mu2 = new ArrayList<>();
                    for (ModuleBean mb : moduleBeanList) {
                        FileDirEntity dirEntity = new FileDirEntity();
                        dirEntity.setType(1);
                        dirEntity.setModuleBean(mb);
                        mu2.add(dirEntity);
                    }
                    list.addAll(mu2);


                    break;

            }
        }

        return list;
    }

    @Override
    public List<Videobean> getVideoList() {
        return dataBaseManager.queryAllVideo();
    }

    @Override
    public void updateFileDirHiden(FileDirBean bean) {
        dataBaseManager.updateFiledirHiden(bean);


    }

    @Override
    public List<FileDirEntity> getFileListDir() {
        List<FileDirEntity> list = dataBaseManager.queryAllFileDir();
        return list;
    }


    @Override
    public List<Videobean> searchVideo(String hint) {
        String sql = "select * from " + SqlHelper.TABLE_NAME[0] + " where title like '%" + hint + "%'";
        return dataBaseManager.queryAllVideo(sql);
    }

    @Override
    public void addModule(List<ModuleBean> ben) {
        dataBaseManager.addModule(ben);
    }


    public static String EcodeTime(long time) {


        long min = time / (1000 * 60);

        long ss = time % (1000 * 60) / 1000;

        String timeEcd = "";
        if (min < 10) {
            timeEcd = "0" + min;
        } else {
            timeEcd = "" + min;
        }

        if (ss < 10) {
            timeEcd += ":0" + ss;

        } else {
            timeEcd += ":" + ss;

        }

        return timeEcd;


    }

}




