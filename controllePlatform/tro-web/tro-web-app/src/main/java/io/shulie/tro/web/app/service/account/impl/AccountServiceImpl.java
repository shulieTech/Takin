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

package io.shulie.tro.web.app.service.account.impl;

import com.pamirs.tro.entity.domain.vo.account.AccountQueryVO;
import com.pamirs.tro.entity.domain.vo.account.AccoutVO;
import io.shulie.tro.web.app.service.account.AccountService;
import io.shulie.tro.web.common.constant.RemoteConstant;
import io.shulie.tro.web.common.domain.WebResponse;
import io.shulie.tro.web.common.http.HttpWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * @ClassName AccountServiceImpl
 * @Description
 * @Author qianshui
 * @Date 2020/5/12 下午8:15
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private HttpWebClient httpWebClient;

    @Override
    public WebResponse getAccountBook() {
        AccoutVO vo = new AccoutVO();
        vo.setRequestUrl(RemoteConstant.ACCOUNT_BOOK);
        vo.setHttpMethod(HttpMethod.GET);
        return httpWebClient.request(vo);
    }

    @Override
    public WebResponse getAccountBalance(AccountQueryVO vo) {
        vo.setRequestUrl(RemoteConstant.ACCOUNT_BALANCE_LIST);
        vo.setHttpMethod(HttpMethod.GET);
        return httpWebClient.request(vo);
    }
}
