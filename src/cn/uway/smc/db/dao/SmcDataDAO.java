package cn.uway.smc.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.jstl.sql.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.DateUtil;
import cn.uway.commons.type.StringUtil;
import cn.uway.ews.dao.AbstractDAO;
import cn.uway.smc.businesses.BussinessMgr;
import cn.uway.smc.businesses.TaskMgr;
import cn.uway.smc.db.conn.DBPool;
import cn.uway.smc.db.conn.DBUtil;
import cn.uway.smc.db.pojo.SMCCfgLevel;
import cn.uway.smc.db.pojo.SMCCfgSource;
import cn.uway.smc.db.pojo.SMCCfgStrategy;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.util.ConstDef;
import cn.uway.smc.util.TimeIntervalMgr;

/**
 * 快递短信表操作
 * 
 * @since 1.0
 */
public class SmcDataDAO extends AbstractDAO<SMCData> {

	private final static Logger LOG = LoggerFactory.getLogger(SmcDataDAO.class);

	private String sql = " select m.* from (select row_number() over(order by d.send_time asc) seq ,d.id d_id,d.SRC_ID ,d.LEVEL_ID ,d.TO_USERS ,d.SEND_WAY ,d.OCCUR_TIME ,d.CONTENT ,to_char(d.SEND_TIME,'yyyy-MM-dd hh24:mi:ss') SEND_TIME ,d.SEND_TIME_EXCLUDE ,d.attachmentfile,d.STAMPTIME ,d.USER_NAME d_user_name ,d.PASSWORD d_password ,d.SENT_OK_TIMES ,d.SUBJECT ,d.TYPE ,s.name s_name, s.username ,s.ip_list,s.password ,s.description ,l.id l_id, l.name ,l.description l_description ,y.id y_id,y.to_usergroup_id ,y.ttl ,y.send_offset_time ,y.send_times ,y.send_interval ,y.take_effect ,y.resend_when_fail ,  y.send_way y_send_way  from smc_data   d ,smc_cfg_source s ,smc_cfg_level  l ,smc_cfg_strategy y  where ( d.SENT_OK_TIMES >= 0 or d.SENT_OK_TIMES is null)  and d.src_id = s.id  and d.level_id = l.levelid  and y.src_id = d.src_id  and y.level_id = d.level_id   and y.take_effect = 1) m  where  seq < 200 order by occur_time   ";

	private String queryAllsql = " select row_number() over(order by d.send_time desc) seq ,d.id d_id,d.SRC_ID ,d.LEVEL_ID ,d.TO_USERS ,d.SEND_WAY ,d.OCCUR_TIME ,d.CONTENT ,to_char(d.SEND_TIME,'yyyy-MM-dd hh24:mi:ss') SEND_TIME ,d.SEND_TIME_EXCLUDE ,d.STAMPTIME ,d.USER_NAME d_user_name ,d.PASSWORD d_password ,d.SENT_OK_TIMES ,d.SUBJECT ,d.TYPE ,s.name s_name, s.username ,s.ip_list,s.password ,s.description ,l.id l_id, l.name ,l.description l_description ,y.id y_id,y.to_usergroup_id ,y.ttl ,y.send_offset_time ,y.send_times ,y.send_interval ,y.take_effect ,y.resend_when_fail ,  y.send_way y_send_way  from smc_data   d ,smc_cfg_source s ,smc_cfg_level  l ,smc_cfg_strategy y  where d.type = 1  and d.SENT_OK_TIMES >= 1  and d.src_id = s.id  and d.level_id = l.levelid  and y.src_id = d.src_id  and y.level_id = d.level_id   and y.take_effect = 1 ";

	@Override
	public List<SMCData> list() throws Exception {
		return common(sql);
	}

	public List<SMCData> query(String sql) throws Exception {
		return common(sql);
	}

