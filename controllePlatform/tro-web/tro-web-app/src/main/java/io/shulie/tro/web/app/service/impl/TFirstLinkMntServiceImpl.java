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

package io.shulie.tro.web.app.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.common.util.PageInfo;
import com.pamirs.tro.entity.domain.entity.TFirstLinkMnt;
import com.pamirs.tro.entity.domain.entity.TSecondBasic;
import com.pamirs.tro.entity.domain.entity.TSecondLinkMnt;
import com.pamirs.tro.entity.domain.vo.TLinkServiceMntVo;
import io.shulie.tro.web.app.common.CommonService;
import io.shulie.tro.web.app.service.TFirstLinkMntService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shulie
 * @description 二级链路业务实现类
 * @create 2018/6/20 14:49
 */
@Service
@SuppressWarnings("all")
public class TFirstLinkMntServiceImpl extends CommonService implements TFirstLinkMntService {

    private static final String SECOND_LINKS = "SECOND_LINKS";
    private static final String LINK_TPS_RULE = "LINK_TPS_RULE";

    @Override
    public void saveLink(TFirstLinkMnt firstLinkMnt) throws TROModuleException {
        String firstLinkName = firstLinkMnt.getLinkName();
        int linkExist = firstLinkMntDao.saveLinkExist(firstLinkName);
        if (linkExist > 0) {
            throw new TROModuleException(TROErrorEnum.CONFCENTER_FIRST_LINK_DUPICATE_EXCEPTION);
        } else {
            firstLinkMnt.setLinkId(String.valueOf(snowflake.next()));
            firstLinkMntDao.addLink(firstLinkMnt);

        }
    }

    @Override
    public PageInfo<TFirstLinkMnt> queryLinkList(String firstLinkName, String secondLinkName,
        Integer pageNum, Integer pageSize) throws TROModuleException {
        if (pageSize == null || pageNum == null) {
            throw new TROModuleException(TROErrorEnum.PAGE_PARAM_EXCEPTION);
        }
        //pageSize!=-1,则分页查询，pageSize=-1，查询所有
        if (pageSize != -1) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<TFirstLinkMnt> firstLinkList = new ArrayList<TFirstLinkMnt>();
        if (StringUtils.isEmpty(secondLinkName)) {
            //如果只根据firstLinkName查找就用原始表
            firstLinkList = firstLinkMntDao.queryLinkList(firstLinkName);
        } else {
            //如果要根据secondLinkName联合查找就用视图表
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("firstLinkName", firstLinkName);
            paramMap.put("secondLinkName", secondLinkName);
            firstLinkList = firstLinkMntDao.queryLinkListByView(paramMap);
        }

        for (TFirstLinkMnt firstLink : firstLinkList) {
            // 将二级链路id转换成二级链路名称
            String secondLinks = firstLink.getSecondLinks();
            String secondNames = transSecondLinkIdToName(firstLink, secondLinks, SECOND_LINKS);
            firstLink.setSecondLinks(secondNames);
            String linkTpsRule = firstLink.getLinkTpsRule();
            String linkTpsRuleName = transSecondLinkIdToName(firstLink, linkTpsRule, LINK_TPS_RULE);
            firstLink.setLinkTpsRule(linkTpsRuleName);
        }

        return new PageInfo<TFirstLinkMnt>(firstLinkList);
    }

