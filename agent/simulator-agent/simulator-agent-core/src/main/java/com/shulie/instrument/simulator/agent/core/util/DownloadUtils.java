/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shulie.instrument.simulator.agent.core.util;

import com.shulie.instrument.simulator.agent.core.exception.AgentDownloadException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 下载工具
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/17 9:35 下午
 */
public class DownloadUtils {
    private final static Logger logger = LoggerFactory.getLogger(DownloadUtils.class);
    private static final String AND_DELIMITER = "&";
    private static final String EQUAL_DELIMITER = "=";
    private static final String QUESTION_DELIMITER = "?";
    private static final String CHARSET = "utf-8";

    public static File download(String path, String downloadDir) {
        if (StringUtils.startsWith(path, "http://") || StringUtils.startsWith(path, "https://")) {
            try {
                // 统一资源
                URL url = new URL(path);
                // 连接类的父类，抽象类
                URLConnection urlConnection = url.openConnection();
                // http的连接类
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                // 设定请求的方法，默认是GET
                httpURLConnection.setRequestMethod("POST");
                // 设置字符编码
                httpURLConnection.setRequestProperty("Charset", "UTF-8");
                // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
                httpURLConnection.connect();

                // 文件大小
                int fileLength = httpURLConnection.getContentLength();

                // 文件名
                String filePathUrl = httpURLConnection.getURL().getFile();
                String fileFullName = filePathUrl.substring(filePathUrl.lastIndexOf(File.separatorChar) + 1);

                logger.info("file length---->{}", fileLength);

                BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());

                String dirPath = downloadDir + File.separator + System.currentTimeMillis();
                if (fileFullName.endsWith(".jar")) {
                    JarUtils.writeFile(bin, dirPath, fileFullName, fileLength, logger);
                } else if (fileFullName.endsWith(".zip")) {
                    ZipUtils.writeFile(bin, dirPath, fileFullName, fileLength, logger);
                } else if (fileFullName.endsWith(".tar.gz") || fileFullName.endsWith(".tgz")) {
                    TarGzUtils.writeFile(bin, dirPath, fileFullName, fileLength, logger);
                } else {
                    throw new AgentDownloadException("文件只能以.jar、.zip、.tgz或者.tar.gz结尾: " + fileFullName);
                }
                return new File(dirPath);
            } catch (AgentDownloadException e) {
                throw e;
            } catch (MalformedURLException e) {
                logger.error("Download file from remote server err. {}", path, e);
                throw new AgentDownloadException("Download file from remote server err. " + path, e);
            } catch (IOException e) {
                logger.error("Download file from remote server err. {}", path, e);
                throw new AgentDownloadException("Download file from remote server err. " + path, e);
            }

        } else {
            try {
                if (StringUtils.indexOf(path, QUESTION_DELIMITER) != -1) {
                    path = StringUtils.substring(path, 0, StringUtils.indexOf(path, QUESTION_DELIMITER));
                }

                File file = new File(path);
                if (!file.exists()) {
                    throw new AgentDownloadException(String.format("agent file %s is not exists!", file.getAbsolutePath()));
                }

                FileInputStream bin = new FileInputStream(file);
                if (file.getName().endsWith(".jar")) {
                    JarUtils.writeFile(bin, downloadDir, file.getName(), (int) file.length(), logger);
                } else if (file.getName().endsWith(".zip")) {
                    ZipUtils.writeFile(bin, downloadDir, file.getName(), (int) file.length(), logger);
                } else if (file.getName().endsWith(".tar.gz") || file.getName().endsWith(".tgz")) {
                    TarGzUtils.writeFile(bin, downloadDir, file.getName(), (int) file.length(), logger);
                } else {
                    throw new RuntimeException("文件只能以.jar、.zip、.tgz或者.tar.gz结尾: " + file.getName());
                }
                return new File(downloadDir);
            } catch (AgentDownloadException e) {
                throw e;
            } catch (MalformedURLException e) {
                logger.error("Download file from remote server err. {}", path, e);
                throw new AgentDownloadException("Download file from remote server err. " + path, e);
            } catch (IOException e) {
                logger.error("Download file from remote server err. {}", path, e);
                throw new AgentDownloadException("Download file from remote server err. " + path, e);
            }
        }
    }

    private static Map<String, String> createQueryParams(String query) throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<String, String>();
        if (StringUtils.isBlank(query)) {
            return result;
        }

        String[] queryParams = query.split(AND_DELIMITER);
        if (ArrayUtils.isEmpty(queryParams)) {
            return result;
        }
        for (String qParam : queryParams) {
            if (qParam.indexOf(EQUAL_DELIMITER) == -1) {
                continue;
            }
            String[] param = qParam.split(EQUAL_DELIMITER);
            String key = URLDecoder.decode(param[0], CHARSET);
            String value = param.length > 1 ? URLDecoder.decode(param[1], CHARSET) : null;
            result.put(key, value);
        }
        return result;
    }
}
