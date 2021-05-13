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

public class PressureMachineExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PressureMachineExample() {
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

        public Criteria andTaskIdIsNull() {
            addCriterion("task_id is null");
            return (Criteria)this;
        }

        public Criteria andTaskIdIsNotNull() {
            addCriterion("task_id is not null");
            return (Criteria)this;
        }

        public Criteria andTaskIdEqualTo(Long value) {
            addCriterion("task_id =", value, "taskId");
            return (Criteria)this;
        }

        public Criteria andTaskIdNotEqualTo(Long value) {
            addCriterion("task_id <>", value, "taskId");
            return (Criteria)this;
        }

        public Criteria andTaskIdGreaterThan(Long value) {
            addCriterion("task_id >", value, "taskId");
            return (Criteria)this;
        }

        public Criteria andTaskIdGreaterThanOrEqualTo(Long value) {
            addCriterion("task_id >=", value, "taskId");
            return (Criteria)this;
        }

        public Criteria andTaskIdLessThan(Long value) {
            addCriterion("task_id <", value, "taskId");
            return (Criteria)this;
        }

        public Criteria andTaskIdLessThanOrEqualTo(Long value) {
            addCriterion("task_id <=", value, "taskId");
            return (Criteria)this;
        }

        public Criteria andTaskIdIn(List<Long> values) {
            addCriterion("task_id in", values, "taskId");
            return (Criteria)this;
        }

        public Criteria andTaskIdNotIn(List<Long> values) {
            addCriterion("task_id not in", values, "taskId");
            return (Criteria)this;
        }

        public Criteria andTaskIdBetween(Long value1, Long value2) {
            addCriterion("task_id between", value1, value2, "taskId");
            return (Criteria)this;
        }

        public Criteria andTaskIdNotBetween(Long value1, Long value2) {
            addCriterion("task_id not between", value1, value2, "taskId");
            return (Criteria)this;
        }

        public Criteria andPublicIpIsNull() {
            addCriterion("public_ip is null");
            return (Criteria)this;
        }

        public Criteria andPublicIpIsNotNull() {
            addCriterion("public_ip is not null");
            return (Criteria)this;
        }

        public Criteria andPublicIpEqualTo(String value) {
            addCriterion("public_ip =", value, "publicIp");
            return (Criteria)this;
        }

        public Criteria andPublicIpNotEqualTo(String value) {
            addCriterion("public_ip <>", value, "publicIp");
            return (Criteria)this;
        }

        public Criteria andPublicIpGreaterThan(String value) {
            addCriterion("public_ip >", value, "publicIp");
            return (Criteria)this;
        }

        public Criteria andPublicIpGreaterThanOrEqualTo(String value) {
            addCriterion("public_ip >=", value, "publicIp");
            return (Criteria)this;
        }

        public Criteria andPublicIpLessThan(String value) {
            addCriterion("public_ip <", value, "publicIp");
            return (Criteria)this;
        }

        public Criteria andPublicIpLessThanOrEqualTo(String value) {
            addCriterion("public_ip <=", value, "publicIp");
            return (Criteria)this;
        }

        public Criteria andPublicIpLike(String value) {
            addCriterion("public_ip like", value, "publicIp");
            return (Criteria)this;
        }

        public Criteria andPublicIpNotLike(String value) {
            addCriterion("public_ip not like", value, "publicIp");
            return (Criteria)this;
        }

        public Criteria andPublicIpIn(List<String> values) {
            addCriterion("public_ip in", values, "publicIp");
            return (Criteria)this;
        }

        public Criteria andPublicIpNotIn(List<String> values) {
            addCriterion("public_ip not in", values, "publicIp");
            return (Criteria)this;
        }

        public Criteria andPublicIpBetween(String value1, String value2) {
            addCriterion("public_ip between", value1, value2, "publicIp");
            return (Criteria)this;
        }

        public Criteria andPublicIpNotBetween(String value1, String value2) {
            addCriterion("public_ip not between", value1, value2, "publicIp");
            return (Criteria)this;
        }

        public Criteria andPrivateIpIsNull() {
            addCriterion("private_ip is null");
            return (Criteria)this;
        }

        public Criteria andPrivateIpIsNotNull() {
            addCriterion("private_ip is not null");
            return (Criteria)this;
        }

        public Criteria andPrivateIpEqualTo(String value) {
            addCriterion("private_ip =", value, "privateIp");
            return (Criteria)this;
        }

        public Criteria andPrivateIpNotEqualTo(String value) {
            addCriterion("private_ip <>", value, "privateIp");
            return (Criteria)this;
        }

