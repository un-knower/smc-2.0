package cn.uway.smc.web.page;

import java.io.Serializable;

public class Navigation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pageCount = "";

	private String pageIndex = "";

	private String params = "";

	private String indexColor = "";

	private String countColor = "";

	public String getCountColor() {
		return countColor;
	}

	public void setCountColor(String countColor) {
		this.countColor = countColor;
	}

	public String getIndexColor() {
		return indexColor;
	}

	public void setIndexColor(String indexColor) {
		this.indexColor = indexColor;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	public String getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}

	public static void main(String[] args) {

	}

}
