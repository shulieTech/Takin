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

package com.pamirs.tro.entity.domain.vo.bottleneck;

import java.util.List;

/**
 * RT 响应时间
 *
 * @author shulie
 * @description
 * @create 2019-06-12 17:27:49
 */
public class RtVo {
    /**
     * 1,页面
     * 2,接口
     */
    private String type;
    /**
     * 瓶颈内容
     */
    private List<BottleNeckVo> summary;

    public RtVo() {
    }

    public RtVo(String type, List<BottleNeckVo> summary) {
        this.type = type;
        this.summary = summary;
    }

    /**
     * Gets the value of type.
     *
     * @return the value of type
     * @author shulie
     * @version 1.0
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * <p>You can use getType() to get the value of type</p>
     *
     * @param type type
     * @author shulie
     * @version 1.0
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the value of summary.
     *
     * @return the value of summary
     * @author shulie
     * @version 1.0
     */
    public List<BottleNeckVo> getSummary() {
        return summary;
    }

    /**
     * Sets the summary.
     *
     * <p>You can use getSummary() to get the value of summary</p>
     *
     * @param summary summary
     * @author shulie
     * @version 1.0
     */
    public void setSummary(List<BottleNeckVo> summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "RtVo{" +
            "type='" + type + '\'' +
            ", summary=" + summary +
            '}';
    }
}