        public Criteria andPrivateIpGreaterThan(String value) {
            addCriterion("private_ip >", value, "privateIp");
            return (Criteria)this;
        }

        public Criteria andPrivateIpGreaterThanOrEqualTo(String value) {
            addCriterion("private_ip >=", value, "privateIp");
            return (Criteria)this;
        }

        public Criteria andPrivateIpLessThan(String value) {
            addCriterion("private_ip <", value, "privateIp");
            return (Criteria)this;
        }

        public Criteria andPrivateIpLessThanOrEqualTo(String value) {
            addCriterion("private_ip <=", value, "privateIp");
            return (Criteria)this;
        }

        public Criteria andPrivateIpLike(String value) {
            addCriterion("private_ip like", value, "privateIp");
            return (Criteria)this;
        }

        public Criteria andPrivateIpNotLike(String value) {
            addCriterion("private_ip not like", value, "privateIp");
            return (Criteria)this;
        }

        public Criteria andPrivateIpIn(List<String> values) {
            addCriterion("private_ip in", values, "privateIp");
            return (Criteria)this;
        }

        public Criteria andPrivateIpNotIn(List<String> values) {
            addCriterion("private_ip not in", values, "privateIp");
            return (Criteria)this;
        }

        public Criteria andPrivateIpBetween(String value1, String value2) {
            addCriterion("private_ip between", value1, value2, "privateIp");
            return (Criteria)this;
        }

        public Criteria andPrivateIpNotBetween(String value1, String value2) {
            addCriterion("private_ip not between", value1, value2, "privateIp");
            return (Criteria)this;
        }

        public Criteria andInstanceIdIsNull() {
            addCriterion("instance_id is null");
            return (Criteria)this;
        }

        public Criteria andInstanceIdIsNotNull() {
            addCriterion("instance_id is not null");
            return (Criteria)this;
        }

        public Criteria andInstanceIdEqualTo(String value) {
            addCriterion("instance_id =", value, "instanceId");
            return (Criteria)this;
        }

        public Criteria andInstanceIdNotEqualTo(String value) {
            addCriterion("instance_id <>", value, "instanceId");
            return (Criteria)this;
        }

        public Criteria andInstanceIdGreaterThan(String value) {
            addCriterion("instance_id >", value, "instanceId");
            return (Criteria)this;
        }

        public Criteria andInstanceIdGreaterThanOrEqualTo(String value) {
            addCriterion("instance_id >=", value, "instanceId");
            return (Criteria)this;
        }

        public Criteria andInstanceIdLessThan(String value) {
            addCriterion("instance_id <", value, "instanceId");
            return (Criteria)this;
        }

        public Criteria andInstanceIdLessThanOrEqualTo(String value) {
            addCriterion("instance_id <=", value, "instanceId");
            return (Criteria)this;
        }

        public Criteria andInstanceIdLike(String value) {
            addCriterion("instance_id like", value, "instanceId");
            return (Criteria)this;
        }

        public Criteria andInstanceIdNotLike(String value) {
            addCriterion("instance_id not like", value, "instanceId");
            return (Criteria)this;
        }

        public Criteria andInstanceIdIn(List<String> values) {
            addCriterion("instance_id in", values, "instanceId");
            return (Criteria)this;
        }

        public Criteria andInstanceIdNotIn(List<String> values) {
            addCriterion("instance_id not in", values, "instanceId");
            return (Criteria)this;
        }

        public Criteria andInstanceIdBetween(String value1, String value2) {
            addCriterion("instance_id between", value1, value2, "instanceId");
            return (Criteria)this;
        }

        public Criteria andInstanceIdNotBetween(String value1, String value2) {
            addCriterion("instance_id not between", value1, value2, "instanceId");
            return (Criteria)this;
        }

        public Criteria andInstanceNameIsNull() {
            addCriterion("instance_name is null");
            return (Criteria)this;
        }

        public Criteria andInstanceNameIsNotNull() {
            addCriterion("instance_name is not null");
            return (Criteria)this;
        }

        public Criteria andInstanceNameEqualTo(String value) {
            addCriterion("instance_name =", value, "instanceName");
            return (Criteria)this;
        }

        public Criteria andInstanceNameNotEqualTo(String value) {
            addCriterion("instance_name <>", value, "instanceName");
            return (Criteria)this;
        }

        public Criteria andInstanceNameGreaterThan(String value) {
            addCriterion("instance_name >", value, "instanceName");
            return (Criteria)this;
        }

        public Criteria andInstanceNameGreaterThanOrEqualTo(String value) {
            addCriterion("instance_name >=", value, "instanceName");
            return (Criteria)this;
        }

