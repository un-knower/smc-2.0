package cn.uway.smc.ui.province;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.DateUtil;
import cn.uway.commons.type.StringUtil;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.util.SysCfg;
import cn.uway.smc.util.TimeIntervalMgr;

public abstract class AbstractProvince {

	private static Logger LOG = LoggerFactory.getLogger(AbstractProvince.class);

	protected static String endPort = null;

	protected static Properties p = new Properties();

	protected boolean runFlag = true;

	public AbstractProvince() {
		initPara();
	}

	public void initPara() {
		InputStream ins = null;
		try {
			String configfile = SysCfg.getInstance().getProvinceConfig();
			ins = new FileInputStream("./conf/other/" + configfile);
			p.load(ins);
		} catch (Exception e) {
			LOG.error("error" + e);
		} finally {
			try {
				if (ins != null)
					ins.close();
			} catch (IOException e) {
			}
		}
	}

	public abstract boolean sendMessage(SMCData data);

	
	public static String getSendSmsUsers(String srcUsers) {
	    if (StringUtil.isNull(srcUsers))
	      return "";
	    int begin = srcUsers.indexOf("<PHONE>");
	    int end = srcUsers.lastIndexOf("</PHONE>");

	    if ((begin == -1) || (end == -1)) {
	      return srcUsers.trim();
	    }
	    srcUsers = srcUsers.substring(begin + 7, end);
	    srcUsers.replace(";", ",").replace("；", ",");

	    String[] users = srcUsers.split(",");
	    StringBuilder sb = new StringBuilder();
	    for (String u : users)
	    {
	      if (StringUtil.isNull(u))
	        continue;
	      if (u.startsWith("86"))
	      {
	        u = u.substring(2);
	      }
	      sb.append(u).append(",");
	    }
	    if (sb.length() > 0)
	      sb.deleteCharAt(sb.length() - 1);
	    return sb.toString();
	  }

	public String getSendTime(String sendtime) {

		Date now = new Date();
		if (StringUtil.isNull(sendtime))
			return DateUtil.getDateString_yyyyMMddHHmmss(now);

		try {
			return DateUtil.getDateString_yyyyMMddHHmmss(new Date(DateUtil
					.getDate(sendtime).getTime()));
		} catch (ParseException e) {
			return DateUtil.getDateString_yyyyMMddHHmmss(now);
		}
	}

	public void hander(SMCData smcData) {
		Date now = TimeIntervalMgr.getInstance().getDateValue(new Date());
		LOG.debug(smcData
				+ "  当前时间:  "
				+ DateUtil.getDateString(now)
				+ "  发送时间:  "
				+ (StringUtil.isNull(smcData.getSendTime()) ? "立即发送" : smcData
						.getSendTime()));

		sendMessage(smcData);
	}

}
