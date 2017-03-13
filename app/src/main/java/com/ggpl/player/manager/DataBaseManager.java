package com.ggpl.player.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ggpl.player.R;
import com.ggpl.player.common.DebugLog;
import com.ggpl.player.model.bean.FileDirBean;
import com.ggpl.player.model.bean.ModuleBean;
import com.ggpl.player.model.bean.Videobean;
import com.ggpl.player.model.entitiy.FileDirEntity;
import com.ggpl.player.sql.SqlHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaoming on 2017/2/15.
 */

public class DataBaseManager implements queryManager {


    private SqlHelper helper;
    private SQLiteDatabase db;
    private Context mcontext;

    public DataBaseManager(Context context) {
        helper = new SqlHelper(context);
        this.mcontext = context;
    }

    /**
     * add
     *
     * @param
     */
    public void addAllvideo(List<Videobean> list) {
        db = helper.getWritableDatabase();

        for (Videobean bean : list) {

            ContentValues cv = new ContentValues();
            cv.put("path", bean.getPath());
            cv.put("wm", bean.getWh());
            cv.put("title", bean.getTitle());
            cv.put("size", bean.getSize());
            cv.put("time", bean.getTime());
            cv.put("dir", bean.getDir());
            cv.put("logtime", System.currentTimeMillis() + "");

            db.insert(SqlHelper.TABLE_NAME[0], "", cv);


        }
        db.close();
    }

    @Override
    public void addOnevideo(Videobean bean) {

        List l = queryAllVideo("select * from " + SqlHelper.TABLE_NAME[0] + " where path='" + bean.getPath() + "'");

        if (l == null || l.size() == 0) {


            db = helper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put("path", bean.getPath());
            cv.put("wm", bean.getWh());
            cv.put("title", bean.getTitle());
            cv.put("size", bean.getSize());
            cv.put("time", bean.getTime());
            cv.put("dir", bean.getDir());
            cv.put("logtime", System.currentTimeMillis() + "");
            db.insert(SqlHelper.TABLE_NAME[0], "", cv);
            db.close();
        }


    }

