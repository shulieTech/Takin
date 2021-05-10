package io.shulie.surge.data.runtime.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.shulie.surge.data.common.utils.HttpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: vernon
 * @Date: 2020/4/2 13:45
 * @Description: api聚合器
 */
@Singleton
public class ApiProcessor {
    @Inject
    @Named("tro.url.ip")
    private String URI;

    @Inject
    @Named("tro.api.path")
    private String PATH;

    @Inject
    @Named("tro.port")
    private String PORT;

    private Gson gson = new Gson();

    protected static Map<String, Map<String, List<String>>> API_COLLECTION = new HashMap<>();

    protected static Map<String, Matcher> MATHERS = new HashMap<>();

    private ScheduledExecutorService service =
            new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("api-collector");
                    t.setDaemon(true);
                    return t;
                }
            });


    public void init() {
        service.scheduleAtFixedRate(
                new Thread(() -> refresh())
                , 0
                , 2
                , TimeUnit.MINUTES)
        ;
    }

    private void refresh() {
        Map<String, Object> res = new HashMap<>();
        try {
            res = gson.fromJson(HttpUtil.doGet(URI, Integer.valueOf(PORT), PATH), Map.class);
        } catch (Throwable e) {
            System.err.println(e.getMessage());
        }
        if (Objects.nonNull(res) && Objects.nonNull(res.get("data"))) {
            Object data = res.get("data");
            Map<String, List<String>> map = (Map<String, List<String>>) data;
            for (String appName : map.keySet()) {
                List<String> apiList = map.get(appName);
                Map<String, List<String>> newApiMap = Maps.newHashMap();
                for (String api : apiList) {
                    String[] splits = api.split("#");
                    String url = splits[0];
                    String type = splits[1];
                    if (Objects.isNull(newApiMap.get(type))) {
                        List<String> list = new ArrayList<>();
                        list.add(url);
                        newApiMap.put(type, list);
                    } else {
                        List<String> newList = newApiMap.get(type);
                        newList.add(url);
                        newApiMap.put(type, newList);
                    }
                }
                API_COLLECTION.put(appName, newApiMap);
            }
            MATHERS.clear();
        }
    }

    /**
     * url 格式化
     *
     * @param url
     * @return
     */
    public static String urlFormat(String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        } else {
            try {
                URL u = new URL(url);
                String protocol = u.getProtocol();
                String host = u.getHost();
/*
                if (IpAddressUtils.isIpv4AddressFast(host)) {
*/
                host = "";
                //}
                if ("null".equals(host)) {
                    host = "";
                }
                url = /*protocol + "://" + host +*/ u.getPath();
            } catch (Exception e) {
                //ignore
            }
        }
        return url;
    }

    /**
     * 截取host部分
     * @param url
     * @return
     */
    public static String formatUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        } else {
            try {
                URL u = new URL(url);
                String protocol = u.getProtocol();
                String host = u.getHost();
                if ("null".equals(host)) {
                    host = "";
                }
                url = protocol + "://" + host;
            } catch (Exception e) {
                //ignore
            }
        }
        return url;
    }

    public static String merge(String appName, String url, String type) {
        url = urlFormat(url);
        if (StringUtils.isBlank(url)) {
            return "";
        }
        Matcher matcher = MATHERS.get(appName);
        if (Objects.isNull(matcher)) {
            Map<String, List<String>> apiMaps = API_COLLECTION.get(appName);
            if (Objects.isNull(apiMaps) || apiMaps.size() < 1) {
                return url;
            }
            matcher = new Matcher(apiMaps);
            MATHERS.putIfAbsent(appName, matcher);
        }
        return matcher.match(url, type);
    }

    /**
     * 获取url Path部分
     */
    public static String parsePath(String href) {
        try {
            URL u = new URL(href);
            return u.getPath();
        } catch (Throwable e) {
        }
        return href;
    }
}

final class Matcher {
    final static Logger logger = LoggerFactory.getLogger(Matcher.class);
    private Map<String, List<String>> apiMap;

    public Matcher(Map<String, List<String>> apiMap) {
        this.apiMap = apiMap;
    }

    protected String match(String url, String type) {
        List<String> apis = apiMap.get(type);
        if (CollectionUtils.isEmpty(apis)) {
            apis = Lists.newArrayList();
            for (Map.Entry<String, List<String>> entry : apiMap.entrySet()) {
                apis.addAll(entry.getValue());
            }
        }
        url = url.trim();
        String res = url;
        try {
            StringBuilder pre = new StringBuilder();
            int index = url.indexOf("://");
            if (index > -1) {
                pre = pre.append(url.substring(0, index + 3));
                url = url.substring(index + 3, url.length());
                if (url.indexOf("/") > -1) {
                    pre = pre.append(url.substring(0, url.indexOf("/")));
                    url = url.substring(url.indexOf("/"), url.length());
                }
            }
            if (apis.contains(url)) {
                return res;
            }
            for (String api : apis) {
                // /app/add/{name}
                // /app/add/1
                if (api.contains("/{") && api.contains("}")) {
                    String subApi = api.substring(0, api.indexOf("/{"));
                    if (res.contains(subApi)) {
                        return pre.append(api).toString();
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return res;
    }


    static enum HttpTypeEnum {

        POST,
        GET,
        PUT,
        DELETE,
        OPTIONS,
        HEAD,
        TRACE,
        CONNECTION

    }


    public static void main(String[] args) {
        System.out.println(ApiProcessor.formatUrl("http://tank-bucket.oss-cn-beijing.aliyuncs.com/tank/ossFile2021-01-26%2022%3A56%3A1"));
    }
}




