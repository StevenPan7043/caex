package com.pmzhongguo.ex.business.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;



import com.pmzhongguo.ex.business.entity.ApiToken;

import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.pmzhongguo.ex.business.entity.ApiAccessLimit;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.resp.Resp;



@Service
public class ApiAccessLimitService {
	Logger logger = LoggerFactory.getLogger(ApiAccessLimitService.class);
	
	public Map<String, ApiAccessLimit> apiAccessLimitRules = null;

	@Autowired
	MemberService memberService;
	
	public void loadApiAccessLimitCache() {
		
		Map<String, ApiAccessLimit> tmpRules = new HashMap();
		
		String rules = HelpUtils.getMgrConfig().getApi_access_limit_rule();
		
		if(HelpUtils.nullOrBlank(rules)) {
			return;
		}
		try {
			String[] rule = rules.split("\\|");
			for(int i = 0; i<rule.length; i++) {
				String ruleDetail[] = rule[i].split(",");
				String interfaceKey = ruleDetail[0];
				Long timeOut = Long.valueOf(ruleDetail[1]);
				Integer limitCount = Integer.valueOf(ruleDetail[2]);
	
				ApiAccessLimit apiAccessLimit = new ApiAccessLimit();
				apiAccessLimit.setInterface_key(interfaceKey);
				apiAccessLimit.setLimit_count(limitCount);
				apiAccessLimit.setCache(CacheBuilder.newBuilder().expireAfterAccess(timeOut, TimeUnit.SECONDS).build(createCacheLoader()));
				
				tmpRules.put(interfaceKey, apiAccessLimit);
			}
		} catch (Exception e) {
			logger.error("初始化api访问限制规则错误：", e);
		}
		
		apiAccessLimitRules = tmpRules;
		
	}
	
	public void check(String interfaceKey, String apiKey) {
		if(null == apiAccessLimitRules) {
			return;
		}

		ApiAccessLimit apiAccessLimit = apiAccessLimitRules.get(interfaceKey);
		if(getVal(interfaceKey, apiKey)>=apiAccessLimit.getLimit_count()) {
			ApiToken apiToken = memberService.getApiToken(apiKey);
			if(1 == apiToken.getApi_limit()) {
				apiAccessLimitRules.get(interfaceKey).getCache().put(apiKey, 1);
			}else {
				logger.warn(ErrorInfoEnum.API_ACCESS_COUNT_MAX.getErrorENMsg() + "  apiKey:" + apiKey);
				throw new BusinessException(Resp.FAIL, ErrorInfoEnum.API_ACCESS_COUNT_MAX.getErrorENMsg());
			}
		}
		setVal(interfaceKey, apiKey);
		
	}
	
	/**
	 * 设置访问次数
	 * @param interfaceKey
	 * @param apiKey
	 */
	private void setVal(String interfaceKey, String apiKey) {
		Integer currentCount = getVal(interfaceKey, apiKey);
		apiAccessLimitRules.get(interfaceKey).getCache().put(apiKey, currentCount+1);
	}
	
	/**
	 * 获取已访问次数
	 * @param interfaceKey
	 * @param apiKey
	 * @return
	 */
	private Integer getVal(String interfaceKey, String apiKey) {
		return apiAccessLimitRules.get(interfaceKey).getCache().getUnchecked(apiKey);
	}
	
	/**
	 * 初始化CacheLoader
	 * @return
	 */
	private CacheLoader<String, Integer> createCacheLoader() {
		return new CacheLoader<String, Integer>() {
			@Override
			public Integer load(String key) throws Exception {
				return 0;
			}
		};
	}

	
	
	public static void main(String[] args) throws Exception {

		LoadingCache<String, Integer> cache = CacheBuilder.newBuilder().expireAfterAccess(2, TimeUnit.SECONDS).build(
			new CacheLoader<String, Integer>() {
				@Override
				public Integer load(String key) throws Exception {
					return 0;
				}
			});

		String key = "apikey1";
		System.out.println(cache.get(key));
		cache.put(key, 1);
		cache.invalidateAll();//cleanUp();
		Thread.sleep(1000);

		System.out.println(cache.get(key));

	}
}
