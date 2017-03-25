package cn.uway.smc.pack;

import SmgwClient.ErrorCode;
import SmgwClient.SmgpMsgID;

/**
 * 电信SMGP协议包
 */
public class SMGPPackage implements Package {

	// 消息类型
	private int msgType;

	// 是否需要状态报
	private int needReport;

	// 消息发送优先级别
	private int nsgLevel;

	// 业务代码
	private String serviceID;

	// 消息格式
	private int msgFormat;

	// 计费类型
	private String feeType;

	// 计费代码
	private String feeCode;

	// 固定费用
	private String fixedFee;

	// 有效期
	private String validTime;

	// 定时发送时间
	private String atTime;

	// 计费用户号码
	private String chargeTermID;

	// 目的用户号码
	private String destTermID;

	// 源用户号码
	private String replyPath;

	// 短消息长度，如果该值为0，表示短消息内容在以sMsgContent所表示的文件中 byte[]
	private int msgLen;

	// 短消息内容 byte[]
	private byte[] msgContent;

	// 保留字段 输入/输出参数：SmgpMsgID sMsgID 短消息ID。
	private byte[] reserve;

	// SmgpMsgID是用来保存短消息ID的类。函数调用结束后 i)方法可得到MSGID。
	private SmgpMsgID msgID;

	// 如SMGPSendSingle
	private ErrorCode errorCode;

	// 可选参数掩码
	private int tlvMask;

	// GSM协议类型
	private int tpPid;

	// GSM协议类型
	private int tp_udhi;

	// SPMS分配的关联上下行消息的唯一标识
	private byte[] linkID;

	// 计费用户类型
	private int chargeUserType;

	// 计费用户的号码类型
	private int chargeTermType;

	// 计费用户的伪码
	private byte[] chargeTermPseudo;

	// 短消息接收方的号码类型
	private int destTermType;

	// 短消息接收方的伪码
	private byte[] destTermPseudo;

	// 相同Msg_Id的消息总条数
	private int pkTotal;

	// 相同Msg_Id的消息序号，从1开始
	private int pkNumber;

	// 消息类型
	private int submitMsgType;

	// 对原请求的处理结果通知
	private int sPDealResult;

	// 信息内容的来源
	private byte[] msgSrc;

	// 业务代码
	private byte[] mServiceID;

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public int getNeedReport() {
		return needReport;
	}

	public void setNeedReport(int needReport) {
		this.needReport = needReport;
	}

	public int getNsgLevel() {
		return nsgLevel;
	}

	public void setNsgLevel(int nsgLevel) {
		this.nsgLevel = nsgLevel;
	}

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	public int getMsgFormat() {
		return msgFormat;
	}

	public void setMsgFormat(int msgFormat) {
		this.msgFormat = msgFormat;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getFixedFee() {
		return fixedFee;
	}

	public void setFixedFee(String fixedFee) {
		this.fixedFee = fixedFee;
	}

	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}

	public String getAtTime() {
		return atTime;
	}

	public void setAtTime(String atTime) {
		this.atTime = atTime;
	}

	public String getChargeTermID() {
		return chargeTermID;
	}

	public void setChargeTermID(String chargeTermID) {
		this.chargeTermID = chargeTermID;
	}

	public String getDestTermID() {
		return destTermID;
	}

	public void setDestTermID(String destTermID) {
		this.destTermID = destTermID;
	}

	public String getReplyPath() {
		return replyPath;
	}

	public void setReplyPath(String replyPath) {
		this.replyPath = replyPath;
	}

	public int getMsgLen() {
		return msgLen;
	}

	public void setMsgLen(int msgLen) {
		this.msgLen = msgLen;
	}

	public byte[] getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(byte[] msgContent) {
		this.msgContent = msgContent;
	}

	public byte[] getReserve() {
		return reserve;
	}

	public void setReserve(byte[] reserve) {
		this.reserve = reserve;
	}

	public SmgpMsgID getMsgID() {
		return msgID;
	}

	public void setMsgID(SmgpMsgID msgID) {
		this.msgID = msgID;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public int getTlvMask() {
		return tlvMask;
	}

	public void setTlvMask(int tlvMask) {
		this.tlvMask = tlvMask;
	}

	public int getTpPid() {
		return tpPid;
	}

	public void setTpPid(int tpPid) {
		this.tpPid = tpPid;
	}

	public int getTp_udhi() {
		return tp_udhi;
	}

	public void setTp_udhi(int tp_udhi) {
		this.tp_udhi = tp_udhi;
	}

	public byte[] getLinkID() {
		return linkID;
	}

	public void setLinkID(byte[] linkID) {
		this.linkID = linkID;
	}

	public int getChargeUserType() {
		return chargeUserType;
	}

	public void setChargeUserType(int chargeUserType) {
		this.chargeUserType = chargeUserType;
	}

	public int getChargeTermType() {
		return chargeTermType;
	}

	public void setChargeTermType(int chargeTermType) {
		this.chargeTermType = chargeTermType;
	}

	public byte[] getChargeTermPseudo() {
		return chargeTermPseudo;
	}

	public void setChargeTermPseudo(byte[] chargeTermPseudo) {
		this.chargeTermPseudo = chargeTermPseudo;
	}

	public int getDestTermType() {
		return destTermType;
	}

	public void setDestTermType(int destTermType) {
		this.destTermType = destTermType;
	}

	public byte[] getDestTermPseudo() {
		return destTermPseudo;
	}

	public void setDestTermPseudo(byte[] destTermPseudo) {
		this.destTermPseudo = destTermPseudo;
	}

	public int getPkTotal() {
		return pkTotal;
	}

	public void setPkTotal(int pkTotal) {
		this.pkTotal = pkTotal;
	}

	public int getPkNumber() {
		return pkNumber;
	}

	public void setPkNumber(int pkNumber) {
		this.pkNumber = pkNumber;
	}

	public int getSubmitMsgType() {
		return submitMsgType;
	}

	public void setSubmitMsgType(int submitMsgType) {
		this.submitMsgType = submitMsgType;
	}

	public int getsPDealResult() {
		return sPDealResult;
	}

	public void setsPDealResult(int sPDealResult) {
		this.sPDealResult = sPDealResult;
	}

	public byte[] getMsgSrc() {
		return msgSrc;
	}

	public void setMsgSrc(byte[] msgSrc) {
		this.msgSrc = msgSrc;
	}

	public byte[] getmServiceID() {
		return mServiceID;
	}

	public void setmServiceID(byte[] mServiceID) {
		this.mServiceID = mServiceID;
	}

}
