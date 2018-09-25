//package com.hsmonkey.weijifen.biz.dal.ibatis;
//
//import javax.sql.DataSource;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
//
///**
// * <p>标题: </p>
// * <p>描述: </p>
// * <p>版权: 驭宝</p>
// * <p>创建时间: 2015年12月29日  上午11:36:22</p>
// * <p>作者：niepeng</p>
// */
//public class BaseScoreIbatisDAO extends SqlMapClientDaoSupport {
//
//	protected final Logger log = LoggerFactory.getLogger(getClass());
//
//	private DataSource dataSourceScore;
//
//	/**
//	 * 容器初始化的时候调用
//	 */
//	public void scoreData() {
//		if (dataSourceScore != null) {
//			getSqlMapClientTemplate().setDataSource(dataSourceScore);
//			return;
//		}
//
//		if (getSqlMapClient().getDataSource() != null) {
//			getSqlMapClientTemplate().setDataSource(getSqlMapClient().getDataSource());
//		}
//	}
//
//
//	protected Logger getLog() {
//		return log;
//	}
//
//	public void setDataSourceScore(DataSource dataSourceScore) {
//		this.dataSourceScore = dataSourceScore;
//	}
//
//}