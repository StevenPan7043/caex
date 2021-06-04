package com.pmzhongguo.zzexrpcprovider.mapper.framework;

import com.pmzhongguo.ex.framework.entity.FrmConfig;
import com.pmzhongguo.ex.framework.entity.FrmUser;
import com.pmzhongguo.ex.framework.entity.RightUserDto;
import com.pmzhongguo.zzexrpcprovider.bean.GenericMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface FrmUserMapper extends GenericMapper {

    List getDictionaryDataList(String dicId);

    List<FrmUser> getLoginUser(FrmUser user);

    List<Map<String, Object>> getUserRightList(
            @Param(value = "userId") int userId);

    FrmConfig findConfig();

    List<Map<String, Object>> getUserMenuList(
            @Param(value = "userId") int userId);

    List<Map<String, Object>> getUserMenuList();

    void modifyMgrConfig(FrmConfig mgrConfig);

    List<Map<String, Object>> getUserRoleInfo(
            @Param(value = "userId") int userId);

    void updateUser(FrmUser user);

    void resetUserPassword(Map param);

    List<Map<String, Object>> getAllFuncs(
            @Param(value = "userId") int userId);

    void deleteRightRole(@Param(value = "roleId") int roleId);

    void deleteRightUser(@Param(value = "userId") int userId);

    List<Map<String, Object>> getRoleFuncs(@Param(value = "roleId") int roleId);

    void addUser(FrmUser user);

    void editUser(FrmUser user);

    void delUser(@Param(value = "id") int id);

    void saveUserRole(Map params);

    void deleteUserRole(@Param(value = "userId") int userId);

    List<Map<String, Object>> getTopMenuList();

    List<Map<String, Object>> getSubMenuList(
            @Param(value = "parent_id") String parent_id);

    void insertRightUserBatch(List<RightUserDto> list);

    List<Map<String, Object>> getAllUserPage(Map param);

    void logVisit(@Param(value = "userName") String userName,
                  @Param(value = "userRealName") String userRealName,
                  @Param(value = "userIp") String userIp,
                  @Param(value = "operDesc") String operFunction,
                  @Param(value = "operUrl") String operUrl);

    List loadOperLogPage(Map params);

    List loadDataBaseBackInfoPage(Map params);

    void backDataBase(Map params);

    List getUserListByRoleId(@Param(value = "roleId") int roleId);

    List loadUserListForRole(@Param(value = "roleId") int roleId);

    void updateFunctionUse(Map map);

    void updateFunctionUsed();

    Map<String, Object> loadFunctionById(String func_id);

    List<Map<String, Object>> getFactoryRightList();

    List<Map<String, Object>> getFactoryMenuList();

}
