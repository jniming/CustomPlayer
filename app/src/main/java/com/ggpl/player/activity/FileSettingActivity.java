package com.ggpl.player.activity;

import android.widget.CompoundButton;
import android.widget.Switch;

import com.ggpl.player.R;
import com.ggpl.player.common.AppConfig;

public class FileSettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private Switch aSwitch1, aSwitch2, aSwitch3;

    private AppConfig.FileConfig config;

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_file_setting;
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.setting_item_1));
        aSwitch1 = (Switch) findViewById(R.id.switch1);
        aSwitch2 = (Switch) findViewById(R.id.switch2);
        aSwitch3 = (Switch) findViewById(R.id.switch3);

        aSwitch1.setOnCheckedChangeListener(this);
        aSwitch2.setOnCheckedChangeListener(this);
        aSwitch3.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initCode() {

        config = AppConfig.getAppConfig(this).GetFileConfig();
        aSwitch1.setChecked(config.isVisliyFile());
        aSwitch2.setChecked(config.isFitleSize());
        aSwitch3.setChecked(config.isScanVideo());


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == aSwitch1) {
               config.setVisliyFile(isChecked);
        }else if(aSwitch2==buttonView){
            config.setFitleSize(isChecked);
        }else if(aSwitch3==buttonView){

            config.setScanVideo(isChecked);
        }

        AppConfig.getAppConfig(this).saveFileConfig(config);
    }
}
