package com.pmzhongguo.ex.business.service.manager;

import com.alibaba.fastjson.JSONObject;
import com.contract.common.FunctionUtils;
import com.contract.common.StaticUtils;
import com.contract.common.number.RandNumUtils;
import com.contract.common.pwd.PwdEncode;
import com.contract.dto.CustomerDto;
import com.contract.entity.CCustomer;
import com.contract.entity.CCustomerExample;
import com.contract.entity.CWallet;
import com.contract.enums.CoinEnums;
import com.contract.enums.RandNumType;
import com.google.common.collect.Maps;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.mapper.ContractCustomerMapper;
import com.pmzhongguo.ex.business.mapper.ContractWalletMapper;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.business.service.RedisUtilsService;
import com.pmzhongguo.ex.business.service.ReturnCommiService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.MacMD5;
import com.pmzhongguo.ex.core.web.Constants;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.zzextool.utils.StringUtil;
import com.qiniu.util.BeanUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @description: 兼容合约处理
 * @date: 2019-12-17 20:20
 * @author: 十一
 */
@Service
@Transactional
public class MemberServiceManager {

    private static Logger logger = LoggerFactory.getLogger(MemberServiceManager.class);


    @Autowired
    private MemberService memberService;

    @Autowired
    private ReturnCommiService returnCommiService;

    @Autowired
    private RedisUtilsService redisUtilsService;
    @Autowired
    private ContractCustomerMapper contractCustomerMapper;
    @Autowired
    private ContractWalletMapper contractWalletMapper;

    @Autowired
    private IntroduceRelationManager introduceRelationManager;


