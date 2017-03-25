package cn.uway.smc.db.pojo;

import java.util.Date;

/**
 * @author liuwx
 */
public class SMCDataHistory {

	private long id;

	private int srcId;// 消息来源编号

	private int levelId;// 消息级别

	private String toUsers; // 接收的地址 （手机号，email 多个可以用，分割开来）

	private int sendWay; // 发送方式 0：短信，1：邮件 默认值：0

	private Date occurTime;// 消息产生时间

	private String content;// 消息具体内容

	private String sendTime; // 消息发送时间

	private String sendTimeExclude;

	private int sentResult;// 发送状态

	private int counter;// 发送次数

	private Date stampTime; // 添加到历史表时间

	private String cause; // 原因

	// 临时存储时间，便于前端显示
	private String occurTimeTmp;

	private String stampTimeTmp;

	private String attachmentfile;// 邮件附件名称;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSrcId() {
		return srcId;
	}

	public void setSrcId(int srcId) {
		this.srcId = srcId;
	}

	public int getLevelId() {
		return levelId;
	}

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

	public String getToUsers() {
		return toUsers;
	}

	public void setToUsers(String toUsers) {
		this.toUsers = toUsers;
	}

	public int getSendWay() {
		return sendWay;
	}

	public void setSendWay(int sendWay) {
		this.sendWay = sendWay;
	}

	public Date getOccurTime() {
		return occurTime;
	}

	public void setOccurTime(Date occurTime) {
		this.occurTime = occurTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendTimeExclude() {
		return sendTimeExclude;
	}

	public void setSendTimeExclude(String sendTimeExclude) {
		this.sendTimeExclude = sendTimeExclude;
	}

	public int getSentResult() {
		return sentResult;
	}

	public void setSentResult(int sentResult) {
		this.sentResult = sentResult;
	}

	public Date getStampTime() {
		return stampTime;
	}

	public void setStampTime(Date stampTime) {
		this.stampTime = stampTime;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getOccurTimeTmp() {
		return occurTimeTmp;
	}

	public void setOccurTimeTmp(String occurTimeTmp) {
		this.occurTimeTmp = occurTimeTmp;
	}

	public String getStampTimeTmp() {
		return stampTimeTmp;
	}

	public void setStampTimeTmp(String stampTimeTmp) {
		this.stampTimeTmp = stampTimeTmp;
	}

	public String getAttachmentfile() {
		return attachmentfile;
	}

	public void setAttachmentfile(String attachmentfile) {
		this.attachmentfile = attachmentfile;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

}
