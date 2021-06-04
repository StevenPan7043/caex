package com.pmzhongguo.ex.business.service;

import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.*;

@Service
public class MemberAuthRecordService extends BaseServiceSupport {
    private static  HashSet<Integer> recordSet = new HashSet();

    public boolean hasMemberAuthedBefore(Integer memberId){
        if (memberId == null || memberId < 0 ){
            return true;
        }
        try {
            if (CollectionUtils.isEmpty(recordSet)){
                recordSet = loadRecordSet();
            }
        }catch (Exception e){
            return true;
        }
        return recordSet.contains(memberId.intValue());
    }

    public HashSet loadRecordSet(){
        List<LinkedCaseInsensitiveMap> list = new ArrayList();
        HashSet<Integer> set = new HashSet();
        try {
            String sql = "select id from m_auth_identity_record";
            list = daoUtil.queryForList(sql);
        }catch (Exception e){
            logger.warn("query auth failed!");
        }
        for (LinkedCaseInsensitiveMap x: list) {
            set.add((Integer) x.get("id"));
        }
        return set;
    }
}
