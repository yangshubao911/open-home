package com.shihui.openpf.home.cache;

import javax.annotation.Resource;

import com.shihui.openpf.common.tools.Constants;
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

    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 增加商品分类销售数量
     *
     * @param categoryId 分类ID
     * @return
     */
    public Long increaseSell(int categoryId) {
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
     * @param categoryId 分类ID
     * @return
     */
    public Long querySell(int categoryId) {
        ShardedJedis jedis = null;
        try {
            String key = Constants.REDIS_KEY_PREFIX + Constants.REDIS_KEY_SEPARATOR + "sellNum" +
                    Constants.REDIS_KEY_SEPARATOR + categoryId;
            jedis = jedisPool.getResource();
            return Long.parseLong(jedis.get(key));
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
