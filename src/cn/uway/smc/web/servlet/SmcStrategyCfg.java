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
import cn.uway.smc.db.dao.SmcCfgLevelDAO;
import cn.uway.smc.db.dao.SmcCfgSourceDAO;
import cn.uway.smc.db.dao.SmcCfgStrategyDAO;
import cn.uway.smc.db.dao.SmcCfgToUserGroupDAO;
import cn.uway.smc.db.pojo.SMCCfgStrategy;
import cn.uway.smc.web.page.Navigation;
import cn.uway.smc.web.page.UiError;

public class SmcStrategyCfg extends BasicServlet<SmcCfgStrategyDAO> {

	// private final static Logger log =
	// LoggerFactory.getLogger(SmcStrategyCfg.class);
	private static final long serialVersionUID = 1L;

	public ActionResult queryList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageQueryResult<SMCCfgStrategy> qr = new PageQueryResult<SMCCfgStrategy>();
		// 由于这里使用的url传参数,要验证url的合法性
		response.setContentType("text/html");
		// 总页数从数据库获得
		String params = "";

		if (StringUtil.isNull(pageIndex))
			pageIndex = "1";

		// 注意这里设置的下面要对应的取值
		params = "key1=value1&key2=value2&action=queryList";

		List<SMCCfgStrategy> list = getList(Integer.parseInt(pageIndex));
		qr.setDatas(list);

		// 把所有参数设置在bean里在jsp页面取出来
		Navigation nav = new Navigation();

		// 设置总页数
		String count = Integer.toString((int) java.lang.Math.ceil((double) dao
				.list().size() / 5));
		nav.setPageCount(count);
		nav.setPageIndex(pageIndex);
		nav.setParams(params);

		// 获得用户分组
		SmcCfgToUserGroupDAO group = new SmcCfgToUserGroupDAO();
		request.setAttribute("groups", group.list());
		// 获得消息源
		SmcCfgSourceDAO source = new SmcCfgSourceDAO();
		request.setAttribute("sources", source.list());
		// 获得级别
		SmcCfgLevelDAO level = new SmcCfgLevelDAO();
		request.setAttribute("levels", level.list());

