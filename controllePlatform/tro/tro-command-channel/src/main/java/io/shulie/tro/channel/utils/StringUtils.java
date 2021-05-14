package io.shulie.tro.channel.utils;

/**
 * @author: Hengyu
 * @className: StringUtils
 * @date: 2021/1/7 4:42 下午
 * @description:
 */
public class StringUtils {

    public static boolean isBlank(String target){
        if (target == null || target.trim().isEmpty()){
            return true;
        }
        return false;
    }
}
