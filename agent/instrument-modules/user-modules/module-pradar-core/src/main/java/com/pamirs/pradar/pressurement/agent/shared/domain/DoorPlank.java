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
package com.pamirs.pradar.pressurement.agent.shared.domain;


/**
 * 门的状态对象
 */
public class DoorPlank implements java.io.Serializable {


    /**
     * <p>{@code CharSequenceUtils} instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    private static final long serialVersionUID = 1L;

    /**
     * 门的状态
     */
    private String status;

    /**
     * 关门原因
     */
    private String closeReason;

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the closeReason
     */
    public String getCloseReason() {
        return closeReason;
    }

    /**
     * @param closeReason the closeReason to set
     */
    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }


    /**
     * <p>{@code CharSequenceUtils} instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public String toString() {
        return "DoorPlank [status=" + status + ", closeReason=" + closeReason
                + "]";
    }


}
