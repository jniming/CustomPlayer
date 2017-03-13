package com.ggpl.player.model.bean;

import android.content.Context;

import com.ggpl.player.util.NetUtil;
import com.ggpl.player.util.PhoneInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangxiaoming on 2017/2/14.
 */

public class BaseData {


    protected String busID; // 业务Id
    protected String proID; // 产品Id;
    protected String cnID; // 渠道ID
    protected String scnID; // 子渠道ID

    protected String imsi; // 国际移动用户识别码
    protected String imei; // 手机设备号
    protected String mcc; // 移动国家码
    protected String mnc; // 移动网络号码

    protected int sysVc; // 系统版本
    protected String apn; // 接入点类型名称
    protected String netType; // 网络类型（wifi、mobile、无连接）
    protected String phoneCode;
    protected Context mContext = null;
    private String phModel;
    private String pakName;
    private String packgeName;


    private int sdkverson ;
    private String androidId;


    public BaseData(Context context) {
        this.mContext = context;
        init();

    }

    private void init() {
        imei = PhoneInfo.getImei(mContext);
        imsi = PhoneInfo.getImsi(mContext);
        mcc = PhoneInfo.getMcc(mContext);
        mnc = PhoneInfo.getMnc(mContext);

        busID=PhoneInfo.getMetaInfo(mContext,"EBUS");
        cnID=PhoneInfo.getMetaInfo(mContext,"ECH");
        scnID=PhoneInfo.getMetaInfo(mContext,"ESCH");
        proID=PhoneInfo.getMetaInfo(mContext,"EAP");

        androidId= PhoneInfo.getAndroidID(mContext);

        sysVc = PhoneInfo.SDK_VERSION;
        phoneCode ="";
        NetUtil.NetInfo netInfo = NetUtil.getNetInfo(mContext);
        phModel =PhoneInfo.getPhoneModel();

        packgeName = PhoneInfo.getAppInfo(mContext).packageName;
        pakName = PhoneInfo.getApplicationName(mContext);
        sdkverson=PhoneInfo.SDK_VERSION;
        androidId=PhoneInfo.getAndroidID(mContext);
        if (null != netInfo) {
            apn = netInfo.getApn();
            netType = netInfo.getNetType();
        }
    }

    /**
     * p1 imsi p2 imei p3 mcc p4 mnc p5 key //渠道key p6 sysVc //系统版本 p7 apn p8
     * netType //网络类型 p9 phoneCode //手机号 p10 packgeName //包名 p11 phModel //设备名
     * p12 wx //0是代表微信客户端不存在,1存在 p13 zfb //0是代表支付宝客户端不存在,1存在
     *
     * @return
     */
    public final JSONObject getBaseDataJsonObj() {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject();
            jsonObj.put("p1", imsi);
            jsonObj.put("p2", imei);
            jsonObj.put("p3", mcc);
            jsonObj.put("p4", mnc);

            jsonObj.put("p5", busID);
            jsonObj.put("p6", proID);
            jsonObj.put("p7", cnID);
            jsonObj.put("p8", scnID);



            jsonObj.put("p9", sysVc);
            jsonObj.put("p10", apn);
            jsonObj.put("p11", netType);
            jsonObj.put("p12", phoneCode);
            jsonObj.put("p13", packgeName);
            jsonObj.put("p14", phModel);

            jsonObj.put("p15", sdkverson);
            jsonObj.put("p16", androidId);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject getDataJson() {

        return getBaseDataJsonObj();

    }

}



