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

package io.shulie.tro.web.common.vo.fastdebug;

import java.util.Map;

import com.google.common.collect.Maps;
import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.common.vo.fastdebug
 * @date 2020/12/28 11:11 上午
 */
@Data
public class ContentTypeVO {
    public static Integer X_WWW_FORM_URLENCODED = 0;
    public static Integer RAW = 1;
    public static String UTF_8 = "utf-8";
    public static String GBK = "gbk";
    public static Map<String,String> map = Maps.newHashMap();
    /**
     *  x-www-form-urlencoded:0, raw:1;
     */
    private Integer radio;
    /**
     * 请求类型
     */
    private String type;
    /**
     * 编码格式
     */
    private String codingFormat;

}
