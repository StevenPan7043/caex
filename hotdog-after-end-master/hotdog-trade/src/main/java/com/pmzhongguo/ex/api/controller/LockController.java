package com.pmzhongguo.ex.api.controller;

import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.business.service.*;
import com.pmzhongguo.ex.business.vo.LockListVo;
import com.pmzhongguo.ex.core.utils.*;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.LstResp;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(value = "锁仓接口", description = "锁仓相关", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class LockController extends TopController {
	private Logger logger = LoggerFactory.getLogger(LockController.class);

	@Autowired
	private LockListService lockListService;
	@Autowired
	private MemberService memberService;



	@ApiOperation(value = "锁仓划转", notes = "用户账户金额划转到锁仓,currency：币种，例如ebank，l_type：周期，例如3，amount：数量，例如100,secPwd:资金密码", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/lock", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Resp lockRecharge(HttpServletRequest request,@RequestBody LockListVo lockList) {
		Member m = JedisUtilMember.getInstance().getMember(request,null);
		if (null == m || m.getId() <= 0) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
		}
		Currency dbcurrency = HelpUtils.getCurrencyMap().get(lockList.getCurrency().toUpperCase());
		if(dbcurrency == null) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg());
		}

		// 数量大于0
		if (lockList.getAmount().compareTo(BigDecimal.ZERO) < 1) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg(), null);
		}
		// 只能是 EBANK 币种
		if(!"EBANK".equalsIgnoreCase(lockList.getCurrency())) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg());
		}
		Member dbMember = memberService.getMemberById(m.getId());
		// 资金密码
		if (HelpUtils.nullOrBlank(lockList.getSecPwd())
				|| !dbMember.getM_security_pwd().equals(MacMD5.CalcMD5Member(lockList.getSecPwd()))) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg(), null);
		}
		LockList entity = new LockList();
		BeanUtils.copyProperties(lockList,entity);
		entity.setCurrency_id(dbcurrency.getId());
		entity.setMember_id(m.getId());
		entity.setCurrency(lockList.getCurrency().toUpperCase());
		Resp result = lockListService.addLockRecharge(entity);
		return result;

	}
	@ApiOperation(value = "锁仓排名", notes = "锁仓排名，根据amount排名，no是排名序号，size：需要显示多少条", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/lock/rank/{size}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Resp lockRankByAmount(@PathVariable Integer size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("size",size);
		List<Map<String, Object>> list = lockListService.findTopLockByMemberAndDate(params);
		return new LstResp(Resp.SUCCESS,Resp.SUCCESS_MSG,list.size(),list);

	}

	@ApiOperation(value = "我的锁仓记录", notes = "锁仓记录：page页码，从1开始，pagesize每页数量", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/lock/record/{page}/{pagesize}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Resp lockRankByAmount(HttpServletRequest request,@PathVariable Integer page,@PathVariable Integer pagesize) {

		Member m = JedisUtilMember.getInstance().getMember(request,null);
		if (null == m || m.getId() <= 0) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pagesize",pagesize);
		params.put("page",page);
		params.put("member_id",m.getId());
		params.put("currency","EBANK");
		List<LockList> list = lockListService.findLockRecordByMemberId(params);
		return new LstResp(Resp.SUCCESS,Resp.SUCCESS_MSG,Integer.parseInt(params.get("total") + ""),list);

	}

	@ApiOperation(value = "我的锁仓冻结数量", notes = "我的锁仓冻结数量,currency:币种，例如EBANK", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/lock/amount/{currency}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Resp lockRankByAmount(HttpServletRequest request,@PathVariable String currency) {

		Member m = JedisUtilMember.getInstance().getMember(request,null);
		if (null == m || m.getId() <= 0) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
		}
		Currency dbcurrency = HelpUtils.getCurrencyMap().get(currency.toUpperCase());
		if(dbcurrency == null) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg());
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("member_id",m.getId());
		params.put("currency",currency.toUpperCase());
		Map<String, Object> result = lockListService.findLockTotalAmount(params);
		return new RespObj(Resp.SUCCESS,Resp.SUCCESS_MSG,result);

	}



}
