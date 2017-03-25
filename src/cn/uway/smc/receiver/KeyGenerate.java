package cn.uway.smc.receiver;

import java.util.Date;

import cn.uway.smc.util.TimeIntervalMgr;

/**
 * 键生成器
 */
public class KeyGenerate {

	private static String space = "";

	public static long generateKey() {

		Date curDbdate = TimeIntervalMgr.getInstance().getDateValue(new Date());

		long dateLong = curDbdate.getTime();

		long i = (long) (Math.random() * 900) + 100;

		String value = dateLong + space + i;

		return Long.valueOf(value);
	}

	public static void main(String[] args) {
		generateKey();
	}

}
