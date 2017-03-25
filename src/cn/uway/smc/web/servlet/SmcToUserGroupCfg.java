package cn.uway.smc.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.uway.commons.type.StringUtil;
import cn.uway.ews.param.ActionResult;
import cn.uway.ews.param.PageQueryResult;
import cn.uway.ews.servlet.BasicServlet;
import cn.uway.smc.db.dao.SmcCfgToUserGroupDAO;
import cn.uway.smc.db.pojo.SMCCfgToUserGroup;
import cn.uway.smc.web.page.Navigation;
import cn.uway.smc.web.page.UiError;

public class SmcToUserGroupCfg extends BasicServlet<SmcCfgToUserGroupDAO> {

	private static final long serialVersionUID = 1L;

	public ActionResult queryList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageQueryResult<SMCCfgToUserGroup> qr = new PageQueryResult<SMCCfgToUserGroup>();
		response.setContentType("text/html");
		// 总页数从数据库获得
		String params = "";

		if (StringUtil.isNull(pageIndex))
			pageIndex = "1";

		// 注意这里设置的下面要对应的取值
		params = "key1=value1&key2=value2&action=queryList";
		List<SMCCfgToUserGroup> list = getList(Integer.parseInt(pageIndex));
		qr.setDatas(list);

		Navigation nav = new Navigation();
		String count = Integer.toString((int) java.lang.Math.ceil((double) dao
				.list().size() / 5));
		nav.setPageCount(count);
		nav.setPageIndex(pageIndex);
		nav.setParams(params);

		ActionResult result = new ActionResult();
		result.setForwardURL("/page/infoUserGroupCfg.jsp");
		result.setData(qr);
		result.setWparam(nav);
		return result;
	}

	public ActionResult add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionResult result = new ActionResult();
		result.setForwardURL(DEFAULT_FORWARD_URL);
		result.setReturnURL("/page/infoUserGroupCfg.do?action=queryList");
		String userGroupName = req.getParameter("userGroupName");
		String desc = req.getParameter("desc");

		SMCCfgToUserGroup user = new SMCCfgToUserGroup();
		user.setName(userGroupName);
		user.setDescription(desc);

		try {
			dao.put(user);
			result.setError(new UiError("", "添加成功.", "", ""));
		} catch (Exception e) {
			result.setError(new UiError("", "添加失败.", "", ""));
			e.printStackTrace();
		}
		// result.setForwardURL("/page/infoUserGroupCfg.do?action=queryList");
		return result;
	}

	public ActionResult save(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionResult result = new ActionResult();
		String strID = req.getParameter("id");
		String name = req.getParameter("userGroupName");
		String desc = req.getParameter("desc");

		result.setForwardURL(DEFAULT_FORWARD_URL);
		result.setReturnURL("/page/infoUserGroupCfg.do?action=queryList");

		SMCCfgToUserGroup str = new SMCCfgToUserGroup();

		str.setId(Integer.valueOf(strID));
		str.setName(name);
		str.setDescription(desc);
		try {
			boolean bFlag = dao.update(str);
			if (bFlag)
				result.setError(new UiError("", "更新成功.", "", ""));
		} catch (Exception e) {
			result.setError(new UiError("", "更新失败.", "", ""));
		}
		result.setData(null);
		// result.setForwardURL("/page/infoUserGroupCfg.do?action=queryList");

		return result;
	}

	public ActionResult update(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionResult result = new ActionResult();
		String strID = req.getParameter("id");

		String forwardURL = req.getParameter("forwardURL");
		String returnURL = req.getParameter("returnURL");

		SMCCfgToUserGroup str = null;
		// 如果不填写forwardURL则为默认页面
		if ( StringUtil.isNull(forwardURL))
			forwardURL = DEFAULT_FORWARD_URL;

		if ((StringUtil.isNull(returnURL))
				|| StringUtil.isNull(strID) ) {
			result.setReturnURL(DEFAULT_RETURN_URL);
		} else {
			try {
				str = dao.get(Integer.parseInt(strID));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			result.setError(new UiError("", "删除成功.", "", ""));
			result.setData(str);
			result.setReturnURL(returnURL);
		}
		result.setForwardURL("/page/infoUserGroupCfgUpdate.jsp");

		return result;
	}

	public ActionResult del(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionResult result = new ActionResult();
		String strID = req.getParameter("groupid");

		String forwardURL = req.getParameter("forwardURL");
		String returnURL = req.getParameter("returnURL");

		// 如果不填写forwardURL则为默认页面
		if ( StringUtil.isNull(forwardURL))
			forwardURL = DEFAULT_FORWARD_URL;

		if ((StringUtil.isNull(returnURL))
				|| StringUtil.isNull(strID) ) {
			result.setReturnURL(DEFAULT_RETURN_URL);
		} else {
			try {
				dao.delete(Integer.parseInt(strID));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			result.setError(new UiError("", "删除成功.", "", ""));
			result.setData(null);
			result.setReturnURL(returnURL);
		}
		result.setForwardURL("/page/infoUserGroupCfg.do?action=queryList");

		return result;
	}

	public List<SMCCfgToUserGroup> getList(int page) {
		List<SMCCfgToUserGroup> list = null;
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
			SMCCfgToUserGroup data = dao.get(Integer.parseInt(id));
			result.setData(data);
		}
		result.setForwardURL("/page/infoUserGroupCfgView.jsp");
		return result;
	}

	public static void main(String[] args) {
		try {
			SmcCfgToUserGroupDAO dao = new SmcCfgToUserGroupDAO();
			for (int i = 0; i < 7; i++) {
				SMCCfgToUserGroup group = new SMCCfgToUserGroup();
				group.setDescription("group" + i);
				group.setName("group" + i);
				group.setId(i);
				group.setToUserGroupLevel(i);
				dao.put(group);
			}
		} catch (Exception e) {
		}
	}

}
