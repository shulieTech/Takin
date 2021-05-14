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

package io.shulie.tro.web.app.utils;

import java.io.InputStream;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;

/**
 * @Auther: vernon
 * @Date: 2019/12/17 11:25
 * @Description:
 */
public class ConfigurationUtils {

    /**
     * get  Configuration
     *
     * @param configFile Configuration file path
     * @return Configuration instance
     * @throws ConfigurationException
     */
    public static Configuration getConfiguration(String configFile) throws ConfigurationException {
        if (StringUtils.isEmpty(configFile)) {
            return null;
        }
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile);
        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.load(in);
        return propertiesConfiguration;
    }

}
