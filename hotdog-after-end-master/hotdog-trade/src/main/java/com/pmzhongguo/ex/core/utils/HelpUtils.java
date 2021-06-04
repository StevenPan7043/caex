package com.pmzhongguo.ex.core.utils;

/**
 * 说明：文本及日期处理方法
 * 编写者：edward
 * 日期：Aug 8, 2007
 * KevinChow版权所有。
 */

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.pmzhongguo.ex.business.dto.SymbolDto;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.business.entity.ReqBaseSecret;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.business.service.MarketService;
import com.pmzhongguo.ex.business.service.RuleServiceManage;
import com.pmzhongguo.ex.core.web.ChainTypeEnum;
import com.pmzhongguo.ex.core.web.Constants;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.framework.entity.FrmConfig;
import com.pmzhongguo.ex.framework.entity.FrmUser;
import com.pmzhongguo.otc.otcenum.MobiInfoTemplateEnum;
import com.pmzhongguo.zzextool.utils.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.logicalcobwebs.proxool.ConnectionPoolDefinitionIF;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.BreakIterator;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * Title:文本及日期处理方法
 * <p>
 * Description:通用工具类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: KevinChow
 * </p>
 */
public class HelpUtils extends StringUtils {
    private static final Logger logger = LoggerFactory.getLogger(HelpUtils.class);
    /**
     * "符号
     */
    private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();

    /**
     * &符号
     */
    private static final char[] AMP_ENCODE = "&amp;".toCharArray();

    /**
     * <符号
     */
    private static final char[] LT_ENCODE = "&lt;".toCharArray();

    /**
     * >符号
     */
    private static final char[] GT_ENCODE = "&gt;".toCharArray();

    /**
     * 摘要算法
     */
    private static MessageDigest digest = null;

    public final static String PRE_INTRODUCE_ID = ""; // UID前缀，加两位是使用户数看起来不那么少

    /**
     * MgrConfig表存放到Session中的Key值
     */
    public final static String MGRCONFIG = "MGRCONFIG";

    public static final String EOSLIST = "frm_config_eoslist";

    public static final int EXPORT_LIMIT_NUM = 5000;

    /**
     * CurrencyPair表存放到Session中的Key值
     */
    public final static String CURRENCYPAIRMAP = "CURRENCYPAIRMAP";
    public final static String CURRENCYPAIRLST = "CURRENCYPAIRLST";
    public final static String CURRENCYPAIRSYMBOL = "CURRENCYPAIRSYMBOL";
    public final static String CURRENCYPAIRSYMBOLSTR = "CURRENCYPAIRSYMBOLSTR";

    /**
     * 缓存 d_currency 所有 is_show = 1 的币种，map 形式存储
     */
    public final static String CURRENCYMAP = "CURRENCYMAP";
    /**
     * 缓存 d_currency 所有 is_show = 1 的币种，list 形式存储
     */
    public final static String CURRENCYLST = "CURRENCYLST";

    /**
     * 缓存 d_currency 所有币种，map 形式存储
     */
    public final static String CURRENCY_ALL_MAP = "CURRENCY_ALL_MAP";
    /**
     * 缓存 d_currency 所有币种，list 形式存储
     */
    public final static String CURRENCY_ALL_LST = "CURRENCY_ALL_LST";
    /**
     * 国际语言
     */
    public final static String COUNTRYMAP = "COUNTRYMAP";

    public final static String COUNTRYLIST = "COUNTRYLIST";

    public final static String CURRENCY_IS_OTC_MAP = "CURRENCY_IS_OTC_MAP";
    public final static String CURRENCY_IS_OTC_LST = "CURRENCY_IS_OTC_LST";
    public final static String CURRENCY_IS_LOCK_LST = "CURRENCY_IS_LOCK_LST";
    public final static String CURRENCY_IS_LOCK_MAP = "CURRENCY_IS_LOCK_MAP";

    private static String databaseUser = "";
    private static String databasePwd = "";
    private static String databaseDatabase = "";
    private static String databaseIP = "";

    public final static String NOTICE_LST = "NOTICE_LST";
    public final static String NEWS_LST = "NEWS_LST";
    public final static String HELP_LST = "HELP_LST";
    public final static String APP_INFO_MAP = "APP_INFO_MAP";
    public final static String CUSTOM_SERVICE_QR_CODE_LST = "CUSTOM_SERVICE_QR_CODE_LST";

    public final static String ARTICLE_TITLE_ID = "ARTICLE_TITLE_ID";

    public static Map firstSymbol = new HashMap<Integer, String>();
    public static Map firstDspName = new HashMap<Integer, String>();

    private static final String YYYY_MM_DD_HHMM_FORMAT = "yyyy-MM-dd HH:mm";
    private static final String YYYY_MM_DD_HHMMSS_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat yyyy_MM_dd_HHmm = new SimpleDateFormat(YYYY_MM_DD_HHMM_FORMAT);
    private static SimpleDateFormat yyyy_MM_dd_HHmmss = new SimpleDateFormat(YYYY_MM_DD_HHMMSS_FORMAT);

    public static final String OFFICIAL_CURRENCY = "USDT";


    /**
     * 系统官方法币大写
     *
     * @return
     */
    public static String getOfficialCurrencyToUpper() {
        return OFFICIAL_CURRENCY;
    }

    /**
     * 系统官方法币小写
     *
     * @return
     */
    public static String getOfficialCurrencyToLower() {
        return OFFICIAL_CURRENCY.toLowerCase();
    }

    /**
     * 获得当前登录的用户
     *
     * @return
     */
    public static FrmUser getFrmUser() {
        FrmUser frmUser = null;
        try {
            frmUser = (FrmUser) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession()
                    .getAttribute(Constants.SYS_SESSION_USER);
        } catch (Exception e) {

        }
        return frmUser;
    }

    /**
     * 获得当前系统的配置信息
     *
     * @return
     */
    public static FrmConfig getMgrConfig() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return (FrmConfig) wac.getServletContext().getAttribute(MGRCONFIG);
    }

    public static String getEoslist(String currency) {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return ((Map<String, String>) wac.getServletContext().getAttribute(EOSLIST)).get(currency);
    }

    /**
     * 获得当前系统的配置信息
     *
     * @return
     */
    public static Map<String, CurrencyPair> getCurrencyPairMap() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return (Map<String, CurrencyPair>) wac.getServletContext().getAttribute(CURRENCYPAIRMAP);
//		return (Map<String, CurrencyPair>) JedisUtil.getInstance().get(CURRENCYPAIRMAP, false);
    }

    /**
     * 获得当前系统的配置信息
     *
     * @return
     */
    public static List<CurrencyPair> getCurrencyPairLst() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return (List<CurrencyPair>) wac.getServletContext().getAttribute(CURRENCYPAIRLST);
//		return (List<CurrencyPair>) JedisUtil.getInstance().get(CURRENCYPAIRLST, false);
    }

    public static List<SymbolDto> getCurrencyPairSymbol() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return (List<SymbolDto>) wac.getServletContext().getAttribute(CURRENCYPAIRSYMBOL);
//		return (List<SymbolDto>) JedisUtil.getInstance().get(CURRENCYPAIRSYMBOL, false);
    }

    public static String getCurrencyPairSymbolStr() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return wac.getServletContext().getAttribute(CURRENCYPAIRSYMBOLSTR) + "";
    }

    public static List getNoticeLst() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return (List) wac.getServletContext().getAttribute(NOTICE_LST);
    }

    public static List getNewsLst() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return (List) wac.getServletContext().getAttribute(NEWS_LST);
    }

    public static List getHelpLst() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return (List) wac.getServletContext().getAttribute(HELP_LST);
    }

    /**
     * 从本地缓存获取所有 is_show = 1 的币种
     *
     * @return map 格式
     */
    public static Map<String, Currency> getCurrencyMap() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return (Map<String, Currency>) wac.getServletContext().getAttribute(CURRENCYMAP);
    }


    /**
     * 从本地缓存获取所有 is_show = 1 的币种
     *
     * @return list 格式
     */
    public static List<Currency> getCurrencyLst() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return (List<Currency>) wac.getServletContext().getAttribute(CURRENCYLST);
    }

    /**
     * 从本地缓存获取所有的币种
     *
     * @return map 格式
     */
    public static Map<String, Currency> getAllCurrencyMap() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return (Map<String, Currency>) wac.getServletContext().getAttribute(CURRENCY_ALL_MAP);
    }


    /**
     * 从本地缓存获取所有的币种
     *
     * @return list 格式
     */
    public static List<Currency> getAllCurrencyLst() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return (List<Currency>) wac.getServletContext().getAttribute(CURRENCY_ALL_LST);
    }

    /**
     * @return
     */
    public static Map<String, Currency> getCurrencyIsOtcMap() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return (Map<String, Currency>) wac.getServletContext().getAttribute(CURRENCY_IS_OTC_MAP);
    }

    public static List<Currency> getCurrencyIsOtcLst() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return (List<Currency>) wac.getServletContext().getAttribute(CURRENCY_IS_OTC_LST);
    }

//	public static Map<String, Currency> getCurrencyIsLockMap() {
//		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
//		return (Map<String, Currency>) wac.getServletContext().getAttribute(CURRENCY_IS_LOCK_MAP);
//	}

