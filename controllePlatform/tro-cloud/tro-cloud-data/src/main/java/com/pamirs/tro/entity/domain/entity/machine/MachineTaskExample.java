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

package com.pamirs.tro.entity.domain.entity.machine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MachineTaskExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MachineTaskExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria)this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria)this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria)this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria)this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria)this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria)this;
        }

        public Criteria andTaskTypeIsNull() {
            addCriterion("task_type is null");
            return (Criteria)this;
        }

        public Criteria andTaskTypeIsNotNull() {
            addCriterion("task_type is not null");
            return (Criteria)this;
        }

        public Criteria andTaskTypeEqualTo(Integer value) {
            addCriterion("task_type =", value, "taskType");
            return (Criteria)this;
        }

        public Criteria andTaskTypeNotEqualTo(Integer value) {
            addCriterion("task_type <>", value, "taskType");
            return (Criteria)this;
        }

        public Criteria andTaskTypeGreaterThan(Integer value) {
            addCriterion("task_type >", value, "taskType");
            return (Criteria)this;
        }

        public Criteria andTaskTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("task_type >=", value, "taskType");
            return (Criteria)this;
        }

        public Criteria andTaskTypeLessThan(Integer value) {
            addCriterion("task_type <", value, "taskType");
            return (Criteria)this;
        }

        public Criteria andTaskTypeLessThanOrEqualTo(Integer value) {
            addCriterion("task_type <=", value, "taskType");
            return (Criteria)this;
        }

        public Criteria andTaskTypeIn(List<Integer> values) {
            addCriterion("task_type in", values, "taskType");
            return (Criteria)this;
        }

        public Criteria andTaskTypeNotIn(List<Integer> values) {
            addCriterion("task_type not in", values, "taskType");
            return (Criteria)this;
        }

        public Criteria andTaskTypeBetween(Integer value1, Integer value2) {
            addCriterion("task_type between", value1, value2, "taskType");
            return (Criteria)this;
        }

        public Criteria andTaskTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("task_type not between", value1, value2, "taskType");
            return (Criteria)this;
        }

        public Criteria andSerialNoIsNull() {
            addCriterion("serial_no is null");
            return (Criteria)this;
        }

        public Criteria andSerialNoIsNotNull() {
            addCriterion("serial_no is not null");
            return (Criteria)this;
        }

        public Criteria andSerialNoEqualTo(String value) {
            addCriterion("serial_no =", value, "serialNo");
            return (Criteria)this;
        }

        public Criteria andSerialNoNotEqualTo(String value) {
            addCriterion("serial_no <>", value, "serialNo");
            return (Criteria)this;
        }

        public Criteria andSerialNoGreaterThan(String value) {
            addCriterion("serial_no >", value, "serialNo");
            return (Criteria)this;
        }

        public Criteria andSerialNoGreaterThanOrEqualTo(String value) {
            addCriterion("serial_no >=", value, "serialNo");
            return (Criteria)this;
        }

        public Criteria andSerialNoLessThan(String value) {
            addCriterion("serial_no <", value, "serialNo");
            return (Criteria)this;
        }

        public Criteria andSerialNoLessThanOrEqualTo(String value) {
            addCriterion("serial_no <=", value, "serialNo");
            return (Criteria)this;
        }

        public Criteria andSerialNoLike(String value) {
            addCriterion("serial_no like", value, "serialNo");
            return (Criteria)this;
        }

        public Criteria andSerialNoNotLike(String value) {
            addCriterion("serial_no not like", value, "serialNo");
            return (Criteria)this;
        }

        public Criteria andSerialNoIn(List<String> values) {
            addCriterion("serial_no in", values, "serialNo");
            return (Criteria)this;
        }

        public Criteria andSerialNoNotIn(List<String> values) {
            addCriterion("serial_no not in", values, "serialNo");
            return (Criteria)this;
        }

        public Criteria andSerialNoBetween(String value1, String value2) {
            addCriterion("serial_no between", value1, value2, "serialNo");
            return (Criteria)this;
        }

        public Criteria andSerialNoNotBetween(String value1, String value2) {
            addCriterion("serial_no not between", value1, value2, "serialNo");
            return (Criteria)this;
        }

        public Criteria andPlatformIdIsNull() {
            addCriterion("platform_id is null");
            return (Criteria)this;
        }

        public Criteria andPlatformIdIsNotNull() {
            addCriterion("platform_id is not null");
            return (Criteria)this;
        }

        public Criteria andPlatformIdEqualTo(Long value) {
            addCriterion("platform_id =", value, "platformId");
            return (Criteria)this;
        }

        public Criteria andPlatformIdNotEqualTo(Long value) {
            addCriterion("platform_id <>", value, "platformId");
            return (Criteria)this;
        }

        public Criteria andPlatformIdGreaterThan(Long value) {
            addCriterion("platform_id >", value, "platformId");
            return (Criteria)this;
        }

        public Criteria andPlatformIdGreaterThanOrEqualTo(Long value) {
            addCriterion("platform_id >=", value, "platformId");
            return (Criteria)this;
        }

        public Criteria andPlatformIdLessThan(Long value) {
            addCriterion("platform_id <", value, "platformId");
            return (Criteria)this;
        }

        public Criteria andPlatformIdLessThanOrEqualTo(Long value) {
            addCriterion("platform_id <=", value, "platformId");
            return (Criteria)this;
        }

        public Criteria andPlatformIdIn(List<Long> values) {
            addCriterion("platform_id in", values, "platformId");
            return (Criteria)this;
        }

        public Criteria andPlatformIdNotIn(List<Long> values) {
            addCriterion("platform_id not in", values, "platformId");
            return (Criteria)this;
        }

        public Criteria andPlatformIdBetween(Long value1, Long value2) {
            addCriterion("platform_id between", value1, value2, "platformId");
            return (Criteria)this;
        }

        public Criteria andPlatformIdNotBetween(Long value1, Long value2) {
            addCriterion("platform_id not between", value1, value2, "platformId");
            return (Criteria)this;
        }

        public Criteria andPlatformNameIsNull() {
            addCriterion("platform_name is null");
            return (Criteria)this;
        }

        public Criteria andPlatformNameIsNotNull() {
            addCriterion("platform_name is not null");
            return (Criteria)this;
        }

        public Criteria andPlatformNameEqualTo(String value) {
            addCriterion("platform_name =", value, "platformName");
            return (Criteria)this;
        }

        public Criteria andPlatformNameNotEqualTo(String value) {
            addCriterion("platform_name <>", value, "platformName");
            return (Criteria)this;
        }

        public Criteria andPlatformNameGreaterThan(String value) {
            addCriterion("platform_name >", value, "platformName");
            return (Criteria)this;
        }

        public Criteria andPlatformNameGreaterThanOrEqualTo(String value) {
            addCriterion("platform_name >=", value, "platformName");
            return (Criteria)this;
        }

        public Criteria andPlatformNameLessThan(String value) {
            addCriterion("platform_name <", value, "platformName");
            return (Criteria)this;
        }

        public Criteria andPlatformNameLessThanOrEqualTo(String value) {
            addCriterion("platform_name <=", value, "platformName");
            return (Criteria)this;
        }

        public Criteria andPlatformNameLike(String value) {
            addCriterion("platform_name like", value, "platformName");
            return (Criteria)this;
        }

        public Criteria andPlatformNameNotLike(String value) {
            addCriterion("platform_name not like", value, "platformName");
            return (Criteria)this;
        }

        public Criteria andPlatformNameIn(List<String> values) {
            addCriterion("platform_name in", values, "platformName");
            return (Criteria)this;
        }

        public Criteria andPlatformNameNotIn(List<String> values) {
            addCriterion("platform_name not in", values, "platformName");
            return (Criteria)this;
        }

        public Criteria andPlatformNameBetween(String value1, String value2) {
            addCriterion("platform_name between", value1, value2, "platformName");
            return (Criteria)this;
        }

        public Criteria andPlatformNameNotBetween(String value1, String value2) {
            addCriterion("platform_name not between", value1, value2, "platformName");
            return (Criteria)this;
        }

        public Criteria andAccountIdIsNull() {
            addCriterion("account_id is null");
            return (Criteria)this;
        }

        public Criteria andAccountIdIsNotNull() {
            addCriterion("account_id is not null");
            return (Criteria)this;
        }

        public Criteria andAccountIdEqualTo(Long value) {
            addCriterion("account_id =", value, "accountId");
            return (Criteria)this;
        }

        public Criteria andAccountIdNotEqualTo(Long value) {
            addCriterion("account_id <>", value, "accountId");
            return (Criteria)this;
        }

        public Criteria andAccountIdGreaterThan(Long value) {
            addCriterion("account_id >", value, "accountId");
            return (Criteria)this;
        }

        public Criteria andAccountIdGreaterThanOrEqualTo(Long value) {
            addCriterion("account_id >=", value, "accountId");
            return (Criteria)this;
        }

        public Criteria andAccountIdLessThan(Long value) {
            addCriterion("account_id <", value, "accountId");
            return (Criteria)this;
        }

        public Criteria andAccountIdLessThanOrEqualTo(Long value) {
            addCriterion("account_id <=", value, "accountId");
            return (Criteria)this;
        }

        public Criteria andAccountIdIn(List<Long> values) {
            addCriterion("account_id in", values, "accountId");
            return (Criteria)this;
        }

        public Criteria andAccountIdNotIn(List<Long> values) {
            addCriterion("account_id not in", values, "accountId");
            return (Criteria)this;
        }

        public Criteria andAccountIdBetween(Long value1, Long value2) {
            addCriterion("account_id between", value1, value2, "accountId");
            return (Criteria)this;
        }

        public Criteria andAccountIdNotBetween(Long value1, Long value2) {
            addCriterion("account_id not between", value1, value2, "accountId");
            return (Criteria)this;
        }

        public Criteria andAccountNameIsNull() {
            addCriterion("account_name is null");
            return (Criteria)this;
        }

        public Criteria andAccountNameIsNotNull() {
            addCriterion("account_name is not null");
            return (Criteria)this;
        }

        public Criteria andAccountNameEqualTo(String value) {
            addCriterion("account_name =", value, "accountName");
            return (Criteria)this;
        }

        public Criteria andAccountNameNotEqualTo(String value) {
            addCriterion("account_name <>", value, "accountName");
            return (Criteria)this;
        }

        public Criteria andAccountNameGreaterThan(String value) {
            addCriterion("account_name >", value, "accountName");
            return (Criteria)this;
        }

        public Criteria andAccountNameGreaterThanOrEqualTo(String value) {
            addCriterion("account_name >=", value, "accountName");
            return (Criteria)this;
        }

        public Criteria andAccountNameLessThan(String value) {
            addCriterion("account_name <", value, "accountName");
            return (Criteria)this;
        }

        public Criteria andAccountNameLessThanOrEqualTo(String value) {
            addCriterion("account_name <=", value, "accountName");
            return (Criteria)this;
        }

        public Criteria andAccountNameLike(String value) {
            addCriterion("account_name like", value, "accountName");
            return (Criteria)this;
        }

        public Criteria andAccountNameNotLike(String value) {
            addCriterion("account_name not like", value, "accountName");
            return (Criteria)this;
        }

        public Criteria andAccountNameIn(List<String> values) {
            addCriterion("account_name in", values, "accountName");
            return (Criteria)this;
        }

        public Criteria andAccountNameNotIn(List<String> values) {
            addCriterion("account_name not in", values, "accountName");
            return (Criteria)this;
        }

        public Criteria andAccountNameBetween(String value1, String value2) {
            addCriterion("account_name between", value1, value2, "accountName");
            return (Criteria)this;
        }

        public Criteria andAccountNameNotBetween(String value1, String value2) {
            addCriterion("account_name not between", value1, value2, "accountName");
            return (Criteria)this;
        }

        public Criteria andSpecIdIsNull() {
            addCriterion("spec_id is null");
            return (Criteria)this;
        }

        public Criteria andSpecIdIsNotNull() {
            addCriterion("spec_id is not null");
            return (Criteria)this;
        }

        public Criteria andSpecIdEqualTo(Long value) {
            addCriterion("spec_id =", value, "specId");
            return (Criteria)this;
        }

        public Criteria andSpecIdNotEqualTo(Long value) {
            addCriterion("spec_id <>", value, "specId");
            return (Criteria)this;
        }

        public Criteria andSpecIdGreaterThan(Long value) {
            addCriterion("spec_id >", value, "specId");
            return (Criteria)this;
        }

        public Criteria andSpecIdGreaterThanOrEqualTo(Long value) {
            addCriterion("spec_id >=", value, "specId");
            return (Criteria)this;
        }

        public Criteria andSpecIdLessThan(Long value) {
            addCriterion("spec_id <", value, "specId");
            return (Criteria)this;
        }

        public Criteria andSpecIdLessThanOrEqualTo(Long value) {
            addCriterion("spec_id <=", value, "specId");
            return (Criteria)this;
        }

        public Criteria andSpecIdIn(List<Long> values) {
            addCriterion("spec_id in", values, "specId");
            return (Criteria)this;
        }

        public Criteria andSpecIdNotIn(List<Long> values) {
            addCriterion("spec_id not in", values, "specId");
            return (Criteria)this;
        }

        public Criteria andSpecIdBetween(Long value1, Long value2) {
            addCriterion("spec_id between", value1, value2, "specId");
            return (Criteria)this;
        }

        public Criteria andSpecIdNotBetween(Long value1, Long value2) {
            addCriterion("spec_id not between", value1, value2, "specId");
            return (Criteria)this;
        }

        public Criteria andSpecIsNull() {
            addCriterion("spec is null");
            return (Criteria)this;
        }

        public Criteria andSpecIsNotNull() {
            addCriterion("spec is not null");
            return (Criteria)this;
        }

        public Criteria andSpecEqualTo(String value) {
            addCriterion("spec =", value, "spec");
            return (Criteria)this;
        }

        public Criteria andSpecNotEqualTo(String value) {
            addCriterion("spec <>", value, "spec");
            return (Criteria)this;
        }

        public Criteria andSpecGreaterThan(String value) {
            addCriterion("spec >", value, "spec");
            return (Criteria)this;
        }

        public Criteria andSpecGreaterThanOrEqualTo(String value) {
            addCriterion("spec >=", value, "spec");
            return (Criteria)this;
        }

        public Criteria andSpecLessThan(String value) {
            addCriterion("spec <", value, "spec");
            return (Criteria)this;
        }

        public Criteria andSpecLessThanOrEqualTo(String value) {
            addCriterion("spec <=", value, "spec");
            return (Criteria)this;
        }

        public Criteria andSpecLike(String value) {
            addCriterion("spec like", value, "spec");
            return (Criteria)this;
        }

        public Criteria andSpecNotLike(String value) {
            addCriterion("spec not like", value, "spec");
            return (Criteria)this;
        }

        public Criteria andSpecIn(List<String> values) {
            addCriterion("spec in", values, "spec");
            return (Criteria)this;
        }

        public Criteria andSpecNotIn(List<String> values) {
            addCriterion("spec not in", values, "spec");
            return (Criteria)this;
        }

        public Criteria andSpecBetween(String value1, String value2) {
            addCriterion("spec between", value1, value2, "spec");
            return (Criteria)this;
        }

        public Criteria andSpecNotBetween(String value1, String value2) {
            addCriterion("spec not between", value1, value2, "spec");
            return (Criteria)this;
        }

        public Criteria andOpenTypeIsNull() {
            addCriterion("open_type is null");
            return (Criteria)this;
        }

        public Criteria andOpenTypeIsNotNull() {
            addCriterion("open_type is not null");
            return (Criteria)this;
        }

        public Criteria andOpenTypeEqualTo(Integer value) {
            addCriterion("open_type =", value, "openType");
            return (Criteria)this;
        }

        public Criteria andOpenTypeNotEqualTo(Integer value) {
            addCriterion("open_type <>", value, "openType");
            return (Criteria)this;
        }

        public Criteria andOpenTypeGreaterThan(Integer value) {
            addCriterion("open_type >", value, "openType");
            return (Criteria)this;
        }

        public Criteria andOpenTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("open_type >=", value, "openType");
            return (Criteria)this;
        }

        public Criteria andOpenTypeLessThan(Integer value) {
            addCriterion("open_type <", value, "openType");
            return (Criteria)this;
        }

        public Criteria andOpenTypeLessThanOrEqualTo(Integer value) {
            addCriterion("open_type <=", value, "openType");
            return (Criteria)this;
        }

        public Criteria andOpenTypeIn(List<Integer> values) {
            addCriterion("open_type in", values, "openType");
            return (Criteria)this;
        }

        public Criteria andOpenTypeNotIn(List<Integer> values) {
            addCriterion("open_type not in", values, "openType");
            return (Criteria)this;
        }

        public Criteria andOpenTypeBetween(Integer value1, Integer value2) {
            addCriterion("open_type between", value1, value2, "openType");
            return (Criteria)this;
        }

        public Criteria andOpenTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("open_type not between", value1, value2, "openType");
            return (Criteria)this;
        }

        public Criteria andOpenTimeIsNull() {
            addCriterion("open_time is null");
            return (Criteria)this;
        }

        public Criteria andOpenTimeIsNotNull() {
            addCriterion("open_time is not null");
            return (Criteria)this;
        }

        public Criteria andOpenTimeEqualTo(Integer value) {
            addCriterion("open_time =", value, "openTime");
            return (Criteria)this;
        }

        public Criteria andOpenTimeNotEqualTo(Integer value) {
            addCriterion("open_time <>", value, "openTime");
            return (Criteria)this;
        }

        public Criteria andOpenTimeGreaterThan(Integer value) {
            addCriterion("open_time >", value, "openTime");
            return (Criteria)this;
        }

        public Criteria andOpenTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("open_time >=", value, "openTime");
            return (Criteria)this;
        }

        public Criteria andOpenTimeLessThan(Integer value) {
            addCriterion("open_time <", value, "openTime");
            return (Criteria)this;
        }

        public Criteria andOpenTimeLessThanOrEqualTo(Integer value) {
            addCriterion("open_time <=", value, "openTime");
            return (Criteria)this;
        }

        public Criteria andOpenTimeIn(List<Integer> values) {
            addCriterion("open_time in", values, "openTime");
            return (Criteria)this;
        }

        public Criteria andOpenTimeNotIn(List<Integer> values) {
            addCriterion("open_time not in", values, "openTime");
            return (Criteria)this;
        }

        public Criteria andOpenTimeBetween(Integer value1, Integer value2) {
            addCriterion("open_time between", value1, value2, "openTime");
            return (Criteria)this;
        }

        public Criteria andOpenTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("open_time not between", value1, value2, "openTime");
            return (Criteria)this;
        }

        public Criteria andMachineNumIsNull() {
            addCriterion("machine_num is null");
            return (Criteria)this;
        }

        public Criteria andMachineNumIsNotNull() {
            addCriterion("machine_num is not null");
            return (Criteria)this;
        }

        public Criteria andMachineNumEqualTo(Integer value) {
            addCriterion("machine_num =", value, "machineNum");
            return (Criteria)this;
        }

        public Criteria andMachineNumNotEqualTo(Integer value) {
            addCriterion("machine_num <>", value, "machineNum");
            return (Criteria)this;
        }

        public Criteria andMachineNumGreaterThan(Integer value) {
            addCriterion("machine_num >", value, "machineNum");
            return (Criteria)this;
        }

        public Criteria andMachineNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("machine_num >=", value, "machineNum");
            return (Criteria)this;
        }

        public Criteria andMachineNumLessThan(Integer value) {
            addCriterion("machine_num <", value, "machineNum");
            return (Criteria)this;
        }

        public Criteria andMachineNumLessThanOrEqualTo(Integer value) {
            addCriterion("machine_num <=", value, "machineNum");
            return (Criteria)this;
        }

        public Criteria andMachineNumIn(List<Integer> values) {
            addCriterion("machine_num in", values, "machineNum");
            return (Criteria)this;
        }

        public Criteria andMachineNumNotIn(List<Integer> values) {
            addCriterion("machine_num not in", values, "machineNum");
            return (Criteria)this;
        }

        public Criteria andMachineNumBetween(Integer value1, Integer value2) {
            addCriterion("machine_num between", value1, value2, "machineNum");
            return (Criteria)this;
        }

        public Criteria andMachineNumNotBetween(Integer value1, Integer value2) {
            addCriterion("machine_num not between", value1, value2, "machineNum");
            return (Criteria)this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria)this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria)this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria)this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria)this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria)this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria)this;
        }

        public Criteria andIsDeleteIsNull() {
            addCriterion("is_delete is null");
            return (Criteria)this;
        }

        public Criteria andIsDeleteIsNotNull() {
            addCriterion("is_delete is not null");
            return (Criteria)this;
        }

        public Criteria andIsDeleteEqualTo(Boolean value) {
            addCriterion("is_delete =", value, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteNotEqualTo(Boolean value) {
            addCriterion("is_delete <>", value, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteGreaterThan(Boolean value) {
            addCriterion("is_delete >", value, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_delete >=", value, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteLessThan(Boolean value) {
            addCriterion("is_delete <", value, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteLessThanOrEqualTo(Boolean value) {
            addCriterion("is_delete <=", value, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteIn(List<Boolean> values) {
            addCriterion("is_delete in", values, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteNotIn(List<Boolean> values) {
            addCriterion("is_delete not in", values, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteBetween(Boolean value1, Boolean value2) {
            addCriterion("is_delete between", value1, value2, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_delete not between", value1, value2, "isDelete");
            return (Criteria)this;
        }

        public Criteria andGmtCreateIsNull() {
            addCriterion("gmt_create is null");
            return (Criteria)this;
        }

        public Criteria andGmtCreateIsNotNull() {
            addCriterion("gmt_create is not null");
            return (Criteria)this;
        }

        public Criteria andGmtCreateEqualTo(Date value) {
            addCriterion("gmt_create =", value, "gmtCreate");
            return (Criteria)this;
        }

        public Criteria andGmtCreateNotEqualTo(Date value) {
            addCriterion("gmt_create <>", value, "gmtCreate");
            return (Criteria)this;
        }

        public Criteria andGmtCreateGreaterThan(Date value) {
            addCriterion("gmt_create >", value, "gmtCreate");
            return (Criteria)this;
        }

        public Criteria andGmtCreateGreaterThanOrEqualTo(Date value) {
            addCriterion("gmt_create >=", value, "gmtCreate");
            return (Criteria)this;
        }

        public Criteria andGmtCreateLessThan(Date value) {
            addCriterion("gmt_create <", value, "gmtCreate");
            return (Criteria)this;
        }

        public Criteria andGmtCreateLessThanOrEqualTo(Date value) {
            addCriterion("gmt_create <=", value, "gmtCreate");
            return (Criteria)this;
        }

        public Criteria andGmtCreateIn(List<Date> values) {
            addCriterion("gmt_create in", values, "gmtCreate");
            return (Criteria)this;
        }

        public Criteria andGmtCreateNotIn(List<Date> values) {
            addCriterion("gmt_create not in", values, "gmtCreate");
            return (Criteria)this;
        }

        public Criteria andGmtCreateBetween(Date value1, Date value2) {
            addCriterion("gmt_create between", value1, value2, "gmtCreate");
            return (Criteria)this;
        }

        public Criteria andGmtCreateNotBetween(Date value1, Date value2) {
            addCriterion("gmt_create not between", value1, value2, "gmtCreate");
            return (Criteria)this;
        }

        public Criteria andGmtUpdateIsNull() {
            addCriterion("gmt_update is null");
            return (Criteria)this;
        }

        public Criteria andGmtUpdateIsNotNull() {
            addCriterion("gmt_update is not null");
            return (Criteria)this;
        }

        public Criteria andGmtUpdateEqualTo(Date value) {
            addCriterion("gmt_update =", value, "gmtUpdate");
            return (Criteria)this;
        }

        public Criteria andGmtUpdateNotEqualTo(Date value) {
            addCriterion("gmt_update <>", value, "gmtUpdate");
            return (Criteria)this;
        }

        public Criteria andGmtUpdateGreaterThan(Date value) {
            addCriterion("gmt_update >", value, "gmtUpdate");
            return (Criteria)this;
        }

        public Criteria andGmtUpdateGreaterThanOrEqualTo(Date value) {
            addCriterion("gmt_update >=", value, "gmtUpdate");
            return (Criteria)this;
        }

        public Criteria andGmtUpdateLessThan(Date value) {
            addCriterion("gmt_update <", value, "gmtUpdate");
            return (Criteria)this;
        }

        public Criteria andGmtUpdateLessThanOrEqualTo(Date value) {
            addCriterion("gmt_update <=", value, "gmtUpdate");
            return (Criteria)this;
        }

        public Criteria andGmtUpdateIn(List<Date> values) {
            addCriterion("gmt_update in", values, "gmtUpdate");
            return (Criteria)this;
        }

        public Criteria andGmtUpdateNotIn(List<Date> values) {
            addCriterion("gmt_update not in", values, "gmtUpdate");
            return (Criteria)this;
        }

        public Criteria andGmtUpdateBetween(Date value1, Date value2) {
            addCriterion("gmt_update between", value1, value2, "gmtUpdate");
            return (Criteria)this;
        }

        public Criteria andGmtUpdateNotBetween(Date value1, Date value2) {
            addCriterion("gmt_update not between", value1, value2, "gmtUpdate");
            return (Criteria)this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }
    }
}
