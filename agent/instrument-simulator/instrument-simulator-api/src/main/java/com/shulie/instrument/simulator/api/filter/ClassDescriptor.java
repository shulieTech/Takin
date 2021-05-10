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
package com.shulie.instrument.simulator.api.filter;

/**
 * 类描述
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/28 12:05 下午
 */
public interface ClassDescriptor {
    /**
     * 返回访问标识符
     *
     * @return
     */
    int getAccess();

    /**
     * 获取类名称
     *
     * @return
     */
    String getClassName();

    /**
     * 返回父类型class名称
     *
     * @return
     */
    String getSuperClassTypeJavaClassName();

    /**
     * 返回所有的接口类型
     *
     * @return
     */
    String[] getInterfaceTypeJavaClassNameArray();

    /**
     * 返回所有的注解类型
     *
     * @return
     */
    String[] getAnnotationTypeJavaClassNameArray();
}
