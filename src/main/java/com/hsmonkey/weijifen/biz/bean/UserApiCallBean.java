package com.hsmonkey.weijifen.biz.bean;

import com.google.common.util.concurrent.RateLimiter;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2017年4月5日  下午8:24:43</p>
 * <p>作者：niepeng</p>
 */
public class UserApiCallBean {

	private RateLimiter deviceListLimiter;
	
	private RateLimiter inTimeDataLimiter;
	
	public UserApiCallBean() {
		// 参数是每秒调用次数限制，6/60代表1分钟限制调用6次
		this(6.d/60, 6.d/60);
	}
	
	public UserApiCallBean(double device, double inTime) {
		deviceListLimiter = RateLimiter.create(device);
		inTimeDataLimiter = RateLimiter.create(inTime);
	}

	public RateLimiter getDeviceListLimiter() {
		return deviceListLimiter;
	}

	public RateLimiter getInTimeDataLimiter() {
		return inTimeDataLimiter;
	}
	
}
