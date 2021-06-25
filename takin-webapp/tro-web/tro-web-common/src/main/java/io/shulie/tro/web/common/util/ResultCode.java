package io.shulie.tro.web.common.util;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.common.util
 * @date 2021/6/8 8:25 下午
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/8 2:22 下午
 */
public class ResultCode {
    /**
     * 返回成功
     */
    static public final String RPC_RESULT_SUCCESS = "00";

    /**
     * 返回失败，一般是业务失败
     */
    static public final String RPC_RESULT_FAILED = "01";

    /**
     * 返回DUBBO错误
     */
    static public final String RPC_RESULT_DUBBO_ERR = "02";

    /**
     * 返回超时错误
     */
    static public final String RPC_RESULT_TIMEOUT = "03";

    /**
     * 未知
     */
    static public final String RPC_RESULT_UNKNOWN = "04";

    /**
     * 判断结果码是否是成功
     *
     * @param resultCode
     * @return
     */
    public static boolean isOk(String resultCode) {
        if (StringUtils.equals(resultCode, RPC_RESULT_SUCCESS)) {
            return true;
        }
        if (NumberUtils.isDigits(resultCode)) {
            Integer status = Integer.valueOf(resultCode);
            if (status >= 200 && status < 400) {
                return true;
            }
        }
        return false;
    }
}
