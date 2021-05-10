package io.shulie.surge.data.deploy.pradar.common;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import io.shulie.surge.data.common.utils.CommonUtils;
import io.shulie.surge.data.common.utils.FormatUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author pamirs
 */
public final class PradarUtils {

    /**
     * 换行匹配
     */
    private static final String NEW_LINE_MATCHER = "\r\n";

    /**
     * 用方括号“[]”括住的字符串
     */
    private static final Pattern URL_REGEX_PATTERN = Pattern.compile("\\[[^\\]]+\\]\\+?");

    private static final int[] EMPTY_INT_ARRAY = new int[0];


    public static int[] parseSpan(String span) {
        if (CommonUtils.isNullEmpty(span)) {
            return EMPTY_INT_ARRAY;
        } else {
            final int len = span.length();
            if (len >= 3 && span.charAt(0) == '[' && span.charAt(len - 1) == ']') {
                int idx = span.indexOf(',');
                if (idx == -1) {
                    // 有 1 个
                    return new int[]{CommonUtils.parseIntQuietly(span.substring(1, len - 1), 0)};
                } else if (idx + 3 < len && span.charAt(idx + 1) == ' ') {
                    // 有 2 个以上
                    int idx2 = span.indexOf(',', idx + 2);
                    if (idx2 == -1) {
                        return new int[]{
                                CommonUtils.parseIntQuietly(span.substring(1, idx), 0),
                                CommonUtils.parseIntQuietly(span.substring(idx + 2, len - 1), 0)};
                    } else {
                        return new int[]{
                                CommonUtils.parseIntQuietly(span.substring(1, idx), 0),
                                CommonUtils.parseIntQuietly(span.substring(idx + 2, idx2), 0)};
                    }
                } else {
                    // 不正确的格式
                    return EMPTY_INT_ARRAY;
                }
            } else {
                return new int[]{CommonUtils.parseIntQuietly(span, 0)};
            }
        }
    }

    /**
     * 获取服务的方法，例如：com.pamirs.uic.common.service.userlogin.UserLoginService@findLoginTraceByUserId
     * ~lb 获取结果：findLoginTraceByUserId~lb
     */
    public static String getMethodName(String serviceName) {
        if (serviceName != null) {
            int pos = serviceName.lastIndexOf('@');
            if (pos >= 0 && pos + 1 < serviceName.length()) {
                StringBuilder appender = new StringBuilder(serviceName.length() - pos + 32);
                FormatUtils.appendWithSoftLineBreak(appender, serviceName, pos + 1, serviceName.length(), 5);
                return appender.toString();
            }
            pos = serviceName.indexOf('!');
            if (pos != -1) {
                serviceName = serviceName.substring(0, serviceName.indexOf('!'));
            }
        }
        return serviceName;
    }

    /**
     * 获取服务的方法，例如：com.pamirs.uic.common.service.userlogin.UserLoginService@findLoginTraceByUserId
     * ~lb 获取结果：findLoginTraceByUserId~lb
     */
    public static String getMethodNameWithoutSoftLineBreak(String serviceName) {
        if (serviceName != null) {
            int pos = serviceName.lastIndexOf('@');
            if (pos >= 0 && pos + 1 < serviceName.length()) {
                serviceName = serviceName.split("@")[1];
            }
            pos = serviceName.indexOf('!');
            if (pos != -1) {
                serviceName = serviceName.substring(0, serviceName.indexOf('!'));
            }
        }
        return serviceName;
    }


    /**
     * 返回 IP 对应的 TraceId 前缀
     */
    public static String getTraceIpSectionForIp(String ip) {
        String[] ips = ip.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (int i = 3; i >= 0; --i) {
            String hex = Integer.toHexString(Integer.parseInt(ips[i]));
            if (hex.length() == 1) {
                sb.append('0').append(hex);
            } else {
                sb.append(hex);
            }
        }
        return sb.toString();
    }


