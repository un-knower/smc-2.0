package cn.uway.smc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.StringUtil;
import cn.uway.ews.dao.AbstractDAO;
import cn.uway.smc.db.conn.DBPool;
import cn.uway.smc.db.conn.DBUtil;
import cn.uway.smc.db.pojo.SMCDataReceive;

/**
 * 短信接收表操作
 * 
 * @since 1.0
 */
public class SmcDataReceiveDAO extends AbstractDAO<SMCDataReceive> {

	private static Logger LOG = LoggerFactory
			.getLogger(SmcDataReceiveDAO.class);

	@Override
	public List<SMCDataReceive> list() throws Exception {
		String sql = "select * from  smc_data_receive  t order by t.receive_time desc  ";
		return this.query(sql);
	}

	@Override
	public List<SMCDataReceive> query(String sql) throws Exception {
		List<SMCDataReceive> list = new ArrayList<SMCDataReceive>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			list = new ArrayList<SMCDataReceive>();
			while (rs.next()) {
				SMCDataReceive sdh = new SMCDataReceive();
				sdh.setId(rs.getInt("ID"));
				String fromuser = rs.getString("FROM_USER");
				sdh.setFromUser(StringUtil.isNull(fromuser) ? "" : fromuser);

				Date receive_time = rs.getDate("RECEIVE_TIME");
				sdh.setReceiveTime(receive_time);

				if (rs.getDate("RECEIVE_TIME") == null) {
					sdh.setReceiveTimeTmp(null);
				} else {
					sdh.setReceiveTimeTmp(rs.getDate("RECEIVE_TIME").toString()
							+ " " + rs.getTime("RECEIVE_TIME"));
				}
				sdh.setContent(rs.getString("CONTENT"));
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

	public SMCDataReceive get(int id) throws Exception {
		String sql = "select * from smc_data_receive where id=" + id;
		List<SMCDataReceive> list = query(sql);
		SMCDataReceive smcData = list.get(0);
		return smcData;

	}

	@Override
	public int add(SMCDataReceive entity) throws Exception {
		String sql = "insert into SMC_Data_Receive  (id, from_user, receive_time, content, BusinessID, ISEFFECT,isparseok,remark,spnumber) values"
				+ " (%s, '%s', %s, '%s', '%s', %s,%s,'%s','%s')";
		sql = String.format(
				sql,
				entity.getId(),
				entity.getFromUser(),
				"sysdate",
				StringUtil.isNull(entity.getContent()) ? "" : entity
						.getContent(), entity.getBusinessId() == -1
						? ""
						: entity.getBusinessId(), entity.getIseffect(), entity
						.getIsParseOk(), StringUtil.isNull(entity.getRemark())
						? ""
						: entity.getRemark(), StringUtil.isNull(entity
						.getSpNumber()) ? "" : entity.getSpNumber());
		LOG.debug(sql);
		return DBUtil.executeUpdate(sql);
	}

	@Override
	public boolean delete(int key) throws Exception {
		String sql = "delete  smc_data_receive  t   where t.id=" + key;
		return DBUtil.executeUpdate(sql) >= 1;
	}

	public static void main(String[] args) {
		SmcDataReceiveDAO dao = new SmcDataReceiveDAO();
		SMCDataReceive entity = new SMCDataReceive();
		entity.setId(0000);
		entity.setFromUser("123");
		entity.setContent("222");
		entity.setIseffect(1);
		entity.setBusinessId(2102);
		entity.setIsParseOk(1);
		entity.setRemark("22");
		try {
			dao.add(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
