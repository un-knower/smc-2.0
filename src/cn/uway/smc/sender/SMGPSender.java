package cn.uway.smc.sender;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.DateUtil;
import cn.uway.commons.type.StringUtil;
import cn.uway.smc.businesses.ForbidphoneMgr;
import cn.uway.smc.businesses.SMGPInterfaceMgr;
import cn.uway.smc.db.pojo.SMCCfgSys;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.pack.Package;
import cn.uway.smc.util.ConstDef;
import cn.uway.smc.util.SysCfg;

public class SMGPSender extends AbstractSender {

	private static Logger LOG = LoggerFactory.getLogger(SMGPSender.class);

	@Override
	public int send(Package data) {
		return 0;
	}

	@Override
	public String getMessageId() {
		return null;
	}

	@Override
	public void close() {

	}

	/**
	 *
	 */
	public boolean sendAll3() {
		boolean bFlag = false;
		String allPhones = buildSmgpSmsPhone(smcData);

		String[] phones = allPhones.split(",");

		List<Boolean> resultList = new ArrayList<Boolean>();

		for (String phone : phones) {
			if (StringUtil.isNull(phone))
				continue;
			int result = 1;
			try {
				StringBuilder sb = new StringBuilder();

				sb.append("msgType:").append(SmgpParaMgr.getInstance().getMsgType()).append(" ");
				sb.append("needReport:").append(SmgpParaMgr.getInstance().getNeedReport()).append(" ");
				sb.append("msgLevel:").append(SmgpParaMgr.getInstance().getMsgLevel()).append(" ");
				sb.append("serviceId:").append(SmgpParaMgr.getInstance().getServiceId()).append(" ");
				sb.append("msgFormat:").append(SmgpParaMgr.getInstance().getMsgFormat()).append(" ");
				sb.append("feeType:").append(SmgpParaMgr.getInstance().getFeeType()).append(" ");
				sb.append("feecode:").append(SmgpParaMgr.getInstance().getFeecode()).append(" ");
				sb.append("fixedFee:").append(SmgpParaMgr.getInstance().getFixedFee()).append(" ");
				sb.append("chargeTermId:").append(SmgpParaMgr.getInstance().getChargeTermId()).append(" ");
				sb.append("spNumber:").append(SmgpParaMgr.getInstance().getSpNumber()).append(" ");

				LOG.debug(sb.toString());

				if (StringUtil.isNotNull(allPhones)) {
					result = SMGPInterfaceMgr.getInstance().SMGPSendSingle(phone, smcData.getContent());
				}
				if (result == 0) {
					resultList.add(true);
				}

			} catch (Exception e) {
				LOG.error(smcData + ",信息发送失败.", e);
			}
		}

		if (resultList.size() > 0) {
			LOG.debug(smcData + ",发送成功，并准备添加到快递历史表");
			statistics();
			sendAfter(0, null);
			bFlag = true;

		} else {
			LOG.debug(smcData + ",发送失败");
		}
		return bFlag;
	}

