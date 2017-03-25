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
import cn.uway.smc.db.pojo.SMCCfgSource;
import cn.uway.smc.web.servlet.SmcUserServlet;

/**
 * 信息源操作DAO
 * 
 * @since 1.0
 */
public class SmcCfgSourceDAO extends AbstractDAO<SMCCfgSource> {

	private final static Logger LOG = LoggerFactory
			.getLogger(SmcUserServlet.class);

	@Override
	public List<SMCCfgSource> list() throws Exception {
		String sql = "select * from smc_cfg_source ";
		return common(sql);
	}

	public List<SMCCfgSource> common(String sql) throws Exception {
		List<SMCCfgSource> list = new ArrayList<SMCCfgSource>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			list = new ArrayList<SMCCfgSource>();
			while (rs.next()) {
				SMCCfgSource e = new SMCCfgSource();
				e.setSrcid(rs.getInt("ID"));
				e.setName(rs.getString("NAME"));
				e.setDescription(rs.getString("DESCRIPTION"));

				e.setUser(rs.getString("USERNAME"));
				e.setPwd(rs.getString("PASSWORD"));

				list.add(e);
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return list;
	}

	public List<String> listId() throws Exception {
		String sql = "select * from smc_cfg_source l ";
		List<SMCCfgSource> list = common(sql);
		List<String> result = new ArrayList<String>();
		for (SMCCfgSource s : list)
			result.add(String.valueOf(s.getSrcid()));

		return result;
	}

	public boolean update(SMCCfgSource source) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" update smc_cfg_source t ");

		sql.append(" set t.name = '").append(source.getName()).append("'");

		sql.append(" , t.username = '").append(source.getUser()).append("'");

		sql.append(" , t.password = '").append(source.getPwd()).append("'");

		sql.append(" , t.ip_list = '").append(source.getIpList()).append("'");

		sql.append(" , t.description = '").append(source.getDescription())
				.append("'");

		sql.append("  where t.id=  ");
		sql.append(source.getSrcid());

		return DBUtil.executeUpdate(sql.toString()) > 0;
	}

	public boolean put(SMCCfgSource entity) throws Exception {
		String sql = "insert into smc_cfg_source (ID ,name ,username , password,IP_LIST,DESCRIPTION ) values (?,?,?,?,?,?)";
		PreparedStatement ps = null;
		int result = 0;
		Connection conn = null;
		try {
			conn = DBPool.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, entity.getSrcid());
			ps.setString(2, entity.getName());
			ps.setString(3, entity.getUser());
			ps.setString(4, entity.getPwd());
			ps.setString(5, entity.getIpList());
			ps.setString(6, entity.getDescription());
			result = ps.executeUpdate();
		} catch (Exception e) {
			LOG.error("添加消息失败,sql:" + sql, e);
		} finally {
			DBUtil.close(null, ps, conn);
		}
		return result > 0;
	}

	public boolean delete(int id) throws Exception {

		String sql = "delete from smc_cfg_source t where t.id=" + id;
		return DBUtil.executeUpdate(sql) > 0;

	}

	public SMCCfgSource get(int key) throws Exception {
		String sql = "select * from smc_cfg_source l where l.id=" + key;
		List<SMCCfgSource> list = common(sql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	public static void main(String[] args) {
		SmcCfgSourceDAO dao = new SmcCfgSourceDAO();

		try {
			for (int i = 0; i < 10; i++) {

				SMCCfgSource entity = new SMCCfgSource(i++, "1", "1", "1", "1",
						"1");
				dao.put(entity);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			// log.debug(dao.list());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
