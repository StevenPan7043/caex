package com.pmzhongguo.ex.core.websocket;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.utils.JsonUtil;
import com.pmzhongguo.ex.core.utils.PropertiesUtil;
import com.pmzhongguo.ex.core.web.Constants;
import com.pmzhongguo.otc.entity.dto.OTCTradeDTO;
import com.pmzhongguo.otc.entity.dto.WCharRecordDTO;
import com.pmzhongguo.otc.service.OTCTradeManager;
import com.pmzhongguo.otc.service.WCharRecordManager;

@ServerEndpoint(value = "/websocket/{jssession}", configurator = WebsocketConfig.class)
@CrossOrigin(origins = "*", maxAge = 3600)
@Service
public class WebSocket {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
//	protected Logger logger = LoggerFactory.getLogger("websocket");

	private static final String REDIS_KEY_PRE = "websocket_char_key_";

	private static final String ADDR_IP = IPAddressPortUtil.IP_ADDRESS;

	private static WCharRecordManager wCharRecordManager;
	
	private static OTCTradeManager oTCTradeManager;

	/* 每个浏览器连接都会有一个新的会话对象 */
	private Session session;

	private String memberId;

	private String nick_name;

	/* 用来存储每个会话的session,静态的不会被实例化 */
	public static ConcurrentHashMap<String, WebSocket> webSocketMaps = new ConcurrentHashMap<String, WebSocket>();

	static {
		JedisUtil.getInstance().del(REDIS_KEY_PRE + ADDR_IP);
	}

	/**
	 * 
	 * @param session
	 * @param config
	 */
	@OnOpen
	public void onOpen(Session session, EndpointConfig config, @PathParam(value = "jssession") String jssession) {
//    	HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		MySessionContext myc = MySessionContext.getInstance();
		HttpSession httpSession = myc.getSession(jssession);
		Member m = (Member) httpSession.getAttribute(Constants.SYS_SESSION_MEMBER);
		if (httpSession == null || m == null) {
			try {
				myc.delSession(jssession);
				session.close();
				logger.warn("httpSession or member is null !");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return;
			}
		}
		this.setMemberId(String.valueOf(m.getId()));
		this.setNick_name(m.getM_nick_name());
		this.session = session;
		webSocketMaps.put(memberId, this);
		JedisUtil.getInstance().sadd(REDIS_KEY_PRE + ADDR_IP, memberId);
	}

	@OnClose
	public void onClose() {
		logger.warn("websocket close. memberId:" + memberId + "\r\n");
		remove();
	}

	@OnError
	public void onError(Throwable e, Session session) {
		logger.warn("websocket exception. memberId:" + memberId + "\r\n" + e);
		remove();
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		WCharRecordDTO dto = JsonUtil.fromJson(message, WCharRecordDTO.class);
//		dto.setMemberId(Integer.valueOf(memberId));
		if(!memberId.equals(String.valueOf(dto.getMemberId())) && !memberId.equals(String.valueOf(dto.getOppositeId()))) {
			dto.setCharContent("错误的会员id");
			session.getAsyncRemote().sendText(dto.toString());
			return;
		}
		
		if(dto.getTradeId() == null) {
			dto.setCharContent("没有交易id");
			session.getAsyncRemote().sendText(dto.toString());
			return;
		}
		
		//模拟发送会导致昵称不对，这个问题暂时由前端做判断以后有埋单后端再优化
		if(memberId.equals(String.valueOf(dto.getOppositeId()))) {
			OTCTradeDTO oTCTradeDTO = oTCTradeManager.findById(dto.getTradeId());
			dto.setTradeId(oTCTradeDTO.getOppositeTId());
		}
		
		dto.setCreateTime(HelpUtils.dateToString(new Date()));
		String oppositeId = String.valueOf(dto.getOppositeId());
		//插入聊天记录
		WCharRecordDTO opposite = wCharRecordManager.insertDouble(dto);
		//存入发送方昵称
		opposite.setOppo_nick_name(nick_name);
		// 获取对方聊天记录id
		int oppositeChatId = opposite.getId();
		// 本服务器登录会员直接发送信息
		if (webSocketMaps.containsKey(oppositeId)) {
			WebSocket webSocket = webSocketMaps.get(oppositeId);
			opposite.setNick_name(webSocket.getNick_name());
			webSocket.session.getAsyncRemote().sendText(opposite.toString());
			wCharRecordManager.updateIsRead(oppositeChatId);
			return;
		}
		String remoteUrl = getMemberUrl(oppositeId);
		// 非本服务器登录会员
		if (!HelpUtils.nullOrBlank(remoteUrl)) {
			HelpUtils.post(remoteUrl + "/otc/wchar/receiveMsg", opposite.toString());
			wCharRecordManager.updateIsRead(oppositeChatId);
		}
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Session getSession() {
		return session;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	/**
	 * 查找接收信息的会员在哪个服务器
	 * 
	 * @param oppositeId
	 * @return
	 */
	private String getMemberUrl(String oppositeId) {
		String url = "";
		// 获取本地ip+port
		String localhost = ADDR_IP;
		String serverStr = PropertiesUtil.getPropValByKey("config.server");
		String[] serverArr = serverStr.split(",");
		for (int i = 0; i < serverArr.length; i++) {
			String rmiHost = serverArr[i];
			if (rmiHost.indexOf(localhost) < 0) {
				boolean b = JedisUtil.getInstance().sismember(REDIS_KEY_PRE + rmiHost.split(":")[0], oppositeId);
				if (b) {
					return rmiHost;
				}
			}
		}
		return url;
	}

	/**
	 * 清除相关数据
	 */
	private void remove() {
		if (!HelpUtils.nullOrBlank(memberId)) {
			boolean b = JedisUtil.getInstance().sismember(REDIS_KEY_PRE + ADDR_IP, memberId);
			if (b) {
				JedisUtil.getInstance().srem(REDIS_KEY_PRE + ADDR_IP, memberId);
			}
			webSocketMaps.remove(memberId);
		}
	}

	@Autowired
	public void setwCharRecordManager(WCharRecordManager wCharRecordManager) {
		this.wCharRecordManager = wCharRecordManager;
	}

	@Autowired
	public void setoTCTradeManager(OTCTradeManager oTCTradeManager) {
		this.oTCTradeManager = oTCTradeManager;
	}

}
