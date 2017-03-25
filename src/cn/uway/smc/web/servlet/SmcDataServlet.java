package cn.uway.smc.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.uway.commons.type.StringUtil;
import cn.uway.ews.param.ActionResult;
import cn.uway.ews.servlet.BasicServlet;
import cn.uway.smc.db.dao.SmcDataDAO;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.util.Util;
import cn.uway.smc.web.page.Navigation;

public class SmcDataServlet extends BasicServlet<SmcDataDAO> {

	// private final static Logger log =
	// LoggerFactory.getLogger(SmcDataServlet.class);
	private static final long serialVersionUID = 1860559865323154020L;

	public ActionResult queryList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/html");
		String params = "";

		if (StringUtil.isNull(pageIndex))
			pageIndex = "1";

		params = "key1=value1&key2=value2&action=queryList";
		cn.uway.smc.db.dao.PageQueryResult<SMCData> list = dao.advQuery(null,
				Util.PAGE_SIZE, Integer.valueOf(pageIndex));

		// 把所有参数设置在bean里在jsp页面取出来
		Navigation nav = new Navigation();

		// 设置总页数
		nav.setPageCount(list.getPageCount() + "");
		nav.setPageIndex(pageIndex);
		nav.setParams(params);

		ActionResult result = new ActionResult();
		result.setForwardURL("/page/shortInfo.jsp");
		result.setData(list);
		result.setWparam(nav);
		return result;
	}

	public ActionResult viewInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionResult result = new ActionResult();
		String id = request.getParameter("id");
		if (StringUtil.isNotNull(id)) {
			SMCData data = dao.get(Integer.parseInt(id));
			result.setData(data);
		}
		result.setForwardURL("/page/shortInfoView.jsp");
		return result;
	}

	public static void main(String[] args) {
	}

}
