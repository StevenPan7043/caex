/**
 * zzex.com Inc.
 * Copyright (c) 2019/5/6 All Rights Reserved.
 */
package com.pmzhongguo.ex.transfer.service;



import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.pmzhongguo.ex.business.dto.WithdrawCreateDtoInner;
import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ex.core.service.BaseService;
import com.pmzhongguo.ex.core.utils.*;
import com.pmzhongguo.ex.core.web.Constants;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.OptSourceEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.transfer.entity.ThirdPartyInfo;
import com.pmzhongguo.ex.transfer.mapper.ThirdPartyMapper;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.qiniu.util.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author ：yukai
 * @date ：Created in 2019/5/6 14:56
 * @description：第三方划转项目方service
 * @version: $
 */
@Service
@Transactional
public class ThirdPartyService extends BaseService {

    protected Logger logger = LoggerFactory.getLogger(ThirdPartyService.class);

    @Autowired
    private ThirdPartyMapper thirdPartyMapper;

    @Autowired
    FundTransferLogManager fundTransferLogManager;


    @Autowired
    private MemberService memberService;

    public static Map<String, ThirdPartyInfo> thirdPartyInfoMap = Maps.newHashMap();

    public List<ThirdPartyInfo> findList(ThirdPartyInfo thirdPartyInfo)
    {
        return thirdPartyMapper.findList(thirdPartyInfo);
    }

    public List<ThirdPartyInfo> getAllThirdPartty(Map param)
    {
        return thirdPartyMapper.getAllThirdPartyPage(param);
    }

    /**
     * 根据ID 查询第三方信息
     *
     * @return
     */
    public ThirdPartyInfo selectByPrimaryKey(Integer id)
    {
        return thirdPartyMapper.selectByPrimaryKey(id);
    }

    /**
     * 先从内存读取，读取不到取redis中数据
     *
     * @param c_name
     * @return
     */
    public RespObj getThirdPartyInfoRespObj(String c_name) {
        if (thirdPartyInfoMap.containsKey(c_name)) {
            return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, thirdPartyInfoMap.get(c_name));
        }
        return this.findThirdPartyInfo(c_name);
    }

    /**
     * redis获取项目方信息
     *
     * @param c_name
     * @return
     */
    public RespObj findThirdPartyInfo(String c_name)
    {
        //校验项目方信息
        ThirdPartyInfo thirdPartyInfo = new ThirdPartyInfo();
        Map<String, ThirdPartyInfo> thirdPartyData = (Map<String, ThirdPartyInfo>) JedisUtil.getInstance().get(Constants.PROJECT_NAME_PRE +c_name+"_transfer", false);
        if (thirdPartyData == null)
        {
            thirdPartyData = Maps.newHashMap();
        }
        if (thirdPartyData.keySet().contains(c_name))
        {
            thirdPartyInfo = thirdPartyData.get(c_name);
        } else
        {
            thirdPartyInfo.setC_name(c_name);
            List<ThirdPartyInfo> thirdPartyInfoList = this.findList(thirdPartyInfo);
            if (thirdPartyInfoList == null || thirdPartyInfoList.size() == 0)
            {
                logger.warn("未获取到第三方信息: c_name=" + c_name);
                return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_THIRDPARTTY_NOT_EXIST.getErrorENMsg(), null);
            }
            thirdPartyInfo = thirdPartyInfoList.get(0);
            // 缓存项目方信息
            thirdPartyData.put(c_name, thirdPartyInfo);
            JedisUtil.getInstance().set(Constants.PROJECT_NAME_PRE + c_name + "_transfer", thirdPartyData, false);
            if (JedisUtil.getInstance().exists((Constants.PROJECT_NAME_PRE + c_name + "_transfer").getBytes())) {
                System.out.println(String.format("redis缓存set成功，key：%s,value:%s", Constants.PROJECT_NAME_PRE + c_name + "_transfer", new Gson().toJson(thirdPartyData)));
                logger.warn("redis缓存set成功，key：{},value:{}", Constants.PROJECT_NAME_PRE + c_name + "_transfer", new Gson().toJson(thirdPartyData));
            }
        }
        if (thirdPartyInfo == null)
        {
            logger.warn("未获取到第三方信息: c_name=" + c_name);
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_THIRDPARTTY_NOT_EXIST.getErrorENMsg(), null);
        }
        // 缓存可用币种信息
        List<String> deposit_currency_list = Lists.newArrayList();
        if (StringUtils.isNotEmpty(thirdPartyInfo.getCan_deposit_currency()))
        {
            deposit_currency_list = Arrays.asList(thirdPartyInfo.getCan_deposit_currency().split(","));
        }
        List<String> withdraw_currency_list = Lists.newArrayList();
        if (StringUtils.isNotEmpty(thirdPartyInfo.getCan_withdraw_currency()))
        {
            withdraw_currency_list = Arrays.asList(thirdPartyInfo.getCan_withdraw_currency().split(","));
        }
        thirdPartyInfo.setDeposit_currency_list(deposit_currency_list);
        thirdPartyInfo.setWithdraw_currency_list(withdraw_currency_list);
        if (!validatePrivilege(thirdPartyInfo))
        {
            logger.warn("权限不足: c_name=" + c_name);
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_SYSTEM_BUSY.getErrorENMsg(), null);
        }
