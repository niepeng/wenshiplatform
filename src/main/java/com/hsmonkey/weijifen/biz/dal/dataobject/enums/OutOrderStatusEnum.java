package com.hsmonkey.weijifen.biz.dal.dataobject.enums;


/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年6月28日  上午11:16:57</p>
 * <p>作者：niepeng</p>
 */
public enum OutOrderStatusEnum {

	buyer_pay(1,"买家已付款或等待出库","WAIT_GROUP","WAIT_SELLER_STOCK_OUT"),
	wait_seller_send(2,"等待卖家发货","WAIT_SELLER_SEND_GOODS",""),
	wai_buyer_sign(3,"等待买家确认收货(卖家已发货)","WAIT_BUYER_CONFIRM_GOODS","WAIT_GOODS_RECEIVE_CONFIRM"),
	exception_type(5,"维权或退货,取消，锁定，暂停","XX","TRADE_CANCELED,LOCKED,PAUSE"),
	buyer_sign(10,"买家已签收,完成","TRADE_BUYER_SIGNED", "FINISHED_L");
	
	
	private OutOrderStatusEnum(int id, String meanning, String youzanStatus, String jdStatus) {
		this.id = id;
		this.meanning = meanning;
		this.youzanStatus = youzanStatus;
		this.jdStatus = jdStatus;
	}
    
	private final int id;

	private final String  meanning;  
	
	private final String youzanStatus;
	
	private final String jdStatus;
	
	public static int getByYouzanStatus(String youzanStatus) {
		for (OutOrderStatusEnum tmp : OutOrderStatusEnum.values()) {
			if (tmp.getYouzanStatus().equals(youzanStatus)) {
				return tmp.getId();
			}
		}
		return 0;
	}
	
	public static int getByJdStatus(String jdStatus) {
		for (OutOrderStatusEnum tmp : OutOrderStatusEnum.values()) {
			String[] tmpJdStatus = tmp.getJdStatus().split(",");
			for (String s : tmpJdStatus) {
				if (s.equals(jdStatus)) {
					return tmp.getId();
				}
			}
		}
		return 0;
	}

	public int getId() {
		return id;
	}

	public String getMeanning() {
		return meanning;
	}

	public String getYouzanStatus() {
		return youzanStatus;
	}

	public String getJdStatus() {
		return jdStatus;
	}
	
}
