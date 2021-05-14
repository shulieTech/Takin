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

package com.pamirs.tro.entity.dao.settle;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.settle.AccountBalance;
import com.pamirs.tro.entity.domain.vo.settle.AccountBalanceQueryVO;
import org.apache.ibatis.annotations.Param;

public interface TAccountBalanceMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(AccountBalance record);

    AccountBalance selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountBalance record);

    List<AccountBalance> getPageList(AccountBalanceQueryVO queryVO);

    AccountBalance getAccountBalance(@Param("outerId") String outerId, @Param("sceneCode") String sceneCode);
}
