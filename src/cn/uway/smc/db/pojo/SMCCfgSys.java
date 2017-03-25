package cn.uway.smc.db.pojo;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * 系统配置表
 * 
 * @author litp Sep 29, 2010
 * @since 1.0
 */
@Entity
public class SMCCfgSys {

	@PrimaryKey(sequence = "ID")
	private int id;// 编号

	private String smtpHost;// 邮件服务器

	private String mailUser; // 邮件账号

	private String mailPwd; // 邮件账号密码

	private int nodeId;// 节点编号

	private int feeType;// 计费类型

	private int agentFlag;// 代收标志

	private int moRelateToMtFlag;// 引起MT消息的原因

	private int priority;// 优先级

	private int reportFlag;// 状态报告标记

	private int tpPid;// GSM协议类型

	private int tpUdhi;// GSM协议类型

	private int messagecoding;// 短消息的编码格式

	private int messageType;// 信息类型

	private String spNumber;// SP的接入号

	private String chargeNumber;// 付费号码

	private String corpId;// 企业代码

	private String serviceType;// 业务代码

	private String feeValue;// 该条短消息的收费值

	private String givenValue;// 赠送用户的话费

	private String expireTime;// 短消息寿命的终止时间

	private String scheduleTime;// 短消息定时发送的时间

	private String serverIp;// 短消息服务器IP

	private int serverPort;// 短消息服务器端口

	private String smsUserName;// 短信用户名

	private String smsUserPwd;// 短信密码

	// add
	private int securityMaxSentCountDay;// 一天时最大发送条数

	private int securityMaxSentCountHour;// 一小时最大发送条数

	private String extTableDriver;// 外部表的驱动

	private String extTableUrl; // 外部表链接

	private String extTableUser;// 外部表用户名

	private String extTablePwd;// 外部表密码

	private String description;// 描述

	private int takeEffect;// 记录是否生效，1为生效，0为失效，默认为1；

	private int serverReceivePort;// 短消息接受端口

	private String serverReceiveUserName;// 短消息接受端口

	private String serverReceivePwd;// 短消息接受端口

	public SMCCfgSys() {
	}