    /**
     * 返回 TraceId 前缀的 IP 部分对应的实际 IP
     */
    public static String getIpFromTraceIpSection(String traceIdPrefix, String defaultValue) {
        if (traceIdPrefix != null && traceIdPrefix.length() >= 8) {
            try {
                int a = Integer.parseInt(traceIdPrefix.substring(0, 2), 16);
                int b = Integer.parseInt(traceIdPrefix.substring(2, 4), 16);
                int c = Integer.parseInt(traceIdPrefix.substring(4, 6), 16);
                int d = Integer.parseInt(traceIdPrefix.substring(6, 8), 16);
                return d + "." + c + "." + b + "." + a;
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    /**
     * 返回 TraceId 中隐含的实际 IP，如果是老版本 Pradar TraceId，返回 defaulValue
     */
    public static String getIpFromTraceId(String traceId, String defaultValue) {
        return getIpFromTraceIpSection(traceId, defaultValue);
    }

    /**
     * 返回 TraceId 中隐含的创建时间
     */
    public static long getStartTimeFromTraceId(String traceId, long defaultValue) {
        // 如果是新版的 Pradar，可以从 trace 里面得到开始时间
            try {
                return Long.parseLong(traceId.substring(8, 21));
            } catch (NumberFormatException e) {
        }
        return defaultValue;
    }


    /**
     * 检查调用链是否需要被采样，采样值需要大于 1 才会做计算，小于等于 1 会作为全部采样处理
     *
     * @param sampling 采样率
     * @return <code>true</code> 调用链需要被分析，<code>false</code> 调用链可以被忽略
     */
    public static boolean isTraceSampleAccepted(final String traceId, final int sampling) {
        if (sampling > 1) {
            if (traceId.length() >= 25) {
                int count = traceId.charAt(21) - '0';
                count = count * 10 + traceId.charAt(22) - '0';
                count = count * 10 + traceId.charAt(23) - '0';
                count = count * 10 + traceId.charAt(24) - '0';
                return count % sampling == 0;
            } else {
                return traceId.hashCode() % sampling == 0;
            }
        }
        return true;
    }

    /**
     * 将 value 转化为对应的十六进制字符，范围在 00~ff， 小于 0 则返回 ff，大于等于 254 则返回 fe
     */
    public static String getTwoDigitHexString(int value) {
        if (value < 0) {
            return "ff";
        } else if (value < 16) {
            return "0" + Integer.toHexString(value);
        } else if (value >= 254) {
            return "fe";
        } else {
            return Integer.toHexString(value);
        }
    }

    /**
     * 将“.” 分隔的版本号、rpcId 切分成整数数组
     */
    public static int[] parseVersion(String str) {
        if (str != null) {
            String[] strs = StringUtils.split(str, '.');
            int[] ints = new int[strs.length];
            for (int i = 0; i < strs.length; ++i) {
                ints[i] = CommonUtils.parseIntQuietly(strs[i], 0);
            }
            return ints;
        }
        return new int[]{0};
    }

    /**
     * 整数数组间的比较，前面的数较大，则算大，相等则比较下一位。 前面都相等时，长的数组较大。
     */
    public static int rpcIdCompare(int[] thisRpcIdArray, int[] thatRpcIdArray) {
        int size = thisRpcIdArray.length < thatRpcIdArray.length ? thisRpcIdArray.length : thatRpcIdArray.length;
        for (int i = 0; i < size; i++) {
            int result = thisRpcIdArray[i] - thatRpcIdArray[i];
            if (result != 0) {
                return result;
            }
        }
        return thisRpcIdArray.length - thatRpcIdArray.length;
    }


    /**
     * 获取 URL 中的 host 部分
     */
    public static String getHostFromUrl(String url) {
        int start;
        if (url.startsWith("http://")) {
            start = 7;
        } else if ((start = url.indexOf("://")) != -1) {
            start += 3;
        } else {
            start = 0;
        }

        int end = url.indexOf('/', start);
        if (end == -1) {
            end = url.length();
        }
        return url.substring(start, end);
    }

    /**
     * 去掉 URL 的前缀和 host 部分，如：<br> http://www.pamirs.com/index.htm => /index.htm
     */
    public static String removeHostFromUrl(String url) {
        int start;
        if (url.startsWith("http://")) {
            start = 7;
        } else if ((start = url.indexOf("://")) != -1) {
            start += 3;
        } else {
            start = 0;
        }

        int end = url.indexOf('/', start);
        if (end == -1) {
            return url.substring(start);
        }
        return url.substring(end);
    }

    /**
     * 从 URL 中获取 host 部分，这里要处理静态化过的正则字符串，替换为：*。 找不到合适的域名则返回 <code>null</code>
     */
    public static String getDomainFromPatternedUrl(String patternUrl) {
        if (patternUrl.startsWith("http://")) {
            patternUrl = replaceURLPatternRegex(patternUrl);
            String host = getHostFromUrl(patternUrl);
            if (!CommonUtils.isNullEmpty(host)) {
                if (host.indexOf(":") != -1) {
                    host = host.substring(0, host.indexOf(":"));
                }
                char lastChar = host.charAt(host.length() - 1);
                if (Character.isLetter(lastChar)) {
                    return host;
                }
            }
        }
        return null;
    }


    /**
     * 按换行符切分字符串，支持各种换行
     */
    public static Iterable<String> iterateByNewLine(String text) {
        return Splitter.on(NEW_LINE_MATCHER).omitEmptyStrings().split(text);
    }

    /**
     * 按换行符切分字符串，支持各种换行
     */
    public static List<String> splitByNewLine(String text) {
        Iterable<String> iterable = iterateByNewLine(text);
        List<String> lines = new ArrayList<String>();
        Iterables.addAll(lines, iterable);
        return lines;
    }

    /**
     * 从 URL 解析出 URI，如果解析不到 URI，就用域名，例如： <p>
     * <pre>
     * "http://shop66155774.pamirs.com/shop/view_shop.htm" => "/shop/view_shop.htm"
     * "http://shop66155774.pamirs.com/shop/" => "/shop"
     * "http://www.pamirs.com" => "www.pamirs.com"
     * "http://www.pamirs.com/" => "www.pamirs.com"
     * "https://pamirs.pradar.top:9999/" => "pamirs.pradar.top:9999"
     * "not_url_entrance" => "not_url_entrance"
     * </pre>
     */
    public static String getUriFromUrl(String url) {
        int start;
        final int len = url.length();
        if (len <= 7) {
            return url;
        }
        if (url.startsWith("http://")) {
            start = 7;
        } else if ((start = url.indexOf("://")) != -1) {
            start += 3;
        } else {
            start = 0;
        }

        // 去掉末尾的 ‘/’
        final int end = (url.charAt(len - 1) == '/') ? (len - 1) : len;
        final int istart = url.indexOf('/', start);
        if (istart >= 0 && istart < end) {
            return url.substring(istart, end);
        }
        return url.substring(start, end);
    }

    /**
     * 替换 URL 规则中用正则表达式静态化的字符串为“*”，即把中括号括住的内容都替换掉
     */
    public static String replaceURLPatternRegex(String patternUrl) {
        return URL_REGEX_PATTERN.matcher(patternUrl).replaceAll("*");
    }

}
