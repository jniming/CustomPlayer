package com.ggpl.player.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhangxiaoming on 2017/2/15.
 * app 设置
 */

public class AppConfig {
    private static String KEY_File = "config_file_key";

    private static String KEY__FILE_VALUE = "config_file_value";


    private static String KEY_PLAYER_File = "config_player_key";

    private static String KEY__PLAYER_VALUE = "config_player_value";

    private static String KEY_SORT_TYPE = "sort_type_key";

    private static String KEY_SORT_TYPE_VALUE = "sort_type_key_value";


    private static String KEY_LIST_SORT = "sort_list_sort";

    private static String KEY_LIST_SORT_VALUE = "sort_list_sort_value";




    private Context mcontext;

    public static AppConfig appConfig;

    public static AppConfig getAppConfig(Context mcontext) {
        if (appConfig == null) {

            appConfig = new AppConfig(mcontext);

        }
        return appConfig;
    }


    public AppConfig(Context mcontext) {
        this.mcontext = mcontext;

    }


    public boolean saveListSort(int sort) {

        SharedPreferences preferences = mcontext.getSharedPreferences(KEY_LIST_SORT, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_LIST_SORT_VALUE,sort);


        return editor.commit();
    }

    public int GetListSort() {

        SharedPreferences preferences = mcontext.getSharedPreferences(KEY_LIST_SORT, Context.MODE_PRIVATE);
        int sort=preferences.getInt(KEY_LIST_SORT_VALUE,0);

        return sort;
    }



    public boolean saveSortType(int sort) {

        SharedPreferences preferences = mcontext.getSharedPreferences(KEY_SORT_TYPE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_SORT_TYPE_VALUE,sort);


        return editor.commit();
    }

    public int GetSortType() {

        SharedPreferences preferences = mcontext.getSharedPreferences(KEY_SORT_TYPE, Context.MODE_PRIVATE);
        int sort=preferences.getInt(KEY_SORT_TYPE_VALUE,0);

        return sort;
    }




    public boolean saveFileConfig(FileConfig fileConfig) {

        SharedPreferences preferences = mcontext.getSharedPreferences(KEY_File, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("fitleSize", fileConfig.isFitleSize());
        editor.putBoolean("ScanVideo", fileConfig.isScanVideo());
        editor.putBoolean("visliyFile", fileConfig.isVisliyFile());

        return editor.commit();
    }

    public FileConfig GetFileConfig() {

        SharedPreferences preferences = mcontext.getSharedPreferences(KEY_File, Context.MODE_PRIVATE);
        FileConfig config = new FileConfig();
        config.setFitleSize(preferences.getBoolean("fitleSize", true));
        config.setScanVideo(preferences.getBoolean("ScanVideo", true));
        config.setVisliyFile(preferences.getBoolean("visliyFile", true));
        return config;
    }


    public boolean savePlayConfig(PlayerConfig playerConfig) {

        SharedPreferences preferences = mcontext.getSharedPreferences(KEY_PLAYER_File, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("saveTink", playerConfig.isSaveTink());
        editor.putBoolean("screen", playerConfig.isScreen());
        editor.putBoolean("ffmecode", playerConfig.isFfmecode());
        return editor.commit();
    }



    public PlayerConfig GetPlayConfig() {

        SharedPreferences preferences = mcontext.getSharedPreferences(KEY_PLAYER_File, Context.MODE_PRIVATE);
        PlayerConfig config = new PlayerConfig();
        config.setSaveTink(preferences.getBoolean("saveTink", true));
        config.setScreen(preferences.getBoolean("screen", true));
        config.setFfmecode(preferences.getBoolean("ffmecode", true));

        return config;
    }


    public class PlayerConfig {


        private boolean saveTink; //保存播放进度

        private boolean screen;   //屏幕自动旋转

        private boolean ffmecode;   //是否使用软解ß


        public boolean isSaveTink() {
            return saveTink;
        }

        public void setSaveTink(boolean saveTink) {
            this.saveTink = saveTink;
        }

        public boolean isScreen() {
            return screen;
        }

        public void setScreen(boolean screen) {
            this.screen = screen;
        }

        public boolean isFfmecode() {
            return ffmecode;
        }

        public void setFfmecode(boolean ffmecode) {
            this.ffmecode = ffmecode;
        }
    }

    public class FileConfig {
        private boolean fitleSize;  //是否过滤大于1m文件

        private boolean ScanVideo;   //是否过滤音频文件

        private boolean visliyFile;   //是否显示隐藏文件

        public boolean isFitleSize() {
            return fitleSize;
        }

        public void setFitleSize(boolean fitleSize) {
            this.fitleSize = fitleSize;
        }

        public boolean isScanVideo() {
            return ScanVideo;
        }

        public void setScanVideo(boolean scanVideo) {
            ScanVideo = scanVideo;
        }

        public boolean isVisliyFile() {
            return visliyFile;
        }

        public void setVisliyFile(boolean visliyFile) {
            this.visliyFile = visliyFile;
        }
    }
}

