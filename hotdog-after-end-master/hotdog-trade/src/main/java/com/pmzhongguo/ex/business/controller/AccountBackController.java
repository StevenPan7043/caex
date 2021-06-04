//package com.pmzhongguo.ex.business.controller;
//
//        import com.mangofactory.swagger.annotations.ApiIgnore;
//        import com.pmzhongguo.ex.core.utils.JsonUtil;
//        import com.pmzhongguo.ex.core.utils.PropertiesUtil;
//        import com.pmzhongguo.ex.core.web.TopController;
//        import com.pmzhongguo.ex.core.web.resp.ObjResp;
//        import com.pmzhongguo.ex.core.web.resp.Resp;
//        import com.pmzhongguo.zzextool.utils.HttpUtil;
//        import com.qiniu.util.BeanUtil;
//        import com.qiniu.util.DateStyleEnum;
//        import com.qiniu.util.DateUtil;
//        import org.apache.commons.lang3.StringUtils;
//        import org.springframework.stereotype.Controller;
//        import org.springframework.web.bind.annotation.RequestMapping;
//        import org.springframework.web.bind.annotation.RequestMethod;
//        import org.springframework.web.bind.annotation.ResponseBody;
//
//        import javax.servlet.http.HttpServletRequest;
//        import javax.servlet.http.HttpServletResponse;
//        import java.util.Date;
//        import java.util.Map;
//
//
///**
// * @author jary
// * @creatTime 2019/11/18 9:56 AM
// * 用户资产
// */
//@ApiIgnore
//@Controller
//@RequestMapping("backstage/account")
////@RequestMapping("a")
//public class AccountBackController extends TopController {
//
//
//    /**
//     * 跳转至用户资产备份界面
//     *
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping(value = "toAccountList", method = RequestMethod.GET)
//    public String toAccountList(HttpServletRequest request, HttpServletResponse response) {
//        $attr("curDateAndReportTime", DateUtil.dateToString(new Date(), DateStyleEnum.YYYY_MM_DD_HH_MM));
//        return "business/member/account_backup_list";
//    }
//
//    /**
//     * 用户资产备份列表
//     *
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping(value = "getAccountList", method = RequestMethod.GET, consumes = "application/json")
//    @ResponseBody
//    public Map getAccountList(HttpServletRequest request, HttpServletResponse response) {
//        Map map = $params(request);
//        try {
//            if (!BeanUtil.isEmpty(map.get("startDate"))) {
//                String timestamp = DateUtil.stringToString(map.get("startDate") + "", DateStyleEnum.YYYY_MM_DD_HH_MM, DateStyleEnum.YYYY_MM_DD);
//                map.put("timestamp", timestamp);
//                String esAccountBackupUrl = PropertiesUtil.getPropValByKey("es_account_backup_url");
//
//                String result = HttpUtil.jsonPost(esAccountBackupUrl, JsonUtil.toJson(map));
//                if (!StringUtils.isEmpty(result)) {
//                    ObjResp objResp = JsonUtil.fromJson(result, ObjResp.class);
//                    if (objResp.getState().equals(Resp.FAIL)) {
//                        logger.warn("用户资产备份列表获取数据失败，失败原因：{}", objResp.getMsg());
//                        return null;
//                    }
//                    return (Map) objResp.getData();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return null;
//    }
//}
