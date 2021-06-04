package com.pmzhongguo.ex.business.mapper;

import com.pmzhongguo.ex.business.entity.IntroduceRelation;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.util.List;

public interface IntroduceRelationMapper extends SuperMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IntroduceRelation record);

    int insertSelective(IntroduceRelation record);

    IntroduceRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IntroduceRelation record);

    int updateByPrimaryKey(IntroduceRelation record);

    List<IntroduceRelation> selectByCondition(IntroduceRelation record);
}