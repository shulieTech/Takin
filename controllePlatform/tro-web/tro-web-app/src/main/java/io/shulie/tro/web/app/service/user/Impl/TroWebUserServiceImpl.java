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

package io.shulie.tro.web.app.service.user.Impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.opencsv.bean.CsvToBeanBuilder;
import com.pamirs.pradar.ext.common.collect.Lists;
import com.pamirs.pradar.ext.commons.lang3.RandomStringUtils;
import com.pamirs.tro.entity.dao.user.TUserMapper;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.vo.user.UserQueryParam;
import io.shulie.tro.cloud.open.req.report.ReportAllocationUserReq;
import io.shulie.tro.cloud.open.req.scenemanage.SceneAllocationUserReq;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.web.app.cache.webimpl.AllUserCache;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.app.input.user.UserAllocationUpdateInput;
import io.shulie.tro.web.app.request.application.ShadowConsumerUpdateUserRequest;
import io.shulie.tro.web.app.request.user.UserAllocationUpdateRequest;
import io.shulie.tro.web.app.request.user.UserCreateRequest;
import io.shulie.tro.web.app.request.user.UserDeleteRequest;
import io.shulie.tro.web.app.request.user.UserDetailQueryRequest;
import io.shulie.tro.web.app.request.user.UserPasswordUpdateRequest;
import io.shulie.tro.web.app.request.user.UserUpdateRequest;
import io.shulie.tro.web.app.response.user.UserCsvBean;
import io.shulie.tro.web.app.response.user.UserDetailResponse;
import io.shulie.tro.web.app.response.user.UserImportResponse;
import io.shulie.tro.web.app.service.ShadowConsumerService;
import io.shulie.tro.web.app.service.user.TroUserService;
import io.shulie.tro.web.app.service.user.TroWebUserService;
import io.shulie.tro.web.app.utils.BCrypt;
import io.shulie.tro.web.app.utils.CSVUtil;
import io.shulie.tro.web.app.utils.UnitConvertor;
import io.shulie.tro.web.auth.api.enums.MenuTypeEnum;
import io.shulie.tro.web.auth.api.enums.UserTypeEnum;
import io.shulie.tro.web.common.util.FileUtil;
import io.shulie.tro.web.data.dao.application.ApplicationApiDAO;
import io.shulie.tro.web.data.dao.application.ApplicationDAO;
import io.shulie.tro.web.data.dao.application.ApplicationDsDAO;
import io.shulie.tro.web.data.dao.application.ApplicationShadowJobDAO;
import io.shulie.tro.web.data.dao.application.ApplicationWhiteListDAO;
import io.shulie.tro.web.data.dao.application.LinkGuardDAO;
import io.shulie.tro.web.data.dao.linkmanage.BusinessLinkManageDAO;
import io.shulie.tro.web.data.dao.linkmanage.LinkManageDAO;
import io.shulie.tro.web.data.dao.linkmanage.SceneDAO;
import io.shulie.tro.web.data.dao.scriptmanage.ScriptManageDAO;
import io.shulie.tro.web.data.dao.user.TroDeptDAO;
import io.shulie.tro.web.data.dao.user.TroDeptUserRelationDAO;
import io.shulie.tro.web.data.dao.user.TroRoleUserRelationDAO;
import io.shulie.tro.web.data.dao.user.TroUserDAO;
import io.shulie.tro.web.data.model.mysql.TroUserDeptRelationEntity;
import io.shulie.tro.web.data.param.application.ApplicationApiUpdateUserParam;
import io.shulie.tro.web.data.param.application.ApplicationDsUpdateUserParam;
import io.shulie.tro.web.data.param.application.ApplicationUpdateParam;
import io.shulie.tro.web.data.param.application.ApplicationWhiteListUpdateParam;
import io.shulie.tro.web.data.param.application.LinkGuardUpdateUserParam;
import io.shulie.tro.web.data.param.application.ShadowJobUpdateUserParam;
import io.shulie.tro.web.data.param.linkmanage.BusinessLinkManageUpdateParam;
import io.shulie.tro.web.data.param.linkmanage.LinkManageUpdateParam;
import io.shulie.tro.web.data.param.linkmanage.SceneUpdateParam;
import io.shulie.tro.web.data.param.user.DeptCreateParam;
import io.shulie.tro.web.data.param.user.UserCreateParam;
import io.shulie.tro.web.data.param.user.UserDeptQueryParam;
import io.shulie.tro.web.data.param.user.UserDeptRelationBatchUserDeleteParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationBatchDeleteParam;
import io.shulie.tro.web.data.result.user.DeptQueryResult;
import io.shulie.tro.web.data.result.user.UserDetailResult;
import io.shulie.tro.web.diff.api.scenemanage.SceneManageApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: fanxx
 * @Date: 2020/3/24 下午4:45
 * @Description:
 */
