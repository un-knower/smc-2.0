package cn.uway.smc.db.conn;

import java.sql.Connection;

import cn.uway.smc.util.SysCfg;

/**
 * 数据库连接池
 * 
 * @author YangJian
 * @version 1.0
 * @since 1.0
 */
public class DBPool {

	private static DBCP dbcp = null;

	private static C3P0 c3p0 = null;

	private static int type = SysCfg.getInstance().getDbPoolType();

	private static boolean b = true;

	public DBPool() {
		super();
	}

	public static void openDbPool() {
		if (dbcp == null && type == 1) {
			dbcp = DBCP.getInstance();
		} else if (c3p0 == null && type == 2) {
			c3p0 = C3P0.getInstance();
			b = false;
		}
	}

	public static Connection getConnection() {
		openDbPool();
		if (b) {
			return dbcp.getConn();
		} else {
			return c3p0.getConnection();
		}

	}

	protected void init() {
		openDbPool();
	}

	public static void shutdown() {
		if (b) {
			if (dbcp != null)
				dbcp.destroy();
		} else {
			c3p0.destroy();
		}
	}

}