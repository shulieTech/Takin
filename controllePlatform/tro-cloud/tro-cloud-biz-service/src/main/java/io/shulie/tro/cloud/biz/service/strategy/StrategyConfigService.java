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

package io.shulie.tro.cloud.biz.service.strategy;

import com.github.pagehelper.PageInfo;
import com.pamirs.tro.entity.domain.dto.strategy.StrategyConfigDTO;
import com.pamirs.tro.entity.domain.dto.strategy.StrategyConfigDetailDTO;
import com.pamirs.tro.entity.domain.vo.strategy.StrategyConfigAddVO;
import com.pamirs.tro.entity.domain.vo.strategy.StrategyConfigQueryVO;
import com.pamirs.tro.entity.domain.vo.strategy.StrategyConfigUpdateVO;
import io.shulie.tro.cloud.biz.output.strategy.StrategyOutput;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName StrategyConfigService
 * @Description
 * @Author qianshui
 * @Date 2020/5/9 下午3:16
 */
public interface StrategyConfigService {

    Boolean add(StrategyConfigAddVO addVO);

    Boolean update(StrategyConfigUpdateVO updateVO);

    Boolean delete(@Param("id") Long id);

    PageInfo<StrategyConfigDTO> queryPageList(StrategyConfigQueryVO queryVO);

    StrategyConfigDetailDTO getDetail(Long id);

    /**
     * 根据最大并发获得策略结果
     *
     * @param expectThroughput
     * @return
     */
    StrategyOutput getStrategy(Integer expectThroughput,Integer tpsNum);

    /**
     * 获取默认策略
     *
     * @return
     */
    StrategyConfigDTO getDefaultStrategyConfig();
}
