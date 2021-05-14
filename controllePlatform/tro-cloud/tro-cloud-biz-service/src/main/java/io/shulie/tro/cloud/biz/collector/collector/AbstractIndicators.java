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

package io.shulie.tro.cloud.biz.collector.collector;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import com.google.common.collect.Lists;
import io.shulie.tro.eventcenter.EventCenterTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import io.shulie.tro.cloud.biz.service.scene.SceneManageService;
import io.shulie.tro.cloud.common.constants.CollectorConstants;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: io.shulie.tro.cloud.poll.poll
 * @Date 2020-04-20 21:08
 */
@Slf4j
public abstract class AbstractIndicators {

    /**
     * 1、判断key是否存在，不存在插入value
     * 2、key存在，比较值大小
     */
    private static final String maxScript =
        "if (redis.call('exists', KEYS[1]) == 0 or redis.call('get', KEYS[1]) < ARGV[1]) then\n" +
            "    redis.call('set', KEYS[1], ARGV[1]);\n" +
            //            "    return 1;\n" +
            "else\n" +
            //            "    return 0;\n" +
            "end";
    private static final String minScript =
        "if (redis.call('exists', KEYS[1]) == 0 or redis.call('get', KEYS[1]) > ARGV[1]) then\n" +
            "    redis.call('set', KEYS[1], ARGV[1]);\n" +
            //            "    return 1;\n" +
            "else\n" +
            //            "    return 0;\n" +
            "end";
    private static final String unlockScript = "if redis.call('exists',KEYS[1]) == 1 then\n" +
        "   redis.call('del',KEYS[1])\n" +
        "else\n" +
        //                    "   return 0\n" +
        "end";
    @Autowired
    protected RedisTemplate redisTemplate;
    @Autowired
    protected EventCenterTemplate eventCenterTemplate;
    @Autowired
    protected SceneManageService sceneManageService;
    private DefaultRedisScript<Void> minRedisScript;
    private DefaultRedisScript<Void> maxRedisScript;
    private DefaultRedisScript<Void> unlockRedisScript;
    private Expiration expiration = Expiration.seconds((int)CollectorConstants.REDIS_KEY_TIMEOUT);

    /**
     * 压测场景强行关闭预留时间 
     */
    @Value("${scene.pressure.forceCloseTime: 20}")
    private Integer forceCloseTime;

    /**
     * 获取Metrics key
     * 示例：COLLECTOR:TASK:102121:213124512312
     *
     * @param sceneId
     * @param reportId
     * @return
     */
    protected String getPressureTaskKey(Long sceneId, Long reportId, Long customerId) {
        // 兼容原始redis key
        if (customerId == null) {
            return String.format("COLLECTOR:TASK:%s:%s", sceneId, reportId);
        }
        return String.format("COLLECTOR:TASK:%s:%s:%S", sceneId, reportId, customerId);
    }

    public boolean lock(String key, String value) {

        return (boolean)redisTemplate.execute((RedisCallback<Boolean>)connection -> {
            Boolean bl = connection.set(getLockPrefix(key).getBytes(), value.getBytes(), expiration,
                RedisStringCommands.SetOption.SET_IF_ABSENT);
            if (null != bl && bl) {
                //connection.expire(key.getBytes(), EXPIREMSECS * 1000);
                return true;
            }
            return false;
        });
    }

    private String getLockPrefix(String key) {
        return String.format("COLLECTOR LOCK:%s", key);
    }

    public void unlock(String key, String value) {
        redisTemplate.execute(unlockRedisScript, Lists.newArrayList(getLockPrefix(key)), value);
    }

    /**
     * 获取Metrics key
     * 示例：COLLECTOR:TASK:102121:213124512312:1587375600000:登录接口
     *
     * @param taskKey
     * @param time
     * @return
     */
    protected String getWindowKey(String taskKey, String transaction, long time) {
        return String.format("%s:%s:%s", taskKey, time, transaction);
    }

    public String getTaskKey(Long sceneId, Long reportId, Long customerId) {
        // 兼容原始redis key
        if (customerId == null) {
            return String.format("%s_%s", sceneId, reportId);
        }
        return String.format("%s_%s_%s", sceneId, reportId, customerId);
    }

    public static String getRedisTpsLimitKey(Long sceneId,Long reportId,Long customerId){
        return String.format("__REDIS_TPS_LIMIT_KEY_%s_%s_%s__", sceneId, reportId, customerId);
    }

    public static String getRedisTpsAllLimitKey(Long sceneId,Long reportId,Long customerId){
        return String.format("__REDIS_TPS_ALL_LIMIT_KEY_%s_%s_%s__", sceneId, reportId, customerId);
    }

    public static String getRedisTpsPodNumKey(Long sceneId,Long reportId,Long customerId){
        return String.format("__REDIS_TPS_POD_NUM_KEY_%s_%s_%s__", sceneId, reportId, customerId);
    }

