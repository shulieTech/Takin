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

package io.shulie.tro.web.app;

import com.pamirs.tro.common.util.SpringContextUtil;
import io.shulie.tro.web.app.license.TroClientValidator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author shulie
 * @package: com.pamirs
 * @Date 2019-07-23 18:30
 */
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@MapperScan({"com.pamirs.tro.*.dao", "io.shulie.tro.web.data.mapper.mysql","io.shulie.tro.web.data.dao.statistics","io.shulie.tro.web.data.convert.*"})
@EnableAspectJAutoProxy
@SpringBootApplication
@ComponentScan(basePackages = {"com.pamirs.tro", "io.shulie.tro"})
public class Application {

    public static void main(String[] args) {
        // 设置tro-cloud访问权限
        TroClientValidator.validate("5b06060a-17cb-4588-bb71-edd7f65035af");
        ApplicationContext applicationContext = new SpringApplicationBuilder().sources(Application.class).run(args);
        SpringContextUtil.setApplicationContext(applicationContext);
    }
}
