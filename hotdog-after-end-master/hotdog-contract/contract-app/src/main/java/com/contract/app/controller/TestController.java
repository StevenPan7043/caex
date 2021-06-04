package com.contract.app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.contract.app.task.GdScheduler;
import com.contract.common.*;
import com.contract.service.HuobiUtils;
import com.googlecode.jsonrpc4j.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web3j.crypto.Credentials;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.contract.dao.CContractOrderMapper;
import com.contract.dao.CZcOrderMapper;
import com.contract.dao.CoinsMapper;
import com.contract.dto.SymbolDto;
import com.contract.entity.CContractOrder;
import com.contract.entity.CZcOrder;
import com.contract.entity.Coins;
import com.contract.entity.CoinsExample;
import com.contract.enums.SysParamsEnums;
import com.contract.service.api.ApiTradeService;
import com.contract.service.redis.RedisUtilsService;
import com.contract.service.thead.HandleTimeoutTask;
import com.contract.service.thead.ThreadPoolConfig;
import com.contract.service.wallet.WalletService;
import com.contract.service.wallet.eth.ECR20Utils;
import com.contract.service.wallet.huobi.HuoBiUtils;


import com.image.common.RestUploadFileInfo;

@Controller
public class TestController {

    @Autowired
    private CoinsMapper coinsMapper;
    @Autowired
    private RedisUtilsService redisUtilsService;
    @Autowired
    private CContractOrderMapper cContractOrderMapper;
    @Autowired
    private ApiTradeService apiTradeService;
    @Autowired
    private ThreadPoolConfig threadPoolConfig;
    @Autowired
    private WalletService walletService;
    @Autowired
    private CZcOrderMapper cZcOrderMapper;
    @Autowired
    private HuobiUtils huobiUtils;
    @Autowired
    private GdScheduler scheduler;

    @RequestMapping(value = "/querySymbol")
    @ResponseBody
    public RestResponse querySymbol() {
        List<SymbolDto> coins = redisUtilsService.querySymDto();
        return GetRest.getSuccess("", coins);
    }

    /**
     * 查询用户所有持仓订单
     * @return
     */
    @RequestMapping(value = "/queryUserPosition")
    @ResponseBody
    public RestResponse queryUserPosition(Integer uid) {
        Object userPositionMes = getUserPositionMes(uid);
        return GetRest.getSuccess("", userPositionMes);
    }
    /**
     * 获取用户的所有持仓订单
     * @param uid
     * @return
     */
    private Object getUserPositionMes(Integer uid) {
        JSONObject positionMsg = new JSONObject();
        //统计用户逐仓持仓订单
        List<CZcOrder> zclist = new ArrayList<>();
        String zc_position = "zc_" + uid + "_*";
        Set<String> zcPositionKeySet = redisUtilsService.getKeys(zc_position);
        if (!CollectionUtils.isEmpty(zcPositionKeySet)) {
            for (String zcPositionKey : zcPositionKeySet) {
                String zkOrder = redisUtilsService.getKey(zcPositionKey);
                if (!StringUtils.isEmpty(zkOrder)) {
                    CZcOrder cZcOrder = JSONObject.parseObject(zkOrder, CZcOrder.class);
                    zclist.add(cZcOrder);
                }
            }
        }
        //统计用户全仓持仓订单
        List<CContractOrder> qcList = new ArrayList<>();
        String qc_position = "order_" + uid + "_*";
        Set<String> qcPositionKeySet = redisUtilsService.getKeys(qc_position);
        if (!CollectionUtils.isEmpty(qcPositionKeySet)) {
            for (String qcPositionKey : qcPositionKeySet) {
                String qcOrder = redisUtilsService.getKey(qcPositionKey);
                if (!StringUtils.isEmpty(qcOrder)) {
                    CContractOrder cContractOrder = JSONObject.parseObject(qcOrder, CContractOrder.class);
                    qcList.add(cContractOrder);
                }
            }
        }
        positionMsg.put("zcPosition", zclist);
        positionMsg.put("qcPosition", qcList);
        return positionMsg;
    }
    @RequestMapping(value = "/testRedis")
    @ResponseBody
    public RestResponse testRedis() {
        String key = "*order_*";
        Set<String> list = redisUtilsService.getKeys(key);
        System.out.println(redisUtilsService.getKey("order_0df317410cc6bb1ae15bfe2efd909165_BTC/USDT_U33179209189"));

        CContractOrder cContractOrder = cContractOrderMapper.selectByPrimaryKey(1);
        redisUtilsService.setKey("order_18025239306_BTC/USDT_U33179209189", JSONObject.toJSONString(cContractOrder));
        return GetRest.getSuccess("", list);
    }

    @RequestMapping(value = "/testTask")
    @ResponseBody
    public RestResponse testTask() {
        for (int i = 1; i < 30; i++) {
            threadPoolConfig.commitTask(new HandleTimeoutTask(null, "" + i, apiTradeService));
        }
        return GetRest.getSuccess("");
    }


