package com.contract.enums;


/**
 * @description: 异常提示错误码
 * @date: 2018-12-19 15:03
 * @author: 十一
 */
public enum ErrorInfoEnum {


    // 通用 5000xx
    UNDEFINE_ERROR(500001, "未知错误", "UNDEFINE_ERROR"),
    PARAMS_ERROR(500002, "参数错误", "PARAMS_ERROR"),
    LANG_REDIS_ERROR(500003, "获取redis失败", "LANG_REDIS_ERROR"),
    JSON_FORMAT_FAIL(500004, "json 格式转换错误！", "JSON_FORMAT_FAIL"),
    LANG_LIMIT_FREQUENCY_OPERTION(500005, "短信操作过于频繁，请稍后再试！", "LANG_LIMIT_FREQUENCY_OPERTION"),
    LANG_DATA_TOO_LONG(500006, "数据太长！", "LANG_DATA_TOO_LONG"),
    LANG_DATA_TOO_BIG(500007, "数据太大！", "LANG_DATA_TOO_BIG"),
    LANG_DATA_ERROR(500008, "数据异常！", "LANG_DATA_ERROR"),
    LANG_REQ_METHOD_NOT_EXIST(500009, "请求的方法不存在！", "LANG_REQ_METHOD_NOT_EXIST"),
    LANG_CAST_TYPE_EXCEPTION(500010, "类型转换错误！", "LANG_CAST_TYPE_EXCEPTION"),
    LANG_REQ_PARAM_ERROR(500011, "请求参数类型不正确！", "LANG_REQ_PARAM_ERROR"),
    LANG_ILLEGAL_SIGN(500012, "签名错误", "LANG_ILLEGAL_SIGN"),
    LANG_ILLEGAL_SUBMIT_DATA(500013, "提交数据不正确", "LANG_ILLEGAL_SUBMIT_DATA"),
    LANG_ACTIVITY_NOT_OPEN(500014, "活动未开始", "LANG_ACTIVITY_NOT_OPEN"),
    LANG_ACTIVITY_HAS_END(500015, "活动已结束", "LANG_ACTIVITY_HAS_END"),
    LANG_REQ_TIME_OUT(500016, "请求超时", "LANG_REQ_TIME_OUT"),
    LANG_SYSTEM_BUSY(500017, "系统繁忙！", "LANG_SYSTEM_BUSY"),
    LANG_DATA_SYNC_FAIL(500018, "数据同步失败！", "LANG_DATA_SYNC_FAIL"),
    ILLEGAL_TIMESTAMP(500019, "错误的时间戳", "ILLEGAL_TIMESTAMP"),
    LANG_FREQUENCY_OPERTION(500020, "操作过于频繁，请稍后再试！", "LANG_FREQUENCY_OPERTION"),

    LANG_CURRENCY_NOT_BLANK(500021, "币种必填", "LANG_CURRENCY_NOT_BLANK"),
    LANG_BASE_CURRENCY_NOT_BLANK(500022, "基础货币必填", "LANG_BASE_CURRENCY_NOT_BLANK"),
    LANG_QUOTE_CURRENCY_NOT_BLANK(500023, "计价货币必填", "LANG_QUOTE_CURRENCY_NOT_BLANK"),
    LANG_MEMBER_NAME_NOT_BLANK(500024, "用户名必填", "LANG_MEMBER_NAME_NOT_BLANK"),
    LANG_MEMBER_ID_NOT_BLANK(500025, "用户id必填", "LANG_MEMBER_ID_NOT_BLANK"),
    LANG_BASE_CURRENCY_UPPER_LIMIT_NOT_BLANK(500026, "基础货币提币上限必填", "LANG_BASE_CURRENCY_UPPER_LIMIT_NOT_BLANK"),
    LANG_QUOTE_CURRENCY_UPPER_LIMIT_NOT_BLANK(500027, "计价货币提币上限必填", "LANG_QUOTE_CURRENCY_UPPER_LIMIT_NOT_BLANK"),
    LANG_FEE_RATE_NOT_BLANK(500028, "手续费比例必填", "LANG_FEE_RATE_NOT_BLANK"),
    LANG_FREE_NOT_BLANK(500029, "是否冻结必填", "LANG_FREE_NOT_BLANK"),
    LANG_CURRENCY_PAIR_NOT_BLANK(500030, "交易对必填", "LANG_CURRENCY_PAIR_NOT_BLANK"),
    LANG_AMOUNT_NOT_BLANK(500031, "提币数必填", "LANG_AMOUNT_NOT_BLANK"),
    LANG_VERIFY_CODE_NOT_BLANK(500032, "验证码必填", "LANG_VERIFY_CODE_NOT_BLANK"),
    LANG_SECURITY_PWD_NOT_BLANK(500033, "资金密码必填", "LANG_SECURITY_PWD_NOT_BLANK"),

