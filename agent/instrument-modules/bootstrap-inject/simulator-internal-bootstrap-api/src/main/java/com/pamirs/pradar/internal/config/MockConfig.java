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
package com.pamirs.pradar.internal.config;

import java.io.Serializable;
import java.util.List;

/**
 * mock 挡板的配置
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/24 6:15 下午
 */
public class MockConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    public final static int MOCK_SCRIPT = 1;
    public final static int MOCK_CLASS = 2;

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

    /**
     * 脚本类型，1:挡板 2:类
     */
    private int type = MOCK_SCRIPT;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getKey() {
        if (methodArgClasses == null || methodArgClasses.isEmpty()) {
            return className + '#' + methodName;
        }
        return className + '#' + methodName + '(' + methodArgClasses.toString() + ")";
    }

    @Override
    public String toString() {
        return "MockConfig{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", type='" + type + '\'' +
                ", methodArgClasses=" + methodArgClasses +
                ", codeScript='" + codeScript + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MockConfig that = (MockConfig) o;

        if (className != null ? !className.equals(that.className) : that.className != null) return false;
        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;
        if (methodArgClasses != null ? !methodArgClasses.equals(that.methodArgClasses) : that.methodArgClasses != null)
            return false;
        return codeScript != null ? codeScript.equals(that.codeScript) : that.codeScript == null;
    }

    @Override
    public int hashCode() {
        int result = className != null ? className.hashCode() : 0;
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        result = 31 * result + (methodArgClasses != null ? methodArgClasses.hashCode() : 0);
        result = 31 * result + (codeScript != null ? codeScript.hashCode() : 0);
        return result;
    }
}
