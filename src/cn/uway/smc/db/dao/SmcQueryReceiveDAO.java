package cn.uway.smc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cn.uway.ews.dao.AbstractDAO;
import cn.uway.smc.businesses.SMCQueryReceive;
import cn.uway.smc.db.conn.DBPool;
import cn.uway.smc.db.conn.DBUtil;

/**
 * 短信息查询以及恢复配置操作
 * 
 * @since 1.0
 */
public class SmcQueryReceiveDAO extends AbstractDAO<SMCQueryReceive> {

	private String sql = "select * from  smc_query_receive  t  ";

	@Override
	public List<SMCQueryReceive> list() throws Exception {
		return this.query(sql);
	}

	public List<SMCQueryReceive> listByClassId(int classid) throws Exception {
		String sqlByClassid = "select * from  smc_query_receive  t  where t.classid= "
				+ classid;
		return this.query(sqlByClassid);
	}

	public List<Integer> queryId(String sql) throws Exception {
		List<Integer> list = new ArrayList<Integer>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			list = new ArrayList<Integer>();
			while (rs.next()) {
				list.add(rs.getInt("QUERYHELPID"));
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return list;
	}

	@Override
	public List<SMCQueryReceive> query(String sql) throws Exception {
		List<SMCQueryReceive> list = new ArrayList<SMCQueryReceive>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			while (rs.next()) {
				SMCQueryReceive sdh = new SMCQueryReceive();
				sdh.setSendSMCType(rs.getString("SENDSMSTYPE"));
				sdh.setBusinessid(rs.getInt("BUSINESSID"));
				sdh.setSendSMCContent(rs.getString("SENDSMSCONTENT"));
				sdh.setContentexplanAtion(rs.getString("CONTENTEXPLANATION"));
				sdh.setProcessType(rs.getInt("PROCESSTYPE"));
				sdh.setIsNeedRebackSMS(rs.getInt("ISNEEDREBACKSMS"));
				sdh.setReBackSMSContent(rs.getString("REBACKSMSCONTENT"));

				list.add(sdh);
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return list;
	}

	public static void main(String[] args) {
	}

}
