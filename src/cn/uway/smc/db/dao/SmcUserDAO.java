package cn.uway.smc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.ews.dao.AbstractDAO;
import cn.uway.smc.db.conn.DBPool;
import cn.uway.smc.db.conn.DBUtil;
import cn.uway.smc.db.pojo.SMCUser;
import cn.uway.smc.util.MD5;

/**
 * 用户操作
 * 
 */
public class SmcUserDAO extends AbstractDAO<SMCUser> {

	private final static Logger LOG = LoggerFactory.getLogger(SmcUserDAO.class);

	public boolean put(SMCUser entity) throws Exception {
		String sql = " insert into   SMC_Cfg_User  (ID,NAME,PWD,DESCRIPTION) values (seq_smc_cfg_user.nextval,?,?,?) ";

		PreparedStatement ps = null;
		int result = 0;
		Connection conn = null;
		try {
			conn = DBPool.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, entity.getUserName());
			ps.setString(2, entity.getPassWord());
			ps.setString(3, entity.getDes());

			result = ps.executeUpdate();
		} catch (Exception e) {
			LOG.error("添加消息失败,sql:" + sql, e);
		} finally {
			DBUtil.close(null, ps, conn);
		}

		return result > 0;
	}

	public boolean delete(int key) throws Exception {
		String sql = "delete SMC_Cfg_User t where t.id=" + key;
		return DBUtil.executeUpdate(sql) > 0;
	}

	public SMCUser get(int key) throws Exception {
		String sql = " select * from  SMC_Cfg_User t where t.id=" + key;
		List<SMCUser> list = common(sql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@Override
	public List<SMCUser> list() throws Exception {
		String sql = "select * from  SMC_Cfg_User ";
		return common(sql);
	}

	public boolean queryByUser(SMCUser user) throws Exception {
		String pwd = user.getPassWord();
		if (pwd == null)
			pwd = "";
		String sql = "select * from  SMC_Cfg_User  t where t.name='"
				+ user.getUserName() + "' and t.pwd='"
				+ MD5.getMD5(pwd.getBytes()) + "'";
		List<SMCUser> list = common(sql);

		if (list != null && list.size() > 0) {
			SMCUser u = list.get(0);
			user.setId(u.getId());
			return true;
		}

		return false;
	}

	public List<SMCUser> common(String sql) throws Exception {
		List<SMCUser> list = new ArrayList<SMCUser>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			list = new ArrayList<SMCUser>();
			while (rs.next()) {
				SMCUser sdh = new SMCUser();
				sdh.setId(rs.getInt("ID"));
				sdh.setUserName(rs.getString("NAME"));
				sdh.setPassWord(rs.getString("pwd"));
				sdh.setDes(rs.getString("DESCRIPTION"));
				list.add(sdh);
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return list;
	}

	/**
	 * 检查用户名和密码是否正确
	 */
	public boolean checkAccount(SMCUser entity) throws Exception {
		return queryByUser(entity);
	}

	public static void main(String[] args) {
	}

}
