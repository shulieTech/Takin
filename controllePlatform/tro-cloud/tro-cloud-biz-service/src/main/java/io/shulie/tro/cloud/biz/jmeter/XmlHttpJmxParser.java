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

package io.shulie.tro.cloud.biz.jmeter;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.pamirs.tro.entity.domain.vo.scenemanage.ScriptUrlVO;
import io.shulie.tro.cloud.common.utils.ParseXmlUtil;
import io.shulie.tro.constants.TroRequestConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author HengYu
 * @className XmlHttpJmxParser
 * @date 2021/4/12 4:02 下午
 * @description
 */
public class XmlHttpJmxParser extends JmxParser {

    private Logger log = LoggerFactory.getLogger(XmlHttpJmxParser.class);

    public static final String HEADER_URL_NAME = "method";

    public static final String PT_NAME = TroRequestConstant.CLUSTER_TEST_HEADER_KEY;

    public static final String PT_VALUE = TroRequestConstant.CLUSTER_TEST_HEADER_VALUE;

    public static final String GET_CONCAT_CHARACTER = "?";

    @Override
    public List<ScriptUrlVO> getEntryContent(Document document, String content, Integer ptSize) {

        List<ScriptUrlVO> voList = Lists.newArrayList();

        StringBuffer http = new StringBuffer().append("/").append("/").append("HTTPSamplerProxy");

        int httpSize = document.selectNodes(http.toString()).size();

        String namePath = new StringBuffer().append("/").append("/").append("HTTPSamplerProxy").append("/@").append(
            "testname").toString();

        String enablePath = new StringBuffer().append("/").append("/").append("HTTPSamplerProxy").append("/@")
            .append("enabled").toString();

        String urlPath = new StringBuffer().append("/").append("/").append("HTTPSamplerProxy")
            .append("/").append("/").append("stringProp")
            .append("[@").append("name=\"HTTPSampler.path\"]").toString();

        List list1;
        for (int index = 0; index < httpSize; index++) {
            Attribute attribute = (Attribute)document.selectNodes(namePath).get(index);
            String name = attribute.getValue();
            if (name.contains(" ")) {
                name = name.replaceAll(" ", "");
                attribute.setValue(name);
            }
            String enable = ((Attribute)document.selectNodes(enablePath).get(index)).getValue();
            list1 = ((Element)document.selectNodes(urlPath).get(index)).content();
            if (CollectionUtils.isEmpty(list1)) {
                continue;
            }
            String url = ((Text)list1.get(0)).getText();
            String headerXml = getHeaderXml(content, index);
            if (headerXml != null) {
                Map<String, String> headerMap = ParseXmlUtil.parseHeaderXml(headerXml);
                if (MapUtils.isNotEmpty(headerMap)) {
                    if (headerMap.containsKey(HEADER_URL_NAME)) {
                        url = headerMap.get(HEADER_URL_NAME);
                    }
                    if (headerMap.containsKey(PT_NAME) && PT_VALUE.equals(headerMap.get(PT_NAME))) {
                        ptSize++;
                    }
                }
            }
            if (url != null) {
                int pos = url.indexOf(GET_CONCAT_CHARACTER);
                if (pos > 0) {
                    url = url.substring(0, pos);
                }
            }
            voList.add(new ScriptUrlVO(name, "true".equals(enable), url));
        }
        if (CollectionUtils.isNotEmpty(voList)) {
            StringBuffer sb = new StringBuffer();
            voList.forEach(data -> sb.append(data.getName()).append(" ").append(data.getPath()).append("\n"));
            log.info("Parse Jmeter Script Result: " + sb.toString());
        } else {
            log.info("Parse Jmeter Script Empty");
        }
        return voList;
    }

    /**
     * 获取第httpIndex对应的headerManager
     *
     * @param
     * @return
     */
    private static String getHeaderXml(String xml, int httpIndex) {
        int times = 0;
        String tempXml = xml;
        int p1 = -1;
        while (times <= httpIndex) {
            p1 = tempXml.indexOf("<HTTPSamplerProxy");
            if (p1 == -1) {
                break;
            }
            tempXml = tempXml.substring(p1 + "<HTTPSamplerProxy".length());
            times++;
        }
        int p2 = tempXml.indexOf("<HTTPSamplerProxy");
        if (p2 > -1) {
            tempXml = tempXml.substring(0, p2);
        }
        int headerStart = tempXml.indexOf("<HeaderManager");
        int headerEnd = tempXml.indexOf("</HeaderManager>");
        if (headerStart == -1 || headerEnd == -1) {
            return null;
        }
        return tempXml.substring(headerStart, headerEnd + "</HeaderManager>".length());
    }

}
