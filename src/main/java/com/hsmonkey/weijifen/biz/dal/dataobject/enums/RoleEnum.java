package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import wint.lang.utils.StringUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年8月23日  下午5:04:28</p>
 * <p>作者：niepeng</p>
 */
public enum RoleEnum {
	
	shareRedArticleSender(1, "红包分享拥有者", new FunctionEnum[]{ FunctionEnum.shareRedArticle}, "userManager/promotion/articleList"),
	transaction(2, "交易系统权限", new FunctionEnum[]{ FunctionEnum.transaction}, ""),
	barginItem(3, "砍价活动权限", new FunctionEnum[]{ FunctionEnum.transaction, FunctionEnum.bargainItem}, "userManager/promotion/bargainItemList"),
	remindGroupBuy(4, "开团提醒", new FunctionEnum[]{ FunctionEnum.remindGroupBuy}, "userManager/promotion/remindGroupBuy"),
	logisticsNotice(5, "物流通知", new FunctionEnum[]{ FunctionEnum.logisticsNotice}, "userManager/promotion/logisticsNotice"),
	record(6, "千里传音者", new FunctionEnum[]{ FunctionEnum.record}, "userManager/promotion/recordList"),
	jifenbao(7, "积分宝", new FunctionEnum[]{ FunctionEnum.jifenbao}, "userManager/textReplay"),
	advArticle(8, "贴片广告", new FunctionEnum[]{ FunctionEnum.advArticle}, "userManager/promotion/advArticleList");
	
	private final int id;

	private final String meaning;
	
	private final FunctionEnum[] functionEnums;
	
	// 拥有该权限的人默认访问页面
	private final String defaultUrlTarget;

	private RoleEnum(int id, String meaning, FunctionEnum[] functionEnums, String defaultUrlTarget) {
		this.id = id;
		this.meaning = meaning;
		this.functionEnums = functionEnums;
		this.defaultUrlTarget = defaultUrlTarget;
	}
	
	public static RoleEnum get(int id) {
		for(RoleEnum tmp : RoleEnum.values()) {
			if(tmp.getId() == id) {
				return tmp;
			}
		}
		return null;
	}
	
	public static FunctionEnum[] getFunction(int id) {
		RoleEnum roleEnum = get(id);
		if(roleEnum != null) {
			return roleEnum.getFunctionEnums();
		}
		return null;
	}
	
	public static boolean hasPermission(int id, FunctionEnum functionEnum) {
		FunctionEnum[] functionEnums = getFunction(id);
		if (functionEnums == null || functionEnum == null) {
			return false;
		}

		for (FunctionEnum f : functionEnums) {
			if (f.getId() == functionEnum.getId()) {
				return true;
			}
		}

		return false;
	}
	
	
	public static String getMeaning(int id) {
		RoleEnum roleEnum = get(id);
		return roleEnum != null ? roleEnum.getMeaning() : StringUtil.EMPTY;
	}

	public int getId() {
		return id;
	}

	public String getMeaning() {
		return meaning;
	}

	public FunctionEnum[] getFunctionEnums() {
		return functionEnums;
	}

	public String getDefaultUrlTarget() {
		return defaultUrlTarget;
	}
	
}
