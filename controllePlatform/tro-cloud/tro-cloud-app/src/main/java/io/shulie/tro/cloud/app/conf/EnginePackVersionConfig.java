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

package io.shulie.tro.cloud.app.conf;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import io.shulie.tro.cloud.common.bean.engine.EngineVersionConfigBean;
import io.shulie.tro.cloud.common.constants.EnginePackVersionConstants;
import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import io.shulie.tro.cloud.common.utils.FileUtils;
import io.shulie.tro.utils.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 无涯
 * @Package io.shulie.tro.cloud.app.conf
 * @date 2021/5/10 4:58 下午
 */
@Component
@Slf4j
public class EnginePackVersionConfig {

    @Value("${engine.pack.version.path:/data/apps/tro-cloud/engine_pack_version.json}")
    private String enginePackVersionConfigJson;
    /**
     * 压测引擎包路径
     */
    @Value("${pressure.engine.install.dir:/Users/hezhongqi/shulie/engine/pressure-engine.tar.gz}")
    private String installDir;

    @Autowired
    private RedisClientUtils redisClientUtils;
    @PostConstruct
    public void init() {
        // 存到redis
        try {
            List<EngineVersionConfigBean> beans = JsonHelper.json2List(FileUtils.readTextFileContent(new File(enginePackVersionConfigJson)),EngineVersionConfigBean.class);

            Map<String,Object> configMap = beans.stream().collect(Collectors.toMap(EngineVersionConfigBean::getMd5,EngineVersionConfigBean::getVersion));
            redisClientUtils.hmset(EnginePackVersionConstants.ENGINE_PACK_VERSION_REDIS_KEY,configMap);
            // 引擎包校验 校验位置：启动项目校验 + 启动压测校验
            if(!new File(installDir).exists()) {
                log.error("未找到引擎包");
                return;
            }
            String md5 = DigestUtils.md5Hex(new FileInputStream(installDir));
            // 检测引擎包是否符合md5值中
            Object version = redisClientUtils.hmget(EnginePackVersionConstants.ENGINE_PACK_VERSION_REDIS_KEY,md5);
            if(version == null) {
                log.error("未找到支持版本的引擎包");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
