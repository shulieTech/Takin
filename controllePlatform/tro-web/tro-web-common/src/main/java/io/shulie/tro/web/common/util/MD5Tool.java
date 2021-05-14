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

package io.shulie.tro.web.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class MD5Tool {
    public static void main(String[] args) throws Exception {
        /*--------------字符串--------------*/
        String str = "12345";
        String md1 = getMD5(str);
        System.out.println(md1);
    }

    /**
     * 逻辑:
     *
     * 1.获取md5对象,通过"信息摘要"获取实例构造("MD5").
     * 2.md5对象对("字符串的"字节形式"-得到的数组)进行摘要",那么会返回一个"摘要的字节数组"
     * 3.摘要字节数组中的"每个二进制值"字节形式,"转成十六进制形式",然后再把这些值给拼接起来,就是MD5值了
     * (PS:为了便于阅读,把多余的fff去掉,并且单个字符前加个0)
     */
    public static String getMD5(String str) throws Exception {

        String MD5 = "";

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = str.getBytes();
        byte[] digest = md5.digest(bytes);

        for (int i = 0; i < digest.length; i++) {
            //摘要字节数组中各个字节的"十六进制"形式.
            int j = digest[i];
            j = j & 0x000000ff;
            String s1 = Integer.toHexString(j);

            if (s1.length() == 1) {
                s1 = "0" + s1;
            }
            MD5 += s1;
        }
        return MD5;
    }
    //重载,所以用户传入"字符串"或者"文件"都可以解决.

    /**
     * 处理逻辑:
     * 1.现在传入的是"文件",不是字符串
     * 2.所以信息摘要对象.进行摘要得到数组不能像上面获得:md5.digest(bytes),因为不是str.getBytes得到bytes
     * 3.其实还是通过mdt.digest();获取到字节数组,但是前期必须要有一个方法必须是md5.update(),即"信息摘要对象"要先更新
     * 4."信息摘要更新"里面有(byte[] input),说明是读取流获取到的数组,所以我们就用这个方法.
     * 5.所以最终的逻辑就是:
     *
     * 1.获取文件的读取流
     * 2.不停的读取流中的"内容"放入字符串,放一部分就"更新"一部分.直到全部完毕
     * 3.然后调用md5.digest();就会得到有内容的字节数组,剩下的就和上边一样了.
     */
    public static String getMD5(File file) throws Exception {
        String MD5 = "";

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(file);

        byte[] bytes = new byte[1024 * 5];

        int len = -1;
        while ((len = fis.read(bytes)) != -1) {
            //一部分一部分更新
            md5.update(bytes, 0, len);
        }
        byte[] digest = md5.digest();
        for (int i = 0; i < digest.length; i++) {
            int n = digest[i] & 0x000000ff;
            String s = Integer.toHexString(n);

            MD5 += s;
        }
        return MD5;
    }
}
