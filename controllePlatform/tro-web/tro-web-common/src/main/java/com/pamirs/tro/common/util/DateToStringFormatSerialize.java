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
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 日期转换成字符串工具类
 *
 * @author shulie
 * @version v1.0
 * @2018年5月16日
 */
public class DateToStringFormatSerialize extends JsonSerializer<Date> {

    //日期格式
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 继承父类的serialize方法
     *
     * @param date        日期
     * @param gen         JsonGenerator gen
     * @param serializers JsonGenerator serializers
     * @author shulie
     * @2018年5月21日
     * @version v1.0
     */
    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
        if (date == null) {
            gen.writeString("");
        } else {
            gen.writeString(DATE_FORMAT.format(date));
        }
    }
}