	public List<SMCData> common(String sql) throws Exception {
		LOG.info("扫描smc_data待发送信息表.语句： " + sql);
		List<SMCData> list = new ArrayList<SMCData>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			list = new ArrayList<SMCData>();
			while (rs.next()) {
				SMCData e = new SMCData();
				try {
					e.setId(rs.getInt("d_id"));
					e.setSrcid(rs.getInt("SRC_ID"));
					e.setLevelid(rs.getInt("LEVEL_ID"));

					int sendWay = rs.getInt("SEND_WAY");

					e.setSendWay(sendWay);

					String to_user = rs.getString("TO_USERS");

					e.setToUsers(to_user);

					String user = rs.getString("TO_USERS");
					// 屏蔽前端应用平台传过来的分割符差异，将","替换为";"
					if (StringUtil.isNotNull(user))
						user = user.replace(",", ";");

					e.setToUsers(user);

					e.setOccurTime(rs.getTimestamp("OCCUR_TIME"));
					String content = rs.getString("CONTENT");
					content = content.replaceAll("\r", "").replaceAll("\n", "")
							.replaceAll(" ", "");

					e.setContent(content);
					e.setSendTime(rs.getString("SEND_TIME"));
					e.setSendTimeExclude(rs.getString("SEND_TIME_EXCLUDE"));

					e.setSentOkTimes(rs.getInt("SENT_OK_TIMES"));
					e.setSubject(rs.getString("SUBJECT"));
					e.setType(rs.getInt("TYPE"));
					e.setAttachmentfile(rs.getString("ATTACHMENTFILE"));

					SMCCfgSource source = new SMCCfgSource();

					source.setSrcid(e.getSrcid());
					source.setIpList(rs.getString("IP_LIST"));
					source.setName(rs.getString("s_name"));
					source.setUser(rs.getString("username"));
					source.setPwd(rs.getString("password"));
					source.setDescription(rs.getString("description"));
					e.setSmcSource(source);

					SMCCfgLevel level = new SMCCfgLevel();
					level.setId(rs.getInt("l_id"));
					level.setLevelid(e.getLevelid());
					level.setName(rs.getString("name"));
					level.setDescription(rs.getString("l_description"));

					e.setSmcLevel(level);

					SMCCfgStrategy y = new SMCCfgStrategy();
					y.setId(rs.getInt("y_id"));
					y.setSrcid(e.getSrcid());
					y.setLevelid(e.getLevelid());
					y.setTtl(rs.getInt("ttl"));
					y.setTakeEffect(rs.getInt("take_effect"));
					y.setEndoOffsetTime(rs.getInt("send_offset_time"));
					y.setSendTimes(rs.getInt("send_times"));
					y.setSendWay(rs.getInt("y_send_way"));
					y.setResendWhenFail(rs.getInt("resend_when_fail"));
					y.setSendInterval(rs.getInt("send_interval"));

					e.setStrategy(y);

					Map<String, List<String>> users = ConstDef
							.getPhoneEmail(to_user);

					if (sendWay == 2) {
						List<String> eList = users.get(ConstDef.EMAIL);
						if (eList == null || eList.isEmpty()) {
							BussinessMgr.sendAfter(TaskMgr.getInstance()
									.getSys(), e,  ConstDef.USERERROR, "邮件接收人为空.");
							continue;
						}
					} else {
						List<String> eList = users.get(ConstDef.PHONE);
						if (eList == null || eList.isEmpty()) {
							BussinessMgr.sendAfter(TaskMgr.getInstance()
									.getSys(), e,  ConstDef.USERERROR, "短信接收人为空.");
							continue;
						}
					}
				} catch (Exception ex) {
					LOG.error(e+",构建出现异常",e);
				}

				list.add(e);
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return list;
	}

	public PageQueryResult<SMCData> advQuery(SMCData t, int pageSize,
			int currentPage) throws Exception {
		int start = pageSize * currentPage - pageSize + 1; // 算出此页第一条记录的rownum
		int end = pageSize * currentPage; // 算出此页最后一条记录的rownum
		int recordCount = 0; // 查询出来的记录数
		String sql = "";
		if (t == null) {
			sql = "with partdata as (select rownum rowno,t.* from ("
					+ queryAllsql
					+ ")  t )"
					+ " select * from partdata where rowno between __start and __end";
		} else {
			StringBuilder b = new StringBuilder();
			b.append("with partdata as (select rownum rowno,t.* from ("
					+ queryAllsql + ")  t where 1<>0");
			if (t.getId() != -1)
				b.append(" and t.id=").append(t.getId());

			b.append(") select * from partdata where rowno between __start and __end");
			sql = b.toString();
		}
		sql = sql.replace("__start", String.valueOf(start));
		sql = sql.replace("__end", String.valueOf(end));

		List<SMCData> list = this.query(sql);
		recordCount = getCount(sql);
		int pageCount = recordCount % pageSize == 0
				? recordCount / pageSize
				: recordCount / pageSize + 1; // 算出当前pareSize情况下的总页数
		PageQueryResult<SMCData> pageQueryResult = new PageQueryResult<SMCData>(
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

	/**
	 * @return
	 * @throws Exception
	 */
	public List<SMCData> scanExpressData() throws Exception {
		Date now = TimeIntervalMgr.getInstance().getDateValue(new Date());
		List<SMCData> list = this.list();
		for (SMCData express : list) {
			boolean b = readyToSend(now, express);
			if (!b)
				continue;
		}
		return list;
	}

	/** 此消息是否可以发送，如果为空，立即发送 */
	private boolean readyToSend(Date now, SMCData express) {
		boolean flag = false;
		if (StringUtil.isNull(express.getSendTime()))
			return true;
		try {
			long sTime = DateUtil.getDate(express.getSendTime()).getTime();
			if (now.getTime() >= sTime)
				return true;
		} catch (java.text.ParseException e) {
			LOG.debug("parse exception ,{}", e);
		}
		return flag;
	}

	public List<SMCData> listAll() throws Exception {
		String sql = "select * from  smc_data ";
		return this.query(sql);
	}

	public int add(SMCData entity) throws Exception {
		String sql = " insert into smc_data "
				+ " (id, src_id, level_id, to_users, send_way, occur_time, "
				+ "content, send_time, send_time_exclude, stamptime, "
				+ "user_name, password, sent_ok_times, subject, type,ATTACHMENTFILE) values (?,?,?,?,?,?,?,?,?,sysdate,?,?,?,?,?,?)";

		PreparedStatement ps = null;
		int result = 0;
		Connection conn = null;
		try {
			conn = DBPool.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, entity.getId());
			ps.setInt(2, entity.getSrcid());
			ps.setInt(3, entity.getLevelid());
			ps.setString(4, entity.getToUsers());
			ps.setInt(5, entity.getSendWay());

			Timestamp ts1 = entity.getOccurTime() == null
					? null
					: new java.sql.Timestamp(entity.getOccurTime().getTime());
			ps.setTimestamp(6, ts1);
			ps.setString(7, entity.getContent());
			Timestamp ts2 = entity.getSendTime() == null
					? null
					: new java.sql.Timestamp(DateUtil.getDate(
							entity.getSendTime()).getTime());
			ps.setTimestamp(8, ts2);

			ps.setString(9, StringUtil.isNull(entity.getSendTimeExclude())
					? null
					: entity.getSendTimeExclude());
			ps.setString(10, StringUtil.isNull(entity.getUsername())
					? null
					: entity.getUsername());
			ps.setString(11, StringUtil.isNull(entity.getPassword())
					? null
					: entity.getPassword());
			ps.setInt(12, entity.getSentOkTimes());
			ps.setString(13, StringUtil.isNull(entity.getSubject())
					? null
					: entity.getSubject());
			ps.setInt(14, entity.getType());
			ps.setString(15, entity.getAttachmentfile());

			result = ps.executeUpdate();
		} catch (Exception e) {
			LOG.error("添加消息失败,sql:" + sql, e);
		} finally {
			if (conn != null)
				conn.commit();
			DBUtil.close(null, ps, conn);
		}
		return result;
	}

	@Override
	public boolean update(SMCData entity) throws Exception {
		String sql = "update smc_data t set t.sent_ok_times= sent_ok_times+1  where t.id="
				+ entity.getId();
		return DBUtil.executeUpdate(sql) > 0;
	}

	@Override
	public boolean delete(int key) throws Exception {
		String sql = " delete smc_data t where t.id =" + key;
		return DBUtil.executeUpdate(sql) > 0;
	}

	public boolean delete(long key) throws Exception {
		String sql = " delete smc_data t where t.id =" + key;
		return DBUtil.executeUpdate(sql) > 0;
	}

	@Override
	public boolean delete(SMCData entity) throws Exception {
		return this.delete(entity.getId());
	}

	public SMCData get(int key) throws Exception {
		String sql = " select * from  smc_data t where t.id =" + key;
		List<SMCData> list = common(sql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	public boolean deleteAll() throws Exception {
		String sql = " delete smc_data t ";
		return DBUtil.executeUpdate(sql) > 0;
	}

	public static void main(String[] args) {
		SmcDataDAO dao = new SmcDataDAO();
		try {
			SMCData data = new SMCData();
			data.setId(333333);
			dao.add(data);
			List<SMCData> list = dao.list();
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String content = "a\r b\nc dd  ee";
		content = content.replaceAll("\r", "").replaceAll("\n", "")
				.replaceAll(" ", "");
		System.out.println(content);

	}

}
