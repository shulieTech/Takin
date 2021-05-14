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

import com.pamirs.tro.entity.domain.entity.TEbmProducer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 说明:
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/9/13 15:25
 */
@Mapper
public interface TEbmProducerDao {

    /**
     * 说明:
     *
     * @param
     * @return
     * @author shulie
     * @date 2018/9/13 15:30
     */
    public void saveEbm(TEbmProducer tEbmProducer);

    /**
     * 说明: 增时防重复校验
     *
     * @param tEbmProducer
     * @return 大于0表示已经存在, 否则不存在
     * @author shulie
     * @date 2018/9/13 15:30
     */
    int selectEbmExist(TEbmProducer tEbmProducer);

    /**
     * 说明: 更新时根据主键查询该消息是否存在
     *
     * @param tepId ESB/IBM的主键id
     * @return
     * @author shulie
     * @date 2018/9/13 17:52
     */
    TEbmProducer selectEbmMsgById(@Param("tepId") Long tepId);

    void updateEbmProduceStatus(@Param("produceStatus") String produceStatus,
        @Param("msgSuccessCount") String msgSuccessCount,
        @Param("produceEndTime") Date produceEndTime,
        @Param("tepId") String tepId);

    /**
     * 说明: 更新生产消息
     *
     * @param tEbmProducer
     * @author shulie
     * @date 2018/9/13 17:52
     */
    void updateEbm(TEbmProducer tEbmProducer);

    /**
     * 说明: 生产消息更新状态
     *
     * @param tEbmProducer
     * @author shulie
     * @date 2018/9/13 17:51
     */
    void updateEbmStatus(TEbmProducer tEbmProducer);

    /**
     * 查询ESB/IBM消息列表
     *
     * @param paramMap
     * @return
     */
    List<TEbmProducer> queryEsbAndIbmList(Map<String, Object> paramMap);

    /**
     * 说明: 根据id列表批量查询ebm生产信息
     *
     * @param eBMProduceIds ebm的id集合
     * @return ebm生产信息集合
     * @author shulie
     * @date 2018/11/6 11:20
     */
    List<TEbmProducer> queryEBMProduceListByIds(@Param("eBMProduceIds") List<String> eBMProduceIds);

    /**
     * 批量删除esb/ibm消息
     *
     * @param tepIdList
     */
    void batchDeleteEsbAndIbm(@Param("tepIdList") List<String> tepIdList);
}
