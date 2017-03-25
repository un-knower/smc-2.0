package cn.uway.smc.ui.province;

import java.net.URL;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.DateUtil;
import cn.uway.smc.db.conn.DBUtil;
import cn.uway.smc.db.dao.SmcDataHistoryDAO;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.db.pojo.SMCDataHistory;
import cn.uway.smc.util.ConstDef;

public class HbProvince extends AbstractProvince {

	private static Logger logger = LoggerFactory.getLogger(HbProvince.class);

	private static String SERVICE_URL = null;

	private static String AP_NAME = null;

	private static String AP_PWD = null;

	private static String AP_COMPCODE = null;

	private static String AP_USER_CODE = null;

	public HbProvince() {
		super();
		init();
	}

	public int sendSMS(String username, String password, int type, int src_id,
			int level, String to_users, int send_way, String content,
			String send_time) {
		logger.debug("湖北个性化短信发送。");

		SMCDataHistory history = new SMCDataHistory();
		Date endDate = new Date();
		history.setOccurTime(endDate);
		history.setSendTime(send_time);
		history.setContent(content);
		history.setSrcId(src_id);
		history.setLevelId(level);
		history.setOccurTime(endDate);
		history.setStampTime(endDate);
		history.setSendTimeExclude(null);
		history.setSendWay(send_way);

		String[] phones = getSendSmsUsers(to_users).split(",");

		APServiceSoapBindingStub apService = null;
		try {
			apService = new APServiceSoapBindingStub(new URL(SERVICE_URL), null);
		} catch (Throwable e) {
			logger.error("短信接口连接失败，原因：" + e.getMessage(), e);
			history.setCause("短信接口连接失败");
			history.setSentResult(-1);
			storeHistorySMSMsg(history, to_users);
			return -1;
		}

		String rsp = "";
		int failCount = 0;
		for (String calledNumber : phones) {
			try {
				logger.debug("湖北个性化短信 Req：apName=" + AP_NAME + "、apPwd"
						+ AP_PWD + "、compCode=" + AP_COMPCODE + "、account="
						+ AP_USER_CODE + "、calledNumber=" + calledNumber
						+ "、send_time=" + send_time + "、content=" + content);
				rsp = apService.sendMessage(AP_NAME, AP_PWD, AP_COMPCODE,
						AP_USER_CODE, calledNumber, send_time, content);
			} catch (Throwable e) {
				failCount++;
				logger.error("向用户：[" + calledNumber + "]发送短信失败，原因：", e);
				history.setCause("向用户：[" + calledNumber + "]发送短信失败，原因："
						+ e.getMessage());
				history.setSentResult(-1);
				storeHistorySMSMsg(history, calledNumber);
			}

			logger.debug("向用户：[" + calledNumber + "]发送短信结果：" + rsp);
			history.setCause("向用户：[" + calledNumber + "]发送短信结果：" + rsp);
			history.setSentResult(0);
			storeHistorySMSMsg(history, calledNumber);

		}
		return failCount;
	}

	public static void storeHistorySMSMsg(SMCDataHistory history,
			String calledNumber) {
		try {
			history.setId(DBUtil.getSeq(ConstDef.SEQ_EXPRESS));
			history.setToUsers(calledNumber);

			SmcDataHistoryDAO expressDataHistoryDAO = new SmcDataHistoryDAO();
			expressDataHistoryDAO.add(history);
		} catch (Throwable e) {
			logger.error("短信息历史数据存储失败，原因：", e);
		}
	}

	public void init() {

		SERVICE_URL = p.getProperty("endPort");
		AP_NAME = p.getProperty("username");
		AP_PWD = p.getProperty("password");

		AP_COMPCODE = p.getProperty("ap_compcode");
		AP_USER_CODE = p.getProperty("ap_usercode");
	}

	@Override
	public boolean sendMessage(SMCData data) {
		int result = sendSMS(data.getUsername(), data.getPassword(),
				data.getType(), data.getSrcid(), data.getLevelid(),
				data.getToUsers(), data.getSendWay(), data.getContent(),
				data.getSendTime());
		return result >= 0;
	}

	public static void main(String[] args) {
		HbProvince h = new HbProvince();
		for (int i = 0; i < 1; i++) {
			SMCData data = new SMCData();
			data.setId(i);
			data.setType(1);
			data.setSrcid(1);
			data.setSendTime(DateUtil.getDateString(new Date()));
			data.setLevelid(1);
			data.setToUsers("<PHONE>8618620374083</PHONE>");
			data.setContent("test sms11");
			h.sendMessage(data);
		}

	}
}