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

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author: mubai
 * @Date: 2021-01-11 20:49
 * @Description:
 */
public class ResponseHeaderUtils {

    //原本调用方式，需要将token放入header中
    public static final String PAGE_TOTAL_HEADER = "x-total-count";

    public static void setTotalCount(Long totalCount){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setHeader("Access-Control-Expose-Headers", PAGE_TOTAL_HEADER);
        if (totalCount !=null){
            response.setHeader(PAGE_TOTAL_HEADER, totalCount + "");
        }else {
            response.setHeader(PAGE_TOTAL_HEADER, 0 + "");
        }

    }
}
