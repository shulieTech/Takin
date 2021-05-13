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

package io.shulie.tro.web.app.response.linkmanage;

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/10/23 2:33 下午
 * @Description:
 */
@Data
public class SupportStatusResponse {
    /**
     * 0：未支持 1：未收录 2:已支持 3:无需支持
     */
    private String label;
    private Integer value;

    public static SupportStatusResponse buildStatus(Integer value) {
        SupportStatusResponse supportStatusResponse = new SupportStatusResponse();
        supportStatusResponse.setValue(value);
        switch (value) {
            case 0:
                supportStatusResponse.setLabel("未支持");
                break;
            case 1:
                supportStatusResponse.setLabel("未收录");
                break;
            case 2:
                supportStatusResponse.setLabel("已支持");
                break;
            case 3:
                supportStatusResponse.setLabel("无需支持");
                break;
            default:
        }
        return supportStatusResponse;
    }
}
