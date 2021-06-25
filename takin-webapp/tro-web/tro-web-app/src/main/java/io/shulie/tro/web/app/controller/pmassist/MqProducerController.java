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
import com.pamirs.tro.common.constant.MQConstant;
import com.pamirs.tro.common.constant.MQEnum;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.entity.domain.entity.TEbmProducer;
import com.pamirs.tro.entity.domain.entity.TRocketmqProducer;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.MqProducerService;
import io.shulie.tro.web.app.service.MqProducerWithScriptService;
import io.swagger.annotations.Api;
import org.apache.commons.collections4.MapUtils;
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
 * 说明: MQ虚拟生产消息接口管理
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/9/13 12:49
 */
@Api(tags = "MQ虚拟生产消息接口管理")
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
public class MqProducerController {

    private final Logger LOGGER = LoggerFactory.getLogger(MqProducerController.class);

    @Autowired
    private MqProducerService mqProducerService;

    @Autowired
    private MqProducerWithScriptService mqProducerWithScriptService;

    /**
     * 说明: API.05.03.001 ESB/IBM 虚拟发送消息
     *
     * @param paraMap ESB或者IBM的参数集合
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @date 2018/9/13 0:15
     */
    @PostMapping(value = APIUrls.API_TRO_MQPRODUCER_EBM_SENDMSG_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> ebmProduceMsg(@RequestBody Map<String, Object> paraMap) {
        //        消息类型数字转文字
        String mqType = MapUtils.getString(paraMap, MQConstant.MSGTYPE);
        mqType = MQConstant.ESB.equals(mqType) ? MQEnum.ESB.toString() : MQEnum.IBM.toString();
        paraMap.put(MQConstant.MSGTYPE, mqType);
        try {
            mqProducerWithScriptService.executeStartEbmScript(paraMap);
            return ResponseOk.create(mqType + "正在生产消息,请等待......");
        } catch (TROModuleException e) {
            LOGGER.error("MqProducerController.esbOrIBMProduceMsg 启动消费异常{}", e);
            return ResponseError.create(e.getErrorCode(), mqType + e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("MqProducerController.esbOrIBMProduceMsg 启动消费异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQPRODUCER_ESB_OR_IBM_EXCEPTION.getErrorCode(),
                mqType + TROErrorEnum.ASSIST_MQPRODUCER_ESB_OR_IBM_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.05.03.002 ROCKETMQ/DPBOOT_ROCKETMQ 虚拟发送消息
     *
     * @param paraMap ESB或者IBM的参数集合
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @date 2018/9/13 0:15
     */
    @PostMapping(value = APIUrls.API_TRO_MQPRODUCER_ROCKETMQ_SENDMSG_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> rocketMqProduceMsg(@RequestBody Map<String, Object> paraMap) {
        try {
            mqProducerWithScriptService.executeStartRocketmqScript(paraMap);
            return ResponseOk.create(MQEnum.ROCKETMQ.toString() + "正在生产消息,请等待......");
        } catch (Exception e) {
            LOGGER.error("MqProducerController.rocketMqProduceMsg 启动消费异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.05.03.003 停止虚拟发送消息
     *
     * @param mqType 1: ESB
     *               2: IBM
     *               3: ROCKETMQ
     *               4: DPBOOT_ROCKETMQ
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @date 2018/9/13 0:15
     */
    @GetMapping(value = APIUrls.API_TRO_MQPRODUCER_STOP_SENDMSG_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> stopMqProduceMsg(@RequestParam("mqType") String mqType,
        @RequestParam("id") String id) {
        try {
            mqProducerWithScriptService.stopMq(mqType, id);
            return ResponseOk.create("正在停止生产消息,请等待......");
        } catch (Exception e) {
            LOGGER.error("MqProducerController.stopMqProduceMsg 停止生产消息异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQPRODUCER_STOP_PRODUCE_MSQ_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQPRODUCER_STOP_PRODUCE_MSQ_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.05.03.004 新增ESB/IBM虚拟发送消息
     *
     * @param tEbmProducer 消息封装对象
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @date 2018/9/13 0:15
     */
    @PostMapping(value = APIUrls.API_TRO_MQPRODUCER_EBM_ADD_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> addEsb(@RequestBody @Valid TEbmProducer tEbmProducer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseError.create(1050300401, bindingResult.getFieldError().getDefaultMessage());
        }
        String msgType = tEbmProducer.getMsgType();
        msgType = MQConstant.ESB.equals(msgType) ? MQEnum.ESB.toString() : MQEnum.IBM.toString();
        try {
            mqProducerService.saveEbm(tEbmProducer);
            return ResponseOk.create("success");
        } catch (TROModuleException e) {
            LOGGER.error("MqProducerController.addEsb " + msgType + e.getErrorMessage());
            return ResponseError.create(e.getErrorCode(), msgType + e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("MqProducerController.addEsb 保存MQ消息异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQPRODUCER_EBM_SAVE_EXCEPTION.getErrorCode(),
                msgType + TROErrorEnum.ASSIST_MQPRODUCER_EBM_SAVE_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.05.03.006 修改ESB/IBM虚拟发送消息
     *
     * @param tEbmProducer 待修改的消息对象信息
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @date 2018/9/13 15:59
     */
    @PostMapping(value = APIUrls.API_TRO_MQPRODUCER_EBM_UPDATE_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateEsb(@RequestBody @Valid TEbmProducer tEbmProducer,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseError.create(1050300601, bindingResult.getFieldError().getDefaultMessage());
        }
        String msgType = tEbmProducer.getMsgType();
        msgType = MQConstant.ESB.equals(msgType) ? MQEnum.ESB.toString() : MQEnum.IBM.toString();
        try {
            mqProducerService.updateEbm(tEbmProducer);
            return ResponseOk.create("success");
        } catch (TROModuleException e) {
            LOGGER.error("MqProducerController.updateEsb " + msgType + e.getErrorMessage());
            return ResponseError.create(e.getErrorCode(), msgType + e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("MqProducerController.updateEsb 修改" + msgType + "生产消息异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQPRODUCER_EBM_UPDATE_EXCEPTION.getErrorCode(),
                msgType + TROErrorEnum.ASSIST_MQPRODUCER_EBM_UPDATE_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明:  API.05.03.005 删除ESB/IBM虚拟发送消息
     *
     * @param tepIds 要删除的消息id列表，可单个和多个删除
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @create 2018/7/31 9:03
     */
    @GetMapping(value = APIUrls.API_TRO_MQPRODUCER_EBM_DEL_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> deteletEsbOrIbm(@RequestParam("tepIds") String tepIds) {
        if (StringUtils.isEmpty(tepIds)) {
            return ResponseError.create(
                TROErrorEnum.ASSIST_MQPRODUCER_ESB_AND_IBM_DELETE_PARAM_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQPRODUCER_ESB_AND_IBM_DELETE_PARAM_EXCEPTION.getErrorMessage());
        }
        try {
            mqProducerService.deteletEsbOrIbm(tepIds);
            return ResponseOk.create("删除成功");
        } catch (Exception e) {
            LOGGER.error("MqProducerController.deteletEsbOrIbm 删除ESB和IBM生产消息异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQPRODUCER_ESB_AND_IBM_DELETE_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQPRODUCER_ESB_AND_IBM_DELETE_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.05.03.007 查询ESB/IBM虚拟发送消息列表
     *
     * @param paramMap 查询条件和分页参数都存在该map中
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @create 2018/7/31 9:03
     */
    @PostMapping(value = APIUrls.API_TRO_MQPRODUCER_EBM_QUERYLIST_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryEsbAndIbmList(@RequestBody Map<String, Object> paramMap) {
        try {
            return ResponseOk.create(mqProducerService.queryEsbAndIbmList(paramMap));
        } catch (Exception e) {
            LOGGER.error("MqProducerController.queryEsbAndIbmList 查询ESB/IBM生产消息列表异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQPRODUCER_ESB_AND_IBM_QUERY_LIST_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQPRODUCER_ESB_AND_IBM_QUERY_LIST_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.05.03.008 根据id查询ESB/IBM虚拟发送消息详情
     *
     * @param tepId 消息id
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @create 2018/7/31 9:03
     */
    @GetMapping(value = APIUrls.API_TRO_MQPRODUCER_EBM_QUERYBYID_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryEsbOrIbmDetail(@RequestParam("tepId") String tepId) {
        try {
            return ResponseOk.create(mqProducerService.queryEsbOrIbmDetail(tepId));
        } catch (Exception e) {
            LOGGER.error("MqProducerController.queryEsbOrIbmDetail 查询ESB/IBM生产消息详情异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQPRODUCER_ESB_OR_IBM_QUERY_DETAIL_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQPRODUCER_ESB_OR_IBM_QUERY_DETAIL_EXCEPTION.getErrorMessage());
        }
    }

    //===================================ROCKETMQ区域=====================================================

    /**
     * API.05.03.009 新增ROCKETMQ虚拟发送消息
     *
     * @param tRocketmqProducer
     * @param bindingResult
     * @return
     */

    @PostMapping(value = APIUrls.API_TRO_MQPRODUCER_ROCKETMQ_ADD_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> addRocketmq(@RequestBody @Valid TRocketmqProducer tRocketmqProducer,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseError.create(1050300201, bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            mqProducerService.addRocketmq(tRocketmqProducer);
            return ResponseOk.create("保存成功");
        } catch (TROModuleException e) {
            LOGGER.error("MqProducerController.addRocketmq " + e.getErrorMessage());
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("MqProducerController.addRocketmq 保存消息异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * API.05.03.011 修改ROCKETMQ虚拟发送消息
     *
     * @param tRocketmqProducer
     * @param bindingResult
     * @return
     */
    @PostMapping(value = APIUrls.API_TRO_MQPRODUCER_ROCKETMQ_UPDATE_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateRocketmq(@RequestBody @Valid TRocketmqProducer tRocketmqProducer,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseError.create(1050300201, bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            mqProducerService.updateRocketmq(tRocketmqProducer);
            return ResponseOk.create("修改成功");
        } catch (TROModuleException e) {
            LOGGER.error("MqProducerController.updateRocketmq " + e.getErrorMessage());
            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error("MqProducerController.updateRocketmq 修改消息异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_UPDATE_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_UPDATE_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.05.03.010 删除ROCKETMQ虚拟发送消息
     *
     * @param trpIds 要删除的消息id列表，可单个和多个删除
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @create 2018/7/31 9:03
     */
    @GetMapping(value = APIUrls.API_TRO_MQPRODUCER_ROCKETMQ_DEL_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> deteletRocketMq(@RequestParam("trpIds") String trpIds) {
        if (StringUtils.isEmpty(trpIds)) {
            return ResponseError.create(TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_DELETE_PARAM_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_DELETE_PARAM_EXCEPTION.getErrorMessage());
        }
        try {
            mqProducerService.deteletRocketMq(trpIds);
            return ResponseOk.create("删除成功");
        } catch (Exception e) {
            LOGGER.error("MqProducerController.deteletRocketMq 删除ROCKETMQ生产消息异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_DELETE_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_DELETE_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.05.03.012 查询ROCKETMQ虚拟发送消息列表
     *
     * @param paramMap 查询条件和分页参数都存在该map中
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @create 2018/7/31 9:03
     */
    @PostMapping(value = APIUrls.API_TRO_MQPRODUCER_ROCKETMQ_QUERYLIST_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryRocketmqList(@RequestBody Map<String, Object> paramMap) {
        try {
            return ResponseOk.create(mqProducerService.queryRocketmqList(paramMap));
        } catch (Exception e) {
            LOGGER.error("MqProducerController.queryRocketmqList 查询ROCKETMQ生产消息列表异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_QUERY_LIST_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_QUERY_LIST_EXCEPTION.getErrorMessage());
        }
    }

    /**
     * 说明: API.05.03.013  根据id查询ROCKETMQ虚拟发送消息详情
     *
     * @param trpId 消息id
     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
     * @author shulie
     * @create 2018/7/31 9:03
     */
    @GetMapping(value = APIUrls.API_TRO_MQPRODUCER_ROCKETMQ_QUERYBYID_URI,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryRocketeMqDetail(@RequestParam("trpId") String trpId) {
        try {
            return ResponseOk.create(mqProducerService.queryRocketeMqDetail(trpId));
        } catch (Exception e) {
            LOGGER.error("MqProducerController.queryRocketeMqDetail 查询ROCKETMQ生产消息详情异常{}", e);
            return ResponseError.create(TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_QUERY_DETAIL_EXCEPTION.getErrorCode(),
                TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_QUERY_DETAIL_EXCEPTION.getErrorMessage());
        }
    }

}
