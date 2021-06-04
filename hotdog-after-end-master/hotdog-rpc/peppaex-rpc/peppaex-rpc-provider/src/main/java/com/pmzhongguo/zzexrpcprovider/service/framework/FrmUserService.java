package com.pmzhongguo.zzexrpcprovider.service.framework;

import com.pmzhongguo.ex.framework.entity.FrmConfig;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/11 10:16
 * @description：系统配置service
 * @version: $
 */
public interface FrmUserService {

//    FrmUser getUser(Integer userId);
//
//    List<FrmUser> getLoginUser(FrmUser user);
//
//    List<Map<String, Object>> getUserRightList(int userId);

    /**
     * 获取系统配置信息
     *
     * @return
     */
    FrmConfig findConfig();

//    void loadApiAccessLimitCache();
//
//    List<Map<String, Object>> loadUserMune(FrmUser user);
//
//    void modifyMgrConfig(FrmConfig mgrConfig);
//
//    void resetUserPassword(Integer userId);
//
//    List<Map<String, Object>> getAllFuncs(Integer userId);
//
//    void logVisit(String userName, String userRealName, String userIp, String operDesc, String operUrl);
//
//    void saveRight(Integer objectId, String operatorType, String funcIds);
//
//    void cacheEoslist(ServletContext servletContext);
//
//    List getAllUser(Map param);
//
//    void addUser(FrmUser user);
//
//    void editUser(FrmUser user);
//
//    void delUser(Integer staffId);
//
//    List<Map<String, Object>> getTopMenuList();
//
//    List<Map<String, Object>> getSubMenuList(String parent_id);
//
//    List loadOperLogPage(Map params);
}
