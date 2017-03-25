package cn.uway.smc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.jsp.jstl.sql.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.DateUtil;
import cn.uway.commons.type.StringUtil;
import cn.uway.ews.dao.AbstractDAO;
import cn.uway.smc.db.conn.DBPool;
import cn.uway.smc.db.conn.DBUtil;
import cn.uway.smc.db.pojo.SMCDataHistory;

/**
 * 快递短信息历史表操作 历史数据存储在oracle数据库中
 * 
 * @since 1.0
 */
public class SmcDataHistoryDAO extends AbstractDAO<SMCDataHistory> {

	String pagecount = " select count(*)  pagecount from smc_express_data_history  ";

	private final static Logger LOG = LoggerFactory
			.getLogger(SmcDataHistoryDAO.class);

	@Override
	public List<SMCDataHistory> list() throws Exception {
		String sql = " select id, src_id, level_id, to_users, send_way, to_char(occur_time,'YYYY-MM-DD hh24:mi:ss') occur_time, content,  to_char(send_time,'YYYY-MM-DD hh24:mi:ss') send_time, send_time_exclude, send_result, to_char(stamptime,'yyyy-MM-dd hh24:mi:ss') stamptime , cause from smc_express_data_history t order by t.stamptime desc  ";
		return this.query(sql);
	}

	public PageQueryResult<SMCDataHistory> advQuery(SMCDataHistory t,
			int pageSize, int currentPage) throws Exception {
		int start = pageSize * currentPage - pageSize + 1; // 算出此页第一条记录的rownum
		int end = pageSize * currentPage; // 算出此页最后一条记录的rownum
		int recordCount = 0; // 查询出来的记录数
		String sql = "";
		if (t == null) {
			sql = "with partdata as (select rownum rowno,t.* from smc_express_data_history t )"
					+ " select * from partdata where rowno between __start and __end";
		} else {
			StringBuilder b = new StringBuilder();
			b.append("with partdata as (select rownum rowno,t.* from smc_express_data_history t where 1<>0");
			if (t.getId() != -1)
				b.append(" and t.id=").append(t.getId());

			b.append(") select * from partdata where rowno between __start and __end");
			sql = b.toString();
		}
		sql = sql.replace("__start", String.valueOf(start));
		sql = sql.replace("__end", String.valueOf(end));

		List<SMCDataHistory> list = this.query(sql);
		recordCount = getCount(sql);
		int pageCount = recordCount % pageSize == 0
				? recordCount / pageSize
				: recordCount / pageSize + 1; // 算出当前pareSize情况下的总页数
		PageQueryResult<SMCDataHistory> pageQueryResult = new PageQueryResult<SMCDataHistory>(
				pageSize, currentPage, pageCount, list);
		return pageQueryResult;
	}

	private int getCount(String sql) {
		String s = sql.replace("with partdata as (", "");
		s = s.substring(0, s.indexOf(") select"));
		s = s.replace(s.substring(7, s.indexOf(" from")), "count(*) as c");

		Result rs = null;
		try {
			rs = DBUtil.queryForResult(s);
		} catch (Exception e) {
			LOG.error("查询记录数时异常", e);
		}
		if (rs == null)
			return 0;

		int c = Integer.parseInt(rs.getRows()[0].get("c").toString());

		return c;
	}

	@Override
	public List<SMCDataHistory> query(String sql) throws Exception {
		List<SMCDataHistory> list = new ArrayList<SMCDataHistory>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			list = new ArrayList<SMCDataHistory>();
			while (rs.next()) {
				SMCDataHistory e = new SMCDataHistory();
				e.setId(rs.getInt("ID"));
				e.setSrcId(rs.getInt("SRC_ID"));
				e.setLevelId(rs.getInt("LEVEL_ID"));
				e.setToUsers(rs.getString("TO_USERS"));
				e.setSendWay(rs.getInt("SEND_WAY"));

				String occur_time = rs.getString("OCCUR_TIME");
				if (StringUtil.isNull(occur_time))
					e.setOccurTime(null);
				else {
					Date d = DateUtil.getDate(occur_time);
					e.setOccurTimeTmp(occur_time);
					e.setOccurTime(d);
				}
				e.setContent(rs.getString("CONTENT"));

				String send_time = rs.getString("SEND_TIME");
				if (StringUtil.isNull(send_time))
					e.setSendTime("");
				else {
					e.setSendTime(send_time);
				}

				e.setSendTimeExclude(rs.getString("SEND_TIME_EXCLUDE"));
				e.setSentResult(rs.getInt("SEND_RESULT"));

				String stmptime = rs.getString("StampTime");

				if (StringUtil.isNull(stmptime))
					e.setStampTime(null);
				else {
					Date d = DateUtil.getDate(stmptime);
					e.setStampTimeTmp(stmptime);
					e.setStampTime(d);
				}
				String cause = rs.getString("CAUSE");
				e.setCause(StringUtil.isNull(cause) ? "" : cause);
				list.add(e);
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return list;
	}

	public SMCDataHistory getId(int id) throws Exception {
		String sql = "select * from smc_express_data_history where id=" + id;
		List<SMCDataHistory> list = query(sql);
		SMCDataHistory smcData = list.get(0);
		return smcData;

	}

	public List<Integer> getIdList(String sql) throws Exception {
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
				list.add(rs.getInt("ID"));
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return list;

	}

	@Override
	public int add(SMCDataHistory entity) throws Exception {
		// ,attachmentfile,counter
		String sql = "insert into smc_express_data_history  (id, src_id, level_id, to_users, send_way, occur_time, content, send_time, send_time_exclude, send_result, stamptime, cause) values"
				+ " (%s, %s, %s, '%s', %s, %s, '%s', %s,'%s', %s, %s, '%s')";

		sql = String.format(
				sql,
				entity.getId(),
				entity.getSrcId(),
				entity.getLevelId(),
				entity.getToUsers() == null ? "" : entity.getToUsers(),
				entity.getSendWay(),
				entity.getOccurTime() == null ? null : "to_date('"
						+ DateUtil.getDateString(entity.getOccurTime())
						+ "','yyyy-mm-dd hh24:mi:ss')",
				entity.getContent() == null ? "" : entity.getContent(),
				entity.getSendTime() == null ? "sysdate" : "to_date('"
						+ entity.getSendTime() + "','yyyy-mm-dd hh24:mi:ss')",
				entity.getSendTimeExclude() == null ? "" : entity
						.getSendTimeExclude(), entity.getSentResult(), entity
						.getStampTime() == null ? "sysdate" : "to_date('"
						+ DateUtil.getDateString(entity.getStampTime())
						+ "','yyyy-mm-dd hh24:mi:ss')",
				entity.getCause() == null ? "" : entity.getCause());
		LOG.debug("快递历史sql语句：" + sql);

		return DBUtil.executeUpdate(sql);
	}

	public static void main(String[] args) {
		// SMCExpressDataHistoryDAO dao = new SMCExpressDataHistoryDAO();
		// try
		// {
		// // SMCExpressDataHistory en=new SMCExpressDataHistory();
		// // en.setId(1);
		// // en.setLevelId(1);
		// // en.setCause("cause");
		// // en.setContent("content");
		// // en.setSrcId(1);
		// // en.setSentResult(1);
		// // dao.add(en);
		// //int size = dao.list().size();
		// //log.debug(size);
		// }
		// catch (Exception e)
		// {
		// e.printStackTrace();
		// }
	}

}
