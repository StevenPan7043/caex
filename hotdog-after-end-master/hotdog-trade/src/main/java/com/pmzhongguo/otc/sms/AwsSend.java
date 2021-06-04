package com.pmzhongguo.otc.sms;

import com.amazonaws.services.sns.model.PublishResult;

public class AwsSend implements Runnable {
	
	AwsSnsUtil awsSMS = new AwsSnsUtil();
	
	String phoneNumber;
	String message;

	public AwsSend(String phoneNumber, String message) {
		super();
		this.phoneNumber = phoneNumber;
		this.message = message;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		PublishResult publishResult = awsSMS.sendSMSMessage(phoneNumber, message);
	}
}
