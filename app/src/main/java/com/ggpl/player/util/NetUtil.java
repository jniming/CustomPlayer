package com.ggpl.player.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.wifi.WifiManager;
import android.provider.Settings;

/**
 * Created by zhangxiaoming on 2017/2/14.
 */

public class NetUtil {
    public static final int WIFI = 0;
    public static final int MOBILE = 1;
    public static final String NET_TYPE_WIFI = "wifi";
    public static final String NET_TYPE_UNCONNECTED = "unconnected";

    private static final String[] networkType = {"UNKNOWN", "GPRS", "EDGE", "UMTS", "CDMA", "EVDO_0", "EVDO_A",
            "1xRTT", "HSDPA", "HSUPA", "HSPA", "IDEN", "EVDO_B", "LTE", "EHRPD", "HSPAP"};

    /**
     * 判断网络状态（WIFI和移动网络）
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static NetInfo getNetInfo(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetInfo netInfo = new NetInfo();

        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (null != activeNetworkInfo) {
            if (ConnectivityManager.TYPE_WIFI == activeNetworkInfo.getType()) {
                netInfo.setNetType(NET_TYPE_WIFI);
                return netInfo;
            } else if (ConnectivityManager.TYPE_MOBILE == activeNetworkInfo.getType()) {
                int subType = activeNetworkInfo.getSubtype();
                String moblieNetType = (subType >= 0) && (subType < networkType.length) ? networkType[subType]
                        : String.valueOf(subType);

                netInfo.setNetType(moblieNetType);
                netInfo.setApn(activeNetworkInfo.getExtraInfo());
                netInfo.setApnProxy(Proxy.getDefaultHost());
                netInfo.setApnPort(Proxy.getDefaultPort());
                return netInfo;
            }
        }
        return null;
    }

    /**
     * 检查手机网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isNetConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (null != activeNetworkInfo) {
            return activeNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 获取当前接入的Apn
     *
     * @param context
     * @return
     */
    public static String getApn(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (info == null) {
            return null;
        }
        return info.getExtraInfo();
    }

    public static String getNetType(Context context) {
        String netType = null;
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            if (null != activeNetworkInfo) {
                if (ConnectivityManager.TYPE_WIFI == activeNetworkInfo.getType()) {// WIFI

                    netType = NET_TYPE_WIFI;

                } else if (ConnectivityManager.TYPE_MOBILE == activeNetworkInfo.getType()) {// mobile
                    // net
                    int subType = activeNetworkInfo.getSubtype();
                    String moblieNetType = (subType >= 0) && (subType < networkType.length) ? networkType[subType]
                            : String.valueOf(subType);
                    netType = moblieNetType;
                }
            }
        } catch (Exception e) {
        }
        return netType;
    }

    /**
     * 提示是否需要wifi或者网络
     */
    public static void showOpenNetworkDialog(final Activity context) {
        // 提示完成后，提示用户是否跳转到网络设置或者wifi界面
        final int[] selectIndex = {0};
        final AlertDialog.Builder[] dialog = {new AlertDialog.Builder(context)};
        dialog[0].setTitle("Check your network !");
        dialog[0].setSingleChoiceItems(new String[]{"wifi", "network"}, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectIndex[0] = which;
            }
        });
        dialog[0].setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (selectIndex[0]) {
                    case 0: // wifi
                        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                        manager.setWifiEnabled(true); // 启用wifi
                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));// 进入无线网络配置界面
                        context.finish();
                        break;
                    case 1: // network 2G 3G 4G
                        context.startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)); // 进入手机中的网络设置界面
                        context.finish();
                        break;
                    default:
                        break;
                }
            }
        });
        dialog[0].setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                context.finish();
            }
        });
        dialog[0].show();
    }

   public  static class NetInfo {
        private String apn;
        private String netType;
        private String apnProxy;
        private int apnPort;

        public String getApn() {
            return apn;
        }

        public void setApn(String apn) {
            this.apn = apn;
        }

        public String getNetType() {
            return netType;
        }

        public void setNetType(String netType) {
            this.netType = netType;
        }

        public String getApnProxy() {
            return apnProxy;
        }

        public void setApnProxy(String apnProxy) {
            this.apnProxy = apnProxy;
        }

        public int getApnPort() {
            return apnPort;
        }

        public void setApnPort(int apnPort) {
            this.apnPort = apnPort;
        }

    }


}
