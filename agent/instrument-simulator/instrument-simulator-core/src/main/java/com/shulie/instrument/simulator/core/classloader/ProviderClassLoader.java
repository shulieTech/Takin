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
package com.shulie.instrument.simulator.core.classloader;

import com.shulie.instrument.simulator.api.annotation.Stealth;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 服务提供库ClassLoader
 *
 */
@Stealth
public class ProviderClassLoader extends RoutingURLClassLoader {

    public ProviderClassLoader(final File providerJarFile,
                               final ClassLoader simulatorClassLoader) throws IOException {
        super(
                new URL[]{new URL("file:" + providerJarFile.getCanonicalPath())},
                new Routing(
                        simulatorClassLoader,
                        "^com\\.shulie\\.instrument\\.simulator\\.api\\..*",
                        "^com\\.shulie\\.instrument\\.simulator\\.spi\\..*",
                        "^com\\.alibaba\\.fastjson\\..*",
                        "^com\\.google\\.common\\..*",
                        "^org\\.apache\\.commons\\.lang3\\..*",
                        "^org\\.codehaus\\.groovy\\..*",
                        "^groovy\\..*",
                        "^org\\.slf4j\\..*",
                        "^ch\\.qos\\.logback\\..*",
                        "^org\\.objectweb\\.asm\\..*",
                        "^javax\\.annotation\\.Resource.*$"
                )
        );
    }
}
