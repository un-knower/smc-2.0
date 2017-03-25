package cn.uway.smc.exception;

/**
 * 配置异常类
 */
public class CfgException extends Exception {

	private static final long serialVersionUID = -7175319615456384630L;

	public CfgException() {
		super();
	}

	public CfgException(String message, Throwable cause) {
		super(message, cause);
	}

	public CfgException(String message) {
		super(message);
	}

	public CfgException(Throwable cause) {
		super(cause);
	}

}
