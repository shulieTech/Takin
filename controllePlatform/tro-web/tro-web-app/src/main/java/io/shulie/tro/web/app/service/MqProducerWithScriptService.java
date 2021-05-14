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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.tro.common.constant.Constants;
import com.pamirs.tro.common.constant.MQConstant;
import com.pamirs.tro.common.constant.MQEnum;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.common.util.ProcessUtils;
import com.pamirs.tro.entity.dao.assist.mqproducer.TEbmProducerDao;
import com.pamirs.tro.entity.dao.assist.mqproducer.TRocketmqProducerDao;
import com.pamirs.tro.entity.domain.entity.TEbmProducer;
import com.pamirs.tro.entity.domain.entity.TRocketmqProducer;
import io.shulie.tro.web.app.common.CommonService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 说明:  调用脚本方式来开启/停止生产任务
 *
 * @author shulie
 * @version 1.0
 * @date 2018/9/21 12:13
 */
@Service
public class MqProducerWithScriptService extends CommonService {

    /**
     * 说明: mq消息类型集合
     *
     * @author shulie
     * @date 2018/9/21 12:13
     */
    private static List<String> mqTypeList = null;

    static {
        mqTypeList = Lists.newArrayList(MQEnum.values()).stream().map(mqEnum -> mqEnum.toString()).collect(
            Collectors.toList());
    }

    @Autowired
    public TRocketmqProducerDao TRocketmqProducerDao;
    @Autowired
    private TEbmProducerDao tEbmProducerDao;

    /**
     * 说明: 开启esb/ibm生产
     *
     * @author shulie
     * @date 2018/9/21 12:13
     */
    public void executeStartEbmScript(Map<String, Object> paraMap) throws TROModuleException {
        String mqtype = MapUtils.getString(paraMap, MQConstant.MSGTYPE);
        if (!mqTypeList.contains(mqtype)) {
            throw new TROModuleException(TROErrorEnum.ASSIST_START_CONSUMER_PARAMERROR);
        }
        String tepId = MapUtils.getString(paraMap, MQConstant.EBMID);
        TEbmProducer tEbmProducer = tEbmProducerDao.selectEbmMsgById(Long.parseLong(tepId));
        if (tEbmProducer == null) {
            throw new TROModuleException(TROErrorEnum.ASSIST_MQPRODUCER_EBM_MSG_NOTFOUND);
        }
        TEbmProducer originEbmProducer = tEbmProducer;
        tEbmProducer.setProduceStartTime(new Date());
        tEbmProducer.setLastProduceTime(tEbmProducer.getProduceEndTime());
        tEbmProducer.setProduceStatus(Constants.PRODUCING);
        tEbmProducerDao.updateEbmStatus(tEbmProducer);

        runShellTaskExecutor.execute(
            asyncStarProduceScript(paraMap, MQEnum.ESB.toString().equals(mqtype) ? MQEnum.ESB : MQEnum.IBM));
    }

    /**
     * 说明: 开启rocketmq/dpboot_rocketmq生产
     *
     * @author shulie
     * @date 2018/9/21 12:13
     */
    public void executeStartRocketmqScript(Map<String, Object> paraMap) throws TROModuleException {
        String mqtype = MapUtils.getString(paraMap, MQConstant.MSGTYPE);
        if (!mqTypeList.contains(mqtype)) {
            throw new TROModuleException(TROErrorEnum.ASSIST_START_CONSUMER_PARAMERROR);
        }
        String trpId = MapUtils.getString(paraMap, MQConstant.ROCKETMQID);
        TRocketmqProducer tRocketmqProducer = TRocketmqProducerDao.selectRocketMqMsgById(Long.parseLong(trpId));
        if (tRocketmqProducer == null) {
            throw new TROModuleException(TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_MSG_NOTFOUND);
        }
        TRocketmqProducer originEbmProducer = tRocketmqProducer;
        tRocketmqProducer.setProduceStartTime(new Date());
        tRocketmqProducer.setLastProduceTime(tRocketmqProducer.getProduceEndTime());
        tRocketmqProducer.setProduceStatus(Constants.PRODUCING);
        TRocketmqProducerDao.updateRocketMqStatus(tRocketmqProducer);

        runShellTaskExecutor.execute(asyncStarProduceScript(paraMap,
            MQEnum.ROCKETMQ.toString().equals(mqtype) ? MQEnum.ROCKETMQ : MQEnum.DPBOOT_ROCKETMQ));
    }

