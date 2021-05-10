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
package com.pamirs.pradar.pressurement.agent.shared.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @since 2020-04-02 10:27
 */
public class PradarSpringUtil {

    private static volatile DefaultListableBeanFactory defaultListableBeanFactory;

    public static void refreshBeanFactory(ApplicationContext applicationContextParam) throws BeansException {
        if (applicationContextParam == null) {
            return;
        }
        if (applicationContextParam.getAutowireCapableBeanFactory() == null) {
            return;
        }
        defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContextParam.getAutowireCapableBeanFactory();
    }

    public static void refreshBeanFactory(DefaultListableBeanFactory beanFactory) {
        defaultListableBeanFactory = beanFactory;
    }

    public static boolean isInit() {
        return defaultListableBeanFactory != null;
    }

    public <T> T getBean(Class<T> tClass) {
        return defaultListableBeanFactory.getBean(tClass);
    }

    public static DefaultListableBeanFactory getBeanFactory() {
        return defaultListableBeanFactory;
    }
}
