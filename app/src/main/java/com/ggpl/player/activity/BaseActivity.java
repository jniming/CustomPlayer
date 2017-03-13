package com.ggpl.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ggpl.player.R;

import java.io.Serializable;

/**
 * Created by zhangxiaoming on 2017/2/15.
 */

public abstract class BaseActivity extends AppCompatActivity{
    protected static String KEY_OBJ = "obj";
    private TextView title;
    public LinearLayout blckview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);



        setContentView(getContentLayoutID());
        title = (TextView) findViewById(R.id.title);
        blckview= (LinearLayout) findViewById(R.id.black_view);
        initView();
        initCode();

        blckview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setTitle(String str) {

        title.setText(str);
    }






    protected void readyGo(Class<?> clazz, Object obj) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(KEY_OBJ, (Serializable) obj);
        startActivity(intent);


    }

    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);

        startActivity(intent);


    }


    @Override
    public void finish() {
        super.finish();
//        BaseAppManager.getInstance().removeActivity(this);
//        if (toggleOverridePendingTransition()) {
//            switch (getOverridePendingTransitionMode()) {
//                case LEFT:
//                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
//                    break;
//                case RIGHT:
//                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
//                    break;
//                case TOP:
//                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
//                    break;
//                case BOTTOM:
//                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
//                    break;
//                case SCALE:
//                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
//                    break;
//                case FADE:
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                    break;
//            }
//        }
    }

    /**
     * 初始化资源文件
     *
     * @return
     */
    protected abstract int getContentLayoutID();

    /**
     * 初始胡视图
     */
    protected abstract void initView();

    /**
     * 初始化逻辑代码
     */
    protected abstract void initCode();   //初始化逻辑代码
}