	public SMCCfgSys(int id, String smtpHost, String mailUser, String mailPwd,
			int nodeId, int feeType, int agentFlag, int moRelateToMtFlag,
			int priority, int reportFlag, int tpPid, int tpUdhi,
			int messagecoding, int messageType, String spNumber,
			String chargeNumber, String corpId, String serviceType,
			String feeValue, String givenValue, String expireTime,
			String scheduleTime, String serverIp, int serverPort,
			String smsUserName, String smsUserPwd, int securityMaxSentCountDay,
			int securityMaxSentCountHour, String extTableDriver,
			String extTableUrl, String extTableUser, String extTablePwd,
			String description, int takeEffect, int serverReceivePort,
			String serverReceiveUserName, String serverReceivePwd) {
		super();
		this.id = id;
		this.smtpHost = smtpHost;
		this.mailUser = mailUser;
		this.mailPwd = mailPwd;
		this.nodeId = nodeId;
		this.feeType = feeType;
		this.agentFlag = agentFlag;
		this.moRelateToMtFlag = moRelateToMtFlag;
		this.priority = priority;
		this.reportFlag = reportFlag;
		this.tpPid = tpPid;
		this.tpUdhi = tpUdhi;
		this.messagecoding = messagecoding;
		this.messageType = messageType;
		this.spNumber = spNumber;
		this.chargeNumber = chargeNumber;
		this.corpId = corpId;
		this.serviceType = serviceType;
		this.feeValue = feeValue;
		this.givenValue = givenValue;
		this.expireTime = expireTime;
		this.scheduleTime = scheduleTime;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.smsUserName = smsUserName;
		this.smsUserPwd = smsUserPwd;
		this.securityMaxSentCountDay = securityMaxSentCountDay;
		this.securityMaxSentCountHour = securityMaxSentCountHour;
		this.extTableDriver = extTableDriver;
		this.extTableUrl = extTableUrl;
		this.extTableUser = extTableUser;
		this.extTablePwd = extTablePwd;
		this.description = description;
		this.takeEffect = takeEffect;
		this.serverReceivePort = serverReceivePort;
		this.serverReceiveUserName = serverReceiveUserName;
		this.serverReceivePwd = serverReceivePwd;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getMailUser() {
		return mailUser;
	}

	public void setMailUser(String mailUser) {
		this.mailUser = mailUser;
	}

	public String getMailPwd() {
		return mailPwd;
	}

	public void setMailPwd(String mailPwd) {
		this.mailPwd = mailPwd;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public int getFeeType() {
		return feeType;
	}

	public void setFeeType(int feeType) {
		this.feeType = feeType;
	}

	public int getAgentFlag() {
		return agentFlag;
	}

	public void setAgentFlag(int agentFlag) {
		this.agentFlag = agentFlag;
	}

	public int getMoRelateToMtFlag() {
		return moRelateToMtFlag;
	}

	public void setMoRelateToMtFlag(int moRelateToMtFlag) {
		this.moRelateToMtFlag = moRelateToMtFlag;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getReportFlag() {
		return reportFlag;
	}

	public void setReportFlag(int reportFlag) {
		this.reportFlag = reportFlag;
	}

	public int getTpPid() {
		return tpPid;
	}

	public void setTpPid(int tpPid) {
		this.tpPid = tpPid;
	}

	public int getTpUdhi() {
		return tpUdhi;
	}

	public void setTpUdhi(int tpUdhi) {
		this.tpUdhi = tpUdhi;
	}

	public int getMessagecoding() {
		return messagecoding;
	}

	public void setMessagecoding(int messagecoding) {
		this.messagecoding = messagecoding;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getSpNumber() {
		return spNumber;
	}

	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}

	public String getChargeNumber() {
		return chargeNumber;
	}

	public void setChargeNumber(String chargeNumber) {
		this.chargeNumber = chargeNumber;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getFeeValue() {
		return feeValue;
	}

	public void setFeeValue(String feeValue) {
		this.feeValue = feeValue;
	}

	public String getGivenValue() {
		return givenValue;
	}

	public void setGivenValue(String givenValue) {
		this.givenValue = givenValue;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getSmsUserName() {
		return smsUserName;
	}

	public void setSmsUserName(String smsUserName) {
		this.smsUserName = smsUserName;
	}

	public String getSmsUserPwd() {
		return smsUserPwd;
	}

	public void setSmsUserPwd(String smsUserPwd) {
		this.smsUserPwd = smsUserPwd;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTakeEffect() {
		return takeEffect;
	}

	public void setTakeEffect(int takeEffect) {
		this.takeEffect = takeEffect;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getSecurityMaxSendCountDay() {
		return securityMaxSentCountDay;
	}

	public void setSecurityMaxSendCountDay(int securityMaxSentCountDay) {
		this.securityMaxSentCountDay = securityMaxSentCountDay;
	}

	public int getSecurityMaxSendCountHour() {
		return securityMaxSentCountHour;
	}

	public void setSecurityMaxSendCountHour(int securityMaxSentCountHour) {
		this.securityMaxSentCountHour = securityMaxSentCountHour;
	}

	public String getExtTableDriver() {
		return extTableDriver;
	}

	public void setExtTableDriver(String extTableDriver) {
		this.extTableDriver = extTableDriver;
	}

	public String getExtTableUrl() {
		return extTableUrl;
	}

	public void setExtTableUrl(String extTableUrl) {
		this.extTableUrl = extTableUrl;
	}

	public String getExtTableUser() {
		return extTableUser;
	}

	public void setExtTableUser(String extTableUser) {
		this.extTableUser = extTableUser;
	}

	public String getExtTablePwd() {
		return extTablePwd;
	}

	public void setExtTablePwd(String extTablePwd) {
		this.extTablePwd = extTablePwd;
	}

	public int getServerReceivePort() {
		return serverReceivePort;
	}

	public void setServerReceivePort(int serverReceivePort) {
		this.serverReceivePort = serverReceivePort;
	}

	public String getServerReceivePwd() {
		return serverReceivePwd;
	}

	public void setServerReceivePwd(String serverReceivePwd) {
		this.serverReceivePwd = serverReceivePwd;
	}

	public String getServerReceiveUserName() {
		return serverReceiveUserName;
	}

	public void setServerReceiveUserName(String serverReceiveUserName) {
		this.serverReceiveUserName = serverReceiveUserName;
	}

}
