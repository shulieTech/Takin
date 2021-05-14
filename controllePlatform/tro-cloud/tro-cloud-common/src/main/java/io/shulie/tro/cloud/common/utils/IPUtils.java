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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IP地址转换工具类
 *
 * @author shulie
 * @version v1.0
 * @2018年5月21日
 */
public class IPUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(IPUtils.class);

    /**
     * 获取用户的IP地址
     *
     * @param httpServletRequest http请求
     * @return 用户的ip地址
     * @author shulie
     * @2018年5月21日
     * @version v1.0
     */
    public static String getIP(HttpServletRequest httpServletRequest) {
        String unknownStr = "unknown";
        String ip = null;
        //这里多个判断条件是相同的，不知道用意何为？？？？
        if (StringUtils.isEmpty(ip) || unknownStr.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || unknownStr.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || unknownStr.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("HTTP_CLIENT_IP");
        }

        if (StringUtils.isEmpty(ip) || unknownStr.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(ip) || unknownStr.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("X-Forwarded-For");
        }

        if (StringUtils.isEmpty(ip) || unknownStr.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("X-Forwarded-Host");
        }
        if (StringUtils.isEmpty(ip) || unknownStr.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getRemoteAddr();
        }
        if (StringUtils.isNotEmpty(ip) && (ip.length() >= 15)) {
            ip = StringUtils.substringBefore(ip, ",");
        }
        return ip;
    }

    /**
     * 将255.255.255.255 形式的IP地址转换成long型，
     * 传入的IP格式为"100.010.000.111"或者"1.10.0.111"，
     * 不能包含字母等
     *
     * @param strIP 字符串ip地址
     * @return long型的ip地址
     * @author shulie
     * @2018年5月21日
     * @version v1.0
     */
    public static long ipToLong(String strIP) {
        if (strIP == null || "".equals(strIP)) {
            return 0;
        }
        long[] ip = new long[4];
        int position1 = strIP.indexOf(".");
        int position2 = strIP.indexOf(".", position1 + 1);
        int position3 = strIP.indexOf(".", position2 + 1);
        ip[0] = Long.parseLong(strIP.substring(0, position1));
        ip[1] = Long.parseLong(strIP.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIP.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIP.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * 说明：判断是否为windows操作系统
     *
     * @return
     * @author shulie
     * @time：2017年10月5日 下午2:46:05
     */
    public static boolean isWindowOS() {
        return StringUtils.containsIgnoreCase(System.getProperty("os.name"), "windows");
    }

    /**
     * 说明：获取linux服务器的ip地址
     *
     * @return
     * @throws UnknownHostException
     * @throws SocketException
     * @author shulie
     * @time：2017年10月5日 下午2:46:42
     */
    public static InetAddress getInetAddress() {
        try {
            if (isWindowOS()) {
                return InetAddress.getLocalHost();
            }

            // 定义一个内容都是NetworkInterface的枚举对象
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            // 如果枚举对象里面还有内容(NetworkInterface)
            while (netInterfaces.hasMoreElements()) {
                // 获取下一个内容(NetworkInterface)
                NetworkInterface ni = netInterfaces.nextElement();
                // 遍历所有IP
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress inetAddress = ips.nextElement();
                    // 属于本地地址
                    if (inetAddress.isSiteLocalAddress()
                        // 不是回环地址
                        && !inetAddress.isLoopbackAddress()
                        // 地址里面没有:号
                        && inetAddress.getHostAddress().indexOf(":") == -1) {
                        return inetAddress;
                    }
                }
            }
        } catch (UnknownHostException e) {
            LOGGER.error("获取远程ip地址失败", e);
        } catch (SocketException e) {
            LOGGER.error("获取远程ip地址失败", e);
        }
        return null;
    }
}
