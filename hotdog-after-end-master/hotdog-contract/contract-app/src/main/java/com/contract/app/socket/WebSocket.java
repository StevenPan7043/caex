package com.contract.app.socket;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.contract.common.DateUtil;
import com.contract.common.ListUtils;
import com.contract.dao.GdBuyRecordMapper;
import com.contract.entity.GdBuyRecord;
import com.contract.entity.GdBuyRecordExample;
import com.contract.enums.KlineTypeEnums;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.contract.dto.CustomerDto;
import com.contract.dto.Depth;
import com.contract.dto.SymbolDto;
import com.contract.entity.CContractOrder;
import com.contract.entity.CZcOrder;
import com.contract.service.SpringBeanFactoryUtils;
import com.contract.service.redis.RedisUtilsService;

import common.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * websocket
 *
 * @author arno
 *
 */
@ServerEndpoint("/websocket/{token}")
@Controller
public class WebSocket {


	@Autowired
	private GdBuyRecordMapper gdBuyRecordMapper;

	private Logger logger = Logger.getLogger(WebSocket.class);

	public static ConcurrentMap<String, WebSocket> webSocketMap = new ConcurrentHashMap<>();
	/**
	 * 当前操作进来的连接对象
	 */
	private String token;
	private Session session;

	/**
	 * 连接建立后触发的方法
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("token") String token) {
		try {
			this.session = session;
			this.token=token;
			put(token, this);
			//发送消息登陆成功发送给自定会员
			List<SymbolDto> dtos=getRedisUtilsService().querySymDto();
			Collections.sort(dtos);
			String message=JSONArray.toJSONString(dtos);
			sendMessageTo(message,token);
		} catch (Exception e) {
			System.out.println("连接失败："+e.getMessage());
		}
	}

	/**
	 *
	 * 连接关闭调用的方法
	 *
	 */

	@OnClose
	public void onClose() {
		if (!StringUtils.isEmpty(token)) {
			remove(token); // 从set中删除
		}
	}

	/**
	 *
	 * 收到客户端消息后调用的方法
	 *
	 * @param message
	 *            客户端发送过来的消息
	 *
	 * @param session
	 *            可选的参数
	 *
	 */

