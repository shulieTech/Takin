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

package jmeter;

import java.io.File;
import java.util.List;

import com.pamirs.tro.entity.domain.vo.scenemanage.ScriptUrlVO;
import io.shulie.tro.cloud.biz.jmeter.XmlJdbcJmxParser;
import io.shulie.tro.cloud.biz.utils.SaxUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class XmlJdbcJmxParserTest {

    public void getEntryContent() throws DocumentException {

        XmlJdbcJmxParser xmlJdbcJmxParser = new XmlJdbcJmxParser();
        String path = "/Users/ranghai/shulie/tro-cloud/doc/jmx/jdbc_template.xml";
        Integer ptSize = 0;
        SAXReader saxReader = new SAXReader();
        /**
         * 读取文件内容
         */
        String content = SaxUtil.readFileContent(path);
        Document document = saxReader.read(new File(path));
        List<ScriptUrlVO> entryContent = xmlJdbcJmxParser.getEntryContent(document, content, ptSize);

    }
}