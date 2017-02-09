package com.hsmonkey.weijifen.biz.ao;

import com.hsmonkey.weijifen.biz.dal.daointerface.KeyValueDAO;
import com.hsmonkey.weijifen.web.action.BaseAction;

public class BaseAO extends BaseAction {
	
	protected KeyValueDAO keyValueDAO;

	public void setKeyValueDAO(KeyValueDAO keyValueDAO) {
		this.keyValueDAO = keyValueDAO;
	}
	
}
