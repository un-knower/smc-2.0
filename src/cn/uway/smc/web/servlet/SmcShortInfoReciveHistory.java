package cn.uway.smc.web.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.uway.commons.type.StringUtil;
import cn.uway.ews.param.ActionResult;
import cn.uway.ews.param.PageQueryResult;
import cn.uway.ews.servlet.BasicServlet;
import cn.uway.smc.db.dao.SmcDataReceiveHistoryDAO;
import cn.uway.smc.db.pojo.SMCDataReceiveHistory;
import cn.uway.smc.web.page.Navigation;

public class SmcShortInfoReciveHistory
		extends
			BasicServlet<SmcDataReceiveHistoryDAO> {

	private static final long serialVersionUID = 1L;

	public ActionResult queryList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageQueryResult<SMCDataReceiveHistory> qr = new PageQueryResult<SMCDataReceiveHistory>();
		// 由于这里使用的url传参数,要验证url的合法性
		response.setContentType("text/html");
		// 总页数从数据库获得
		String params = "";

		if (StringUtil.isNull(pageIndex))
			pageIndex = "1";

		// 注意这里设置的下面要对应的取值
		params = "key1=value1&key2=value2&action=queryList";

		// 在界面我就用jstl进行迭代
		List<SMCDataReceiveHistory> list = getList(Integer.parseInt(pageIndex));
		qr.setDatas(list);

		// 把所有参数设置在bean里在jsp页面取出来
		Navigation nav = new Navigation();

		// 设置总页数
		String count = Integer.toString((int) java.lang.Math.ceil((double) dao
				.list().size() / 5));
		nav.setPageCount(count);
		nav.setPageIndex(pageIndex);
		nav.setParams(params);

		ActionResult result = new ActionResult();
		result.setForwardURL("/page/shortInfoReciveHistory.jsp");
		result.setData(qr);
		result.setWparam(nav);
		return result;
	}

	public ActionResult viewInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionResult result = new ActionResult();
		String id = request.getParameter("id");
		if (id != null && !id.equals("")) {
			SMCDataReceiveHistory data = dao.getId(Integer.parseInt(id));
			result.setData(data);
		}
		result.setForwardURL("/page/shortInfoReciveHistoryView.jsp");
		return result;
	}

	public List<SMCDataReceiveHistory> getList(int page) {
		List<SMCDataReceiveHistory> list = new ArrayList<SMCDataReceiveHistory>();
		try {
			if (dao.list() != null) {
				int listSize = dao.list().size();

				if (listSize < 5) {
					list = dao.list().subList(0, listSize);
				} else if ((5 * page) > listSize) {
					list = dao.list().subList(5 * (page - 1), listSize);
				} else {
					list = dao.list().subList(5 * (page - 1), 5 * page);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