    // 会员相关 5001xx
    LANG_NO_LOGIN(500100, "请先登录", "LANG_NO_LOGIN"),
    LANG_SMSCODE_ERR_TIP(500101, "验证码错误", "LANG_SMSCODE_ERR_TIP"),
    LANG_SMSCODE_EXPIRE_TIP(500101, "验证码已失效", "LANG_SMSCODE_EXPIRE_TIP"),
    LANG_SMSCODE_NULL_TIP(500102, "验证码必填", "LANG_SMSCODE_NULL_TIP"),
    LANG_SEC_PWD_TIP(500103, "资金密码错误", "LANG_SEC_PWD_TIP"),
    LANG_GOOGLE_CODE_NULL(500104, "谷歌验证码为空", "LANG_GOOGLE_CODE_NULL"),
    LANG_GOOGLE_CODE_ERROR(500105, "谷歌验证码错误", "LANG_GOOGLE_CODE_ERROR"),
    LANG_AUTH_IDENTITY_FIRST(500106, "请先完成实名认证", "LANG_AUTH_IDENTITY_FIRST"),
    LANG_MAIL_NULL_TIP(500107, "请输入邮箱", "LANG_MAIL_NULL_TIP"),
    LANG_PWD_NULL_TIP(500108, "请输入密码", "LANG_PWD_NULL_TIP"),
    LANG_ALREADY_REG_FINDPWD(500109, "该账号已注册", "LANG_ALREADY_REG_FINDPWD"),
    LANG_ACCOUNT_LOCKED(500110, "该账号已锁定", "LANG_ACCOUNT_LOCKED"),
    LANG_PLEASE_LOGIN_AFTER_REGISTRATION(500111, "该账号未完成注册", "LANG_PLEASE_LOGIN_AFTER_REGISTRATION"),
    LANG_OLD_PWD_ERR_TIP(500112, "原密码错误", "LANG_OLD_PWD_ERR_TIP"),
    LANG_PWD_SAME_TIP(500113, "安全密码和登录密码不能一致", "LANG_PWD_SAME_TIP"),
    LANG_ILLEGAL_FROM(500114, "请求来源非法", "LANG_ILLEGAL_FROM"),
    LANG_M_NAME_ERROR_TIP(500115, "账号格式错误", "LANG_M_NAME_ERROR_TIP"),
    LANG_ILLEGAL_CHECK_CODE(500116, "图片验证码错误", "LANG_ILLEGAL_CHECK_CODE"),
    LANG_ILLEGAL_GT_CHECK_CODE(500117, "按钮证码错误", "LANG_ILLEGAL_GT_CHECK_CODE"),
    LANG_ACCOUNT_NOT_EXIST(500118, "无此币种资产", "LANG_ACCOUNT_NOT_EXIST"),
    LANG_MAIL_FAIL(500119, "发送验证码失败", "LANG_MAIL_FAIL"),
    LANG_OLD_GOOGLE_CODE_ERROR(500120, "原谷歌验证码错误", "LANG_OLD_GOOGLE_CODE_ERROR"),
    LANG_UNDEFINED_ERROR(500121, "原谷歌验证码错误", "LANG_UNDEFINED_ERROR"),
    LANG_ALI_OSS_ERROR(500122, "获取上传信息失败", "LANG_ALI_OSS_ERROR"),
    LANG_ID_NUM_ALREADY_EXIST(500123, "证件号已经存在", "LANG_ID_NUM_ALREADY_EXIST"),
    LANG_AUTH_IDENTITY_SUB_SUCCESS(500124, "身份认证申请提交成功", "LANG_AUTH_IDENTITY_SUB_SUCCESS"),
    LANG_ILLEGAL_ID(500125, "错误的ID", "LANG_ILLEGAL_ID"),
    LANG_API_TOKEN_NOT_EXIST(500126, "ApiToken不存在", "LANG_API_TOKEN_NOT_EXIST"),
    LANG_API_LABEL_ERR(500127, "Api标签不能为空", "LANG_API_LABEL_ERR"),
    LANG_API_PRIVILEGE_ERR(500128, "Api权限不能为空", "LANG_API_PRIVILEGE_ERR"),
    LANG_INVALID_NAME(500129, "请输入用户名", "LANG_INVALID_NAME"),
    LANG_INVALID_EMAIL_OR_PASSWORD(500130, "账号或密码错误", "LANG_INVALID_EMAIL_OR_PASSWORD"),
    API_ACCESS_COUNT_MAX(500131, "api访问过于频繁", "API_ACCESS_COUNT_MAX"),
    LANG_API_COUNT_MAX_3(500132, "api数量不能超过3个", "LANG_API_COUNT_MAX_3"),
    API_NOT_EXISTS(500133, "api不存在", "API_NOT_EXISTS"),
    LANG_ILLEGAL_IP(500134, "ip地址不合法", "LANG_ILLEGAL_IP"),
    API_NO_PRIVILEGE(500135, "api没有权限", "API_NO_PRIVILEGE"),
    LANG_LIMIT_OPERTION(500136, "已经达到操作限制，不能进行操作", "LANG_LIMIT_OPERTION"),
    LANG_LOGIN_TOKEN_EXPIRE(500137, "页面失效，请重新登录！", "LANG_LOGIN_TOKEN_EXPIRE"),
    LANG_UNBOUND_COLLECTION(500138, "未添加收款方式！", "LANG_UNBOUND_COLLECTION"),
    LANG_MISMATCH_COLLECTION(500139, "收款方式不匹配！", "LANG_MISMATCH_COLLECTION"),
    LANG_ISNOTEMPTY_NICKNAME(500140, "用户昵称不能为空！", "LANG_ISNOTEMPTY_NICKNAME"),
    LANG_USER_NOT_EXIST(500141, "用户不存在！", "LANG_USER_NOT_EXIST"),
    PSL_SET_PHONE(500142, "请先设置手机号码！", "PSL_SET_PHONE"),
    PHONE_HAS_BINDING(500143, "该手机号码已绑定！", "PHONE_HAS_BINDING"),
    LANG_THIRDPARTTY_NOT_EXIST(500144, "项目方信息不存在！", "LANG_THIRDPARTTY_NOT_EXIST"),
    QUERY_DIRECTION_NOT_EXIST(500145, "请输入查询方向！", "QUERY_DIRECTION_NOT_EXIST"),
    LEVEL_NOT_EXIST(500146, "查询等级不能为空！", "LEVEL_NOT_EXIST"),
    LEVEL_MAST_BE_NUMBER(500147, "等级参数必须为数字！", "LEVEL_MAST_BE_NUMBER"),
    CALLBACK_FUNCTION_ADDRESS_NOT_EXIST(500148, "回调函数地址为空！", "CALLBACK_FUNCTION_ADDRESS_NOT_EXIST"),
    COUNTRY_INFO_IS_NULL(500149,"区号信息不存在，请联系管理员","COUNTRY_INFO_IS_NULL"),
    LANG_REG_FAIL(500150,"注册失败","LANG_REG_FAIL"),
    INSUFFICIENT_RESIDUAL_ASSETS(500151,"提现后剩余总资产要大于等于5 USDT","INSUFFICIENT_RESIDUAL_ASSETS"),


