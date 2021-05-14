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

package io.shulie.tro.web.auth.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.pamirs.tro.common.redis.RedisManager;
import com.pamirs.tro.entity.dao.auth.TAuthorityMapper;
import com.pamirs.tro.entity.dao.auth.TBaseRoleMapper;
import com.pamirs.tro.entity.dao.auth.TResourceMapper;
import com.pamirs.tro.entity.domain.entity.auth.Authority;
import com.pamirs.tro.entity.domain.entity.auth.AuthorityExample;
import com.pamirs.tro.entity.domain.entity.auth.TreeConvertUtil;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.vo.user.UserLoginParam;
import io.shulie.tro.web.auth.api.AuthService;
import io.shulie.tro.web.auth.api.ResourceService;
import io.shulie.tro.web.auth.api.RoleService;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.shulie.tro.web.auth.api.exception.TroLoginException;
import io.shulie.tro.web.auth.common.YundaBaseInfo;
import io.shulie.tro.web.auth.common.YundaProperties;
import io.shulie.tro.web.auth.utils.HttpClientUtils;
import io.shulie.tro.web.data.dao.user.TroDeptUserRelationDAO;
import io.shulie.tro.web.data.dao.user.TroResourceDAO;
import io.shulie.tro.web.data.dao.user.TroRoleResourceAuthDAO;
import io.shulie.tro.web.data.dao.user.TroRoleUserRelationDAO;
import io.shulie.tro.web.data.dao.user.TroUserDAO;
import io.shulie.tro.web.data.param.user.ResourceMenuQueryParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthDeleteParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthQueryParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthUpdateParam;
import io.shulie.tro.web.data.param.user.UserDeptConditionQueryParam;
import io.shulie.tro.web.data.param.user.UserDeptQueryParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationBatchQueryParam;
import io.shulie.tro.web.data.result.user.ResourceAuthResult;
import io.shulie.tro.web.data.result.user.ResourceMenuResult;
import io.shulie.tro.web.data.result.user.UserDeptConditionResult;
import io.shulie.tro.web.data.result.user.UserDeptResult;
import io.shulie.tro.web.data.result.user.UserDetailResult;
import io.shulie.tro.web.data.result.user.UserRoleRelationResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-09-01
 */
