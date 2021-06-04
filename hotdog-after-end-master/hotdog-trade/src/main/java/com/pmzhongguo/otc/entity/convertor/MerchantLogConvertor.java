package com.pmzhongguo.otc.entity.convertor;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.otc.entity.dataobject.MerchantLogDO;
import com.pmzhongguo.otc.entity.dto.MerchantLogDTO;
import com.pmzhongguo.otc.otcenum.AuditStatusEnum;
import com.pmzhongguo.otc.otcenum.WhetherEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MerchantLogConvertor {

    public static MerchantLogDO DTO2DO(MerchantLogDTO merchantDTO) {
        MerchantLogDO merchantDO = new MerchantLogDO();
        merchantDO.setId(merchantDTO.getId());
        merchantDO.setMemberId(merchantDTO.getMemberId());
        if (!HelpUtils.nullOrBlank(merchantDTO.getDepositCurrency())) {
            merchantDO.setDepositCurrency(merchantDTO.getDepositCurrency().toUpperCase());
        }
        merchantDO.setDepositVolume(merchantDTO.getDepositVolume());
        if (merchantDTO.getStatus() != null) {
            merchantDO.setStatus(merchantDTO.getStatus().getType());
        }
        merchantDO.setCreateTime(merchantDTO.getCreateTime());
        merchantDO.setModifyTime(merchantDTO.getModifyTime());
        merchantDO.setMemo(merchantDTO.getMemo());
        if (merchantDTO.getIsDeposit() != null) {
            merchantDO.setIsDeposit(merchantDTO.getIsDeposit().getType());
        }
        merchantDO.setLast_login_time(merchantDTO.getLast_login_time());
        return merchantDO;
    }

    public static MerchantLogDTO DO2DTO(MerchantLogDO merchantDO) {
        MerchantLogDTO merchantDTO = new MerchantLogDTO();
        merchantDTO.setId(merchantDO.getId());
        merchantDTO.setMemberId(merchantDO.getMemberId());
        merchantDTO.setDepositCurrency(merchantDO.getDepositCurrency());
        merchantDTO.setDepositVolume(merchantDO.getDepositVolume());
        merchantDTO.setStatus(AuditStatusEnum.getEnumByType(merchantDO.getStatus()));
        merchantDTO.setCreateTime(merchantDO.getCreateTime());
        merchantDTO.setModifyTime(merchantDO.getModifyTime());
        merchantDTO.setMemo(merchantDO.getMemo());
        merchantDTO.setIsDeposit(WhetherEnum.getEnumByType(merchantDO.getIsDeposit()));
        merchantDTO.setmName(merchantDO.getmName());
        merchantDTO.setLast_login_time(merchantDO.getLast_login_time());
        return merchantDTO;
    }

    public static List<MerchantLogDO> DTO2DO(List<MerchantLogDTO> dtoList) {
        List<MerchantLogDO> doList = new ArrayList<MerchantLogDO>();
        for (MerchantLogDTO m : dtoList) {
            doList.add(DTO2DO(m));
        }
        return doList;
    }

    public static List<MerchantLogDTO> DO2DTO(List<MerchantLogDO> doList) {
        List<MerchantLogDTO> dotList = new ArrayList<MerchantLogDTO>();
        for (MerchantLogDO m : doList) {
            dotList.add(DO2DTO(m));
        }
        return dotList;
    }

    /**
     * 将查询条件map里的枚举转换为枚举type
     *
     * @param param
     */
    public static void initMap(Map<String, Object> param) {

        if (!HelpUtils.isMapNullValue(param, "isDeposit")) {
            WhetherEnum whetherEnum = WhetherEnum.getWhetherEnum(String.valueOf(param.get("isDeposit")));
            if (whetherEnum != null) {
                param.put("isDeposit", whetherEnum.getType());
            }
        }

        if (!HelpUtils.isMapNullValue(param, "status")) {
            AuditStatusEnum auditStatusEnum = AuditStatusEnum.getAuditStatusEnum(String.valueOf(param.get("status")));
            if (auditStatusEnum != null) {
                param.put("status", auditStatusEnum.getType());
            }
        }

        if (!HelpUtils.isMapNullValue(param, "depositCurrency")) {
            param.put("depositCurrency", param.get("depositCurrency").toString().toUpperCase());
        }
    }

}
