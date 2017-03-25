package cn.uway.smc.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.StringUtil;
import cn.uway.ews.param.ActionResult;
import cn.uway.ews.param.PageQueryResult;
import cn.uway.ews.servlet.BasicServlet;
import cn.uway.smc.db.dao.SmcDataReceiveDAO;
import cn.uway.smc.db.pojo.SMCDataReceive;
import cn.uway.smc.web.page.Navigation;

public class SmcDataReceiveServlet extends BasicServlet<SmcDataReceiveDAO> {

	private final static Logger log = LoggerFactory
			.getLogger(SmcDataReceiveServlet.class);

	private static final long serialVersionUID = 1L;

	public ActionResult queryList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageQueryResult<SMCDataReceive> qr = new PageQueryResult<SMCDataReceive>();
		// 由于这里使用的url传参数,要验证url的合法性
		response.setContentType("text/html");
		// 总页数从数据库获得
		String params = "";
		if (StringUtil.isNull(pageIndex))
			pageIndex = "1";
		params = "key1=value1&key2=value2&action=queryList";

		// 在界面我就用jstl进行迭代
		// request.setAttribute("list", getList(Integer.parseInt(pageIndex)));
		List<SMCDataReceive> list = getList(Integer.parseInt(pageIndex));
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
		result.setForwardURL("/page/shortInfoRecive.jsp");
		result.setData(qr);
		result.setWparam(nav);
		return result;
	}

	public List<SMCDataReceive> getList(int page) {
		List<SMCDataReceive> list = null;
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
			SMCDataReceive data = dao.get(Integer.parseInt(id));
			result.setData(data);
		}
		result.setForwardURL("/page/shortInfoReciveView.jsp");
		return result;
	}

	public static void main(String[] args) {
		SmcDataReceiveDAO dao = new SmcDataReceiveDAO();
		for (int i = 0; i < 6; i++) {
			// SMCDataReceive entity = new SMCDataReceive();
			// entity.setFromUser("2101");
			// entity.setReceiveTime(new Date());
			// try
			// {
			// dao.add(entity);
			// }
			// catch (Exception e)
			// {
			// e.printStackTrace();
			// }
			// try
			// {
			// dao.delete(i);
			// }
			// catch (Exception e)
			// {
			// e.printStackTrace();
			// }
		}
		try {
			List<SMCDataReceive> list = dao.list();
			for (SMCDataReceive li : list) {
				log.debug(li.getFromUser() + " : " + li.getReceiveTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
