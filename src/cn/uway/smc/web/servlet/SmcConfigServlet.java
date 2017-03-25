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
import cn.uway.smc.db.dao.SmcCfgSysDAO;
import cn.uway.smc.db.pojo.SMCCfgSys;
import cn.uway.smc.web.page.Navigation;
import cn.uway.smc.web.page.UiError;

public class SmcConfigServlet extends BasicServlet<SmcCfgSysDAO> {

	private final static Logger LOG = LoggerFactory
			.getLogger(SmcConfigServlet.class);

	private static final long serialVersionUID = 1L;

	public ActionResult queryList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageQueryResult<SMCCfgSys> qr = new PageQueryResult<SMCCfgSys>();
		response.setContentType("text/html");
		String params = "";

		if (StringUtil.isNull(pageIndex))
			pageIndex = "1";

		// 注意这里设置的下面要对应的取值
		params = "key1=value1&key2=value2&action=queryList";

		// 在界面我就用jstl进行迭代
		List<SMCCfgSys> list = getList(Integer.parseInt(pageIndex));
		qr.setDatas(list);

		Navigation nav = new Navigation();

		// 设置总页数
		String count = Integer.toString((int) java.lang.Math.ceil((double) dao
				.list().size() / 1));
		nav.setPageCount(count);
		nav.setPageIndex(pageIndex);
		nav.setParams(params);

		ActionResult result = new ActionResult();
		result.setForwardURL("/page/config.jsp");
		result.setData(qr);
		result.setWparam(nav);
		return result;
	}

	public List<SMCCfgSys> getList(int page) {
		List<SMCCfgSys> list = null;
		try {
			int listSize = dao.list().size();

			if (listSize < 1) {
				list = dao.list().subList(0, listSize);
			} else if ((5 * page) > listSize) {
				list = dao.list().subList(1 * (page - 1), listSize);
			} else {
				list = dao.list().subList(1 * (page - 1), 1 * page);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ActionResult updatePage(HttpServletRequest req,
			HttpServletResponse resp) {
		String id = req.getParameter("pageId");
		SMCCfgSys cfg = null;
		try {
			cfg = dao.getById(Integer.valueOf(id));
		} catch (Exception e) {
			LOG.error("查找策略失败，原因:{}", e);
		}
		ActionResult result = new ActionResult();
		result.setForwardURL("/page/configUpdate.jsp");
		result.setData(cfg);
		return result;
	}

	public ActionResult update(HttpServletRequest req, HttpServletResponse resp) {
		ActionResult result = new ActionResult();
		result.setForwardURL(DEFAULT_FORWARD_URL);
		result.setReturnURL("/page/config.do?action=queryList");

		String id = req.getParameter("id");
		String smtpHost = req.getParameter("smtpHost");
		String mailUser = req.getParameter("mailUser");
		String mailPwd = req.getParameter("mailPwd");
		String nodeId = req.getParameter("nodeId");
		String feeType = req.getParameter("feeType");
		String agentFlag = req.getParameter("agentFlag");
		String moRelateToMtFlag = req.getParameter("moRelateToMtFlag");
		String priority = req.getParameter("priority");
		String reportFlag = req.getParameter("reportFlag");
		String tpPid = req.getParameter("tpPid");
		String tpUdhi = req.getParameter("tpUdhi");
		String messagecoding = req.getParameter("messagecoding");
		String messageType = req.getParameter("messageType");
		String spNumber = req.getParameter("spNumber");
		String chargeNumber = req.getParameter("chargeNumber");
		String corpId = req.getParameter("corpId");
		String serviceType = req.getParameter("serviceType");
		String feeValue = req.getParameter("feeValue");
		String givenValue = req.getParameter("givenValue");
		String expireTime = req.getParameter("expireTime");
		String scheduleTime = req.getParameter("scheduleTime");
		String serverIp = req.getParameter("serverIp");
		String serverPort = req.getParameter("serverPort");
		String smsUserName = req.getParameter("smsUserName");
		String smsUserPwd = req.getParameter("smsUserPwd");
		String securityMaxSendCountDay = req
				.getParameter("securityMaxSendCountDay");
		String securityMaxSendCountHour = req
				.getParameter("securityMaxSendCountHour");
		String extTableDriver = req.getParameter("extTableDriver");
		String extTableUrl = req.getParameter("extTableUrl");
		String extTableUser = req.getParameter("extTableUser");
		String extTablePwd = req.getParameter("extTablePwd");
		String description = req.getParameter("description");
		String takeEffect = req.getParameter("takeEffect");
		String serverReceivePort = req.getParameter("serverReceivePort");
		String serverReceivePwd = req.getParameter("serverReceivePwd");
		String serverReceiveUserName = req
				.getParameter("serverReceiveUserName");

		SMCCfgSys cfg = new SMCCfgSys();
		cfg.setId(Integer.parseInt(id));
		cfg.setSmtpHost(smtpHost);
		cfg.setMailUser(mailUser);
		cfg.setMailPwd(mailPwd);
		cfg.setNodeId((int) Long.parseLong(nodeId));
		cfg.setFeeType(Integer.parseInt(feeType));
		cfg.setAgentFlag(Integer.parseInt(agentFlag));
		cfg.setMoRelateToMtFlag(Integer.parseInt(moRelateToMtFlag));
		cfg.setPriority(Integer.parseInt(priority));
		cfg.setReportFlag(Integer.parseInt(reportFlag));
		cfg.setTpPid(Integer.parseInt(tpPid));
		cfg.setTpUdhi(Integer.parseInt(tpUdhi));
		cfg.setMessagecoding(Integer.parseInt(messagecoding));
		cfg.setMessageType(Integer.parseInt(messageType));
		cfg.setSpNumber(spNumber);
		cfg.setChargeNumber(chargeNumber);
		cfg.setCorpId(corpId);
		cfg.setServiceType(serviceType);
		cfg.setFeeValue(feeValue);
		cfg.setGivenValue(givenValue);
		cfg.setExpireTime(expireTime);
		cfg.setScheduleTime(scheduleTime);
		cfg.setServerIp(serverIp);
		cfg.setServerPort(Integer.parseInt(serverPort));
		cfg.setSmsUserName(smsUserName);
		cfg.setSmsUserPwd(smsUserPwd);
		cfg.setSecurityMaxSendCountDay(Integer
				.parseInt(securityMaxSendCountDay));
		cfg.setSecurityMaxSendCountHour(Integer
				.parseInt(securityMaxSendCountHour));
		cfg.setExtTableDriver(extTableDriver);
		cfg.setExtTableUrl(extTableUrl);
		cfg.setExtTableUser(extTableUser);
		cfg.setExtTablePwd(extTablePwd);
		cfg.setDescription(description);
		cfg.setTakeEffect(Integer.parseInt(takeEffect));

		cfg.setServerReceivePort(Integer.parseInt(serverReceivePort));
		cfg.setServerReceivePwd(serverReceivePwd);

		cfg.setServerReceiveUserName(serverReceiveUserName);
		try {
			boolean bool = dao.update(cfg);
			if (bool) {
				result.setError(new UiError("", "更新成功.", "", ""));
			} else {
				result.setError(new UiError("", "更新失败.", "", ""));
			}
		} catch (Exception e) {
			result.setError(new UiError("", "更新失败.", "", ""));
		}
		return result;
	}

	public static void main(String[] args) {

		SmcCfgSysDAO dao = new SmcCfgSysDAO();
		SMCCfgSys cfg = new SMCCfgSys();
		cfg.setId(3);

		try {
			dao.put(cfg);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			List<SMCCfgSys> list = dao.list();
			System.out.println(list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
