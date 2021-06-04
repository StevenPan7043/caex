package com.contract.service.wallet.btc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.contract.service.wallet.btc.dto.Fees;
import com.contract.service.wallet.btc.dto.Input;
import com.contract.service.wallet.btc.dto.RawTransaction;
import com.contract.service.wallet.btc.dto.SignTransaction;
import com.contract.service.wallet.btc.dto.UtxoInfo;
import com.contract.service.wallet.btc.enums.NodeEnums;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

public class UsdtSend {

    private static BigDecimal usdtBasicFee = new BigDecimal("0.00000546");


    public static String sendrawtransaction(NodeEnums n,String sender, String feeAddress, String receive, String amountStr) throws Throwable {
    	    System.out.println("Sender:{"+sender+"} Receiver:{"+receive+"} Fee:{"+feeAddress+"}  outAmount:{"+amountStr+"}" );
    	
    	    String creb = Base64.encodeBase64String((n.getUser() + ":" + n.getPassword()).getBytes());
		Map<String, String> headers = new HashMap<>(2);
		headers.put("Authorization", "Basic " + creb);
		headers.put("server", "1");
		JsonRpcHttpClient client = new JsonRpcHttpClient(new URL("http://"+n.getUrl()+":"+n.getPort()), headers);
		
        List<UtxoInfo> utxoList = new ArrayList<>();
        // 1.
        // 查询Sender地址Utxo
        String senderUtxoResult = JSONArray.toJSONString(client.invoke("listunspent", new Object[]{0, 999999, new String[]{sender}}, Object[].class));
        System.out.println("Sender utxo result:{}"+senderUtxoResult);
        for (UtxoInfo utxo : JSON.parseArray(senderUtxoResult, UtxoInfo.class)) {
            BigDecimal utxoAmount =utxo.getAmount();
            utxo.setValue(utxoAmount.toPlainString());
            if (utxoAmount.compareTo(usdtBasicFee) >=0) {
                utxoList.add(utxo);
                break;
            }
        }
        if (utxoList.isEmpty()) {
            System.out.println("Sender {} utxo insufficient."+ sender);
            throw new Exception("Sender utxo insufficient.");
        }

        // 手续费
        BigDecimal fee = new BigDecimal(String.valueOf((2 * 148 + 34 * 3 + 10))).multiply(new BigDecimal(getFeeRate())).multiply(new BigDecimal("0.00000001"));
        System.out.println("Sender:{"+sender+"} Receiver:{"+receive+"} Fee:{"+feeAddress+"} feeAmount:{"+fee.toPlainString()+"} outAmount:{"+amountStr+"}" );
        // 查询BTC地址Utxo
        // 自己拼接input,USDT地址放在第一个，旷工费地址放在下面就可以了 凌晨3点钟效率最高转账
        String feeUtxoResult = JSONArray.toJSONString(client.invoke("listunspent", new Object[]{0, 999999, new String[]{feeAddress}}, Object[].class));
        System.out.println("Fee utxo result:{}"+ feeUtxoResult);
        for (UtxoInfo utxo : JSON.parseArray(feeUtxoResult, UtxoInfo.class)) {
            BigDecimal utxoAmount =utxo.getAmount();
            utxo.setValue(utxoAmount.toPlainString());
            if (utxoAmount.compareTo(usdtBasicFee) >=0 && utxoAmount.compareTo(fee) >=0) {
                utxoList.add(utxo);
                break;
            }
        }
        if (utxoList.size() != 2) {
            System.out.println("Fee {} utxo insufficient."+feeAddress);
            throw new Exception("Fee utxo insufficient.");
        }

        List<RawTransaction> rawTransactionList = new ArrayList<>();
        List<Input> utxoInputList = new ArrayList<>();
        for (UtxoInfo utxo : utxoList) {
            RawTransaction rawTransaction = new RawTransaction();
            rawTransaction.setTxid(utxo.getTxid());
            rawTransaction.setVout(utxo.getVout());
            rawTransactionList.add(rawTransaction);
            Input input = new Input(utxo.getTxid(), utxo.getVout(), utxo.getScriptPubKey(), utxo.getValue());
            utxoInputList.add(input);
        }
      
        // 2 构造发送代币类型和代币数量数据（payload）
        String payload = client.invoke("omni_createpayload_simplesend", new Object[]{31, amountStr}, String.class);
        System.out.println("第2步返回:{}"+ payload);
        // 3 构造交易基本数据（transaction base）
        String txBaseStr = createRawTransaction(client, rawTransactionList);
        System.out.println("第3步返回:{}"+ txBaseStr);
        // 4 在交易数据中加上omni代币数据
        String opreturn = client.invoke("omni_createrawtx_opreturn", new Object[]{txBaseStr, payload}, String.class);
        System.out.println("第4步返回:{}"+opreturn);
        // 5在交易数据上加上接收地址
        String reference = client.invoke("omni_createrawtx_reference", new Object[]{opreturn, receive}, String.class);
        System.out.println("第5步返回:{}"+ reference);
        // 6 在交易数据上指定矿工费用
        String createRaw = omniCreateRawTxChange(client, reference, utxoInputList, feeAddress, fee.toPlainString());
        System.out.println("第6步返回:{}"+ createRaw);

        // 7 签名
        SignTransaction signTransaction = client.invoke("signrawtransaction", new Object[]{createRaw}, SignTransaction.class);
        System.out.println("第7步返回:{}"+ JSON.toJSONString(signTransaction));
        if (signTransaction.isComplete()) {
            // 8 广播
        		try {
        		    Object txid = client.invoke("sendrawtransaction", new Object[]{signTransaction.getHex()}, Object.class);
                System.out.println("第8步返回:{}"+ JSON.toJSONString(txid));
                return txid.toString();
			} catch (Exception e) {
				e.printStackTrace();
				if("64: too-long-mempool-chain".equals(e.getMessage())) {
					return "-999";
				}
			}
           
        }
        return null;
    }


