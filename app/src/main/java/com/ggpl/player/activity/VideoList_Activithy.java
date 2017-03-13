package com.ggpl.player.activity;

import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ggpl.player.R;
import com.ggpl.player.adpter.VideoListAdpter;
import com.ggpl.player.common.AppConfig;
import com.ggpl.player.manager.DataManager;
import com.ggpl.player.manager.PopuManager;
import com.ggpl.player.model.bean.Videobean;
import com.ggpl.player.model.entitiy.FileDirEntity;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by zhangxiaoming on 2017/2/15.
 */

public class VideoList_Activithy extends BaseActivity implements VideoListAdpter.ItemClick, View.OnClickListener,
        PopuManager.Scanner {

    private RecyclerView recyclerView;

    private VideoListAdpter adpter;

    private FileDirEntity entity;

    private ImageButton menu;

    private PopupWindow popu;
    private View titleview;

    private PopuManager popuManager;

    private TextView mb;

    private List<Videobean> list = new ArrayList<>();

    @Override
    protected int getContentLayoutID() {
        return R.layout.video_activity;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.video_recy);
        menu = (ImageButton) findViewById(R.id.menu);
        mb = (TextView) findViewById(R.id.mb_text);
        menu.setVisibility(View.VISIBLE);
        titleview = findViewById(R.id.titleview2);
        menu.setOnClickListener(this);
    }

    @Override
    protected void initCode() {
        popuManager = new PopuManager(this, 1);
        popuManager.setScanner(this);
        entity = (FileDirEntity) getIntent().getSerializableExtra(KEY_OBJ);
        list.addAll(entity.getVideoList());
        if (list.size() > 0) {
            mb.setVisibility(View.GONE);

        } else {
            mb.setVisibility(View.VISIBLE);
        }
        int sort_index = AppConfig.getAppConfig(this).GetListSort();
        ListSort(sort_index);  //排序


        setTitle(entity.getDirBean().getDirname());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adpter = new VideoListAdpter(list, this);
        recyclerView.setAdapter(adpter);
        adpter.setItemClick(this);


    }


    public void ListSort(final int index) {

        final Collator collator = Collator.getInstance();

        Collections.sort(list, new Comparator<Videobean>() {

            /*
             * int compare(Student o1, Student o2) 返回一个基本类型的整型，
             * 返回负数表示：o1 小于o2，
             * 返回0 表示：o1和o2相等，
             * 返回正数表示：o1大于o2。
             */
            public int compare(Videobean o1, Videobean o2) {
                if (index == 0) {   //按时间排序
                    long time1 = Long.valueOf(o1.getLogtime()) / (1000 * 60 / 60);
                    long time2 = Long.valueOf(o2.getLogtime()) / (1000 * 60 / 60);
                    return (int) (time1 - time2);

                } else if (index == 1) {  //文件名
                    CollationKey key1 = collator.getCollationKey(o1.getTitle());
                    CollationKey key2 = collator.getCollationKey(o2.getTitle());
                    return key1.compareTo(key2);
                } else if (index == 2) {  //时长
                    long time1 = Long.valueOf(o1.getTime()) / (1000);
                    long time2 = Long.valueOf(o2.getTime()) / (1000);

                    return (int) (time1 - time2);


                } else if (index == 3) {  //大小
                    String size1 = o1.getSize().substring(0, o1.getSize().length() - 1);
                    String size2 = o2.getSize().substring(0, o2.getSize().length() - 1);

                    int big1 = (int) (Double.valueOf(size1) * 10);
                    int big2 = (int) (Double.valueOf(size2) * 10);
                    return big1 - big2;
                }

                return 0;
            }
        });


    }


    @Override
    public void itemClickListener(Videobean position) {
        readyGo(Player2_Activity.class, position);
    }

    @Override
    public void onClick(View v) {
        if (v == menu) {
            popuManager.showPop(menu);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void scaning(Dialog dialog) {

        DataManager.getIntence(this).ScanerSDk();
        dialog.dismiss();
        Toast.makeText(this, R.string.scanfinsh, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sort(int index) {
        ListSort(index);
        adpter.notifyDataSetChanged();
    }
}
