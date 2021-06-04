package com.pmzhongguo.ex.transfer.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.transfer.entity.RespUserSecret;
import com.pmzhongguo.ex.transfer.service.SSOUsersTokenManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jary
 * @creatTime 2019/6/25 2:03 PM
 */
@ApiIgnore
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/tk")
public class ThirdProTokenController extends TopController {

    @Autowired
    private SSOUsersTokenManager ssoUsersTokenManager;

    /**
     * 用户token信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getUserSSOToken", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getUserSSOToken(HttpServletRequest request) {
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
        }
        // 登录校验
        ObjResp objResp = memberService.loginCommon(request, member, false);
        if (objResp.getState() != 1) {
            return objResp;
        }
//		Member memberParam = memberService.getMemberById(member.getId());
//		if (HelpUtils.isMobile(member.getM_name())) {
//			memberParam.setPhone(member.getM_name());
//			memberParam.setGoogle_auth_key(null);
//			memberService.updateMember(memberParam);
//		}
        if (StringUtils.isBlank(member.getPhone())) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.PSL_SET_PHONE.getErrorENMsg(), null);
        }
        RespUserSecret reqBaseSecret = ssoUsersTokenManager.getUserSSOToken(member, "lastWinner");
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, reqBaseSecret);
    }
}
