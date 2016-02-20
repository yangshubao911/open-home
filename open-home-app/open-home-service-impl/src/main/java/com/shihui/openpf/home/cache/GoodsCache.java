package com.shihui.openpf.home.cache;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import com.shihui.openpf.common.util.Constants;
import javax.annotation.Resource;

/**
 * Created by zhoutc on 2016/2/20.
 */
public class GoodsCache {

    @Resource
    private ShardedJedisPool jedisPool;

    /**
     * 增加商品分类销售数量
     * @param categoryId  分类ID

     * @return
     */
    public Long increaseSell(int categoryId){
        String key = Constants.REDIS_KEY_PREFIX + Constants.REDIS_KEY_SEPARATOR + "sellNum" +
                 Constants.REDIS_KEY_SEPARATOR + categoryId;
        ShardedJedis jedis = jedisPool.getResource();
        return jedis.incr(key);
    }

    /**
     * 查询商品分类销售数量
     * @param categoryId  分类ID

     * @return
     */
    public Long querySell(int categoryId){
        String key = Constants.REDIS_KEY_PREFIX + Constants.REDIS_KEY_SEPARATOR + "sellNum" +
                Constants.REDIS_KEY_SEPARATOR + categoryId;
        ShardedJedis jedis = jedisPool.getResource();
        return Long.parseLong(jedis.get(key));
    }

}
