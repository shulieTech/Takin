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

package io.shulie.tro.cloud.common.utils;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @ClassName ParseXmlUtil
 * @Description
 * @Author qianshui
 * @Date 2020/7/24 上午11:50
 */
@Slf4j
public class ParseXmlUtil {

    public static Map<String, String> parseHeaderXml(String xml) {
        Map<String, String> dataMap = Maps.newHashMap();
        try {
            Document doc = DocumentHelper.parseText(xml);
            Element rootElt = doc.getRootElement();
            boolean enabled = checkEnabled(rootElt);
            log.info("根节点：" + rootElt.getName() + "; 是否启用：" + enabled);
            if (!enabled) {
                return null;
            }
            List<Element> collectionProps = rootElt.elements("collectionProp");
            if (CollectionUtils.isEmpty(collectionProps)) {
                return null;
            }
            for (Element collectionProp : collectionProps) {
                if (!"HeaderManager.headers".equalsIgnoreCase(collectionProp.attributeValue("name"))) {
                    continue;
                }
                List<Element> elementProps = collectionProp.elements("elementProp");
                if (CollectionUtils.isEmpty(elementProps)) {
                    return null;
                }
                for (Element elementProp : elementProps) {
                    if (!"Header".equalsIgnoreCase(elementProp.attributeValue("elementType"))) {
                        continue;
                    }
                    List<Element> stringProps = elementProp.elements("stringProp");
                    if (CollectionUtils.isEmpty(stringProps)) {
                        continue;
                    }
                    String name = null;
                    String value = null;
                    for (Element stringProp : stringProps) {
                        if ("Header.name".equalsIgnoreCase(stringProp.attributeValue("name"))) {
                            name = StringUtils.replace(stringProp.getText(), " ", "");
                        } else if ("Header.value".equalsIgnoreCase(stringProp.attributeValue("name"))) {
                            value = StringUtils.replace(stringProp.getText().trim(), " ", "");
                        }
                    }
                    if (name != null && value != null) {
                        dataMap.put(name, value);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Parse String Xml Error: " + e.getMessage());
        }
        return dataMap;
    }

    private static boolean checkEnabled(Element element) {
        try {
            return "true".equalsIgnoreCase(element.attributeValue("enabled"));
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String xml =
            "<HeaderManager guiclass=\"HeaderPanel\" testclass=\"HeaderManager\" testname=\"HTTP信息头管理器\" "
                + "enabled=\"true\">\n"
                +
                "  <collectionProp name=\"HeaderManager.headers\">\n" +
                "    <elementProp name=\"\" elementType=\"Header\">\n" +
                "      <stringProp name=\"Header.name\">Content-Type</stringProp>\n" +
                "      <stringProp name=\"Header.value\">application/json</stringProp>\n" +
                "    </elementProp>\n" +
                "    <elementProp name=\"\" elementType=\"Header\">\n" +
                "      <stringProp name=\"Header.name\">module</stringProp>\n" +
                "      <stringProp name=\"Header.value\">waybill-web</stringProp>\n" +
                "    </elementProp>\n" +
                "    <elementProp name=\"\" elementType=\"Header\">\n" +
                "      <stringProp name=\"Header.name\">method</stringProp>\n" +
                "      <stringProp name=\"Header.value\">/waybill-web/cost/queryCost</stringProp>\n" +
                "    </elementProp>\n" +
                "    <elementProp name=\"\" elementType=\"Header\">\n" +
                "      <stringProp name=\"Header.name\">rpcType</stringProp>\n" +
                "      <stringProp name=\"Header.value\">http</stringProp>\n" +
                "    </elementProp>\n" +
                "    <elementProp name=\"\" elementType=\"Header\">\n" +
                "      <stringProp name=\"Header.name\">httpMethod</stringProp>\n" +
                "      <stringProp name=\"Header.value\">post</stringProp>\n" +
                "    </elementProp>\n" +
                "    <elementProp name=\"\" elementType=\"Header\">\n" +
                "      <stringProp name=\"Header.name\">env</stringProp>\n" +
                "      <stringProp name=\"Header.value\">7070</stringProp>\n" +
                "    </elementProp>\n" +
                "    <elementProp name=\"\" elementType=\"Header\">\n" +
                "      <stringProp name=\"Header.name\">siteCode</stringProp>\n" +
                "      <stringProp name=\"Header.value\">1</stringProp>\n" +
                "    </elementProp>\n" +
                "    <elementProp name=\"\" elementType=\"Header\">\n" +
                "      <stringProp name=\"Header.name\">User-Agent</stringProp>\n" +
                "      <stringProp name=\"Header.value\">PerfomanceTest</stringProp>\n" +
                "    </elementProp>\n" +
                "  </collectionProp>\n" +
                "  <collectionProp name=\"HeaderManager.headers\">\n" +
                "    <elementProp name=\"\" elementType=\"Header\">\n" +
                "      <stringProp name=\"Header.name\">Content-Type1</stringProp>\n" +
                "      <stringProp name=\"Header.value\">application/json1</stringProp>\n" +
                "    </elementProp>\n" +
                "    <elementProp name=\"\" elementType=\"Header\">\n" +
                "      <stringProp name=\"Header.name\">module1</stringProp>\n" +
                "      <stringProp name=\"Header.value\">waybill-web1</stringProp>\n" +
                "    </elementProp>\n" +
                "  </collectionProp>\n" +
                "</HeaderManager>";

        Map<String, String> map = parseHeaderXml(xml);
        map.forEach((key, value) -> {
            System.out.println("name:" + key + "; value:" + value);
        });
    }
}
