package com.pmzhongguo.ex.core.utils;

import java.util.Map;

public class PagingUtil {
	public static Map<String, Object> paging(Map<String, Object> params,
			int needPagin) {
		if (null == params) {
			return null;
		}

		/*
		 * if(!HelpUtils.nullOrBlank(params.get("sortname")+"") &&
		 * !HelpUtils.nullOrBlank(params.get("sortorder")+"")) {
		 * params.put("sort", true); params.put("_sortname",
		 * params.get("sortname")); params.put("_sortorder",
		 * params.get("sortorder")); }
		 */
		params.put("pagin", needPagin);

		if (needPagin == 1) {
			int page = Integer.parseInt(params.get("page") + "");
			int pagesize = Integer.parseInt(params.get("pagesize") + "");
			params.put("_start", (page - 1) * pagesize);
			params.put("_limit", pagesize);
		}

		return params;
	}
}
