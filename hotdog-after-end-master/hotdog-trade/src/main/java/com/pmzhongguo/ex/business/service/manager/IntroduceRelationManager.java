package com.pmzhongguo.ex.business.service.manager;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.pmzhongguo.ex.business.entity.IntroduceRelation;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.service.IntroduceRelationService;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Daily
 * @date 2020/7/10 9:34
 */

@Service
public class IntroduceRelationManager {
    protected Logger logger = LoggerFactory.getLogger(IntroduceRelationManager.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private IntroduceRelationService introduceRelationService;

    @Autowired
    private DaoUtil daoUtil;

    /**
     * 生成一条用户关联关系记录
     * @param record
     * @return
     */
    public int create(IntroduceRelation record){
        return introduceRelationService.insert(record);
    }

    /**
     * 根据条件查询结果
     * @param record
     * @return
     */
    public List<IntroduceRelation> selectByCondition(IntroduceRelation record){
        return   introduceRelationService.selectByCondition(record);
    }

    /**
     * 获取所有上级memberId
     * @param memberId
     * @return
     */
    public Set<Integer> getParentid(Integer memberId){
        return introduceRelationService.getParentid(memberId);
    }

    /**
     * 获取所有后代的memberId
     * @param pid
     * @return
     */
    public Set<Integer> getosterity(Integer pid){
        return introduceRelationService.getosterity(pid);
    }

    /**
     * 获取memberId最顶层的推荐人id
     * @param memberId
     * @return
     */
    public Integer getTopParentId(Integer memberId){
        Integer id = memberId.intValue();
        while (true){
            Member member = memberService.getMemberById(id);
            if(isPidSelf(member)){
                return id;
            }
            id = member.getIntroduce_m_id().intValue();
        }
    }

    /**
     * 查询memberId的最接近的父id是否在Set里
     * @param memberId
     * @param set
     * @return
     */
    public String getParentid(Integer memberId, Set<String> set){
        Integer id = memberId.intValue();
        while (true){
            Member member = memberService.getMemberById(id);
            if(isPidSelf(member)){
                return "";
            }
            if(set.contains(member.getIntroduce_m_id().toString())){
                return member.getIntroduce_m_id().toString();
            }
            id = member.getIntroduce_m_id().intValue();
        }
    }

    /**
     *  添加该用户所有的上级关联关系
     * @param memberId
     * @return
     */
    public int addIntroduceRelation(Integer memberId){
        Integer id = memberId.intValue();
        int i = 0;
        while (true){
            Member member = memberService.getMemberById(id);
            if(isPidSelf(member)){
                logger.warn("用户：" + memberId + " 添加了 " + i + " 条上级关联关系记录！");
                return i;
            }
            IntroduceRelation record = new IntroduceRelation();
            record.setIntroduceMId(member.getIntroduce_m_id());
            record.setMemberId(memberId);
            introduceRelationService.insert(record);
            i++;
            id = member.getIntroduce_m_id().intValue();
        }
    }

    /**
     * 添加所有用户的上级关联关系
     * @return
     */
    public int addAllIntroduceRelation(){
        //为了预防万一，加锁
        String globalLockKey = "INTRODUCERELATIONMANAGER_ADDALLINTRODUCERELATION";
        boolean globalIsLock = JedisUtil.getInstance().getLock(globalLockKey, IPAddressPortUtil.IP_ADDRESS, 1000 * 60 * 60);
        int result = 0;
        if (globalIsLock) {
            try{
                int start = 0;
                int limit = 100;
                while (true) {
                    String query = "SELECT id FROM `m_member` m ORDER BY m.`id`  LIMIT ?, ?";
                    List<Map<String, Integer>> idList = daoUtil.queryForList(query, start, limit);
                    if(CollectionUtils.isEmpty(idList)){
                        return result;
                    }
                    result += idList.size();
                    for(Map<String, Integer> m : idList){
                        addIntroduceRelation(m.get("id"));
                    }
                    start += limit;
                }
            }catch (Exception e){
                e.printStackTrace();
            } finally {
                JedisUtil.getInstance().releaseLock(globalLockKey, IPAddressPortUtil.IP_ADDRESS);
            }
        }
        return result;
    }


    /**
     * 如果用户为空或者pid为空或者pid是自己
     * 说明没这个用户或者没上级
     * @param member
     * @return
     */
    private boolean isPidSelf(Member member){
        return  member == null || member.getIntroduce_m_id() == null || member.getIntroduce_m_id().intValue() == member.getId().intValue();
    }
}
