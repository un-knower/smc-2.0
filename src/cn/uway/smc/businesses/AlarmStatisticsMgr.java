package cn.uway.smc.businesses;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.DateUtil;
import cn.uway.commons.type.StringUtil;
import cn.uway.smc.db.conn.DBUtil;
import cn.uway.smc.db.dao.SmcCfgToUserDAO;
import cn.uway.smc.db.pojo.SMCCfgSys;
import cn.uway.smc.db.pojo.SMCCfgToUser;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.util.ConstDef;
import cn.uway.smc.util.TimeIntervalMgr;

/**
 * 发送短信息统计管理
 */
public class AlarmStatisticsMgr {

	private final static Logger LOG = LoggerFactory
			.getLogger(AlarmStatisticsMgr.class);

	private boolean isRunning = true;

	private long onehour = 60 * 60 * 1000;// 一小时

	private long oneDay = 24 * onehour;// 一天

	private AlarmStatistics statistics = null;

	private AlarmStatisticsMgr() {
		init();
	}

	private void init() {
		statistics = new AlarmStatistics();
		statistics.setDaySendCount(0);
		statistics.setHourCountSum(0);
		statistics.setHourSendCount(0);

	}

	private static class SMCSenderMgrContainer {

		private static AlarmStatisticsMgr instance = new AlarmStatisticsMgr();
	}

	public synchronized static AlarmStatisticsMgr getInstance() {
		return SMCSenderMgrContainer.instance;
	}

	/**
	 * 启动告警进程
	 */
	public void start() {
		LOG.debug("启动系统告警进程");
		new AlarmThread().start();
	}

	/**
	 * @param content
	 *            发送内容
	 * @param sendWay
	 *            发送方式
	 * @return 短消息发送包
	 */
	private SMCData builderSMCExpress(String content, int sendWay) {
		SMCData smcData = new SMCData();

		SmcCfgToUserDAO userDao = new SmcCfgToUserDAO();
		List<SMCCfgToUser> userList = null;
		StringBuilder phones = new StringBuilder(ConstDef.PHONEFLAGBEGIN);
		StringBuilder emails = new StringBuilder(ConstDef.EMAILFLAGBEGIN);
		try {
			userList = userDao.list();
			for (SMCCfgToUser user : userList) {
				if (StringUtil.isNotNull(user.getCellphone()))
					phones.append(user.getCellphone()).append(ConstDef.SPLIT);
				if (StringUtil.isNotNull(user.getEmail()))
					emails.append(user.getEmail()).append(ConstDef.SPLIT);
			}
			phones.append(ConstDef.PHONEFLAGEND);
			emails.append(ConstDef.EMAILFLAGEND);
			smcData.setToUsers(phones.toString() + "" + emails.toString());
			smcData.setSendWay(sendWay);
			smcData.setId(DBUtil.getSeq(ConstDef.SEQ_EXPRESS));
			smcData.setLevelid(1);
			smcData.setSrcid(1);
			Date now = TimeIntervalMgr.getInstance().getDateValue(new Date());

			smcData.setOccurTime(now);
			smcData.setSendTime(DateUtil.getDateString(now));
			smcData.setSentOkTimes(0);
			smcData.setSubject(ConstDef.SMCALARM);
			smcData.setContent(content);

		} catch (Exception e) {
			LOG.error("构建系统告警短消息失败.", e);
		}

		return smcData;
	}

	/**
	 * @return 是否运行中
	 */
	public synchronized boolean isRunning() {
		return isRunning;
	}

	public synchronized void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public synchronized void close() {
		isRunning = false;
	}

	/**
	 * 告警线程
	 */
	class AlarmThread extends Thread {

		@Override
		public void run() {
			statistics();
		}

