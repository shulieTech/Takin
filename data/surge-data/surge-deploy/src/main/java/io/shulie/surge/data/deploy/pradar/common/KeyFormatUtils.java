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

package io.shulie.surge.data.deploy.pradar.common;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 */
public class KeyFormatUtils {

    public static String keyEncode(String entrance, String timeStr) {
        return new String(Base64.encodeBase64(DigestUtils.md5(entrance))) + "~|" + timeStr;
    }

    public static String keyEncodeName(String appName) {
        return new String(Base64.encodeBase64(DigestUtils.md5(appName)));
    }
}
