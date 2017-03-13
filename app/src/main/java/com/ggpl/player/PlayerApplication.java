package com.ggpl.player;

import android.app.Application;

import com.ggpl.player.util.ThumbLoader;

/**
 * Created by zhangxiaoming on 2017/2/16.
 */

public class PlayerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ThumbLoader.getIntentce(this);
    }
}
