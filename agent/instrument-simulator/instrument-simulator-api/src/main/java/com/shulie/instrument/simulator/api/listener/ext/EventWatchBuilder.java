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
package com.shulie.instrument.simulator.api.listener.ext;

import com.shulie.instrument.simulator.api.filter.Filter;
import com.shulie.instrument.simulator.api.resource.ModuleEventWatcher;
import com.shulie.instrument.simulator.api.util.CollectionUtils;
import com.shulie.instrument.simulator.api.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import static java.util.regex.Pattern.quote;

/**
 * 事件观察者类构建器
 * <p>
 * 方便构建事件观察者，原有的{@link Filter}是一个比较原始、暴力、直接的接口，虽然很万能，但要精巧的构造门槛很高！
 * 这里设计一个Builder对是为了降低实现的门槛
 * </p>
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public class EventWatchBuilder {

    // -------------------------- 这里开始实现 --------------------------


    private final ModuleEventWatcher moduleEventWatcher;
    private final PatternType patternType;
    private List<ClassMatchBuilder> bfClasses = new ArrayList<ClassMatchBuilder>();

    /**
     * 构造事件观察者构造器(通配符匹配模式)
     *
     * @param moduleEventWatcher 模块事件观察者
     */
    public EventWatchBuilder(final ModuleEventWatcher moduleEventWatcher) {
        this(moduleEventWatcher, PatternType.WILDCARD);
    }

    /**
     * 构造事件观察者构造器
     *
     * @param moduleEventWatcher 模块事件观察者
     * @param patternType        模版匹配模式
     */
    public EventWatchBuilder(final ModuleEventWatcher moduleEventWatcher,
                             final PatternType patternType) {
        this.moduleEventWatcher = moduleEventWatcher;
        this.patternType = patternType;
    }

    /**
     * 匹配任意类
     * <p>
     * 等同于{@code onClass("*")}
     * </p>
     *
     * @return IBuildingForClass
     */
    public IClassMatchBuilder onAnyClass() {
        switch (patternType) {
            case REGEX:
                return onClass(".*");
            case WILDCARD:
            default:
                return onClass("*");
        }
    }

    /**
     * 将字符串数组转换为正则表达式字符串数组
     *
     * @param stringArray 目标字符串数组
     * @return 正则表达式字符串数组
     */
    private static String[] toRegexQuoteArray(final String[] stringArray) {
        if (null == stringArray) {
            return null;
        }
        final String[] regexQuoteArray = new String[stringArray.length];
        for (int index = 0; index < stringArray.length; index++) {
            regexQuoteArray[index] = quote(stringArray[index]);
        }
        return regexQuoteArray;
    }

    /**
     * 匹配指定类
     * <p>
     * 等同于{@code onClass(clazz.getCanonicalName())}
     * </p>
     *
     * @param clazz 指定Class，这里的Class可以忽略ClassLoader的差异。
     *              这里主要取Class的类名
     * @return IBuildingForClass
     */
    public IClassMatchBuilder onClass(final Class<?>... clazz) {
        switch (patternType) {
            case REGEX: {
                return onClass(toRegexQuoteArray(StringUtil.getJavaClassNameArray(clazz)));
            }
            case WILDCARD:
            default:
                return onClass(StringUtil.getJavaClassNameArray(clazz));
        }

    }

    /**
     * 模版匹配类名称(包含包名)
     * <p>
     * 例子：
     * <ul>
     * <li>"com.shulie.*"</li>
     * <li>"java.util.ArrayList"</li>
     * </ul>
     *
     * @param pattern 类名匹配模版
     * @return IBuildingForClass
     */
    public IClassMatchBuilder onClass(final String pattern) {
        return CollectionUtils.add(bfClasses, new ClassMatchBuilder(moduleEventWatcher, patternType, pattern));
    }

    /**
     * 模版匹配类名称(包含包名)
     * <p>
     * 例子：
     * <ul>
     * <li>"com.shulie.*"</li>
     * <li>"java.util.ArrayList"</li>
     * </ul>
     *
     * @param pattern 类名匹配模版
     * @return IBuildingForClass
     */
    public IClassMatchBuilder onClass(final String... pattern) {
        return CollectionUtils.add(bfClasses, new ClassMatchBuilder(moduleEventWatcher, patternType, pattern));
    }
}
