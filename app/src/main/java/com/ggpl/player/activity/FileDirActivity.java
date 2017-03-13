package com.ggpl.player.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ggpl.player.R;
import com.ggpl.player.adpter.FileDirAdpter;
import com.ggpl.player.manager.DataManager;
import com.ggpl.player.model.bean.FileDirBean;
import com.ggpl.player.model.entitiy.FileDirEntity;

import java.util.List;

/**
 * Created by zhangxiaoming on 2017/2/16.
 */

public class FileDirActivity extends BaseActivity implements FileDirAdpter.ItemClick {
    private RecyclerView recv;
    private FileDirAdpter adpter;
    private List<FileDirEntity> list;

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_file_dir;
    }

    @Override
    protected void initView() {
        recv = (RecyclerView) findViewById(R.id.recy);
        recv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initCode() {

        setTitle(getResources().getString(R.string.main_menu_item4));
        list = DataManager.getIntence(this).getFileListDir();
        adpter = new FileDirAdpter(list, this);
        adpter.setItemClick(this);
        recv.setAdapter(adpter);
    }


    @Override
    public void itemClickListener(FileDirBean position) {
        DataManager.getIntence(this).updateFileDirHiden(position);

    }
}
