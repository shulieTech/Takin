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

package com.pamirs.tro.entity.dao.confcenter;

import java.util.List;
import java.util.Map;

import com.pamirs.tro.entity.domain.entity.TLinkServiceMnt;
import com.pamirs.tro.entity.domain.entity.TSecondLinkMnt;
import com.pamirs.tro.entity.domain.vo.TLinkApplicationInterface;
import com.pamirs.tro.entity.domain.vo.TLinkBasicVO;
import com.pamirs.tro.entity.domain.vo.TLinkMntDictoryVo;
import com.pamirs.tro.entity.domain.vo.TLinkNodesVo;
import com.pamirs.tro.entity.domain.vo.TLinkServiceMntVo;
import com.pamirs.tro.entity.domain.vo.TLinkTopologyInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 说明: 链路管理dao层
 *
 * @author shulie
 * @version v1.0
 * @2018年4月26日
 */
@Mapper
public interface TLinkMntDao {

    /**
     * 说明: 保存时根据链路名称判断链路是否存在
     *
     * @param linkName 链路名称
     * @return 大于0表示该应用已存在, 否则不存在
     * @author shulie
     */
    int saveLinkExist(@Param("linkName") String linkName);

    /**
     * 说明: 更新时根据链路id判断链路是否存在
     *
     * @param linkId 链路名称
     * @return 大于0表示该应用已存在, 否则不存在
     * @author shulie
     */
    int updateLinkExist(@Param("linkId") String linkId);

    /**
     * 说明: 保存时校验链路关联服务是否已经关联
     *
     * @param linkId        链路名称
     * @param interfaceName 接口名称
     * @return 大于0表示该应用已存在, 否则不存在
     * @author shulie
     */
    int saveLinkInterfaceExist(@Param("linkId") String linkId, @Param("interfaceName") String interfaceName);

    /**
     * 说明: 更新时校验链路关联服务是否已经关联
     *
     * @param linkId        链路名称
     * @param linkServiceId 链路服务id
     * @return 大于0表示该应用已存在, 否则不存在
     * @author shulie
     */
    int updateLinkInterfaceExist(@Param("linkId") String linkId, @Param("linkServiceId") String linkServiceId);

    /**
     * 说明: 添加链路信息
     *
     * @param tLinkServiceMntVo 链路服务实体类
     * @author shulie
     */
    void addBasicLink(TLinkServiceMntVo tLinkServiceMntVo);

    /**
     * 说明: 添加链路服务
     *
     * @param tLinkServiceMntLists 链路服务实体类列表
     * @author shulie
     */
    void addLinkInterface(@Param("tLinkServiceMntLists") List<TLinkServiceMnt> tLinkServiceMntLists);

    /**
     * 说明: 查询链路信息列表
     *
     * @param paramMap 链路名称,应用名称,负责人工号,接口名称链路等级
     * @return 链路列表
     * @author shulie
     */
    List<TLinkApplicationInterface> queryBasicLinkList(Map<String, Object> paramMap);

    List<TLinkServiceMntVo> queryBasicLinkListDownload(Map<String, Object> paramMap);

    List<TLinkServiceMnt> queryLinkInterface(@Param("linkId") String linkId);

    /**
     * 说明: 根据链路id和接口名称查询链路服务id
     *
     * @param linkId        链路id
     * @param interfaceName 接口名称
     * @return 链路服务id
     * @author shulie
     */
    String selectLinkInterfaceId(@Param("linkId") String linkId,
        @Param("interfaceName") String interfaceName
    );

    /**
     * 说明: 根据基础链路id查询链路信息详情
     *
     * @param linkId 链路id
     * @return 链路服务信息
     * @author shulie
     */
    TLinkServiceMntVo queryLinkByLinkId(@Param("linkId") String linkId);

    /**
     * 说明: 根据业务链路的id批量转换业务链路名称
     *
     * @param linkIdList 业务链路id集合
     * @return 业务链路名称集合
     * @author shulie
     * @date 2018/12/27 14:38
     */
    List<String> transferBusinessLinkName(@Param("linkIdList") List<String> linkIdList);

