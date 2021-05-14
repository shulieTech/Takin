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

package com.pamirs.tro.common.ump.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

/**
 * @description md5加密+base64编码 转换工具类
 */
public class MD5Encrypt {

    private static final String ALGORITHM = "MD5";

    /**
     * encrypt  md5加密+base64编码<br/>
     */
    public static String encrypt(String param) {
        MessageDigest digest = null;
        String result = null;
        if (param == null) {
            return null;
        }
        try {
            digest = MessageDigest.getInstance(ALGORITHM);
            Base64 base64 = new Base64();
            result = base64.encodeToString(digest.digest(param.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
