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

package com.pamirs.tro.entity.domain.query.whitelist;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AgentWhiteList {

    @JsonProperty("INTERFACE_NAME")
    private String interfaceName;

    @JsonProperty("TYPE")
    private String type;

    @JsonProperty("SOURCETYPE")
    private String sourceType;

    @JsonProperty("WLISTID")
    private Long wlistId;


    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        AgentWhiteList that = (AgentWhiteList)o;
        return Objects.equals(interfaceName, that.interfaceName) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(interfaceName, type);
    }
}
