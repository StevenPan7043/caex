package com.zytx.common.mapper;

import com.zytx.common.entity.CChatInfo;
import com.zytx.common.entity.CChatInfoExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CChatInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_info
     *
     * @mbggenerated
     */
    int countByExample(CChatInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_info
     *
     * @mbggenerated
     */
    int deleteByExample(CChatInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_info
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_info
     *
     * @mbggenerated
     */
    int insert(CChatInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_info
     *
     * @mbggenerated
     */
    int insertSelective(CChatInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_info
     *
     * @mbggenerated
     */
    List<CChatInfo> selectByExample(CChatInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_info
     *
     * @mbggenerated
     */
    CChatInfo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_info
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CChatInfo record, @Param("example") CChatInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_info
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CChatInfo record, @Param("example") CChatInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CChatInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CChatInfo record);
}