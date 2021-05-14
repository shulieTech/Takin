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

package io.shulie.tro.cloud.biz.service.record.impl;

import java.util.List;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.pamirs.tro.entity.dao.schedule.TScheduleRecordMapper;
import com.pamirs.tro.entity.domain.dto.schedule.ScheduleRecordDTO;
import com.pamirs.tro.entity.domain.entity.schedule.ScheduleRecord;
import com.pamirs.tro.entity.domain.vo.schedule.ScheduleRecordQueryVO;
import io.shulie.tro.cloud.biz.service.record.ScheduleRecordService;
import io.shulie.tro.cloud.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * @ClassName ScheduleRecordServiceImpl
 * @Description
 * @Author qianshui
 * @Date 2020/5/9 下午2:16
 */
@Slf4j
@Service
public class ScheduleRecordServiceImpl implements ScheduleRecordService {

    @Resource
    private TScheduleRecordMapper TScheduleRecordMapper;

    @Override
    public PageInfo<ScheduleRecordDTO> queryPageList(ScheduleRecordQueryVO queryVO) {
        Page page = PageHelper.startPage(queryVO.getCurrentPage() + 1, queryVO.getPageSize());

        List<ScheduleRecord> queryList = TScheduleRecordMapper.getPageList(queryVO);
        if (CollectionUtils.isEmpty(queryList)) {
            return new PageInfo<>(Lists.newArrayList());
        }
        List<ScheduleRecordDTO> resultList = Lists.newArrayList();
        queryList.forEach(data -> {
            ScheduleRecordDTO dto = new ScheduleRecordDTO();
            dto.setId(data.getId());
            dto.setPodNum(data.getPodNum());
            dto.setPodClass(data.getPodClass());
            dto.setStatusInt(data.getStatus());
            dto.setMemorySize(data.getMemorySize());
            dto.setCpuCoreNum(data.getCpuCoreNum());
            dto.setCreateTime(DateUtil.getYYYYMMDDHHMMSS(data.getCreateTime()));
            resultList.add(dto);
        });

        PageInfo pageInfo = new PageInfo<>(resultList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }
}
