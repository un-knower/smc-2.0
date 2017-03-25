package cn.uway.smc.db.pojo;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * 消息级别
 * 
 * @author litp Sep 29, 2010
 * @since 1.0
 */
@Entity
public class SMCCfgLevel {

	private int id; // 编号

	@PrimaryKey(sequence = "ID")
	private int levelid; // 消息级别

	private String name;// 级别名称

	private String description;// 类型描述

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLevelid() {
		return levelid;
	}

	public void setLevelid(int levelid) {
		this.levelid = levelid;
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

	@Override
	public String toString() {
		return " " + this.getName() + "  " + getDescription();
	}

}
