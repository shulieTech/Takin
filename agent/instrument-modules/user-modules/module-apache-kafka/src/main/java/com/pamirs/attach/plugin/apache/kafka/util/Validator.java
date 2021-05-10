/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.attach.plugin.apache.kafka.util;


import com.pamirs.pradar.Pradar;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.apache.kafka.util
 * @Date 2020-04-01 23:23
 */
public class Validator {

    private static final Set<String> registereTopic = new HashSet<String>();

    public static boolean validate(List<String> topics) {
        for (String topic : topics) {
            if (Pradar.isClusterTestPrefix(topic)) {
                return false;
            }
        }
        return true;
    }

    public static boolean validate(String topic) {
        if (registereTopic.contains(topic)) {
            return false;
        }
        registereTopic.add(topic);
        if (Pradar.isClusterTestPrefix(topic)) {
            return false;
        }
        return true;
    }
}
