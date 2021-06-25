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


import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * @author pamirs
 */
public class IpAddressUtils {

    private static final String LOCAL_IP_ADDRESS = getLocalInetAddress();
    private static final String LOCAL_HOST_NAME = getHostName();

    private static String getLocalInetAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress address = null;
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address.getHostAddress().indexOf(":") == -1) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (Throwable t) {
        }
        return "127.0.0.1";
    }

    private static String getHostName() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostName();
        } catch (Throwable t) {
        }
        return "localhost";
    }

    /**
     * 获取本机 IP
     *
     * @return 找不到 IP 地址则返回 127.0.0.1
     */
    public static final String getLocalAddress() {
        return LOCAL_IP_ADDRESS;
    }

    /**
     * 获取本机 IP
     *
     * @return 找不到 IP 地址则返回 127.0.0.1
     */
    public static final String getLocalHostName() {
        return LOCAL_HOST_NAME;
    }

    private static Pattern IPV4_ADDRESS_PATTERN = Pattern
            .compile("((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");

    public static final boolean isIpv4Address(String str) {
        return IPV4_ADDRESS_PATTERN.matcher(str).matches();
    }

    /**
     * 较快速的判断方法，存在误判的可能
     */
    public static final boolean isIpv4AddressFast(String str) {
        if (CommonUtils.countMatches(str, '.') == 3 && str.length() <= 15) {
            for (int i = 0; i < str.length(); ++i) {
                if (!isDigitOrDot(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static final boolean isDigitOrDot(char c) {
        return (c >= '0' && c <= '9') || c == '.';
    }

    /**
     * 去掉 IP 地址跟着的端口号，xxx.xxx.xxx.xxx:port -> xxx.xxx.xxx.xxx
     */
    public static final String removePort(String ipWithPort) {
        return StringUtils.substringBeforeLast(ipWithPort, ":");
    }

    /**
     * 去掉 IP 地址的 D 段，即 a.b.c.d -> a.b.c.
     */
    public static final String removeDClass(String ip) {
        if (ip == null) {
            return null;
        }
        int lastDot = ip.lastIndexOf('.');
        return lastDot > 0 ? ip.substring(0, lastDot + 1) : null;
    }

    /**
     * 去掉 IP 地址的 CD 段，即 a.b.c.d -> a.b.
     */
    public static final String removeCAndDClass(String ip) {
        if (ip == null) {
            return null;
        }
        int dot = ip.indexOf('.');
        if (dot > 0) {
            dot = ip.indexOf('.', dot + 1);
        }
        return dot > 0 ? ip.substring(0, dot + 1) : null;
    }

    /**
     * 把IP地址转化为int
     *
     * @return int
     */
    public static byte[] ipToBytesByReg(String ipAddr) {
        byte[] ret = new byte[4];
        try {
            StringTokenizer token = new StringTokenizer(ipAddr, ".");
            ret[0] = (byte) (Integer.parseInt(token.nextToken()) & 0xFF);
            ret[1] = (byte) (Integer.parseInt(token.nextToken()) & 0xFF);
            ret[2] = (byte) (Integer.parseInt(token.nextToken()) & 0xFF);
            ret[3] = (byte) (Integer.parseInt(token.nextToken()) & 0xFF);
            return ret;
        } catch (Exception e) {
            throw new IllegalArgumentException(ipAddr + " is invalid IP");
        }

    }

    /**
     * 字节数组转化为IP
     *
     * @return int
     */
    public static String bytesToIp(byte[] bytes) {
        return new StringBuffer().append(bytes[0] & 0xFF).append('.').append(
                bytes[1] & 0xFF).append('.').append(bytes[2] & 0xFF)
                .append('.').append(bytes[3] & 0xFF).toString();
    }

    /**
     * 根据位运算把 byte[] -> int
     *
     * @return int
     */
    public static int bytesToInt(byte[] bytes) {
        int addr = bytes[3] & 0xFF;
        addr |= ((bytes[2] << 8) & 0xFF00);
        addr |= ((bytes[1] << 16) & 0xFF0000);
        addr |= ((bytes[0] << 24) & 0xFF000000);
        return addr;
    }

    /**
     * 把IP地址转化为int
     *
     * @return int
     */
    public static int ipToInt(String ipAddr) {
        return bytesToInt(ipToBytesByReg(ipAddr));
    }

    /**
     * 把int->ip地址
     *
     * @return String
     */
    public static String intToIp(int ipInt) {
        return new StringBuilder().append(((ipInt >> 24) & 0xff)).append('.')
                .append((ipInt >> 16) & 0xff).append('.').append(
                        (ipInt >> 8) & 0xff).append('.').append((ipInt & 0xff))
                .toString();
    }

    public static String addressToIp(String address) {
        if (StringUtils.isBlank(address)) {
            return "";
        }
        if (StringUtils.isNotBlank(address)) {
            int colon = address.indexOf(':');
            if (colon > 0) {
                address = address.substring(0, colon);
            }
            if (address.charAt(0) == '/') {
                address = address.substring(1);
            }
        }
        return address;
    }

    public static void main(String[] args) {
        int ipInt = ipToInt("192.168.1.11");
        System.out.println(ipInt);
        String ipStr = intToIp(ipInt);
        System.out.println(ipStr);
        ipInt = ipToInt("10.1.1.2");
        System.out.println(ipInt);
        ipStr = intToIp(ipInt);
        System.out.println(ipStr);
    }
}
