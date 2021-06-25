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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import io.shulie.tro.common.beans.response.ResponseResult;

/**
 * @Author: mubai
 * @Date: 2020-05-26 10:35
 * @Description:
 */
public class HttpUtils {
    public static void writeResponse(HttpServletResponse response, String code, ResponseResult result,
        InputStream inputStream) throws IOException {
        PrintWriter writer = null;
        try {
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("type", "opaqueredirect");
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            writer = response.getWriter();
            writer.write(JSON.toJSONString(result));
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }
}
