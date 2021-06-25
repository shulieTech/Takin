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

package io.shulie.tro.cloud.common.utils;

import java.nio.charset.Charset;
import java.security.Key;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * @Author: 慕白
 * @Date: 2019-11-11 09:28
 * @Description:
 */
public abstract class AESCoder {

    public static final String KEY_PRADAR_PARAM = "KwBHRgqFEygN1VZC2TR7Qw==";

    // 密钥算法
    private static final String KEY_ALGORITHM = "AES";
    //加解密算法/工作模式/填充方式,在128位--16字节下兼容C#的PKCS7Padding
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    //将Cipher以密钥做键存放起来
    private static Map<String, Cipher> enCipherMap =
        new ConcurrentHashMap<String, Cipher>();

    private static Map<String, Cipher> deCipherMap =
        new ConcurrentHashMap<String, Cipher>();

    private static Cipher initCipher(String key, int mode) throws Exception {
        // 实例化Cipher对象，它用于完成实际的加密操作
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        Key k = new SecretKeySpec(Base64.decodeBase64(key.getBytes()), KEY_ALGORITHM);
        // 加密模式
        cipher.init(mode, k);
        return cipher;
    }

    private static Cipher getEnCipher(String key) throws Exception {
        if (enCipherMap.containsKey(key)) {
            return enCipherMap.get(key);
        }
        Cipher cipher = initCipher(key, Cipher.ENCRYPT_MODE);
        enCipherMap.put(key, cipher);
        return cipher;
    }

    private static Cipher getDeCipher(String key) throws Exception {
        if (deCipherMap.containsKey(key)) {
            return deCipherMap.get(key);
        }
        Cipher cipher = initCipher(key, Cipher.DECRYPT_MODE);
        deCipherMap.put(key, cipher);
        return cipher;
    }

    /**
     * 加密数据
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return 加密后的数据
     */
    public static String encrypt(String data, String key) {
        try {
            //执行加密操作。加密后的结果通常都会用Base64编码进行传输
            return new String(Base64.encodeBase64(getEnCipher(key)
                .doFinal(data.getBytes(Charset.forName("UTF-8")))), Charset.forName("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("加密失败，原数据：" + data, e);
        }
    }

    /**
     * 解密数据
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return 解密后的数据
     */
    public static String decrypt(String data, String key) {
        try {
            //执行解密操作
            return new String(getDeCipher(key)
                .doFinal(Base64.decodeBase64(data.getBytes())), Charset.forName("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("解密失败，原数据：" + data, e);
        }
    }

    public static void main(String[] args) {
        String data = "";
        String encrypt = encrypt(data, AESCoder.KEY_PRADAR_PARAM);

        System.out.println(encrypt);

        String decrypt = decrypt(encrypt, AESCoder.KEY_PRADAR_PARAM);

        System.out.println(decrypt);

    }

}
