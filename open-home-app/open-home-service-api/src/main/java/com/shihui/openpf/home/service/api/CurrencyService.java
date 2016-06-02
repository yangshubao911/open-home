package com.shihui.openpf.home.service.api;

/**
 * Created by zhoutc on 2016/2/27.
 */
public interface CurrencyService {

    /**
     * 查询用户实惠现金余额
     *
     * @param userId 用户ID
     *
     * @return 实惠现金余额
     */
    long getUserBalance(long userId);
}
