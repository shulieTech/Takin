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

import java.util.ArrayList;
import java.util.List;

import com.pamirs.tro.entity.domain.vo.scenemanage.ScriptUrlVO;
import io.shulie.tro.cloud.biz.utils.SaxUtil;
import io.shulie.tro.constants.TroRequestConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author HengYu
 * @className XmlDubboJmxParser
 * @date 2021/4/12 4:02 下午
 * @description dubbo 脚本解析器
 */
public class XmlDubboJmxParser extends JmxParser {

    private Logger log = LoggerFactory.getLogger(XmlDubboJmxParser.class);

    public static final String HEADER_URL_NAME = "method";

    public static final String PT_NAME = TroRequestConstant.CLUSTER_TEST_HEADER_KEY;

    public static final String PT_VALUE = TroRequestConstant.CLUSTER_TEST_HEADER_VALUE;

    public static final String GET_CONCAT_CHARACTER = "?";

    @Override
    public List<ScriptUrlVO> getEntryContent(Document document, String content, Integer ptSize) {
        List<ScriptUrlVO> voList = new ArrayList<>();
        List<Element> allElement = SaxUtil.getAllElement("io.github.ningyu.jmeter.plugin.dubbo.sample.DubboSample",
            document);

        for (Element element : allElement) {
            Attribute attribute = element.attribute("testname");
            String testname = attribute.getValue();
            if (testname.contains(" ")) {
                testname = testname.replaceAll(" ", "");
                attribute.setValue(testname);
            }
            String enable = element.attributeValue("enabled");
            Element fieldDubboInterface = SaxUtil.selectElementByEleNameAndAttr("stringProp", "name",
                "FIELD_DUBBO_INTERFACE", element.elements());
            Element fieldDubboMethod = SaxUtil.selectElementByEleNameAndAttr("stringProp", "name", "FIELD_DUBBO_METHOD",
                element.elements());
            if (fieldDubboInterface == null || fieldDubboMethod == null) {
                continue;
            }
            List<Element> stringPropList = new ArrayList<>();
            SaxUtil.selectElement("stringProp", element.elements(), stringPropList);

            if (CollectionUtils.isNotEmpty(stringPropList)) {
                String attachmentArgsValue = "";
                for (Element ele : stringPropList) {
                    if (ele.attributeValue("name") != null && ele.attributeValue("name").startsWith(
                        "FIELD_DUBBO_ATTACHMENT_ARGS_KEY")
                        && "p-pradar-cluster-test".equals(ele.getText())) {
                        String attributeValue = ele.attributeValue("name");
                        attachmentArgsValue = attributeValue.replace("KEY", "VALUE");
                    }
                }
                if (StringUtils.isNotBlank(attachmentArgsValue)) {
                    Element dubboAttachmentValue = SaxUtil.selectElementByEleNameAndAttr("stringProp", "name",
                        attachmentArgsValue, element.elements());
                    if (dubboAttachmentValue != null && "true".equals(dubboAttachmentValue.getText())) {
                        ptSize++;
                    }
                }
            }
            voList.add(new ScriptUrlVO(testname, "true".equals(enable),
                fieldDubboInterface.getText() + "#" + fieldDubboMethod.getText()));
        }
        return voList;
    }

}
