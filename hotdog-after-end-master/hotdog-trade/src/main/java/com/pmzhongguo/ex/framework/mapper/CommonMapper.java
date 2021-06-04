package com.pmzhongguo.ex.framework.mapper;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.mapper.SuperMapper;


public interface CommonMapper<T> extends SuperMapper<T> {
	public List<Map<String, Object>> getDictionaryDataById(String dicId);

	public List<Map<String, Object>> getDictionaryDataByName(String dicId);

	public List getUserListPage(Map<String, Object> params);
	
	public List getMemberListPage(Map<String, Object> params);

	public List isDicCodeExist(String text);

	public List<Map<String, Object>> getFrmBankCard();

	public List<Map<String, Object>> getCurrencyPair();
	
	public List<Map<String, Object>> getCurrency();
	
	public List getColumnsBySiteId(Map newHashMap);
	public List getColumnsWithTop();

	public List getLeafColumnsBySiteId(Map newHashMap);

	public List<Map<String, Object>> getOTCOwner();
}
