package com.hsmonkey.weijifen.biz.dal.daointerface;

import java.util.List;

import com.hsmonkey.weijifen.biz.dal.dataobject.KeyValueDO;

import wint.help.biz.query.BaseQuery;

/**
* this file is auto generate.
*/
public interface KeyValueDAO {

	long create(KeyValueDO keyValue);

	int delete(long id);

    int update(KeyValueDO keyValue);

	KeyValueDO queryById(long id);
	
	KeyValueDO queryByKey(String key);

    List<KeyValueDO> queryForPage(BaseQuery query);

	KeyValueDO queryByKeyDesc(String key);
}
