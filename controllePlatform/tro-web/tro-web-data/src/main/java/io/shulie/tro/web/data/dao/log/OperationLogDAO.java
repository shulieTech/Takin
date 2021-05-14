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

package io.shulie.tro.web.data.dao.log;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.data.param.log.OperationLogCreateParam;
import io.shulie.tro.web.data.param.log.OperationLogQueryParam;
import io.shulie.tro.web.data.result.log.OperationLogResult;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: fanxx
 * @Date: 2020/9/24 3:54 下午
 * @Description:
 */
public interface OperationLogDAO {

    /**
     * 分页查询操作日志列表
     *
     * @param queryParam
     * @return
     */
    PagingList<OperationLogResult> selectPage(@Param("queryParam") OperationLogQueryParam queryParam);

    int insert(OperationLogCreateParam createParam);
}