//        logger.warn("获取缓存中的项目方信息,结果：{}",new Gson().toJson(thirdPartyInfo));
        return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, thirdPartyInfo);
    }

    /**
     * 校验项目方接口是否可用
     *
     * @param thirdPartyInfo
     * @return
     */
    public boolean validatePrivilege(ThirdPartyInfo thirdPartyInfo)
    {
        if (thirdPartyInfo.getC_flag() == 0)
        {
            return true;
        }
        return false;
    }

    /**
     * 更新或者插入项目方信息
     *
     * @param thirdPartyInfo
     * @return
     */
    public int addOrEditThirdParty(ThirdPartyInfo thirdPartyInfo)
    {
        // 缓存redis数据
        Map<String, ThirdPartyInfo> thirdPartyData = (Map<String, ThirdPartyInfo>) JedisUtil.getInstance().get(Constants.PROJECT_NAME_PRE +thirdPartyInfo.getC_name()+"_transfer", false);
        logger.warn("项目方，缓存数据：{}",new Gson().toJson(thirdPartyData));
        if (thirdPartyData == null)
        {
            thirdPartyData = Maps.newHashMap();
        }
        int result;
        if (thirdPartyInfo.getId() != null)
        {
            // 查询是否存在
            ThirdPartyInfo entity = thirdPartyMapper.selectByPrimaryKey(thirdPartyInfo.getId());
            if (entity == null)
            {
                throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_THIRDPARTTY_NOT_EXIST.getErrorENMsg());
            }
            // 选择性更新
            thirdPartyInfo.setUpdate_time(HelpUtils.formatDate8(new Date()));
            result = thirdPartyMapper.updateByPrimaryKeySelective(thirdPartyInfo);
            entity = thirdPartyMapper.selectByPrimaryKey(thirdPartyInfo.getId());
            entity.setUpdate_time(thirdPartyInfo.getUpdate_time());
            thirdPartyData.put(entity.getC_name(), entity);
        } else
        {
            //插入数据
            if (thirdPartyInfo.getC_name_type() == null) {
                thirdPartyInfo.setC_name_type(2);
            }
            thirdPartyInfo.setCreate_time(HelpUtils.formatDate8(new Date()));
            thirdPartyInfo.setUpdate_time(HelpUtils.formatDate8(new Date()));
            //生成apiKey 和 secretKey
            Map keyPair = KeySecretUtil.genKeyScrectPair();
            if (StringUtils.isEmpty(thirdPartyInfo.getS_apiKey())) {
                thirdPartyInfo.setS_apiKey(keyPair.get("api_key") + "");
            }
            if (StringUtils.isEmpty(thirdPartyInfo.getS_secretKey())) {
                thirdPartyInfo.setS_secretKey(keyPair.get("api_secret") + "");
            }
            result = thirdPartyMapper.insert(thirdPartyInfo);
            thirdPartyData.put(thirdPartyInfo.getC_name(), thirdPartyInfo);
        }
        if (result > 0) {
            logger.warn("项目方，更新缓存，更新参数：{}", new Gson().toJson(thirdPartyData));
            JedisUtil.getInstance().set(Constants.PROJECT_NAME_PRE + thirdPartyInfo.getC_name() + "_transfer", thirdPartyData, false);
        }
        return result;
    }

