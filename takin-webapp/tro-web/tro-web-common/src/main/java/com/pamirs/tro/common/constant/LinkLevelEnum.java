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

package com.pamirs.tro.common.constant;

/**
 * 链路级别枚举
 *
 * @author shulie
 * @version v1.0
 * @2018年5月16日
 */
public enum LinkLevelEnum {
    //一级链路标志，queryApplicationListByLinkInfo方法中使用
    FIRST_LINK_LEVEL("FIRST_LINK"),
    //二级链路标志，queryApplicationListByLinkInfo方法中使用
    SECOND_LINK_LEVEL("SECOND_LINK"),
    //基础链路标志，queryApplicationListByLinkInfo方法中使用
    BASE_LINK_LEVEL("BASE_LINK");

    //链路级别名称
    private String name;

    /**
     * 构造方法
     *
     * @param name 链路级别名称
     */
    LinkLevelEnum(String name) {
        this.name = name;
    }

    /**
     * 获取name属性值
     *
     * @return 返回name值
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name属性
     */
    public void setName(String name) {
        this.name = name;
    }
}
