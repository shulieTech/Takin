package utils.http;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.utils.http.HttpHelper;
import io.shulie.tro.utils.http.TroResponseEntity;
import io.shulie.tro.utils.json.JsonHelper;
import lombok.Data;
import org.junit.Test;

/**
 * @author 何仲奇
 * @Package utils.http
 * @date 2020/10/20 4:18 下午
 */
public class HttpHelperTest {

    @Test
    public void test() {
        String body = "{\"data\":{\"dubbo\":[{\"pluginId\":1,\"pluginName\":\"dubbo-all\",\"pluginType\":\"dubbo\",\"gmtUpdate\":\"2021-01-06 14:43:58\"}]},\"success\":true}";
        System.out.println(JsonHelper.bean2Json(JsonHelper.string2Obj(body, new TypeReference<ResponseResult<Map<String, List<EnginePluginSimpleInfoResp>>>>() {
        })));
        //String str = "{\"error\":null,\"data\":{\"id\":160,\"customId\":9725,"
        //    + "\"pressureTestSceneName\":\"脚本被压次数top统计01\",\"businessActivityConfig\":[{\"businessActivityId\":87,"
        //    + "\"businessActivityName\":\"业务活动05\",\"targetTPS\":1,\"targetRT\":100,\"targetSuccessRate\":50,"
        //    + "\"targetSA\":70,\"bindRef\":\"HTTP请求\",\"applicationIds\":\"6730304800056938496\",\"id\":1531,"
        //    + "\"scriptId\":null,\"businessFlowId\":null,\"hasPT\":null}],\"concurrenceNum\":1,\"ipNum\":1,"
        //    + "\"pressureTestSecond\":60,\"pressureTestTime\":{\"time\":1,\"unit\":\"m\"},\"pressureMode\":1,"
        //    + "\"increasingSecond\":0,\"increasingTime\":{\"time\":null,\"unit\":null},\"step\":null,"
        //    + "\"estimateFlow\":1.00,\"scriptType\":0,\"uploadFile\":[{\"id\":1129,"
        //    + "\"fileName\":\"oo9999999999999999999999999999 99999999.jmx\",\"fileType\":0,\"fileSize\":null,"
        //    + "\"uploadTime\":\"2020-12-14 19:47:13\",\"uploadPath\":\"160/oo9999999999999999999999999999 99999999"
        //    + ".jmx\",\"isDeleted\":0,\"uploadedData\":null,\"isSplit\":0,\"topic\":null}],"
        //    + "\"stopCondition\":[{\"id\":2468,\"ruleName\":\"1\",\"businessActivity\":[\"87\"],"
        //    + "\"rule\":{\"indexInfo\":1,\"condition\":0,\"during\":900,\"times\":1},\"status\":0,"
        //    + "\"event\":\"destory\"}],\"warningCondition\":[{\"id\":2469,\"ruleName\":\"2\","
        //    + "\"businessActivity\":[\"87\"],\"rule\":{\"indexInfo\":2,\"condition\":4,\"during\":0,\"times\":1},"
        //    + "\"status\":0,\"event\":\"warn\"}],\"status\":0,\"totalTestTime\":60,\"updateTime\":\"2020-12-14 "
        //    + "19:57:10\",\"lastPtTime\":\"2020-12-14 19:57:10\",\"features\":\"{\\\"scriptId\\\":560,"
        //    + "\\\"configType\\\":1}\",\"configType\":null,\"scriptId\":null,\"executeTime\":null,\"tag\":null,"
        //    + "\"isScheduler\":null,\"businessFlowId\":null},\"totalNum\":null,\"success\":true}";
        //ResponseResult<SceneManageWrapperResp> respResponseResult =  JsonHelper.string2Obj(str,new
        // TypeReference<ResponseResult<SceneManageWrapperResp>>() {});
        //System.out.println(respResponseResult);
        //List<SceneBusinessActivityRefResp> businessActivityConfig = Lists.newArrayList();
        //SceneBusinessActivityRefResp refResp = new SceneBusinessActivityRefResp();
        //refResp.setApplicationIds("aaaa");
        //businessActivityConfig.add(refResp);
        //SceneManageWrapperResp resp = new SceneManageWrapperResp();
        //resp.setBusinessActivityConfig(businessActivityConfig);
        //ResponseResult<SceneManageWrapperResp> result = ResponseResult.success(resp);
        //String temp = JsonHelper.bean2Json(result);
        //System.out.println(temp);
        //
        //String ss = "{\"error\":null,\"data\":{\"id\":null,\"customId\":null,\"pressureTestSceneName\":null,"
        //    + "\"businessActivityConfig\":[{\"id\":null,\"bindRef\":null,\"applicationIds\":\"aaaa\","
        //    + "\"scriptId\":null}],\"concurrenceNum\":null,\"ipNum\":null,\"pressureTestSecond\":null,"
        //    + "\"pressureMode\":null,\"increasingSecond\":null,\"step\":null,\"estimateFlow\":null,"
        //    + "\"scriptType\":null,\"uploadFile\":null,\"stopCondition\":null,\"warningCondition\":null,"
        //    + "\"status\":null,\"totalTestTime\":null,\"updateTime\":null,\"lastPtTime\":null,\"features\":null,"
        //    + "\"configType\":null,\"scriptId\":null,\"executeTime\":null,\"tag\":null,\"isScheduler\":null,"
        //    + "\"businessFlowId\":null},\"totalNum\":null,\"success\":true}";
        //
        //ResponseResult<SceneManageWrapperResp> test =  JsonHelper.string2Obj(str,new
        // TypeReference<ResponseResult<SceneManageWrapperResp>>() {});

    }

