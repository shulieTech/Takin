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

package io.shulie.tro.web.app.response.perfomanceanaly;

import java.io.Serializable;

import lombok.Data;

/**
 * @ClassName ThreadStatusResponse
 * @Description 线程状态
 * @Author qianshui
 * @Date 2020/11/4 上午11:26
 */
@Data
public class ThreadStatusResponse implements Serializable, Comparable<ThreadStatusResponse> {

    private static final long serialVersionUID = -5845702420534131372L;

    private String status;

    private Integer count;

    @Override
    public int compareTo(ThreadStatusResponse o) {
        int diff = o.getCount() - this.getCount();
        if (diff == 0) {
            return this.getStatus().compareTo(o.getStatus());
        }
        return diff;
    }
}
