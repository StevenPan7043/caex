package com.contract.service.wallet.btc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.contract.common.HttpUtil;
import com.contract.exception.ThrowJsonException;
import com.contract.service.wallet.btc.dto.BitDto;
import com.contract.service.wallet.btc.enums.NodeEnums;

import net.iharder.Base64;

public class UsdtConfig {
	
	private static Logger logger =Logger.getLogger(UsdtConfig.class);
	
	// 正式网络usdt=31，测试网络可以用2
	private static final int propertyid =31;
	private final static String RESULT = "result";
	private final static String METHOD_SEND_TO_ADDRESS = "omni_send";
	// 可以指定手续费地址
	private final static String METHOD_OMNI_FUNDED_SEND = "omni_funded_send";
	private final static String METHOD_GET_TRANSACTION = "omni_gettransaction";
	private final static String METHOD_GET_BLOCK_COUNT = "getblockcount";
	private final static String METHOD_NEW_ADDRESS = "getnewaddress";
	private final static String METHOD_GET_BALANCE = "omni_getbalance";
	private final static String METHOD_GET_LISTTRANSACTIONS = "omni_listtransactions";

	private NodeEnums n=null;
	private static UsdtConfig usdtConfig = null;
	
	public static UsdtConfig getInstance(NodeEnums n) {
		usdtConfig = new UsdtConfig(n);
		return usdtConfig;
	}
	
	public NodeEnums getN() {
		return n;
	}

	public void setN(NodeEnums n) {
		this.n = n;
	}

	public UsdtConfig (NodeEnums n) {
		this.n=n;
	}
	
	public String getBalance() {
		try {
			JSONObject json = doRequest("getbalance");
			if (isError(json)) {
				return "";
			} else {
				return json.getString(RESULT);
			}
		} catch (Exception e) {
			throw new ThrowJsonException("获取信息异常");
		}
	}
	
	/**
     * 验证地址的有效性
     * @param address
     * @return
     * @throws Exception
     */
	public boolean vailedAddress(String address) {
		JSONObject json = doRequest("validateaddress",address);
		if(json==null) {
			return false;
		}
		boolean isvalid=json.getBooleanValue("isvalid");
		return isvalid;
	 }


	/**
	 *  * USDT查询余额  * @return  
	 */
	public double getBalance(String addr) {
		JSONObject json = doRequest(METHOD_GET_BALANCE,addr, propertyid);
		logger.info("getBalance:"+json);
		if (isError(json)) {
			return 0;
		}
		return json.getJSONObject(RESULT).getDouble("balance");
	}
	
	/**
	 *      * 区块高度      * @return      
	 */
	public int getBlockCount() {
		JSONObject json = null;
		try {
			json = doRequest(METHOD_GET_BLOCK_COUNT);
			logger.info("getBlockCount:"+json);
			if (!isError(json)) {
				return json.getInteger("result");
			} else {
				return 0;
			}
		} catch (Exception e) {
			throw new ThrowJsonException("获取区块高度异常");
		}
	}

	/**
	 *      * USDT产生地址      * @return      
	 */
	public String getNewAddress() {
		JSONObject json = doRequest(METHOD_NEW_ADDRESS);
		logger.info("getNewAddress:"+json);
		if (isError(json)) {
//			throw new ThrowJsonException("生成钱包地址错误");
			return "";
		}
		return json.getString(RESULT);
	}
	
	
	/**
	 *      * USDT转帐      
	 * * @param toAddr      
	 * * @param value      
	 * * @return      
	 */
	public String send(String fromAddr, String toAddr, String value) {
		if (vailedAddress(toAddr)) {
			JSONObject json = doRequest(METHOD_SEND_TO_ADDRESS, fromAddr, toAddr, propertyid, value);
			if (isError(json)) {
				return "";
			} else {
				return json.getString(RESULT);
			}
		} else {
			return "";
		}
	}
	
	/**
	 *      * USDT转帐      
	 * * @param toAddr      
	 * * @param value      
	 * * @return      
	 */
	public String send(String fromAddr, String toAddr, String value,String feeadd) {
		if (vailedAddress(toAddr)) {
			JSONObject json = doRequest(METHOD_OMNI_FUNDED_SEND, fromAddr, toAddr, propertyid, value,feeadd);
			if (isError(json)) {
				return "";
			} else {
				return json.getString(RESULT);
			}
		} else {
			return "";
		}
	}
	
	/**
	 *  获取    交易列表
	 * * @param toAddr      
	 * * @return      
	 */
	public List<BitDto> getTxlist(String addr) {
		if (vailedAddress(addr)) {
			JSONObject json = doRequest(METHOD_GET_LISTTRANSACTIONS, addr);
			if (isError(json)) {
				return null;
			} else {
				return JSONObject.parseArray(json.getString(RESULT), BitDto.class);
			}
		} else {
			return null;
		}
	}
	
	/**
	 *  获取    交易列表  
	 * @param count 查询条数
	 * @param index 下标
	 * @return
	 */
	public List<BitDto> getTxlist(Integer count,Integer index) {
		JSONObject json = doRequest(METHOD_GET_LISTTRANSACTIONS, "",count,index);
		if (isError(json)) {
			return null;
		} else {
			return JSONObject.parseArray(json.getString(RESULT), BitDto.class);
		}
	}
	

	/**
	 *  获取 根据单号查询交易   
	 * * @param toAddr      
	 * * @return      
	 */
	public BitDto getTx(String txid) {
		try {
			JSONObject json = doRequest(METHOD_GET_TRANSACTION, txid);
			if (isError(json)) {
				return null;
			} else {
				return JSONObject.toJavaObject(json.getJSONObject(RESULT), BitDto.class);
			}
		} catch (Exception e) {
			return null;
		}
	}
	
    /***************************************请求方法*****************************/
	public JSONObject doRequest(String method, Object... params) {
		JSONObject param = new JSONObject();
		param.put("id", System.currentTimeMillis() + "");
		param.put("jsonrpc", "2.0");
		param.put("method", method);
		if (params != null) {
			param.put("params", params);
		}
		String creb = Base64.encodeBytes((n.getUser() + ":" + n.getPassword()).getBytes());
		Map<String, String> headers = new HashMap<>(2);
		headers.put("Authorization", "Basic " + creb);
		headers.put("server", "1");
		String resp = HttpRpc.jsonPost("http://"+n.getUrl()+":"+n.getPort(), headers, param.toJSONString());
		return JSON.parseObject(resp);
	}

	private boolean isError(JSONObject json) {
		if (json == null || (StringUtils.isNotEmpty(json.getString("error")) && json.get("error") != "null")) {
			return true;
		}
		return false;
	}
	
	public String getBalanceApi(String addr) {
		String btc="0";
		try {
			String result=HttpUtil.getInstance().get("http://www.tokenview.com:8088/addr/b/BTC/"+addr);
			if(!StringUtils.isEmpty(result)) {
				JSONObject jsonObject=JSONObject.parseObject(result);
				String code=jsonObject.getString("code");
				if("1".equals(code)) {
					btc=jsonObject.getString("data");
				}
			}
		} catch (Exception e) {
			System.out.println("获取BTC异常");
			btc="获取失败,请点击查看";
		}
		return btc;
	}
}
