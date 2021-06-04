import axios from 'axios';
import Cookies from 'js-cookie';

import {
  Message,
} from 'iview';
import {
  getLang,
} from './utils';

const messageArr = ['UNDEFINE_ERROR', 'PARAMS_ERROR', 'LANG_REDIS_ERROR', 'JSON_FORMAT_FAIL',
  'LANG_NO_LOGIN', 'LANG_SMSCODE_ERR_TIP', 'LANG_SMSCODE_NULL_TIP', 'LANG_SEC_PWD_TIP',
  'LANG_GOOGLE_CODE_NULL', 'LANG_GOOGLE_CODE_ERROR', 'LANG_AUTH_IDENTITY_FIRST',
  'LANG_MAIL_NULL_TIP', 'LANG_PWD_NULL_TIP', 'LANG_ALREADY_REG_FINDPWD', 'LANG_ACCOUNT_LOCKED',
  'LANG_PLEASE_LOGIN_AFTER_REGISTRATION', 'LANG_OLD_PWD_ERR_TIP', 'LANG_PWD_SAME_TIP',
  'LANG_ILLEGAL_FROM', 'LANG_M_NAME_ERROR_TIP', 'LANG_ILLEGAL_CHECK_CODE',
  'LANG_ILLEGAL_GT_CHECK_CODE', 'LANG_ACCOUNT_NOT_EXIST', 'LANG_MAIL_FAIL',
  'LANG_OLD_GOOGLE_CODE_ERROR', 'LANG_UNDEFINED_ERROR', 'LANG_ALI_OSS_ERROR',
  'LANG_ID_NUM_ALREADY_EXIST', 'LANG_AUTH_IDENTITY_SUB_SUCCESS', 'LANG_ILLEGAL_ID',
  'LANG_API_TOKEN_NOT_EXIST', 'LANG_API_LABEL_ERR', 'LANG_API_PRIVILEGE_ERR', 'LANG_INVALID_NAME',
  'LANG_INVALID_EMAIL_OR_PASSWORD', 'API_ACCESS_COUNT_MAX', 'LANG_API_COUNT_MAX_3',
  'API_NOT_EXISTS', 'LANG_ILLEGAL_IP', 'API_NO_PRIVILEGE', 'LANG_ILLEGAL_CURRENCY',
  'LANG_CANNOT_WITHDRAW', 'LANG_ILLEGAL_ADDRESS', 'LANG_WITHDRAW_ADDR_NULL_TIP',
  'LANG_WITHDRAW_LABEL_NULL_TIP', 'LANG_ILLEGAL_AMOUNT', 'LANG_WITHDRAW_AMOUNT_MIN_THAN_MIN_TIP',
  'LANG_STOP_EX', 'LANG_ILLEGAL_WITHDRAW_ID', 'LANG_ILLEGAL_WITHDRAW_STATUS',
  'LANG_REPEAT_ADDRESS', 'LANG_ILLEGAL_STATUS', 'LANG_ILLEGAL_REJECT_REASON',
  'LANG_ILLEGAL_TRADE_PAIR', 'LANG_ORDER_DOES_NOT_EXIST', 'LANG_MARKET_ORDER_CANNOT_BE_CANCELLED',
  'LANG_ORDER_IS_DONE_OR_CANCELED', 'LANG_ILLEGAL_SYMBOL', 'LANG_ILLEGAL_ORDER_ID',
  'LANG_ILLEGAL_PRICE', 'LANG_ILLEGAL_O_PRICE_TYPE', 'LANG_ILLEGAL_O_TYPE', 'LANG_ILLEGAL_SOURCE',
  'LANG_SYMBOL_NOT_OPEN_TRADE_RANK', 'LANG_SYMBOL_OPEN_TRADE_RANK', 'LANG_ILLEGAL_ADS_TYPE',
  'LANG_SYMBOL_NOT_TRADE_RANK_DATA', 'LANG_LITTLE_THAN_MIN_BUYVOLUME_TIP',
  'LANG_LITTLE_THAN_MIN_BUYAMOUNT_TIP', 'LANG_LITTLE_THAN_MIN_SELLVOLUME_TIP',
  'NOT_SUFFICIENT_FUNDS', 'LANG_ERROR_A_TYPE', 'LANG_ORDER_NOT_EXISTS',
  'LANG_ORDER_ALREADY_CONFIRMED', 'LANG_ILLEGAL_ADS_ID', 'LANG_ILLEGAL_ACCOUNT_TYPE',
  'LANG_WAITING_ORDER_CONFIRM_MAX_THAN_2', 'LANG_WAITING_ORDER_MAX_THAN_2',
  'LANG_ACCOUNT_HAS_PROBLEM', 'LANG_POOL_NO_ADDRESS_EXISTS', 'LANG_ADDRESS_ALREADY_EXISTS',
  'LANG_WITHDRAW_STATUS_IS_NOT_WATTING', 'LANG_CREATE_ORDER_FAIL', 'LANG_ACCOUNT_FUNDS_PROBLEM',
  'LANG_CANCEL_ORDER_ERROR', 'LANG_ORDER_TRADE_ERROR', 'LANG_FIND_CONDITION_NOT_NULL',
  'LANG_LOCK_TYPE_ERROR', 'LANG_ERROR_OPERATION_OBJECT', 'LANG_TRADE_RANK_NOT_END_NOT_REWORD',
  'LANG_REWORD_HAS_SEND', 'PASSWORD_ERROR', 'PLEASE_SELECT_CURRENCY',
  'TRADE_AREA_HAS_NUMBER_ONE_SYMBOL', 'BASE_CURRENCY_NOT_EXISTS', 'QUOTE_CURRENCY_NOT_EXISTS',
  'ACCESS_HAS_BEEN_DENIED', 'DEL_COLUMN_TIP', 'SEND_AUDIT_ARTICLE_TIP', 'AUDIT_ARTICLE_TIP',
  'BASE_AND_QUOTE_CURRENCY_NOT_CONSISTENT', 'ORDER_STATUS_NOT_CONFIRM', 'ADS_HAS_USED_NOT_DELETE',
  'ACCOUNT_HAS_LOCKED', 'ACCOUNT_OR_PASSWORD_ERROR', 'LOGIN_INFO_ERROR', 'PLEASE_SELECT_SYMBOL',
  'INVALID_MERCHANT', 'TRADE_COMPLAINING', 'ERROR_OTC_CURRENCY', 'ACCOUNTTYPE_ONLY_ONE',
  'LANG_REPEAT_ERROR', 'LANG_ERROR_STATUS', 'LANG_NO_RECORD', 'LANG_OPERTION_FAIL',
  'RELATION_CUSTOMER_SERVICE', 'ONLY_MOBILE_CUSTOMER', 'SELF_TRADE_ERROR',
  'COMPLETE_OTHER_TRADE_PAY', 'CANCEL_TRADE_OVERMUCH', 'LANG_ERROR_DATA', 'LANG_ERROR_OPERTION',
  'TOTAL_BALANCE_LESS_THAN', 'LACK_OF_EFFECTIVE_ASSETS', 'ORDER_TYPE_ONLY_TWO', '未能获取到锁！',
  'TOKEN_NOT_EXISTS',
  'LANG_LIMIT_FREQUENCY_OPERTION',
  'LANG_SMSCODE_EXPIRE_TIP',
  'LANG_LOGIN_TOKEN_EXPIRE', 'LANG_ILLEGAL_SIGN', 'LANG_ILLEGAL_SUBMIT_DATA',
  'LANG_ACTIVITY_NOT_OPEN',
  'LANG_ACTIVITY_HAS_END',
  'LANG_REQ_TIME_OUT',
  'LANG_USER_NOT_EXIST',
  'COMPLAIN_BY_UNCONFIRMED',
  'PSL_SET_PHONE',
  'PHONE_HAS_BINDING',
  'HAS_ONE_TRADING',
  'BUY_HAS_ONE_TRADING',
  'SELL_HAS_ONE_TRADING',
  'LANG_INVALID_BINDNAME',
];
const messageArrZH = ['未知错误', '参数错误', '获取redis失败', 'json 格式转换错误！', '请先登录', '验证码错误', '验证码必填',
  '资金密码错误', '谷歌验证码为空', '谷歌验证码错误', '请先完成实名认证', '请输入邮箱', '请输入密码', '该账号已注册', '该账号已锁定', '该账号未完成注册',
  '原密码错误', '安全密码和登录密码不能一致', '请求来源非法', '账号格式错误', '图片验证码错误', '按钮证码错误', '无此币种资产', '发送验证码失败',
  '原谷歌验证码错误', '原谷歌验证码错误', '获取上传信息失败', '证件号已经存在', '身份认证申请提交成功', '错误的ID', 'ApiToken不存在',
  'Api标签不能为空', 'Api权限不能为空', '请输入用户名', '账号或密码错误', 'api访问过于频繁', 'api数量不能超过3个', 'api不存在', 'ip地址不合法',
  'api没有权限', '不存在的币种', '暂停提现', '不存在的地址', '提现地址必填', '提现标签必填', '错误的数量', '提现金额小于最低提现额', '暂停交易',
  '错误的提现ID', '提现状态不为待处理，不能取消', '添加地址重复', '错误的提现状态', '拒绝提现', '错误的交易对', '订单不存在', '市价单不允许取消',
  '订单已成交或已经取消', '错误的交易对', '错误的订单ID', '错误的价格', '错误的价格类型', '错误的订单类型', '错误的订单来源', '该交易对没有开启交易排名',
  '该交易对已开启交易排名', '广告类型错误', '该交易对没有交易排名数据', '数量小于最小买入数量', '总额小于最小买入总额', '数量小于最小卖出数量', '账户资金不足',
  '错误的账户类型(只支持 银行卡 支付宝 微信)', '订单不存在', '订单不是待付款状态', '广告ID不存在', '请完善对应的收款设置', '超过两个订单待处理，不能再添加新订单',
  '超过两个订单未付款，不能再添加新订单', '该用户账户可疑，冻结余额大于总金额', '该币种地址不存在', '该币种地址已存在', '提现正在处理中', '创建订单失败',
  '账户资金错误', '取消订单异常', '订单交易异常', '查询条件不能为空', '锁仓周期类型错误', '操作错误的对象', '交易排名还没有结束,不能发送奖励!',
  '该交易排名奖励已发放, 不能重复发送!', '对不起，您输入的当前密码有误！', '请选择币种！', '该交易区已有排名第一的交易!', '基础货币不存在!', '计价货币不存在!',
  '您没有访问此页面的权限！', '本栏目已经使用，不能删除，您可以修改它的是否启用为“否”！', '只有状态为新建的文章才可以送审，修改文章可以使文章状态变成新建！',
  '只有状态为待审核的文章才可以审核！', '基础货币和计价货币不能一致 ！', '该订单状态不为未确认 ！', '本广告主已经使用，不能删除！',
  '对不起，您的账号已被锁定,请与管理员联系！', '您输入的用户名或密码错误！', '用户登录信息错误！', '请选择交易对！', '无效的商家！', '交易申诉中！',
  '错误的OTC交易币种！', '账户类型只能绑定一个账号', '不能重复操作或者数据重复', '错误的状态', '没有记录', '操作失败', '联系客服', '仅支持手机用户',
  '不能自己交易', '需要完成其他交易的付款', '撤销交易过多', '错误的数据', '错误的操作', '总额小于0', '有效资产不足', '每个币种同一交易类型最多只能发布两笔广告', '未能获取到锁！', '登录出错，请重新登录',
  '短信操作过于频繁，请稍后再试！',
  '验证码已失效! ',
  '页面失效，请重新登录！', '签名错误', '提交数据不正确',
  '活动未开始',
  '活动已结束',
  '请求超时',
  '用户不存在！',
  '未放行申诉，二十四小时内不能出售',
  '请先设置手机号码！',
  '该手机号码已绑定！',
  '您已存在一单进行的交易，处理完成后方可再次购买！',
  '您已存在一笔买单正在进行交易，处理完成后方可再次购买！',
  '您已存在一卖单正在进行交易，处理完成后方可再次购买！',
  '绑定名字与实名认证名字不一致',
];
const messageArrEN = [
  'Please login first',
  'Currency that does not exist',
  'Suspend withdrawal',
  'Address that does not exist',
  'Number of errors',
  'The amount is less than the minimum withdrawal',
  'Verification code error',
  'Security password error',
  'Google verification code error',
  'Please complete the authentication',
  'Wrong withdraw ID',
  'The state is not unreachable',
  'Verification code required',
  'Address is required',
  'Withdrawal label required',
  'Please enter email address',
  'Please enter your password',
  'The account has been registered',
  'The account was locked',
  'The account has not been registered',
  'The security and login passwords do not match',
  'Illegal source of request',
  'The account does not exist',
  'Sending email failed',
  'Wrong account or password',
  'The id number already exists',
  'Submit the application for certification',
  'Wrong signature',
  'Order does not exist',
  'Single not allowed to cancel',
  'Order closed or cancelled',
  'Wrong deal',
  'Suspended',
  'Wrong order number',
  'Number of errors',
  'Wrong price',
  'Wrong order type',
  'Wrong type of price',
  'Wrong source of orders',
  'Wrong deal',
  'The quantity is less than the minimum purchase quantity',
  'The total amount is less than the minimum purchase amount',
  'The quantity is less than the minimum quantity sold',
  'Suspended',
  'Wrong order ID',
  'The pool of addresses has run out',
  'Recharge address already exists',
  'Cache server error',
  'Order status error',
  'Original Google verification code error',
  'Not sufficient funds',
  'Error of original password',
  'Wrong account type',
  'One account type (bank card, WeChat, alipay) can only bind one account',
  'Please complete the authentication first',
  'Wrong fund password',
  'Wrong type of advertisement',
  'AD ID does not exist',
  "Quantity doesn't meet the requirement",
  'Please complete the collection Settings',
  'More than two orders are not paid and new orders cannot be added',
  'Order does not exist',
  'The order is not pending',
  'Verification code error!',
  'Wrong verification code',
  'The number of sales must be a multiple of 100',
  'The quantity does not meet the requirements.',
  'Add address repeat',
  'Button validation error',
  'More than two orders to be processed',
  'No sufficient funds',
  'Account format error',
  'LANG API COUNT MAX 3',
  'Signature Error',
  'incorrect data submitted ',
  'the activity has not started ',
  'the activity is over ',
  'request timeout ',
];

