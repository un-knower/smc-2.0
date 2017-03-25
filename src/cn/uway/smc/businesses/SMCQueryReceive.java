package cn.uway.smc.businesses;

/**
 * 短息查询接收配置对象
 * 
 * @author liuwx 2010-11-17
 */
public class SMCQueryReceive {

	private String sendSMCType; // 短信类型

	private int businessid; // 业务编号

	private String sendSMCContent;// SMC内容

	private String contentexplanAtion;// SMC内容说明

	private int processType;// 处理类型

	private int isNeedRebackSMS;// 是否需要回复 1:需要 0：不需要

	private String reBackSMSContent;// 回复内容

	public String getSendSMCType() {
		return sendSMCType;
	}

	public void setSendSMCType(String sendSMCType) {
		this.sendSMCType = sendSMCType;
	}

	public int getBusinessid() {
		return businessid;
	}

	public void setBusinessid(int businessid) {
		this.businessid = businessid;
	}

	public String getSendSMCContent() {
		return sendSMCContent;
	}

	public void setSendSMCContent(String sendSMCContent) {
		this.sendSMCContent = sendSMCContent;
	}

	public String getContentexplanAtion() {
		return contentexplanAtion;
	}

	public void setContentexplanAtion(String contentexplanAtion) {
		this.contentexplanAtion = contentexplanAtion;
	}

	public int getProcessType() {
		return processType;
	}

	public void setProcessType(int processType) {
		this.processType = processType;
	}

	public int getIsNeedRebackSMS() {
		return isNeedRebackSMS;
	}

	public void setIsNeedRebackSMS(int isNeedRebackSMS) {
		this.isNeedRebackSMS = isNeedRebackSMS;
	}

	public String getReBackSMSContent() {
		return reBackSMSContent;
	}

	public void setReBackSMSContent(String reBackSMSContent) {
		this.reBackSMSContent = reBackSMSContent;
	}

}