		/**
		 * 统计
		 */
		public void statistics() {
			SMCCfgSys sys = SystemMgr.getInstance().getSys();

			Calendar hourSta = Calendar.getInstance();
			hourSta.set(Calendar.MINUTE, 0);
			hourSta.set(Calendar.SECOND, 0);

			Calendar daySta = Calendar.getInstance();
			daySta.set(Calendar.HOUR, 0);
			daySta.set(Calendar.MINUTE, 0);
			daySta.set(Calendar.SECOND, 0);

			Date day = daySta.getTime();

			long begin = hourSta.getTime().getTime();
			long next = 0;
			int mincount = 0;
			while (isRunning()) {
				try {
					next = new Date().getTime();
					int count = 0;
					int daycount = 0;

					// 每小时统计一次
					if (next - begin >= onehour) {
						count = statistics.getHourSendCount();

						// 如果一个小时发送的短信息超过1000，这发出告警
						if (count >= sys.getSecurityMaxSendCountHour()) {
							String info = DateUtil
									.getDateString(new Date(begin))
									+ "至"
									+ DateUtil.getDateString(new Date(begin
											+ onehour));
							LOG.info(info + " , 这小时已经发送信息条数： " + count);

							String content = "SMC系统告警:"
									+ DateUtil.getDateString(new Date(begin))
									+ "-"
									+ DateUtil.getDateString(new Date(begin
											+ onehour)) + " , 发送短信超过系统配置"
									+ sys.getSecurityMaxSendCountHour()
									+ " 条,SMC系统退出,请重启并设置，如有疑问，请联系开发人员，谢谢！";

							SMCData smcData = builderSMCExpress(content, 1);
							SystemMgr.getInstance().sender(smcData);
							LOG.info(smcData + "  " + content
									+ " , SMC系统告警发送成功.系统退出");
							System.exit(0);
						}
						begin = next;

						statistics.setHourCountSum(statistics.getHourCountSum()
								+ statistics.getHourSendCount());
						statistics.setHourSendCount(0);

					}
					// 每天统计一次
					if (next - day.getTime() >= oneDay) {
						// 如果一个天发送的短信息超过3000，这发出告警
						String info = DateUtil.getDateString(day)
								+ "-"
								+ DateUtil.getDateString(new Date(day.getTime()
										+ oneDay));

						daycount = statistics.getHourCountSum();

						LOG.info(info + "当天发送短信个数： " + daycount);

						if (daycount >= sys.getSecurityMaxSendCountDay()) {
							String content = "SMC系统告警:"
									+ DateUtil.getDateString(day)
									+ "-"
									+ DateUtil.getDateString(new Date(day
											.getTime() + oneDay))
									+ " 发送短信超过系统配置"
									+ sys.getSecurityMaxSendCountHour()
									+ "条,SMC系统退出,请重启并设置，如有疑问，请联系开发人员，谢谢!";

							LOG.info(content);

							SMCData smcExpressData = builderSMCExpress(content,
									3);
							SystemMgr.getInstance().sender(smcExpressData);

							LOG.info(smcExpressData + " , SMC系统告警发送成功.");

							clear();
							System.exit(0);
						}

						day = new Date(day.getTime() + oneDay);
						clear();
					}
					mincount++;
					// 5*12=60 一分钟打印下统计信息
					if (mincount == 36) {
						String dayinfo = DateUtil.getDateString(day)
								+ "-"
								+ DateUtil.getDateString(new Date(day.getTime()
										+ oneDay));

						String hourInfo = DateUtil
								.getDateString(new Date(begin))
								+ "至"
								+ DateUtil.getDateString(new Date(begin
										+ onehour));

						LOG.info(dayinfo + "当天发送短信息条："
								+ statistics.getHourCountSum() + " 条.  "
								+ hourInfo + " 发送信息条数："
								+ statistics.getHourSendCount() + " 条. ");
						mincount = 0;
					}
				} catch (Exception e) {
					LOG.error("统计短信失败：原因:{}", e);
				}
				try {
					Thread.sleep(5 * 1000);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	private void clear() {
		statistics.setDaySendCount(0);
		statistics.setHourCountSum(0);
		statistics.setDaySendCount(0);
	}

	public AlarmStatistics getStatistics() {
		return statistics;
	}

	public void setStatistics(AlarmStatistics statistics) {
		this.statistics = statistics;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AlarmStatisticsMgr mgr = AlarmStatisticsMgr.getInstance();
		mgr.start();
	}
}
