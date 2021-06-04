package com.contract.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.contract.common.DateUtil;
import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import com.contract.common.StaticUtils;
import com.contract.dao.*;
import com.contract.dto.CustomerDto;
import com.contract.dto.TeamNode;
import com.contract.entity.*;
import com.contract.enums.GdLevelEnum;
import com.contract.enums.HandleTypeEnums;
import com.contract.enums.SysParamsEnums;
import com.contract.service.BipbService;
import com.contract.service.UtilsService;
import com.contract.service.redis.RedisUtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GdService {

    private static final Logger logger = LoggerFactory.getLogger(GdService.class);

    @Autowired
    private GdProjectMapper gdProjectMapper;
    @Autowired
    private BipbService bipbService;
    @Autowired
    private UtilsService utilsService;
    @Autowired
    private GdBuyRecordMapper gdBuyRecordMapper;
    @Autowired
    private GdOutputMapper gdOutputMapper;
    @Autowired
    private GdUserBonusMapper gdUserBonusMapper;
    @Autowired
    private CCustomerMapper cCustomerMapper;
    @Autowired
    private RedisUtilsService redisUtilsService;
    @Autowired
    private GdTeamBonusMapper gdTeamBonusMapper;
    @Autowired
    private GdDetailMapper gdDetailMapper;

    /**
     * 购买跟单
     *
     * @param token
     * @param id
     * @return
     */
    public RestResponse buyGd(String token, Integer id) {

        CustomerDto customerDto = utilsService.getCusByToken(token);
        GdProject documentaryProject = gdProjectMapper.selectByPrimaryKey(id);
        if (documentaryProject == null) {
            return GetRest.getFail("跟单不存在");
        }
        GdDetailExample detailExample = new GdDetailExample();
        Date date = new Date();

        detailExample.createCriteria().andCidEqualTo(customerDto.getId()).andRemarkEqualTo("购买跟单").andCostEqualTo(documentaryProject.getPrice());
        List<GdDetail> gdDetails = gdDetailMapper.selectByExample(detailExample);
        if(gdDetails.size()>0){
            for(GdDetail gdDetail :gdDetails){

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String format1 = df.format(date);
                String format = df.format(gdDetail.getCreatetime());
                if(format1.equals(format)){
                    return GetRest.getFail("不可以重复下单");
                }
            }
        }

        /**
         * 资产变化
         */
        GdBuyRecord record = new GdBuyRecord();
        record.setMemberId(customerDto.getId());
        record.setNum(1);
        record.setCreateTime(new Date());
        record.setProjectId(documentaryProject.getId());
        record.setStatus("1");
        record.setOutputCurrency(documentaryProject.getOutputCurrency());
        record.setPrice(documentaryProject.getPrice());
        record.setTotal(documentaryProject.getPrice());
        record.setModifyTime(new Date());
        gdBuyRecordMapper.insertSelective(record);
        bipbService.handleGdDetail(record.getMemberId(), HandleTypeEnums.buygd.getId(), StaticUtils.pay_out, record.getId().toString(), record.getTotal(), "购买跟单", record.getMemberId());
        String orderkey = "gd_" + customerDto.getId() + "_" + documentaryProject.getOutputCurrency();
        redisUtilsService.setKey(orderkey, JSONObject.toJSONString(record));
        return GetRest.getSuccess("成功");
    }

    /**
     * 跟单日收益
     *
     * @param date
     */
    public void personReward(String date) {
        GdProjectExample example = new GdProjectExample();
        example.createCriteria().andRunStatusEqualTo("2");
        List<GdProject> documentaryProjects = gdProjectMapper.selectByExample(example);
        if (documentaryProjects.size() <= 0) {
            return;
        }
        BigDecimal mul = new BigDecimal("10");

        /**
         * 如果收益范围都相同走此逻辑
         */
        String key = redisUtilsService.getKey(SysParamsEnums.gd_scale.getKey());
        String[] split = key.split("-");
        int floor = new BigDecimal(split[0]).multiply(mul).intValue();
        int upper = new BigDecimal(split[1]).multiply(mul).intValue();
        Random rand = new Random();
        int bound = upper - floor + 1;
        int capacity = rand.nextInt(bound) + floor;//今日产出量
        BigDecimal out = BigDecimal.valueOf(capacity).divide(mul);

        Date now_date = new Date();
        Map<Integer, GdOutput> outputMap = new HashMap<>();//收益
        List<Integer> idList = new ArrayList<>();//跟单ID
        for (GdProject project : documentaryProjects) {
//            int floor = project.getOutputFloor().multiply(mul).intValue();
//            int upper = project.getOutputUpper().multiply(mul).intValue();
//            Random rand = new Random();
//            int bound = upper - floor + 1;
//            int capacity = rand.nextInt(bound) + floor;//今日产出量
//            BigDecimal out = BigDecimal.valueOf(capacity).divide(mul);

            GdOutput output = new GdOutput();
            output.setCapacity(out);
            output.setOutputCurrency(project.getOutputCurrency());
            output.setOutputDate(date);
            output.setProjectId(project.getId());
            output.setCreateTime(now_date);
            int i = gdOutputMapper.insertSelective(output);
            if (i < 1) {
                logger.warn("保存跟单日收益失败", JSON.toJSONString(output));
            } else {
                outputMap.put(project.getId(), output);
                idList.add(project.getId());
            }
        }

        GdBuyRecordExample buyRecordExample = new GdBuyRecordExample();
        buyRecordExample.createCriteria().andStatusEqualTo("1").andProjectIdIn(idList);
        //购买跟单记录
        List<GdBuyRecord> documentaryBuyRecords = gdBuyRecordMapper.selectByExample(buyRecordExample);
        if (documentaryBuyRecords.size() <= 0) {
            return;
        }
        Map<Integer, BigDecimal> rewardMap = new HashMap<>();//每个人的总奖励
        for (GdBuyRecord record : documentaryBuyRecords) {
            Date createTime = record.getCreateTime();
            long hms = DateUtil.getHMS(now_date, createTime);
            //下单超过12小时才会有收益
            if (hms < 12 * 60 * 60 * 1000) {
                continue;
            }
            GdUserBonus userBonus = new GdUserBonus();
            userBonus.setBonusDate(date);
            userBonus.setCreateTime(now_date);
            userBonus.setOutputCurrency(record.getOutputCurrency());
            userBonus.setProjectId(record.getProjectId());
            userBonus.setRecordId(record.getId());
            userBonus.setMemberId(record.getMemberId());
            userBonus.setBonusNum(outputMap.get(record.getProjectId()).getCapacity());
            //分完收益关闭
            record.setStatus("2");
            gdBuyRecordMapper.updateByPrimaryKeySelective(record);
            int i = gdUserBonusMapper.insertSelective(userBonus);
            if (i < 1) {
                logger.warn("保存跟单用户收益失败", JSON.toJSONString(userBonus));
            } else {
                if (rewardMap.get(record.getMemberId()) == null) {
                    rewardMap.put(record.getMemberId(), userBonus.getBonusNum());
                } else {
                    rewardMap.put(record.getMemberId(), userBonus.getBonusNum().add(rewardMap.get(record.getMemberId())));
                }
                //保存到用户资产表中
                bipbService.handleGdDetail(userBonus.getMemberId(), HandleTypeEnums.gdbouns.getId(), StaticUtils.pay_in, userBonus.getId().toString(), userBonus.getBonusNum().add(record.getTotal()), "跟单日收益和跟单金额", userBonus.getMemberId());
            }
        }
        inviteBonus(rewardMap);
    }

    public void teamReward() {
        Date date = DateUtil.dateAddInt(new Date(), -2);
        GdBuyRecordExample example = new GdBuyRecordExample();
        example.createCriteria().andCreateTimeGreaterThan(date).andStatusEqualTo("2");
        List<GdBuyRecord> gdBuyRecordList = gdBuyRecordMapper.selectByExample(example);//获取上次购买记录
        Map<Integer, BigDecimal> buyMap = new HashMap<>();//每个人的总消费
        for (GdBuyRecord record : gdBuyRecordList) {
            if (buyMap.get(record.getMemberId()) == null) {
                buyMap.put(record.getMemberId(), record.getTotal());
            } else {
                buyMap.put(record.getMemberId(), record.getTotal().add(buyMap.get(record.getMemberId())));
            }
        }

        Date date1 = DateUtil.dateAddInt(date, 1);
        String time = DateUtil.toDateString(date1);

        GdUserBonusExample example1 = new GdUserBonusExample();
        example1.createCriteria().andBonusDateEqualTo(time);
        //用户分红
        List<GdUserBonus> gdUserBonuses = gdUserBonusMapper.selectByExample(example1);
        Map<Integer, BigDecimal> bonusMap = new HashMap<>();//每个人的收益
        for (GdUserBonus bonus : gdUserBonuses) {
            if (bonusMap.get(bonus.getMemberId()) == null) {
                bonusMap.put(bonus.getMemberId(), bonus.getBonusNum());
            } else {
                bonusMap.put(bonus.getMemberId(), bonus.getBonusNum().add(bonusMap.get(bonus.getMemberId())));
            }

        }

        CCustomerExample cCustomerExample = new CCustomerExample();
        cCustomerExample.createCriteria().andParentidIsNull();
        List<CCustomer> cCustomers = cCustomerMapper.selectByExample(cCustomerExample);//获取非邀请注册的用户
        Map<Integer, TeamNode> nodeMap = new HashMap<>();//团队节点数
        List<Integer> idList = new ArrayList<>();//用户ID
        for (CCustomer customer : cCustomers) {
            TeamNode node = new TeamNode();
            node.setMemberId(customer.getId());
//            node.setBonus(buyMap.get(customer.getId()) == null ? BigDecimal.ZERO : buyMap.get(customer.getId()));
            node.setBonus(BigDecimal.ZERO);
            node.setRate(BigDecimal.ZERO);
            node.setCount(0);
            node.setStatus(0);
            nodeMap.put(customer.getId(), node);
            idList.add(customer.getId());
        }
        //循环查询，直到没有推荐用户为止
        while (true) {
            cCustomerExample.clear();
            cCustomerExample.createCriteria().andParentidIn(idList);
            cCustomers = cCustomerMapper.selectByExample(cCustomerExample);
            if (cCustomers.size() <= 0) {
                break;
            }
            idList.clear();
            for (CCustomer customer : cCustomers) {
                TeamNode node = new TeamNode();
                node.setMemberId(customer.getId());
//                node.setBonus(buyMap.get(customer.getId()) == null ? BigDecimal.ZERO : buyMap.get(customer.getId()));
                node.setBonus(BigDecimal.ZERO);
                node.setRate(BigDecimal.ZERO);
                node.setCount(0);
                node.setStatus(0);
                node.setParentId(customer.getParentid());
                nodeMap.put(customer.getId(), node);
                idList.add(customer.getId());
                //设置父级的子级，增加父级推荐人数
                TeamNode teamNode = nodeMap.get(customer.getParentid());
                teamNode.setCount(teamNode.getCount() + 1);
                teamNode.getChildId().add(customer.getId());
                nodeMap.put(customer.getParentid(), teamNode);
            }
        }

        //金额汇总
        for (Integer id : nodeMap.keySet()) {
            TeamNode parentNode = nodeMap.get(id);
            BigDecimal allBonus = BigDecimal.ZERO;//团队消费
            BigDecimal allRate = BigDecimal.ZERO;//团队收益
            List<Integer> childIdList = parentNode.getChildId();
            while (childIdList.size() > 0) {
                List<Integer> IdList = new ArrayList<>();
                for (Integer childId : childIdList) {
                    if (buyMap.get(childId) != null) {
                        if (nodeMap.get(childId).getStatus() == 1) {
                            allBonus.add(buyMap.get(childId)).add(nodeMap.get(childId).getBonus());
                            allRate.add(buyMap.get(childId)).add(nodeMap.get(childId).getRate());
                        } else {
                            allBonus.add(buyMap.get(childId));
                            allRate.add(buyMap.get(childId));
                            if (nodeMap.get(childId).getChildId().size() > 0) {
                                IdList.addAll(nodeMap.get(childId).getChildId());
                            }
                        }
                    }
                }
                childIdList = IdList;
            }
            nodeMap.get(id).setStatus(1);
            nodeMap.get(id).setBonus(allBonus);
            nodeMap.get(id).setRate(allRate);
        }

        for (Integer id : nodeMap.keySet()) {
            TeamNode node = nodeMap.get(id);
            if (node.getRate().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            GdLevelEnum level = getLevel(node.getCount(), node.getBonus());
            if (level != null) {
                GdTeamBonus teamBonus = new GdTeamBonus();
                teamBonus.setBonusDate(time);
                teamBonus.setCreateTime(new Date());
                teamBonus.setLevel(level.getLevel());
                teamBonus.setBonusRate(level.getScale());
                teamBonus.setMemberId(id);
                teamBonus.setSubBonusBase(node.getRate());
                teamBonus.setTeamBonus(node.getRate().multiply(level.getScale()));
                gdTeamBonusMapper.insertSelective(teamBonus);
                //保存到用户资产表中
                bipbService.handleGdDetail(node.getMemberId(), HandleTypeEnums.gdteam.getId(), StaticUtils.pay_in, teamBonus.getId().toString(), teamBonus.getTeamBonus(), "团队收益奖励", node.getMemberId());
                if (node.getParentId() != null) {
                    TeamNode parentNode = nodeMap.get(node.getParentId());
                    GdLevelEnum level1 = getLevel(parentNode.getCount(), parentNode.getBonus());
                    if (level1 != null && level1.compareTo(level) > 0) {
                        GdTeamBonus teamBonus1 = new GdTeamBonus();
                        teamBonus1.setBonusDate(time);
                        teamBonus1.setCreateTime(new Date());
                        teamBonus1.setLevel(level1.getLevel());
                        teamBonus1.setBonusRate(level1.getScale().subtract(level.getScale()));
                        teamBonus1.setMemberId(parentNode.getMemberId());
                        teamBonus1.setSubBonusBase(parentNode.getRate());
                        teamBonus1.setTeamBonus(parentNode.getRate().multiply(teamBonus1.getBonusRate()));
                        gdTeamBonusMapper.insertSelective(teamBonus);

                        bipbService.handleGdDetail(parentNode.getMemberId(), HandleTypeEnums.gdteam.getId(), StaticUtils.pay_in, teamBonus1.getId().toString(), teamBonus1.getTeamBonus(), "团队收益奖励", parentNode.getMemberId());
                    }
                }
            }
        }
    }


    /**
     * 推荐人奖励
     */
    private void inviteBonus(Map<Integer, BigDecimal> rewardMap) {
        Set<Integer> idSet = rewardMap.keySet();
        CCustomerExample example = new CCustomerExample();
        example.createCriteria().andIdIn(new ArrayList<>(idSet));
        List<CCustomer> cCustomers = cCustomerMapper.selectByExample(example);
        if (cCustomers.size() <= 0) {
            return;
        }
        for (CCustomer customer : cCustomers) {
            if (customer.getParentid() != null && customer.getParentid() != 0) {
                BigDecimal getReward = rewardMap.get(customer.getId()).multiply(BigDecimal.valueOf(0.2));//推荐人获取收益的20%
                //保存奥用户资产表
                bipbService.handleGdDetail(customer.getParentid(), HandleTypeEnums.gdbouns.getId(), StaticUtils.pay_in, customer.getId().toString(), getReward, "跟单日收益推荐人奖励", customer.getParentid());
            }
        }
    }

    /**
     * 判断等级
     *
     * @param count 人数
     * @param money 消费
     * @return
     */
    private GdLevelEnum getLevel(Integer count, BigDecimal money) {
        if (count >= 10 && money.compareTo(BigDecimal.valueOf(500000)) >= 0) {
            return GdLevelEnum.level3;
        } else if (count >= 6 && money.compareTo(BigDecimal.valueOf(100000)) >= 0) {
            return GdLevelEnum.level2;
        } else if (count >= 4 && money.compareTo(BigDecimal.valueOf(20000)) >= 0) {
            return GdLevelEnum.level1;
        }
        return null;
    }
}
