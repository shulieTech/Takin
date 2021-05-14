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
package com.pamirs.pradar.exception;

/**
 * 压测错误，可能在压测流量时产生，也可能在非压测流量时产生，此错误会强行中断流程
 * 使用该错误时需要注意，不能随便乱用，不然可能会影响真实业务流量
 *
 * @since 2020-05-14
 */
public class PressureMeasureError extends Error {
    private boolean isClusterTest;

    public PressureMeasureError(Throwable e) {
        super(e);
    }

    public PressureMeasureError(Throwable e, boolean isClusterTest) {
        super(e);
        this.isClusterTest = isClusterTest;
    }

    public boolean isClusterTest() {
        if (isClusterTest) {
            return true;
        }
        return false;
    }

    /**
     * 抛出此错误时不管是压测流量还是非压测流量都会中断业务流程
     *
     * @param errorMsg 错误信息
     */
    public PressureMeasureError(String errorMsg) {
        super(errorMsg);
    }

    /**
     * 抛出此错误时不管是压测流量还是非压测流量都会中断业务流程
     *
     * @param errorMsg      错误信息
     * @param isClusterTest 是否是压测流量
     */
    public PressureMeasureError(String errorMsg, boolean isClusterTest) {
        super(errorMsg);
        this.isClusterTest = isClusterTest;
    }

    public PressureMeasureError(String message, Throwable cause) {
        super(message, cause);
    }

    public PressureMeasureError(String message, Throwable cause, boolean isClusterTest) {
        super(message, cause);
        this.isClusterTest = isClusterTest;
    }
}
