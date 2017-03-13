package com.ggpl.player.activity;

import android.app.Dialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.ggpl.player.R;
import com.ggpl.player.common.DebugLog;
import com.ggpl.player.model.bean.Videobean;
import com.ggpl.player.model.entitiy.FileDirEntity;
import com.ggpl.player.util.UiScreen;
import com.ggpl.player.view.BrowserLayout;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;

/**
 * Created by zhangxiaoming on 2017/2/14.
 */

public class H5_Activity extends BaseActivity {
    private BrowserLayout browserLayout;
    private String url;

    private Dialog loadDialog;
private String video_url;
    @Override
    protected int getContentLayoutID() {
        return R.layout.h5activity;
    }

    @Override
    protected void initView() {
        browserLayout = (BrowserLayout) findViewById(R.id.browserlayout);


    }

    @Override
    protected void initCode() {
        setTitle(getString(R.string.return_string));
        CreateLoadingDialog();
        FileDirEntity dirEntity = (FileDirEntity) getIntent().getSerializableExtra(KEY_OBJ);
        url = dirEntity.getModuleBean().getH5url();




        browserLayout.getWebView().loadUrl(url);

        browserLayout.getWebView().registerHandler("submitFromWeb", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                DebugLog.d("data-->"+data);

                        if(!"".equals(data)){
                            video_url=data;
                            Videobean ben=new Videobean();
                            DebugLog.d("video_url-->"+video_url);
                            ben.setTitle(video_url);
                            ben.setPath(video_url);


                            readyGo(Player2_Activity.class,ben);

                }





            }

        });


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            browserLayout.goBack();

        }

        return true;

    }


    public void CreateLoadingDialog() {
        loadDialog = new Dialog(this, R.style.customAlertt);
        View view = LayoutInflater.from(this).inflate(R.layout.h5_alert, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Button yes = (Button) view.findViewById(R.id.yes_btn);
        Button no = (Button) view.findViewById(R.id.no_btn);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDialog.dismiss();



            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Videobean ben=new Videobean();
                DebugLog.d("video_url-->"+video_url);
                ben.setTitle(video_url);
                ben.setPath(video_url);


                readyGo(Player2_Activity.class,ben);
                loadDialog.dismiss();
            }
        });

        loadDialog.setContentView(view);
        WindowManager.LayoutParams p = loadDialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = UiScreen.dip2px(this, 200); // 宽度设置为屏幕的0.65
        p.height = UiScreen.dip2px(this, 140);
        loadDialog.setCanceledOnTouchOutside(false);
        loadDialog.getWindow().setAttributes(p);


    }

}
