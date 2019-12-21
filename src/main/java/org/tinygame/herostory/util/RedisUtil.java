package org.tinygame.herostory.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/20 20:31
 */
public final class RedisUtil {
  private RedisUtil() {
  }

  private static JedisPool jedisPool;

  public static void init() {
    jedisPool = new JedisPool("106.12.29.140", 6379);
  }

  public static Jedis getJedis() {
    if(jedisPool==null) {
      throw new RuntimeException("jedisPool未初始化");
    }

    return jedisPool.getResource();
    // 如果 redis 设置了密码，需要校验
    // jedis.auth("xxx");
  }
}
