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
import cn.uway.smc.db.pojo.SMCCfgLevel;

/**
 * 级别配置操作
 * 
 * @since 1.0
 */
public class SmcCfgLevelDAO extends AbstractDAO<SMCCfgLevel> {

	private final static Logger LOG = LoggerFactory
			.getLogger(SmcCfgLevelDAO.class);

	@Override
	public List<SMCCfgLevel> list() throws Exception {
		String sql = "select * from SMC_Cfg_Level le ";
		return common(sql);
	}

	public List<SMCCfgLevel> common(String sql) throws Exception {
		List<SMCCfgLevel> list = new ArrayList<SMCCfgLevel>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			list = new ArrayList<SMCCfgLevel>();
			while (rs.next()) {
				SMCCfgLevel sdh = new SMCCfgLevel();
				sdh.setId(rs.getInt("ID"));
				// sdh.setLevel(rs.getInt("LEVEL"));
				sdh.setLevelid(rs.getInt("LEVELID"));
				sdh.setName(rs.getString("NAME"));
				sdh.setDescription(rs.getString("DESCRIPTION"));
				list.add(sdh);
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return list;
	}

	public boolean put(SMCCfgLevel entity) throws Exception {
		String sql = "insert into smc_cfg_level (id ,levelid,name,DESCRIPTION) values (?,?,?,?)";

		PreparedStatement ps = null;
		int result = 0;
		Connection conn = null;
		try {
			conn = DBPool.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, entity.getId());
			ps.setInt(2, entity.getLevelid());
			ps.setString(3,
					StringUtil.isNull(entity.getName()) ? "" : entity.getName());
			ps.setString(4, StringUtil.isNull(entity.getDescription())
					? ""
					: entity.getDescription());

			result = ps.executeUpdate();
		} catch (Exception e) {
			LOG.error("添加消息级别失败,sql:" + sql, e);
		} finally {
			DBUtil.close(null, ps, conn);
		}
		return result > 0;

	}

	public boolean delete(int id) throws Exception {
		String sql = "delete from smc_cfg_level t where t.id=" + id;
		return DBUtil.executeUpdate(sql) > 0;

	}

	public SMCCfgLevel get(int key) throws Exception {
		String sql = "select * from SMC_Cfg_Level l where l.id=" + key;
		List<SMCCfgLevel> list = common(sql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@Override
	public List<SMCCfgLevel> query(String sql) throws Exception {

		return list();
	}

	public static void main(String[] args) {
		SmcCfgLevelDAO level = new SmcCfgLevelDAO();
		try {

			for (int i = 0; i < 10; i++) {
				SMCCfgLevel l = new SMCCfgLevel();
				l.setId(i);
				l.setLevelid(i);
				l.setName("级别" + i);
				level.put(l);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
