package io.shulie.amdb.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import io.shulie.amdb.AMDBAPIBootstrap;
import io.shulie.amdb.BaseTest;
import io.shulie.amdb.common.dto.link.entrance.ServiceInfoDTO;
import io.shulie.amdb.entity.AppDO;
import io.shulie.amdb.entity.TAmdbAppInstanceDO;
import io.shulie.amdb.model.AmdbResult;
import io.shulie.amdb.response.instance.AmdbAppInstanceResponse;
import io.shulie.amdb.service.AppInstanceService;
import io.shulie.amdb.service.AppService;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration("src/main/resources")
@SpringBootTest(classes = AMDBAPIBootstrap.class)
public class AppInstanceControllerTest extends BaseTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    AppInstanceService appInstanceService;
    @Autowired
    AppService appService;

    private String baseUrl = "http://localhost:10032/amdb/db/api/appInstance";

    @Before
    public void setUp() throws Exception {
        //使用上下文构建mockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        //构建APP数据
        File file = new File(AppInstanceControllerTest.class.getResource("/").getPath() + "/mock/appData.json");
        List<AppDO> appDOList = JSONArray.parseArray(FileUtils.readFileToString(file), AppDO.class);
        for (AppDO appDO : appDOList) {
            appService.insert(appDO);
        }
        //构建实例数据
        file = new File(AppInstanceControllerTest.class.getResource("/").getPath() + "/mock/instanceData1.json");
        List<TAmdbAppInstanceDO> instanceDOList = JSONArray.parseArray(FileUtils.readFileToString(file), TAmdbAppInstanceDO.class);
        file = new File(AppInstanceControllerTest.class.getResource("/").getPath() + "/mock/instanceData2.json");
        instanceDOList.addAll(JSONArray.parseArray(FileUtils.readFileToString(file), TAmdbAppInstanceDO.class));
        for (TAmdbAppInstanceDO amdbAppInstanceDO : instanceDOList) {
            appInstanceService.insert(amdbAppInstanceDO);
        }

    }

    /**
     * 分页查询
     * 支持批量查询
     * 结果中包含分页信息
     * 查询结果只包含在线状态的实例
     *
     * @throws Exception
     */
    @Test
    public void selectByBatchAppParams() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(baseUrl + "/selectByBatchAppParams")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("appNames", "mock-test1,mock-test2")
                        .param("pageSize", "10").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //得到返回代码
        int status = mvcResult.getResponse().getStatus();
        //得到返回结果
        String content = mvcResult.getResponse().getContentAsString();
        AmdbResult<List<AmdbAppInstanceResponse>> result = JSON.parseObject(content,
                new TypeReference<AmdbResult<List<AmdbAppInstanceResponse>>>() {
                });
        //断言，判断返回代码是否正确
        Assert.assertEquals(200, status);
        //断言，返回结果状态成功
        Assert.assertEquals(true, result.getSuccess());
        //断言，判断返回数据条数是不是10条
        Assert.assertEquals(10, result.getData().size());
        //断言，total是不是100
        Assert.assertEquals(100, result.getTotal().longValue());
    }

    /**
     * 查询参数为空校验
     *
     * @throws Exception
     */
    @Test
    public void selectByBatchAppParams_emptyParam() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(baseUrl + "/selectByBatchAppParams")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("pageSize", "10").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //得到返回代码
        int status = mvcResult.getResponse().getStatus();
        //得到返回结果
        String content = mvcResult.getResponse().getContentAsString();
        AmdbResult result = JSON.parseObject(content, AmdbResult.class);
        //断言，判断返回代码是否正确
        Assert.assertEquals(200, status);
        //断言，返回结果状态失败
        Assert.assertEquals(false, result.getSuccess());
        //断言，返回错误信息
        Assert.assertNotNull(result.getError());
        //断言，参数校验不通过
        Assert.assertEquals(400, result.getError().getCode());
    }

    /**
     * 分页--分页从1开始
     *
     * @throws Exception
     */
    @Test
    public void selectByBatchAppParams_page() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(baseUrl + "/selectByBatchAppParams")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("appNames", "mock-test1,mock-test2")
                        .param("currentPage", "1")
                        .param("pageSize", "10").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //得到返回代码
        int status = mvcResult.getResponse().getStatus();
        //得到返回结果
        String content = mvcResult.getResponse().getContentAsString();
        AmdbResult<List<AmdbAppInstanceResponse>> result = JSON.parseObject(content,
                new TypeReference<AmdbResult<List<AmdbAppInstanceResponse>>>() {
                });
        //断言，判断返回代码是否正确
        Assert.assertEquals("分页从1开始-判断返回代码是否正确", 200, status);
        //断言，返回结果状态成功
        Assert.assertEquals("分页从1开始-返回结果状态成功", true, result.getSuccess());
        //断言，判断返回数据条数是不是10条
        Assert.assertEquals("分页从1开始-判断返回数据条数是不是10条", 10, result.getData().size());
        //断言，判断返回的数据是不是第1页
        Assert.assertEquals("分页从1开始-判断返回的数据是不是第1页", "1.1.1.1", result.getData().get(0).getAgentId());
    }

    /**
     * 分页--分页从0开始
     *
     * @throws Exception
     */
    @Test
    public void selectByBatchAppParams_page1() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(baseUrl + "/selectByBatchAppParams")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("appNames", "mock-test1,mock-test2")
                        .param("currentPage", "0")
                        .param("pageSize", "10").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //得到返回代码
        int status = mvcResult.getResponse().getStatus();
        //得到返回结果
        String content = mvcResult.getResponse().getContentAsString();
        AmdbResult<List<AmdbAppInstanceResponse>> result = JSON.parseObject(content,
                new TypeReference<AmdbResult<List<AmdbAppInstanceResponse>>>() {
                });
        //断言，判断返回代码是否正确
        Assert.assertEquals("分页从0开始-判断返回代码是否正确", 200, status);
        //断言，返回结果状态成功
        Assert.assertEquals("分页从0开始-返回结果状态成功", true, result.getSuccess());
        //断言，判断返回数据条数是不是10条
        Assert.assertEquals("分页从0开始-判断返回数据条数是不是10条", 10, result.getData().size());
        //断言，判断返回的数据是不是第1页
        Assert.assertEquals("分页从0开始-判断返回的数据是不是第1页", "1.1.1.1", result.getData().get(0).getAgentId());
    }

    /**
     * 分页--当前页小于0
     *
     * @throws Exception
     */
    @Test
    public void selectByBatchAppParams_page2() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(baseUrl + "/selectByBatchAppParams")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("appNames", "mock-test1,mock-test2")
                        .param("currentPage", "-1")
                        .param("pageSize", "10").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //得到返回代码
        int status = mvcResult.getResponse().getStatus();
        //得到返回结果
        String content = mvcResult.getResponse().getContentAsString();
        AmdbResult<List<AmdbAppInstanceResponse>> result = JSON.parseObject(content,
                new TypeReference<AmdbResult<List<AmdbAppInstanceResponse>>>() {
                });
        //断言，判断返回代码是否正确
        Assert.assertEquals("当前页小于0-判断返回代码是否正确", 200, status);
        //断言，返回结果状态成功
        Assert.assertEquals("当前页小于0-返回结果状态成功", true, result.getSuccess());
        //断言，判断返回数据条数是不是10条
        Assert.assertEquals("当前页小于0-判断返回数据条数是不是10条", 10, result.getData().size());
        //断言，判断返回的数据是不是第1页
        Assert.assertEquals("当前页小于0-判断返回的数据是不是第1页", "1.1.1.1", result.getData().get(0).getAgentId());
    }

    /**
     * 分页--当前页超出最大值
     *
     * @throws Exception
     */
    @Test
    public void selectByBatchAppParams_page3() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(baseUrl + "/selectByBatchAppParams")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("appNames", "mock-test1,mock-test2")
                        .param("currentPage", "99999999")
                        .param("pageSize", "10").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //得到返回代码
        int status = mvcResult.getResponse().getStatus();
        //得到返回结果
        String content = mvcResult.getResponse().getContentAsString();
        AmdbResult<List<AmdbAppInstanceResponse>> result = JSON.parseObject(content,
                new TypeReference<AmdbResult<List<AmdbAppInstanceResponse>>>() {
                });
        //断言，判断返回代码是否正确
        Assert.assertEquals("当前页超出最大值-判断返回代码是否正确", 200, status);
        //断言，返回结果状态成功
        Assert.assertEquals("当前页超出最大值-返回结果状态成功", true, result.getSuccess());
        //断言，判断返回数据条数是不是0条
        Assert.assertEquals("当前页超出最大值-判断返回数据条数是不是0条", 0, result.getData().size());

    }

    /**
     * 分页--分页大小小于0
     *
     * @throws Exception
     */
    @Test
    public void selectByBatchAppParams_page4() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(baseUrl + "/selectByBatchAppParams")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("appNames", "mock-test1,mock-test2")
                        .param("currentPage", "1")
                        .param("pageSize", "-1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //得到返回代码
        int status = mvcResult.getResponse().getStatus();
        //得到返回结果
        String content = mvcResult.getResponse().getContentAsString();
        AmdbResult<List<AmdbAppInstanceResponse>> result = JSON.parseObject(content,
                new TypeReference<AmdbResult<List<AmdbAppInstanceResponse>>>() {
                });
        //断言，判断返回代码是否正确
        Assert.assertEquals("分页大小小于0-判断返回代码是否正确", 200, status);
        //断言，返回结果状态成功
        Assert.assertEquals("分页大小小于0-返回结果状态成功", true, result.getSuccess());
        //断言，判断返回数据条数是不是0条
        Assert.assertEquals("分页大小小于0-判断返回数据条数是不是0条", 0, result.getData().size());
    }

    /**
     * 分页--分页大小等于0
     *
     * @throws Exception
     */
    @Test
    public void selectByBatchAppParams_page5() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(baseUrl + "/selectByBatchAppParams")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("appNames", "mock-test1,mock-test2")
                        .param("currentPage", "1")
                        .param("pageSize", "0").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //得到返回代码
        int status = mvcResult.getResponse().getStatus();
        //得到返回结果
        String content = mvcResult.getResponse().getContentAsString();
        AmdbResult<List<AmdbAppInstanceResponse>> result = JSON.parseObject(content,
                new TypeReference<AmdbResult<List<AmdbAppInstanceResponse>>>() {
                });
        //断言，判断返回代码是否正确
        Assert.assertEquals("分页大小等于0-判断返回代码是否正确", 200, status);
        //断言，返回结果状态成功
        Assert.assertEquals("分页大小等于0-返回结果状态成功", true, result.getSuccess());
        //断言，判断返回数据条数是不是0条
        Assert.assertEquals("分页大小等于0-判断返回数据条数是不是0条", 0, result.getData().size());
    }

    /**
     * 分页--分页大小超出最大值-分页大小超出最大值
     *
     * @throws Exception
     */
    @Test
    public void selectByBatchAppParams_page6() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(baseUrl + "/selectByBatchAppParams")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("appNames", "mock-test1,mock-test2")
                        .param("currentPage", "1")
                        .param("pageSize", "9999999").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //得到返回代码
        int status = mvcResult.getResponse().getStatus();
        //得到返回结果
        String content = mvcResult.getResponse().getContentAsString();
        AmdbResult<List<AmdbAppInstanceResponse>> result = JSON.parseObject(content,
                new TypeReference<AmdbResult<List<AmdbAppInstanceResponse>>>() {
                });
        //断言，判断返回代码是否正确
        Assert.assertEquals("分页大小超出最大值-判断返回代码是否正确", 200, status);
        //断言，返回结果状态成功
        Assert.assertEquals("分页大小超出最大值-返回结果状态成功", true, result.getSuccess());
        //断言，判断返回数据条数是不是100条
        Assert.assertEquals("分页大小超出最大值-判断返回数据条数是不是100条", 100, result.getData().size());
    }


}