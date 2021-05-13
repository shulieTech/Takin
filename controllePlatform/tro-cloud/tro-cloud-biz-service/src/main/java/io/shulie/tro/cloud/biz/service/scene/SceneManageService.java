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

package io.shulie.tro.cloud.biz.service.scene;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.pamirs.tro.entity.domain.vo.scenemanage.SceneManageStartRecordVO;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneManageQueryInput;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneManageWrapperInput;
import io.shulie.tro.cloud.biz.output.scenemanage.SceneManageListOutput;
import io.shulie.tro.cloud.biz.output.scenemanage.SceneManageWrapperOutput;
import io.shulie.tro.cloud.biz.output.scenemanage.SceneManageWrapperOutput.SceneBusinessActivityRefOutput;
import io.shulie.tro.cloud.common.bean.scenemanage.SceneManageQueryOpitons;
import io.shulie.tro.cloud.common.bean.scenemanage.UpdateStatusBean;
import io.shulie.tro.cloud.common.request.scenemanage.UpdateSceneFileRequest;


/**
 * @ClassName SceneManage
 * @Description
 * @Author qianshui
 * @Date 2020/4/17 下午3:31
 */
public interface SceneManageService {

    Long addSceneManage(SceneManageWrapperInput wrapperVO);

    PageInfo<SceneManageListOutput> queryPageList(SceneManageQueryInput queryVO);

    void updateSceneManage(SceneManageWrapperInput wrapperVO);

    void updateSceneManageStatus(UpdateStatusBean statusVO);

    void delete(Long id);

    SceneManageWrapperOutput getSceneManage(Long id, SceneManageQueryOpitons options);

    /**
     * 根据场景ID获取业务活动配置
     *
     * @param sceneId
     * @return
     */
    List<SceneBusinessActivityRefOutput> getBusinessActivityBySceneId(Long sceneId);

    BigDecimal calcEstimateFlow(SceneManageWrapperInput wrapperVO);

    Map<String, Object> parseScript(Long scriptId, String scriptPath, boolean absolutePath);

    Map<String, Object> parseAndUpdateScript(Long scriptId, String scriptPath, boolean absolutePath);

    /**
     * 获取压测场景目标路径,当前以/结尾
     * @param sceneId
     * @return
     */
    String getDestPath (Long sceneId);

    /**
     * 严格更新 压测场景生命周期
     *
     * @param statusVO
     */
    Boolean updateSceneLifeCycle(UpdateStatusBean statusVO);

    /**
     * 记录场景启动过程  比如job 是否创建成功，pod 是否创建成功，
     *
     * @param recordVO
     */
    void reportRecord(SceneManageStartRecordVO recordVO);

    /**
     * 不分页查询所有场景信息，带脚本信息
     *
     * @return
     */
    List<SceneManageListOutput> querySceneManageList();

    /**
     * 压测场景-指定责任人
     *
     * @return
     */
    int allocationUser(Long dataId, Long userId);

    /**
     * 更新 脚本id 关联的场景下的文件
     *
     * @param request 请求所需的参数
     */
    void updateFileByScriptId(UpdateSceneFileRequest request);
    
    List<SceneManageWrapperOutput> getByIds(List<Long> sceneIds);

}