	@OnMessage
	public void onMessage(String message, Session session) {
		try {
			if (!StringUtils.isEmpty(message)) {
				System.out.println("======="+message);
				String[] split = message.split("_");
				String tk = split[0];
				System.out.println("token为:"+tk);
				String event = split[1];
				System.out.println("类型为:"+event);
				switch (event) {
					case "home":
						//发送消息登陆成功发送给自定会员
						List<SymbolDto> dtos = getRedisUtilsService().querySymDto();
						Collections.sort(dtos);
						String msg = JSONArray.toJSONString(dtos);
						sendMessageTo(msg, tk);
						break;
					case "depth":
						String dto = getRedisUtilsService().getKey(tk);
						if (StringUtils.isEmpty(dto)) {
							sendMessageTo("1001", tk);
							return;
						}
						CustomerDto customerDto = JSONObject.parseObject(dto, CustomerDto.class);
						if (customerDto == null) {
							sendMessageTo("1001", tk);
							return;
						}
						String coin = split[2];
						String key = coin + "_depth";

						Depth depthMsg = getRedisUtilsService().getDepth(key);
						SymbolDto symDto = getRedisUtilsService().getSymbolDto(coin);
						if (symDto != null) {
							depthMsg.setCoinPrice(symDto.getUsdtPrice());
							depthMsg.setCoinCny(symDto.getCny());
//						String orderkey="*order_"+customerDto.getId()+"_"+split[2]+"_*";
							List<CContractOrder> list = getRedisUtilsService().queryOrder(customerDto.getId(), symDto, split[2]);
							depthMsg.setList(list);

							//处理逐仓
							String allKey = "zc_" + customerDto.getId() + "_" + symDto.getName() + "_*";
							Set<String> zcOrderKeys = getRedisUtilsService().getKeys(allKey);
							List<CZcOrder> zclist = new ArrayList<>();
							for (String z : zcOrderKeys) {
								String str = getRedisUtilsService().getKey(z);
								if (!StringUtils.isEmpty(str)) {
									CZcOrder cZcOrder = JSONObject.parseObject(str, CZcOrder.class);
									zclist.add(cZcOrder);
								}
							}
							depthMsg.setZclist(zclist);
							//获取总额
							JSONObject moneyMap = getRedisUtilsService().getOutMoney(customerDto.getId(), split[2]);
							depthMsg.setMoneyMap(moneyMap);
							sendMessageTo(JSONObject.toJSONString(depthMsg), tk);
						}
						break;
					case "contractall":
						dto = getRedisUtilsService().getKey(tk);
						if (StringUtils.isEmpty(dto)) {
							sendMessageTo("1001", tk);
							return;
						}
						customerDto = JSONObject.parseObject(dto, CustomerDto.class);
						if (customerDto == null) {
							sendMessageTo("1001", tk);
							return;
						}
						//key order_token_coin_ordercode
//					String allKey="*order_"+customerDto.getId()+"_*";
						String allKeyMoeny = "conmoney_" + customerDto.getId() + "_*";
						Set<String> keySetAllTotal = getRedisUtilsService().getKeys(allKeyMoeny);
						List<CContractOrder> listAll = new ArrayList<>();
						for (String m : keySetAllTotal) {
							String string = m.split("_")[2];
							SymbolDto symbolDto = getRedisUtilsService().getSymbolDto(string);
							if (symbolDto != null) {
								List<CContractOrder> orders = getRedisUtilsService().queryOrder(customerDto.getId(), symbolDto, string);
								if (orders != null) {
									listAll.addAll(orders);
								}
							}
						}
						String orderAllMsg = JSONArray.toJSONString(listAll);
						sendMessageTo(orderAllMsg, tk);
						break;
					case "kline":
						SymbolDto k_dto = getRedisUtilsService().getSymbolDto(split[2]);
						//深度
						Depth k_depth = getRedisUtilsService().getDepth(split[2] + "_depth");
						JSONObject obj = new JSONObject();
						obj.put("dto", k_dto);
						obj.put("depth", k_depth);
						sendMessageTo(obj.toJSONString(), tk);
						break;
					case "klineCurrent":
						String type = "";
						if (split.length < 4) {
							type = KlineTypeEnums.min1.getName();
						} else {
							type = split[3];
						}
						String klineKey = "kline_" + split[2].toUpperCase()+"_" + type+"_current";
						String kline = getRedisUtilsService().getKey(klineKey);
						kline =StringEscapeUtils.unescapeJavaScript(kline);
						sendMessageTo(kline, tk);
						break;
					case "position":
						dto = getRedisUtilsService().getKey(tk);
						if (StringUtils.isEmpty(dto)) {
							sendMessageTo("1001", tk);
							return;
						}
						customerDto = JSONObject.parseObject(dto, CustomerDto.class);
						if (customerDto == null) {
							sendMessageTo("1001", tk);
							return;
						}
						Object userPositionMes = getUserPositionMes(customerDto.getId());
						sendMessageTo(JSONArray.toJSONString(userPositionMes), tk);
						break;
					default:
						break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("监听消息失败msg:" + message);
			remove(token);
		}
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
		Set<String> zcPositionKeySet = getRedisUtilsService().getKeys(zc_position);
		if (!CollectionUtils.isEmpty(zcPositionKeySet)) {
			for (String zcPositionKey : zcPositionKeySet) {
				String zkOrder = getRedisUtilsService().getKey(zcPositionKey);
				if (!StringUtils.isEmpty(zkOrder)) {
					CZcOrder cZcOrder = JSONObject.parseObject(zkOrder, CZcOrder.class);
					zclist.add(cZcOrder);
				}
			}
		}
		//统计用户全仓持仓订单
		List<CContractOrder> qcList = new ArrayList<>();
		String qc_position = "order_" + uid + "_*";
		Set<String> qcPositionKeySet = getRedisUtilsService().getKeys(qc_position);

		if (!CollectionUtils.isEmpty(qcPositionKeySet)) {
			Date now_time = new Date();
			for (String qcPositionKey : qcPositionKeySet) {
				System.out.println("全仓持仓订单:"+qcPositionKey);
				String qcOrder = getRedisUtilsService().getKey(qcPositionKey);
				if (!StringUtils.isEmpty(qcOrder)) {
					CContractOrder cContractOrder = JSONObject.parseObject(qcOrder, CContractOrder.class);
					if(cContractOrder.getStatus() == 1) {
						cContractOrder.setRunLength(cContractOrder.getRunTime());
						long hms = DateUtil.getHMS(now_time, cContractOrder.getCreatetime()) / 1000;
						Integer runTime = cContractOrder.getRunTime();
						if(runTime==null||runTime.intValue()>=1000){
							runTime = 1000;
							cContractOrder.setRunTime(runTime);
						}else{
							cContractOrder.setRunTime(runTime > hms ? runTime - (int) hms : 0);
						}
						qcList.add(cContractOrder);
						System.out.println("全仓:" + qcList);
					}
				}
			}
		}

		//统计用户跟单持仓订单
		List<GdBuyRecord> gdList = new ArrayList<>();
		String gd_position = "gd_" + uid + "_*";
		Set<String> gdPositionKeySet = getRedisUtilsService().getKeys(gd_position);
		if(!CollectionUtils.isEmpty(gdPositionKeySet)){
			for(String gdPositionKey : gdPositionKeySet){
				String gdOrder = getRedisUtilsService().getKey(gdPositionKey);
				if(!StringUtils.isEmpty(gdOrder)){
					GdBuyRecord gdBuyRecord = JSONObject.parseObject(gdOrder, GdBuyRecord.class);
					gdList.add(gdBuyRecord);
				}
			}
		}

		positionMsg.put("zcPosition", zclist);
		positionMsg.put("qcPosition", qcList);
		positionMsg.put("gdPosition", gdList);
		//获取盈利订单
		positionMsg.put("ylPosition", ListUtils.winList);
		System.out.println("盈利订单:"+ListUtils.winList.toString());
		return positionMsg;
	}
	/**
	 *
	 * 发生错误时调用
	 *
	 * @param session
	 *
	 * @param error
	 *
	 */

	@OnError
	public void onError(Session session, Throwable error) {
		logger.info("发生错误token:" + token);
		remove(token);
	}

	/**
	 * 单个人发送信息
	 * @param message
	 * @param To
	 * @throws IOException
	 */
	public void sendMessageTo(String message, String To){
		try {
			for (WebSocket item : getValues()) {
				if (To.equals(item.token)) {
					item.session.getAsyncRemote().sendText(message);
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("数据传输失败token："+To);
		}
	}

	/**
	 * 批量发送
	 * @param message
	 * @throws IOException
	 */
	public void sendMessageAll(String message){
		try {
			for (WebSocket item : getValues()) {
				item.session.getAsyncRemote().sendText(message);
			}
		} catch (Exception e) {
			System.out.println("批量发送异常");
		}
	}

	/**
	 * 注入redis
	 * @return
	 */
	private RedisUtilsService getRedisUtilsService() {
		return SpringBeanFactoryUtils.getApplicationContext().getBean(RedisUtilsService.class);
    }
	public static void put(String key, WebSocket myWebSocketServer) {
		webSocketMap.put(key, myWebSocketServer);
	}

	public static WebSocket get(String key) {
		return webSocketMap.get(key);
	}

	public static void remove(String key) {
		webSocketMap.remove(key);
	}

	public static Collection<WebSocket> getValues() {
		return webSocketMap.values();
	}

	public static ConcurrentMap<String, WebSocket> getWebSocketMap() {
		return webSocketMap;
	}

	public static void setWebSocketMap(ConcurrentMap<String, WebSocket> webSocketMap) {
		WebSocket.webSocketMap = webSocketMap;
	}


}
