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
package com.shulie.instrument.simulator.message;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/4/24 12:29 下午
 */
/**
 * 返回结果
 */
public class Result {

    public static final int RESULT_STATE_NONE = 0;
    public static final int RESULT_STATE_RETURN = 1;
    public static final int RESULT_STATE_THROWS = 2;
    public static final Result RESULT_NONE = new Result(RESULT_STATE_NONE, null);
    /**
     * 返回状态(0:NONE;1:RETURN;2:THROWS)
     */
    public final int state;
    /**
     * 应答对象
     */
    public final Object result;

    /**
     * 构造返回结果
     *
     * @param state   返回状态
     * @param result 应答对象
     */
    private Result(int state, Object result) {
        this.state = state;
        this.result = result;
    }

    public static Result newNone() {
        return RESULT_NONE;
    }

    public static Result newReturn(Object object) {
        return new Result(RESULT_STATE_RETURN, object);
    }

    public static Result newThrows(Throwable throwable) {
        return new Result(RESULT_STATE_THROWS, throwable);
    }

}
