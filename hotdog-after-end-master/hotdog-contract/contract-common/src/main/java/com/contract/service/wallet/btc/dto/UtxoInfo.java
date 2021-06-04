package com.contract.service.wallet.btc.dto;

import java.math.BigDecimal;

public class UtxoInfo {

	private String txid;
	private int vout;
	private String address;
	private String account;
	private String scriptPubKey;
	private BigDecimal amount;
	private int confirmations;
	private boolean spendable;
	private String value;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getScriptPubKey() {
		return scriptPubKey;
	}
	public void setScriptPubKey(String scriptPubKey) {
		this.scriptPubKey = scriptPubKey;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public int getConfirmations() {
		return confirmations;
	}
	public void setConfirmations(int confirmations) {
		this.confirmations = confirmations;
	}
	public boolean isSpendable() {
		return spendable;
	}
	public void setSpendable(boolean spendable) {
		this.spendable = spendable;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
