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

package io.shulie.tro.web.app.conf;

import java.util.Objects;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.web.app.common.RestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/11/4 11:02 上午
 * @Description:
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        User user = RestContext.getUser();
        if (!Objects.isNull(user)) {
            this.strictInsertFill(metaObject, "customerId", Long.class, user.getCustomerId());
            this.strictInsertFill(metaObject, "userId", Long.class, user.getId());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
