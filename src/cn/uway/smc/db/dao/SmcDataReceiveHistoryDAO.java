package cn.uway.smc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.DateUtil;
import cn.uway.commons.type.StringUtil;
import cn.uway.ews.dao.AbstractDAO;
import cn.uway.smc.db.conn.DBPool;
import cn.uway.smc.db.conn.DBUtil;
import cn.uway.smc.db.pojo.SMCDataReceiveHistory;
import cn.uway.smc.util.ConstDef;

/**
 * 短信接收历史表操作
 * 
 * @since 1.0
 */
public class SmcDataReceiveHistoryDAO
		extends
			AbstractDAO<SMCDataReceiveHistory> {

	private static Logger LOG = LoggerFactory
			.getLogger(SmcDataReceiveHistoryDAO.class);

	@Override
	public List<SMCDataReceiveHistory> list() throws Exception {
		String sql = "select * from  SMC_Data_Receive_History  t order by t.receive_time desc  ";
		return this.query(sql);
	}

	@Override
	public List<SMCDataReceiveHistory> query(String sql) throws Exception {
		List<SMCDataReceiveHistory> list = new ArrayList<SMCDataReceiveHistory>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			list = new ArrayList<SMCDataReceiveHistory>();
			while (rs.next()) {
				SMCDataReceiveHistory sdh = new SMCDataReceiveHistory();
				sdh.setId(rs.getInt("ID"));
				sdh.setFromUser(rs.getString("FROM_USER"));

				if (rs.getDate("RECEIVE_TIME") != null) {
					String oc = rs.getDate("RECEIVE_TIME").toString() + " "
							+ rs.getTime("RECEIVE_TIME").toString();
					sdh.setReceiveTime(DateUtil
							.getDate(oc, ConstDef.DATEFORMAT));
					sdh.setReceiveTimeTmp(oc);
				} else {
					sdh.setReceiveTimeTmp(null);
				}

				sdh.setReceiveTime(rs.getDate("RECEIVE_TIME"));
				sdh.setContent(rs.getString("CONTENT"));

				if (rs.getDate("stamptime") != null) {
					String oc = rs.getDate("stamptime").toString() + " "
							+ rs.getTime("stamptime").toString();
					sdh.setStampTimeTmp(oc);
				} else {
					sdh.setStampTimeTmp(null);
				}
				sdh.setStampTime(rs.getTimestamp("stamptime"));

				sdh.setResult(rs.getInt("RESULT"));
				sdh.setCause(rs.getString("CAUSE"));

				sdh.setBusinessId(rs.getInt("BusinessID"));
				sdh.setIseffect(rs.getInt("ISEFFECT"));
				sdh.setIsParseOk(rs.getInt("ISPARSEOK"));
				sdh.setRemark(rs.getString("REMARK"));

				sdh.setSpNumber(rs.getString("SPNUMBER"));

				list.add(sdh);
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return list;
	}

	public SMCDataReceiveHistory getId(int id) throws Exception {
		String sql = "select * from SMC_Data_Receive_History where id=" + id;
		List<SMCDataReceiveHistory> list = query(sql);
		if (list == null || list.size() <= 0)
			return null;
		SMCDataReceiveHistory smcData = list.get(0);
		return smcData;

	}

	@Override
	public int add(SMCDataReceiveHistory entity) throws Exception {
		String sql = "insert into SMC_Data_Receive_History  (id, from_user, receive_time, content, stamptime, result, cause,BUSINESSID,ISEFFECT,ISPARSEOK,REMARK,spnumber) values"
				+ " (%s, '%s', %s, '%s', %s, %s,'%s',%s, %s,%s,'%s','%s')";
		sql = String.format(
				sql,
				entity.getId(),
				entity.getFromUser(),

				entity.getReceiveTime() == null ? "sysdate" : "to_date('"
						+ DateUtil.getDateString(entity.getReceiveTime())
						+ "','yyyy-mm-dd hh24:mi:ss')",
				StringUtil.isNull(entity.getContent()) ? "" : entity
						.getContent(), "sysdate", entity.getResult(), entity
						.getCause() == null ? "" : entity.getCause(), entity
						.getBusinessId() == -1 ? null : entity.getBusinessId(),
				entity.getIseffect(), null, entity.getRemark() == null
						? ""
						: entity.getRemark(), StringUtil.isNull(entity
						.getSpNumber()) ? "" : entity.getSpNumber());
		LOG.debug(sql);
		return DBUtil.executeUpdate(sql);
	}

	public static void main(String[] args) {
		SmcDataReceiveHistoryDAO dao = new SmcDataReceiveHistoryDAO();

		SMCDataReceiveHistory d = new SMCDataReceiveHistory();
		d.setId(1);
		d.setBusinessId(1000);
		d.setCause("11");
		d.setContent("1");
		d.setReceiveTime(new Date());
		d.setStampTime(new Date());
		d.setFromUser("s");
		d.setResult(1);
		try {
			dao.add(d);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			dao.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
