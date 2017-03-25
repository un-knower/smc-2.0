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

public class SmcShortInfoHisServlet extends BasicServlet<SmcDataHistoryDAO> {

	// private final static Logger log =
	// LoggerFactory.getLogger(SmcShortInfoHisServlet.class);
	private static final long serialVersionUID = 1L;

	public ActionResult queryList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 由于这里使用的url传参数,要验证url的合法性
		response.setContentType("text/html");
		// 总页数从数据库获得
		String params = "";

		if (StringUtil.isNull(pageIndex))
			pageIndex = "1";

		params = "key1=value1&key2=value2&action=queryList";

		// 在界面我就用jstl进行迭代
		// request.setAttribute("list", getList(Integer.parseInt(pageIndex)));
		cn.uway.smc.db.dao.PageQueryResult<SMCDataHistory> qr = dao.advQuery(
				null, Util.PAGE_SIZE, Integer.valueOf(pageIndex));
		// qr.setDatas(list);

		// 把所有参数设置在bean里在jsp 页面取出来
		Navigation nav = new Navigation();

		// 设置总页数
		nav.setPageCount(qr.getPageCount() + "");
		nav.setPageIndex(pageIndex);
		nav.setParams(params);

		ActionResult result = new ActionResult();
		result.setForwardURL("/page/shortInfoHistory.jsp");
		result.setData(qr);
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
		result.setForwardURL("/page/shortInfoHistoryView.jsp");
		return result;
	}

	public static void main(String[] args) {
		// SmcDataDAO dao = new SmcDataDAO();
		// try
		// {
		// List<SMCDataHistory> list = dao.list();
		// System.out.println(list.size());
		// }
		// catch (Exception e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

}
