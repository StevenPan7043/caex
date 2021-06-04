package com.contract.service.cms;

import javax.servlet.http.HttpServletRequest;

import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contract.common.pwd.PwdEncode;
import com.contract.dao.MtCliqueUserMapper;
import com.contract.entity.MtCliqueUser;

@Service
public class BaseService {

	@Autowired
	private MtCliqueUserMapper mtCliqueUserMapper;
	/**
	 * 修改密码
	 * @param request
	 * @param password
	 * @param newPassword
	 * @param newPasswordConfirm
	 * @return
	 */
	public RestResponse editPassword(HttpServletRequest request, String password, String newPassword, String newPasswordConfirm) {
		Integer userid=PlatSession.getUserId(request);
		if(StringUtils.isBlank(password)) {
			return GetRest.getFail("请输入原始密码");
		}
		if(StringUtils.isBlank(newPassword)) {
			return GetRest.getFail("请输入新密码");
		}
		if(!newPassword.equals(newPasswordConfirm)) {
			return GetRest.getFail("两次密码不一致");
		}
		//如果是集团修改密码
		MtCliqueUser cliqueUser=mtCliqueUserMapper.selectByPrimaryKey(userid);
		if(cliqueUser==null) {
			return GetRest.getFail("信息错误");
		}
		String pwd=PwdEncode.encodePwd(password);
		if(!pwd.equals(cliqueUser.getPassword())) {
			return GetRest.getFail("原始密码不正确");
		}
		String newpwd=PwdEncode.encodePwd(newPassword);
		cliqueUser.setPassword(newpwd);
		mtCliqueUserMapper.updateByPrimaryKeySelective(cliqueUser);
		
		return GetRest.getFail("修改成功");
	}
}