    @RequestMapping(value = "/testSocket")
    @ResponseBody
    public RestResponse testSocket() {
        List<Coins> coins = redisUtilsService.queryCoins();
//		WebSocketClient.HBSocket(coins);
        return GetRest.getSuccess("");
    }

    @RequestMapping(value = "/getKline")
    @ResponseBody
    public RestResponse getKline() {
        try {
            CoinsExample coinsExample = new CoinsExample();
            coinsExample.setOrderByClause("sort asc");
            List<Coins> coins = coinsMapper.selectByExample(coinsExample);
            for (Coins s : coins) {
                String symbol = s.getSymbol().replace("_", "");
                List<com.huobi.response.Kline> list = HuoBiUtils.getInstance().queryKline(symbol, "1day", "1");
                if (list != null && list.size() > 0) {
                    com.huobi.response.Kline kline = list.get(0);
                    s.setOpenval(new BigDecimal(kline.getOpen()));
                    s.setUpdatetime(DateUtil.currentDate());
                    coinsMapper.updateByPrimaryKeySelective(s);
                }
                Thread.sleep(1000);
            }
            String coinarr = JSONArray.toJSONString(coins);
            redisUtilsService.setKey("coins_key", coinarr);
        } catch (Exception e) {

        }
        return GetRest.getSuccess("");
    }

    @RequestMapping(value = "/autoOrder")
    @ResponseBody
    public RestResponse autoOrder() {
//		SymbolDto symDto=redisUtilsService.getSymbolDto("BTC/USDT");
//		String depth_key ="BTC/USDT_depth";
//		String aa=redisUtilsService.getKey(depth_key);
//		return GetRest.getSuccess("",symDto,aa);
        walletService.autoOrder();
        return GetRest.getSuccess("");
    }

    @RequestMapping(value = "/deletekey")
    @ResponseBody
    public RestResponse deletekey() {
        Set<String> list = redisUtilsService.getKeys("*");
        for (String l : list) {
            redisUtilsService.deleteKey(l);
        }
        return GetRest.getSuccess("");
    }

    @RequestMapping(value = "/setcompany")
    @ResponseBody
    public RestResponse setcompany() {
        Set<String> list = redisUtilsService.getKeys("conmoney*");
        for (String l : list) {
            JSONObject jsonObject = JSONObject.parseObject(redisUtilsService.getKey(l));
            BigDecimal totalval = jsonObject.getBigDecimal("totalval");
            BigDecimal totalmoney = jsonObject.getBigDecimal("totalmoney");
            BigDecimal scale = FunctionUtils.div(totalval, totalmoney, 6);
            scale = FunctionUtils.mul(scale, new BigDecimal(100), 4);
            jsonObject.put("scale", scale);
            redisUtilsService.setKey(l, jsonObject.toJSONString());
        }
        return GetRest.getSuccess("");
    }

    @RequestMapping(value = "/createEth")
    @ResponseBody
    public RestResponse createEth() {
        String path = ECR20Utils.creatAccount("123456");// 创建钱包 密码是cid
        Credentials wallet = ECR20Utils.loadWallet(path, "123456");// 加载钱包 解析钱包地址秘钥
        String uuid = wallet.getAddress();// 钱包地址
        System.out.println(path);
        System.out.println(uuid);
        return GetRest.getSuccess(uuid, path);
    }

    @RequestMapping(value = "/removeZc")
    @ResponseBody
    public RestResponse removeZc() {
        Set<String> list = redisUtilsService.getKeys("*zc_*");
        for (String l : list) {
            String str = redisUtilsService.getKey(l);
            if (!StringUtils.isEmpty(str)) {
                CZcOrder cZcOrder = JSONObject.parseObject(str, CZcOrder.class);
                cZcOrder = cZcOrderMapper.getByOrdercode(cZcOrder.getOrdercode());
                if (FunctionUtils.isEquals(2, cZcOrder.getStatus())) {
                    redisUtilsService.deleteKey(l);
                }
            }
        }
        return GetRest.getSuccess("");
    }

    public static void main(String[] args) {
        try {
            String content = "http://localhost:8008/downLoad";
            RestUploadFileInfo r = com.image.common.Service.uploadQR(PathUtils.cus_qr, content);
            if (r.isStatus()) {
                System.out.println(r.getServiceName() + r.getFilePath() + r.getFileName());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @RequestMapping("/testHuobi")
    public SymbolDto testHuoBi(String coin){
        SymbolDto symbolDto = huobiUtils.getSymbolDto(coin);
        return symbolDto;
    }


    /**
     * 测试跟单收益
     */
    @RequestMapping("/testGrantUserBonus")
    public void grantUserBonus(){
        scheduler.grantUserBonus();
    }
}
