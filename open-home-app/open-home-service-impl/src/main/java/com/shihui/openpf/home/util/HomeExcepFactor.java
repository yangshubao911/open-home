package com.shihui.openpf.home.util;


import me.weimi.api.core.ExcepFactor;
import me.weimi.api.core.HttpStatus;

/**
 * Created by zhoutc on 2015/8/21.
 */
public class HomeExcepFactor extends ExcepFactor {

    public static final HomeExcepFactor Service_Close;

    public static final HomeExcepFactor Group_Unfound;

    public static final HomeExcepFactor Goods_Unfound;
    protected HomeExcepFactor(HttpStatus httpStatus, int errorCode, String errorMsg, String errorMsgCn) {
        super(29, httpStatus, errorCode, errorMsg, errorMsgCn);
    }

    static {
        Service_Close = new HomeExcepFactor(HttpStatus.BAD_REQUEST, 1, "service unserviceable", "服务已经暂停使用");
        Group_Unfound = new HomeExcepFactor(HttpStatus.BAD_REQUEST, 1, "group id error", "小区暂时无法提供服务");
        Goods_Unfound = new HomeExcepFactor(HttpStatus.BAD_REQUEST, 1, "can't find goods", "无法提供服务内容");

    }
}
