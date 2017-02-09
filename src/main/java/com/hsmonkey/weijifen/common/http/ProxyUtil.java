package com.hsmonkey.weijifen.common.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 代理IP工具类
 *
 * @author xiaoqi
 * @date 2015年4月15日 下午9:31:09
 */
public class ProxyUtil {

	public static boolean open = false;

	private static PriorityBlockingQueue<ProxyIp> piQueue = new PriorityBlockingQueue<ProxyIp>(
			200, new Comparator<ProxyIp>() {
				@Override
				public int compare(ProxyIp o1, ProxyIp o2) {
					// if (o2.getSpeed() - o1.getSpeed() < 0) {
					// return -1;
					// } else if (o2.getSpeed() - o1.getSpeed() > 0) {
					// return 1;
					// } else {
					return 0;
					// }
				}
			});

	public static ProxyIp getProxyIp() {
		ProxyIp pi = null;
		while (pi == null) {
			try {
				pi = piQueue.take();
				if (!get_thread.isAlive() && piQueue.size() < 20) {
					initProxyIpQueue();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return pi;
	}

	private static HttpClient client = new HttpClient(false);

	private static String api_url = "http://www.kuaidaili.com/api/getproxy/?orderid=952794181928374&port=80&num=500&browser=1&protocol=1&method=1&sort=0&format=text&sep=1";

	private static Thread get_thread;

	static {
		initProxyIpQueue();
	}

	private static void initProxyIpQueue() {
		get_thread = new Thread(new Runnable() {
			@Override
			public void run() {
				String content = client.subGet(api_url, Charset.UTF8);
				String ips[] = content.split("\n");
				if (ips.length > 1) {
					open = true;
					for (final String ip : ips) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								String[] a_p = ip.trim().split(":");
								String addr = a_p[0];
								int port = Integer.valueOf(a_p[1]);
								if (testConnSpeed(addr, port)) {
									piQueue.add(new ProxyIp(addr, port));
								}
								System.out.println(ip);
							}
						}).start();
					}
				} else {
					open = false;
				}
			}
		});
		get_thread.start();
	}

	private static boolean testConnSpeed(String ip, int port) {
		boolean bo = true;
		final int connect_timeout = 1000;// 连接超时,毫秒
		final int timeout = 1 * 1000;// 建立连接的超时
		Socket conn = null;
		try {
			conn = new Socket();
			conn.connect(new InetSocketAddress(ip, port), connect_timeout);
			conn.setSoTimeout(timeout);
			conn.setKeepAlive(true);
			conn.setTcpNoDelay(true);
			conn.close();
		} catch (IOException e) {
			System.out.println("ip:" + ip + "无法调通");
			bo = false;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bo;
	}
}