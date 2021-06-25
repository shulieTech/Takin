package io.shulie.tro.utils.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author shiyajian
 * create: 2020-10-20
 */
@Slf4j
public class JsonHelper {
    private static ObjectMapper mapper = new ObjectMapper();

    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static {
        //对象的所有字段全部列入
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认转换timestamps形式
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空Bean转json的错误
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        mapper.setDateFormat(new SimpleDateFormat(STANDARD_FORMAT));
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    /**
     * 对象转string
     * @param data
     * @return
     */
    public static String bean2Json(Object data) {
        if (data == null) {
            return null;
        }
        try {
            return data instanceof String ? (String) data : mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("Parse Object to String error : {}", e.getMessage());
            return null;
        }
    }

    /**
     * 对象转Json格式字符串(格式化的Json字符串)
     * @param obj 对象
     * @return 美化的Json格式字符串
     */
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Parse Object to String error : {}", e.getMessage());
            return null;
        }
    }

    /**
     * json 转bean
     * @param jsonData
     * @param beanType
     * @param <T>
     * @return
     */
    public static <T> T json2Bean(String jsonData, Class<T> beanType) {
        if(StringUtils.isEmpty(jsonData) || beanType == null){
            return null;
        }
        try {
            return beanType.equals(String.class) ? (T) jsonData : mapper.readValue(jsonData, beanType);
        } catch (Exception e) {
            log.error("Parse String to Object error : {}", e.getMessage());
            return null;
        }
    }

    /**
     * 集合对象 转benn
     * @param str
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? str : mapper.readValue(str, typeReference));
        } catch (IOException e) {
            log.error("Parse String to Object error", e);
            return null;
        }
    }

    /**
     * 自定义集合对象 转换 bean
     * @param str
     * @param collectionClazz
     * @param elementClazzes
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, Class<?> collectionClazz, Class<?>... elementClazzes) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClazz, elementClazzes);
        try {
            return mapper.readValue(str, javaType);
        } catch (IOException e) {
            log.error("Parse String to Object error : {}" + e.getMessage());
            return null;
        }
    }

    /**
     * string 转 list对象
     * @param jsonData
     * @param beanType
     * @param <T>
     * @return
     */
    public static <T> List<T> json2List(String jsonData, Class<T> beanType) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, beanType);

        try {
            List<T> resultList = mapper.readValue(jsonData, javaType);
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * string 转 map对象
     * @param jsonData
     * @param keyType
     * @param valueType
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> json2Map(String jsonData, Class<K> keyType, Class<V> valueType) {
        JavaType javaType = mapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);

        try {
            Map<K, V> resultMap = mapper.readValue(jsonData, javaType);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
