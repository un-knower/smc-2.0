package cn.uway.smc.sender;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import cn.uway.commons.type.StringUtil;

/**
 * <pre>
 *    @param nMsgType 消息类型
 * @param nNeedReport 是否需要状态报告
 * @param nMsgLevel 消息发送优先级别
 * @param nServiceID 业务代码
 * @param nMsgFormat 消息格式
 * @param sFeeType 计费类型
 * @param sFeeCode 计费代码
 * @param sFixedFee 固定费用
 * @param sChargeTermID 计费用户号码
 * </pre>
 * 
 */
public class SmgpParaMgr {

	private static String file = System.getProperty("user.dir")
			+ File.separator + "smgpc.ini";

	private static Properties smgpPara = new Properties();

	private SmgpParaMgr() {
		try {
			init();
		} catch (Exception e) {
		}
	}

	public static SmgpParaMgr getInstance() {

		return SmgpParaContain.para;
	}

	private static class SmgpParaContain {

		private static SmgpParaMgr para = new SmgpParaMgr();

	}

	public String getPropert(String paraName) {
		return (String) smgpPara.get(paraName);
	}

	public Properties init() throws IOException {

		InputStream is = new FileInputStream(file);
		smgpPara.load(is);
		return smgpPara;
	}

	public String getMsgType() {
		String value = getPropert("msgType");

		if (StringUtil.isNull(value))
			value = "";
		return value.trim();
	}

	public String getNeedReport() {

		String value = getPropert("needReport");
		if (StringUtil.isNull(value))
			value = "";
		return value.trim();

	}

	public String getMsgLevel() {

		String value = getPropert("msgLevel");

		if (StringUtil.isNull(value))
			value = "";
		return value.trim();
	}

	public String getServiceId() {

		String value = getPropert("serviceId");

		if (StringUtil.isNull(value))
			value = "";
		return value.trim();

	}

	public String getMsgFormat() {

		String value = getPropert("msgFormat");

		if (StringUtil.isNull(value))
			value = "";
		return value.trim();

	}

	public String getFeeType() {

		String value = getPropert("feeType");

		if (StringUtil.isNull(value))
			value = "";
		return value.trim();

	}

	public String getFeecode() {

		String value = getPropert("feecode");

		if (StringUtil.isNull(value))
			value = "";
		return value.trim();

	}

	public String getFixedFee() {

		String value = getPropert("fixedFee");

		if (StringUtil.isNull(value))
			value = "";
		return value.trim();

	}

	public String getChargeTermId() {

		String value = getPropert("chargeTermId");

		if (StringUtil.isNull(value))
			value = "";
		return value.trim();

	}

	public String getSpNumber() {

		String value = getPropert("spNumber");

		if (StringUtil.isNull(value))
			value = "";
		return value.trim();

	}

	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));

		String ss = SmgpParaMgr.getInstance().getMsgType();
		System.out.println(ss);

		String getChargeTermId = SmgpParaMgr.getInstance().getChargeTermId();
		System.out.println(getChargeTermId);

		String getSpNumber = SmgpParaMgr.getInstance().getSpNumber();
		System.out.println(getSpNumber);
		String getServiceId = SmgpParaMgr.getInstance().getServiceId();
		System.out.println(getServiceId);

	}
}
