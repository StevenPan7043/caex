package com.contract.app.task;

import com.contract.dao.CWalletHitstoryMapper;
import com.contract.dao.CWalletMapper;
import com.contract.dao.MAccountHitstoryMapper;
import com.contract.dao.OAccountHitstoryMapper;
import com.contract.entity.*;
import com.contract.service.redis.RedisUtilsService;
import com.contract.service.utils.DaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Daily
 * @date 2020/8/25 17:31
 */
@Component
public class AssetBackupTask {

    Logger logger = LoggerFactory.getLogger(AssetBackupTask.class);

    @Autowired
    private RedisUtilsService redisUtilsService;

    @Autowired
    private DaoUtil daoUtil;

    @Autowired
    private CWalletMapper cWalletMapper;

    @Autowired
    private CWalletHitstoryMapper cWalletHitstoryMapper;

    @Autowired
    private OAccountHitstoryMapper oAccountHitstoryMapper;

    @Autowired
    private MAccountHitstoryMapper mAccountHitstoryMapper;

    @Scheduled(cron="0 1 0 * * ?")
    public void cWalletBack(){
        String key = "AssetBackupTask_cWalletBack";
        boolean isLock = redisUtilsService.lock(key, 60 * 60 * 1000);
        if(isLock){
            try {
                doCWalletBack();
            } finally {
                redisUtilsService.deleteKey(key);
            }
        }
    }

    @Scheduled(cron="0 3 0 * * ?")
    public void oAccountBack(){
        String key = "AssetBackupTask_oAccountBack";
        boolean isLock = redisUtilsService.lock(key, 60 * 60 * 1000);
        if(isLock){
            try {
                doOAccountBack();
            } finally {
                redisUtilsService.deleteKey(key);
            }
        }
    }

    @Scheduled(cron="0 5 0 * * ?")
    public void mAccountBack(){
        String key = "AssetBackupTask_mAccountBack";
        boolean isLock = redisUtilsService.lock(key, 60 * 60 * 1000);
        if(isLock){
            try {
                doMAccountBack();
            } finally {
                redisUtilsService.deleteKey(key);
            }
        }
    }

    private String getYesterday(){
        Calendar currentC = Calendar.getInstance();
        currentC.setTime(new Date());
        currentC.add(Calendar.DAY_OF_YEAR, -1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String yesterday = formatter.format(currentC.getTime());
        return yesterday;
    }

    private void doCWalletBack(){
        String yesterday = getYesterday();
        CWalletExample example = new CWalletExample();
        List<CWallet> wallets = cWalletMapper.selectByExample(example);
        for(CWallet w : wallets){
            CWalletHitstory cWalletHitstory = new CWalletHitstory();
            BeanUtils.copyProperties(w, cWalletHitstory);
            cWalletHitstory.setRecordDate(yesterday);
            cWalletHitstory.setCreateDate(new Date());
            cWalletHitstoryMapper.insert(cWalletHitstory);
        }
    }

    private void doOAccountBack(){
        String yesterday = getYesterday();
        List<Map<String, Object>> maps = daoUtil.queryForList("SELECT * FROM `o_account`");
        for(Map<String, Object> map : maps){
            OAccountHitstory accountHitstory = new OAccountHitstory();
            accountHitstory.setCreateDate(new Date());
            accountHitstory.setCurrency(String.valueOf(map.get("currency")));
            accountHitstory.setFrozenBalance(new BigDecimal(String.valueOf(map.get("frozen_balance"))));
            accountHitstory.setMemberId(Integer.valueOf(String.valueOf(map.get("member_id"))));
            accountHitstory.setRecordDate(yesterday);
            accountHitstory.setTotalBalance(new BigDecimal(String.valueOf(map.get("total_balance"))));
            oAccountHitstoryMapper.insert(accountHitstory);
        }
    }

    private void doMAccountBack(){
        String yesterday = getYesterday();
        List<Map<String, Object>> maps = daoUtil.queryForList("SELECT * FROM `m_account`");
        for(Map<String, Object> map : maps){
            MAccountHitstory accountHitstory = new MAccountHitstory();
            accountHitstory.setCreateDate(new Date());
            accountHitstory.setCurrency(String.valueOf(map.get("currency")));
            accountHitstory.setFrozenBalance(new BigDecimal(String.valueOf(map.get("frozen_balance"))));
            accountHitstory.setMemberId(Integer.valueOf(String.valueOf(map.get("member_id"))));
            accountHitstory.setRecordDate(yesterday);
            accountHitstory.setTotalBalance(new BigDecimal(String.valueOf(map.get("total_balance"))));
            mAccountHitstoryMapper.insert(accountHitstory);
        }
    }
}
