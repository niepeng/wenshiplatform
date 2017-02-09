package com.hsmonkey.weijifen.common.http;

public class ProxyIp {
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	// public float getSpeed() {
	// return speed;
	// }
	//
	// public void setSpeed(float speed) {
	// this.speed = speed;
	// }

	private String ip;
	private int port;

	// private float speed;

	public ProxyIp(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
		// this.speed = speed;
	}

}