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

package com.pamirs.tro.common.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

/**
 * long型转换成字符串序列化类
 *
 * @author shulie
 * @version v1.0
 * @2018年5月21日
 */
public class LongToStringFormatSerialize extends JsonSerializer<Long> {

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s 字符串
     * @return
     * @author shulie
     * @2018年5月21日
     * @version v1.0
     */
    public String subZeroAndDot(String s) {
        if (StringUtils.contains(s, ".")) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 继承父类的serialize方法
     *
     * @param value       字符串value
     * @param gen         JsonGenerator gen
     * @param serializers JsonGenerator serializers
     * @author shulie
     * @2018年5月21日
     * @version v1.0
     */
    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException, JsonProcessingException {
        gen.writeString(subZeroAndDot(value.toString()));
    }

}
