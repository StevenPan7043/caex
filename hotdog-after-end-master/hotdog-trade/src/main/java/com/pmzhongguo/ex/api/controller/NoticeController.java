package com.pmzhongguo.ex.api.controller;


import com.pmzhongguo.ex.business.vo.WalletRechargeWithdrawVo;
import com.pmzhongguo.ex.business.model.NoticeContentBuilder;
import com.pmzhongguo.ex.business.service.manager.NoticeManager;
import com.pmzhongguo.ex.core.web.*;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.zzextool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/a/notice")
public class NoticeController extends TopController {



	@Autowired
	private NoticeManager noticeManager;

    /**
     * 钱包充值/提现通知
     * @param rechargeRequest
     * @return
     */
	@RequestMapping(value = "/wallet", method = RequestMethod.POST)
	@ResponseBody
	public Resp walletRecharge(@RequestBody WalletRechargeWithdrawVo rechargeRequest) {

        if (StringUtil.isNullOrBank(rechargeRequest.getMemberId())
                || StringUtil.isNullOrBank(rechargeRequest.getAmount())
                || StringUtil.isNullOrBank(rechargeRequest.getCurrency())
                || StringUtil.isNullOrBank(rechargeRequest.getMsgType())
                || StringUtil.isNullOrBank(rechargeRequest.getTime())
        ) {
            return Resp.failMsg(ErrorInfoEnum.PARAMS_ERROR.getErrorENMsg());
        }
        NoticeContentBuilder parse
                = new NoticeContentBuilder(rechargeRequest.getMsgType()
                ,rechargeRequest.getCurrency()
                ,rechargeRequest.getTime()
                ,rechargeRequest.getAmount());
        if (StringUtil.isNullOrBank(parse.getContent())) {
            return Resp.failMsg(ErrorInfoEnum.PARAMS_ERROR.getErrorENMsg());
        }
        noticeManager.sendByMemberId(rechargeRequest.getMemberId(),parse.getContent());
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}




}
