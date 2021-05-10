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

import java.util.Collection;

/**
 * 增强类
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/11 5:51 下午
 */
public interface InstrumentClass {
    /**
     * 包含被Bootstrap所加载的类
     */
    void includeBootstrap();

    /**
     * 是否包含被Bootstrap所加载的类
     *
     * @param isIncludeBootstrap TRUE:包含Bootstrap;FALSE:不包含Bootstrap;
     * @see #includeBootstrap()
     */
    void isIncludeBootstrap(boolean isIncludeBootstrap);

    /**
     * 获取方法
     *
     * @param methodName     方法名称
     * @param parameterTypes 参数列表
     * @return 返回InstrumentMethod
     */
    InstrumentMethod getDeclaredMethod(String methodName, String... parameterTypes);

    /**
     * 获取方法，匹配指定的参数
     *
     * @param methodName     方法名称
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @return
     */
    InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1);

    /**
     * 获取方法，匹配指定的参数
     *
     * @param methodName     方法名称
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @param index2         参数下标2
     * @param parameterType2 参数类型匹配规则2
     * @return
     */
    InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2);

    /**
     * 获取方法，匹配指定的参数
     *
     * @param methodName     方法名称
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @param index2         参数下标2
     * @param parameterType2 参数类型匹配规则2
     * @param index3         参数下标3
     * @param parameterType3 参数类型匹配规则3
     * @return
     */
    InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2,
                                       int index3, String parameterType3);

    /**
     * 获取方法，匹配指定的参数
     *
     * @param methodName     方法名称
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @param index2         参数下标2
     * @param parameterType2 参数类型匹配规则2
     * @param index3         参数下标3
     * @param parameterType3 参数类型匹配规则3
     * @param index4         参数下标4
     * @param parameterType4 参数类型匹配规则4
     * @return
     */
    InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2,
                                       int index3, String parameterType3, int index4, String parameterType4);

    /**
     * 获取方法，匹配指定的参数
     *
     * @param methodName     方法名称
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @param index2         参数下标2
     * @param parameterType2 参数类型匹配规则2
     * @param index3         参数下标3
     * @param parameterType3 参数类型匹配规则3
     * @param index4         参数下标4
     * @param parameterType4 参数类型匹配规则4
     * @param index5         参数下标5
     * @param parameterType5 参数类型匹配规则5
     * @return
     */
    InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2,
                                       int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5);

    /**
     * 获取方法，匹配指定的参数
     *
     * @param methodName     方法名称
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @param index2         参数下标2
     * @param parameterType2 参数类型匹配规则2
     * @param index3         参数下标3
     * @param parameterType3 参数类型匹配规则3
     * @param index4         参数下标4
     * @param parameterType4 参数类型匹配规则4
     * @param index5         参数下标5
     * @param parameterType5 参数类型匹配规则5
     * @param index6         参数下标6
     * @param parameterType6 参数类型匹配规则6
     * @return
     */
    InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2,
                                       int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5,
                                       int index6, String parameterType6);


    /**
     * 获取方法，匹配指定的参数
     *
     * @param methodName     方法名称
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @param index2         参数下标2
     * @param parameterType2 参数类型匹配规则2
     * @param index3         参数下标3
     * @param parameterType3 参数类型匹配规则3
     * @param index4         参数下标4
     * @param parameterType4 参数类型匹配规则4
     * @param index5         参数下标5
     * @param parameterType5 参数类型匹配规则5
     * @param index6         参数下标6
     * @param parameterType6 参数类型匹配规则6
     * @param index7         参数下标7
     * @param parameterType7 参数类型匹配规则7
     * @return
     */
    InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2,
                                       int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5,
                                       int index6, String parameterType6, int index7, String parameterType7);

    /**
     * 获取方法，匹配指定的参数
     *
     * @param methodName     方法名称
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @param index2         参数下标2
     * @param parameterType2 参数类型匹配规则2
     * @param index3         参数下标3
     * @param parameterType3 参数类型匹配规则3
     * @param index4         参数下标4
     * @param parameterType4 参数类型匹配规则4
     * @param index5         参数下标5
     * @param parameterType5 参数类型匹配规则5
     * @param index6         参数下标6
     * @param parameterType6 参数类型匹配规则6
     * @param index7         参数下标7
     * @param parameterType7 参数类型匹配规则7
     * @param index8         参数下标8
     * @param parameterType7 参数类型匹配规则8
     * @return
     */
    InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2,
                                       int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5,
                                       int index6, String parameterType6, int index7, String parameterType7, int index8, String parameterType8);

    /**
     * 获取方法，匹配指定的参数
     *
     * @param methodName     方法名称
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @param index2         参数下标2
     * @param parameterType2 参数类型匹配规则2
     * @param index3         参数下标3
     * @param parameterType3 参数类型匹配规则3
     * @param index4         参数下标4
     * @param parameterType4 参数类型匹配规则4
     * @param index5         参数下标5
     * @param parameterType5 参数类型匹配规则5
     * @param index6         参数下标6
     * @param parameterType6 参数类型匹配规则6
     * @param index7         参数下标7
     * @param parameterType7 参数类型匹配规则7
     * @param index8         参数下标8
     * @param parameterType7 参数类型匹配规则8
     * @param index9         参数下标9
     * @param parameterType9 参数类型匹配规则9
     * @return
     */
    InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2,
                                       int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5,
                                       int index6, String parameterType6, int index7, String parameterType7, int index8, String parameterType8,
                                       int index9, String parameterType9);

    /**
     * 获取方法，匹配指定的参数
     *
     * @param methodName      方法名称
     * @param index1          参数下标1
     * @param parameterType1  参数类型匹配规则1
     * @param index2          参数下标2
     * @param parameterType2  参数类型匹配规则2
     * @param index3          参数下标3
     * @param parameterType3  参数类型匹配规则3
     * @param index4          参数下标4
     * @param parameterType4  参数类型匹配规则4
     * @param index5          参数下标5
     * @param parameterType5  参数类型匹配规则5
     * @param index6          参数下标6
     * @param parameterType6  参数类型匹配规则6
     * @param index7          参数下标7
     * @param parameterType7  参数类型匹配规则7
     * @param index8          参数下标8
     * @param parameterType7  参数类型匹配规则8
     * @param index9          参数下标9
     * @param parameterType9  参数类型匹配规则9
     * @param index10         参数下标10
     * @param parameterType10 参数类型匹配规则10
     * @return
     */
    InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2,
                                       int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5,
                                       int index6, String parameterType6, int index7, String parameterType7, int index8, String parameterType8,
                                       int index9, String parameterType9, int index10, String parameterType10);

    /**
     * 获取方法
     *
     * @param methodNames 方法名称列表
     * @return 返回InstrumentMethod
     */
    InstrumentMethod getDeclaredMethods(Collection<String> methodNames);

    /**
     * 获取方法
     *
     * @param methodNames 方法名称列表
     * @return 返回InstrumentMethod
     */
    InstrumentMethod getDeclaredMethods(String... methodNames);

    /**
     * 获取构造方法
     *
     * @param parameterTypes 参数列表
     * @return 返回构造函数InstrumentMethod
     */
    InstrumentMethod getConstructor(String... parameterTypes);

    /**
     * 获取构造方法，匹配指定的参数
     *
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @return
     */
    InstrumentMethod getConstructor(int index1, String parameterType1);

    /**
     * 获取构造方法，匹配指定的参数
     *
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @param index2         参数下标2
     * @param parameterType2 参数类型匹配规则2
     * @return
     */
    InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2);

    /**
     * 获取构造方法，匹配指定的参数
     *
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @param index2         参数下标2
     * @param parameterType2 参数类型匹配规则2
     * @param index3         参数下标3
     * @param parameterType3 参数类型匹配规则3
     * @return
     */
    InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2,
                                    int index3, String parameterType3);

    /**
     * 获取构造方法，匹配指定的参数
     *
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @param index2         参数下标2
     * @param parameterType2 参数类型匹配规则2
     * @param index3         参数下标3
     * @param parameterType3 参数类型匹配规则3
     * @param index4         参数下标4
     * @param parameterType4 参数类型匹配规则4
     * @return
     */
    InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2,
                                    int index3, String parameterType3, int index4, String parameterType4);

    /**
     * 获取构造方法，匹配指定的参数
     *
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @param index2         参数下标2
     * @param parameterType2 参数类型匹配规则2
     * @param index3         参数下标3
     * @param parameterType3 参数类型匹配规则3
     * @param index4         参数下标4
     * @param parameterType4 参数类型匹配规则4
     * @param index5         参数下标5
     * @param parameterType5 参数类型匹配规则5
     * @return
     */
    InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2,
                                    int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5);

    /**
     * 获取构造方法，匹配指定的参数
     *
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @param index2         参数下标2
     * @param parameterType2 参数类型匹配规则2
     * @param index3         参数下标3
     * @param parameterType3 参数类型匹配规则3
     * @param index4         参数下标4
     * @param parameterType4 参数类型匹配规则4
     * @param index5         参数下标5
     * @param parameterType5 参数类型匹配规则5
     * @param index6         参数下标6
     * @param parameterType6 参数类型匹配规则6
     * @return
     */
    InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2,
                                    int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5,
                                    int index6, String parameterType6);


    /**
     * 获取构造方法，匹配指定的参数
     *
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @param index2         参数下标2
     * @param parameterType2 参数类型匹配规则2
     * @param index3         参数下标3
     * @param parameterType3 参数类型匹配规则3
     * @param index4         参数下标4
     * @param parameterType4 参数类型匹配规则4
     * @param index5         参数下标5
     * @param parameterType5 参数类型匹配规则5
     * @param index6         参数下标6
     * @param parameterType6 参数类型匹配规则6
     * @param index7         参数下标7
     * @param parameterType7 参数类型匹配规则7
     * @return
     */
    InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2,
                                    int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5,
                                    int index6, String parameterType6, int index7, String parameterType7);

    /**
     * 获取构造方法，匹配指定的参数
     *
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @param index2         参数下标2
     * @param parameterType2 参数类型匹配规则2
     * @param index3         参数下标3
     * @param parameterType3 参数类型匹配规则3
     * @param index4         参数下标4
     * @param parameterType4 参数类型匹配规则4
     * @param index5         参数下标5
     * @param parameterType5 参数类型匹配规则5
     * @param index6         参数下标6
     * @param parameterType6 参数类型匹配规则6
     * @param index7         参数下标7
     * @param parameterType7 参数类型匹配规则7
     * @param index8         参数下标8
     * @param parameterType7 参数类型匹配规则8
     * @return
     */
    InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2,
                                    int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5,
                                    int index6, String parameterType6, int index7, String parameterType7, int index8, String parameterType8);

    /**
     * 获取构造方法，匹配指定的参数
     *
     * @param index1         参数下标1
     * @param parameterType1 参数类型匹配规则1
     * @param index2         参数下标2
     * @param parameterType2 参数类型匹配规则2
     * @param index3         参数下标3
     * @param parameterType3 参数类型匹配规则3
     * @param index4         参数下标4
     * @param parameterType4 参数类型匹配规则4
     * @param index5         参数下标5
     * @param parameterType5 参数类型匹配规则5
     * @param index6         参数下标6
     * @param parameterType6 参数类型匹配规则6
     * @param index7         参数下标7
     * @param parameterType7 参数类型匹配规则7
     * @param index8         参数下标8
     * @param parameterType7 参数类型匹配规则8
     * @param index9         参数下标9
     * @param parameterType9 参数类型匹配规则9
     * @return
     */
    InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2,
                                    int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5,
                                    int index6, String parameterType6, int index7, String parameterType7, int index8, String parameterType8,
                                    int index9, String parameterType9);

    /**
     * 获取构造方法，匹配指定的参数
     *
     * @param index1          参数下标1
     * @param parameterType1  参数类型匹配规则1
     * @param index2          参数下标2
     * @param parameterType2  参数类型匹配规则2
     * @param index3          参数下标3
     * @param parameterType3  参数类型匹配规则3
     * @param index4          参数下标4
     * @param parameterType4  参数类型匹配规则4
     * @param index5          参数下标5
     * @param parameterType5  参数类型匹配规则5
     * @param index6          参数下标6
     * @param parameterType6  参数类型匹配规则6
     * @param index7          参数下标7
     * @param parameterType7  参数类型匹配规则7
     * @param index8          参数下标8
     * @param parameterType7  参数类型匹配规则8
     * @param index9          参数下标9
     * @param parameterType9  参数类型匹配规则9
     * @param index10         参数下标10
     * @param parameterType10 参数类型匹配规则10
     * @return
     */
    InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2,
                                    int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5,
                                    int index6, String parameterType6, int index7, String parameterType7, int index8, String parameterType8,
                                    int index9, String parameterType9, int index10, String parameterType10);

    /**
     * 获取静态初始化代码块
     *
     * @return
     */
    InstrumentMethod getStaticInitBlock();

    /**
     * 获取所有的构造方法
     *
     * @return
     */
    InstrumentMethod getConstructors();

    /**
     * 重置
     */
    void reset();
}