    @Test
    public void HttpTest() {
        //HttpHelper.doPost("http://127.0.0.1:8080/hessian/insertUser", null, String.class, "");
        //TroResponseEntity<String> a = HttpHelper.doGet("http://127.0.0.1:8080/hessian/insertUser", String.class);
        //System.out.println("111" + a.getBody());

        Map params = Maps.newHashMap();
        params.put("pluginTypes", Lists.newArrayList("dubbo"));
        TroResponseEntity<ResponseResult<Map<String, List<EnginePluginSimpleInfoResp>>>> result = HttpHelper.doPost(
                "http://192.168.100.135:10010/tro-cloud/open-api/engine/fetchAvailableEnginePlugins",
                getHeaders("5b06060a-17cb-4588-bb71-edd7f65035af"),
                new TypeReference<ResponseResult<Map<String, List<EnginePluginSimpleInfoResp>>>>() {
                }, params);
        result.getBody().getData().get("dubbo").forEach(item -> System.out.printf(item.getPluginName()));

//        TroResponseEntity<ResponseResult<SceneManageWrapperResp>> list = HttpHelper.doGet(
//            "http://localhost:10010/tro-cloud/open-api/scenemanage/detail/1",
//            getHeaders("5b06060a-17cb-4588-bb71-edd7f65035af"),
//            new TypeReference<ResponseResult<SceneManageWrapperResp>>() {});
//
//        System.out.println(list);
        //TroResponseEntity<List<ApplicationInterfaceDTO>> list1 = HttpHelper.doGet(
        //    "http://192.168.1.101:8090/WhiteList",
        //    null, new TypeReference<List<ApplicationInterfaceDTO>>() {});
        //System.out.println(list1);
    }

    private Map<String, String> getHeaders(String license) {
        Map<String, String> map = Maps.newHashMap();
        map.put("t", "5b06060a-17cb-4588-bb71-edd7f65035aa");
        map.put("licenseKey", license);
        return map;
    }

    //@Test
    //public void HttpTest() {
    //    //HttpHelper.doJsonPost("http://127.0.0.1:8080/hessian/insertUser",null,String.class,"");
    //    //HttpRequest httpRequest = new HttpRequest();
    //    //String a =HttpHelper.concatGetUrl("http://127.0.0.1:8080/hessian/insertUser",httpRequest);
    //    //System.out.println(a);
    //    ResponseResult<Object> responseResult = HttpHelper.doGet("http://127.0.0
    //    .1:10010/tro-cloud/api/scenemanage/ipnum/{id}",
    //        getHeaders("5b06060a-17cb-4588-bb71-edd7f65035af"), ResponseResult.class,
    //        2);
    //    System.out.println(responseResult);
    //}
    //
    //private Map<String, String> getHeaders(String license) {
    //    Map<String, String> map = Maps.newHashMap();
    //    map.put(HttpRequest.LICENSE_REQUIRED, "false");
    //    map.put(HttpRequest.LICENSE_KEY, license);
    //    return map;
    //}
    @Data
    public static class SceneManageWrapperResp implements Serializable {

        private static final long serialVersionUID = 7324148443733465383L;

        private Long id;

        private Long customId;

        private String pressureTestSceneName;

        private List<SceneBusinessActivityRefResp> businessActivityConfig;

        private Integer concurrenceNum;

        private Integer ipNum;

        private Long pressureTestSecond;

        private Integer pressureMode;

        private Long increasingSecond;

        private Integer step;

        private BigDecimal estimateFlow;

        private Integer scriptType;

        private List<SceneScriptRefResp> uploadFile;

        private List<SceneSlaRefResp> stopCondition;

        private List<SceneSlaRefResp> warningCondition;

        private Integer status;

        private transient Long totalTestTime;

        private transient String updateTime;

        private transient String lastPtTime;

        private String features;

        private Integer configType;

        private Long scriptId;

        private String BusinessFlowId;

        private String executeTime;

        private List<String> tag;

        private Boolean isScheduler;

    }

    @Data
    public static class SceneSlaRefResp implements Serializable {

        private static final long serialVersionUID = 5117439939447730586L;

        private Long id;

        private String ruleName;

        private String[] businessActivity;

        private Integer status;

        private String event;
    }

    @Data
    public static class SceneScriptRefResp implements Serializable {

        private static final long serialVersionUID = -1038145286303661484L;

        private Long id;

        private String fileName;

        private Integer fileType;

        private String fileSize;

        private String uploadTime;

        private String uploadPath;

        private Integer isDeleted;

        private Long uploadedData;

        private Integer isSplit;

        private String topic;
    }

    @Data
    public static class SceneBusinessActivityRefResp {

        private static final long serialVersionUID = -6384484202725660595L;

        private Long id;

        private String bindRef;

        private String applicationIds;

        private Long scriptId;

    }

    @Data
    public static class ApplicationInterfaceDTO implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 接口ID
         */
        private String id;

        /**
         * 接口类型
         */
        private String interfaceType;

        /**
         * 接口名称
         */
        private String interfaceName;

    }

    @Data
    public static class EnginePluginSimpleInfoResp implements Serializable {

        private Long pluginId;

        private String pluginName;

        private String pluginType;

        private String gmtUpdate;

    }

    @Data
    public static class EnginePluginDetailResp implements Serializable {

        private Long pluginId;

        private String pluginType;

        private String pluginName;

        private List<String> supportedVersions;

        private List<EnginePluginFileResp> uploadFiles;

    }

    @Data
    public static class EnginePluginFileResp implements Serializable {

        private Long fileId;

        private String fileName;

        private String filePath;

        private Integer isDeleted;
    }
}
