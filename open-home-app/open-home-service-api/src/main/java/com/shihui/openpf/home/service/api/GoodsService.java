/**
 *
 */
package com.shihui.openpf.home.service.api;

import java.util.List;

import com.shihui.openpf.home.model.Goods;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月19日 下午6:42:35
 */
public interface GoodsService {

    public String create(Goods goods);

    public String update(Goods goods);

    public Goods findById(long id);

    public Goods findByCity(int categoryId, int cityId);

    public List<Goods> list(int categoryId);

}
