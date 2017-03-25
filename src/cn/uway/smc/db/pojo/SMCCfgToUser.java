package cn.uway.smc.db.pojo;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.NotPersistent;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * 消息接收用户表
 * 
 * @author litp Sep 29, 2010
 * @since 1.0
 */
@Entity
public class SMCCfgToUser {

	@PrimaryKey(sequence = "ID")
	private int id;

	private String name;// 用户名

	private int groupId;// 用户分组ID号

	private String cellphone;// 用户手机号

	private String email;// 电子邮箱

	private String description;// 类型描述

	@NotPersistent
	private SMCCfgToUserGroup group;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SMCCfgToUserGroup getGroup() {
		return group;
	}

	public void setGroup(SMCCfgToUserGroup group) {
		this.group = group;
	}

}
