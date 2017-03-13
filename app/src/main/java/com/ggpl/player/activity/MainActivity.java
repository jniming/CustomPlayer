package com.ggpl.player.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ggpl.player.R;
import com.ggpl.player.adpter.MainDirListAdpter;
import com.ggpl.player.common.AppConfig;
import com.ggpl.player.common.ConstValue;
import com.ggpl.player.common.DebugLog;
import com.ggpl.player.manager.DataManager;
import com.ggpl.player.manager.PopuManager;
import com.ggpl.player.model.bean.BaseData;
import com.ggpl.player.model.bean.ModuleBean;
import com.ggpl.player.model.entitiy.FileDirEntity;
import com.ggpl.player.util.AESUtil;
import com.ggpl.player.util.HttpConn;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ggpl.player.R.id.mb_text;

public class MainActivity extends BaseActivity implements MainDirListAdpter.ItemClick, View.OnClickListener,
        PopuManager.Scanner, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recv;

    private MainDirListAdpter adpter;

    private List<FileDirEntity> list = new ArrayList<>();

    private ImageButton menu;
    private View titleview;

    private PopuManager popuManager;

    private SwipeRefreshLayout swip;

    private Dialog loading;

    private TextView mb;


    private int REQUEST_PERMISSION_CAMERA_CODE = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (list != null) {
                    list.clear();

                }

                List<FileDirEntity> list1 = DataManager.getIntence(MainActivity.this).getFileDir();

                DebugLog.d("得到的个数-->" + list1.size());
                list.addAll(list1);


                adpter.notifyDataSetChanged();
                if (swip.isRefreshing()) {
                    swip.setRefreshing(false);
                }
                if (loading != null && loading.isShowing()) {
                    loading.dismiss();
                }
            } else if (msg.what == 1) {
                Dialog dialog = (Dialog) msg.obj;
                dialog.dismiss();
                Toast.makeText(MainActivity.this, R.string.scanfinsh, Toast.LENGTH_SHORT).show();
            }

//            if (list.size() > 0) {
//                mb.setVisibility(View.VISIBLE);
//
//            } else {
//                mb.setVisibility(View.GONE);
//            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        popuManager.ClosePop();
        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        recv = (RecyclerView) findViewById(R.id.main_recv);
        menu = (ImageButton) findViewById(R.id.menu);
        swip = (SwipeRefreshLayout) findViewById(R.id.swip);
        mb = (TextView) findViewById(mb_text);
        mb.setVisibility(View.GONE);


        menu.setOnClickListener(this);
        titleview = findViewById(R.id.titleview);
        swip.setOnRefreshListener(this);
        recv.setLayoutManager(new LinearLayoutManager(this));


        adpter = new MainDirListAdpter(list, this);
        adpter.setItemClick(this);
        recv.setAdapter(adpter);

    }

    @Override
    protected void initCode() {
        mb.setVisibility(View.GONE);

        popuManager = new PopuManager(this, 0);
        popuManager.setScanner(this);
        loading = popuManager.getLoadDialog();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //当前系统大于等于6.0
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager
                    .PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED) {
                //具有拍照权限，直接调用相机
                //具体调用代码

                initData();

            } else {
                //不具有拍照权限，需要进行权限申请
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_CAMERA_CODE);
            }
        } else {
            //当前系统小于6.0，直接调用拍照
            initData();
        }


//        initData();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA_CODE) {
            if (grantResults.length >= 1) {
                int cameraResult = grantResults[0];//相机权限
                boolean cameraGranted = cameraResult == PackageManager.PERMISSION_GRANTED;//拍照权限
                if (cameraGranted) {
                    //具有拍照权限，调用相机
                    initData();

                } else {
                    mb.setText(R.string.no_promtion);
                    mb.setVisibility(View.VISIBLE);
                    //不具有相关权限，给予用户提醒，比如Toast或者对话框，让用户去系统设置-应用管理里把相关权限开启
                }
            }
        }
    }


    /**
     * 首次刷新
     */
    public void refsh() {
        swip.post(new Runnable() {
            @Override
            public void run() {
                swip.setRefreshing(true);
            }
        });
        this.onRefresh();
    }

    @Override
    public void itemClickListener(FileDirEntity position) {
        if (position.getType() == 0) {
            readyGo(VideoList_Activithy.class, position);
        } else {

            readyGo(H5_Activity.class, position);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == menu) {
            popuManager.showPop(menu);

        }
    }

    @Override
    public void scaning(Dialog dialog) {

//        DataManager.getIntence(this).ScanerSDk();
//        if (list != null) {
//            list.clear();
//        }
//        list.addAll(DataManager.getIntence(this).getFileDir());
//        adpter.notifyDataSetChanged();
//        Message msg = handler.obtainMessage();
//        msg.obj = dialog;
//        msg.what = 1;
//        handler.sendMessageDelayed(msg, 1000);

        initData();

    }


    public void initData() {

        loading.show();
        new ReqThread().start();


    }


    @Override
    public void sort(int index) {
        new ReqThread().start();
    }


    @Override
    public void onRefresh() {

        new ReqThread().start();
    }

    class ReqThread extends Thread {

        @Override
        public void run() {
            super.run();
            DataManager.getIntence(MainActivity.this).ScanerSDk();  //扫描sdk
            BaseData baseData = new BaseData(MainActivity.this);
            DebugLog.d("返回的数据,未解密->" + baseData.getDataJson().toString());
            String ecode_str = AESUtil.encode(baseData.getDataJson().toString());
            String res = HttpConn.post(ConstValue.req_url, ecode_str);
            DebugLog.d("返回的数据,未解密->" + res);
            if (!res.equals("")) {
                String result = AESUtil.decode(res);

                if (!result.equals("{}")) {
                    DebugLog.d("返回的数据,解密->" + result);
                    try {


                        JSONObject jsonObject = new JSONObject(result);
                        String type = jsonObject.getString("type");

                        JSONArray array = jsonObject.getJSONArray("module");
                        List<ModuleBean> moduleBeanList = new ArrayList<>();

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject js = array.getJSONObject(i);
                            ModuleBean bean = new ModuleBean();
                            String imgurl = js.getString("imgurl");

                            File imgfile = HttpConn.DownImage(MainActivity.this, imgurl);
                            if (imgfile != null) {
                                imgurl = imgfile.getPath();
                            }

                            bean.setH5url(js.getString("h5url"));
                            bean.setImgurl(imgurl);
                            bean.setIshiden(js.getString("ishiden"));
                            bean.setName(js.getString("name"));
                            bean.setDesc(js.getString("desc"));
                            //保存模块到数据库
                            moduleBeanList.add(bean);

                        }

                        DataManager.getIntence(MainActivity.this).addModule(moduleBeanList);
                        AppConfig.getAppConfig(MainActivity.this).saveSortType(Integer.valueOf(type));

                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
                        handler.sendEmptyMessage(0);
                    }
                } else
                    handler.sendEmptyMessage(0);
            } else {
                handler.sendEmptyMessage(0);
            }

        }
    }

    long lastKeyDownTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long curTime = System.currentTimeMillis();
            if (curTime - lastKeyDownTime < 2000) {

                finish();
            } else {
                Toast.makeText(this, R.string.exit_string,
                        Toast.LENGTH_SHORT).show();
            }
            lastKeyDownTime = curTime;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