    LANG_USER_IS_EXIST(500150, "该用户已绑定该交易对！", "LANG_USER_IS_EXIST"),
    SYMBOL_FEE_TOTAL_NOT_MORE_THAN_100(500151, "交易对手续费总和不得超过100%！", "SYMBOL_FEE_TOTAL_NOT_MORE_THAN_100"),
    SYMBOL_FEE_ACCOUNT_NOT_EXIST(500152, "手续费账户不存在！", "SYMBOL_FEE_ACCOUNT_NOT_EXIST"),
    SYMBOL_FEE_ACCOUNT_NOT_AUTH(500153, "手续费交易对没有权限！", "SYMBOL_FEE_ACCOUNT_NOT_AUTH"),



    // 交易相关 5002xx
    LANG_ILLEGAL_CURRENCY(500200, "不存在的币种", "LANG_ILLEGAL_CURRENCY"),
    LANG_CANNOT_WITHDRAW(500201, "暂停提现", "LANG_CANNOT_WITHDRAW"),
    LANG_ILLEGAL_ADDRESS(500202, "不存在的地址", "LANG_ILLEGAL_ADDRESS"),
    LANG_WITHDRAW_ADDR_NULL_TIP(500203, "提现地址必填", "LANG_WITHDRAW_ADDR_NULL_TIP"),
    LANG_WITHDRAW_LABEL_NULL_TIP(500204, "提现标签必填", "LANG_WITHDRAW_LABEL_NULL_TIP"),
    LANG_ILLEGAL_AMOUNT(500205, "错误的数量", "LANG_ILLEGAL_AMOUNT"),
    LANG_WITHDRAW_AMOUNT_MIN_THAN_MIN_TIP(500206, "提现金额小于最低提现额", "LANG_WITHDRAW_AMOUNT_MIN_THAN_MIN_TIP"),
    LANG_STOP_EX(500207, "暂停交易", "LANG_STOP_EX"),
    LANG_ILLEGAL_WITHDRAW_ID(500208, "错误的提现ID", "LANG_ILLEGAL_WITHDRAW_ID"),
    LANG_ILLEGAL_WITHDRAW_STATUS(500209, "提现状态不为待处理，不能取消", "LANG_ILLEGAL_WITHDRAW_STATUS"),
    LANG_REPEAT_ADDRESS(500210, "添加地址重复", "LANG_REPEAT_ADDRESS"),
    LANG_ILLEGAL_STATUS(500211, "错误的提现状态", "LANG_ILLEGAL_STATUS"),
    LANG_ILLEGAL_REJECT_REASON(500212, "拒绝提现", "LANG_ILLEGAL_REJECT_REASON"),
    LANG_ILLEGAL_TRADE_PAIR(500213, "错误的交易对", "LANG_ILLEGAL_TRADE_PAIR"),
    LANG_ORDER_DOES_NOT_EXIST(500214, "订单不存在", "LANG_ORDER_DOES_NOT_EXIST"),
    LANG_MARKET_ORDER_CANNOT_BE_CANCELLED(500215, "市价单不允许取消", "LANG_MARKET_ORDER_CANNOT_BE_CANCELLED"),
    LANG_ORDER_IS_DONE_OR_CANCELED(500216, "订单已成交或已经取消", "LANG_ORDER_IS_DONE_OR_CANCELED"),
    LANG_ILLEGAL_SYMBOL(500217, "错误的交易对", "LANG_ILLEGAL_SYMBOL"),
    LANG_ILLEGAL_ORDER_ID(500218, "错误的订单ID", "LANG_ILLEGAL_ORDER_ID"),
    LANG_ILLEGAL_PRICE(500219, "错误的价格", "LANG_ILLEGAL_PRICE"),
    LANG_ILLEGAL_O_PRICE_TYPE(500220, "错误的价格类型", "LANG_ILLEGAL_O_PRICE_TYPE"),
    LANG_ILLEGAL_O_TYPE(500221, "错误的订单类型", "LANG_ILLEGAL_O_TYPE"),
    LANG_ILLEGAL_SOURCE(500222, "错误的订单来源", "LANG_ILLEGAL_SOURCE"),
    LANG_SYMBOL_NOT_OPEN_TRADE_RANK(500223, "该交易对没有开启交易排名", "LANG_SYMBOL_NOT_OPEN_TRADE_RANK"),
    LANG_SYMBOL_OPEN_TRADE_RANK(500224, "该交易对已开启交易排名", "LANG_SYMBOL_OPEN_TRADE_RANK"),
    LANG_ILLEGAL_ADS_TYPE(500225, "广告类型错误", "LANG_ILLEGAL_ADS_TYPE"),
    LANG_SYMBOL_NOT_TRADE_RANK_DATA(500226, "该交易对没有交易排名数据", "LANG_SYMBOL_NOT_TRADE_RANK_DATA"),
    LANG_LITTLE_THAN_MIN_BUYVOLUME_TIP(500227, "数量小于最小买入数量", "LANG_LITTLE_THAN_MIN_BUYVOLUME_TIP"),
    LANG_LITTLE_THAN_MIN_BUYAMOUNT_TIP(500228, "总额小于最小买入总额", "LANG_LITTLE_THAN_MIN_BUYAMOUNT_TIP"),
    LANG_LITTLE_THAN_MIN_SELLVOLUME_TIP(500229, "数量小于最小卖出数量", "LANG_LITTLE_THAN_MIN_SELLVOLUME_TIP"),
    NOT_SUFFICIENT_FUNDS(500230, "账户资金不足", "NOT_SUFFICIENT_FUNDS"),
    LANG_ERROR_A_TYPE(500231, "错误的账户类型(只支持 银行卡 支付宝 微信)", "LANG_ERROR_A_TYPE"),
    LANG_ORDER_NOT_EXISTS(500232, "订单不存在", "LANG_ORDER_NOT_EXISTS"),
    LANG_ORDER_ALREADY_CONFIRMED(500233, "订单不是待付款状态", "LANG_ORDER_ALREADY_CONFIRMED"),
    LANG_ILLEGAL_ADS_ID(500234, "广告ID不存在", "LANG_ILLEGAL_ADS_ID"),
    LANG_ILLEGAL_ACCOUNT_TYPE(500235, "请完善对应的收款设置", "LANG_ILLEGAL_ACCOUNT_TYPE"),
    LANG_WAITING_ORDER_CONFIRM_MAX_THAN_2(500236, "超过两个订单待处理，不能再添加新订单", "LANG_WAITING_ORDER_CONFIRM_MAX_THAN_2"),
    LANG_WAITING_ORDER_MAX_THAN_2(500237, "超过两个订单未付款，不能再添加新订单", "LANG_WAITING_ORDER_MAX_THAN_2"),
    LANG_ACCOUNT_HAS_PROBLEM(500238, "该用户账户可疑，冻结余额大于总金额", "LANG_ACCOUNT_HAS_PROBLEM"),
    LANG_POOL_NO_ADDRESS_EXISTS(500239, "该币种地址不存在", "LANG_POOL_NO_ADDRESS_EXISTS"),
    LANG_ADDRESS_ALREADY_EXISTS(500240, "该币种地址已存在", "LANG_ADDRESS_ALREADY_EXISTS"),
    LANG_WITHDRAW_STATUS_IS_NOT_WATTING(500241, "提现正在处理中", "LANG_WITHDRAW_STATUS_IS_NOT_WATTING"),
    LANG_CREATE_ORDER_FAIL(500242, "创建订单失败", "LANG_CREATE_ORDER_FAIL"),
    LANG_ACCOUNT_FUNDS_PROBLEM(500243, "账户资金错误", "LANG_ACCOUNT_FUNDS_PROBLEM"),
    LANG_CANCEL_ORDER_ERROR(500244, "取消订单异常", "LANG_CANCEL_ORDER_ERROR"),
    LANG_ORDER_TRADE_ERROR(500245, "订单交易异常", "LANG_ORDER_TRADE_ERROR"),
    LANG_FIND_CONDITION_NOT_NULL(500246, "查询条件不能为空", "LANG_FIND_CONDITION_NOT_NULL"),
    LANG_LOCK_TYPE_ERROR(500246, "锁仓周期类型错误", "LANG_LOCK_TYPE_ERROR"),
    //eg. 会员A登录后操作会员B的订单、交易、账户之类的操作就报这个错误
    LANG_ERROR_OPERATION_OBJECT(500247, "操作错误的对象", "LANG_ERROR_OPERATION_OBJECT"),
    LANG_INVALID_DATA(500248, "价格已经过期失效，请重新下单", "LANG_INVALID_DATA"),
    LANG_PLS_SET_SEC_PWD(500249, "请先设置资金密码！", "LANG_PLS_SET_SEC_PWD"),
    LANG_INSUFFICIENT_STOCK(500250, "库存不足！", "LANG_INSUFFICIENT_STOCK"),
    LANG_UPPER_AMOUNT(500251, "已达到最大购买额度！", "LANG_UPPER_AMOUNT"),
    LANG_LOWER_AMOUNT(500252, "不能低于最小购买额度！", "LANG_LOWER_AMOUNT"),
    LANG_ORDER_DUPLICATE_ERROR(500253, "订单号重复", "DUPLICATE O_no"),
    CROWD_ORDER_IS_STOP(500254, "暂停下单", "CROWD_ORDER_IS_STOP"),
    COMPLAIN_BY_UNCONFIRMED(500255, "未放行申诉，二十四小时内不能出售", "COMPLAIN_BY_UNCONFIRMED"),
    HAS_ONE_TRADING(500256, "您已存在一单进行的交易，处理完成后方可再次购买！", "HAS_ONE_TRADING"),
    BUY_HAS_ONE_TRADING(500257, "您已存在一笔买单正在进行交易，处理完成后方可再次购买！", "BUY_HAS_ONE_TRADING"),
    SELL_HAS_ONE_TRADING(500258, "您已存在一卖单正在进行交易，处理完成后方可再次购买！", "SELL_HAS_ONE_TRADING"),
    LANG_ILLEGAL_TRANSFERNUM(500259, "错误的划转数量", "LANG_ILLEGAL_TRANSFERNUM"),
    LANG_ILLEGAL_CLIENT_STATUS(500260, "客户端服务不可用", "LANG_ILLEGAL_CLIENT_STATUS"),
    LANG_INVALID_BINDNAME(500261, "绑定名字与实名认证名字不一致", "LANG_INVALID_BINDNAME"),
    PAYMENT_PASSWORD_CANNOT_BE_EMPTY(500262, "支付密码不能为空", "PAYMENT_PASSWORD_CANNOT_BE_EMPTY"),
    TRANSFER_ACCOUNT_CANNOT_BE_EMPTY(500263, "转入账号不能为空", "TRANSFER_ACCOUNT_CANNOT_BE_EMPTY"),
    FLASH_SALE_NOT_START(500264,"未开放此抢购","FLASH_SALE_NOT_START"),
    LANG_BIGGER_THAN_MAX_BUYVOLUME_TIP(500265, "数量大于最大购买量", "LANG_BIGGER_THAN_MAX_BUYVOLUME_TIP"),
    CURRENCY_WITHDRAWAL_EXCEPTION(500266, "提币失败", "CURRENCY_WITHDRAWAL_EXCEPTION"),
    WITHDRAW_ADDR_ILLEGAL(500267, "提币地址不合法", "WITHDRAW_ADDR_ILLEGAL"),
    TYPE_FAIL(500268, "类型不正确", "TYPE_FAIL"),
    NOT_SUFFICIENT_FROZEN_FUNDS(500230, "账户冻结资金不足", "NOT_SUFFICIENT_FROZEN_FUNDS"),
    LANG_BIGGER_THAN_MAX_PRICE(500231, "下单价大于最大限定价", "LANG_BIGGER_THAN_MAX_PRICE"),
    LANG_BIGGER_THAN_MIN_PRICE(500232, "下单价小于最小限定价", "LANG_BIGGER_THAN_MIN_PRICE"),
    LANG_INVALID_PRICE_UPS_DOWN_HIGH(500262, "超过最高限额", "LANG_INVALID_PRICE_UPS_DOWN_HIGH"),
    LANG_INVALID_PRICE_UPS_DOWN_LOW(500263, "超过最低限额", "LANG_INVALID_PRICE_UPS_DOWN_LOW"),

