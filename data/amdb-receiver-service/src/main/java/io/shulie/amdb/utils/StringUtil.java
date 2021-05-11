package io.shulie.amdb.utils;

/**
 * @Author: xingchen
 * @ClassName: StringUtil
 * @Package: io.shulie.amdb.utils
 * @Date: 2021/3/2310:19
 * @Description:
 */
public class StringUtil {
    public static boolean isNotBlank(final String str) {
        return !isBlank(str);
    }

    public static boolean isBlank(final String str) {
        if (str == null || "null".equals(str) || "".equals(str)) {
            return true;
        }
        return false;
    }
}
