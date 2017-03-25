package cn.uway.smc.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.uway.commons.type.StringUtil;
import cn.uway.smc.db.pojo.SMCCfgSys;
import cn.uway.smc.pack.Package;
import cn.uway.smc.pack.SMSPackage;

/**
 * 常量定义类
 * 
 * @author YangJian
 * @version 1.0
 * @since 1.0
 */
public final class ConstDef {

	/** 文件分隔符 */
	public static final String F_SEP = File.separator;

	/** 系统配置文件位置 */
	public static final String SYSCFG_FILE_URL = "." + F_SEP + "conf" + F_SEP
			+ "config.xml";

	/** 系统日志配置文件位置 */
	public static final String LOGCFG_FILE_URL = "." + F_SEP + "conf" + F_SEP
			+ "logback.xml";

	public static int SM_SENDER_POOL_SIZE = 3;// 默认线程池大小为3

	public static String FILE_SUFFIX = ".txt";

	public static int SMSWAY = 1;

	// 电话号码开始标记
	public static final String PHONEFLAGBEGIN = "<PHONE>";

	// 电话号码，以及邮件分隔标记
	public static final String SPLIT = ";";

	// 电话号码结束标记
	public static final String PHONEFLAGEND = "</PHONE>";

	// 电话号码开始标记
	public static final String EMAILFLAGBEGIN = "<EMAIL>";

	// 电话号码结束标记
	public static final String EMAILFLAGEND = "</EMAIL>";

	public static final String PHONE = "PHONE";

	public static final String EMAIL = "EMAIL";

	public static final int MESSAGELENGTH = 800;

	// 每条短信个数
	public static int ONEMESSAGELENGTH = 140;

	public static final String SMCALARM = "SMC系统告警";

	// 短息快递序列
	public static final String SEQ_EXPRESS = "SEQ_EXPRESS";

	// 短息接收序列
	public static final String SEQ_RECEIVE = "SEQ_RECEIVE";

	public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final int SENDERSUC = 0;

	public static final int SENDERFAIL = -1;

	public static final int SENDEREXPIRED = -2;

	public static final int SENDMAXTIMES = -3;

	public static final int EXCEPTION = -4;
	
	public static final int USERERROR = -5;
	
	public static final int EMAILADDRESS_ERROR = -5;
	
	public static final int EMAIL_FORBID_ERROR = -6;
	public static final int INVALID_MOBILE_NUMBER = -7;
	

	// 通过正则表达式查找业务代码
	public static final String ISBUSINESSIDREGEX = "(\\d{4})";

	// 禁止接收短信指令
	public static final String FORBIDRECEIVE = "0000#1";

	// 恢复接收短信指令
	public static final String COMEBACKRECEIVE = "0000#2";

	public static final String FLAG = "#";

	public static final String HELPFLAG = "?";

	/* 判断是否是短信息帮助信息,正则表达式 */
	public static final String FLAGREGEX = "(\\d{4})#.?{1}[1,2]";

	public static final String FLAGREGEXFORBID = "(\\d{4})#.?{1}[1]";

	public static final String FLAGREGEXRECOVERY = "(\\d{4})#.?{1}[2]";

	public static final String FLAGREGEXRECOVERYALL = "(\\d{4})#\\d{1}";

	public ConstDef() {
		ONEMESSAGELENGTH = SysCfg.getInstance().getMessagelength();
		SM_SENDER_POOL_SIZE = SysCfg.getInstance().getThreadPoolSize();
	}

	/**
	 * 获取电话，短信息
	 * 
	 * @param content
	 * @return Map<phone, 电话信息> Map<email, 邮件信息>
	 */
	public static Map<String, List<String>> getPhoneEmail(String content) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		if (StringUtil.isNull(content))
			return null;
		int pIndex = content.indexOf(PHONEFLAGBEGIN);
		int pEndIndex = content.indexOf(PHONEFLAGEND);

		int eIndex = content.indexOf(EMAILFLAGBEGIN);
		int eEndIndex = content.indexOf(EMAILFLAGEND);

		String phones[] = null;
		String emails[] = null;
		
		String  phone=null;
		String  email=null;
		
