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
import cn.uway.smc.db.dao.SmcCfgToUserDAO;
import cn.uway.smc.db.dao.SmcCfgToUserGroupDAO;
import cn.uway.smc.db.pojo.SMCCfgToUser;
import cn.uway.smc.web.page.Navigation;
import cn.uway.smc.web.page.UiError;

public class SmcToUserCfg extends BasicServlet<SmcCfgToUserDAO> {

	private static final long serialVersionUID = 1L;

	public ActionResult queryList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageQueryResult<SMCCfgToUser> qr = new PageQueryResult<SMCCfgToUser>();
		// 由于这里使用的url传参数,要验证url的合法性
		response.setContentType("text/html");
		// 总页数从数据库获得
		String params = "";

		if (StringUtil.isNull(pageIndex))
			pageIndex = "1";

		params = "key1=value1&key2=value2&action=queryList";

		List<SMCCfgToUser> list = getList(Integer.parseInt(pageIndex));
		qr.setDatas(list);

		// 把所有参数设置在bean里在jsp页面取出来
		Navigation nav = new Navigation();

		List<SMCCfgToUser> ls = dao.list();

		if (ls != null) {
			String count = Integer.toString((int) java.lang.Math
					.ceil((double) ls.size() / 5));
			nav.setPageCount(count);
			nav.setPageIndex(pageIndex);
			nav.setParams(params);
		}
		SmcCfgToUserGroupDAO groups = new SmcCfgToUserGroupDAO();
		request.setAttribute("groups", groups.list());
		ActionResult result = new ActionResult();
		result.setForwardURL("/page/infoToUserCfg.jsp");
		result.setData(qr);
		result.setWparam(nav);
		return result;
	}

	public ActionResult del(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionResult result = new ActionResult();
		String strID = req.getParameter("id");
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
		result.setForwardURL(forwardURL);

		return result;
	}

	public ActionResult add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionResult result = new ActionResult();
		result.setForwardURL(DEFAULT_FORWARD_URL);
		result.setReturnURL("/page/infoToUserCfg.do?action=queryList");
		String tel = req.getParameter("tel");
		String email = req.getParameter("email");
		String desc = req.getParameter("desc");
		String userName = req.getParameter("userName");
		String groupId = req.getParameter("groupId");

		SMCCfgToUser user = new SMCCfgToUser();

		user.setName(userName);
		user.setGroupId(Integer.valueOf(groupId));
		user.setCellphone(tel);
		user.setDescription(desc);
		user.setEmail(email);

		try {
			dao.put(user);
			result.setError(new UiError("", "添加成功.", "", ""));
		} catch (Exception e) {
			result.setError(new UiError("", "添加失败.", "", ""));
			e.printStackTrace();
		}
		// result.setForwardURL("/page/infoToUserCfg.do?action=queryList");
		return result;
	}

	public List<SMCCfgToUser> getList(int page) {
		List<SMCCfgToUser> list = null;
		try {
			List<SMCCfgToUser> ls = dao.list();
			if (ls == null)
				return null;
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

	public ActionResult save(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionResult result = new ActionResult();
		String strID = req.getParameter("id");
		String name = req.getParameter("name");
		String groupId = req.getParameter("groupId");
		String cellphone = req.getParameter("tel");
		String email = req.getParameter("email");
		String description = req.getParameter("desc");

		result.setForwardURL(DEFAULT_FORWARD_URL);
		result.setReturnURL("/page/infoToUserCfg.do?action=queryList");

		SMCCfgToUser str = null;
		try {
			str = dao.get(Integer.parseInt(strID));
			str.setId(Integer.valueOf(strID));
			str.setName(name);
			str.setCellphone(cellphone);
			str.setDescription(description);
			str.setEmail(email);
			str.setGroupId(Integer.valueOf(groupId));
			dao.put(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		result.setError(new UiError("", "更新成功.", "", ""));
		result.setData(null);
		// result.setForwardURL("/page/infoToUserCfg.do?action=queryList");

		return result;
	}

	public ActionResult update(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionResult result = new ActionResult();
		String strID = req.getParameter("id");

		String forwardURL = req.getParameter("forwardURL");
		String returnURL = req.getParameter("returnURL");

		SMCCfgToUser str = null;
		// 如果不填写forwardURL则为默认页面
		if ( StringUtil.isNull(forwardURL))
			forwardURL = DEFAULT_FORWARD_URL;

		if ((StringUtil.isNull(returnURL))
				|| StringUtil.isNull(strID) ) {
			result.setReturnURL(DEFAULT_RETURN_URL);
		} else {
			try {
				str = dao.get(Integer.parseInt(strID));
				SmcCfgToUserGroupDAO groups = new SmcCfgToUserGroupDAO();
				req.setAttribute("groups", groups.list());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			result.setError(new UiError("", "删除成功.", "", ""));
			result.setData(str);
			result.setReturnURL(returnURL);
		}
		result.setForwardURL("/page/infoToUserCfgUpdate.jsp");

		return result;
	}

	public ActionResult viewInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionResult result = new ActionResult();
		String id = request.getParameter("id");
		if (id != null && !id.equals("")) {
			SMCCfgToUser data = dao.get(Integer.parseInt(id));
			result.setData(data);
		}
		result.setForwardURL("/page/infoToUserCfgView.jsp");
		return result;
	}

	public static void main(String[] args) {
		try {
			SmcCfgToUserDAO dao = new SmcCfgToUserDAO();
			for (int i = 0; i <= 3; i++) {
				SMCCfgToUser touser = new SMCCfgToUser();
				touser.setId(i);
				touser.setCellphone("13500000000");
				touser.setGroupId(i);
				touser.setDescription("" + i);
				touser.setEmail("uway@uway.cn");
				touser.setName("user");
				dao.put(touser);
			}
		} catch (Exception e) {
		}
	}

}
