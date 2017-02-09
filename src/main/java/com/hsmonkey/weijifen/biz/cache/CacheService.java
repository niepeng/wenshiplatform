package com.hsmonkey.weijifen.biz.cache;

import java.util.List;

public interface CacheService {
	
	CacheResult get(int namespace, Object key);
	
	List<CacheResult> mget(int namespace, List<Object> keys);
	
	/**
	 * @param namespace
	 * @param key
	 * @param value
	 * @param expire 单位：秒
	 */
	void put(int namespace, Object key, Object value, int expire);
	
	void delete(int namespace, Object key);
	
	void mdelete(int namespace, List<Object> keys);

}
