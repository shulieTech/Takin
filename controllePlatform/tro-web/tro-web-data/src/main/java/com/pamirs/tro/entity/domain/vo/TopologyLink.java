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

package com.pamirs.tro.entity.domain.vo;

/**
 * 邵奇
 *
 * @author 298403
 */
public class TopologyLink {

    /**
     * 开始链路id
     */
    private String from;

    /**
     * 结束链路id
     */
    private String to;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "TopologyLink{" +
            "from='" + from + '\'' +
            ", to='" + to + '\'' +
            '}';
    }

    /**
     * 重写hashcode
     *
     * @return
     */
    @Override
    public int hashCode() {
        return from.hashCode() * to.hashCode();
    }

    /**
     * 判断是否相等
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj.getClass() == TopologyLink.class) {
            TopologyLink vo = (TopologyLink)obj;
            // 比较每个属性的值 一致时才返回true
            if (vo.from.equals(this.from) && vo.to.equals(this.to)) {
                return true;
            }
        }
        return false;
    }

}