    List<Map<String, Object>> transferBusinessLinkNameAndId(@Param("linkIdList") List<String> linkIdList);

    /**
     * 说明：根据二级链路ID查询基础链路列表
     *
     * @param secondLinkId 二级链路ID
     * @return
     * @author zhangxian
     */
    List<TLinkServiceMntVo> queryLinksBySecondLinkId(@Param("secondLinkId") String secondLinkId);

    /**
     * 说明: 根据二级链路id和选中的基础链路ids查询选中的基础链路信息
     *
     * @param map 二级链路id和多个基础链路id
     * @return 该二级链路下选中的基础链路信息
     * @author shulie
     * @date 2018/7/11 14:18
     */
    List<TLinkServiceMntVo> queryBasicLinkInfoBySecondLinkIdAndBasicLInks(
        @Param("secondLinkId") String secondLinkId,
        @Param("baseLinkIdsList") List<String> baseLinkIdsList
    );

    /**
     * 说明: 批量删除链路
     *
     * @param linkIdLists 链路id列表
     * @author shulie
     */
    void deleteLinkByLinkIds(@Param("linkIdLists") List<String> linkIdLists);

    /**
     * 说明: 删除业务链路和技术链路关联关系
     *
     * @param
     * @return
     * @author shulie
     * @date 2018/12/27 17:31
     */
    void deleteBTLinkRelationShip(@Param("linkIdLists") List<String> linkIdLists);

    /**
     * 说明: 批量删除链路服务信息
     *
     * @param linkIdLists 链路id列表
     * @author shulie
     */
    void deleteLinkInterfaceByLinkIds(@Param("linkIdLists") List<String> linkIdLists);

    /**
     * 说明: 更新链路信息
     *
     * @param tLinkServiceMntVo 链路服务实体类
     * @author shulie
     */
    void updateLink(TLinkServiceMntVo tLinkServiceMntVo);

    /**
     * 说明: 批量删除业务链路和技术链路关联关系
     *
     * @param linkIdLists 业务链路id集合
     * @author shulie
     * @date 2018/12/28 9:57
     */
    void deleteReLationShipByTLinkId(@Param("linkIdLists") List<String> linkIdLists);

    /**
     * 说明: 更新链路服务信息
     *
     * @param updateServiceMntLists 链路服务实体类列表
     * @author JasonYaFn
     */
    void updateLinkInterface(@Param("updateServiceMntLists") List<TLinkServiceMnt> updateServiceMntLists);

    /**
     * 说明: 根据链路名称查询链路id
     *
     * @param linkName 链路名称
     * @return 链路id
     * @author shulie
     */
    Map<String, Object> selectLinkId(@Param("linkName") String linkName);

    /**
     * 说明: 删除链路服务信息接口
     *
     * @param linkServiceIdsList 链路服务关系表id
     * @author shulie
     */
    void deleteLinkInterfaceByLinkServiceId(@Param("linkServiceIdsList") List<String> linkServiceIdsList);

    List<TLinkServiceMntVo> queryLinksByLinkIds(List<String> ids);

    /**
     * 说明: 删除二级链路关联基础链路关系
     *
     * @param basicLinkIdLists 基础链路ids
     * @author shulie
     * @date 2018/6/18 19:17
     */
    void deleteSecondLinkRelationBasicLinkByBasicLinkIds(@Param("basicLinkIdLists") List<String> basicLinkIdLists);

    /**
     * @param linkId 基础链路id
     * @param
     * @return
     * @description 根据基础链路id删除二级/基础链路关系
     * @author shulie
     * @create 2018/6/28 21:21
     */
    void deleteSecondLinkRelationBasicLinkByBasicLinkId(@Param("basicLinkId") String linkId);

    /**
     * 说明: 根据基础链路id查询关联的二级链路个数和基础链路名称
     *
     * @param
     * @return
     * @author shulie
     * @date 2018/7/6 15:22
     */
    Map<String, Object> querySecondLinkRelationBasicLinkByBasicLinkId(@Param("basicLinkId") String basicLinkId);

