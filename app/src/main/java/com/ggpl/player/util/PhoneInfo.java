package com.ggpl.player.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.ggpl.player.common.DebugLog;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class PhoneInfo {

    private static final String DEFAULT_VALUE = "";

    /**
     * 获取系统sdk版本
     */
    public static final int SDK_VERSION = Build.VERSION.SDK_INT;
    /**
     * 获取系统版本
     */
    public static final String SYSTEM_VERSION_CODE = Build.VERSION.RELEASE;
    /**
     * 获取手机型号
     */
    public static final String PHONE_MODEL = Build.MODEL;


    /**
     * 获取文件缩略图
     *
     * @param path   文件路径
     * @param width  显示的宽度
     * @param height 显示的高度
     * @return Bitmap 缩略图
     * @Description 定宽高解码缩略图
     */
    public static Bitmap decodeThumbBitmapForFile(final String path, final int width, final int height) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);

        DebugLog.d("封面的宽度-高度-->" + bitmap.getWidth() + "--" + bitmap.getHeight());

        //还可以选择MINI_KIND和MICRO_KIND
        if (bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            //根据源图片指定宽高生成新的图片
        }
        return bitmap;
    }

    /**
     * 获取文件缩略图
     *
     * @param path   文件路径
     * @param width  显示的宽度
     * @param height 显示的高度
     * @return Bitmap 缩略图
     * @Description 定宽高解码缩略图
     */
    public static String decodeThumbBitmapForFileToWidth(final String path) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);


        return bitmap.getWidth() + "x" + bitmap.getHeight();
    }


    public String EcodeTime(long time) {
        if (time < 1000 * 60 * 60) {
            DateFormat dateForma = new SimpleDateFormat("mm:ss");
            Date date = new Date(time);
            String time2 = dateForma.format(date);
            return "00:" + time2;

        } else {
            DateFormat dateForma = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date(time);
            String time2 = dateForma.format(date);
            return time2;
        }
    }

    /**
     * IMSI
     *
     * @param context
     * @return
     */
    public static String getImsi(Context context) {
        String imsi = null;
        try {
            TelephonyManager telManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imsi = telManager.getSubscriberId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TextUtils.isEmpty(imsi) ? DEFAULT_VALUE : imsi;
    }

    /**
     * IMEI
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        String imei = null;
        try {
            TelephonyManager telManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imei = telManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TextUtils.isEmpty(imei) ? DEFAULT_VALUE : imei;
    }

    /**
     * ��ȡMCC
     *
     * @param context
     * @return
     */
    public static String getMcc(Context context) {
        String mcc = null;
        try {
            String imsi = getImsi(context);
            if (null != imsi && !DEFAULT_VALUE.equals(imsi)) {
                mcc = imsi.substring(0, 3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TextUtils.isEmpty(mcc) ? DEFAULT_VALUE : mcc;
    }

    /**
     * ��ȡMNC
     *
     * @param context
     * @return
     */
    public static String getMnc(Context context) {
        String mnc = null;
        try {
            TelephonyManager telManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String perator = telManager.getSimOperator();
            String mcc = getMcc(context);
            if (null != perator && null != mcc && !DEFAULT_VALUE.equals(mcc)) {
                mnc = perator.substring(mcc.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TextUtils.isEmpty(mnc) ? DEFAULT_VALUE : mnc;
    }

    /**
     * ��ȡ meta_info
     *
     * @param context
     * @return
     */
    public static String getMetaInfo(Context context, String str) {
        String metaDataStr = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo appInfo = packageManager.getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle metaData = appInfo.metaData;


            Object metaObj = metaData.get(str);
            if (null != metaObj) {
                metaDataStr = metaObj.toString();
            } else {
                return DEFAULT_VALUE;
            }
        } catch (Exception e) {
            return DEFAULT_VALUE;
//			e.printStackTrace();
        }
        return TextUtils.isEmpty(metaDataStr) ? DEFAULT_VALUE : metaDataStr;
    }

    /**
     * ��ȡ�ֻ� �豸��
     *
     * @return
     */
    public static String getPhoneModel() {
        String model = null;
        try {
            model = Build.MODEL;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TextUtils.isEmpty(model) ? DEFAULT_VALUE : model;
    }


    /**
     * ��ȡ��ǰSDK�汾��Ϣ
     *
     * @param
     * @return
     */
    public static ApplicationInfo getAppInfo(Context context) {
        ApplicationInfo appInfo = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            appInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager
                .getApplicationLabel(applicationInfo);
        return applicationName == null ? "" : applicationName;
    }

    /**
     * ��ȡӦ�ð汾����
     *
     * @return
     */
    public static String getAppVersionName(Context context) {
        String versionName = null;
        try {
            versionName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * �ж��Ƿ���ϵͳ���
     *
     * @param pInfo
     * @return
     */
    public static boolean isSystemApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    /**
     * �ж��Ƿ���ϵͳ����ĸ������
     *
     * @param pInfo
     * @return
     */
    public static boolean isSystemUpdateApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
    }

    /**
     * �ж��Ƿ�Ϊ��ϵͳӦ��
     *
     * @param
     * @return
     */
    public static boolean isUserApp(Context context) {
        boolean result = false;
        try {
            PackageInfo pgkInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);

            result = !isSystemApp(pgkInfo) && !isSystemUpdateApp(pgkInfo);

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * ��ȡƽ̨��Ϣ
     *
     * @return
     */
    public static String getPlatformInfo() {
        String property = null;
        try {
            FileInputStream in = new FileInputStream("/proc/cpuinfo");
            Properties prop = new Properties();
            prop.load(in);
            property = prop.getProperty("Hardware");
            return property;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ��ȡGame app ��װĿ¼
     *
     * @param context
     * @return
     */
    public static String getAppInsDir(Context context) {
        List<PackageInfo> packs = context.getPackageManager()
                .getInstalledPackages(0);

        if (null != packs) {
            for (PackageInfo pkInfo : packs) {
                String pkName = pkInfo.packageName;
                if (context.getPackageName().equals(pkName)) {
                    return pkInfo.applicationInfo.sourceDir;
                }
            }
        }
        return null;
    }

    /**
     * ��ȡApp����ʱ��
     *
     * @param pkInfo
     * @return
     */
    @SuppressLint("NewApi")
    public static long getAppStayTime(PackageInfo pkInfo) {
        long firstInstallTime = pkInfo.firstInstallTime;
        long currentTimeMillis = System.currentTimeMillis();

        return (currentTimeMillis - firstInstallTime) / (1000 * 60);
    }

    /**
     * ��ȡApp����ʱ��
     *
     * @param pkInfo
     * @return
     */
    @SuppressLint("NewApi")
    public static long getAppStayTime(Context context) {
        long stayTime = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);

            long firstInstallTime = packageInfo.firstInstallTime;
            long currentTimeMillis = System.currentTimeMillis();

            stayTime = (currentTimeMillis - firstInstallTime) / (1000 * 60);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stayTime;
    }

    /**
     * �ж�Ӧ���Ƿ��Ѿ���װ
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isPackageExists(Context context, String packageName) {
        List<ApplicationInfo> packages;
        packages = context.getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * ��ȡӦ�ð汾���Ͱ汾��
     *
     * @param context
     * @param packageName
     * @return
     */
    public static int getVersionCode(Context context, String packageName) {
        PackageInfo pkInfo;
        try {
            pkInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            return pkInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * ��ȡ�ֻ���ǰ��ʱ�䣨��������
     *
     * @return
     */
    public static int getCurrentTime() {
        return (int) (System.currentTimeMillis() / (60 * 1000));
    }

    /**
     * ��ȡӦ��icon ��ԴID
     *
     * @param context
     * @return
     */
    public static int getAppIconResId(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), 0);
            return appInfo.icon;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @SuppressLint("NewApi")
    public static String getPhoneCode(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String te1 = tm.getLine1Number();// ��ȡ��������
            return te1 == null ? "" : te1;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return "";
    }


    public static String getCurrentPkgName(Context context) {
        ActivityManager.RunningAppProcessInfo currentInfo = null;
        Field field = null;
        int START_TASK_TO_FRONT = 2;
        String pkgName = null;

        if (SDK_VERSION < 21) {
            ActivityManager systemService = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningTaskInfo> infos = systemService.getRunningTasks(1);
            String packname = infos.get(0).topActivity.getPackageName().trim();

            if (packname == null) {
                return "--";
            }
            return packname;

        } else {

            try {
                field = ActivityManager.RunningAppProcessInfo.class
                        .getDeclaredField("processState");
            } catch (Exception e) {
                e.printStackTrace();
            }
            ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appList = am
                    .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo app : appList) {
                if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Integer state = null;
                    try {
                        state = field.getInt(app);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (state != null && state == START_TASK_TO_FRONT) {
                        currentInfo = app;
                        break;
                    }
                }
            }
            if (currentInfo != null) {
                pkgName = currentInfo.processName;
            }
            if (pkgName == null) {
                return "--";
            }
            return pkgName;
        }
    }

    /**
     * �ж�app�Ƿ����
     *
     * @param context
     * @param pakeName
     * @return
     */
    public static boolean isAppAvilible(Context context, String pakeName) {
        final PackageManager packageManager = context.getPackageManager();// ��ȡpackagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// ��ȡ�����Ѱ�װ����İ���Ϣ
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(pakeName)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String getAppPackage(Context context) {
        String str = context.getPackageName();
        Log.d("zjm", "packName---" + str);
        // PackageManager pManager=context.getPackageManager();
        // ActivityManager manager = (ActivityManager)
        // context.getSystemService(Context.ACTIVITY_SERVICE);
        // PackageInfo packageInfo=pManager.getPackageInfo(packageName, flags)
        // pManager.p
        return str;
    }

    public static String getAndroidID(Context context) {
        String ANDROID_ID = Settings.System.getString(
                context.getContentResolver(), Settings.System.ANDROID_ID);
        if (ANDROID_ID.isEmpty()) {
            return DEFAULT_VALUE;
        }
        return ANDROID_ID;
    }

}
