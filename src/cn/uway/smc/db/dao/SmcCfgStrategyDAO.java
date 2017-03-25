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
import cn.uway.smc.db.pojo.SMCCfgLevel;
import cn.uway.smc.db.pojo.SMCCfgSource;
import cn.uway.smc.db.pojo.SMCCfgStrategy;
import cn.uway.smc.db.pojo.SMCCfgToUserGroup;

/**
 * 配置策略操作
 * 
 * @since 1.0
 */
public class SmcCfgStrategyDAO extends AbstractDAO<SMCCfgStrategy> {

	private final static Logger LOG = LoggerFactory
			.getLogger(SmcCfgStrategyDAO.class);

	@Override
	public List<SMCCfgStrategy> list() throws Exception {
		String sql = "select  ID,SRC_ID	  ,LEVEL_ID,TO_USERGROUP_ID,TTL,SEND_OFFSET_TIME,SEND_TIMES,SEND_INTERVAL	  ,SEND_WAY,RESEND_WHEN_FAIL,TAKE_EFFECT from SMC_Cfg_Strategy ";
		return common(sql);
	}

	public List<SMCCfgStrategy> common(String sql) throws Exception {
		List<SMCCfgStrategy> list = new ArrayList<SMCCfgStrategy>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			list = new ArrayList<SMCCfgStrategy>();
			while (rs.next()) {
				SMCCfgStrategy e = new SMCCfgStrategy();
				e.setId(rs.getInt("ID"));
				e.setSrcid(rs.getInt("SRC_ID"));
				e.setLevelid(rs.getInt("LEVEL_ID"));
				e.setGroupId(rs.getInt("TO_USERGROUP_ID"));
				e.setTtl(rs.getInt("TTL"));
				e.setEndoOffsetTime(rs.getInt("SEND_OFFSET_TIME"));
				e.setSendTimes(rs.getInt("SEND_TIMES"));
				e.setSendInterval(rs.getInt("SEND_INTERVAL"));
				e.setSendWay(rs.getInt("SEND_WAY"));
				e.setResendWhenFail(rs.getInt("RESEND_WHEN_FAIL"));
				e.setTakeEffect(rs.getInt("TAKE_EFFECT"));
				list.add(e);
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return list;
	}

	public List<SMCCfgStrategy> list2() throws Exception {
		String sql = " select   d.id d_id,d.name d_name,d.description d_description,s.name s_name,s.username,s.ip_list,s.password,s.description,l.id l_id,l.name,l.description l_description,y.id y_id,y.to_usergroup_id,y.ttl,y.level_id,y.src_id,y.send_offset_time,y.send_times,y.send_interval,y.take_effect,y.resend_when_fail,y.send_way y_send_way   from smc_cfg_tousergroup         d,smc_cfg_source   s,smc_cfg_level    l,smc_cfg_strategy y  where y.src_id = s.id and y.level_id = l.levelid  and y.to_usergroup_id = d.id and y.take_effect = 1  ";
		return common2(sql, true);
	}

	public SMCCfgStrategy chechStrategy(int srcid, int levelid)
			throws Exception {
		String sql = " select distinct s.name s_name,s.username,s.ip_list,s.password,s.description,l.id l_id,l.name,l.description l_description,y.id y_id,y.to_usergroup_id,y.ttl,y.level_id,y.src_id,y.send_offset_time,y.send_times,y.send_interval,y.take_effect,y.resend_when_fail,y.send_way y_send_way   from smc_cfg_tousergroup         d,smc_cfg_source   s,smc_cfg_level    l,smc_cfg_strategy y  where y.src_id = s.id and y.level_id = l.levelid   and y.take_effect = 1   and  y.src_id="
				+ srcid + " and y.level_id= " + levelid;
		List<SMCCfgStrategy> list = common2(sql, false);
		if (list == null || list.size() <= 0)
			return null;

		return list.get(0);
	}

	public List<SMCCfgStrategy> common2(String sql, boolean flag)
			throws Exception {
		System.out.println(sql);
		List<SMCCfgStrategy> list = new ArrayList<SMCCfgStrategy>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			list = new ArrayList<SMCCfgStrategy>();
			while (rs.next()) {
				SMCCfgStrategy y = new SMCCfgStrategy();
				y.setId(rs.getInt("y_id"));
				y.setSrcid(rs.getInt("src_id"));
				y.setLevelid(rs.getInt("level_id"));
				y.setTtl(rs.getInt("ttl"));
				y.setTakeEffect(rs.getInt("take_effect"));
				y.setEndoOffsetTime(rs.getInt("send_offset_time"));
				y.setSendTimes(rs.getInt("send_times"));
				y.setSendWay(rs.getInt("y_send_way"));
				y.setResendWhenFail(rs.getInt("resend_when_fail"));
				y.setSendInterval(rs.getInt("send_interval"));

				SMCCfgSource source = new SMCCfgSource();

				source.setSrcid(y.getSrcid());
				source.setIpList(rs.getString("IP_LIST"));
				source.setName(rs.getString("s_name"));
				source.setUser(rs.getString("username"));
				source.setPwd(rs.getString("password"));
				source.setDescription(rs.getString("description"));
				y.setSmcSource(source);

				SMCCfgLevel level = new SMCCfgLevel();
				level.setId(rs.getInt("l_id"));
				level.setLevelid(y.getLevelid());
				level.setName(rs.getString("name"));
				level.setDescription(rs.getString("l_description"));

				y.setSmcLevel(level);

				if (flag) {
					SMCCfgToUserGroup g = new SMCCfgToUserGroup();
					g.setId(rs.getInt("d_id"));
					g.setName(rs.getString("d_name"));
					g.setDescription(rs.getString("description"));
					y.setSmcToUserGroup(g);
				}

				list.add(y);
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return list;
	}

	/**
	 * 保存
	 * 
	 * @param entity
	 */
	public boolean put(SMCCfgStrategy entity) throws Exception {
		String sql = "insert into  SMC_Cfg_Strategy (  ID,SRC_ID	  ,LEVEL_ID,TO_USERGROUP_ID,TTL,SEND_OFFSET_TIME,SEND_TIMES,SEND_INTERVAL	  ,SEND_WAY,RESEND_WHEN_FAIL,TAKE_EFFECT ) "
				+ "values (seq_SMC_Cfg_Strategy.nextval,?,?,?,?,?,?,?,?,?,?)";

		LOG.debug("添加策略sql语句: " + sql);

		PreparedStatement ps = null;
		int result = 0;
		Connection conn = null;
		try {
			conn = DBPool.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, entity.getSrcid());
			ps.setInt(2, entity.getLevelid());
			ps.setInt(3, entity.getGroupId());
			ps.setInt(4, entity.getTtl());
			ps.setInt(5, entity.getEndoOffsetTime());
			ps.setInt(6, entity.getSendTimes());
			ps.setInt(7, entity.getSendInterval());
			ps.setInt(8, entity.getSendWay());
			ps.setInt(9, entity.getResendWhenFail());
			ps.setInt(10, entity.getTakeEffect());

			result = ps.executeUpdate();
		} finally {
			DBUtil.close(null, ps, conn);
		}
		return result > 0;
	}

	/*
	 * 删除
	 */
	public boolean delete(int key) throws Exception {
		String sql = "delete SMC_Cfg_Strategy t where t.id =" + key;
		return DBUtil.executeUpdate(sql) > 0;

	}

	@Override
	public boolean update(SMCCfgStrategy entity) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" update smc_cfg_strategy  t   ");

