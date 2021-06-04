package com.pmzhongguo.gd.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GdProjectExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitSize;

    public GdProjectExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
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

    public void setLimitStart(Integer limitStart) {
        this.limitStart = limitStart;
    }

    public Integer getLimitStart() {
        return limitStart;
    }

    public void setLimitSize(Integer limitSize) {
        this.limitSize = limitSize;
    }

    public Integer getLimitSize() {
        return limitSize;
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

        public Criteria andQuoteCurrencyIsNull() {
            addCriterion("quote_currency is null");
            return (Criteria) this;
        }

        public Criteria andQuoteCurrencyIsNotNull() {
            addCriterion("quote_currency is not null");
            return (Criteria) this;
        }

        public Criteria andQuoteCurrencyEqualTo(String value) {
            addCriterion("quote_currency =", value, "quoteCurrency");
            return (Criteria) this;
        }

        public Criteria andQuoteCurrencyNotEqualTo(String value) {
            addCriterion("quote_currency <>", value, "quoteCurrency");
            return (Criteria) this;
        }

        public Criteria andQuoteCurrencyGreaterThan(String value) {
            addCriterion("quote_currency >", value, "quoteCurrency");
            return (Criteria) this;
        }

        public Criteria andQuoteCurrencyGreaterThanOrEqualTo(String value) {
            addCriterion("quote_currency >=", value, "quoteCurrency");
            return (Criteria) this;
        }

        public Criteria andQuoteCurrencyLessThan(String value) {
            addCriterion("quote_currency <", value, "quoteCurrency");
            return (Criteria) this;
        }

        public Criteria andQuoteCurrencyLessThanOrEqualTo(String value) {
            addCriterion("quote_currency <=", value, "quoteCurrency");
            return (Criteria) this;
        }

        public Criteria andQuoteCurrencyLike(String value) {
            addCriterion("quote_currency like", value, "quoteCurrency");
            return (Criteria) this;
        }

        public Criteria andQuoteCurrencyNotLike(String value) {
            addCriterion("quote_currency not like", value, "quoteCurrency");
            return (Criteria) this;
        }

        public Criteria andQuoteCurrencyIn(List<String> values) {
            addCriterion("quote_currency in", values, "quoteCurrency");
            return (Criteria) this;
        }

        public Criteria andQuoteCurrencyNotIn(List<String> values) {
            addCriterion("quote_currency not in", values, "quoteCurrency");
            return (Criteria) this;
        }

        public Criteria andQuoteCurrencyBetween(String value1, String value2) {
            addCriterion("quote_currency between", value1, value2, "quoteCurrency");
            return (Criteria) this;
        }

        public Criteria andQuoteCurrencyNotBetween(String value1, String value2) {
            addCriterion("quote_currency not between", value1, value2, "quoteCurrency");
            return (Criteria) this;
        }

        public Criteria andPriceIsNull() {
            addCriterion("price is null");
            return (Criteria) this;
        }

        public Criteria andPriceIsNotNull() {
            addCriterion("price is not null");
            return (Criteria) this;
        }

        public Criteria andPriceEqualTo(BigDecimal value) {
            addCriterion("price =", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotEqualTo(BigDecimal value) {
            addCriterion("price <>", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThan(BigDecimal value) {
            addCriterion("price >", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("price >=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThan(BigDecimal value) {
            addCriterion("price <", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("price <=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceIn(List<BigDecimal> values) {
            addCriterion("price in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotIn(List<BigDecimal> values) {
            addCriterion("price not in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price not between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andOutputFloorIsNull() {
            addCriterion("output_floor is null");
            return (Criteria) this;
        }

        public Criteria andOutputFloorIsNotNull() {
            addCriterion("output_floor is not null");
            return (Criteria) this;
        }

        public Criteria andOutputFloorEqualTo(BigDecimal value) {
            addCriterion("output_floor =", value, "outputFloor");
            return (Criteria) this;
        }

        public Criteria andOutputFloorNotEqualTo(BigDecimal value) {
            addCriterion("output_floor <>", value, "outputFloor");
            return (Criteria) this;
        }

        public Criteria andOutputFloorGreaterThan(BigDecimal value) {
            addCriterion("output_floor >", value, "outputFloor");
            return (Criteria) this;
        }

        public Criteria andOutputFloorGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("output_floor >=", value, "outputFloor");
            return (Criteria) this;
        }

        public Criteria andOutputFloorLessThan(BigDecimal value) {
            addCriterion("output_floor <", value, "outputFloor");
            return (Criteria) this;
        }

        public Criteria andOutputFloorLessThanOrEqualTo(BigDecimal value) {
            addCriterion("output_floor <=", value, "outputFloor");
            return (Criteria) this;
        }

        public Criteria andOutputFloorIn(List<BigDecimal> values) {
            addCriterion("output_floor in", values, "outputFloor");
            return (Criteria) this;
        }

        public Criteria andOutputFloorNotIn(List<BigDecimal> values) {
            addCriterion("output_floor not in", values, "outputFloor");
            return (Criteria) this;
        }

        public Criteria andOutputFloorBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("output_floor between", value1, value2, "outputFloor");
            return (Criteria) this;
        }

        public Criteria andOutputFloorNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("output_floor not between", value1, value2, "outputFloor");
            return (Criteria) this;
        }

        public Criteria andOutputUpperIsNull() {
            addCriterion("output_upper is null");
            return (Criteria) this;
        }

        public Criteria andOutputUpperIsNotNull() {
            addCriterion("output_upper is not null");
            return (Criteria) this;
        }

        public Criteria andOutputUpperEqualTo(BigDecimal value) {
            addCriterion("output_upper =", value, "outputUpper");
            return (Criteria) this;
        }

        public Criteria andOutputUpperNotEqualTo(BigDecimal value) {
            addCriterion("output_upper <>", value, "outputUpper");
            return (Criteria) this;
        }

        public Criteria andOutputUpperGreaterThan(BigDecimal value) {
            addCriterion("output_upper >", value, "outputUpper");
            return (Criteria) this;
        }

        public Criteria andOutputUpperGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("output_upper >=", value, "outputUpper");
            return (Criteria) this;
        }

        public Criteria andOutputUpperLessThan(BigDecimal value) {
            addCriterion("output_upper <", value, "outputUpper");
            return (Criteria) this;
        }

        public Criteria andOutputUpperLessThanOrEqualTo(BigDecimal value) {
            addCriterion("output_upper <=", value, "outputUpper");
            return (Criteria) this;
        }

        public Criteria andOutputUpperIn(List<BigDecimal> values) {
            addCriterion("output_upper in", values, "outputUpper");
            return (Criteria) this;
        }

        public Criteria andOutputUpperNotIn(List<BigDecimal> values) {
            addCriterion("output_upper not in", values, "outputUpper");
            return (Criteria) this;
        }

        public Criteria andOutputUpperBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("output_upper between", value1, value2, "outputUpper");
            return (Criteria) this;
        }

        public Criteria andOutputUpperNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("output_upper not between", value1, value2, "outputUpper");
            return (Criteria) this;
        }

        public Criteria andRunStatusIsNull() {
            addCriterion("run_status is null");
            return (Criteria) this;
        }

        public Criteria andRunStatusIsNotNull() {
            addCriterion("run_status is not null");
            return (Criteria) this;
        }

        public Criteria andRunStatusEqualTo(String value) {
            addCriterion("run_status =", value, "runStatus");
            return (Criteria) this;
        }

        public Criteria andRunStatusNotEqualTo(String value) {
            addCriterion("run_status <>", value, "runStatus");
            return (Criteria) this;
        }

        public Criteria andRunStatusGreaterThan(String value) {
            addCriterion("run_status >", value, "runStatus");
            return (Criteria) this;
        }

        public Criteria andRunStatusGreaterThanOrEqualTo(String value) {
            addCriterion("run_status >=", value, "runStatus");
            return (Criteria) this;
        }

        public Criteria andRunStatusLessThan(String value) {
            addCriterion("run_status <", value, "runStatus");
            return (Criteria) this;
        }

        public Criteria andRunStatusLessThanOrEqualTo(String value) {
            addCriterion("run_status <=", value, "runStatus");
            return (Criteria) this;
        }

        public Criteria andRunStatusLike(String value) {
            addCriterion("run_status like", value, "runStatus");
            return (Criteria) this;
        }

        public Criteria andRunStatusNotLike(String value) {
            addCriterion("run_status not like", value, "runStatus");
            return (Criteria) this;
        }

        public Criteria andRunStatusIn(List<String> values) {
            addCriterion("run_status in", values, "runStatus");
            return (Criteria) this;
        }

        public Criteria andRunStatusNotIn(List<String> values) {
            addCriterion("run_status not in", values, "runStatus");
            return (Criteria) this;
        }

        public Criteria andRunStatusBetween(String value1, String value2) {
            addCriterion("run_status between", value1, value2, "runStatus");
            return (Criteria) this;
        }

        public Criteria andRunStatusNotBetween(String value1, String value2) {
            addCriterion("run_status not between", value1, value2, "runStatus");
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

        public Criteria andModifyTimeIsNull() {
            addCriterion("modify_time is null");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIsNotNull() {
            addCriterion("modify_time is not null");
            return (Criteria) this;
        }

        public Criteria andModifyTimeEqualTo(Date value) {
            addCriterion("modify_time =", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotEqualTo(Date value) {
            addCriterion("modify_time <>", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeGreaterThan(Date value) {
            addCriterion("modify_time >", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("modify_time >=", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeLessThan(Date value) {
            addCriterion("modify_time <", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeLessThanOrEqualTo(Date value) {
            addCriterion("modify_time <=", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIn(List<Date> values) {
            addCriterion("modify_time in", values, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotIn(List<Date> values) {
            addCriterion("modify_time not in", values, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeBetween(Date value1, Date value2) {
            addCriterion("modify_time between", value1, value2, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotBetween(Date value1, Date value2) {
            addCriterion("modify_time not between", value1, value2, "modifyTime");
            return (Criteria) this;
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