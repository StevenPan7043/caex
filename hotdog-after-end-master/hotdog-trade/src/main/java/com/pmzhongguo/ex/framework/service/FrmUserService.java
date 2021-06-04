package com.pmzhongguo.ex.framework.service;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import com.pmzhongguo.ex.business.vo.OperLogExcelVo;
import com.pmzhongguo.ex.business.service.IDataProcess;
import com.pmzhongguo.ex.core.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pmzhongguo.ex.business.service.ApiAccessLimitService;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.framework.entity.FrmConfig;
import com.pmzhongguo.ex.framework.entity.FrmUser;
import com.pmzhongguo.ex.framework.entity.RightUserDto;
import com.pmzhongguo.ex.framework.mapper.FrmUserMapper;


//表明该文件需要事务
//@Transactional
//表明该文件是一个Service
@Service
public class FrmUserService extends BaseServiceSupport implements IDataProcess {
	
	@Resource
	private ApiAccessLimitService apiAccessLimitService;
	
	// 这个属性由Spring帮我们注入。Spring会帮我们new一个的
	// MyBatis帮我们管理xml与类的映射及Mapper，所以我们直接用@Autowired进行注入就可以了
	@Autowired
	private FrmUserMapper userMapper;
	private Map<String, Object> params = new HashMap<String, Object>();

	public FrmUser getUser(Integer userId) {
		return (FrmUser) userMapper.findById(userId);
	}

	public List<FrmUser> getLoginUser(FrmUser user) {
		params.put("user_name", user.getUser_name());
		params.put("user_pwd", user.getUser_pwd());
		return userMapper.getLoginUser(user);
	}

	public List<Map<String, Object>> getUserRightList(int userId) {
		return userMapper.getUserRightList(userId);
	}

	public FrmConfig findConfig() {
		FrmConfig frmConfig = this.userMapper.findConfig();
		if(frmConfig.getOtc_deposit_volume().compareTo(BigDecimal.ZERO) == 0) {
			frmConfig.setOtc_deposit_volume(frmConfig.getOtc_deposit_volume().stripTrailingZeros());
		}
		
		return frmConfig;
	}
	
	public void loadApiAccessLimitCache() {
		apiAccessLimitService.loadApiAccessLimitCache();
	}

	public List<Map<String, Object>> loadUserMune(FrmUser user) {
		return this.userMapper.getUserMenuList(user.getId());
	}

	public void modifyMgrConfig(FrmConfig mgrConfig) {
		userMapper.modifyMgrConfig(mgrConfig);
	}

	public void resetUserPassword(Integer userId) {
		Map map = new HashMap();
		map.put("password",
				MacMD5.CalcMD5(HelpUtils.getMgrConfig().getDef_passwd()));
		map.put("userId", userId);
		userMapper.resetUserPassword(map);
	}

	public List<Map<String, Object>> getAllFuncs(Integer userId) {
		return userMapper.getAllFuncs(userId);
	}

	public void logVisit(String userName, String userRealName, String userIp,
			String operDesc, String operUrl) {
		userMapper.logVisit(userName, userRealName, userIp, operDesc, operUrl);
	}

	public void saveRight(Integer objectId, String operatorType, String funcIds) {
		List<RightUserDto> funcList = new ArrayList<RightUserDto>();
		for (String func_id : funcIds.split(",")) {
			RightUserDto dto = new RightUserDto();
			dto.setFuncId(func_id);
			dto.setUserId(objectId);
			if (HelpUtils.nullOrBlank(func_id)) {
				continue;
			}
			funcList.add(dto);
		}
		userMapper.deleteRightUser(objectId);
		if (funcList.size() > 0) {
			userMapper.insertRightUserBatch(funcList);
		}
	}
	
	public void cacheEoslist(ServletContext servletContext) {
		Map<String, String> eoslistMap = new HashMap<String, String>();
		String eoslistStr = HelpUtils.getMgrConfig().getEoslist();
		if(!HelpUtils.nullOrBlank(eoslistStr)) {
			String[] eoslist = eoslistStr.split(",|\\|");
			eoslistMap = HelpUtils.toMap(eoslist);
		}
		servletContext.setAttribute(HelpUtils.EOSLIST, eoslistMap);
	}

	public List getAllUser(Map param) {
		return userMapper.getAllUserPage(param);
	}

	public void addUser(FrmUser user) {
		user.setUser_pwd(MacMD5.CalcMD5(HelpUtils.getMgrConfig()
				.getDef_passwd()));
		this.userMapper.addUser(user);
	}

	public void editUser(FrmUser user) {
		this.userMapper.editUser(user);
	}

	public void delUser(Integer staffId) {
		this.userMapper.delUser(staffId);
	}

	public List<Map<String, Object>> getTopMenuList() {
		return userMapper.getTopMenuList();
	}

	public List<Map<String, Object>> getSubMenuList(String parent_id) {
		return userMapper.getSubMenuList(parent_id);
	}

	public List loadOperLogPage(Map params) {
		return userMapper.loadOperLogPage(params);
	}

	public List<OperLogExcelVo> findOperLogList(Map<String, Object> param) {
		return userMapper.findOperLogList(param);

	}

	@Override
	public void dataSync(ServletContext servletContext) {
		FrmConfig mgrConfig = findConfig();
		servletContext.setAttribute(HelpUtils.MGRCONFIG, mgrConfig);
		cacheEoslist(servletContext);
		loadApiAccessLimitCache();
	}
}
