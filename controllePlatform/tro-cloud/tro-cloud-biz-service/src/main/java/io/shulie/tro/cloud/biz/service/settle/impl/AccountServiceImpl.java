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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.tro.entity.dao.settle.TAccountBalanceMapper;
import com.pamirs.tro.entity.dao.settle.TAccountBookMapper;
import com.pamirs.tro.entity.dao.settle.TAccountMapper;
import com.pamirs.tro.entity.domain.dto.settle.AccountBalanceDTO;
import com.pamirs.tro.entity.domain.entity.settle.Account;
import com.pamirs.tro.entity.domain.entity.settle.AccountBalance;
import com.pamirs.tro.entity.domain.entity.settle.AccountBook;
import com.pamirs.tro.entity.domain.vo.settle.AccountBalanceQueryVO;
import com.pamirs.tro.entity.domain.vo.settle.RechargeVO;
import io.shulie.tro.cloud.biz.service.settle.AccountService;
import io.shulie.tro.cloud.common.enums.DirectEnum;
import io.shulie.tro.cloud.common.enums.account.AccountSceneCodeEnum;
import io.shulie.tro.cloud.common.exception.ApiException;
import io.shulie.tro.cloud.common.utils.DateUtil;
import io.shulie.tro.cloud.common.utils.SettleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * @ClassName AccountServiceImpl
 * @Description
 * @Author qianshui
 * @Date 2020/5/9 下午4:22
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private TAccountMapper TAccountMapper;

    @Resource
    private TAccountBookMapper TAccountBookMapper;

    @Resource
    private TAccountBalanceMapper TAccountBalanceMapper;

    @Override
    public void initAccount(Long userId) {
        Account account = new Account();
        account.setUid(userId);
        TAccountMapper.insertSelective(account);

        AccountBook book = new AccountBook();
        book.setAccId(account.getId());
        book.setUid(userId);
        TAccountBookMapper.insertSelective(book);
    }

    @Override
    public AccountBook getAccountByUserId(Long userId) {
        return TAccountBookMapper.selectOneByUserId(userId);
    }

    @Override
    public List<AccountBook> getAccountBookByUserIds(List<Long> userIds) {
        return TAccountBookMapper.selectByUserIds(userIds);
    }

    @Override
    public void rechargeAccount(RechargeVO vo) {
        AccountBook book = getAccountByUserId(vo.getCustomId());
        if (book == null) {
            throw ApiException.create(500, "无账本信息");
        }
        AccountBalance balance = new AccountBalance();
        balance.setAccId(book.getAccId());
        balance.setBookId(book.getId());
        balance.setAmount(vo.getFlowAmount().abs());
        balance.setBalance(vo.getFlowAmount().add(book.getBalance()));
        balance.setLockBalance(book.getLockBalance());
        balance.setParentBookId(book.getParentBookId());
        balance.setDirect(vo.getFlowAmount().compareTo(BigDecimal.ZERO) >= 0 ? DirectEnum.FORWARD.getValue()
            : DirectEnum.BACK.getValue());
        balance.setSceneCode(AccountSceneCodeEnum.RECHARGE.getCode());
        Map<String, Object> features = Maps.newHashMap();
        features.put("cashAmount", vo.getCashAmount());
        balance.setFeatures(JSON.toJSONString(features));
        TAccountBalanceMapper.insertSelective(balance);

        book.setBalance(balance.getBalance());
        book.setTotalBalance(book.getBalance().add(book.getLockBalance()));
        TAccountBookMapper.updateByPrimaryKeySelective(book);
    }

    @Override
    public PageInfo<AccountBalanceDTO> getPageList(AccountBalanceQueryVO queryVO) {
        AccountBook book = getAccountByUserId(queryVO.getCustomId());
        if (book == null) {
            throw ApiException.create(500, "无账本信息");
        }
        Page page = PageHelper.startPage(queryVO.getCurrentPage() + 1, queryVO.getPageSize());
        queryVO.setBookId(book.getId());
        List<AccountBalance> queryList = TAccountBalanceMapper.getPageList(queryVO);
        if (CollectionUtils.isEmpty(queryList)) {
            return new PageInfo<>(Lists.newArrayList());
        }
        List<AccountBalanceDTO> resultList = Lists.newArrayList();
        queryList.forEach(data -> {
            AccountBalanceDTO dto = new AccountBalanceDTO();
            dto.setGmtCreate(DateUtil.getYYYYMMDDHHMMSS(data.getGmtCreate()));
            dto.setFlowAmount(SettleUtil.direct(data.getDirect()) + SettleUtil.format(data.getAmount()));
            dto.setLeftAmount(SettleUtil.format(data.getBalance()));
            dto.setSceneCode(AccountSceneCodeEnum.getNameByCode(data.getSceneCode()));
            dto.setRemark(data.getRemark());
            resultList.add(dto);
        });

        PageInfo pageInfo = new PageInfo<>(resultList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }
}

