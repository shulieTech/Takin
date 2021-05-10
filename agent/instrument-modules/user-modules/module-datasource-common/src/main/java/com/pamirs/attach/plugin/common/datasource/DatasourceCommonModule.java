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
package com.pamirs.attach.plugin.common.datasource;

import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import org.kohsuke.MetaInfServices;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/11 7:17 下午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "datasource-common", version = "1.0.0", author = "xiaobin@shulie.io",description = "数据源通用依赖模块,提供给各个数据源模块依赖")
public class DatasourceCommonModule implements ExtensionModule {

}
