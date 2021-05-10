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
package com.pamirs.pradar.exception;

/**
 * Created by xiaobin on 2017/1/19.
 */
@SuppressWarnings("serial")
public class PradarException extends RuntimeException {
    public PradarException() {
    }

    public PradarException(String message) {
        super(message);
    }

    public PradarException(String message, Throwable cause) {
        super(message, cause);
    }

    public PradarException(Throwable cause) {
        super(cause);
    }
}
