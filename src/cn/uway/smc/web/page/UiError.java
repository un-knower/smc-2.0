package cn.uway.smc.web.page;

import cn.uway.ews.param.WebError;

public class UiError extends WebError {

	private String code;

	private String des;

	private String cause;

	private String action;

	public UiError() {
		super();
	}

	public UiError(String code, String des, String cause, String action) {
		super();
		this.code = code;
		this.des = des;
		this.cause = cause;
		this.action = action;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
