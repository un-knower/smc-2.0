package cn.uway.smc.db.pojo;

import java.io.Serializable;
import java.util.Date;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.NotPersistent;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class SMCDataReceive implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKey(sequence = "ID")
	private long id;

	private String fromUser;//

	private Date receiveTime;//

	private String content;//

	// 接收短信新增加4字段 2010-11-08
	private int businessId;

	private int iseffect;// 接收短信是否有效

	private int isParseOk;// 接收短信是否可以解析，方便前台处理

	private String remark;// 说明

	private boolean isHelpInfo = false;

	@NotPersistent
	private String receiveTimeTmp;// 将 receiveTime 转化为 字符串存储

	@NotPersistent
	private String spNumber;

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

	public boolean isHelpInfo() {
		return isHelpInfo;
	}

	public void setHelpInfo(boolean isHelpInfo) {
		this.isHelpInfo = isHelpInfo;
	}

	public String getReceiveTimeTmp() {
		return receiveTimeTmp;
	}

	public void setReceiveTimeTmp(String receiveTimeTmp) {
		this.receiveTimeTmp = receiveTimeTmp;
	}

	public String getSpNumber() {
		return spNumber;
	}

	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}

}
