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

package io.shulie.tro.web.app.utils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import com.pamirs.tro.common.enums.ds.DsTypeEnum;
import com.pamirs.tro.entity.domain.vo.dsmanage.Configurations;
import com.pamirs.tro.entity.domain.vo.dsmanage.DataSource;
import com.pamirs.tro.entity.domain.vo.dsmanage.DatasourceMediator;
import io.shulie.tro.web.app.constant.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 业务上的工具类
 * 影子库/表 工具类
 *
 * @author loseself
 * @date 2021/4/5 12:02 下午
 **/
@Slf4j
public class DsManageUtil  {


    /**
     * 影子ES Server方案
     *
     * @param dsType 方案类型
     * @return 是否
     */
    public static boolean isEsServerType(Integer dsType) {
        return DsTypeEnum.SHADOW_ES_SERVER.getCode().equals(dsType);
    }


    /**
     * 影子Hbase Server方案
     * @param dsType 方案类型
     * @return 是否
     */
    public static boolean isHbaseServerType(Integer dsType) {
        return DsTypeEnum.SHADOW_HBASE_SERVER.getCode().equals(dsType);
    }

    /**
     * 影子Server方案
     *
     * @param dsType 方案类型
     * @return 是否
     */
    public static boolean isServerDsType(Integer dsType) {
        return DsTypeEnum.SHADOW_REDIS_SERVER.getCode().equals(dsType);
    }

    /**
     * 影子表方案
     *
     * @param dsType 方案类型
     * @return 是否
     */
    public static boolean isTableDsType(Integer dsType) {
        return DsTypeEnum.SHADOW_TABLE.getCode().equals(dsType);
    }

    /**
     * 影子库方案
     *
     * @param dsType 方案类型
     * @return 是否
     */
    public static boolean isSchemaDsType(Integer dsType) {
        return DsTypeEnum.SHADOW_DB.getCode().equals(dsType);
    }

    /**
     * 影子库方案
     *
     * @param dsType       方案类型
     * @param isNewVersion 是否是新版 agent
     * @return 是否
     */
    public static boolean isNewVersionSchemaDsType(Integer dsType, boolean isNewVersion) {
        return DsTypeEnum.SHADOW_DB.getCode().equals(dsType) && isNewVersion;
    }

    /**
     * 根据 config 解析 table 的 url
     *
     * @param url 配置
     * @return table 所属 数据库 url
     */
    public static String parseShadowTableUrl(String url) {
        if (url == null) {
            return null;
        }

        if (url.contains(AppConstants.QUESTION_MARK)) {
            return url.substring(0, url.indexOf(AppConstants.QUESTION_MARK));
        }

        return url;
    }

    /**
     * json 原有的密码元素
     *
     * @param password 密码
     * @return json 原有的密码元素
     */
    public static String getOriginPasswordFieldAboutJson(String password) {
        return String.format(AppConstants.PASSWORD_JSON, password);
    }

    /**
     * json 的脱敏密码元素
     *
     * @return json 的脱敏密码元素
     */
    public static String getSafePasswordFieldAboutJson() {
        return String.format(AppConstants.PASSWORD_JSON, AppConstants.PASSWORD_COVER);
    }

    /**
     * xml 的脱敏密码元素
     *
     * @return xml 的脱敏密码元素
     */
    public static String getSafePasswordElementAboutXml() {
        return String.format(AppConstants.PASSWORD_XML, AppConstants.PASSWORD_COVER);
    }

    /**
     * xml 原有的密码元素
     *
     * @param password 密码
     * @return xml 原有的密码元素
     */
    public static String getOriginPasswordElementAboutXml(String password) {
        return String.format(AppConstants.PASSWORD_XML, password);
    }