    /**
     * 将链路id转换成链路名称
     *
     * @param linkIds
     * @return
     * @throws Exception
     */
    private String transSecondLinkIdToName(TFirstLinkMnt firstLink, String linkIds, String updateFiled)
        throws TROModuleException {
        if (linkIds == null) {
            throw new TROModuleException(TROErrorEnum.CONFCENTER_SECOND_LINKID_LIST_IS_NULL_EXCEPTION);
        }
        String[] linkIdArr = linkIds.split(",");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < linkIdArr.length; i++) {
            String linkId = linkIdArr[i];
            TSecondLinkMnt secondLinkMnt = secondLinkDao.queryLinkByLinkId(linkId);
            if (secondLinkMnt != null) {
                sb.append(secondLinkMnt.getLinkName());
                sb.append(",");
            }
        }
        if (sb.length() == 0) {
            return "";
        } else {
            return sb.substring(0, sb.length() - 1).toString();
        }
    }

    @Override
    public TFirstLinkMnt queryLinkByLinkId(String linkId) throws TROModuleException {
        TFirstLinkMnt firstLinkMnt = firstLinkMntDao.queryLinkByLinkId(linkId);
        return firstLinkMnt;
    }

    @Override
    public Map<String, Object> queryLinkMapByLinkId(String linkId) throws TROModuleException {
        TFirstLinkMnt firstLinkMnt = firstLinkMntDao.queryLinkByLinkId(linkId);
        //返回信息中要包含二级链路的id和名称
        Map<String, Object> firstLinkMap = new HashMap<String, Object>();
        if (firstLinkMnt != null) {
            List<Map<String, Object>> secondLinkList = getLinkDetailByIds(firstLinkMnt.getSecondLinks());
            List<Map<String, Object>> linkTpsRuleList = getLinkDetailByIds(firstLinkMnt.getLinkTpsRule());
            firstLinkMap.put("linkId", firstLinkMnt.getLinkId());
            firstLinkMap.put("linkName", firstLinkMnt.getLinkName());
            firstLinkMap.put("linkTps", firstLinkMnt.getLinkTps());
            firstLinkMap.put("linkTpsRule", linkTpsRuleList);
            firstLinkMap.put("createTime", firstLinkMnt.getCreateTime());
            firstLinkMap.put("remark", firstLinkMnt.getRemark());
            firstLinkMap.put("secondLinks", secondLinkList);
            firstLinkMap.put("updateTime", firstLinkMnt.getUpdateTime());
            firstLinkMap.put("useYn", firstLinkMnt.getUseYn());
        }

        return firstLinkMap;
    }

    /**
     * 根据链路ids获取链路详情[{label:"",value:""}]
     *
     * @param linkIds 链路id串
     * @return
     * @throws Exception
     */
    private List<Map<String, Object>> getLinkDetailByIds(String linkIds) {
        String[] linkIdArr = linkIds.split(",");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < linkIdArr.length; i++) {
            String linkId = linkIdArr[i];
            //根据id查询出二级链路信息
            TSecondLinkMnt secondLinkMnt = secondLinkDao.queryLinkByLinkId(linkId);
            if (secondLinkMnt != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                String linkName = secondLinkMnt.getLinkName();
                map.put("label", linkName);
                map.put("value", linkId);
                list.add(map);
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLinkByLinkIds(String linkIds) throws TROModuleException {
        List<String> linkIdLists = Arrays.stream(linkIds.split(",")).filter(e -> StringUtils.isNotEmpty(e)).distinct()
            .collect(Collectors.toList());
        firstLinkMntDao.deleteLinkByLinkIds(linkIdLists);
        List<TFirstLinkMnt> tFirstLinkMnts = firstLinkMntDao.queryFirstLinkByIds(linkIdLists);

    }

    @Override
    public void updateLinkinfo(TFirstLinkMnt firstLinkMnt) throws TROModuleException {
        String linkId = firstLinkMnt.getLinkId();
        //        int linkExist = firstLinkMntDao.updateLinkExist(linkId);
        TFirstLinkMnt originFirstLinkMnt = firstLinkMntDao.queryLinkByLinkId(linkId);
        if (!Objects.isNull(originFirstLinkMnt)) {
            firstLinkMntDao.updateLink(firstLinkMnt);

        } else {
            throw new TROModuleException(TROErrorEnum.CONFCENTER_NOT_FOUND_FIRST_LINKID_EXCEPTION);
        }
    }

    @Override
    public Map<String, Object> getLinkTopologyByFirstLinkId(String linkId) throws TROModuleException {
        //查询拓扑图
        //1，查询出一级链路
        //2，根据secondLinks查询二级链路列表
        //3，根据baseLinks查询基础链路列表
        TFirstLinkMnt firstLinkMnt = firstLinkMntDao.queryLinkByLinkId(linkId);
        if (firstLinkMnt != null) {
            String secondLinks = firstLinkMnt.getSecondLinks();
            String[] secondLinkIds = secondLinks.split(",");
            Map<String, Object> firstLinkMap = new HashMap<String, Object>();
            firstLinkMap.put("firstLinkId", firstLinkMnt.getLinkId());
            firstLinkMap.put("firstLinkName", firstLinkMnt.getLinkName());
            firstLinkMap.put("firstLinkTps", firstLinkMnt.getLinkTps());
            firstLinkMap.put("firstLinkTpsRule", firstLinkMnt.getLinkTpsRule());
            firstLinkMap.put("createTime", firstLinkMnt.getCreateTime());
            firstLinkMap.put("remark", firstLinkMnt.getRemark());
            firstLinkMap.put("secondLinks", firstLinkMnt.getSecondLinks());
            firstLinkMap.put("updateTime", firstLinkMnt.getUpdateTime());
            firstLinkMap.put("firstUseYn", firstLinkMnt.getUseYn());

            List<Map<String, Object>> secondLinkMntList = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < secondLinkIds.length; i++) {
                String secondLinkId = secondLinkIds[i];
                TSecondLinkMnt secondLinkMnt = secondLinkDao.queryLinkByLinkId(secondLinkId);
                if (secondLinkMnt != null) {
                    Map<String, Object> secondLinkMap = new HashMap<String, Object>();
                    //FIXME 修改
                    //					String baseLinks = secondLinkMnt.getBaseLinks();
                    //					String [] baseLinkIds = baseLinks.split(",");
                    secondLinkMap.put("secondLinkId", secondLinkMnt.getLinkId());
                    secondLinkMap.put("secondLinkName", secondLinkMnt.getLinkName());
                    secondLinkMap.put("secondAswanId", secondLinkMnt.getAswanId());
                    secondLinkMap.put("baseLinks", secondLinkMnt.getBaseLinks());
                    secondLinkMap.put("createTime", secondLinkMnt.getCreateTime());
                    secondLinkMap.put("secondLinkTps", secondLinkMnt.getLinkTps());
                    secondLinkMap.put("secondLinkTpsRule", secondLinkMnt.getLinkTpsRule());
                    secondLinkMap.put("remark", secondLinkMnt.getRemark());
                    secondLinkMap.put("updateTime", secondLinkMnt.getUpdateTime());
                    secondLinkMap.put("secondUseYn", secondLinkMnt.getUseYn());
                    List<Object> basicLinkList = getBasicLinkBySecondLinkId(secondLinkId);
                    //					List<Map<String,Object>> baseLinkList  = new ArrayList<Map<String,Object>>();
                    //					for(int j=0; j< baseLinkIds.length;j++){
                    //						String baseLinkId = baseLinkIds[j];
                    //						TLinkServiceMntVo linkServiceMntVo = tLinkMnDao.queryLinkByLinkId
                    //						(baseLinkId);
                    //						if(linkServiceMntVo!=null){
                    //							Map<String,Object> baseLinkMap = new HashMap<String,Object>();
                    //							baseLinkMap.put("baseLinkId", linkServiceMntVo.getLinkId()+"");
                    //							baseLinkMap.put("baseLinkName", linkServiceMntVo.getLinkName());
                    //							baseLinkMap.put("baseAswanId", linkServiceMntVo.getAswanId());
                    //							baseLinkMap.put("baseLinkDesc", linkServiceMntVo.getLinkDesc());
                    //							baseLinkMap.put("createTime", linkServiceMntVo.getCreateTime());
                    //							baseLinkMap.put("dictType", linkServiceMntVo.getDictType());
                    //							baseLinkMap.put("linkEntrence", linkServiceMntVo.getLinkEntrence());
                    //							baseLinkMap.put("linkRank", linkServiceMntVo.getLinkRank());
                    //							baseLinkMap.put("principalNo", linkServiceMntVo.getPrincipalNo());
                    //							baseLinkMap.put("rt", linkServiceMntVo.getRt());
                    //							baseLinkMap.put("rtSa", linkServiceMntVo.getRtSa());
                    //							baseLinkMap.put("targetSuccessRate", linkServiceMntVo
                    //							.getTargetSuccessRate());
                    //							baseLinkMap.put("linkServiceMntList", linkServiceMntVo
                    //							.gettLinkServiceMntList());
                    //							baseLinkMap.put("tps", linkServiceMntVo.getTps());
                    //							baseLinkMap.put("updateTime", linkServiceMntVo.getUpdateTime());
                    //							baseLinkMap.put("useYn", linkServiceMntVo.getUseYn());
                    //							baseLinkList.add(baseLinkMap);
                    //						}
                    //					}
                    secondLinkMap.put("baseLinkList", basicLinkList);
                    secondLinkMntList.add(secondLinkMap);
                }
            }
            firstLinkMap.put("secondLinkList", secondLinkMntList);

            return firstLinkMap;
        } else {
            return Collections.EMPTY_MAP;
        }
    }

    /**
     * @param linkId 二级链路id
     * @return java.util.List<java.util.List
        * <
        *
        *
        *
        *
        *
        *
        *
        *
        * java.util.Map
        *
        *
        *
        *
        *
        *
        *
        *
        * <
        *
        *
        *
        *
        *
        *
        *
        *
        * java.lang.String
        *
        *
        *
        *
        *
        *
        *
        *
        *,
        *
        *
        *
        *
        *
        *
        *
        *
        * java.lang.Object>>>
     * @description 获取二级链路对应的基础链路信息
     * @author shulie
     * @create 2018/6/20 10:07
     */
    private List<Object> getBasicLinkBySecondLinkId(String linkId) {
        List<Object> baseLinkList = new ArrayList<>();
        //1，根据二级链路找到基础链路列表
        List<TSecondBasic> tSecondBasics = secondBasicDao.querySecondBasicLinkBySecondLinkId(linkId);
        if (tSecondBasics != null && tSecondBasics.size() > 0) {
            List<Integer> blinkBankList = getBlinkBank(tSecondBasics);
            //2，遍历每个级别
            for (Integer linkBank : blinkBankList) {
                List<TLinkServiceMntVo> baseLinkBankList = new ArrayList<>();
                for (TSecondBasic secondBasic : tSecondBasics) {
                    int blinkBank = secondBasic.getBlinkBank();
                    //遍历二级/基础链路关联列表，找到相同的级别
                    if (blinkBank == linkBank) {
                        long basicLinkId = secondBasic.getBasicLinkId();
                        TLinkServiceMntVo tLinkServiceMntVo = tLinkMnDao.queryLinkByLinkId(String.valueOf(basicLinkId));
                        if (tLinkServiceMntVo != null) {
                            Map<String, Object> map = new HashMap<>(5);
                            String linkName = tLinkServiceMntVo.getLinkName();
                            map.put("label", linkName);
                            map.put("value", tLinkServiceMntVo);
                            baseLinkBankList.add(tLinkServiceMntVo);
                        }
                    }
                }
                baseLinkList.add(baseLinkBankList);
            }
        }
        return baseLinkList;
    }

    /**
     * @param tSecondBasics
     * @return java.util.List<java.lang.Integer>
     * @description 获取基础链路的级别
     * @author shulie
     * @create 2018/6/20 10:03
     */
    private List<Integer> getBlinkBank(List<TSecondBasic> tSecondBasics) {
        List<Integer> blinkBankList = new ArrayList<>();
        for (TSecondBasic secondBasic : tSecondBasics) {
            //FIXME 这里如何拼接成json格式的数据传到前台"[[{name:,id:},{}],[]]"(已添加)
            //1,先取出总共有多少个级别
            int blinkBank = secondBasic.getBlinkBank();
            if (!blinkBankList.contains(blinkBank)) {
                blinkBankList.add(blinkBank);
            }
        }
        return blinkBankList;
    }

    @Override
    public List<TFirstLinkMnt> queryLinkBySecondLinkId(String secondLinkId) {
        List<TFirstLinkMnt> tFirstLinkMnts = firstLinkMntDao.queryLinkBySecondLinkId(secondLinkId);

        return tFirstLinkMnts;
    }

	/*@Override
	public Map<String, Object> querySecondLinkMapByLinkId(String linkId) {
		TSecondLinkMnt secondLinkMnt = secondLinkMntDao.queryLinkByLinkId(linkId);
		Map<String, Object> secondLinkMap = new HashMap<>(15);
		if (secondLinkMnt != null) {
			try {
				List<List<Map<String, Object>>> baseLinkList = getBasicLinkBySecondLinkId(linkId);
				//2，遍历，查询出
				//FIXME 这里基础链路需要替换
				List<Map<String, Object>> linkTpsRuleList = getLinkDetailByIds(secondLinkMnt.getLinkTpsRule());
				secondLinkMap.put("linkId", secondLinkMnt.getLinkId());
				secondLinkMap.put("linkName", secondLinkMnt.getLinkName());
				secondLinkMap.put("aswanId", secondLinkMnt.getAswanId());
				secondLinkMap.put("baseLinks", baseLinkList);
				secondLinkMap.put("createTime", secondLinkMnt.getCreateTime());
				secondLinkMap.put("linkTps", secondLinkMnt.getLinkTps());
				secondLinkMap.put("linkTpsRule", linkTpsRuleList);
				secondLinkMap.put("remark", secondLinkMnt.getRemark());
				secondLinkMap.put("updateTime", secondLinkMnt.getUpdateTime());
				secondLinkMap.put("useYn", secondLinkMnt.getUseYn());
				secondLinkMap.put("testStatus", secondLinkMnt.getTestStatus());
			} catch (IOException e) {
				logger.error("基础链路解析异常", e);
				e.printStackTrace();
			}
		}
		return secondLinkMap;
	}

	*//**
     * @description 获取二级链路对应的基础链路信息
     * @author shulie
     * @create 2018/6/20 10:07
     * @param linkId 二级链路id
     * @return java.util.List<java.util.List < java.util.Map < java.lang.String, java.lang.Object>>>
     *//*
	private List<List<Map<String, Object>>> getBasicLinkBySecondLinkId(String linkId) {
		List<List<Map<String, Object>>> baseLinkList = new ArrayList<>();
		//1，根据二级链路找到基础链路列表
		List<TSecondBasic> tSecondBasics = secondBasicDao.querySecondBasicLinkBySecondLinkId(linkId);
		if (tSecondBasics != null && tSecondBasics.size() > 0) {
			List<Integer> blinkBankList = getBlinkBank(tSecondBasics);
			//2，遍历每个级别
			for(Integer linkBank : blinkBankList){
				List<Map<String, Object>> baseLinkBankList = new ArrayList<>();
				for (TSecondBasic secondBasic : tSecondBasics) {
					int blinkBank = secondBasic.getBlinkBank();
					//遍历二级/基础链路关联列表，找到相同的级别
					if(blinkBank==linkBank){
						long basicLinkId = secondBasic.getBasicLinkId();
						TLinkServiceMntVo tLinkServiceMntVo = tLinkMnDao.queryLinkByLinkId(String.valueOf
						(basicLinkId));
						if (tLinkServiceMntVo != null) {
							Map<String, Object> map = new HashMap<>(5);
							String linkName = tLinkServiceMntVo.getLinkName();
							map.put("label", linkName);
							map.put("value", basicLinkId);
							baseLinkBankList.add(map);
						}
					}
				}
				baseLinkList.add(baseLinkBankList);
			}
		}
		return baseLinkList;
	}
	*//**
     * @description 获取基础链路的级别
     * @author shulie
     * @create 2018/6/20 10:03
     * @param tSecondBasics
     * @return java.util.List<java.lang.Integer>
     *//*
	private List<Integer> getBlinkBank(List<TSecondBasic> tSecondBasics) {
		List<Integer> blinkBankList = new ArrayList<>();
		for (TSecondBasic secondBasic : tSecondBasics) {
			//FIXME 这里如何拼接成json格式的数据传到前台"[[{name:,id:},{}],[]]"(已添加)
			//1,先取出总共有多少个级别
			int blinkBank = secondBasic.getBlinkBank();
			if (!blinkBankList.contains(blinkBank)) {
				blinkBankList.add(blinkBank);
			}
		}
		return blinkBankList;
	}*/

}
