package com.contract.service.wallet.eth;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.contract.common.HttpUtil;
import com.contract.common.StaticUtils;
import com.contract.dao.TxLogsMapper;
import com.contract.entity.CWallet;
import com.contract.entity.TxLogs;
import com.contract.entity.TxLogsExample;
import com.contract.enums.HandleTypeEnums;
import com.contract.service.BipbService;

@Service
public class ETHService {
	@Autowired
	private BipbService bipbService;
	@Autowired
	private TxLogsMapper txLogsMapper;
	/**
	 * 充值
	 * @param list
	 */
	public void ethRecharge(List<CWallet> list) {
		for(CWallet c:list) {
			if(!StringUtils.isEmpty(c.getAddr())) {
				updateETHlist(c.getCid(), c.getLabel());
			}
		}		
	}
	
	
	/**
	 * 获取ECR20协议 代币交易记录并保存到明细表 更新交易记录
	 * @param address
	 * @return
	 */
	public String updateETHlist(Integer cid,String address){
		String contractAddress=ECR20Utils.usdt_contractAddress;
		TreeMap<String,Object> treeMap = new TreeMap<>();
		treeMap.put("module", "account");  // 模块类别
		treeMap.put("action", "tokentx");  // 请求接口
		treeMap.put("contractaddress", contractAddress);// 请求参数：代币合约地址
		treeMap.put("address", address);// 请求参数：钱包地址
		treeMap.put("startblock",0); // 请求参数：查询起始区块
		treeMap.put("endblock",999999999);  // 请求参数：查询结束区块
		treeMap.put("page", "1");
		treeMap.put("offset", "20");
		treeMap.put("sort", "desc");
		StringBuffer sbf = new StringBuffer();
		for (String str : treeMap.keySet()) {
			sbf.append(str).append("=").append(treeMap.get(str)).append("&");
		}
		String url = ECR20Utils.ecr20_url.concat("?").concat(sbf.substring(0, sbf.length() - 1));
		String send=HttpUtil.getInstance().get(url);
		if(StringUtils.isEmpty(send)) {
			return "未找到交易记录";
		}
		JSONObject object=JSONObject.parseObject(send);
		if(!"1".equals(object.getString("status"))) {
			return object.getString("message");
		}
		//根据key获取到存储到redis里面的值是否重复了
		JSONArray array=JSONArray.parseArray(object.getString("result"));
		if(array!=null && array.size()>0) {
			for(int i=0;i<array.size();i++) {
				JSONObject jsonObject=array.getJSONObject(i);
				String hash=jsonObject.getString("hash");
				String blocknumber=jsonObject.getString("blockNumber");
				String timestamp=jsonObject.getString("timeStamp");
				String nonce=jsonObject.getString("nonce");
				String blockhash=jsonObject.getString("blockHash");
				String fromaddr=jsonObject.getString("from");
				String contractaddress=jsonObject.getString("contractAddress");
				String toaddr=jsonObject.getString("to");
				String valuecoin=jsonObject.getString("value");
				String tokenname=jsonObject.getString("tokenName");
				String tokensymbol=jsonObject.getString("tokenSymbol");
				String tokendecimal=jsonObject.getString("tokenDecimal");
				String gaslimit=jsonObject.getString("gas");
				String gasprice=jsonObject.getString("gasPrice");
				String gasused=jsonObject.getString("gasUsed");
				
				//将 交易记录的金额 做精度转换
				BigDecimal usdt=ECR20Utils.toDecimal(Integer.parseInt(tokendecimal),new BigInteger(String.valueOf(valuecoin)));
				//判断当前交易单号是否存在于缓存中 存在的话表示后面的肯定都已经保存过了 直接结束掉
				TxLogs logs=new TxLogs(hash,cid, blocknumber, timestamp, nonce, blockhash, fromaddr,
						contractaddress, toaddr,valuecoin, tokenname, tokensymbol, tokendecimal, gaslimit, gasprice, gasused);
				TxLogsExample example=new TxLogsExample();
				example.createCriteria().andHashEqualTo(hash);
				int count=txLogsMapper.countByExample(example);
				if(count<1) {//没有同步过才保存 只保存收入的
					if(toaddr.equals(address)) {//如果接收方是自己表示  表示当前会员新NB数量 还未同步的数量增加
						//如果接收方是自己  表示需要转移至平台 新单进账就要判断ETH 够不够不够的话就给他冲ETH然后再把代币转到总钱包
						//这部分交由定时任务处理
						//自己接受那么NB正价
						bipbService.handleCUsdtDetail(cid, HandleTypeEnums.recharge.getId(), StaticUtils.pay_in, hash, usdt, "接受来至【"+fromaddr+"】充值(ECR20)",cid);
						txLogsMapper.insertSelective(logs);
					}
				}else {
					break;
				}
			}
		}
		return "";
	}

	
}
