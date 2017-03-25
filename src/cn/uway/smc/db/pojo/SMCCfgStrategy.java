package cn.uway.smc.db.pojo;

import java.io.Serializable;

import com.sleepycat.persist.model.Entity;

/**
 * 短消息发送策略表
 * 
 * @author litp Sep 29, 2010
 * @since 1.0
 */
@Entity
public class SMCCfgStrategy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private int srcid;// 消息类型

	private int levelid;// 消息级别

	private SMCCfgToUserGroup smcToUserGroup;// 接收消息的用户分组编号

	private SMCCfgSource smcSource;// 消息源

	private SMCCfgLevel smcLevel;// 消息级别

	private int groupId;

	private int ttl;// 消息生存时间,0为不限制；单位为分钟。默认值为120分钟

	private int endoOffsetTime;// 发送偏移时间，默认为null，当为null时按正常时间发送，当有取值时按配置的偏移时间发送,单位为分钟.

	private int sendTimes = 0;// 发送次数

	private int sendInterval;// 时间间隔，单位为分钟，此值只在发送次数字段有效时才有效

	private int sendWay;// 发送方式，0为短信、1为邮件，默认值为0；

	private int resendWhenFail;// 当发送失败时是否重发，0为不重复，1为重发，默认为0；

	private int takeEffect;// 是否生效，1为生效，0为不生效，默认为1；

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public SMCCfgToUserGroup getSmcToUserGroup() {
		return smcToUserGroup;
	}

	public void setSmcToUserGroup(SMCCfgToUserGroup smcToUserGroup) {
		this.smcToUserGroup = smcToUserGroup;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public int getEndoOffsetTime() {
		return endoOffsetTime;
	}

	public void setEndoOffsetTime(int endoOffsetTime) {
		this.endoOffsetTime = endoOffsetTime;
	}

	public int getSendTimes() {
		return sendTimes;
	}

	public void setSendTimes(int sendTimes) {
		this.sendTimes = sendTimes;
	}

	public int getSendInterval() {
		return sendInterval;
	}

	public void setSendInterval(int sendInterval) {
		this.sendInterval = sendInterval;
	}

	public int getSendWay() {
		return sendWay;
	}

	public void setSendWay(int sendWay) {
		this.sendWay = sendWay;
	}

	public int getResendWhenFail() {
		return resendWhenFail;
	}

	public void setResendWhenFail(int resendWhenFail) {
		this.resendWhenFail = resendWhenFail;
	}

	public int getTakeEffect() {
		return takeEffect;
	}

	public void setTakeEffect(int takeEffect) {
		this.takeEffect = takeEffect;
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

}
