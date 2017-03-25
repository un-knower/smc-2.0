package cn.uway.smc.util;

import static cn.uway.smc.util.ConstDef.SYSCFG_FILE_URL;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.DateUtil;
import cn.uway.commons.type.StringUtil;
import cn.uway.smc.exception.CfgException;

/**
 * 系统配置管理类
 * 
 * @author YangJian
 * @version 1.0
 * @since 1.0
 */
public final class SysCfg {

	private static Logger log = LoggerFactory.getLogger(SysCfg.class);

	public static int sendersleep = 30;

	private PropertiesXML propertiesXML;

	public static String CITY_NAME_EN = null;

	private SysCfg() {
		super();
		load();
	}

	private static class SysCfgContainer {

		private static SysCfg instance = new SysCfg();
	}

	public static SysCfg getInstance() {
		return SysCfgContainer.instance;
	}

	/**
	 * 加载系统配置文件
	 */
	private void load() {
		try {
			propertiesXML = new PropertiesXML(SYSCFG_FILE_URL);
		} catch (CfgException e) {
			log.error("加载系统配置文件信息失败,原因:", e);
		}
	}

	public void reload() {
		propertiesXML = null;
		load();
	}

	/**
	 * 短信协议 1:sgip 联通协议 2:smgp 电信协议
	 * 
	 * @return
	 */
	public int getSmsProtocol() {
		int i = 1;
		String protocol = propertiesXML
				.getProperty("smc_cfg.system.SmsProtocol");
		if (!StringUtil.isNull(protocol)) {
			i = Integer.valueOf(protocol);
		}
		return i;
	}

	public String getProvinceConfig() {
		String protocol = propertiesXML
				.getProperty("smc_cfg.other.province_config");
		if (!StringUtil.isNull(protocol)) {
			protocol = protocol.trim();
		}
		return protocol;
	}

	public boolean getIsUsed() {
		boolean bflag = false;
		String protocol = propertiesXML.getProperty("smc_cfg.other.is_used");
		if (!StringUtil.isNull(protocol)) {
			if (protocol.equalsIgnoreCase("1")
					|| "true".equalsIgnoreCase(protocol))
				bflag = true;
		}
		return bflag;
	}

	public String getSmcCityEn() throws Exception {
		if (StringUtil.isNotNull(CITY_NAME_EN))
			return CITY_NAME_EN;
		String config = getProvinceConfig();

		if (StringUtil.isNull(config))
			throw new Exception("请配置个性化属性配置文件.");

		String str[] = config.split("_");
		if (str.length <= 1)
			throw new Exception("个性化属性配置文件名不规范.smc_cfg.other.province_config");

		CITY_NAME_EN = str[0].toUpperCase();
		return CITY_NAME_EN;
	}

	/**
	 * 邮件附件目录，用户存放传递过来的附件
	 * 
	 * @return
	 */
	public String getEmaildirectory() {
		String protocol = propertiesXML
				.getProperty("smc_cfg.system.emaildirectory");
		if (StringUtil.isNull(protocol)) {
			String dir = System.getProperty("user.dir");
			String fileStr = dir + File.separator + "email";
			File file = new File(fileStr);
			if (!file.exists())
				file.mkdirs();
		}
		if (protocol.charAt(protocol.length() - 1) != '/') {
			protocol = protocol + "/";
		}
		return protocol;
	}

	/**
	 * 短信时间偏移量 (时间单位:s)
	 * 
	 * @return
	 */
	public int getSmsDateOffSet() {
		int i = 300;// 180秒
		String offset = propertiesXML
				.getProperty("smc_cfg.system.smsdateoffset");
		if (!StringUtil.isNull(offset)) {
			i = Integer.valueOf(offset);
		}
		return i * 1000;
	}

