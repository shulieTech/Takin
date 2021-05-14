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

package com.pamirs.tro.entity.domain.dto.linkmanage.mapping.enums;

import lombok.Getter;

/**
 * @Author: fanxx
 * @Date: 2020/7/6 下午5:22
 * @Description: 链路拓扑图节点分类
 */
@Getter
public enum NodeClassEnum {
    APP("0", "应用"),

    GATEWAY("1", "网关"),

    DB("2", "数据库"),

    MQ("3", "消息队列"),

    CACHE("4", "缓存"),

    OTHER("5", "其他"),

    ES("6", "搜索"),

    OSS("7", "文件"),

    UNKNOWN("1000", "未知");

    private String code;
    private String desc;

    NodeClassEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static NodeClassEnum getValue(String code) {
        for (NodeClassEnum ele : values()) {
            if (ele.getCode().equals(code)) { return ele; }
        }
        return null;
    }

    /**
     * 获取请求参数类型
     * 例如：
     * 数据库的对应的是库名、表名
     * MQ 对应的是TOPIC
     * 缓存对应的是缓存KEY
     *
     * @return
     */
    public String getDataType() {
        String dataType = null;
        switch (code) {
            case "0":
                break;
            case "1":
                break;
            case "2":
                dataType = "库表";
                break;
            case "3":
                dataType = "Topic";
                break;
            case "4":
                dataType = "缓存Key";
                break;
            case "5":
                break;
            case "6":
                dataType = "索引名";
                break;
            case "7":
                dataType = "文件路径";
                break;
            default:
                dataType = "";
        }
        return dataType;
    }
}
