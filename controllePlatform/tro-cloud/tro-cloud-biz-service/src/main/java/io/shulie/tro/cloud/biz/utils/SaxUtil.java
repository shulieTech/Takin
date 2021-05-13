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

package io.shulie.tro.cloud.biz.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.Maps;
import com.pamirs.tro.entity.domain.vo.scenemanage.ScriptUrlVO;
import io.shulie.tro.cloud.biz.jmeter.XmlDubboJmxParser;
import io.shulie.tro.cloud.biz.jmeter.XmlHttpJmxParser;
import io.shulie.tro.cloud.biz.jmeter.XmlJdbcJmxParser;
import io.shulie.tro.cloud.biz.jmeter.XmlKafkaJmxParser;
import io.shulie.tro.utils.file.FileManagerHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * @ClassName SaxUtil
 * @Description 1、获取jmx文件内容
 * * 2、获取http接口数量
 * * 3、循环获取http的name和url
 * * 4、获取该http对应的header信息
 * *      通过字符串截取先获得整个<headerManager></headerManager>
 * *      非启用状态的header不纳入计算范围
 * *      再获取header里面的method(顺丰的请求url会放到这里），和User-Agent信息
 * * 5、替换url
 * @Author qianshui
 * @Date 2020/4/22 上午4:09
 */
@Slf4j
public class SaxUtil {

    public static Map<String, Object> parseXML(String path) {
        Long startTime = System.currentTimeMillis();
        Integer ptSize = 0;
        SAXReader saxReader = new SAXReader();
        String xmlContent = "";
        Map<String, Object> dataMap = Maps.newHashMap();
        try {
            /**
             * 读取文件内容
             */
            String content = readFileContent(path);
            if (content == null) {
                return Maps.newHashMap();
            }
            xmlContent = content;
            Document document = saxReader.read(new File(path));
            //去除所有禁用节点和对应的所有子节点
            cleanAllDisableElement(document);
            List<ScriptUrlVO> scriptUrls = getScriptUrlFromJmx(ptSize, content, document);
            dataMap.put("requestUrl", scriptUrls);
        } catch (Exception e) {
            log.error("Parse Jmeter Script Error: ", e);
        }

        dataMap.put("ptSize", ptSize);
        dataMap.put("xmlContent", xmlContent);
        log.info("Parse Jmeter Total Cost：" + (System.currentTimeMillis() - startTime) + "ms");
        return dataMap;
    }

    private static List<ScriptUrlVO> getXmlJdbcContent(Document document, String content, Integer ptSize) {
        XmlJdbcJmxParser parser = new XmlJdbcJmxParser();
        return parser.getEntryContent(document,content,ptSize);
    }

    private static List<ScriptUrlVO> getXmlKafkaContent(Document document, String content, Integer ptSize) {
        XmlKafkaJmxParser parser = new XmlKafkaJmxParser();
        return parser.getEntryContent(document, content, ptSize);
    }

    public static Map<String, Object> parseAndUpdateXML(String path) {
        Long startTime = System.currentTimeMillis();
        Integer ptSize = 0;
        SAXReader saxReader = new SAXReader();
        String xmlContent = "";
        Map<String, Object> dataMap = Maps.newHashMap();
        try {
            /**
             * 读取文件内容
             */
            String content = readFileContent(path);
            if (content == null) {
                return Maps.newHashMap();
            }
            Document document = saxReader.read(new File(path));
            //去除所有禁用节点和对应的所有子节点
            cleanAllDisableElement(document);
            List<ScriptUrlVO> scriptUrls = getScriptUrlFromJmx(ptSize, content, document);

            dataMap.put("requestUrl", scriptUrls);

            StringWriter writer = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(writer, new OutputFormat());
            xmlWriter.write(document);
            xmlWriter.close();
            xmlContent = writer.toString();
            FileManagerHelper.deleteFilesByPath(path);
            FileManagerHelper.createFileByPathAndString(path, xmlContent);
        } catch (Exception e) {
            log.error("Parse Jmeter Script Error: ", e);
        }

        dataMap.put("ptSize", ptSize);
        dataMap.put("xmlContent", xmlContent);
        log.info("Parse Jmeter Total Cost：" + (System.currentTimeMillis() - startTime) + "ms");
        return dataMap;
    }

    private static List<ScriptUrlVO> getScriptUrlFromJmx(Integer ptSize, String content, Document document) {
        List<ScriptUrlVO> scriptUrls = new ArrayList<>();
        List<ScriptUrlVO> xmlHttpContent = getXmlHttpContent(document, content, ptSize);
        List<ScriptUrlVO> dubboContents = getXmlDubboContent(document, content, ptSize);
        List<ScriptUrlVO> kafkaContents = getXmlKafkaContent(document, content, ptSize);
        List<ScriptUrlVO> jdbcContents = getXmlJdbcContent(document, content, ptSize);

        if (CollectionUtils.isNotEmpty(xmlHttpContent)) {
            scriptUrls.addAll(xmlHttpContent);
        }
        if (CollectionUtils.isNotEmpty(dubboContents)) {
            scriptUrls.addAll(dubboContents);
        }
        if (CollectionUtils.isNotEmpty(kafkaContents)) {
            scriptUrls.addAll(kafkaContents);
        }
        if (CollectionUtils.isNotEmpty(jdbcContents)) {
            scriptUrls.addAll(jdbcContents);
        }
        log.info("jmx parser start ==================");
        scriptUrls.stream().forEach((scriptUrlVO)->{
            log.info(JSON.toJSONString(scriptUrlVO));
        });
        log.info("jmx parser end ==================");
        return scriptUrls;
    }

    private static List<ScriptUrlVO> getXmlDubboContent(Document document, String content, Integer ptSize) {
        XmlDubboJmxParser parser = new XmlDubboJmxParser();
        return parser.getEntryContent(document, content, ptSize);
    }

    private static List<ScriptUrlVO> getXmlHttpContent(Document document, String content, Integer ptSize) {
        XmlHttpJmxParser httpJmxParser = new XmlHttpJmxParser();
        return httpJmxParser.getEntryContent(document, content, ptSize);
    }

    public static List<Element> getAllElement(String elementName, Document document) {
        List<Element> result = new ArrayList<>();
        Element rootElement = document.getRootElement();
        selectElement(elementName, rootElement.elements(), result);
        return result;
    }

    public static void cleanAllDisableElement(Document document) {
        Element rootElement = document.getRootElement();
        cleanDisableElement(rootElement.elements());
    }

    public static void cleanDisableElement(List elements) {
        if (CollectionUtils.isNotEmpty(elements)) {
            for (int i = 0; i < elements.size(); i++) {
                Element element = (Element)elements.get(i);
                cleanDisableElement(element.elements());
                if (element.attributeValue("enabled") != null && !"true".equals(element.attributeValue("enabled"))) {
                    if (elements.size() > i + 1) {
                        Element nextElement = (Element)elements.get(i + 1);
                        if ("hashTree".equals(nextElement.getName())) {
                            elements.remove(nextElement);
                        }
                    }
                    elements.remove(element);
                    i--;
                }
            }
        }
    }

    public static Element selectElementByEleNameAndAttr(String elementName, String attributeName, String attributeValue,
        List elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return null;
        }
        for (Object it : elements) {
            Element element = (Element)it;
            if (element.getName().equals(elementName) && attributeValue.equals(element.attributeValue(attributeName))) {
                return element;
            }
            Element childElement = selectElementByEleNameAndAttr(elementName, attributeName, attributeValue,
                element.elements());
            if (childElement != null) {
                return childElement;
            }
        }
        return null;
    }

    public static void selectElement(String elementName, List elements, List<Element> result) {
        if (CollectionUtils.isEmpty(elements)) {
            return;
        }
        for (Iterator it = elements.iterator(); it.hasNext(); ) {
            Element element = (Element)it.next();
            if (element.getName().equals(elementName)) {
                result.add(element);
            }
            List childElements = element.elements();
            selectElement(elementName, childElements, result);
        }
    }

    public static String readFileContent(String path) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            StringBuffer sb = new StringBuffer();
            String temp;
            while ((temp = reader.readLine()) != null) {
                sb.append(temp);
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("ReadFileContent Error: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws InterruptedException {
        String path = "/Users/shulie/Documents/test.jmx";
        //        SaxUtil.updatePressTestTags(path);
        File file = new File(path);
        //因为新增场景脚本是异步的，这里最多等待5分钟
        int i = 0;
        while (!file.exists()) {
            i++;
            Thread.sleep(100L);
            if (i > 3000) {
                return;
            }
        }
        System.out.println("有文件");
    }

    /**
     * 将dubbo压测标的值从true修改为false
     * 将http的压测标从PerfomanceTest 修改为flowDebug
     *
     * @param path
     */
    public static void updatePressTestTags(String path) {
        SAXReader saxReader = new SAXReader();
        try {

            File file = new File(path);
            //因为新增场景脚本是异步的，这里最多等待5分钟
            int i = 0;
            while (!file.exists()) {
                i++;
                Thread.sleep(100L);
                if (i > 3000) {
                    return;
                }
            }

            /**
             * 读取文件内容
             */
            Document document = saxReader.read(file);
            //去除所有禁用节点和对应的所有子节点
            cleanAllDisableElement(document);
            updateXmlHttpPressTestTags(document);
            updateXmlDubboPressTestTags(document);

            StringWriter writer = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(writer, new OutputFormat());
            xmlWriter.write(document);
            xmlWriter.close();
            String xmlContent = writer.toString();
            FileManagerHelper.deleteFilesByPath(path);
            FileManagerHelper.createFileByPathAndString(path, xmlContent);
        } catch (Exception e) {
            log.error("Parse Jmeter Script Error: ", e);
        }
    }

    private static void updateXmlDubboPressTestTags(Document document) {

        List<Element> allElement = getAllElement("io.github.ningyu.jmeter.plugin.dubbo.sample.DubboSample", document);
        for (Element element : allElement) {
            List<Element> stringPropList = new ArrayList<>();
            selectElement("stringProp", element.elements(), stringPropList);
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
                    Element dubboAttachmentValue = selectElementByEleNameAndAttr("stringProp", "name",
                        attachmentArgsValue, element.elements());
                    if (dubboAttachmentValue != null && "true".equals(dubboAttachmentValue.getText())) {
                        dubboAttachmentValue.setText("false");
                    }
                }
            }
        }
    }

    public static void updateXmlHttpPressTestTags(Document document) {
        List<Element> allElement = getAllElement("HeaderManager", document);
        if (CollectionUtils.isNotEmpty(allElement)) {
            List<Element> allElementProp = new ArrayList<>();
            for (Element headerElement : allElement) {
                selectElement("elementProp", headerElement.elements(), allElementProp);
            }
            if (CollectionUtils.isNotEmpty(allElementProp)) {
                for (Element elementProp : allElementProp) {
                    Element nameElement = selectElementByEleNameAndAttr("stringProp", "name", "Header.name",
                        elementProp.elements());
                    Element valueElement = selectElementByEleNameAndAttr("stringProp", "name", "Header.value",
                        elementProp.elements());
                    if (nameElement != null && valueElement != null && "User-Agent".equals(nameElement.getText())
                        && "PerfomanceTest".equals(valueElement.getText())) {
                        valueElement.setText("FlowDebug");
                    }
                }
            }
        }
    }

}
