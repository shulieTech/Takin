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

package io.shulie.tro.web.app.service.linkManage;

import java.util.List;
import java.util.Optional;

import com.pamirs.tro.entity.domain.entity.linkmanage.figure.LinkEdge;
import com.pamirs.tro.entity.domain.entity.linkmanage.figure.RpcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Auther: vernon
 * @Date: 2019/12/10 02:06
 * @Description:
 */

@Component
public class LinkManage {
    private static final Logger logger = LoggerFactory.getLogger(LinkManage.class);

    /**
     * 组装root顶点信息中的入口名
     *
     * @param edge 顶级节点
     * @return
     */
    public static String generateEntrance(LinkEdge edge) {
        StringBuilder builder = new StringBuilder();
        builder.append(edge.getApplicationName())
            .append("|")
            .append(RpcType.getByValue(edge.getRpcType().intValue(), null))
            .append("|")
            .append(edge.getServiceName());
        return builder.toString();
    }

    public static String generateEntranceWithoutAppName(LinkEdge edge) {
        StringBuilder builder = new StringBuilder();
        builder.append(
            RpcType.getByValue(edge.getRpcType().intValue(), null)
        )
            .append("|")
            .append(edge.getServiceName());
        return builder.toString();
    }

    /**
     * 过滤并组装root顶点信息中的入口名
     *
     * @param edges
     * @return
     */
    public static String generateEntrance(List<LinkEdge> edges) {
        String result = "";
        Optional<LinkEdge> optional
            = edges.stream().filter(edge -> "0".equals(edge.getRpcId())).findFirst();
        if (optional.get() == null) {
            logger.info("获取顶级节点失败...");
            return result;
        }
        LinkEdge root = optional.get();
        result = generateEntrance(root);
        return result;

    }

    /**
     * 组装节点信息
     *
     * @param edge
     * @return
     */
    private static String buildNodeInfo(LinkEdge edge) {
        StringBuilder builder = new StringBuilder();
        builder.append(edge.getApplicationName())
            .append("|")
            .append(RpcType.getByValue(edge.getRpcType().intValue(), null))
            .append("|")
            .append(edge.getServiceName());
        return builder.toString();
    }

    public static String buildNodeInfo(List<LinkEdge> edges) {
        StringBuilder builder = new StringBuilder();
        for (LinkEdge edge : edges) {
            builder.append(buildNodeInfo(edge))
                .append(";");
        }
        return builder.toString();
    }

    /**
     * 统计str中包含多少个key
     *
     * @param str
     * @param key
     * @return
     */
    public static int getSubCount(String str, String key) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(key, index)) != -1) {
            index = index + key.length();

            count++;
        }
        return count;
    }

}
