package com.pmzhongguo.otc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.core.websocket.WebSocket;
import com.pmzhongguo.otc.entity.dto.OTCTradeDTO;
import com.pmzhongguo.otc.entity.dto.WCharRecordDTO;
import com.pmzhongguo.otc.service.OTCTradeManager;
import com.pmzhongguo.otc.service.WCharRecordManager;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "聊天接口", description = "websocket聊天用接口", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("otc/wchar")
public class WCharRecordController extends TopController {

	@Autowired
	private WCharRecordManager wCharRecordManager;
	
	private static OTCTradeManager oTCTradeManager;
	
	@Autowired
	public void setoTCTradeManager(OTCTradeManager oTCTradeManager) {
		this.oTCTradeManager = oTCTradeManager;
	}

	@ApiOperation(value = "获得用户聊天记录", notes = "输入字段：对方ID:oppositeId  默认获取最新的10条聊天记录", httpMethod = "GET")
	@RequestMapping(value = "getRecordPage", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getRecordPage(HttpServletRequest request, HttpServletResponse response) {
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		Map<String, Object> map = $params(request);
		if(HelpUtils.isMapNullValue(map, "oppositeId")) {
			return new ObjResp(Resp.FAIL, "lost oppositeId!", null);
		}
		if(HelpUtils.isMapNullValue(map, "tradeId")) {
			return new ObjResp(Resp.FAIL, "lost tradeId!", null);
		}
		if(HelpUtils.isMapNullValue(map, "pagesize")) {
			map.put("pagesize", 10);
		}
		if(HelpUtils.isMapNullValue(map, "page")) {
			map.put("page", 1);
		}
		Integer oppositeId = Integer.valueOf(String.valueOf(map.get("oppositeId")));
		map.put("memberId", m.getId());
		map.put("sortorder", "desc");
		map.put("sortname", "create_time");
		wCharRecordManager.batchUpdateIsRead(m.getId(), oppositeId);
		List<WCharRecordDTO> list = wCharRecordManager.findByConditionPage(map);
		Member opposite = memberService.getMemberById(oppositeId);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.newHashMap("records", list, "opposite_nick_name", opposite.getM_nick_name()));
	}
	
	@ApiOperation(value = "获得用户未读聊天记录信息", notes = "获得用户未读聊天记录信息", httpMethod = "GET")
	@RequestMapping(value = "getNoRecordInfo", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getNoRecordInfo(HttpServletRequest request, HttpServletResponse response) {
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		List<Map<String, Object>> result = wCharRecordManager.getNoRecordInfo(m.getId());
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, result);
	}
	
//	@ApiOperation(value = "获得用户聊天记录", notes = "", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiIgnore
	@RequestMapping(value = "receiveMsg", method = RequestMethod.POST)
	@ResponseBody
	public void receiveMsg(HttpServletRequest request, HttpServletResponse response, @RequestBody WCharRecordDTO record) {
		String oppositeId = String.valueOf(record.getOppositeId());
		if(WebSocket.webSocketMaps.containsKey(oppositeId)) {
			WebSocket webSocket = WebSocket.webSocketMaps.get(oppositeId);
			record.setNick_name(webSocket.getNick_name());
			webSocket.getSession().getAsyncRemote().sendText(record.toString());
		}
	}
	
//	@ApiOperation(value = "获取websocket key", notes = "", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiIgnore
	@RequestMapping(value = "getWebsocketKey", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getWebsocketKey(HttpServletRequest request, HttpServletResponse response) {
		List<String> list = new ArrayList<String>();
		Enumeration<String> e = WebSocket.webSocketMaps.keys();
		while(e.hasMoreElements()) {
			list.add(e.nextElement());
		}
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, list);
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param apiOrderCreate
	 * @return
	 * @throws Exception
	 */
//	websocket没好之前临时用一下
//	@ApiOperation(value = "直接将聊天记录写入数据库", notes = "直接将聊天记录写入数据库", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "insertChar", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp insertChar(HttpServletRequest request, HttpServletResponse response,
			@RequestBody WCharRecordDTO dto) throws Exception {
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		String memberId = m.getId().toString();
//		String memberId = dto.getMemberId().toString();
//		if(!memberId.equals(String.valueOf(dto.getMemberId())) && !memberId.equals(String.valueOf(dto.getOppositeId()))) {
//			dto.setCharContent("错误的会员id");
//			return;
//		}
//		
//		if(dto.getTradeId() == null) {
//			dto.setCharContent("没有交易id");
//			return;
//		}
		
		//模拟发送会导致昵称不对，这个问题暂时由前端做判断以后有埋单后端再优化
		if(memberId.equals(String.valueOf(dto.getOppositeId()))) {
			OTCTradeDTO oTCTradeDTO = oTCTradeManager.findById(dto.getTradeId());
			dto.setTradeId(oTCTradeDTO.getOppositeTId());
		}
		
		dto.setCreateTime(HelpUtils.dateToString(new Date()));
		String oppositeId = String.valueOf(dto.getOppositeId());
		//插入聊天记录
		WCharRecordDTO opposite = wCharRecordManager.insertDouble(dto);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, opposite.toString());
	}
	
}
