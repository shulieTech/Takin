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
package com.shulie.instrument.simulator.api.instrument;

import com.shulie.instrument.simulator.api.ExtensionModule;

/**
 * 增强模板
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/10 9:47 上午
 */
public interface EnhanceTemplate {

    /**
     * 对目标类进行增强
     *
     * @param module    模块
     * @param className 需要增强的类名
     * @param callback  增强的回调函数
     * @return 返回增强类对象
     */
    InstrumentClass enhance(ExtensionModule module, String className, EnhanceCallback callback);

    /**
     * 对目标类进行增强
     *
     * @param module     模块
     * @param callback   增强的回调函数
     * @param classNames 需要增强的类名
     * @return 返回增强类对象
     */
    InstrumentClass enhance(ExtensionModule module, EnhanceCallback callback, String... classNames);

    /**
     * 对目标父类所有的子类增强
     *
     * @param module         模块
     * @param superClassName 需要增强的所有该父类的子类
     * @param callback       增强的回调函数
     * @return 返回增强对象
     */
    InstrumentClass enhanceWithSuperClass(ExtensionModule module, String superClassName, EnhanceCallback callback);

    /**
     * 对目标父类所有的子类增强
     *
     * @param module          模块
     * @param callback        增强的回调函数
     * @param superClassNames 需要增强的所有该父类的子类
     * @return 返回增强对象
     */
    InstrumentClass enhanceWithSuperClass(ExtensionModule module, EnhanceCallback callback, String... superClassNames);

    /**
     * 对目标接口所有的子类增强
     *
     * @param module             模块
     * @param interfaceClassName 需要增强的所有该接口的子类
     * @param callback           增强的回调函数
     * @return 返回增强对象
     */
    InstrumentClass enhanceWithInterface(ExtensionModule module, String interfaceClassName, EnhanceCallback callback);

    /**
     * 对目标接口所有的子类增强
     *
     * @param module              模块
     * @param interfaceClassNames 需要增强的所有该接口的子类
     * @param callback            增强的回调函数
     * @return 返回增强对象
     */
    InstrumentClass enhanceWithInterface(ExtensionModule module, EnhanceCallback callback, String... interfaceClassNames);

}