    /**
     * xml 解析获得影子库配置实例
     *
     * @param xml xml 配置
     * @return 配置实例
     */
    @SuppressWarnings("unchecked")
    public static Configurations getConfigurationsByXml(String xml) {
        xml = replaceSpecialCharacters(xml);// 处理xml文本中 特殊字符 '&', 当jdbc url中出现多参数的情况
        SAXReader reader = new SAXReader();
        try {
            Configurations configurations = new Configurations();
            Document document = reader.read(new ByteArrayInputStream(xml.getBytes()));
            Element root = document.getRootElement();
            Element datasourceMediatorElement = root.element("datasourceMediator");
            List<Element> datasourceElementList = root.element("datasources").elements("datasource");
            DatasourceMediator datasourceMediator = parseDatasourceMediator(datasourceMediatorElement);
            List<DataSource> dataSourceList = new ArrayList<>();
            for (Element element : datasourceElementList) {
                dataSourceList.add(parseDataSource(element));
            }
            configurations.setDatasourceMediator(datasourceMediator);
            configurations.setDataSources(dataSourceList);
            return configurations;
        } catch (Exception e) {
            log.error("XML 解析错误: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 替换转义后的字符
     *
     * @param xmlString xmlString
     * @return 替换后的 xml
     */
    private static String replaceSpecialCharacters(String xmlString) {
        return xmlString.replaceAll("&", "&amp;");
    }

    /**
     * 解析数据调停者
     *
     * @param element 元素
     * @return 数据调停者实例
     */
    @SuppressWarnings("unchecked")
    private static DatasourceMediator parseDatasourceMediator(Element element) {
        if (element == null) {
            return null;
        }
        DatasourceMediator mediator = new DatasourceMediator();
        List<Element> propertyList = element.elements("property");
        for (Element property : propertyList) {
            Attribute nameAttr = property.attribute("name");
            Attribute refAttr = property.attribute("ref");
            if (nameAttr != null && refAttr != null) {
                String name = nameAttr.getValue();
                String ref = refAttr.getValue();
                if ("dataSourceBusiness".equals(name)) {
                    mediator.setDataSourceBusiness(ref);
                }
                if ("dataSourcePerformanceTest".equals(name)) {
                    mediator.setDataSourcePerformanceTest(ref);
                }
            }
        }
        return mediator;
    }

    /**
     * 数据源解析
     *
     * @param element 元素
     * @return 数据源实例
     */
    @SuppressWarnings("unchecked")
    private static DataSource parseDataSource(Element element) {
        if (element == null) {
            return null;
        }
        DataSource dataSource = new DataSource();
        Attribute idAttr = element.attribute("id");
        if (idAttr != null && idAttr.getValue() != null) {
            dataSource.setId(idAttr.getValue());
        }
        List<Element> propertyList = element.elements("property");
        for (Element property : propertyList) {
            Attribute nameAttr = property.attribute("name");
            Attribute valueAttr = property.attribute("value");
            if (nameAttr != null && valueAttr != null) {
                String name = nameAttr.getValue();
                String value = valueAttr.getValue();
                if ("driverClassName".equals(name)) {
                    dataSource.setDriverClassName(value);
                }
                if ("url".equals(name)) {
                    dataSource.setUrl(value);
                }
                if ("schema".equals(name)) {
                    dataSource.setSchema(value);
                }
                if ("username".equals(name)) {
                    dataSource.setUsername(value);
                }
                if ("password".equals(name)) {
                    dataSource.setPassword(value);
                }
                if ("initialSize".equals(name)) {
                    dataSource.setInitialSize(value);
                }
                if ("minIdle".equals(name)) {
                    dataSource.setMinIdle(value);
                }
                if ("maxActive".equals(name)) {
                    dataSource.setMaxActive(value);
                }
                if ("maxWait".equals(name)) {
                    dataSource.setMaxWait(value);
                }
                if ("timeBetweenEvictionRunsMillis".equals(name)) {
                    dataSource.setTimeBetweenEvictionRunsMillis(value);
                }
                if ("minEvictableIdleTimeMillis".equals(name)) {
                    dataSource.setMinEvictableIdleTimeMillis(value);
                }
                if ("validationQuery".equals(name)) {
                    dataSource.setValidationQuery(value);
                }
                if ("testWhileIdle".equals(name)) {
                    dataSource.setTestWhileIdle(value);
                }
                if ("testOnBorrow".equals(name)) {
                    dataSource.setTestOnBorrow(value);
                }
                if ("testOnReturn".equals(name)) {
                    dataSource.setTestOnReturn(value);
                }
                if ("poolPreparedStatements".equals(name)) {
                    dataSource.setPoolPreparedStatements(value);
                }
                if ("maxPoolPreparedStatementPerConnectionSize".equals(name)) {
                    dataSource.setMaxPoolPreparedStatementPerConnectionSize(value);
                }
                if ("connectionProperties".equals(name)) {
                    dataSource.setConnectionProperties(value);
                }
            }
        }
        return dataSource;
    }

}