@Component
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    TAuthorityMapper TAuthorityMapper;

    @Autowired
    RoleService roleService;

    @Autowired
    TBaseRoleMapper TBaseRoleMapper;

    @Autowired
    ResourceService resourceService;

    @Autowired
    RedisManager redisManager;

    @Autowired
    TResourceMapper TResourceMapper;

    @Autowired
    private TroResourceDAO troResourceDAO;

    @Autowired
    private TroRoleResourceAuthDAO troRoleResourceAuthDAO;

    @Autowired
    private TroRoleUserRelationDAO troRoleUserRelationDAO;

    @Autowired
    private TroDeptUserRelationDAO troDeptUserRelationDAO;

    @Autowired
    private TroUserDAO troUserDAO;

    @Autowired
    private RedisTemplate redisTemplate;

    private String yundaLoginUser = "YUNDA_LOGIN_USER";

    @Autowired
    private YundaProperties yundaProperties;

    @Value("${yunda.admin.id}")
    private Long adminId;

    @Override
    public User login(HttpServletRequest request, UserLoginParam userLoginParam) throws TroLoginException {
        User user = this.check(request, null);
        if (user == null) {
            throw new TroLoginException("用户不存在");
        }
        return user;
    }

    /**
     * @Description: 根据map的key排序
     * @Author danrenying
     * @Param: [map, isDesc: true：降序，false：升序]
     * @Return: java.util.Map<K, V>
     * @Date 2020/7/13
     */
    public static <K extends Comparable<? super K>, V> Map<K, V> orderByKey(Map<K, V> map, boolean isDesc) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<K, V> result = Maps.newLinkedHashMap();
        if (isDesc) {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey().reversed())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        } else {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }

    @Override
    public void loginSuccess(User user) {

    }

    @Override
    public List<Authority> getUserAuth(Integer type, List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        AuthorityExample example = new AuthorityExample();
        AuthorityExample.Criteria criteria = example.createCriteria();
        criteria.andObjectTypeEqualTo(type);
        criteria.andObjectIdIn(ids);
        return TAuthorityMapper.selectByExample(example);
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String cookie = request.getHeader("Cookie");
        if (StringUtils.isEmpty(cookie) || !cookie.startsWith("SESSION") || !checkYundaCookie(cookie)) {
            cookie = getYundaCookie(request, response);
            log.info("logout->>cookie:{}", cookie);
        }
        redisTemplate.delete(yundaLoginUser + cookie);
        request.getSession().invalidate();
        Cookie emptyCookie = new Cookie("SESSION", "");
        emptyCookie.setMaxAge(0);//设置存活时间，“0”即马上消失
        emptyCookie.setPath("/");
        response.addCookie(emptyCookie);
        if (StringUtils.isNotBlank(cookie) && cookie.startsWith("SESSION") && checkYundaCookie(cookie)) {
            String result = HttpClientUtils.getInstance().sendHttpGetByCookie(YundaBaseInfo.YUNDA_INNER_URL + YundaBaseInfo.YUNDA_LOGOUT_REDIRECT_URL, cookie);
            log.info("logout->>>清除韵达服务cookie，result：{}", result);
        }
        String indexUrl = YundaBaseInfo.YUNDA_LOGOUT_URL;
        log.info("韵达登出url：{}", indexUrl);
        return indexUrl;
    }

    @Override
    public void redirect(String redirectUrl, HttpServletRequest request, HttpServletResponse response) {
        String indexUrl = YundaBaseInfo.YUNDA_REDIRECT_URL;
        if (StringUtils.isNotBlank(redirectUrl)) {
            indexUrl = redirectUrl;
        }
        // 接口有携带SESSION参数，重定向至tro-web首页
        try {
            log.info("重定向url：{}", indexUrl);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.sendRedirect(indexUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getYundaCookie(HttpServletRequest request, HttpServletResponse response) {
        String cookie = "";
        Cookie[] cookies = request.getCookies();
        log.info("getYundaCookie->>requestCookies：{}", JSON.toJSONString(cookies));
        if (cookies != null) {
            for (Cookie cookie2 : cookies) {
                if ("SESSION".equals(cookie2.getName())) {
                    cookie = cookie2.getName() + "=" + cookie2.getValue();
                    if (checkYundaCookie(cookie)) {
                        return cookie;
                    }
                    continue;
                }
            }
        }
        //把url里SESSION参数值设置为cookie
        if ((StringUtils.isBlank(cookie) || !cookie.startsWith("SESSION")) && StringUtils.isNotBlank(request.getParameter("SESSION"))) {
            log.error("check->>Cookie为空！！cookie:{}", cookie);
            cookie = getParameterCookie(request, response);
        }
        return cookie;
    }


    /**
     * 韵达cookie是否有效
     */
    private boolean checkYundaCookie(String cookie) {
        JSONObject jsonObject = getByYundaCookie(cookie);
        if (jsonObject != null && StringUtils.isNotBlank(jsonObject.getString("data"))) {
            return true;
        }
        return false;
    }

    @Override
    public User check(HttpServletRequest request, HttpServletResponse response) {
        //获取韵达cookie
        String cookie = getYundaCookie(request, response);

        //第一次查询，根据cookie
        User user = getUserBySession(cookie);
        if (user != null) {
            return user;
        }
        //给韵达提供的重定向接口
        if (YundaBaseInfo.YUNDA_REDIRECT_URI.equalsIgnoreCase(request.getRequestURI())) {
            String indexUrl = YundaBaseInfo.YUNDA_LOGOUT_URL;
            //redirectUrl有值，拼接到login接口后面
            if (StringUtils.isNotBlank(request.getParameter("redirectUrl"))) {
                Map<String, Object> param = Maps.newHashMap();
                param.put("redirectUrl", request.getParameter("redirectUrl"));
                appendUrl(indexUrl, param);
            }
            try {
                response.sendRedirect(indexUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String appendUrl(String url, Map<String, Object> data) {
        String newUrl = url;
        StringBuffer param = new StringBuffer();
        for (String key : data.keySet()) {
            param.append(key + "=" + data.get(key).toString() + "&");
        }
        String paramStr = param.toString();
        paramStr = paramStr.substring(0, paramStr.length() - 1);
        if (newUrl.indexOf("?") >= 0) {
            newUrl += "&" + paramStr;
        } else {
            newUrl += "?" + paramStr;
        }
        return newUrl;
    }

    private String getParameterCookie(HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(request.getParameter("SESSION"))) {
            return "";
        }
        String cookie = "SESSION=" + request.getParameter("SESSION");
        log.info("韵达登录getParameterCookie，cookie：{}", cookie);
        if (checkYundaCookie(cookie)) {
            Cookie c = new Cookie("SESSION", request.getParameter("SESSION"));
            c.setPath("/");
            //把SESSION参数赋值给Cookie
            response.addCookie(c);
            return cookie;
        }
        return "";
    }

    @Override
    public boolean hasPermissionUrl(User user, String url) {
        if (user.getUserType() == 0) {
            return true;
        }
        String checkUrl = url.substring(url.indexOf("/api"));
        if (checkUrl.contains("?")) {
            checkUrl = checkUrl.substring(0, checkUrl.indexOf("?") - 1);
        }
        List<String> permissionUrlList = user.getPermissionUrl();
        if (CollectionUtils.isEmpty(permissionUrlList)) {
            return false;
        }
        if (permissionUrlList.contains(checkUrl)) {
            return true;
        }
        return false;
    }


    private JSONObject getByYundaCookie(String cookie) {
        JSONObject resultJsonObject;
        long timeMillis = System.currentTimeMillis();
        //{"orgName":"自动化测试组","orgCode":"63000195","userRole":"admin","userName":"马永飞","userId":"98691767"}
        String url = YundaBaseInfo.YUNDA_INNER_URL + YundaBaseInfo.YUNDA_CHECK_SESSION;
        log.info("getUserBySession->>调用韵达接口！！url:{}!!!cookie:{}:", url, cookie);
        String result = HttpClientUtils.getInstance().sendHttpGetByCookie(url, cookie);

        log.info("根据session获取用户信息消耗时间{}毫秒", System.currentTimeMillis() - timeMillis);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        try {
            resultJsonObject = JSON.parseObject(result);
        } catch (Exception e) {
            log.error("韵达用户信息json转换异常！！Cookie:{}", cookie);
            return null;
        }
        log.info("根据session获取用户信息result:{}", resultJsonObject);
        Long code = Optional.ofNullable(resultJsonObject).map(resultJson -> resultJson.getLong("code")).orElse(0L);
        String data = Optional.ofNullable(resultJsonObject).map(resultJson -> resultJson.getString("data")).orElse("");
        if (resultJsonObject == null || code != 200 || StringUtils.isBlank(data)) {
            return null;
        }
        return resultJsonObject;
    }

    private User getUserBySession(String cookie) {
        if (StringUtils.isEmpty(cookie) || "null".equalsIgnoreCase(cookie) || !cookie.startsWith("SESSION")) {
            log.error("getUserBySession->>Cookie不可用！！cookie:{}", cookie);
            return null;
        }
        //1、去redis中取用户信息，用户存在 return true
        //2、redis中没有用户信息，调用韵达getUserInfo接口
        //3、返回用户信息成功，用户信息存入redis， return true
        //4、返回用户信息失败，重定向至韵达魔盒登录页面
        User redisUser = (User) redisTemplate.opsForValue().get(yundaLoginUser + cookie);
        if (redisUser != null) {
            return redisUser;
        }
        //获取用户信息
        JSONObject resultJsonObject = getByYundaCookie(cookie);
        if (resultJsonObject == null) {
            return null;
        }
        Long userId = Optional.ofNullable(resultJsonObject)
                .map(result -> result.getJSONObject("data"))
                .map(data -> data.getLong("userId"))
                .orElse(null);

        if (userId == null) {
            return null;
        }

        if (adminId.equals(userId)) {
            userId = 1L;
        }
        User user = new User();
        user.setId(userId);
        //查询用户基础信息
        user = assembleBasicInfo(user);
        //查询用户部门信息
        user = assembleDeptInfo(user);
        //查询用户权限信息
        user = assembleUserAuth(user);
        redisTemplate.opsForValue().set(yundaLoginUser + cookie, user, 3, TimeUnit.MINUTES);
        return user;
    }

    public User assembleBasicInfo(User user) {
        UserDetailResult userDetailResult = troUserDAO.selectUserById(user.getId());
        user.setId(userDetailResult.getId());
        user.setUserType(userDetailResult.getUserType());
        user.setName(userDetailResult.getName());
        user.setKey(userDetailResult.getKey());
        UserDetailResult adminUser = troUserDAO.selectUserById(userDetailResult.getCustomerId());
        user.setCustomerId(adminUser.getId());
        user.setCustomerKey(adminUser.getKey());
        return user;
    }

    public User assembleDeptInfo(User user) {
        UserDeptQueryParam userDeptQueryParam = new UserDeptQueryParam();
        userDeptQueryParam.setUserIdList(Arrays.asList(String.valueOf(user.getId())));
        List<UserDeptResult> userDeptResultList = selectList(userDeptQueryParam);
        List<UserDeptResult> userDeptResults = Optional.ofNullable(userDeptResultList).orElse(Lists.newArrayList());
        if (CollectionUtils.isNotEmpty(userDeptResults)) {
            user.setDeptList(userDeptResults.get(0).getDeptList());
        }
        return user;
    }

    public User assembleUserAuth(User user) {
        //设置用户菜单url，用户菜单权限验证
        user = getUserMenuResource(user);
        //设置用户菜单权限，前端渲染菜单用
        user = getUserMenu(user);
        //设置用户操作权限，前端渲染按钮用
        user = getUserAction(user);
        //设置用户数据权限，数据隔离用
        user = getUserData(user);
        return user;
    }

    public User getUserMenuResource(User user) {
        if (Objects.isNull(user)) {
            return user;
        }
        List<ResourceMenuResult> resourceList;
        if (user.getUserType() == 0) {
            resourceList = selectMenu(new ResourceMenuQueryParam());
        } else {
            List<ResourceAuthResult> authResultList = getUserAuth(String.valueOf(user.getId()));
            if (CollectionUtils.isEmpty(authResultList)) {
                return user;
            }
            List<Long> resourceIdList = authResultList
                    .stream()
                    .map(ResourceAuthResult::getResoureId)
                    .collect(Collectors.toList());
            ResourceMenuQueryParam resourceMenuQueryParam = new ResourceMenuQueryParam();
            resourceMenuQueryParam.setResourceIdList(resourceIdList);
            resourceList = selectMenu(resourceMenuQueryParam);
        }
        if (CollectionUtils.isEmpty(resourceList)) {
            log.info("用户暂未授权，请联系管理员：" + user.getName());
            return user;
        }
        Set<String> urlSet = Sets.newHashSet();
        for (ResourceMenuResult menuResult : resourceList) {
            if (StringUtils.isNotBlank(menuResult.getValue())) {
                String urlStr = menuResult.getValue();
                JSONArray array = JSON.parseArray(urlStr);
                for (Object object : array) {
                    urlSet.add(object.toString());
                }
            }
        }
        user.setPermissionUrl(new ArrayList<>(urlSet));
        return user;
    }

    public User getUserMenu(User user) {
        Map<String, Boolean> userResourceMap = Maps.newLinkedHashMap();
        List<ResourceMenuResult> resourceList;
        if (user.getUserType() == 0) {
            //系统管理员
            resourceList = selectMenu(new ResourceMenuQueryParam());
        } else {
            //普通用户
            List<ResourceMenuResult> allResourceList = selectMenu(new ResourceMenuQueryParam());
            List<ResourceAuthResult> authResultList = getUserAuth(String.valueOf(user.getId()));
            if (CollectionUtils.isEmpty(authResultList)) {
                log.error("获取用户菜单失败：该用户下暂无资源信息");
                return user;
            }
            List<Long> resourceIdList = authResultList
                    .stream()
                    .map(ResourceAuthResult::getResoureId)
                    .collect(Collectors.toList());
            ResourceMenuQueryParam resourceMenuQueryParam = new ResourceMenuQueryParam();
            resourceMenuQueryParam.setResourceIdList(resourceIdList);
            resourceList = selectMenu(resourceMenuQueryParam);
            if (CollectionUtils.isEmpty(resourceList)) {
                log.error("获取用户菜单失败：未找到资源信息");
                return user;
            }
            TreeConvertUtil.getParentResourceByResourceIdList(allResourceList, resourceList);
        }
        resourceList = resourceList.stream().sorted(Comparator.comparing(ResourceMenuResult::getSequence)).collect(
                Collectors.toList());
        for (ResourceMenuResult menuResult : resourceList) {
            userResourceMap.put(menuResult.getCode(), Boolean.TRUE);
        }
        user.setPermissionMenu(userResourceMap);
        return user;
    }

    private List<ResourceAuthResult> getUserAuth(String userId) {
        UserRoleRelationBatchQueryParam userRoleRelationBatchQueryParam = new UserRoleRelationBatchQueryParam();
        userRoleRelationBatchQueryParam.setUserIdList(Arrays.asList(userId));
        //查询用户下所有角色
        List<UserRoleRelationResult> roleResultList = selectUserRoleRelationBatch(userRoleRelationBatchQueryParam);
        if (CollectionUtils.isNotEmpty(roleResultList)) {
            List<Long> roleIdList = roleResultList
                    .stream()
                    .map(UserRoleRelationResult::getRoleId)
                    .map(Long::parseLong)
                    .distinct()
                    .collect(Collectors.toList());
            RoleResourceAuthQueryParam roleResourceAuthQueryParam = new RoleResourceAuthQueryParam();
            roleResourceAuthQueryParam.setRoleIdList(roleIdList);
            roleResourceAuthQueryParam.setStatus(Boolean.TRUE);
            List<ResourceAuthResult> authResultList = selectList(roleResourceAuthQueryParam);
            return authResultList;
        } else {
            log.error("用户角色为空，查询权限失败:[{}]", userId);
        }
        return Lists.newArrayList();
    }

    public User getUserAction(User user) {
        //查询可配置的全量菜单
        List<ResourceMenuResult> resourceMenuResultList = selectAuthConfigMenu();
        Map<String, Boolean> actionMap = Maps.newLinkedHashMap();
        if (!Objects.isNull(user)) {
            if (user.getUserType() == 0) {
                resourceMenuResultList.forEach(menuResult -> {
                    List<Integer> actionList = menuResult.getActionList();
                    if (CollectionUtils.isNotEmpty(actionList)) {
                        actionList.forEach(action -> {
                            String key = menuResult.getCode() + "_" + action + "_" + Objects.requireNonNull(ActionTypeEnum.getNameByCode(action)).toLowerCase();
                            if (!actionMap.containsKey(key)) {
                                actionMap.put(key, Boolean.TRUE);
                            }
                        });
                    }
                });
            } else {
                List<ResourceAuthResult> authResultList = getUserAuth(String.valueOf(user.getId()));
                if (CollectionUtils.isNotEmpty(authResultList)) {
                    authResultList.forEach(authResult -> {
                        Optional<ResourceMenuResult> optional = resourceMenuResultList
                                .stream()
                                .filter(resourceMenuResult -> resourceMenuResult.getId().equals(authResult.getResoureId()))
                                .findFirst();
                        if (optional.isPresent()) {
                            ResourceMenuResult resourceMenuResult = optional.get();
                            List<Integer> actionList = authResult.getActionList();
                            //有菜单默认就有查询权限
                            actionList.add(ActionTypeEnum.QUERY.getCode());
                            actionList.forEach(action -> {
                                String key = resourceMenuResult.getCode() + "_" + action + "_" + Objects.requireNonNull(ActionTypeEnum.getNameByCode(action)).toLowerCase();
                                if (!actionMap.containsKey(key)) {
                                    actionMap.put(key, Boolean.TRUE);
                                }
                            });
                        } else {
                            log.error("菜单不存在:[{}]", authResult.getResoureId());
                        }
                    });
                } else {
                    log.error("用户权限为空，查询操作权限失败:[{}]", user.getId());
                }
            }
        } else {
            log.error("用户为空，查询角色失败");
        }
        if (actionMap.size() > 0) {
            orderByKey(actionMap, false);
        }
        user.setPermissionAction(actionMap);
        return user;
    }

    public User getUserData(User user) {
        Map<String, List<Integer>> scopeMap = Maps.newHashMap();
        if (!Objects.isNull(user)) {
            if (user.getUserType() == 0) {
                return user;
            }
            List<ResourceAuthResult> authResultList = getUserAuth(String.valueOf(user.getId()));
            if (CollectionUtils.isNotEmpty(authResultList)) {
                authResultList.forEach(authResult -> {
                    //有菜单默认就有查询权限
                    List<Integer> actionList = authResult.getActionList();
                    actionList.add(ActionTypeEnum.QUERY.getCode());
                    for (Integer action : actionList) {
                        String key = authResult.getResourceCode() + "_" + action + "_" + Objects.requireNonNull(ActionTypeEnum.getNameByCode(action)).toLowerCase();
                        if (!scopeMap.containsKey(key)) {
                            List<Integer> tmpScopeList = Lists.newArrayList();
                            if (CollectionUtils.isNotEmpty(authResult.getScopeList())) {
                                tmpScopeList.addAll(authResult.getScopeList());
                                scopeMap.put(key, tmpScopeList);
                            }
                        } else {
                            List<Integer> scopeList = scopeMap.get(key);
                            if (CollectionUtils.isNotEmpty(authResult.getScopeList())) {
                                scopeList.addAll(authResult.getScopeList());
                            }
                            List<Integer> withoutDuplicateScopeList = scopeList.stream().distinct().collect(Collectors.toList());
                            scopeMap.put(key, withoutDuplicateScopeList);
                        }
                    }
                });
            }
        }
        user.setPermissionData(scopeMap);
        return user;
    }

    /**
     * 查询可配置权限的（有页面的）菜单列表
     *
     * @return
     */
    @Override
    public List<ResourceMenuResult> selectAuthConfigMenu() {
        return troResourceDAO.selectAuthConfigMenu();
    }

    /**
     * 查询全量菜单列表
     *
     * @return
     */
    @Override
    public List<ResourceMenuResult> selectMenu(ResourceMenuQueryParam queryParam) {
        return troResourceDAO.selectMenu(queryParam);
    }

    /**
     * 查询角色权限
     *
     * @param queryParam
     * @return
     */
    @Override
    public List<ResourceAuthResult> selectList(RoleResourceAuthQueryParam queryParam) {
        return troRoleResourceAuthDAO.selectList(queryParam);
    }

    /**
     * 更新角色权限
     *
     * @param updateParamList
     * @return
     */
    @Override
    public void updateRoleAuth(List<RoleResourceAuthUpdateParam> updateParamList) {
        troRoleResourceAuthDAO.updateRoleAuth(updateParamList);
    }

    /**
     * 删除角色权限
     *
     * @param deleteParamList
     * @return
     */
    @Override
    public void deleteRoleAuth(List<RoleResourceAuthDeleteParam> deleteParamList) {
        troRoleResourceAuthDAO.deleteRoleAuth(deleteParamList);
    }

    /**
     * 查询用户下所有角色列表
     *
     * @param param
     * @return
     */
    @Override
    public List<UserRoleRelationResult> selectUserRoleRelationBatch(UserRoleRelationBatchQueryParam param) {
        return troRoleUserRelationDAO.selectUserRoleRelationBatch(param);
    }

    /**
     * 根据用户id查询用户部门信息
     *
     * @param queryParam
     * @return
     */
    @Override
    public List<UserDeptResult> selectList(UserDeptQueryParam queryParam) {
        return troDeptUserRelationDAO.selectList(queryParam);
    }

    /**
     * 根据部门id查询用户列表
     *
     * @param queryParam
     * @return
     */
    @Override
    public List<UserDeptConditionResult> selectUserByDeptIds(UserDeptConditionQueryParam queryParam) {
        return troDeptUserRelationDAO.selectUserByDeptIds(queryParam);
    }

    @Override
    public void checkFail(HttpServletResponse response) {
        // 重定向至韵达魔盒登录页面
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            log.info("登录信息过期，请重新登录：{}", YundaBaseInfo.YUNDA_LOGOUT_URL);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            PrintWriter printWriter = response.getWriter();
            Map<String, Object> map = Maps.newHashMap();
            map.put("msg", "用户未登录");
            map.put("status", HttpStatus.UNAUTHORIZED.value());
            String s = JSON.toJSONString(map);
            printWriter.write(s);
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