    LANG_GREATER_THAN_LIMIT(500264, "购买总量大于个人可购限额", "LANG_GREATER_THAN_LIMIT"),

    LANG_CLOSE_ORDER_LIMIT(500269, "下单60s内不能平仓", "LANG_CLOSE_ORDER_LIMIT"),


    // 后台管理端 5003xx
    LANG_TRADE_RANK_NOT_END_NOT_REWORD(500300, "交易排名还没有结束,不能发送奖励!", "LANG_TRADE_RANK_NOT_END_NOT_REWORD"),
    LANG_REWORD_HAS_SEND(500301, "该交易排名奖励已发放, 不能重复发送!", "LANG_REWORD_HAS_SEND"),
    PASSWORD_ERROR(500302, "对不起，您输入的当前密码有误！", "PASSWORD_ERROR"),
    PLEASE_SELECT_CURRENCY(500303, "请选择币种！", "PLEASE_SELECT_CURRENCY"),
    TRADE_AREA_HAS_NUMBER_ONE_SYMBOL(500304, "该交易区已有排名第一的交易!", "TRADE_AREA_HAS_NUMBER_ONE_SYMBOL"),
    BASE_CURRENCY_NOT_EXISTS(500305, "基础货币不存在!", "BASE_CURRENCY_NOT_EXISTS"),
    QUOTE_CURRENCY_NOT_EXISTS(500306, "计价货币不存在!", "QUOTE_CURRENCY_NOT_EXISTS"),
    ACCESS_HAS_BEEN_DENIED(500307, "您没有访问此页面的权限！", "ACCESS_HAS_BEEN_DENIED"),
    DEL_COLUMN_TIP(500308, "本栏目已经使用，不能删除，您可以修改它的是否启用为“否”！", "DEL_COLUMN_TIP"),
    SEND_AUDIT_ARTICLE_TIP(500309, "只有状态为新建的文章才可以送审，修改文章可以使文章状态变成新建！", "SEND_AUDIT_ARTICLE_TIP"),
    AUDIT_ARTICLE_TIP(500310, "只有状态为待审核的文章才可以审核！", "AUDIT_ARTICLE_TIP"),
    BASE_AND_QUOTE_CURRENCY_NOT_CONSISTENT(500311, "基础货币和计价货币不能一致 ！", "BASE_AND_QUOTE_CURRENCY_NOT_CONSISTENT"),
    ORDER_STATUS_NOT_CONFIRM(500312, "该订单状态不为未确认 ！", "ORDER_STATUS_NOT_CONFIRM"),
    ADS_HAS_USED_NOT_DELETE(500313, "本广告主已经使用，不能删除！", "ADS_HAS_USED_NOT_DELETE"),
    ACCOUNT_HAS_LOCKED(500314, "对不起，您的账号已被锁定,请与管理员联系！", "ACCOUNT_HAS_LOCKED"),
    ACCOUNT_OR_PASSWORD_ERROR(500315, "您输入的用户名或密码错误！", "ACCOUNT_OR_PASSWORD_ERROR"),
    LOGIN_INFO_ERROR(500316, "用户登录信息错误！", "LOGIN_INFO_ERROR"),
    PLEASE_SELECT_SYMBOL(500317, "请选择交易对！", "PLEASE_SELECT_SYMBOL"),
    INCLUDE_SENSITIVE_WORDS(500318, "包含敏感词汇", "INCLUDE_SENSITIVE_WORDS"),
    JOB_GROUP_NAME_DUPLICATION(500319, "任务组名称重复", "JOB_GROUP_NAME_DUPLICATION"),
    JOB_MAX(500319, "任务数量已到最大", "JOB_MAX"),
    JOB_SEC_NUM_THAN_10(500320, "任务数量不能超过10", "JOB_SEC_NUM_THAN_10"),
    EXPORT_EXCEL_LIMIT_GT_5000(500321, "导出记录不能超过5000", "EXPORT_EXCEL_LIMIT_GT_5000"),
    NOT_IS_SUPERUSER(500322, "该用户不是管理员账户", "NOT_IS_SUPERUSER"),
    SYMBOL_IS_ALREADY_EXISTS(500323, "该交易区交易排名已存在!", "SYMBOL_IS_ALREADY_EXISTS"),

