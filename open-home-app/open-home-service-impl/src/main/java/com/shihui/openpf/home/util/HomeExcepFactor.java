package com.shihui.openpf.home.util;


import me.weimi.api.core.ExcepFactor;
import me.weimi.api.core.HttpStatus;

/**
 * Created by zhoutc on 2015/8/21.
 */
public class HomeExcepFactor extends ExcepFactor {

	private static final long serialVersionUID = 6306671362373933341L;

	public static final HomeExcepFactor Service_Close;

    public static final HomeExcepFactor Group_Unfound;

    public static final HomeExcepFactor Goods_Unfound;
    public static final HomeExcepFactor Goods_Close;

    public static final HomeExcepFactor Category_Unfound;
    public static final HomeExcepFactor Category_Close;

    public static final HomeExcepFactor Merchant_Unfound;

    public static final HomeExcepFactor Merchant_Refresh;

    public static final HomeExcepFactor Price_Wrong;

    public static final HomeExcepFactor Third_Order_Fail;

    public static final HomeExcepFactor Order_Fail;

    public static final HomeExcepFactor Place_Unsupport;

    protected HomeExcepFactor(HttpStatus httpStatus, int errorCode, String errorMsg, String errorMsgCn) {
        super(29, httpStatus, errorCode, errorMsg, errorMsgCn);
    }

    static {
        Service_Close = new HomeExcepFactor(HttpStatus.BAD_REQUEST, 1, "Service Unavailable", "服务已经暂停使用");
        Group_Unfound = new HomeExcepFactor(HttpStatus.BAD_REQUEST, 2, "group id error", "小区暂时无法提供服务");
        Goods_Unfound = new HomeExcepFactor(HttpStatus.BAD_REQUEST, 3, "can't find goods", "无法提供服务内容");
        Goods_Close = new HomeExcepFactor(HttpStatus.BAD_REQUEST, 4, "goods unserviceable", "无法提供服务内容");
        Category_Unfound = new HomeExcepFactor(HttpStatus.BAD_REQUEST,5, "can't find category", "无法提供服务内容");
        Category_Close = new HomeExcepFactor(HttpStatus.BAD_REQUEST, 6, "category unserviceable", "无法提供服务内容");
        Merchant_Unfound = new HomeExcepFactor(HttpStatus.BAD_REQUEST,7, "can't find merchant", "无法提供服务内容");
        Merchant_Refresh = new HomeExcepFactor(HttpStatus.BAD_REQUEST,8, "rechoice time and place", "所选地区和时间无法提供服务,请重新选择");
        Price_Wrong = new HomeExcepFactor(HttpStatus.BAD_REQUEST,9, "price has changed", "价格变动,请重新下单");
        Third_Order_Fail = new HomeExcepFactor(HttpStatus.BAD_REQUEST,10, "create third order fail", "创建订单失败");
        Order_Fail = new HomeExcepFactor(HttpStatus.BAD_REQUEST,11, "create local order fail", "创建订单失败");
        Place_Unsupport = new HomeExcepFactor(HttpStatus.BAD_REQUEST,12, "place not support", "不在服务范围内");

    }
}