//    /**
//     * 删除项目方redis缓存key
//     *
//     */
//    public void delProRedisKeyName(String keyByte) {
//        if (JedisUtil.getInstance().exists(keyByte.getBytes())) {
//            byte[] bytes = JedisUtil.getInstance().getBytes(keyByte, false);
//            if (!BeanUtil.isEmpty(bytes)) {
//                JedisUtil.getInstance().zrem(keyByte.getBytes(), bytes);
//                logger.warn("项目方,删除redis缓存数据成功，key：{}", keyByte);
//            }
//        }
//    }
    /**
     * 根据ID 删除项目方信息
     *
     * @param id
     * @return
     */
    public int delThirdParty(Integer id) {
        List<ThirdPartyInfo> thirdPartyInfos = thirdPartyMapper.getAllThirdPartyPage(HelpUtils.newHashMap("id", id));
        if (!CollectionUtils.isEmpty(thirdPartyInfos)) {
            String c_name = thirdPartyInfos.get(0).getC_name();
            String redisKey = Constants.PROJECT_NAME_PRE + c_name + "_transfer";
            if (JedisUtil.getInstance().exists(redisKey.getBytes())) {
                JedisUtil.getInstance().delByteKey(redisKey);
                if (!JedisUtil.getInstance().exists(redisKey.getBytes())) {
                    logger.warn("删除redis缓存数据成功，key:{}", redisKey);
                    System.out.println("删除redis缓存数据成功，key");
                }
            }
        }
        return thirdPartyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public SuperMapper getMapper() {
        return thirdPartyMapper;
    }



    /**
     * 第三方提币
     */
    public ObjResp thirdPartyWithdraw(Member member, WithdrawCreateDtoInner withdrawCreateDto, List<ThirdPartyInfo> thirdPartyInfos, String ipAddr) throws BusinessException {


        if (!Arrays.asList(thirdPartyInfos.get(0).getCan_withdraw_currency().split(",")).contains(withdrawCreateDto.getCurrency().toLowerCase())) {
            return new ObjResp(Resp.FAIL, "无该币种提币权限", null);
        }

        ApiFundTransfer apiFundTransfer = new ApiFundTransfer();
        apiFundTransfer.setApi_key(thirdPartyInfos.get(0).getC_appKey());
        apiFundTransfer.setAddr(withdrawCreateDto.getAddr());
        apiFundTransfer.setCurrency(withdrawCreateDto.getCurrency().toLowerCase());
        apiFundTransfer.setTransferNum(withdrawCreateDto.getAmount().negate());
        apiFundTransfer.setSign_type("MD5");
        apiFundTransfer.setTimestamp((long) HelpUtils.getNowTimeStampInt());
        apiFundTransfer.setTransferType("");
        apiFundTransfer.setC_name(thirdPartyInfos.get(0).getC_name());
        apiFundTransfer.setTransferType("withdraw");
        ObjResp resp = (ObjResp) fundTransferLogManager.addFundTransferLog(apiFundTransfer, member, thirdPartyInfos.get(0).getC_name(), ipAddr, OptSourceEnum.getEnumByCode(thirdPartyInfos.get(0).getC_name()));
        if (resp.getState().equals(Resp.FAIL)) {
            return resp;
        }
        apiFundTransfer.setTransferNum(HelpUtils.stripTrailingZeros(withdrawCreateDto.getAmount()));
        apiFundTransfer.setTransferType("");
        apiFundTransfer.setTradeID(String.valueOf(resp.getData()));
        String sign = HelpUtils.createSign(HelpUtils.objToMap(apiFundTransfer), thirdPartyInfos.get(0).getS_secretKey());
        apiFundTransfer.setSign(sign);
        String url = thirdPartyInfos.get(0).getC_ip();
//        logger.warn("第三方提现url：【{}】,请求参数：{}", url, new Gson().toJson(apiFundTransfer));
        String result = HttpUtils.postWithJSON(url, JsonUtil.toJson(apiFundTransfer));
//        logger.warn("第三方提现函数url：【{}】,返回结果：{}", url, result);

        if (StringUtils.isBlank(result)) {
            logger.warn("第三方提现超时1，c_name:[{}],返回结果：{}", thirdPartyInfos.get(0).getC_name(), result);
            throw new BusinessException(Resp.FAIL, "connection timed out", null);
        }
        ObjResp objResp = JSON.parseObject(result, ObjResp.class);
        if (objResp == null) {
            logger.warn("第三方提现超时2，c_name:[{}],返回结果：{}", thirdPartyInfos.get(0).getC_name(), result);
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CLIENT_STATUS.getErrorCNMsg(), null);
        }
        if (objResp.getState().equals(Resp.FAIL)) {
            logger.warn("第三方提现异常，c_name:[{}],返回结果：{}", thirdPartyInfos.get(0).getC_name(), result);
            throw new BusinessException(Resp.FAIL, objResp.getMsg(), objResp.getData() + "");
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }


    /**
     * 项目方账户提币归集
     *
     * @param timeStart 开始时间
     * @param timeEnd   结束时间
     * @param c_name    项目方标识
     */
    public void execute(String timeStart, String timeEnd, String c_name) {
        //todo 查询项目方信息
        //todo 遍历，判断项目方信息是否有账户UID，若有对支持提现币种进行归集。若无，执行下一条
        List<ThirdPartyInfo> thirdPartyInfos = thirdPartyMapper.getAllThirdPartyPage(HelpUtils.newHashMap("c_name", c_name));
        for (ThirdPartyInfo info : thirdPartyInfos) {
            if (BeanUtil.isEmpty(info.getExt())) {
                continue;
            }
            String join = "'" + String.join("','", info.getCan_withdraw_currency().toUpperCase().split(",")) + "'";
            List<CoinWithdraw> coinWithdraws = memberService.getSumAmountGroupByMemberId(HelpUtils.newHashMap("currency", join, "create_time_start", timeStart, "create_time_end", timeEnd));
            if (CollectionUtils.isEmpty(coinWithdraws)) {
                continue;
            }
            logger.warn("项目方账户提币归集开始,c_name:{},归集币种：{}", info.getC_name(), join);
            for (CoinWithdraw coinWithdraw : coinWithdraws) {
                memberService.accountProc(coinWithdraw.getW_amount(), coinWithdraw.getCurrency().toUpperCase(), Integer.valueOf(info.getExt()), 3, OptSourceEnum.getEnumByCode(info.getC_name().toUpperCase()));
            }
            logger.warn("项目方账户提币归集结束,c_name:{},归集币种：{}", info.getC_name(), join);
            logger.warn("******************************************************");
        }
    }
}