const service = axios.create({

  // withCredentials: true,
});


service.interceptors.request.use((config) => {
  let token = localStorage.getItem('token');
  if (token === null) {
    token = 'web';
  }
  config.headers = {
    PEPPAEX_TOKEN: token,
  };
  return config;
});


service.interceptors.response.use(
  (response) => {
    let lang = getLang();
    if (!lang) {
      lang = 'zh';
    }
    if (response.data) {
      const res = response.data;
      if (res.success === 1) {
        return res;
      }
      if (res.state === 1) {
        return response.data;
      }
      if (res.state === -1) {
        if (res.msg === 'LANG_NO_LOGIN') {
          localStorage.removeItem('token');
          return res;
        }
        if (res.msg === 'BUY_HAS_ONE_TRADING') {
          return res;
        }
        const index = messageArr.indexOf(res.msg);
        if (index > -1) {
          if (lang === 'zh') {
            Message.error({
              content: messageArrZH[index],
              duration: 2,
              closable: true,
            });
          } else {
            Message.error({
              content: messageArrEN[index],
              duration: 2,
              closable: true,
            });
          }
        } else {
          Message.error({
            // content: res.msg,
            content: '未知错误',
            duration: 2,
            closable: true,
          });
        }

        return res;
      }
      return res;
    }
    return response.data;
  },
  error => Promise.reject(error),
);

export default service;