    /**
     * 获取Metrics 指标key
     * 示例：COLLECTOR:TASK:102121:213124512312:1587375600000:rt
     *
     * @param indicatorsName
     * @return
     */
    protected String getIndicatorsKey(String windowKey, String indicatorsName) {
        return String.format("%s:%s", windowKey, indicatorsName);
    }

    protected String saCountKey(String taskKey, String transaction, long timeWindow) {
        return getIndicatorsKey(getWindowKey(taskKey, transaction, timeWindow), "saCount");
    }

    protected String activeThreadsKey(String taskKey, String transaction, long timeWindow) {
        return getIndicatorsKey(getWindowKey(taskKey, transaction, timeWindow), "activeThreads");
    }

    protected String errorKey(String taskKey, String transaction, long timeWindow) {
        return getIndicatorsKey(getWindowKey(taskKey, transaction, timeWindow), "error");
    }

    /**
     * time 不进行转换
     *
     * @param taskKey
     * @return
     */
    protected String last(String taskKey) {
        return getIndicatorsKey(String.format("%s:%s", taskKey, "last"), "last");
    }

    /**
     * 强行自动标识
     * @param taskKey
     * @return
     */
    protected String forceCloseTime(String taskKey) {
        return getIndicatorsKey(String.format("%s:%s", taskKey,"forceClose"),"force");
    }

    protected String countKey(String taskKey, String transaction, long timeWindow) {
        return getIndicatorsKey(getWindowKey(taskKey, transaction, timeWindow), "count");
    }

    protected String failCountKey(String taskKey, String transaction, long timeWindow) {
        return getIndicatorsKey(getWindowKey(taskKey, transaction, timeWindow), "failCount");
    }

    protected String rtKey(String taskKey, String transaction, long timeWindow) {
        return getIndicatorsKey(getWindowKey(taskKey, transaction, timeWindow), "rt");
    }

    protected String maxRtKey(String taskKey, String transaction, long timeWindow) {
        return getIndicatorsKey(getWindowKey(taskKey, transaction, timeWindow), "maxRt");
    }

    protected String minRtKey(String taskKey, String transaction, long timeWindow) {
        return getIndicatorsKey(getWindowKey(taskKey, transaction, timeWindow), "minRt");
    }

    protected void doubleCumulative(String key, Double value) {
        redisTemplate.opsForValue().increment(key, value);
        setTTL(key);
    }

    protected void setLast(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 强行自动关闭时间
     * @param key
     * @param startTime
     * @param pressureTime 秒
     */
    protected void setForceCloseTime(String key, Long startTime,Long pressureTime) {
        Long forceTime = startTime + pressureTime * 1000 + forceCloseTime*1000;
        redisTemplate.opsForValue().set(key, forceTime);
    }

    protected void longCumulative(String key, Long value) {
        redisTemplate.opsForValue().increment(key, value);
        setTTL(key);
    }

    protected void intCumulative(String key, Integer value) {
        redisTemplate.opsForValue().increment(key, value);
        setTTL(key);
    }

    protected void setError(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        setTTL(key);
    }

    protected void setMax(String key, Long value) {
        if (redisTemplate.hasKey(key)) {
            long temp = getLongValue(key);
            if (value.longValue() < temp) {
                redisTemplate.opsForValue().set(key, value);
            }
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    protected void setMin(String key, Long value) {
        if (redisTemplate.hasKey(key)) {
            long temp = getLongValue(key);
            if (value.longValue() > temp) {
                redisTemplate.opsForValue().set(key, value);
            }
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    protected void mostValue(String key, Double value, Integer type) {
        if (0 == type) {
            redisTemplate.execute(maxRedisScript, Lists.newArrayList(key), value);
        } else if (1 == type) {
            redisTemplate.execute(minRedisScript, Lists.newArrayList(key), value);
        }
        setTTL(key);
    }

    private void setTTL(String key) {
        redisTemplate.expire(key, CollectorConstants.REDIS_KEY_TIMEOUT, TimeUnit.SECONDS);
    }

    protected Integer getIntValue(String key) {
        Object object = redisTemplate.opsForValue().get(key);
        if (null != object) {
            return (int)object;
        }
        return null;
    }

    protected Long getLongValue(String key) {
        Object object = redisTemplate.opsForValue().get(key);
        if (null != object) {
            return (long)object;
        }
        return null;
    }

    protected Double getDoubleValue(String key) {
        Object object = redisTemplate.opsForValue().get(key);
        if (null != object) {
            return Double.valueOf(String.valueOf(object));
        }
        return null;
    }

    @PostConstruct
    public void init() {
        minRedisScript = new DefaultRedisScript<Void>();
        minRedisScript.setResultType(Void.class);
        minRedisScript.setScriptText(minScript);

        maxRedisScript = new DefaultRedisScript<Void>();
        maxRedisScript.setResultType(Void.class);
        maxRedisScript.setScriptText(maxScript);

        unlockRedisScript = new DefaultRedisScript<Void>();
        unlockRedisScript.setResultType(Void.class);
        unlockRedisScript.setScriptText(unlockScript);

    }
}
