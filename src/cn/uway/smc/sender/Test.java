package cn.uway.smc.sender;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import SmgwClient.ErrorCode;
import SmgwClient.SmgpMsgID;
import SmgwClient.UserInterface;

public class Test {

	private static Logger log = LoggerFactory.getLogger(Test.class);

	private UserInterface ui = new UserInterface();

	ErrorCode err = new ErrorCode();

	public void send() {

		try {
			int nMsgType = 0;

			int nNeedReport = 1;

			int nMsgLevel = 1;

			String nServiceID = "test";

			int nMsgFormat = 15;

			String sFeeType = "01";

			String sFeeCode = "000010";

			String sFixedFee = "000100";

			String sValidTime = "";

			String sAtTime = "";

			String sChargeTermID = "106593610";

			String sDestTermID = "13316905278";

			String sReplyPath = "106593610";

			byte[] sMsgContent = "test sms ".getBytes("gbk");;

			int nMsgLen = sMsgContent.length;

			byte[] sReserve = "reserve".getBytes();

			int nTLVMask = 16777215;

			int nTP_pid = 0;

			int nTP_udhi = 1;

			byte[] sLinkID = "89021135".getBytes();//

			int nChargeUserType = 1;

			int nChargeTermType = 1;

			byte[] sChargeTermPseudo = "fbgahdfjkgdahs".getBytes();//

			int nDestTermType = 1;

			byte[] sDestTermPseudo = "dsfdfas".getBytes(); //

			int nPkTotal = 1;

			int nPkNumber = 1;

			int nSubmitMsgType = 1;

			int nSPDealResult = 1;

			byte[] sMsgSrc = "mylove".getBytes();//

			byte[] sMServiceID = "test".getBytes();//

			SmgpMsgID msgid = new SmgpMsgID();

			int ret = ui.SMGPSendSingle(nMsgType,

			nNeedReport,

			nMsgLevel,

			nServiceID,

			nMsgFormat,

			sFeeType,

			sFeeCode,

			sFixedFee,

			sValidTime,

			sAtTime,

			sChargeTermID,

			sDestTermID,

			sReplyPath,

			nMsgLen,

			sMsgContent,

			sReserve,

			msgid,

			err,

			nTLVMask,

			nTP_pid,

			nTP_udhi,

			sLinkID,

			nChargeUserType,

			nChargeTermType,

			sChargeTermPseudo,

			nDestTermType,

			sDestTermPseudo,

			nPkTotal,

			nPkNumber,

			nSubmitMsgType,

			nSPDealResult,

			sMsgSrc,

			sMServiceID);

			String rtn = err.GetErrorCodeString();
			log.error(rtn +  " "+ err.GetErrorCodeValue());
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
	}

	public static void main(String[] args) {
		Test test = new Test();
		test.send();
	}
}
