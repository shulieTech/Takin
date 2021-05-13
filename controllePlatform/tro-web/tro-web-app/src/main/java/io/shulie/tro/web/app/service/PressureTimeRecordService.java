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

package io.shulie.tro.web.app.service;

import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.entity.dao.transparentflow.TPressureTimeRecordDao;
import com.pamirs.tro.entity.domain.entity.TPressureTimeRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shulie
 * @description
 * @create 2019-04-12 09:16:23
 */
@Service
public class PressureTimeRecordService {
    @Autowired
    private TPressureTimeRecordDao tPressureTimeRecordDao;

    public void savePressureTimeRecord(TPressureTimeRecord pressureTimeRecord) throws TROModuleException {
        String startTime = pressureTimeRecord.getStartTime();
        if (startTime == null) {
            throw new TROModuleException(TROErrorEnum.PRESSURE_TIME_RECORD_SAVE_PARAM_EXCEPTION);
        }
        TPressureTimeRecord record = tPressureTimeRecordDao.queryLatestPressureTime();
        //如果此时数据库中存在在压测的时间记录，则不能添加
        if (record != null && StringUtils.isNotEmpty(record.getStartTime())) {
            throw new TROModuleException(TROErrorEnum.PRESSURE_TIME_RECORD_EXIST_EXCEPTION);
        }
        tPressureTimeRecordDao.insert(startTime);
    }

    public void updatePressureTimeRecord(TPressureTimeRecord pressureTimeRecord) throws TROModuleException {
        String recordId = pressureTimeRecord.getRecordId();
        String endTime = pressureTimeRecord.getEndTime();
        if (StringUtils.isEmpty(recordId) || StringUtils.isEmpty(endTime)) {
            throw new TROModuleException(TROErrorEnum.PRESSURE_TIME_RECORD_UPDATE_PARAM_EXCEPTION);
        }
        tPressureTimeRecordDao.updateByPrimaryKey(recordId, endTime);
    }

    /**
     * 说明: 查询最新的链路压测时间
     *
     * @return TPressureTimeRecord
     * @author shulie
     * @create 2019/4/12 10:17
     */
    public TPressureTimeRecord queryLatestPressureTime() throws TROModuleException {
        /**
         * 1,开始时间不为空，结束时间为空
         * 2,最大的开始时间
         */
        return tPressureTimeRecordDao.queryLatestPressureTime();
    }
}
