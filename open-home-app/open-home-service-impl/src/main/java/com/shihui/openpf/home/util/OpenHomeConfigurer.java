package com.shihui.openpf.home.util;

/**
 * Created by zhoutc on 2016/2/26.
 */
public class OpenHomeConfigurer {

    public  static String snapshot_url;
    public  static String snapshot_appid;
    public  static String snapshot_salt;
    public  static String snapshot_appsn;


    public static void setSnapshot_url(String snapshot_url) {
        OpenHomeConfigurer.snapshot_url = snapshot_url;
    }

    public static void setSnapshot_appid(String snapshot_appid) {
        OpenHomeConfigurer.snapshot_appid = snapshot_appid;
    }

    public static void setSnapshot_salt(String snapshot_salt) {
        OpenHomeConfigurer.snapshot_salt = snapshot_salt;
    }

    public static void setSnapshot_appsn(String snapshot_appsn) {
        OpenHomeConfigurer.snapshot_appsn = snapshot_appsn;
    }
}
