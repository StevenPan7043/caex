package com.pmzhongguo.ex.core.tag;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.pmzhongguo.ex.framework.service.DictionaryService;


public class DicCheckBoxTag extends TagSupport {

	private String controlName = null;
	private String controlId = null;
	private String dicId = null;
	private String valueField = null; // 取值的字段，默认是ID
	private String initValue = null; // 初始选中的值

	public String getInitValue() {
		return initValue;
	}

	public void setInitValue(String initValue) {
		this.initValue = initValue;
	}

	public String getValueField() {
		return valueField;
	}

	public void setValueField(String valueField) {
		this.valueField = valueField;
	}

	public String getControlName() {
		return controlName;
	}

	public void setControlName(String controlName) {
		this.controlName = controlName;
	}

	public String getControlId() {
		return controlId;
	}

	public void setControlId(String controlId) {
		this.controlId = controlId;
	}

	public String getDicId() {
		return dicId;
	}

	public void setDicId(String dicId) {
		this.dicId = dicId;
	}

	public int doEndTag() throws JspException {
		JspWriter out = pageContext.getOut();

		WebApplicationContext wac = ContextLoader
				.getCurrentWebApplicationContext();
		DictionaryService dictionaryService = (DictionaryService) wac
				.getBean("dictionaryService");

		try {
			List<Map> list = dictionaryService.getDictionaryDataList(dicId);

			for (int i = 0; i < list.size(); i++) {
				String value = "";
				if (null == valueField || valueField.equals("")) {
					valueField = "dic_data_code";
				}
				value = list.get(i).get(valueField) + "";

				String isCheck = "";
				if (null != initValue
						&& ("," + initValue + ",").indexOf("," + value + ",") >= 0) {
					isCheck = "checked='checked'";
				}
				out.println("<input type='checkbox' class='checkbox_"
						+ controlId
						+ "_class' id='"
						+ controlId
						+ "_"
						+ i
						+ "' name='"
						+ controlName
						+ "' "
						+ isCheck
						+ " value='"
						+ value
						+ "' style='vertical-align:middle; width:auto;' /><label for='"
						+ controlId
						+ "_"
						+ i
						+ "' style='margin-left:2px;vertical-align:middle;cursor:hand;cursor:pointer; color: blue;'>"
						+ list.get(i).get("dic_data_name") + "</label>");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
}
