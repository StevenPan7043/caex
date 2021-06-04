package com.pmzhongguo.ex.core.web;


import com.pmzhongguo.zzextool.utils.PropertiesUtil;

public class Constants
{
    /**
     * sys开头为系统参数
     */
    public static final String SYS_SESSION_TEMP = "TEMP_";
    public static final String SYS_SESSION_MOD = "SYS_SESSION_MOD";
    public static final String SYS_SESSION_USER = "SYS_SESSION_USER";// 用户登录session
    public static final String SYS_SESSION_MEMBER = "SYS_SESSION_MEMBER";// 前台会员登录session
    public static final String SYS_SESSION_RANDOM = "SYS_SESSION_RANDOM";
    public static final String SYS_SESSION_USER_CODE = "SYS_SESSION_USER_CODE";
    public static final String SYS_PAGE_ENCODE = "UTF-8";
    public static final String SYS_AJAX_RESULT_FLAG = "success";
    public static final String SYS_AJAX_RESULT_MESSAGE = "msg";
    public static final String SYS_AJAX_RESULT_DATA = "data";
    public static final String SYS_ACTION_MESSAGE = "msg";
    public static final String SESSION_OUT = "session_out";
    public static final String NO_AUTHORITY_ACCESS = "no_authority_access"; // 访问限制

    public static final String USER_FUNCTION_KEY = "USER_FUNCTIONCELL_LIST";// 用户权限名称
    public static final String FACTORY_FUNCTION_KEY = "FACTORY_FUNCTION_KEY";// 工厂权限名称

    /**
     * app开头的为程序中用到的参数
     */
    public static final String APP_LOGIN_IMGE_CODE = "APP_LOGIN_IMGE_CODE";
    public static final int APP_SQL_BATCH_NUM = 200;
    public static final String APP_SQL_BATCH_KEY = "sqlName";

    public static final String APP_EXCEL_COLID = "excelColIds";
    public static final String APP_EXCEL_COLTEXT = "excelColTexts";
    public static final String APP_EXCEL_FILENAME = "fileName";

    public static final String JPEGCODE = "JPEGCODE";

    public static final Integer maxSize = 20000;
    // 存放在header中token的key
    public static final String LOGIN_TOKEN = "PEPPAEX_TOKEN";
    // 短信超时时间，单位：分钟
    public static final Integer SMSCODE_TIME_OUT = 5;

    public static int MEMBER_SESSION_TIME_OUT = 7200; //用户session超时时间，单位秒，2小时
    /**
     * 用户Token超时时间，单位秒 24 小时
     */
    public static int MEMBER_TOKEN_TIME_OUT = 60 * 60 * 24;
    /**
     * 用户上次登录超过4个小时，需要输入短信验证码验证 单位毫秒
     */
    public static int MEMBER_LAST_LOGIN_TIME = 4 * 60 * 60 * 1000;
    /**
     * 用户Token超时时间，单位秒 3分钟
     */
    public static int MEMBER_SMSCODE_TIME_OUT = 180;
    /**
     * 用户Token超时时间，单位秒 3分钟
     */
    public static int CHECK_CODE_TIME_OUT = 180;

    public static int LOCK_HRC_PRECISION = 4;

    /**
     * 用户登录发送验证码的token
     */
    public static final String LOGIN_TOKEN_VERIFICATION = "login_token_verification_";

    /**
     * 第三方授权key
     */
    public static final String AUTH_TOKEN_VERIFICATION = "auth2_token_verification_";
    /**
     * 用户登录发送验证码的token有效时间，单位秒
     */
    public static final int LOGIN_TOKEN_VERIFICATION_EXPIRE = 5 * 60;

    /**
     * 第三方授权过期默认7天
     */
    public static final int AUTH_TOKEN_VERIFICATION_EXPIRE = 7 * 24 * 60 * 60;


    /**
     * 划转第三方信息缓存前缀
     */
    public static final String PROJECT_NAME_PRE = "third_party_pre_";

    /**
     * 根目录
     */
    public static String ZK_ROOT = "/zzex";

    /**
     * zk同步目录
     */
    public static String ZK_SYNC_PATH = ZK_ROOT + "/sync";

    /**
     * zk 监听路径 交易对
     */
    public static String ZK_WATCH_PATH_CURRENCY_PAIR = ZK_SYNC_PATH + "/currency-pair";

    /**
     * zk 监听路径 系统配置
     */
    public static String ZK_WATCH_PATH_SYS_CONFIG = ZK_SYNC_PATH + "/sys-config";

    /**
     * zk 监听路径 交易币种
     */
    public static String ZK_WATCH_PATH_CURRENCY = ZK_SYNC_PATH + "/currency";

    /**
     * zk 监听路径 公告文章
     */
    public static String ZK_WATCH_PATH_NEWS = ZK_SYNC_PATH + "/news";

    /**
     * zk 监听路径 app更新
     */
    public static String ZK_WATCH_PATH_APP = ZK_SYNC_PATH + "/app";

    /**
     * zk 监听路径 币种规则缓存
     */
    public static String ZK_WATCH_PATH_CURRENCY_RULE = ZK_SYNC_PATH + "/currency-rule";

}
