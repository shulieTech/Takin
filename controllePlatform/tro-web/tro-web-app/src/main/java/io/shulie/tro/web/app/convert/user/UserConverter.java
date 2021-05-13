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

package io.shulie.tro.web.app.convert.user;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.input.user.UserQueryInput;
import io.shulie.tro.web.app.output.user.UserQueryOutput;
import io.shulie.tro.web.app.request.user.UserQueryRequest;
import io.shulie.tro.web.app.response.user.UserQueryResponse;
import io.shulie.tro.web.data.result.user.UserCommonResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @Author ZhangXT
 * @Description UserConverter
 * @Date 2020/11/4 17:50
 */
@Mapper(imports = {StringUtils.class})
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    /**
     * UserCommonResult TO UserQueryOutput
     */
    UserQueryOutput result2Output(UserCommonResult user);

    default PagingList<UserQueryOutput> toOutputPagingList(PagingList<UserCommonResult> pageInfo) {
        List<UserQueryOutput> result = Lists.newArrayList();
        if (null == pageInfo || CollectionUtils.isEmpty(pageInfo.getList())) {
            return PagingList.of(result, 0);
        }
        List<UserQueryOutput> collect = pageInfo.getList().stream().map(this::result2Output).collect(
            Collectors.toList());

        return PagingList.of(collect, pageInfo.getTotal());
    }

    /**
     * UserQueryOutput TO UserQueryResponse
     */
    @Mappings({
        @Mapping(target = "id", expression = "java(user.getUserId())"),
        @Mapping(target = "accountName", expression = "java(user.getUserName())"),
        @Mapping(target = "department", expression = "java(StringUtils.join(user.getDeptNameList(),\"/\"))")
    })
    UserQueryResponse output2Response(UserQueryOutput user);

    default PagingList<UserQueryResponse> toResponsePagingList(PagingList<UserQueryOutput> pageInfo) {
        List<UserQueryResponse> result = Lists.newArrayList();
        if (null == pageInfo || CollectionUtils.isEmpty(pageInfo.getList())) {
            return PagingList.of(result, 0);
        }
        List<UserQueryResponse> collect = pageInfo.getList().stream().map(this::output2Response).collect(
            Collectors.toList());

        return PagingList.of(collect, pageInfo.getTotal());
    }

    /**
     * UserQueryRequest TO UserQueryInput
     */
    @Mappings({
        @Mapping(target = "name", expression = "java(request.getAccountName())"),
        @Mapping(target = "roleIds",
            expression = "java(request.getRoleId()==null?null:java.util.Arrays.asList(request.getRoleId().toString())"
                + ")"),
        @Mapping(target = "deptIds",
            expression = "java(request.getDepartmentId()==null?null:java.util.Arrays.asList(request.getDepartmentId()"
                + ".toString()))")
    })
    UserQueryInput request2Input(UserQueryRequest request);

}
