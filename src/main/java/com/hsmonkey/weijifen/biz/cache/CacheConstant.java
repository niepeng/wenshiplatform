package com.hsmonkey.weijifen.biz.cache;

public interface CacheConstant {


	public static final int MIN = 60;

	public static final int HOUR = 60 * MIN;

	public static final int DAY = 24 * HOUR;

	public interface Cache {

		public static final int NAMESPACES = 1;


		public static final String USERDATA = "userdata";

	}
	

}