    /**
     * 说明: 异步执行启动生产脚本
     *
     * @param paraMap 脚本参数
     * @throws Exception 异常
     * @author shulie
     */
    public Runnable asyncStarProduceScript(Map<String, Object> paraMap, MQEnum mqEnum) {
        //ESB和IBM特有的参数
        String hostName = MapUtils.getString(paraMap, MQConstant.MSGHOST);
        String port = MapUtils.getString(paraMap, MQConstant.MSGPORT);
        String queueManager = MapUtils.getString(paraMap, MQConstant.QUEUEMANAGER);
        String channel = MapUtils.getString(paraMap, MQConstant.QUEUECHANNEL);
        String CCSID = MapUtils.getString(paraMap, MQConstant.CCSID);
        String esbcode = MapUtils.getString(paraMap, MQConstant.ESBCODE);
        String requestComout = MapUtils.getString(paraMap, MQConstant.REQUESTCOMOUT);
        String tepId = MapUtils.getString(paraMap, MQConstant.EBMID);

        //ROCKETMQ/DPBOOT_ROCKETMQ特有的参数
        String topic = MapUtils.getString(paraMap, MQConstant.TOPIC);
        String groupName = MapUtils.getString(paraMap, MQConstant.GROUPNAME);
        String clusterIp = MapUtils.getString(paraMap, MQConstant.MSGIP);
        String trpId = MapUtils.getString(paraMap, MQConstant.ROCKETMQID);

        //ESB、IBM和ROCKETMQ/DPBOOT_ROCKETMQ 共有的参数
        String threadCount = MapUtils.getString(paraMap, MQConstant.THREADCOUNT);
        String msgCount = MapUtils.getString(paraMap, MQConstant.MSGCOUNT);
        String messageSize = MapUtils.getString(paraMap, MQConstant.MESSAGESIZE);
        String sleepTime = MapUtils.getString(paraMap, MQConstant.SLEEPTIME);

        String shStrPath = getBasePath() + getMqProducerScriptName();
        LOGGER.info("========================shStrPath===============" + shStrPath);
        return () -> {
            Process ps = null;
            Process process = null;
            InputStream inputStream = null;
            ByteArrayOutputStream bytes = null;
            try {
                String cmd = "";
                switch (mqEnum) {
                    case ESB:
                        cmd = shStrPath + " " + mqEnum.toString() + " " + hostName + " " + port + " " + queueManager
                            + " " + channel + " " + CCSID + " " + esbcode + " " + requestComout + " " + threadCount
                            + " " + msgCount + " " + messageSize + " " + sleepTime;
                        break;
                    case IBM:
                        cmd = shStrPath + " " + mqEnum.toString() + " " + hostName + " " + port + " " + queueManager
                            + " " + channel + " " + CCSID + " " + esbcode + " " + requestComout + " " + threadCount
                            + " " + msgCount + " " + messageSize + " " + sleepTime;
                        break;
                    case ROCKETMQ:
                        cmd = shStrPath + " " + mqEnum.toString() + " " + topic + " " + groupName + " " + clusterIp
                            + " " + threadCount + " " + msgCount + " " + messageSize + " " + sleepTime;
                        break;
                    case DPBOOT_ROCKETMQ:
                        cmd = shStrPath + " " + mqEnum.toString() + " " + topic + " " + groupName + " " + clusterIp
                            + " " + threadCount + " " + msgCount + " " + messageSize + " " + sleepTime;
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
                //                    updateFailedStatus(mqEnum, tepId, trpId);
                //                }
            } catch (Exception e) {
                //更新状态为失败
                LOGGER.error("MqProducerWithScriptService.asyncStarProduceScript 启动消息错误{}", e);
                updateFailedStatus(mqEnum, tepId, trpId);
            } finally {
                closeAll(null, inputStream, bytes, ps, process);
            }
        };
    }

    /**
     * 更新失败状态
     *
     * @param mqEnum mq类型
     * @param tepId  ebm id
     * @param trpId  rocketmq id
     */
    private void updateFailedStatus(MQEnum mqEnum, String tepId, String trpId) {
        boolean isEbm = StringUtils.isNotEmpty(tepId) && (MQEnum.ESB.toString().equals(mqEnum.toString()) || MQEnum.IBM
            .toString().equals(mqEnum.toString()));
        if (isEbm) {
            tEbmProducerDao.updateEbmProduceStatus(Constants.PRODUCE_FAILED, null, null, tepId);
            return;
        }

        boolean isRocketMq = StringUtils.isNotEmpty(trpId) && (MQEnum.ROCKETMQ.toString().equals(mqEnum.toString())
            || MQEnum.DPBOOT_ROCKETMQ.toString().equals(mqEnum.toString()));
        if (isRocketMq) {
            TRocketmqProducerDao.updateRocketMqProduceStatus(Constants.PRODUCE_FAILED, null, null, trpId);
        }
    }

    /**
     * 说明: 根据消息类型停止生产消息
     * 1: ESB
     * 2: IBM
     * 3: ROCKETMQ
     * 4. DPBOOT_ROCKETMQ
     *
     * @param mqType
     * @param id
     * @return
     * @author shulie
     * @date 2018/9/21 14:08
     */
    public void stopMq(String mqType, String id) {
        Map<String, Object> paramMap = Maps.newHashMap();
        if (MQConstant.ESB.equals(mqType) || MQConstant.IBM.equals(mqType)) {
            TEbmProducer tEbmProducer = tEbmProducerDao.selectEbmMsgById(Long.parseLong(id));
            paramMap.put(MQConstant.EBMID, id);
            paramMap.put(MQConstant.MSGTYPE,
                MQConstant.ESB.equals(mqType) ? MQEnum.ESB.toString() : MQEnum.IBM.toString());
            paramMap.put(MQConstant.MSGHOST, tEbmProducer.getMsgHost());
            paramMap.put(MQConstant.MSGPORT, tEbmProducer.getMsgPort());
            paramMap.put(MQConstant.QUEUEMANAGER, tEbmProducer.getQueueManager());
            paramMap.put(MQConstant.QUEUECHANNEL, tEbmProducer.getQueueChannel());
            paramMap.put(MQConstant.CCSID, tEbmProducer.getCcsid());
            paramMap.put(MQConstant.ESBCODE, tEbmProducer.getEsbcode());
            paramMap.put(MQConstant.REQUESTCOMOUT, tEbmProducer.getRequestComout());
            paramMap.put(MQConstant.THREADCOUNT, tEbmProducer.getThreadCount());
            paramMap.put(MQConstant.MSGCOUNT, tEbmProducer.getMsgCount());
            paramMap.put(MQConstant.MESSAGESIZE, tEbmProducer.getMessageSize());
            paramMap.put(MQConstant.SLEEPTIME, tEbmProducer.getSleepTime());
        }
        if (MQConstant.ROCKETMQ.equals(mqType) || MQConstant.DPBOOT_ROCKETMQ.equals(mqType)) {
            TRocketmqProducer tRocketmqProducer = TRocketmqProducerDao.selectRocketMqMsgById(Long.parseLong(id));
            paramMap.put(MQConstant.ROCKETMQID, id);
            paramMap.put(MQConstant.MSGTYPE,
                MQConstant.ROCKETMQ.equals(mqType) ? MQEnum.ROCKETMQ.toString() : MQEnum.DPBOOT_ROCKETMQ.toString());
            paramMap.put(MQConstant.TOPIC, tRocketmqProducer.getTopic());
            paramMap.put(MQConstant.GROUPNAME, tRocketmqProducer.getGroupName());
            paramMap.put(MQConstant.MSGIP, tRocketmqProducer.getMsgIp());
            paramMap.put(MQConstant.THREADCOUNT, tRocketmqProducer.getThreadCount());
            paramMap.put(MQConstant.MSGCOUNT, tRocketmqProducer.getMsgCount());
            paramMap.put(MQConstant.MESSAGESIZE, tRocketmqProducer.getMessageSize());
            paramMap.put(MQConstant.SLEEPTIME, tRocketmqProducer.getSleepTime());
        }

        executeStopProduceScript(paramMap);

    }

    /**
     * 说明: 停止消费进程
     *
     * @param paraMap 消息id
     * @return
     * @author shulie
     * @date 2018/7/31 16:43
     */
    public void executeStopProduceScript(Map<String, Object> paraMap) {

        List<String> piDs = getPIDs(paraMap);

        String mqtype = MapUtils.getString(paraMap, MQConstant.MSGTYPE);
        if (MQEnum.ROCKETMQ.toString().equals(mqtype) || MQEnum.DPBOOT_ROCKETMQ.toString().equals(mqtype)) {
            killRocketmqProcess(piDs, MapUtils.getString(paraMap, MQConstant.ROCKETMQID));
        } else {
            killEbmProcess(piDs, MapUtils.getString(paraMap, MQConstant.EBMID));
        }
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
        String mqtype = MapUtils.getString(paraMap, MQConstant.MSGTYPE);
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
        //ESB和IBM特有的参数
        String hostName = MapUtils.getString(paraMap, MQConstant.MSGHOST);
        String port = MapUtils.getString(paraMap, MQConstant.MSGPORT);
        String queueManager = MapUtils.getString(paraMap, MQConstant.QUEUEMANAGER);
        String channel = MapUtils.getString(paraMap, MQConstant.QUEUECHANNEL);
        String CCSID = MapUtils.getString(paraMap, MQConstant.CCSID);
        String esbcode = MapUtils.getString(paraMap, MQConstant.ESBCODE);
        String requestComout = MapUtils.getString(paraMap, MQConstant.REQUESTCOMOUT);
        String tepId = MapUtils.getString(paraMap, MQConstant.EBMID);

        //ROCKETMQ特有的参数
        String topic = MapUtils.getString(paraMap, MQConstant.TOPIC);
        String groupName = MapUtils.getString(paraMap, MQConstant.GROUPNAME);
        String clusterIp = MapUtils.getString(paraMap, MQConstant.MSGIP);
        String trpId = MapUtils.getString(paraMap, MQConstant.ROCKETMQID);

        //ESB、IBM和ROCKETMQ共有的参数
        String threatCount = MapUtils.getString(paraMap, MQConstant.THREADCOUNT);
        String msgCount = MapUtils.getString(paraMap, MQConstant.MSGCOUNT);
        String messageSize = MapUtils.getString(paraMap, MQConstant.MESSAGESIZE);
        String sleepTime = MapUtils.getString(paraMap, MQConstant.SLEEPTIME);

        String cmd = "";
        switch (mqEnum) {
            case ESB:
                cmd = mqEnum.toString() + " " + hostName + " " + port + " " + queueManager + " " + channel + " " + CCSID
                    + " " + esbcode + " " + requestComout + " " + threatCount + " " + msgCount + " " + messageSize + " "
                    + sleepTime;
                break;
            case IBM:
                cmd = mqEnum.toString() + " " + hostName + " " + port + " " + queueManager + " " + channel + " " + CCSID
                    + " " + esbcode + " " + requestComout + " " + threatCount + " " + msgCount + " " + messageSize + " "
                    + sleepTime;
                break;
            case ROCKETMQ:
                cmd = mqEnum.toString() + " " + topic + " " + groupName + " " + clusterIp + " " + threatCount + " "
                    + msgCount + " " + messageSize + " " + sleepTime;
                break;
            case DPBOOT_ROCKETMQ:
                cmd = mqEnum.toString() + " " + topic + " " + groupName + " " + clusterIp + " " + threatCount + " "
                    + msgCount + " " + messageSize + " " + sleepTime;
                break;
            default:
                break;
        }

        cmd = " ps -ef | grep MqServerMain | grep " + "\"" + cmd + "\"" + " | grep -v grep";

        LOGGER.info("生产消息停止脚本的命令为{} " + cmd);
        BufferedReader reader = null;
        BufferedReader brReader = null;
        StringBuffer sb = new StringBuffer();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", cmd});
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            int exit = process.waitFor();
            LOGGER.info("stop pid exit info ----->" + exit);
            if (exit != 0) {
                brReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorDesc = new StringBuilder();
                for (String str = brReader.readLine(); str != null; str = brReader.readLine()) {
                    errorDesc.append(str);
                }
                brReader.close();
                LOGGER.error("MqProducerWithScriptService.getPIDs 执行脚本错误信息{}", errorDesc);
                updateFailedStatus(mqEnum, tepId, trpId);
            }
        } catch (Exception e) {
            LOGGER.error("MqProducerWithScriptService.getPIDs 获取进程号执行脚本异常{}", e);
            updateFailedStatus(mqEnum, tepId, trpId);
        } finally {
            closeAll(reader, null, null, process);
        }
        LOGGER.info("produce进程号集合为{}" + ProcessUtils.getPIdsList(sb.toString()));
        return ProcessUtils.getPIdsList(sb.toString());
    }

