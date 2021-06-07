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
//package io.shulie.tro.web.app.service.fastdebug;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//import com.google.common.collect.Lists;
//import com.pamirs.pradar.Pradar;
//import com.pamirs.tro.common.constant.LogLevelEnum;
//import com.pamirs.tro.common.constant.LogPullStatusEnum;
//import com.pamirs.tro.common.util.DateUtils;
//import com.pamirs.tro.entity.dao.user.TUserMapper;
//import com.pamirs.tro.entity.domain.entity.user.User;
//import io.shulie.tro.cloud.common.redis.RedisClientUtils;
//import io.shulie.tro.web.app.cache.webimpl.AllUserCache;
//import io.shulie.tro.web.app.common.RestContext;
//import io.shulie.tro.web.app.constant.FastDebugLogPathFactory;
//import io.shulie.tro.web.app.output.fastdebug.FastDebugStackInfoOutPut;
//import io.shulie.tro.web.app.request.fastdebug.FastDebugAgentLogUploadRequest;
//import io.shulie.tro.web.app.request.fastdebug.FastDebugLogReq;
//import io.shulie.tro.web.app.request.fastdebug.FastDebugMachinePerformanceCreateRequest;
//import io.shulie.tro.web.app.request.fastdebug.FastDebugStackInfoCreateRequest;
//import io.shulie.tro.web.app.request.fastdebug.FastDebugStackUploadCreateRequest;
//import io.shulie.tro.web.app.utils.FileUtils;
//import io.shulie.tro.web.common.vo.fastdebug.FastDebugExceptionVO;
//import io.shulie.tro.web.data.dao.exception.ExceptionDao;
//import io.shulie.tro.web.data.dao.fastdebug.FastDebugDao;
//import io.shulie.tro.web.data.dao.fastdebug.FastDebugMachinePerformanceDao;
//import io.shulie.tro.web.data.dao.fastdebug.FastDebugStackInfoDao;
//import io.shulie.tro.web.data.param.fastdebug.FastDebugExceptionParam;
//import io.shulie.tro.web.data.param.fastdebug.FastDebugMachinePerformanceCreateParam;
//import io.shulie.tro.web.data.param.fastdebug.FastDebugStackInfoCreateParam;
//import io.shulie.tro.web.data.param.fastdebug.FastDebugStackInfoQueryParam;
//import io.shulie.tro.web.data.result.exception.ExceptionResult;
//import io.shulie.tro.web.data.result.fastdebug.FastDebugStackInfoResult;
//import io.shulie.tro.web.data.result.user.UserCacheResult;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//
///**
// * @Author: mubai
// * @Date: 2020-12-28 13:46
// * @Description:
// */
//
//@Service
//@Slf4j
//public class FastDebugAgentUploadServiceImpl implements FastDebugAgentUploadService {
//    public static Logger logger = LoggerFactory.getLogger(FastDebugAgentUploadServiceImpl.class);
//    @Autowired
//    private FastDebugMachinePerformanceDao fastDebugMachinePerformanceDao;
//    @Autowired
//    private FastDebugStackInfoDao fastDebugStackInfoDao;
//    @Autowired
//    private RedisClientUtils redisClientUtils;
//    @Autowired
//    private FastDebugLogPathFactory fastDebugLogPathFactory;
//    @Autowired
//    private TUserMapper tUserMapper;
//    @Autowired
//    private ExceptionDao exceptionDao;
//    @Autowired
//    private FastDebugDao fastDebugDao;
//    @Autowired
//    private AllUserCache allUserCache;
//
//    @Autowired
//    @Qualifier("fastDebugThreadPool")
//    private ThreadPoolExecutor threadPoolExecutor;
//
//    @Override
//    public void uploadDebugStack(List<FastDebugStackUploadCreateRequest> requestList, String userAppKey) {
//        threadPoolExecutor.execute(() -> {
//            doUploadDebugStack(requestList, userAppKey);
//        });
//    }
//
//    public void doUploadDebugStack(List<FastDebugStackUploadCreateRequest> requestList, String userAppKey) {
//        if (requestList == null) {
//            throw new RuntimeException("入参不能为空");
//        }
//
//        if (CollectionUtils.isNotEmpty(requestList)) {
//            for (FastDebugStackUploadCreateRequest request : requestList) {
//                List<FastDebugStackInfoCreateRequest> errorConfigLogs = Lists.newArrayList();
//                // 转换类型
//                Integer logType = request.getLogType();
//                Integer type = 0;
//                if (null != logType) {
//                    type = request.getLogType() == Pradar.LOG_TYPE_RPC_CLIENT ? 0 : 1;
//                }
//                request.setLogType(type);
//                saveFastDebugStack(request, errorConfigLogs);
//                //记录traceId 上报日志次数
//                recordUploadTimes(request.getTraceId());
//                //存机器数据
//                saveMachineInfo(request);
//                //处理配置异常信息
//                dealConfigException(request.getAgentId(), request.getAppName(), errorConfigLogs, userAppKey);
//            }
//        }
//    }
//
//    private void saveMachineInfo(FastDebugStackUploadCreateRequest request) {
//        List<FastDebugMachinePerformanceCreateRequest> machineInfo = request.getMachineInfo();
//        if (CollectionUtils.isNotEmpty(machineInfo)) {
//            List<FastDebugMachinePerformanceCreateParam> machineParamList = Lists.newArrayList();
//            for (FastDebugMachinePerformanceCreateRequest req : machineInfo) {
//                FastDebugMachinePerformanceCreateParam param = new FastDebugMachinePerformanceCreateParam();
//                BigDecimal zero = new BigDecimal(0);
//                BigDecimal cpuLoad = StringUtils.isBlank(req.getCpuLoad()) ? zero : new BigDecimal(req.getCpuLoad());
//                BigDecimal cpuUsage = StringUtils.isBlank(req.getCpuUsage()) ? zero : new BigDecimal(req.getCpuUsage());
//                BigDecimal memoryUsage = StringUtils.isBlank(req.getMemoryUsage()) ? zero : new BigDecimal(
//                    req.getMemoryUsage());
//                BigDecimal memoryTotal = StringUtils.isBlank(req.getMemoryTotal()) ? zero : new BigDecimal(
//                    req.getMemoryTotal());
//                BigDecimal ioWait = StringUtils.isBlank(req.getIoWait()) ? zero : new BigDecimal(req.getIoWait());
//                param.setCpuLoad(cpuLoad);
//                param.setCpuUsage(cpuUsage);
//                param.setMemoryUsage(memoryUsage);
//                param.setMemoryTotal(memoryTotal);
//                param.setIoWait(ioWait);
//                param.setIndex(req.getIndex());
//                param.setTraceId(request.getTraceId());
//                param.setRpcId(request.getRpcId());
//                param.setYoungGcCount(req.getYoungGcCount());
//                param.setYoungGcTime(req.getYoungGcTime());
//                param.setOldGcCount(req.getOldGcCount());
//                param.setOldGcTime(req.getOldGcTime());
//                param.setLogType(request.getLogType());
//                machineParamList.add(param);
//            }
//            fastDebugMachinePerformanceDao.insert(machineParamList);
//        }
//    }
//
//    private void saveFastDebugStack(FastDebugStackUploadCreateRequest request,
//        List<FastDebugStackInfoCreateRequest> errorConfigLogs) {
//        FastDebugStackInfoCreateRequest createRequest = request.getLog();
//        if (createRequest != null) {
//            String level = createRequest.getLevel();
//            String content = createRequest.getContent();
//            //配置异常信息根据异常类别进行区分，ERROR 类型日志为配置异常信息
//            Boolean stackLogFlag = true;
//            if (null != level && level.equals(LogLevelEnum.ERROR.name()) && StringUtils.isNotBlank(content)
//                && content.contains("errorCode") && content.contains("errorType") && content.contains(
//                "traceId")) {
//                errorConfigLogs.add(createRequest);
//                stackLogFlag = false;
//            }
//            FastDebugStackInfoCreateParam createParam = new FastDebugStackInfoCreateParam();
//            createParam.setTraceId(request.getTraceId());
//            String rpcId = StringUtils.isBlank(request.getRpcId()) ? "null" : request.getRpcId();
//            createParam.setRpcId(rpcId);
//            if (StringUtils.isBlank(createParam.getTraceId()) || StringUtils.isBlank(createParam.getRpcId())) {
//                return;
//            }
//            createParam.setLevel(createRequest.getLevel());
//            StringBuilder builder = new StringBuilder();
//            builder.append(StringUtils.isBlank(createRequest.getContent()) ? "" : createRequest.getContent());
//            createParam.setContent(createRequest.getContent());
//            createParam.setType(request.getLogType());
//            createParam.setAppName(request.getAppName());
//            createParam.setAgentId(request.getAgentId());
//            createParam.setIsStack(stackLogFlag);
//            fastDebugStackInfoDao.insert(createParam);
//        }
//    }
//
//    /**
//     * 配置异常信息入库
//     */
//    private void dealConfigException(String agentId, String appName,
//        List<FastDebugStackInfoCreateRequest> errorConfigLogs, String userAppKey) {
//        //异常配置日志文件
//        // traceId:1564a8c016105418146071001d1d5f|rpcId:0.1|isPressure:true|errorType:DataSource|
//        // errorCode:datasource-0002|message:没有配置对应的影子表或影子库！|
//        // detail:业务库配置:::url: jdbc:mysql://192.168.1
//        // .107:3306/trodb?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true ;username: root|
//        log.debug("errorConfigLogs size :{} ", errorConfigLogs.size());
//        if (CollectionUtils.isEmpty(errorConfigLogs)) {
//            return;
//        }
//        List<FastDebugExceptionParam> params = new ArrayList<>();
//        for (FastDebugStackInfoCreateRequest log : errorConfigLogs) {
//            Map<String, String> map = new HashMap<>();
//            if (StringUtils.isNotBlank(log.getContent())) {
//                String content = log.getContent();
//                String[] keyValueArray = content.split("\\|");
//                for (String keyValue : keyValueArray) {
//                    if (!keyValue.contains(":")) {
//                        continue;
//                    }
//                    String[] array = keyValue.split(":");
//                    if (array.length == 2) {
//                        map.put(array[0], array[1]);
//                    } else if (array.length > 2) {
//                        String key = keyValue.substring(0, keyValue.indexOf(":"));
//                        String value = keyValue.substring(keyValue.indexOf(":") + 1);
//                        map.put(key, value);
//                    }
//                }
//            }
//            String message = map.get("message");
//            String traceId = map.get("traceId");
//            String rpcId = map.get("rpcId");
//            String errorCode = map.get("errorCode");
//            String url = Optional.ofNullable(map.get("url")).orElse("");
//            User user = tUserMapper.selectByKey(userAppKey);
//            if (null == user) {
//                logger.debug("userAppKey:{} ; {}", RestContext.getTenantUserKey(), "can not find user from db ...");
//                continue;
//            }
//
//            Long customerId = user.getCustomerId();
//            FastDebugExceptionParam param = new FastDebugExceptionParam();
//            List<String> agentIds = Lists.newArrayList();
//            agentIds.add(agentId);
//            param.setAgentId(agentIds);
//            param.setApplicationName(appName);
//            param.setCustomerId(customerId);
//            param.setTraceId(traceId);
//            param.setRpcId(rpcId);
//            param.setCode(errorCode);
//            StringBuilder builder = new StringBuilder();
//            if (StringUtils.isNotBlank(errorCode)) {
//                ExceptionResult exceptionResult = exceptionDao.getByAgentCode(errorCode);
//                if (exceptionResult != null) {
//                    param.setCode(exceptionResult.getCode());
//                    param.setType(exceptionResult.getType());
//                    String des = exceptionResult.getDescription() == null ? "" : exceptionResult.getDescription();
//                    param.setSuggestion(exceptionResult.getSuggestion() + " " + url);
//                    builder.append(des);
//                    builder.append("|");
//                    builder.append(message);
//                    if (!errorCode.equals("whiteList-0001")) {
//                        String detail = Optional.ofNullable(map.get("detail")).orElse("");
//                        builder.append("|" + detail);
//                    }
//                    param.setDescription(builder.toString());
//                } else {
//                    logger.error("异常编码：{} ,在异常表未找到；", errorCode);
//                }
//            }
//            param.setTime(DateUtils.dateToString(new Date(), DateUtils.FORMATE_YMDHMS));
//            params.add(param);
//
//            //去重
//            List<FastDebugExceptionVO> exception = fastDebugDao.getException(traceId, "", appName,
//                param.getDescription());
//            if (CollectionUtils.isNotEmpty(exception)) {
//                continue;
//            } else {
//                fastDebugDao.insertException(param);
//            }
//        }
//
//    }
//
//    private void recordUploadTimes(String traceId) {
//        redisClientUtils.increment(fastDebugLogPathFactory.agentStackUploadTimesKey(traceId), 1);
//    }
//
//    /**
//     * 上传agent log
//     *
//     * @param request
//     */
//    @Override
//    public void uploadAgentLog(FastDebugAgentLogUploadRequest request) {
//        String userAppKey = RestContext.getTenantUserKey();
//        threadPoolExecutor.execute(() -> {
//            doUploadAgentLog(request, userAppKey);
//        });
//    }
//
//    private void doUploadAgentLog(FastDebugAgentLogUploadRequest request, String userAppKey) {
//
//        if (request == null || StringUtils.isBlank(userAppKey)) {
//            return;
//        }
//        UserCacheResult user = allUserCache.getCachedUserByKey(userAppKey);
//        // User user = tUserMapper.selectByKey(userAppKey);
//        if (null == user) {
//            logger.debug("userAppKey:{} ; {}", userAppKey, "can not find user from db ...");
//            return;
//        }
//        Long customerId = user.getCustomerId();
//        List<FastDebugLogReq> logs = request.getLogs();
//        for (FastDebugLogReq log : logs) {
//            String filePath = fastDebugLogPathFactory.agentLogPath(customerId, request.getAppName(),
//                request.getAgentId(), request.getTraceId(), log.getFileName());
//            try {
//                String content = log.getLogContent() == null ? "" : log.getLogContent();
//                FileUtils.appentContent2(filePath, content);
//                //日志上次同步记录，最后一行存储在redis
//                redisClientUtils.setString(
//                    fastDebugLogPathFactory
//                        .agentLogUploadLastLineKey(customerId, request.getAgentId(), request.getTraceId(),
//                            log.getFileName()),
//                    String.valueOf(log.getEndLine()), 15, TimeUnit.DAYS);
//            } catch (Exception e) {
//                logger.error(e.getMessage());
//            }
//            //更新日志记录：
//            redisClientUtils.setString(fastDebugLogPathFactory
//                    .agentLogPullStatusKey(customerId, request.getAppName(), request.getAgentId(), request.getTraceId(),
//                        log.getFileName()),
//                LogPullStatusEnum.PULLED.getName(), 30, TimeUnit.SECONDS);
//        }
//        //更新agent 日志名字获取状态；
//        redisClientUtils.setString(
//            fastDebugLogPathFactory
//                .getAgentLogNamesStatus(customerId, request.getAppName(), request.getAgentId(), request.getTraceId()),
//            LogPullStatusEnum.PULLED.getName());
//    }
//
//    /**
//     * 上传app日志
//     *
//     * @param request
//     */
//    @Override
//    public void uploadAppLog(FastDebugAgentLogUploadRequest request) {
//        String userAppKey = RestContext.getTenantUserKey();
//        threadPoolExecutor.execute(() -> {
//            doUploadAppLog(request, userAppKey);
//        });
//    }
//
//    public void doUploadAppLog(FastDebugAgentLogUploadRequest request, String userAppKey) {
//
//        if (request == null || CollectionUtils.isEmpty(request.getLogs())) {
//            return;
//        }
//        UserCacheResult user = allUserCache.getCachedUserByKey(userAppKey);
//        //  User user = tUserMapper.selectByKey(userAppKey);
//        if (null == user) {
//            logger.debug("userAppKey:{} ; {}", userAppKey, "can not find user from db ...");
//            return;
//        }
//        Long customerId = user.getCustomerId();
//        List<FastDebugLogReq> logs = request.getLogs();
//        for (FastDebugLogReq log : logs) {
//            if (log.getHasLogFile() == false) {
//                //文件不存在
//                redisClientUtils.setString(
//                    fastDebugLogPathFactory.appLogIsExistKey(customerId, request.getAgentId(), log.getFilePath()),
//                    "false", 180, TimeUnit.SECONDS);
//                continue;
//            }
//            redisClientUtils.setString(
//                fastDebugLogPathFactory
//                    .appLogUploadLastLineKey(customerId, request.getAgentId(), request.getTraceId(), log.getFilePath()),
//                String.valueOf(log.getEndLine()), 20, TimeUnit.DAYS);
//            String newPath = log.getFilePath().replace("/", "-");
//            String filePath = fastDebugLogPathFactory.appLogPath(customerId, request.getAppName(), request.getAgentId(),
//                request.getTraceId(), newPath, log.getFileName());
//            try {
//                FileUtils.appentContent2(filePath, log.getLogContent());
//                redisClientUtils.setString(fastDebugLogPathFactory
//                        .appLogPullStatusKey(customerId, request.getAppName(), request.getAgentId(),
//                            request.getTraceId(), log.getFilePath(), log.getFileName()),
//                    LogPullStatusEnum.PULLED.getName());
//            } catch (Exception e) {
//                logger.error(e.getMessage());
//            }
//        }
//    }
//
//    @Override
//    public Integer getStackUploadTimes(String traceId) {
//        if (StringUtils.isBlank(traceId)) {
//            throw new RuntimeException("traceId 不能为空");
//        }
//        String times = redisClientUtils.getString(fastDebugLogPathFactory.agentStackUploadTimesKey(traceId));
//        return times == null ? 0 : Integer.valueOf(times);
//    }
//
//    @Override
//    public void deleteStackUploadTimesKey(String traceId) {
//        if (StringUtils.isBlank(traceId)) {
//            throw new RuntimeException("traceId 不能为空");
//        }
//        redisClientUtils.delete(fastDebugLogPathFactory.agentStackUploadTimesKey(traceId));
//    }
//
//    @Override
//    public Long hasStackErrorLog(String traceId) {
//        FastDebugStackInfoQueryParam param = new FastDebugStackInfoQueryParam();
//        param.setLogLevel(LogLevelEnum.ERROR.getName());
//        param.setTraceId(traceId);
//        param.setIsStack(true);
//        List<FastDebugStackInfoResult> fastDebugStackInfoResults = fastDebugStackInfoDao.selectByExample(param);
//        // 去重
//        return fastDebugStackInfoResults.stream().map(FastDebugStackInfoResult::toString).distinct().count();
//    }
//
//    @Override
//    public List<FastDebugStackInfoOutPut> getErrorStackInfoList(String traceId) {
//        FastDebugStackInfoQueryParam param = new FastDebugStackInfoQueryParam();
//        param.setLogLevel(LogLevelEnum.ERROR.getName());
//        param.setTraceId(traceId);
//        param.setIsStack(true);
//        List<FastDebugStackInfoResult> fastDebugStackInfoResults = fastDebugStackInfoDao.selectByExample(param);
//        // 去重
//        return fastDebugStackInfoResults.stream().distinct().map(result -> {
//            FastDebugStackInfoOutPut outPut = new FastDebugStackInfoOutPut();
//            BeanUtils.copyProperties(result,outPut);
//            return outPut;
//        }).collect(Collectors.toList());
//    }
//
//    @Override
//    public FastDebugStackInfoOutPut getById(Long id) {
//        FastDebugStackInfoResult result = fastDebugStackInfoDao.getById(id);
//        if(result == null) {
//            return null;
//        }
//        FastDebugStackInfoOutPut outPut = new FastDebugStackInfoOutPut();
//        BeanUtils.copyProperties(result,outPut);
//        return outPut;
//    }
//}