        public Criteria andInstanceNameLessThan(String value) {
            addCriterion("instance_name <", value, "instanceName");
            return (Criteria)this;
        }

        public Criteria andInstanceNameLessThanOrEqualTo(String value) {
            addCriterion("instance_name <=", value, "instanceName");
            return (Criteria)this;
        }

        public Criteria andInstanceNameLike(String value) {
            addCriterion("instance_name like", value, "instanceName");
            return (Criteria)this;
        }

        public Criteria andInstanceNameNotLike(String value) {
            addCriterion("instance_name not like", value, "instanceName");
            return (Criteria)this;
        }

        public Criteria andInstanceNameIn(List<String> values) {
            addCriterion("instance_name in", values, "instanceName");
            return (Criteria)this;
        }

        public Criteria andInstanceNameNotIn(List<String> values) {
            addCriterion("instance_name not in", values, "instanceName");
            return (Criteria)this;
        }

        public Criteria andInstanceNameBetween(String value1, String value2) {
            addCriterion("instance_name between", value1, value2, "instanceName");
            return (Criteria)this;
        }

        public Criteria andInstanceNameNotBetween(String value1, String value2) {
            addCriterion("instance_name not between", value1, value2, "instanceName");
            return (Criteria)this;
        }

        public Criteria andRegionIdIsNull() {
            addCriterion("region_id is null");
            return (Criteria)this;
        }

        public Criteria andRegionIdIsNotNull() {
            addCriterion("region_id is not null");
            return (Criteria)this;
        }

        public Criteria andRegionIdEqualTo(String value) {
            addCriterion("region_id =", value, "regionId");
            return (Criteria)this;
        }

        public Criteria andRegionIdNotEqualTo(String value) {
            addCriterion("region_id <>", value, "regionId");
            return (Criteria)this;
        }

        public Criteria andRegionIdGreaterThan(String value) {
            addCriterion("region_id >", value, "regionId");
            return (Criteria)this;
        }

        public Criteria andRegionIdGreaterThanOrEqualTo(String value) {
            addCriterion("region_id >=", value, "regionId");
            return (Criteria)this;
        }

        public Criteria andRegionIdLessThan(String value) {
            addCriterion("region_id <", value, "regionId");
            return (Criteria)this;
        }

        public Criteria andRegionIdLessThanOrEqualTo(String value) {
            addCriterion("region_id <=", value, "regionId");
            return (Criteria)this;
        }

        public Criteria andRegionIdLike(String value) {
            addCriterion("region_id like", value, "regionId");
            return (Criteria)this;
        }

        public Criteria andRegionIdNotLike(String value) {
            addCriterion("region_id not like", value, "regionId");
            return (Criteria)this;
        }

        public Criteria andRegionIdIn(List<String> values) {
            addCriterion("region_id in", values, "regionId");
            return (Criteria)this;
        }

        public Criteria andRegionIdNotIn(List<String> values) {
            addCriterion("region_id not in", values, "regionId");
            return (Criteria)this;
        }

        public Criteria andRegionIdBetween(String value1, String value2) {
            addCriterion("region_id between", value1, value2, "regionId");
            return (Criteria)this;
        }

        public Criteria andRegionIdNotBetween(String value1, String value2) {
            addCriterion("region_id not between", value1, value2, "regionId");
            return (Criteria)this;
        }

        public Criteria andRegionNameIsNull() {
            addCriterion("region_name is null");
            return (Criteria)this;
        }

        public Criteria andRegionNameIsNotNull() {
            addCriterion("region_name is not null");
            return (Criteria)this;
        }

        public Criteria andRegionNameEqualTo(String value) {
            addCriterion("region_name =", value, "regionName");
            return (Criteria)this;
        }

        public Criteria andRegionNameNotEqualTo(String value) {
            addCriterion("region_name <>", value, "regionName");
            return (Criteria)this;
        }

        public Criteria andRegionNameGreaterThan(String value) {
            addCriterion("region_name >", value, "regionName");
            return (Criteria)this;
        }

        public Criteria andRegionNameGreaterThanOrEqualTo(String value) {
            addCriterion("region_name >=", value, "regionName");
            return (Criteria)this;
        }

        public Criteria andRegionNameLessThan(String value) {
            addCriterion("region_name <", value, "regionName");
            return (Criteria)this;
        }

        public Criteria andRegionNameLessThanOrEqualTo(String value) {
            addCriterion("region_name <=", value, "regionName");
            return (Criteria)this;
        }

        public Criteria andRegionNameLike(String value) {
            addCriterion("region_name like", value, "regionName");
            return (Criteria)this;
        }

