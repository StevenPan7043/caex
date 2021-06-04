package com.contract.service.wallet.btc.dto;

public class RawTransaction {

	private String txid;
	private int vout;
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
	
}
