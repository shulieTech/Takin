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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.pamirs.tro.common.constant.Constants;
import com.pamirs.tro.common.constant.MQConstant;
import com.pamirs.tro.common.constant.MQEnum;
import com.pamirs.tro.common.constant.TRODictTypeEnum;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.common.util.PageInfo;
import com.pamirs.tro.common.util.ProcessUtils;
import com.pamirs.tro.entity.dao.assist.mqconsumer.TMqMsgDao;
import com.pamirs.tro.entity.domain.entity.TMqMsg;
import io.shulie.tro.web.app.common.CommonService;
import io.shulie.tro.web.app.utils.GlobalVariableUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 说明: mq虚拟消费消息service
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/7/26 17:07
 */
@Service
@SuppressWarnings("all")
public class MqConsumerService extends CommonService {

    /**
     * 说明: mq消息类型集合
     *
     * @author shulie
     * @date 2018/8/16 10:19
     */
    private static List<String> mqTypeList = null;

    static {
        mqTypeList = Lists.newArrayList(MQEnum.values()).stream().map(mqEnum -> mqEnum.toString()).collect(
            Collectors.toList());
    }

    @Autowired
    public TMqMsgDao TMqMsgDao;
    @Autowired
    private ConfCenterService confCenterService;

    /**
     * 说明: 启动消费执行脚本
     *
     * @param paraMap ESB或者IBM的参数集合
     * @author shulie
     * @date 2018/7/30 10:02
     */
    public void executeStartConsumerScript(Map<String, Object> paraMap) throws TROModuleException {
        String mqtype = MapUtils.getString(paraMap, MQConstant.MSGTYPE);
        if (!mqTypeList.contains(mqtype)) {
            throw new TROModuleException(TROErrorEnum.ASSIST_START_CONSUMER_PARAMERROR);
        }
        String msgId = MapUtils.getString(paraMap, MQConstant.MSGID);
        TMqMsg tMqMsg = TMqMsgDao.selectMqMsgById(Long.parseLong(msgId));
        TMqMsg originMqMsg = tMqMsg;
        if (tMqMsg == null) {
            throw new TROModuleException(TROErrorEnum.ASSIST_START_CONSUMER_MSG_NOTFOUND);
        }

        tMqMsg.setConsumeStartTime(new Date());
        tMqMsg.setLastConsumeTime(tMqMsg.getConsumeEndTime());
        tMqMsg.setConsumeStatus(Constants.CONSUMING);
        TMqMsgDao.updateByPrimaryKey(tMqMsg);

        MQEnum mqEnum = getMqEnum(mqtype);

        runShellTaskExecutor.execute(asyncStartConsumerScript(paraMap, mqEnum));
    }