	/**
	 *
	 */
	public boolean sendAll() {
		boolean bFlag = false;
		String pNum = buildSmgpSmsPhone(smcData);
		String phonePath = this.getSmgpPhonePath(smcData, pNum);
		List<String> contentPathList = this.getSmgpContentPath(smcData);

		int count = 0;
		for (String contentPath : contentPathList) {
			int result = 1;
			try {
				StringBuilder sb = new StringBuilder();

				sb.append("msgType:").append(SmgpParaMgr.getInstance().getMsgType()).append(" ");
				sb.append("needReport:").append(SmgpParaMgr.getInstance().getNeedReport()).append(" ");
				sb.append("msgLevel:").append(SmgpParaMgr.getInstance().getMsgLevel()).append(" ");
				sb.append("serviceId:").append(SmgpParaMgr.getInstance().getServiceId()).append(" ");
				sb.append("msgFormat:").append(SmgpParaMgr.getInstance().getMsgFormat()).append(" ");
				sb.append("feeType:").append(SmgpParaMgr.getInstance().getFeeType()).append(" ");
				sb.append("feecode:").append(SmgpParaMgr.getInstance().getFeecode()).append(" ");
				sb.append("fixedFee:").append(SmgpParaMgr.getInstance().getFixedFee()).append(" ");
				sb.append("chargeTermId:").append(SmgpParaMgr.getInstance().getChargeTermId()).append(" ");
				sb.append("spNumber:").append(SmgpParaMgr.getInstance().getSpNumber()).append(" ");

				LOG.debug(sb.toString());

				if (StringUtil.isNotNull(pNum)) {
					// result = SMGPInterfaceMgr.getInstance().sendBatch(0, 1,
					// 1,
					// "test", 15, "01", "000010", "000100", "", "",
					// sys.getChargeNumber(), phonePath, sys.getSpNumber(), 0,
					// contentPath.getBytes(), "reserve".getBytes());
					result = SMGPInterfaceMgr.getInstance().sendBatch(Integer.valueOf(SmgpParaMgr.getInstance().getMsgType()),
							Integer.valueOf(SmgpParaMgr.getInstance().getNeedReport()), Integer.valueOf(SmgpParaMgr.getInstance().getMsgLevel()),
							SmgpParaMgr.getInstance().getServiceId(), Integer.valueOf(SmgpParaMgr.getInstance().getMsgFormat()),
							SmgpParaMgr.getInstance().getFeeType(), SmgpParaMgr.getInstance().getFeecode(), SmgpParaMgr.getInstance().getFixedFee(),
							"", "", SmgpParaMgr.getInstance().getChargeTermId(), phonePath, SmgpParaMgr.getInstance().getSpNumber(), 0,
							contentPath.getBytes(), "reserve".getBytes());

					/*
					 * result = SMGPInterfaceMgr.getInstance().SMGPSendSingle(Integer .valueOf(SmgpParaMgr.getInstance() .getMsgType(),
					 * Integer.valueOf(SmgpParaMgr.getInstance() .getNeedReport(), Integer.valueOf(SmgpParaMgr.getInstance() .getMsgLevel(),
					 * SmgpParaMgr.getInstance().getServiceId(), Integer.valueOf(SmgpParaMgr.getInstance() .getMsgFormat(),
					 * SmgpParaMgr.getInstance().getFeeType(), SmgpParaMgr.getInstance().getFeecode(), SmgpParaMgr.getInstance().getFixedFee(),
					 * sValidTime", sAtTime", SmgpParaMgr.getInstance().getChargeTermId(), sDestTermID, sReplyPath, nMsgLen, sMsgContent, sReserve,
					 * sMsgID, nErrorCode, nTLVMask, nTP_pid, nTP_udhi, sLinkID, nChargeUserType, nChargeTermType, sChargeTermPseudo, nDestTermType,
					 * sDestTermPseudo, nPkTotal, nPkNumber, nSubmitMsgType, nSPDealResult, sMsgSrc, sMServiceID);
					 */

				}

				if (result == 0) {
					LOG.debug(smcData + ",发送成功，并 添加到快递历史表.内容路径： " + contentPath);
					statistics();
					count++;
					sendAfter(0, null);
					bFlag = true;
					deleteFile(contentPath);
				} else {
					bFlag = false;
					LOG.debug(smcData + ",信息发送失败.内容路径： " + contentPath);
					sendAfter(-1, "信息发送失败");
				}

			} catch (Exception e) {
				LOG.error(smcData + ",信息发送失败.", e);
			}

		}
		if (count == contentPathList.size())
			deleteFile(phonePath);
		return bFlag;
	}

	private void deleteFile(String file) {
		File f = new File(file);
		// if (f.exists())
		// f.delete();

	}

	@Override
	public List<Package> builderPackage(SMCData smcExpressData, SMCCfgSys sys) {
		this.sys = sys;
		this.smcData = smcExpressData;
		return null;
	}

