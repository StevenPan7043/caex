package com.pmzhongguo.ex.business.service;

import com.contract.common.FunctionUtils;
import com.contract.common.StaticUtils;


import com.contract.entity.CUsdtDetail;
import com.contract.entity.CWallet;
import com.contract.entity.CZcDetail;
import com.contract.entity.GdDetail;
import com.contract.enums.CoinEnums;
import com.contract.enums.HandleTypeEnums;
import com.contract.exception.ThrowJsonException;
import com.pmzhongguo.ex.business.mapper.CUsdtDetailMapper;
import com.pmzhongguo.ex.business.mapper.CZcDetailMapper;
import com.pmzhongguo.ex.business.mapper.ContractWalletMapper;
import com.pmzhongguo.ex.business.mapper.GdDetailMapper;
import common.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BipbService {

    private static Logger logger = Logger.getLogger(com.contract.service.BipbService.class);
    @Autowired
    private ContractWalletMapper cWalletMapper;
    @Autowired
    private CUsdtDetailMapper cUsdtDetailMapper;
    @Autowired
    private RedisUtilsService redisUtilsService;
    @Autowired
    private CZcDetailMapper cZcDetailMapper;
    @Autowired
    private GdDetailMapper gbDetailMapper;

    /**
     * 保存用户金额明细
     *
     * @param cid     会员id
     * @param typeid  操作类型
     * @param isout   收入/支出
     * @param paycode 交易流水号
     * @param cost    操作金额
     * @param remark  备注
     * @return
     */
    public void handleCUsdtDetail(Integer cid, Integer typeid, Integer isout, String paycode
            , BigDecimal cost, String remark, Integer targetid) {

        if(remark.equals("addBalance")){
            remark = "币币转合约";
        }
        if(remark.equals("outBalance")){
            remark = "合约转币币";
        }
        if(remark.equals("otcAddBalance")){
            remark = "法币转合约";
        }
        if(remark.equals("otcOutBalance")){
            remark = "合约转法币";
        }
        CWallet wallet = cWalletMapper.selectByPrimaryKey(cid, CoinEnums.USDT.name());
        BigDecimal originalPoint = wallet.getBalance();
        BigDecimal point = wallet.getBalance();
        if (FunctionUtils.isEquals(StaticUtils.pay_in, isout)) {
            point = FunctionUtils.add(point, cost, 6);
        } else {
            if (!FunctionUtils.isEquals(HandleTypeEnums.orderout.getId(), typeid)
                    && !FunctionUtils.isEquals(HandleTypeEnums.order_tax.getId(), typeid)) {
                if (cost.compareTo(originalPoint) > 0) {
                    throw new ThrowJsonException("USDT不足");
                }
            } else {
                //如果是平仓 那么如果当前扣除金额大于原始金额 那么全部扣除
                if (cost.compareTo(originalPoint) > 0) {
                    cost = originalPoint;
                }
            }
            point = FunctionUtils.sub(point, cost, 6);
        }
        wallet.setBalance(point);
        //首先修改余额 根据版本号乐观锁
        int i = cWalletMapper.updateMoneyLockVersion(wallet);
        if (i < 1) {
            throw new ThrowJsonException("重复操作回滚");
        }
        redisUtilsService.setKey(cid + "_banlance", wallet.getBalance().toPlainString());
        CUsdtDetail details = new CUsdtDetail();
        details.setCid(cid);
        details.setTypeid(typeid);
        details.setPaycode(paycode);
        details.setIsout(isout);
        details.setOriginal(originalPoint);
        details.setCost(cost);
        details.setLast(point);
        details.setRemark(remark);
        details.setTargetid(targetid);
        cUsdtDetailMapper.insertSelective(details);
    }

    /**
     * 保存用户逐仓金额明细
     *
     * @param cid     会员id
     * @param typeid  操作类型
     * @param isout   收入/支出
     * @param paycode 交易流水号
     * @param cost    操作金额
     * @param remark  备注
     * @return
     */
    public void handleCZcDetail(Integer cid, Integer typeid, Integer isout, String paycode
            , BigDecimal cost, String remark, Integer targetid) {
        CWallet wallet = cWalletMapper.selectByPrimaryKey(cid, CoinEnums.USDT.name());
        BigDecimal originalPoint = wallet.getZcbalance();
        BigDecimal point = wallet.getZcbalance();
        if (FunctionUtils.isEquals(StaticUtils.pay_in, isout)) {
            point = FunctionUtils.add(point, cost, 6);
        } else {
            if (cost.compareTo(originalPoint) > 0) {
                throw new ThrowJsonException("逐仓资金不足");
            }
            point = FunctionUtils.sub(point, cost, 6);
        }
        wallet.setZcbalance(point);
        //首先修改余额 根据版本号乐观锁
        int i = cWalletMapper.updateMoneyLockVersion(wallet);
        if (i < 1) {
            throw new ThrowJsonException("重复操作回滚");
        }
        CZcDetail details = new CZcDetail();
        details.setCid(cid);
        details.setTypeid(typeid);
        details.setPaycode(paycode);
        details.setIsout(isout);
        details.setOriginal(originalPoint);
        details.setCost(cost);
        details.setLast(point);
        details.setRemark(remark);
        details.setTargetid(targetid);
        cZcDetailMapper.insertSelective(details);
    }

    /**
     * 保存用户跟单金额明细
     *
     * @param cid     会员id
     * @param typeid  操作类型
     * @param isout   收入/支出
     * @param paycode 交易流水号
     * @param cost    操作金额
     * @param remark  备注
     * @return
     */
    public void handleGdDetail(Integer cid, Integer typeid, Integer isout, String paycode
            , BigDecimal cost, String remark, Integer targetid) {
        CWallet wallet = cWalletMapper.selectByPrimaryKey(cid, CoinEnums.USDT.name());
        if(remark.equals("addGdBalance")){
            remark = "币币转跟单";
        }
        if(remark.equals("outGdBalance")){
            remark = "跟单转币币";
        }
        BigDecimal originalPoint = wallet.getGdbalance();
        BigDecimal point = wallet.getGdbalance();
        if (FunctionUtils.isEquals(StaticUtils.pay_in, isout)) {
            point = FunctionUtils.add(point, cost, 6);
        } else {
            if (cost.compareTo(originalPoint) > 0) {
                throw new ThrowJsonException("资金不足");
            }
            point = FunctionUtils.sub(point, cost, 6);
        }
        wallet.setGdbalance(point);
        //首先修改余额 根据版本号乐观锁
        int i = cWalletMapper.updateMoneyLockVersion(wallet);
        if (i < 1) {
            throw new ThrowJsonException("重复操作回滚");
        }
        GdDetail detail = new GdDetail();
        detail.setCid(cid);
        detail.setTypeid(typeid);
        detail.setPaycode(paycode);
        detail.setIsout(isout);
        detail.setOriginal(originalPoint);
        detail.setCost(cost);
        detail.setLast(point);
        detail.setRemark(remark);
        detail.setTargetid(targetid);
        gbDetailMapper.insertSelective(detail);
    }
}
