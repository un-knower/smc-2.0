package cn.uway.smc.businesses;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.DateUtil;
import cn.uway.commons.type.StringUtil;
import cn.uway.smc.db.conn.DBUtil;
import cn.uway.smc.db.dao.SmcDataDAO;
import cn.uway.smc.db.dao.SmcDataHistoryDAO;
import cn.uway.smc.db.pojo.SMCCfgSource;
import cn.uway.smc.db.pojo.SMCCfgStrategy;
import cn.uway.smc.db.pojo.SMCCfgSys;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.db.pojo.SMCDataHistory;
import cn.uway.smc.util.ConstDef;
import cn.uway.smc.util.MD5;
import cn.uway.smc.util.TimeIntervalMgr;

public class BussinessMgr {

	private final static Logger LOG = LoggerFactory
			.getLogger(BussinessMgr.class);

	/** 此消息是否可以发送 */
	public static boolean readyToSend(Date now, SMCData smcData) {
		boolean flag = false;

		if (StringUtil.isNull(smcData.getSendTime())) {
			return true;
		}

		SMCCfgStrategy strategy = smcData.getStrategy();

		int sentOKTimes = smcData.getSentOkTimes();
		// 如果发送次数达到策略要求的发送次数，那么就不再发送此条消息
		if (sentOKTimes >= strategy.getSendTimes() || sentOKTimes < 0) {
            sendAfter(TaskMgr.getInstance().getSys(), smcData, -3, "达到最大发送次数. ");
         	TaskMgr.getInstance().delActiveTask(smcData.getId());
			return flag;
		}
		int endOffsetTime = strategy.getEndoOffsetTime();
		int interval = strategy.getSendInterval();
		try {
			long sendTime = 0;
			if (sentOKTimes >= 1) {
				sendTime = DateUtil.getDate(smcData.getSendTime()).getTime()
						+ (endOffsetTime + interval * (sentOKTimes + 1)) * 60
						* 1000;
			} else {
				sendTime = DateUtil.getDate(smcData.getSendTime()).getTime();
			}

			// 数据库的时间大于短信发送的时间
			if (now.getTime() >= sendTime)
				return true;
		} catch (ParseException e) {
			LOG.error(smcData + " 解析时间失败.", e);
		}
		return flag;
	}

	/**
	 * 检查信息是否有效
	 * 
	 * @param ip
	 *            ip地址
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param type
	 *            类型
	 * @param src_id
	 *            消息源编号
	 * @param level
	 *            级别
	 * @param to_users
	 *            接收人
	 * @param send_way
	 *            发送方式
	 * @param content
	 *            内容
	 * @param send_time
	 *            发送时间
	 * @return
	 */
	public static int checkInfoValidate(String ip, String username,
			String password, int type, int src_id, int level, String to_users,
			int send_way, String content, String send_time) {
		// 检查参数是否符合接口规范
		if (type != 1 && type != 2) {
			LOG.error(ip + " type 取值范围为1或者2，无效的信息传递类型 " + type);
			return -1;
		}

		if (send_way != 1 && send_way != 2 && send_way != 3) {
			LOG.error(ip + " send_way 取值范围为1或者2或者3，无效的信息传递类型 " + send_way);
			return -1;
		}

		if (StringUtil.isNull(to_users)) {
			LOG.error(ip + " to_users为空 " + to_users);
			return -1;
		}

		if (StringUtil.isNull(content)) {
			LOG.error(ip + " 内容为空 " + content);
			return -1;
		}

		if (content.length() > 4000) {
			LOG.error(ip + " 内容长度大于4000 " + content);
			return -1;
		}

		if (StringUtil.isNull(username)) {
			LOG.error(ip + " 用户名为空 " + username);
			return -1;
		}

		if (StringUtil.isNull(password)) {
			LOG.error(ip + " 用户密码为空 " + password);
			return -1;
		}

		return 0;
	}

