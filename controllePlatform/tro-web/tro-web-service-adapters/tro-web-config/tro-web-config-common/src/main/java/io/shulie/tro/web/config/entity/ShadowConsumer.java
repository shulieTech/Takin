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

package io.shulie.tro.web.config.entity;

import java.io.Serializable;

import io.shulie.tro.web.config.enums.ShadowConsumerType;

/**
 * @author shiyajian
 * create: 2020-09-15
 */
public class ShadowConsumer implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private ShadowConsumerType type;

    private String group;

    private String topic;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShadowConsumerType getType() {
        return type;
    }

    public void setType(ShadowConsumerType type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
