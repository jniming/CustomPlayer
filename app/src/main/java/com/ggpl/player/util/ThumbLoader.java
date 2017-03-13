package com.ggpl.player.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.ggpl.player.common.DebugLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaoming on 2017/2/16.
 */

public class ThumbLoader {


    //创建cache
    private LruCache<String, Bitmap> lruCache;

    private Context mContext;

    private String imgpath = "";

    private static ThumbLoader loader;

    public static ThumbLoader getIntentce(Context mContext) {
        if (loader == null) {
            loader = new ThumbLoader(mContext);

        }
        return loader;
    }


    @SuppressLint("NewApi")
    public ThumbLoader(Context mContext) {

    if(ExistSDCard()) {

        imgpath = Environment.getExternalStorageDirectory().getPath() + "/playerdir";
        DebugLog.d("保存目录" + imgpath);
    }else{
        DebugLog.d("");
        List<String> extPaths = getExtSDCardPath();
        if(extPaths!=null&&extPaths.size()!=0){
            for (String path : extPaths) {
                DebugLog.d("外置SD卡路径：" + path + "\r\n");
                imgpath=path+"/playerdir";
            }
        }

    }




        this.mContext = mContext;
        int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取最大的运行内存
        int maxSize = maxMemory * 2 / 3;
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //这个方法会在每次存入缓存的时候调用
                return value.getByteCount();
            }
        };
    }


    /**
     * 获取外置SD卡路径
     * @return  应该就一条记录或空
     */
    public List<String> getExtSDCardPath()
    {
        List<String> lResult = new ArrayList<String>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard"))
                {
                    String [] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory())
                    {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
        }
        return lResult;
    }
    private boolean ExistSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    public void addVideoThumbToCache(String path, Bitmap bitmap) {
        if (getVideoThumbToCache(path) == null) {
            //当前地址没有缓存时，就添加

            lruCache.put(path, bitmap);
        }
    }

    public Bitmap getVideoThumbToCache(String path) {

        return lruCache.get(path);

    }

    public void showThumbByAsynctack(String path, ImageView imgview) {

        DebugLog.d("imageview-->"+imgview);

        if(imgview==null){
            return;
        }

        if (getVideoThumbToCache(path) == null) {

            File file = new File(imgpath, getFileName(path));

            if (file.exists()) {   //如果本地存在,加载本地
                DebugLog.d("获取本地图片");

                imgview.setImageBitmap(toRoundCornerImage(getLoacalBitmap(file), UiScreen.dip2px(mContext, 6)));

            } else {
                DebugLog.d("获取缩略图");
                //异步加载
                new MyBobAsynctack(imgview, path).execute(path);
            }
        } else {
            DebugLog.d("设置缓存图片");
            imgview.setImageBitmap(getVideoThumbToCache(path));
        }

    }

    class MyBobAsynctack extends AsyncTask<String, Void, Bitmap> {
        private ImageView imgView;
        private String path;

        public MyBobAsynctack(ImageView imageView, String path) {
            this.imgView = imageView;
            this.path = path;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //这里的创建缩略图方法是调用VideoUtil类的方法，也是通过 android中提供的 ThumbnailUtils.createVideoThumbnail(vidioPath, kind);
            Bitmap bitmap = PhoneInfo.decodeThumbBitmapForFile(path, UiScreen.dip2px(mContext, 120), UiScreen
                    .dip2px(mContext, 87));
            saveBitmap(path, bitmap);
            //加入缓存中
            if (getVideoThumbToCache(params[0]) == null) {
                addVideoThumbToCache(path, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (imgView.getTag().equals(path)) {//通过 Tag可以绑定 图片地址和 imageView，这是解决Listview加载图片错位的解决办法之一

                imgView.setImageBitmap(toRoundCornerImage(bitmap, UiScreen.dip2px(mContext, 6)));
            }
        }
    }

    /**
     * 保存方法
     */
    public void saveBitmap(String path, Bitmap bitmap) {
      File dir=new File(imgpath);

        if(!dir.exists()){
            dir.mkdir();
        }

        File f = new File(imgpath, getFileName(path));

        try {
if(f.exists()){
    DebugLog.d("图片存在");
    return;
}

            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 加载本地图片
     *
     * @param
     * @return
     */
    public Bitmap getLoacalBitmap(File file) {

        try {
            FileInputStream fis = new FileInputStream(file);

            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFileName(String path) {
        String imgname = MD5Util.mMD5Digest(path) + ".jpg";

        return imgname;
    }

    /**
     * 获取圆角位图的方法
     *
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap toRoundCornerImage(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        // 抗锯齿
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

}

