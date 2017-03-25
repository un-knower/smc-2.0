package cn.uway.smc.db.conn;

import java.beans.PropertyVetoException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.smc.util.SysCfg;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * C3P0数据库连接池
 * 
 * @author liuwx
 * @version 1.0
 * @since 1.0
 */
public class C3P0 {

	private static Logger LOG = LoggerFactory.getLogger(C3P0.class);

	private static C3P0 c3p0;

	private static ComboPooledDataSource cpds = null;

	private C3P0() {
		super();
	}

	public static C3P0 getInstance() {

		if (c3p0 == null) {
			c3p0 = new C3P0();
			if (cpds == null) {
				cpds = new ComboPooledDataSource();
				setPropertes();
			}
		}
		return c3p0;
	}

	public Connection getConnection() {
		Connection conn = null;
		try {
			// 获取数据连接
			conn = cpds.getConnection();
		} catch (SQLException e) {
			LOG.error("获取数据库连接失败,原因:{}", e);
		}
		return conn;
	}

	public void destroy() {
		try {
			// 关闭数据连接池
			if (cpds != null)
				DataSources.destroy(cpds);
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
			Class<?> classz = cpds.getClass();
			Class<?>[] types = new Class[0];
			Method method = classz.getDeclaredMethod("close", types);
			if (method != null) {
				method.setAccessible(true);
				Object[] args = new Object[0];
				method.invoke(cpds, args);
			}
		} catch (Exception e) {
			LOG.error("DbPool: 尝试关闭原有的数据库连接池 [" + cpds.getClass().getName()
					+ "]时失败.", e);
		}
	}

	private static void setPropertes() {

		SysCfg cfg = SysCfg.getInstance();
		try {
			// 设置驱动
			cpds.setDriverClass(cfg.getDbDriver());

			// 设置数据库URL
			cpds.setJdbcUrl(cfg.getDbUrl());
			// 设置用户名
			cpds.setUser(cfg.getDbUserName());
			// 设置密码
			cpds.setPassword(cfg.getDbPassword());
			// 当连接池中的连接用完时，C3PO一次性创建新的连接数目;
			cpds.setAcquireIncrement(3);
			// 定义在从数据库获取新的连接失败后重复尝试获取的次数，默认为30;
			cpds.setAcquireRetryAttempts(30);
			// 两次连接中间隔时间默认为1000毫秒
			cpds.setAcquireRetryDelay(1000);
			// 连接关闭时默认将所有未提交的操作回滚 默认为false;
			cpds.setAutoCommitOnClose(false);
			// 获取连接失败将会引起所有等待获取连接的线程异常,但是数据源仍有效的保留,并在下次调用getConnection()的时候继续尝试获取连接.如果设为true,那么尝试获取连接失败后该数据源将申明已经断开并永久关闭.默认为false
			cpds.setBreakAfterAcquireFailure(false);
			// 当连接池用完时客户端调用getConnection()后等待获取新连接的时间,超时后将抛出SQLException,如设为0则无限期等待.单位毫秒,默认为0
			cpds.setCheckoutTimeout(0);
			// 隔多少秒检查所有连接池中的空闲连接,默认为0表示不检查;
			cpds.setIdleConnectionTestPeriod(0);
			// 初始化时创建的连接数,应在minPoolSize与maxPoolSize之间取值.默认为3
			cpds.setInitialPoolSize(10);
			// 最大空闲时间,超过空闲时间的连接将被丢弃.为0或负数据则永不丢弃.默认为0;
			cpds.setMaxIdleTime(0);
			// 连接池中保留的最大连接数据.默认为15
			cpds.setMaxPoolSize(20);
			// JDBC的标准参数,用以控制数据源内加载的PreparedStatement数据.但由于预缓存的Statement属于单个Connection而不是整个连接池.所以设置这个参数需要考滤到多方面的因素,如果maxStatements
			// 与maxStatementsPerConnection均为0,则缓存被关闭.默认为0;
			cpds.setMaxStatements(0);
			// 连接池内单个连接所拥有的最大缓存被关闭.默认为0;
			cpds.setMaxStatementsPerConnection(0);
			// C3P0是异步操作的,缓慢的JDBC操作通过帮助进程完成.扩展这些操作可以有效的提升性能,通过多数程实现多个操作同时被执行.默为为3
			cpds.setNumHelperThreads(3);
			// 用户修改系统配置参数执行前最多等待的秒数.默认为300;
			cpds.setPropertyCycle(300);
		} catch (PropertyVetoException e) {
			LOG.error("数据库配置设置失败,原因:{}", e);
		}
	}

	public static void close(Connection o) {
		try {
			if (o != null)
				o.close();
		} catch (Exception e) {
			LOG.error("数据库连接关闭失败,原因:{}", e);
		}
	}

	public static void main(String[] args) {
		C3P0 c = C3P0.getInstance();
		Connection conn = c.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		if (conn != null) {
			LOG.debug("OK");
			// 关闭连接,当前连接被连接池收回
			try {
				stmt = conn.createStatement();

				rs = stmt.executeQuery("SELECT * FROM clt_conf_task");
				while (rs.next()) {
					LOG.debug(rs.getString(1));
				}
				rs.close();
				stmt.close();
				conn.close();
				c.destroy();
				LOG.debug("aaaa " + conn);

				// conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}