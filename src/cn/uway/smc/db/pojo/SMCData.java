package cn.uway.smc.db.pojo;

import java.io.Serializable;
import java.util.Date;

import cn.uway.commons.type.StringUtil;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.NotPersistent;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * @author liuwx
 */
@Entity
public class SMCData implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKey(sequence = "ID")
	private long id;

	private int srcid;// 消息类型

	private int levelid;// 消息级别

	@NotPersistent
	private SMCCfgSource smcSource;// 消息源

	@NotPersistent
	private SMCCfgLevel smcLevel;// 消息级别

	private String toUsers;

	private int sendWay; // 发送方式

	private Date occurTime;// 消息产生时间

	private String content;// 消息具体内容

	private String sendTime; // 发送时间

	private String sendTimeExclude;

	@NotPersistent
	private SMCCfgStrategy strategy;

	private int sentOkTimes = 0;// 消息已经发送的次数(开始为0)//add

	private String subject;// 主题，针对邮件

	@NotPersistent
	private int isReceiveGw = 0;// 是否是从网关接收

	@NotPersistent
	private String spNumber;

	private String attachmentfile;// 针对邮件附件

	@NotPersistent
	private int type = 2;

	@NotPersistent
	private String username;

	@NotPersistent
	private String password;

	public SMCCfgStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(SMCCfgStrategy strategy) {
		this.strategy = strategy;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SMCCfgSource getSmcSource() {
		return smcSource;
	}

	public void setSmcSource(SMCCfgSource smcSource) {
		this.smcSource = smcSource;
	}

	public SMCCfgLevel getSmcLevel() {
		return smcLevel;
	}

	public void setSmcLevel(SMCCfgLevel smcLevel) {
		this.smcLevel = smcLevel;
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

	public int getSentOkTimes() {
		return sentOkTimes;
	}

	public void setSentOkTimes(int sentOkTimes) {
		this.sentOkTimes = sentOkTimes;
	}

	public int getSrcid() {
		return srcid;
	}

	public void setSrcid(int srcid) {
		this.srcid = srcid;
	}

	public int getLevelid() {
		return levelid;
	}

	public void setLevelid(int levelid) {
		this.levelid = levelid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getIsReceiveGw() {
		return isReceiveGw;
	}

	public void setIsReceiveGw(int isReceiveGw) {
		this.isReceiveGw = isReceiveGw;
	}

	public String getSpNumber() {
		return spNumber;
	}

	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}

	public String getAttachmentfile() {
		if (StringUtil.isNull(attachmentfile))
			attachmentfile = "";

		return attachmentfile;
	}

	public void setAttachmentfile(String attachmentfile) {
		this.attachmentfile = attachmentfile;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "SMCExpressData [id=" + id + ", srcid=" + srcid + ", levelid="
				+ levelid + "]";
	}
}
