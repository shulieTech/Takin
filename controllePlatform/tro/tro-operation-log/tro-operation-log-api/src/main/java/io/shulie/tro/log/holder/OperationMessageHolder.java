package io.shulie.tro.log.holder;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-09-16
 */
@Slf4j
@Component
public class OperationMessageHolder implements InitializingBean {

    private OperationMessageHolder() { /* no instance */ }

    public static final Map<String, String> MESSAGE_HOLDER = new ConcurrentHashMap<>();

    public static String formatMessage(String key, Map<String, String> vars) {
        String messagePattern = MESSAGE_HOLDER.get(key);
        if (messagePattern == null) {
            log.error("日志记录失败，key[{}]对应的描述信息在文件中没有发现", key);
            return "";
        }
        StringSubstitutor sub = new StringSubstitutor(vars);
        try {
            return sub.replace(messagePattern);
        } catch (Exception e) {
            log.error("日志记录失败，key[{}]对应的描述信息转换失败,messagePattern:[{}],变量为:[{}]", key, messagePattern, vars);
            return "";
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            ClassPathResource classPathResource = new ClassPathResource("log_message/operation_log.properties");
            System.out.println(classPathResource.getPath());
            if (!classPathResource.exists()) {
                log.error("操作日志描述文件加载失败，在classpath下面，没有找到/log_message的目录");
                throw new IllegalArgumentException(
                    "操作日志描述文件加载失败，在classpath下面，没有找到[log_message/operation_log.properties]的文件");
            }
            Properties properties = new Properties();
            properties.load(new InputStreamReader(classPathResource.getInputStream(), StandardCharsets.UTF_8));
            Enumeration<Object> keys = properties.keys();
            while (keys.hasMoreElements()) {
                String key = (String)keys.nextElement();
                String value = (String)properties.get(key);
                MESSAGE_HOLDER.put(key, value);
            }
        } catch (Exception e) {
            //ignore
        }
    }
}
