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

import java.util.ArrayList;
import java.util.List;

import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.enums.LinkChangeEum;

/**
 * @Auther: vernon
 * @Date: 2019/12/23 22:35
 * @Description:
 */
public class LinkChangeEnumMapping {
    public static LinkChangeEum getByCode(String code) {
        for (LinkChangeEum statusEnum : LinkChangeEum.values()) {
            if (statusEnum.getCode().equalsIgnoreCase(code)) {
                return statusEnum;
            }
        }
        return LinkChangeEum.NORMAL;
    }

    public static EnumResult parse(LinkChangeEum linkChangeEum) {
        EnumResult result = new EnumResult();
        if (linkChangeEum != null) {
            switch (linkChangeEum) {
                case NORMAL:
                    result.label(linkChangeEum.getDesc()).value(linkChangeEum.getCode()).num(0);
                    break;
                case CHANGED:
                    result.label(linkChangeEum.getDesc()).value(linkChangeEum.getCode()).num(1);
                    break;

            }
        } else {
        }
        return result;
    }

    public static List<EnumResult> neededEnumResults() {
        List<EnumResult> enumResults = new ArrayList<>();
        for (LinkChangeEum statusEnum : LinkChangeEum.values()) {
            enumResults.add(parse(statusEnum));
        }
        return enumResults;
    }
}
