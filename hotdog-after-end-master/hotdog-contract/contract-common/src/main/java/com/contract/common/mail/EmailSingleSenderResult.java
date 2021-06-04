package com.contract.common.mail;

public class EmailSingleSenderResult {
/*
{
    "result": 0,
    "errmsg": "OK", 
    "ext": "", 
    "sid": "xxxxxxx", 
    "fee": 1
}
 */
	public int result;
	public String errmsg ;
	public Integer surplus;
	public String sequenceId;
	
	
	public int getResult() {
		return result;
	}


	public void setResult(int result) {
		this.result = result;
	}


	public String getErrmsg() {
		return errmsg;
	}


	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}


	public Integer getSurplus() {
		return surplus;
	}


	public void setSurplus(Integer surplus) {
		this.surplus = surplus;
	}


	public String getSequenceId() {
		return sequenceId;
	}


	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}


	@Override
	public String toString() {
		return "EmailSingleSenderResult [result=" + result + ", errmsg=" + errmsg + ", surplus=" + surplus
				+ ", sequenceId=" + sequenceId + "]";
	}
	
	
}
