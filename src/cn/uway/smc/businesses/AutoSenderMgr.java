package cn.uway.smc.businesses;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.StringUtil;
import cn.uway.smc.db.pojo.SMCCfgSys;
import cn.uway.smc.db.pojo.SMCData;

/**
 * 管理系统自身告警，以及自动恢复用户查询帮助信息
 */
public class AutoSenderMgr {

	private static Logger log = LoggerFactory.getLogger(AutoSenderMgr.class);

	/**
	 * 从数据库中获得短信接收以及恢复配置
	 */
	public static List<SMCQueryReceive> QUERYRECEIVELIST = null;

	public SMCCfgSys sys = null;

	private AutoSenderMgr() {
		init();
	}

	private static class SMCSenderMgrContainer {

		private static AutoSenderMgr instance = new AutoSenderMgr();
	}

	public synchronized static AutoSenderMgr getInstance() {
		return SMCSenderMgrContainer.instance;
	}

	public void init() {
		QUERYRECEIVELIST = SystemMgr.getInstance().getQueryList();
		this.sys = SystemMgr.getInstance().getSys();
	}

	public List<String> regexFind(String reg, int group) {
		List<String> list = new ArrayList<String>();
		for (SMCQueryReceive e : QUERYRECEIVELIST) {
			String content = e.getSendSMCContent();
			if (StringUtil.isNull(content))
				continue;

			String result = BussinessMgr.findByRegex(content, reg, group);

			if (StringUtil.isNull(result))
				continue;
			list.add(result);
		}
		return list;
	}

	public SMCCfgSys getSys() {
		return sys;
	}

	public void setSys(SMCCfgSys sys) {
		this.sys = sys;
	}

	public static void main(String[] args) {
		/*
		 * AutoSenderMgr.getInstance().start(); SMCData express = new SMCData();
		 * express.setLevelid(1); express.setSrcid(1);
		 * express.setIsReceiveGw(1); express.setContent(null);
		 * express.setToUsers(ConstDef.PHONEFLAGBEGIN + "12345" +
		 * ConstDef.PHONEFLAGEND); express.setSendWay(1);
		 * express.setSubject("2101#2测试接收的内容+++++++++++++++");// 接收的内容 try {
		 * getInstance().put(express); getInstance().poll(); } catch
		 * (InterruptedException e) { e.printStackTrace(); }
		 */
	}

	public void put(SMCData express) {
		// TODO

	}

	public void start() {
		// TODO

	}

}