    // OTC相关 5004xx
    //商家状态不是 APPLY_PASSED(2, "apply_passed")
    INVALID_MERCHANT(500401, "无效的商家！", "INVALID_MERCHANT"),
    TRADE_COMPLAINING(500402, "交易申诉中！", "TRADE_COMPLAINING"),
    ERROR_OTC_CURRENCY(500403, "错误的OTC交易币种！", "ERROR_OTC_CURRENCY"),
    ACCOUNTTYPE_ONLY_ONE(500404, "账户类型只能绑定一个账号", "ACCOUNTTYPE_ONLY_ONE"),
    LANG_REPEAT_ERROR(500405, "不能重复操作或者数据重复", "LANG_REPEAT_ERROR"),
    LANG_ERROR_STATUS(500406, "错误的状态", "LANG_ERROR_STATUS"),
    LANG_NO_RECORD(500407, "没有记录", "LANG_NO_RECORD"),
    LANG_OPERTION_FAIL(500408, "操作失败", "LANG_OPERTION_FAIL"),
    RELATION_CUSTOMER_SERVICE(500409, "联系客服", "RELATION_CUSTOMER_SERVICE"),
    ONLY_MOBILE_CUSTOMER(500410, "仅支持手机用户", "ONLY_MOBILE_CUSTOMER"),
    SELF_TRADE_ERROR(500411, "不能自己交易", "SELF_TRADE_ERROR"),
    COMPLETE_OTHER_TRADE_PAY(500412, "需要完成其他交易的付款", "COMPLETE_OTHER_TRADE_PAY"),
    CANCEL_TRADE_OVERMUCH(500413, "撤销交易过多", "CANCEL_TRADE_OVERMUCH"),
    LANG_ERROR_DATA(500414, "错误的数据", "LANG_ERROR_DATA"),
    LANG_ERROR_OPERTION(500415, "错误的操作", "LANG_ERROR_OPERTION"),
    TOTAL_BALANCE_LESS_THAN(500416, "总额小于0", "TOTAL_BALANCE_LESS_THAN"),
    LACK_OF_EFFECTIVE_ASSETS(500417, "有效资产不足", "LACK_OF_EFFECTIVE_ASSETS"),
    ORDER_TYPE_ONLY_TWO(500418, "每个币种同一交易类型最多只能发布两笔广告", "ORDER_TYPE_ONLY_TWO"),
    TOKEN_NOT_EXISTS(500419, "TOKEN不存在", "TOKEN_NOT_EXISTS"),
    GET_LOCK_FAIL(5004200, "未能获取到锁！", "GET_LOCK_FAIL"),
    MAST_BE_AUDIT_PASS(5004201, "该商家未审核通过，请先审核！", "MAST_BE_AUDIT_PASS"),
    RULE_IS_EXISTS(500419, "当前币种锁仓规则已存在", "RULE_IS_EXISTS"),
    RULE_IS_NOT_EXISTS(500420, "当前币种锁仓规则不存在", "RULE_IS_NOT_EXISTS"),
    RULE_IS_NOT_ENABLE(500421, "当前币种锁仓规则未启用", "RULE_IS_NOT_ENABLE"),
    RULE_IS_ENABLE(500422, "当前币种锁仓规则已启用", "RULE_IS_ENABLE"),
    RELEASE_END(500423, "当前用户锁仓资产已停止释放", "RELEASE_END"),
    RELEASING(500424, "当前用户锁仓资产正在释放", "RELEASING"),
    UN_RELEASE(500425, "当前用户锁仓资产处于待释放状态", "UN_RELEASE"),
    RULE_USERS_USING(500426, "当前币种锁仓规则有用户在使用", "RULE_USERS_USING"),
    CURRENCY_EXCEPTION(500427, "请求币种参数与当前的币种不匹配", "CURRENCY_EXCEPTION"),
    OTHER_RULE_IS_ENABLE(500428, "当前币种已有其他锁仓规则在启用", "OTHER_RULE_IS_ENABLE"),
    UN_LOCK_CURRENCY(500429, "当前币种为非锁仓币", "UN_LOCK_CURRENCY"),
    HOUR_MAX_23(500430, "非交易锁仓小时最大间隔时长为23小时", "HOUR_MAX_23"),
    DAY_MAX_29(500431, "非交易锁仓天数最大间隔时长为29天", "DAY_MAX_29"),
    MONTH_MAX_11(500432, "非交易锁仓月数最大间隔时长为11个月", "MONTH_MAX_11"),
    MIN_MAX_59(500436, "非交易锁仓分钟数最大间隔时长为59分钟", "MIN_MAX_59"),
    COINRECHARGEID_IS_ALREADY_EXISTS(500433, "当前锁仓充值信息已存在，请勿重复确认", "COINRECHARGEID_IS_ALREADY_EXISTS"),
    RULEDETAIL_MAST_BE_MORE_THAN_ZERO(500435, "释放比例必须在0～100之间", "RULEDETAIL_MAST_BE_MORE_THAN_ZERO"),
    LOCKRELEASETIME_MAST_BE_MORE_THAN_ZERO(500434, "释放间隔时长必须大于0", "LOCKRELEASETIME_MAST_BE_MORE_THAN_ZERO"),

