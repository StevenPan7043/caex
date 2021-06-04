package com.contract.service.cms;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.contract.common.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contract.common.pwd.PwdEncode;
import com.contract.dao.MtCliqueMenusMapper;
import com.contract.dao.MtCliqueRolesMapper;
import com.contract.dao.MtCliqueUserMapper;
import com.contract.entity.MtCliqueMenus;
import com.contract.entity.MtCliqueMenusExample;
import com.contract.entity.MtCliqueRoles;
import com.contract.entity.MtCliqueRolesExample;
import com.contract.entity.MtCliqueUser;
import com.contract.entity.MtCliqueUserExample;
import com.contract.exception.ThrowPageException;

@Service
public class RoleService {

	@Autowired
	private MtCliqueRolesMapper cliqueRolesMapper;
	@Autowired
	private MtCliqueUserMapper mtCliqueUserMapper;
	@Autowired
	private MtCliqueMenusMapper mtCliqueMenusMapper;
	
	
	/**
	 * 查询角色
	 * @return
	 */
	public List<MtCliqueRoles> queryRoles(HttpServletRequest request){
		MtCliqueRolesExample example=new MtCliqueRolesExample();
		List<MtCliqueRoles> list=cliqueRolesMapper.selectByExample(example);
		for(MtCliqueRoles l:list) {
			Map<String, Object> map=new HashMap<>();
			map.put("roleid", l.getId());
			List<String> userlist=mtCliqueUserMapper.queryUsername(l.getId());
			l.setUserList(userlist);
		}
		return list;
	}
	
	public List<MtCliqueRoles> querySysroles(HttpServletRequest request){
		MtCliqueRolesExample example=new MtCliqueRolesExample();
		List<MtCliqueRoles> list=cliqueRolesMapper.selectByExample(example);
		return list;
	}
	
	/**
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	public RestResponse updateRolestatus(Integer id, Integer status) {
		if(id==1) {
			return GetRest.getFail("系统管理员角色不可操作");
		}
		MtCliqueRoles roles=cliqueRolesMapper.selectByPrimaryKey(id);
		if(roles==null) {
			return GetRest.getFail("未找到角色信息");
		}
		roles.setStatus(status.shortValue());
		cliqueRolesMapper.updateByPrimaryKeySelective(roles);
		return GetRest.getSuccess("操作成功");
	}
	
	/**
	 * 查询角色菜单
	 * @param id
	 * @return
	 */
	public List<MtCliqueMenus> queryMenus(HttpServletRequest request,Integer id){
		String menuids="";
		if(id!=null) {
			MtCliqueRoles roles=cliqueRolesMapper.selectByPrimaryKey(id);
			if(roles==null) {
				throw new ThrowPageException("未找到角色信息");
			}
			menuids=","+roles.getMenuids()+",";
		}
		//查询当前这个角色具备的功能权限
		
		MtCliqueMenusExample example=new MtCliqueMenusExample();
		MtCliqueMenusExample.Criteria criteria= example.createCriteria();
		criteria.andStatusEqualTo(StaticUtils.status_yes.shortValue());
		example.setOrderByClause("id asc,type asc");
		List<MtCliqueMenus> list=mtCliqueMenusMapper.selectByExample(example);
		for(MtCliqueMenus l:list) {
			//如果有角色id
			if(!StringUtils.isBlank(menuids)) {
				String m=","+l.getId()+",";
				if(menuids.indexOf(m)>-1) {
					l.setChecked("checked");
				}
			}
		}
		return list;
	}
	
	public MtCliqueRoles getRoleById(Integer id) {
		MtCliqueRoles roles=cliqueRolesMapper.selectByPrimaryKey(id);
		return roles;
	}
	
	/**
	 * 编辑角色
	 * @param roles
	 * @return
	 */
	public RestResponse editRole(MtCliqueRoles roles,HttpServletRequest request) {
		if(StringUtils.isBlank(roles.getName())) {
			return GetRest.getFail("请填写角色名称");
		}
		if(StringUtils.isBlank(roles.getMenuids())) {
			return GetRest.getFail("请选择菜单");
		}
		//去重复
		Set<String> menulist=new HashSet<>(Arrays.asList(roles.getMenuids().split(",")));
		String menuss=StringUtils.join(menulist.toArray(),",");
		roles.setMenuids(menuss);
		//如果不是平台  保存目标id
		if(roles.getId()!=null) {
			cliqueRolesMapper.updateByPrimaryKeySelective(roles);
		}else {
			cliqueRolesMapper.insertSelective(roles);
		}
		return GetRest.getSuccess("操作成功");
	}
	
