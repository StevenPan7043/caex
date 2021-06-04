package com.pmzhongguo.crowd.service;

import com.google.common.collect.ImmutableMap;
import com.pmzhongguo.crowd.config.rediskey.CrowdOrderKey;
import com.pmzhongguo.crowd.config.rediskey.CrowdOrderMemberIdKey;
import com.pmzhongguo.crowd.config.rediskey.CrowdProjectKey;
import com.pmzhongguo.crowd.crowdenum.CrowdOrderEnum;
import com.pmzhongguo.crowd.dao.CrowdOrderMapper;
import com.pmzhongguo.crowd.dto.CrowdOrderDto;
import com.pmzhongguo.crowd.entity.CrowdJob;
import com.pmzhongguo.crowd.entity.CrowdOrder;
import com.pmzhongguo.crowd.entity.CrowdProject;
import com.pmzhongguo.crowd.vo.CrowdOrderVo;
import com.pmzhongguo.ex.business.entity.Account;
import com.pmzhongguo.ex.business.entity.CoinRecharge;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 众筹项目订单服务
 * @date: 2019-03-03 10:15
 * @author: 十一
 */
@Transactional
@Service
public class CrowdOrderService {

    private static Logger logger = LoggerFactory.getLogger(CrowdOrderService.class);

    @Autowired
    private CrowdProjectService crowdProjectService;

    @Autowired
    private CrowdOrderMapper crowdOrderMapper;

    @Autowired
    private MemberService memberService;


