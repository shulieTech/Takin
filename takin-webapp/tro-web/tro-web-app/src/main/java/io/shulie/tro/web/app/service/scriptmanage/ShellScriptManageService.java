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

package io.shulie.tro.web.app.service.scriptmanage;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.input.scriptmanage.ShellExecuteInput;
import io.shulie.tro.web.app.input.scriptmanage.ShellScriptManageCreateInput;
import io.shulie.tro.web.app.input.scriptmanage.ShellScriptManagePageQueryInput;
import io.shulie.tro.web.app.input.scriptmanage.ShellScriptManageUpdateInput;
import io.shulie.tro.web.app.output.scriptmanage.shell.ScriptExecuteOutput;
import io.shulie.tro.web.app.output.scriptmanage.shell.ShellScriptManageContentOutput;
import io.shulie.tro.web.app.output.scriptmanage.shell.ShellScriptManageDetailOutput;
import io.shulie.tro.web.app.output.scriptmanage.shell.ShellScriptManageExecuteOutput;
import io.shulie.tro.web.app.output.scriptmanage.shell.ShellScriptManageOutput;

/**
* @Package io.shulie.tro.web.app.service.scriptmanage
* @author 无涯
* @description:
* @date 2020/12/8 4:23 下午
*/
public interface ShellScriptManageService {

    /**
     * 创建shell脚本
     * @param input
     * @return
     */
    Long createScriptManage(ShellScriptManageCreateInput input);

    /**
     * 修改shell脚本
     * @param input
     */
    String updateScriptManage(ShellScriptManageUpdateInput input);

    /**
     * 删除shell脚本
     * @param scriptId
     */
    void deleteScriptManage(Long scriptId);

    /**
     * 查询脚本实例详情
     * @param scriptId
     * @return
     */
    ShellScriptManageDetailOutput getScriptManageDetail(Long scriptId);

    /**
     * 分页查询脚本列表
     * @param input
     * @return
     */
    PagingList<ShellScriptManageOutput> pageQueryScriptManage(ShellScriptManagePageQueryInput input);

    /**
     * 执行脚本，传实例id
     * @param scriptManageDeployId
     * @return
     */
    ShellScriptManageExecuteOutput execute(Long scriptManageDeployId);



    /**
     * 根据版本获取脚本内容
     * @param scriptId
     * @param version
     * @return
     */
    ShellScriptManageContentOutput getShellScriptManageContent(Long scriptId,Integer version);


    /**
     * 获取执行记录
     * @param input
     * @return
     */
    PagingList<ScriptExecuteOutput> getExecuteResult(ShellExecuteInput input);


}
