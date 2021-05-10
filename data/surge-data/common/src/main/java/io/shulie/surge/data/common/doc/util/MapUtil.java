package io.shulie.surge.data.common.doc.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import io.shulie.surge.data.common.doc.annotation.Id;
import io.shulie.surge.data.common.doc.model.Trace;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: xingchen
 * @ClassName: MapUtil
 * @Package: io.shulie.surge.data.common.elastic.util
 * @Date: 2020/11/2618:23
 * @Description:
 */
public class MapUtil {
    public static Map<String, Object> mapToObj(Object obj) throws Exception {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        Map<String, Object> objectMap = Maps.newLinkedHashMap();
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            io.shulie.surge.data.common.doc.annotation.Field annoField =
                    field.getAnnotation(io.shulie.surge.data.common.doc.annotation.Field.class);
            String key = field.getName();
            if (annoField != null && StringUtils.isNotBlank(annoField.name())) {
                key = annoField.name();
            }
            field.setAccessible(Boolean.TRUE);
            Object value = field.get(obj);
            if (!Objects.isNull(value)) {
                objectMap.put(key, value);
            }
        }
        return objectMap;
    }

    public static String resolveId(Object obj) throws Exception {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        String value = "";
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            Id annoField = field.getAnnotation(Id.class);
            if (annoField != null) {
                field.setAccessible(Boolean.TRUE);
                value = field.get(obj).toString();
                break;
            }
        }
        return value;
    }

    public static void main(String[] args) throws Exception {
        Trace trace = new Trace();
        trace.setAgentId("agentId");
        Map<String, Object> map = mapToObj(trace);

        System.out.println(JSON.toJSONString(map));
    }
}
