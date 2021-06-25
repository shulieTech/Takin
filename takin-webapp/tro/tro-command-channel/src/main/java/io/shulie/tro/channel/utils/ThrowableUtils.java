package io.shulie.tro.channel.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * 异常工具类
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/9/29 3:47 下午
 */
public class ThrowableUtils {

    public static String toString(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(bos);
        try {
            throwable.printStackTrace(writer);
            try {
                return new String(bos.toByteArray(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return new String(bos.toByteArray());
            }
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
            }
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }
}
