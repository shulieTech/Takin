/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.cloud.common.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @Author: 慕白
 * @Date: 2019-04-17 9:24
 * redis 工具类
 */
@Component
public class RedisClientUtils {

    /**
     * lock 超时时间
     */
    private static final int EXPIREMSECS = 30;
    private static final String unlockScript = "if redis.call('exists',KEYS[1]) == 1 then\n" +
        "   redis.call('del',KEYS[1])\n" +
        "else\n" +
        //                    "   return 0\n" +
        "end";
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    private DefaultRedisScript<Void> unlockRedisScript;
    private Expiration expiration = Expiration.seconds(EXPIREMSECS);

    @PostConstruct
    public void init() {
        unlockRedisScript = new DefaultRedisScript<Void>();
        unlockRedisScript.setResultType(Void.class);
        unlockRedisScript.setScriptText(unlockScript);
    }

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void setString(final String key, final String value,
        final int expire, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, expire, timeUnit);
    }

    public void setString(final String key, final String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public String getString(final String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void leftPushAll(final String key, final List<String> numList) {
        stringRedisTemplate.opsForList().leftPushAll(key, numList);
    }

    public Object getObject(final String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void delete(final String key) {
        stringRedisTemplate.delete(key);
    }

    public boolean lock(String key, String value) {

        return (boolean)redisTemplate.execute((RedisCallback<Boolean>)connection -> {
            Boolean bl = connection.set(getLockPrefix(key).getBytes(), value.getBytes(), expiration,
                RedisStringCommands.SetOption.SET_IF_ABSENT);
            if (null != bl && bl) {
                return true;
            }
            return false;
        });
    }

    private String getLockPrefix(String key) {
        return String.format("LOCK:%s", key);
    }

    public void unlock(String key, String value) {
        redisTemplate.execute(unlockRedisScript, Lists.newArrayList(getLockPrefix(key)), value);
    }

    public Long increment(final String key, final long l) {
        stringRedisTemplate.expire(key, 24, TimeUnit.HOURS);
        return stringRedisTemplate.opsForValue().increment(key, l);
    }

    /**
     * 获取并自增l个，key不过期
     *
     * @param key
     * @param l
     * @return
     */
    public Long incrementAndNotExpire(final String key, final long l) {
        return stringRedisTemplate.opsForValue().increment(key, l);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key, int indexdb) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value, int indexdb) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            //不影响其他的使用，用一个临时的template
            StringRedisTemplate redisTemplate = stringRedisTemplate;
            redisTemplate.setHashValueSerializer(RedisSerializer.json());
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hmset(String key, String field, Object value) {
        try {
            stringRedisTemplate.opsForHash().put(key, field, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            stringRedisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public Object hmget(String key, String field) {
        return stringRedisTemplate.opsForHash().get(key, field);
    }

    public List<String> hmget(String key, List<String> fieldList) {
        return redisTemplate.opsForHash().multiGet(key, fieldList);
    }

    public void hmdelete(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    /**------------------zSet相关操作--------------------------------*/

    /**
     * 添加元素,有序集合是按照元素的score值由小到大排列
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Boolean zAdd(String key, String value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * @param key
     * @param values
     * @return
     */
    public Long zAdd(String key, Set<ZSetOperations.TypedTuple<String>> values) {
        return redisTemplate.opsForZSet().add(key, values);
    }

    /**
     * @param key
     * @param values
     * @return
     */
    public Long zRemove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 增加元素的score值，并返回增加后的值
     *
     * @param key
     * @param value
     * @param delta
     * @return
     */
    public Double zIncrementScore(String key, String value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    //    /**
    //     * 获取集合的元素, 从小到大排序
    //     *
    //     * @param key
    //     * @param start 开始位置
    //     * @param end   结束位置, -1查询所有
    //     * @return
    //     */
    //    public Set<String> zRange(String key, long start, long end) {
    //        return redisTemplate.opsForZSet().range(key, start, end);
    //    }
    //
    //    /**
    //     * 获取集合元素, 从小到大排序, 并且把score值也获取
    //     *
    //     * @param key
    //     * @param start
    //     * @param end
    //     * @return
    //     */
    //    public Set<ZSetOperations.TypedTuple<String>> zRangeWithScores(String key, long start,
    //                                                                   long end) {
    //        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    //    }

    /**
     * 获取集合的元素, 从大到小排序
     *
     * @param key
     * @param start
     * @param end   ["num1","num2","num3"]
     * @return
     */
    public Set<String> zReverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 获取集合的元素, 从大到小排序, 并返回score值
     *
     * @param key
     * @param start
     * @param end   [{"score":100,"value":"num1"},{"score":99,"value":"num2"},{"score":98,"value":"num3"}]
     * @return
     */
    public Set<ZSetOperations.TypedTuple<String>> zReverseRangeWithScores(String key,
        long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start,
            end);
    }

    /**
     * list 右边插入
     * @param key
     * @param value
     */
    public void rightPush(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public List<String> rangeValue(String key, long begin, long end) {
        return redisTemplate.opsForList().range(key, begin, end);
    }

    public Long setSetValue(String key, String value) {
        return redisTemplate.opsForSet().add(key, value);
    }

    public Long getSetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public void removeSetValue(String key, String value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    public void trimList(String key, long begin, long end) {
        redisTemplate.opsForList().trim(key, begin, end);
    }

    public Long zsetAdd(String key, String value) {
        return redisTemplate.opsForSet().add(key, value);
    }
}
