package com.ggpl.player.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhangxiaoming on 2017/2/16.
 */



public class MD5Util {
    private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f' };

    private static int getChrInt(char chr) {
        int iRet = 0;
        switch (chr) {
            case '0':
                iRet = 0;
                break;
            case '1':
                iRet = 1;
                break;
            case '2':
                iRet = 2;
                break;
            case '3':
                iRet = 3;
                break;
            case '4':
                iRet = 4;
                break;
            case '5':
                iRet = 5;
                break;
            case '6':
                iRet = 6;
                break;
            case '7':
                iRet = 7;
                break;
            case '8':
                iRet = 8;
                break;
            case '9':
                iRet = 9;
                break;
            case 'A':
                iRet = 10;
                break;
            case 'B':
                iRet = 11;
                break;
            case 'C':
                iRet = 12;
                break;
            case 'D':
                iRet = 13;
                break;
            case 'E':
                iRet = 14;
                break;
            case 'F':
                iRet = 15;
                break;
        }
        return iRet;
    }

    public static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    /**
     * md5加密
     *
     * @param digestStr
     * @return
     */
    public static String mMD5Digest(String digestStr) {
        MessageDigest digest;
        byte[] digestedData = null;
        try {
            digest = MessageDigest.getInstance("md5");
            digestedData = digest.digest(digestStr.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bufferToHex(digestedData);
    }

    /**
     * md5加密方法(加盐)
     *
     * @param password
     * @return
     */
    public static String md5Password(String password) {

        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把没一个byte 做一个与运算 0xff
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                // System.out.println(str);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 将MD5 16进制String转为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] getKeyByStr(String str) {
        byte bRet[] = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            Integer itg = Integer.valueOf(16 * getChrInt(str.charAt(2 * i))
                    + getChrInt(str.charAt(2 * i + 1)));
            bRet[i] = itg.byteValue();
        }
        return bRet;
    }

    public static String getFileMd5(File file) {
        if (file.exists()) {
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                byte[] buf = new byte[1024];
                int len;
                MessageDigest digest = MessageDigest.getInstance("md5");
                while (-1 != (len = in.read(buf))) {
                    digest.update(buf, 0, len);
                }
                return bufferToHex(digest.digest());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } finally {
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}