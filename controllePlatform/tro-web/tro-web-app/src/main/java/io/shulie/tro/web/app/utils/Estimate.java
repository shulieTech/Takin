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

import org.apache.commons.lang3.StringUtils;

public class Estimate {

    public static void notBlank(Object object, String errorMessage) {
        if (null == object) {
            throw new IllegalArgumentException(errorMessage);
        }
        if (object instanceof String) {
            String obj = (String)object;
            if (StringUtils.isBlank(obj)) {
                throw new IllegalArgumentException(errorMessage);
            }
        } else if (object instanceof Integer) {
            Integer obj = (Integer)object;
            if (0 == obj) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }
}
