package com.hsmonkey.weijifen.biz.dal.ibatis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: </p>
 * <p>创建时间: Sep 24, 2014  2:46:54 PM</p>
 * <p>作者：聂鹏</p>
 */
public class BaseIbatisDAO extends SqlMapClientDaoSupport {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected Logger getLog() {
		return log;
	}
}