    /**
     * 说明: 杀掉所给定的进程号(ROCKETMQ/DPBOOT_ROCKETMQ)
     *
     * @param trpId 消息id, processStrs待杀掉的进程号集合
     * @author shulie
     * @date 2018/8/9 14:22
     */
    public void killRocketmqProcess(List<String> processStrs, String trpId) {
        Process process = null;
        BufferedReader brReader = null;
        TRocketmqProducer tRocketmqProducer = TRocketmqProducerDao.selectRocketMqMsgById(Long.parseLong(trpId));
        String pids = Joiner.on(" ").join(processStrs);
        try {
            //杀掉进程
            process = Runtime.getRuntime().exec("kill -9 " + pids);
            int produceExit = process.waitFor();

            LOGGER.info("stop produceExit info ----->" + produceExit);

            if (produceExit == 0) {
                tRocketmqProducer.setProduceStatus(Constants.PRODUCE_FINISH);
                tRocketmqProducer.setProduceEndTime(new Date());
                TRocketmqProducerDao.updateRocketMqStatus(tRocketmqProducer);
            } else {
                brReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorDesc = new StringBuilder();
                for (String str = brReader.readLine(); str != null; str = brReader.readLine()) {
                    errorDesc.append(str);
                }
                LOGGER.error("MqProducerWithScriptService.getPIDs 执行脚本错误信息{}", errorDesc);
                TRocketmqProducerDao.updateRocketMqProduceStatus(Constants.PRODUCE_FAILED, null, null, trpId);
            }
        } catch (Exception e) {
            LOGGER.info("MqProducerWithScriptService.killRocketmqProcess 杀掉进程异常{}", e);
            TRocketmqProducerDao.updateRocketMqProduceStatus(Constants.PRODUCE_FAILED, null, null, trpId);
        } finally {
            closeAll(brReader, null, null, process);
        }
    }

