package com.pmzhongguo.zzextool.utils;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

public class IPAddressPortUtil {
	
	public static final String IP_ADDRESS = getIPAddress();

	/**
	 * 获取本机局域网IP
	 * 
	 * @return
	 */
	private static String getIPAddress() {
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ip;
	}

	/**
	 * 	获取tomcat端口
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
	public static int getTomcatPort() {
		String port = "0";
		try {
			MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
			Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
					Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
			port = objectNames.iterator().next().getKeyProperty("port");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return Integer.valueOf(port);
	}
}
