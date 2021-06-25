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

package io.shulie.tro.web.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * @Author: mubai
 * @Date: 2020-05-13 11:49
 * @Description:
 */
public class LicenceUtils {

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String generateLicense(String userAppKey, String expireTime) {
        Map<String, Object> licenseMap = new HashMap<>();
        licenseMap.put("key", userAppKey);
        licenseMap.put("expireTime", expireTime);
        return AESCoder.encrypt(JSON.toJSONString(licenseMap), AESCoder.KEY_PRADAR_PARAM);
    }

    public static Map<String, Object> analysisLicense(String license) {
        String data = AESCoder.decrypt(license, AESCoder.KEY_PRADAR_PARAM);
        return JSON.parseObject(data, Map.class);
    }

    public static void main(String[] args) {
        String s = generateLicense("5b06060a-17cb-4588-bb71-edd7f65035af", "2021-05-11 11:11:11");
        System.out.println("license : " + s);
        Map<String, Object> map = analysisLicense(s);
        System.out.println(map.toString());
    }

    /**
     * 说明：转换时间成date
     *
     * @param str
     * @return
     * @author shulie
     * @time：2017年10月12日 下午7:37:32
     */
    public static Date transferTime(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
