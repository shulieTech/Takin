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

import io.shulie.tro.web.app.Application;
import io.shulie.tro.web.app.request.application.ApplicationDsCreateRequest;
import io.shulie.tro.web.app.service.dsManage.DsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author loseself
 * @date 2021/3/29 4:34 下午
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DsServiceTest {

    @Autowired
    private DsService dsService;

    @Test
    public void testAdd() {
        this.addOld();
        this.addNew();
    }

    private void addNew() {
        ApplicationDsCreateRequest request = new ApplicationDsCreateRequest();
        request.setApplicationId(6780372349628715008L);
        request.setDbType(0);
        request.setDsType(0);
        request.setUrl("jdbc:mysql://114.55.42.181:3306/test_liuchuan_new");
        request.setShadowDbUrl("jdbc:mysql://114.55.42.181:3306/pt_test_liuchuan_new");
        request.setShadowDbMaxActive("100");
        request.setShadowDbMinIdle("10");
        request.setShadowDbUserName("liuchuan");
        request.setShadowDbPassword("test");

        dsService.dsAdd(request);
    }

    private void addOld() {
        // 旧版影子库增加
        ApplicationDsCreateRequest request = new ApplicationDsCreateRequest();
        request.setOldVersion(true);
        request.setApplicationId(6780372349628715008L);
        request.setDbType(0);
        request.setDsType(0);
        request.setConfig("<configurations>\n" +
                "              <!--数据源调停者-->\n" +
                "              <datasourceMediator id=\"dbMediatorDataSource\">\n" +
                "                  <property name=\"dataSourceBusiness\" ref=\"dataSourceBusiness\"/><!--业务数据源-->\n" +
                "                  <property name=\"dataSourcePerformanceTest\" ref=\"dataSourcePerformanceTest\"/><!--压测数据源-->\n" +
                "              </datasourceMediator>\n" +
                "          \n" +
                "              <!--数据源集合-->\n" +
                "              <datasources>\n" +
                "                  <datasource id=\"dataSourceBusiness\"><!--业务数据源--> <!--业务数据源只需要URL及用户名即可进行唯一性确认等验证-->\n" +
                "                      <property name=\"url\" value=\"jdbc:mysql://114.55.42.181:3306/test_liuchuan\"/><!--数据库连接URL-->\n" +
                "                      <property name=\"username\" value=\"admin2017\"/><!--数据库连接用户名-->\n" +
                "                  </datasource>\n" +
                "                  <datasource id=\"dataSourcePerformanceTest\"><!--压测数据源-->\n" +
                "                      <property name=\"driverClassName\" value=\"com.mysql.cj.jdbc.Driver\"/><!--数据库驱动-->\n" +
                "                      <property name=\"url\" value=\"jdbc:mysql://114.55.42.181:3306/pt_test_liuchuan\"/><!--数据库连接URL-->\n" +
                "                      <property name=\"username\" value=\"admin2017\"/><!--数据库连接用户名-->\n" +
                "                      <property name=\"password\" value=\"admin2017\"/><!--数据库连接密码-->\n" +
                "                      <property name=\"initialSize\" value=\"5\"/>\n" +
                "                      <property name=\"minIdle\" value=\"5\"/>\n" +
                "                      <property name=\"maxActive\" value=\"20\"/>\n" +
                "                      <property name=\"maxWait\" value=\"60000\"/>\n" +
                "                      <property name=\"timeBetweenEvictionRunsMillis\" value=\"60000\"/>\n" +
                "                      <property name=\"minEvictableIdleTimeMillis\" value=\"300000\"/>\n" +
                "                      <property name=\"validationQuery\" value=\"SELECT 1 FROM DUAL\"/>\n" +
                "                      <property name=\"testWhileIdle\" value=\"true\"/>\n" +
                "                      <property name=\"testOnBorrow\" value=\"false\"/>\n" +
                "                      <property name=\"testOnReturn\" value=\"false\"/>\n" +
                "                      <property name=\"poolPreparedStatements\" value=\"true\"/>\n" +
                "                      <property name=\"maxPoolPreparedStatementPerConnectionSize\" value=\"20\"/>\n" +
                "                      <property name=\"connectionProperties\" value=\"druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500\"/>\n" +
                "                  </datasource>\n" +
                "              </datasources>\n" +
                "          </configurations>");

        dsService.dsAdd(request);
    }

}
