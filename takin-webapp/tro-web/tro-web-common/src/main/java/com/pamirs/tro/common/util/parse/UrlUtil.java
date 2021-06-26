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

package com.pamirs.tro.common.util.parse;

import java.util.Map;

import com.google.common.collect.Maps;
//import io.shulie.amdb.common.enums.RpcType;
import com.pamirs.tro.common.enums.amdb.common.enums.RpcType;
import io.shulie.tro.web.common.util.ActivityUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName UrlUtil
 * @Description
 * @Author qianshui
 * @Date 2020/4/20 下午2:56
 */
public class UrlUtil {
    public static Map<String, String> convertUrl(ActivityUtil.EntranceJoinEntity entranceJoinEntity) {
        Map<String, String> map = Maps.newHashMap();
        //如果为http请求
        if (entranceJoinEntity.getRpcType().equals(RpcType.TYPE_WEB_SERVER+"")){
            String url = entranceJoinEntity.getServiceName();
            String[] urls = StringUtils.split(url, "|");
            if (urls == null || urls.length == 0) {
                return map;
            }
            url = urls[urls.length - 1].replace("//", "/");
            map.put("url", deleteFront(url));
        }
        //如果为rpc请求
        if (entranceJoinEntity.getRpcType().equals(RpcType.TYPE_RPC+"")){
            String methodName = entranceJoinEntity.getMethodName();
            String substring = methodName.substring(0, methodName.indexOf("("));
            if (substring.endsWith("~")){
                substring = substring.substring(0,substring.length() - 1);
            }
            String url = entranceJoinEntity.getServiceName().split(":")[0] + "#" + substring;
            map.put("url", url);
        }
        //如果为mq
        if (entranceJoinEntity.getRpcType().equals(RpcType.TYPE_MQ+"")){
            map.put("url", entranceJoinEntity.getServiceName());
        }
        return map;
    }

    public static Boolean checkEqual(String pradarPath, String jmeterPath) {
        if (StringUtils.isBlank(pradarPath) || StringUtils.isBlank(jmeterPath)) {
            return false;
        }
        int pos = pradarPath.indexOf("?");
        if (pos > 0) {
            pradarPath = pradarPath.substring(0, pos);
        }
        pos = jmeterPath.indexOf("?");
        if (pos > 0) {
            jmeterPath = jmeterPath.substring(0, pos);
        }
        pradarPath = pradarPath.replace("/", "");
        jmeterPath = jmeterPath.replace("/", "");
        //匹配 /api/employee/{id} 的情况
        jmeterPath = jmeterPath.replace("$", "");
        //endWith 匹配 网关转发时，加前缀的情况 /partner-open-api  网关转发，去前缀的情况 /api
        if (pradarPath.equals(jmeterPath) || pradarPath.endsWith(jmeterPath) || jmeterPath.endsWith(pradarPath)) {
            return true;
        }
        return false;
    }

    /**
     * 删除字符串中，冒号及之前的字符
     *
     * @param str
     * @return
     */
    private static String deleteFront(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        int index = str.lastIndexOf(":");
        if (index == -1) {
            return str;
        }
        str = str.substring(index + 1);
        if (StringUtils.startsWith(str, "/")) {
            str = str.substring(1);
        }
        index = str.indexOf("/");
        return str.substring(index + 1);
    }

    public static void main(String[] args) {
        String url = "crm|TRACE|http://47.99.119.242:8081/dock/exchange/create";
        String url1 = "crm|TRACE|http://47.99.119.242/dock/exchange/create";
        String url2 = "crm|TRACE|dock/exchange/create";
        String url3 = "crm|TRACE|/dock/exchange/create";

    }
}
