package cn.uway.smc.web.page;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class NavigationTag extends BodyTagSupport {

	private static final long serialVersionUID = -4247241487535299862L;

	private String indexColor = "";

	private String countColor = "";

	private String navigationUrl = "";

	private int pageIndex;

	private int pageCount;

	// 自定义参数
	private String params = "";

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	@Override
	public int doStartTag() throws JspException {
		// 获取ｕｒｌ
		navigationUrl = getParameterUrl();
		// 设置显示页数的颜色
		getColor();
		StringBuffer strb = new StringBuffer();

		// 分页
		int front = pageIndex - 4; // 前面一截
		int back = pageIndex + 4; // 后面一截

		if (pageIndex == 1) {
			strb.append(" 首页 ");
			strb.append(" 上一页 ");
		} else {
			strb.append("<a href=" + navigationUrl + "pageIndex=1> 首页 </a>");
			strb.append("<a href=" + navigationUrl + "pageIndex="
					+ (pageIndex - 1) + "> 上一页 </a> ");
		}

		if (pageIndex < 5) // 如果索引在前5页
		{
			if (pageCount <= 10) // 总页数不够10页
			{
				for (int i = 1; i <= pageCount; i++) {
					if (i == pageIndex)
						strb.append(" " + pageIndex + " ");
					else
						strb.append("<a href=" + navigationUrl + "pageIndex="
								+ i + ">[" + i + "]</a>");
				}
			} else {
				for (int i = 1; i < 10; i++) {
					if (i == pageIndex)
						strb.append(" " + pageIndex + " ");
					else
						strb.append("<a href=" + navigationUrl + "pageIndex="
								+ i + ">[" + i + "]</a>");
				}
			}
		} else {
			if (front >= 1) {
				for (int i = 4; i > 0; i--)
					strb.append("<a href=" + navigationUrl + "pageIndex="
							+ (pageIndex - i) + ">[" + (pageIndex - i)
							+ "]</a>");
			} else {
				for (int i = 1; i < pageIndex; i++)
					strb.append("<a href=" + navigationUrl + "pageIndex=" + i
							+ ">[" + i + "]</a>");
			}

			strb.append(" " + pageIndex + " "); // 分界线 eg: 5
			// ,前面一截就是1234,后面一截就是6789

			if (back <= pageCount) {
				for (int i = 1; i < 5; i++)
					strb.append("<a href=" + navigationUrl + "pageIndex="
							+ (pageIndex + i) + ">[" + (pageIndex + i)
							+ "]</a>");
			} else {
				for (int i = 1; i < pageCount - pageIndex + 1; i++)
					strb.append("<a href=" + navigationUrl + "pageIndex="
							+ (pageIndex + i) + ">[" + (pageIndex + i)
							+ "]</a>");
			}
		}

		if (pageIndex == pageCount) {
			strb.append(" 下一页 ");
			strb.append(" 最后一页 ");
		} else {
			strb.append(" <a href=" + navigationUrl + "pageIndex="
					+ (pageIndex + 1) + "> 下一页 </a>");
			strb.append("<a href=" + navigationUrl + "pageIndex=" + pageCount
					+ "> 最后一页 </a>");
		}
		strb.append("  <font color=" + indexColor + ">" + pageIndex
				+ "</font>/<font color=" + countColor + ">" + pageCount
				+ "</font>");
		JspWriter out = pageContext.getOut();
		try {
			out.println(strb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	private void getColor() {
		if (indexColor.equals(""))
			indexColor = "red";
		if (countColor.equals(""))
			countColor = "blue";
	}

	private String getParameterUrl() {
		// log.debug("params | "+params);
		// 没有自定义参数
		if (params.equals(""))
			return navigationUrl;
		StringBuffer url = new StringBuffer();
		url.append(navigationUrl).append("?").append(params).append("&");
		return url.toString();
	}

	public String getNavigationUrl() {
		return navigationUrl;
	}

	public void setNavigationUrl(String navigationUrl) {
		this.navigationUrl = navigationUrl;
	}

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

}