    /**
     * 说明：获取链路nodes
     *
     * @param linkId
     * @return
     * @author shulie
     * @date 2019/1/11
     */
    List<TLinkNodesVo> getNodesByBlinkId(@Param("linkId") String linkId);

    /**
     * 通过链路类型获取链路列表
     *
     * @param linkTypeList
     * @return
     */
    List<TLinkBasicVO> queryLinksByLinkType(@Param("linkTypeList") List<Integer> linkTypeList);

    /**
     * 查询链路id与名称
     *
     * @return
     */
    List<Map<String, String>> queryLinkIdName();

    /**
     * 添加二级链路和业务/技术链路的关联关系
     *
     * @param secondLinkId 二级链路Id
     * @param linkId       业务/技术链路Id
     */
    void addSecondLinkRef(@Param("secondLinkId") String secondLinkId, @Param("linkId") String linkId);

    /**
     * 更新二级链路和业务/技术链路的关联关系
     *
     * @param secondLinkId 二级链路Id
     * @param linkId       业务/技术链路Id
     */
    void updateSecondLinkRef(@Param("secondLinkId") String secondLinkId, @Param("linkId") String linkId);

    /**
     * 说明: 删除t_second_basic_ref表中的关系
     *
     * @param linkIds 业务/技术链路ID
     * @return void
     * @author shulie
     * @create 2019/4/9 20:39
     */
    void deleteSecondBasicLinkRef(@Param("linkIds") List<String> linkIds);

    /**
     * 说明: 查询链路头部信息列表
     *
     * @return java.util.List<TLinkMntDictoryVo>
     * @author shulie
     * @create 2019/4/10 10:23
     */
    @Deprecated
    List<TLinkMntDictoryVo> queryLinkHeaderInfo();

    /**
     * 说明: 查询链路头部信息列表
     *
     * @return java.util.List<TLinkMntDictoryVo>
     * @author shulie
     * @create 2019/4/10 10:23
     */
    List<TLinkTopologyInfoVo> queryLinkHeaderInfoList();

    /**
     * 说明: 根据链路模块查询对应的下单/订单计算链路列表
     *
     * @param linkModule 链路模块
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.String>>
     * @author shulie
     * @create 2019/4/10 14:43
     */
    @Deprecated
    List<Map<String, String>> queryCalcVolumeLinkList(@Param("linkModule") String linkModule);

    /**
     * 说明: 根据链路模块查询对应的下单/订单计算链路列表
     *
     * @param linkModule 链路模块
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.String>>
     * @author shulie
     * @create 2019/4/10 14:43
     */
    List<Map<String, String>> queryCalcVolumeLinkListByModule(@Param("linkModule") String linkModule);

    /**
     * 说明: 根据链路模块查询二级链路列表
     *
     * @param linkModule 链路模块
     * @return java.util.List<TSecondLinkMnt>
     * @author shulie
     * @create 2019/4/15 17:59
     */
    List<TSecondLinkMnt> querySecondLinkByModule(@Param("linkModule") String linkModule);

    /**
     * 说明: 根据链路模块查询二级链路列表
     *
     * @param linkModule 链路模块
     * @return java.util.List<TSecondLinkMnt>
     * @author shulie
     * @create 2019/4/15 17:59
     */
    List<TSecondLinkMnt> querySecondLinkMapByModule(@Param("linkModule") String linkModule);

    /**
     * 判断是否存在链路关系
     *
     * @param secondLinkId 二级链路ID
     * @param linkId       链路ID
     * @return 存在返回1, 不存在返回0
     */
    int existSecondLinkRef(@Param("secondLinkId") String secondLinkId, @Param("linkId") String linkId);

    Long queryAppIdByAppName(@Param("linkName") String appName);

    void updateExpectTps(@Param("expectTps") String expectTps, @Param("linkId") long linkId);
}
