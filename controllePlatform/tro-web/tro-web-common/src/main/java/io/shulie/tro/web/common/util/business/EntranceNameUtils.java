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

package io.shulie.tro.web.common.util.business;

import io.shulie.tro.web.common.enums.activity.ServiceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.common.util.business
 * @date 2021/5/12 4:12 下午
 */
public class EntranceNameUtils {

    /**
     * 获取rpcType
     * @param type
     * @return
     */
    public static String getRpcType(String type) {
        return ServiceTypeEnum.getEnumByType(type);
    }

    public static void checkEntranceName(String entranceName) {
        if(!entranceName.contains("#")) {
            throw new RuntimeException("服务参数不正确,缺少method");
        }
    }
    public static EntranceNameVO getEntranceNameVO(String entranceName,String type) {
        if(ServiceTypeEnum.HTTP.getType().equalsIgnoreCase(type)) {
            String[] entry = entranceName.split("#");
            return new EntranceNameVO(entry[0],entry[1]);
        }else if(ServiceTypeEnum.DUBBO.getType().equalsIgnoreCase(type)){
            String[] entry = entranceName.split("#");
            return new EntranceNameVO(entry[0],entry[1]);
        }
        return new EntranceNameVO(entranceName,entranceName);
    }

    @Data
    @AllArgsConstructor
    public static class EntranceNameVO {
        private String serviceName;
        private String method;
    }


}
