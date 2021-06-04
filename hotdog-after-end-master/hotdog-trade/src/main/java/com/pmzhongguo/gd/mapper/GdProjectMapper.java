package com.pmzhongguo.gd.mapper;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.gd.entity.GdProject;
import com.pmzhongguo.gd.entity.GdProjectExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GdProjectMapper extends SuperMapper {
    long countByExample(GdProjectExample example);

    int deleteByExample(GdProjectExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GdProject record);

    int insertSelective(GdProject record);

    List<GdProject> selectByExampleWithBLOBs(GdProjectExample example);

    List<GdProject> selectByExample(GdProjectExample example);

    GdProject selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GdProject record, @Param("example") GdProjectExample example);

    int updateByExampleWithBLOBs(@Param("record") GdProject record, @Param("example") GdProjectExample example);

    int updateByExample(@Param("record") GdProject record, @Param("example") GdProjectExample example);

    int updateByPrimaryKeySelective(GdProject record);

    int updateByPrimaryKeyWithBLOBs(GdProject record);

    int updateByPrimaryKey(GdProject record);

    List<GdProject> queryGdListPage(Map map);
}