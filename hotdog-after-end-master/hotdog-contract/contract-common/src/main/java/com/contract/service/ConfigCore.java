package com.contract.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * 初始化加载
 * 
 * @author arno
 *
 */
@Component
public class ConfigCore implements ApplicationListener<ContextRefreshedEvent> {

	public static Web3j web3j;//链接以太坊对象

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() == null){
			try {
				//连接方式1：使用infura 提供的客户端
				HttpService httpService=new HttpService("https://mainnet.infura.io/v3/9dd5f97ecb8242c1aa38c6dfcebde820");
				Web3j web3j1 = Web3j.build(httpService);
				
			    //测试是否连接成功
			    String web3ClientVersion = web3j1.web3ClientVersion().send().getWeb3ClientVersion();
			    if(!StringUtils.isEmpty(web3ClientVersion)) {
			    		System.out.println("以太坊链接成功版本号："+web3ClientVersion);
			    		web3j=web3j1;
			    }else {
			    		System.out.println("以太坊连接失败");
			    }
			} catch (Exception e) {
				System.out.println("以太坊连接失败"+e.getMessage());
			}
			System.out.println("核心基础配置加载完毕");
		}
	}
}
