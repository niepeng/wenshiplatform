package com.hsmonkey.weijifen.biz.dal;

import junit.framework.Assert;

import com.hsmonkey.weijifen.BaseTest;
import com.hsmonkey.weijifen.biz.dal.daointerface.ApiAccessTokenDAO;
import com.hsmonkey.weijifen.biz.dal.dataobject.ApiAccessTokenDO;

/**
* this file is auto generate.
*/
public class TestsApiAccessTokenDAO extends BaseTest {

	private ApiAccessTokenDAO apiAccessTokenDAO;

    public void testCreate() {
        ApiAccessTokenDO apiAccessTokenDO = new ApiAccessTokenDO();
		apiAccessTokenDO.setGmtModified(new java.util.Date());
		apiAccessTokenDO.setPsw("a");
		apiAccessTokenDO.setAccessToken("a");
		apiAccessTokenDO.setGmtCreate(new java.util.Date());
		apiAccessTokenDO.setUser("a");
		long id = apiAccessTokenDAO.create(apiAccessTokenDO);
		Assert.assertTrue(id > 0);
	}

    public void testQueryById() {
        ApiAccessTokenDO apiAccessTokenDO = new ApiAccessTokenDO();
		apiAccessTokenDO.setGmtModified(new java.util.Date());
		apiAccessTokenDO.setPsw("a");
		apiAccessTokenDO.setAccessToken("a");
		apiAccessTokenDO.setGmtCreate(new java.util.Date());
		apiAccessTokenDO.setUser("a");
        long id = apiAccessTokenDAO.create(apiAccessTokenDO);
        Assert.assertTrue(id > 0);
        ApiAccessTokenDO apiAccessTokenDO_2 = apiAccessTokenDAO.queryById(id);
        Assert.assertNotNull(apiAccessTokenDO_2);
    }

    public void testDelete() {
        ApiAccessTokenDO apiAccessTokenDO = new ApiAccessTokenDO();
		apiAccessTokenDO.setGmtModified(new java.util.Date());
		apiAccessTokenDO.setPsw("a");
		apiAccessTokenDO.setAccessToken("a");
		apiAccessTokenDO.setGmtCreate(new java.util.Date());
		apiAccessTokenDO.setUser("a");
        long id = apiAccessTokenDAO.create(apiAccessTokenDO);
        Assert.assertTrue(id > 0);
        ApiAccessTokenDO apiAccessTokenDO_2 = apiAccessTokenDAO.queryById(id);
        Assert.assertNotNull(apiAccessTokenDO_2);
        apiAccessTokenDAO.delete(id);
        apiAccessTokenDO_2 = apiAccessTokenDAO.queryById(id);
        Assert.assertNull(apiAccessTokenDO_2);
    }

    public void testUpdate() {
        ApiAccessTokenDO apiAccessTokenDO = new ApiAccessTokenDO();
		apiAccessTokenDO.setGmtModified(new java.util.Date());
		apiAccessTokenDO.setPsw("a");
		apiAccessTokenDO.setAccessToken("a");
		apiAccessTokenDO.setGmtCreate(new java.util.Date());
		apiAccessTokenDO.setUser("a");
        long id = apiAccessTokenDAO.create(apiAccessTokenDO);
        Assert.assertTrue(id > 0);
        ApiAccessTokenDO apiAccessTokenDO_2 = apiAccessTokenDAO.queryById(id);
        // TODO finish update


        apiAccessTokenDAO.update(apiAccessTokenDO_2);
        apiAccessTokenDO_2 = apiAccessTokenDAO.queryById(id);
        // TODO finish your asserts

    }


    public void setApiAccessTokenDAO(ApiAccessTokenDAO apiAccessTokenDAO) {
		this.apiAccessTokenDAO = apiAccessTokenDAO;
	}

}
