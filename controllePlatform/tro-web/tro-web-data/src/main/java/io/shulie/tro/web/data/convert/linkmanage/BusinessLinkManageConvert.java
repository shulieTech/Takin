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

package io.shulie.tro.web.data.convert.linkmanage;

import java.util.List;

import io.shulie.tro.web.data.model.mysql.BusinessLinkManageTableEntity;
import io.shulie.tro.web.data.result.linkmange.BusinessLinkResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.convert.linkmanage
 * @date 2020/12/30 10:52 上午
 */
@Mapper
public interface BusinessLinkManageConvert {
    BusinessLinkManageConvert INSTANCE = Mappers.getMapper(BusinessLinkManageConvert.class);

    /**
     * 转换
     * @param entities
     * @return
     */
    List<BusinessLinkResult> ofList(List<BusinessLinkManageTableEntity> entities);

}
