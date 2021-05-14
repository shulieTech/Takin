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

package io.shulie.tro.web.app.controller.openapi.converter;

import java.util.List;

import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessActiveViewListDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.BusinessFlowDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.SceneDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.SystemProcessViewListDto;
import com.pamirs.tro.entity.domain.dto.linkmanage.TechLinkDto;
import io.shulie.tro.web.app.controller.openapi.response.linkmanage.BusinessActiveViewListOpenApiResp;
import io.shulie.tro.web.app.controller.openapi.response.linkmanage.BusinessFlowOpenApiResp;
import io.shulie.tro.web.app.controller.openapi.response.linkmanage.BusinessLinkOpenApiResp;
import io.shulie.tro.web.app.controller.openapi.response.linkmanage.SceneOpenApiResp;
import io.shulie.tro.web.app.controller.openapi.response.linkmanage.SystemProcessViewListOpenApiResp;
import io.shulie.tro.web.app.controller.openapi.response.linkmanage.TechLinkOpenApiResp;
import io.shulie.tro.web.app.response.linkmanage.BusinessLinkResponse;
import org.apache.commons.lang.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author zhaoyong
 */
@Mapper(imports = {StringUtils.class})
public interface LinkManageOpenApiConverter {
    LinkManageOpenApiConverter INSTANCE = Mappers.getMapper(LinkManageOpenApiConverter.class);

    List<SystemProcessViewListOpenApiResp> ofListSystemProcessViewListDto(List<SystemProcessViewListDto> data);

    List<BusinessActiveViewListOpenApiResp> ofListBusinessActiveViewListOpenApiResp(List<BusinessActiveViewListDto> bussisnessLinks);

    BusinessLinkOpenApiResp ofBusinessLinkOpenApiResp(BusinessLinkResponse businessLinkResponse);

    List<SceneOpenApiResp> ofListSceneOpenApiResp(List<SceneDto> data);

    BusinessFlowOpenApiResp ofBusinessFlowOpenApiResp(BusinessFlowDto dto);

    TechLinkOpenApiResp ofTechLinkOpenApiResp(TechLinkDto dto);
}
