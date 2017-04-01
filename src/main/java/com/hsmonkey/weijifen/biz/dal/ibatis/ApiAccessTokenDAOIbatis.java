package com.hsmonkey.weijifen.biz.dal.ibatis;

import java.util.List;
import java.util.ArrayList;

import com.hsmonkey.weijifen.biz.dal.daointerface.ApiAccessTokenDAO;
import com.hsmonkey.weijifen.biz.dal.dataobject.ApiAccessTokenDO;

/*  import wint.dal.ibatis.ReadWriteSqlMapClientDaoSupport;   */
import wint.help.biz.query.BaseQuery;

/**
* this file is auto generate.
*/
public class ApiAccessTokenDAOIbatis extends BaseIbatisDAO implements ApiAccessTokenDAO {


    @Override
	public long create(ApiAccessTokenDO apiAccessToken) {
		return (Long)this.getSqlMapClientTemplate().insert("ApiAccessTokenDAO.create", apiAccessToken);
	}

    @Override
	public int delete(long id) {
		return this.getSqlMapClientTemplate().update("ApiAccessTokenDAO.delete", id);
	}

    @Override
	public int update(ApiAccessTokenDO apiAccessToken) {
		return this.getSqlMapClientTemplate().update("ApiAccessTokenDAO.update", apiAccessToken);
	}

    @Override
	public ApiAccessTokenDO queryById(long id) {
		return (ApiAccessTokenDO)this.getSqlMapClientTemplate().queryForObject("ApiAccessTokenDAO.queryById", id);
	}
    
    @Override
	public ApiAccessTokenDO queryByUserLimit1(String user) {
		return (ApiAccessTokenDO)this.getSqlMapClientTemplate().queryForObject("ApiAccessTokenDAO.queryByUserLimit1", user);
	}

	@Override
	public ApiAccessTokenDO queryByAccessToken(String accessToken) {
		return (ApiAccessTokenDO)this.getSqlMapClientTemplate().queryForObject("ApiAccessTokenDAO.queryByAccessToken", accessToken);
	}

	@Override
    public List<ApiAccessTokenDO> queryForPage(BaseQuery query) {
        int count = (Integer)this.getSqlMapClientTemplate().queryForObject("ApiAccessTokenDAO.queryForPageCount", query);
        if (count == 0) {
            return new ArrayList<ApiAccessTokenDO>(0);
        }
        query.setTotalResultCount(count);
        return (List<ApiAccessTokenDO>)this.getSqlMapClientTemplate().queryForList("ApiAccessTokenDAO.queryForPage", query);
    }

}
