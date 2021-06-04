package com.pmzhongguo.ex.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.contract.common.FunctionUtils;
import com.contract.dao.CWalletMapper;
import com.pmzhongguo.ex.business.dto.AccountHistoryDto;
import com.pmzhongguo.ex.business.dto.AccountHistoryExportDto;
import com.pmzhongguo.ex.business.dto.WalletExportDto;
import com.pmzhongguo.ex.business.mapper.AccountHistoryMapper;
import com.pmzhongguo.ex.business.mapper.ContractWalletMapper;
import com.pmzhongguo.ex.business.vo.CoinRechargeExcelVo;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.zzextool.utils.ExportExcel;
import com.pmzhongguo.zzextool.utils.HttpUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberBalanceService extends BaseServiceSupport {

    @Autowired
    private AccountHistoryMapper accountHistoryMapper;
    @Autowired
    private ContractWalletMapper contractWalletMapper;

    public List<AccountHistoryDto> everyTotalBalance(Map map) {
        List<AccountHistoryDto> accountHistoryDtos = accountHistoryMapper.queryAllHistoryPage(map);
        return accountHistoryDtos;
    }

    /**
     * 获取每个人每天的总资产
     * <p>
     * startTime 开始时间
     * endTime   结束时间
     * m_name    用户账号
     *
     * @return
     */
    public Map<String, List<AccountHistoryDto>> balanceHistoryDetails(Map map) {
        Map<String, List<AccountHistoryDto>> returnMap = new HashMap<>();
        //全仓和逐仓
        List<AccountHistoryDto> walletList = accountHistoryMapper.queryCWallentHistory(map);
        returnMap.put("wallet", walletList);
        //币币
        List<AccountHistoryDto> mAccountList = accountHistoryMapper.queryMAccountHistory(map);
        returnMap.put("mAccount", mAccountList);
        //法币
        List<AccountHistoryDto> oAccountList = accountHistoryMapper.queryOAccountHistory(map);
        returnMap.put("oAccount", oAccountList);
        return returnMap;
    }

    public Resp balanceHistoryExport(Map map, HttpServletResponse response) {
        String m_status = map.get("m_status").toString();
        List<AccountHistoryDto> list = new ArrayList<>();
        String fileName = "";
        switch (m_status) {
            case "0":
                //全仓和逐仓
                list = accountHistoryMapper.queryCWallentHistory(map);
                fileName = "合约资产记录-" + HelpUtils.getCurrTime();
                break;
            case "1":
                //币币
                list = accountHistoryMapper.queryMAccountHistory(map);
                fileName = "币币资产记录-" + HelpUtils.getCurrTime();
                break;
            case "2":
                //法币
                list = accountHistoryMapper.queryOAccountHistory(map);
                fileName = "法币资产记录-" + HelpUtils.getCurrTime();
                break;
        }
        for (AccountHistoryDto dto : list) {
            if (dto.getBalance() != null && dto.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
                dto.setBalance(BigDecimal.ZERO);
            }
            if (dto.getZcbalance() != null && dto.getZcbalance().compareTo(BigDecimal.ZERO) <= 0) {
                dto.setZcbalance(BigDecimal.ZERO);
            }
            if (dto.getTotal_balance() != null && dto.getTotal_balance().compareTo(BigDecimal.ZERO) <= 0) {
                dto.setTotal_balance(BigDecimal.ZERO);
            }
            if (dto.getFrozen_balance() != null && dto.getFrozen_balance().compareTo(BigDecimal.ZERO) <= 0) {
                dto.setFrozen_balance(BigDecimal.ZERO);
            }
        }
        List<AccountHistoryExportDto> exportList = JSON.parseArray(JSON.toJSONString(list), AccountHistoryExportDto.class);
        ExportExcel excel = new ExportExcel(fileName, AccountHistoryExportDto.class);
        excel.setDataList(exportList);
        try {
            excel.write(response, fileName + ".xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    public List<AccountHistoryDto> contractBalance(Map map) {
        return contractWalletMapper.selectWalletPage(map);
    }

    /**
     * 合约资产导出
     *
     * @param map
     * @return
     */
    public void contractBalanceExport(Map map, HttpServletResponse response) {
        String fileName = "合约资产表-" + HelpUtils.getCurrTime();
        ExportExcel excel = new ExportExcel(fileName, WalletExportDto.class);
        List<AccountHistoryDto> list = contractWalletMapper.selectWalletPage(map);
        for (AccountHistoryDto dto : list) {
            if (dto.getBalance() != null && dto.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
                dto.setBalance(BigDecimal.ZERO);
            }
            if (dto.getZcbalance() != null && dto.getZcbalance().compareTo(BigDecimal.ZERO) <= 0) {
                dto.setZcbalance(BigDecimal.ZERO);
            }
        }
        excel.setDataList(list);
        try {
            excel.write(response, fileName + ".xls");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前不同货币之间的比例
     */
    private Map<String, BigDecimal> currencyScale() {
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        String tickerStr = HttpUtils.get("https://hotdogvip.com:443/m/allticker/" + timeInMillis);
        JSONObject jsonObject = JSON.parseObject(tickerStr).getJSONObject("data");
        Map<String, BigDecimal> resultMap = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            String key1 = jsonObject.getJSONObject(key).getString("baseCurrencyName");
            BigDecimal value = jsonObject.getJSONObject(key).getBigDecimal("close");
            resultMap.put(key1, value);
        }
        return resultMap;
    }

    /**
     * 不同货币的资产转换成usdt货币，并计算出每个人每天的总资产
     *
     * @param historyList
     * @param scale
     * @return
     */
    private List<AccountHistoryDto> conversionToUSDT
    (List<AccountHistoryDto> historyList, Map<String, BigDecimal> scale) {
        for (AccountHistoryDto history : historyList) {
            history.setIdtime(history.getMember_id() + "_" + history.getRecord_date());
            String type = history.getCurrency();
            if (scale.get(type) != null) {
                //全仓余额
                if (history.getBalance() != null) {
                    BigDecimal mul = FunctionUtils.mul(history.getBalance(), scale.get(type), 6);
                    history.setTotalMoney(history.getTotalMoney() == null ? mul : FunctionUtils.add(history.getTotalMoney(), mul, 6));
                }
                //逐仓余额
                if (history.getZcbalance() != null) {
                    BigDecimal mul = FunctionUtils.mul(history.getZcbalance(), scale.get(type), 6);
                    history.setTotalMoney(history.getTotalMoney() == null ? mul : FunctionUtils.add(history.getTotalMoney(), mul, 6));
                }
                //币币或法币总余额
                if (history.getTotal_balance() != null) {
                    BigDecimal mul = FunctionUtils.mul(history.getTotal_balance(), scale.get(type), 6);
                    history.setTotalMoney(history.getTotalMoney() == null ? mul : FunctionUtils.add(history.getTotalMoney(), mul, 6));
                }
            }
        }
        Map<String, AccountHistoryDto> collectMap = historyList.stream().collect(Collectors.toMap(AccountHistoryDto::getIdtime, a -> a, (a, b) -> {
            if (a.getTotalMoney() == null) {
                return b;
            } else if (b.getTotalMoney() == null) {
                return a;
            } else {
                a.setTotalMoney(FunctionUtils.add(a.getTotalMoney(), b.getTotalMoney(), 6));
                return a;
            }
        }));
        return collectMap.values().stream().collect(Collectors.toList());
    }
}