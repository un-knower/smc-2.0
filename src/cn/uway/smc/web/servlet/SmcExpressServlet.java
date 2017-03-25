package cn.uway.smc.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.uway.commons.type.StringUtil;
import cn.uway.ews.param.ActionResult;
import cn.uway.ews.param.PageQueryResult;
import cn.uway.ews.servlet.BasicServlet;
import cn.uway.smc.db.dao.SmcDataDAO;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.web.page.Navigation;

public class SmcExpressServlet extends BasicServlet<SmcDataDAO> {

	// private final static Logger LOG =
	// LoggerFactory.getLogger(SmcExpressServlet.class);
	private static final long serialVersionUID = 1L;

	public ActionResult queryList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageQueryResult<SMCData> qr = new PageQueryResult<SMCData>();
		// 由于这里使用的url传参数,要验证url的合法性
		response.setContentType("text/html");
		// 总页数从数据库获得
		String params = "";

		if (StringUtil.isNull(pageIndex))
			pageIndex = "1";

		// 设置用户自定义的参数
		// 注意这里设置的下面要对应的取值
		params = "key1=value1&key2=value2&action=queryList";

		List<SMCData> list = getList(Integer.parseInt(pageIndex));
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
		result.setForwardURL("/page/expresstInfo.jsp");
		result.setData(qr);
		result.setWparam(nav);
		return result;
	}

	public List<SMCData> getList(int page) {
		List<SMCData> list = null;
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
			SMCData data = dao.get(Integer.parseInt(id));
			result.setData(data);
		}
		result.setForwardURL("/page/expresstInfoView.jsp");
		return result;
	}

	public static void main(String[] args) {
		SmcDataDAO dao = new SmcDataDAO();

		// en.setSendTime(new Date().toLocaleString());
		for (int i = 0; i <= 9088; i++) {
			try {
				dao.delete(i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		List<SMCData> list;
		try {
			list = dao.list();
			System.out.println("清除短信个数--------" + list.size());
			for (SMCData li : list) {
				System.out.println(li.getId() + " : " + li.getToUsers());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
