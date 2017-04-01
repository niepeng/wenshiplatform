package com.hsmonkey.weijifen.biz.dal.ibatis;

import java.util.ArrayList;
import java.util.List;

import wint.help.biz.query.BaseQuery;

import com.hsmonkey.weijifen.biz.dal.daointerface.KeyValueDAO;
import com.hsmonkey.weijifen.biz.dal.dataobject.KeyValueDO;

/**
* this file is auto generate.
*/
public class KeyValueDAOIbatis extends BaseIbatisDAO implements KeyValueDAO {


    @Override
	public long create(KeyValueDO keyValue) {
		return (Long)this.getSqlMapClientTemplate().insert("KeyValueDAO.create", keyValue);
	}

    @Override
	public int delete(long id) {
		return this.getSqlMapClientTemplate().update("KeyValueDAO.delete", id);
	}

    @Override
	public int update(KeyValueDO keyValue) {
		return this.getSqlMapClientTemplate().update("KeyValueDAO.update", keyValue);
	}

    @Override
	public KeyValueDO queryById(long id) {
		return (KeyValueDO)this.getSqlMapClientTemplate().queryForObject("KeyValueDAO.queryById", id);
	}

    @SuppressWarnings("unchecked")
	@Override
    public List<KeyValueDO> queryForPage(BaseQuery query) {
        int count = (Integer)this.getSqlMapClientTemplate().queryForObject("KeyValueDAO.queryForPageCount", query);
        if (count == 0) {
            return new ArrayList<KeyValueDO>(0);
        }
        query.setTotalResultCount(count);
        return (List<KeyValueDO>)this.getSqlMapClientTemplate().queryForList("KeyValueDAO.queryForPage", query);
    }

	@Override
	public KeyValueDO queryByKey(String key) {
		return (KeyValueDO)this.getSqlMapClientTemplate().queryForObject("KeyValueDAO.queryByKey", key);
	}
    
	@Override
	public KeyValueDO queryByKeyDesc(String key) {
		return (KeyValueDO)this.getSqlMapClientTemplate().queryForObject("KeyValueDAO.queryByKeyDesc", key);
	}

}
