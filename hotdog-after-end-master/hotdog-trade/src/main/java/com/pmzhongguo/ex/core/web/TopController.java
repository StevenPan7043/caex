package com.pmzhongguo.ex.core.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.pmzhongguo.ex.business.dto.AuthDto;
import com.pmzhongguo.ex.business.dto.OrderCreateDto;
import com.pmzhongguo.ex.business.dto.WithdrawCreateDto;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.business.model.OssConstants;
import com.pmzhongguo.ex.business.service.ExService;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.*;
import com.pmzhongguo.ex.core.web.req.BaseSecretReq;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.udun.constant.API;
import com.pmzhongguo.udun.constant.CoinType;
import com.pmzhongguo.udun.util.HttpUtil;
import com.pmzhongguo.udun.util.UdunUtil;
import com.pmzhongguo.zzextool.consts.JedisChannelConst;
import com.pmzhongguo.zzextool.consts.StaticConst;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.zzextool.utils.DateUtils;
import com.pmzhongguo.zzextool.utils.StringUtil;
import com.qiniu.util.BeanUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class TopController {

	protected Logger logger = LoggerFactory.getLogger(TopController.class);
	
	@Resource
	protected MemberService memberService;
	@Autowired
	protected ExService exService;

	@Autowired
	public DaoUtil daoUtil;


	/** ??????@ExceptionHandler???????????? */
	@ExceptionHandler(BusinessException.class)
	@ResponseBody
	public Resp exp(HttpServletRequest request, HttpServletResponse response, BusinessException e) {
		logger.error("BusinessException Error: ", e);
		String msg = handleMsg(e.getMsg());
		return new Resp(Resp.FAIL, msg);
	}

    private String handleMsg(String msg) {
	    if (StringUtil.isNullOrBank(msg)) {
	        return ErrorInfoEnum.LANG_SYSTEM_BUSY.getErrorENMsg();
        }
        ErrorInfoEnum errorInfoEnum = ErrorInfoEnum.valueOf(msg);
	    if (errorInfoEnum == null) {
            return ErrorInfoEnum.LANG_SYSTEM_BUSY.getErrorENMsg();
        }
	    return errorInfoEnum.getErrorENMsg();

    }

    /** ??????@ExceptionHandler???????????? */
	@ExceptionHandler
	@ResponseBody
	public Resp exp(HttpServletRequest request, HttpServletResponse response,
			Exception e) {
		Integer state = -1;
		String respMsg = e.toString();
		String respTip = null;
		if (e instanceof DataAccessException) {
			Throwable root = ((DataAccessException) e).getRootCause();
			String str = root != null ? root.getMessage()
					: ((DataAccessException) e).getMessage();
			String reg1 = "Data truncation: Data too long for column '(.*)' at row \\d*";
			String reg2 = "Data truncation: Out of range value for column '(.*)' at row \\d*";
			String reg3 = "Duplicate entry '(.*)' for key (.*)";
			String reg4 = "????????????????????????????????????";
			String[] msg = str.split("'");
			if (str.matches(reg1) || str.matches(reg4)) {
				str = msg[1] + " is too long";
				respTip = ErrorInfoEnum.LANG_DATA_TOO_LONG.getErrorENMsg();
			} else if (str.matches(reg2)) {
				str = msg[1] + " is too big";
				respTip = ErrorInfoEnum.LANG_DATA_TOO_BIG.getErrorENMsg();
			} else if (str.matches(reg3)) {
				str = "???????????????";
				respTip = ErrorInfoEnum.LANG_DATA_ERROR.getErrorENMsg();
			}
			respMsg = str;
		} else if (e instanceof BusinessException) {
			BusinessException be = (BusinessException) e;
			respMsg = be.getMsg();
			state = be.getStatus();
		} else if (e instanceof NoSuchMethodException) {
			respMsg = "????????????????????????";
			respTip = ErrorInfoEnum.LANG_REQ_METHOD_NOT_EXIST.getErrorENMsg();
		} else if (e instanceof BindException) {
			respMsg = "??????????????????";
			respTip = ErrorInfoEnum.LANG_CAST_TYPE_EXCEPTION.getErrorENMsg();
		} else if (e instanceof HttpMediaTypeNotSupportedException) {
			respMsg = "???????????????????????????";
			respTip = ErrorInfoEnum.LANG_REQ_PARAM_ERROR.getErrorENMsg();
		} else if (e instanceof MethodArgumentTypeMismatchException) {
			respMsg = "?????????????????????";
			respTip = ErrorInfoEnum.LANG_REQ_PARAM_ERROR.getErrorENMsg();
		} else {
			respMsg = e.toString();
			respTip = ErrorInfoEnum.LANG_SYSTEM_BUSY.getErrorENMsg();
		}
		logger.error("Exception: ",e);
        logger.warn("?????????????????????" + respMsg + " \n\r " + HelpUtils.getRequestInfo(request) + " " + HelpUtils.getRequestHead(request));
		return new ObjResp(state, respTip,respMsg);
	}

	/**
	 * ??????????????????
	 */
	public static String $(String name) {
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest().getParameter(name);
	}

	public static String $(String name, String def) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		String v = request.getParameter(name);
		return HelpUtils.nullOrBlank(v) ? def : v;
	}

	/**
	 * ??????request???????????????
	 */
	public static Map<String, Object> $params(HttpServletRequest request) {
		return $params(false, request);
	}

	public static Map<String, Object> $params(boolean putUserInfo,
			HttpServletRequest request) {
		Map<String, Object> params = WebUtils.getParametersStartingWith(
				request, "");
		return params;
	}

	public static Long $long(String name) {
		String v = $(name);
		return HelpUtils.nullOrBlank(v) ? null : Long.parseLong(v);
	}

	public static Double $double(String name) {
		String v = $(name);
		return HelpUtils.nullOrBlank(v) ? null : Double.parseDouble(v);
	}

	public static Float $float(String name) {
		String v = $(name);
		return HelpUtils.nullOrBlank(v) ? null : Float.parseFloat(v);
	}

	public static Float $float(String name, Float def) {
		String v = $(name);
		return HelpUtils.nullOrBlank(v) ? def : Float.parseFloat(v);
	}

	public static Integer $int(String name) {
		String v = $(name);
		return HelpUtils.nullOrBlank(v) ? null : Integer.parseInt(v);
	}

	public static BigInteger $bigInt(String name) {
		String v = $(name);
		return HelpUtils.nullOrBlank(v) ? null : new BigInteger(v);
	}

	public static Integer $int(String name, Integer def) {
		String v = $(name);
		if (HelpUtils.nullOrBlank(v)) {
			return def;
		} else {
			return Integer.parseInt(v);
		}

	}

	public static Boolean $bool(String name) {
		String v = $(name, "false");
		return HelpUtils.nullOrBlank(v) ? null : Boolean.parseBoolean(v);
	}

	public static Boolean $bool(String name, boolean def) {
		String v = $(name);
		return HelpUtils.nullOrBlank(v) ? def : Boolean.parseBoolean(v);
	}

	public static Object $attr(String attrKey) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		return request.getAttribute(attrKey);
	}

	public static void $attr(String attrKey, Object attrValue) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		request.setAttribute(attrKey, attrValue);
	}

	public static void $attrs(Object... args) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		for (int i = 1; i < args.length; i += 2) {
			request.setAttribute(String.valueOf(args[i - 1]), args[i]);
		}
	}

	// ----------------------?????????????????????-------------------------------------------------------------
	public void attrCommonDateTime() {

		// ??????
		$attr("curDate", HelpUtils.formatDate(new Date()));
		$attr("curDateAndCurTime", HelpUtils.formatDate6(new Date()));

		// ???????????????
		$attr("curMonthFirstDate", HelpUtils.getMonthFirstDay(0));

		// ???????????????
		$attr("nextMonthFirstDate", HelpUtils.getMonthFirstDay(1));

		// ??????
		$attr("yesterdayDate", HelpUtils.dateAddDay(-1));
		$attr("yesterdayDateAndCurTime", HelpUtils.dateAddHour(-24));

		// ??????
		$attr("tomorrowDate", HelpUtils.dateAddDay(1));
		$attr("tomorrowDateAndCurTime", HelpUtils.dateAddHour(24));

		// 5??????
		$attr("fiveDaysBeforeDate", HelpUtils.dateAddDay(-5));
		$attr("fiveDaysBeforeDateAndCurTime", HelpUtils.dateAddHour(-120));

		// 5??????
		$attr("fiveDaysAfterDate", HelpUtils.dateAddDay(5));
		$attr("fiveDaysAfterDateAndCurTime", HelpUtils.dateAddHour(120));

	}

	// ------------------------------------------------------------------------------------------------

	protected void writeMessage(HttpServletResponse response, String message) {
		response.setContentType("text/html;charset=utf-8");
		response.setDateHeader("Expires", -10);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(message);
			out.flush();
		} catch (IOException e) {
			logger.error(e.toString());
		} finally {
			out.close();
		}
	}
	
	
	protected AuthDto valideRecharge(BaseSecretReq baseSecretReq, Map<String, Object> params) {
		// ????????????
		AuthDto authDto = new AuthDto();
		authDto.setSuccess(false);

		if (null == baseSecretReq.getTimestamp()
				|| !(baseSecretReq.getTimestamp() instanceof Integer)
				|| (baseSecretReq.getTimestamp() + "").length() != 10) {
			authDto.setMsg("Illegal timestamp format");
			return authDto;
		}

		if (Math.abs(baseSecretReq.getTimestamp()
				- HelpUtils.getNowTimeStampInt()) > 30) {
			authDto.setMsg("Timestamp is too early or too late");
			return authDto;
		}

		if (null == baseSecretReq.getSign()
				|| (baseSecretReq.getSign() + "").length() != 28) {
			authDto.setMsg("Illegal sign len");
			return authDto;
		}
		
		// ????????????
		if (!baseSecretReq.getSign().equals(createSign(params, HelpUtils.getMgrConfig().getEx_secreat()))) {
			authDto.setMsg("Illegal sign");
			return authDto;
		}
		
		authDto.setSuccess(true);
		authDto.setMsg("success");
		
		return authDto;
	}

	/**
	 * ????????????
	 * 
	 * @param params
	 * @param apiSecret
	 * @return
	 */
	private static String createSign(Map<String, Object> params, String apiSecret) {
		SortedMap<String, Object> sortedMap = new TreeMap<String, Object>(
				params);

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
		if ("MD5".equals(params.get("sign_type"))) {
			sb.append("apiSecret=" + apiSecret); //apiSecret????????????
		} else {
			sb.deleteCharAt(sb.length() - 1); // ???????????????&
		}
		
		String payload = sb.toString();
		String actualSign = "";
		if ("MD5".equals(params.get("sign_type"))) {
			actualSign = MacMD5.CalcMD5(payload, 28);
		} else {
			Mac hmacSha256 = null;
			try {
				hmacSha256 = Mac.getInstance("HmacSHA256");
				SecretKeySpec secKey = new SecretKeySpec(
						apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
				hmacSha256.init(secKey);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("No such algorithm: " + e.getMessage());
			} catch (InvalidKeyException e) {
				throw new RuntimeException("Invalid key: " + e.getMessage());
			}
			byte[] hash = hmacSha256.doFinal(payload
					.getBytes(StandardCharsets.UTF_8));
			actualSign = Base64.encodeBase64String(hash);
			actualSign = MacMD5.CalcMD5(actualSign, 28);
		}
		
		//System.out.println("actualSign:" + actualSign);
		//System.out.println("payload:" + payload);
		return actualSign;
	}


	protected Map<String, String> getAliPolicy() {
		String host = "https://" + OssConstants.BUCKET_TEMP_NAME + "." + OssConstants.ALIYUN_OSS_ENDPOINT;
		OSSClient client = new OSSClient(OssConstants.ALIYUN_OSS_ENDPOINT, OssConstants.ALIYUN_ACCESS_KEY, OssConstants.ALIYUN_ACCESS_SECRET);
		Map<String, String> respMap = new LinkedHashMap<String, String>();
		try {
			long expireTime = 300;
			long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
			Date expiration = new Date(expireEndTime);
			PolicyConditions policyConds = new PolicyConditions();
			policyConds.addConditionItem(
					PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
			policyConds.addConditionItem(MatchMode.StartWith,
					PolicyConditions.COND_KEY, OssConstants.ALIYUN_IDCARD_DIR);

			String postPolicy = client.generatePostPolicy(expiration,
					policyConds);
			byte[] binaryData = postPolicy.getBytes("utf-8");
			String encodedPolicy = BinaryUtil.toBase64String(binaryData);
			String postSignature = client.calculatePostSignature(postPolicy);

			respMap.put("accessid", OssConstants.ALIYUN_ACCESS_KEY);
			respMap.put("policy", encodedPolicy);
			respMap.put("signature", postSignature);
			respMap.put("dir", OssConstants.ALIYUN_IDCARD_DIR);
			respMap.put("host", host);
			respMap.put("expire", String.valueOf(expireEndTime / 1000));
		} catch (Exception e) {
			respMap = null;
		}

		return respMap;
	}

	/**
	 * ?????????????????????????????????????????????????????????Map?????????????????? t?????????pojo???
	 * 
	 * @param t
	 */
	public static <T extends Object> Map<String, Object> objToMap(T t) {
		Map<String, Object> params = new HashMap<String, Object>();

		Class<?> clazz = t.getClass();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				Field[] fields = clazz.getDeclaredFields();

				for (int j = 0; j < fields.length; j++) { // ??????????????????
					String name = fields[j].getName(); // ?????????????????????
					Object value = null;

					Method method = t.getClass().getMethod(
							"get" + name.substring(0, 1).toUpperCase()
									+ name.substring(1));
					value = method.invoke(t);

					if (value != null)
						params.put(name, value);
				}
			} catch (Exception e) {
			}
		}

		return params;
	}
	
	
	/** 
	 * ????????????????????????????????????????????? 
	 * 
	 * @param request 
	 * @return 
	 */  
	public static void validateRequest(HttpServletRequest request) {
		String referer = "";
		boolean referer_sign = true;// true ??????????????????????????? //false ???????????????????????????
		Enumeration headerValues = request.getHeaders("referer");
		while (headerValues.hasMoreElements()) {
			referer = (String) headerValues.nextElement();
		}

		// ??????????????????????????????
		if (StringUtils.isBlank(referer)) {
			referer_sign = false;
		} else {
			// ?????????????????????getRequestURI????????????
			String servername_str = request.getServerName();
			if (StringUtils.isNotBlank(servername_str)) {
				int index = 0;
				if (StringUtils.indexOf(referer, "https://") == 0) {
					index = 8;
				} else if (StringUtils.indexOf(referer, "http://") == 0) {
					index = 7;
				}
				if (referer.length() - index < servername_str.length()) {// ????????????
					referer_sign = false;
				} else { // ?????????????????????????????????????????????
					String referer_str = referer.substring(index, index
							+ servername_str.length());
					if (!servername_str.equalsIgnoreCase(referer_str)) {
						referer_sign = false;
					}
				}
			} else {
				referer_sign = false;
			}
		}
		if (!referer_sign) {
			throw new BusinessException(-1, "referer error");
		}
	}
	
	
	protected AuthDto valideWithdraw(Map<String, Object> params) {
		// ????????????
		AuthDto authDto = new AuthDto();
		authDto.setSuccess(false);

		if (null == params.get("timestamp")
				|| !(params.get("timestamp") instanceof Integer)
				|| (params.get("timestamp") + "").length() != 10) {
			authDto.setMsg("Illegal timestamp format");
			return authDto;
		}

		if (Math.abs(Integer.parseInt((params.get("timestamp") + ""))
				- HelpUtils.getNowTimeStampInt()) > 30) {
			authDto.setMsg("Timestamp is too early or too late");
			return authDto;
		}

		if (null == params.get("sign")
				|| (params.get("sign") + "").length() != 28) {
			authDto.setMsg("Illegal sign len");
			return authDto;
		}
		
		// ????????????
		if (!params.get("sign").equals(createSign(params, HelpUtils.getMgrConfig().getEx_secreat()))) {
			authDto.setMsg("Illegal sign");
			return authDto;
		}
		
		authDto.setSuccess(true);
		authDto.setMsg("success");
		
		return authDto;
	}
	
	
	protected boolean validateSecurityPwd(Integer memberId, String securityPwd) {
		boolean result = false;
		Member m = memberService.getMemberById(memberId);
		String m_pwd = MacMD5.CalcMD5Member(securityPwd);
		if(m_pwd.equals(m.getM_security_pwd())) {
			result = true;
		}
		return result;
	}
	
	protected void validateApiToken(Map paramMap, ApiToken apiToken, String ip, String privilege) {
		
		if (null == apiToken) {
			throw new BusinessException(Resp.FAIL, ErrorInfoEnum.API_NOT_EXISTS.getErrorENMsg());
		}
		
		ip = ip.replaceAll(" ", "");
		String[] ipArr = ip.split(",");
		String realIp=ipArr[0];
		
		if (!HelpUtils.nullOrBlank(apiToken.getTrusted_ip())) {
			if ((apiToken.getTrusted_ip()).indexOf(realIp) < 0) {
				throw new BusinessException(-1, ErrorInfoEnum.LANG_ILLEGAL_IP.getErrorENMsg());
			}
		}
		String validateRet = HelpUtils.validateBaseSecret(paramMap, apiToken.getApi_secret());
		if (!"".equals(validateRet)) {
			throw new BusinessException(-1, ErrorInfoEnum.LANG_ILLEGAL_SIGN.getErrorENMsg());
		}
		
		if(HelpUtils.nullOrBlank(apiToken.getApi_privilege()) || apiToken.getApi_privilege().indexOf(privilege)<0) {
			logger.warn(apiToken.getApi_key() + " " + privilege + " " + " API_NO_PRIVILEGE");
			throw new BusinessException(Resp.FAIL, ErrorInfoEnum.API_NO_PRIVILEGE.getErrorENMsg());
		}
	}

	/**
	 * ?????????
	 * @param paramMap
	 * @param apiToken
	 * @param ip
	 * @param privilege
	 * @return
	 */
	protected RespObj validateProApiToken(Map paramMap, ApiToken apiToken, String ip, String privilege) {

		if (null == apiToken) {
			return new RespObj(Resp.FAIL, ErrorInfoEnum.API_NOT_EXISTS.getErrorCNMsg(), null);
		}

		ip = ip.replaceAll(" ", "");
		String[] ipArr = ip.split(",");
		String realIp = ipArr[0];

		if (!HelpUtils.nullOrBlank(apiToken.getTrusted_ip())) {
			if ((apiToken.getTrusted_ip()).indexOf(realIp) < 0) {
				return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_IP.getErrorCNMsg(), null);
			}
		}
		String validateRet = HelpUtils.validateBaseSecret(paramMap, apiToken.getApi_secret());
		if (!"".equals(validateRet)) {
			return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SIGN.getErrorCNMsg(), null);
		}

		if (HelpUtils.nullOrBlank(apiToken.getApi_privilege()) || apiToken.getApi_privilege().indexOf(privilege) < 0) {
			logger.warn(apiToken.getApi_key() + " " + privilege + " " + " API_NO_PRIVILEGE");
			return new RespObj(Resp.FAIL, ErrorInfoEnum.API_NO_PRIVILEGE.getErrorCNMsg(), null);
		}
		return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
	}
	
	/**
	 * ?????????????????????
	 * @param orderCreateDto
	 * @return
	 */
	protected AuthDto validateCreateOrder(OrderCreateDto orderCreateDto,Integer memberId) {
		
		// ????????????
		AuthDto authDto = new AuthDto();
		authDto.setSuccess(false);

		// ????????????
		if (HelpUtils.getMgrConfig().getIs_stop_ex() == 1) {
			authDto.setMsg(ErrorInfoEnum.LANG_STOP_EX.getErrorENMsg());
			return authDto;
		}
		
		// ??????????????????
		if (HelpUtils.nullOrBlank(orderCreateDto.getO_no())) {
			authDto.setMsg(ErrorInfoEnum.LANG_ILLEGAL_ORDER_ID.getErrorENMsg());
			return authDto;
		}

		// ??????????????????
		if (null == orderCreateDto.getVolume()
				|| orderCreateDto.getVolume().compareTo(BigDecimal.ZERO) <= 0) {
			authDto.setMsg(ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg());
			return authDto;
		}

		// ???????????????????????????0????????????????????????
		if (null == orderCreateDto.getPrice()
				|| orderCreateDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
			authDto.setMsg(ErrorInfoEnum.LANG_ILLEGAL_PRICE.getErrorENMsg());
			return authDto;
		}

		// ???????????????????????????????????????0????????????
		if ("limit".equals(orderCreateDto.getO_price_type())
				&& orderCreateDto.getPrice().compareTo(BigDecimal.ZERO) == 0) {
			authDto.setMsg(ErrorInfoEnum.LANG_ILLEGAL_PRICE.getErrorENMsg());
			return authDto;
		}

		// ????????????????????????
		if (!"buy".equals(orderCreateDto.getO_type())
				&& !"sell".equals(orderCreateDto.getO_type())) {
			authDto.setMsg(ErrorInfoEnum.LANG_ILLEGAL_O_TYPE.getErrorENMsg());
			return authDto;
		}
		
		// ????????????????????????
		if (!"limit".equals(orderCreateDto.getO_price_type())
				&& !"market".equals(orderCreateDto.getO_price_type())) {
			authDto.setMsg(ErrorInfoEnum.LANG_ILLEGAL_O_PRICE_TYPE.getErrorENMsg());
			return authDto;
		}
		//???????????????????????????(?????????????????????)
		CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(orderCreateDto.getSymbol());
		if (currencyPair.getIs_ups_downs_limit().equals(1)) {
			//?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			BigDecimal price = orderCreateDto.getPrice();
			if (currencyPair.getClosePrice() != null) {
				//?????????????????????????????????????????????
				if (orderCreateDto.getO_type().equals("buy")) {
					//???
					if (price.compareTo(currencyPair.getHighPrice()) == 1) {
						authDto.setMsg("LANG_INVALID_PRICE_UPS_DOWN_HIGH");
						return authDto;
					}
				} else {
					//???
					if (price.compareTo(currencyPair.getLowPrice()) == -1) {
						authDto.setMsg("LANG_INVALID_PRICE_UPS_DOWN_LOW");
						return authDto;
					}
				}
			}
		}

		if ("market".equals(orderCreateDto.getO_price_type()) 
				&& orderCreateDto.getPrice().compareTo(BigDecimal.ZERO) != 0) {
			authDto.setMsg(ErrorInfoEnum.LANG_ILLEGAL_PRICE.getErrorENMsg());
			return authDto;
		}

		// ????????????????????????
		if (null == orderCreateDto.getSource()
				|| (orderCreateDto.getSource() + "").length() < 3) {
			authDto.setMsg(ErrorInfoEnum.LANG_ILLEGAL_SOURCE.getErrorENMsg());
			return authDto;
		}

		// ????????????????????????
		CurrencyPair cp = HelpUtils.getCurrencyPairMap().get(
				orderCreateDto.getSymbol());
		if (null == cp) {
			authDto.setMsg(ErrorInfoEnum.LANG_ILLEGAL_TRADE_PAIR.getErrorENMsg());
			return authDto;
		}
		if (orderCreateDto.getSource().equals("web")) {
			if (cp.getMax_price().subtract(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) != 0
					&& (cp.getMax_price().subtract(orderCreateDto.getPrice()).compareTo(BigDecimal.ZERO) < 0)
					&& ("buy".equals(orderCreateDto.getO_type()))) {
				authDto.setMsg(ErrorInfoEnum.LANG_BIGGER_THAN_MAX_PRICE.getErrorENMsg());
				return authDto;
			}
			if (cp.getMin_price().subtract(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) != 0
					&& (cp.getMin_price().subtract(orderCreateDto.getPrice()).compareTo(BigDecimal.ZERO) > 0)
					&& ("sell".equals(orderCreateDto.getO_type()))) {
				authDto.setMsg(ErrorInfoEnum.LANG_BIGGER_THAN_MIN_PRICE.getErrorENMsg());
				return authDto;
			}
		}

		// ????????????????????????????????????????????????????????????
		if ("buy".equals(orderCreateDto.getO_type()) && "limit".equals(orderCreateDto.getO_price_type()) 
				&& orderCreateDto.getVolume().compareTo(cp.getMin_buy_volume()) == -1) {
			authDto.setMsg(ErrorInfoEnum.LANG_LITTLE_THAN_MIN_BUYVOLUME_TIP.getErrorENMsg());
			return authDto;
		}
		if ("buy".equals(orderCreateDto.getO_type()) && "market".equals(orderCreateDto.getO_price_type()) 
				&& orderCreateDto.getVolume().compareTo(cp.getMin_buy_amount()) == -1) {
			authDto.setMsg(ErrorInfoEnum.LANG_LITTLE_THAN_MIN_BUYAMOUNT_TIP.getErrorENMsg());
			return authDto;
		}
		if ("sell".equals(orderCreateDto.getO_type()) && orderCreateDto.getVolume().compareTo(cp.getMin_sell_volume()) == -1) {
			authDto.setMsg(ErrorInfoEnum.LANG_LITTLE_THAN_MIN_SELLVOLUME_TIP.getErrorENMsg());
			return authDto;
		}
		
		// ??????????????????
		if ("buy".equals(orderCreateDto.getO_type()) && cp.getCan_buy() == 0) {
			authDto.setMsg(ErrorInfoEnum.LANG_STOP_EX.getErrorENMsg());
			return authDto;
		}
		
		// ??????????????????
		if ("sell".equals(orderCreateDto.getO_type()) && cp.getCan_sell() == 0) {
			authDto.setMsg(ErrorInfoEnum.LANG_STOP_EX.getErrorENMsg());
			return authDto;
		}

		//??????????????????????????????
		if (cp.getIs_flash_sale_open() == 1 && "buy".equals(orderCreateDto.getO_type())){
			//?????????????????????????????????
			if (cp.getFlash_sale_close_time() == null || cp.getIs_flash_sale_open() == null){
				authDto.setMsg(ErrorInfoEnum.FLASH_SALE_NOT_START.getErrorENMsg());
				return authDto;
			}
			//??????????????????????????????
			long opentime = DateUtils.differentDaysBySecond(cp.getFlash_sale_open_time(), null);
			long closetime = DateUtils.differentDaysBySecond(cp.getFlash_sale_close_time(), null);
			if (!(opentime > StaticConst.ADVANCE_CURRENCY_PAIR_OPEN_TIME && closetime < StaticConst.ADVANCE_CURRENCY_PAIR_OPEN_TIME)){
				authDto.setMsg(ErrorInfoEnum.FLASH_SALE_NOT_START.getErrorENMsg());
				return authDto;
			}

			if (cp.getFixed_buy_price().compareTo(orderCreateDto.getPrice()) != 0){
				authDto.setMsg(ErrorInfoEnum.LANG_ILLEGAL_PRICE.getErrorENMsg());
				return authDto;
			}
			//?????????????????????????????????
			if (cp.getMax_buy_volume().compareTo(BigDecimal.ZERO) != 0 && cp.getMax_buy_volume().compareTo(BigDecimal.ZERO) != 0){
				String table = "t_order_"+orderCreateDto.getSymbol().toLowerCase();
				String sql = "SELECT volume from " + table + " WHERE 1 = 1 and o_type= 'buy' and member_id = ? and create_time > ? and create_time < ? and o_status in ('watting','partial-done','done');";
				String sql1 = "SELECT done_volume as volume from " + table + " WHERE 1 = 1 and o_type= 'buy' and member_id = ? and create_time > ? and create_time < ? and o_status = 'partial-canceled';";
				List<Map> list = daoUtil.queryForList(sql,memberId,cp.getFlash_sale_open_time(),cp.getFlash_sale_close_time());
				list.addAll(daoUtil.queryForList(sql1,memberId,cp.getFlash_sale_open_time(),cp.getFlash_sale_close_time()));
				BigDecimal totalVolume = orderCreateDto.getVolume();
				for (Map m:list) {
					totalVolume = totalVolume.add((BigDecimal) m.get("volume")) ;
				}
				if (cp.getMax_buy_volume().compareTo(totalVolume) == -1) {
					authDto.setMsg(ErrorInfoEnum.LANG_BIGGER_THAN_MAX_BUYVOLUME_TIP.getErrorENMsg());
					return authDto;
				}
			}
		}




		// ???????????????????????????????????????????????????????????????
		if ("limit".equals(orderCreateDto.getO_price_type()) &&
				HelpUtils.getPrecision(orderCreateDto.getPrice()) > cp.getPrice_precision()) {
			// ?????????????????????
			BigDecimal subPricePrecision = HelpUtils.subPricePrecision(orderCreateDto.getPrice(), cp.getPrice_precision());
			if(subPricePrecision.compareTo(BigDecimal.ZERO) <= 0) {
				authDto.setMsg(ErrorInfoEnum.LANG_ILLEGAL_PRICE.getErrorENMsg());
				return authDto;
			}
			orderCreateDto.setPrice(subPricePrecision);
//			authDto.setMsg("LANG_ILLEGAL_PRICE");
//			return authDto;
		}

		// ???/???????????????????????????????????????????????????????????????
		Integer volumePrecision = HelpUtils.getPrecision(orderCreateDto.getVolume());
		if ("limit".equals(orderCreateDto.getO_price_type()) 
				|| ("market".equals(orderCreateDto.getO_price_type()) && "sell".equals(orderCreateDto.getO_type()))) {
			if (volumePrecision > cp.getVolume_precision()) {
				// ?????????????????????
				BigDecimal subVolumePrecision = HelpUtils.subPricePrecision(orderCreateDto.getVolume(), cp.getVolume_precision());
				orderCreateDto.setVolume(subVolumePrecision);
//				authDto.setMsg("LANG_ILLEGAL_VOLUME");
//				return authDto;
			}
		} else {
			if (volumePrecision > (cp.getVolume_precision() + cp.getPrice_precision())) {
				// ?????????????????????
				BigDecimal subVolumePrecision = HelpUtils.subPricePrecision(orderCreateDto.getVolume(), cp.getVolume_precision() + cp.getPrice_precision());
				orderCreateDto.setVolume(subVolumePrecision);
//				authDto.setMsg("LANG_ILLEGAL_VOLUME");
//				return authDto;
			}
		}
		

		authDto.setSuccess(true);
		authDto.setMsg("success");
		authDto.setTokenId(0);		
		authDto.setCurrencyPair(cp);

		return authDto;
	}
	
	
	/**
	 * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
	 * 
	 * @param withdrawDto
	 * @param currencyId
	 * @param withDrawAddr
	 * @param ipAddr
	 * @param memberId
	 * @return
	 */

	protected Long addWithdraw(OTCAds ads, WithdrawCreateDto withdrawDto, Integer currencyId, String withDrawAddr,
			String withDrawAddrLabel, String ipAddr, Integer memberId) {
		CoinWithdraw coinWithdraw = new CoinWithdraw();
		coinWithdraw.setCurrency(withdrawDto.getCurrency());
		coinWithdraw.setCurrency_id(currencyId);
		coinWithdraw.setMember_coin_addr(withDrawAddr);
		// ???????????????????????????????????????????????????????????????
		// coinWithdraw.setMember_coin_addr_id(Integer.parseInt(withDrawAddr.get("id") +
		// ""));
		// coinWithdraw.setMember_coin_addr_label(withDrawAddr.get("addr_label") + "");
		coinWithdraw.setMember_coin_addr_label(withDrawAddrLabel);
		if (HelpUtils.nullOrBlank(withDrawAddrLabel)) {
			coinWithdraw.setMember_coin_addr_label("-");
		}

		coinWithdraw.setMember_coin_addr_id(0);

		coinWithdraw.setMember_id(memberId);
		coinWithdraw.setOper_ip(ipAddr);
		coinWithdraw.setW_amount(withdrawDto.getAmount());
		coinWithdraw.setW_create_time(HelpUtils.formatDate8(new Date()));

		// ???????????????????????????????????????
		if (ads == null || ads.getId() <= 0) { // OTC???????????????
			Currency currency = HelpUtils.getCurrencyMap().get(coinWithdraw.getCurrency());
			if (currency.getWithdraw_fee_percent() == 1) { // ???????????????????????????
				BigDecimal fee = withdrawDto.getAmount().multiply(currency.getWithdraw_fee())
						.divide(BigDecimal.valueOf(100));
				if (fee.compareTo(currency.getWithdraw_fee_min()) < 0) { // ???????????????????????????????????????????????????
					fee = currency.getWithdraw_fee_min();
				}
				if (currency.getWithdraw_fee_max().compareTo(BigDecimal.ZERO) > 0
						&& fee.compareTo(currency.getWithdraw_fee_max()) > 0) { // ?????????????????????????????????????????????
					fee = currency.getWithdraw_fee_max();
				}
				fee = fee.setScale(currency.getC_precision(), BigDecimal.ROUND_DOWN);

				coinWithdraw.setW_fee(fee);
			} else { // ?????????????????????
				coinWithdraw.setW_fee(currency.getWithdraw_fee());
			}
		} else {
			coinWithdraw.setW_fee(BigDecimal.ZERO);
		}

		if (coinWithdraw.getW_fee().compareTo(coinWithdraw.getW_amount()) >= 0) {
			throw new BusinessException(-1, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg());
		}

		coinWithdraw.setW_status(0);

		// OTC ??????
		if (ads == null || ads.getId() <= 0) {
			coinWithdraw.setOtc_ads_id(0);
			coinWithdraw.setOtc_oppsite_currency("");
			coinWithdraw.setOtc_owner_name("");
			coinWithdraw.setOtc_price(BigDecimal.ZERO);
			coinWithdraw.setOtc_volume(BigDecimal.ZERO);

			Long id = allMemberFrozenAndProcSyn(withdrawDto.getAmount(), withdrawDto.getCurrency(), memberId,
					"coinWithdraw", null, coinWithdraw);

			CoinType type = CoinType.getCoin(coinWithdraw.getCurrency());
			if(type == null){
				throw new BusinessException(Resp.FAIL, "????????????", coinWithdraw.getCurrency());
			}
			Map<String, Object> param = new HashMap<>();
			param.put("merchantId", API.MERCHANT_ID);
			//??????????????????????????????
			param.put("coinType", type.getCoinType());
			param.put("mainCoinType", type.getMainCoinType());
			param.put("address", coinWithdraw.getMember_coin_addr());
			param.put("amount", coinWithdraw.getW_amount().subtract(coinWithdraw.getW_fee()));
			param.put("businessId", "" + id);
			String result = HttpUtil.getInstance().post(API.getUrl(API.WITHDRAW), UdunUtil.requestMap(param));
			if (org.apache.commons.lang3.StringUtils.isBlank(result)) {
				logger.warn("????????????,???????????????{}", JSON.toJSONString(coinWithdraw));
				throw new BusinessException(Resp.FAIL, "????????????", result);
			}
			JSONObject jsonObject = JSON.parseObject(result);
			String code = jsonObject.getString("code");
			if (!"200".equals(code)) {
				throw new BusinessException(Resp.FAIL, "????????????", result);
			}
			return id;
		} else { // OTC??????
			coinWithdraw.setOtc_ads_id(ads.getId());
			coinWithdraw.setOtc_oppsite_currency(ads.getBase_currency());
			coinWithdraw.setOtc_owner_name(ads.getO_name());
			coinWithdraw.setOtc_price(ads.getPrice());
			coinWithdraw.setOtc_volume(coinWithdraw.getW_amount().divide(ads.getPrice()));

			// ?????????????????????????????????????????????????????????
			Long id = allMemberFrozenAndProcSyn(coinWithdraw.getOtc_volume(),
					coinWithdraw.getOtc_oppsite_currency(), memberId, "coinWithdraw", null, coinWithdraw);
            return id;
		}

	}
	
	
	protected synchronized Long allMemberFrozenAndProcSyn(BigDecimal frozen, String frozenCurrency, Integer memberId,
			String type, Order order, CoinWithdraw coinWithdraw) {
		return exService.allMemberFrozenAndProcOutter(frozen, frozenCurrency, memberId, type, order, coinWithdraw);
	}
	
	
	
	public static void main(String[] args) {
		String a = "????????????????????????";
		try {
			System.out.println(new String(a.getBytes("ISO8859_1"), "UTF-8")
					.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ??????????????????????????????
	 * @param rule
	 * @return
	 */
	protected Resp ruleEnable(Rule rule){
		String warehouse_recharge_url = PropertiesUtil.getPropValByKey("rule_enable_url");
		String post = HttpUtils.postWithJSON(warehouse_recharge_url, JsonUtil.toJson(rule));

		if (BeanUtil.isEmpty(post)) {
			logger.warn(LockReleaseEnum.getEnumByType(rule.getEnable()).getCodeCn() + "??????????????????????????????{}", JsonUtil.toJson(rule));
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_REQ_TIME_OUT.getErrorENMsg(), null);
		}
		ObjResp objResp = JsonUtil.fromJson(post, ObjResp.class);
		if (objResp.getState().equals(Resp.FAIL)) {
			logger.warn(LockReleaseEnum.getEnumByType(rule.getEnable()).getCodeCn() + "????????????????????????{}", post);
			return objResp;
		}
		JedisUtil.getInstance().publish(JedisChannelConst.JEDIS_CHANNEL_SYNC_CURRENCY_RULE, "sync");
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}
}
