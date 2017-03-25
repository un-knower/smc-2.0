package cn.uway.smc.sender;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.StringUtil;
import cn.uway.smc.db.pojo.SMCCfgSys;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.pack.EmailPackage;
import cn.uway.smc.pack.Package;
import cn.uway.smc.util.ConstDef;
import cn.uway.smc.util.Email;
import cn.uway.smc.util.SysCfg;

public class EmailSender extends AbstractSender {

	private final static Logger LOG = LoggerFactory
			.getLogger(EmailSender.class);

	private String messageId;

	private List<Package> packList = new ArrayList<Package>();

	@Override
	public int send(Package data) {
		int result = -1;
		if (data == null || !(data instanceof EmailPackage))
			return result;

		EmailPackage ePack = (EmailPackage) data;
		Email mail = new Email();

		try {
			// 收件箱地址
			String[] to = ePack.getMailTO();

			// SMTP 服务器
			String host = ePack.getMailSMTPHost();
			host = host.trim();
			// 发件箱地址
			String account = ePack.getMailAccount();
			// 发件箱密码
			String password = ePack.getMailPassword();
			// 发送主题
			String subjcet = ePack.getSubject();
			// 发关内容
			String content = ePack.getContent();

			String attachment = ePack.getStrFileAttachment();
			mail.setAddress(to, Email.TO);
			mail.setSMTPHost(host, account, password);
			mail.setFromAddress(account);
			mail.setSubject(subjcet);
			mail.setHtmlBody(content);
			if (StringUtil.isNotNull(attachment))
				mail.setFileAttachment(attachment);

			try {
				mail.sendBatch();
				result = 0;
				Date now = new Date();
				messageId = String.valueOf(now.getTime())
						+ String.valueOf(content.hashCode());
			} catch (Exception e) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
				}
				mail.sendBatch();

				result = 0;
				Date now = new Date();
				messageId = String.valueOf(now.getTime())
						+ String.valueOf(content.hashCode());
			}
		} catch (UnsupportedEncodingException e) {
			LOG.error("邮件字符编码异常！", e);
			result = -1;
		} catch (AddressException e) {
			LOG.error("邮件地址异常！", e);
			result = -1;
		} catch (MessagingException e) {
			LOG.error("邮件异常！", e);
			result = -1;
		}
		return result;
	}

	@Override
	public String getMessageId() {
		return messageId;
	}

	@Override
	public void close() {

	}

	private Package buildEmailPack(SMCData smcExpressData, SMCCfgSys sys) {
		EmailPackage pack = new EmailPackage();
		pack.setContent(smcExpressData.getContent());
		pack.setMailAccount(sys.getMailUser().trim());
		pack.setMailPassword(sys.getMailPwd().trim());
		pack.setMailSMTPHost(sys.getSmtpHost().trim());

		Map<String, List<String>> users = ConstDef.getPhoneEmail(smcExpressData
				.getToUsers());
		
		List<String> userEmailList = users.get(ConstDef.EMAIL);
		
		if(userEmailList==null || userEmailList.isEmpty())
			return null; 

		List<String> mailTo = new ArrayList<String>();
		for (String email : userEmailList) {
			if (email == null || (email = email.trim()).equals(""))
				continue;
			mailTo.add(email);
		}
		pack.setMailTO(mailTo.toArray(new String[0]));
		pack.setSubject(smcExpressData.getSubject());
		// pack.setTitle(smcExpressData.getEmailTitle());
		String att = smcExpressData.getAttachmentfile();
		if (StringUtil.isNull(att)
				|| SysCfg.getInstance().getEmaildirectory()
						.equalsIgnoreCase(att))
			att = null;
		pack.setStrFileAttachment(att);
		return pack;
	}

	@Override
	public List<Package> builderPackage(SMCData smcExpressData, SMCCfgSys sys) {
		this.smcData = smcExpressData;
		Package pack = buildEmailPack(smcExpressData, sys);
		if(pack==null){
			return packList; 
		}
		packList.add(pack);
		return packList;
	}

	@Override
	public boolean sendAll() {
		int rResult = 0;
		for (Package pack : packList) {
			int result = send(pack);
			// 对于邮件来说，只要结果返回0就表示发送成功，所以sentOkTimes加1
			if (result == 0) {
				rResult++;
				// BussinessMgr.updateExpressSentTimes(this.smcData);
				statistics();
				sendAfter(0, null);

				LOG.debug(smcData + " : 邮件发送成功.");
			} else {
				boolean b = reSendSent(pack);
				if (b) {
					rResult++;
					statistics();
					sendAfter(0, null);
					LOG.debug(smcData + " : 邮件发送成功.");
				} else {
					LOG.debug(smcData + " : 邮件重试发送失败.");
					// sendAfter(-1, "邮件重试发送失败");
				}
			}
		}
		LOG.debug(smcData + " : 发送状态," + rResult + ": " + (rResult > 0));
		return rResult > 0;
	}

	public static void main(String[] args) {

		EmailSender e = new EmailSender();
		for (int i = 0; i < 1; i++) {
			EmailPackage p = new EmailPackage();
			p.setMailSMTPHost("smtp.uway.cn");
			p.setContent("test1111");
			p.setMailAccount("liuwx@uway.cn");
			p.setMailPassword("xiangzi1qazx");
			String ss[] = new String[]{"liuwx@uway.cn"};
			p.setMailTO(ss);
			p.setSubject("test");
			p.setTitle("test111");
			e.send(p);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

}
