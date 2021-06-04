package com.pmzhongguo.gd.service;


import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.gd.entity.GdProject;
import com.pmzhongguo.gd.mapper.GdProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GdService {

    @Autowired
    private GdProjectMapper gdProjectMapper;


    public List<GdProject> queryGdListPage(Map params){
        return gdProjectMapper.queryGdListPage(params);
    }

    public Resp insert(GdProject gdProject){
        int i = gdProjectMapper.insertSelective(gdProject);
        if(i > 0){
            return new Resp(Resp.SUCCESS,"添加成功");
        }else{
            return new Resp(Resp.FAIL,"添加失败");
        }
    }

    public GdProject selectById(Integer id){
       return gdProjectMapper.selectByPrimaryKey(id);
    }

    public Resp update(GdProject gdProject){
       int i = gdProjectMapper.updateByPrimaryKeySelective(gdProject);
       if(i > 0){
           return new Resp(Resp.SUCCESS,"修改成功");
       }else{
           return new Resp(Resp.FAIL,"修改失败");
       }
    }
}