    //ocr和人像对比相关
    OCR_FRONT_ERROR(500500, "身份证正面识别失败", "OCR_FRONT_ERROR"),
    OCR_BACK_ERROR(500501, "身份证反面识别失败", "OCR_BACK_ERROR"),
    OCR_IDCARD_ALREADY(500502, "该身份证已被实名", "OCR_IDCARD_ALREADY"),
    OCR_REQUEST_TO_OFTEN(500503, "因请求过于频繁，您已被系统限制，请您在24小时后再次进行实名！或提交工单联系客服进行处理", "OCR_REQUEST_TO_OFTEN"),
    OCR_OCR_ID_STATUS_UNDER_REVIEW(500504, "您的实名认证正在审核中，请耐心等待哦！", "UNDER_REVIEW"),
    OCR_ID_STATUS_AUDIT_SUCCEEDED(500505, "您的实名认证已经审核成功请勿重复提交！", "AUDIT_SUCCEEDED"),
    PORTRAIT_CONTRAST_UPLOAD_ID_CARD(500506, "请您先上传身份证正反面再进行人脸比对！", "Please upload the front and back of ID card before face comparison"),
    PORTRAIT_CONTRAST_ERROR(500507, "人像对比失败！", "PORTRAIT_CONTRAST_ERROR"),
    OCR_UPLOAD_FRONT_IMAGE_EXCEED_SIZE(500508, "您上传的身份证正面超出上传大小，请压缩后重试！", "OCR_UPLOAD_FRONT_IMAGE_EXCEED_SIZE"),
    OCR_UPLOAD_BACK_IMAGE_EXCEED_SIZE(500509, "您上传的身份证背面超出上传大小，请压缩后重试！", "OCR_UPLOAD_BACK_IMAGE_EXCEED_SIZE"),
    PORTRAIT_CONTRAST_UPLOAD_IMAGE_EXCEED_SIZE(500510, "您上传的图片超出上传大小，请压缩后重试！", "PORTRAIT_CONTRAST_UPLOAD_IMAGE_EXCEED_SIZE"),



