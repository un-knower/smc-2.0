package cn.uway.smc.db.pojo;

import java.util.List;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.NotPersistent;

/**
 * 消息接收用户分组表
 * 
 * @author litp Sep 29, 2010
 * @since 1.0
 */
@Entity
public class SMCCfgToUserGroup {

	private int id;

	private int toUserGroupLevel;// 用户级别

	private String description;// 类型描述

	@NotPersistent
	private List<SMCCfgToUser> groupUser;// 此级别的所有用户

	private String name;

	public List<SMCCfgToUser> getGroupUser() {
		return groupUser;
	}

	public void setGroupUser(List<SMCCfgToUser> groupUser) {
		this.groupUser = groupUser;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getToUserGroupLevel() {
		return toUserGroupLevel;
	}

	public void setToUserGroupLevel(int toUserGroupLevel) {
		this.toUserGroupLevel = toUserGroupLevel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
