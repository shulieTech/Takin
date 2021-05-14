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

package io.shulie.tro.web.app.controller.pmassist;

import java.util.Map;

import javax.validation.Valid;

import com.pamirs.tro.common.ResponseError;
import com.pamirs.tro.common.ResponseOk;
import com.pamirs.tro.common.constant.TRODictTypeEnum;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.entity.domain.entity.TMqMsg;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.ConfCenterService;
import io.shulie.tro.web.app.service.MqConsumerService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 说明: MQ虚拟消费接口
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/7/26 17:05
 */
@Api(tags = "MQ虚拟消费接口")
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
public class MqConsumerController {

    private final Logger LOGGER = LoggerFactory.getLogger(MqConsumerController.class);

    @Autowired
    private MqConsumerService mqConsumerService;

    @Autowired
    private ConfCenterService confCenterService;

    /**
     * 说明: API.05.01.001 启动消费脚本接口
     *
     * @param paraMap ESB或者IBM的参数集合
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_ASSIST_START_CONSUMER_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> executeStartConsumerScript(@RequestBody Map<String, Object> paraMap) {
        try {
            mqConsumerService.executeStartConsumerScript(paraMap);
            return ResponseOk.create("启动消费,请等待......");
        } catch (TROModuleException e) {
            LOGGER.error("PressureMeasurementAssistController.executeStartConsumerScript esbcode不符合要求{}", e);
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("PressureMeasurementAssistController.executeStartConsumerScript 启动消费异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_START_CONSUMER_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_START_CONSUMER_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.05.01.002 停止消费脚本接口
     *
     * @param paraMap 脚本名称和消息id
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     */
    @PostMapping(value = APIUrls.API_TRO_ASSIST_STOP_CONSUMER_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> executeStopConsumerScript(@RequestBody Map<String, Object> paraMap) {
        try {
            mqConsumerService.asyncStopConsumerScript(paraMap);
            return ResponseOk.create("停止消费,请等待......");
        } catch (Exception e) {
            LOGGER.error("PressureMeasurementAssistController.executeStopConsumerScript 停止消费异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_STOP_CONSUMER_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_STOP_CONSUMER_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.05.01.003 保存mq消息接口
     *
     * @param tMqMsg 新增的MQ消息存放在该对象中
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @create 2018/7/31 9:07
     */
    @PostMapping(value = APIUrls.API_TRO_MQMSG_ADD_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> addMqMsg(@RequestBody @Valid TMqMsg tMqMsg, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseError.create(1050100301, bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            mqConsumerService.addMqMsg(tMqMsg);
            return ResponseOk.create("保存成功");
        } catch (TROModuleException e) {
            LOGGER.error("PressureMeasurementAssistController.addMqMsg " + e.getErrorMessage());
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("PressureMeasurementAssistController.addMqMsg 保存MQ消息异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQMSG_SAVE_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQMSG_SAVE_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.05.01.004 修改MQ消息接口
     *
     * @param tMqMsg 要修改的内容都存在该对象中
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @create 2018/7/31 9:03
     */
    @PostMapping(value = APIUrls.API_TRO_MQMSG_UPDATE_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateMqMsg(@RequestBody @Valid TMqMsg tMqMsg, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseError.create(1050100401, bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            mqConsumerService.updateMqMsg(tMqMsg);
            return ResponseOk.create("修改成功");
        } catch (TROModuleException e) {
            LOGGER.error("PressureMeasurementAssistController.updateMqMsg " + e.getErrorMessage());
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("PressureMeasurementAssistController.updateMqMsg 修改MQ消息异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQMSG_UPDATE_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQMSG_UPDATE_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.05.01.005 删除MQ消息接口
     *
     * @param msgIds 要删除的消息id列表，可单个和多个删除
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @create 2018/7/31 9:03
     */
    @GetMapping(value = APIUrls.API_TRO_MQMSG_DELETE_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> deteletMqMsg(@RequestParam("msgIds") String msgIds) {
        if (StringUtils.isEmpty(msgIds)) {
            return ResponseError.create(TROErrorEnum.ASSIST_MQMSG_MQMSGID_IS_EMPTY_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQMSG_MQMSGID_IS_EMPTY_EXCEPTION.getErrorMessage());
        }
        try {
            mqConsumerService.batchDeleteMqMsg(msgIds);
            return ResponseOk.create("删除成功");
        } catch (Exception e) {
            LOGGER.error("PressureMeasurementAssistController.deteletMqMsg 删除MQ消息异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQMSG_DELETE_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQMSG_DELETE_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.05.01.006 查询MQ消息列表接口
     *
     * @param paramMap 查询条件和分页参数都存在该map中
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @create 2018/7/31 9:03
     */
    @PostMapping(value = APIUrls.API_TRO_MQMSG_QUERY_LIST_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryMqMsgList(@RequestBody Map<String, Object> paramMap) {
        try {
            return ResponseOk.create(mqConsumerService.queryMqMsgList(paramMap));
        } catch (Exception e) {
            LOGGER.error("PressureMeasurementAssistController.queryMqMsgList 查询MQ消息列表异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQMSG_QUERY_MQMSG_LIST_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQMSG_QUERY_MQMSG_LIST_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.05.01.007 查询MQ消息详情接口
     *
     * @param msgId 消息id
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @create 2018/7/31 9:03
     */
    @GetMapping(value = APIUrls.API_TRO_MQMSG_QUERY_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryMqMsgDetail(@RequestParam("msgId") String msgId) {
        try {
            return ResponseOk.create(mqConsumerService.queryMqMsgDetail(msgId));
        } catch (Exception e) {
            LOGGER.error("PressureMeasurementAssistController.queryMqMsgDetail 查询MQ消息列表异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQMSG_QUERY_MQMSG_DETAIL_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQMSG_QUERY_MQMSG_DETAIL_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.05.01.008 查询消息类型字典列表接口
     *
     * @return 成功, 则返回白名单字典列表;失败则返回错误编码和错误信息
     * @author shulie
     * @create 2018/7/31 13:09
     */
    @GetMapping(value = APIUrls.API_TRO_MQMSG_QUERY_TYPE_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryMqMsgTypeDict() {
        try {
            return ResponseOk.create(confCenterService.queryDicList(TRODictTypeEnum.MQMSG));
        } catch (Exception e) {
            LOGGER.error("PressureMeasurementAssistController.queryMqMsgTypeDict  查询消息类型字典列表异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQMSG_QUERY_MQMSG_TYPE_DICT_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQMSG_QUERY_MQMSG_TYPE_DICT_EXCEPTION.getErrorMessage());
        }
    }
}
