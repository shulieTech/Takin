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
