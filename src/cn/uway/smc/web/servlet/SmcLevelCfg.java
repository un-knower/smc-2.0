package cn.uway.smc.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.uway.commons.type.StringUtil;
import cn.uway.ews.param.ActionResult;
import cn.uway.ews.param.PageQueryResult;
import cn.uway.ews.servlet.BasicServlet;
import cn.uway.smc.db.dao.SmcCfgLevelDAO;
import cn.uway.smc.db.pojo.SMCCfgLevel;
import cn.uway.smc.web.page.Navigation;

public class SmcLevelCfg extends BasicServlet<SmcCfgLevelDAO> {

	private static final long serialVersionUID = 1L;

	public ActionResult queryList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageQueryResult<SMCCfgLevel> qr = new PageQueryResult<SMCCfgLevel>();
		response.setContentType("text/html");
		String params = "";

		if (StringUtil.isNull(pageIndex))
			pageIndex = "1";
		params = "key1=value1&key2=value2&action=queryList";

		// 在界面我就用jstl进行迭代
		// request.setAttribute("list", getList(Integer.parseInt(pageIndex)));
		List<SMCCfgLevel> list = getList(Integer.parseInt(pageIndex));
		qr.setDatas(list);

		Navigation nav = new Navigation();

		String count = Integer.toString((int) java.lang.Math.ceil((double) dao
				.list().size() / 5));
		// log.debug("总页数 : " + count);
		nav.setPageCount(count);
		nav.setPageIndex(pageIndex);
		nav.setParams(params);

		ActionResult result = new ActionResult();
		result.setForwardURL("/page/infoLevelCfg.jsp");
		result.setData(qr);
		result.setWparam(nav);
		return result;
	}

	public List<SMCCfgLevel> getList(int page) {
		List<SMCCfgLevel> list = null;
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
			SMCCfgLevel data = dao.get(Integer.parseInt(id));
			result.setData(data);
		}
		result.setForwardURL("/page/infoLevelCfgView.jsp");
		return result;
	}

	public static void main(String[] args) {
		try {
			SmcCfgLevelDAO dao = new SmcCfgLevelDAO();
			for (int i = 0; i <= 11; i++) {
				SMCCfgLevel level = new SMCCfgLevel();
				// dao.delete(i);
				// level.setLevel(i);
				level.setId(i);
				level.setLevelid(i);
				level.setName(String.valueOf(i));
				level.setDescription("level" + i);
				dao.put(level);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
