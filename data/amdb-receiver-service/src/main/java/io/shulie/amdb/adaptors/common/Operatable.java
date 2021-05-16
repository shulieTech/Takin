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

package io.shulie.amdb.adaptors.common;


/**
 * 可操作
 *
 * @author vincent
 */
public interface Operatable {

    /**
     * 模型时间戳
     *
     * @return
     */
    long timestamp();

    /**
     * 编号
     *
     * @return
     */
    String id();

    /**
     * 模型类型
     * c 创建
     * u 更新
     * d 删除
     * s 查询
     *
     * @return
     */
    char opType();

    public enum OpType {
        C, D, U, R;

        public char getValue() {
            return this.toString().charAt(0);
        }

        public static OpType valueOf(char value) {
            switch (value) {
                case 'C':
                    return OpType.C;
                case 'D':
                    return OpType.D;
                case 'R':
                    return OpType.R;
                case 'U':
                    return OpType.U;
            }
            return null;
        }
    }
}
