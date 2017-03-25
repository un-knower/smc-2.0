package cn.uway.smc.businesses;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.DateUtil;
import cn.uway.commons.type.StringUtil;
import cn.uway.smc.db.dao.SmcCfgSysDAO;
import cn.uway.smc.db.dao.SmcDataDAO;
import cn.uway.smc.db.pojo.SMCCfgStrategy;
import cn.uway.smc.db.pojo.SMCCfgSys;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.util.ConstDef;
import cn.uway.smc.util.SysCfg;
import cn.uway.smc.util.TimeIntervalMgr;

/**
 * 任务管理器
 */
public class TaskMgr {

	private static Logger LOG = LoggerFactory.getLogger(TaskMgr.class);

	private Map<Long, SMCData> activeTask; // 保存所有正在运行的“正常任务”任务信息

	private Map<Long, SMCData> activeTasksForRegather; // 保存所有正在运行的“补采任务”的信息

	private SMCCfgSys sys;

	private TaskMgr() {
		super();
		activeTask = Collections.synchronizedMap(new HashMap<Long, SMCData>());
		activeTasksForRegather = Collections
				.synchronizedMap(new HashMap<Long, SMCData>());
		init();
	}

	public void start() {
		Timer timer = new Timer();
		ScheduleRunTask task = new ScheduleRunTask();
		long rPerroid = SysCfg.getInstance().getScanPriod();
		Date now = new Date();
		timer.schedule(task, now, rPerroid);

	}

	private class ScheduleRunTask extends TimerTask {

		@Override
		public void run() {
			try {
				loadNormalTasksFromDB(new Date());
			} catch (Exception e) {
				LOG.error("加载任务时异常.", e);
			}
		}
	}

	public SMCCfgSys getSys() {
		return sys;
	}

	public void setSys(SMCCfgSys sys) {
		this.sys = sys;
	}

	private void init() {
		try {
			sys = new SmcCfgSysDAO().getEffectSys();
		} catch (Exception e) {
			LOG.error("加载系统配置出现错误,原因={}", e);
		}

	}

	private static class TaskMgrContainer {

		private static TaskMgr instance = new TaskMgr();
	}

	public static TaskMgr getInstance() {
		return TaskMgrContainer.instance;
	}

	/** 向采集队列添加采集任务 */
	@SuppressWarnings("static-access")
	public boolean addTask(SMCData obj) {
		if (obj == null)
			return false;

		try {
			// 如果任务为非活动状态并且此时系统没有达到预配置的最大线程数则添加到任务表中
			long keyID = obj.getId();
			if (!isActive(keyID)) {
				if (!isMaxThreadCount()) {
					LOG.debug(" 添加{}到任务队列中", keyID);
					activeTask.put(keyID, obj);

					long sleepTime=0;
					if (obj.getSendWay() == 2) {
						if (SysCfg.getInstance().isEmailForbid()) {

							BussinessMgr.sendAfter(TaskMgr.getInstance()
									.getSys(), obj,
									ConstDef.EMAIL_FORBID_ERROR,
									"现场不支持外网，邮件已经禁用。 ");
							
							delActiveTask(obj.getId());
							
							return false;
						}
						 sleepTime = SysCfg.getInstance().getEmailSendersleep();
					}else {
						 sleepTime = SysCfg.getInstance().getSendersleep();
						 
						List<String>  invalidMobiles= SysCfg.getInstance().getInvalidMobileNumbers ();
						if(invalidMobiles!=null){
							for(String  invalidMobile: invalidMobiles)
							if(obj.getToUsers().contains(invalidMobile)){
								BussinessMgr.sendAfter(TaskMgr.getInstance()
										.getSys(), obj,
										ConstDef.INVALID_MOBILE_NUMBER,
										"无效的手机号码。 ");
								
								delActiveTask(obj.getId());
								return false;
							}
						}
					}
					try {
						Thread.currentThread().sleep(sleepTime);
					} catch (InterruptedException e) {
					}

					SMCDataHandler handler = new SMCDataHandler(obj);
					handler.hander();
					return true;
				}

			}
		} catch (Exception e) {
			LOG.error("出现异常,原因={}", e);
		}

		return false;
	}

	/**
	 * 判断当前任务是否是活动的
	 */
	public boolean isActive(long taskID) {
		boolean bExist = activeTask.containsKey(taskID);
		return bExist;
	}

	/**
	 * 删除指定任务
	 * 
	 * @param taskID
	 */
	public void delActiveTask(long taskID) {
		if (activeTask.containsKey(taskID))
			activeTask.remove(taskID);
	}

