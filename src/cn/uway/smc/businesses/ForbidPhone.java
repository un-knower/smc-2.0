package cn.uway.smc.businesses;

/**
 * 禁用电话号码对象
 * 
 * @author liuwx 2010-11-17
 */
public class ForbidPhone {

	private String phone;// 手机号码

	private int businessId;// 业务ID

	private String sendSMSContent;// 短信消息内容

	public ForbidPhone(String phone, int businessId) {
		this.phone = phone;
		this.businessId = businessId;
	}

	public ForbidPhone() {
		super();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getBusinessId() {
		return businessId;
	}

	public void setBusinessId(int businessId) {
		this.businessId = businessId;
	}

	public String getSendSMSContent() {
		return sendSMSContent;
	}

	public void setSendSMSContent(String sendSMSContent) {
		this.sendSMSContent = sendSMSContent;
	}

}
