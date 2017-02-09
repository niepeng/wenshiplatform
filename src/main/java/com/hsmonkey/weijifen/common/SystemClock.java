package com.hsmonkey.weijifen.common;

public class SystemClock {

	public static void sleepRandom(long min, long max) {
		long sleepTime = (long) (Math.random() * (max - min)) + min;
//		System.out.println(sleepTime);
		sleep(sleepTime);
	}

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
		}
	}
	
	public static void main(String[] args) {
		for(int i=0;i<10;i++) {
			sleepRandom(5000, 15000);
		}
	}
}
