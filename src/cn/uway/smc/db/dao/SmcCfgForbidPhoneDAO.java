package cn.uway.smc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.StringUtil;
import cn.uway.ews.dao.AbstractDAO;
import cn.uway.smc.businesses.ForbidPhone;
import cn.uway.smc.db.conn.DBPool;
import cn.uway.smc.db.conn.DBUtil;

/**
 * 禁止短信操作
 * 
 * @since 1.0
 */
public class SmcCfgForbidPhoneDAO extends AbstractDAO<ForbidPhone> {

	private static Logger LOG = LoggerFactory
			.getLogger(SmcCfgForbidPhoneDAO.class);

	private String sql = " select * from  SMC_DATA_FORBIDPHONE  t  ";

	private String insert = "INSERT INTO SMC_DATA_FORBIDPHONE  (TELEPHONE, BUSINESSID) VALUES "
			+ " ('%s', '%s')";

	private String delete = "DELETE SMC_DATA_FORBIDPHONE WHERE TELEPHONE= '%s' and  BUSINESSID= %d";

	@Override
	public List<ForbidPhone> list() throws Exception {
		return this.query(sql);
	}

	public Map<String, List<Integer>> map() throws Exception {
		LOG.debug("加载禁用号码业务信息.");
		return this.queryMap(sql);
	}

	public Map<String, List<Integer>> queryMap(String sql) throws Exception {
		List<ForbidPhone> list = new ArrayList<ForbidPhone>();
		Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			while (rs.next()) {
				if (StringUtil.isNull(rs.getString("telephone")))
					continue;
				ForbidPhone sdh = new ForbidPhone();
				sdh.setBusinessId(rs.getInt("BusinessID"));
				String pho = rs.getString("telephone").startsWith("86") ? rs
						.getString("telephone") : "86"
						+ rs.getString("telephone");
				sdh.setPhone(pho);
				list.add(sdh);
				if (!map.containsKey(sdh.getPhone())) {
					List<Integer> ls = new ArrayList<Integer>();
					ls.add(sdh.getBusinessId());
					map.put(sdh.getPhone(), ls);
				} else {
					List<Integer> ls = map.get(sdh.getPhone());
					if (!ls.contains(sdh.getBusinessId())) {
						ls.add(sdh.getBusinessId());
					}
				}
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return map;
	}

	public List<ForbidPhone> query(String sql) throws Exception {
		List<ForbidPhone> list = new ArrayList<ForbidPhone>();

		Map<String, List<ForbidPhone>> map = new HashMap<String, List<ForbidPhone>>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			list = new ArrayList<ForbidPhone>();
			while (rs.next()) {
				ForbidPhone phoneObj = new ForbidPhone();
				phoneObj.setBusinessId(rs.getInt("BUSINESSID"));
				phoneObj.setPhone(rs.getString("TELEPHONE"));
				list.add(phoneObj);
				if (!map.containsKey(phoneObj.getPhone())) {
					List<ForbidPhone> ls = new ArrayList<ForbidPhone>();
					ls.add(phoneObj);
					map.put(phoneObj.getPhone(), ls);
				} else {
					List<ForbidPhone> ls = map.get(phoneObj.getPhone());
					if (!ls.contains(phoneObj)) {
						ls.add(phoneObj);
					}
				}
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return list;
	}

	public List<ForbidPhone> get(int id) throws Exception {
		String sql = " SELECT * FROM SMC_DATA_FORBIDPHONE WHERE TELEPHONE='"
				+ id + "'";
		return query(sql);

	}

	public int add(ForbidPhone entity) throws Exception {
		String sql = new String(insert);
		sql = String.format(sql, entity.getPhone(), entity.getBusinessId());
		return DBUtil.executeUpdate(sql);
	}

	public boolean addList(List<ForbidPhone> entitys) throws Exception {
		if (entitys == null)
			return false;

		List<String> sqlList = new ArrayList<String>();
		for (ForbidPhone entity : entitys) {
			String sql = new String(insert);
			sql = String.format(sql, entity.getPhone(), entity.getBusinessId());
			sqlList.add(sql);
		}

		return DBUtil.executeBatch(sqlList) != null;
	}

	public int delete(String phone, int busid) throws Exception {
		String deletesql = new String(delete);
		deletesql = String.format(deletesql, phone, busid);
		return DBUtil.executeUpdate(deletesql);
	}

	public boolean deleteList(List<ForbidPhone> list) throws SQLException {
		List<String> sql = new ArrayList<String>();
		for (ForbidPhone f : list) {
			String deletesql = new String(delete);
			deletesql = String.format(deletesql, f.getPhone(),
					f.getBusinessId());
			sql.add(deletesql);
		}

		return DBUtil.executeBatch(sql) != null;
	}

	public static void main(String[] args) {
		SmcCfgForbidPhoneDAO dao = new SmcCfgForbidPhoneDAO();
		ForbidPhone entity = new ForbidPhone();
		entity.setBusinessId(2102);
		entity.setPhone("123");
		try {
			dao.add(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
