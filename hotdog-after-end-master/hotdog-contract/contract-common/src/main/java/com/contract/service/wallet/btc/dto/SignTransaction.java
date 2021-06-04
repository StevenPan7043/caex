package com.contract.service.wallet.btc.dto;

public class SignTransaction {

		private String hex;
		private boolean complete;
		public String getHex() {
			return hex;
		}
		public void setHex(String hex) {
			this.hex = hex;
		}
		public boolean isComplete() {
			return complete;
		}
		public void setComplete(boolean complete) {
			this.complete = complete;
		}
		
}
