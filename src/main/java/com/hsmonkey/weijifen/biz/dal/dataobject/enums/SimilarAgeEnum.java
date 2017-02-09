package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

/**
 * <p>标题: 相似度和年龄的测试枚举类</p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年9月9日  下午12:18:36</p>
 * <p>作者：niepeng</p>
 */
public enum SimilarAgeEnum {
	
	simiar_age_1(1, 0, 40, Integer.MIN_VALUE, -6, "妆前妆后相似度%s","自信增龄%s岁","请不要用两个人的照片糊弄我！"),
	simiar_age_2(2, 0, 40, -6, 0, "妆前妆后相似度%s","自信增龄%s岁","有些人是化妆，有些人是乔装…"),
	simiar_age_3(3, 0, 40, 0,6, "妆前妆后相似度%s","轻松减龄%s岁","哇塞，这是整容术还是化妆术？"),
	simiar_age_4(4, 0, 40, 6,Integer.MAX_VALUE, "妆前妆后相似度%s","成功减龄%s岁","这真的是同一个人嘛？"),

	simiar_age_5(5, 40, 80, Integer.MIN_VALUE, -6, "妆前妆后相似度%s","成功增龄%s岁","如果世界漆黑，其实我很美…"),
	simiar_age_6(6, 40, 80, -6, 0, "妆前妆后相似度%s","稍稍增龄%s岁","要不换个化妆品试试吧？"),
	simiar_age_7(7, 40, 80, 0,6, "妆前妆后相似度%s","稍稍减龄%s岁","这妆我给82分，剩下的以666的方式给你~"),
	simiar_age_8(8, 40, 80, 6,Integer.MAX_VALUE, "妆前妆后相似度%s","成功减龄%s岁","超级减龄妆，你值得拥有！"),
	
	simiar_age_9(5, 80, 101, Integer.MIN_VALUE, -6, "妆前妆后相似度%s","自信增龄%s岁","要不你以后还是素颜吧？"),
	simiar_age_10(6, 80, 101, -6, 0, "妆前妆后相似度%s","自信增龄%s岁","气质气场噌噌长有木有~(￣▽￣)~*"),
	simiar_age_11(7, 80, 101, 0,6, "妆前妆后相似度%s","嫩了%s岁哦","我只想说：永远18喔✪ω✪！"),
	simiar_age_12(8, 80, 101, 6,Integer.MAX_VALUE, "妆前妆后相似度%s","轻松减龄%s岁","这个妆，我给满分，不怕你骄傲！̑");
	
	private final int id;
	
	private final int startSimilar;
	
	private final int endSimilar;
	
	private final int startDistanceAge;
	
	private final int endDistanceAge;
	
	private final String text1;
	
	private final String text2;
	
	private final String text3;
	
	private SimilarAgeEnum(int id, int startSimilar, int endSimilar, int startDistanceAge, int endDistanceAge, String text1, String text2, String text3) {
		this.id = id;
		this.startSimilar = startSimilar;
		this.endSimilar = endSimilar;
		this.startDistanceAge = startDistanceAge;
		this.endDistanceAge = endDistanceAge;
		this.text1 = text1;
		this.text2 = text2;
		this.text3 = text3;
	}
	
	public static SimilarAgeEnum find(double similar, int distanceAge) {
		for (SimilarAgeEnum tmp : SimilarAgeEnum.values()) {
			if (tmp.getStartSimilar() < similar && tmp.getEndSimilar() >= similar && tmp.getStartDistanceAge() < distanceAge && tmp.getEndDistanceAge() >= distanceAge) {
				return tmp;
			}
		}
		return SimilarAgeEnum.simiar_age_12;
	}

	public int getId() {
		return id;
	}

	public int getStartSimilar() {
		return startSimilar;
	}

	public int getEndSimilar() {
		return endSimilar;
	}

	public int getStartDistanceAge() {
		return startDistanceAge;
	}

	public int getEndDistanceAge() {
		return endDistanceAge;
	}

	public String getText1() {
		return text1;
	}

	public String getText2() {
		return text2;
	}

	public String getText3() {
		return text3;
	}
	
}