    /**
     * 说明: 异步执行启动消费脚本
     *
     * @param scriptPath 脚本路径
     * @throws Exception 异常
     * @author shulie
     */
    public Runnable asyncStartConsumerScript(Map<String, Object> paraMap, MQEnum mqEnum) {
        String queueManager = MapUtils.getString(paraMap, MQConstant.QUEUEMANAGER);
        String hostName = MapUtils.getString(paraMap, MQConstant.MSGHOST);
        String channel = MapUtils.getString(paraMap, MQConstant.QUEUECHANNEL);
        String port = MapUtils.getString(paraMap, MQConstant.MSGPORT);
        String clusterIp = MapUtils.getString(paraMap, MQConstant.MSGIP);
        String transporttype = MapUtils.getString(paraMap, MQConstant.TRANSPORTTYPE);
        String CCSID = MapUtils.getString(paraMap, MQConstant.CCSID);
        String baseQueueName = MapUtils.getString(paraMap, MQConstant.BASEQUEUENAME);
        String esbcode = MapUtils.getString(paraMap, MQConstant.ESBCODE);
        String msgId = MapUtils.getString(paraMap, MQConstant.MSGID);
        String topic = MapUtils.getString(paraMap, MQConstant.TOPIC);
        String groupName = MapUtils.getString(paraMap, MQConstant.GROUPNAME);
        String shStrPath = getBasePath() + getMqScriptName();
        return () -> {
            Process ps = null;
            Process process = null;
            InputStream inputStream = null;
            ByteArrayOutputStream bytes = null;
            try {
                String cmd = "";
                switch (mqEnum) {
                    case ESB:
                        cmd = shStrPath + " " + mqEnum.toString() + " " + queueManager + " " + hostName + " " + channel
                            + " " + port + " " + CCSID + " " + baseQueueName + " " + transporttype + " " + esbcode;
                        break;
                    case IBM:
                        cmd = shStrPath + " " + mqEnum.toString() + " " + queueManager + " " + hostName + " " + channel
                            + " " + port + " " + transporttype + " " + esbcode;
                        break;
                    case ROCKETMQ:
                        cmd = shStrPath + " " + mqEnum.toString() + " " + topic + " " + groupName + " " + clusterIp;
                        break;
                    case DPBOOT_ROCKETMQ:
                        cmd = shStrPath + " " + mqEnum.toString() + " " + topic + " " + groupName + " " + clusterIp;
                        break;
                    default:
                        break;
                }
                //解决脚本没有执行权限
                ProcessBuilder builder = new ProcessBuilder("/bin/chmod", "755", shStrPath);
                ps = builder.start();
                ps.waitFor();

                LOGGER.info("脚本的命令为: " + cmd);
                process = Runtime.getRuntime().exec(cmd);
                process.waitFor();

                //                inputStream = process.getErrorStream();
                //                bytes = new ByteArrayOutputStream();
                //                String errorStr = printOutContent(inputStream, bytes);
                //                if (StringUtils.isNotEmpty(errorStr)) {
                //                    mqMsgDao.updateConsumeStatusById(msgId, Constants.CONSUME_FAILED);
                //                }
            } catch (Exception e) {
                TMqMsgDao.updateConsumeStatusById(msgId, Constants.CONSUME_FAILED);
                LOGGER.error("PressureMeasurementAssistService.asyncStartConsumerScript 启动消息错误{}", e);
            } finally {
                closeAll(null, inputStream, bytes, ps, process);
            }
        };
    }

    /**
     * 说明: 停止消费进程
     *
     * @param msgId 消息id
     * @return
     * @author shulie
     * @date 2018/7/31 16:43
     */
    public void asyncStopConsumerScript(Map<String, Object> paraMap) {
        List<String> piDs = getPIDs(paraMap);
        killLinuxProcess(piDs, MapUtils.getString(paraMap, MQConstant.MSGID));
    }

