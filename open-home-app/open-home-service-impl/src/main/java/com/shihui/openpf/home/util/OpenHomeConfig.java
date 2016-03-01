package com.shihui.openpf.home.util;

/**
 * Created by zhoutc on 2016/2/26.
 */
public class OpenHomeConfig {

    public static String snapshot_url;
    public static String snapshot_appid;
    public static String snapshot_salt;
    public static String snapshot_appsn;
    public static String order_submit_url;


    public static void setSnapshot_url(String snapshot_url) {
        OpenHomeConfig.snapshot_url = snapshot_url;
    }

    public static void setSnapshot_appid(String snapshot_appid) {
        OpenHomeConfig.snapshot_appid = snapshot_appid;
    }

    public static void setSnapshot_salt(String snapshot_salt) {
        OpenHomeConfig.snapshot_salt = snapshot_salt;
    }

    public static void setSnapshot_appsn(String snapshot_appsn) {
        OpenHomeConfig.snapshot_appsn = snapshot_appsn;
    }

    public static void setOrder_submit_url(String order_submit_url) {
        OpenHomeConfig.order_submit_url = order_submit_url;
    }
}
