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

package com.pamirs.tro.entity.dao.common;

import java.util.List;
import java.util.Map;

import com.pamirs.tro.entity.domain.entity.RelationLinkModel;
import com.pamirs.tro.entity.domain.vo.TLinkApplicationInterfaceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 说明: 公共dao层
 *
 * @author shulie
 * @version v1.0
 * @2018年4月26日
 */
@Mapper
public interface CommonDao {

    /**
     * 说明: 根据链路id查询链路应用服务信息插入到链路检测表中
     *
     * @param linkId 链路id
     * @return 链路应用服务信息
     * @author shulie
     */
    List<TLinkApplicationInterfaceVo> selectLinkWholeInfo(@Param("linkId") String linkId);

    /**
     * 说明: 新增链路时查询数据更新到数据构建表中
     *
     * @param linkId 链路id
     * @return 链路应用列表
     * @author shulie
     */
    List<TLinkApplicationInterfaceVo> selectLinkApplicationInfo(@Param("linkId") String linkId);

    /**
     * 说明: 插入关联关系链路表
     *
     * @param relationLinkModel 关联关系链路模型
     * @author shulie
     * @date 2018/12/26 17:57
     */
    void saveRelationLink(RelationLinkModel relationLinkModel);

    List<Map<String, Object>> queryRelationLinkRelationShip(@Param("objTable") String objTable,
        @Param("parentLinkId") String parentLinkId);
}
