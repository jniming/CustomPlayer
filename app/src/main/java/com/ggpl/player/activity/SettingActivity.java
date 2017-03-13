package com.ggpl.player.activity;

import android.view.View;
import android.widget.FrameLayout;

import com.ggpl.player.R;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout item1, item2, item3;

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        item1 = (FrameLayout) findViewById(R.id.setting_item1);
        item2 = (FrameLayout) findViewById(R.id.setting_item2);
        item3 = (FrameLayout) findViewById(R.id.setting_item3);
        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item3.setOnClickListener(this);
    }

    @Override
    protected void initCode() {
 setTitle(getResources().getString(R.string.main_menu_item5));
    }

    @Override
    public void onClick(View v) {
        if (v == item1) {
            readyGo(FileSettingActivity.class);
        } else if (v == item2) {
            readyGo(PlayerSettingActivity.class);
        } else if (v == item3) {
            readyGo(GYActivity.class);
        }
    }
}
