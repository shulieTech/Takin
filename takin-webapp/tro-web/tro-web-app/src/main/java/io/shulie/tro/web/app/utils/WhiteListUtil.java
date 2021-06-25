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

package io.shulie.tro.web.app.utils;

import io.shulie.tro.web.common.constant.WhiteListConstants;

/**
 * @author liuchuan
 * @date 2021/4/13 5:29 下午
 */
public class WhiteListUtil {

    /**
     * 获取 interface, type 组装的唯一
     *
     * @param interfaceName 接口名
     * @param interfaceType 类型
     * @return 唯一
     */
    public static String getInterfaceAndType(String interfaceName, String interfaceType) {
        return String.format(WhiteListConstants.INTERFACE_AND_TYPE, interfaceName, interfaceType);
    }

}