	/**
	 * 查询操作员
	 * @return
	 */
	public List<MtCliqueUser> queryUserList(HttpServletRequest request){
		Integer roleid=PlatSession.getRoleId(request);
		MtCliqueUserExample example=new MtCliqueUserExample();
		if(!FunctionUtils.isEquals(roleid, 1)) {
			//如果不是管理员
			example.createCriteria().andIdEqualTo(PlatSession.getUserId(request));
		}
		example.setOrderByClause("id desc");
		List<MtCliqueUser> list=mtCliqueUserMapper.selectByExample(example);
		for(MtCliqueUser l:list) {
			MtCliqueRoles cliqueRoles=cliqueRolesMapper.selectByPrimaryKey(l.getRoleid());
			if(cliqueRoles!=null) {
				l.setRolename(cliqueRoles.getName());
			}
		}
		return list;
	}
	
	/**
	 * 修改用户状态
	 * @param id
	 * @param status
	 * @return
	 */
	public RestResponse updateUserstatus(Integer id,Integer status) {
		MtCliqueUser cliqueUser=mtCliqueUserMapper.selectByPrimaryKey(id);
		if(cliqueUser==null) {
			return GetRest.getFail("操作员不存在");
		}
		cliqueUser.setStatus(status);
		mtCliqueUserMapper.updateByPrimaryKeySelective(cliqueUser);
		return GetRest.getSuccess("操作成功");
	}
	
	public MtCliqueUser getUserById(Integer id){
		MtCliqueUser cliqueUser=mtCliqueUserMapper.selectByPrimaryKey(id);
		return cliqueUser;
	}
	
	/**
	 * 编辑集团操作员
	 * @param cliqueUser
	 * @return
	 */
	public RestResponse editUser(MtCliqueUser cliqueUser,HttpServletRequest request) {
		if(cliqueUser.getRoleid()==null) {
			return GetRest.getFail("请选择角色");
		}
		if(StringUtils.isBlank(cliqueUser.getLogin())) {
			return GetRest.getFail("请输入账号");
		}
		if(StringUtils.isBlank(cliqueUser.getPhone())) {
			return GetRest.getFail("请输入联系方式");
		}
		String msg = RpxUtils.valid_phone(cliqueUser.getPhone());
		if (StringUtils.isNotBlank(msg)) {
			return GetRest.getFail(msg);
		}
		if(cliqueUser.getId()==null) {
			MtCliqueUserExample example=new MtCliqueUserExample();
			example.createCriteria().andLoginEqualTo(cliqueUser.getLogin());
			int count=mtCliqueUserMapper.countByExample(example);
			if(count>0) {
				return GetRest.getFail("账号已存在");
			}
			if(StringUtils.isBlank(cliqueUser.getPassword())) {
				return GetRest.getFail("请输入密码");
			}
			String pwd=PwdEncode.encodePwd(cliqueUser.getPassword());
			cliqueUser.setTargetid(PlatSession.getUserId(request));
			cliqueUser.setPassword(pwd);
			int i=mtCliqueUserMapper.insertSelective(cliqueUser);
			if(i<1) {
				return GetRest.getFail("新增失败");
			}
		}else {
			MtCliqueUserExample example=new MtCliqueUserExample();
			example.createCriteria().andLoginEqualTo(cliqueUser.getLogin()).andIdNotEqualTo(cliqueUser.getId());
			int count=mtCliqueUserMapper.countByExample(example);
			if(count>0) {
				return GetRest.getFail("账号已存在");
			}
			int i=mtCliqueUserMapper.updateByPrimaryKeySelective(cliqueUser);
			if(i<1) {
				return GetRest.getFail("编辑失败,未找到操作员");
			}
		}
		return GetRest.getSuccess("操作成功");
	}
	
	/**
	 * 重置操作员密码
	 * @return
	 */
	public RestResponse restUserPwd(Integer id) {
		MtCliqueUser cliqueUser=mtCliqueUserMapper.selectByPrimaryKey(id);
		if(cliqueUser==null) {
			return GetRest.getFail("会员不存在");
		}
		String pwd=PwdEncode.encodePwd(StaticUtils.DEFAULT_PASSWORD);
		cliqueUser.setPassword(pwd);
		mtCliqueUserMapper.updateByPrimaryKey(cliqueUser);
		return GetRest.getSuccess("重置成功,密码为:"+StaticUtils.DEFAULT_PASSWORD);
	}
}
