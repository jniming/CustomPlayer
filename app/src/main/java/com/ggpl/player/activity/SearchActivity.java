package com.ggpl.player.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import com.ggpl.player.R;
import com.ggpl.player.adpter.VideoListAdpter;
import com.ggpl.player.common.DebugLog;
import com.ggpl.player.manager.DataManager;
import com.ggpl.player.model.bean.Videobean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaoming on 2017/2/15.
 */

public class SearchActivity extends BaseActivity implements VideoListAdpter.ItemClick {

    private SearchView searchView;
    private RecyclerView recv;
    private List<Videobean> list = new ArrayList<>();
    private VideoListAdpter adpter;


    @Override
    protected int getContentLayoutID() {
        return R.layout.search_activity;
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.main_menu_item2));
        recv = (RecyclerView) findViewById(R.id.search_recy);
        recv.setLayoutManager(new LinearLayoutManager(this));
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.onActionViewExpanded();
        Class<?> c = searchView.getClass();
        try {
            Field f = c.getDeclaredField("mSearchPlate");//通过反射，获得类对象的一个属性对象
            f.setAccessible(true);//设置此私有属性是可访问的
            View v = (View) f.get(searchView);//获得属性的值
            v.setBackgroundResource(R.color.write);//设置此view的背景
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initCode() {

        adpter = new VideoListAdpter(list, this);
        adpter.setItemClick(this);
        recv.setAdapter(adpter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                              @Override
                                              public boolean onQueryTextSubmit(String query) {
                                                  if (!query.isEmpty()) {
                                                      if (list != null) {
                                                          list.clear();

                                                      }
                                                      list.addAll(DataManager.getIntence(SearchActivity.this)
                                                              .searchVideo
                                                              (query));
                                                  }
                                                  return false;
                                              }

                                              @Override
                                              public boolean onQueryTextChange(String newText) {
                                                  DebugLog.d("allen->newText-" + newText);
                                                  if (!newText.isEmpty())

                                                  {
                                                      if (list != null) {
                                                          list.clear();

                                                      }
                                                      list.addAll(DataManager.getIntence(SearchActivity.this)
                                                              .searchVideo
                                                              (newText));
                                                      adpter.notifyDataSetChanged();

                                                  }

                                                  return false;
                                              }
                                          }

        );
    }

    @Override
    public void itemClickListener(Videobean position) {
        readyGo(Player2_Activity.class, position);
    }
}
