package com.pmzhongguo.ex.core.web;

import javax.servlet.ServletContext;

import com.pmzhongguo.ex.core.config.RedisMsgPubSubListener;
import com.pmzhongguo.ex.core.config.SimpleServiceFactoryHandler;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.utils.UserWithdrawPropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;


public class SysInit implements InitializingBean, ServletContextAware {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RedisMsgPubSubListener redisMsgPubSubListener;

	private SysInit() {
	}
	/**
	 * 启动时加载读取配置信息,并初始化
	 */
	@Override
	public void setServletContext(ServletContext servletContext) {
		SimpleServiceFactoryHandler handler = new SimpleServiceFactoryHandler(servletContext);
		logger.error("---启动初始化加载读取配置信息start，time:{}", HelpUtils.getNowTimeStamp());
		handler.initData();
		logger.error("---启动初始化加载读取配置信息 end , time:{}",HelpUtils.getNowTimeStamp());
		JedisListen();
		logger.error("load user withdraw config");
		loadUserWithdrawConfig();
	}

	/**
	 * 项目启动时加载走固定验证码用户的配置文件
	 */
	public void loadUserWithdrawConfig(){
		UserWithdrawPropertiesUtil.loadProperties("../user_withdraw.properties");
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	private void JedisListen() {
		new Thread(() -> {
			while (true) {
				JedisUtil.getInstance().subscribe(redisMsgPubSubListener, JedisChannelEnum.getEnumCodeEn());
			}
		}).start();
	}
}
