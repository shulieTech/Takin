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

/**
 * 瓶颈内容类
 *
 * @author shulie
 * @description
 * @create 2019-06-12 17:09:36
 */
public class BottleNeckVo {
    /**
     * 瓶颈产生时间
     */
    private String createTime;
    /**
     * 瓶颈内容
     */
    private String bottleNeckContent;

    /**
     * Gets the value of createTime.
     *
     * @return the value of createTime
     * @author shulie
     * @version 1.0
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * Sets the createTime.
     *
     * <p>You can use getCreateTime() to get the value of createTime</p>
     *
     * @param createTime createTime
     * @author shulie
     * @version 1.0
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * Gets the value of bottleNeckContent.
     *
     * @return the value of bottleNeckContent
     * @author shulie
     * @version 1.0
     */
    public String getBottleNeckContent() {
        return bottleNeckContent;
    }

    /**
     * Sets the bottleNeckContent.
     *
     * <p>You can use getBottleNeckContent() to get the value of bottleNeckContent</p>
     *
     * @param bottleNeckContent bottleNeckContent
     * @author shulie
     * @version 1.0
     */
    public void setBottleNeckContent(String bottleNeckContent) {
        this.bottleNeckContent = bottleNeckContent;
    }

    @Override
    public String toString() {
        return "BottleNeckVo{" +
            "createTime='" + createTime + '\'' +
            ", bottleNeckContent='" + bottleNeckContent + '\'' +
            '}';
    }
}

