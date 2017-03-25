package cn.uway.smc.db.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.StringUtil;

/**
 * 数据库操作公共类/静态方法
 * 
 * @author YangJian
 * @version 1.0
 * @since 1.0
 */
public final class DBUtil {

	private static Logger log = LoggerFactory.getLogger(DBUtil.class);

	/**
	 * 向数据库批量添加新数据
	 * 
	 * @param pagecount
	 * @return 返回批量提交受影响的行数
	 */
	public static int[] executeBatch(List<String> sqlList) throws SQLException {
		int[] result = null;
		Connection con = null;
		Statement stm = null;
		con = DBPool.getConnection();
		if (con == null) {
			log.error("批量提交获取数据库连接失败！");
			return result;
		}
		try {
			if (sqlList != null && !sqlList.isEmpty()) {
				con.setAutoCommit(false);
				stm = con.createStatement();
				for (String sql : sqlList) {
					stm.addBatch(sql);
				}
				result = stm.executeBatch();
				con.commit();
			}
		} finally {
			close(null, stm, con);
		}
		return result;
	}

	/**
	 * 关闭所有连接
	 */
	public static void close(ResultSet rs, Statement stm, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stm != null) {
				stm.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {

		}
	}

	/**
	 * @param sql
	 */
	public static int executeUpdate(String sql) throws SQLException {
		int count = 0;

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBPool.getConnection();
			ps = con.prepareStatement(sql);
			count = ps.executeUpdate();
			con.commit();
		} finally {
			close(null, ps, con);
		}

		return count;
	}

	public static Connection getConnection() {
		return DBPool.getConnection();
	}

	/**
	 * @param pwd
	 * @param user
	 * @param url
	 * @param driver
	 */
	public static Connection getConnection( String driver,String url, String user,String pwd
			) {
		Connection conn = null;

		try {
			Class.forName(driver);

			conn = DriverManager.getConnection(url, user, pwd);
		} catch (Exception ex) {
			log.error("获取连接失败,原因:", ex);
		}

		return conn;
	}

	/**
	 * 执行select数据
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static Result queryForResult(String sql) throws Exception {
		Result result = null;
		ResultSet resultSet = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = DBPool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			result = ResultSupport.toResult(resultSet);
		} finally {
			close(resultSet, preparedStatement, connection);
		}

		return result;

	}

	/**
	 * @param sql
	 */
	/**
	 * 执行select数据
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static ResultSet queryForResultSet(String sql) throws Exception {
		ResultSet resultSet = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		connection = DBPool.getConnection();
		preparedStatement = connection.prepareStatement(sql);
		resultSet = preparedStatement.executeQuery();

		return resultSet;

	}

	/**
	 * @param sql
	 */
	/**
	 * 执行select数据
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static int queryCount(String sql) throws SQLException {
		ResultSet resultSet = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {
			connection = DBPool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				count = resultSet.getInt(1);
			}
		} finally {
			close(resultSet, preparedStatement, connection);
		}
		return count;

	}

	/**
	 * @param sql
	 */
	/**
	 * 执行select数据
	 * 
	 * @param pagecount
	 * @return
	 * @throws Exception
	 */
	public static int getSeq(String seqName) throws Exception {
		String sql = "select " + seqName + ".nextval as SEQ from dual ";
		ResultSet resultSet = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = DBPool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			int intSeq = 0;
			while (resultSet.next()) {
				intSeq = resultSet.getInt("SEQ");
			}
			return intSeq;
		} finally {
			close(resultSet, preparedStatement, connection);
		}

	}

	/**
	 * 执行select数据
	 * 
	 * @param pagecount
	 * @return
	 * @throws Exception
	 */
	public static String getOracleSystemDate() throws Exception {
		String sql = "select sysdate as  ORACLEDATE from dual ";
		ResultSet resultSet = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = DBPool.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			String str = null;
			while (resultSet.next()) {
				str = resultSet.getString("ORACLEDATE");
			}
			int i = str.lastIndexOf(".");
			if (StringUtil.isNotNull(str)) {
				if (i != -1 && (i == (str.length() - 2))) {
					str = str.substring(0, i);
				}
			}
			return str;
		} finally {
			close(resultSet, preparedStatement, connection);
		}

	}

}