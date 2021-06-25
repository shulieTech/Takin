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

package com.pamirs.tro.entity.dao.linkmanage;

import java.util.List;

import com.pamirs.tro.entity.domain.dto.linkmanage.linkstatistics.LinkRemarkmiddleWareDto;
import com.pamirs.tro.entity.domain.entity.linkmanage.TMiddlewareInfo;
import com.pamirs.tro.entity.domain.entity.linkmanage.statistics.StatisticsQueryVo;
import com.pamirs.tro.entity.domain.vo.linkmanage.MiddleWareEntity;
import org.apache.ibatis.annotations.Param;

public interface TMiddlewareInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TMiddlewareInfo record);

    int insertSelective(TMiddlewareInfo record);

    TMiddlewareInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TMiddlewareInfo record);

    int updateByPrimaryKey(TMiddlewareInfo record);

    List<TMiddlewareInfo> selectBySelective(TMiddlewareInfo info);

    List<TMiddlewareInfo> selectBySystemProcessId(@Param("systemProcessId") Long systemProcessId);

    /**
     * 统计页面中间件信息连表查询
     *
     * @param vo
     * @return
     */
    List<LinkRemarkmiddleWareDto> selectforstatistics(StatisticsQueryVo vo);

    List<MiddleWareEntity> selectByIds(@Param("list") List<Long> midllewareIdslong);
}
