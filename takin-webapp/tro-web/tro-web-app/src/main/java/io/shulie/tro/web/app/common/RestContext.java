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

package io.shulie.tro.web.app.common;

import java.util.Arrays;
import java.util.List;

import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;

/**
 * Create by xuyh at 2020/3/27 10:35.
 */
public final class RestContext {

    /**
     * 通过 http 登录的当前用户
     */
    public static InheritableThreadLocal<User> userThreadLocal = new InheritableThreadLocal<>();

    public static InheritableThreadLocal<String> tenantUserAppKeyThreadLocal = new InheritableThreadLocal<>();

    public static InheritableThreadLocal<List<Long>> queryAllowUserIdListThreadLocal = new InheritableThreadLocal<>();

    public static InheritableThreadLocal<List<Long>> updateAllowUserIdListThreadLocal = new InheritableThreadLocal<>();

    public static InheritableThreadLocal<List<Long>> deleteAllowUserIdListThreadLocal = new InheritableThreadLocal<>();

    public static InheritableThreadLocal<List<Long>> enableDisableAllowUserIdListThreadLocal
        = new InheritableThreadLocal<>();

    public static InheritableThreadLocal<List<Long>> startStopAllowUserIdListThreadLocal
        = new InheritableThreadLocal<>();

    public static InheritableThreadLocal<List<Long>> downloadAllowUserIdListThreadLocal
        = new InheritableThreadLocal<>();

    private RestContext() { /* no instance */ }

    public static User getUser() {
        return userThreadLocal.get();
    }

    public static void setUser(User user) {
        userThreadLocal.remove();
        userThreadLocal.set(user);
    }

    /**
     * 获取租户id
     *
     * @return
     */
    public static Long getCustomerId() {
        User user = userThreadLocal.get();
        if (user != null) {
            return user.getCustomerId();
        }
        throw new TroWebException(ExceptionCode.GET_CUSTOMER_ID_ERROR, "查看该账户登录的租户Id是否存在");
    }



    public static void clear() {
        userThreadLocal.remove();
        tenantUserAppKeyThreadLocal.remove();
    }

    public static void clearAuth() {
        queryAllowUserIdListThreadLocal.remove();
        updateAllowUserIdListThreadLocal.remove();
        deleteAllowUserIdListThreadLocal.remove();
        enableDisableAllowUserIdListThreadLocal.remove();
        startStopAllowUserIdListThreadLocal.remove();
        downloadAllowUserIdListThreadLocal.remove();
    }

    public static void setEmptyAuth() {
        clearAuth();
        queryAllowUserIdListThreadLocal.set(Arrays.asList(-1L));
        updateAllowUserIdListThreadLocal.set(Arrays.asList(-1L));
        deleteAllowUserIdListThreadLocal.set(Arrays.asList(-1L));
        enableDisableAllowUserIdListThreadLocal.set(Arrays.asList(-1L));
        startStopAllowUserIdListThreadLocal.set(Arrays.asList(-1L));
        downloadAllowUserIdListThreadLocal.set(Arrays.asList(-1L));
    }

    public static String getTenantUserKey() {
        return tenantUserAppKeyThreadLocal.get();
    }

    public static void setTenantUserKey(String userAppKey) {
        tenantUserAppKeyThreadLocal.remove();
        tenantUserAppKeyThreadLocal.set(userAppKey);
    }

    public static List<Long> getQueryAllowUserIdList() {
        return queryAllowUserIdListThreadLocal.get();
    }

    public static void setQueryAllowUserIdList(List<Long> userIdList) {
        queryAllowUserIdListThreadLocal.remove();
        queryAllowUserIdListThreadLocal.set(userIdList);
    }

    public static List<Long> getUpdateAllowUserIdList() {
        return updateAllowUserIdListThreadLocal.get();
    }

    public static void setUpdateAllowUserIdList(List<Long> updateAllowUserIdList) {
        updateAllowUserIdListThreadLocal.remove();
        updateAllowUserIdListThreadLocal.set(updateAllowUserIdList);
    }

    public static List<Long> getDeleteAllowUserIdList() {
        return deleteAllowUserIdListThreadLocal.get();
    }

    public static void setDeleteAllowUserIdList(List<Long> deleteAllowUserIdList) {
        deleteAllowUserIdListThreadLocal.remove();
        RestContext.deleteAllowUserIdListThreadLocal.set(deleteAllowUserIdList);
    }

    public static List<Long> getEnableDisableAllowUserIdList() {
        return enableDisableAllowUserIdListThreadLocal.get();
    }

    public static void setEnableDisableAllowUserIdList(List<Long> enableDisableAllowUserIdList) {
        enableDisableAllowUserIdListThreadLocal.remove();
        enableDisableAllowUserIdListThreadLocal.set(enableDisableAllowUserIdList);
    }

    public static List<Long> getStartStopAllowUserIdList() {
        return startStopAllowUserIdListThreadLocal.get();
    }

    public static void setStartStopAllowUserIdList(List<Long> startStopAllowUserIdList) {
        startStopAllowUserIdListThreadLocal.remove();
        startStopAllowUserIdListThreadLocal.set(startStopAllowUserIdList);
    }

    public static List<Long> getDownloadAllowUserIdList() {
        return downloadAllowUserIdListThreadLocal.get();
    }

    public static void setDownloadAllowUserIdList(List<Long> downloadAllowUserIdList) {
        downloadAllowUserIdListThreadLocal.remove();
        downloadAllowUserIdListThreadLocal.set(downloadAllowUserIdList);
    }
}
