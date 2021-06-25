///*
// * Copyright 2021 Shulie Technology, Co.Ltd
// * Email: shulie@shulie.io
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package io.shulie.tro.web.app.fastdebug;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.pamirs.pradar.log.parser.trace.RpcStack;
//import com.pamirs.tro.entity.domain.entity.user.User;
//import io.shulie.tro.utils.file.FileManagerHelper;
//import io.shulie.tro.utils.json.JsonHelper;
//import io.shulie.tro.web.app.service.fastdebug.FastDebugService;
//import io.shulie.tro.web.app.service.fastdebug.FastDebugServiceImpl;
//import io.shulie.tro.web.common.enums.fastdebug.FastDebugEnum;
//import io.shulie.tro.web.common.enums.fastdebug.FastDebugResultEnum;
//import io.shulie.tro.web.common.vo.fastdebug.ContentTypeVO;
//import io.shulie.tro.web.common.vo.fastdebug.FastDebugCallStackExceptionVO;
//import io.shulie.tro.web.common.vo.fastdebug.FastDebugCallStackVO;
//import io.shulie.tro.web.common.vo.fastdebug.TraceNodeVO;
//import io.shulie.tro.web.data.param.fastdebug.FastDebugResultParam;
//import io.shulie.tro.web.data.result.fastdebug.FastDebugConfigResult;
//import io.shulie.tro.web.data.result.fastdebug.FastDebugResult;
//import io.shulie.tro.web.data.result.linkmange.BusinessLinkResult;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.http.HttpHeaders;
//
///**
// * @author 无涯
// * @Package io.shulie.tro.web.app.fastdebug
// * @date 2021/1/7 3:29 下午
// */
//public class FastDebugTest {
//    @Test
//    public void sendHttpTest() {
//        FastDebugServiceImpl impl = new FastDebugServiceImpl();
//        FastDebugConfigResult result = new FastDebugConfigResult();
//        result.setName("test");
//        result.setId(1L);
//        result.setRequestUrl("http://192.168.1.117:8091/rds-web/fastdebug/test");
//        result.setCustomerId(1L);
//        // 随便写
//        result.setBusinessLinkId(11L);
//        result.setBusinessLinkId(11L);
//        result.setHttpMethod("PUT");
//        result.setHeaders("token:shulie");
//        result.setBody("{\"id\":12}");
//        result.setIsRedirect(false);
//        ContentTypeVO contentTypeVo  = new ContentTypeVO();
//        contentTypeVo.setCodingFormat("GBK");
//        contentTypeVo.setRadio(0);
//        result.setTimeout(5000);
//        result.setContentTypeVo(contentTypeVo);
//        BusinessLinkResult businessLinkResult = new BusinessLinkResult();
//        businessLinkResult.setLinkName("zz");
//        FastDebugResult debugResult = new FastDebugResult();
//        impl.sendHttp(result,debugResult,1L);
//        Assert.assertEquals(true,true);
//    }
//
//    @Test
//    public void watchDebugTest() {
//        FastDebugResult result = new FastDebugResult();
//        FastDebugServiceImpl impl = new FastDebugServiceImpl();
//        //impl.watchDebug(result,1L，new );
//        Map<String,String> map = Maps.newHashMap();
//        map.put("error","调用栈异常:agent上报节点数和调用栈统计Rpc节点不一致,配置异常2个,");
//        Map<String, List<String>> mapResult = Maps.newHashMap();
//        List<String> errors = Lists.newArrayList();
//        errors.add("0.1.2");
//        errors.add("0.1.3");
//        List<String> successful = Lists.newArrayList();
//        successful.add("0.1.4");
//        mapResult.put("error",errors);
//        mapResult.put("success",successful);
//        map.put("callStack", JsonHelper.bean2Json(mapResult));
//        System.out.println(JsonHelper.bean2Json(map));
//        Assert.assertEquals(true,true);
//    }
//
//
//    @Test
//    public void getTraceNode() {
//        FastDebugServiceImpl impl = new FastDebugServiceImpl();
//        String str = "0.1|1|test|1994-12-31";
//        TraceNodeVO vo = impl.getTraceNode(str);
//        Assert.assertEquals("0.1".equals(vo.getRpcId()) && "1".equals(vo.getLogType())
//            && "test".equals(vo.getAppName()) && "1994-12-31".equals(vo.getAgentId()),
//            true);
//    }
//
//    @Test
//    public void buildHeaderTest() {
//        FastDebugServiceImpl impl = new FastDebugServiceImpl();
//        String cookies = "Cookie:aa" +  "\n"
//            + "Cookie:cc";
//        String headers = "aa:bb"+ "\n" + "cc:dd";
//        ContentTypeVO vo = new ContentTypeVO();
//        HttpHeaders httpHeaders =  impl.buildHeader(cookies,headers,vo);
//        System.out.println(JsonHelper.bean2Json(httpHeaders));
//
//    }
//
//    @Test
//    public void test(){
//        StringBuilder builder = new StringBuilder();
//        builder.append("aa");
//        String s = null;
//        builder.append(s);
//
//        System.out.println(builder.toString());
//    }
//
//    @Test
//    public void test1() {
//        User user = null;
//        FastDebugResultParam param = new FastDebugResultParam();
//        param.setCreatorId(Optional.ofNullable(user).orElseGet(User::new).getId());
//        System.out.println(JsonHelper.bean2Json(param));
//        FastDebugResult result = new FastDebugResult();
//        result.setDebugResult(false);
//        String  desc = Optional.ofNullable(result.getDebugResult()).map(FastDebugResultEnum::getDescByStatus)
//            .orElse(FastDebugEnum.STARTING.getDesc());
//        System.out.println(desc);
//    }
//
//    @Test
//    public void getCallStack() throws IOException {
//        // 获取
//        FastDebugServiceImpl impl = new FastDebugServiceImpl();
//        Integer id = 0;
//        String path = "/Users/hezhongqi/Downloads/f277632f16141699155774934d0001_1.log";
//        String str = FileManagerHelper.readFileToString(new File(path),"UTF-8");
//        RpcStack rpcStack = JsonHelper.json2Bean(str,RpcStack.class);
//        List<String> errorStack = Lists.newArrayList();
//        // appName + '|' + agentId + '|' +  rpcId  + '|' + type ;
//        errorStack.add("quick_dms_mall|172.18.0.11-1|0|1");
//        errorStack.add("quick_partner_center|172.18.0.19-1|0.5.1|0");
//
//        List<String> noKnownNodes = Lists.newArrayList();
//        // appName + '|' + agentId + '|' +  rpcId  + '|' + type ;
//        noKnownNodes.add("quick_dms_mall|172.18.0.11-1|0.1|0");
//        noKnownNodes.add("quick_partner_center|172.18.0.19-1|0.5.2|0");
//
//        List<FastDebugCallStackVO> vos =  impl.getFastDebugCallStackVOS(id,rpcStack,errorStack,noKnownNodes);
//        System.out.println(vos.size());
//    }
//
//}
