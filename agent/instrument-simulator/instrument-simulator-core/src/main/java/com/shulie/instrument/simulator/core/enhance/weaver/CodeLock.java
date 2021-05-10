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
package com.shulie.instrument.simulator.core.enhance.weaver;

/**
 * 代码锁
 * 什么叫代码锁?代码锁的出现是由于在字节码中，我们无法用简单的if语句来判定这段代码是生成的还是原有的。
 * 这会导致一些监控逻辑的混乱，比如trace命令如果不使用代码锁保护，将能看到Greys所植入的代码并进行跟踪
 */
public interface CodeLock {

    /**
     * 根据字节码流锁或解锁代码
     * 通过对字节码流的判断，决定当前代码是锁定和解锁
     *
     * @param opcode 字节码
     */
    void code(int opcode);

    /**
     * 判断当前代码是否还在锁定中
     *
     * @return true/false
     */
    boolean isLock();

    /**
     * 将一个代码块纳入代码锁保护范围
     *
     * @param block 代码块
     */
    void lock(Block block);

    /**
     * 代码块
     */
    interface Block {
        /**
         * 代码
         */
        void code();
    }

}
