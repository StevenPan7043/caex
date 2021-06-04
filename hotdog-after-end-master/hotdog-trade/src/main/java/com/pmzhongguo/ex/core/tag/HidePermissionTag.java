package com.pmzhongguo.ex.core.tag;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class HidePermissionTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 198112266334L;
	// 按钮的权限ID
	private String functionId;

	@Override
	public int doStartTag() throws JspException {
		// 是否有权限
		boolean hasPermission = false;

		List<Map<String, Object>> ls = (List<Map<String, Object>>) ((HttpServletRequest) pageContext
				.getRequest()).getSession().getAttribute("userMune");

		if (null == ls) {
			ls = (List<Map<String, Object>>) ((HttpServletRequest) pageContext
					.getRequest()).getSession().getAttribute("factoryMune");
		}

		for (int i = 0; null != ls && i < ls.size(); i++) {
			Map<String, Object> one = ls.get(i);

			if (functionId.equals(one.get("id"))) {
				hasPermission = true;
			}
		}

		// 真：返回EVAL_BODY_INCLUDE（执行标签）；假：返回SKIP_BODY（跳过标签不执行）
		if (hasPermission) {
			return SKIP_BODY;
		}

		return EVAL_BODY_INCLUDE;

	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
}