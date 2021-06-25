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
import java.util.List;

/**
 * 挡板
 *
 * @author shiyajian
 * create: 2020-09-17
 */
public class Guard implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 类全路径
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法参数的全路径
     * 如果为空，不判断参数；
     * 如果有多个实现，这类需要根据入参的对象进行查找；
     */
    private List<String> methodArgClasses;

    /**
     * groovy 脚本
     */
    private String codeScript;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeScript() {
        return codeScript;
    }

    public void setCodeScript(String codeScript) {
        this.codeScript = codeScript;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getMethodArgClasses() {
        return methodArgClasses;
    }

    public void setMethodArgClasses(List<String> methodArgClasses) {
        this.methodArgClasses = methodArgClasses;
    }
}
