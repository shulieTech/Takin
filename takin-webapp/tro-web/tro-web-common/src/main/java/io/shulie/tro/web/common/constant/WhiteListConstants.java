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

package io.shulie.tro.web.common.constant;

/**
 * 白名单常量池
 *
 * @author liuchuan
 * @date 2021/4/9 5:01 下午
 */
public interface WhiteListConstants {

    /**
     * 白名单加入状态
     */
    int STATUS_JOIN = 1;

    /**
     * 白名单未加入状态
     */
    int STATUS_NOT_JOIN = 0;

    /**
     * agent 上报的白名单, 默认字典类型
     */
    String DEFAULT_DICT_TYPE = "ca888ed801664c81815d8c4f5b8dff0c";

    /**
     * topicGroup_类型
     */
    String INTERFACE_AND_TYPE = "%s_%s";

}
