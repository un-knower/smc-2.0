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
import cn.uway.smc.db.pojo.SMCCfgSys;
import cn.uway.smc.util.Util;

/**
 * 系统配置操作
 * 
 * @since 1.0
 */
public class SmcCfgSysDAO extends AbstractDAO<SMCCfgSys> {

	private final static Logger LOG = LoggerFactory
			.getLogger(SmcCfgSysDAO.class);

	@Override
	public List<SMCCfgSys> list() throws Exception {
		String sql = " select id, smtp_host, mail_user, mail_pwd, nodeid, feetype, agentflag, morelatetomtflag, priority,"
				+ " reportflag, tp_pid, tp_udhi, messagecoding, messagetype, spnumber, chargenumber, corpid, servicetype, "
				+ "feevalue, givenvalue, expiretime, scheduletime, serveip, serveport, sms_username, sms_userpwd, "
				+ "server_receive_username, server_receive_pwd, server_receive_port, security_max_sent_count_day, security_max_sent_count_hour, ext_table_driver, ext_table_url, ext_table_user, ext_table_pwd, description, take_effect from smc_cfg_sys t  where t.take_effect =1 ";
		return common(sql);
	}

	public List<SMCCfgSys> common(String sql) throws Exception {
		List<SMCCfgSys> list = new ArrayList<SMCCfgSys>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = DBPool.getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			list = new ArrayList<SMCCfgSys>();
			while (rs.next()) {
				SMCCfgSys e = new SMCCfgSys();
				e.setId(rs.getInt("id"));
				String host = rs.getString("smtp_Host");

				e.setSmtpHost(Util.isNull(host) ? "" : host.trim());

				String emailuser = rs.getString("mail_User");
				e.setMailUser(Util.isNull(emailuser) ? "" : emailuser.trim());

				String mail_Pwd = rs.getString("mail_Pwd");

				e.setMailPwd(Util.isNull(mail_Pwd) ? "" : mail_Pwd.trim());

				e.setNodeId(rs.getInt("nodeId"));
				e.setFeeType(rs.getInt("feeType"));
				e.setAgentFlag(rs.getInt("agentFlag"));
				e.setMoRelateToMtFlag(rs.getInt("moRelateToMtFlag"));
				e.setPriority(rs.getInt("priority"));
				e.setReportFlag(rs.getInt("reportFlag"));
				e.setTpPid(rs.getInt("tp_Pid"));
				e.setTpUdhi(rs.getInt("tp_Udhi"));
				e.setMessagecoding(rs.getInt("messagecoding"));
				e.setMessageType(rs.getInt("messageType"));

				String spname = rs.getString("spNumber");

				e.setSpNumber(Util.isNull(spname) ? "" : spname.trim());
				String chargeNumber = rs.getString("chargeNumber");

				e.setChargeNumber(Util.isNull(chargeNumber) ? "" : chargeNumber
						.trim());
				String corpId = rs.getString("corpId");
				e.setCorpId(Util.isNull(corpId) ? "" : corpId);
				String serviceType = rs.getString("serviceType");

				e.setServiceType(Util.isNull(serviceType) ? "" : serviceType
						.trim());

				String feevalue = rs.getString("feeValue");
				e.setFeeValue(Util.isNull(feevalue) ? "" : feevalue.trim());

				// Util.isNull()?"":
				String givenValue = rs.getString("givenValue");
				e.setGivenValue(Util.isNull(givenValue) ? "" : givenValue
						.trim());

				String expireTime = rs.getString("expireTime");
				e.setExpireTime(Util.isNull(expireTime) ? "" : expireTime
						.trim());

				String scheduleTime = rs.getString("scheduleTime");
				e.setScheduleTime(Util.isNull(scheduleTime) ? "" : scheduleTime
						.trim());

				String serveIp = rs.getString("serveIp");
				e.setServerIp(Util.isNull(serveIp) ? "" : serveIp.trim());

				String sms_UserName = rs.getString("sms_UserName");
				e.setSmsUserName(Util.isNull(sms_UserName) ? "" : sms_UserName
						.trim());

				String sms_UserPwd = rs.getString("sms_UserPwd");
				e.setSmsUserPwd(Util.isNull(sms_UserPwd) ? "" : sms_UserPwd
						.trim());

				String description = rs.getString("description");
				e.setDescription(Util.isNull(description) ? "" : description
						.trim());

				e.setTakeEffect(rs.getInt("take_Effect"));
				e.setServerPort(rs.getInt("servePort"));
				e.setSecurityMaxSendCountDay(rs
						.getInt("security_Max_Sent_Count_Day"));
				e.setSecurityMaxSendCountHour(rs
						.getInt("security_Max_Sent_Count_Hour"));

				String ext_Table_Driver = rs.getString("ext_Table_Driver");
				e.setExtTableDriver(Util.isNull(ext_Table_Driver)
						? ""
						: ext_Table_Driver.trim());

				String ext_Table_Url = rs.getString("ext_Table_Url");
				e.setExtTableUrl(Util.isNull(ext_Table_Url)
						? ""
						: ext_Table_Url.trim());

				String ext_Table_User = rs.getString("ext_Table_User");
				e.setExtTableUser(Util.isNull(ext_Table_User)
						? ""
						: ext_Table_User.trim());

				String ext_Table_Pwd = rs.getString("ext_Table_Pwd");
				e.setExtTablePwd(Util.isNull(ext_Table_Pwd)
						? ""
						: ext_Table_Pwd.trim());

				e.setServerReceivePort(rs.getInt("server_Receive_Port"));

				String server_Receive_Pwd = rs.getString("server_Receive_Pwd");
				e.setServerReceivePwd(Util.isNull(server_Receive_Pwd)
						? ""
						: server_Receive_Pwd.trim());

				String server_Receive_UserName = rs
						.getString("server_Receive_UserName");
				e.setServerReceiveUserName(Util.isNull(server_Receive_UserName)
						? ""
						: server_Receive_UserName.trim());

				list.add(e);
			}
		} finally {
			DBUtil.close(rs, stm, conn);
		}
		return list;
	}

	public SMCCfgSys getEffectSys() throws Exception {
		String sql = " select id, smtp_host, mail_user, mail_pwd, nodeid, feetype, agentflag, morelatetomtflag, priority, reportflag, tp_pid, tp_udhi, messagecoding, messagetype, spnumber, chargenumber, corpid, servicetype, feevalue, givenvalue, expiretime, scheduletime, serveip, serveport, sms_username, sms_userpwd, server_receive_username, server_receive_pwd, server_receive_port, security_max_sent_count_day, security_max_sent_count_hour, ext_table_driver, ext_table_url, ext_table_user, ext_table_pwd, description, take_effect from smc_cfg_sys t  where t.take_effect =1 ";
		List<SMCCfgSys> list = common(sql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	public boolean update(SMCCfgSys en) throws Exception {
		StringBuffer update = new StringBuffer();
		update.append(" update smc_cfg_sys t  ");
		update.append("set smtp_host ='")
				.append(Util.isNull(en.getSmtpHost()) ? "" : en.getSmtpHost())
				.append("'");
		update.append(" ,mail_user ='")
				.append(Util.isNull(en.getMailUser()) ? "" : en.getMailUser())
				.append("'");
		update.append(" ,mail_pwd  ='")
				.append(Util.isNull(en.getMailPwd()) ? "" : en.getMailPwd())
				.append("'");
		update.append(" ,nodeid =").append(en.getNodeId());
		update.append(" ,feetype=").append(en.getFeeType());
		update.append(" ,agentflag =").append(en.getAgentFlag());
		update.append(" ,morelatetomtflag=").append(en.getMoRelateToMtFlag());
		update.append(" ,priority  =").append(en.getPriority());
		update.append(" ,reportflag=").append(en.getReportFlag());
		update.append(" ,tp_pid =").append(en.getTpPid());
		update.append(" ,tp_udhi=").append(en.getTpUdhi());
		update.append(" ,messagecoding=").append(en.getMessagecoding());
		update.append(" ,messagetype  =").append(en.getMessageType());
		update.append(" ,spnumber  ='")
				.append(Util.isNull(en.getSpNumber()) ? "" : en.getSpNumber())
				.append("'");
		update.append(" ,chargenumber ='")
				.append(Util.isNull(en.getChargeNumber()) ? "" : en
						.getChargeNumber()).append("'");
		update.append(" ,corpid ='")
				.append(Util.isNull(en.getCorpId()) ? "" : en.getCorpId())
				.append("'");
		update.append(" ,servicetype  ='")
				.append(Util.isNull(en.getServiceType()) ? "" : en
						.getServiceType()).append("'");
		update.append(" ,feevalue  ='")
				.append(Util.isNull(en.getFeeValue()) ? "" : en.getFeeValue())
				.append("'");
		update.append(" ,givenvalue='")
				.append(Util.isNull(en.getGivenValue()) ? "" : en
						.getGivenValue()).append("'");
		update.append(" ,expiretime='")
				.append(Util.isNull(en.getExpireTime()) ? "" : en
						.getExpireTime()).append("'");
		update.append(" ,scheduletime ='")
				.append(Util.isNull(en.getScheduleTime()) ? "" : en
						.getScheduleTime()).append("'");
		update.append(" ,serveip='")
				.append(Util.isNull(en.getServerIp()) ? "" : en.getServerIp())
				.append("'");
		update.append(" ,serveport =").append(en.getServerPort());
		update.append(" ,sms_username ='")
				.append(Util.isNull(en.getSmsUserName()) ? "" : en
						.getSmsUserName()).append("'");
		update.append(" ,sms_userpwd  ='")
				.append(Util.isNull(en.getSmsUserPwd()) ? "" : en
						.getSmsUserPwd()).append("'");
		update.append(" ,server_receive_username  ='")
				.append(Util.isNull(en.getServerReceiveUserName()) ? "" : en
						.getServerReceiveUserName()).append("'");
		update.append(" ,server_receive_pwd ='")
				.append(Util.isNull(en.getServerReceivePwd()) ? "" : en
						.getServerReceivePwd()).append("'");
		update.append(" ,server_receive_port=").append(
				en.getServerReceivePort());
		update.append(" ,security_max_sent_count_day =").append(
				en.getSecurityMaxSendCountDay());
		update.append(" ,security_max_sent_count_hour=").append(
				en.getSecurityMaxSendCountHour());
		update.append(" ,ext_table_driver='")
				.append(Util.isNull(en.getExtTableDriver()) ? "" : en
						.getExtTableDriver()).append("'");
		update.append(" ,ext_table_url='")
				.append(Util.isNull(en.getExtTableUrl()) ? "" : en
						.getExtTableUrl()).append("'");
		update.append(" ,ext_table_user  ='")
				.append(Util.isNull(en.getExtTableUser()) ? "" : en
						.getExtTableUser()).append("'");
		update.append(" ,ext_table_pwd='")
				.append(Util.isNull(en.getExtTablePwd()) ? "" : en
						.getExtTablePwd()).append("'");
		update.append(" ,description  ='")
				.append(Util.isNull(en.getDescription()) ? "" : en
						.getDescription()).append("'");
		update.append(" ,take_effect  =").append(en.getTakeEffect());
		update.append("where t.id =").append(en.getId());

		return DBUtil.executeUpdate(update.toString()) > 0;
	}

	public int put(SMCCfgSys en) throws Exception {
		String sql = " insert into smc_cfg_sys "
				+ "(id, smtp_host, mail_user, mail_pwd, nodeid, feetype, agentflag, morelatetomtflag, priority,"
				+ " reportflag, tp_pid, tp_udhi, messagecoding, messagetype, spnumber, chargenumber, corpid, "
				+ "servicetype, feevalue, givenvalue, expiretime, scheduletime, serveip, serveport, "
				+ "sms_username, sms_userpwd, server_receive_username, server_receive_pwd,"
				+ " server_receive_port, security_max_sent_count_day, security_max_sent_count_hour, "
				+ "ext_table_driver, ext_table_url, ext_table_user, ext_table_pwd, description, take_effect)"
				+ " values ( "
				+ en.getId()
				+ ",'"
				+ (Util.isNull(en.getSmtpHost()) ? "" : en.getSmtpHost())
				+ "','"
				+ (Util.isNull(en.getMailUser()) ? "" : en.getMailUser())
				+ "','"
				+ (Util.isNull(en.getMailPwd()) ? "" : en.getMailPwd())
				+ "',"
				+ en.getNodeId()
				+ ","
				+ en.getFeeType()
				+ ","
				+ en.getAgentFlag()
				+ ","
				+ en.getMoRelateToMtFlag()
				+ ","
				+ en.getPriority()
				+ ","
				+ en.getReportFlag()
				+ ","
				+ en.getTpPid()
				+ ","
				+ en.getTpUdhi()
				+ ","
				+ en.getMessagecoding()
				+ ","
				+ en.getMessageType()
				+ ","
				+ (Util.isNull(en.getSpNumber()) ? "" : en.getSpNumber())
				+ ","
				+ "'"
				+ (Util.isNull(en.getChargeNumber()) ? "" : en
						.getChargeNumber())
				+ "','"
				+ (Util.isNull(en.getCorpId()) ? "" : en.getCorpId())
				+ "','"
				+ (Util.isNull(en.getServiceType()) ? "" : en.getServiceType())
				+ "','"
				+ (Util.isNull(en.getFeeValue()) ? "" : en.getFeeValue())
				+ "','"
				+ (Util.isNull(en.getGivenValue()) ? "" : en.getGivenValue())
				+ "','"
				+ (Util.isNull(en.getExpireTime()) ? null : en.getExpireTime())
				+ "','"
				+ (Util.isNull(en.getScheduleTime()) ? null : en
						.getScheduleTime())
				+ "','"
				+ (Util.isNull(en.getServerIp()) ? null : en.getServerIp())
				+ "',"
				+ en.getServerPort()
				+ ","
				+ "'"
				+ (Util.isNull(en.getSmsUserName()) ? null : en
						.getSmsUserName())
				+ "','"
				+ (Util.isNull(en.getSmsUserPwd()) ? null : en.getSmsUserPwd())
				+ "','"
				+ (Util.isNull(en.getServerReceiveUserName()) ? null : en
						.getServerReceiveUserName())
				+ "','"
				+ (Util.isNull(en.getServerReceivePwd()) ? null : en
						.getServerReceivePwd())
				+ "',"
				+ en.getServerReceivePort()
				+ ","
				+ en.getSecurityMaxSendCountDay()
				+ ","
				+ en.getSecurityMaxSendCountHour()
				+ ","
				+ (Util.isNull(en.getExtTableDriver()) ? null : en
						.getExtTableDriver())
				+ ","
				+ (Util.isNull(en.getExtTableUrl()) ? null : en
						.getExtTableUrl())
				+ ","
				+ (Util.isNull(en.getExtTableUser()) ? null : en
						.getExtTableUser())
				+ ",'"
				+ (Util.isNull(en.getExtTablePwd()) ? null : en
						.getExtTablePwd())
				+ "','"
				+ (Util.isNull(en.getDescription()) ? null : en
						.getDescription()) + "','" + en.getTakeEffect() + "')";
		System.out.println(sql);
		return DBUtil.executeUpdate(sql);
	}

	public boolean delete(int key) throws Exception {
		String sql = "delete smc_cfg_sys t where t.id =" + key;
		return DBUtil.executeUpdate(sql) > 0;

	}

	public SMCCfgSys getById(int key) throws Exception {
		String sql = "select * from smc_cfg_sys t where t.id =" + key;
		List<SMCCfgSys> list = common(sql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	public boolean validateUser(String username, String pwd) {
		try {
			List<SMCCfgSys> list = list();
			if (list == null || list.size() <= 0)
				return false;
			List<SMCCfgSys> result = new ArrayList<SMCCfgSys>();
			for (SMCCfgSys sys : list) {
				if (sys.getSmsUserName().equalsIgnoreCase(username)
						&& sys.getSmsUserPwd().equalsIgnoreCase(pwd))
					result.add(sys);
			}
			return result.size() > 0;
		} catch (Exception e) {
			LOG.error("验证系统配置用户名失败", e);
		}
		return false;
	}

	public static void main(String[] args) {
		SmcCfgSysDAO dao = new SmcCfgSysDAO();
		try {
			SMCCfgSys sys = new SMCCfgSys();
			sys.setId(1);
			sys.setTakeEffect(1);
			dao.put(sys);
			dao.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
