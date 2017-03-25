package cn.uway.smc.db.pojo;

import java.util.Date;

public class SMCDataReceiveHistory {

	private long id; // 编号

	private String fromUser;// 消息来源

	private Date receiveTime;// 消息接收时间

	private String content;// 具体内容

	private Date stampTime; // 添加历史表时间

	private int result;// 处理结果

	private String cause;// 原因

	// 接收短信新增加4字段 2010-11-08
	private int businessId;

	private int iseffect;// 接收短信是否有效

	private int isParseOk;// 接收短信是否可以解析，方便前台处理

	private String remark;// 说明

	// 存储
	private String receiveTimeTmp;

	private String stampTimeTmp;

	private String spNumber; // 网关接入号

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getStampTime() {
		return stampTime;
	}

	public void setStampTime(Date stampTime) {
		this.stampTime = stampTime;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public int getIseffect() {
		return iseffect;
	}

	public void setIseffect(int iseffect) {
		this.iseffect = iseffect;
	}

	public int getIsParseOk() {
		return isParseOk;
	}

	public void setIsParseOk(int isParseOk) {
		this.isParseOk = isParseOk;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getBusinessId() {
		return businessId;
	}

	public void setBusinessId(int businessId) {
		this.businessId = businessId;
	}

	public String getReceiveTimeTmp() {
		return receiveTimeTmp;
	}

	public void setReceiveTimeTmp(String receiveTimeTmp) {
		this.receiveTimeTmp = receiveTimeTmp;
	}

	public String getStampTimeTmp() {
		return stampTimeTmp;
	}

	public void setStampTimeTmp(String stampTimeTmp) {
		this.stampTimeTmp = stampTimeTmp;
	}

	public String getSpNumber() {
		return spNumber;
	}

	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}

}