		sb.append(" set t.level_id = ").append(entity.getLevelid()).append("");
		sb.append(" , t.src_id = ").append(entity.getSrcid()).append("");
		sb.append(" , t.ttl = ").append(entity.getTtl()).append("");

		sb.append(" , t.send_offset_time = ")
				.append(entity.getEndoOffsetTime()).append("");

		sb.append(" , t.TO_USERGROUP_ID = ").append(entity.getGroupId())
				.append("");

		sb.append(" , t.send_interval = ").append(entity.getSendInterval())
				.append("");
		sb.append(" , t.send_times = ").append(entity.getSendTimes())
				.append("");

		sb.append(" , t.resend_when_fail = ")
				.append(entity.getResendWhenFail()).append("");

		sb.append(" , t.send_way = ").append(entity.getSendWay()).append("");
		sb.append(" , t.take_effect = ").append(entity.getTakeEffect())
				.append("");
		sb.append(" where t.id =").append(entity.getId());

		return DBUtil.executeUpdate(sb.toString()) > 0;
	}

	/**
	 * 根据id获取策略对象
	 * 
	 * @param key
	 * @return
	 */
	public SMCCfgStrategy get(int key) throws Exception {
		String sql = "select  ID,SRC_ID	  ,LEVEL_ID,TO_USERGROUP_ID,TTL,SEND_OFFSET_TIME,SEND_TIMES,SEND_INTERVAL	  ,SEND_WAY,RESEND_WHEN_FAIL,TAKE_EFFECT from SMC_Cfg_Strategy  where id = "
				+ key;
		List<SMCCfgStrategy> list = common(sql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	public SMCCfgStrategy getBySrcidLevelId(int srcid, int levelid)
			throws Exception {
		String sql = "select  ID,SRC_ID	  ,LEVEL_ID,TO_USERGROUP_ID,TTL,SEND_OFFSET_TIME,SEND_TIMES,SEND_INTERVAL	  ,SEND_WAY,RESEND_WHEN_FAIL,TAKE_EFFECT from SMC_Cfg_Strategy  where src_id = "
				+ srcid + " and level_id=" + levelid;
		List<SMCCfgStrategy> list = common(sql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	public static void main(String[] args) {
		SmcCfgStrategyDAO dao = new SmcCfgStrategyDAO();
		try {
			dao.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
