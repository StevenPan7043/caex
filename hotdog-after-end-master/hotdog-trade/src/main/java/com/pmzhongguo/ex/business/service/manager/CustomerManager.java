package com.pmzhongguo.ex.business.service.manager;

import com.alibaba.fastjson.JSONObject;
import com.contract.entity.CCustomer;
import com.contract.entity.CCustomerExample;
import com.contract.entity.CWallet;
import com.contract.enums.CoinEnums;
import com.pmzhongguo.ex.business.entity.AuthIdentity;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.mapper.ContractCustomerMapper;
import com.pmzhongguo.ex.business.mapper.ContractWalletMapper;
import com.pmzhongguo.ex.business.mapper.MemberMapper;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.business.service.RedisUtilsService;
import com.pmzhongguo.ex.business.service.ReturnCommiService;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.Constants;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.qiniu.util.BeanUtil;
import com.qiniu.util.DateStyleEnum;
import com.qiniu.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 合约用户
 * @author jary
 * @creatTime 2020/2/27 9:53 AM
 */
@Service
@Transactional
public class CustomerManager   {
    @Autowired
    private MemberService memberService;
    @Autowired
    private RedisUtilsService redisUtilsService;

    @Autowired MemberServiceManager memberServiceManager;
    @Autowired
    private ContractCustomerMapper contractCustomerMapper;
    @Autowired
    private ContractWalletMapper contractWalletMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private ReturnCommiService returnCommiService;


    public ObjResp editCustomer(Map<String, Object> map, Member member) {
        Object isvalid = map.get("isValid");
        if (BeanUtil.isEmpty(isvalid) || !isvalid.equals("0")) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.PARAMS_ERROR.getErrorENMsg(), null);
        }

        CCustomer cCustomer = contractCustomerMapper.selectByPrimaryKey(member.getId());
        Member memberById = memberService.getMemberById(member.getId());
        if (BeanUtil.isEmpty(cCustomer)) {
            cCustomer = new CCustomer();
            if (memberById.getAuth_grade() != 0) {
                AuthIdentity authIdentityById = memberService.getAuthIdentityById(member.getId());
                cCustomer.setRealname((StringUtils.isEmpty(authIdentityById.getFamily_name()) ? "" : authIdentityById.getFamily_name()) +
                        (StringUtils.isEmpty(authIdentityById.getGiven_name()) ? "" : authIdentityById.getGiven_name()));
                cCustomer.setIdcard(authIdentityById.getId_number());
                cCustomer.setAuthflag(3);
            }else {
                cCustomer.setAuthflag(1);
            }
            cCustomer.setId(memberById.getId());
            cCustomer.setPhone(memberById.getM_name());
            cCustomer.setPassword(memberById.getM_pwd());
            cCustomer.setPayword(memberById.getM_security_pwd());
            cCustomer.setInvitationcode(memberById.getInvite_code());

            cCustomer.setCreatetime(DateUtil.stringToDate(memberById.getReg_time(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS.getValue()));
            cCustomer.setParentid(memberById.getIntroduce_m_id());
            cCustomer.setLayer(1);
            cCustomer.setIsvalid(0);
            contractCustomerMapper.insertSelective(cCustomer);
            //保存钱包
            for (CoinEnums e : CoinEnums.values()) {
                CWallet wallet = new CWallet();
                wallet.setCid(member.getId());
                wallet.setType(e.name());
                contractWalletMapper.insertSelective(wallet);
            }
            redisUtilsService.setKey(member.getId() + "_banlance", "0");
            // 同时登录合约
//            return memberServiceManager.handlerContractLogin(member);
            String jsonObject = JSONObject.toJSONString(cCustomer); /// modify
            String token= member.getContactToken();
            // token 有效时间
            long maxtime = Constants.MEMBER_TOKEN_TIME_OUT;
            redisUtilsService.setKey(token, jsonObject, maxtime);
            return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, member);
        } else {
            cCustomer.setIsvalid(Integer.valueOf(isvalid + ""));
            cCustomer.setParentid(memberById.getIntroduce_m_id());
            contractCustomerMapper.updateByPrimaryKeySelective(cCustomer);
        }
        member.setIsValid(cCustomer.getIsvalid());
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, member);
    }

    /**
     * 进入合约时，没有邀请码，绑定邀请码
     * @param request
     * @param map
     * @param member
     * @return
     */
    public ObjResp bindingInviteCode(HttpServletRequest request, Map<String, Object> map, Member member) {
        Object invite_code = map.get("invite_code");
        if (invite_code == null) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_INVITATION_CODE_DOES_NOT_NULL.getErrorENMsg(), null);
        }
        CCustomerExample cCustomerExample = new CCustomerExample();
        cCustomerExample.createCriteria().andInvitationcodeEqualTo(invite_code + "");
        List<CCustomer> cCustomers = contractCustomerMapper.selectByExample(cCustomerExample);
        if (!CollectionUtils.isEmpty(cCustomers)) {
            //存入session
            CCustomer cCustomerParent = cCustomers.get(0);
            member.setIntroduce_m_id(cCustomerParent.getId());
            member.setInvite_code(cCustomerParent.getInvitationcode());
            JedisUtilMember.getInstance().setMember(request, member);

            //更新用户信息
            Member memberParam = new Member();
            memberParam.setIntroduce_m_id(cCustomerParent.getId());
            memberParam.setId(member.getId());
            memberMapper.updateMember(memberParam);
            // 添加返佣记录
            boolean result = returnCommiService.isExistByMemberIdAndIntroduceId(member);
            if (!result) {
                returnCommiService.add(member);
            }
        } else {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_INVITATION_CODE_DOES_NOT_EXIST.getErrorENMsg(), null);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, member);
    }
}
