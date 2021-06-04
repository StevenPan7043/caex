package com.pmzhongguo.ex.core.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmzhongguo.zzextool.exception.BusinessException;


public class JsonUtil {

	private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * 如果JSON字符串为Null或"null"字符串,返回Null. 如果JSON字符串为"[]",返回空集合.
	 * 
	 * 如需读取集合如List/Map,且不是List<String>这种简单类型时使用如下语句: List<MyBean> beanList =
	 * binder.getMapper().readValue(listString, new
	 * TypeReference<List<MyBean>>() {});
	 */
	public static <T> T fromJson(String jsonString, Class<T> clazz) {
		if (HelpUtils.nullOrBlank(jsonString)) {
			return null;
		}

		try {
			mapper.configure(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);

			return mapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	/**
	 * 如果对象为Null,返回"null". 如果集合为空集合,返回"[]".
	 */
	public static String toJson(Object object) {

		try {
			return mapper.writeValueAsString(object);
		} catch (IOException e) {
			logger.warn("write to json string error:" + object, e);
			return null;
		}
	}

	/**
	 * 设置转换日期类型的format pattern,如果不设置默认打印Timestamp毫秒数.
	 */
	public void setDateFormat(String pattern) {
		if (!HelpUtils.nullOrBlank(pattern)) {
			DateFormat df = new SimpleDateFormat(pattern);
			mapper.getSerializationConfig().setDateFormat(df);
			mapper.getDeserializationConfig().setDateFormat(df);
		}
	}

	/**
	 * 取出Mapper做进一步的设置或使用其他序列化API.
	 */
	public ObjectMapper getMapper() {
		return mapper;
	}

	public static Map jsonToMap(String jsonStr) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = mapper.readValue(jsonStr,
					new TypeReference<HashMap<String, String>>() {
					});
		} catch (Exception e) {

			logger.error("json 格式转换错误" + jsonStr);
			throw new BusinessException(-1, ErrorInfoEnum.JSON_FORMAT_FAIL.getErrorENMsg());

		}

		return map;
	}

	public static String objectToJackson(Object obj) throws Exception {
		return mapper.writeValueAsString(obj);
	}

	public static List jacksonToCollection(String jsonStr) {
		List result = null;
		try {
			result = mapper.readValue(jsonStr,
					new TypeReference<List<Map<String, Object>>>() {
					});
		} catch (Exception e) {
			logger.error("json 格式转换错误" + jsonStr);
			e.printStackTrace();
			throw new BusinessException(-1, ErrorInfoEnum.JSON_FORMAT_FAIL.getErrorENMsg());
		}

		return result;
	}

	public static JavaType getCollectionType(Class<?> collectionClass,
			Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass,
				elementClasses);
	}

}