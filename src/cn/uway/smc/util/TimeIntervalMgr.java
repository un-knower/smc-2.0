package cn.uway.smc.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.DateUtil;
import cn.uway.smc.db.conn.DBUtil;

/**
 * 数据库时间和系统时间时间间隔管理
 * 
 * @author liuwx
 */
public class TimeIntervalMgr {

	private static Logger LOG = LoggerFactory.getLogger(TimeIntervalMgr.class);

	public static boolean FLAG = true;

	private static TimeIntervalMgr instance = new TimeIntervalMgr();

	private TimeIntervalMgr() {
	}

	public static TimeIntervalMgr getInstance() {
		return instance;
	}

	public static long VALUE = 0;
	{
		init();
	}

	public static void init() {
		String str = null;
		try {
			str = DBUtil.getOracleSystemDate();
			Date d = DateUtil.getDate(str);
			long oracletime = d.getTime(); // 12

			Date curDate = new Date(); // 13
			long curtime = curDate.getTime();

			if (oracletime >= curtime) {
				VALUE = oracletime - curtime;
			}

			else {
				VALUE = curtime - oracletime;
				FLAG = false;
			}

		} catch (Exception e) {
			LOG.error("获得数据库时间失败,原因:{}", e);
		}
	}

	/**
	 * 数据库时间和当前程序服务器时间差
	 * 
	 * @param input
	 *            system date
	 * @return
	 */
	public Date getDateValue(Date input) {
		if (FLAG) {
			return new Date(input.getTime() + VALUE);
		} else {
			return new Date(input.getTime() - VALUE);
		}

	}

}
