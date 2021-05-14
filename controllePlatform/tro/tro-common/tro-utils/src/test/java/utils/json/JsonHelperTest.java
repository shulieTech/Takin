package utils.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import io.shulie.tro.utils.json.JsonHelper;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 无涯
 * @Package utils.json
 * @date 2020/10/29 10:20 上午
 */
public class JsonHelperTest {
    @Test
    public void test () {
        // 举例
        //User user1 = new User();
        //user1.setId(1);
        //user1.setEmail("chenhaifei@163.com");
        //User user2 = new User();
        //user2.setId(2);
        //user2.setEmail("tianxiaorui@126.com");
        //List<User> userList = new ArrayList<>();
        //userList.add(user1);
        //userList.add(user2);
        //String userListJson = JsonHelper.obj2StringPretty(userList);
        //// 三种方式
        //List<User> userListBean = JsonHelper.string2Obj(userListJson, new TypeReference<List<User>>() {});
        //List<User> userListBean2 = JsonHelper.string2Obj(userListJson, List.class, User.class);
        //List<User> userListBean3 = JsonHelper.json2List(userListJson, User.class);
        String json = "{\"id\":{\"id\":1,\"email\":\"aa\"}}";
        Map<String,User>  userMap = JsonHelper.string2Obj(json, new TypeReference<Map<String,User>>(){});
        //Map<String,User>  userMap = JsonHelper.json2Map(json,String.class,User.class);
        System.out.println(userMap);
        ;

    }

    @Test
    public void testList(){
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        String json = JsonHelper.bean2Json(users);
        System.out.println(json);
        Assert.assertEquals(json,"[{\"id\":null,\"email\":null},{\"id\":null,\"email\":null}]");
        List<User> users1 = JsonHelper.json2List(json, User.class);
        Assert.assertEquals(users1.size(),2);
    }


    @Test
    public void testList2(){

        List<String> list01 = new ArrayList<>();
        list01.add("test");
        List<String> list02 = new ArrayList<>();
        list02.add("test2");
        List<List<String>> list = new ArrayList<>();
        list.add(list01);
        list.add(list02);

        String json = JsonHelper.bean2Json(list);
        System.out.println(json);

        List<List> jsonList = JsonHelper.json2List(json,List.class);
        System.out.println(jsonList);
    }

    @Data
    static class User {
        private Integer id;
        private String email;
    }
}