	/**
	 * 扫描周期
	 * 
	 * @return
	 */
	public int getScanPriod() {
		String period = propertiesXML.getProperty("smc_cfg.system.period");
		if (StringUtil.isNull(period)) {
			period = "60";
		}
		return Integer.parseInt(period) * 1000;
	}

	public int getSendersleep() {
		String sleep = propertiesXML.getProperty("smc_cfg.system.sendersleep");
		if (StringUtil.isNull(sleep)) {
			sendersleep = 60;
		} else {
			sendersleep = Integer.parseInt(sleep);
		}

		return sendersleep;
	}
	public int getEmailSendersleep() {
		String sleep = propertiesXML.getProperty("smc_cfg.system.emailsendersleep");
		if (StringUtil.isNull(sleep)) {
			sendersleep = 1000;
		} else {
			sendersleep = Integer.parseInt(sleep);
		}

		return sendersleep;
	}
	
	public boolean isEmailForbid() {
		String emailForbid = propertiesXML.getProperty("smc_cfg.system.email_forbid");
		if (!StringUtil.isNull(emailForbid)) {
			if("1".equalsIgnoreCase(emailForbid)||"true".equalsIgnoreCase(emailForbid))
				return true;
		}

		return false;
	}
	

	/**
	 * 扫描周期
	 * 
	 * @return
	 */
	public int getThreadPoolSize() {
		String size = propertiesXML
				.getProperty("smc_cfg.system.threadpoolsize");
		if (StringUtil.isNull(size)) {
			size = "3";
		}
		return Integer.parseInt(size);
	}

	/*
	 * 获取Berkeley db 根路径
	 * 
	 * @return
	 */
	public String getBerkeleyDbRootPath() {
		return propertiesXML.getProperty("smc_cfg.system.berkeleydb.rootpath");
	}

	// 设置每小时发送次数
	public String getHourSendCount() {
		return propertiesXML
				.getProperty("smc_cfg.system.statistics.hoursendcount");
	}

	/**
	 * 每天发送短信息个数
	 * 
	 * @return
	 */
	public String getDaySendCount() {
		return propertiesXML
				.getProperty("smc_cfg.system.statistics.daysendcount");
	}

	/**
	 * 每秒钟发送短信个数，流量控制
	 */
	public int getGwinvaltime() {
		String v = propertiesXML.getProperty("smc_cfg.system.gwinvaltime");
		if (StringUtil.isNull(v))
			v = "10";
		return Integer.parseInt(v);
	}

	/**
	 * 
	 */
	public int getGwSleepTime() {
		return 1 * 1000;
	}

	/**
	 * 获取版本号，默认空字符串
	 * 
	 * @return
	 */
	public String getEdition() {
		String e = propertiesXML.getProperty("smc_cfg.system.version.edition");
		if (StringUtil.isNull(e)) {
			e = "";
		}
		return e;
	}

