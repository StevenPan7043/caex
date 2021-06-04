package com.contract.service.wallet.huobi;


import com.huobi.api.HBApi;
import com.huobi.response.Kline;
import com.huobi.response.KlineResponse;

import java.util.List;

/**
 * 货币获取价格
 * @author arno
 *
 */
public class HuoBiUtils {
	private static HuoBiUtils huoBiUtils = null;

	public static HuoBiUtils getInstance() {
		if (huoBiUtils == null) {
			synchronized (HuoBiUtils.class) {
				if (huoBiUtils == null) {
					huoBiUtils = new HuoBiUtils();
				}
			}
		}
		return huoBiUtils;
	}
	
	/**
	 * 货币获取K线图
	 * @param symbol 交易对 格式 btcusdt 
	 * @param type 1min, 5min, 15min, 30min, 60min, 1day, 1mon, 1week, 1year
	 * @param size 1-2000
	 * @return
	 */
	public List<Kline> queryKline(String symbol, String type, String size){
		 HBApi client = new HBApi(HuobiConfig.access_key, HuobiConfig.secret_key);
		 KlineResponse<List<Kline>> kline = client.kline(symbol, type,size);
		 return kline.data;
	}
}
