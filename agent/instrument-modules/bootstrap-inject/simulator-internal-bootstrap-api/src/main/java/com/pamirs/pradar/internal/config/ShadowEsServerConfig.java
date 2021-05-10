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
package com.pamirs.pradar.internal.config;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ShadowEsServerConfig {

    private List<String> businessNodes;
    private List<String> performanceTestNodes;
    private String bizClusterName;
    private String ptClusterName;

    public List<String> getBusinessNodes() {
        return businessNodes;
    }

    public void setBusinessNodes(List<String> businessNodes) {
        this.businessNodes = businessNodes;
    }

    public List<String> getPerformanceTestNodes() {
        return performanceTestNodes;
    }

    public String getPtClusterName() {
        return ptClusterName;
    }

    public void setPerformanceTestNodes(List<String> performanceTestNodes) {
        this.performanceTestNodes = performanceTestNodes;
    }

    public ShadowEsServerConfig(List<String> businessNodes
        , List<String> performanceTestNodes
        , String bizClusterName
        , String ptClusterName) {
        this.businessNodes = businessNodes;
        this.performanceTestNodes = performanceTestNodes;
        this.bizClusterName = bizClusterName;
        this.ptClusterName = ptClusterName;
    }

    public ShadowEsServerConfig() {
    }

    public String identifyKey() {
        String[] businessNodeArray = sort(businessNodes);
        String[] performanceTestNodeArray = sort(performanceTestNodes);
        return join(",", businessNodeArray) + "|" + join(",", performanceTestNodeArray);
    }

    public boolean matchBusinessNodes(List<String> nodesAsString) {
        String[] nodeArray = sort(nodesAsString);
        String[] businessNodeArray = sort(businessNodes);
        return Arrays.equals(nodeArray, businessNodeArray);
    }

    private String[] sort(List<String> list) {
        String[] array = new String[list.size()];
        list.toArray(array);
        Arrays.sort(array, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return array;
    }

    public static String join(String separator, String... strs) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : strs) {
            if (!first) {
                sb.append(separator);
            }
            sb.append(s);
            first = false;
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "ShadowEsServerConfig{" +
            "businessNodes='" + businessNodes + '\'' +
            ", performanceTestNodes=" + performanceTestNodes +
            '}';
    }

}
