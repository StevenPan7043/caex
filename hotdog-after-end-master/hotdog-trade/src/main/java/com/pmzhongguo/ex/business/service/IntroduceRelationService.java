package com.pmzhongguo.ex.business.service;

import com.pmzhongguo.ex.business.entity.IntroduceRelation;
import com.pmzhongguo.ex.business.mapper.IntroduceRelationMapper;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.zzextool.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Daily
 * @date 2020/7/9 16:20
 */
@Service
public class IntroduceRelationService {

    @Resource
    private IntroduceRelationMapper introduceRelationMapper;

    /**
     * 插入一条记录
     * @param record
     * @return
     */
    public int insert(IntroduceRelation record){
        if(StringUtil.isNullOrBank(record.getCreateTime())){
            record.setCreateTime(HelpUtils.formatDate8(new Date()));
        }
        return introduceRelationMapper.insertSelective(record);
    }

    /**
     * 根据条件查询结果
     * @param record
     * @return
     */
    public List<IntroduceRelation> selectByCondition(IntroduceRelation record){
        return   introduceRelationMapper.selectByCondition(record);
    }

    /**
     * 获取所有上级memberId
     * @param memberId
     * @return
     */
    public Set<Integer> getParentid(Integer memberId){
        IntroduceRelation record = new IntroduceRelation();
        record.setMemberId(memberId);
        List<IntroduceRelation> list = selectByCondition(record);
        Set<Integer> set = new HashSet<Integer>();
        for(IntroduceRelation i : list){
            set.add(i.getIntroduceMId());
        }
        return set;
    }

    /**
     * 获取所有后代的memberId
     * @param memberId
     * @return
     */
    public Set<Integer> getosterity(Integer memberId){
        IntroduceRelation record = new IntroduceRelation();
        record.setIntroduceMId(memberId);
        List<IntroduceRelation> list = selectByCondition(record);
        Set<Integer> set = new HashSet<Integer>();
        for(IntroduceRelation i : list){
            set.add(i.getMemberId());
        }
        return set;
    }


}
