package com.contract.service.wallet.zb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.contract.common.DateUtil;
import com.contract.common.FunctionUtils;
import com.contract.common.HttpUtil;
import com.contract.dto.Depth;
import com.contract.dto.Kline;
import com.contract.dto.Ticker;
import com.contract.exception.ThrowJsonException;

/**
 * 中币
 * 
 * @author arno
 *
 */
public class ZBUtils {

	// 正式
	public final String ACCESS_KEY = "4ca7e599-ec08-4e02-80ca-5a35a8070571";
	public final String SECRET_KEY = "3e1d350f-9c3d-4b0c-b69b-d8cbd787f89d";
	public final String URL_PREFIX = "https://trade.zb.com/api/";
	public static String API_DOMAIN = "http://api.zb.cn";
	
	
	public static void main(String[] args) {
//		Kline kline= getMarket("eth_usdt");
//		System.out.println(kline.getClose());
//		System.out.println(kline.getOpen());
//		System.out.println((kline.getClose()-kline.getOpen())/kline.getOpen());
//		getDepth("btc_usdt","0.01");
//		Ticker ticker=getRealPrice("eth_usdt");
//		System.out.println(ticker.getLast());
		for(int i=10;i>0;i--) {
			System.out.println(i);
		}
	}
	
	/**
	 * 
	 * @param symbol
	 */
	public static Kline getMarket(String symbol) {
		String currency =symbol;
		// 请求地址
		String url = API_DOMAIN + "/data/v1/kline?market=" + currency + "&type=1day&size=1";
		String callback = HttpUtil.getInstance().get(url);
		if(StringUtils.isEmpty(callback)) {
			throw new ThrowJsonException("获取K线图失败");
		}
		JSONObject json = JSONObject.parseObject(callback);
		JSONArray array=json.getJSONArray("data");
		JSONArray arr=array.getJSONArray(0);
		Kline kline=new Kline();
		kline.setOpen(arr.getDoubleValue(1));
		kline.setClose(arr.getDoubleValue(4));
		kline.setHigh(arr.getIntValue(2));
		kline.setLow(arr.getIntValue(3));
		kline.setVol(arr.getDoubleValue(5));
		return kline;
	}
	
	/**
	 * 获取K线图
	 * @param symbol  交易对
	 * @param type 类型 1min 3min 30min 1day
	 * @param size 数据条数
	 * @return
	 */
	public static JSONObject queryKline(String symbol,String type,Integer size) {
		String currency =symbol;
		// 请求地址
		String url = API_DOMAIN + "/data/v1/kline?market=" + currency + "&type="+type+"&size="+size;
		String callback = HttpUtil.getInstance().get(url);
		if(StringUtils.isEmpty(callback)) {
			return new JSONObject();
		}
		JSONObject json = JSONObject.parseObject(callback);
		JSONArray array=json.getJSONArray("data");
		if(array==null || array.size()<1) {
			return new JSONObject();
		}
		List<List<BigDecimal>> list=new ArrayList<>();//定义中坐标
		List<BigDecimal> vols=new ArrayList<>();//交易量
		List<String> dates=new ArrayList<>();//时间轴
		
		//倒叙
		for (int i =0; i < array.size(); i++) {
			JSONArray arr=array.getJSONArray(i);
			List<BigDecimal> list2=new ArrayList<>();
			list2.add(arr.getBigDecimal(1));//开
			list2.add(arr.getBigDecimal(4));//收
			list2.add(arr.getBigDecimal(3));//低
			list2.add(arr.getBigDecimal(2));//高
			list.add(list2);
			vols.add(arr.getBigDecimal(5));
			dates.add(DateUtil.toDateString(arr.getDate(0),"yyyy-MM-dd HH:mm"));
		}
		//组装时间轴  X轴数量=size
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("data", list);//组装纵坐标数据
		jsonObject.put("volumes", vols);//组装交易量
		jsonObject.put("dates", dates);//组装时间轴
//		jsonObject.put("topObj", topObj);//顶部数据
		return jsonObject;
	}
	/**
	 * 获取深度
	 * @param currency
	 * @param depth
	 * @return
	 */
	public static Depth getDepth(String currency,String depth) {
		// 请求地址
		String url = API_DOMAIN + "/data/v1/depth?market=" + currency+"&merge="+depth;
		String callback =  HttpUtil.getInstance().get(url);
		if(StringUtils.isEmpty(callback)) {
			return null;
		}
		Depth depth2=JSONObject.parseObject(callback, Depth.class);
		return depth2;
	}
	
	/**
	 * 获取深度
	 * @param currency
	 * @param depth
	 * @return
	 */
	public static Depth getDepth(String currency,int size) {
		// 请求地址
		String url = API_DOMAIN + "/data/v1/depth?market=" + currency+"&size="+size;
		String callback =  HttpUtil.getInstance().get(url);
		if(StringUtils.isEmpty(callback)) {
			return null;
		}
		Depth depth2=JSONObject.parseObject(callback, Depth.class);
		return depth2;
	}
	
	/**
	 * 获取行情
	 * @param currency
	 */
	public static Ticker getRealPrice(String currency) {
		// 请求地址
		String url = API_DOMAIN + "/data/v1/ticker?market=" + currency;
		String callback =  HttpUtil.getInstance().get(url);
		if(StringUtils.isEmpty(callback)) {
			throw new ThrowJsonException("获取行情失败");
		}
		JSONObject jsonObject=JSONObject.parseObject(callback);
		String tk=jsonObject.getString("ticker");
		Ticker ticker= JSONObject.parseObject(tk, Ticker.class);
		return ticker;
	}
}
