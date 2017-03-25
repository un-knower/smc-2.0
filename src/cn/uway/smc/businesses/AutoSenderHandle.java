package cn.uway.smc.businesses;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.StringUtil;
import cn.uway.smc.db.conn.DBUtil;
import cn.uway.smc.db.dao.SmcCfgForbidPhoneDAO;
import cn.uway.smc.db.dao.SmcCfgToUserDAO;
import cn.uway.smc.db.dao.SmcDataReceiveDAO;
import cn.uway.smc.db.dao.SmcDataReceiveHistoryDAO;
import cn.uway.smc.db.pojo.SMCCfgToUser;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.db.pojo.SMCDataReceive;
import cn.uway.smc.db.pojo.SMCDataReceiveHistory;
import cn.uway.smc.receiver.ReceiveMgr;
import cn.uway.smc.util.ConstDef;
import cn.uway.smc.util.TimeIntervalMgr;

/**
 * SMC 接收短信息后自动恢复管理
 */
public class AutoSenderHandle
// implements Runnable
{

	private final static Logger LOG = LoggerFactory
			.getLogger(AutoSenderHandle.class);

	private SMCData smcData;

	/**
	 * 是否是从短信网关接收的短信息
	 */
	private boolean isReceiveGw = false;

	public AutoSenderHandle(SMCData SMCExpressData) {
		this.smcData = SMCExpressData;
	}

	public void handle() {
		// 是否是接收来自用户短信息
		if (smcData.getIsReceiveGw() == 1) {
			isReceiveGw = true;
			LOG.debug(smcData + ": 解析短信息信息,号码：" + smcData.getToUsers());
			SMCDataReceive receive = parse();
			// 入库
			save(receive);
			// 发送
			if (StringUtil.isNotNull(smcData.getContent())) {
				smcData.setToUsers(ConstDef.PHONEFLAGBEGIN
						+ smcData.getToUsers() + ConstDef.PHONEFLAGEND);
				send();
			}
		} else {
			// 系统自身告警短信通知
			smcData.setToUsers(ConstDef.PHONEFLAGBEGIN + smcData.getToUsers()
					+ ConstDef.PHONEFLAGEND);
			send();
		}

	}

	public void run() {
		handle();
	}

	/**
	 * 发送短信息
	 */
	private void send() {
		if (!isReceiveGw)
			builderSMCExpress(smcData.getContent());
		SystemMgr.getInstance().sender(smcData);

	}

	/**
	 * 保存到数据库
	 * 
	 * @param receive
	 */
	private void save(SMCDataReceive receive) {
		try {
			LOG.debug("业务号码:" + receive.getBusinessId() + "是否有效:"
					+ (receive.getIseffect() == 1 ? true : false)
					+ " 是否是帮助信息： " + receive.isHelpInfo());

			if (receive.getIseffect() == 1 && !receive.isHelpInfo()) {
				SmcDataReceiveDAO dao = new SmcDataReceiveDAO();
				dao.add(receive);
			} else {
				SmcDataReceiveHistoryDAO dao = new SmcDataReceiveHistoryDAO();
				SMCDataReceiveHistory history = new SMCDataReceiveHistory();
				history.setId(receive.getId());
				history.setBusinessId(receive.getBusinessId());
				history.setContent(receive.getContent());
				history.setFromUser(receive.getFromUser());
				history.setIseffect(receive.getIseffect());
				history.setSpNumber(receive.getSpNumber());
				// history.setResult(1);
				if (receive.getIseffect() == 0)
					history.setRemark("业务代码无效");
				dao.add(history);
			}
		} catch (Exception e) {
			LOG.error("添加短息入库失败", e);
		}
	}

	/**
	 * 是否需要回复,根据配置来确定是否需要回复
	 * 
	 * @param it
	 * @param bussniessId
	 * @param result
	 * @param sendMessage
	 */
	public void isReback(Iterator<SMCQueryReceive> it, int bussniessId,
			String result, StringBuilder sendMessage) {

		boolean isReback = false;
		while (it.hasNext()) {
			SMCQueryReceive rec = it.next();

			if ((bussniessId & rec.getBusinessid()) == bussniessId
					&& result.trim().equals(
							StringUtil.isNull(rec.getSendSMCContent())
									? ""
									: rec.getSendSMCContent().trim())) {
				sendMessage.append(rec.getSendSMCContent()).append(":")
						.append(rec.getContentexplanAtion()).append(";");
			}
			if (result.trim().equals(rec.getSendSMCContent())
					&& rec.getIsNeedRebackSMS() == 1) {
				isReback = true;
			}
		}
		// 如果需要回复，则设置需要回复的短信息内容
		if (isReback)
			smcData.setContent(sendMessage.toString());
	}

	/*
	 * 2100&2101=2100 // 0000#1 // 0000&2100=0000 // 2100#？ *
	 * 解析接收短信息,如果是短息接收，恢复指令，以及短信息帮助指令则需要作特殊处理
	 * 
	 * @return 接收数据对象
	 */

	private SMCDataReceive parse() {

		String srcNumber = smcData.getToUsers();
		String msgContent = smcData.getSubject();

		// 判断接收短信是否为空，如果为空，则此短息无效，并将此短信息标志设置为无效状态。
		boolean b = StringUtil.isNull(msgContent);
		SMCDataReceive receive = new SMCDataReceive();
		receive.setIseffect(0);
		if (b) {
			receive.setBusinessId(-1);
			receive.setContent("");
		} else {
			msgContent = msgContent.trim();

			// 根据接收内容判断是否是接收指令
			String head = null;
			String busId = null;
			if (msgContent.length() > 10)
				head = msgContent.substring(0, 10);
			else
				head = msgContent;

			String result = BussinessMgr.findByRegex(head, ConstDef.FLAGREGEX,
					0);

			// 是否是帮助短信息
			boolean bb = StringUtil.isNull(result);
			LOG.debug("是否是帮助信息： " + bb);
			if (!bb) {
				busId = result.substring(0, 4);
				if ((busId.equals("0000") || busId.equals("2100") || ReceiveMgr
						.getInstance().getSourceList().contains(busId))) {
					receive.setIseffect(1);
					receive.setHelpInfo(true);

					List<SMCQueryReceive> queryReceiveList = AutoSenderMgr.QUERYRECEIVELIST;
					Iterator<SMCQueryReceive> it = queryReceiveList.iterator();

					SmcCfgForbidPhoneDAO dao = new SmcCfgForbidPhoneDAO();

					StringBuilder sendMessage = new StringBuilder();
					String res[] = result.split(ConstDef.FLAG);
					int tempBusid = Integer.parseInt(res[0]);
					String type = res[1];
					if (type.equals("?")) {
						// 构建快递帮助短信息，并发送
						while (it.hasNext()) {
							SMCQueryReceive rec = it.next();
							if ((tempBusid & rec.getBusinessid()) == tempBusid
									&& (tempBusid == rec.getBusinessid())) {
								sendMessage.append(rec.getSendSMCContent())
										.append(":")
										.append(rec.getContentexplanAtion())
										.append(";");
							}
						}
						smcData.setContent(sendMessage.toString());

						LOG.debug("当前接收短信内容: " + sendMessage.toString());

					} else if (type.equals("1")) {
						try {
							// 禁止接受短信息
							synchronized (ForbidphoneMgr.FORBIDMAP) {

								if ("0000".equals(busId)) {
									forbid(dao, srcNumber);
									LOG.debug(srcNumber + " ,业务编号 =[" + busId
											+ "], 停止所有短信通知.");
								} else if ("2100".equals(busId)) {
									forbid(dao, srcNumber);
									LOG.debug(srcNumber
											+ " ,业务编号 =["
											+ busId
											+ "], 停止一切属于本人短信通知(性能告警,硬件告警，工单催办)等.");
								} else if (!ForbidphoneMgr.FORBIDMAP
										.containsKey(srcNumber)) {
									ForbidPhone fPhone = new ForbidPhone(
											srcNumber, tempBusid);
									dao.add(fPhone);
									ForbidphoneMgr.getInstance().add(fPhone);

									LOG.debug(srcNumber + " ,业务编号 =[" + busId
											+ "], 禁止接收短信息成功");

								}
							}
						} catch (Exception e) {
							LOG.error(srcNumber + " 禁用个人业务{}接收短信失败，原因：{}",
									busId, e);
						}
						// 是否需要回复短信息,此字段值是根据
						// smc_query_receive表中的isneedrebacksms字段来决定是否自动回复短信息内容
						isReback(it, tempBusid, result, sendMessage);
						LOG.debug(sendMessage.toString());

					} else if (type.equals("2")) {
						try {
							// 恢复接受短信息
							synchronized (ForbidphoneMgr.FORBIDMAP) {
								if ("0000".equals(busId)) {
									List<String> l = AutoSenderMgr
											.getInstance()
											.regexFind(
													ConstDef.FLAGREGEXRECOVERYALL,
													1);
									dao.deleteList(ForbidphoneMgr.getInstance()
											.builderList(srcNumber, l));
									LOG.debug(srcNumber + " ,业务编号 =[" + busId
											+ "], 恢复所有短信通知");

									ForbidphoneMgr.getInstance().remove(
											new ForbidPhone(srcNumber, Integer
													.parseInt(busId)));
								} else if ("2100".equals(busId)) {
									List<String> l = AutoSenderMgr
											.getInstance().regexFind(
													ConstDef.FLAGREGEXRECOVERY,
													1);
									dao.deleteList(ForbidphoneMgr.getInstance()
											.builderList(srcNumber, l));
									LOG.debug(srcNumber
											+ " ,业务编号 =["
											+ busId
											+ "], 恢复一切属于本人短信通知(性能告警,硬件告警，工单催办)等.");
									ForbidphoneMgr.getInstance().remove(busId);
								} else {
									ForbidphoneMgr.getInstance().remove(busId);
									int delFlag = dao.delete(srcNumber,
											tempBusid);
									if (delFlag >= 1)
										LOG.debug(srcNumber + " 业务编号 = ["
												+ tempBusid + "], 恢复接受短信息成功");

								}

							}
						} catch (Exception e) {
							LOG.error(srcNumber + " 恢复个人业务{}接收短信失败，原因：{}",
									busId, e);
						}
						isReback(it, tempBusid, result, sendMessage);
					}
				}
			} else {
				if (msgContent.length() >= 4) {
					// 判断接受的短信前四个字符是否是有效的业务编号
					String bid = BussinessMgr.findByRegex(
							msgContent.substring(0, 4),
							ConstDef.ISBUSINESSIDREGEX, 0);
					LOG.debug("业务编号:" + bid);
					if (!StringUtil.isNull(bid)) {
						busId = bid;
					}

				}
				receive.setIseffect(1);
				receive.setHelpInfo(false);
			}
			receive.setIsParseOk(0);
			receive.setContent(msgContent);
			receive.setSpNumber(smcData.getSpNumber());
			// 检查此业务代码是否有效
			receive.setBusinessId(StringUtil.isNull(busId) ? -1 : Integer
					.valueOf(busId));

		}
		receive.setFromUser(srcNumber);
		receive.setId(smcData.getId());
		return receive;
	}

	public void forbid(SmcCfgForbidPhoneDAO dao, String srcNumber)
			throws Exception {
		List<String> list = AutoSenderMgr.getInstance().regexFind(
				ConstDef.FLAGREGEXFORBID, 1);
		dao.addList(ForbidphoneMgr.getInstance().builderList(srcNumber, list));
		ForbidphoneMgr.getInstance().addList(srcNumber, list);
	}

	/**
	 * 构建快递短信息
	 * 
	 * @param content
	 */
	private void builderSMCExpress(String content) {
		smcData = new SMCData();
		SmcCfgToUserDAO userDao = new SmcCfgToUserDAO();
		List<SMCCfgToUser> userList = null;
		StringBuilder phones = new StringBuilder(ConstDef.PHONEFLAGBEGIN);
		StringBuilder emails = new StringBuilder(ConstDef.EMAILFLAGBEGIN);
		try {
			userList = userDao.list();
			for (SMCCfgToUser user : userList) {
				if (user.getName() != null && !user.getName().trim().equals(""))
					phones.append(user.getCellphone()).append(ConstDef.SPLIT);
				if (user.getEmail() != null
						&& !user.getEmail().trim().equals(""))
					emails.append(user.getEmail()).append(ConstDef.SPLIT);
			}
			phones.append(ConstDef.PHONEFLAGEND);
			emails.append(ConstDef.EMAILFLAGEND);
			smcData.setToUsers(phones.toString() + "" + emails.toString());
			smcData.setSendWay(1);
			smcData.setId(DBUtil.getSeq(ConstDef.SEQ_EXPRESS));
			smcData.setLevelid(1);
			smcData.setSrcid(1);

			Date now = TimeIntervalMgr.getInstance().getDateValue(new Date());
			smcData.setOccurTime(now);
			smcData.setSentOkTimes(0);
			smcData.setSubject(ConstDef.SMCALARM);
			smcData.setContent(content);

		} catch (Exception e) {
			LOG.error("构建快消息失败，原因：{}", e);
		}
	}

	public static void main(String[] args) {
		// String result = SysUtils.findByRegex("2103abc", ConstDef.FLAGREGEX,
		// 0);
		// System.out.println(result);
		SMCData r = new SMCData();
		r.setId(1);
		r.setIsReceiveGw(1);
		r.setLevelid(1);
		r.setOccurTime(new Date());
		r.setSendWay(1);
		r.setSrcid(2101);
		r.setToUsers("13537847532");
		r.setSubject("2105＃001caiq；拥塞1112222。".replaceAll("＃", "#").replaceAll(
				"；", ";"));

		// AutoSenderHandle a = new AutoSenderHandle(r);
		// Thread t = new Thread(a);
		// t.start();
	}
}
