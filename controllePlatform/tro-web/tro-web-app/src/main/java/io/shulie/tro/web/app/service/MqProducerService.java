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

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Splitter;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.common.util.PageInfo;
import com.pamirs.tro.entity.dao.assist.mqproducer.TEbmProducerDao;
import com.pamirs.tro.entity.dao.assist.mqproducer.TRocketmqProducerDao;
import com.pamirs.tro.entity.domain.entity.TEbmProducer;
import com.pamirs.tro.entity.domain.entity.TRocketmqProducer;
import io.shulie.tro.web.app.common.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 说明: mq虚拟生产消息service
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/9/13 12:50
 */
@Service
public class MqProducerService extends CommonService {

    @Autowired
    public TRocketmqProducerDao TRocketmqProducerDao;
    @Autowired
    private TEbmProducerDao tEbmProducerDao;

    /**
     * 新增rocketmq信息
     *
     * @param tRocketmqProducer
     * @throws TROModuleException
     */

    public void addRocketmq(TRocketmqProducer tRocketmqProducer) throws TROModuleException {

        int rocketmqExist = TRocketmqProducerDao.isRocketmqExist(tRocketmqProducer);
        if (rocketmqExist > 0) {
            throw new TROModuleException(TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_SAVE_DUPICATE);
        }
        tRocketmqProducer.setTrpId(snowflake.next());
        tRocketmqProducer.setProduceStatus("0");

        String regex = "^[1-9]+[0-9]*$";
        Pattern p = Pattern.compile(regex);
        Matcher m1 = p.matcher(tRocketmqProducer.getSleepTime());
        Matcher m2 = p.matcher(tRocketmqProducer.getMsgCount());

        //        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.
        //        (\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        //        Pattern p1 = Pattern.compile(rexp);
        //        Matcher m3 = p1.matcher(tRocketmqProducer.getMsgIp());

        if (m1.find() && m2.find()) {
            TRocketmqProducerDao.insert(tRocketmqProducer);

        } else {

            throw new TROModuleException(TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_SAVE_EXCEPTION);
        }
    }

    /**
     * 更新rocketMq信息
     *
     * @param tRocketmqProducer
     * @throws TROModuleException
     */
    public void updateRocketmq(TRocketmqProducer tRocketmqProducer) throws TROModuleException {
        Long rocketmqExist = tRocketmqProducer.getTrpId();
        TRocketmqProducer rocketmqExistById = TRocketmqProducerDao.isRocketmqExistById(rocketmqExist);
        if (rocketmqExistById != null) {

            String regex = "^[1-9]+[0-9]*$";
            Pattern p = Pattern.compile(regex);
            Matcher m1 = p.matcher(tRocketmqProducer.getSleepTime());
            Matcher m2 = p.matcher(tRocketmqProducer.getMsgCount());

            //            String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.
            //            (\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
            //            Pattern p1 = Pattern.compile(rexp);
            //            Matcher m3 = p1.matcher(tRocketmqProducer.getMsgIp());

            if (m1.find() && m2.find()) {
                TRocketmqProducerDao.update(tRocketmqProducer);

            }
        } else {
            throw new TROModuleException(TROErrorEnum.ASSIST_MQPRODUCER_ROCKETMQ_UPDATE_EXCEPTION);
        }
    }

    /**
     * 说明: API.05.03.004 新增ESB/IBM虚拟发送消息
     *
     * @param tEbmProducer 消息封装对象
     * @author shulie
     * @date 2018/9/13 16:45
     */
    public void saveEbm(TEbmProducer tEbmProducer) throws TROModuleException {
        int exist = tEbmProducerDao.selectEbmExist(tEbmProducer);
        if (exist > 0) {
            throw new TROModuleException(TROErrorEnum.ASSIST_MQPRODUCER_EBM_SAVE_DUPICATE);
        }
        tEbmProducer.setTepId(snowflake.next());
        tEbmProducer.setProduceStatus("0");
        tEbmProducer.setEsbcode(filterIllegalCharacters(tEbmProducer.getEsbcode()));
        tEbmProducerDao.saveEbm(tEbmProducer);

    }

    /**
     * 说明: API.05.03.006 修改ESB/IBM虚拟发送消息
     *
     * @param tEbmProducer 待修改的消息对象信息
     * @author shulie
     * @date 2018/9/13 16:45
     */
    public void updateEbm(TEbmProducer tEbmProducer) throws TROModuleException {
        TEbmProducer ebmProducer = tEbmProducerDao.selectEbmMsgById(tEbmProducer.getTepId());
        if (ebmProducer == null) {
            throw new TROModuleException(TROErrorEnum.ASSIST_MQPRODUCER_EBM_NOT_FOUNT_EXCEPTION);
        }
        tEbmProducerDao.updateEbm(tEbmProducer);

    }

    /**
     * 查询rocketmq生产消息列表
     *
     * @param paramMap {pageNum, pageSize, topic, groupName}
     * @return
     */
    public PageInfo<TRocketmqProducer> queryRocketmqList(Map<String, Object> paramMap) {
        PageHelper.startPage(PageInfo.getPageNum(paramMap), PageInfo.getPageSize(paramMap));
        List<TRocketmqProducer> rocketmqProducerList = TRocketmqProducerDao.queryRocketmqList(paramMap);

        return new PageInfo<>(rocketmqProducerList);
    }

    /**
     * 批量删除rocketmq消息
     *
     * @param trpIds rocketmq生产消息id列表
     */
    public void deteletRocketMq(String trpIds) {
        List<String> trpIdList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(trpIds);
        TRocketmqProducerDao.batchDeleteRocketMq(trpIdList);
        List<TRocketmqProducer> tRocketmqProducers = TRocketmqProducerDao.queryRocketMqListByIds(trpIdList);

    }

    /**
     * 查询rocketmq消息详情
     *
     * @param trpId rocketmq生产消息id
     * @return
     */
    public TRocketmqProducer queryRocketeMqDetail(String trpId) {
        TRocketmqProducer tRocketmqProducer = TRocketmqProducerDao.queryRocketmqDetail(trpId);

        return tRocketmqProducer;
    }

    /**
     * 查询esb/ibm消息详情
     *
     * @param tepId ESB/IBM消息id
     * @return
     */
    public TEbmProducer queryEsbOrIbmDetail(String tepId) {
        TEbmProducer tEbmProducer = tEbmProducerDao.selectEbmMsgById(Long.parseLong(tepId));

        return tEbmProducer;
    }

    /**
     * 查询ESB/IBM消息列表
     *
     * @param paramMap { pageNum, pageSize, esb, produceStartTime, produceEndTime }
     * @return
     */
    public PageInfo<TEbmProducer> queryEsbAndIbmList(Map<String, Object> paramMap) {
        PageHelper.startPage(PageInfo.getPageNum(paramMap), PageInfo.getPageSize(paramMap));
        List<TEbmProducer> ebmProducerList = tEbmProducerDao.queryEsbAndIbmList(paramMap);

        return new PageInfo<>(ebmProducerList);
    }

    /**
     * 批量删除esb/ibm消息
     *
     * @param tepIds
     */
    public void deteletEsbOrIbm(String tepIds) {
        List<String> tepIdList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(tepIds);
        tEbmProducerDao.batchDeleteEsbAndIbm(tepIdList);
        List<TEbmProducer> tEbmProducers = tEbmProducerDao.queryEBMProduceListByIds(tepIdList);

    }
}