    // 钱包 5006xx
    WALLET_BACKWRITE_ERROR(500600, "钱包回写异常", "WALLET_BACKWRITE_ERROR"),

    //合约
    LANG_EXIST_ORDER(500600, "您有未完成的全仓订单", "LANG_EXIST_ORDER"),
    ERROR_CURRENCY(500601, "该币种无划转权限", "ERROR_CURRENCY"),
    CONTRACT_INVITATION_CODE_DOES_NOT_EXIST(500602, "邀请码不存在", "CONTRACT_INVITATION_CODE_DOES_NOT_EXIST"),
    CONTRACT_INVITATION_CODE_DOES_NOT_NULL(500603, "邀请码不能为空", "CONTRACT_INVITATION_CODE_DOES_NOT_NULL"),
    CONTRACT_INVITATION_CODE_ALREADY_EXISTS(500604, "已绑定邀请码，请勿重复绑定", "CONTRACT_INVITATION_CODE_ALREADY_EXISTS"),
    CONTRACT_VA_OPERATION_LIMIT(500605, "模拟账号没有此操作权限", "CONTRACT_VA_OPERATION_LIMIT"),
    CONTRACT_ANALOG_ACCOUNT_NO_INVITER(500606,"模拟账号不能作为邀请人","CONTRACT_ANALOG_ACCOUNT_NO_INVITER")
    ;



    private Integer errorCode;

    private String errorCNMsg;

    private String errorENMsg;

    ErrorInfoEnum(Integer errorCode, String errorCNMsg, String errorENMsg) {
        this.errorCode = errorCode;
        this.errorCNMsg = errorCNMsg;
        this.errorENMsg = errorENMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorCNMsg() {
        return errorCNMsg;
    }

    public String getErrorENMsg() {
        return errorENMsg;
    }

    /**
     * 根据英文拿到中文
     * @param enMsg
     * @return
     */
    public static String errorCNMsgOf(String  enMsg) {
        for (ErrorInfoEnum errorInfoEnum : values()) {
            if(errorInfoEnum.getErrorENMsg().equals(enMsg)) {
                return errorInfoEnum.getErrorCNMsg();
            }
        }
        return null;
    }

}