	/**
	 * 发送短信或邮件之后的善后处理
	 * 
	 * @param sys
	 *            系统配置
	 * @param smcData
	 *            消息实体类
	 * @param result
	 *            是否成功0成功 非0失败
	 * @param cause
	 *            失败原因
	 */
	public static void sendAfter(SMCCfgSys sys, SMCData smcData, int result,
			String cause) {
		SmcDataHistoryDAO history = new SmcDataHistoryDAO();
		SMCDataHistory entity = new SMCDataHistory();
		try {
			entity.setId(smcData.getId());
			entity.setSrcId(smcData.getSrcid());
			entity.setCause(cause);
			entity.setContent(smcData.getContent());
			entity.setLevelId(smcData.getLevelid());
			entity.setOccurTime(smcData.getOccurTime());
			entity.setSendTime(smcData.getSendTime());
			entity.setSendTimeExclude(smcData.getSendTimeExclude());
			entity.setSendWay(smcData.getSendWay());
			entity.setSentResult(result);
			Date now = TimeIntervalMgr.getInstance().getDateValue(new Date());
			entity.setStampTime(now);
			entity.setToUsers(smcData.getToUsers());
			entity.setAttachmentfile(smcData.getAttachmentfile());
			entity.setCounter(smcData.getSentOkTimes() + 1);

			SmcDataDAO expressDao = new SmcDataDAO();
			// 成功则从数据库快递表中删除
			if (result == ConstDef.SENDERSUC
					|| result == ConstDef.SENDEREXPIRED) {
				SMCCfgStrategy stra = smcData.getStrategy();
				boolean b = false;
				int time = 0;
				time = stra.getSendTimes();

				// 是否达到最大发送次数
				if (smcData.getSentOkTimes() + 1 >= time && time != 0) {
					boolean flag = expressDao.delete(smcData.getId());
					LOG.info(smcData + "从快递表中删除" + (flag == true ? "成功" : "失败"));
				} else {

					if (time != 0 && smcData.getSentOkTimes() < time) {
						b = updateExpressSentTimes(smcData);
						LOG.info(smcData + "从快递表中更新发送次数"
								+ (b == true ? "成功" : "失败"));

					} else {
						boolean flag = expressDao.delete(smcData.getId());
						LOG.info(smcData + "从快递表中删除"
								+ (flag == true ? "成功" : "失败"));
					}
				}

			} else if (result == ConstDef.SENDMAXTIMES) {
				boolean flag = expressDao.delete(smcData.getId());
				LOG.info(smcData + " 达到发送最大次数，从快递表中删除"
						+ (flag == true ? "成功" : "失败"));
			} else if (result == ConstDef.EXCEPTION) {
				updateExpressSentTimes(smcData);
			}
			else if (result == ConstDef.USERERROR) {
				 expressDao.delete(smcData.getId());
			}
			else if (result == ConstDef.EMAILADDRESS_ERROR) {
				 expressDao.delete(smcData.getId());
			}
			

		} catch (Exception e) {
			LOG.error("添加到快递历史表失败", e);
		} finally {
			int i = 0;
			try {
				i = history.add(entity);
			} catch (Exception e) {

			}
			LOG.info(smcData + " 已添加到快递历史表,状态: " + (i >= 1));
		}
	}

	public static int checkBussiness(SMCCfgStrategy strategy, String ip,
			String username, String password, int type, int src_id, int level,
			String to_users, int send_way, String content, String send_time) {

		if (strategy == null) {
			LOG.error(ip + "无此消息策略，请在业务页面配置配置相关的策略 ,消息源(src_id): " + src_id
					+ " 消息级别(level_id) : " + level);
			return -1;
		}
		SMCCfgSource source = strategy.getSmcSource();

		boolean sourceFlag = false;

		// 检查 消息来源编号，消息级别编号，是否合法
		if (source.getUser().equalsIgnoreCase(username)
				&& MD5.getMD5(source.getPwd().getBytes()).equalsIgnoreCase(
						password)) {
			sourceFlag = true;
		}
		if (!sourceFlag) {
			LOG.error(ip + "客户端用户名或密码错误");
			return -1;
		}

		if (!source.checkIp(ip)) {
			LOG.debug(ip + "不具有权限，请在业务配置消息源中配置IP访问权限");
			return -1;
		}
		return 0;
	}

