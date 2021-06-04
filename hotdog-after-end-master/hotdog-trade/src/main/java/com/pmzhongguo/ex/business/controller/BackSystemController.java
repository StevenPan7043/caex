package com.pmzhongguo.ex.business.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.service.ApiAccessLimitService;
import com.pmzhongguo.ex.business.vo.OperLogExcelVo;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.Constants;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.framework.controller.LoginController;
import com.pmzhongguo.ex.framework.entity.FrmConfig;
import com.pmzhongguo.ex.framework.entity.FrmUser;
import com.pmzhongguo.ex.framework.service.FrmUserService;
import com.pmzhongguo.zzextool.consts.JedisChannelConst;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.zzextool.utils.ExportExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台系统配置
 * @author jary
 * @creatTime 2019/9/25 10:38 AM
 */
@ApiIgnore
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("backstage/frm")
public class BackSystemController extends TopController {
    private static Logger zkLog = LoggerFactory.getLogger("zookeeper");

    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Resource
    private FrmUserService userService;

    @Resource
    private ApiAccessLimitService apiAccessLimitService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }

    @RequestMapping("/toManageRight")
    public String toManageRight(HttpServletRequest request) {
        $attr("userName", HelpUtils.getFrmUser().getUser_name());
        return "system/right/right_list";
    }

    @RequestMapping("/listAllRight")
    @ResponseBody
    public Map listAllRight(HttpServletRequest request) {
        String functionId = $("functionId");
        List<Map<String, Object>> list = null;
        if (HelpUtils.nullOrBlank(functionId)) {
            list = userService.getTopMenuList();
        } else {
            list = userService.getSubMenuList(functionId);
        }

        Map map = new HashMap();
        map.put("Rows", list);
        map.put("Total", list.size());
        return map;
    }

    /**
     * 移除所有session中存在的信息
     *
     * @param session
     */
    private void removeSession(HttpSession session) {
        if (null != session.getAttribute(Constants.USER_FUNCTION_KEY))
            session.removeAttribute(Constants.USER_FUNCTION_KEY);
        if (null != session.getAttribute(Constants.SYS_SESSION_USER))
            session.removeAttribute(Constants.SYS_SESSION_USER);
        session.invalidate();
    }

    @RequestMapping("/toListOperLog")
    public String toListOperLog(HttpServletRequest request) {
        attrCommonDateTime();

        return "framework/operLog_list";
    }

    @RequestMapping(value = "/listOperLog", method = RequestMethod.GET, consumes="application/json")
    @ResponseBody
    public Map listOperLog(HttpServletRequest request) {
        Map params = $params(request);
        List<Map<String, Object>> list = userService.loadOperLogPage(params);

        Map map = new HashMap();
        map.put("Rows", list);
        map.put("Total", params.get("total"));
        return map;
    }

    /**
     * 导出 操作日志记录
     *
     * @param request
     * @param response
     * @throws BusinessException
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/operlog/export", method = RequestMethod.GET)
    public void withdrawRechargeExport(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map<String,Object> param = $params(request);
        String fileName = "操作日志记录报表-"+HelpUtils.getCurrTime();
        ExportExcel excel = new ExportExcel(fileName, OperLogExcelVo.class);
        List<OperLogExcelVo> list = userService.findOperLogList(param);
        if (list != null && list.size() > HelpUtils.EXPORT_LIMIT_NUM) {
            throw new BusinessException(Resp.FAIL,ErrorInfoEnum.EXPORT_EXCEL_LIMIT_GT_5000.getErrorCNMsg());
        }
        excel.setDataList(list);
        excel.write(response,  fileName + ".xls");
    }

    @RequestMapping("/viewMrgConfig")
    public String viewMrgConfig(HttpServletRequest request,
                                HttpServletResponse response) {
        FrmConfig mgrConfig = this.userService.findConfig();
        request.setAttribute("mgrConfig", mgrConfig);
        FrmUser user = HelpUtils.getFrmUser();
        request.setAttribute("user", user);
        return "framework/mgrConfig";
    }

    @RequestMapping("/modifyMgrConfig")
    @ResponseBody
    public Resp modifyMgrConfig(@ModelAttribute("form") FrmConfig mgrConfig,
                                HttpServletRequest request, HttpServletResponse response) {

        if(!HelpUtils.nullOrBlank(mgrConfig.getOtc_deposit_currency())) {
            mgrConfig.setOtc_deposit_currency(mgrConfig.getOtc_deposit_currency().toUpperCase());
        }

        this.userService.modifyMgrConfig(mgrConfig);
        // 修改以后需要刷新 ServeletContext 里面的数据，以免其他类取出的数据不是最新的
        mgrConfig = this.userService.findConfig();
        request.getServletContext()
                .setAttribute(HelpUtils.MGRCONFIG, mgrConfig);

        apiAccessLimitService.loadApiAccessLimitCache();
        // 服务器系统配置信息同步
        JedisUtil.getInstance().publish(JedisChannelConst.JEDIS_CHANNEL_SYNC_CONFIG,JedisChannelConst.SYNC_MESSAGE);
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }
}
