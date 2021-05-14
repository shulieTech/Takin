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

package io.shulie.tro.web.app.output.fastdebug;

import com.pamirs.tro.entity.domain.vo.linkmanage.BusinessLinkVo;
import io.shulie.tro.web.common.vo.fastdebug.ContentTypeVO;
import io.shulie.tro.web.common.vo.fastdebug.FastDebugConfigVO;
import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.output.output
 * @date 2020/12/28 9:44 上午
 */
@Data
public class FastDebugConfigDetailOutput extends FastDebugConfigVO {
    private Long id;
    /**
     * 更新时间
     */
    private String gmtModified;

    /**
     * 更新人
     */
    private String modifierName;

    /**
     * contentType数据
     */
    private ContentTypeVO contentTypeVo;

    /**
     * 状态
     */
    private String status;

    /**
     * 业务活动
     */
    private BusinessLinkVo businessLinkVo;

}
