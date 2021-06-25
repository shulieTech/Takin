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

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneManageWrapperInput;
import io.shulie.tro.cloud.biz.output.scenemanage.SceneManageWrapperOutput;
import io.shulie.tro.cloud.common.page.PageUtils;
import io.shulie.tro.cloud.open.req.scenemanage.SceneManageWrapperReq;
import io.shulie.tro.cloud.open.resp.scenemanage.SceneManageWrapperResp;
import io.shulie.tro.utils.json.JsonHelper;
import net.sf.cglib.beans.BeanCopier;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

/**
 * @author 无涯
 * @Package PACKAGE_NAME
 * @date 2020/10/28 11:50 下午
 */

public class SceneManageTest {
    @Test
    public void test() {
        String json = "{\"pageSize\":20,\"current\":0,\"license\":null,\"id\":null,\"customId\":1,"
                + "\"pressureTestSceneName\":null,\"businessActivityConfig\":null,\"concurrenceNum\":null,\"ipNum\":null,"
                + "\"pressureTestTime\":null,\"pressureMode\":null,\"increasingTime\":null,\"step\":null,"
                + "\"scriptType\":null,\"uploadFile\":null,\"stopCondition\":null,\"warningCondition\":null,"
                + "\"currentPage\":0,\"offset\":0}\n";
        SceneManageWrapperReq wrapperReq1 = new SceneManageWrapperReq();
        wrapperReq1.setCustomId(1L);
        System.out.println(JsonHelper.bean2Json(wrapperReq1));
        SceneManageWrapperReq wrapperReq = JsonHelper.json2Bean(json, SceneManageWrapperReq.class);
        SceneManageWrapperInput input = new SceneManageWrapperInput();

        BeanUtils.copyProperties(wrapperReq, input);
        System.out.println(JsonHelper.bean2Json(input));
    }

    @Test
    public void tests() {
        String s = "/Users/chengjiacai/test/copyfile/test.txt";
        String substring = s.substring(0, s.lastIndexOf("/"));
        System.out.println(substring);
        new Thread(() -> {
            System.out.println(" thread");

            System.out.println(" thread");
        }).start();
    }
}