    public Resp saveCrowdOrder(Member member, CrowdOrderVo crowdOrder, Account account, BigDecimal totalPrice,CrowdProject cacheCrowdProject) {

        // 构建订单实体
        CrowdOrder order = new CrowdOrder();
        order.setCurrency(cacheCrowdProject.getCurrency());
        String orderNo = member.getId() + "-" + System.currentTimeMillis() + HelpUtils.randomString(10);
        order.setMember_id(member.getId());
        order.setOrder_no(orderNo);
        order.setDone_time(HelpUtils.formatDate8(new Date()));
        order.setIs_lock(cacheCrowdProject.isIs_lock());
        order.setOper_ip(crowdOrder.getOperIp());
        order.setOrder_source(crowdOrder.getSource());
        order.setPrice(cacheCrowdProject.getRush_price());
        order.setQuote_currency(cacheCrowdProject.getQuote_currency());
        order.setVolume(crowdOrder.getBuy_amount());
        order.setAddr(crowdOrder.getAddress());
        order.setTotal_price(totalPrice);
        order.setCrd_pro_id(crowdOrder.getProject_id());

        // 查询购买人数
        Integer count =
                crowdOrderMapper.findCountMemberIdAndProjectId(HelpUtils.newHashMap("member_id",member.getId(),"crd_pro_id",cacheCrowdProject.getId()));

        // 拿到锁才可以操作
        long start = System.currentTimeMillis();
        String crowdOrderKeyPrefix = CrowdOrderKey.crowdOrderKey.getPrefix();
        boolean isLock = JedisUtil.getInstance().getLockRetry(crowdOrderKeyPrefix
                , IPAddressPortUtil.IP_ADDRESS, CrowdOrderKey.crowdOrderKey.getExpireSeconds(), 20, 100);
        if (isLock) {
            // 从数据库中获取购买信息
            CrowdProject crowdProject = crowdProjectService.findOrderInfoById(crowdOrder.getProject_id());

            if (crowdProject.getRemain_amount().compareTo(BigDecimal.ZERO) == 0 ||
                    crowdProject.getRemain_amount().compareTo(crowdOrder.getBuy_amount()) < 0) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_INSUFFICIENT_STOCK.getErrorENMsg());
            }
            // 再次判断用户资产
            Account dbAccount = memberService.getAccount(ImmutableMap.of("currency", account.getCurrency(),"member_id", account.getMember_id()));
            if ((dbAccount.getTotal_balance().subtract(dbAccount.getFrozen_balance())).compareTo(totalPrice) < 0) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorENMsg());
            }
            // 再次判断购买上限
            BigDecimal memberTotalPrice = findCurrencyAmountByMemberIdAndProjectId(
                    HelpUtils.newHashMap("member_id", member.getId()
                            , "crd_pro_id", crowdOrder.getProject_id()));
            if (memberTotalPrice != null) {
                BigDecimal plusTotalPrice = memberTotalPrice.add(crowdOrder.getBuy_amount().multiply(crowdProject.getRush_price()));
                if (plusTotalPrice.compareTo(cacheCrowdProject.getBuy_upper_limit()) > 0) {
                    return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_UPPER_AMOUNT.getErrorENMsg());
                }
            }

            try {
                // 保存订单
                crowdOrderMapper.insertCrowdOrder(order);

                // 更新用户账户信息
                account.setTotal_balance(account.getTotal_balance().subtract(totalPrice));
                memberService.updateAccount(account);

                // 更新项目币种的剩余数量和购买人数
                CrowdProject project = new CrowdProject();
                if (count == null || count < 1) {
                    project.setBuy_person_count(crowdProject.getBuy_person_count() + 1);
                }
                project.setRemain_amount(crowdProject.getRemain_amount().subtract(crowdOrder.getBuy_amount()));
                project.setId(crowdProject.getId());

                BigDecimal limit = crowdProject.getBuy_lower_limit().divide(crowdProject.getRush_price(),BigDecimal.ROUND_HALF_DOWN
                ,crowdProject.getP_precision());

                if(project.getRemain_amount().compareTo(limit) < 0) {
                    project.setRemain_amount(BigDecimal.ZERO);
                }
                // 更新剩余数量和购买人数
                crowdProjectService.updateCrowdProjectBuyInfo(project);


            }catch (Exception e) {
                logger.error("<===================================== 众筹项目下单异常" + e.getCause());
            }finally {
                // 释放锁
                boolean isRelease = JedisUtil.getInstance().releaseLock(crowdOrderKeyPrefix, IPAddressPortUtil.IP_ADDRESS);
                if (!isRelease) {
                    long end = System.currentTimeMillis();
                    logger.warn("release CrowdOrderKey fail 。key:{}  耗时 : {}  ms", crowdOrderKeyPrefix, end - start);
                }
            }

            crowdProject.setRemain_amount(crowdProject.getRemain_amount().subtract(crowdOrder.getBuy_amount()));
            String key = CrowdProjectKey.crowdProjectKey.getPrefix() + ":" + crowdProject.getId();
            JedisUtil.getInstance().set(key, crowdProject, false);
            // 重新设置下过期时间
            JedisUtil.getInstance().expire(key, CrowdProjectKey.expire);
            return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
        }

        return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SYSTEM_BUSY.getErrorENMsg());

    }

    public List<CrowdOrder> findOrderByMember(Map<String, Object> params) {

        return crowdOrderMapper.findMemberIdByPage(params);
    }

    public List<Map<String, Object>> findMgrByPage(Map<String, Object> params) {

        return crowdOrderMapper.findMgrByPage(params);
    }


    public BigDecimal findCurrencyAmountByMemberIdAndProjectId(Map map) {
        return crowdOrderMapper.findCurrencyAmountByMemberIdAndProjectId(map);
    }

    /**
     * 刷单，只能内部定时任务调用
     * @ job.getProject_id()     项目id
     * @ job.getOrder_volume() 多少个usdt
     * @ job.getStart_member_id() 用户起始id递增
     * @return
     */
    public ObjResp mockOrder(CrowdJob job) {
        Integer projectId = job.getProject_id();
        BigDecimal usdtVolume = job.getOrder_volume();
        // 先拿到锁
        int memberId = 0;
        long start = System.currentTimeMillis();
        String crowdOrderKeyPrefix = CrowdOrderKey.crowdOrderKey.getPrefix();
        boolean isLock = JedisUtil.getInstance().getLockRetry(crowdOrderKeyPrefix
                , IPAddressPortUtil.IP_ADDRESS, CrowdOrderKey.crowdOrderKey.getExpireSeconds(), 100, 30);
        if (isLock) {
            // 从数据库中获取购买信息
            CrowdProject crowdProject = crowdProjectService.findOrderInfoById(projectId);

            // 下单的数量 = usdt / 抢购价格
            BigDecimal orderVolume = usdtVolume.divide(crowdProject.getRush_price(),crowdProject.getP_precision(),BigDecimal.ROUND_HALF_DOWN);
            logger.info("<----------------- 下单数量：" + orderVolume + "\tUSDT数量：" + usdtVolume);
            if (crowdProject.getRemain_amount().compareTo(BigDecimal.ZERO) == 0 ||
                    crowdProject.getRemain_amount().compareTo(orderVolume) < 0) {
                return new ObjResp(ErrorInfoEnum.LANG_INSUFFICIENT_STOCK.getErrorCode(), ErrorInfoEnum.LANG_INSUFFICIENT_STOCK.getErrorENMsg(),ErrorInfoEnum.LANG_INSUFFICIENT_STOCK.getErrorENMsg());
            }
            memberId = getMemberId(job);
            try {

                // 构建订单实体
                CrowdOrder order = new CrowdOrder();
                order.setCurrency(crowdProject.getCurrency());
                String orderNo = memberId + "-" + System.currentTimeMillis() + HelpUtils.randomString(10);
                order.setMember_id(memberId);
                order.setOrder_no(orderNo);
                order.setDone_time(HelpUtils.formatDate8(new Date()));
                order.setIs_lock(crowdProject.isIs_lock());
                order.setOper_ip("127.0.0.1");
                order.setOrder_source(CrowdOrderEnum.API.getStatus());
                order.setPrice(crowdProject.getRush_price());
                order.setQuote_currency(crowdProject.getQuote_currency());
                order.setVolume(orderVolume);
                order.setAddr(CrowdOrderEnum.API.getStatus_info());
                order.setTotal_price(crowdProject.getRush_price().multiply(orderVolume)
                        .setScale(crowdProject.getP_precision(),BigDecimal.ROUND_HALF_DOWN));
                order.setCrd_pro_id(projectId);
                // 保存订单
                crowdOrderMapper.insertCrowdOrder(order);

                // 更新项目币种的剩余数量和购买人数
                CrowdProject project = new CrowdProject();

                Integer count =  crowdOrderMapper.findCountMemberIdAndProjectId(
                        HelpUtils.newHashMap("member_id",memberId,"crd_pro_id",crowdProject.getId()));

                if (count == null || count <= 1) {
                    project.setBuy_person_count(crowdProject.getBuy_person_count() + 1);
                }
                project.setRemain_amount(crowdProject.getRemain_amount().subtract(orderVolume));
                project.setId(crowdProject.getId());


                if(project.getRemain_amount().compareTo(orderVolume) < 0) {
                    logger.warn("<<<<<<<<<<<<<<<<<<<<<<<<<<<<< 最后清零数量：" + crowdProject.getRemain_amount() );
                    project.setRemain_amount(BigDecimal.ZERO);
                }
                // 更新剩余数量和购买人数
                crowdProjectService.updateCrowdProjectBuyInfo(project);
                // 更新MemberId
                JedisUtil.getInstance().incr(CrowdOrderMemberIdKey.crowdOrderMemberIdKey.getMemberIdKey(job.getJob_group(),job.getProject_id()));
            }catch (Exception e) {
                logger.error("<===================================== 众筹项目定时任务下单异常" + e.getCause());
            }finally {
                // 释放锁
                boolean isRelease = JedisUtil.getInstance().releaseLock(crowdOrderKeyPrefix, IPAddressPortUtil.IP_ADDRESS);
                if (!isRelease) {
                    long end = System.currentTimeMillis();
                    logger.warn("release CrowdOrderKey fail 。key:{}  耗时 : {}  ms", crowdOrderKeyPrefix, end - start);
                }
            }
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,memberId);
    }

    /**
     * 获取任务的key，job_group是唯一的，来保证key的唯一性
     * @param job
     * @return
     */
    private int getMemberId(CrowdJob job) {
        String memberIdKey = CrowdOrderMemberIdKey.crowdOrderMemberIdKey
                .getMemberIdKey(job.getJob_group(),job.getProject_id());
        Object objId = JedisUtil.getInstance().get(memberIdKey, true);
        if(objId == null) {
            JedisUtil.getInstance().set(memberIdKey,job.getStart_member_id()+"",true);
            return job.getStart_member_id();
        }
        return Integer.parseInt(objId.toString());


    }


    public void transfer2MemberAccount(List<CrowdOrderDto> crowdOrderList) {
        for (CrowdOrderDto crowdOrder : crowdOrderList) {
            if (crowdOrder.getIs_transfer() == 1) {
                continue;
            }
            CoinRecharge coinRecharge = new CoinRecharge();
            coinRecharge.setCurrency(crowdOrder.getCurrency());
            coinRecharge.setR_amount(crowdOrder.getVolume());
            coinRecharge.setR_address("IEO众筹");
            coinRecharge.setM_name((crowdOrder.getM_name()));
            coinRecharge.setMember_id(crowdOrder.getMember_id());
            coinRecharge.setR_guiji(1);
            coinRecharge.setIs_frozen(0);
            memberService.manAddCoinRecharge(coinRecharge);
            updateCrowdOrderTransferStatus(crowdOrder);
        }
    }

    private void updateCrowdOrderTransferStatus(CrowdOrderDto crowdOrder) {
        Map<String,Object> param = new HashMap<>();
        param.put("update_time",HelpUtils.formatDate8(new Date()));
        param.put("id",crowdOrder.getId());
        param.put("is_transfer",1);
        crowdOrderMapper.updateOrderStatusById(param);
    }
}