//	public static List<Currency> getCurrencyIsLockLst() {
//		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
//		return (List<Currency>) wac.getServletContext().getAttribute(CURRENCY_IS_LOCK_LST);
//	}

    /**
     * 判断当前用户是否具备某个权限
     *
     * @param functionId
     * @return
     */
    public static boolean userHasPermission(String functionId) {
        // 是否有权限
        boolean hasPermission = false;

        List<Map<String, Object>> ls = (List<Map<String, Object>>) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                .getSession().getAttribute("userMune");

        for (int i = 0; null != ls && i < ls.size(); i++) {
            Map<String, Object> one = ls.get(i);

            if (functionId.equals(one.get("id"))) {
                hasPermission = true;
            }
        }

        return hasPermission;
    }

    /**
     * 获取Proxool文件中的配置项
     *
     * @param name 目前支持USER PWD DATABASE
     * @return
     */
    public static String getProxoolConfigInfo(String name) {
        try {
            if ("USER".equals(name)) {
                if ("".equals(databaseUser)) {
                    ConnectionPoolDefinitionIF cpd = ProxoolFacade.getConnectionPoolDefinition("Pool_mySql");
                    databaseUser = cpd.getUser();
                }
                return databaseUser;
            }
            if ("PWD".equals(name)) {
                if ("".equals(databasePwd)) {
                    ConnectionPoolDefinitionIF cpd = ProxoolFacade.getConnectionPoolDefinition("Pool_mySql");
                    databasePwd = cpd.getPassword();
                }
                return databasePwd;
            }
            if ("DATABASE".equals(name)) {
                if ("".equals(databaseDatabase)) {
                    ConnectionPoolDefinitionIF cpd = ProxoolFacade.getConnectionPoolDefinition("Pool_mySql");
                    databaseDatabase = cpd.getUrl().substring(cpd.getUrl().lastIndexOf("/") + 1);
                }
                return databaseDatabase;
            }

            if ("DATABASEIP".equals(name)) {
                if ("".equals(databaseIP)) {
                    ConnectionPoolDefinitionIF cpd = ProxoolFacade.getConnectionPoolDefinition("Pool_mySql");
                    databaseIP = cpd.getUrl().substring(cpd.getUrl().lastIndexOf("//") + 2, cpd.getUrl().lastIndexOf(":3306"));
                }
                return databaseIP;
            }

        } catch (ProxoolException e) {
            logger.error("获得Proxool中的用户密码出错：" + e.toString());
        }
        return "";
    }

    public static Map newHashMap(Object... args) {
        return toMap(args);
    }

    public static Map toMap(Object[] args) {
        Map map = new HashMap();
        for (int i = 1; i < args.length; i += 2) {
            map.put(args[i - 1], args[i]);
        }
        return map;
    }

    /**
     * Pushlet需要用到这种方式的转码
     *
     * @param input
     * @return
     */
    public static String utf8ToISO(String input) {
        try {
            String output = new String((input).getBytes("UTF-8"), "ISO-8859-1");
            return output;
        } catch (UnsupportedEncodingException e) {
        }

        return "";
    }

    /**
     * 检查用户名中是否含有非法字符
     *
     * @param username
     * @return boolean
     */
    public static boolean validateUserName(String username) {
        Pattern p = Pattern.compile("^\\w+$");
        Matcher m = p.matcher(username);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 将字符数组转换成字符串，以","分隔并且加入字符"'"号
     *
     * @param values
     * @return String
     */
    public static String Array3String(String[] values) {
        StringBuffer result = new StringBuffer();
        if (values == null)
            return "";
        for (int i = 0; i < values.length; i++) {
            if (result.length() <= 0)
                result.append("'" + values[i] + "'");
            else
                result.append(",").append("'" + values[i] + "'");
        }
        return result.toString();
    }

    /**
     * 将字符数组转换成字符串，以","分隔
     *
     * @param values
     * @return String
     */
    public static String Array2String(String[] values) {
        StringBuffer result = new StringBuffer();
        if (values == null)
            return "";
        for (int i = 0; i < values.length; i++) {
            if (result.length() <= 0)
                result.append(values[i]);
            else
                result.append(",").append(values[i]);
        }
        return result.toString();
    }

    /**
     * 将对象数组转换成字符串，以","分隔
     *
     * @param values
     * @return String
     */
    public static String Array2String(Object[] values) {
        StringBuffer result = new StringBuffer();
        if (values == null)
            return "";
        for (int i = 0; i < values.length; i++) {
            if (result.length() <= 0)
                result.append(values[i].toString());
            else
                result.append(",").append(values[i].toString());
        }
        return result.toString();
    }

    /**
     * 将LIST对象转换成字符串
     *
     * @param values
     * @return String
     */
    public static String Array2String(List values) {
        StringBuffer result = new StringBuffer();
        if (values == null)
            return "";
        for (int i = 0; i < values.size(); i++) {
            if (result.length() <= 0)
                result.append(values.get(i).toString());
            else
                result.append(",").append(values.get(i).toString());
        }
        return result.toString();
    }

    /**
     * 将文本转换成64位编码
     *
     * @param txt
     * @return String
     */
    public static String base64Encode(String txt) {
        if (txt != null && txt.length() > 0) {
            txt = new BASE64Encoder().encode(txt.getBytes());
        }
        return txt;
    }

    /**
     * 将BYTE形式的字符转换为64位编码
     *
     * @param txt
     * @return String
     */
    public static String base64Encode(byte[] txt) {
        String encodeTxt = "";
        if (txt != null && txt.length > 0) {
            encodeTxt = new BASE64Encoder().encode(txt);
        }
        return encodeTxt;
    }

    /**
     * 将64位编码方式转换为字符串
     *
     * @param txt
     * @return String
     */
    public static String base64decode(String txt) {
        if (txt != null && txt.length() > 0) {
            byte[] buf;
            try {
                buf = new BASE64Decoder().decodeBuffer(txt);
                txt = new String(buf);
            } catch (IOException ex) {
            }
        }
        return txt;
    }

    /**
     * 将64位编码字符串转换为byte方式
     *
     * @param txt
     * @return byte[]
     */
    public static byte[] base64decodebyte(String txt) {
        byte[] buf = null;
        if (txt != null && txt.length() > 0) {
            try {
                buf = new BASE64Decoder().decodeBuffer(txt);
            } catch (IOException ex) {
            }
        }
        return buf;
    }

    /**
     * 替换字符串
     *
     * @param line      源字符串
     * @param oldString 需要替换的字符串
     * @param newString 替换为的字符串
     * @return String
     */
    public static final String replace(String line, String oldString, String newString) {
        if (line == null) {
            return null;
        }
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }

    /**
     * 替换字符串，忽略大小写
     *
     * @param line      源字符串
     * @param oldString 需要替换的字符串
     * @param newString 替换为的字符串
     * @return String
     */
    public static final String replaceIgnoreCase(String line, String oldString, String newString) {
        if (line == null) {
            return null;
        }
        String lcLine = line.toLowerCase();
        String lcOldString = oldString.toLowerCase();
        int i = 0;
        if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }

    /**
     * 替换字符串，忽略大小写，并记住被替换的位置
     *
     * @param line      源字符串
     * @param oldString 需要替换的字符串
     * @param newString 替换为的字符串
     * @param count     记录被替换的位置信息
     * @return String
     */
    public static final String replaceIgnoreCase(String line, String oldString, String newString, int[] count) {
        if (line == null) {
            return null;
        }
        String lcLine = line.toLowerCase();
        String lcOldString = oldString.toLowerCase();
        int i = 0;
        if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
            int counter = 0;
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
                counter++;
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            count[0] = counter;
            return buf.toString();
        }
        return line;
    }

    /**
     * 替换字符串，并记住被替换的位置
     *
     * @param line      源字符串
     * @param oldString 需要替换的字符串
     * @param newString 替换为的字符串
     * @param count     记录被替换的位置信息
     * @return String
     */
    public static final String replace(String line, String oldString, String newString, int[] count) {
        if (line == null) {
            return null;
        }
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0) {
            int counter = 0;
            counter++;
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0) {
                counter++;
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            count[0] = counter;
            return buf.toString();
        }
        return line;
    }

    /**
     * 将所有的HTML标记进行转码 包括(ie, &lt;b&gt;&lt;table&gt;, etc)
     *
     * @param in
     * @return String
     */
    public static final String escapeHTMLTags(String in) {
        if (null == in) {
            return null;
        }
        return in.replaceAll("(?i)<script", "&lt;script").replaceAll("(?i)<img", "&lt;img").replaceAll("<", "&lt;").replaceAll("(?i)script", "s_cript").replaceAll("(?i).src", ".s_rc").replaceAll("(?i)document", "d_ocument");
    }

    /**
     * 使用MD5算法，返回字符串的HASH字段 Hashes a String using the Md5 algorithm and returns
     * the result as a String of hexadecimal numbers. This method is
     * synchronized to avoid excessive MessageDigest object creation. If calling
     * this method becomes a bottleneck in your code, you may wish to maintain a
     * pool of MessageDigest objects instead of using this method.
     * <p>
     * A hash is a one-way function -- that is, given an input, an output is
     * easily computed. However, given the output, the input is almost
     * impossible to compute. This is useful for passwords since we can store
     * the hash and a hacker will then have a very hard time determining the
     * original password.
     * <p>
     * In Jive, every time a user logs in, we simply take their plain text
     * password, compute the hash, and compare the generated hash to the stored
     * hash. Since it is almost impossible that two passwords will generate the
     * same hash, we know if the user gave us the correct password or not. The
     * only negative to this system is that password recovery is basically
     * impossible. Therefore, a reset password method is used instead.
     *
     * @param data the String to compute the hash of.
     * @return a hashed version of the passed-in String
     */
    public synchronized static final String hash(String data) {
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException nsae) {
                System.err.println("Failed to load the MD5 MessageDigest. " + "We will be unable to function normally.");
                logger.error(nsae.toString());
            }
        }
        // Now, compute hash.
        digest.update(data.getBytes());
        return encodeHex(digest.digest());
    }

    /**
     * 将bytes数组转换成16进制编码字符串
     *
     * @param bytes
     * @return String
     */
    public static final String encodeHex(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        int i;

        for (i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buf.toString().toUpperCase();
    }

    /**
     * 将16进制的编码转换回bytes数组
     *
     * @param hex
     * @return String
     */
    public static final byte[] decodeHex(String hex) {
        char[] chars = hex.toCharArray();
        byte[] bytes = new byte[chars.length / 2];
        int byteCount = 0;
        for (int i = 0; i < chars.length; i += 2) {
            byte newByte = 0x00;
            newByte |= hexCharToByte(chars[i]);
            newByte <<= 4;
            newByte |= hexCharToByte(chars[i + 1]);
            bytes[byteCount] = newByte;
            byteCount++;
        }
        return bytes;
    }

    /**
     * 将16进制char转换成字符串
     *
     * @param ch
     * @return String
     */
    private static final byte hexCharToByte(char ch) {
        switch (ch) {
            case '0':
                return 0x00;
            case '1':
                return 0x01;
            case '2':
                return 0x02;
            case '3':
                return 0x03;
            case '4':
                return 0x04;
            case '5':
                return 0x05;
            case '6':
                return 0x06;
            case '7':
                return 0x07;
            case '8':
                return 0x08;
            case '9':
                return 0x09;
            case 'a':
                return 0x0A;
            case 'b':
                return 0x0B;
            case 'c':
                return 0x0C;
            case 'd':
                return 0x0D;
            case 'e':
                return 0x0E;
            case 'f':
                return 0x0F;
        }
        return 0x00;
    }

    /**
     * 使用BreakIterator.wordInstance()进行分词转换 Converts a line of text into an
     * array of lower case words using a BreakIterator.wordInstance().
     * <p>
     * <p>
     * This method is under the Jive Open Source Software License and was
     * written by Mark Imbriaco.
     *
     * @param text a String of text to convert into an array of words
     * @return text broken up into an array of words.
     */
    public static final String[] toLowerCaseWordArray(String text) {
        if (text == null || text.length() == 0) {
            return new String[0];
        }
        ArrayList wordList = new ArrayList();
        BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(text);
        int start = 0;
        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
            String tmp = text.substring(start, end).trim();
            tmp = replace(tmp, "+", "");
            tmp = replace(tmp, "/", "");
            tmp = replace(tmp, "\\", "");
            tmp = replace(tmp, "#", "");
            tmp = replace(tmp, "*", "");
            tmp = replace(tmp, ")", "");
            tmp = replace(tmp, "(", "");
            tmp = replace(tmp, "&", "");
            if (tmp.length() > 0) {
                wordList.add(tmp);
            }
        }
        return (String[]) wordList.toArray(new String[wordList.size()]);
    }

    /**
     * Array of numbers and letters of mixed case. Numbers appear in the list
     * twice so that there is a more equal chance that a number will be picked.
     * We can use the array to get a random number or letter by picking a random
     * array index.
     */
    /**
     * 数字和字符char[] random对象来操作
     */
    private static char[] numbersAndLetters = ("23456789ABCDEFGHJKMNPQRSTUVWXYZ").toCharArray();

    /**
     * 使用随机的方式取得数个字符，来自numbersAndLetters中的定义
     *
     * @param length
     * @return String
     */
    public static final String randomString(int length) {
        if (length < 1) {
            return null;
        }
        // Create a char buffer to put random letters and numbers in.
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[getRandom(30)];
        }
        return new String(randBuffer);
    }

    private static char[] numbers = ("01234567890123456789").toCharArray();

    public static final String randomNum(int length) {
        if (length < 1) {
            return null;
        }
        // Create a char buffer to put random letters and numbers in.
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbers[getRandom(19)];
        }
        return new String(randBuffer);
    }

    public static final String randomString(int length, Integer prefix) {
        if (length < 1) {
            return null;
        }
        // Create a char buffer to put random letters and numbers in.
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[getRandom(30)];
        }
        return prefix + new String(randBuffer);
    }

    /*
     * 单条短信发送,智能匹配短信模板
     *
     * @param apikey 成功注册后登录云片官网,进入后台可查看
     *
     * @param text 需要使用已审核通过的模板或者默认模板
     *
     * @param mobile 接收的手机号,仅支持单号码发送
     *
     * @return json格式字符串
     */
	/*public static boolean sendSms(String content, String mobile) {
		try {
			content = "【bigen】短信验证码：" + content + "，请在3分钟内验证，切勿泄露给他人。如非本人操作，请忽略。";
			// 连接超时及读取超时设置
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000"); // 连接超时：30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时：30秒
			String encode = "UTF-8";
			// 新建一个StringBuffer链接
			StringBuffer buffer = new StringBuffer();

			String contentUrlEncode = URLEncoder.encode(content, encode); // 对短信内容做Urlencode编码操作。注意：如

			// 把发送链接存入buffer中，如连接超时，可能是您服务器不支持域名解析，请将下面连接中的：【m.5c.com.cn】修改为IP：【115.28.23.78】
			buffer.append("http://m.5c.com.cn/api/send/index.php?username=" + "sypgr" + "&password_md5=" + MacMD5.CalcMD5("asdf1234", 32) + "&mobile=" + mobile + "&apikey="
					+ "fffae7c7c36f2c7e553491cc35f7e9ab" + "&content=" + contentUrlEncode + "&encode=" + encode);

			// System.out.println(buffer); //调试功能，输入完整的请求URL地址

			// 把buffer链接存入新建的URL中
			URL url = new URL(buffer.toString());

			// 打开URL链接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// 使用POST方式发送
			connection.setRequestMethod("POST");

			// 使用长链接方式
			connection.setRequestProperty("Connection", "Keep-Alive");

			// 发送短信内容
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

			// 获取返回值
			String result = reader.readLine();

			// 输出result内容，查看返回值，成功为success，错误为error，详见该文档起始注释
			logger.error("sms:" + result);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}*/

    /**
     * 过滤掉多余的0
     *
     * @param bigDecimal
     * @return
     */
    public static BigDecimal stripTrailingZeros(BigDecimal bigDecimal) {
        return new BigDecimal(bigDecimal.stripTrailingZeros().toPlainString());
    }

    public static boolean sendSms(String content, String mobile) {
        //		return sendSms(mobile, "SMS_144451615", content);  原阿里云短信
        String format = String.format(MobiInfoTemplateEnum.JH_SECURITY_CODE.getCode(), content, 5);
//		JuheUtil.send(mobile, MobiInfoTemplateEnum.JH_SECURITY_CODE.getType(), format);
        return true;
    }

    public static boolean sendSms(String mobile, String templateCode, String content) {
        //签名
        final String SIGN = "zzex";
        //key
        final String ACCESS_KEY_ID = "LTAIFAqz7HbDyumd";
        //secret
        final String ACCESS_SECRET = "90uOMbu2NMkLaPP9jjtWhwukXwB8Nk";
        //短信模板编号
//		final String TEMPLATE_CODE = "SMS_144451615";
        //阿里云短信服务regionID
        final String REGIONID = "cn-hangzhou";
        //短信模板内容
        String TEMPLATE_CONTENT = "您的验证码${code}，该验证码5分钟内有效，请勿泄漏与他人！";
        //短信API产品域名（接口地址固定，无需修改）
        final String DOMAIN = "dysmsapi.aliyuncs.com";
        try {
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）

            IClientProfile profile = DefaultProfile.getProfile(REGIONID, ACCESS_KEY_ID,
                    ACCESS_SECRET);
            DefaultProfile.addEndpoint(REGIONID, REGIONID, product, DOMAIN);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，
            request.setPhoneNumbers(mobile);
            request.setSignName(SIGN);
            request.setTemplateCode(templateCode);
            request.setTemplateParam("{\"code\":" + content + "}");
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            logger.error("短信请求结果: code：{}，message：{}", sendSmsResponse.getCode(), sendSmsResponse.getMessage());
            //请求成功
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("短信发送失败：{}", e.getMessage());
        }
        return false;
    }

    private static char[] numbersNo0 = ("1235678912356789").toCharArray();

    public static final String randomNumNo0(int length) {
        if (length < 1) {
            return null;
        }
        // Create a char buffer to put random letters and numbers in.
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersNo0[getRandom(15)];
        }
        return new String(randBuffer);
    }

    /**
     * 获得一个随机数
     *
     * @param maxRandom 随机数的取值访问
     * @return int
     */
    public static final int getRandom(int maxRandom) {
        return (int) ((1 - Math.random()) * maxRandom);
    }

    /**
     * Intelligently chops a String at a word boundary (whitespace) that occurs
     * at the specified index in the argument or before. However, if there is a
     * newline character before <code>length</code>, the String will be chopped
     * there. If no newline or whitespace is found in <code>string</code> up to
     * the index <code>length</code>, the String will chopped at
     * <code>length</code>.
     * <p>
     * For example, chopAtWord("This is a nice String", 10) will return "This is
     * a" which is the first word boundary less than or equal to 10 characters
     * into the original String.
     *
     * @param string the String to chop.
     * @param length the index in <code>string</code> to start looking for a
     *               whitespace boundary at.
     * @return a substring of <code>string</code> whose length is less than or
     * equal to <code>length</code>, and that is chopped at whitespace.
     */
    public static final String chopAtWord(String string, int length) {
        if (string == null) {
            return string;
        }

        char[] charArray = string.toCharArray();
        int sLength = string.length();
        if (length < sLength) {
            sLength = length;
        }

        // First check if there is a newline character before length; if so,
        // chop word there.
        for (int i = 0; i < sLength - 1; i++) {
            // Windows
            if (charArray[i] == '\r' && charArray[i + 1] == '\n') {
                return string.substring(0, i + 1);
            }
            // Unix
            else if (charArray[i] == '\n') {
                return string.substring(0, i);
            }
        }
        // Also check boundary case of Unix newline
        if (charArray[sLength - 1] == '\n') {
            return string.substring(0, sLength - 1);
        }

        // Done checking for newline, now see if the total string is less than
        // the specified chop point.
        if (string.length() < length) {
            return string;
        }

        // No newline, so chop at the first whitespace.
        for (int i = length - 1; i > 0; i--) {
            if (charArray[i] == ' ') {
                return string.substring(0, i).trim();
            }
        }

        // Did not find word boundary so return original String chopped at
        // specified length.
        return string.substring(0, length);
    }

    /**
     * 将XML标记中敏感的字符换成转义字符
     *
     * @param string
     * @return String
     */
    public static final String escapeForXML(String string) {
        if (string == null) {
            return null;
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = string.toCharArray();
        int len = input.length;
        StringBuffer out = new StringBuffer((int) (len * 1.3));
        for (; i < len; i++) {
            ch = input[i];
            if (ch > '>') {
                continue;
            } else if (ch == '<') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(LT_ENCODE);
            } else if (ch == '&') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(AMP_ENCODE);
            } else if (ch == '"') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(QUOTE_ENCODE);
            }
        }
        if (last == 0) {
            return string;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
    }

    /**
     * 将特殊字符转换成转义字符
     *
     * @param string
     * @return String
     */
    public static final String escapeForSpecial(String string) {
        if (string == null) {
            return null;
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = string.toCharArray();
        int len = input.length;
        StringBuffer out = new StringBuffer((int) (len * 1.3));
        for (; i < len; i++) {
            ch = input[i];
            if (ch > '>') {
                continue;
            } else if (ch == '<') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(LT_ENCODE);
            } else if (ch == '&') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(AMP_ENCODE);
            } else if (ch == '"') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(QUOTE_ENCODE);
            } else if (ch == '>') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(GT_ENCODE);
            }
        }
        if (last == 0) {
            return string;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
    }

    /**
     * 将转义字符转换成XML中的标准字符
     *
     * @param string
     * @return String
     */
    public static final String unescapeFromXML(String string) {
        string = replace(string, "&lt;", "<");
        string = replace(string, "&gt;", ">");
        string = replace(string, "&quot;", "\"");
        return replace(string, "&amp;", "&");
    }

    private static final char[] zeroArray = "0000000000000000".toCharArray();

    /**
     * 指定字符串长度，不足的加0
     *
     * @param string
     * @param length
     * @return String
     */
    public static final String zeroPadString(String string, int length) {
        if (string == null || string.length() > length) {
            return string;
        }
        StringBuffer buf = new StringBuffer(length);
        buf.append(zeroArray, 0, length - string.length()).append(string);
        return buf.toString();
    }

    /**
     * 将日期转换成毫秒，以15位记录，不足的补0
     *
     * @param date
     * @return String
     */
    public static final String dateToMillis(Date date) {
        return zeroPadString(Long.toString(date.getTime()), 15);
    }

    /**
     * 将集合类型转换成字符串，并以指定的分割符分割
     *
     * @param c     集合
     * @param spilt 分隔符
     * @return String
     */
    public static final String collectionToString(Collection c, String spilt) {
        if (c == null) {
            return null;
        }
        if (spilt == null) {
            return null;
        }
        String ret = "";
        ArrayList a = new ArrayList(c);
        try {
            for (int i = 0; i < a.size(); i++) {
                String t = (String) a.get(i);
                if (i == a.size() - 1) {
                    ret = ret + t;
                } else {
                    ret = ret + t + spilt;
                }
            }
            return ret;
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 根据指定的长度构建一个密码，密码中没有包含0,o,l和I，以免误解
     *
     * @param length
     * @return String
     */
    public static String genPassword(int length) {
        if (length < 1) {
            return null;
        }
        String[] strChars = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "m", "n", "p", "q", "r", "s",
                "t", "u", "v", "w", "x", "y", "z", "a"};
        // 没有0,o,l和I，以免误解
        StringBuffer strPassword = new StringBuffer();
        int nRand = (int) java.lang.Math.round(java.lang.Math.random() * 100);
        for (int i = 0; i < length; i++) {
            nRand = (int) java.lang.Math.round(java.lang.Math.random() * 100);
            strPassword.append(strChars[nRand % (strChars.length - 1)]);
        }
        return strPassword.toString();
    }

    /**
     * 根据指定的长度构建一个密码，以数字组成
     *
     * @param length
     * @return String
     */
    public static String genNumPassword(int length) {
        if (length < 1) {
            return null;
        }
        String[] strChars = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        StringBuffer strPassword = new StringBuffer();
        int nRand = (int) java.lang.Math.round(java.lang.Math.random() * 100);
        for (int i = 0; i < length; i++) {
            nRand = (int) java.lang.Math.round(java.lang.Math.random() * 100);
            strPassword.append(strChars[nRand % (strChars.length - 1)]);
        }
        return strPassword.toString();
    }

    /**
     * 返回中英文混合的字符串长度，一个中文字符算2个长度
     *
     * @param value
     * @return
     */
    public static int chineseLength(String value) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 截取中英文混合的字符串的指定长度，一个中文字符算2个长度
     *
     * @param s
     * @param length
     * @return
     */
    public static String chineseSubstring(String s, int length) {
        byte[] bytes = null;
        try {
            bytes = s.getBytes("Unicode");
        } catch (Exception e) {
            return "";
        }
        int n = 0; // 表示当前的字节数
        int i = 2; // 前两个字节是标志位，bytes[0] = -2，bytes[1] = -1。所以从第3位开始截取。
        for (; i < bytes.length && n < length; i++) {
            // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
            if (i % 2 == 1) {
                n++; // 在UCS2第二个字节时n加1
            } else {
                // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
                if (bytes[i] != 0) {
                    n++;
                }
            }
        }
        // 如果i为奇数时，处理成偶数
        if (i % 2 == 1) {
            // 该UCS2字符是汉字时，去掉这个截一半的汉字
            if (bytes[i - 1] != 0)
                i = i - 1;
                // 该UCS2字符是字母或数字，则保留该字符
            else
                i = i + 1;
        }

        String retStr = "";

        try {
            retStr = new String(bytes, 0, i, "Unicode");
        } catch (UnsupportedEncodingException e) {

        }

        return retStr;
    }

    /**
     * 根据指定的长度构建一个空格组成字符串
     *
     * @param length
     * @return String
     */
    public static String genSpaceString(int length) {
        if (length < 1) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * 输入URL，得到这个URL中的HTML代码
     *
     * @param str
     * @return String
     */
    public static String getHTML(String str) {
        // 判断该页面是否存在
        if (urlExists(str)) {
            StringBuffer sb = new StringBuffer();
            try {
                URL urlObj = new URL(str);
                InputStream streamObj = urlObj.openStream();
                InputStreamReader readerObj = new InputStreamReader(streamObj);
                BufferedReader buffObj = new BufferedReader(readerObj);
                String strLine;
                while ((strLine = buffObj.readLine()) != null) {
                    sb.append(strLine);
                }
                buffObj.close();
            } catch (Exception e) {
                return sb.toString();
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    /**
     * 输入一个数字得到它的ASCII码
     *
     * @param digit
     * @return String
     */
    public static String getAsciiString(int digit) {
        byte ret[] = new byte[1];
        ret[0] = (byte) digit;
        return new String(ret);
    }

    /**
     * 输入一个字符串得到它的ASCII码
     *
     * @param s
     * @return String
     */
    public static int getAsciiNum(String s) {
        if (s.length() < 1) {
            return 0;
        }
        byte b = s.getBytes()[0];
        return b;
    }

    /**
     * 获得某个月的第一天
     *
     * @param monthOffset 相对于本月的偏移量，例如上个月就是-1，下个月就是1
     * @return
     */
    public static String getMonthFirstDay(int monthOffset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, monthOffset);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String retStr = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return retStr;
    }

    /**
     * 获得当前时间，并转换成yyyyMMddHHmmss格式
     *
     * @return String
     */
    public static String getCurrTime() {
        return formatDateByFormatStr(new Date(), "yyyyMMddHHmmss");
    }

    /**
     * 获得当前时间的前面或者后面多少分钟
     *
     * @param min 负数表示前面多少分钟，正数表示当前时间后多少分钟
     * @return
     */
    public static String dateAddMin(int min) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(java.util.Calendar.MINUTE, min);

        return formatDate6(calendar.getTime());
    }


    /**
     * 获得当前时间的前面或者后面多少分钟
     *
     * @param min 负数表示前面多少分钟，正数表示当前时间后多少分钟
     * @return 返回 2019-12-01 18：06：22
     */
    public static String dateAddMinFormat8(int min) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(java.util.Calendar.MINUTE, min);

        return formatDate8(calendar.getTime());
    }

    /**
     * 获得当前时间的前面或者后面多少小时
     *
     * @param hour 负数表示前面多少小时，正数表示当前时间后多少小时
     * @return
     */
    public static String dateAddHour(int hour) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(java.util.Calendar.HOUR, hour);
        return formatDate6(calendar.getTime());
    }

    /**
     * 获得当前时间的前面或者后面多少天
     *
     * @param day 负数表示前面多少天，正数表示当前时间后多少天
     * @return
     */
    public static String dateAddDay(int day) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(java.util.Calendar.DATE, day);
        return formatDate(calendar.getTime());
    }

    /**
     * 获得当前时间的前面或者后面多少分钟
     *
     * @param min 负数表示前面多少分钟，正数表示当前时间后多少分钟
     * @return
     */
    public static Date dateAddMin(String strDate, int min, String dateFormat) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = sdf.parse(strDate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, min);
        return c.getTime();
    }

    /**
     * 格式化日期yyyy-MM-dd
     *
     * @param date
     * @return String
     */
    public static String formatDate(Date date) {
        return formatDateByFormatStr(date, "yyyy-MM-dd");
    }

    /**
     * 格式化日期MM-dd HH:mm
     *
     * @param myDate
     * @return String
     */
    public static String formatDate3(Date myDate) {
        return formatDateByFormatStr(myDate, "MM-dd HH:mm");
    }

    /**
     * 格式化日期HH:mm
     *
     * @param myDate
     * @return String
     */
    public static String formatDate10(Date myDate) {
        return formatDateByFormatStr(myDate, "HH:mm");
    }

    /**
     * 格式化日期yyyyMMdd
     *
     * @param myDate
     * @return String
     */
    public static String formatDate4(Date myDate) {
        return formatDateByFormatStr(myDate, "yyyyMMdd");
    }

    /**
     * 格式化日期时间yyyy-MM-dd HH:mm
     *
     * @param myDate
     * @return String
     */
    public static String formatDate6(Date myDate) {
        return formatDateByFormatStr(myDate, "yyyy-MM-dd HH:mm");
    }

    /**
     * 格式化日期yyyy年MM月dd日
     *
     * @param myDate
     * @return String
     */
    public static String formatDate7(Date myDate) {
        return formatDateByFormatStr(myDate, "yyyy年MM月dd日");
    }

    /**
     * 格式化日期yyyy年MM月dd日
     *
     * @param myDate
     * @return String
     */
    public static String formatDate9(Date myDate) {
        return formatDateByFormatStr(myDate, "MM月dd日");
    }

    /**
     * 格式化日期 yyyy-MM-dd HH:mm:ss
     *
     * @param myDate
     * @return
     */
    public static String formatDate8(Date myDate) {
        return formatDateByFormatStr(myDate, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化日期
     *
     * @param yyyyMMdd 20181012
     * @return
     * @throws ParseException
     */
    public static Date formatDate10(String yyyyMMdd) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date parse = format.parse(yyyyMMdd);
        return parse;
    }

    public static String formatDateOrdreNo(Date myDate) {
        return formatDateByFormatStr(myDate, "yyMMddHHmmss");
    }

    /**
     * 通过格式化字段来格式化日期
     *
     * @param myDate    输入的日期
     * @param formatStr 需要格式化的样式例如yyyy-M-D
     * @return 格式化后的日期类型
     */
    public static String formatDateByFormatStr(Date myDate, String formatStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
        return formatter.format(myDate);
    }

    /**
     * 判断当前时间是否在时间date2之前
     *
     * @param date2
     * @return
     */
    public static boolean isNowBefore(String date2) {
        try {
            Date date1 = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return date1.before(sdf.parse(date2));
        } catch (ParseException e) {
            System.out.print("[SYS] " + e.getMessage());
            return false;
        }
    }

    /**
     * @param str
     * @return
     */
    public static String trim(String str) {
        return str.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
    }

    public static boolean isNowBefore2(String date2) {
        try {
            Date date1 = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return date1.before(sdf.parse(date2));
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 通过格式化字段来格式化日期
     *
     * @param myDate    输入的日期
     * @param formatStr 需要格式化的样式例如yyyy-M-D
     * @return 格式化后的日期类型
     */
    public static String formatDateByFormatStr(Object myDate, String formatStr) {
        if (myDate == null)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
        return formatter.format(myDate);
    }

    /**
     * 通过格式参数格式化float
     *
     * @param f         输入的值
     * @param formatStr 需要格式化的样式例如####.##
     * @return String
     */
    public static String formatDouble(float f, String formatStr) {
        DecimalFormat format = new DecimalFormat(formatStr);
        return format.format(f);
    }

    /**
     * 通过格式参数格式化Object
     *
     * @param f         输入的值
     * @param formatStr 需要格式化的样式例如####.##
     * @return String
     */
    public static String formatDouble(Object f, String formatStr) {
        if (f == null)
            return "";
        DecimalFormat format = new DecimalFormat(formatStr);
        return format.format(f);
    }

    /**
     * 通过格式参数格式化double
     *
     * @param d         输入的值
     * @param formatStr 需要格式化的样式例如####.##
     * @return String
     */
    public static String formatDouble(double d, String formatStr) {
        DecimalFormat format = new DecimalFormat(formatStr);
        return format.format(d);
    }

    /**
     * 将年月日转换成long
     *
     * @param year
     * @param month
     * @param date
     * @return String
     */
    public static long Date2Long(int year, int month, int date) {
        Calendar cld = Calendar.getInstance();
        month = month - 1;
        cld.set(year, month, date);
        return cld.getTime().getTime();
    }

    /**
     * 将年月日时分秒转换成long
     *
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return long
     */
    public static long Time2Long(int year, int month, int date, int hour, int minute, int second) {
        Calendar cld = Calendar.getInstance();
        month = month - 1;
        cld.set(year, month, date, hour, minute, second);
        return cld.getTime().getTime();
    }

    /**
     * 从一个long型的时间中获得年
     *
     * @param t
     * @return int
     */
    public static int getYear(long t) {
        Calendar cld = Calendar.getInstance();
        if (t > 0) {
            cld.setTime(new java.util.Date(t));
        }
        return cld.get(Calendar.YEAR);
    }

    /**
     * 从long型时间中获得月
     *
     * @param t
     * @return int
     */
    public static int getMonth(long t) {
        Calendar cld = Calendar.getInstance();
        if (t > 0) {
            cld.setTime(new java.util.Date(t));
        }
        return cld.get(Calendar.MONTH) + 1;
    }

    /**
     * 从long型时间中获得日
     *
     * @param t
     * @return int
     */
    public static int getDay(long t) {
        Calendar cld = Calendar.getInstance();
        if (t > 0) {
            cld.setTime(new java.util.Date(t));
        }
        return cld.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 从long型时间中获得小时
     *
     * @param t
     * @return int
     */
    public static int getHour(long t) {
        Calendar cld = Calendar.getInstance();
        if (t > 0) {
            cld.setTime(new java.util.Date(t));
        }
        return cld.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 从long型时间中获得分
     *
     * @param t
     * @return int
     */
    public static int getMinute(long t) {
        Calendar cld = Calendar.getInstance();
        if (t > 0) {
            cld.setTime(new java.util.Date(t));
        }
        return cld.get(Calendar.MINUTE);
    }

    /**
     * 从long型时间中获得秒
     *
     * @param t
     * @return int
     */
    public static int getSecond(long t) {
        Calendar cld = Calendar.getInstance();
        if (t > 0) {
            cld.setTime(new java.util.Date(t));
        }
        return cld.get(Calendar.SECOND);
    }

    /**
     * 从Date中获得年
     *
     * @param date
     * @return int
     */
    public static int getYear(Date date) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        return cld.get(Calendar.YEAR);
    }

    /**
     * 从Date中获得月
     *
     * @param date
     * @return int
     */
    public static int getMonth(Date date) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        return cld.get(Calendar.MONTH) + 1;
    }

    /**
     * 从Date中获得日
     *
     * @param date
     * @return int
     */
    public static int getDay(Date date) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        return cld.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 从Date中获得小时
     *
     * @param date
     * @return int
     */
    public static int getHour(Date date) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        return cld.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 从Date中获得分钟
     *
     * @param date
     * @return int
     */
    public static int getMinute(Date date) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        return cld.get(Calendar.MINUTE);
    }

    /**
     * 从Date中获得秒
     *
     * @param date
     * @return int
     */
    public static int getSecond(Date date) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        return cld.get(Calendar.SECOND);
    }

    /**
     * 获得当前年份
     *
     * @return int
     */
    public static int getYear() {
        Calendar cld = Calendar.getInstance();
        cld.setTime(new java.util.Date());
        return cld.get(Calendar.YEAR);
    }

    /**
     * 获得当前月份
     *
     * @return int
     */
    public static int getMonth() {
        Calendar cld = Calendar.getInstance();
        cld.setTime(new java.util.Date());
        return cld.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得当前日期
     *
     * @return int
     */
    public static int getDay() {
        Calendar cld = Calendar.getInstance();
        cld.setTime(new java.util.Date());
        return cld.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得系统时间毫秒数
     *
     * @return int
     */
    public static long getLongTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获得当前时间是星期几
     *
     * @param dt
     * @return String
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();

        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];

    }

    /**
     * 检查一个字符串是null还是空的
     *
     * @param param
     * @return boolean
     */
    public static boolean nullOrBlank(Object param) {
        return (param == null || param.toString().length() == 0 || param.toString().trim().equals("") || param.toString().trim().equalsIgnoreCase("null") || param
                .toString().trim().equals("undefined")) ? true : false;
    }

    /**
     * 判断Map这个key是否有这值
     *
     * @param param
     * @param key
     * @return
     */
    public static boolean isMapNullValue(Map<String, Object> param, String key) {
        return nullOrBlank(String.valueOf(param.get(key)));
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param dateStr
     * @param dateStr2
     * @return
     */
    public static int differentDaysByMillisecond(String dateStr, String dateStr2) {
        if (HelpUtils.nullOrBlank(dateStr)) {
            return 100;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date2 = format.parse(dateStr2);
            Date date1 = format.parse(dateStr);

            return (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @param dateStr
     * @param strFormat 字符串日期格式和当前时间对比判断相差多少秒,为空，默认是yyyy-MM-dd HH:mm
     * @return 返回相差的时间，单位秒
     */
    public static long differentDaysBySecond(String dateStr, String strFormat) {

        Date date = null;
        try {
            if (null == strFormat) {
                strFormat = "yyyy-MM-dd HH:mm";
            }
            if (YYYY_MM_DD_HHMM_FORMAT.equals(strFormat)) {
                date = yyyy_MM_dd_HHmm.parse(dateStr);
            } else if (YYYY_MM_DD_HHMMSS_FORMAT.equals(strFormat)) {
                date = yyyy_MM_dd_HHmmss.parse(dateStr);
            }
            long time = System.currentTimeMillis() - date.getTime();
            return time / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将字符串去除空格，如果字符串为空则返回""
     *
     * @param param
     * @return String
     */
    public static String trimStr(String param) {
        return param == null ? "" : param.trim();
    }

    public static String clearCheckBoxVal(String sourceStr) {
        String newStr = sourceStr;

        if (!HelpUtils.nullOrBlank(newStr)) {
            while (newStr.indexOf(",,") >= 0) {
                newStr = newStr.replaceAll(",,", ",");
            }
            if (newStr.endsWith(",")) {
                newStr = newStr.substring(0, newStr.length() - 1);
            }
        } else {
            newStr = "";
        }

        return newStr;
    }

    /**
     * dateToString(Date inDate) 把日期型转换成字符型"yyyy-MM-dd HH:mm:ss"
     *
     * @param inDate Date
     * @return String
     */
    public static String dateToString(Date inDate) {
        String outDateStr = "";
        if (inDate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            outDateStr = formatter.format(inDate);
        }
        return outDateStr;
    }

    /**
     * dateToString(Date inDate) 把日期型转换成字符型"yyyy-MM-dd HH:mm"
     *
     * @param inDate Date
     * @return String
     */
    public static String dateToStryyyyMMddHHmm(Date inDate) {
        String outDateStr = "";
        if (inDate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            outDateStr = formatter.format(inDate);
        }
        return outDateStr;
    }

    /**
     * dateToString(Date inDate) 把日期型转换成指定格式的字符型, 如 "yyyy/MM/dd HH:mm:ss"
     *
     * @param inDate Date
     * @param format String
     * @return String
     */
    public static String dateToString(Date inDate, String format) {
        String outDateStr = "";
        if (inDate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            outDateStr = formatter.format(inDate);
        }
        return outDateStr;
    }

    /**
     * 把日期型转换成字符型"yyyy-MM-dd"
     *
     * @param inDate Date 需要转换的日期时间
     * @return outDateStr String
     */
    public static String dateToSimpleStr(Date inDate) {
        String outDateStr = "";
        if (inDate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            outDateStr = formatter.format(inDate);
        }
        return outDateStr;
    }

    /**
     * 把字符型"yyyy-MM-dd HH:mm:ss"转换成日期型
     *
     * @param s String 需要转换的日期时间字符串
     * @return theDate Date
     */
    public static Date stringToDateWithTime(String s) {
        Date theDate = new Date();
        try {
            if (s != null) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                theDate = dateFormatter.parse(s);
            } else {
                theDate = null;
            }
        } catch (ParseException pe) {
            logger.error(pe.toString());
        }
        return theDate;
    }

    /**
     * 把字符型"yyyy-MM-dd"转换成日期型
     *
     * @param s String 需要转换的日期时间字符串
     * @return theDate Date
     */
    public static Date stringToDate(String s) {
        Date theDate = new Date();
        try {
            if (s != null) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                theDate = dateFormatter.parse(s);
            } else {
                theDate = null;
            }
        } catch (ParseException pe) {
            logger.error(pe.toString());
        }
        return theDate;
    }

    public static Date parse(String date, String format) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.parse(date);
    }

    /**
     * Date +/- int = 新的Date
     *
     * @param inDate     Date 原日期
     * @param AddDateInt int 要加减的天数
     * @return ReturnDate Date 新的Date
     */
    public static Date dateAddInt(Date inDate, int AddDateInt) {
        Calendar currentC = Calendar.getInstance();
        currentC.setTime(inDate);
        currentC.add(Calendar.DAY_OF_YEAR, AddDateInt);
        return currentC.getTime();
    }

    /**
     * Date +/- int = 新的Date
     *
     * @param inDate     Date 原日期,字符串型
     * @param AddDateInt int 要加减的天数
     * @return ReturnDate Date 新的Date
     */
    public static Date dateAddInt(String inDate, int AddDateInt) {
        try {
            Date date = new Date();
            if (inDate != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd");
                date = formatter.parse(inDate);
            }
            Calendar currentC = Calendar.getInstance();
            currentC.setTime(date);
            currentC.add(Calendar.DAY_OF_YEAR, AddDateInt);
            return currentC.getTime();
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获得星期几 <br>
     * 星期天0--星期六6
     *
     * @param inDate
     * @return dayOfWeek int 获得星期几
     */
    public static int dayOfWeek(Date inDate) {
        int dayOfWeek = 0;
        Calendar theCalendar = new GregorianCalendar();
        String DateStr = dateToString(inDate);
        theCalendar.set(Integer.parseInt(DateStr.substring(0, 4)), Integer.parseInt(DateStr.substring(5, 7)) - 1, Integer.parseInt(DateStr.substring(8, 10)));
        dayOfWeek = theCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 7) {
            dayOfWeek = 0;
        }
        return dayOfWeek;
    }

    /**
     * 获得星期几 <br>
     * 星期天0--星期六6 这里输入的是字符串型
     *
     * @param inDate
     * @return dayOfWeek int 获得星期几
     */
    public static int dayOfWeek(String inDate) {
        int dayOfWeek = 0;
        Calendar theCalendar = new GregorianCalendar();
        String DateStr = inDate;
        theCalendar.set(Integer.parseInt(DateStr.substring(0, 4)), Integer.parseInt(DateStr.substring(5, 7)) - 1, Integer.parseInt(DateStr.substring(8, 10)));
        dayOfWeek = theCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 7) {
            dayOfWeek = 0;
        }
        return dayOfWeek;
    }

    /**
     * minusDate 计算两个日期的相隔天数
     *
     * @param beginDate 开始日期
     * @param endDate   开始日期
     * @return result long
     */
    public static long minusDate(Date beginDate, Date endDate) {
        long result = (beginDate.getTime() - endDate.getTime()) / (1000 * 60 * 60 * 24);
        return result;
    }

    /**
     * 计算2个10位时间戳相差多少天
     *
     * @param begin
     * @param end
     * @return
     */
    public static long minusTimeSec(int begin, long end) {
        long r = end - begin;
        return r / 24 / 60 / 60;
    }

    /**
     * 判断2个日期相差天数，不需要精确到时分秒
     *
     * @param begin 20181010
     * @param end   20181014
     * @return
     */
    public static int minusTimeDate(Date begin, Date end) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(begin);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(end);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else {
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else {
            System.out.println("两个日期相差天数 : " + (day2 - day1));
            return day2 - day1;
        }
    }

    /**
     * 计算两个时间中间隔的小时
     *
     * @param beginDate
     * @param endDate
     * @return long
     */
    public static long minusHour(Date beginDate, Date endDate) {
        long result = (beginDate.getTime() - endDate.getTime()) / (1000 * 60 * 60);
        return result;
    }

    /**
     * 计算两个时间中间隔的分钟
     *
     * @param beginDate
     * @param endDate
     * @return long
     */
    public static long minusMinute(Date beginDate, Date endDate) {
        long result = (beginDate.getTime() - endDate.getTime()) / (1000 * 60);
        return result;
    }

    /**
     * 将日期字符串yyyy-mm-dd转换为long型
     *
     * @param dateStr 日期字符串yyyy-mm-dd
     * @return long theDay
     */
    public static long dateStrToLong(String dateStr) {
        long theDate = getLongTime();
        Date thisDate = stringToDate(dateStr);
        if (thisDate != null) {
            theDate = thisDate.getTime();
        }
        return theDate;
    }

    /**
     * 日期字符串yyyy-MM-dd HH:mm:ss转换为long型
     *
     * @param dateStr 日期字符串yyyy-MM-dd HH:mm:ss
     * @return long theDay
     */
    public static long dateStrWithTimeToLong(String dateStr) {
        long theDate = getLongTime();
        Date thisDate = stringToDateWithTime(dateStr);
        if (thisDate != null) {
            theDate = thisDate.getTime();
        }
        return theDate;
    }

    /**
     * long型日期转换成yyyy-MM-dd格式
     *
     * @param theDateLong
     * @return String
     */
    public static String longToDateStr(long theDateLong) {
        String dateStr = "1970-01-01";
        try {
            dateStr = Integer.toString(getYear(theDateLong)) + "-" + getMonth(theDateLong) + "-" + getDay(theDateLong);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
        return dateStr;
    }

    /**
     * long型日期转换成yyyy-MM-dd HH:mm:ss格式
     *
     * @param theDateLong
     * @return String DateStr yyyy-MM-dd HH:mm:ss
     */
    public static String longToDateWithTimeStr(long theDateLong) {
        String dateStr = "1970-01-01 00:00:00";
        try {
            dateStr = Integer.toString(getYear(theDateLong)) + "-" + getMonth(theDateLong) + "-" + getDay(theDateLong) + " " + getHour(theDateLong) + ":"
                    + getMinute(theDateLong) + ":" + getSecond(theDateLong);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
        return dateStr;
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符
     *
     * @param inStr
     * @return String
     */
    public static String replaceBlank(String inStr) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(inStr);
        return m.replaceAll("");
    }

    /**
     * 去除字符串中的回车、换行符、制表符，但保留空格
     *
     * @param inStr
     * @return String
     */
    public static String replaceBlank2(String inStr) {
        Pattern p = Pattern.compile("\\[s\\s]|\t|\r|\n");
        Matcher m = p.matcher(inStr);
        return m.replaceAll("");
    }

    /**
     * 对URL进行UTF-8编码
     *
     * @param inStr
     * @return url
     */
    public static String encodeUrlByUTF8(String inStr) {
        try {
            return java.net.URLEncoder.encode(inStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.toString());
        }
        return inStr;
    }

    /**
     * 将Unicode转换成String
     *
     * @param str
     * @return String
     */
    public static String UnicodeToString(String str) {
        String res = null;
        StringBuffer sb = new StringBuffer();
        try {
            while (str.length() > 0) {
                if (str.startsWith("\\u")) {
                    int x = Integer.parseInt(str.substring(2, 6), 16);
                    sb.append((char) x);
                    str = str.substring(6);
                } else {
                    sb.append(str.charAt(0));
                    str = str.substring(1);
                }
            }
            res = sb.toString();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            logger.error(e.toString());
        }
        return res;
    }

    /**
     * 将String转换为Unicode
     *
     * @param szOrg
     * @return String
     */
    public static String StringToUnicode(String szOrg) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < szOrg.length(); i++) {
            if (szOrg.charAt(i) > 128) {
                sb.append("\\u" + Integer.toHexString(szOrg.charAt(i)));
            } else
                sb.append(szOrg.charAt(i));
        }
        return sb.toString();
    }

    /**
     * 检查URL是否存在
     *
     * @param URLName
     * @return boolean
     */
    public static boolean urlExists(String URLName) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
            // con.setRequestMethod("HEAD");
            con.setRequestMethod("GET");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }

    /**
     * 清除重复的List中的对象
     *
     * @param list
     */
    public static List distinctList(List list) {
        Set<Object> set = new HashSet<Object>();
        for (Object o : list) {
            set.add(o);
        }
        List li = new ArrayList<Object>();
        for (Object o : set) {
            li.add(o);
        }
        list.removeAll(list);
        return li;
    }

    /**
     * 活的客户端可IP
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("CF-Connecting-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 替换回车和空格。
     *
     * @param txt
     * @return
     */
    public static String replaceSpaceAndEnter(String txt) {
        if (HelpUtils.nullOrBlank(txt))
            return "";

        String tmp = "";
        tmp = txt.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
        tmp = tmp.replaceAll(" ", "&nbsp;&nbsp;");
        return tmp;
    }

    public static String getNextDay() {
        try {
            Date date = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DATE, 1);
            date = calendar.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateString = formatter.format(date);

            return dateString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getToDay() {
        try {
            Date date = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            date = calendar.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(date);

            return dateString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void exec(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null)
                System.out.println(line);
            input.close();
        } catch (IOException e) {
            logger.error("exec:" + e.toString());
        }
    }

    /**
     * 自动补零
     *
     * @param pattern 000000
     * @param i       123
     * @return 000123
     */
    public static String getBu0(String pattern, int i) {
        java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
        return df.format(i);
    }

    /**
     * 保留小数，对应水泥、骨料、水等，但小数位必须是5
     *
     * @param obj                配方值
     * @param setUpPercentPerTen 减少十分比
     * @param xsw                小数位
     * @return
     */
    public static double dataDedutDouble(Object obj, double setUpPercentPerTen, int xsw) {
        if (null == obj) {
            return 0;
        }
        if ("0".equals(obj)) {
            return 0;
        }

        double dObj = Double.valueOf(obj + "");

        // 如果不扣减，则直接返回原值
        if (setUpPercentPerTen == 0) {
            return dObj;
        }

        Double afterDedutObj = dObj - dObj * setUpPercentPerTen / 10;

        if (0 == xsw) {
            return Math.floor(afterDedutObj);
        }

        int xswNum = 1;
        for (int i = 0; i < xsw; i++) {
            xswNum *= 10;
        }

        return Math.floor(afterDedutObj * xswNum) / xswNum;
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     */
    public static void deleteFile(String sPath) {
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    /**
     * 防止跨站攻击
     *
     * @param successUrl
     * @return
     */
    public static String xssRecover(String successUrl) {
        if (!nullOrBlank(successUrl)) {
            successUrl = successUrl.replaceAll("<", "")// 匹配尖括号
                    .replaceAll(">", "")// 匹配尖括号
                    .replaceAll("\"", "")// 匹配双引号
                    .replaceAll("\'", "")// 匹配单引号
                    .replaceAll("\\(.*?\\)", "")// 匹配左右括号
                    .replaceAll("[+]", "");// 匹配加号
        }
        return successUrl;
    }

    public static String get4RadomNum() {
        String[] beforeShuffle = new String[]{"2", "3", "4", "5", "6", "7", "8", "9"};
        List list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        String result = afterShuffle.substring(0, 4);

        return result;
    }

    public static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "utf-8"));

            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return json.toString();
    }

    /**
     * 获取指定IP对应的经纬度（为空返回当前机器经纬度）
     *
     * @param request
     * @return
     */
    public static String getIPXYLocation(HttpServletRequest request) {
        String ak = "f9EiMhQBZc6flHT6Fw5OrgFeSLQozFVg";

        String ip = getIpAddr(request);

        if (null == ip) {
            ip = "";
        }

        String url = "http://api.map.baidu.com/location/ip?ak=" + ak + "&coor=bd09ll";
        String json = loadJSON(url);

        return json;
    }

    /**
     * 微信坐标转百度坐标
     *
     * @param ip
     * @return
     */
    public static String getBaiDuXYFromWx(String lng, String lat) {
        String ak = "f9EiMhQBZc6flHT6Fw5OrgFeSLQozFVg";

        String url = "http://api.map.baidu.com/geoconv/v1/?coords=" + lng + "," + lat + "&from=3&to=5&ak=" + ak;
        String json = loadJSON(url);

        return json;
    }

    public static String getBaiDuAddressFromWx(String lng, String lat) {
        String ak = "f9EiMhQBZc6flHT6Fw5OrgFeSLQozFVg";

        String url = "http://api.map.baidu.com/geocoder/v2/?ak=" + ak + "&output=json&pois=0" + "&location=" + lat + "," + lng;
        String json = loadJSON(url);

        return json;
    }

    public static String getBaiDuXYFromCity(String city) {

        String ak = "37492c0ee6f924cb5e934fa08c6b1676";

        String url = "http://api.map.baidu.com/geocoder?key=" + ak + "&output=json&address=" + city + "&city=" + city;
        String json = loadJSON(url);

        return json;
    }

    /**
     * 设置cookie
     *
     * @param response
     * @param name     cookie名字
     * @param value    cookie值
     * @param maxAge   cookie生命周期 以秒为单位
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge > 0)
            cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 根据名字获取cookie
     *
     * @param request
     * @param name    cookie名字
     * @return
     */
    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie;
        } else {
            return null;
        }
    }

    /**
     * 将cookie封装到Map里面
     *
     * @param request
     * @return
     */
    private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

    /**
     * 基于HttpClient 4.3的通用POST方法
     *
     * @param url       提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */

    public static String post(String url, Map<String, String> paramsMap) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }

    /**
     * 调用 API
     *
     * @param parameters
     * @return
     */
    public static InputStream post(String url, String parameters) {
        InputStream body = null;
        logger.error("parameters:" + parameters);
        HttpPost method = new HttpPost(url);
        HttpClient httpClient = new DefaultHttpClient();

        if (method != null & parameters != null && !"".equals(parameters.trim())) {
            try {

                // 建立一个NameValuePair数组，用于存储欲传送的参数
                method.addHeader("Content-type", "application/json; charset=utf-8");
                method.setHeader("Accept", "application/json");
                method.setEntity(new StringEntity(parameters, Charset.forName("UTF-8")));

                HttpResponse response = httpClient.execute(method);

                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode != HttpStatus.SC_OK) {
                    logger.error("Method failed:" + response.getStatusLine());
                }

                // Read the response body
                body = response.getEntity().getContent();

            } catch (IOException e) {
                logger.error(e.toString());
                e.printStackTrace();
            }

        }
        return body;
    }

    /**
     * HttpClient get请求
     *
     * @return
     */
    public static String get(String url) {

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            // 发送get请求
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");

            HttpResponse response = client.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                logger.error("Method failed:" + response.getStatusLine());
            }

            HttpEntity responseEntity = response.getEntity();
            String jsonString = EntityUtils.toString(responseEntity);
            return jsonString;
        } catch (IOException e) {
            logger.error("========> get请求url：" + url + "    get请求失败：" + e.getCause());
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Java将Unix时间戳转换成指定格式日期字符串
     *
     * @param timestampString 时间戳 如："1473048265";
     * @param formats         要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
     * @return 返回结果 如："2016-09-05 16:06:42";
     */
    public static String TimeStamp2Date(String timestampString, String formats) {
        if (null == formats) {
            formats = "yyyy-MM-dd HH:mm:ss";
        }
        Long timestamp = Long.parseLong(timestampString);
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    /**
     * 获取某一天的日期
     *
     * @param day 天数，正数表示未来，负数表示过去
     * @return 2019-03-22 13：23：00
     */
    public static String getSomeDay(int day) {
        return TimeStamp2Date(getAfterSomeDayIntTime(day) + "000", null);
    }

    public static long getAfterSomeDayLongTime(int day) {
        long time = getNowTimeStampMillisecond() + (1000 * 60 * 60 * 24 * day);
        return time;

    }

    public static String TimeStamp2StringDate(long timestamp, String formats) {
        Date date = new Date(timestamp);
        if (null == formats) {
            formats = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formats);
        return simpleDateFormat.format(date);
    }


    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return nowTimeStamp
     */
    public static String getNowTimeStamp() {
        long time = System.currentTimeMillis();
        String nowTimeStamp = String.valueOf(time / 1000);
        return nowTimeStamp;
    }

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return nowTimeStamp
     */
    public static Integer getNowTimeStampInt() {
        long time = System.currentTimeMillis();
        Integer nowTimeStamp = Integer.parseInt(String.valueOf(time / 1000));
        return nowTimeStamp;
    }

    /**
     * 取得当前时间戳（精确到毫秒）
     *
     * @return nowTimeStamp
     */
    public static long getNowTimeStampMillisecond() {
        return System.currentTimeMillis();
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static Integer getPrecision(BigDecimal i) {
        String s = i.toPlainString();
        if (s.indexOf(".") < 0) {
            return 0;
        }
        int position = s.length() - s.indexOf(".") - 1;

        return position;
    }

    // 截取价格
    public static BigDecimal subPricePrecision(BigDecimal price, int precision) {
        String s = price.toPlainString();
        s = s.substring(0, s.indexOf(".") + 1 + precision);
        return BigDecimal.valueOf(Double.valueOf(s));
    }


    public static boolean isEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-zA-Z0-9]+[-|\\-|_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[-|\\-|_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,6}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static String removeZero(String code) {
        return code.replaceAll("^(0+)", "");
    }

    public static boolean startsWithAreaCode(String areaCode, String mobile) {
        if (mobile.startsWith(areaCode)) {
            return false;
        }
        return true;
    }

    public static boolean isMobile(String mobile) {
        boolean flag = false;
        try {
            String check = "^(297|93|244|1264|358|355|376|971|54|374|1684|1268|61|43|994|257|32|229|226|880|359|973|1242|387|590|375|501|1441|591|55|1246|673|975|267|236|1|61|41|56|86|225|237|243|242|682|57|269|238|506|53|5999|61|1345|357|420|49|253|1767|45|1809|1829|1849|213|593|20|291|212|34|372|251|358|679|500|33|298|691|241|44|995|44|233|350|224|590|220|245|240|30|1473|299|502|594|1671|592|852|504|385|509|36|62|44|91|246|353|98|964|354|972|39|1876|44|962|81|76|77|254|996|855|686|1869|82|383|965|856|961|231|218|1758|423|94|266|370|352|371|853|590|212|377|373|261|960|52|692|389|223|356|95|382|976|1670|258|222|1664|596|230|265|60|262|264|687|227|672|234|505|683|31|47|977|674|64|968|92|507|64|51|63|680|675|48|1787|1939|850|351|595|970|689|974|262|40|7|250|966|249|221|65|500|4779|677|232|503|378|252|508|381|211|239|597|421|386|46|268|1721|248|963|1649|235|228|66|992|690|993|670|676|1868|216|90|688|886|255|256|380|598|1|998|3906698|379|1784|58|1284|1340|84|678|681|685|967|27|260|263)(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{4,20}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(mobile);
            flag = matcher.matches();

            // 再单独校验中国的手机号
            if (flag && mobile.startsWith("86")) {
                check = "861[2,3,4,5,6,7,8,9]\\d{9}";
                regex = Pattern.compile(check);
                matcher = regex.matcher(mobile);
                flag = matcher.matches();
                mobile = mobile.substring(mobile.indexOf("86") + 2);
                if (flag) {
//                    logger.warn("手机号截取：{}", mobile);
                    if (mobile.length() != 11) {
                        flag = false;
                    }
                }
            }

        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证码是否超时
     *
     * @param nowTime     当前时间
     * @param smsCodeTime 验证码发送时间
     * @param timeOut     超时时间（单位分钟）
     * @return
     */
    public static boolean isSmsCodeTimeOut(String nowTime, String smsCodeTime, Integer timeOut) {
        // timeOut为零时表示不超时
        if (timeOut == 0) {
            return false;
        }
        if (nullOrBlank(smsCodeTime)) {
            return false;
        }
        if (nullOrBlank(nowTime)) {
            nowTime = HelpUtils.formatDate8(new Date());
        }

        try {
            Date begin = DateUtils.parseDate(smsCodeTime, "yyyy-MM-dd HH:mm:ss");
            Date end = DateUtils.parseDate(nowTime, "yyyy-MM-dd HH:mm:ss");
            Integer between = Integer.parseInt(((end.getTime() - begin.getTime()) / 1000 / 60) + "");//转成分钟
            if (between <= timeOut) {
                return false;
            }
        } catch (ParseException e) {
        }

        return true;
    }

    /**
     * 签名验证
     *
     * @param params
     * @param apiSecret
     * @return
     */
    public static String createSignIncludeValueNull(Map<String, Object> params, String apiSecret) {
        SortedMap<String, Object> sortedMap = new TreeMap<String, Object>(params);

        StringBuffer sb = new StringBuffer();
        Set es = sortedMap.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (!"sign".equals(k)) {
                sb.append(k + "=" + v + "&");
            } else if ("o_no".equals(k) && ("".equals(v) || null == v)) {
                sb.append(k + "=" + v + "&");
            }
        }
        if ("MD5".equals(params.get("sign_type"))) {
            sb.append("apiSecret=" + apiSecret); // MD5签名时，apiSecret放在最后
        } else if ("HmacSHA256".equals(params.get("sign_type"))) {
            sb.deleteCharAt(sb.length() - 1); // 删除最后的&
        } else {
            return null;
        }

        String valueToDigest = sb.toString();
        String actualSign = "";
        if ("MD5".equals(params.get("sign_type"))) {
            actualSign = MacMD5.CalcMD5(valueToDigest, 28);
        } else if ("HmacSHA256".equals(params.get("sign_type"))) {
            byte[] hash = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, apiSecret).hmac(valueToDigest);
            actualSign = Base64.encodeBase64String(hash);
            actualSign = MacMD5.CalcMD5(actualSign, 28); // 因为256算法算出的加密字符串中含/，导致Get请求无效，因此再进行一次MD5
        }

        return actualSign;
    }

    /**
     * 签名验证
     *
     * @param params
     * @param apiSecret
     * @return
     */
    public static String createSign(Map<String, Object> params, String apiSecret) {
        SortedMap<String, Object> sortedMap = new TreeMap<String, Object>(params);

        StringBuffer sb = new StringBuffer();
        Set es = sortedMap.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
//		logger.warn("生成签名前apiSecret值：[{}]",new Gson().toJson(apiSecret));
        if ("MD5".equals(params.get("sign_type"))) {
            sb.append("apiSecret=" + apiSecret); // MD5签名时，apiSecret放在最后
        } else if ("HmacSHA256".equals(params.get("sign_type"))) {
            sb.deleteCharAt(sb.length() - 1); // 删除最后的&
        } else {
            return null;
        }

        String valueToDigest = sb.toString();
//		logger.warn("生成签名前：「{}」",valueToDigest);
        String actualSign = "";
        if ("MD5".equals(params.get("sign_type"))) {
            actualSign = MacMD5.CalcMD5(valueToDigest, 28);
        } else if ("HmacSHA256".equals(params.get("sign_type"))) {
            byte[] hash = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, apiSecret).hmac(valueToDigest);
            actualSign = Base64.encodeBase64String(hash);
            actualSign = MacMD5.CalcMD5(actualSign, 28); // 因为256算法算出的加密字符串中含/，导致Get请求无效，因此再进行一次MD5
        }
//		logger.warn("生成签名后：「{}」",actualSign);
        return actualSign;
    }


    /**
     * API预校验
     *
     * @param reqBaseSecret
     * @return
     */
    public static String preValidateBaseSecret(ReqBaseSecret reqBaseSecret) {
        if (null == reqBaseSecret.getTimestamp() || (reqBaseSecret.getTimestamp() + "").length() != 10) {
            return "ILLEGAL_TIMESTAMP_FORMAT";
        }
        if (HelpUtils.nullOrBlank(reqBaseSecret.getSign_type())) {
            return "ILLEGAL_SIGN_TYPE";
        }
        if (!"MD5".equals(reqBaseSecret.getSign_type()) && !"HmacSHA256".equals(reqBaseSecret.getSign_type())) {
            return "ILLEGAL_SIGN_TYPE";
        }
        if (HelpUtils.nullOrBlank(reqBaseSecret.getApi_key())) {
            return "ILLEGAL_API_KEY";
        }
        if (HelpUtils.nullOrBlank(reqBaseSecret.getSign())) {
            return "ILLEGAL_SIGN";
        }
        if (Math.abs(reqBaseSecret.getTimestamp() - HelpUtils.getNowTimeStampInt()) > 900) {
            logger.warn("项目方校验token信息时间戳：对方时间戳：【{}】,系统当前时间戳：【{}】", reqBaseSecret.getTimestamp(), HelpUtils.getNowTimeStampInt() + "，new Date时间戳：" + new Date().getTime() / 1000);
            return ErrorInfoEnum.ILLEGAL_TIMESTAMP.getErrorENMsg();
        }

        return "";
    }


    /**
     * API预校验
     *
     * @param map
     * @return
     */
    public static String preValidateBaseSecret(Map map) {
        if (null == map.get("timestamp") || (map.get("timestamp") + "").length() != 10) {
            return "ILLEGAL_TIMESTAMP_FORMAT";
        }
        if (HelpUtils.nullOrBlank(map.get("sign_type"))) {
            return "ILLEGAL_SIGN_TYPE";
        }
        if (!"MD5".equals(map.get("sign_type")) && !"HmacSHA256".equals(map.get("sign_type"))) {
            return "ILLEGAL_SIGN_TYPE";
        }
        if (HelpUtils.nullOrBlank(map.get("api_key"))) {
            return "ILLEGAL_API_KEY";
        }
        if (HelpUtils.nullOrBlank(map.get("sign"))) {
            return "ILLEGAL_SIGN";
        }
        if (Math.abs(Integer.valueOf(map.get("timestamp").toString()) - HelpUtils.getNowTimeStampInt()) > 900) {
            logger.warn("项目方校验token信息时间戳：对方时间戳：【{}】,系统当前时间戳：【{}】", map.get("timestamp"), HelpUtils.getNowTimeStampInt() + "，new Date时间戳：" + new Date().getTime() / 1000);
            return ErrorInfoEnum.ILLEGAL_TIMESTAMP.getErrorENMsg();
        }
        return "";
    }

    /**
     * API校验
     *
     * @param map
     * @param apiSecret
     * @return
     */
    public static String validateBaseSecret(Map<String, Object> map, String apiSecret) {
        String sign = createSign(map, apiSecret);
        // 签名校验
        if ((map.get("sign") + "").equals(sign)) {
            return "";
        }
        // 这里要兼容o_no为""的情况
        if ("".equals(map.get("o_no"))) {
            sign = createSignIncludeValueNull(map, apiSecret);
            if ((map.get("sign") + "").equals(sign)) {
                return "";
            }
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        logger.warn("<========================= 请求路径: 【{}】", request.getServletPath());
        logger.warn("<========================= sign map:" + JsonUtil.toJson(map) + " apiSecret:" + apiSecret + "  server sign:" + sign);
        return "ILLEGAL_SIGN";
    }
//	public static String validateBaseSecret(Map<String, Object> map, String apiSecret) {
//		String sign = createSign(map, apiSecret);
//		// 签名校验
//		if (!(map.get("sign") + "").equals(sign)) {
//			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//			logger.warn("<=================================  visit: " + request.getServletPath());
//			logger.warn("sign map:" + JsonUtil.toJson(map) + " apiSecret:" + apiSecret + "  server sign:" + sign);
//			if(!"".equals(map.get("o_no"))) {
//				return "ILLEGAL_SIGN";
//			}
//			// 这里要兼容o_no为""的情况
//			sign = createSignIncludeValueNull(map, apiSecret);
//			if (!(map.get("sign") + "").equals(sign)) {
//				return "ILLEGAL_SIGN";
//			}
//
//		}
//
//		return "";
//	}

    /**
     * 将泛型形参给出的类中设置的属性值转换为Map形式的键值对 t一般是pojo类
     *
     * @param params
     * @param t
     */
    public static <T extends Object> Map<String, Object> objToMap(T t) {
        Map<String, Object> params = new HashMap<String, Object>();

        Class<?> clazz = t.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field[] fields = clazz.getDeclaredFields();

                for (int j = 0; j < fields.length; j++) { // 遍历所有属性
                    String name = fields[j].getName(); // 获取属性的名字
                    Object value = null;

                    Method method = t.getClass()
                            .getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                    value = method.invoke(t);

                    if (value != null)
                        params.put(name, value);
                }
            } catch (Exception e) {
            }
        }

        return params;
    }

    //Requstr的一些处理

    public static String getRequestInfo(HttpServletRequest request) {
        String result = getRequestUri(request) + " param:" + getparameter(request) + "  from " + HelpUtils.getIpAddr(request);
        return result;
    }

    public static String getRequestHead(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("Request head:[");
        Enumeration e1 = request.getHeaderNames();
        while (e1.hasMoreElements()) {
            String headerName = (String) e1.nextElement();
            String headValue = request.getHeader(headerName);
            sb.append(headerName + "=" + headValue + ",");
        }
        String result = sb.toString();
        result = result.substring(0, result.length() - 1);
        result += "]";
        return result;
    }

    public static String getRequestUri(HttpServletRequest request) {
        String uri = (String) request.getAttribute(WebUtils.INCLUDE_REQUEST_URI_ATTRIBUTE);
        if (uri == null) {
            uri = request.getRequestURI();
        }
        return uri;
    }

    /**
     * 获取请求的参数，get和post获取方式不一样
     *
     * @param request
     * @return
     */
    public static String getparameter(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        // map没有，说明要在body中获取
        if (map == null || CollectionUtils.isEmpty(map)) {
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
            try {
                return requestWrapper.getReader().lines().collect(Collectors.joining());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return JsonUtil.toJson(map);
    }


    /**
     * 获取n天后的时间戳
     *
     * @param day 多少天
     * @return
     */
    public static int getAfterSomeDayIntTime(int day) {
        int time = getNowTimeStampInt() + (60 * 60 * 24 * day);
        return time;

    }

    /**
     * 生成单号 pre + yyyyMMddHHmmss + 三位随机数
     *
     * @param
     * @return
     */
    public static String getNumber(String pre) {
        return pre + formatDateOrdreNo(new Date()) + HelpUtils.randomString(3);
    }

    /**
     * 缓存文章路径信息
     *
     * @return
     */
    public static ConcurrentHashMap<Integer, LinkedHashMap<Integer, Object>> getArticleParentInfo() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return (ConcurrentHashMap<Integer, LinkedHashMap<Integer, Object>>) wac.getServletContext().getAttribute(ARTICLE_TITLE_ID);
    }

    public static void main(String[] args) throws Exception {
//		String curr = dateAddMinFormat8(10);
//		System.out.println(curr);
//		long yesterday = HelpUtils.getAfterSomeDayLongTime(-1);
//		String yyyyMMdd = HelpUtils.formatDateOrdreNo(new Date());
//		System.out.println(yyyyMMdd);
//		String s = getSomeDay(30);
//		System.out.println(s);
//		long yesterday = HelpUtils.getAfterSomeDayLongTime(-1);
//		String yyyyMMdd = HelpUtils.TimeStamp2StringDate(yesterday, "yyyy-MM-dd");
//		System.out.println(yyyyMMdd);
//		boolean b = sendSms("654321", "13916155453");
//		System.out.println(b);
//		System.out.println("13667325834".substring(0, 3) + "*****" + "13667325834".substring(8, 11));
//		System.out.println(differentDaysBySecond("2018-12-13 14:38",null) );
//		System.out.println(getAfterSomeDayIntTime(1));
//		System.out.println(getAfterSomeDayIntTime(30));
    }

    /**
     * 某个日期加多少秒
     *
     * @param inDate 某个日期
     * @param second 多少s
     * @return 加上s后的时间
     */
    public static Date dateAddSecondInt(Date inDate, int second) {
        Calendar currentC = Calendar.getInstance();
        currentC.setTime(inDate);
        currentC.add(Calendar.SECOND, second);
        return currentC.getTime();
    }


    /**
     * 获取某个币ZC的汇率，ZC区有就直接返回，否则，去其它区找，再和ZC进行转换
     *
     * @param currency 币种
     * @return 该币种对应ZC的汇率
     */
    public static BigDecimal getSomeCurrencyWithZcRate(String currency) {
        currency = currency.toLowerCase();
        Map<String, TickerResp> ticker = MarketService.getTicker();
        if (ticker == null) {
            return BigDecimal.ZERO;
        }
        Currency currencyInfo = getCurrencyMap().get(currency.toUpperCase());
        int precision = 6;
        if (currencyInfo != null) {
            precision = currencyInfo.getC_precision();
        }
        // 法币自己
        if (currency.equalsIgnoreCase(OFFICIAL_CURRENCY)) {
            return new BigDecimal("1");
        }
        // usdt 区
        TickerResp tickerResp = ticker.get(currency + getOfficialCurrencyToLower() + "_ticker");
        if (tickerResp != null) {
            return tickerResp.getClose();
        }
        // ETH 区
        TickerResp tickerEth = ticker.get(currency  + "eth_ticker");
        if (tickerEth != null) {
            TickerResp tickerEthZc = ticker.get("eth" + getOfficialCurrencyToLower() + "_ticker");
            return tickerEth.getClose().multiply(tickerEthZc.getClose()).setScale(precision, BigDecimal.ROUND_HALF_UP);
        }
        // btc 区
        TickerResp tickerUsdt = ticker.get(currency  + "btc_ticker");
        if (tickerUsdt != null) {
            TickerResp tickerUsdtZc = ticker.get("btc" + getOfficialCurrencyToLower() + "_ticker");
            return tickerUsdt.getClose().multiply(tickerUsdtZc.getClose()).setScale(precision, BigDecimal.ROUND_HALF_UP);
        }

        return BigDecimal.ZERO;
    }

    /**
     * 判断当前用户是否是超级管理员
     *
     * @return
     */
    public static boolean isAdmin() {
        return "admin".equals(getFrmUser().getUser_name());

    }

    /**
     * 判断某个币种是否是锁仓币
     *
     * @param currencyName
     * @return
     */
    public static boolean isLockCurrency(String currencyName) {
        return RuleServiceManage.getCurrencyRuleByCache(currencyName) != null;
    }

    /**
     * 判断字符串是不是包含中文
     *
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]+";

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 验证usdt类型，为了兼容app,除了OMNI，其它都返回null
     *
     * @param currency
     * @param currencyChainType
     * @return OMNI或null
     */
    public static String usdtChainType(String currency, String currencyChainType) {
        if (!"usdt".equalsIgnoreCase(currency)) {
            return null;
        } else {
            Currency currencyInfo = getCurrencyMap().get(currency.toUpperCase());
            if (currencyInfo.getIs_in_eth() == 1 && StringUtil.isNullOrBank(currencyChainType)) {
                return ChainTypeEnum.OMNI.getType();
            } else {
                return ChainTypeEnum.ERC20.getType().equalsIgnoreCase(currencyChainType) ? null : ChainTypeEnum.OMNI.getType();
            }
        }
    }


}
