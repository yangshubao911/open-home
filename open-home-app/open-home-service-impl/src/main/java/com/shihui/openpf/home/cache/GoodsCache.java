package com.shihui.openpf.home.cache;

import javax.annotation.Resource;

import com.shihui.openpf.common.tools.Constants;
import com.shihui.openpf.home.dao.CategoryDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by zhoutc on 2016/2/20.
 */
@Service
public class GoodsCache {

    @Resource
    private ShardedJedisPool jedisPool;
    @Resource
    private CategoryDao categoryDao;
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 增加商品分类销售数量
     *
     * @param categoryId 商品Id
     * @return
     */
    public Long increaseSell(long categoryId) {
        ShardedJedis jedis = null;
        try {
            String key = Constants.REDIS_KEY_PREFIX + Constants.REDIS_KEY_SEPARATOR + "sellNum" +
                    Constants.REDIS_KEY_SEPARATOR + categoryId;
            jedis = jedisPool.getResource();
            return jedis.incr(key);
        } catch (Exception e) {
            log.error("GoodsCache increaseSell error!!", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0l;
    }

    /**
     * 查询商品分类销售数量
     *
     * @param categoryId 商品Id
     * @return
     */
    public Long querySell(long categoryId) {
        ShardedJedis jedis = null;
        try {
            String key = Constants.REDIS_KEY_PREFIX + Constants.REDIS_KEY_SEPARATOR + "sellNum" +
                    Constants.REDIS_KEY_SEPARATOR + categoryId;
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            int sold =categoryDao.querySold(categoryId);
            if(value==null) return (long)sold;
            return Long.parseLong(value)+sold;
        } catch (Exception e) {
            log.error("GoodsCache querySell error!!", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0l;
    }

}
