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

package com.pamirs.tro.entity.domain.entity.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuickAccessExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public QuickAccessExample() {
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

        public Criteria andCustomIdIsNull() {
            addCriterion("custom_id is null");
            return (Criteria)this;
        }

        public Criteria andCustomIdIsNotNull() {
            addCriterion("custom_id is not null");
            return (Criteria)this;
        }

        public Criteria andCustomIdEqualTo(Long value) {
            addCriterion("custom_id =", value, "customId");
            return (Criteria)this;
        }

        public Criteria andCustomIdNotEqualTo(Long value) {
            addCriterion("custom_id <>", value, "customId");
            return (Criteria)this;
        }

        public Criteria andCustomIdGreaterThan(Long value) {
            addCriterion("custom_id >", value, "customId");
            return (Criteria)this;
        }

        public Criteria andCustomIdGreaterThanOrEqualTo(Long value) {
            addCriterion("custom_id >=", value, "customId");
            return (Criteria)this;
        }

        public Criteria andCustomIdLessThan(Long value) {
            addCriterion("custom_id <", value, "customId");
            return (Criteria)this;
        }

        public Criteria andCustomIdLessThanOrEqualTo(Long value) {
            addCriterion("custom_id <=", value, "customId");
            return (Criteria)this;
        }

        public Criteria andCustomIdIn(List<Long> values) {
            addCriterion("custom_id in", values, "customId");
            return (Criteria)this;
        }

        public Criteria andCustomIdNotIn(List<Long> values) {
            addCriterion("custom_id not in", values, "customId");
            return (Criteria)this;
        }

        public Criteria andCustomIdBetween(Long value1, Long value2) {
            addCriterion("custom_id between", value1, value2, "customId");
            return (Criteria)this;
        }

        public Criteria andCustomIdNotBetween(Long value1, Long value2) {
            addCriterion("custom_id not between", value1, value2, "customId");
            return (Criteria)this;
        }

        public Criteria andQuickNameIsNull() {
            addCriterion("quick_name is null");
            return (Criteria)this;
        }

        public Criteria andQuickNameIsNotNull() {
            addCriterion("quick_name is not null");
            return (Criteria)this;
        }

        public Criteria andQuickNameEqualTo(String value) {
            addCriterion("quick_name =", value, "quickName");
            return (Criteria)this;
        }

        public Criteria andQuickNameNotEqualTo(String value) {
            addCriterion("quick_name <>", value, "quickName");
            return (Criteria)this;
        }

        public Criteria andQuickNameGreaterThan(String value) {
            addCriterion("quick_name >", value, "quickName");
            return (Criteria)this;
        }

        public Criteria andQuickNameGreaterThanOrEqualTo(String value) {
            addCriterion("quick_name >=", value, "quickName");
            return (Criteria)this;
        }

        public Criteria andQuickNameLessThan(String value) {
            addCriterion("quick_name <", value, "quickName");
            return (Criteria)this;
        }

        public Criteria andQuickNameLessThanOrEqualTo(String value) {
            addCriterion("quick_name <=", value, "quickName");
            return (Criteria)this;
        }

        public Criteria andQuickNameLike(String value) {
            addCriterion("quick_name like", value, "quickName");
            return (Criteria)this;
        }

        public Criteria andQuickNameNotLike(String value) {
            addCriterion("quick_name not like", value, "quickName");
            return (Criteria)this;
        }

        public Criteria andQuickNameIn(List<String> values) {
            addCriterion("quick_name in", values, "quickName");
            return (Criteria)this;
        }

        public Criteria andQuickNameNotIn(List<String> values) {
            addCriterion("quick_name not in", values, "quickName");
            return (Criteria)this;
        }

        public Criteria andQuickNameBetween(String value1, String value2) {
            addCriterion("quick_name between", value1, value2, "quickName");
            return (Criteria)this;
        }

        public Criteria andQuickNameNotBetween(String value1, String value2) {
            addCriterion("quick_name not between", value1, value2, "quickName");
            return (Criteria)this;
        }

        public Criteria andQuickLogoIsNull() {
            addCriterion("quick_logo is null");
            return (Criteria)this;
        }

        public Criteria andQuickLogoIsNotNull() {
            addCriterion("quick_logo is not null");
            return (Criteria)this;
        }

        public Criteria andQuickLogoEqualTo(String value) {
            addCriterion("quick_logo =", value, "quickLogo");
            return (Criteria)this;
        }

        public Criteria andQuickLogoNotEqualTo(String value) {
            addCriterion("quick_logo <>", value, "quickLogo");
            return (Criteria)this;
        }

        public Criteria andQuickLogoGreaterThan(String value) {
            addCriterion("quick_logo >", value, "quickLogo");
            return (Criteria)this;
        }

        public Criteria andQuickLogoGreaterThanOrEqualTo(String value) {
            addCriterion("quick_logo >=", value, "quickLogo");
            return (Criteria)this;
        }

        public Criteria andQuickLogoLessThan(String value) {
            addCriterion("quick_logo <", value, "quickLogo");
            return (Criteria)this;
        }

        public Criteria andQuickLogoLessThanOrEqualTo(String value) {
            addCriterion("quick_logo <=", value, "quickLogo");
            return (Criteria)this;
        }

        public Criteria andQuickLogoLike(String value) {
            addCriterion("quick_logo like", value, "quickLogo");
            return (Criteria)this;
        }

        public Criteria andQuickLogoNotLike(String value) {
            addCriterion("quick_logo not like", value, "quickLogo");
            return (Criteria)this;
        }

        public Criteria andQuickLogoIn(List<String> values) {
            addCriterion("quick_logo in", values, "quickLogo");
            return (Criteria)this;
        }

        public Criteria andQuickLogoNotIn(List<String> values) {
            addCriterion("quick_logo not in", values, "quickLogo");
            return (Criteria)this;
        }

        public Criteria andQuickLogoBetween(String value1, String value2) {
            addCriterion("quick_logo between", value1, value2, "quickLogo");
            return (Criteria)this;
        }

        public Criteria andQuickLogoNotBetween(String value1, String value2) {
            addCriterion("quick_logo not between", value1, value2, "quickLogo");
            return (Criteria)this;
        }

        public Criteria andUrlAddressIsNull() {
            addCriterion("url_address is null");
            return (Criteria)this;
        }

        public Criteria andUrlAddressIsNotNull() {
            addCriterion("url_address is not null");
            return (Criteria)this;
        }

        public Criteria andUrlAddressEqualTo(String value) {
            addCriterion("url_address =", value, "urlAddress");
            return (Criteria)this;
        }

        public Criteria andUrlAddressNotEqualTo(String value) {
            addCriterion("url_address <>", value, "urlAddress");
            return (Criteria)this;
        }

        public Criteria andUrlAddressGreaterThan(String value) {
            addCriterion("url_address >", value, "urlAddress");
            return (Criteria)this;
        }

        public Criteria andUrlAddressGreaterThanOrEqualTo(String value) {
            addCriterion("url_address >=", value, "urlAddress");
            return (Criteria)this;
        }

        public Criteria andUrlAddressLessThan(String value) {
            addCriterion("url_address <", value, "urlAddress");
            return (Criteria)this;
        }

        public Criteria andUrlAddressLessThanOrEqualTo(String value) {
            addCriterion("url_address <=", value, "urlAddress");
            return (Criteria)this;
        }

        public Criteria andUrlAddressLike(String value) {
            addCriterion("url_address like", value, "urlAddress");
            return (Criteria)this;
        }

        public Criteria andUrlAddressNotLike(String value) {
            addCriterion("url_address not like", value, "urlAddress");
            return (Criteria)this;
        }

        public Criteria andUrlAddressIn(List<String> values) {
            addCriterion("url_address in", values, "urlAddress");
            return (Criteria)this;
        }

        public Criteria andUrlAddressNotIn(List<String> values) {
            addCriterion("url_address not in", values, "urlAddress");
            return (Criteria)this;
        }

        public Criteria andUrlAddressBetween(String value1, String value2) {
            addCriterion("url_address between", value1, value2, "urlAddress");
            return (Criteria)this;
        }

        public Criteria andUrlAddressNotBetween(String value1, String value2) {
            addCriterion("url_address not between", value1, value2, "urlAddress");
            return (Criteria)this;
        }

        public Criteria andOrderIsNull() {
            addCriterion("order is null");
            return (Criteria)this;
        }

        public Criteria andOrderIsNotNull() {
            addCriterion("order is not null");
            return (Criteria)this;
        }

        public Criteria andOrderEqualTo(Integer value) {
            addCriterion("order =", value, "order");
            return (Criteria)this;
        }

        public Criteria andOrderNotEqualTo(Integer value) {
            addCriterion("order <>", value, "order");
            return (Criteria)this;
        }

        public Criteria andOrderGreaterThan(Integer value) {
            addCriterion("order >", value, "order");
            return (Criteria)this;
        }

        public Criteria andOrderGreaterThanOrEqualTo(Integer value) {
            addCriterion("order >=", value, "order");
            return (Criteria)this;
        }

        public Criteria andOrderLessThan(Integer value) {
            addCriterion("order <", value, "order");
            return (Criteria)this;
        }

        public Criteria andOrderLessThanOrEqualTo(Integer value) {
            addCriterion("order <=", value, "order");
            return (Criteria)this;
        }

        public Criteria andOrderIn(List<Integer> values) {
            addCriterion("order in", values, "order");
            return (Criteria)this;
        }

        public Criteria andOrderNotIn(List<Integer> values) {
            addCriterion("order not in", values, "order");
            return (Criteria)this;
        }

        public Criteria andOrderBetween(Integer value1, Integer value2) {
            addCriterion("order between", value1, value2, "order");
            return (Criteria)this;
        }

        public Criteria andOrderNotBetween(Integer value1, Integer value2) {
            addCriterion("order not between", value1, value2, "order");
            return (Criteria)this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria)this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria)this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria)this;
        }

        public Criteria andIsDeletedIsNull() {
            addCriterion("is_deleted is null");
            return (Criteria)this;
        }

        public Criteria andIsDeletedIsNotNull() {
            addCriterion("is_deleted is not null");
            return (Criteria)this;
        }

        public Criteria andIsDeletedEqualTo(Byte value) {
            addCriterion("is_deleted =", value, "isDeleted");
            return (Criteria)this;
        }

        public Criteria andIsDeletedNotEqualTo(Byte value) {
            addCriterion("is_deleted <>", value, "isDeleted");
            return (Criteria)this;
        }

        public Criteria andIsDeletedGreaterThan(Byte value) {
            addCriterion("is_deleted >", value, "isDeleted");
            return (Criteria)this;
        }

        public Criteria andIsDeletedGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_deleted >=", value, "isDeleted");
            return (Criteria)this;
        }

        public Criteria andIsDeletedLessThan(Byte value) {
            addCriterion("is_deleted <", value, "isDeleted");
            return (Criteria)this;
        }

        public Criteria andIsDeletedLessThanOrEqualTo(Byte value) {
            addCriterion("is_deleted <=", value, "isDeleted");
            return (Criteria)this;
        }

        public Criteria andIsDeletedIn(List<Byte> values) {
            addCriterion("is_deleted in", values, "isDeleted");
            return (Criteria)this;
        }

        public Criteria andIsDeletedNotIn(List<Byte> values) {
            addCriterion("is_deleted not in", values, "isDeleted");
            return (Criteria)this;
        }

        public Criteria andIsDeletedBetween(Byte value1, Byte value2) {
            addCriterion("is_deleted between", value1, value2, "isDeleted");
            return (Criteria)this;
        }

        public Criteria andIsDeletedNotBetween(Byte value1, Byte value2) {
            addCriterion("is_deleted not between", value1, value2, "isDeleted");
            return (Criteria)this;
        }

        public Criteria andIsEnableIsNull() {
            addCriterion("is_enable is null");
            return (Criteria)this;
        }

        public Criteria andIsEnableIsNotNull() {
            addCriterion("is_enable is not null");
            return (Criteria)this;
        }

        public Criteria andIsEnableEqualTo(Byte value) {
            addCriterion("is_enable =", value, "isEnable");
            return (Criteria)this;
        }

        public Criteria andIsEnableNotEqualTo(Byte value) {
            addCriterion("is_enable <>", value, "isEnable");
            return (Criteria)this;
        }

        public Criteria andIsEnableGreaterThan(Byte value) {
            addCriterion("is_enable >", value, "isEnable");
            return (Criteria)this;
        }

        public Criteria andIsEnableGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_enable >=", value, "isEnable");
            return (Criteria)this;
        }

        public Criteria andIsEnableLessThan(Byte value) {
            addCriterion("is_enable <", value, "isEnable");
            return (Criteria)this;
        }

        public Criteria andIsEnableLessThanOrEqualTo(Byte value) {
            addCriterion("is_enable <=", value, "isEnable");
            return (Criteria)this;
        }

        public Criteria andIsEnableIn(List<Byte> values) {
            addCriterion("is_enable in", values, "isEnable");
            return (Criteria)this;
        }

        public Criteria andIsEnableNotIn(List<Byte> values) {
            addCriterion("is_enable not in", values, "isEnable");
            return (Criteria)this;
        }

        public Criteria andIsEnableBetween(Byte value1, Byte value2) {
            addCriterion("is_enable between", value1, value2, "isEnable");
            return (Criteria)this;
        }

        public Criteria andIsEnableNotBetween(Byte value1, Byte value2) {
            addCriterion("is_enable not between", value1, value2, "isEnable");
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
