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
package com.shulie.instrument.simulator.api;

/**
 * 仿真器环境模块
 * <p>
 * 要求模块的实现必须符合JDK6的SPI规范
 * </p>
 * <ol>
 * <li>必须实现{@link ExtensionModule}接口</li>
 * <li>必须拥有无参构造函数</li>
 * <li>必须在{@code META-INF/services/com.shulie.instrument.simulator.api.ExtensionModule}文件中注册</li>
 * </ol>
 * <p>
 * 模块加载时，将会调用模块实现类的默认构造函数完成模块类的实例化。但类完成了实例化并不代表模块加载完成。
 * </p>
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface ExtensionModule {

}
