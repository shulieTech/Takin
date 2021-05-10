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
package com.shulie.instrument.simulator.module.model.vmoption;

import com.sun.management.VMOption;

import java.io.Serializable;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/3 5:23 下午
 */
public class VmOption implements Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数值
     */
    private String value;

    /**
     * 是否写
     */
    private boolean writeable;

    /**
     * 参数区域,表示是哪个类型的参数
     */
    private String origin;

    public VmOption(VMOption vmOption) {
        if (vmOption == null) {
            return;
        }
        this.name = vmOption.getName();
        this.value = vmOption.getValue();
        this.writeable = vmOption.isWriteable();
        this.origin = vmOption.getOrigin().name();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isWriteable() {
        return writeable;
    }

    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", writeable=" + writeable +
                ", origin='" + origin + '\'' +
                '}';
    }
}
