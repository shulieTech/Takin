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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.pamirs.tro.entity.domain.vo.scenemanage.ScriptUrlVO;
import io.shulie.tro.cloud.biz.utils.SaxUtil;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author HengYu
 * @className XmlJdbcJmxParser
 * @date 2021/4/12 4:02 下午
 * @description jdbc 插件 脚本解析器
 *
 */
public class XmlJdbcJmxParser extends JmxParser {

    public static final String JAVA_SAMPLER_ATTR_NAME = "testname";
    public static final String JAVA_SAMPLER = "JDBCSampler";
    private  Logger log = LoggerFactory.getLogger(XmlJdbcJmxParser.class);

    @Override
    public List<ScriptUrlVO> getEntryContent(Document document, String content, Integer ptSize) {
        List<ScriptUrlVO> voList = new ArrayList<>();

        List<Element> allElement = SaxUtil.getAllElement(JAVA_SAMPLER, document);
        Set<Element> cache =  new HashSet<Element>();
        for (Element element : allElement) {
            if (cache.contains(element)){
                continue;
            }
            Element SampleParent = element.getParent();
            List<Element> elements = SampleParent.elements();
            Iterator<Element> iterator = elements.iterator();
            while (iterator.hasNext()){
                processJdbcSample(voList,cache, iterator);
            }
        }
        log.info("jdbc parser jmx get entry size : {}",voList.size());
        return voList;
    }

    private void processJdbcSample(List<ScriptUrlVO> voList,
        Set<Element> cache, Iterator<Element> iterator) {
        Element subElement = iterator.next();

        if (subElement.getName().equals(JAVA_SAMPLER)){

            ScriptUrlVO scriptUrlVO = new ScriptUrlVO();
            Attribute attribute = subElement.attribute(JAVA_SAMPLER_ATTR_NAME);
            String testName = attribute.getValue();
            if (testName.contains(" ")) {
                testName = testName.replaceAll(" ", "");
                attribute.setValue(testName);
            }

            System.out.println(testName);

            String enable = subElement.attributeValue("enabled");
            scriptUrlVO.setEnable(Boolean.valueOf(enable));
            scriptUrlVO.setName(testName);


            Element nextElement = iterator.next();
            processHashTree(nextElement,scriptUrlVO);
            voList.add(scriptUrlVO);
        }
        cache.add(subElement);
    }

    private void processHashTree(Element hashTreeElement,
        ScriptUrlVO scriptUrlVO) {

        if (hashTreeElement != null){
            Element headerPanelElement = SaxUtil.selectElementByEleNameAndAttr("HeaderManager", "testclass", "HeaderManager",
                hashTreeElement.elements());

            Element collectionPropElement = SaxUtil.selectElementByEleNameAndAttr("collectionProp", "name",
                "HeaderManager.headers", headerPanelElement.elements());


            List<Element> result = new ArrayList<>();
            SaxUtil.selectElement("stringProp",collectionPropElement.elements(),result);

            for (int i = 0; i < result.size(); i++) {
                Element element1 = result.get(i);
                String name = element1.attributeValue("name");
                String value = element1.getStringValue();
                if (name.equalsIgnoreCase("Header.name") && value.equalsIgnoreCase("request_url")){
                    i++;
                    Element element2 = result.get(i);
                    if (element2 != null){
                        String name2 = element2.attributeValue("name");
                        String value2 = element2.getText();
                        System.out.println(name2 + "" + value2);
                        scriptUrlVO.setPath(value2);
                    }
                }
            }
        }
    }

}
