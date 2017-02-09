package com.hsmonkey.weijifen.biz.dal.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Description: 所有DO的父类</p>
 * <p>Company: wedo</p>
 * @author wull
 * @version 1.0
 */
public class BaseDO implements Serializable {

	private static final long serialVersionUID = -5157130340054747503L;

	protected long id;

	protected Date gmtCreate;

	protected Date gmtModified;


	// -------------- extend attribute -----------------------

	// -------------- normal moethod -------------------------

	// -------------- setter/getter --------------------------

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

}