@Component
@Slf4j
public class TroWebUserServiceImpl implements TroWebUserService {

    @Resource
    private TUserMapper TUserMapper;

    @Autowired
    private TroUserService troUserService;

    @Autowired
    private SceneDAO sceneDAO;

    @Autowired
    private ApplicationDAO applicationDAO;

    @Autowired
    private BusinessLinkManageDAO businessLinkManageDAO;

    @Autowired
    private LinkManageDAO linkManageDAO;

    @Autowired
    private ScriptManageDAO scriptManageDAO;

    @Autowired
    private SceneManageApi sceneManageApi;

    @Autowired
    private ApplicationWhiteListDAO whiteListDAO;

    @Autowired
    private ApplicationDsDAO applicationDsDAO;

    @Autowired
    private LinkGuardDAO linkGuardDAO;

    @Autowired
    private ApplicationShadowJobDAO shadowJobDAO;

    @Autowired
    private ShadowConsumerService shadowConsumerService;

    @Autowired
    private ApplicationApiDAO applicationApiDAO;

    @Autowired
    private TroDeptUserRelationDAO troDeptUserRelationDAO;

    @Autowired
    private TroRoleUserRelationDAO troRoleUserRelationDAO;

    private static final String deptNameSeparator = "/";
    @Autowired
    private AllUserCache allUserCache;

    @Autowired
    private TroDeptDAO troDeptDAO;

    @Autowired
    private TroUserDAO troUserDAO;

    @Value("${file.upload.user.data.dir}")
    private String fileDir;

