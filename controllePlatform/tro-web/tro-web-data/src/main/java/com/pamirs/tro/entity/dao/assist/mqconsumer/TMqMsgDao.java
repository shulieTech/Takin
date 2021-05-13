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

package com.pamirs.tro.entity.dao.assist.mqconsumer;

import java.util.List;
import java.util.Map;

import com.pamirs.tro.entity.domain.entity.TMqMsg;
import org.apache.ibatis.annotations.Param;

/**
 * @author shulie
 * @description mq消息接口
 * @create 2018/7/30 16:12
 */
public interface TMqMsgDao {

    /**
     * @param msgId 消息id
     * @return int
     * @description 删除消息
     * @author shulie
     * @create 2018/7/30 16:13
     */
    int deleteByPrimaryKey(Long msgId);

    /**
     * @param tMqMsg 消息对象
     * @return int
     * @description 向数据库中插入一条消息
     * @author shulie
     * @create 2018/7/31 14:22
     */
    int insert(TMqMsg tMqMsg);

    /**
     * @param msgId 消息id
     * @return TMqMsg
     * @description 查询MQ消息详情
     * @author shulie
     * @create 2018/7/31 14:21
     */
    TMqMsg selectMqMsgById(Long msgId);

    /**
     * @param tMqMsg 消息对象
     * @return int
     * @description 根据主键更新消息对象（字段为空不更新）
     * @author shulie
     * @create 2018/7/31 14:20
     */
    int updateByPrimaryKeySelective(TMqMsg tMqMsg);

    /**
     * @param tMqMsg 　消息对象
     * @return int
     * @description 根据主键更新
     * @author shulie
     * @create 2018/7/31 14:19
     */
    int updateByPrimaryKey(TMqMsg tMqMsg);

    /**
     * 批量删除mq消息
     *
     * @param msgIds 消息id列表
     */
    void batchDeleteMqMsg(@Param("msgIds") List<String> msgIds);

    /**
     * 获取mq消息列表
     *
     * @param paramMap 条件信息
     * @return
     */
    List<TMqMsg> selectMqMsgList(Map<String, Object> paramMap);

    /**
     * 获取mq消息列表
     *
     * @param paramMap 条件信息
     * @return
     */
    List<TMqMsg> selectRocketMqMsgList(Map<String, Object> paramMap);

    /**
     * @param msgId         消息id
     * @param consumeStatus 消费状态
     * @return void
     * @description 更新消息消费状态
     * @author shulie
     * @create 2018/8/4 17:28
     */
    void updateConsumeStatusById(@Param("msgId") String msgId, @Param("consumeStatus") String consumeStatus);

    /**
     * 说明: 根据id列表批量查询消费信息
     *
     * @param consumeListIds 消息id集合
     * @return 消费信息集合
     * @author shulie
     * @date 2018/11/6 11:09
     */
    List<TMqMsg> queryConsumeListByIds(@Param("consumeListIds") List<String> consumeListIds);

    /**
     * 说明: 校验该mq信息是否存在
     *
     * @param tMqMsg mq消息实体
     * @return >0表示存在，否则不存在可添加
     * @author shulie
     * @date 2018/8/6 17:14
     */
    int mqMsgExist(TMqMsg tMqMsg);
}
