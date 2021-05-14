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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 说明: 菜鸟阿斯旺/Prada接口请求参数封装
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2019/3/4 11:23
 * @see RequestPradaUtilTest
 */
public class RequestPradarUtil {

    // pradar同步mysql接口数据url
    final public static String PRADA_SYNCHRONIZED_MYSQL_URL = "/AppUrlInfo";
    // pradar获取appname地址
    final public static String PRADA_APPNAME_URL = "/pradarapp/source/queryAppNames.do";
    // pradar 获取 影子表配置
    final public static String PRADAR_GET_SHADOWCONFIG_URL = "/PamirsDatabaseInfo";
    //阿斯旺APPKEY
    final private static String APPKEY_SPT = "sto-full-link";
    //阿斯旺SECRETKEY
    final private static String SECRETKEY_SPT = "123456";
    //prada APPKEY
    final private static String APPKEY_PRADA = "pradar";
    //prada SECRETKEY
    final private static String SECRETKEY_PRADA = "pradar-tro";

    /**
     * 初始化SPT请求参数
     *
     * @return 包含请求token的map集合
     * @author shulie
     * @2018年5月21日
     * @version v1.0
     */
    public static Map<String, Object> initSPT() {
        Map<String, Object> parameters = new HashMap<>(10);
        String timestamp = "" + System.currentTimeMillis();
        String token = MD5Util.getMD5(APPKEY_SPT + SECRETKEY_SPT + timestamp);
        parameters.put("appKey", APPKEY_SPT);
        parameters.put("token", token);
        parameters.put("timestamp", timestamp);
        return parameters;
    }

    /**
     * 说明: 初始化prada请求参数
     *
     * @return 包含请求token的map集合
     * @author shulie
     * @date 2019/3/4 11:16
     */
    public static Map<String, Object> initPrada() {
        Map<String, Object> parameters = new HashMap<>(10);
        String timestamp = "" + System.currentTimeMillis();
        String token = MD5Util.getMD5("python_tro" + "python" + timestamp);
        parameters.put("appKey", "python_tro");
        parameters.put("token", token);
        parameters.put("timestamp", timestamp);
        return parameters;
    }

    /**
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        return date.getTime();
    }
}
