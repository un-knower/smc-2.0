package cn.uway.smc.util;

public class Util {

	public static final int PAGE_SIZE = 15;

	public static boolean isNull(String input) {
		if (input == null || input.equalsIgnoreCase("null") || input.trim().equals(""))
			return true;
		return false;
	}
}
