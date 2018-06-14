package com.service;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author: Jaime
 * @Date: 2018/5/31 1:44
 * @Description: *
 */
public interface RedisService {
     void hset(String key, String field, Object obj, int extime);
     <T> T hget(String key, String field);
     boolean hexists(String key, String field);
     long hsize(String key);
     void hdel(String key, String field);
     void setRedisTemplate(RedisTemplate redisTemplate);
     RedisTemplate getRedisTemplate();
}
