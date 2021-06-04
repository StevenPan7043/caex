package com.contract.service.cms;


import com.contract.dao.GdBuyRecordMapper;
import com.contract.dao.GdDetailMapper;
import com.contract.dao.GdTeamBonusMapper;
import com.contract.dao.GdUserBonusMapper;
import com.contract.entity.GdBuyRecord;
import com.contract.entity.GdDetail;
import com.contract.entity.GdTeamBonus;
import com.contract.entity.GdUserBonus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CycleService {


    @Autowired
    private GdBuyRecordMapper gdBuyRecordMapper;
    @Autowired
    private GdUserBonusMapper gdUserBonusList;
    @Autowired
    private GdTeamBonusMapper gdTeamBonusMapper;
    @Autowired
    private GdDetailMapper gdDetailMapper;

    public List<GdBuyRecord> gdByRecordList(GdBuyRecord gdBuyRecord){
        return gdBuyRecordMapper.queryBuyRecordList(gdBuyRecord);
    }


    public List<GdUserBonus> gdUserBonusList(GdUserBonus gdUserBonus){
        return gdUserBonusList.gdUserBonusList(gdUserBonus);
    }

    public List<GdTeamBonus> gdTeamBonusList(GdTeamBonus gdTeamBonus){
        return gdTeamBonusMapper.gdTeamBonusList(gdTeamBonus);
    }

    public List<GdDetail> gdDetailList(GdDetail gdDetail){
       return gdDetailMapper.gdDetailList(gdDetail);
    }
}
