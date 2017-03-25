package cn.uway.smc.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.StringUtil;
import cn.uway.ews.param.ActionResult;
import cn.uway.ews.param.PageQueryResult;
import cn.uway.ews.servlet.BasicServlet;
import cn.uway.smc.db.dao.SmcUserDAO;
import cn.uway.smc.db.pojo.SMCUser;
import cn.uway.smc.web.page.Navigation;
import cn.uway.smc.web.page.UiError;

public class SmcUserServlet extends BasicServlet<SmcUserDAO> {

	private final static Logger log = LoggerFactory
			.getLogger(SmcUserServlet.class);

	private static final long serialVersionUID = 1L;

	public ActionResult queryList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageQueryResult<SMCUser> qr = new PageQueryResult<SMCUser>();
		// 由于这里使用的url传参数,要验证url的合法性
		response.setContentType("text/html");
		// 总页数从数据库获得
		String params = "";
		if (StringUtil.isNull(pageIndex))
			pageIndex = "1";

		params = "key1=value1&key2=value2&action=queryList";

		// 在界面我就用jstl进行迭代
		// request.setAttribute("list", getList(Integer.parseInt(pageIndex)));
		List<SMCUser> list = getList(Integer.parseInt(pageIndex));
		qr.setDatas(list);

		Navigation nav = new Navigation();
		// 设置总页数
		String count = Integer.toString((int) java.lang.Math.ceil((double) dao
				.list().size() / 5));
		nav.setPageCount(count);
		nav.setPageIndex(pageIndex);
		nav.setParams(params);

		ActionResult result = new ActionResult();
		result.setForwardURL("/page/user.jsp");
		result.setData(qr);
		result.setWparam(nav);
		return result;
	}

	public List<SMCUser> getList(int page) {
		List<SMCUser> list = null;
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
		result.setReturnURL("/page/user.do?action=queryList");
		String userName = req.getParameter("userName");
		String passwd = req.getParameter("passwd1");
		String desc = req.getParameter("desc");
		SMCUser user = new SMCUser();
		user.setUserName(userName);
		user.setPassWord(passwd);
		user.setDes(desc);
		try {
			dao.put(user);
			result.setError(new UiError("", "添加成功.", "", ""));
		} catch (Exception e) {
			result.setError(new UiError("", "添加失败.", "", ""));
			e.printStackTrace();
		}
		// result.setForwardURL("/page/user.jsp");
		return result;
	}

	public ActionResult updatePage(HttpServletRequest req,
			HttpServletResponse resp) {
		String id = req.getParameter("id");
		SMCUser user = null;
		try {
			List<SMCUser> list = dao.list();
			for (SMCUser li : list) {
				String keyId = String.valueOf(li.getId());
				if (id.equals(keyId)) {
					user = li;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ActionResult result = new ActionResult();
		result.setForwardURL("/page/userUpdate.jsp");
		result.setData(user);
		return result;
	}

	public ActionResult update(HttpServletRequest req, HttpServletResponse resp) {
		ActionResult result = new ActionResult();
		result.setForwardURL(DEFAULT_FORWARD_URL);
		result.setReturnURL("/page/user.do?action=queryList");
		String id = req.getParameter("id");
		String userName = req.getParameter("userName");
		String passwd1 = req.getParameter("passwd1");
		// String passwd2 =req.getParameter("passwd2");
		String desc = req.getParameter("desc");

		SMCUser user = new SMCUser();
		user.setId(Integer.parseInt(id));
		user.setUserName(userName);
		user.setPassWord(passwd1);
		user.setDes(desc);

		try {
			boolean bool = dao.delete(Integer.parseInt(id));
			if (bool) {

				try {
					dao.put(user);
					result.setError(new UiError("", "更新成功.", "", ""));
				} catch (Exception e) {
					result.setError(new UiError("", "更新失败.", "", ""));
					e.printStackTrace();
				}
			} else {
				result.setError(new UiError("", "更新失败.", "", ""));
			}
		} catch (Exception e) {

		}

		return result;
	}

	public static void main(String[] args) throws Exception {
		SmcUserDAO dao = new SmcUserDAO();
		for (int i = 0; i <= 7; i++) {
			SMCUser u = new SMCUser();
			u.setId(i);
			u.setDes("user" + i);
			u.setUserName("" + i);
			u.setPassWord("1");
			dao.put(u);
		}
		List<SMCUser> list = dao.list();
		for (SMCUser s : list) {
			log.debug(s.getUserName() + "-------" + s.getPassWord());
		}
	}

}
