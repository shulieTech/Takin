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
 * 统计产生瓶颈的应用数量
 *
 * @author shulie
 * @description
 * @create 2019-06-12 17:32:33
 */
public class BottleLevelCountVo {

    /**
     * 严重瓶颈应用数量
     */
    private int seriousBottle;
    /**
     * 普通瓶颈应用数量
     */
    private int bottle;
    /**
     * 正常应用数量
     */
    private int normal;

    /**
     * Gets the value of seriousBottle.
     *
     * @return the value of seriousBottle
     * @author shulie
     * @version 1.0
     */
    public int getSeriousBottle() {
        return seriousBottle;
    }

    /**
     * Sets the seriousBottle.
     *
     * <p>You can use getSeriousBottle() to get the value of seriousBottle</p>
     *
     * @param seriousBottle seriousBottle
     * @author shulie
     * @version 1.0
     */
    public void setSeriousBottle(int seriousBottle) {
        this.seriousBottle = seriousBottle;
    }

    /**
     * Gets the value of bottle.
     *
     * @return the value of bottle
     * @author shulie
     * @version 1.0
     */
    public int getBottle() {
        return bottle;
    }

    /**
     * Sets the bottle.
     *
     * <p>You can use getBottle() to get the value of bottle</p>
     *
     * @param bottle bottle
     * @author shulie
     * @version 1.0
     */
    public void setBottle(int bottle) {
        this.bottle = bottle;
    }

    /**
     * Gets the value of normal.
     *
     * @return the value of normal
     * @author shulie
     * @version 1.0
     */
    public int getNormal() {
        return normal;
    }

    /**
     * Sets the normal.
     *
     * <p>You can use getNormal() to get the value of normal</p>
     *
     * @param normal normal
     * @author shulie
     * @version 1.0
     */
    public void setNormal(int normal) {
        this.normal = normal;
    }

    @Override
    public String toString() {
        return "BottleLevelCountVo{" +
            "seriousBottle=" + seriousBottle +
            ", bottle=" + bottle +
            ", normal=" + normal +
            '}';
    }
}
