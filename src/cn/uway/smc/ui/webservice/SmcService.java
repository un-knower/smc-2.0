package cn.uway.smc.ui.webservice;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.StringUtil;
import cn.uway.smc.businesses.BussinessMgr;
import cn.uway.smc.db.conn.DBUtil;
import cn.uway.smc.db.dao.SmcCfgStrategyDAO;
import cn.uway.smc.db.dao.SmcDataDAO;
import cn.uway.smc.db.pojo.SMCCfgLevel;
import cn.uway.smc.db.pojo.SMCCfgSource;
import cn.uway.smc.db.pojo.SMCCfgStrategy;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.util.ConstDef;
import cn.uway.smc.util.SysCfg;
import cn.uway.smc.util.TimeIntervalMgr;

/**
 * 短消息投递接口，WEBSERVICE 接口，供外部程序调用
 * URL:http://localhost:8081/services/SmcService?wsdl
 * 
 * @author liuwx
 */
public class SmcService {

	private final static Logger LOG = LoggerFactory.getLogger(SmcService.class);

	/**
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param type
	 *            type值定义如下： 1:一般短消息发送 2: 快递短消息发送
	 * @param src_id
	 *            业务类型编码
	 * @param level
	 *            消息级别等级，越小越紧急
	 * @param to_users
	 *            手机号码或email地址，多个使用逗号分隔；(最大长度500) <PHONE>137342;1043233</PHONE>
	 *            <EMAIL>fdsa@uway.cn;</EMAIL>
	 * @param send_way
	 *            发送方式，1为短信、2为邮件, 3短信和邮件，默认值为1；
	 * @param content
	 *            消息具体内容 (最大长度800)
	 * @param send_time
	 *            消息发送时间,如果为空则立即发送,否则按照指定时间进行发送.默认为空;yyyy-MM-dd HH24:mm:ss
	 * @return
	 */
	public int deliver(String username, String password, int type, int src_id,
			int level, String to_users, int send_way, String content,
			String send_time) {
		try {
			// 获得客户端IP,确定ip是指是否具有权限
			MessageContext mc = MessageContext.getCurrentContext();
			HttpServletRequest request = (HttpServletRequest) mc
					.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
			String ip = request.getRemoteAddr();

			String logmsg = "远程消息： 主机: " + ip + ":" + request.getRemotePort()
					+ "" + "用户名(username)" + username + " 密码(password) "
					+ password + " 服务类型(type): " + type + " 消息源(src_id) "
					+ src_id + " 消息级别(level): " + level + " 接收用户(to_users):"
					+ to_users + " 发送方式(send_way):  " + send_way + " 发送内容： "
					+ content;
			
			

			LOG.debug(logmsg);
			int checkResult = BussinessMgr.checkInfoValidate(ip, username,
					password, type, src_id, level, to_users, send_way, content,
					send_time);
			
			
			if (checkResult < 0)
				return checkResult;
			
			to_users =to_users.replace(",",";");
			
			content=content.replaceAll("\r", "").replaceAll("\n", "").replaceAll(" ", "");

			SmcCfgStrategyDAO dao = new SmcCfgStrategyDAO();
			SMCCfgStrategy strategy = null;
			try {
				strategy = dao.chechStrategy(src_id, level);
			} catch (Exception e1) {
				LOG.error(ip + "检验消息策略出现异常，请工程人员检验策略: src_id: " + src_id
						+ " level: " + level);
				return -1;
			}

			int check = BussinessMgr.checkBussiness(strategy, ip, username,
					password, type, src_id, level, to_users, send_way, content,
					send_time);
			if (check < 0)
				return check;

			LOG.info(ip + "  构件消息以及邮件");
			// 获得电话号码，以及邮件，构件消息以及邮件
			SMCData expressData = new SMCData();
			expressData.setLevelid(level);
			expressData.setSrcid(src_id);

			Date sysdate = new Date();
			Date curDbdate = TimeIntervalMgr.getInstance()
					.getDateValue(sysdate);

			expressData.setOccurTime(curDbdate);

			expressData.setToUsers(to_users);
			expressData.setSendWay(send_way);

			expressData.setSmcLevel(strategy.getSmcLevel());
			expressData.setContent(content);
			expressData.setSendTime(StringUtil.isNull(send_time)
					? null
					: send_time);

			expressData.setUsername(username);
			expressData.setPassword(password);

			expressData.setSubject("");

			expressData.setType(type);

			expressData.setStrategy(strategy);
			expressData.setSmcSource(strategy.getSmcSource());
			try {
				expressData.setId(DBUtil.getSeq(ConstDef.SEQ_EXPRESS));

			} catch (Exception e) {
				LOG.error("获取序列失败", e);
			}

			try {

				SmcDataDAO dataDao = new SmcDataDAO();
				dataDao.add(expressData);

			} catch (Exception e1) {
				LOG.error(ip + ": 出现异常.  ", e1);

				return -1;
			}

			LOG.debug(ip + ": 客户端调用成功.  ");
		} catch (Throwable e) {
			LOG.error("调用deliver 服务出现异常.  ", e);

		}
		return 0;
	}