    /**
     * m_member,c_customer 两个表注册
     *
     * @param member
     * @return
     */
    public Resp handlerRegister(Member member) {

        String login_key = "register_login_" + member.getM_name();
        boolean lockflag_phone = redisUtilsService.setIncrSecond(login_key, 5);
        //设置2s有效期这个手机号基本一天手机号注册可以成功的如果消失了后面还有固定判断 主要是保证短时间不要有并发问题
        if (!lockflag_phone) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_LIMIT_FREQUENCY_OPERTION.getErrorENMsg());
        }

        //用户名全部用小写
        Member oldMember = memberService.getMemberBy(HelpUtils.newHashMap("m_name", member.getM_name()));

        if (null != oldMember && oldMember.getM_status() == 1) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ALREADY_REG_FINDPWD.getErrorENMsg());
        } else if (null != oldMember && oldMember.getM_status() == 2) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_LOCKED.getErrorENMsg());
        }
        // 后台再校验校验码是否正确
        if (null == oldMember || !member.getSms_code().equals(oldMember.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
        }

        // 完成注册
        String pwd = MacMD5.CalcMD5Member(member.getM_pwd());
        member.setM_pwd(pwd);
        member.setId(oldMember.getId());
        member.setM_status(1);
        //刷新验证码，防止用户可以用原来的验证码处理其他事物
        member.setSms_code(HelpUtils.randomNumNo0(6));

        // 推荐人是空则跳过
        CCustomer introMember = null;
        if (!HelpUtils.nullOrBlank(member.getInvite_code())) {
            try {
                CCustomerExample cCustomerExample = new CCustomerExample();
                cCustomerExample.createCriteria().andInvitationcodeEqualTo(member.getInvite_code());
                List<CCustomer> cCustomers = contractCustomerMapper.selectByExample(cCustomerExample);
                CCustomer PhoneCustomer = contractCustomerMapper.getByInvitationcode(member.getInvite_code());
                if(PhoneCustomer.getIdentity()==3){
                    return new Resp(Resp.FAIL,ErrorInfoEnum.CONTRACT_ANALOG_ACCOUNT_NO_INVITER.getErrorENMsg());
                }
                if (!CollectionUtils.isEmpty(cCustomers)) {
                    introMember = cCustomers.get(0);
                    member.setIntroduce_m_id(introMember.getId());
                } else {
                    return new Resp(Resp.FAIL, ErrorInfoEnum.CONTRACT_INVITATION_CODE_DOES_NOT_EXIST.getErrorENMsg());
                }
            } catch (Exception e) {
                logger.warn("用户邀请异常：【 {} 】", e.fillInStackTrace());
            }
        }
//        else {
//            return new Resp(Resp.FAIL, ErrorInfoEnum.CONTRACT_INVITATION_CODE_DOES_NOT_NULL.getErrorENMsg());
//        }
        String inviteCode = RandNumUtils.get(RandNumType.NUMBER_LETTER,8);
        member.setInvite_code(inviteCode);
        // 只有当用户是被邀请注册的才可以返佣
        if (!StringUtil.isNullOrBank(member.getIntroduce_m_id())) {
            // 添加返佣记录
            boolean result = returnCommiService.isExistByMemberIdAndIntroduceId(member);
            if (!result) {
                returnCommiService.add(member);
            }
        }
        member.setApi_status(2);
        memberService.updateMember(member, true, false);

        // =================== 兼容合约代码 =========================

        CCustomer customer = new CCustomer();
        customer.setId(member.getId());
//        if (!member.getM_name().contains("@")) {
//            customer.setPhone(member.getM_name());
//        }
//        if (HelpUtils.isMobile(member.getM_name())) {
        customer.setPhone(member.getM_name());
//        } else {
//            customer.setPhone(BeanUtil.isEmpty(member.getPhone()) ? "" : member.getPhone());
//        }
        if(member.getCheckeds()==1){
            customer.setIdentity(3);
        }
        // 邀请码
        customer.setInvitationcode(member.getInvite_code());
        if (introMember != null) {
            customer.setParentid(introMember.getId());
            customer.setLayer(introMember.getLayer() + 1);
            customer.setUserid(introMember.getUserid());
            customer.setSalesman(introMember.getSalesman());
        }
        customer.setPassword(pwd);


        int i = contractCustomerMapper.insertSelective(customer);
        if (i < 1) {
            logger.error("合约注册失败：{}", JSONObject.toJSONString(customer));
            return Resp.failMsg(ErrorInfoEnum.LANG_REG_FAIL.getErrorENMsg());
        }
        //保存钱包
        for (CoinEnums e : CoinEnums.values()) {
            CWallet wallet = new CWallet();
            wallet.setCid(customer.getId());
            wallet.setType(e.name());
            contractWalletMapper.insertSelective(wallet);
        }
        redisUtilsService.setKey(customer.getId() + "_banlance", "0");
        //添加关联关系
        try {
            new Thread(()->{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                introduceRelationManager.addIntroduceRelation(member.getId());}).start();
        } catch (Exception e) {
            logger.warn("new Thread(()->introduceRelationManager.addIntroduceRelation(member.getId())).start()。memeberid:" + member.getId() + " \nException:" + e.getStackTrace());
        }
        return Resp.SUCCESS_STATE;


    }

    /**
     * 合约退出
     * @param member
     * @return
     */
    public ObjResp handlerContractLogin(Member member) {
        String login = member.getM_name();
        String key = getContractTokenRedisKey(login);
//        if (!HelpUtils.nullOrBlank(key)) {
//            String header = request.getHeader(Constants.LOGIN_TOKEN);
//            if (header.equals("app") || header.equals("web")) {
//                key = header + "_" + key;
//            }
//        }
//        String enter_key = redisUtilsService.getKey(key);
//        if (!StringUtils.isEmpty(enter_key)) {
//            redisUtilsService.deleteKey(enter_key);
//            //退出
//            redisUtilsService.deleteKey(key);
//        }
        // 合约不支持邮箱，需要用id来查
//        CCustomer customer = contractCustomerMapper.getByPhone(login);
        CCustomer customer = contractCustomerMapper.selectByPrimaryKey(member.getId());
        if (customer != null) {
            member.setIsValid(customer.getIsvalid());
            if (FunctionUtils.isEquals(StaticUtils.status_no, customer.getStatus())) {
                return ObjResp.failMsg(ErrorInfoEnum.LANG_ACCOUNT_LOCKED.getErrorENMsg());
            }

            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            String jsonObject = JSONObject.toJSONString(customerDto);
//            String token = PwdEncode.encodePwd(getContractTokenRedisKey(login));
            String token = PwdEncode.encodePwd(key);
            // token 有效时间
            long maxtime = Constants.MEMBER_TOKEN_TIME_OUT;
            redisUtilsService.setKey(token, jsonObject, maxtime);
//            redisUtilsService.setKey(key, token);
            member.setContactToken(token);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, member);
    }

    /**
     * 合约的这个key是用来存具体用户对象的，可以通过这个可以获得 getContractLoginRedisKey(java.lang.String)
     * @param login
     * @return
     */
    private String getContractTokenRedisKey(String login) {
        return login + "_" + com.contract.common.DateUtil.currentTimeMillis();
    }

    /**
     * 合约的这个key是用来获取token值的
     * @param login
     * @return
     */
    private String getContractLoginRedisKey(String login) {
        return login + "_token";
    }

    /**
     * 删除合约再redis中的key
     * @param member
     */
    public void handlerContractLogout(Member member) {
        if (member == null) {
            return;
        }
        String contactToken = member.getContactToken();
        if(!StringUtil.isNullOrBank(contactToken)) {
            redisUtilsService.deleteKey(contactToken);
        }
//        HashMap<Object, Object> params = Maps.newHashMap();
//        params.put("id",member.getId());
//        Member memberById = memberService.getMemberBy(params);
//        if (memberById == null) {
//            return;
//        }
//        String contractLoginRedisKey = getContractLoginRedisKey(memberById.getM_name());
//        String token = redisUtilsService.getKey(contractLoginRedisKey);
//        redisUtilsService.deleteKey(contractLoginRedisKey);
//        if (!StringUtil.isNullOrBank(token)) {
//            redisUtilsService.deleteKey(token);
//        }

    }

    /**
     * 登录密码用的是交易所一样的
     * 资金密码
     * 更新合约用户信息，资金密码和登录密码
     * @param member
     */
    public void updateContractMember(Member member,boolean isReg,boolean needLog,String payPwd) {

        memberService.updateMember(member,isReg,needLog);


        CCustomer customer = new CCustomer();
        customer.setId(member.getId());
        customer.setPayword(PwdEncode.encodePwd(payPwd));
        contractCustomerMapper.updateByPrimaryKeySelective(customer);
    }
}
