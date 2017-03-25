package cn.uway.smc.businesses;

/**
 * 发送短信息统计对象结构
 */
public class AlarmStatistics {

	/**
	 * 每小时发送个数统计
	 */
	private int hourSendCount;

	/**
	 * 每天发送个数统计
	 */
	private int daySendCount;

	/**
	 * 当天各个小时发送短信息总和
	 */
	private int hourCountSum;

	public synchronized int getHourSendCount() {
		return hourSendCount;
	}

	public synchronized void setHourSendCount(int hourSendCount) {
		this.hourSendCount = hourSendCount;
	}

	public synchronized int getDaySendCount() {
		return daySendCount;
	}

	public synchronized void setDaySendCount(int daySendCount) {
		this.daySendCount = daySendCount;
	}

	public synchronized int getHourCountSum() {
		return hourCountSum;
	}

	public synchronized void setHourCountSum(int hourCountSum) {
		this.hourCountSum = hourCountSum;
	}

}
