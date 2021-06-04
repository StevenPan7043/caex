package com.contract.service.wallet.eth;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import com.contract.exception.ThrowJsonException;
import com.contract.service.ConfigCore;
import com.fasterxml.jackson.databind.ObjectMapper;

import common.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 以太坊基于ECR20协议 工具类
 * @author arno
 *
 */
public class ECR20Utils{
	private static Logger logger =Logger.getLogger(ECR20Utils.class);
	
	public static final String path="/Users/arno/Documents/myGitHub/";//测试环境 注意生成钱包时候的文件目录地址 上线后钱包文件地址必须保持一致 不然解析出来的钱包地址不一样
	public static final String ecr20_url="https://api.etherscan.io/api/";
	public static final String usdt_contractAddress="0xdac17f958d2ee523a2206206994597c13d831ec7";
	
	public static final String GAS_PRICE="20";
	public static final String GAS_LIMIT="60000";
	
	/**
	 * 创建钱包地址
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws InvalidAlgorithmParameterException
	 * @throws CipherException
	 * @throws IOException
	 */
	public static String creatAccount(String passWord) {
		try {
			String filename="";//文件名
		    //钱包文件保持路径，请替换位自己的某文件夹路径  生成证书 密码为用户账号
		    filename = WalletUtils.generateNewWalletFile(passWord, new File(path),true);
		    String url=path+filename;
		    logger.info("生成会员钱包文件路径："+url);
		    return url;
		}catch (NoSuchProviderException e) {
			throw new ThrowJsonException("未找到服务异常");
		}catch (NoSuchAlgorithmException e) {
			throw new ThrowJsonException("加密算法异常");
		}catch (InvalidAlgorithmParameterException e) {
			throw new ThrowJsonException("证书异常");
		}catch (CipherException e) {
			throw new ThrowJsonException("初始化异常");
		}catch (IOException e) {
			throw new ThrowJsonException("文件流异常");
		}
	}
	
	/**
	 * 根据会员钱包文件获取用户钱包地址 公钥私钥
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws CipherException
	 */
	public static Credentials loadWallet(String walleFilePath,String passWord) {
		try {
			Credentials credentials = WalletUtils.loadBip39Credentials(passWord, walleFilePath);
		    return credentials;
		} catch (Exception e) {
			throw new ThrowJsonException("钱包地址解析失败");
		}
	}
	
