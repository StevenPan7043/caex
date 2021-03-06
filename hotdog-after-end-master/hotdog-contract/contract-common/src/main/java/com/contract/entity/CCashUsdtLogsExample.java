package com.contract.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CCashUsdtLogsExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    public CCashUsdtLogsExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
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
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
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

        public Criteria andCidIsNull() {
            addCriterion("cid is null");
            return (Criteria) this;
        }

        public Criteria andCidIsNotNull() {
            addCriterion("cid is not null");
            return (Criteria) this;
        }

        public Criteria andCidEqualTo(Integer value) {
            addCriterion("cid =", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidNotEqualTo(Integer value) {
            addCriterion("cid <>", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidGreaterThan(Integer value) {
            addCriterion("cid >", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidGreaterThanOrEqualTo(Integer value) {
            addCriterion("cid >=", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidLessThan(Integer value) {
            addCriterion("cid <", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidLessThanOrEqualTo(Integer value) {
            addCriterion("cid <=", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidIn(List<Integer> values) {
            addCriterion("cid in", values, "cid");
            return (Criteria) this;
        }

        public Criteria andCidNotIn(List<Integer> values) {
            addCriterion("cid not in", values, "cid");
            return (Criteria) this;
        }

        public Criteria andCidBetween(Integer value1, Integer value2) {
            addCriterion("cid between", value1, value2, "cid");
            return (Criteria) this;
        }

        public Criteria andCidNotBetween(Integer value1, Integer value2) {
            addCriterion("cid not between", value1, value2, "cid");
            return (Criteria) this;
        }

        public Criteria andFromaddrIsNull() {
            addCriterion("fromaddr is null");
            return (Criteria) this;
        }

        public Criteria andFromaddrIsNotNull() {
            addCriterion("fromaddr is not null");
            return (Criteria) this;
        }

        public Criteria andFromaddrEqualTo(String value) {
            addCriterion("fromaddr =", value, "fromaddr");
            return (Criteria) this;
        }

        public Criteria andFromaddrNotEqualTo(String value) {
            addCriterion("fromaddr <>", value, "fromaddr");
            return (Criteria) this;
        }

        public Criteria andFromaddrGreaterThan(String value) {
            addCriterion("fromaddr >", value, "fromaddr");
            return (Criteria) this;
        }

        public Criteria andFromaddrGreaterThanOrEqualTo(String value) {
            addCriterion("fromaddr >=", value, "fromaddr");
            return (Criteria) this;
        }

        public Criteria andFromaddrLessThan(String value) {
            addCriterion("fromaddr <", value, "fromaddr");
            return (Criteria) this;
        }

        public Criteria andFromaddrLessThanOrEqualTo(String value) {
            addCriterion("fromaddr <=", value, "fromaddr");
            return (Criteria) this;
        }

        public Criteria andFromaddrLike(String value) {
            addCriterion("fromaddr like", value, "fromaddr");
            return (Criteria) this;
        }

        public Criteria andFromaddrNotLike(String value) {
            addCriterion("fromaddr not like", value, "fromaddr");
            return (Criteria) this;
        }

        public Criteria andFromaddrIn(List<String> values) {
            addCriterion("fromaddr in", values, "fromaddr");
            return (Criteria) this;
        }

        public Criteria andFromaddrNotIn(List<String> values) {
            addCriterion("fromaddr not in", values, "fromaddr");
            return (Criteria) this;
        }

        public Criteria andFromaddrBetween(String value1, String value2) {
            addCriterion("fromaddr between", value1, value2, "fromaddr");
            return (Criteria) this;
        }

        public Criteria andFromaddrNotBetween(String value1, String value2) {
            addCriterion("fromaddr not between", value1, value2, "fromaddr");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNull() {
            addCriterion("money is null");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNotNull() {
            addCriterion("money is not null");
            return (Criteria) this;
        }

        public Criteria andMoneyEqualTo(BigDecimal value) {
            addCriterion("money =", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotEqualTo(BigDecimal value) {
            addCriterion("money <>", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThan(BigDecimal value) {
            addCriterion("money >", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("money >=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThan(BigDecimal value) {
            addCriterion("money <", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("money <=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyIn(List<BigDecimal> values) {
            addCriterion("money in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotIn(List<BigDecimal> values) {
            addCriterion("money not in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("money between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("money not between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andTaxIsNull() {
            addCriterion("tax is null");
            return (Criteria) this;
        }

        public Criteria andTaxIsNotNull() {
            addCriterion("tax is not null");
            return (Criteria) this;
        }

        public Criteria andTaxEqualTo(BigDecimal value) {
            addCriterion("tax =", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxNotEqualTo(BigDecimal value) {
            addCriterion("tax <>", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxGreaterThan(BigDecimal value) {
            addCriterion("tax >", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("tax >=", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxLessThan(BigDecimal value) {
            addCriterion("tax <", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxLessThanOrEqualTo(BigDecimal value) {
            addCriterion("tax <=", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxIn(List<BigDecimal> values) {
            addCriterion("tax in", values, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxNotIn(List<BigDecimal> values) {
            addCriterion("tax not in", values, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tax between", value1, value2, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tax not between", value1, value2, "tax");
            return (Criteria) this;
        }

        public Criteria andRealmoneyIsNull() {
            addCriterion("realmoney is null");
            return (Criteria) this;
        }

        public Criteria andRealmoneyIsNotNull() {
            addCriterion("realmoney is not null");
            return (Criteria) this;
        }

        public Criteria andRealmoneyEqualTo(BigDecimal value) {
            addCriterion("realmoney =", value, "realmoney");
            return (Criteria) this;
        }

        public Criteria andRealmoneyNotEqualTo(BigDecimal value) {
            addCriterion("realmoney <>", value, "realmoney");
            return (Criteria) this;
        }

        public Criteria andRealmoneyGreaterThan(BigDecimal value) {
            addCriterion("realmoney >", value, "realmoney");
            return (Criteria) this;
        }

        public Criteria andRealmoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("realmoney >=", value, "realmoney");
            return (Criteria) this;
        }

        public Criteria andRealmoneyLessThan(BigDecimal value) {
            addCriterion("realmoney <", value, "realmoney");
            return (Criteria) this;
        }

        public Criteria andRealmoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("realmoney <=", value, "realmoney");
            return (Criteria) this;
        }

        public Criteria andRealmoneyIn(List<BigDecimal> values) {
            addCriterion("realmoney in", values, "realmoney");
            return (Criteria) this;
        }

        public Criteria andRealmoneyNotIn(List<BigDecimal> values) {
            addCriterion("realmoney not in", values, "realmoney");
            return (Criteria) this;
        }

        public Criteria andRealmoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("realmoney between", value1, value2, "realmoney");
            return (Criteria) this;
        }

        public Criteria andRealmoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("realmoney not between", value1, value2, "realmoney");
            return (Criteria) this;
        }

        public Criteria andToaddrIsNull() {
            addCriterion("toaddr is null");
            return (Criteria) this;
        }

        public Criteria andToaddrIsNotNull() {
            addCriterion("toaddr is not null");
            return (Criteria) this;
        }

        public Criteria andToaddrEqualTo(String value) {
            addCriterion("toaddr =", value, "toaddr");
            return (Criteria) this;
        }

        public Criteria andToaddrNotEqualTo(String value) {
            addCriterion("toaddr <>", value, "toaddr");
            return (Criteria) this;
        }

        public Criteria andToaddrGreaterThan(String value) {
            addCriterion("toaddr >", value, "toaddr");
            return (Criteria) this;
        }

        public Criteria andToaddrGreaterThanOrEqualTo(String value) {
            addCriterion("toaddr >=", value, "toaddr");
            return (Criteria) this;
        }

        public Criteria andToaddrLessThan(String value) {
            addCriterion("toaddr <", value, "toaddr");
            return (Criteria) this;
        }

        public Criteria andToaddrLessThanOrEqualTo(String value) {
            addCriterion("toaddr <=", value, "toaddr");
            return (Criteria) this;
        }

        public Criteria andToaddrLike(String value) {
            addCriterion("toaddr like", value, "toaddr");
            return (Criteria) this;
        }

        public Criteria andToaddrNotLike(String value) {
            addCriterion("toaddr not like", value, "toaddr");
            return (Criteria) this;
        }

        public Criteria andToaddrIn(List<String> values) {
            addCriterion("toaddr in", values, "toaddr");
            return (Criteria) this;
        }

        public Criteria andToaddrNotIn(List<String> values) {
            addCriterion("toaddr not in", values, "toaddr");
            return (Criteria) this;
        }

        public Criteria andToaddrBetween(String value1, String value2) {
            addCriterion("toaddr between", value1, value2, "toaddr");
            return (Criteria) this;
        }

        public Criteria andToaddrNotBetween(String value1, String value2) {
            addCriterion("toaddr not between", value1, value2, "toaddr");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andHashcodeIsNull() {
            addCriterion("hashcode is null");
            return (Criteria) this;
        }

        public Criteria andHashcodeIsNotNull() {
            addCriterion("hashcode is not null");
            return (Criteria) this;
        }

        public Criteria andHashcodeEqualTo(String value) {
            addCriterion("hashcode =", value, "hashcode");
            return (Criteria) this;
        }

        public Criteria andHashcodeNotEqualTo(String value) {
            addCriterion("hashcode <>", value, "hashcode");
            return (Criteria) this;
        }

        public Criteria andHashcodeGreaterThan(String value) {
            addCriterion("hashcode >", value, "hashcode");
            return (Criteria) this;
        }

        public Criteria andHashcodeGreaterThanOrEqualTo(String value) {
            addCriterion("hashcode >=", value, "hashcode");
            return (Criteria) this;
        }

        public Criteria andHashcodeLessThan(String value) {
            addCriterion("hashcode <", value, "hashcode");
            return (Criteria) this;
        }

        public Criteria andHashcodeLessThanOrEqualTo(String value) {
            addCriterion("hashcode <=", value, "hashcode");
            return (Criteria) this;
        }

        public Criteria andHashcodeLike(String value) {
            addCriterion("hashcode like", value, "hashcode");
            return (Criteria) this;
        }

        public Criteria andHashcodeNotLike(String value) {
            addCriterion("hashcode not like", value, "hashcode");
            return (Criteria) this;
        }

        public Criteria andHashcodeIn(List<String> values) {
            addCriterion("hashcode in", values, "hashcode");
            return (Criteria) this;
        }

        public Criteria andHashcodeNotIn(List<String> values) {
            addCriterion("hashcode not in", values, "hashcode");
            return (Criteria) this;
        }

        public Criteria andHashcodeBetween(String value1, String value2) {
            addCriterion("hashcode between", value1, value2, "hashcode");
            return (Criteria) this;
        }

        public Criteria andHashcodeNotBetween(String value1, String value2) {
            addCriterion("hashcode not between", value1, value2, "hashcode");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNull() {
            addCriterion("createtime is null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNotNull() {
            addCriterion("createtime is not null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeEqualTo(Date value) {
            addCriterion("createtime =", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotEqualTo(Date value) {
            addCriterion("createtime <>", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThan(Date value) {
            addCriterion("createtime >", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("createtime >=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThan(Date value) {
            addCriterion("createtime <", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThanOrEqualTo(Date value) {
            addCriterion("createtime <=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIn(List<Date> values) {
            addCriterion("createtime in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotIn(List<Date> values) {
            addCriterion("createtime not in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeBetween(Date value1, Date value2) {
            addCriterion("createtime between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotBetween(Date value1, Date value2) {
            addCriterion("createtime not between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andChecktimeIsNull() {
            addCriterion("checktime is null");
            return (Criteria) this;
        }

        public Criteria andChecktimeIsNotNull() {
            addCriterion("checktime is not null");
            return (Criteria) this;
        }

        public Criteria andChecktimeEqualTo(Date value) {
            addCriterion("checktime =", value, "checktime");
            return (Criteria) this;
        }

        public Criteria andChecktimeNotEqualTo(Date value) {
            addCriterion("checktime <>", value, "checktime");
            return (Criteria) this;
        }

        public Criteria andChecktimeGreaterThan(Date value) {
            addCriterion("checktime >", value, "checktime");
            return (Criteria) this;
        }

        public Criteria andChecktimeGreaterThanOrEqualTo(Date value) {
            addCriterion("checktime >=", value, "checktime");
            return (Criteria) this;
        }

        public Criteria andChecktimeLessThan(Date value) {
            addCriterion("checktime <", value, "checktime");
            return (Criteria) this;
        }

        public Criteria andChecktimeLessThanOrEqualTo(Date value) {
            addCriterion("checktime <=", value, "checktime");
            return (Criteria) this;
        }

        public Criteria andChecktimeIn(List<Date> values) {
            addCriterion("checktime in", values, "checktime");
            return (Criteria) this;
        }

        public Criteria andChecktimeNotIn(List<Date> values) {
            addCriterion("checktime not in", values, "checktime");
            return (Criteria) this;
        }

        public Criteria andChecktimeBetween(Date value1, Date value2) {
            addCriterion("checktime between", value1, value2, "checktime");
            return (Criteria) this;
        }

        public Criteria andChecktimeNotBetween(Date value1, Date value2) {
            addCriterion("checktime not between", value1, value2, "checktime");
            return (Criteria) this;
        }

        public Criteria andCheckidIsNull() {
            addCriterion("checkid is null");
            return (Criteria) this;
        }

        public Criteria andCheckidIsNotNull() {
            addCriterion("checkid is not null");
            return (Criteria) this;
        }

        public Criteria andCheckidEqualTo(Integer value) {
            addCriterion("checkid =", value, "checkid");
            return (Criteria) this;
        }

        public Criteria andCheckidNotEqualTo(Integer value) {
            addCriterion("checkid <>", value, "checkid");
            return (Criteria) this;
        }

        public Criteria andCheckidGreaterThan(Integer value) {
            addCriterion("checkid >", value, "checkid");
            return (Criteria) this;
        }

        public Criteria andCheckidGreaterThanOrEqualTo(Integer value) {
            addCriterion("checkid >=", value, "checkid");
            return (Criteria) this;
        }

        public Criteria andCheckidLessThan(Integer value) {
            addCriterion("checkid <", value, "checkid");
            return (Criteria) this;
        }

        public Criteria andCheckidLessThanOrEqualTo(Integer value) {
            addCriterion("checkid <=", value, "checkid");
            return (Criteria) this;
        }

        public Criteria andCheckidIn(List<Integer> values) {
            addCriterion("checkid in", values, "checkid");
            return (Criteria) this;
        }

        public Criteria andCheckidNotIn(List<Integer> values) {
            addCriterion("checkid not in", values, "checkid");
            return (Criteria) this;
        }

        public Criteria andCheckidBetween(Integer value1, Integer value2) {
            addCriterion("checkid between", value1, value2, "checkid");
            return (Criteria) this;
        }

        public Criteria andCheckidNotBetween(Integer value1, Integer value2) {
            addCriterion("checkid not between", value1, value2, "checkid");
            return (Criteria) this;
        }

        public Criteria andDztimeIsNull() {
            addCriterion("dztime is null");
            return (Criteria) this;
        }

        public Criteria andDztimeIsNotNull() {
            addCriterion("dztime is not null");
            return (Criteria) this;
        }

        public Criteria andDztimeEqualTo(Date value) {
            addCriterion("dztime =", value, "dztime");
            return (Criteria) this;
        }

        public Criteria andDztimeNotEqualTo(Date value) {
            addCriterion("dztime <>", value, "dztime");
            return (Criteria) this;
        }

        public Criteria andDztimeGreaterThan(Date value) {
            addCriterion("dztime >", value, "dztime");
            return (Criteria) this;
        }

        public Criteria andDztimeGreaterThanOrEqualTo(Date value) {
            addCriterion("dztime >=", value, "dztime");
            return (Criteria) this;
        }

        public Criteria andDztimeLessThan(Date value) {
            addCriterion("dztime <", value, "dztime");
            return (Criteria) this;
        }

        public Criteria andDztimeLessThanOrEqualTo(Date value) {
            addCriterion("dztime <=", value, "dztime");
            return (Criteria) this;
        }

        public Criteria andDztimeIn(List<Date> values) {
            addCriterion("dztime in", values, "dztime");
            return (Criteria) this;
        }

        public Criteria andDztimeNotIn(List<Date> values) {
            addCriterion("dztime not in", values, "dztime");
            return (Criteria) this;
        }

        public Criteria andDztimeBetween(Date value1, Date value2) {
            addCriterion("dztime between", value1, value2, "dztime");
            return (Criteria) this;
        }

        public Criteria andDztimeNotBetween(Date value1, Date value2) {
            addCriterion("dztime not between", value1, value2, "dztime");
            return (Criteria) this;
        }

        public Criteria andDzstatusIsNull() {
            addCriterion("dzstatus is null");
            return (Criteria) this;
        }

        public Criteria andDzstatusIsNotNull() {
            addCriterion("dzstatus is not null");
            return (Criteria) this;
        }

        public Criteria andDzstatusEqualTo(Integer value) {
            addCriterion("dzstatus =", value, "dzstatus");
            return (Criteria) this;
        }

        public Criteria andDzstatusNotEqualTo(Integer value) {
            addCriterion("dzstatus <>", value, "dzstatus");
            return (Criteria) this;
        }

        public Criteria andDzstatusGreaterThan(Integer value) {
            addCriterion("dzstatus >", value, "dzstatus");
            return (Criteria) this;
        }

        public Criteria andDzstatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("dzstatus >=", value, "dzstatus");
            return (Criteria) this;
        }

        public Criteria andDzstatusLessThan(Integer value) {
            addCriterion("dzstatus <", value, "dzstatus");
            return (Criteria) this;
        }

        public Criteria andDzstatusLessThanOrEqualTo(Integer value) {
            addCriterion("dzstatus <=", value, "dzstatus");
            return (Criteria) this;
        }

        public Criteria andDzstatusIn(List<Integer> values) {
            addCriterion("dzstatus in", values, "dzstatus");
            return (Criteria) this;
        }

        public Criteria andDzstatusNotIn(List<Integer> values) {
            addCriterion("dzstatus not in", values, "dzstatus");
            return (Criteria) this;
        }

        public Criteria andDzstatusBetween(Integer value1, Integer value2) {
            addCriterion("dzstatus between", value1, value2, "dzstatus");
            return (Criteria) this;
        }

        public Criteria andDzstatusNotBetween(Integer value1, Integer value2) {
            addCriterion("dzstatus not between", value1, value2, "dzstatus");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
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