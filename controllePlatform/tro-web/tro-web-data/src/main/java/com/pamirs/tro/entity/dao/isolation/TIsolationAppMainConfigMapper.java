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

package com.pamirs.tro.entity.dao.isolation;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.TIsolationAppMainConfig;
import com.pamirs.tro.entity.domain.vo.TIsolationAppConfigVO;

public interface TIsolationAppMainConfigMapper {

    List<TIsolationAppMainConfig> selectListByVO(TIsolationAppConfigVO tIsolationAppConfigVO);

    int insert(TIsolationAppMainConfig tIsolationAppMainConfig);

    TIsolationAppMainConfig selectByApplicationId(Long applicationId);

    List<TIsolationAppMainConfig> selectByApplicationIds(List<Long> applicationIds);

    int updateByApplicationId(TIsolationAppMainConfig tIsolationAppMainConfig);

    int deleteByApplicationIds(List<Long> applicationIds);

}