	private String buildSmgpSmsPhone(SMCData smcData) {
		String toUsers = smcData.getToUsers();
		Map<String, List<String>> users = ConstDef.getPhoneEmail(toUsers);
		List<String> userPhoneList = users.get(ConstDef.PHONE);
		if (userPhoneList == null)
			return null;

		// 网关是否支持群发，支持群发，就以群发方式发送，如果不支持，将用户号码拆分为多个号码发送
		boolean isGroupSender = SysCfg.getInstance().isGroupSender();
		List<String> userPhones = new ArrayList<String>();

		int userCount = 0;
		StringBuilder userNumber = new StringBuilder();
		for (String cell : userPhoneList) {
			if (cell == null || (cell = cell.trim()).equals(""))
				continue;
			if (!cell.startsWith("86"))
				cell = "86" + cell;

			boolean b = false;
			// 判断用户号码是否被禁用，如果被禁用，将不会将短信发给此用户
			synchronized (ForbidphoneMgr.FORBIDMAP) {
				List<Integer> list = ForbidphoneMgr.FORBIDMAP.get(cell);
				if (list != null) {
					for (Integer f : list) {
						if (f == smcData.getSrcid()) {
							b = true;
							break;
						}
					}
				}
			}
			if (b)
				continue;

			LOG.debug(smcData + ", 是否禁用:" + b);

			if (!cell.startsWith("86")) {
				userNumber.append("86" + cell).append(",");
				userPhones.add("86" + cell);
			} else {
				userNumber.append(cell).append(",");
				userPhones.add(cell);
			}

			if (!isGroupSender) {
				userCount = 1;
			} else {
				userCount++;
			}
		}

		if (userNumber.length() > 0) {
			LOG.debug(smcData + ", 发送号码个数为" + userCount);
			userNumber.deleteCharAt(userNumber.length() - 1);
		} else {
			LOG.debug(smcData + ", 用户号码为空");
			return null;
		}
		if (StringUtil.isNull(smcData.getContent()))
			return null;

		return userNumber.toString();
	}

	/**
	 * @param smcExpressData
	 * @param userNumber
	 *            用户号码
	 * @return
	 */
	public String getSmgpPhonePath(SMCData smcExpressData, String userNumber) {
		String common = getSmgpSmsPath() + File.separator + DateUtil.getDateString_yyyyMMddHHmmssSSS(new Date());
		String smgpPath = common + "_P_" + smcExpressData.getId() + ConstDef.FILE_SUFFIX;
		FileWriter fw = null;
		try {
			File f = new File(smgpPath);
			if (!f.exists())
				f.createNewFile();
			fw = new FileWriter(smgpPath);

			String[] ns = userNumber.split(",");
			for (String s : ns) {
				if (s.startsWith("86"))
					s = s.substring(2);
				fw.write(s);
				fw.write("\n");
			}
			fw.flush();

		} catch (IOException e) {
			LOG.error(smcData + ": 获取SMGP短信电话号码文件路径失败,原因:{}", e);
		} finally {
			try {
				if (fw != null) {
					fw.flush();
					fw.close();
				}
			} catch (IOException e) {
			}

		}
		return smgpPath;

	}

	public List<String> getSmgpContentPath(SMCData smcExpressData) {
		String common = getSmgpSmsPath() + File.separator + DateUtil.getDateString_yyyyMMddHHmmssSSS(new Date());
		List<String> contentPathList = new ArrayList<String>();

		String content = new String(smcExpressData.getContent());

		int contentLen = content.length();

		int oneSmsLen = SysCfg.getInstance().getMessagelength();
		int count = (int) Math.ceil((float) contentLen / (float) oneSmsLen);

		for (int i = 0; i < count; i++) {

			String smgpContentPath = common + "_C_" + smcExpressData.getId() + "_" + (i + 1) + ConstDef.FILE_SUFFIX;
			String contentTmp = content.substring(i * oneSmsLen, oneSmsLen * (i + 1) > contentLen ? contentLen : (oneSmsLen * (i + 1)> contentLen? contentLen: oneSmsLen * (i + 1)));
			LOG.error(smcData + " count: "+count+": "+ contentTmp);
			FileWriter fwContent = null;
			try {
				File f = new File(smgpContentPath);
				if (!f.exists())
					f.createNewFile();
				fwContent = new FileWriter(smgpContentPath);
				fwContent.write(contentTmp);

				contentPathList.add(smgpContentPath);

			} catch (IOException e) {
				LOG.error(smcData + ": 获取SMGP短信内容文件路径失败,原因:{}", e);
			} finally {
				try {
					if (fwContent != null) {
						fwContent.flush();
						fwContent.close();
					}
				} catch (IOException e) {

				}

			}

		}

		return contentPathList;
	}

