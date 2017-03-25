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
import cn.uway.smc.db.pojo.SMCCfgToUserGroup;

/**
 * 用户组操作
 * 
 * @author liuwx 2010-11-17
 * @since 1.0
 */
public class SmcCfgToUserGroupDAO extends AbstractDAO<SMCCfgToUserGroup> {

	private final static Logger LOG = LoggerFactory
			.getLogger(SmcCfgToUserGroupDAO.class);

	@Override
	public List<SMCCfgToUserGroup> list() throws Exception {
		String sql = " select id, name, description from smc_cfg_tousergroup   ";
		return common(sql);
	}

	public List<SMCCfgToUserGroup> common(String sql) throws Exception {
		List<SMCCfgToUserGroup> list = new ArrayList<SMCCfgToUserGroup>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			list = new ArrayList<SMCCfgToUserGroup>();
			while (rs.next()) {
				SMCCfgToUserGroup group = new SMCCfgToUserGroup();
				group.setId(rs.getInt("ID"));
				group.setName(rs.getString("NAME"));
				group.setDescription(rs.getString("DESCRIPTION"));
				list.add(group);
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return list;
	}

	public boolean put(SMCCfgToUserGroup entity) throws Exception {

		String sql = "insert into smc_cfg_tousergroup (id, name, description) values (seq_smc_cfg_tousergroup.nextval,?,?)";
		PreparedStatement ps = null;
		int result = 0;
		Connection conn = null;
		try {
			conn = DBPool.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, entity.getName());
			ps.setString(2, entity.getDescription());
			result = ps.executeUpdate();
		} catch (Exception e) {
			LOG.error("添加消息失败,sql:" + sql, e);
		} finally {
			DBUtil.close(null, ps, conn);
		}
		return result > 0;

	}

	@Override
	public boolean update(SMCCfgToUserGroup entity) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" update smc_cfg_tousergroup t   ");
		sb.append(" set t.id = ").append(entity.getId());
		if (StringUtil.isNotNull(entity.getName())) {
			sb.append(" , t.name = '").append(entity.getName()).append("'");
		}
		if (StringUtil.isNotNull(entity.getDescription())) {
			sb.append(" , t.description = '").append(entity.getDescription())
					.append("'");
		}
		sb.append(" where t.id =").append(entity.getId());
		return DBUtil.executeUpdate(sb.toString()) > 0;
	}

	@Override
	public boolean delete(int key) throws Exception {
		String sql = "delete smc_cfg_tousergroup t where t.id=" + key;
		return DBUtil.executeUpdate(sql) > 0;
	}

	@Override
	public boolean delete(SMCCfgToUserGroup entity) throws Exception {
		return this.delete(entity.getId());
	}

	public SMCCfgToUserGroup get(int key) throws Exception {
		String sql = " select id, name, description from smc_cfg_tousergroup   t where t.id= "
				+ key;

		List<SMCCfgToUserGroup> list = common(sql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	public static void main(String[] args) {
		SmcCfgToUserGroupDAO dao = new SmcCfgToUserGroupDAO();
		try {
			dao.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
