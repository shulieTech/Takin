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

package io.shulie.tro.cloud.biz.jmeter;

import java.util.List;

import com.pamirs.tro.entity.domain.vo.scenemanage.ScriptUrlVO;
import org.dom4j.Document;

/**
 * @author HengYu
 * @className JmxParse
 * @date 2021/4/12 4:02 下午
 * @description
 */
public abstract class JmxParser {

    /**
     * 获取Jmeter 脚本请求入口
     * @param document JMX对应文档对象
     * @param content 脚本原文
     * @param ptSize pt 数量对象
     * @return
     */
    abstract List<ScriptUrlVO> getEntryContent(Document document, String content, Integer ptSize);
}
