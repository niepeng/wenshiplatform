package com.hsmonkey.weijifen.biz.dal.dataobject;

import java.util.Date;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2017年4月1日  下午9:44:01</p>
 * <p>作者：niepeng</p>
 */
public class ApiAccessTokenDO extends BaseDO {

	private static final long serialVersionUID = 4089288920529705771L;

	private String user;
	
	private String psw;
	
	private String accessToken;
	
	// 过期时间
	private Date expireDate;
	
	// -------------- extend attribute --------------------
	
	// 过期时间段，单位秒
	private long expiresIn;

	// -------------- normal method -----------------------

	// -------------- setter/getter -----------------------

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}
	
}
