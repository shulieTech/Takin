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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.pamirs.tro.common.constant.Constants;
import com.pamirs.tro.common.util.PageInfo;
import com.pamirs.tro.entity.domain.entity.TApplicationInfoUpload;
import com.pamirs.tro.entity.domain.vo.TUploadInterfaceDetailVo;
import com.pamirs.tro.entity.domain.vo.TUploadInterfaceVo;
import io.shulie.tro.web.app.common.CommonService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 应用上传信息 接口
 */
@Service
public class ApplicationInfoUploadService extends CommonService {

    public void saveApplicationInfoUpload(TUploadInterfaceVo vo) {
        //如果没有 直接 return
        if (StringUtils.isEmpty(vo.getAppName()) || CollectionUtils.isEmpty(vo.getAppDetails())) {
            return;
        }
        List<TApplicationInfoUpload> infoList = new ArrayList<>();
        for (TUploadInterfaceDetailVo detail : vo.getAppDetails()) {
            //如果没有值 也不需要保存
            if (StringUtils.isEmpty(detail.getInterfaceName())) {
                continue;
            }
            //不是堆栈 与 SQL 异常 也不需要保存
            if (Constants.APPLICATION_INFO_TYPE_SQL_ERROR_STRING.equalsIgnoreCase(detail.getType())
                || Constants.APPLICATION_INFO_TYPE_TRACE_STRING.equalsIgnoreCase(detail.getType())) {
                TApplicationInfoUpload infoUpload = new TApplicationInfoUpload();
                infoUpload.setTaiuId(snowflake.next());
                infoUpload.setApplicationName(vo.getAppName());
                if (Constants.APPLICATION_INFO_TYPE_SQL_ERROR_STRING.equalsIgnoreCase(detail.getType())) {
                    infoUpload.setInfoType(Constants.APPLICATION_INFO_TYPE_SQL_ERROR);
                }
                if (Constants.APPLICATION_INFO_TYPE_TRACE_STRING.equalsIgnoreCase(detail.getType())) {
                    infoUpload.setInfoType(Constants.APPLICATION_INFO_TYPE_TRACE);
                }
                if (detail.getInterfaceName().length() > 2000) {
                    infoUpload.setUploadInfo(detail.getInterfaceName().substring(0, 1999));
                } else {
                    infoUpload.setUploadInfo(detail.getInterfaceName());
                }
                infoList.add(infoUpload);
            }
        }
        //list 为空 也不需要保存了
        if (CollectionUtils.isNotEmpty(infoList)) {
            tApplicationInfoUploadDao.insertList(infoList);
        }
    }

    /**
     * 分页查询
     *
     * @param paramMap
     * @return
     */
    public PageInfo<TApplicationInfoUpload> queryApplicationPage(Map<String, Object> paramMap) {
        PageHelper.startPage(PageInfo.getPageNum(paramMap), PageInfo.getPageSize(paramMap));
        List<TApplicationInfoUpload> voList = tApplicationInfoUploadDao.queryUploadInfoPage(paramMap);
        return new PageInfo<>(CollectionUtils.isEmpty(voList) ? Lists.newArrayList() : voList);
    }

}
