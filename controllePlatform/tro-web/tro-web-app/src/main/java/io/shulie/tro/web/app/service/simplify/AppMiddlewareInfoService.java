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

package io.shulie.tro.web.app.service.simplify;

import java.util.List;

import javax.annotation.Resource;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pamirs.tro.entity.dao.confcenter.TApplicationMntDao;
import com.pamirs.tro.entity.dao.simplify.TAppMiddlewareInfoMapper;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.simplify.TAppMiddlewareInfo;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.query.agent.AppMiddlewareQuery;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.common.RestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.tro.web.api.service.simplify
 * @Date 2020-03-25 17:43
 */
@Slf4j
@Service
public class AppMiddlewareInfoService {

    @Resource
    private TAppMiddlewareInfoMapper tAppMiddlewareInfoMapper;

    @Resource
    private TApplicationMntDao tApplicationMntDao;

    public Response queryPage(AppMiddlewareQuery query) {
        User user = RestContext.getUser();
        if (1 == user.getRole()) {
            query.setUserId(user.getId());
        }
        TApplicationMnt tApplicationMnt = tApplicationMntDao.queryApplicationinfoById(query.getApplicationId());
        if (null == tApplicationMnt) {
            return Response.fail("未查询到应用相关数据");
        }
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<TAppMiddlewareInfo> list = tAppMiddlewareInfoMapper.selectList(query);
        PageInfo<TAppMiddlewareInfo> pageInfo = new PageInfo<>(list);
        return Response.success(pageInfo.getList(), pageInfo.getTotal());
    }
}
