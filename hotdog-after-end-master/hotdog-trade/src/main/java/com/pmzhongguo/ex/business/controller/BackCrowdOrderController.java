package com.pmzhongguo.ex.business.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.crowd.config.rediskey.CrowdProjectKey;
import com.pmzhongguo.crowd.dto.CrowdOrderDto;
import com.pmzhongguo.crowd.entity.CrowdOrder;
import com.pmzhongguo.crowd.entity.CrowdProject;
import com.pmzhongguo.crowd.service.CrowdOrderService;
import com.pmzhongguo.crowd.service.CrowdProjectService;
import com.pmzhongguo.crowd.vo.CrowdOrderVo;
import com.pmzhongguo.ex.business.entity.Account;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.business.service.MarketService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.utils.MacMD5;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.LstResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description: 众筹项目订单接口
 * @date: 2019-03-03 10:09
 * @author: 十一
 */
@ApiIgnore
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("backstage/crowd")
public class BackCrowdOrderController extends TopController{

    private static Logger logger = LoggerFactory.getLogger(BackCrowdOrderController.class);

    @Autowired
    private CrowdOrderService crowdOrderService;

    @Autowired
    private CrowdProjectService crowdProjectService;


    // 加密salt
    private static final String ORDER_KEY = "9skdjs2d";
    // 有效时间，单位秒
    private static final Integer VAILD_TIME = 15;
    /**
     * 限定一个值
     */
    private static final BigDecimal ZZEX_LIMIT_PRICE = new BigDecimal("0.5");



//    @ApiOperation(value = "众筹项目下单", notes = "project_id：项目id,address: 地址，buy_amount: 购买数量，fund_pwd: 资金密码，ts： 时间戳(秒)，key：加密串，source：订单来源，0:web，1:ios，2:Android，3:api", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ResponseBody
    public Resp saveOrder(HttpServletRequest request, @RequestBody CrowdOrderVo crowdOrderVo) {

        Member member = JedisUtilMember.getInstance().getMember(request, null);

        if (null == member || member.getId() <= 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }

        if (HelpUtils.nullOrBlank(crowdOrderVo.getBuy_amount()) || HelpUtils.nullOrBlank(crowdOrderVo.getAddress())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SUBMIT_DATA.getErrorENMsg());
        }