    /**
     * 说明: 获取进程号集合
     *
     * @param paraMap 参数集合
     * @return 进程号集合
     * @author shulie
     * @date 2018/8/9 14:25
     */
    public List<String> getPIDs(Map<String, Object> paraMap) {
        //      ps -ef | grep "ESB MQM2 10.230.29.24 LMS.CLIENT 1428 1208 QU_LMS_REQUEST_COM_IN 1
        //      PT_LMS_ESB2LMS_PRO_TEST" | grep -v grep
        String mqtype = MapUtils.getString(paraMap, MQConstant.MSGTYPE);
        MQEnum mqEnum = getMqEnum(mqtype);
        String msgId = MapUtils.getString(paraMap, MQConstant.MSGID);
        String queueManager = MapUtils.getString(paraMap, MQConstant.QUEUEMANAGER);
        String hostName = MapUtils.getString(paraMap, MQConstant.MSGHOST);
        String channel = MapUtils.getString(paraMap, MQConstant.QUEUECHANNEL);
        String port = MapUtils.getString(paraMap, MQConstant.MSGPORT);
        String clusterIp = MapUtils.getString(paraMap, MQConstant.MSGIP);
        String transporttype = MapUtils.getString(paraMap, MQConstant.TRANSPORTTYPE);
        String CCSID = MapUtils.getString(paraMap, MQConstant.CCSID);
        String baseQueueName = MapUtils.getString(paraMap, MQConstant.BASEQUEUENAME);
        String esbcode = MapUtils.getString(paraMap, MQConstant.ESBCODE);
        String topic = MapUtils.getString(paraMap, MQConstant.TOPIC);
        String groupName = MapUtils.getString(paraMap, MQConstant.GROUPNAME);

        String cmd = "";
        switch (mqEnum) {
            case ESB:
                cmd = mqEnum.toString() + " " + queueManager + " " + hostName + " " + channel + " " + port + " " + CCSID
                    + " " + baseQueueName + " " + transporttype + " " + esbcode;
                break;
            case IBM:
                cmd = mqEnum.toString() + " " + queueManager + " " + hostName + " " + channel + " " + port + " "
                    + transporttype + " " + esbcode;
                break;
            case ROCKETMQ:
                cmd = mqEnum.toString() + " " + topic + " " + groupName + " " + clusterIp;
                break;
            case DPBOOT_ROCKETMQ:
                cmd = mqEnum.toString() + " " + topic + " " + groupName + " " + clusterIp;
                break;
            default:
                break;
        }

        cmd = " ps -ef | grep MqConsumeMain | grep " + "\"" + cmd + "\"" + " | grep -v grep";

        LOGGER.info("停止脚本的命令为: " + cmd);
        BufferedReader reader = null;
        BufferedReader brReader = null;
        StringBuffer sb = new StringBuffer();
        InputStream inputStream = null;
        ByteArrayOutputStream bytes = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", cmd});
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            inputStream = process.getErrorStream();
            bytes = new ByteArrayOutputStream();
            String errorStr = printOutContent(inputStream, bytes);
            if (StringUtils.isNotEmpty(errorStr)) {
                TMqMsgDao.updateConsumeStatusById(msgId, Constants.CONSUME_FAILED);
            }
        } catch (Exception e) {
            TMqMsgDao.updateConsumeStatusById(msgId, Constants.CONSUME_FAILED);
            LOGGER.error("MqConsumerService.getPIDs 获取进程号执行脚本异常{}", e);
        } finally {
            closeAll(reader, inputStream, bytes, process);
        }
        LOGGER.info("consume进程号集合为{}" + ProcessUtils.getPIdsList(sb.toString()));
        return ProcessUtils.getPIdsList(sb.toString());
    }

    /**
     * 说明: 根据消息类型获取mq枚举类型
     *
     * @param mqtype 消息类型
     * @return 消息枚举类
     * @author shulie
     * @date 2018/10/11 18:38
     */
    private MQEnum getMqEnum(String mqtype) {
        MQEnum mqEnum = null;
        if (MQEnum.ESB.toString().equals(mqtype)) {
            mqEnum = MQEnum.ESB;
        }

        if (MQEnum.IBM.toString().equals(mqtype)) {
            mqEnum = MQEnum.IBM;
        }

        if (MQEnum.ROCKETMQ.toString().equals(mqtype)) {
            mqEnum = MQEnum.ROCKETMQ;
        }

        if (MQEnum.DPBOOT_ROCKETMQ.toString().equals(mqtype)) {
            mqEnum = MQEnum.DPBOOT_ROCKETMQ;
        }
        return mqEnum;
    }

    /**
     * 说明: 杀掉所给定的进程号
     *
     * @param msgId 消息id, processStrs待杀掉的进程号集合
     * @author shulie
     * @date 2018/8/9 14:22
     */
    public void killLinuxProcess(List<String> processStrs, String msgId) {
        Process process = null;
        BufferedReader brReader = null;
        TMqMsg tMqMsg = TMqMsgDao.selectMqMsgById(Long.parseLong(msgId));
        String pids = Joiner.on(" ").join(processStrs);
        try {
            //杀掉进程
            process = Runtime.getRuntime().exec("kill -9 " + pids);
            int exit = process.waitFor();

            if (exit == 0) {
                tMqMsg.setConsumeEndTime(new Date());
                tMqMsg.setConsumeStatus(Constants.CONSUME_SUCCESS);
                TMqMsgDao.updateByPrimaryKey(tMqMsg);
            } else {
                brReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorDesc = new StringBuilder();
                for (String str = brReader.readLine(); str != null; str = brReader.readLine()) {
                    errorDesc.append(str);
                }
                LOGGER.error("MqConsumerService.killLinuxProcess 执行脚本错误信息{}", errorDesc);
                TMqMsgDao.updateConsumeStatusById(msgId, Constants.CONSUME_FAILED);
            }
        } catch (Exception e) {
            LOGGER.info("MqConsumerService.killLinuxProcess 杀掉进程异常{}", e);
            TMqMsgDao.updateConsumeStatusById(msgId, Constants.CONSUME_FAILED);
        } finally {
            closeAll(brReader, null, null, process);
        }
    }

    /**
     * 说明: 执行脚本后打印错误信息
     *
     * @param process 进程类
     * @return 错误信息
     * @author shulie
     * @date 2018/8/9 14:21
     */
    private String printOutContent(InputStream inputStream, ByteArrayOutputStream bytes) throws IOException {
        byte[] bs = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(bs)) > 0) {
            bytes.write(bs, 0, len);
        }
        LOGGER.info("EXCUTE SHELL ERROR INFO : " + new String(bytes.toByteArray()));
        return new String(bytes.toByteArray());
    }

    //=======================================mq消息功CRUD能区==========================================

    /**
     * 保存MQ消息
     *
     * @param tMmqMsg mq消息
     * @return
     */
    public int addMqMsg(TMqMsg tMqMsg) throws TROModuleException {

        int mqMsgExist = TMqMsgDao.mqMsgExist(tMqMsg);
        if (mqMsgExist > 0) {

            throw new TROModuleException(TROErrorEnum.ASSIST_SAVE_MQ_DUPICATE);
        }
        String msgType = tMqMsg.getMsgType();
        if (MQEnum.IBM.toString().equals(msgType) && StringUtils.contains(tMqMsg.getEsbcode(), "|")) {

            throw new TROModuleException(TROErrorEnum.ASSIST_SAVE_MQ_PARAMERROR);
        }

        if (MQEnum.ESB.toString().equals(msgType) || MQEnum.IBM.toString().equals(msgType)) {
            tMqMsg.setTransportType("1");
        }
        Map<String, Object> mqMsgMap = GlobalVariableUtil.getValue(TRODictTypeEnum.MQMSG);
        //先从内存中取数据，如果没有取到或取到空的，则从数据库中拿，然后放到内存中
        if (mqMsgMap == null || mqMsgMap.isEmpty()) {
            mqMsgMap = queryDicList(TRODictTypeEnum.MQMSG);
            if (mqMsgMap == null) {
                throw new TROModuleException(TROErrorEnum.ASSIST_NOT_FOUND_MQTYPE_EXCEPTION);
            }
            GlobalVariableUtil.setValue(TRODictTypeEnum.MQMSG, mqMsgMap);
        }

        Object dicList = mqMsgMap.get("dicList");
        if (!Objects.isNull(dicList) && dicList instanceof Map) {
            Map<String, Object> dicMap = (Map<String, Object>)dicList;
            Map<Object, String> newDicMap = dicMap.entrySet().stream().collect(
                Collectors.toMap(key -> key.getValue(), value -> value.getKey()));
            String valueOrder = MapUtils.getString(newDicMap, tMqMsg.getMsgType());
            tMqMsg.setMsgType(valueOrder);
        }
        tMqMsg.setDictType(String.valueOf(mqMsgMap.get("dictType")));
        tMqMsg.setMsgId(snowflake.next());
        tMqMsg.setEsbcode(filterIllegalCharacters(tMqMsg.getEsbcode()));
        int insert = TMqMsgDao.insert(tMqMsg);

        return insert;
    }

    /**
     * 修改mq消息
     *
     * @param tMqMsg mq消息
     * @throws TROModuleException 找不到消息抛出该异常
     */
    public void updateMqMsg(TMqMsg tMqMsg) throws TROModuleException {
        TMqMsg mqMsg = TMqMsgDao.selectMqMsgById(tMqMsg.getMsgId());
        if (mqMsg == null) {

            throw new TROModuleException(TROErrorEnum.ASSIST_MQMSG_NOT_FOUNT_EXCEPTION);
        }
        TMqMsg originMqMsg = mqMsg;
        String msgType = tMqMsg.getMsgType();
        if (MQEnum.IBM.toString().equals(msgType) && StringUtils.contains(tMqMsg.getEsbcode(), "|")) {

            throw new TROModuleException(TROErrorEnum.ASSIST_SAVE_MQ_PARAMERROR);
        }
        Map<String, Object> mqMsgMap = GlobalVariableUtil.getValue(TRODictTypeEnum.MQMSG);
        //先从内存中取数据，如果没有取到或取到空的，则从数据库中拿，然后放到内存中
        if (mqMsgMap == null || mqMsgMap.isEmpty()) {
            mqMsgMap = queryDicList(TRODictTypeEnum.MQMSG);
            if (mqMsgMap == null) {
                throw new TROModuleException(TROErrorEnum.ASSIST_NOT_FOUND_MQTYPE_EXCEPTION);
            }
            GlobalVariableUtil.setValue(TRODictTypeEnum.MQMSG, mqMsgMap);
        }
        Object dicList = mqMsgMap.get("dicList");
        if (!Objects.isNull(dicList) && dicList instanceof Map) {
            Map<String, Object> dicMap = (Map<String, Object>)dicList;
            Map<Object, String> newDicMap = dicMap.entrySet().stream().collect(
                Collectors.toMap(key -> key.getValue(), value -> value.getKey()));
            String valueOrder = MapUtils.getString(newDicMap, tMqMsg.getMsgType());
            tMqMsg.setMsgType(valueOrder);
        }
        tMqMsg.setDictType(String.valueOf(mqMsgMap.get("dictType")));

        tMqMsg.setEsbcode(filterIllegalCharacters(tMqMsg.getEsbcode()));
        TMqMsgDao.updateByPrimaryKeySelective(tMqMsg);

    }

    /**
     * @param dictContent  数据字典内容
     * @param dictTypeEnum 数据字典类型
     * @return {
     * "valueOrder": "1",
     * "dictType": "123456789"
     * }
     * @description 获取数据字典Map
     * @author shulie
     * @create 2018/8/18 9:02
     */
    @SuppressWarnings("unused")
    private Map<String, Object> getDictMap(String dictContent, TRODictTypeEnum dictTypeEnum) {
        //前端每次传过来的msgType字段为'ESB','IBM','ROCKETMQ'
        // 增加数据字典信息，dictType和valueOrder
        Map<String, Object> map = confCenterService.queryDicList(dictTypeEnum);
        Map<String, Object> dictMap = (Map)map.get("dicList");
        String newDictContent = dictMap.keySet().stream().filter(dictKey -> dictContent.equals(dictMap.get(dictKey)))
            .findFirst().orElse("");
        if (StringUtils.isEmpty(newDictContent)) {
            return null;
        }
        return new HashMap<String, Object>() {{
            put("valueOrder", newDictContent);
            put("dictType", map.get("dictType"));
        }};
    }

    /**
     * 删除消息
     *
     * @param msgId 消息id
     * @return
     */
    public int deleteMqMsg(String msgId) {
        return TMqMsgDao.deleteByPrimaryKey(Long.parseLong(msgId));
    }

    /**
     * 批量删除mq消息
     *
     * @param msgIdList 消息id列表
     */
    public void batchDeleteMqMsg(String msgIdList) {
        List<String> msgIds = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(msgIdList);
        List<TMqMsg> tMqMsgs = TMqMsgDao.queryConsumeListByIds(msgIds);
        TMqMsgDao.batchDeleteMqMsg(msgIds);

    }

    /**
     * @param paramMap {MSG_TYPE, pageSize, pageNum, ESBCODE, START_TIME, END_TIME}
     * @return PageInfo<TMqMsg>
     * @description 查询mq消息列表
     * @author shulie
     * @create 2018/7/31 13:00
     */
    public PageInfo<TMqMsg> queryMqMsgList(Map<String, Object> paramMap) {
        String pageSize = MapUtils.getString(paramMap, "pageSize");
        if (!StringUtils.equals("-1", pageSize)) {
            PageHelper.startPage(PageInfo.getPageNum(paramMap), PageInfo.getPageSize(paramMap));
        }
        String msgType = MapUtils.getString(paramMap, MQConstant.MSGTYPE);
        if (MQConstant.ROCKETMQ.equals(msgType)) {
            List<TMqMsg> mqMsgList = TMqMsgDao.selectRocketMqMsgList(paramMap);

            return new PageInfo<>(mqMsgList);
        }
        List<TMqMsg> mqMsgList = TMqMsgDao.selectMqMsgList(paramMap);

        return new PageInfo<>(mqMsgList);
    }

    /**
     * @param msgId 消息id
     * @return TMqMsg
     * @description 查询mq消息详情
     * @author shulie
     * @create 2018/7/31 13:05
     */
    public TMqMsg queryMqMsgDetail(String msgId) {
        TMqMsg tMqMsg = TMqMsgDao.selectMqMsgById(Long.parseLong(msgId));

        return tMqMsg;
    }
}
