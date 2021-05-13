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

import java.util.List;

import com.google.common.collect.Lists;
import com.pamirs.tro.entity.domain.entity.TLinkServiceMnt;
import com.pamirs.tro.entity.domain.vo.TLinkServiceMntVo;

/**
 * @author 何仲奇
 * @Package com.pamirs.tro.web.api.service
 * @date 2020/9/2 9:19 下午
 * @see ConfCenterService
 */
public class ConfCenterServiceTest {

    public static void main(String[] args) {
        TLinkServiceMntVo tLinkServiceMntVo = new TLinkServiceMntVo();
        tLinkServiceMntVo.setAswanId("000");
        tLinkServiceMntVo.setLinkId(33333333);
        tLinkServiceMntVo.setLinkName("hjkskskksk");
        tLinkServiceMntVo.setRt("99");
        List<TLinkServiceMnt> list = Lists.newArrayList();
        TLinkServiceMnt tLinkServiceMnt = new TLinkServiceMnt();
        tLinkServiceMnt.setInterfaceDesc("sjskslsl");
        tLinkServiceMnt.setLinkServiceId(9988L);
        list.add(tLinkServiceMnt);
        tLinkServiceMntVo.settLinkServiceMntList(list);

        //        String s = new Gson().toJson(tLinkServiceMntVo);
        //        System.out.println(s);

        System.out.println(org.apache.commons.lang3.builder.ToStringBuilder
            .reflectionToString(tLinkServiceMntVo, org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE));

    }

}
