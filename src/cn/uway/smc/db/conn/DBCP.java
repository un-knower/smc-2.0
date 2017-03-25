package cn.uway.smc.db.conn;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.smc.util.SysCfg;

import com.mchange.v2.c3p0.DataSources;

/**
 * DBCP数据库连接池
 * 
 * @author liuwx
 * @version 1.0
 * @since 1.0
 */
public class DBCP {

	private static Logger LOG = LoggerFactory.getLogger(DBCP.class);

	private static DBCP instance = null;

	private DBCP() {
		super();
	}

	public static DBCP getInstance() {
		if (instance == null) {
			instance = new DBCP();
		}
		return instance;
	}

	static DataSource dataSource = null;

	public void destroy() {
		try {
			// 关闭数据连接池
			if (dataSource != null)
				DataSources.destroy(dataSource);
		} catch (SQLException e) {
			LOG.error("关闭数据连接池失败,原因:{}", e);
		}
	}

	/**
	 * 关闭现有的数据库连接池
	 * 
	 * @param dataSource
	 *            要关闭的数据库连接池
	 */
	public void close() {
		try {
			Class<?> classz = dataSource.getClass();
			Class<?>[] types = new Class[0];
			Method method = classz.getDeclaredMethod("close", types);
			if (method != null) {
				method.setAccessible(true);
				Object[] args = new Object[0];
				method.invoke(dataSource, args);
			}
		} catch (Exception e) {
			LOG.error("DbPool: 尝试关闭原有的数据库连接池 ["
					+ dataSource.getClass().getName() + "]时失败.", e);
		}
	}

	public Connection getConn() {
		Connection conn = null;
		try {
			if (dataSource == null) {
				createDataSource();
			}
			conn = dataSource.getConnection();
			// BasicDataSource bds = ((BasicDataSource) dataSource);
			// log.debug("数据库连接池信息：当前活动连接数=" + bds.getNumActive() + ",
			// 设定的最大活动连接数="
			// + bds.getMaxActive() + ", 当前获取连接的线程="
			// + Thread.currentThread());
		} catch (Exception e) {
			LOG.error("DbPool: error when got a connection from DB pool.", e);
		}

		return conn;
	}

	/**
	 * 创建一个新的数据库连接池
	 * 
	 * @param name
	 *            JNDI的搜索名
	 * @param record
	 *            数据库连接池的基本信息
	 * @param constants
	 *            系统内置的环境管理类
	 * @return 新的数据库连接池，null表示创建失败
	 */
	private static DataSource createDataSource() {
		String name = "";
		try {
			Properties p = new Properties();
			SysCfg cfg = SysCfg.getInstance();
			p.put("name", cfg.getPoolName());
			p.put("type", cfg.getPoolType());
			p.put("driverClassName", cfg.getDbDriver());
			p.put("url", cfg.getDbUrl());
			p.put("maxActive", String.valueOf(cfg.getPoolMaxActive()));
			p.put("username", cfg.getDbUserName());
			p.put("password", cfg.getDbPassword());
			p.put("maxIdle", String.valueOf(cfg.getPoolMaxIdle()));
			p.put("maxWait", String.valueOf(cfg.getPoolMaxWait()));
			p.put("validationQuery", cfg.getDbValidationQueryString());

			name = cfg.getPoolName();
			dataSource = BasicDataSourceFactory.createDataSource(p);
			LOG.debug("DbPool: 创建数据库连接池：" + name);
		} catch (Exception e) {
			LOG.error("DbPool: 创建数据源 " + name + " 失败：", e);
		}

		return dataSource;
	}

	public static void main(String[] args) {
		try {
			// 获取数据连接
			Connection conn = new DBCP().getConn();
			Statement stmt = null;
			ResultSet rs = null;
			if (conn != null) {
				LOG.debug("OK");
				// 关闭连接,当前连接被连接池收回
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT * FROM clt_conf_task");
				while (rs.next()) {
					LOG.debug(rs.getString(1));
				}
				rs.close();
				stmt.close();
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}