	/**
	 * 邮件接口
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param src_id
	 *            业务类型编码
	 * @param level
	 *            消息级别等级，越小越紧急
	 * @param to_users
	 *            手机号码或email地址，多个使用逗号分隔；(最大长度500)，邮件默认为格式为 <EMAIL><to>a@uway.
	 *            cn;b@uway.cn</to></EMAIL>等价<EMAIL>a@uway.cn;b@uway.cn</EMAIL>,
	 *            to:收件人 . cc: 抄送 .bcc密送 <PHONE>137342;1043233</PHONE>
	 *            <EMAIL><to>a@uway.cn</to><cc></cc><bcc></bcc></EMAIL>
	 * @param content
	 *            消息具体内容 (最大长度800)
	 * @param send_time
	 *            消息发送时间,如果为空则立即发送,否则按照指定时间进行发送.默认为空;yyyy-MM-dd HH24:mm:ss
	 * @param title
	 *            邮件标题
	 * @param attachmentfile
	 *            附件数组
	 * @param attachment
	 *            附件名
	 * @param fos
	 * @return
	 */
	public int deliverEmail(String username, String password, int src_id,
			int level, String to_users, String content, String send_time,
			String title, byte[] attachmentfile, String attachment) {
		// 获得客户端IP,确定ip是指是否具有权限
		MessageContext mc = MessageContext.getCurrentContext();
		HttpServletRequest request = (HttpServletRequest) mc
				.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);

		String ip = request.getRemoteAddr();

		String logmsg = "远程消息： 主机: " + ip + ":" + request.getRemotePort() + ""
				+ " 用户名(username) " + username + " 密码(password)" + password
				+ " type " + 2 + " 消息源(src_id) " + src_id + " level " + level
				+ " 接收用户(to_users):" + to_users + " 发送方式(send_way):  " + 2
				+ " 发送内容： " + content;

		LOG.debug(logmsg);

		int checkResult = BussinessMgr.checkInfoValidate(ip, username,
				password, 2, src_id, level, to_users, 2, content, send_time);
		if (checkResult < 0)
			return checkResult;
		
		to_users =to_users.replace(",",";");

		SmcCfgStrategyDAO dao = new SmcCfgStrategyDAO();
		SMCCfgStrategy strategy = null;
		try {
			strategy = dao.chechStrategy(src_id, level);
		} catch (Exception e1) {
			LOG.error(ip + "检验消息策略出现异常，请工程人员检验策略: src_id: " + src_id
					+ " level: " + level);
			return -1;
		}

		int type = 2;
		int sendtype = 2;

		int check = BussinessMgr.checkBussiness(strategy, ip, username,
				password, type, src_id, level, to_users, sendtype, content,
				send_time);
		if (check < 0)
			return check;
		ip = ip.trim();

		// 获得电话号码，以及邮件，构件消息以及邮件

		SMCData expressData = new SMCData();
		expressData.setLevelid(level);
		expressData.setSrcid(src_id);
		Date sysdate = new Date();

		Date curDbdate = TimeIntervalMgr.getInstance().getDateValue(sysdate);

		expressData.setOccurTime(curDbdate);

		expressData.setToUsers(to_users);
		expressData.setSendWay(sendtype);// 以邮件发送
		expressData.setType(type);
		SMCCfgLevel lev = new SMCCfgLevel();
		lev.setId(level);
		expressData.setSmcLevel(lev);
		expressData.setContent(content);
		expressData
				.setSendTime(StringUtil.isNull(send_time) ? null : send_time);

		strategy.setLevelid(level);
		strategy.setSrcid(src_id);
		expressData.setStrategy(strategy);
		SMCCfgSource source = new SMCCfgSource();
		source.setSrcid(src_id);
		expressData.setSmcSource(source);

		expressData.setSubject(title);

		String fullPath = SysCfg.getInstance().getEmaildirectory() + attachment;
		File file = new File(fullPath);
		int count = 0;

		if (attachmentfile != null) {
			InputStream input = null;
			FileOutputStream fos = null;
			try {
				input = new ByteArrayInputStream(attachmentfile);
				fos = new FileOutputStream(file);
				byte[] buffer = new byte[8 * 1024];
				while ((count = input.read(buffer)) != -1) {
					fos.write(buffer, 0, count);
					fos.flush();
				}

			} catch (IOException e) {
				LOG.error("邮件投递接口失败,原因:{} ", e);
			} finally {
				try {
					if (input != null)
						input.close();
					if (fos != null)
						fos.close();
				} catch (IOException e) {
				}

			}
		}
		if (StringUtil.isNotNull(attachment))
			expressData.setAttachmentfile(fullPath);

		try {
			expressData.setId(DBUtil.getSeq(ConstDef.SEQ_EXPRESS));

		} catch (Exception e) {
			LOG.error("获取序列失败", e);
		}

		try {
			SmcDataDAO dataDao = new SmcDataDAO();
			dataDao.add(expressData);

		} catch (Exception e1) {
			LOG.error(ip + ": 出现异常.  ", e1);

			return -1;
		}

		LOG.debug(ip + ": 客户端调用成功.  ");
		return 0;
	}

	public static void main(String[] args) {

	}
}
