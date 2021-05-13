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

package io.shulie.tro.cloud.open.api.impl.util;

import io.shulie.tro.cloud.open.constant.CloudApiConstant;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.tro.properties.TroCloudClientProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * url 业务工具类
 * 提供各个模块的 url 前缀
 *
 * @author liuchuan
 * @date 2021/4/25 10:22 上午
 */
@Component
public class UrlBusinessUtil implements ApplicationContextAware {

    @Autowired
    private TroCloudClientProperties troCloudClientProperties;

    /**
     * troCloud 属性配置, 静态属性
     */
    private static TroCloudClientProperties tccp;

    /**
     * 场景管理更新脚本文件 url
     *
     * @return 场景管理更新脚本文件 url
     */
    public static String getSceneMangeUpdateFileUrl() {
        return tccp.getUrl() + CloudApiConstant.SCENE_MANAGE_UPDATE_FILE_URL;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        tccp = troCloudClientProperties;
    }

}
