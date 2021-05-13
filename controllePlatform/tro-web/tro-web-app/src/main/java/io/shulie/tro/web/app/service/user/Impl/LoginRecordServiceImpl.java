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

package io.shulie.tro.web.app.service.user.Impl;

import java.util.List;
import java.util.stream.Collectors;

import com.pamirs.tro.common.util.DateUtils;
import io.shulie.tro.web.app.input.user.LoginRecordSearchInput;
import io.shulie.tro.web.app.output.user.LoginRecordTotalOutput;
import io.shulie.tro.web.app.service.user.LoginRecordService;
import io.shulie.tro.web.common.vo.user.LoginRecordVO;
import io.shulie.tro.web.data.dao.user.LoginRecordDao;
import io.shulie.tro.web.data.param.user.LoginRecordSearchParam;
import io.shulie.tro.web.data.result.user.LoginRecordResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.service.user
 * @date 2021/4/8 11:09 上午
 */
@Service
public class LoginRecordServiceImpl implements LoginRecordService {
    @Autowired
    private LoginRecordDao loginRecordDao;

    @Override
    public LoginRecordTotalOutput getTotal(LoginRecordSearchInput input) {
        LoginRecordSearchParam param = new LoginRecordSearchParam();
        BeanUtils.copyProperties(input,param);
        if(StringUtils.isNotBlank(input.getStartTime())) {
            param.setStartTime(DateUtils.transferTime(input.getStartTime()));
        }
        if(StringUtils.isNotBlank(input.getEndTime())) {
            param.setEndTime(DateUtils.transferTime(input.getEndTime()));
        }
        List<LoginRecordResult> results = loginRecordDao.getList(param);
        List<LoginRecordVO> vos = results.stream().map(result -> {
            LoginRecordVO vo = new LoginRecordVO();
            BeanUtils.copyProperties(result,vo);
            vo.setLoginDate(DateUtils.dateToString(result.getGmtCreate(),DateUtils.FORMATE_YMDHMS));
            return vo;
        }).collect(Collectors.toList());
        LoginRecordTotalOutput output = new LoginRecordTotalOutput();
        output.setVo(vos);
        output.setTotal(vos.size());
        return output;
    }
}
