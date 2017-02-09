package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年9月2日  下午1:43:40</p>
 * <p>作者：niepeng</p>
 */
public enum OrderStatusEnum {

	wait_pay(1, "待支付"),
	buyer_pay(3, "待发货"),
	seller_send(5, "已发货"),
	buyer_sign(7, "已完成"),
	refund(8, "已退款"),
	close(9, "已关闭");

	private final int id;
	
	private final String meaning;
	
	private OrderStatusEnum(int id, String meaning) {
		this.id = id;
		this.meaning = meaning;
	}
	
	public static OrderStatusEnum get(int id) {
		for(OrderStatusEnum tmp : OrderStatusEnum.values()) {
			if(tmp.getId() == id) {
				return tmp;
			}
		}
		return null;
	}
	
	public static String getMeaningById(int id) {
		OrderStatusEnum tmp = get(id);
		return tmp == null ? "" : tmp.getMeaning();
	}
	
	public int getId() {
		return id;
	}

	public String getMeaning() {
		return meaning;
	}
	
}
