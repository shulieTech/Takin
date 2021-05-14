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
 * 瓶颈详情类
 *
 * @author shulie
 * @description
 * @create 2019-06-12 17:18:41
 */
public class BottleNeckDetailVo {

    /**
     * 基础资源负载及异常
     */
    private List<BottleNeckVo> basic;

    /**
     * 异步处理
     */
    private List<AsyncVo> async;

    /**
     * TPS/RT 稳定性
     */
    private List<StabilityVo> stability;

    /**
     * RT 响应时间
     */
    private List<RtVo> rt;

    /**
     * Gets the value of basic.
     *
     * @return the value of basic
     * @author shulie
     * @version 1.0
     */
    public List<BottleNeckVo> getBasic() {
        return basic;
    }

    /**
     * Sets the basic.
     *
     * <p>You can use getBasic() to get the value of basic</p>
     *
     * @param basic basic
     * @author shulie
     * @version 1.0
     */
    public void setBasic(List<BottleNeckVo> basic) {
        this.basic = basic;
    }

    /**
     * Gets the value of async.
     *
     * @return the value of async
     * @author shulie
     * @version 1.0
     */
    public List<AsyncVo> getAsync() {
        return async;
    }

    /**
     * Sets the async.
     *
     * <p>You can use getAsync() to get the value of async</p>
     *
     * @param async async
     * @author shulie
     * @version 1.0
     */
    public void setAsync(List<AsyncVo> async) {
        this.async = async;
    }

    /**
     * Gets the value of stability.
     *
     * @return the value of stability
     * @author shulie
     * @version 1.0
     */
    public List<StabilityVo> getStability() {
        return stability;
    }

    /**
     * Sets the stability.
     *
     * <p>You can use getStability() to get the value of stability</p>
     *
     * @param stability stability
     * @author shulie
     * @version 1.0
     */
    public void setStability(List<StabilityVo> stability) {
        this.stability = stability;
    }

    /**
     * Gets the value of rt.
     *
     * @return the value of rt
     * @author shulie
     * @version 1.0
     */
    public List<RtVo> getRt() {
        return rt;
    }

    /**
     * Sets the rt.
     *
     * <p>You can use getRt() to get the value of rt</p>
     *
     * @param rt rt
     * @author shulie
     * @version 1.0
     */
    public void setRt(List<RtVo> rt) {
        this.rt = rt;
    }

    @Override
    public String toString() {
        return "BottleNeckDetailVo{" +
            "basic=" + basic +
            ", async=" + async +
            ", stability=" + stability +
            ", rt=" + rt +
            '}';
    }
}
