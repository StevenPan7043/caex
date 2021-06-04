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
 * @date 2020/8/4 15:56
 */
@Component
public class ProjectSaleStatusScheduler {

    @Autowired
    private DaoUtil daoUtil;

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void setStart(){
        Date date = new Date();
        saleStart(date);
    }



    @Scheduled(cron = "0 0 0/1 * * ?")
    public void setEnd(){
        Date date = new Date();
        saleEnd(date);
    }

    public String saleStart(Date date){
        String query = "SELECT t.id, t.`sale_start_time` FROM `ipfs_project` t WHERE t.`sale_status` = '1';";
        List<Map<String, Object>> list = daoUtil.queryForList(query);
        if(CollectionUtils.isEmpty(list)){
            return "not data!";
        }
        String update = "update ipfs_project set sale_status = 2 where id = ?";
        try {
//            Date sysDate = HelpUtils.parse("2020-08-06 15:00:00", "yyyy-MM-dd HH:mm:ss");
            for(Map<String, Object> map : list){
                String startDateStr = String.valueOf(map.get("sale_start_time"));
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

    public String saleEnd(Date date){
        String query = "SELECT t.id, t.`sale_end_time` FROM `ipfs_project` t WHERE t.`sale_status` in ('2', '3');";
        List<Map<String, Object>> list = daoUtil.queryForList(query);
        if(CollectionUtils.isEmpty(list)){
            return "not data!";
        }
        String update = "update ipfs_project set sale_status = 4 where id = ?";
        try {
//            Date sysDate = HelpUtils.parse("2020-08-06 15:00:00", "yyyy-MM-dd HH:mm:ss");
            for(Map<String, Object> map : list){
                String startDateStr = String.valueOf(map.get("sale_end_time"));
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
