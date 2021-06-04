package com.contract.service.wallet.btc.dto;

public class Input {

	
	private String txid;
	private int vout;
	private String value;
	private String scriptPubKey;
	public String getTxid() {
		return txid;
	}
	public void setTxid(String txid) {
		this.txid = txid;
	}
	public int getVout() {
		return vout;
	}
	public void setVout(int vout) {
		this.vout = vout;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getScriptPubKey() {
		return scriptPubKey;
	}
	public void setScriptPubKey(String scriptPubKey) {
		this.scriptPubKey = scriptPubKey;
	}
	public Input(String txid,int vout,String scriptPubKey,String value) {
		this.txid=txid;
		this.vout=vout;
		this.scriptPubKey=scriptPubKey;
		this.value=value;
	}
	
}
