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

package io.shulie.tro.k8s.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import io.fabric8.kubernetes.api.model.Node;
import io.shulie.tro.cloud.common.constants.EnginePackVersionConstants;
import io.shulie.tro.cloud.common.constants.PressureInstanceRedisKey;
import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import io.shulie.tro.cloud.common.utils.FileUtils;
import io.shulie.tro.k8s.service.MicroDeployService;
import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.utils.linux.LinuxHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author 无涯
 * @Package io.shulie.tro.k8s.service.impl
 * @date 2021/5/6 4:12 下午
 */
@Service
@Slf4j
public class LocalThreadServiceImpl implements MicroDeployService {

    @Autowired
    private RedisClientUtils redisClientUtils;

    @Autowired
    @Qualifier("enginePodThreadPoolExecutor")
    private ThreadPoolExecutor threadPoolExecutor;

    private AtomicReference<Process> shellProcess = new AtomicReference<>();

    /**
     * 压测引擎包路径
     */
    @Value("${pressure.engine.install.dir:/Users/hezhongqi/shulie/engine/pressure-engine.tar.gz}")
    private String installDir;
    /**
     * 调度任务路径
     */
    @Value("${pressure.engine.task.dir:/Users/hezhongqi/shulie/engine}")
    private String taskDir;


    @Override
    public String createJob(Object job, String engineRedisKey) {
        if(!new File(installDir).exists()) {
            return "未找到引擎包";
        }
        try {
            String md5 = DigestUtils.md5Hex(new FileInputStream(installDir));
            // 检测引擎包是否符合md5值中
            Object version = redisClientUtils.hmget(EnginePackVersionConstants.ENGINE_PACK_VERSION_REDIS_KEY,md5);
            if(version == null) {
                return "未找到支持版本的引擎包";
            }
            log.info("本次压测使用引擎包版本：{}",version);
        } catch (IOException e) {
            return e.getLocalizedMessage();
        }
        // 解压引擎包 ---> 目录结构 /pressure-engine/pressure-engine/bin
        String enginePackDir = getEnginePackDir();
        FileUtils.tarGzFileToFile(installDir,enginePackDir);
        String enginePackBin = getEnginePackBin(enginePackDir);
        if (StringUtils.isBlank(enginePackBin)) {
            return "未找到引擎包的解压包";
        }
        // todo 目前只支持单机模式
        threadPoolExecutor.execute(() -> {
            StringBuilder sb = new StringBuilder();
            sb.append("cd ").append(enginePackBin).append("/bin");
            sb.append(" && ");
            sb.append("sh start.sh -t \"jmeter\" -c");
            sb.append(" \"").append(taskDir).append("/").append(job).append(" \"");
            sb.append(" -f y ");
            log.info("执行压测包，执行命令如下:{}",sb);
            int state = LinuxHelper.runShell(sb.toString(), 60L,
                process -> shellProcess.set(process),
                message -> {
                    log.info("执行返回结果:{}", message);
                }
            );
            System.out.println(state);
        });
        // 异步
        return "";
    }

    private String getEnginePackBin(String enginePackDir) {
        // 查看 /pressure-engine 下的目录
        //new File(fileDir).listFiles()[1].getPath()
        File[] files = new File(enginePackDir).listFiles();
        if(files == null || files.length == 0) {
            return null;
        }
        File enginePackFile = Arrays.stream(files).filter(f -> !f.isHidden()).collect(Collectors.toList()).get(0);
        return enginePackFile.getPath();
    }

    private String getEnginePackDir() {
        String enginePackDir = new File(installDir).getParent() + "/pressure-engine";
        return enginePackDir;
    }

    @Override
    public void deleteJob(String jobName, String engineRedisKey) {
        // 删除解压包
        String enginePackDir = getEnginePackDir();
        FileUtils.deleteDirectory(enginePackDir);
    }

    @Override
    public void createConfigMap(Object configMap, String engineInstanceRedisKey) {
        Map<String, Object> map = (Map<String, Object>)configMap;
        String fileName = (String) map.get("name");
        FileUtils.writeTextFile(JsonHelper.obj2StringPretty(map.get("engine.conf")),taskDir + "/" + fileName);
        redisClientUtils.hmset(engineInstanceRedisKey, PressureInstanceRedisKey.SecondRedisKey.CONFIG_NAME,fileName);
    }

    @Override
    public String getType() {
        return "localThread";
    }

    @Override
    public void deleteConfigMap(String configMapName) {
        //todo /bak中
        String sourceFile = taskDir + "/" + configMapName;
        String targetFile = taskDir + "/bak/" + configMapName;
        if(new File(sourceFile).exists()) {
            FileUtils.writeTextFile(FileUtils.readTextFileContent(new File(sourceFile)),targetFile);
        }
    }

    @Override
    public List<Node> getNodeList() {
        return null;
    }
}
