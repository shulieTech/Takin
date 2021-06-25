package io.shulie.tro.utils.xml;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.StringWriter;

/**
 * @author zhaoyong
 */
@Slf4j
public class XmlHelper {

    public static String formatXml(String str){
        try {
            Document document = null;
            document = DocumentHelper.parseText(str);
            // 格式化输出格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            StringWriter writer = new StringWriter();
            // 格式化输出流
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            // 将document写入到输出流
            xmlWriter.write(document);
            xmlWriter.close();
            return writer.toString();
        }catch (Exception e){
            log.error("解析文件到xml出错！",e);
        }
        return null;
    }
}