    /**
     * 构建交易基础(3)
     *
     * @param params
     * @return
     */
    public static String createRawTransaction(JsonRpcHttpClient client, List<RawTransaction> params) {
        String result = "";
        try {
            Map data = new HashMap();
            System.out.println("第3步参数:{}"+ JSON.toJSONString(params));
            result = client.invoke("createrawtransaction", new Object[]{params, data}, String.class);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param client
     * @param reference 第五步返回的参数
     * @param params    UTXO账本列表
     * @return
     */
    public static String omniCreateRawTxChange(JsonRpcHttpClient client, String reference, List<Input> params, String feeAddress, String fee) {
        String result = "";
        try {
        	 System.out.println("第6步参数:{}"+ JSON.toJSONString(params));
            result = client.invoke("omni_createrawtx_change", new Object[]{reference, params, feeAddress, fee}, String.class);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getFeeRate() {
//        String httpResult = "";
//        CloseableHttpClient httpCilent = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet("https://bitcoinfees.earn.com/api/v1/fees/recommended");
//        try {
//            CloseableHttpResponse response = httpCilent.execute(httpGet);
//            httpResult = convertStreamToString(response.getEntity().getContent());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                httpCilent.close();//释放资源
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        Fees fees = JSON.parseObject(httpResult, Fees.class);
//        fees.setFastestFee(new BigDecimal(120));
        return "80";
    }
    /**
     * input转String
     *
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
    		for(int i=0;i<30;i++) {
    			int a=(int)(Math.random()*2);
    			System.out.println(a);
    		}
//		System.out.println(UsdtConfig.getInstance(NodeEnums.node_1).getN().getId());
//		System.out.println(UsdtConfig.getInstance(NodeEnums.node_2).getN().getId());
	}
}