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

package io.shulie.tro.cloud.biz.service.settle.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.pamirs.tro.entity.dao.settle.TAccountBalanceMapper;
import com.pamirs.tro.entity.dao.settle.TAccountBookMapper;
import com.pamirs.tro.entity.domain.entity.settle.AccountBalance;
import com.pamirs.tro.entity.domain.entity.settle.AccountBook;
import com.pamirs.tro.entity.domain.vo.settle.AccountTradeRequest;
import io.shulie.tro.cloud.biz.service.settle.SettleService;
import io.shulie.tro.cloud.common.bean.FlowVO;
import io.shulie.tro.cloud.common.bean.TimeBean;
import io.shulie.tro.cloud.common.enums.account.AccountSceneCodeEnum;
import io.shulie.tro.cloud.common.exception.ApiException;
import io.shulie.tro.cloud.common.utils.FlowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author 莫问
 * @Date 2020-05-14
 */
@Service
@Slf4j
public class SettleServiceImpl implements SettleService {

    @Resource
    private TAccountBookMapper TAccountBookMapper;

    @Resource
    private TAccountBalanceMapper TAccountBalanceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockAccount(AccountTradeRequest request) {
        BigDecimal amount = calcEstimateAmount(request);
        if (amount.compareTo(new BigDecimal(0)) < 0) {
            log.warn("流量计算异常，扣款为负数：{}", JSON.toJSONString(request));
            throw ApiException.create(500, "流量计算异常，扣款为负数");
        }
        AccountBook accountBook = TAccountBookMapper.selectOneByUserId(request.getUid());
        if (accountBook == null || accountBook.getBalance().compareTo(amount) < 0) {
            log.warn("账户余额不足，预冻结失败");
            throw ApiException.create(500, "账户余额不足，预冻结失败");
        }
        accountBook.setBalance(accountBook.getBalance().subtract(amount));
        accountBook.setLockBalance(accountBook.getLockBalance().add(amount));
        accountBook.setGmtUpdate(new Date());
        TAccountBookMapper.updateByPrimaryKeySelective(accountBook);

        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setDirect(1);
        accountBalance.setSceneCode(AccountSceneCodeEnum.PRE_LOCK.getCode());
        accountBalance.setAmount(amount);
        accountBalance.setBalance(accountBook.getBalance());
        accountBalance.setLockBalance(accountBook.getLockBalance());
        accountBalance.setAccId(accountBook.getAccId());
        accountBalance.setBookId(accountBook.getId());
        accountBalance.setOuterId(request.getTaskId().toString());
        accountBalance.setStatus(0);
        TAccountBalanceMapper.insertSelective(accountBalance);

    }



    @Override
    @Transactional
    public void unLockAccount(Long uid, String outerId) {
        AccountBalance ab = TAccountBalanceMapper.getAccountBalance(outerId, AccountSceneCodeEnum.PRE_LOCK.getCode());
        if (ab != null) {
            AccountBook accountBook = TAccountBookMapper.selectOneByUserId(uid);
            accountBook.setBalance(accountBook.getBalance().add(ab.getAmount()));
            accountBook.setLockBalance(accountBook.getLockBalance().subtract(ab.getAmount()));
            accountBook.setGmtUpdate(new Date());
            TAccountBookMapper.updateByPrimaryKeySelective(accountBook);

            AccountBalance accountBalance = new AccountBalance();
            accountBalance.setDirect(0);
            accountBalance.setSceneCode(AccountSceneCodeEnum.UN_PRE_LOCK.getCode());
            accountBalance.setAmount(ab.getAmount());
            accountBalance.setBalance(accountBook.getBalance());
            accountBalance.setLockBalance(accountBook.getLockBalance());
            accountBalance.setAccId(accountBook.getAccId());
            accountBalance.setBookId(accountBook.getId());
            accountBalance.setOuterId(outerId);
            accountBalance.setStatus(0);
            TAccountBalanceMapper.insertSelective(accountBalance);
        }

    }

    @Override
    @Transactional
    public BigDecimal settle(AccountTradeRequest request) {
        String outerId = request.getTaskId().toString();
        //先解冻
        unLockAccount(request.getUid(), outerId);

        BigDecimal amount = calcAmount(request);
        log.info("流量结算，本次共计花费：{}", amount);
        AccountBook accountBook = TAccountBookMapper.selectOneByUserId(request.getUid());
        accountBook.setBalance(accountBook.getBalance().subtract(amount));
        accountBook.setTotalBalance(accountBook.getTotalBalance().subtract(amount));
        accountBook.setGmtUpdate(new Date());
        TAccountBookMapper.updateByPrimaryKeySelective(accountBook);

        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setDirect(1);
        accountBalance.setSceneCode(AccountSceneCodeEnum.SETTLE.getCode());
        accountBalance.setAmount(amount);
        accountBalance.setBalance(accountBook.getBalance());
        accountBalance.setLockBalance(accountBook.getLockBalance());
        accountBalance.setAccId(accountBook.getAccId());
        accountBalance.setBookId(accountBook.getId());
        accountBalance.setOuterId(request.getTaskId().toString());
        accountBalance.setStatus(0);
        TAccountBalanceMapper.insertSelective(accountBalance);

        return amount;
    }

    private BigDecimal calcEstimateAmount(AccountTradeRequest accountTradeRequest) {
        FlowVO flow = new FlowVO();
        flow.setConcurrenceNum(accountTradeRequest.getExpectThroughput());
        flow.setPressureTestTime(new TimeBean(accountTradeRequest.getPressureTotalTime(), "s"));
        flow.setPressureMode(accountTradeRequest.getPressureMode());
        flow.setIncreasingTime(new TimeBean(accountTradeRequest.getIncreasingTime(), "s"));
        flow.setStep(accountTradeRequest.getStep());
        flow.setPressureType(accountTradeRequest.getPressureType());
        log.warn("流量计算:{}", flow.toString());
        return FlowUtil.calcEstimateFlow(flow);
    }

    /**
     * 计算总时长
     *
     * @param accountTradeRequest
     * @return
     */
    private BigDecimal calcAmount(AccountTradeRequest accountTradeRequest) {
        FlowVO flow = new FlowVO();
        flow.setConcurrenceNum(accountTradeRequest.getExpectThroughput());
        flow.setPressureTestTime(new TimeBean(accountTradeRequest.getPressureTotalTime(), "s"));
        flow.setPressureMode(accountTradeRequest.getPressureMode());
        flow.setIncreasingTime(new TimeBean(accountTradeRequest.getIncreasingTime(), "s"));
        flow.setStep(accountTradeRequest.getStep());
        flow.setAvgConcurrent(accountTradeRequest.getAvgConcurrent());
        flow.setPressureType(accountTradeRequest.getPressureType());
        log.warn("流量计算:{}", flow.toString());
        return FlowUtil.calcRealityFlow(flow);
    }

}