        // 判断不能超过15s
        if (Math.abs((HelpUtils.getNowTimeStampInt() - crowdOrderVo.getTs())) > VAILD_TIME) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_REQ_TIME_OUT.getErrorENMsg());
        }

        // md5校验防止该接口恶意被刷,ts+key+buy_amount+ts  32位md5加密  取前10位
        String info = crowdOrderVo.getTs() + ORDER_KEY + crowdOrderVo.getBuy_amount() + crowdOrderVo.getTs();
        String serverSign = MacMD5.CalcMD5(info, 10);
        logger.warn("sign：" + serverSign + " params：" + info);
        if (!crowdOrderVo.getKey().equals(serverSign)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SIGN.getErrorENMsg());
        }

        if (!"1".equals(member.getM_security_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PLS_SET_SEC_PWD.getErrorENMsg());
        }

        // 先从缓存中获取,取出来的数据只能用于判断基本信息，剩余数量不能用这个判断，需从数据库中查询
        String crowdKey = CrowdProjectKey.crowdProjectKey.getPrefix()+":"+crowdOrderVo.getProject_id();
        CrowdProject cacheCrowdProject = (CrowdProject) JedisUtil.getInstance().get(crowdKey,false);
        if (cacheCrowdProject == null) {
            cacheCrowdProject = crowdProjectService.findById(crowdOrderVo.getProject_id());
            String key = CrowdProjectKey.crowdProjectKey.getPrefix()+":"+cacheCrowdProject.getId();
            JedisUtil.getInstance().set(key,cacheCrowdProject,false);
            if (cacheCrowdProject == null) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.PARAMS_ERROR.getErrorENMsg());
            }
        }
        // 重新设置下过期时间
        JedisUtil.getInstance().expire(crowdKey, CrowdProjectKey.expire);

        BigDecimal totalPrice = crowdOrderVo.getBuy_amount().multiply(cacheCrowdProject.getRush_price())
                .setScale(cacheCrowdProject.getP_precision(),BigDecimal.ROUND_HALF_DOWN);
        // 如果是这个币种，并且计价货币是zzex，就要使用另外的计价策略
        if ("ZGT".equalsIgnoreCase(cacheCrowdProject.getCurrency()) && "ZZEX".equalsIgnoreCase(cacheCrowdProject.getQuote_currency())) {
            TickerResp tickerResp = MarketService.getTicker().get("zzexzc_ticker");
            if (tickerResp == null ||  BigDecimal.ZERO.compareTo(tickerResp.getClose()) >= 0) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.PARAMS_ERROR.getErrorENMsg());
            }
            // 限定价格/zzex价=真正的抢购价格
            BigDecimal rushPrice = ZZEX_LIMIT_PRICE.divide(tickerResp.getClose(), cacheCrowdProject.getP_precision(), BigDecimal.ROUND_HALF_DOWN);

            totalPrice = crowdOrderVo.getBuy_amount().multiply(rushPrice)
                    .setScale(cacheCrowdProject.getP_precision(),BigDecimal.ROUND_HALF_DOWN);
            cacheCrowdProject.setRush_price(rushPrice);
        }


        // 最大额度
        if(totalPrice.compareTo(cacheCrowdProject.getBuy_upper_limit()) > 1) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_UPPER_AMOUNT.getErrorENMsg());
        }

        // 最小额度saveCrowdOrder
        if(cacheCrowdProject.getBuy_lower_limit().compareTo(totalPrice) > 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_LOWER_AMOUNT.getErrorENMsg());
        }

        // 总额度
        BigDecimal memberTotalPrice =
                crowdOrderService.findCurrencyAmountByMemberIdAndProjectId(HelpUtils.newHashMap("member_id", member.getId(), "crd_pro_id", crowdOrderVo.getProject_id()));
        if (memberTotalPrice != null) {
            BigDecimal plusTotalPrice = memberTotalPrice.add(crowdOrderVo.getBuy_amount().multiply(cacheCrowdProject.getRush_price()));
            if (plusTotalPrice.compareTo(cacheCrowdProject.getBuy_upper_limit()) > 0) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_UPPER_AMOUNT.getErrorENMsg());
            }

        }

        // 购买时间
        if (HelpUtils.stringToDateWithTime(cacheCrowdProject.getRush_begin_time() + ":00").getTime() > HelpUtils.getLongTime()) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ACTIVITY_NOT_OPEN.getErrorENMsg());
        }
        if (HelpUtils.stringToDateWithTime(cacheCrowdProject.getRush_end_time() + ":00").getTime() < HelpUtils.getLongTime()) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ACTIVITY_HAS_END.getErrorENMsg());
        }

        Member oldMember = memberService.getMemberById(member.getId());
        // 资金密码
        if (!MacMD5.CalcMD5Member(crowdOrderVo.getFund_pwd()).equals(oldMember.getM_security_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg());
        }
        if (oldMember.getAuth_grade() == 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_AUTH_IDENTITY_FIRST.getErrorENMsg());
        }

        // 资金是否满足
        Account account = memberService.getAccount(HelpUtils.newHashMap("member_id", oldMember.getId(),"currency",cacheCrowdProject.getQuote_currency()));
        if (HelpUtils.nullOrBlank(account)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorENMsg());
        }
        if ((account.getTotal_balance().subtract(account.getFrozen_balance())).compareTo(totalPrice) < 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorENMsg());
        }
        crowdOrderVo.setOperIp(HelpUtils.getIpAddr(request));

        return crowdOrderService.saveCrowdOrder(member, crowdOrderVo, account,totalPrice, cacheCrowdProject);
    }

//    @ApiOperation(value = "众筹项目订单查询", notes = "查询用户订单", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/order/{page}/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    public Resp findOrderByMember(HttpServletRequest request,@PathVariable Integer page, @PathVariable Integer pageSize) {

        Member m = JedisUtilMember.getInstance().getMember(request, null);

        if (null == m || m.getId() <= 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("member_id",m.getId());
        params.put("pagesize",pageSize);
        params.put("page",page);
        List<CrowdOrder> list =  crowdOrderService.findOrderByMember(params);
        return new LstResp(Resp.SUCCESS,Resp.SUCCESS_MSG,Integer.parseInt(params.get("total") + ""),list);
    }

    /**===================================================================后台管理端,要加@ApiIgnore=============================================================**/

    /**
     * 分页获取轮播图列表
     * @return
     */
    @RequestMapping(value = "/order/mgr/list_page", method = RequestMethod.GET)
    @ApiIgnore
    public String getOrderPage() {

        return "crowd/order/order_list";
    }


    /**
     * 后台订单查询
     * @param request
     * @return
     */
    @RequestMapping(value = "/order/mgr/list", method = RequestMethod.GET)
    @ApiIgnore
    @ResponseBody
    public Map findOrderByMember(HttpServletRequest request) {

        Map<String,Object> params = $params(request);
        List<Map<String,Object>> list =  crowdOrderService.findMgrByPage(params);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("Rows", list);
        map.put("Total", params.get("total"));
        return map;
    }



    @ResponseBody
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public Resp listAuthIdentityNotPass(HttpServletRequest request, @RequestBody List<CrowdOrderDto> crowdOrderList) {

        CrowdOrderDto crowdOrderDto = crowdOrderList.get(0);
        Currency currency = HelpUtils.getCurrencyMap().get(crowdOrderDto.getCurrency().toUpperCase());
        if(currency == null) {
            return new Resp(Resp.FAIL,ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorCNMsg());
        }

        crowdOrderService.transfer2MemberAccount(crowdOrderList);
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }





}
