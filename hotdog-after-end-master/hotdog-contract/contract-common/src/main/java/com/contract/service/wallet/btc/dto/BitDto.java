package com.contract.service.wallet.btc.dto;

public class BitDto {

//	"amount": "98.00000000",
//    "divisible": true,
//    "fee": "0.00017280",
//    "txid": "dabfdf60f229214ad3359cf3d4a1f6f9f502470eda68aba5d8503282e3c74d3f",
//    "ismine": true,
//    "type": "Simple Send",
//    "confirmations": 4,
//    "version": 0,
//    "sendingaddress": "1mY1oX9McZBiTNAqVaMeh3Tqcus8kJvbK",
//    "valid": true,
//    "blockhash": "0000000000000000001dfc66635f72ac785917eb25c606052f063e28643f6761",
//    "blocktime": 1555488101,
//    "positioninblock": 943,
//    "referenceaddress": "1DnqRMrcLR4j4aGQ7WZVxV9cpd2xavXyZj",
//    "block": 571997,
//    "propertyid": 31,
//    "type_int": 0
	private String amount;
	private boolean divisible;
	private String fee;
	private String txid;
	private boolean ismine;
	private String type;
	private int confirmations;
	private int version;
	private String sendingaddress;
	private boolean valid;
	private String blockhash;
	private long blocktime;
	private int positioninblock;
	private String referenceaddress;
	private int block;
	private int propertyid;
	private int type_int;
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public boolean isDivisible() {
		return divisible;
	}
	public void setDivisible(boolean divisible) {
		this.divisible = divisible;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getTxid() {
		return txid;
	}
	public void setTxid(String txid) {
		this.txid = txid;
	}
	public boolean isIsmine() {
		return ismine;
	}
	public void setIsmine(boolean ismine) {
		this.ismine = ismine;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getConfirmations() {
		return confirmations;
	}
	public void setConfirmations(int confirmations) {
		this.confirmations = confirmations;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getSendingaddress() {
		return sendingaddress;
	}
	public void setSendingaddress(String sendingaddress) {
		this.sendingaddress = sendingaddress;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getBlockhash() {
		return blockhash;
	}
	public void setBlockhash(String blockhash) {
		this.blockhash = blockhash;
	}
	public long getBlocktime() {
		return blocktime;
	}
	public void setBlocktime(long blocktime) {
		this.blocktime = blocktime;
	}
	public int getPositioninblock() {
		return positioninblock;
	}
	public void setPositioninblock(int positioninblock) {
		this.positioninblock = positioninblock;
	}
	public String getReferenceaddress() {
		return referenceaddress;
	}
	public void setReferenceaddress(String referenceaddress) {
		this.referenceaddress = referenceaddress;
	}
	public int getBlock() {
		return block;
	}
	public void setBlock(int block) {
		this.block = block;
	}
	public int getPropertyid() {
		return propertyid;
	}
	public void setPropertyid(int propertyid) {
		this.propertyid = propertyid;
	}
	public int getType_int() {
		return type_int;
	}
	public void setType_int(int type_int) {
		this.type_int = type_int;
	}
	
}
