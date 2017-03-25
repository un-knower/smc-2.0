package cn.uway.smc.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.smc.businesses.AlarmStatistics;
import cn.uway.smc.businesses.AlarmStatisticsMgr;
import cn.uway.smc.businesses.BussinessMgr;
import cn.uway.smc.db.dao.SmcDataDAO;
import cn.uway.smc.db.pojo.SMCCfgStrategy;
import cn.uway.smc.db.pojo.SMCCfgSys;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.pack.Package;

public abstract class AbstractSender implements Sender {

	private static Logger LOG = LoggerFactory.getLogger(AbstractSender.class);

	protected SmcDataDAO dao = new SmcDataDAO();

	protected SMCCfgSys sys;

	protected SMCData smcData;

	public SMCCfgSys getSys() {
		return sys;
	}

	public void setSys(SMCCfgSys sys) {
		this.sys = sys;
	}

	public SMCData getSmcData() {
		return smcData;
	}

	public void setSmcData(SMCData smcData) {
		this.smcData = smcData;
	}

	public boolean updateExpressSentTimes(SMCData smcData) {
		try {
			smcData.setSentOkTimes(smcData.getSentOkTimes() + 1);
			return dao.update(smcData);
		} catch (Exception e) {
			LOG.debug(smcData + " 更新发送次数失败.");
		}
		return false;
	}

	public boolean reSendSent(Package pack) {
		boolean b = false;
		SMCCfgStrategy strategy = smcData.getStrategy();
		if (strategy == null) {
			return false;
		}
		int resend = strategy.getResendWhenFail();
		if (resend == 0)
			return false;

		for (int i = 0; i < smcData.getStrategy().getSendTimes(); i++) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}

			int resultTmp = send(pack);
			if (resultTmp == 0) {
				b = true;
				LOG.debug("{}第{}次重试成功.", smcData.getId(), i + 1);
				break;
			} else {
				LOG.debug("{}第{}次重试失败.", smcData.getId(), i + 1);
			}

		}
		return b;

	}

	public void sendAfter(int result, String cause) {
		BussinessMgr.sendAfter(sys, smcData, result, cause);
	}

	public void statistics() {
		AlarmStatisticsMgr mgr = AlarmStatisticsMgr.getInstance();
		synchronized (mgr) {
			AlarmStatistics sta = mgr.getStatistics();
			int hourcount = sta.getHourSendCount();
			sta.setHourSendCount(hourcount + 1);
			mgr.setStatistics(sta);
		}
	}

}
