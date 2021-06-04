package com.contract.service.cms;

import com.contract.dao.*;
import com.contract.dto.ReportDto;
import com.contract.entity.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderService {

	@Autowired
	private MtCliqueUserMapper cliqueUserMapper;
	@Autowired
	private CContractOrderMapper cContractOrderMapper;
	@Autowired
	private CCustomerMapper cCustomerMapper;
	@Autowired
	private CEntrustOrderMapper cEntrustOrderMapper;
	@Autowired
	private CZcOrderMapper cZcOrderMapper;
	
	public List<CContractOrder> queryOrderList(CContractOrder order) {
		if(!StringUtils.isEmpty(order.getParentname())) {
			CCustomer cus=cCustomerMapper.getByPhone(order.getParentname());
			if(cus!=null) {
				order.setParentid(cus.getId());
			}else {
				order.setParentid(-1);
			}
		}
		PageHelper.startPage(order.getPage(),order.getRows());
		List<CContractOrder> list=cContractOrderMapper.queryOrderList(order);
		for(CContractOrder l:list) {
			if(l.getUserid()!=null) {
				MtCliqueUser cliqueUser=cliqueUserMapper.selectByPrimaryKey(l.getUserid());
				if(cliqueUser!=null) {
					l.setUsername(cliqueUser.getLogin());
				}
			}
		}
		return list;
	}
	
//	public ReportDto getOrdermoney(CContractOrder order){
//		if(!StringUtils.isEmpty(order.getParentname())) {
//			CCustomer cus=cCustomerMapper.getByPhone(order.getParentname());
//			if(cus!=null) {
//				order.setParentid(cus.getId());
//			}else {
//				order.setParentid(-1);
//			}
//		}
//		ReportDto map=cContractOrderMapper.getOrderMoney(order);
//		return map;
//	}

	public ReportDto getOrderReport(CContractOrder order) {
		return cContractOrderMapper.getOrderMoney(order);
	}
	
	public ReportDto getOrderReport1(CZcOrder order) {
		return cZcOrderMapper.getZcMoney(order);
	}

	public List<CEntrustOrder> queryEntrustList(CEntrustOrder cEntrustOrder) {

		if(!StringUtils.isEmpty(cEntrustOrder.getParentname())) {
			CCustomer cus=cCustomerMapper.getByPhone(cEntrustOrder.getParentname());
			if(cus!=null) {
				cEntrustOrder.setParentid(cus.getId());
			}else {
				cEntrustOrder.setParentid(-1);
			}
		}
		PageHelper.startPage(cEntrustOrder.getPage(),cEntrustOrder.getRows());
		List<CEntrustOrder> list=cEntrustOrderMapper.queryList(cEntrustOrder);
		for(CEntrustOrder l:list) {
			if(l.getUserid()!=null) {
				MtCliqueUser cliqueUser=cliqueUserMapper.selectByPrimaryKey(l.getUserid());
				if(cliqueUser!=null) {
					l.setUsername(cliqueUser.getLogin());
				}
			}
		}
		return list;
	
	}

	public List<CZcOrder> queryZcList(CZcOrder zcOrder) {
		if(!StringUtils.isEmpty(zcOrder.getParentname())) {
			CCustomer cus=cCustomerMapper.getByPhone(zcOrder.getParentname());
			if(cus!=null) {
				zcOrder.setParentid(cus.getId());
			}else {
				zcOrder.setParentid(-1);
			}
		}
		PageHelper.startPage(zcOrder.getPage(),zcOrder.getRows());
		List<CZcOrder> list=cZcOrderMapper.queryZCList(zcOrder);
		for(CZcOrder l:list) {
			if(l.getUserid()!=null) {
				MtCliqueUser cliqueUser=cliqueUserMapper.selectByPrimaryKey(l.getUserid());
				if(cliqueUser!=null) {
					l.setUsername(cliqueUser.getLogin());
				}
			}
		}
		return list;
	}

	public ReportDto getZCmoney(CZcOrder zcOrder) {
		return cZcOrderMapper.getZcMoney(zcOrder);
	}

	public List<ContractExportDto> getContractExport(Map<String, Object> map) {

		List<ContractExportDto> list = cContractOrderMapper.getContractExport(map);
		for(ContractExportDto l:list) {
			if(l.getUserid()!=null) {
				MtCliqueUser cliqueUser=cliqueUserMapper.selectByPrimaryKey(l.getUserid());
				if(cliqueUser!=null) {
					l.setUsername(cliqueUser.getLogin());
				}
			}
		}
		return list;
	}

    public List<CContractOrder> selectContractOrder(Map<String, Object> map) {
        return cContractOrderMapper.selectContractOrder(map);
    }
}
