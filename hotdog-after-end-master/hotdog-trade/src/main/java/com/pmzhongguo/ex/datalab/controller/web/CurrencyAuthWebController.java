package com.pmzhongguo.ex.datalab.controller.web;

import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.datalab.manager.CurrencyAuthManager;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/12/3 9:59 AM
 */
@Api(value = "数据实验室", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("dataLab")
public class CurrencyAuthWebController  extends TopController {


    @Resource
    private CurrencyAuthManager currencyAuthManager;


    @ApiOperation(value = "获得实验室基础数据", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getDataLabFirst", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getDataLabFirst(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 校验用户是否登录
            Member member = JedisUtilMember.getInstance().getMember(request, null);
            if (null == member) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
            }
            Map<String, Object> reqMap = $params(request);
            return currencyAuthManager.getDataLabFirst(reqMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg(), null);
        }
    }
}
