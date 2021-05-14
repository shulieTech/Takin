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

package com.pamirs.tro.entity.domain.vo;

import com.pamirs.tro.entity.domain.entity.TLinkMnt;

/**
 * 说明： 增加数据字典的字段
 *
 * @author shulie
 * @description
 * @create 2019-04-10 09:26:28
 */
public class TLinkMntDictoryVo extends TLinkMnt {
    private String name;
    private String order;

    /**
     * Gets the value of name.
     *
     * @return the value of name
     * @author shulie
     * @version 1.0
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * <p>You can use getName() to get the value of name</p>
     *
     * @param name name
     * @author shulie
     * @version 1.0
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of order.
     *
     * @return the value of order
     * @author shulie
     * @version 1.0
     */
    public String getOrder() {
        return order;
    }

    /**
     * Sets the order.
     *
     * <p>You can use getOrder() to get the value of order</p>
     *
     * @param order order
     * @author shulie
     * @version 1.0
     */
    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "TLinkMntDictoryVo{" +
            "name='" + name + '\'' +
            ", order='" + order + '\'' +
            '}';
    }

}