        public Criteria andRegionNameNotLike(String value) {
            addCriterion("region_name not like", value, "regionName");
            return (Criteria)this;
        }

        public Criteria andRegionNameIn(List<String> values) {
            addCriterion("region_name in", values, "regionName");
            return (Criteria)this;
        }

        public Criteria andRegionNameNotIn(List<String> values) {
            addCriterion("region_name not in", values, "regionName");
            return (Criteria)this;
        }

        public Criteria andRegionNameBetween(String value1, String value2) {
            addCriterion("region_name between", value1, value2, "regionName");
            return (Criteria)this;
        }

        public Criteria andRegionNameNotBetween(String value1, String value2) {
            addCriterion("region_name not between", value1, value2, "regionName");
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

        public Criteria andRefSpecIsNull() {
            addCriterion("ref_spec is null");
            return (Criteria)this;
        }

        public Criteria andRefSpecIsNotNull() {
            addCriterion("ref_spec is not null");
            return (Criteria)this;
        }

        public Criteria andRefSpecEqualTo(String value) {
            addCriterion("ref_spec =", value, "refSpec");
            return (Criteria)this;
        }

        public Criteria andRefSpecNotEqualTo(String value) {
            addCriterion("ref_spec <>", value, "refSpec");
            return (Criteria)this;
        }

        public Criteria andRefSpecGreaterThan(String value) {
            addCriterion("ref_spec >", value, "refSpec");
            return (Criteria)this;
        }

        public Criteria andRefSpecGreaterThanOrEqualTo(String value) {
            addCriterion("ref_spec >=", value, "refSpec");
            return (Criteria)this;
        }

        public Criteria andRefSpecLessThan(String value) {
            addCriterion("ref_spec <", value, "refSpec");
            return (Criteria)this;
        }

        public Criteria andRefSpecLessThanOrEqualTo(String value) {
            addCriterion("ref_spec <=", value, "refSpec");
            return (Criteria)this;
        }

        public Criteria andRefSpecLike(String value) {
            addCriterion("ref_spec like", value, "refSpec");
            return (Criteria)this;
        }

        public Criteria andRefSpecNotLike(String value) {
            addCriterion("ref_spec not like", value, "refSpec");
            return (Criteria)this;
        }

        public Criteria andRefSpecIn(List<String> values) {
            addCriterion("ref_spec in", values, "refSpec");
            return (Criteria)this;
        }

        public Criteria andRefSpecNotIn(List<String> values) {
            addCriterion("ref_spec not in", values, "refSpec");
            return (Criteria)this;
        }

        public Criteria andRefSpecBetween(String value1, String value2) {
            addCriterion("ref_spec between", value1, value2, "refSpec");
            return (Criteria)this;
        }

        public Criteria andRefSpecNotBetween(String value1, String value2) {
            addCriterion("ref_spec not between", value1, value2, "refSpec");
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

        public Criteria andExpireDateIsNull() {
            addCriterion("expire_date is null");
            return (Criteria)this;
        }

        public Criteria andExpireDateIsNotNull() {
            addCriterion("expire_date is not null");
            return (Criteria)this;
        }

        public Criteria andExpireDateEqualTo(Date value) {
            addCriterion("expire_date =", value, "expireDate");
            return (Criteria)this;
        }

        public Criteria andExpireDateNotEqualTo(Date value) {
            addCriterion("expire_date <>", value, "expireDate");
            return (Criteria)this;
        }

        public Criteria andExpireDateGreaterThan(Date value) {
            addCriterion("expire_date >", value, "expireDate");
            return (Criteria)this;
        }

        public Criteria andExpireDateGreaterThanOrEqualTo(Date value) {
            addCriterion("expire_date >=", value, "expireDate");
            return (Criteria)this;
        }

        public Criteria andExpireDateLessThan(Date value) {
            addCriterion("expire_date <", value, "expireDate");
            return (Criteria)this;
        }

        public Criteria andExpireDateLessThanOrEqualTo(Date value) {
            addCriterion("expire_date <=", value, "expireDate");
            return (Criteria)this;
        }

        public Criteria andExpireDateIn(List<Date> values) {
            addCriterion("expire_date in", values, "expireDate");
            return (Criteria)this;
        }

        public Criteria andExpireDateNotIn(List<Date> values) {
            addCriterion("expire_date not in", values, "expireDate");
            return (Criteria)this;
        }

        public Criteria andExpireDateBetween(Date value1, Date value2) {
            addCriterion("expire_date between", value1, value2, "expireDate");
            return (Criteria)this;
        }

        public Criteria andExpireDateNotBetween(Date value1, Date value2) {
            addCriterion("expire_date not between", value1, value2, "expireDate");
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
