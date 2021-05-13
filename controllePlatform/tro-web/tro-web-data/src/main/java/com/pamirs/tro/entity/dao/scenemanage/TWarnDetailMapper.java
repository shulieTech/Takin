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

package com.pamirs.tro.entity.dao.scenemanage;

import java.util.List;

import com.pamirs.tro.entity.domain.bo.scenemanage.WarnBO;
import com.pamirs.tro.entity.domain.entity.scenemanage.WarnDetail;
import com.pamirs.tro.entity.domain.vo.sla.WarnQueryParam;

public interface TWarnDetailMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(WarnDetail record);

    WarnDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WarnDetail record);

    /**
     * 警告汇总
     *
     * @param reportId
     * @return
     */
    List<WarnBO> summaryWarnByReportId(Long reportId);

    /**
     * 警告列表
     *
     * @param param
     * @return
     */
    List<WarnDetail> listWarn(WarnQueryParam param);

    /**
     * 统计报告总警告次数
     *
     * @param reportId
     * @return
     */
    Long countReportTotalWarn(Long reportId);

}
