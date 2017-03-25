package cn.uway.smc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.StringUtil;
import cn.uway.ews.dao.AbstractDAO;
import cn.uway.smc.db.conn.DBPool;
import cn.uway.smc.db.conn.DBUtil;
import cn.uway.smc.db.pojo.SMCCfgToUser;
import cn.uway.smc.db.pojo.SMCCfgToUserGroup;

/**
 * 用户管理操作
 * 
 * @since 1.0
 */
public class SmcCfgToUserDAO extends AbstractDAO<SMCCfgToUser> {

	private final static Logger LOG = LoggerFactory
			.getLogger(SmcCfgToUserDAO.class);

	private String sql = " select id, name, group_id, cellphone, email, description from smc_cfg_touser t ";

	public List<SMCCfgToUser> list() throws Exception {
		return common(sql);
	}

	public List<SMCCfgToUser> common(String sql) throws Exception {
		List<SMCCfgToUser> list = new ArrayList<SMCCfgToUser>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			list = new ArrayList<SMCCfgToUser>();
			while (rs.next()) {
				SMCCfgToUser sdh = new SMCCfgToUser();
				sdh.setId(rs.getInt("ID"));
				sdh.setName(rs.getString("NAME"));
				sdh.setGroupId(rs.getInt("GROUP_ID"));
				sdh.setCellphone(rs.getString("CELLPHONE"));
				sdh.setEmail(rs.getString("EMAIL"));
				sdh.setDescription(rs.getString("DESCRIPTION"));
				list.add(sdh);
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return list;
	}

	public List<SMCCfgToUser> listUsersByGroupId(int groupid) throws Exception {
		List<SMCCfgToUser> list = new ArrayList<SMCCfgToUser>();
		String sql = " select t.id as u_id, t.name as u_name , t.group_id as u_group_id , t.cellphone as u_cellphone , t.email u_email, t.description u_description, m.name m_name, m.DESCRIPTION m_DESCRIPTION from smc_cfg_touser t ,SMC_Cfg_ToUserGroup   m  where t.group_id=m.id and m.id ="
				+ groupid;
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			list = new ArrayList<SMCCfgToUser>();
			while (rs.next()) {
				SMCCfgToUser sdh = new SMCCfgToUser();
				sdh.setId(rs.getInt("U_ID"));
				sdh.setName(rs.getString("U_NAME"));
				sdh.setGroupId(rs.getInt("U_GROUPID"));
				sdh.setCellphone(rs.getString("U_CELLPHONE"));
				sdh.setEmail(rs.getString("U_EMAIL"));
				sdh.setDescription(rs.getString("U_DESCRIPTION"));

				SMCCfgToUserGroup group = new SMCCfgToUserGroup();
				group.setId(groupid);
				group.setName(rs.getString("M_NAME"));
				group.setDescription(rs.getString("M_DESCRIPTION"));
				sdh.setGroup(group);
				list.add(sdh);
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return list;
	}

	public boolean put(SMCCfgToUser entity) throws Exception {
		String sql = "insert into smc_cfg_touser (id, name, group_id, cellphone, email, description) values (seq_smc_cfg_touser.nextval,?,?,?,?,?)";

		PreparedStatement ps = null;
		int result = 0;
		Connection conn = null;
		try {
			conn = DBPool.getConnection();
			ps = conn.prepareStatement(sql);

			ps.setString(
					1,
					StringUtil.isNull(entity.getName()) ? null : entity
							.getName());
			ps.setInt(2, entity.getGroupId());
			ps.setString(3, StringUtil.isNull(entity.getCellphone())
					? null
					: entity.getCellphone());
			ps.setString(4, StringUtil.isNull(entity.getEmail())
					? null
					: entity.getEmail());
			ps.setString(5, StringUtil.isNull(entity.getDescription())
					? null
					: entity.getDescription());

			result = ps.executeUpdate();
		} catch (Exception e) {
			LOG.error("添加用户失败,sql:" + sql, e);
		} finally {
			DBUtil.close(null, ps, conn);
		}
		return result > 0;

	}

	public boolean delete(int key) throws Exception {
		String sql = " delete  smc_cfg_touser  t where t.id =" + key;
		return DBUtil.executeUpdate(sql) > 0;

	}

	public SMCCfgToUser get(int key) throws Exception {
		String sql = " select * from smc_cfg_touser t wher t.id=" + key;
		List<SMCCfgToUser> list = common(sql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	public static void main(String[] args) {
		SmcCfgToUserDAO dao = new SmcCfgToUserDAO();
		try {
			dao.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