	/**
	 * 校验ETH钱包地址
	 * @param input
	 * @return
	 */
	public static boolean isETHValidAddress(String input) {
        if (StringUtils.isBlank(input) || !input.startsWith("0x")) {
        		return false;
        }
        String cleanInput = Numeric.cleanHexPrefix(input);
        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }
        return cleanInput.length() == 40;
    }
	
	/**
	 * 获取以太坊余额
	 * @param web3j
	 * @throws IOException
	 */
	public static BigDecimal getBlanceOf(String address) {
		try {
			//第二个参数：区块的参数，建议选最新区块
		    BigInteger balance = ConfigCore.web3j.ethGetBalance(address,DefaultBlockParameterName.LATEST).send().getBalance();
		    //格式转化 wei-ether
		    BigDecimal blanceETH = Convert.fromWei(balance.toString(), Convert.Unit.ETHER);
		    return blanceETH;
		} catch (Exception e) {
			throw new ThrowJsonException("获取ETH余额异常");
		}
	}
	
	
	/**
	 * 查询代币余额
	 * @param url  ecr20接口地址 默认 https://api.etherscan.io/api/
	 * @param contractAddress 合约地址  合约地址是创建代币的时候有的
	 * @param address 钱包地址
	 * @param decimal 经度
	 * @return
	 */
	public static BigDecimal getERC20Balance(String contractAddress,String address,int decimal){
		TreeMap<String,String> treeMap = new TreeMap<>();
		treeMap.put("module", "account");
		treeMap.put("action", "tokenbalance");
		treeMap.put("contractaddress", contractAddress);
		treeMap.put("address", address);
		treeMap.put("tag", "latest");
		treeMap.put("apikey", contractAddress);
		StringBuffer sbf = new StringBuffer();
		for (String str : treeMap.keySet()) {
			sbf.append(str).append("=").append(treeMap.get(str)).append("&");
		}
		String url = ecr20_url.concat("?").concat(sbf.substring(0, sbf.length() - 1));
		Object send = send(url);
		return toDecimal(decimal,new BigInteger(String.valueOf(send)));
	}
	
	
	/**
	 * 转换成符合 decimal 的数值
	 * @param decimal
	 * @param str
	 * @return
	 */
	public static BigDecimal toDecimal(int decimal,BigInteger integer){
		StringBuffer sbf = new StringBuffer("1");
		for (int i = 0; i < decimal; i++) {
			sbf.append("0");
		}
		BigDecimal balance = new BigDecimal(integer).divide(new BigDecimal(sbf.toString()), decimal, BigDecimal.ROUND_DOWN);
		return balance;
	}
	
	public static Object send(String url){
		 String responseMessageGet =getResponseMessageGet(url);
		 ObjectMapper mapper = new ObjectMapper();
		 Map<String,String> reMap = null;
		 try {
			reMap = mapper.readValue(responseMessageGet, new com.fasterxml.jackson.core.type.TypeReference<Map<String,Object>>(){});
		} catch (Exception e) {
			throw new ThrowJsonException("ECR20协议请求错误");
		}
		 return reMap.get("result");
	}
	
	/**
	 * okhttp get 请求
	 */
	public static ResponseBody testGetClient(String url){
		OkHttpClient okClient = new OkHttpClient();
		Request request = new Request.Builder().get().url(url).build();
		ResponseBody resBody = null;
		try {
			Response response = okClient.newCall(request).execute();
			resBody = response.body();
		} catch (IOException e) {
			throw new ThrowJsonException("ECR20协议请求错误01");
		}
		return resBody;
	}
	
	public static String getResponseMessageGet(String url) {
		ResponseBody body = null;String string = null;
		body = testGetClient(url);
		if(checkEmpty(body)) return null;
		try {
			string = body.string();
		} catch (IOException e) {
			throw new ThrowJsonException("ECR20协议请求错误02");
		}
		return string;
	}
	
	public static boolean checkEmpty(Object obj){
		if(null!=obj && !obj.toString().trim().equals("")){
			return false;
		}
		return true;
		
	}

	/**
	 * 转账
	 * @param credentials
	 * @param contractAddress
	 * @param toAddress
	 * @param amount
	 * @return
	 */
	public static String ecr20Trans(Credentials credentials,String contractAddress,String toAddress,BigDecimal amount,BigDecimal fee) {
		String fromAddress = credentials.getAddress();
		if (fee == null || fee.compareTo(BigDecimal.ZERO) <= 0) {
			BigInteger bigInteger=new BigInteger(GAS_LIMIT);
            fee = getMinerFee(bigInteger);
        }
		BigDecimal fromfee=getBlanceOf(fromAddress);
        if(fromfee.compareTo(fee) < 0){
	         throw new ThrowJsonException("ETH手续费不足");
        }
        boolean flag=ECR20Utils.isETHValidAddress(toAddress);
        if(!flag) {
        		throw new ThrowJsonException("提币地址错误{"+toAddress+"}");
        }
        try {
        		EthGetTransactionCount ethGetTransactionCount = ConfigCore.web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            BigInteger gasPrice = getGasPrice();
            BigInteger value=Convert.toWei(amount, Convert.Unit.WEI).toBigInteger();
            System.out.println("value:"+value);
            Function fn = new Function("transfer", Arrays.asList(new Address(toAddress), new Uint256(value)), Collections.<TypeReference<?>> emptyList());
            String data = FunctionEncoder.encode(fn);
            BigInteger maxGas=Convert.toWei(fee, Convert.Unit.ETHER).toBigInteger().divide(gasPrice);
            System.out.println("from={"+credentials.getAddress()+"},value={"+value+"},gasPrice={"+gasPrice+"},gasLimit={"+maxGas+"},nonce={"+nonce+"},address={"+toAddress+"}");
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce, gasPrice, maxGas, contractAddress, data);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction =  ConfigCore.web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();
            if(ethSendTransaction.getError()!=null) {
				System.out.println("错误信息："+ethSendTransaction.getError().getMessage());
			}
			System.out.println("交易单号："+transactionHash);
			return transactionHash;
		} catch (InterruptedException e) {
			throw new ThrowJsonException("线程异常");
		}catch (ExecutionException e) {
			throw new ThrowJsonException("任务异常");
		}
	}
	
	/**
	 * 计算手续费
	 * @param gasLimit
	 * @return
	 */
	public static BigDecimal getMinerFee(BigInteger gasLimit){
        BigDecimal fee = new BigDecimal(getGasPrice().multiply(gasLimit));
        return Convert.fromWei(fee, Convert.Unit.ETHER);
    }
	/**
	 * 计算手续费
	 * @return
	 */
	public static BigInteger getGasPrice(){
		try {
			EthGasPrice gasPrice = ConfigCore.web3j.ethGasPrice().send();
	        return gasPrice.getGasPrice();
		} catch (Exception e) {
			throw new ThrowJsonException("链接ETH失败，未获取到费率");
		}
    }
	
	/**
	 * web3j 基于ECR20协议 代币互转
	 * @param password 钱包密码
	 * @param credentials 发送方 用户钱包文件解析对象
	 * @param contractAddress 合约地址
	 * @param toAddress 接收方地址
	 * @param num 表示为给对方转账数量
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static String ecr20Trans1(Credentials credentials,String contractAddress,String toAddress,String num,BigInteger gasprice,BigInteger gaslimit) {
		try {
			String fromAddress = credentials.getAddress();
			EthGetTransactionCount ethGetTransactionCount = ConfigCore.web3j.ethGetTransactionCount(
			        fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
			BigInteger nonce = ethGetTransactionCount.getTransactionCount();
			Address address=null;
			try {
				 address = new Address(toAddress);
			} catch (Exception e) {
				throw new ThrowJsonException("接收ETH钱包的地址错误");
			}
			BigInteger amount=Convert.toWei(num, Convert.Unit.WEI).toBigInteger();
			Uint256 value = new Uint256(amount);
			List<Type> parametersList = new ArrayList<>();
			parametersList.add(address);
			parametersList.add(value);
			
			List<TypeReference<?>> outList = new ArrayList<>();
			
			Function function = new Function("transfer", parametersList, outList);
			String encodedFunction = FunctionEncoder.encode(function);
			
			BigInteger gas=gasprice;
			BigInteger limit=gaslimit;//Convert.toWei(gaslimit, Convert.Unit.WEI).toBigInteger();//  DefaultGasProvider.GAS_LIMIT;
			
			RawTransaction rawTransaction = RawTransaction.createTransaction(nonce,gas,
					limit, contractAddress, encodedFunction);
			
			byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
			String hexValue = Numeric.toHexString(signedMessage);
			EthSendTransaction ethSendTransaction = ConfigCore.web3j.ethSendRawTransaction(hexValue).send();
			String transactionHash = ethSendTransaction.getTransactionHash();
			if(ethSendTransaction.getError()!=null) {
				System.out.println("错误信息："+ethSendTransaction.getError().getMessage());
			}
			System.out.println("交易单号："+transactionHash);
			return transactionHash;
		}catch (InterruptedException e) {
			throw new ThrowJsonException("线程异常");
		}catch (ExecutionException e) {
			throw new ThrowJsonException("任务异常");
		}catch (IOException e) {
			throw new ThrowJsonException("IO异常");
		}
	}
	
	
	/**
	 * ETH 基于web3j互转
	 * @param credentials
	 * @param toaddr
	 * @param num
	 * @return
	 * @throws Exception
	 */
	public static String transto(Credentials credentials,String toaddr,BigInteger num,BigInteger gasprice,BigInteger gaslimit){
		try {
			BigInteger gas=gasprice;//Convert.toWei(gasprice, Convert.Unit.GWEI).toBigInteger();// DefaultGasProvider.GAS_PRICE;
			BigInteger limit=gaslimit;//Convert.toWei(gaslimit, Convert.Unit.WEI).toBigInteger();
			
			EthGetTransactionCount transactionCount = ConfigCore.web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
	        BigInteger nonce = transactionCount.getTransactionCount();
	        
	        //创建RawTransaction交易对象
	        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gas, limit,toaddr, num);
	        //签名Transaction，这里要对交易做签名
	        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
	        String hexValue = Numeric.toHexString(signMessage);
	        //发送交易
	        EthSendTransaction ethSendTransaction = ConfigCore.web3j.ethSendRawTransaction(hexValue).sendAsync().get();
	        String hashcode=ethSendTransaction.getTransactionHash();
	        System.out.println("ETH交易单号："+hashcode);
	        return hashcode;
		} catch (Exception e) {
			throw new ThrowJsonException("转账异常");
		}
    }
	
	/**
     * 根据hash值获取交易
     *
     * @param hash
     * @return
     * @throws IOException
     */
	public static EthGetTransactionReceipt getTransactionByHash(String hash) {
		try {
			EthGetTransactionReceipt request = ConfigCore.web3j.ethGetTransactionReceipt(hash).send();
			return request;
		} catch (Exception e) {
			throw new ThrowJsonException("流异常");
		}
	}
	
}