    private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(UserCreateRequest createRequest) {
        UserQueryParam param = new UserQueryParam();
        param.setName(createRequest.getName());
        param.setCustomerId(RestContext.getCustomerId());
        User userExist = TUserMapper.queryByName(param);
        //1、校验用户名是否已存在
        if (userExist != null) {
            throw new TroWebException(ExceptionCode.USER_MANAGE_ADD_ERROR, "用户已存在");
        }
        int minLength = 8;
        int maxLength = 20;
        int passwordLength = createRequest.getPassword().length();
        if (passwordLength > maxLength || passwordLength < minLength) {
            throw new TroWebException(ExceptionCode.USER_MANAGE_ADD_ERROR, "密码不合法，请输入8~20个字符");
        }
        int nameLength = createRequest.getName().length();
        if (nameLength > maxLength) {
            throw new TroWebException(ExceptionCode.USER_MANAGE_ADD_ERROR, "用户名超长，不得超过20个字符");
        }
        UserCreateParam user = new UserCreateParam();
        user.setName(createRequest.getName());
        user.setNick(createRequest.getName());
        user.setKey(UUID.randomUUID().toString());
        String salt = BCrypt.gensalt();
        String pwd = BCrypt.hashpw(createRequest.getPassword(), salt);
        user.setSalt(salt);
        user.setPassword(pwd);
        user.setUserType(UserTypeEnum.NORMAL.getCode());
        user.setCustomerId(RestContext.getCustomerId());
        Long userId = troUserDAO.insert(user);

        List<Long> deptIdList = createRequest.getDeptIdList();
        List<TroUserDeptRelationEntity> deptUserEntityList = deptIdList.stream().map(deptId -> {
            TroUserDeptRelationEntity entity = new TroUserDeptRelationEntity();
            entity.setDeptId(String.valueOf(deptId));
            entity.setUserId(String.valueOf(userId));
            return entity;
        }).collect(Collectors.toList());
        troDeptUserRelationDAO.batchInsert(deptUserEntityList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateRequest updateRequest) {
        UserDetailResult userOld = troUserDAO.selectUserById(updateRequest.getId());
        if (userOld == null) {
            throw new TroWebException(ExceptionCode.USER_MANAGE_UPDATE_ERROR, "用户不存在");
        }
        User updateUser = new User();
        updateUser.setId(updateRequest.getId());
        updateUser.setName(updateRequest.getName());
        //1、校验用户名是否已存在
        if (!updateRequest.getName().equals(userOld.getName())) {
            UserQueryParam param = new UserQueryParam();
            param.setName(updateRequest.getName());
            param.setCustomerId(RestContext.getCustomerId());
            User userExist = TUserMapper.queryByName(param);
            if (userExist != null) {
                throw new TroWebException(ExceptionCode.USER_MANAGE_UPDATE_ERROR, "用户已存在");
            }
        }
        String pwd = BCrypt.hashpw(updateRequest.getPassword(), userOld.getSalt());
        if (!pwd.equals(userOld.getPassword())) {
            String newSalt = BCrypt.gensalt();
            String newPwd = BCrypt.hashpw(updateRequest.getPassword(), newSalt);
            updateUser.setSalt(newSalt);
            updateUser.setPassword(newPwd);
        }
        TUserMapper.updateByPrimaryKeySelective(updateUser);

        UserDeptRelationBatchUserDeleteParam deptRelationBatchUserDeleteParam
            = new UserDeptRelationBatchUserDeleteParam();
        deptRelationBatchUserDeleteParam.setUserIdList(Collections.singletonList(updateRequest.getId()));
        troDeptUserRelationDAO.deleteByUserIds(deptRelationBatchUserDeleteParam);
        List<Long> deptIdList = updateRequest.getDeptIdList();
        List<TroUserDeptRelationEntity> deptUserEntityList = deptIdList.stream().map(deptId -> {
            TroUserDeptRelationEntity entity = new TroUserDeptRelationEntity();
            entity.setDeptId(String.valueOf(deptId));
            entity.setUserId(String.valueOf(updateRequest.getId()));
            return entity;
        }).collect(Collectors.toList());
        troDeptUserRelationDAO.batchInsert(deptUserEntityList);
    }

    @Override
    public void updatePassword(UserPasswordUpdateRequest updateRequest) {
        UserDetailResult userOld = troUserDAO.selectUserById(updateRequest.getId());
        String oldPassword = BCrypt.hashpw(updateRequest.getOldPassword(), userOld.getSalt());
        if (!userOld.getPassword().equals(oldPassword)) {
            throw new TroWebException(ExceptionCode.USER_MANAGE_UPDATE_ERROR, "原密码错误");
        }
        if (updateRequest.getOldPassword().equals(updateRequest.getNewPassword())) {
            throw new TroWebException(ExceptionCode.USER_MANAGE_UPDATE_ERROR, "新旧密码不许重复");
        }
        int minLength = 8;
        int maxLength = 20;
        int passwordLength = updateRequest.getNewPassword().length();
        if (passwordLength > maxLength || passwordLength < minLength) {
            throw new TroWebException(ExceptionCode.USER_MANAGE_UPDATE_ERROR, "新密码不合法，请输入8~20个字符");
        }
        User updateUser = new User();
        updateUser.setId(updateRequest.getId());
        String newSalt = BCrypt.gensalt();
        String newPwd = BCrypt.hashpw(updateRequest.getNewPassword(), newSalt);
        updateUser.setSalt(newSalt);
        updateUser.setPassword(newPwd);
        TUserMapper.updateByPrimaryKeySelective(updateUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(UserDeleteRequest deleteRequest) {
        List<Long> userIdList = deleteRequest.getUserIdList();
        TUserMapper.deleteByIds(userIdList);

        UserDeptRelationBatchUserDeleteParam deptRelationBatchUserDeleteParam
            = new UserDeptRelationBatchUserDeleteParam();
        deptRelationBatchUserDeleteParam.setUserIdList(userIdList);
        troDeptUserRelationDAO.deleteByUserIds(deptRelationBatchUserDeleteParam);

        List<String> userIdStringList = userIdList.stream().map(String::valueOf).collect(Collectors.toList());
        UserRoleRelationBatchDeleteParam deleteParam = new UserRoleRelationBatchDeleteParam();
        deleteParam.setUserId(userIdStringList);
        troRoleUserRelationDAO.deleteUserRoleRelationBatch(deleteParam);
    }

    private static boolean allFieldIsNotNull(Object o, List<String> ignoreField) {
        try {
            for (Field field : o.getClass().getDeclaredFields()) {
                //把私有属性公有化
                field.setAccessible(true);
                Object object = field.get(o);
                String fieldName = field.getName();
                if (CollectionUtils.isNotEmpty(ignoreField) && ignoreField.contains(fieldName)) {
                    return true;
                }
                if (Objects.isNull(object)) {
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("execute method allFieldIsNotNull error:", e);
            return false;
        }
        return true;
    }

    @Override
    public UserDetailResponse queryUser(UserDetailQueryRequest queryRequest) {
        UserDetailResult user = troUserDAO.selectUserById(queryRequest.getId());
        if (Objects.isNull(user)) {
            throw new TroWebException(ExceptionCode.USER_MANAGE_QUERY_ERROR, "用户不存在");
        }
        UserDetailResponse detailResponse = new UserDetailResponse();
        detailResponse.setId(user.getId());
        detailResponse.setName(user.getName());

        UserDeptQueryParam queryParam = new UserDeptQueryParam();
        queryParam.setUserIdList(Collections.singletonList(String.valueOf(queryRequest.getId())));
        List<Long> deptIdList = troDeptUserRelationDAO.selectDeptIdList(queryParam);
        detailResponse.setDeptIdList(deptIdList);
        return detailResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserImportResponse importUser(MultipartFile file) {
        UserImportResponse importResponse = verifyFile(file);
        if (!Objects.isNull(importResponse)) {
            return importResponse;
        } else {
            importResponse = new UserImportResponse();
            importResponse.setSuccess(Boolean.FALSE);
            importResponse.setWriteBack(Boolean.FALSE);
        }
        File commonFile = FileUtil.convertMultipartFile(file);
        try {
            List<UserCsvBean> beans = new CsvToBeanBuilder<UserCsvBean>(
                new BufferedReader(new InputStreamReader(new FileInputStream(commonFile), "GBK")))
                .withType(UserCsvBean.class).build().parse();
            commonFile.delete();
            if (CollectionUtils.isEmpty(beans) || beans.size() <= 1) {
                return wrapImportResponse(importResponse, "用户信息为空");
            }
            Map<String, UserCsvBean> beanMap = Maps.newLinkedHashMap();
            for (UserCsvBean userCsvBean : beans) {
                if (allFieldIsNotNull(userCsvBean, Collections.singletonList("errorMsg"))) {
                    beanMap.putIfAbsent(userCsvBean.getUsername(), userCsvBean);
                }
            }
            beanMap.remove("账号名称");
            LinkedList<UserCsvBean> writeBackUserCsvBeanList = verfiyUser(beanMap);
            boolean writeBack = writeBackUserCsvBeanList.stream().anyMatch(
                w -> StringUtils.isNotBlank(w.getErrorMsg()));
            if (writeBack) {
                importResponse.setSuccess(Boolean.FALSE);
                importResponse.setWriteBack(Boolean.TRUE);
                importResponse.setErrorMsg("导入失败，失败明细已下载到本地，请修改后再导入");
                String[] fileNameArr = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
                String fileName = fileNameArr[0] + System.currentTimeMillis() + "." + fileNameArr[1];
                String basePath = fileDir + "/file/temp/" + fileName;
                importResponse.setFilePath(basePath);
                CSVUtil.writeCsvFile(writeBackUserCsvBeanList, basePath);
                return importResponse;
            }
            //批量新增部门
            batchImportDept(writeBackUserCsvBeanList);
            //批量新增用户
            batchImportUser(beanMap);
            importResponse.setSuccess(Boolean.TRUE);
        } catch (FileNotFoundException e) {
            log.error("导入用户失败:{}", file.getOriginalFilename(), e);
            return wrapImportResponse(importResponse, "文件不存在");
        } catch (Exception e) {
            log.error("导入用户失败:{}", file.getOriginalFilename(), e);
            importResponse.setErrorMsg("导入异常，请联系管理员处理");
        }
        return importResponse;
    }

    private void batchImportDept(LinkedList<UserCsvBean> writeBackUserCsvBeanList) {
        LinkedHashSet<String> deptNameSet = Sets.newLinkedHashSet();
        writeBackUserCsvBeanList.forEach(userCsvBean -> {
            String deptName = userCsvBean.getDeptNames();
            if (deptName.contains(deptNameSeparator)) {
                String[] deptNameArr = deptName.split(deptNameSeparator);
                deptNameSet.addAll(Arrays.asList(deptNameArr));
            } else {
                deptNameSet.add(deptName);
            }
        });
        if (!deptNameSet.isEmpty()) {
            deptNameSet.forEach(deptName -> {
                DeptQueryResult deptQueryResult = troDeptDAO.selectDetailByName(deptName);
                if (Objects.isNull(deptQueryResult)) {
                    DeptCreateParam createParam = new DeptCreateParam();
                    createParam.setName(deptName.trim());
                    createParam.setCode(RandomStringUtils.randomAlphanumeric(8));
                    troDeptDAO.insert(createParam);
                }
            });
        }
    }

    private UserImportResponse verifyFile(MultipartFile file) {
        String supportSuffix = "csv";
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(supportSuffix)) {
            UserImportResponse importResponse = new UserImportResponse();
            return wrapImportResponse(importResponse, "存在不支持的上传文件类型");
        }
        if (file.isEmpty()) {
            UserImportResponse importResponse = new UserImportResponse();
            return wrapImportResponse(importResponse, "文件内容不许为空");
        }
        BigDecimal fileSizeDecimal = UnitConvertor.byteToMb(new BigDecimal(file.getSize()));
        if (fileSizeDecimal.compareTo(new BigDecimal(2)) > 0) {
            UserImportResponse importResponse = new UserImportResponse();
            return wrapImportResponse(importResponse, "文件大小不许超过2MB");
        }
        return null;
    }

    private void batchImportUser(Map<String, UserCsvBean> beanMap) {
        beanMap.forEach((name, userCsvBean) -> {
            UserCreateParam user = new UserCreateParam();
            user.setName(name.trim());
            user.setNick(name.trim());
            user.setKey(UUID.randomUUID().toString());
            String salt = BCrypt.gensalt();
            String pwd = BCrypt.hashpw(userCsvBean.getPassword().trim(), salt);
            user.setSalt(salt);
            user.setPassword(pwd);
            user.setUserType(UserTypeEnum.NORMAL.getCode());
            user.setCustomerId(RestContext.getCustomerId());
            Long userId = troUserDAO.insert(user);
            String deptNames = userCsvBean.getDeptNames();
            List<String> deptNameList = new ArrayList<>();
            if (deptNames.contains(deptNameSeparator)) {
                deptNameList = Arrays.asList(deptNames.split(deptNameSeparator));
            } else {
                deptNameList.add(deptNames);
            }

            List<TroUserDeptRelationEntity> list = Lists.newArrayList();
            for (String deptName : deptNameList) {
                DeptQueryResult deptQueryResult = troDeptDAO.selectDetailByName(deptName);
                if (!Objects.isNull(deptQueryResult)) {
                    TroUserDeptRelationEntity entity = new TroUserDeptRelationEntity();
                    entity.setUserId(String.valueOf(userId));
                    entity.setDeptId(String.valueOf(deptQueryResult.getId()));
                    list.add(entity);
                }
            }
            troDeptUserRelationDAO.batchInsert(list);
        });
    }

    @Override
    public void download(HttpServletResponse httpServletResponse, String path) {
        File fileF = new File(path);
        if (!fileF.exists()) {
            throw new TroWebException(ExceptionCode.USER_MANAGE_DOWNLOAD_ERROR, "文件不存在!");
        }
        FileInputStream fileInputStream = null;
        OutputStream outputStream = null;
        httpServletResponse.setHeader("Content-Type", "application/octet-stream");
        try {
            httpServletResponse.setHeader("Content-Disposition",
                "attachment;filename=" + java.net.URLEncoder.encode(fileF.getName(), "UTF-8"));
            fileInputStream = new FileInputStream(fileF);
            outputStream = httpServletResponse.getOutputStream();
            byte[] bytes = new byte[32];
            int length;
            while ((length = fileInputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, length);
            }
            outputStream.flush();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            throw new TroWebException(ExceptionCode.USER_MANAGE_DOWNLOAD_ERROR, e.getMessage());
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                service.schedule(fileF::delete, 10_000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
                throw new TroWebException(ExceptionCode.USER_MANAGE_DOWNLOAD_ERROR, e.getMessage());
            }
        }
    }

    @Override
    public void downloadExample(HttpServletResponse httpServletResponse) {
        String filePath = "/file/example/example.csv";
        String downloadPath = fileDir + filePath;
        File fileF = new File(downloadPath);
        if (!fileF.exists()) {
            throw new TroWebException(ExceptionCode.USER_MANAGE_DOWNLOAD_ERROR, "模板不存在!");
        }
        FileInputStream fileInputStream = null;
        OutputStream outputStream = null;
        httpServletResponse.setHeader("Content-Type", "application/octet-stream");
        try {
            httpServletResponse.setHeader("Content-Disposition",
                "attachment;filename=" + java.net.URLEncoder.encode(fileF.getName(), "UTF-8"));
            fileInputStream = new FileInputStream(fileF);
            outputStream = httpServletResponse.getOutputStream();
            byte[] bytes = new byte[32];
            int length;
            while ((length = fileInputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, length);
            }
            outputStream.flush();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            throw new TroWebException(ExceptionCode.USER_MANAGE_DOWNLOAD_ERROR, e.getMessage());
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
                throw new TroWebException(ExceptionCode.USER_MANAGE_DOWNLOAD_ERROR, e.getMessage());
            }
        }
    }

    private UserImportResponse wrapImportResponse(UserImportResponse importResponse, String errorMsg) {
        importResponse.setSuccess(Boolean.FALSE);
        importResponse.setErrorMsg(errorMsg);
        importResponse.setWriteBack(Boolean.FALSE);
        return importResponse;
    }

    private LinkedList<UserCsvBean> verfiyUser(Map<String, UserCsvBean> beanMap) {
        LinkedList<UserCsvBean> writeBackUserCsvBeanList = Lists.newLinkedList();
        // 1、用户名称重复性校验
        beanMap.forEach((name, userCsvBean) -> {
            List<String> msgList = new ArrayList<>();
            String username = userCsvBean.getUsername();
            String password = userCsvBean.getPassword();
            String deptNames = userCsvBean.getDeptNames();
            int minLength = 8;
            int maxLength = 20;
            if (StringUtils.isBlank(username)) {
                msgList.add("用户名不能为空");
            } else {
                int usernameLength = username.length();
                if (usernameLength > maxLength) {
                    msgList.add("用户名超长，不得超过20个字符");
                }
                UserDetailResult userDetailResult = troUserDAO.selectUserByName(username);
                if (null != userDetailResult) {
                    msgList.add("用户名称已存在");
                }
            }
            if (StringUtils.isBlank(password)) {
                msgList.add("密码不能为空");
            } else {
                int passwordLength = password.length();
                if (passwordLength > maxLength || passwordLength < minLength) {
                    msgList.add("密码不合法，请输入8~20个字符");
                }
            }
            if (StringUtils.isBlank(deptNames)) {
                msgList.add("部门名称不能为空");
            } else {
                String[] deptNameArr = deptNames.split("/");
                for (String deptName : deptNameArr) {
                    int deptNameLength = deptName.length();
                    if (deptNameLength > maxLength) {
                        msgList.add("部门名称超长，不得超过20个字符");
                    }
                }
            }
            if (!msgList.isEmpty()) {
                userCsvBean.setErrorMsg(Strings.join(msgList, '，'));
            }
            writeBackUserCsvBeanList.add(userCsvBean);
        });
        return writeBackUserCsvBeanList;
    }

    @Override
    public User queryUserByKey(String key) {
        if (StringUtils.isBlank(key)) {
            log.error("查询用户失败：key为空");
            return null;
        }
        User user = TUserMapper.selectByKey(key);
        if (Objects.isNull(user)) {
            log.error("查询用户失败，用户不存在：" + key);
            return null;
        }
        troUserService.syncAuth(user);
        return user;
    }

    /**
     * 指定责任人
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response allocationUser(UserAllocationUpdateRequest request) {
        UserAllocationUpdateInput input = new UserAllocationUpdateInput();
        BeanUtils.copyProperties(request, input);
        if (null == input.getDataId() || null == input.getUserId() || StringUtils.isBlank(
            input.getMenuCode())) {
            return Response.fail("0", "参数不能为空");
        }
        Long dataId = input.getDataId();
        Long userId = input.getUserId();
        boolean success = true;
        switch (Objects.requireNonNull(MenuTypeEnum.getByCode(input.getMenuCode()))) {
            //系统流程
            case LINK_MANAGE:
                linkManageDAO.allocationUser(new LinkManageUpdateParam(dataId, userId));
                break;
            //业务活动
            case BUSINESS_ACTIVITY:
                businessLinkManageDAO.allocationUser(new BusinessLinkManageUpdateParam(dataId, userId));
                break;
            //业务流程
            case BUSINESS_FLOW:
                sceneDAO.allocationUser(new SceneUpdateParam(dataId, userId));
                break;
            //应用管理
            case APPLICATION_MNT:
                //修改应用负责人
                applicationDAO.allocationUser(new ApplicationUpdateParam(dataId, userId));
                //修改影子库表配置的用户归属
                applicationDsDAO.allocationUser(new ApplicationDsUpdateUserParam(dataId, userId));
                //修改挡板配置的用户归属
                linkGuardDAO.allocationUser(new LinkGuardUpdateUserParam(dataId, userId));
                //修改定时任务配置的用户归属
                shadowJobDAO.allocationUser(new ShadowJobUpdateUserParam(dataId, userId));
                //修改白名单配置的用户归属
                whiteListDAO.allocationUser(
                    new ApplicationWhiteListUpdateParam(String.valueOf(dataId), userId));
                //修改影子消费者配置的用户归属
                shadowConsumerService.allocationUser(new ShadowConsumerUpdateUserRequest(dataId, userId));
                //修改入口规则配置的用户归属
                applicationApiDAO.allocationUser(new ApplicationApiUpdateUserParam(dataId, userId));
                break;
            //压测场景
            case SCENE_MANAGE:
                SceneAllocationUserReq sceneReq = new SceneAllocationUserReq();
                sceneReq.setDataId(dataId);
                sceneReq.setUserId(userId);
                ResponseResult sceneResult = sceneManageApi.allocationSceneUser(sceneReq);
                success = sceneResult.getSuccess();
                break;
            //压测报表
            case SCENE_RECORD:
                ReportAllocationUserReq reportReq = new ReportAllocationUserReq();
                reportReq.setDataId(dataId);
                reportReq.setUserId(userId);
                ResponseResult reporeResult = sceneManageApi.allocationReportUser(reportReq);
                success = reporeResult.getSuccess();
                break;
            //脚本管理
            case SCRIPT_MANAGE:
                scriptManageDAO.allocationUser(dataId, userId);
                break;
            default:
                break;
        }

        return success ? Response.success("success") : Response.fail("操作失败");
    }
}
