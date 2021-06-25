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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Date;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

/**
 * The <tt>HmacSha1Signature</tt> shows how to calculate a message
 * authentication code using HMAC-SHA1 algorithm.
 *
 * <pre>
 *
 * % java -version
 * java version "1.6.0_11"
 * % javac HmacSha1Signature.java
 * % java -ea HmacSha1Signature
 * 104152c5bfdca07bc633eebd46199f0255c9f49d
 * </pre>
 */
public class HmacSha1Signature {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static String toHexString(byte[] bytes) {
        try (Formatter formatter = new Formatter()) {
            for (byte b : bytes) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }

    public static String calculateRFC2104HMAC(String data, String key)
        throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        return toHexString(mac.doFinal(data.getBytes()));
    }

    //	public static void main(String[] args) throws Exception {
    //		String hmac = calculateRFC2104HMAC("data", "78951f25d9d58aef1ed96b23");
    //		System.out.println(hmac);
    //		assert hmac.equals("104152c5bfdca07bc633eebd46199f0255c9f49d");

    //		Date sDate = new Date(new Date().getTime()-( 1000 * 24 * 60 * 60));
    //
    //
    //		Date eDate = new Date();
    //
    //		String result =  getDateMinute( eDate,  sDate);
    //		System.out.println(result);
    //	}

    public static String getDateMinute(Date endDate, Date nowDate) {

        long interval = (endDate.getTime() - nowDate.getTime()) / (1000 * 60);
        return StringUtils.substringBeforeLast(interval + "", ".");
    }

    public static String getDateMinuteModified(Date endDate, Date nowDate) {
        String data = getDateMinute(endDate, nowDate);
        return "-" + data + "m";
    }
}