	public static String getSmgpSmsPath() {
		String path = System.getProperty("user.home");
		String fname = "sms_tmp";
		File smsFile = new File(path + File.separator + fname);
		if (!smsFile.exists() && !smsFile.isDirectory()) {
			smsFile.mkdir();
		}
		return smsFile.getAbsolutePath();
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		// int i = 12345;
		/*
		 * System.out.println(String.valueOf(i).length() + " " + String.valueOf(i).getBytes().length); String cc = "1好人"; char ccc = '好';
		 * System.out.println(cc.length() + " " + cc.getBytes("gbk").length + " "); System.out.println(cc.getBytes().length + "  :-- " +
		 * cc.getBytes("gbk").length); char[] cs = cc.toCharArray(); System.out.println(cc.length() + ": : " + cs.length); String aa = "好人"; String
		 * content = new String(aa.getBytes("gbk")); String content1 = new String(aa.getBytes("utf-8")); System.out.println("gbk : " + content +
		 * " utf-8: " + content1);
		 * 
		 * String t =
		 * "hfjkds中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国中国hfsdkj<img src='sasa' /> fjldsajflkdsjaflkdsjalf <img src='sada' ait=''/>sfdsfadas" ;
		 * 
		 * String utf8 = new String(t.getBytes("UTF-8")); System.out.println(utf8); String unicode = new String(utf8.getBytes(), "UTF-8");
		 * System.out.println(unicode); String gbk = new String(unicode.getBytes("GBK"));
		 * 
		 * System.out.println(gbk);
		 */
		String common = getSmgpSmsPath() + File.separator + DateUtil.getDateString_yyyyMMddHHmmssSSS(new Date());
		List<String> contentPathList = new ArrayList<String>();

		
		int count1 = (int) Math.ceil((float) 107 / (float) 64);

		System.out.println((double) (107 / 64));

		String id = "11";

		String content = "以下是用java将GBK字符转成UTF-8编码格式， 那么反过来，如何将UTF-8字符转成GBK编码格式呢,,测试向StringBuilder中插入字符和字符串的性能区别。 这个性能区别主要是字符串需要创建类造成的";

		int contentLen = content.length();

		int oneSmsLen = SysCfg.getInstance().getMessagelength();
		int count = (int) Math.ceil((float) contentLen / (float) oneSmsLen);

		for (int i = 0; i < count; i++) {

			String smgpContentPath = common + "_C_" + 1 + "_" + (i + 1) + ConstDef.FILE_SUFFIX;
			String contentTmp = content.substring(i * oneSmsLen, oneSmsLen * (i + 1) > contentLen ? contentLen : (oneSmsLen * (i + 1)> contentLen? contentLen: oneSmsLen * (i + 1)));
			LOG.error( " count: "+count+": "+ contentTmp);
			FileWriter fwContent = null;
			try {
				File f = new File(smgpContentPath);
				if (!f.exists())
					f.createNewFile();
				fwContent = new FileWriter(smgpContentPath);
				fwContent.write(contentTmp);

				contentPathList.add(smgpContentPath);

			} catch (IOException e) {
				LOG.error( ": 获取SMGP短信内容文件路径失败,原因:{}", e);
			} finally {
				try {
					if (fwContent != null) {
						fwContent.flush();
						fwContent.close();
					}
				} catch (IOException e) {

				}

			}

		}
	}
}
