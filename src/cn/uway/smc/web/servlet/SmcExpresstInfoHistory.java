package cn.uway.smc.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.uway.commons.type.StringUtil;
import cn.uway.ews.param.ActionResult;
import cn.uway.ews.servlet.BasicServlet;
import cn.uway.smc.db.dao.SmcDataHistoryDAO;
import cn.uway.smc.db.pojo.SMCDataHistory;
import cn.uway.smc.util.Util;
import cn.uway.smc.web.page.Navigation;

public class SmcExpresstInfoHistory extends BasicServlet<SmcDataHistoryDAO> {

	private static final long serialVersionUID = 1L;

	public ActionResult queryList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 由于这里使用的url传参数,要验证url的合法性
		response.setContentType("text/html");
		// 总页数从数据库获得
		String params = "";

		if (StringUtil.isNull(pageIndex))
			pageIndex = "1";

		// 注意这里设置的下面要对应的取值
		params = "action=queryList";

		cn.uway.smc.db.dao.PageQueryResult<SMCDataHistory> list = dao.advQuery(
				null, Util.PAGE_SIZE, Integer.valueOf(pageIndex));

		list.setDatas(list.getDatas());

		Navigation nav = new Navigation();

		nav.setPageCount(list.getPageCount() + "");
		nav.setPageIndex(pageIndex);
		nav.setParams(params);

		ActionResult result = new ActionResult();
		result.setForwardURL("/page/expresstInfoHistory.jsp");
		result.setData(list);
		result.setWparam(nav);
		return result;
	}

	public List<SMCDataHistory> getList(int page) {
		List<SMCDataHistory> list = null;
		try {
			int listSize = dao.list().size();

			if (listSize < 5) {
				list = dao.list().subList(0, listSize);
			} else if ((5 * page) > listSize) {
				list = dao.list().subList(5 * (page - 1), listSize);
			} else {
				list = dao.list().subList(5 * (page - 1), 5 * page);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ActionResult viewInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionResult result = new ActionResult();
		String id = request.getParameter("id");
		if (id != null && !id.equals("")) {
			SMCDataHistory data = dao.getId(Integer.parseInt(id));
			result.setData(data);
		}
		result.setForwardURL("/page/expresstInfoHistoryView.jsp");
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
