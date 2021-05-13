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

package com.pamirs.tro.common.constant;

import lombok.Getter;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.tro.common.constant
 * @Date 2020-03-17 18:31
 */
@Getter
public enum JobEnum {
    /**
     * 0-quartz、1-elastic-job、2-xxl-job 后期维护到字典表中
     */
    QUARTZ("quartz"),
    ELASTIC_JOB("elastic-job"),
    XXL_JOB("xxl-job"),
    UNKNOW("unknow"),
    SPRING_QUARTZ("spring-quartz"),
    ;

    private String text;

    JobEnum(String text) {
        this.text = text;
    }

    public static JobEnum getJobByIndex(int index) {
        for (JobEnum jobEnum : JobEnum.values()) {
            if (jobEnum.ordinal() == index) {
                return jobEnum;
            }
        }
        return JobEnum.UNKNOW;
    }

    public static JobEnum getJobByText(String text) {
        for (JobEnum jobEnum : JobEnum.values()) {
            if (text.equals(jobEnum.getText())) {
                return jobEnum;
            }
        }
        return JobEnum.UNKNOW;
    }
}
