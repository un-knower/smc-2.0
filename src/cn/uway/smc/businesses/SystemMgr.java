package cn.uway.smc.businesses;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.smc.db.dao.SmcCfgSysDAO;
import cn.uway.smc.db.dao.SmcDataDAO;
import cn.uway.smc.db.dao.SmcQueryReceiveDAO;
import cn.uway.smc.db.pojo.SMCCfgSys;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.pack.Package;
import cn.uway.smc.sender.EmailSender;
import cn.uway.smc.sender.SMGPSender;
import cn.uway.smc.sender.SMSSender;
import cn.uway.smc.sender.Sender;
import cn.uway.smc.ui.province.AbstractProvince;
import cn.uway.smc.ui.province.ProvinceMgr;
import cn.uway.smc.util.SysCfg;

public class SystemMgr {

	private static Logger LOG = LoggerFactory.getLogger(SystemMgr.class);

	private SMCCfgSys sys = null;

	private static int protocolType = SysCfg.getInstance().getSmsProtocol();

	private List<SMCQueryReceive> queryList = null;

	private SystemMgr() {
		init();
	}

	private void init() {
		try {
			loadQueryHelp();
			sys = new SmcCfgSysDAO().list().get(0);
		} catch (Exception e) {
			LOG.error("加载系统配置失败.", e);
		}
	}

	private void loadQueryHelp() {
		LOG.debug("加载短息接收帮助信息");
		try {
			SmcQueryReceiveDAO dao = new SmcQueryReceiveDAO();
			queryList = dao.list();
		} catch (Exception e) {
			LOG.debug("加载短息接收帮助信息失败.", e);
		}

	}

	private static class SysMgrContain {

		private static SystemMgr instance = new SystemMgr();
	}

	public synchronized static SystemMgr getInstance() {
		return SysMgrContain.instance;
	}

	public SMCCfgSys getSys() {
		return sys;
	}

	public void setSys(SMCCfgSys sys) {
		this.sys = sys;
	}

	public List<SMCQueryReceive> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<SMCQueryReceive> queryList) {
		this.queryList = queryList;
	}

	public boolean process(SMCData smcData) {
		boolean b = false;
		boolean isused = SysCfg.getInstance().getIsUsed();
		try {
			String cityId = SysCfg.getInstance().getSmcCityEn();

			if (isused && smcData.getSendWay() == 1) {
				AbstractProvince provinceMgr = ProvinceMgr.getInstance()
						.createProvince(cityId);
				provinceMgr.hander(smcData);
				b = true;
			} else if (isused && smcData.getSendWay() != 1) {
				SmcDataDAO dataDao = new SmcDataDAO();
				dataDao.add(smcData);
				b = true;
			}

		} catch (Exception e1) {
			LOG.error("出现异常.  ", e1);
		}
		return b;
	}

	public boolean sender(SMCData smcData) {
		if (process(smcData))
			return true;
		Sender sender = null;
		boolean bflag = false;
		int senderType = smcData.getSendWay();
		try {
			List<Package> plist = null;
			switch (senderType) {
				case 1 :
					if (protocolType == 1) {
						sender = new SMSSender(sys);
						plist = sender.builderPackage(smcData, sys);
						if (plist != null && plist.size() > 0)
							bflag = sender.sendAll();
						else {
							LOG.warn(smcData + ", 发送用户to_user 无短信接收人");
							bflag = true;
						}

					} else {
						sender = new SMGPSender();
						plist = sender.builderPackage(smcData, sys);
						bflag = sender.sendAll();

					}

					break;
				case 2 :
					sender = new EmailSender();
					plist = sender.builderPackage(smcData, sys);
					if (plist != null && plist.size() > 0)
						bflag = sender.sendAll();
					else {
						LOG.warn(smcData + ", 发送用户to_user 无邮件接收人");
						bflag = true;
					}
					break;
				case 3 :

					try {
						if (protocolType == 1) {
							sender = new SMSSender(sys);
							plist = sender.builderPackage(smcData, sys);
							if (plist != null && plist.size() > 0)
								bflag = sender.sendAll();
							else {
								LOG.warn(smcData + ", 发送用户to_user 无短信接收人");
								bflag = true;
							}

						} else {
							sender = new SMGPSender();
							plist = sender.builderPackage(smcData, sys);
							bflag = sender.sendAll();

						}

					} catch (Exception e) {
						LOG.error(smcData + ",发送短信失败 sendtype=3，原因:{}", e);
					}
					sender = new EmailSender();
					plist = sender.builderPackage(smcData, sys);
					if (plist != null && plist.size() > 0) {
						bflag = sender.sendAll();
					} else {
						LOG.warn(smcData + ", 发送用户to_user 无邮件接收人");
						bflag = true;
					}
					break;
				default :
					break;
			}
		}  finally {
			TaskMgr.getInstance().delActiveTask(smcData.getId());
		}
		return bflag;
	}
}
