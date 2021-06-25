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

package com.pamirs.tro.entity.domain.dto.linkmanage.mapping;

import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.enums.LinkDomainEnum;

/**
 * @Auther: vernon
 * @Date: 2019/12/23 22:40
 * @Description:
 */
public class LinkDomainEnumMapping {
    public static LinkDomainEnum getByCode(String code) {
        for (LinkDomainEnum linkDomainEnum : LinkDomainEnum.values()) {
            if (linkDomainEnum.getCode().equalsIgnoreCase(code)) {
                return linkDomainEnum;
            }
        }
        return null;
    }
}
