package com.ggpl.player.activity;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ggpl.player.R;
import com.ggpl.player.util.PhoneInfo;
import com.ggpl.player.util.UiScreen;

public class GYActivity extends BaseActivity {

    private TextView verson;

    private TextView name, update;
    private Dialog loadDialog;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (loadDialog.isShowing()) {
                Toast.makeText(GYActivity.this, R.string.update_msg,Toast.LENGTH_SHORT).show();
                loadDialog.dismiss();
            }
        }
    };

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_gy;
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.setting_item_3));
        verson = (TextView) findViewById(R.id.version);
        name = (TextView) findViewById(R.id.textView2);
        update = (TextView) findViewById(R.id.update);
    }

    @Override
    protected void initCode() {
        CreateLoadingDialog();
        String ver=getResources().getString(R.string.verson_string);
        verson.setText(ver + "V" + PhoneInfo.getAppVersionName(this));
        name.setText(PhoneInfo.getApplicationName(this));
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDialog.show();
                handler.sendEmptyMessageDelayed(0, 2000);
            }
        });
    }

    public void CreateLoadingDialog() {
        loadDialog = new Dialog(this, R.style.customAlertt);
        View view = LayoutInflater.from(this).inflate(R.layout.loading, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        loadDialog.setContentView(view);
        WindowManager.LayoutParams p = loadDialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = UiScreen.dip2px(this, 100); // 宽度设置为屏幕的0.65
        p.height = UiScreen.dip2px(this, 100);
        loadDialog.setCanceledOnTouchOutside(false);
        loadDialog.getWindow().setAttributes(p);


    }
}