        if(content.contains(PHONEFLAGBEGIN)){
		 phone = content.substring(pIndex + PHONEFLAGBEGIN.length(),
				pEndIndex);
        }
        if(content.contains(EMAILFLAGBEGIN)){
		 email = content.substring(eIndex + EMAILFLAGBEGIN.length(),
				eEndIndex);
        }
		// 只有短息
		if (pIndex != -1 && eIndex == -1) {
			if (StringUtil.isNotNull(phone)) {
				phones = phone.split(SPLIT);
				result.put(PHONE, Arrays.asList(phones));
			}
		}
		// 既有短息，又有邮件
		else if (pIndex != -1 && eIndex != -1) {
			if (StringUtil.isNotNull(phone)) {

				phones = phone.split(SPLIT);
				result.put(PHONE, Arrays.asList(phones));
			}
			if (StringUtil.isNotNull(email)) {
				emails = email.split(SPLIT);
				result.put(EMAIL, Arrays.asList(emails));
			}
		}
		// 只有邮件
		else if (pIndex == -1 && eIndex != -1) {
			if (StringUtil.isNotNull(email)) {
				emails =email.split(SPLIT);
				result.put(EMAIL, Arrays.asList(emails));
			}
		}
		return result;

	}

	/**
	 * 字符长度限制140 *5=720个字符，短信最大长度不能超过720个字符，SMC会自动拆分成5个短信来发
	 * 
	 * @param content
	 * @return
	 */
	public static List<Package> explorMessage(List<Package> pkList,
			String spNumber, String chargeNumber, String userNumber,
			String serviceType, String corpId, String messageContent,
			int userCount, SMCCfgSys sys) {
		if (StringUtil.isNull(messageContent))
			return null;
		Package pack = null;
		int len = messageContent.length();
		int mod = len % ONEMESSAGELENGTH;
		int count = len / ONEMESSAGELENGTH;
		if (mod != 0) {
			count = len / ONEMESSAGELENGTH + 1;
		}

		int bindex = 0;
		for (int i = 0; i < count; i++) {// 200
			String c = messageContent.substring(bindex, (i + 1) >= count
					? (ONEMESSAGELENGTH * i + (mod == 0
							? ONEMESSAGELENGTH
							: mod)) : ONEMESSAGELENGTH * (i + 1));
			bindex = ONEMESSAGELENGTH * (i + 1);

			pack = new SMSPackage.Builder(sys.getSpNumber(),
					sys.getChargeNumber(), userNumber.toString(),
					sys.getServiceType(), sys.getCorpId(), c, sys.getFeeType(),
					sys.getFeeValue(), sys.getMoRelateToMtFlag(),
					sys.getReportFlag(), sys.getAgentFlag(), sys.getPriority(),
					sys.getExpireTime(), sys.getScheduleTime(), sys.getTpPid(),
					sys.getTpUdhi(), sys.getMessagecoding())
					.setUserCount(userCount).setMessageLength(c.length())
					.build();
			pkList.add(pack);
		}
		return pkList;
	}

	/**
	 * 文件转化为字节数组
	 */
	public static byte[] getBytesFromFile(File f) {
		if (f == null) {
			return null;
		}
		int bufferSize = 1024;
		try {
			FileInputStream stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(bufferSize);
			byte[] b = new byte[bufferSize];
			int n;
			while ((n = stream.read(b)) != -1)
				out.write(b, 0, n);
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
		}
		return null;
	}

	public static void main(String[] args) {
		int i = SysCfg.getInstance().getMessagelength();
		System.out.println(i);
		
		String  content ="<PHONE>12344555</PHONE>";
		
		int pIndex = content.indexOf(PHONEFLAGBEGIN);
		int pEndIndex = content.indexOf(PHONEFLAGEND);

		int eIndex = content.indexOf(EMAILFLAGBEGIN);
		int eEndIndex = content.indexOf(EMAILFLAGEND);

		String phones[] = null;
		String emails[] = null;

		String phone = content.substring(pIndex + PHONEFLAGBEGIN.length(),
				pEndIndex);
		String email = content.substring(eIndex + EMAILFLAGBEGIN.length(),
				eEndIndex);
		
		
		System.out.println();
	}
}
