package com.ggpl.player.manager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.ggpl.player.R;
import com.ggpl.player.activity.FileManagerActivity;
import com.ggpl.player.activity.SearchActivity;
import com.ggpl.player.activity.SettingActivity;
import com.ggpl.player.common.AppConfig;
import com.ggpl.player.util.UiScreen;

/**
 * Created by zhangxiaoming on 2017/2/16.
 */

public class PopuManager {

    private Activity mcontext;

    private PopupWindow popu;

    private Dialog sort_Dialog, loadDialog;

    private int sort_type = 0;//0是默认,1是最新,2文件名,3时长,4大小

    private Scanner scanner;
    private int width;
    private int height;
    private int isv;

    public PopuManager(Activity mcontext, int isv) {
        this.mcontext = mcontext;
        this.isv = isv;
        popuMainMenu();
        CreateLoadingDialog();
        setDialog();
        DisplayMetrics metric = new DisplayMetrics();
        mcontext.getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）
        height = metric.heightPixels;   // 屏幕高度（像素）


    }


    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showPop(View view) {

//        popu.showAtLocation(view,Gravity.TOP | Gravity.RIGHT, -UiScreen.dip2px(mcontext, 6), 10);
//popu.showAsDropDown(view,-UiScreen.dip2px(mcontext, 6), 10,Gravity.TOP | Gravity.RIGHT);
//        popu.showAsDropDown(view,-UiScreen.dip2px(mcontext, 100), 20);

        popu.showAsDropDown(view, -UiScreen.dip2px(mcontext, 105), UiScreen.dip2px(mcontext, 12));
    }

    public void ClosePop() {
        if (popu != null && popu.isShowing()) {
            popu.dismiss();
        }
    }

    public void popuMainMenu() {
        View popview = LayoutInflater.from(mcontext).inflate(R.layout.pop_main_menu, null);
        LinearLayout item1 = (LinearLayout) popview.findViewById(R.id.item1);
        LinearLayout item2 = (LinearLayout) popview.findViewById(R.id.item2);
        LinearLayout item3 = (LinearLayout) popview.findViewById(R.id.item3);
        LinearLayout item4 = (LinearLayout) popview.findViewById(R.id.item4);
        LinearLayout item5 = (LinearLayout) popview.findViewById(R.id.item5);
        LinearLayout fg = (LinearLayout) popview.findViewById(R.id.sort_fg);
        if (isv == 0) {
            item3.setVisibility(View.GONE);
            fg.setVisibility(View.GONE);
        } else {
            item3.setVisibility(View.VISIBLE);
            fg.setVisibility(View.VISIBLE);
        }


        setListener(item1, 1);
        setListener(item2, 2);
        setListener(item3, 3);
        setListener(item4, 4);
        setListener(item5, 5);


        popu = new PopupWindow(popview, UiScreen.dip2px(mcontext, 150), UiScreen.dip2px(mcontext, isv == 0 ? 171 :
                214));

        popu.setOutsideTouchable(true);   //设置点击屏幕外消失


    }

    public void setListener(View v, final int index) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (index) {

                    case 1:  //全盘扫描
                        popu.dismiss();
                        loadDialog.show();
                        scanner.scaning(loadDialog);
                        break;
                    case 2:
                        popu.dismiss();
                        mcontext.startActivity(new Intent(mcontext, SearchActivity.class));

                        break;
                    case 3:  //排序
                        popu.dismiss();

                        sort_Dialog.show();

                        break;
                    case 4:  //目录浏览
                        popu.dismiss();

                        mcontext.startActivity(new Intent(mcontext, FileManagerActivity.class));

                        break;
                    case 5:
                        popu.dismiss();
                        mcontext.startActivity(new Intent(mcontext, SettingActivity.class));


                        break;


                }
            }
        });

    }

    public void setRadListner(RadioButton rad, final RadioButton[] radioButtons) {


        rad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    int index = 0;
                    for (int i = 0; i < radioButtons.length; i++) {
                        if (buttonView != radioButtons[i]) {
                            radioButtons[i].setChecked(false);

                        } else {
                            index = i;

                        }

                    }
                    AppConfig.getAppConfig(mcontext).saveListSort(index);
                    scanner.sort(index);


                    sort_Dialog.dismiss();
                }

            }
        });
    }

    public void setDialog() {
        sort_Dialog = new Dialog(mcontext, R.style.customAlertt);
        View view = LayoutInflater.from(mcontext).inflate(R.layout.sort_alert_view, null);

        final RadioButton radio_item1 = (RadioButton) view.findViewById(R.id.radio_item1);
        final RadioButton radio_item2 = (RadioButton) view.findViewById(R.id.radio_item2);
        final RadioButton radio_item3 = (RadioButton) view.findViewById(R.id.radio_item3);
        final RadioButton radio_item4 = (RadioButton) view.findViewById(R.id.radio_item4);

        final RadioButton[] radioButtons = new RadioButton[]{radio_item1, radio_item2, radio_item3, radio_item4};
        int index = AppConfig.getAppConfig(mcontext).GetListSort();
        for (int i = 0; i < radioButtons.length; i++) {
            if (index == i) {
                radioButtons[i].setChecked(true);
            } else {
                radioButtons[i].setChecked(false);
            }


        }


        setRadListner(radio_item1, radioButtons);
        setRadListner(radio_item2, radioButtons);
        setRadListner(radio_item3, radioButtons);
        setRadListner(radio_item4, radioButtons);


        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        sort_Dialog.setContentView(view);

        WindowManager.LayoutParams p = sort_Dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = UiScreen.dip2px(mcontext, 286); // 宽度设置为屏幕的0.65
        p.height = UiScreen.dip2px(mcontext, 245);
        sort_Dialog.getWindow().setAttributes(p);
        sort_Dialog.setCanceledOnTouchOutside(true);

    }


    public void CreateLoadingDialog() {
        loadDialog = new Dialog(mcontext, R.style.customAlertt);
        View view = LayoutInflater.from(mcontext).inflate(R.layout.loading, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        loadDialog.setContentView(view);
        WindowManager.LayoutParams p = loadDialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = UiScreen.dip2px(mcontext, 100); // 宽度设置为屏幕的0.65
        p.height = UiScreen.dip2px(mcontext, 100);
        loadDialog.setCanceledOnTouchOutside(false);

        loadDialog.getWindow().setAttributes(p);


    }

    public Dialog getLoadDialog() {
        return loadDialog;
    }

    public void setLoadDialog(Dialog loadDialog) {
        this.loadDialog = loadDialog;
    }

    public interface Scanner {

        void scaning(Dialog dialog);

        void sort(int index);


    }


}
