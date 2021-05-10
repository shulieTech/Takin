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
package com.pamirs.pradar.pressurement.agent.shared.util;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public abstract class RocketMQUtils {

    public static boolean equalsNameSrvs(String namesrvs1, String namesrvs2) {
        if (namesrvs1 == null ? namesrvs2 == null : namesrvs1.equals(namesrvs2)) {
            return true;
        }
        List<String> namesrvs1List = Arrays.asList(StringUtils.split(namesrvs1, ';'));
        HashSet<String> namesrvs1Set = new HashSet<String>(namesrvs1List);

        List<String> namesrvs2List = Arrays.asList(StringUtils.split(namesrvs2, ';'));
        HashSet<String> namesrvs2Set = new HashSet<String>(namesrvs2List);
        return namesrvs1Set.equals(namesrvs2Set);
    }

    public static boolean equalsNameSrvs(List<String> namesrvs1, String namesrvs2) {
        if (namesrvs1 == null && namesrvs2 == null) {
            return true;
        }
        List<String> namesrvs2List = Arrays.asList(StringUtils.split(namesrvs2, ';'));
        HashSet<String> namesrvs2Set = new HashSet<String>(namesrvs2List);
        return new TreeSet<String>(namesrvs1).equals(namesrvs2Set);
    }

}
