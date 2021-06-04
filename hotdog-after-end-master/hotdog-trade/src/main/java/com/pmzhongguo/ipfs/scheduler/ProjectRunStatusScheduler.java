package com.pmzhongguo.ipfs.scheduler;

import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Daily
 * @date 2020/8/4 16:17
 */
@Component
public class ProjectRunStatusScheduler {

    @Autowired
    private DaoUtil daoUtil;

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void setHashrateStart(){
        Date date = new Date();
        runStart(date);
    }

    public String runStart(Date date){
        String query = "SELECT t.id, t.`run_time` FROM `ipfs_project` t WHERE t.`run_status` = '1';";
        List<Map<String, Object>> list = daoUtil.queryForList(query);
        if(CollectionUtils.isEmpty(list)){
            return "not data!";
        }
        String update = "update ipfs_project set run_status = 2 where id = ?";
        try {
//            Date sysDate = HelpUtils.parse("2020-08-06 15:00:00", "yyyy-MM-dd HH:mm:ss");
            for(Map<String, Object> map : list){
                String startDateStr = String.valueOf(map.get("run_time"));
                Date saleDate = HelpUtils.parse(startDateStr, "yyyy-MM-dd HH:mm:ss");
                if(date.compareTo(saleDate) > -1){
                    int id = Integer.parseInt(String.valueOf(map.get("id")));
                    daoUtil.update(update, id);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  "succeed";
    }
}
