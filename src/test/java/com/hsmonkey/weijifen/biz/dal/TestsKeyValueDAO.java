package com.hsmonkey.weijifen.biz.dal;

import junit.framework.Assert;

import com.hsmonkey.weijifen.BaseTest;
import com.hsmonkey.weijifen.biz.dal.daointerface.KeyValueDAO;
import com.hsmonkey.weijifen.biz.dal.dataobject.KeyValueDO;

/**
* this file is auto generate.
*/
public class TestsKeyValueDAO extends BaseTest {

	private KeyValueDAO keyValueDAO;

    public void testCreate() {
        KeyValueDO keyValueDO = new KeyValueDO();
		keyValueDO.setGmtModified(new java.util.Date());
		keyValueDO.setKeyName("a");
		keyValueDO.setType(2);
		keyValueDO.setGmtCreate(new java.util.Date());
		keyValueDO.setValue("a");
		long id = keyValueDAO.create(keyValueDO);
		Assert.assertTrue(id > 0);
	}

    public void testQueryById() {
        KeyValueDO keyValueDO = new KeyValueDO();
		keyValueDO.setGmtModified(new java.util.Date());
		keyValueDO.setKeyName("a");
		keyValueDO.setType(2);
		keyValueDO.setGmtCreate(new java.util.Date());
		keyValueDO.setValue("a");
        long id = keyValueDAO.create(keyValueDO);
        Assert.assertTrue(id > 0);
        KeyValueDO keyValueDO_2 = keyValueDAO.queryById(id);
        Assert.assertNotNull(keyValueDO_2);
    }

    public void testDelete() {
        KeyValueDO keyValueDO = new KeyValueDO();
		keyValueDO.setKeyName("a");
		keyValueDO.setType(2);
		keyValueDO.setValue("a");
        long id = keyValueDAO.create(keyValueDO);
        Assert.assertTrue(id > 0);
        KeyValueDO keyValueDO_2 = keyValueDAO.queryById(id);
        Assert.assertNotNull(keyValueDO_2);
        keyValueDAO.delete(id);
        keyValueDO_2 = keyValueDAO.queryById(id);
        Assert.assertNull(keyValueDO_2);
    }

    public void testUpdate() {
        KeyValueDO keyValueDO = new KeyValueDO();
		keyValueDO.setKeyName("a");
		keyValueDO.setType(2);
		keyValueDO.setValue("a");
        long id = keyValueDAO.create(keyValueDO);
        Assert.assertTrue(id > 0);
        KeyValueDO keyValueDO_2 = keyValueDAO.queryById(id);

        keyValueDAO.update(keyValueDO_2);
        keyValueDO_2 = keyValueDAO.queryById(id);
    }


    public void setKeyValueDAO(KeyValueDAO keyValueDAO) {
		this.keyValueDAO = keyValueDAO;
	}

}
