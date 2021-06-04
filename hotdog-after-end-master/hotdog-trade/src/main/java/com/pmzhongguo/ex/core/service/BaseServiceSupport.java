package com.pmzhongguo.ex.core.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pmzhongguo.ex.core.utils.DaoUtil;


public class BaseServiceSupport {

	@Autowired
	public DaoUtil daoUtil;

	public Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 根据frm_gen_code表中的配置生成对应的编号。
	 * 
	 * @param id
	 * @param preFix
	 *            前缀，可以为空(注意不要传null，传"")，例如结算时，前缀是财务周期名称，就要传此参数
	 * @return
	 */
	protected synchronized String genCode(int id, String preFix) {
		Map data = daoUtil
				.queryForMap(
						"SELECT CONCAT(prefix, IF(need_date = 1, DATE_FORMAT(NOW(),'%y%m%d'), ''), '-') prefix, t.suffix, t.for_table, t.for_column FROM frm_gen_code t where t.id = ?",
						id);
		String prefix = preFix + (String) data.get("prefix");
		String suffix = (String) data.get("suffix");
		String forTable = (String) data.get("for_table");
		String forColumn = (String) data.get("for_column");

		String sql = "select max(" + forColumn + ") from " + forTable
				+ " where " + forColumn + " like '" + prefix + "%'";
		String strCode = daoUtil.queryForObject(sql, String.class);
		if (strCode == null) {
			return prefix + suffix;
		} else {
			String[] temp = strCode.split("-");
			String num = (Integer.parseInt(temp[1]) + 1) + "";
			String finalNum = StringUtils.leftPad(num, temp[1].length(), '0');
			return prefix + finalNum;
		}
	}

}
