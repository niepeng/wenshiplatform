package com.hsmonkey.weijifen.biz.cache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import wint.lang.convert.ConvertUtil;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;

import com.hsmonkey.weijifen.biz.cache.CacheResult.Codes;

public class SimpleRamCacheServiceImpl implements CacheService {

	private boolean enable = false;

	private Map<Integer, Map<Object, SoftReference<StoreEntity>>> data = new HashMap<Integer, Map<Object, SoftReference<StoreEntity>>>();

	public void delete(int namespace, Object key) {
		if (!enable) {
			return;
		}
		getTargetMap(namespace).remove(key);
	}

	public CacheResult get(int namespace, Object key) {
		if (!enable) {
			return new CacheResult(Codes.NOT_ENABLE.getValue());
		}
		SoftReference<StoreEntity> ref = getTargetMap(namespace).get(key);
		if (ref == null) {
			return new CacheResult(Codes.NOT_EXIST.getValue());
		}
		StoreEntity se = ref.get();
		if (se == null) {
			return new CacheResult(Codes.NOT_EXIST.getValue());
		}
		if (se.isExpired()) {
			return new CacheResult(Codes.EXPIRED.getValue());
		}
		return new CacheResult(key, se.getValue());
	}

	public void mdelete(int namespace, List<Object> keys) {
		if (!enable) {
			return;
		}
		if (CollectionUtil.isEmpty(keys)) {
			return;
		}
		for (Object key : keys) {
			delete(namespace, key);
		}
	}

	public List<CacheResult> mget(int namespace, List<Object> keys) {
		List<CacheResult> ret = CollectionUtil.newArrayList();
		if (CollectionUtil.isEmpty(keys)) {
			return ret;
		}
		for (Object key : keys) {
			ret.add(get(namespace, key));
		}
		return ret;
	}

	public void put(int namespace, Object key, Object value, int expire) {
		if (!enable) {
			return;
		}
		getTargetMap(namespace).put(key, new SoftReference<StoreEntity>(new StoreEntity(value, expire)));
	}

	protected Map<Object, SoftReference<StoreEntity>> getTargetMap(int namespace) {
		Map<Object, SoftReference<StoreEntity>> partData = data.get(namespace);
		if (partData == null) {
			throw new RuntimeException("namespace:" + namespace + " has not set, please use setNamespaces to set it!");
		}
		return partData;
	}

	public void setNamespaces(String namespaces) {
		if (StringUtil.isEmpty(namespaces)) {
			return;
		}
		List<String> parts = StringUtil.splitTrim(namespaces, ",");
		for (String part : parts) {
			int namespace = ConvertUtil.toInt(part, 0);
			data.put(namespace, new ConcurrentHashMap<Object, SoftReference<StoreEntity>>());
		}
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	static class StoreEntity {
		Object value;
		long ts;
		long expireMS;

		public StoreEntity(Object value, int expire) {
			super();
			this.value = value;
			this.expireMS = expire * 1000;
			this.ts = System.currentTimeMillis();
		}

		public boolean isExpired() {
			long escape = System.currentTimeMillis() - ts;
			if (escape >= expireMS) {
				return true;
			}
			return false;
		}

		public Object getValue() {
			return value;
		}

	}

}