		ActionResult result = new ActionResult();
		result.setForwardURL("/page/infoStrategyCfg.jsp");
		result.setData(qr);
		result.setWparam(nav);
		return result;
	}

	public List<SMCCfgStrategy> getList(int page) {
		List<SMCCfgStrategy> list = null;
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
		if (forwardURL == "" || forwardURL == null)
			forwardURL = DEFAULT_FORWARD_URL;

		if ((returnURL == "" || returnURL == null)
				|| (strID == "" || strID == null)) {
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
		result.setForwardURL("/page/infoStrategyCfg.do?action=queryList");

		return result;
	}

	public ActionResult update(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionResult result = new ActionResult();
		String strID = req.getParameter("id");
		String forwardURL = req.getParameter("forwardURL");
		String returnURL = req.getParameter("returnURL");

		SMCCfgStrategy str = null;

		try {
			// 如果不填写forwardURL则为默认页面
			if ( StringUtil.isNull(forwardURL))
				forwardURL = DEFAULT_FORWARD_URL;

			if ((StringUtil.isNull(returnURL))
					|| StringUtil.isNull(strID) ) {
				result.setReturnURL(DEFAULT_RETURN_URL);
			} else {

				str = dao.get(Integer.parseInt(strID));

				// 获得用户分组
				try {
					SmcCfgToUserGroupDAO group = new SmcCfgToUserGroupDAO();
					req.setAttribute("groups", group.list());
					// 获得消息源
					SmcCfgSourceDAO source = new SmcCfgSourceDAO();
					req.setAttribute("sources", source.list());
					// 获得级别
					SmcCfgLevelDAO level = new SmcCfgLevelDAO();
					req.setAttribute("levels", level.list());
				} catch (Exception e) {
					e.printStackTrace();
				}
				req.setAttribute("strategy", str);

				result.setError(new UiError("", "删除成功.", "", ""));
				result.setData(null);
				result.setReturnURL(returnURL);
			}
			result.setForwardURL("/page/infoStrategyCfgUpdate.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public ActionResult save(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionResult result = new ActionResult();
		result.setForwardURL(DEFAULT_FORWARD_URL);
		result.setReturnURL("/page/infoStrategyCfg.do?action=queryList");
		String id = req.getParameter("id");
		String srcid = req.getParameter("srcid");
		String levelid = req.getParameter("levelid");
		String groupId = req.getParameter("id");

		String ttl = req.getParameter("ttl");
		String endoOffsetTime = req.getParameter("endoOffsetTime");
		String sendTimes = req.getParameter("sendTimes");

		String sendInterval = req.getParameter("sendInterval");
		String sendWay = req.getParameter("sendWay");
		String resendWhenFail = req.getParameter("resendWhenFail");
		String takeEffect = req.getParameter("takeEffect");

		SMCCfgStrategy stra = new SMCCfgStrategy();
		stra.setId(Integer.valueOf(id));
		stra.setLevelid(Integer.valueOf(levelid));
		stra.setSrcid(Integer.valueOf(srcid));
		stra.setGroupId(Integer.valueOf(groupId));
		if (StringUtil.isNotNull(ttl)) {
			stra.setTtl(Integer.valueOf(ttl));
		} else {
			// 默认存活时间120
			stra.setTtl(120);
		}

		stra.setEndoOffsetTime(Integer.valueOf(endoOffsetTime));
		stra.setSendTimes(Integer.valueOf(sendTimes));
		stra.setSendInterval(Integer.valueOf(sendInterval));
		if (StringUtil.isNotNull(sendWay)) {
			stra.setSendWay(Integer.valueOf(sendWay));
		} else {
			// 默认发送方式:以短信发送
			stra.setSendWay(1);
		}
		stra.setResendWhenFail(Integer.valueOf(resendWhenFail));
		stra.setTakeEffect(Integer.valueOf(takeEffect));
		try {
			boolean bool = dao.update(stra);
			if (bool) {
				result.setError(new UiError("", "更新成功.", "", ""));
			} else
				result.setError(new UiError("", "更新失败.", "", ""));
		} catch (Exception e) {
			result.setError(new UiError("", "更新失败.", "", ""));
		}
		return result;
	}

	public ActionResult add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionResult result = new ActionResult();
		result.setForwardURL(DEFAULT_FORWARD_URL);
		result.setReturnURL(DEFAULT_RETURN_URL);
		String srcid = req.getParameter("srcid");
		String levelid = req.getParameter("levelid");
		String groupId = req.getParameter("id");

		String ttl = req.getParameter("ttl");
		String endoOffsetTime = req.getParameter("endoOffsetTime");
		String sendTimes = req.getParameter("sendTimes");

		String sendInterval = req.getParameter("sendInterval");
		String sendWay = req.getParameter("sendWay");
		String resendWhenFail = req.getParameter("resendWhenFail");
		String takeEffect = req.getParameter("takeEffect");

		SMCCfgStrategy stra = new SMCCfgStrategy();
		if (StringUtil.isNotNull(levelid)) {
			stra.setLevelid(Integer.valueOf(levelid));
		}
		if (StringUtil.isNotNull(srcid)) {
			stra.setSrcid(Integer.valueOf(srcid));
		}
		if (StringUtil.isNotNull(groupId)) {
			stra.setGroupId(Integer.valueOf(groupId));
		}
		if (StringUtil.isNotNull(ttl)) {
			stra.setTtl(Integer.valueOf(ttl));
		} else {
			// 默认存活时间120
			stra.setTtl(120);
		}

		if (StringUtil.isNotNull(endoOffsetTime)) {
			stra.setEndoOffsetTime(Integer.valueOf(endoOffsetTime));
		}
		if (StringUtil.isNotNull(sendTimes)) {
			stra.setSendTimes(Integer.valueOf(sendTimes));
		}
		if (StringUtil.isNotNull(sendInterval)) {
			stra.setSendInterval(Integer.valueOf(sendInterval));
		}
		if (StringUtil.isNotNull(sendWay)) {
			stra.setSendWay(Integer.valueOf(sendWay));
		} else {
			// 默认发送方式:以短信发送
			stra.setSendWay(1);
		}
		if (StringUtil.isNotNull(resendWhenFail)) {
			stra.setResendWhenFail(Integer.valueOf(resendWhenFail));
		}
		if (StringUtil.isNotNull(takeEffect)) {
			stra.setTakeEffect(Integer.valueOf(takeEffect));
		}
		try {
			dao.put(stra);
			result.setError(new UiError("", "添加成功.", "", ""));
		} catch (Exception e) {
			result.setError(new UiError("", "添加失败.", "", ""));
			e.printStackTrace();
		}
		// result.setForwardURL("/page/user.jsp");
		return result;
	}

	public ActionResult viewInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionResult result = new ActionResult();
		String id = request.getParameter("id");
		if (id != null && !id.equals("")) {
			SMCCfgStrategy data = dao.get(Integer.parseInt(id));
			result.setData(data);
		}
		result.setForwardURL("/page/infoStrategyCfgView.jsp");
		return result;
	}

	public static void main(String[] args) {
		try {
			SmcCfgStrategyDAO dao = new SmcCfgStrategyDAO();
			for (int i = 0; i < 11; i++) {
				SMCCfgStrategy s = new SMCCfgStrategy();
				s.setId(i);
				s.setLevelid(i);
				s.setResendWhenFail(i);
				s.setSendInterval(2);
				s.setSendTimes(3);
				s.setSendWay(1);
				s.setSrcid(i);
				s.setTtl(120);
				s.setTakeEffect(1);
				dao.put(s);
			}

			List<SMCCfgStrategy> li = dao.list();
			for (SMCCfgStrategy ss : li) {
				System.out.println(ss.getSendWay());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