	/**
	 * 是否达到最大线程数量
	 */
	public boolean isMaxThreadCount() {

		int size = activeTask.size();
		SysCfg sc = SysCfg.getInstance();
		int maxThreadCount = sc.getMaxThreadCount();

		if ((size < maxThreadCount) || (maxThreadCount <= 0))
			return false;
		else {
			LOG.warn("已达到本机最大运行线程数(" + maxThreadCount + ")");
			return true;
		}
	}

	/**
	 * 获取任务表映射关系
	 */
	public Map<Long, SMCData> getTasksMap() {
		return this.activeTask;
	}

	public Map<Long, SMCData> getActiveTasksForRegather() {
		return activeTasksForRegather;
	}

	public SMCData getTask(long taskID) {
		return activeTask.get(taskID);
	}

	public int size() {
		return activeTask.size() + activeTasksForRegather.size();
	}

	public boolean loadNormalTasksFromDB(Date scanDate) {
		boolean bReturn = true;
		try {
			LOG.debug("开始加载任务信息...");

			SmcDataDAO dao = new SmcDataDAO();
			// 处理发送

			List<SMCData> list = dao.list();

			Date now = TimeIntervalMgr.getInstance().getDateValue(new Date());

			for (SMCData data : list) {
				boolean b = isActive(data.getId());
				if (b)
					continue;

				if (checkDataTime(data, now)) {
					addTask(data);
				}

			}
		} catch (Exception e) {
			bReturn = false;
			LOG.error("", e);

		}

		return bReturn;
	}

	/** 查看此消息是否过期 */
	public boolean isExpired(SMCData smcExpressData, Date now) {
		SMCCfgStrategy strategy = smcExpressData.getStrategy();
		boolean flag = false;
		// 默认的消息生存时间为120分钟
		if (strategy.getTtl() == 0) {
			strategy.setTtl(120);
		}
		try {
			if (StringUtil.isNull(smcExpressData.getSendTime()))
				return false;
			long expiredDate = DateUtil.getDate(smcExpressData.getSendTime())
					.getTime() + strategy.getTtl() * 60 * 1000;
			flag = now.getTime() > expiredDate;
		} catch (ParseException e) {
			LOG.error("判断消息过期出现异常，原因：{}", e);
		}
		return flag;
	}

	private boolean checkDataTime(SMCData smcdata, Date now) {
		boolean b = isExpired(smcdata, now);
		if (b) {
			LOG.info(smcdata.getId() + " 此消息已经过期  ");
			// 消息过期之后，需要添加到历史表中，并从历史Map中移除，消息过期时间默认是120分钟
			BussinessMgr.sendAfter(sys, smcdata, ConstDef.SENDEREXPIRED,
					"消息已经过期");
			return false;
		}
		boolean bf = BussinessMgr.readyToSend(now, smcdata);
		if (!bf)
			return false;
		// 检查是否达到最大发送次数
		if (!checkIsSent(smcdata)) {
			BussinessMgr.sendAfter(sys, smcdata, ConstDef.SENDMAXTIMES,
					"达到发送最大数,不会发送短信或邮件");
			LOG.debug(smcdata.getId() + " 达到发送最大数,不会发送短信或邮件,移除到历史表中.");
			return false;
		}
		return true;
	}

	public boolean checkIsSent(SMCData smcdata) {
		int i = smcdata.getSentOkTimes();// 1
		int strategyTimes = smcdata.getStrategy().getSendTimes();
		// strategyTimes=2;
		if (i + 1 > strategyTimes && strategyTimes > 0) // 1
		{
			LOG.info(smcdata + " , 达到发送次数, 当前发送次数: " + i + ", 策略设置次数: "
					+ strategyTimes);
			return false;
		}
		try {
			if (!StringUtil.isNull(smcdata.getSendTimeExclude())) {
				String t = smcdata.getSendTimeExclude();
				String excs[] = t.split(";");

				Date begin = DateUtil.getDate(excs[0]);
				Date end = DateUtil.getDate(excs[1]);
				if (begin.getTime() > end.getTime()) {
					Date d = new Date();
					d = begin;
					begin = end;
					end = d;
				}

				Date d = new Date();
				if (d.getTime() >= begin.getTime()
						&& d.getTime() <= end.getTime())
					return true;

			}
		} catch (ParseException e) {
			LOG.error("检验是否发送出现异常.原因:{}", e);
		}
		return true;
	}

	public synchronized SMCData getObjByID(long id) {
		SMCData obj = activeTask.get(id);
		if (obj == null)
			obj = activeTasksForRegather.get(id);
		return obj;
	}

}