    /**
     * 说明: 杀掉所给定的进程号(esb和ibm)
     *
     * @param tepId 消息id, processStrs待杀掉的进程号集合
     * @author shulie
     * @date 2018/8/9 14:22
     */
    public void killEbmProcess(List<String> processStrs, String tepId) {
        Process process = null;
        BufferedReader brReader = null;
        TEbmProducer tEbmProducer = tEbmProducerDao.selectEbmMsgById(Long.parseLong(tepId));
        String pids = Joiner.on(" ").join(processStrs);
        try {
            //杀掉进程
            process = Runtime.getRuntime().exec("kill -9 " + pids);
            int ebmExit = process.waitFor();
            if (ebmExit == 0) {
                tEbmProducer.setProduceStatus(Constants.PRODUCE_FINISH);
                tEbmProducer.setProduceEndTime(new Date());
                tEbmProducerDao.updateEbmStatus(tEbmProducer);
            } else {
                brReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorDesc = new StringBuilder();
                for (String str = brReader.readLine(); str != null; str = brReader.readLine()) {
                    errorDesc.append(str);
                }
                LOGGER.error("MqProducerWithScriptService.getPIDs 执行脚本错误信息{}", errorDesc);
                tEbmProducerDao.updateEbmProduceStatus(Constants.PRODUCE_FAILED, null, null, tepId);
            }
        } catch (Exception e) {
            LOGGER.info("MqProducerWithScriptService.killEbmProcess 杀掉进程异常{}", e);
            tEbmProducerDao.updateEbmProduceStatus(Constants.PRODUCE_FAILED, null, null, tepId);
        } finally {
            closeAll(brReader, null, null, process);
        }
    }

    /**
     * 说明: 开启rocketmq生产
     *
     * @author shulie
     * @date 2018/9/21 12:13
     */

    /**
     * 说明: 结束rocketmq生产
     *
     * @author shulie
     * @date 2018/9/21 12:13
     */

    /**
     * 说明: 开启esb/ibm生产
     *
     * @author shulie
     * @date 2018/9/21 12:13
     */

    /**
     * 说明: 执行脚本后打印错误信息
     *
     * @param inputStream 错误信息
     * @return 错误信息
     * @author shulie
     * @date 2018/8/9 14:21
     */
    //    private String printOutContent(InputStream inputStream, ByteArrayOutputStream bytes) throws IOException {
    //        byte[] bs = new byte[1024];
    //        int len = 0;
    //        while ((len = inputStream.read(bs)) > 0) {
    //            bytes.write(bs, 0, len);
    //            bytes.flush();
    //        }
    //        LOGGER.info("EXCUTE SHELL ERROR INFO : " + new String(bytes.toByteArray()));
    //        return new String(bytes.toByteArray());
    //    }

}