	/**
	 * 网关是否支持群发，1.支撑：true 2 . 不支持 ：fasle
	 * 
	 * @return
	 */
	public boolean isGroupSender() {
		String e = propertiesXML.getProperty("smc_cfg.system.groupsender");
		boolean flag = false;
		if (StringUtil.isNull(e)) {
			e = "false";
		} else {
			if (e.trim().equals("true")) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获取发布时间（yyyy-mm-dd hh:mm:ss），默认空字符串
	 * 
	 * @return
	 */
	public String getReleaseTime() {
		String d = propertiesXML
				.getProperty("smc_cfg.system.version.releaseTime");
		if (StringUtil.isNull(d)) {
			return "";
		}
		try {
			d = DateUtil.getDateString(DateUtil.getDate(d));
		} catch (Exception e) {
			return "";
		}
		return d;
	}

	/** 是否为 oracle 数据库 */
	public boolean isOracle() {
		String strDriver = propertiesXML.getProperty("smc_cfg.db.dbpooltype");

		return strDriver.contains("oracle");
	}

	/** 是否为 sybase 数据库 */
	public boolean isSybase() {
		String strDriver = propertiesXML.getProperty("smc_cfg.db.dbpooltype");

		return strDriver.contains("sybase");
	}

	/**
	 * 获取数据库连接池类型，默认类型 (dbcp=1),其他类型 c3p0=2
	 * 
	 * @return
	 */
	public int getDbPoolType() {
		int type = 1;
		String dbpooltype = propertiesXML.getProperty("smc_cfg.db.dbpooltype");
		if (StringUtil.isNull(dbpooltype)) {
			return type;
		}
		try {
			type = Integer.valueOf(dbpooltype);
		} catch (NumberFormatException e) {
			type = 1;
			return type;
		}
		if (type < 0 || type > 2) {
			type = 1;
		}

		return type;
	}

	/**
	 * 获取连接池名称，默认值：S3_DB_POOL
	 * 
	 * @return
	 */
	public String getPoolName() {
		String dbName = propertiesXML.getProperty("smc_cfg.db.name");

		if (StringUtil.isNull(dbName)) {
			dbName = "S3_DB_POOL";
		}

		return dbName;
	}

	/**
	 * 获取连接池类型，默认值：javax.sql.DataSource
	 * 
	 * @return
	 */
	public String getPoolType() {
		String name = propertiesXML.getProperty("smc_cfg.db.type");

		if (StringUtil.isNull(name)) {
			name = "javax.sql.DataSource";
		}

		return name;
	}

	/**
	 * 获取数据库驱动类，默认值：oracle.jdbc.driver.OracleDriver
	 * 
	 * @return
	 */
	public String getDbDriver() {
		String d = propertiesXML.getProperty("smc_cfg.db.driverClassName");

		if (StringUtil.isNull(d)) {
			d = "oracle.jdbc.driver.OracleDriver";
		}

		return d;
	}

	/**
	 * 获取数据库连接字符串，默认值为空字符串
	 * 
	 * @return
	 */
	public String getDbUrl() {
		String url = propertiesXML.getProperty("smc_cfg.db.url");

		if (StringUtil.isNull(url)) {
			url = "";
		}

		return url;
	}

	/**
	 * 获取数据库服务名，默认为空字符串
	 * 
	 * @return
	 */
	public String getDbService() {
		String service = propertiesXML.getProperty("smc_cfg.db.service");
		if (StringUtil.isNull(service)) {
			service = "";
		}
		return service;
	}

	/**
	 * 获取数据库用户名，默认为空字符串
	 * 
	 * @return
	 */
	public String getDbUserName() {
		String user = propertiesXML.getProperty("smc_cfg.db.user");
		if (StringUtil.isNull(user)) {
			user = "";
		}
		return user;
	}

	/**
	 * 获取数据库密码，默认为空字符串
	 * 
	 * @return
	 */
	public String getDbPassword() {
		String pwd = propertiesXML.getProperty("smc_cfg.db.password");
		if (StringUtil.isNull(pwd)) {
			pwd = "";
		}
		return pwd;
	}

	/**
	 * 获取连接池最大活动连接数，默认值：12
	 * 
	 * @return
	 */
	public int getPoolMaxActive() {
		int ma = 12;
		try {
			ma = Integer.parseInt(propertiesXML
					.getProperty("smc_cfg.db.maxActive"));
		} catch (Exception e) {
		}
		if (ma <= 0) {
			ma = 12;
		}
		return ma;
	}

	/**
	 * 获取连接池最大活动空闲连接数，默认值：5
	 * 
	 * @return
	 */
	public int getPoolMaxIdle() {
		int maxIdle = 5;
		try {
			maxIdle = Integer.parseInt(propertiesXML
					.getProperty("smc_cfg.db.maxIdle"));
		} catch (Exception e) {
		}
		if (maxIdle <= 0) {
			maxIdle = 5;
		}
		return maxIdle;
	}

	/**
	 * 获取连接池最大等待数，默认值：10000
	 * 
	 * @return
	 */
	public int getPoolMaxWait() {
		int maxWait = 10000;
		try {
			maxWait = Integer.parseInt(propertiesXML
					.getProperty("smc_cfg.db.maxWait"));
		} catch (Exception e) {
		}
		if (maxWait <= 0) {
			maxWait = 10000;
		}
		return maxWait;
	}

	/**
	 * 执行SELECT时的超时时间，单位为秒，默认60秒
	 * 
	 * @return
	 */
	public int getQueryTimeout() {
		int timeout = 60;
		try {
			timeout = Integer.parseInt(propertiesXML
					.getProperty("smc_cfg.db.queryTimeout"));
		} catch (Exception e) {
		}
		if (timeout <= 0) {
			timeout = 60;
		}
		return timeout;
	}

	/**
	 * 获取数据库连接验证语句，默认值：select sysdate from dual
	 * 
	 * @return
	 */
	public String getDbValidationQueryString() {
		String sql = propertiesXML.getProperty("smc_cfg.db.validationQuery");
		if (StringUtil.isNull(sql)) {
			sql = "select sysdate from dual";
		}
		return sql;
	}

	/**
	 * 是否删除sqlldr文件，默认值：true
	 * 
	 * @return
	 */
	public boolean isDeleteLog() {
		boolean b = true;

		try {
			b = Boolean.parseBoolean(propertiesXML
					.getProperty("smc_cfg.externalTool.sqlldr.delLog"));
		} catch (Exception e) {
		}
		return b;
	}

	/**
	 * 获取sqlldr字符集，默认值：ZHS16GBK
	 * 
	 * @return
	 */
	public String getSqlldrCharset() {
		String s = propertiesXML
				.getProperty("smc_cfg.externalTool.sqlldr.charset");

		if (StringUtil.isNull(s)) {
			s = "ZHS16GBK";
		}

		return s;
	}

	/**
	 * 是否开启告警模块 true 开启, false 关闭
	 */
	public boolean isEnableAlarm() {
		boolean b = false;
		String on = propertiesXML.getProperty("smc_cfg.module.alarm.enable");
		if (StringUtil.isNotNull(on)) {
			on = on.toLowerCase().trim();
			if (on.equals("on") || on.equals("true"))
				b = true;
		}
		return b;
	}

	/**
	 * 是否开启Web模块，默认为true | on
	 * 
	 * @return
	 */
	public boolean isEnableWeb() {
		boolean b = false;
		String str = propertiesXML.getProperty("smc_cfg.module.web.enable");
		if (StringUtil.isNull(str)) {
			return b;
		}
		str = str.toLowerCase().trim();
		if (str.equals("on") || str.equals("true")) {
			b = true;
		} else if (str.equals("off") || str.equals("false")) {
			b = false;
		}
		return b;
	}

	public int getWebPort() {
		int port = 8080;
		try {
			port = Integer.parseInt(propertiesXML
					.getProperty("smc_cfg.module.web.port"));
		} catch (Exception e) {
		}
		if (port <= 0) {
			port = 8080;
		}
		return port;
	}

	public String getWebCharset() {
		return propertiesXML.getProperty("smc_cfg.module.web.charset");
	}

	/** 用户自定义log级别，0为debug，1为info，2为warn，3为error，4为fatal */
	public String getWebServerLogLevel() {
		String str = propertiesXML.getProperty("smc_cfg.module.web.loglevel");
		if (str == null || str.equals("") || str.equalsIgnoreCase("info"))
			str = "1";
		else if (str.equalsIgnoreCase("debug"))
			str = "0";
		else if (str.equalsIgnoreCase("warn"))
			str = "2";
		else if (str.equalsIgnoreCase("error"))
			str = "3";
		else if (str.equalsIgnoreCase("fatal"))
			str = "4";
		else
			str = "1";

		return str;
	}

	/*
	 * 短信测试号码
	 */
	public String getSMCTestPhone() {
		String user = propertiesXML.getProperty("smc_cfg.smctest.phone");
		if (StringUtil.isNull(user)) {
			user = "";
		}
		return user;
	}

	public String getSMCTestUsername() {
		String user = propertiesXML.getProperty("smc_cfg.smctest.username");
		if (StringUtil.isNull(user)) {
			user = "1";
		}
		return user;
	}

	public String getSMCTestSend_time() {
		String user = propertiesXML.getProperty("smc_cfg.smctest.send_time");
		if (StringUtil.isNull(user)) {
			user = null;
		}
		return user;
	}

	public String getSMCTestPassword() {
		String user = propertiesXML.getProperty("smc_cfg.smctest.password");
		if (StringUtil.isNull(user)) {
			user = "c4ca4238a0b923820dcc509a6f75849b";
		}
		return user;
	}

	public String getEmailAttFile() {
		String file = propertiesXML.getProperty("smc_cfg.smctest.file");
		if (StringUtil.isNull(file)) {
			file = null;
		}
		return file;
	}
	

	public String getEmailTitle() {
		String file = propertiesXML.getProperty("smc_cfg.smctest.emailTitle");
		if (StringUtil.isNull(file)) {
			file = " Test email ";
		}
		return file;
	}
	

	public int getSMCTestSrcid() {
		String user = propertiesXML.getProperty("smc_cfg.smctest.src_id");
		if (StringUtil.isNull(user)) {
			user = "1";
		}
		return Integer.valueOf(user);
	}

	public int getSMCTestLevelId() {
		String user = propertiesXML.getProperty("smc_cfg.smctest.level");
		if (StringUtil.isNull(user)) {
			user = "1";
		}
		return Integer.valueOf(user);
	}

	/**
	 * 测试邮件地址
	 * 
	 * @return
	 */
	public String getSMCTestEmail() {
		String user = propertiesXML.getProperty("smc_cfg.smctest.email");
		if (StringUtil.isNull(user)) {
			user = "";
		}
		return user;
	}

	/**
	 * 短信发送方式 1:短信 2：邮件 3:短信与邮件
	 * 
	 * @return
	 */
	public int getSendWay() {
		String sendWay = propertiesXML.getProperty("smc_cfg.smctest.sendWay");
		if (StringUtil.isNull(sendWay)) {
			sendWay = "1";
		}
		return Integer.parseInt(sendWay);
	}

	/**
	 * 用于短信测试内容
	 * 
	 * @return
	 */
	public String getContent() {
		String content = propertiesXML.getProperty("smc_cfg.smctest.content");
		if (StringUtil.isNull(content)) {
			content = "";
		}
		return content;
	}

	/**
	 * 取得最大短小时长度
	 * 
	 * @return
	 */
	public int getMessagelength() {
		int length = 140;
		String content = propertiesXML
				.getProperty("smc_cfg.system.messagelength");
		if (StringUtil.isNotNull(content)) {
			length = Integer.parseInt(content);
		}
		return length;
	}
	
	

	public List<String> getInvalidMobileNumbers() {
		List<String> mobiles = propertiesXML.getPropertyes("smc_cfg.smctest.invalid_mobile_numbers.invalid_mobile_number");
		
		if(mobiles==null || mobiles.size()==0)
			return null;
		List<String>  result =new ArrayList<String>();
		for(String  mobile:mobiles){
			if(StringUtil.isNull(mobile))
				continue;
			if(mobile.length()<11)
				continue;
			result.add(mobile);
		}
        return result; 
	}
	

	public static void main(String[] args) {
		SysCfg s = SysCfg.getInstance();
		s.getBerkeleyDbRootPath();
		log.debug(s.getDaySendCount() + " " + s.getSendersleep());

		String dir = System.getProperty("user.dir");
		System.out.println(dir);
	}

	public int getMaxThreadCount() {

		return 50;
	}

}
