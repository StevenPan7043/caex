package com.contract.enums;

public enum ETHEnums {

	eth1("eth1","0xc2d9ac06ba661fd828c695636b63b5969f7eb35b","/Users/arno/Documents/myGitHub/UTC--2019-11-09T19-56-41.278000000Z--862cfbcec981c0a87b0b561526281b527bab79b6.json","123456"),
	eth2("eth2","0x234beb8f836a85a7b3f76535e830538b2940cf18","/Users/arno/Documents/myGitHub/UTC--2019-11-09T19-57-04.14000000Z--023cdd4fa1a8aeadb955c6dcc04bd63c262ffb0c.json","123456"),
	eth3("eth3","0xe40080d540dd8acc9aedcc5e99e62f4e09d25fb5","/Users/arno/Documents/myGitHub/UTC--2019-11-09T19-57-53.868000000Z--21d43b89129277022b52296f0e8d12fb8504783c.json","123456"),
	eth4("eth4","0xdb3211abcfc005c1effcf8f8c7d753e7e033623a","/Users/arno/Documents/myGitHub/UTC--2019-11-09T19-58-11.871000000Z--a07056d698767e30c69a410699b2e5db86f96821.json","123456");
	
	
	private String key;
	private String addr;
	private String path;
	private String password;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	private ETHEnums(String key,String addr,String path,String password) {
		this.key=key;
		this.addr=addr;
		this.path=path;
		this.password=password;
	}
}
