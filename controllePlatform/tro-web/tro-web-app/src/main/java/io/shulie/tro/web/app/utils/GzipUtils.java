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

package io.shulie.tro.web.app.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @Author: 710524
 * @ClassName: GzipUtils
 * @Package: com.monitor.platform.util
 * @Date: 2019年 06月18日 08:36
 * @Description: 压缩解压工具
 */
public class GzipUtils {

    public static byte[] compress(String str) throws IOException {
        if (str != null && str.length() > 0) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                 GZIPOutputStream gzip = new GZIPOutputStream(out)) {
                gzip.write(str.getBytes());
                gzip.finish();
                return out.toByteArray();
            }
        }
        return null;
    }

    // 解压缩
    public static byte[] uncompress(InputStream is) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPInputStream gunzip = new GZIPInputStream(is);) {
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        }
    }

    public static void main(String[] args) {
        String s = "";
        //        String sss = CommonFileDlpUtils.getDecryptFile(s, "_D");

        //        System.out.println(sss);
    }
}
