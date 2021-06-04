package com.pmzhongguo.ex.framework.mapper;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.business.vo.OperLogExcelVo;
import org.apache.ibatis.annotations.Param;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ex.framework.entity.FrmConfig;
import com.pmzhongguo.ex.framework.entity.FrmUser;
import com.pmzhongguo.ex.framework.entity.RightUserDto;


public interface FrmUserMapper extends SuperMapper<FrmUser> {

	public List getDictionaryDataList(String dicId);

	public List<FrmUser> getLoginUser(FrmUser user);

	public List<Map<String, Object>> getUserRightList(
			@Param(value = "userId") int userId);

	public FrmConfig findConfig();

	public List<Map<String, Object>> getUserMenuList(
			@Param(value = "userId") int userId);

	public List<Map<String, Object>> getUserMenuList();

	public void modifyMgrConfig(FrmConfig mgrConfig);

	public List<Map<String, Object>> getUserRoleInfo(
			@Param(value = "userId") int userId);

	public void updateUser(FrmUser user);

	public void resetUserPassword(Map param);

	public List<Map<String, Object>> getAllFuncs(
			@Param(value = "userId") int userId);

	public void deleteRightRole(@Param(value = "roleId") int roleId);

	public void deleteRightUser(@Param(value = "userId") int userId);

	public List<Map<String, Object>> getRoleFuncs(
			@Param(value = "roleId") int roleId);

	public void addUser(FrmUser user);

	public void editUser(FrmUser user);

	public void delUser(@Param(value = "id") int id);

	public void saveUserRole(Map params);

	public void deleteUserRole(@Param(value = "userId") int userId);

	public List<Map<String, Object>> getTopMenuList();

	public List<Map<String, Object>> getSubMenuList(
			@Param(value = "parent_id") String parent_id);

	public void insertRightUserBatch(List<RightUserDto> list);

	public List<Map<String, Object>> getAllUserPage(Map param);

	public void logVisit(@Param(value = "userName") String userName,
			@Param(value = "userRealName") String userRealName,
			@Param(value = "userIp") String userIp,
			@Param(value = "operDesc") String operFunction,
			@Param(value = "operUrl") String operUrl);

	public List loadOperLogPage(Map params);
	public List<OperLogExcelVo> findOperLogList(Map params);

	public List loadDataBaseBackInfoPage(Map params);

	public void backDataBase(Map params);

	public List getUserListByRoleId(@Param(value = "roleId") int roleId);

	public List loadUserListForRole(@Param(value = "roleId") int roleId);

	public void updateFunctionUse(Map map);

	public void updateFunctionUsed();

	public Map<String, Object> loadFunctionById(String func_id);

	public List<Map<String, Object>> getFactoryRightList();

	public List<Map<String, Object>> getFactoryMenuList();

}
