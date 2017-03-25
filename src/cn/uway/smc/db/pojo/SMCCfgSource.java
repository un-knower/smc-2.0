package cn.uway.smc.db.pojo;

import java.io.Serializable;

import com.sleepycat.persist.model.Entity;

/**
 * 短消息来源
 * 
 * @author litp Sep 29, 2010
 * @since 1.0
 */
@Entity
public class SMCCfgSource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int srcid; // 编号

	private String name;// 类型名，如(igp,s3);etc

	private String description;// 类型描述

	private String user; // 用户名

	private String pwd; // 用户密码

	private String ipList; // 进程所在ip列表,以逗号分隔

	public SMCCfgSource(int srcid, String name, String description,
			String user, String pwd, String ipList) {
		super();
		this.srcid = srcid;
		this.name = name;
		this.description = description;
		this.user = user;
		this.pwd = pwd;
		this.ipList = ipList;
	}

	public SMCCfgSource() {
		super();
	}

	public int getSrcid() {
		return srcid;
	}

	public void setSrcid(int id) {
		this.srcid = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getIpList() {
		return ipList;
	}

	public void setIpList(String ipList) {
		this.ipList = ipList;
	}

	public boolean checkIp(String ip) {
		if (ipList == null)
			return true;
		return ipList.contains(ip);
	}

}
