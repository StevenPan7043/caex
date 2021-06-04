package com.pmzhongguo.ex.business.service.manager;

import com.contract.common.FunctionUtils;
import com.contract.common.StaticUtils;


import com.contract.entity.CContractOrderExample;
import com.contract.entity.CWallet;

import com.contract.enums.HandleTypeEnums;
import com.pmzhongguo.contract.contractenum.ContractOperateTypeEnum;
import com.pmzhongguo.contract.utils.ContractRedisLockConstants;
import com.pmzhongguo.ex.business.mapper.CContractOrderMapper;
import com.pmzhongguo.ex.business.mapper.ContractWalletMapper;
import com.pmzhongguo.ex.business.service.BipbService;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.OptSourceEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.otcenum.AccountOperateTypeEnum;
import com.pmzhongguo.otc.service.OTCAccountManager;
import com.pmzhongguo.otc.utils.OTCRedisLockConstants;
import com.pmzhongguo.zzextool.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class ContractAccountManager {

    protected Logger TradeLogger = LoggerFactory.getLogger("tradeInfo");

    @Autowired
    private MemberService memberService;

    @Autowired
    protected BipbService bipbService ;

    @Autowired
    private ContractWalletMapper cWalletMapper;

    @Autowired
    private OTCAccountManager oTCAccountManager;

    @Autowired
    private CContractOrderMapper cContractOrderMapper;

    /**
     * 	会员资产划转
     * @param memberId
     * @param currency
     * @param num
     * @return
     */
    public ObjResp accountTransfer(Integer memberId, String currency, BigDecimal num, ContractOperateTypeEnum operate) {
        ObjResp result = new ObjResp();
        String lockKey = ContractRedisLockConstants.CONTRACT_ACCOUNT_REDIS_LOCK_PRE + String.valueOf(memberId) + currency;
        boolean isLock = JedisUtil.getInstance().getLockRetry(lockKey, IPAddressPortUtil.IP_ADDRESS,
                ContractRedisLockConstants.CONTRACT_ACCOUNT_REDIS_LOCK_EXPIRE_TIME, ContractRedisLockConstants.REDIS_LOCK_RETRY_10, ContractRedisLockConstants.REDIS_LOCK_INTERVAL_100);
        if (isLock) {
            try {
                result = assetChange(memberId, currency, num, operate);
                memberService.accountProc(num.negate(), currency, memberId, 3, OptSourceEnum.CONTRACT);
            } finally {
                JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
            }
        } else {
            result = new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorENMsg(), lockKey + " Don`t get lock!");
        }
        failThrowException(result);
        return result;
    }



    /**
     * 	法币资产划转
     * @param memberId
     * @param currency
     * @param num
     * @return
     */
    public ObjResp otcAccountTransfer(Integer memberId, String currency, BigDecimal num, ContractOperateTypeEnum operate) {
        ObjResp result = new ObjResp();
        String lockKey = ContractRedisLockConstants.CONTRACT_ACCOUNT_REDIS_LOCK_PRE + String.valueOf(memberId) + currency;
        boolean isLock = JedisUtil.getInstance().getLockRetry(lockKey, IPAddressPortUtil.IP_ADDRESS,
                ContractRedisLockConstants.CONTRACT_ACCOUNT_REDIS_LOCK_EXPIRE_TIME, ContractRedisLockConstants.REDIS_LOCK_RETRY_10, ContractRedisLockConstants.REDIS_LOCK_INTERVAL_100);
        if (isLock) {
            try {
                switch (operate.getType()) {
                    case 105:
                    case 107:
                        result = oTCAccountManager.assetChange(memberId, currency, num.negate(), AccountOperateTypeEnum.OUT);
                        break;
                    case 106:
                    case 108:
                        result = oTCAccountManager.assetChange(memberId, currency, num.negate(), AccountOperateTypeEnum.INTO);
                        break;
                    default:
                        result.setState(Resp.FAIL);
                }
                if (!result.getState().equals(Resp.FAIL)){
                    result = assetChange(memberId, currency, num, operate);
                }
            } finally {
                JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
            }
        } else {
            result = new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorENMsg(), lockKey + " Don`t get lock!");
        }
        failThrowException(result);
        return result;
    }

    /**
     *
     * 全仓模式有订单时全仓不能转出
     * @param memberId
     * @return
     */
    public boolean checkOrderExist(Integer memberId){
        CContractOrderExample example=new CContractOrderExample();
        example.createCriteria().andCidEqualTo(memberId).andStatusEqualTo(1);
        int count=cContractOrderMapper.countByExample(example);
        return count > 0;
    }

    /**
     *
     *
     * @param memberId
     * @param currency
     * @param num
     * @param procType
     * @return
     */
    public ObjResp assetChange(Integer memberId, String currency, BigDecimal num, ContractOperateTypeEnum procType) {
        ObjResp result = verifyAccount(memberId, currency);

        if (result.getState().intValue() != Resp.SUCCESS.intValue()
                && procType.getType() != AccountOperateTypeEnum.ADDTOTAL.getType()
                && procType.getType() != AccountOperateTypeEnum.INTO.getType()
                && procType.getType() != AccountOperateTypeEnum.SECEDE.getType()) {
            return result;
        }
        String lockKey = OTCRedisLockConstants.OTC_ACCOUNT_REDIS_LOCK_PRE + String.valueOf(memberId) + "_" + currency;
        boolean isLock = JedisUtil.getInstance().getLockRetry(lockKey, IPAddressPortUtil.IP_ADDRESS,
                OTCRedisLockConstants.OTC_ACCOUNT_REDIS_LOCK_EXPIRE_TIME, OTCRedisLockConstants.REDIS_LOCK_RETRY_10, OTCRedisLockConstants.REDIS_LOCK_INTERVAL_100);
        ObjResp returnResult =new ObjResp();
        if (isLock) {
            try {
                String paycode;
                switch (procType.getType()) {
                    case 101:
                    case 105:
                         paycode= FunctionUtils.getOrderCode("I");
                        bipbService.handleCUsdtDetail(memberId, procType.getType(), StaticUtils.pay_in, paycode, num, procType.getCode(), memberId);
                        break;
                    case 102:
                    case 106:
                         paycode= FunctionUtils.getOrderCode("O");
                        bipbService.handleCUsdtDetail(memberId, procType.getType(), StaticUtils.pay_out, paycode, num.negate(), procType.getCode(), memberId);
                        break;
                    case 103:
                    case 107:
                        paycode= FunctionUtils.getOrderCode("I");
                        bipbService.handleCZcDetail(memberId,procType.getType(),StaticUtils.pay_in,paycode,num,procType.getCode(),memberId);
                        break;
                    case 104:
                    case 108:
                        paycode= FunctionUtils.getOrderCode("O");
                        bipbService.handleCZcDetail(memberId, procType.getType(), StaticUtils.pay_out, paycode, num.negate(), procType.getCode(), memberId);
                        break;
                    case 109:
                        paycode= FunctionUtils.getOrderCode("O");
                        bipbService.handleCUsdtDetail(memberId, procType.getType(), StaticUtils.pay_out, paycode, num, procType.getCode(), memberId);
                        paycode= FunctionUtils.getOrderCode("I");
                        bipbService.handleCZcDetail(memberId, procType.getType(), StaticUtils.pay_in, paycode, num, procType.getCode(), memberId);
                        break;
                    case 110:
                        paycode= FunctionUtils.getOrderCode("I");
                        bipbService.handleCUsdtDetail(memberId, procType.getType(), StaticUtils.pay_in, paycode, num, procType.getCode(), memberId);
                        paycode= FunctionUtils.getOrderCode("O");
                        bipbService.handleCZcDetail(memberId, procType.getType(), StaticUtils.pay_out, paycode, num, procType.getCode(),memberId);
                        break;
                    case 111:
                        paycode= FunctionUtils.getOrderCode("I");
                        bipbService.handleGdDetail(memberId, procType.getType(), StaticUtils.pay_in, paycode, num, procType.getCode(), memberId);
                        break;
                    case 112:
                        paycode= FunctionUtils.getOrderCode("O");
                        bipbService.handleGdDetail(memberId, procType.getType(), StaticUtils.pay_out, paycode, num.negate(), procType.getCode(), memberId);
                }
            } finally {
                JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
            }
        } else {
            returnResult = new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorENMsg(), lockKey + " Don`t get lock!");
        }
        return returnResult;
    }


    /**
     * 查询账户信息
     *
     * @param memberId
     * @param currency
     * @return State Resp.SUCCESS 查询到账户信息 Data保存账户信息 State Resp.FAIL 未能查询到账户信息
     */
    public ObjResp verifyAccount(Integer memberId, String currency) {
        boolean b = false;
        CWallet cWallet = cWalletMapper.selectByPrimaryKey(memberId, currency);
        ObjResp result = new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_NOT_EXIST.getErrorENMsg(), null);
        b = cWallet != null;
        if (b) {
            return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, cWallet);
        }
        return result;
    }


    /**
     *
     * @param resp
     */
    private void failThrowException(ObjResp resp) {
        if (resp.getState().intValue() != Resp.SUCCESS.intValue()) {
            TradeLogger.warn(resp.getMsg());
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorENMsg());
        }
    }
}
