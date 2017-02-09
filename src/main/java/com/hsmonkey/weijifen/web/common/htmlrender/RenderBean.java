package com.hsmonkey.weijifen.web.common.htmlrender;

import java.util.List;

import wint.lang.utils.CollectionUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: 驭宝</p>
 * <p>创建时间: 2015-11-25  下午5:56:25</p>
 * <p>作者：niepeng</p>
 */
public class RenderBean {

	// 当前元素的id
	private String htmlId;

	// 当前元素的class
	private String htmlClass;

	// 当前元素的data
	private String data;

	// 当前值
	private String value;

	// 当前元素的body,子集的内容
	private String sonValue;

	// 其他需要的值，通过需要按照顺序写入内容
	private List<String> otherValue;

	// 子元素的列表
	List<RenderBean> sonList;

	// -------------- normal method -----------------------

	public void addOtherValue(String value) {
		if (otherValue == null) {
			otherValue = CollectionUtil.newArrayList();
		}
		otherValue.add(value);
	}

	public void addSon(RenderBean bean) {
		if (sonList == null) {
			sonList = CollectionUtil.newArrayList();
		}
		sonList.add(bean);
	}



	// -------------- setter/getter -----------------------

	public String getHtmlId() {
		return htmlId;
	}

	public void setHtmlId(String htmlId) {
		this.htmlId = htmlId;
	}

	public String getHtmlClass() {
		return htmlClass;
	}

	public void setHtmlClass(String htmlClass) {
		this.htmlClass = htmlClass;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSonValue() {
		return sonValue;
	}

	public void setSonValue(String sonValue) {
		this.sonValue = sonValue;
	}

	public List<String> getOtherValue() {
		return otherValue;
	}

	public void setOtherValue(List<String> otherValue) {
		this.otherValue = otherValue;
	}

	public List<RenderBean> getSonList() {
		return sonList;
	}

	public void setSonList(List<RenderBean> sonList) {
		this.sonList = sonList;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
