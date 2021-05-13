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

package io.shulie.tro.cloud.biz.service.machine;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import io.shulie.tro.common.beans.response.ResponseResult;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: fanxx
 * @Date: 2020/5/18 下午3:44
 * @Description:
 */
public class MachineUtil {
    private static final Logger log = LoggerFactory.getLogger(MachineUtil.class);

    public static String FALSE_CORE = "0";

    public static ResponseResult parseXMLToMap(String xmlContent, Map map) {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(new ByteArrayInputStream(xmlContent.getBytes("UTF-8")));
            Element root = document.getRootElement();
            for (Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element element = (Element)i.next();
                Attribute key = element.attribute("name");
                Attribute value = element.attribute("value");
                map.put(key.getValue(), value.getValue());
            }
        } catch (DocumentException e) {
            log.error(e.getMessage(), e);
            return ResponseResult.fail(FALSE_CORE, "更多配置解析失败", "");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return ResponseResult.success(map);
    }
}