	public static boolean updateExpressSentTimes(SMCData smcExpressData) {
		try {
			SmcDataDAO dao = new SmcDataDAO();
			smcExpressData.setSentOkTimes(smcExpressData.getSentOkTimes() + 1);
			return dao.update(smcExpressData);
		} catch (Exception e) {
			LOG.debug(smcExpressData + " 更新失败.");
		}
		return false;
	}

	public static void receiveAfter(SMCData smcExpressData, int result,
			String cause) {

		SmcDataHistoryDAO history = new SmcDataHistoryDAO();
		SMCDataHistory entity = new SMCDataHistory();
		int id = 0;
		try {
			id = DBUtil.getSeq("SEQ_EXPRESS");
		} catch (Exception e1) {
			LOG.error("系统自动恢复 ，获取快递序列失败   :" + id, e1);
		}
		entity.setId(id);
		entity.setSrcId(smcExpressData.getSrcid());
		entity.setCause(cause);
		entity.setContent(smcExpressData.getContent());
		entity.setLevelId(smcExpressData.getLevelid());
		entity.setOccurTime(smcExpressData.getOccurTime());
		entity.setSendTime(null);
		entity.setSendTimeExclude(smcExpressData.getSendTimeExclude());
		entity.setSendWay(smcExpressData.getSendWay());
		entity.setSentResult(result);

		Date now = TimeIntervalMgr.getInstance().getDateValue(new Date());
		entity.setStampTime(now);

		entity.setToUsers(smcExpressData.getToUsers());

		entity.setAttachmentfile(smcExpressData.getAttachmentfile());

		entity.setCounter(smcExpressData.getSentOkTimes() + 1);
		try {
			SmcDataDAO expressDao = new SmcDataDAO();
			expressDao.delete(smcExpressData.getId());
			LOG.debug(smcExpressData.getId() + "已经从快递表中删除");
			int i = history.add(entity);
			LOG.debug(smcExpressData.getId() + "已添加到快递历史表 " + i + "  "
					+ DateUtil.getDateString_yyyyMMddHHmmssSSS(new Date()));

		} catch (Exception e) {
			LOG.error(id + " :  系统自动恢复,添加到快递历史表失败", e);
		}
	}

	// 通过正则表达式查找
	public static String findByRegex(String str, String regEx, int group) {
		String resultValue = null;
		if (regEx == null || (regEx != null && "".equals(regEx.trim()))) {
			return resultValue;
		}
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);

		boolean result = m.find();// 查找是否有匹配的结果
		if (result) {
			resultValue = m.group(group);// 找出匹配的结果
		}
		return resultValue;
	}

	public static void main(String[] args) {
		try {
			SmcDataDAO dDAO = new SmcDataDAO();
			SMCData smcExpressData = new SMCData();
			List<SMCData> list = null;

			list = dDAO.listAll();
			smcExpressData = list.get(0);
			smcExpressData.setSentOkTimes(smcExpressData.getSentOkTimes() + 1);
			dDAO.update(smcExpressData);
		} catch (Exception e) {
			LOG.debug(" 更新失败.");
		}
	}

	/* 16进制输出byte[] */// 例如 byte[0] = 1 byte[1] = 2 ----> 0x3132
	public static String rhex(byte[] in) {
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(in));
		String str = "0x";
		try {
			for (int j = 0; j < in.length; j++) {
				String tmp = Integer.toHexString(data.readUnsignedByte());
				if (tmp.length() == 1) {
					tmp = "0" + tmp;
				}
				str = str + tmp;
			}
		} catch (Exception ex) {
		}
		return str;
	}
}