    @Override
    public List<Videobean> queryAllVideo() {
        db = helper.getReadableDatabase();
        ArrayList<Videobean> persons = new ArrayList<Videobean>();

        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            Videobean bean = new Videobean();
            bean.setPath(c.getString(c.getColumnIndex("path")));
            bean.setTime(c.getString(c.getColumnIndex("time")));
            bean.setWh(c.getString(c.getColumnIndex("wm")));
            bean.setTitle(c.getString(c.getColumnIndex("title")));
            bean.setSize(c.getString(c.getColumnIndex("size")));
            bean.setDir(c.getString(c.getColumnIndex("dir")));
            bean.setLogtime(c.getString(c.getColumnIndex("logtime")));
            persons.add(bean);
        }
        c.close();
        db.close();
        return persons;
    }

    @Override
    public List<Videobean> queryAllVideo(String sql) {
        db = helper.getReadableDatabase();
        ArrayList<Videobean> persons = new ArrayList<Videobean>();

        DebugLog.w("查询路径是否存在-->" + sql);

        Cursor c = db.rawQuery(sql, null);

        while (c.moveToNext()) {
            Videobean bean = new Videobean();
            bean.setPath(c.getString(c.getColumnIndex("path")));
            bean.setTime(c.getString(c.getColumnIndex("time")));
            bean.setWh(c.getString(c.getColumnIndex("wm")));
            bean.setTitle(c.getString(c.getColumnIndex("title")));
            bean.setSize(c.getString(c.getColumnIndex("size")));
            bean.setDir(c.getString(c.getColumnIndex("dir")));
            bean.setLogtime(c.getString(c.getColumnIndex("logtime")));
            persons.add(bean);
        }
        db.close();
        c.close();

        return persons;
    }


    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM " + SqlHelper.TABLE_NAME[0], null);
        return c;
    }


    /**
     * add
     *
     * @param
     */
    public void addFileDir(FileDirBean dirBean) {
        List l = queryFileDir("select * from " + SqlHelper.TABLE_NAME[1] + " where dirname='" + dirBean.getDirname()
                + "'");
        if (l == null || l.size() == 0) {
            db = helper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("dirname", dirBean.getDirname());
            cv.put("ishiden", dirBean.getIshiden());
            cv.put("logtime", System.currentTimeMillis() + "");
            db.insert(SqlHelper.TABLE_NAME[1], "", cv);

            db.close();
        } else {
            DebugLog.w("目录存在");

        }

    }

    @Override
    public List<FileDirEntity> queryAllFileDir() {

        db = helper.getReadableDatabase();
        ArrayList<FileDirEntity> persons = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM " + SqlHelper.TABLE_NAME[1], null);
        while (c.moveToNext()) {
            FileDirEntity dirEntity = new FileDirEntity();

            FileDirBean ben = new FileDirBean();
            String dirname = c.getString(c.getColumnIndex("dirname"));
            ben.setDirname(dirname);
            ben.setIshiden(c.getString(c.getColumnIndex("ishiden")));
            ben.setLogtime(c.getString(c.getColumnIndex("logtime")));
            List count = null;
            if (dirname.equals(mcontext.getResources().getString(R.string.all_filedir))) {

                count = queryAllVideo();
            } else {
                count = queryAllVideo("select * from " + SqlHelper.TABLE_NAME[0] + " where dir='" + dirname + "'");


            }


            dirEntity.setDirBean(ben);
            dirEntity.setVideoList(count);
            persons.add(dirEntity);
        }
        c.close();
        db.close();
        return persons;

    }

    @Override
    public List<FileDirEntity> queryFileDir(String sql) {
        db = helper.getReadableDatabase();
        List<FileDirEntity> list = new ArrayList<FileDirEntity>();
        Cursor cursor = db.rawQuery(sql, null);


        if (cursor.getCount() <= 0) {

            return list;

        } else {

            while (cursor.moveToNext()) {
                FileDirEntity dirEntity = new FileDirEntity();

                FileDirBean ben = new FileDirBean();
                String dirname = cursor.getString(cursor.getColumnIndex("dirname"));
                ben.setDirname(dirname);
                ben.setIshiden(cursor.getString(cursor.getColumnIndex("ishiden")));
                ben.setLogtime(cursor.getString(cursor.getColumnIndex("logtime")));
                List count = null;

                if (dirname.equals(mcontext.getResources().getString(R.string.all_filedir))) {

                    count = queryAllVideo();
                } else {
                    count = queryAllVideo("select * from " + SqlHelper.TABLE_NAME[0] + " where dir='" + dirname + "'");


                }
                dirEntity.setDirBean(ben);
                dirEntity.setVideoList(count);
                list.add(dirEntity);


            }

            return list;
        }
    }

    @Override
    public void updateFiledirHiden(FileDirBean bean) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ishiden", bean.getIshiden());
        db.update(SqlHelper.TABLE_NAME[1], values, "dirname=?", new String[]{bean.getDirname()});
        db.close();

    }

    @Override
    public void clearVideoTable() {
        db = helper.getWritableDatabase();
        //删除除了显示的文件夹的数据    ,这里要加等于问好
        db.delete(SqlHelper.TABLE_NAME[1], "ishiden=?", new String[]{"0"});
        //删除视频列表
        db.delete(SqlHelper.TABLE_NAME[0], null, null);
        db.delete(SqlHelper.TABLE_NAME[2], null, null);

        db.close();
    }

    @Override
    public void addModule(List<ModuleBean> ben) {
        db = helper.getWritableDatabase();
        db.delete(SqlHelper.TABLE_NAME[2], null, null);
        for (ModuleBean b : ben) {
            ContentValues values = new ContentValues();
            values.put("desc", b.getDesc());
            values.put("name", b.getName());
            values.put("imgurl", b.getImgurl());
            values.put("h5url", b.getH5url());
            values.put("ishiden", b.getIshiden());
            values.put("logtime", System.currentTimeMillis() + "");

            long i = db.insert(SqlHelper.TABLE_NAME[2], "", values);
            DebugLog.d("插入的id-->" + i);

        }
        db.close();

    }

    @Override
    public List<ModuleBean> getModule() {

        String sql = "select * from " + SqlHelper.TABLE_NAME[2]+" where ishiden=0";
        List<ModuleBean> list = new ArrayList<>();

        db = helper.getReadableDatabase();
//        Cursor c = db.query(SqlHelper.TABLE_NAME[2], null, "ishiden=?", new String[]{"0"}, null, null, null);
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            ModuleBean bean = new ModuleBean();
            bean.setDesc(c.getString(c.getColumnIndex("desc")));
            bean.setName(c.getString(c.getColumnIndex("name")));
            bean.setH5url(c.getString(c.getColumnIndex("h5url")));
            bean.setImgurl(c.getString(c.getColumnIndex("imgurl")));
            bean.setIshiden(c.getString(c.getColumnIndex("ishiden")));
            bean.setLogtime(c.getString(c.getColumnIndex("logtime")));
            list.add(bean);
        }


        return list;
    }


}
