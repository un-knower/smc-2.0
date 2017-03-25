package cn.uway.smc.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import SmgwClient.DeliverResp;
import SmgwClient.ErrorCode;
import cn.uway.smc.businesses.AutoSenderMgr;
import cn.uway.smc.businesses.BussinessMgr;
import cn.uway.smc.businesses.SMGPInterfaceMgr;
import cn.uway.smc.db.pojo.SMCData;

/**
 * 电信smgp协议
 */
public class SMGPReceiver extends AbstractReceiver {

	private static final Logger log = LoggerFactory.getLogger(SMGPReceiver.class);

	@Override
	public void run() {
		receive();
	}

	/**
	 * 接收短信
	 */
	public void receive() {
		SMGPInterfaceMgr ui = SMGPInterfaceMgr.getInstance();
		ErrorCode err = new ErrorCode();
		int ret = 0;
		while (true) {
			try {
				log.debug("电信SMGP开始接收短信 ... ");

				DeliverResp deliverresp = new DeliverResp();
				ret = ui.smgpDeliver(20, deliverresp, err);

				if (ret != 0) {
					//log.debug("SMGP短信接受错误码: " + err.GetErrorCodeString());
					Thread.sleep(10000);
					continue;
				}
				log.debug("短消息标识：" + BussinessMgr.rhex(deliverresp.GetMsgID()));
				log.debug("短消息格式："
						+ Integer.toString(deliverresp.GetMsgFormat()));
				log.debug("源号码：" + deliverresp.GetSrcTermID());
				log.debug("是否状态报告："
						+ Integer.toString(deliverresp.GetIsReport()));
				log.debug("内容长度：" + Integer.toString(deliverresp.GetMsgLen()));
				log.debug("HEX内容："
						+ BussinessMgr.rhex(deliverresp.GetMsgContent()));
				log.debug("字符串内容：" + new String(deliverresp.GetMsgContent()));
				log.debug("目的号码：" + deliverresp.GetDestTermID());
				log.debug("短消息接收时间：" + deliverresp.GetRecvTime());
				log.debug("短消息TLVmask："
						+ Integer.toString(deliverresp.GetTLVMask()));
				log.debug("短消息LinkID：" + deliverresp.GetLinkID());
				log.debug("短消息SubmitMsgType："
						+ Integer.toString(deliverresp.GetSubmitMsgType()));
				log.debug("短消息SPDealResult："
						+ Integer.toString(deliverresp.GetSPDealResult()));

				if (deliverresp.GetIsReport() == 1) {
					continue;
				}
				SMCData express = new SMCData();
				express.setId(KeyGenerate.generateKey());
				express.setLevelid(1);
				express.setIsReceiveGw(1);
				express.setContent(null);
				String phone = deliverresp.GetSrcTermID();
				if (!phone.startsWith("86"))
					phone = "86" + phone;
				express.setToUsers(phone);
				express.setSendWay(1);
				express.setSubject(BussinessMgr.rhex(deliverresp
						.GetMsgContent()));
				express.setSpNumber(deliverresp.GetDestTermID());
				AutoSenderMgr.getInstance().put(express);
				Thread.sleep(100L);
			} catch (Exception e) {
				log.error("电信SMGP接收短信出现异常 ... ",e);
				try {
					Thread.sleep(10 * 1000L);
				} catch (Exception e2) {
				
				}
			}
		}
	}
}

/*
 */
