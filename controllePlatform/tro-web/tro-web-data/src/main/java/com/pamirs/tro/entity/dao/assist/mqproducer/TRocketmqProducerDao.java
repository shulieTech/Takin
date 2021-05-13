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

package com.pamirs.tro.entity.dao.assist.mqproducer;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pamirs.tro.entity.domain.entity.TRocketmqProducer;
import org.apache.ibatis.annotations.Param;

/**
 * @author lk
 * @description ESB/IBM生产消息接口
 * @create 2018/9/13 13:42
 */
public interface TRocketmqProducerDao {

    /**
     * 说明: 校验该是否存在
     *
     * @param tRocketmqProducer 消息实体
     * @return >0表示存在，否则不存在可添加
     */
    int isRocketmqExist(TRocketmqProducer tRocketmqProducer);

    /**
     * @param tRocketmqProducer 消息对象
     * @return int
     * @description 向数据库中插入一条消息
     */
    int insert(TRocketmqProducer tRocketmqProducer);

    /**
     * 查询rocketmq消息列表
     *
     * @param paramMap
     * @return
     */
    List<TRocketmqProducer> queryRocketmqList(Map<String, Object> paramMap);

    /**
     * 根据id查询，返回对象
     *
     * @param trpId
     * @return
     */

    TRocketmqProducer selectRocketMqMsgById(long trpId);

    /**
     * 批量删除rocketmq生产消息
     *
     * @param trpIdList
     */
    void batchDeleteRocketMq(@Param("trpIdList") List<String> trpIdList);

    /**
     * 查询rocketmq消息详情
     *
     * @param trpId rocketmq生产消息id
     * @return
     */
    TRocketmqProducer queryRocketmqDetail(String trpId);

    /**
     * 说明: 根据id列表批量查询rocketmq生产信息
     *
     * @param rocketMqProduceListIds id列表
     * @return rocketmq生产信息列表
     * @author shulie
     * @date 2018/11/6 14:24
     */
    List<TRocketmqProducer> queryRocketMqListByIds(
        @Param("rocketMqProduceListIds") List<String> rocketMqProduceListIds);

    /**
     * 更新生产消息状态
     *
     * @param tRocketmqProducer
     */
    void updateRocketMqStatus(TRocketmqProducer tRocketmqProducer);

    /**
     * 更新生产状态、成功数、结束时间
     *
     * @param produceStatus
     * @param msgSuccessCount
     * @param produceEndTime
     * @param trpId
     */
    void updateRocketMqProduceStatus(@Param("produceStatus") String produceStatus,
        @Param("msgSuccessCount") String msgSuccessCount,
        @Param("produceEndTime") Date produceEndTime,
        @Param("trpId") String trpId);

    /**
     * 根据id查询，判断是否存在。
     *
     * @param rocketmqExist
     * @return
     */
    TRocketmqProducer isRocketmqExistById(Long rocketmqExist);

    /**
     * 更新操作
     *
     * @param tRocketmqProducer
     */

    void update(TRocketmqProducer tRocketmqProducer);
}
