package com.shihui.openpf.home.util;


import me.weimi.api.core.ExcepFactor;
import me.weimi.api.core.HttpStatus;

/**
 * Created by zhoutc on 2015/8/21.
 */
public class HomeExcepFactor extends ExcepFactor {

    public static final HomeExcepFactor TEST;


    protected HomeExcepFactor(HttpStatus httpStatus, int errorCode, String errorMsg, String errorMsgCn) {
        super(29, httpStatus, errorCode, errorMsg, errorMsgCn);
    }

    static {
        TEST = new HomeExcepFactor(HttpStatus.BAD_REQUEST, 1, "TEST", "TEST");
    }
}
