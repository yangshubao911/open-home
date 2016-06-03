package com.shihui.openpf.home.service.impl;

import com.shihui.openpf.home.service.api.CurrencyService;
import com.shihui.tradingcenter.commons.dispatcher.currency.AccountDubbo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zhoutc on 2016/2/27.
 */
@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Resource
    AccountDubbo accountDubbo;

    /**
     * 查询用户实惠现金余额
     *
     * @param userId 用户ID
     *
     * @return 实惠现金余额
     */
    @Override
    public long getUserBalance(long userId) {
        return accountDubbo.getUserSHGlodAmount(userId);
    }




}
