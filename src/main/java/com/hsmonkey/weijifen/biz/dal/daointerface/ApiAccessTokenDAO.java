package com.hsmonkey.weijifen.biz.dal.daointerface;

import java.util.List;
import com.hsmonkey.weijifen.biz.dal.dataobject.ApiAccessTokenDO;
import wint.help.biz.query.BaseQuery;

/**
* this file is auto generate.
*/
public interface ApiAccessTokenDAO {

	long create(ApiAccessTokenDO apiAccessToken);

	int delete(long id);

    int update(ApiAccessTokenDO apiAccessToken);

	ApiAccessTokenDO queryById(long id);
	
	ApiAccessTokenDO queryByUserLimit1(String user);
	
	ApiAccessTokenDO queryByAccessToken(String accessToken);

    List<ApiAccessTokenDO> queryForPage(BaseQuery query);
}
