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

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:43 下午
 */

import com.shulie.instrument.simulator.api.listener.EventListener;

/**
 * 进度报告
 * <p>
 * 观察类是需要对类进行增强，有时候需要对大量的类进行渲染，耗时比较长。
 * 通过这样的报告方式可以让外部感知到当前渲染的进度。
 * 在渲染完成之前，事件是不会触发给到{@link EventListener}的
 * </p>
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:47 下午
 */
public interface Progress {

    /**
     * 进度开始
     *
     * @param total 总共需要渲染类的总数
     */
    void begin(int total);

    /**
     * 进度报告(成功)
     *
     * @param clazz 当前进行行变的类
     * @param index 当前形变类的序号,从0开始
     */
    void progressOnSuccess(Class<?> clazz, int index);

    /**
     * 进度报告(失败)
     *
     * @param clazz 当前进行行变的类
     * @param index 当前形变类的序号,从0开始
     * @param cause 失败异常
     */
    void progressOnFailed(Class<?> clazz, int index, Throwable cause);

    /**
     * 进度结束
     * <p>如果是add方法，则影响的数量是递增；</p>
     * <p>如果是remove方法，则影响的数量是递减；</p>
     * <p>当彻remove完成之后，cCnt = mCnt = 0;</p>
     *
     * @param cCnt 影响类总数
     * @param mCnt 影响方法总数
     */
    void finish(int cCnt, int mCnt);

}
