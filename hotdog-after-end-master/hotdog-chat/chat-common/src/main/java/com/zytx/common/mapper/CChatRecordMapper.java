package com.zytx.common.mapper;

import com.zytx.common.dto.ChatMessageDto;
import com.zytx.common.entity.CChatRecord;
import com.zytx.common.entity.CChatRecordExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CChatRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_record
     *
     * @mbggenerated
     */
    int countByExample(CChatRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_record
     *
     * @mbggenerated
     */
    int deleteByExample(CChatRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_record
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_record
     *
     * @mbggenerated
     */
    int insert(CChatRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_record
     *
     * @mbggenerated
     */
    int insertSelective(CChatRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_record
     *
     * @mbggenerated
     */
    List<CChatRecord> selectByExample(CChatRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_record
     *
     * @mbggenerated
     */
    CChatRecord selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_record
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CChatRecord record, @Param("example") CChatRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_record
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CChatRecord record, @Param("example") CChatRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_record
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CChatRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_chat_record
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CChatRecord record);

    /**
     * ??????????????????
     * @param map
     * @return
     */
    List<ChatMessageDto> queryChatRecord(Map<String,Object> map);
}