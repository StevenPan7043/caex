package com.contract.service.wallet.btc;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.contract.exception.ThrowJsonException;
import com.contract.service.wallet.btc.enums.NodeEnums;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

import net.iharder.Base64;
 
public class CoinUtils {
	private JsonRpcHttpClient client;
	
	public CoinUtils(NodeEnums n) {
		try {
			// 身份认证
			String cred = Base64.encodeBytes((n.getUser() + ":" + n.getPassword()).getBytes());
			Map<String, String> headers = new HashMap<String, String>(1);
			headers.put("Authorization", "Basic " + cred);
			client = new JsonRpcHttpClient(new URL("http://"+n.getUrl()+":"+n.getPort()), headers);
		} catch (Exception e) {
			
		}
	}
	
	
	/**
	 * 验证地址是否存在
	 * @param address
	 * @return
	 * @throws Throwable
	 */
	public boolean validateaddress(String address){
//		try {
//			String result= (String) client.invoke("validateaddress", new Object[] {address}, Object.class).toString();
//			System.out.println(result);
//			if(StringUtils.isEmpty(result)) {
//				return false;
//			}
//			JSONObject jsonObject=JSONObject.parseObject(result);
//			boolean isvalid=jsonObject.getBooleanValue("isvalid");
//			return isvalid;
//		} catch (Throwable e) {
//			throw new ThrowJsonException("地址错误");
//		}
		return true;
		
	}
 
	/**
	 * 如果钱包加密需要临时解锁钱包
	 * @param password
	 * @param time
	 * @return
	 * @throws Throwable
	 */
	public String walletpassphase(String password,int time)throws Throwable{
		return  (String) client.invoke("walletpassphase", new Object[] {password,time}, Object.class).toString();
	}
	
	/**
	 * 转账到制定的账户中
	 * @param address
	 * @param amount
	 * @return
	 * @throws Throwable
	 */
	public String sendtoaddress(String address,double amount){
		try {
			return  (String) client.invoke("sendtoaddress", new Object[] {address,amount}, Object.class).toString();
		} catch (Throwable e) {
			throw new ThrowJsonException("转账异常"+e.getMessage());
		}
		
	}
	
	public String queryAll(){
		try {
			return  (String) client.invoke("listaddressgroupings", new Object[] {}, Object.class).toString();
		} catch (Throwable e) {
			throw new ThrowJsonException("转账异常"+e.getMessage());
		}
		
	}
	
	/**
	 * 获取btc交易
	 * @param txid
	 * @return
	 */
	public JSONObject gettransaction(String txid) {
		try {
			JSONObject jsonObject= client.invoke("gettransaction", new Object[] {txid}, JSONObject.class);
			System.out.println(jsonObject);
			return jsonObject;
		} catch (Throwable e) {
			throw new ThrowJsonException("获取异常"+e.getMessage());
		}
	}
	
	/**
	 * 校验交易是否确认
	 * @param txid
	 * @return
	 */
	public boolean  validTransConfirmations(String txid) {
		JSONObject obj= gettransaction(txid);
		if(obj==null || obj.getInteger("confirmations")!=1) {
			return false;
		}
		return true;
	}
	
	/**
	 * 转账到制定的账户中
	 * @param address
	 * @param amount
	 * @return
	 * @throws Throwable
	 */
	public String sendtoaddress(String address,int proid,double amount)throws Throwable{
		return  (String) client.invoke("sendtoaddress", new Object[] {address,proid,amount}, Object.class).toString();
	}
	
	/**
	 * 转账到制定的账户中
	 * @param address
	 * @param amount
	 * @return
	 * @throws Throwable
	 */
	public String sendfrom(String fromaddress,String address,double amount)throws Throwable{
		return  (String) client.invoke("sendfrom", new Object[] {fromaddress,address,amount}, Object.class).toString();
	}
	
	/**
	 * 查询账户下的交易记录
	 * @param account
	 * @param count
	 * @param offset
	 * @return
	 * @throws Throwable
	 */
	public String listtransactions(String account, int count ,int offset )throws Throwable{
		return  (String) client.invoke("listtransactions", new Object[] {account,count,offset}, Object.class).toString();
	}
	
	/**
	 * 获取地址下未花费的币量
	 * @param account
	 * @param count
	 * @param offset
	 * @return
	 * @throws Throwable
	 */
	public String listunspent( int minconf ,int maxconf ,String address)throws Throwable{
		String[] addresss= new String[]{address};
		return  (String) client.invoke("listunspent", new Object[] {minconf,maxconf,addresss}, Object.class).toString();
	}
	
	/**
	 * 生成新的接收地址
	 * @return
	 * @throws Throwable
	 */
	public String getNewaddress() throws Throwable{
		return  (String) client.invoke("getnewaddress", new Object[] {}, Object.class).toString();
	}
	
	/**
	 * 获取钱包信息
	 * @return
	 * @throws Throwable
	 */
	public String getInfo() throws Throwable{
		return  client.invoke("getblockchaininfo", new Object[] {}, Object.class).toString();
	}
	
	/**
	 * 获取钱包信息
	 * @return
	 * @throws Throwable
	 */
	public String getbalance(String account) {
		try {
			String result=client.invoke("omni_getbalance", new Object[] {account,31}, Object.class).toString();
			return  result;
		} catch (Throwable e) {
			throw new ThrowJsonException("获取BTC失败");
		}
	}
	
	/**
	 * 获取钱包信息
	 * @return
	 * @throws Throwable
	 */
	public String getBtcbalance(String account) {
		try {
			String result=client.invoke("getbalance", new Object[] {account}, Object.class).toString();
			return  JSONObject.parseObject(result).getString("balance");
		} catch (Throwable e) {
			throw new ThrowJsonException("获取BTC失败");
		}
	}
	
	/**
	 *      * USDT转帐      
	 * * @param toAddr      
	 * * @param value      
	 * * @return      
	 * @throws Throwable 
	 */
	public void importkey(String key) throws Throwable {
		String result=client.invoke("importprivkey", new Object[] {key,"",false}, Object.class).toString();
		System.out.println(result);
	}
	
	public void dumpprivkey(String addr) throws Throwable {
		String result=client.invoke("dumpprivkey", new Object[] {addr}, Object.class).toString();
		System.out.println(result);
	}
	
	public static void main(String[] args) throws Throwable {
//		CoinUtils config=new CoinUtils(NodeEnums.node_1);
//		config.dumpprivkey("15KJTSCfiMbfc6YASc6LKwvQrSQG9t65bk");
		CoinUtils config=new CoinUtils(NodeEnums.node_1);
//		config.getkey("15KJTSCfiMbfc6YASc6LKwvQrSQG9t65bk");
		config.importkey("L1BuSA2XpuEqPv8fLege3Pgs1UQPf6tR4CJnMfujzV7cnJEXAS5W");
	}
}
 