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

package io.shulie.tro.web.app.controller.confcenter;

import com.pamirs.tro.common.ResponseError;
import com.pamirs.tro.common.ResponseOk;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.entity.domain.entity.TPressureTimeRecord;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.PressureTimeRecordService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shulie
 * @description
 * @create 2019-04-12 09:10:28
 */
@Api(tags = "PressureTimeRecord")
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
public class PressureTimeRecordController {

    Logger logger = LoggerFactory.getLogger(PressureTimeRecordController.class);

    @Autowired
    private PressureTimeRecordService pressureTimeRecordService;

    /**
     * API.01.11.001
     * 说明: 保存开始压测时间
     *
     * @param pressureTimeRecord
     * @return org.springframework.http.ResponseEntity<java.lang.Object>
     * @author shulie
     * @create 2019/4/12 9:22
     */
    @RequestMapping(value = APIUrls.TRO_CONFCENTER_ADD_PRESSURETIME, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> savePressureTimeRecord(@RequestBody TPressureTimeRecord pressureTimeRecord) {
        try {
            pressureTimeRecordService.savePressureTimeRecord(pressureTimeRecord);
            return ResponseOk.create("保存成功");
        } catch (TROModuleException e) {
            logger.error("PressureTimeRecordController.savePressureTimeRecord 保存压测开始时间异常{}", e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            logger.error("PressureTimeRecordController.savePressureTimeRecord 保存压测开始时间异常{}", e);
            return ResponseError.create(TROErrorEnum.PRESSURE_TIME_RECORD_SAVE_EXCEPTION.getErrorCode(),
                TROErrorEnum.PRESSURE_TIME_RECORD_SAVE_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * API.01.11.002
     * 说明: 更新结束压测时间
     *
     * @param pressureTimeRecord
     * @return org.springframework.http.ResponseEntity<java.lang.Object>
     * @author shulie
     * @create 2019/4/12 9:22
     */
    @RequestMapping(value = APIUrls.TRO_CONFCENTER_UPDATE_PRESSURETIME,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updatePressureTimeRecord(@RequestBody TPressureTimeRecord pressureTimeRecord) {
        try {
            pressureTimeRecordService.updatePressureTimeRecord(pressureTimeRecord);
            return ResponseOk.create("更新成功");
        } catch (TROModuleException e) {
            logger.error("PressureTimeRecordController.updatePressureTimeRecord 更新压测结束时间异常{}", e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            logger.error("PressureTimeRecordController.updatePressureTimeRecord 更新压测结束时间异常{}", e);
            return ResponseError.create(TROErrorEnum.PRESSURE_TIME_RECORD_UPDATE_EXCEPTION.getErrorCode(),
                TROErrorEnum.PRESSURE_TIME_RECORD_UPDATE_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * API.01.11.003
     * 说明: 查询最新的压测时间记录
     *
     * @return org.springframework.http.ResponseEntity<java.lang.Object>
     * @author shulie
     * @create 2019/4/12 9:22
     */
    @RequestMapping(value = APIUrls.TRO_CONFCENTER_QUERY_LATEST_PRESSURETIME,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryLatestPressureTime() {
        try {
            TPressureTimeRecord pressureTimeRecord = pressureTimeRecordService.queryLatestPressureTime();
            return ResponseOk.create(pressureTimeRecord);
        } catch (TROModuleException e) {
            logger.error("PressureTimeRecordController.queryLatestPressureTime 查询最新的压测时间记录异常{}", e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            logger.error("PressureTimeRecordController.queryLatestPressureTime 查询最新的压测时间记录异常{}", e);
            return ResponseError.create(TROErrorEnum.PRESSURE_TIME_RECORD_QUERY_LATEST_EXCEPTION.getErrorCode(),
                TROErrorEnum.PRESSURE_TIME_RECORD_QUERY_LATEST_EXCEPTION.getErrorMessage());
        }
    }

}
