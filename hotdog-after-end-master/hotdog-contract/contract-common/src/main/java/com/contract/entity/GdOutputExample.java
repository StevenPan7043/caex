package com.contract.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GdOutputExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table documentary_output
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table documentary_output
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table documentary_output
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_output
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public GdOutputExample() {
        oredCriteria = new ArrayList<>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_output
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_output
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_output
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_output
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_output
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_output
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_output
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_output
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_output
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_output
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table documentary_output
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
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
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andProjectIdIsNull() {
            addCriterion("project_id is null");
            return (Criteria) this;
        }

        public Criteria andProjectIdIsNotNull() {
            addCriterion("project_id is not null");
            return (Criteria) this;
        }

        public Criteria andProjectIdEqualTo(Integer value) {
            addCriterion("project_id =", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotEqualTo(Integer value) {
            addCriterion("project_id <>", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdGreaterThan(Integer value) {
            addCriterion("project_id >", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("project_id >=", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdLessThan(Integer value) {
            addCriterion("project_id <", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdLessThanOrEqualTo(Integer value) {
            addCriterion("project_id <=", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdIn(List<Integer> values) {
            addCriterion("project_id in", values, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotIn(List<Integer> values) {
            addCriterion("project_id not in", values, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdBetween(Integer value1, Integer value2) {
            addCriterion("project_id between", value1, value2, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotBetween(Integer value1, Integer value2) {
            addCriterion("project_id not between", value1, value2, "projectId");
            return (Criteria) this;
        }

        public Criteria andOutputCurrencyIsNull() {
            addCriterion("output_currency is null");
            return (Criteria) this;
        }

        public Criteria andOutputCurrencyIsNotNull() {
            addCriterion("output_currency is not null");
            return (Criteria) this;
        }

        public Criteria andOutputCurrencyEqualTo(String value) {
            addCriterion("output_currency =", value, "outputCurrency");
            return (Criteria) this;
        }

        public Criteria andOutputCurrencyNotEqualTo(String value) {
            addCriterion("output_currency <>", value, "outputCurrency");
            return (Criteria) this;
        }

        public Criteria andOutputCurrencyGreaterThan(String value) {
            addCriterion("output_currency >", value, "outputCurrency");
            return (Criteria) this;
        }

        public Criteria andOutputCurrencyGreaterThanOrEqualTo(String value) {
            addCriterion("output_currency >=", value, "outputCurrency");
            return (Criteria) this;
        }

        public Criteria andOutputCurrencyLessThan(String value) {
            addCriterion("output_currency <", value, "outputCurrency");
            return (Criteria) this;
        }

        public Criteria andOutputCurrencyLessThanOrEqualTo(String value) {
            addCriterion("output_currency <=", value, "outputCurrency");
            return (Criteria) this;
        }

        public Criteria andOutputCurrencyLike(String value) {
            addCriterion("output_currency like", value, "outputCurrency");
            return (Criteria) this;
        }

        public Criteria andOutputCurrencyNotLike(String value) {
            addCriterion("output_currency not like", value, "outputCurrency");
            return (Criteria) this;
        }

        public Criteria andOutputCurrencyIn(List<String> values) {
            addCriterion("output_currency in", values, "outputCurrency");
            return (Criteria) this;
        }

        public Criteria andOutputCurrencyNotIn(List<String> values) {
            addCriterion("output_currency not in", values, "outputCurrency");
            return (Criteria) this;
        }

        public Criteria andOutputCurrencyBetween(String value1, String value2) {
            addCriterion("output_currency between", value1, value2, "outputCurrency");
            return (Criteria) this;
        }

        public Criteria andOutputCurrencyNotBetween(String value1, String value2) {
            addCriterion("output_currency not between", value1, value2, "outputCurrency");
            return (Criteria) this;
        }

        public Criteria andCapacityIsNull() {
            addCriterion("capacity is null");
            return (Criteria) this;
        }

        public Criteria andCapacityIsNotNull() {
            addCriterion("capacity is not null");
            return (Criteria) this;
        }

        public Criteria andCapacityEqualTo(BigDecimal value) {
            addCriterion("capacity =", value, "capacity");
            return (Criteria) this;
        }

        public Criteria andCapacityNotEqualTo(BigDecimal value) {
            addCriterion("capacity <>", value, "capacity");
            return (Criteria) this;
        }

        public Criteria andCapacityGreaterThan(BigDecimal value) {
            addCriterion("capacity >", value, "capacity");
            return (Criteria) this;
        }

        public Criteria andCapacityGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("capacity >=", value, "capacity");
            return (Criteria) this;
        }

        public Criteria andCapacityLessThan(BigDecimal value) {
            addCriterion("capacity <", value, "capacity");
            return (Criteria) this;
        }

        public Criteria andCapacityLessThanOrEqualTo(BigDecimal value) {
            addCriterion("capacity <=", value, "capacity");
            return (Criteria) this;
        }

        public Criteria andCapacityIn(List<BigDecimal> values) {
            addCriterion("capacity in", values, "capacity");
            return (Criteria) this;
        }

        public Criteria andCapacityNotIn(List<BigDecimal> values) {
            addCriterion("capacity not in", values, "capacity");
            return (Criteria) this;
        }

        public Criteria andCapacityBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("capacity between", value1, value2, "capacity");
            return (Criteria) this;
        }

        public Criteria andCapacityNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("capacity not between", value1, value2, "capacity");
            return (Criteria) this;
        }

        public Criteria andOutputDateIsNull() {
            addCriterion("output_date is null");
            return (Criteria) this;
        }

        public Criteria andOutputDateIsNotNull() {
            addCriterion("output_date is not null");
            return (Criteria) this;
        }

        public Criteria andOutputDateEqualTo(String value) {
            addCriterion("output_date =", value, "outputDate");
            return (Criteria) this;
        }

        public Criteria andOutputDateNotEqualTo(String value) {
            addCriterion("output_date <>", value, "outputDate");
            return (Criteria) this;
        }

        public Criteria andOutputDateGreaterThan(String value) {
            addCriterion("output_date >", value, "outputDate");
            return (Criteria) this;
        }

        public Criteria andOutputDateGreaterThanOrEqualTo(String value) {
            addCriterion("output_date >=", value, "outputDate");
            return (Criteria) this;
        }

        public Criteria andOutputDateLessThan(String value) {
            addCriterion("output_date <", value, "outputDate");
            return (Criteria) this;
        }

        public Criteria andOutputDateLessThanOrEqualTo(String value) {
            addCriterion("output_date <=", value, "outputDate");
            return (Criteria) this;
        }

        public Criteria andOutputDateLike(String value) {
            addCriterion("output_date like", value, "outputDate");
            return (Criteria) this;
        }

        public Criteria andOutputDateNotLike(String value) {
            addCriterion("output_date not like", value, "outputDate");
            return (Criteria) this;
        }

        public Criteria andOutputDateIn(List<String> values) {
            addCriterion("output_date in", values, "outputDate");
            return (Criteria) this;
        }

        public Criteria andOutputDateNotIn(List<String> values) {
            addCriterion("output_date not in", values, "outputDate");
            return (Criteria) this;
        }

        public Criteria andOutputDateBetween(String value1, String value2) {
            addCriterion("output_date between", value1, value2, "outputDate");
            return (Criteria) this;
        }

        public Criteria andOutputDateNotBetween(String value1, String value2) {
            addCriterion("output_date not between", value1, value2, "outputDate");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table documentary_output
     *
     * @mbg.generated do_not_delete_during_merge Tue Nov 10 11:00:52 CST 2020
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table documentary_output
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

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
    }
}