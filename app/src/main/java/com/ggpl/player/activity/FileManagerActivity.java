package com.ggpl.player.activity;

import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ggpl.player.R;
import com.ggpl.player.adpter.PathListAdpter;
import com.ggpl.player.model.bean.Videobean;
import com.ggpl.player.util.MediaFile;

import java.io.DataOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaoming on 2017/2/25.
 */

public class FileManagerActivity extends BaseActivity implements PathListAdpter.ItemClick {

    private Button topbtnl;

    private RecyclerView recv;

    private TextView path;

    private PathListAdpter adpter;

    private List<File> files = new ArrayList<>();
    private File file;
    private TextView emtiy_text;


    private static final File baseFile = new File(Environment.getExternalStorageDirectory().toString());

    @Override
    protected int getContentLayoutID() {
        return R.layout.filemanager;
    }

    @Override
    protected void initView() {
        topbtnl = (Button) findViewById(R.id.toptext);
        path = (TextView) findViewById(R.id.path);
        recv = (RecyclerView) findViewById(R.id.file_recv);
        emtiy_text = (TextView) findViewById(R.id.emtiy_text);
        recv.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void initCode() {
        setTitle(getResources().getString(R.string.main_menu_item4));
//        String apkRoot = "chmod 777 " + getPackageCodePath();
//        RootCommand(apkRoot);   //获取文件权限
        getFileList(baseFile,false);
        adpter = new PathListAdpter(files, this);

        adpter.setItemClick(this);
        recv.setAdapter(adpter);

        topbtnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFileList(file,true);

            }
        });
    }


    /**
     * 修改文件权限
     *
     * @param command
     * @return
     */
    public static boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void getFileList(File folder, boolean is) {
        path.setText(folder.getAbsolutePath());

        if (files != null) {
            files.clear();
        }

        File[] filterFiles = folder.listFiles();

        if (null != filterFiles && filterFiles.length > 0) {
            for (File file : filterFiles) {
                files.add(file);
            }
        }
        if (is)
            adpter.notifyDataSetChanged();
        if (folder.getPath().equals(baseFile.getPath())) {
            topbtnl.setText(R.string.base_path);
            file = baseFile;
        } else {
            topbtnl.setText(R.string.return_path);
            file = folder.getParentFile();
        }

        if (files.size() == 0) {
            emtiy_text.setVisibility(View.VISIBLE);
        } else {
            emtiy_text.setVisibility(View.GONE);
        }


    }

    @Override
    public void itemClickListener(File position) {

        if (position.isDirectory()) {

            getFileList(position,true);
        } else {
            if (MediaFile.isVideoFileType(position.getPath())) {
                Videobean videobean = new Videobean();
                videobean.setTitle(position.getName());
                videobean.setPath(position.getPath());


                readyGo(Player2_Activity.class, videobean);
            } else {
                Toast.makeText(this, R.string.no_player, Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            getFileList(file,true);


        }

        return true;

    }


}
