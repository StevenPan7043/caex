package com.contract.service.cms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contract.common.FunctionUtils;
import com.contract.common.StaticUtils;
import com.contract.common.pwd.PwdEncode;
import com.contract.dao.MtCliqueMenusMapper;
import com.contract.dao.MtCliqueRolesMapper;
import com.contract.dao.MtCliqueUserMapper;
import com.contract.dao.UsdtRechargeLogMapper;
import com.contract.dto.UserDto;
import com.contract.entity.MtCliqueMenus;
import com.contract.entity.MtCliqueMenusExample;
import com.contract.entity.MtCliqueRoles;
import com.contract.entity.MtCliqueUser;
import com.contract.exception.ThrowJsonException;
import com.contract.service.UtilsService;
import com.contract.service.redis.RedisUtilsService;

@Service
public class LoginService {
	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
	@Autowired
	private MtCliqueUserMapper mtCliqueUserMapper;
	@Autowired
	private MtCliqueMenusMapper cliqueMenusMapper;
	@Autowired
	private MtCliqueRolesMapper cliqueRolesMapper;
	@Autowired
	private UtilsService utilsService;
	@Autowired
	private UsdtRechargeLogMapper usdtRechargeLogMapper;
	/**
	 * 登陆
	 * @param login
	 * @param password
	 * @param validcode
	 * @return
	 */
	public RestResponse enter(HttpServletRequest request, String login, String password, String validcode, String verCode) {
	    logger.info("cms登录日志：login：{},password:{},validcode:{},verCode:{}",login,password,validcode,verCode);
		if(StringUtils.isEmpty(login)) {
			return GetRest.getFail("请输入账号");
		}
		if(StringUtils.isEmpty(password)) {
			return GetRest.getFail("请输入密码");
		}
		if (StringUtils.isBlank(verCode)) {
			return GetRest.getFail("请输入短信验证码");
		}
		if(StringUtils.isEmpty(validcode)) {
			return GetRest.getFail("请填写验证码");
		}
		String validCode = PlatSession.getValidCode(request);
		if (validCode != null) {
			logger.warn("cms合约后台登录验证码:{},request:{}", validcode, validCode);
			if (!validcode.toLowerCase().equals(PlatSession.getValidCode(request).toLowerCase())) {
				return GetRest.getFail("验证码错误");
			}
		}
		MtCliqueUser  user=mtCliqueUserMapper.getByLogin(login);
		if(user==null) {
			return GetRest.getFail("账号或密码错误");
		}
		String pwd=PwdEncode.encodePwd(password);
		if(!pwd.equals(user.getPassword())) {
			return GetRest.getFail("账号或密码错误");
		}
		if(FunctionUtils.isEquals(StaticUtils.status_no, user.getStatus())) {
			return GetRest.getFail("账户已失效");
		}
		// 校验短信验证码
		RestResponse valid=utilsService.validPhone(user.getPhone(), verCode);
		if(!valid.isStatus()) {
			throw new ThrowJsonException(valid.getDesc());
		}
		Integer roleid=user.getRoleid();
		Integer userid=user.getId();
		UserDto userDto=new UserDto();
		userDto.setUserid(userid);
		userDto.setManageid(userid);
		userDto.setName(user.getName());
		userDto.setAccount(user.getLogin());
		MtCliqueRoles cliqueRoles=cliqueRolesMapper.getValidByid(roleid);
		if(cliqueRoles!=null) {
			userDto.setRolename(cliqueRoles.getName());
			List<Integer> list=FunctionUtils.getIntegerList(cliqueRoles.getMenuids().split(","));
			if(list==null ||list.size()<1) {
				return GetRest.getFail("当前角色没有菜单权限");
			}
			MtCliqueMenusExample example=new MtCliqueMenusExample();
			example.createCriteria().andIdIn(list).andStatusEqualTo(StaticUtils.status_yes.shortValue());
			example.setOrderByClause("sort asc");
			List<MtCliqueMenus> menus=cliqueMenusMapper.selectByExample(example);
			PlatSession.setUserId(request, userid);//用户id
			PlatSession.setMtUser(request, userDto);//登陆对象
			PlatSession.setRoleId(request, roleid);
			PlatSession.setMenuList(request, menus);
			List<String> menuurls=new ArrayList<>();
			for(MtCliqueMenus m: menus) {
				menuurls.add(m.getUrl());
			}
			PlatSession.setMenuUrlList(request, menuurls);
			return GetRest.getSuccess("登陆成功");
		}else {
			return GetRest.getFail("登陆角色不存在或被禁用");
		}
	}

	
	public BigDecimal getUsdtRechargeLog(String nodeid) {
		return usdtRechargeLogMapper.getNodemoney(nodeid);
	}


	public BigDecimal getWaitUsdt() {
		return usdtRechargeLogMapper.getWaitUsdt();
	}


	public BigDecimal getRecharge() {
		return usdtRechargeLogMapper.getRecharge();
	}
	
	/**
	 * 平台登录发送短信验证码
	 * @param login
	 * @return
	 */
	public RestResponse sendLoginCode(String login) {
		if(StringUtils.isEmpty(login)) {
			return GetRest.getFail("请输入账号");
		}
		MtCliqueUser  user=mtCliqueUserMapper.getByLogin(login);
		if(user==null) {
			return GetRest.getFail("账号错误");
		}
		if(FunctionUtils.isEquals(StaticUtils.status_no, user.getStatus())) {
			return GetRest.getFail("账户已失效");
		}
		if (StringUtils.isBlank(user.getPhone())) {
			return GetRest.getFail("请先完善联系方式");
		}
		return utilsService.sendPhonecode(user.getPhone());
	}
}
