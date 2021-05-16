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

package io.shulie.surge.data.common.utils;

import sun.net.util.IPAddressUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;

/**
 *
 * @author vincent
 */
public class UrlUtils {


    /**
     * 获取端口号
     *
     * @param href 网址, ftp, http, nntp, ... 等等
     * @return
     * @throws IOException
     */
    public static int parsePort(String href) throws IOException {
        //java.net中存在的类
        URL url = new URL(href);
        // 端口号; 如果 href 中没有明确指定则为 -1
        int port = url.getPort();
        if (port < 0) {
            // 获取对应协议的默认端口号
            port = url.getDefaultPort();
        }
        return port;
    }

    /**
     * 获取Host部分
     *
     * @param href 网址, ftp, http, nntp, ... 等等
     * @return
     * @throws IOException
     */
    public static String parseHost(String href) throws IOException {
        //
        URL url = new URL(href);
        // 获取 host 部分
        String host = url.getHost();
        return host;
    }

    /**
     * 获取service部分
     *
     * @param href 网址, ftp, http, nntp, ... 等等
     * @return
     * @throws IOException
     */
    public static String parseService(String href) {
        try {
            String host = parseHost(href);
            int port = parsePort(href);
            if (port != 80) {
                host = host + ":" + port;
            }
            return href.substring(href.lastIndexOf(host) + host.length());
        } catch (Throwable e) {
        }
        return href;
    }

    /**
     * 根据域名(host)解析IP地址
     *
     * @param host 域名
     * @return
     * @throws IOException
     */
    public static String parseIp(String host) throws IOException {
        // 根据域名查找IP地址
        InetAddress inetAddress = InetAddress.getByName(host);
        // IP 地址
        String address = inetAddress.getHostAddress();
        return address;
    }


    /**
     * 识别IP是公网段还是内网段
     * tcp/ip协议中，专门保留了三个IP地址区域作为私有地址，其地址范围如下： 
     * A类  10.0.0.0-10.255.255.255  
     * B类  172.16.0.0-172.31.255.255  
     * C类  192.168.0.0-192.168.255.255  
     * 环回地址为：127.0.0.0-127.255.255.255
     * <p>
     * 如果是ipv6则全部判断为公网地址
     * <p>
     * 对于本地配置的 host 视为 内网
     *
     * @param ip ip地址
     * @return
     */
    public static boolean internalIp(String ip) {
        boolean iPv6LiteralAddress = IPAddressUtil.isIPv6LiteralAddress(ip);
        boolean isIPv4LiteralAddress = IPAddressUtil.isIPv4LiteralAddress(ip);
        if (iPv6LiteralAddress) {
            return false;
        } else if (isIPv4LiteralAddress) {
            byte[] addr = IPAddressUtil.textToNumericFormatV4(ip);
            return internalIp(addr);
        } else {
            return true;
        }
    }

    public static boolean internalIp(byte[] addr) {
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        //10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        //172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        //192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                switch (b1) {
                    case SECTION_6:
                        return true;
                }
            default:
                return false;
        }
    }
}
