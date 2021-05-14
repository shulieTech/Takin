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

import com.pamirs.tro.entity.dao.user.TQuickAccessMapper;
import com.pamirs.tro.entity.domain.entity.user.QuickAccess;
import com.pamirs.tro.entity.domain.entity.user.QuickAccessExample;
import com.pamirs.tro.entity.domain.vo.user.QuickAccessVo;
import io.shulie.tro.web.app.convert.user.QuickAccessEntityConvert;
import io.shulie.tro.web.app.service.user.QuickAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: mubai
 * @Date: 2020-06-29 10:57
 * @Description:
 */

@Service
public class QuickAccessServiceImpl implements QuickAccessService {
    @Autowired
    private TQuickAccessMapper TQuickAccessMapper;

    @Override
    public List<QuickAccessVo> getQuickAccess(Long uid) {
        QuickAccessExample example = new QuickAccessExample();
        example.setOrderByClause("`order`");
        List<QuickAccess> quickAccesses = TQuickAccessMapper.selectByExample(example);
        return QuickAccessEntityConvert.INSTANCE.ofs(quickAccesses);
    }
}
