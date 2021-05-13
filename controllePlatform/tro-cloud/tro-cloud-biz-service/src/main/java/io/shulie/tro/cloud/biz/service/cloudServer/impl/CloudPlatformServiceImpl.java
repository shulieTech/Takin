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

package io.shulie.tro.cloud.biz.service.cloudServer.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pamirs.tro.entity.dao.cloudserver.TCloudPlatformMapper;
import com.pamirs.tro.entity.domain.entity.cloudserver.CloudPlatform;
import com.pamirs.tro.entity.domain.query.CloudPlatformQueryParam;
import com.pamirs.tro.entity.domain.vo.cloudserver.CloudPlatformVO;
import io.shulie.tro.cloud.biz.cloudserver.CloudPlatformEntityToDTOConvert;
import io.shulie.tro.cloud.biz.service.cloudServer.CloudPlatformService;
import io.shulie.tro.cloud.common.page.PageInfo;
import io.shulie.tro.cloud.common.utils.FileUtils;
import io.shulie.tro.common.beans.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: mubai
 * @Date: 2020-05-11 11:01
 * @Description:
 */

@Slf4j
@Service
public class CloudPlatformServiceImpl implements CloudPlatformService {
    public static String FALSE_CORE = "0";
    @Value("${pradar_cloud_sdk_path}")
    private String sdk_path;

    //@Autowired
    //private CloudSdkLoader cloudSdkLoader;
    @Resource
    private TCloudPlatformMapper TCloudPlatformMapper;

    @Override
    public ResponseResult addCloudPlatform(CloudPlatform platform) {
        try {
            TCloudPlatformMapper.insertSelective(platform);
            //cloudSdkLoader.init();
        } catch (Exception e) {
            return ResponseResult.fail(FALSE_CORE, "添加云平台失败", "");
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult deleteById(Long id) {
        try {
            CloudPlatform platform = TCloudPlatformMapper.selectByPrimaryKey(id);
            if (platform != null && StringUtils.isNotBlank(platform.getJarName())) {
                FileUtils.deleteDir(new File(sdk_path + "/" + platform.getJarName()));
            }
            TCloudPlatformMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            return ResponseResult.fail(FALSE_CORE, "删除云平台失败", "");
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult updateById(CloudPlatform platform) {
        try {
            if (platform == null || platform.getId() == null) {
                return ResponseResult.fail(FALSE_CORE, "平台id不能为空", "");
            }
            TCloudPlatformMapper.updateByPrimaryKeySelective(platform);
            //  loadCloudSDK.init();
        } catch (Exception e) {
            return ResponseResult.fail(FALSE_CORE, "添加云平台失败", "");
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult queryById(Long id) {
        CloudPlatform cloudPlatform = TCloudPlatformMapper.selectByPrimaryKey(id);
        if (cloudPlatform != null) {
            CloudPlatformVO of = CloudPlatformEntityToDTOConvert.INSTANCE.of(cloudPlatform);
            generateAuthTemplate(of);
            return ResponseResult.success(of);
        }
        return ResponseResult.fail(FALSE_CORE, "平台不存在", "");
    }

    @Override
    public ResponseResult queryByExample() {
        return null;
    }

    @Override
    public ResponseResult uploadSDK(MultipartFile file, String fileName) {
        File targetFile = new File(sdk_path + "/" + file.getOriginalFilename());
        File parentFile = targetFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return ResponseResult.fail(e.getMessage(), "");
        }
        CloudPlatformVO cloudPlatformVO = new CloudPlatformVO();
        cloudPlatformVO.setJarName(file.getOriginalFilename());
        cloudPlatformVO.setName(fileName);
        return ResponseResult.success(cloudPlatformVO);
    }

    @Override
    public ResponseResult editSDK(String name, String jarName, Long id) {
        if (jarName == null) {
            return ResponseResult.fail(FALSE_CORE, "jarName can not be null", "");
        }
        try {
            CloudPlatform platform = new CloudPlatform();
            platform.setJarName(jarName);
            platform.setName(name);
            platform.setId(id);
            this.confirmCloudPlatform(platform);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseResult.fail(FALSE_CORE, "编辑失败", "");
        }
        return ResponseResult.success(true);
    }

    @Override
    public ResponseResult confirmCloudPlatform(CloudPlatform param) {
        try {
            String filePath = "file:" + sdk_path + "/" + param.getJarName();
            //cloudSdkLoader.loadJar(param.getId(), param.getName(), param.getJarName(), filePath, true);
        } catch (Exception e) {
            return ResponseResult.fail(FALSE_CORE, e.getMessage(), "");
        }
        return ResponseResult.success();
    }

    @Override
    public PageInfo<CloudPlatformVO> getPageList(CloudPlatformQueryParam param) {
        Page<Object> page = PageHelper.startPage(param.getCurrentPage() + 1, param.getPageSize());
        List<CloudPlatform> cloudPlatforms = TCloudPlatformMapper.selectByExample(param);
        if (cloudPlatforms.isEmpty()) {
            return new PageInfo<>(new ArrayList<>());
        }
        List<CloudPlatformVO> platformVOS = CloudPlatformEntityToDTOConvert.INSTANCE.ofs(cloudPlatforms);
        if (platformVOS != null) {
            for (int i = 0; i < platformVOS.size(); i++) {
                CloudPlatformVO cloudPlatformVO = platformVOS.get(i);
                generateAuthTemplate(cloudPlatformVO);
            }
        }
        PageInfo pageInfo = new PageInfo(platformVOS);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    public void generateAuthTemplate(CloudPlatformVO of) {
        if (of != null && StringUtils.isNotBlank(of.getAuthorizeParam())) {
            String authorizeParam = of.getAuthorizeParam();
            Set<String> set = JSON.parseObject(authorizeParam, Set.class);
            Map<String, Object> map = new HashMap<>();
            set.stream().forEach(s -> {
                map.put(s, s + UUID.randomUUID().toString());
            });
            of.setAuthorizeTemplate(JSON.toJSONString(map));
        }
    }

}
