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

package io.shulie.tro.cloud.biz.service.settle;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.pamirs.tro.entity.domain.dto.settle.AccountBalanceDTO;
import com.pamirs.tro.entity.domain.entity.settle.AccountBook;
import com.pamirs.tro.entity.domain.vo.settle.AccountBalanceQueryVO;
import com.pamirs.tro.entity.domain.vo.settle.RechargeVO;

/**
 * @ClassName AccountService
 * @Description
 * @Author qianshui
 * @Date 2020/5/9 下午4:21
 */
public interface AccountService {

    void initAccount(Long userId);

    AccountBook getAccountByUserId(Long userId);

    List<AccountBook> getAccountBookByUserIds(List<Long> userIds);

    void rechargeAccount(RechargeVO vo);

    PageInfo<AccountBalanceDTO> getPageList(AccountBalanceQueryVO queryVO);

}
