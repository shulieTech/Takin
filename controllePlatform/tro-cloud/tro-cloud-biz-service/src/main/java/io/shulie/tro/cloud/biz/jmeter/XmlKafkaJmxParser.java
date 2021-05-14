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
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author HengYu
 * @className XmlKafkaJmxParser
 * @date 2021/4/12 4:02 下午
 * @description kafka 脚本解析器
 *
 */
public class XmlKafkaJmxParser extends JmxParser {

    private  Logger log = LoggerFactory.getLogger(XmlKafkaJmxParser.class);

    @Override
    public List<ScriptUrlVO> getEntryContent(Document document, String content, Integer ptSize) {
        List<ScriptUrlVO> voList = new ArrayList<>();
        List<Element> allElement = SaxUtil.getAllElement("JavaSampler", document);
        for (Element element : allElement) {
            Attribute attribute = element.attribute("testname");
            String testName = attribute.getValue();
            if (testName.contains(" ")) {
                testName = testName.replaceAll(" ", "");
                attribute.setValue(testName);
            }
            String enable = element.attributeValue("enabled");
            Element kafkaClassName = SaxUtil.selectElementByEleNameAndAttr("stringProp", "name", "classname", element.elements());
            if (kafkaClassName != null && "com.gslab.pepper.sampler.PepperBoxKafkaSampler".equals(kafkaClassName.getText())) {
                Element kafkaElement = SaxUtil.selectElementByEleNameAndAttr("elementProp", "name", "kafka.topic.name", element.elements());
                if (kafkaElement != null) {
                    Element kafkaTopicElement = SaxUtil.selectElementByEleNameAndAttr("stringProp", "name",
                        "Argument.value", kafkaElement.elements());
                    if (kafkaTopicElement != null) {
                        if (kafkaTopicElement.getText().startsWith("PT_")) {
                            ptSize++;
                        }
                        voList.add(new ScriptUrlVO(testName, "true".equals(enable), kafkaTopicElement.getText()));
                    }
                }
            }
        }
        return voList;
    }

}
