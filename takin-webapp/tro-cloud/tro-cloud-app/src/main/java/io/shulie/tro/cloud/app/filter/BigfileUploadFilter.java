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

package io.shulie.tro.cloud.app.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Preconditions;
import com.pamirs.tro.entity.domain.vo.cloudserver.BigFileUploadVO;
import io.shulie.tro.common.beans.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.shulie.tro.cloud.biz.service.cloudServer.BigFileService;
import io.shulie.tro.cloud.common.utils.HttpUtils;

/**
 * @Author: mubai
 * @Date: 2020-05-26 13:48
 * @Description:
 */

@Component
public class BigfileUploadFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(BigfileUploadFilter.class);
    @Autowired(required = false)
    private BigFileService bigFileService;

    /**
     * inputStream转换为byte字节数组
     *
     * @param inStream
     * @return
     * @throws IOException
     */
    public static final byte[] input2byte(InputStream inStream, int byteLength) throws IOException {
        byte[] bytes = toByteArray(inStream, byteLength);
        return bytes;
    }

    public static byte[] toByteArray(InputStream in, int byteLength) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(6 * 1024 * 1024);
        long start = System.currentTimeMillis();
        copy((InputStream)in, (OutputStream)out, byteLength);
        logger.info("copy cost : " + (System.currentTimeMillis() - start) + " ms");
        return out.toByteArray();
    }

    public static long copy(InputStream from, OutputStream to, int byteLength) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        byte[] buf = new byte[1 * 1024 * 1024];
        long total = 0L;
        while (true) {
            int r = from.read(buf);
            if (r == -1) {
                return total;
            }
            to.write(buf, 0, r);
            total += (long)r;
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String requestURI = request.getRequestURI();
        if ("/tro-web/api/bigfile/upload".equals(requestURI)) {
            ResponseResult result = null;

            String license = request.getHeader("license");
            String sceneId = request.getHeader("sceneId");
            String fileName = request.getHeader("fileName");
            String startPos = request.getHeader("startPos");
            String byteLength = request.getHeader("byteLength");
            if (license == null || sceneId == null || fileName == null) {
                result = ResponseResult.fail("license | sceneId | fileName can not be null", "");
                HttpUtils.writeResponse(response, "0", result, null);
            }

            if (byteLength == null) {
                byteLength = "0";
            }
            ServletInputStream inputStream = request.getInputStream();
            BigFileUploadVO param = new BigFileUploadVO();
            param.setByteData(input2byte(inputStream, Integer.valueOf(byteLength)));
            param.setFileName(fileName);
            param.setLicense(license);
            param.setSceneId(Long.valueOf(sceneId));
            if (startPos != null) {
                param.setStartPos(Long.valueOf(startPos));
            }
            logger.info("filter receive data ....");
            result = bigFileService.upload(license, param);
            logger.info("result :{}", result.getSuccess());
            HttpUtils.writeResponse(response, "0", result, inputStream);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
