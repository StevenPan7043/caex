package com.pmzhongguo.ex.framework.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.PaginData;
import com.pmzhongguo.ex.framework.mapper.CommonMapper;


/**
 * 公共Service，获取公共数据，如下拉列表，弹出列表
 */
@Service
public class CommonService extends BaseServiceSupport {
	@Autowired
	private CommonMapper commonMapper;

	private Map<String, Object> params = new HashMap<String, Object>();

	/**
	 * 下拉列表取字典值 例外：对于启用/禁用，使用enable 对于是/否，使用bool
	 * 
	 * @param codes
	 * @return
	 */
	public Map<String, Object> getComboData(String[] codes) {

		Map<String, Object> data = new HashMap<String, Object>();
		for (int i = 0; i < codes.length; i++) {
			String code = codes[i].split("\\.")[0];
			String type = codes[i].split("\\.")[1];
			if ("id".equals(type)) {
				List<Map<String, Object>> list = null;
				if ("enable".equals(code)) {
					list = new ArrayList<Map<String, Object>>();

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", "1");
					map.put("text", "启用");
					list.add(map);

					map = new HashMap<String, Object>();
					map.put("id", "0");
					map.put("text", "禁用");
					list.add(map);

				} else if ("enable1".equals(code)) {// 对工程
					list = new ArrayList<Map<String, Object>>();

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", "0");
					map.put("text", "启用");
					list.add(map);

					map = new HashMap<String, Object>();
					map.put("id", "1");
					map.put("text", "禁用");
					list.add(map);

				} else if ("bool".equals(code)) {
					list = new ArrayList<Map<String, Object>>();

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", "1");
					map.put("text", "是");
					list.add(map);

					map = new HashMap<String, Object>();
					map.put("id", "0");
					map.put("text", "否");
					list.add(map);

				} else if ("sex".equals(code)) {
					list = new ArrayList<Map<String, Object>>();

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", "1");
					map.put("text", "男");
					list.add(map);

					map = new HashMap<String, Object>();
					map.put("id", "0");
					map.put("text", "女");
					list.add(map);

				} else if ("ad_type".equals(code)) {
					list = new ArrayList<Map<String, Object>>();

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", "1");
					map.put("text", "买入");
					list.add(map);

					map = new HashMap<String, Object>();
					map.put("id", "2");
					map.put("text", "卖出");
					list.add(map);

				} else if ("frm_bank_card".equals(code)) {
					list = commonMapper.getFrmBankCard();
				} else if ("currency_pair".equals(code)) {
					list = commonMapper.getCurrencyPair();
				} else if ("currency".equals(code)) {
					list = commonMapper.getCurrency();
				} else if ("otc_owner".equals(code)) {
					list = commonMapper.getOTCOwner();
				} else {
					list = commonMapper.getDictionaryDataById(code);
				}
				data.put(codes[i], list);
			} else {
				List<Map<String, Object>> list = commonMapper
						.getDictionaryDataByName(code);
				data.put(codes[i], list);
			}
		}

		return data;
	}

	public boolean isDicCodeExist(String text) {
		List list = commonMapper.isDicCodeExist(text);
		if (list.size() > 0) {
			return true;
		}
		return false;
	}
	
	public List getColumnsBySiteId(Map newHashMap) {
		return commonMapper.getColumnsBySiteId(newHashMap);
	}

	public List<Map<String,Object>>  getColumnsWithTop() {
		return commonMapper.getColumnsWithTop();
	}
	
	public List getLeafColumnsBySiteId(Map newHashMap) {
		return commonMapper.getLeafColumnsBySiteId(newHashMap);
	}

	@SuppressWarnings("unchecked")
	public PaginData getCommonList(Map<String, Object> params) {

		String text = HelpUtils.trimStr(params.get("text") + "");
		String code = HelpUtils.trimStr(params.get("code") + "");
		String param = HelpUtils.trimStr(params.get("param") + "");

		// 如果点击过查询，就清掉initWhere，适用于配比设置时，初次匹配强度等级，点击查询后，不需要匹配强度等级的情况。
		if ("查询".equals(params.get("btnOK"))) {
			params.remove("initWhere");
		}

		PaginData pd = new PaginData();

		List list = null;

		if ("user".equals(code)) {
			// list = commonMapper.getUserList(text);
			pd.setList(commonMapper.getUserListPage(params));
			pd.setCount(Integer.parseInt(params.get("total") + ""));
		} else if ("multiUser".equals(code)) {

			params.put("needPage", 0);
			list = commonMapper.getUserListPage(params);

			// List listDic = dictionaryMapper.getDictionaryList(null);
			String[] ids = param.split(",");

			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> user = (Map<String, Object>) list.get(i);
				String userid = String.valueOf(user.get("id"));

				for (int j = 0; j < ids.length; j++) {
					if (ids[j].equals(userid)) {
						user.put("checked", true);
						list.set(i, user);
					}
				}

			}
			pd.setList(list);
			pd.setCount(list.size());
		} else if ("m_member".equals(code)) {
			pd.setList(commonMapper.getMemberListPage(params));
			pd.setCount(Integer.parseInt(params.get("total") + ""));
		}

		return pd;
	}
}
