package cn.uway.smc.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.codec.CodecUtil;
import cn.uway.commons.type.StringUtil;
import cn.uway.ews.param.ActionResult;
import cn.uway.ews.param.PageQueryResult;
import cn.uway.ews.servlet.BasicServlet;
import cn.uway.smc.db.dao.SmcCfgSourceDAO;
import cn.uway.smc.db.pojo.SMCCfgSource;
import cn.uway.smc.web.page.Navigation;
import cn.uway.smc.web.page.UiError;

public class SmcInfoSourceCfgServlet extends BasicServlet<SmcCfgSourceDAO> {

	private final static Logger LOG = LoggerFactory
			.getLogger(SmcInfoSourceCfgServlet.class);

	private static final long serialVersionUID = 1L;

	public ActionResult queryList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageQueryResult<SMCCfgSource> qr = new PageQueryResult<SMCCfgSource>();
		// 由于这里使用的url传参数,要验证url的合法性
		response.setContentType("text/html");
		// 总页数从数据库获得
		String params = "";

		if (StringUtil.isNull(pageIndex))
			pageIndex = "1";

		params = "key1=value1&key2=value2&action=queryList";

		List<SMCCfgSource> list = getList(Integer.parseInt(pageIndex));

		qr.getDatas().clear();
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
		result.setForwardURL("/page/infoSourceCfg.jsp");
		result.setData(qr);
		result.setWparam(nav);
		return result;
	}

	public List<SMCCfgSource> getList(int page) {
		List<SMCCfgSource> list = null;
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
		String strID = req.getParameter("srcid");
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
				result.setError(new UiError("", "删除成功.", "", ""));
			} catch (Exception e) {
				result.setError(new UiError("", "删除失败 .", "", ""));
			}

			result.setData(null);
			result.setReturnURL(returnURL);
		}
		result.setForwardURL("/page/infoSourceCfg.do?action=queryList");

		return result;
	}

	public ActionResult add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionResult result = new ActionResult();
		result.setForwardURL(DEFAULT_FORWARD_URL);
		result.setReturnURL(DEFAULT_RETURN_URL);
		String id = req.getParameter("srcid");
		String sourceName = req.getParameter("sourceName");
		String userName = req.getParameter("userName");
		String passwd1 = req.getParameter("passwd1");
		String IP = req.getParameter("IP");
		String desc = req.getParameter("desc");

		SMCCfgSource source = new SMCCfgSource();
		source.setSrcid(Integer.valueOf(id));
		source.setName(sourceName);
		source.setUser(userName);
		source.setPwd(CodecUtil.toMD5(passwd1));
		source.setIpList(IP);
		source.setDescription(desc);
		try {
			dao.put(source);
			result.setError(new UiError("", "添加成功.", "", ""));
		} catch (Exception e) {
			result.setError(new UiError("", "添加失败.", "", ""));
			e.printStackTrace();
		}
		result.setForwardURL("/page/infoSourceCfg.do?action=queryList");
		return result;
	}

	public ActionResult update(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionResult result = new ActionResult();
		result.setForwardURL(DEFAULT_FORWARD_URL);
		result.setReturnURL(DEFAULT_RETURN_URL);
		String id = req.getParameter("srcid");
		try {
			SMCCfgSource source = dao.get(Integer.valueOf(id));
			result.setError(new UiError("", "更新成功.", "", ""));
			req.setAttribute("source", source);
		} catch (Exception e) {
			result.setError(new UiError("", "更新失败.", "", ""));
			e.printStackTrace();
		}
		result.setForwardURL("/page/infoSourceCfgUpdate.jsp");
		return result;
	}

	public void checkPassword(HttpServletRequest req, HttpServletResponse resp) {
		String id = req.getParameter("id");
		String passwd = req.getParameter("passwd");
		try {
			SMCCfgSource ids = dao.get(Integer.valueOf(id));
			if (!ids.getPwd().equals(CodecUtil.toMD5(passwd))) {
				resp.getWriter().write(" 密码错误，请重新确认输入");
			}
		} catch (Exception e) {
			LOG.error(" 密码错误，请重新确认输入");
		}
	}

	public ActionResult save(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionResult result = new ActionResult();
		result.setForwardURL(DEFAULT_FORWARD_URL);
		result.setReturnURL(DEFAULT_RETURN_URL);
		String id = req.getParameter("id");
		String sourceName = req.getParameter("sourceName");
		String userName = req.getParameter("userName");

		String passwd1 = req.getParameter("passwd");
		String IP = req.getParameter("IP");
		String desc = req.getParameter("desc");

		SMCCfgSource source = new SMCCfgSource();
		source.setSrcid(Integer.valueOf(id));
		source.setName(sourceName);
		source.setUser(userName);
		source.setPwd(StringUtil.isNull(passwd1) ? "" : passwd1.trim());
		source.setIpList(IP);
		source.setDescription(desc);

		try {
			dao.update(source);
			result.setError(new UiError("", "更新成功.", "", ""));
		} catch (Exception e) {
			result.setError(new UiError("", "更新失败.", e.getMessage(), ""));
		}
		return result;
	}

	public ActionResult viewInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionResult result = new ActionResult();
		String id = request.getParameter("srcid");
		if (id != null && !id.equals("")) {
			SMCCfgSource data = dao.get(Integer.parseInt(id));
			result.setData(data);
		}
		result.setForwardURL("/page/infoSourceCfgView.jsp");
		return result;
	}

	public static void main(String[] args) {
		try {
			SmcCfgSourceDAO dao = new SmcCfgSourceDAO();
			for (int i = 0; i < 3; i++) {
				SMCCfgSource source = new SMCCfgSource();
				// source.setSrcid(123);
				source.setSrcid(i);
				source.setIpList("127.0.0.1");
				source.setName("" + i);
				source.setPwd("c4ca4238a0b923820dcc509a6f75849b");
				source.setSrcid(i);
				source.setUser("" + i);
				source.setDescription("" + i);

				dao.put(source);

			}
			SMCCfgSource s = dao.get(1);
			System.out.println(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
