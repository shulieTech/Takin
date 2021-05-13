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

package com.pamirs.tro.common.util;

import java.io.Serializable;

import lombok.Data;

/**
 * @author shulie
 * @package: com.pamirs.tro.common.util
 * @Date 2019-06-25 17:15
 */
@Data
public class ClassLoaderBean implements Serializable {

    private ClassLoader classLoader;

    private Class<?> loadClass;
}
