package com.hsmonkey.weijifen;

import junit.framework.TestCase;
import wint.help.tools.gen.AutoGen;
import wint.help.tools.ibatis.AutoGenDAO;

import com.hsmonkey.weijifen.biz.dal.dataobject.KeyValueDO;


public class GenCodes extends TestCase {

	private static final String suffix = "weijifen_";
	
    public void testKeyValueDO() {
    		genDAO(KeyValueDO.class);
    }
    
    
//	public void testProductPage() {
//		// product -->  http://127.0.0.1:8080/admin/product/list.htm
//		genPageContainDAO(ProductDO.class, "admin");
//	}


	/**
	 * 1. 生成dao层的内容，包括配置
	 * 2. 生成vm页面、ao、action、form，实现CRUD
	 */
	private void genPageContainDAO(Class<?> class1, String actionContext) {
		AutoGen autoGen = new AutoGen(suffix);
		autoGen.gen(class1, actionContext);
	}

	/**
	 * 生成dao层的内容，包括配置
	 */
	private void genDAO(Class<?> class1) {
		AutoGenDAO autoGenDAO = new AutoGenDAO(suffix);
		autoGenDAO.gen(class1);
	}
}
