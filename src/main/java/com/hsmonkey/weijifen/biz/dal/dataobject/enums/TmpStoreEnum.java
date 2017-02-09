package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import wint.lang.utils.StringUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: </p>
 * <p>创建时间: Sep 24, 2014  2:50:28 PM</p>
 * <p>作者：聂鹏</p>
 */
public enum TmpStoreEnum {
	

	weijifen_import_article_url(1, "weijifen_import_article_url_", 1, "导入外部文章内容的存储的临时内容");

	private final int id;
	
	private final String key;
	
	private final int type;

	private final String meaning;

	private TmpStoreEnum(int id, String key, int type, String meaning) {
		this.id = id;
		this.key = key;
		this.type = type;
		this.meaning = meaning;
	}

	public static String getMeaning(int status) {
		for(TmpStoreEnum tmp : TmpStoreEnum.values()) {
			if(tmp.getId() == status) {
				return tmp.getMeaning();
			}
		}
		return StringUtil.EMPTY;
	}

	public int getId() {
		return id;
	}

	public String getMeaning() {
		return meaning;
	}

	public String getKey() {
		return key;
	}

	public int getType() {
		return type;
	}
	
}

