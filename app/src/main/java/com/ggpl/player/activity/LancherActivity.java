package com.ggpl.player.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ggpl.player.R;

/**
 * Created by zhangxiaoming on 2017/2/23.
 */

public class LancherActivity extends Activity {


    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startActivity(new Intent(LancherActivity.this,MainActivity.class));
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lancher);
        handler.sendEmptyMessageDelayed(1,1500);

    }
}
