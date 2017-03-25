package cn.uway.smc.util;

import java.util.Comparator;

import cn.uway.smc.db.pojo.SMCData;

public class SMCLevelExpressComparator implements Comparator<SMCData> {

	@Override
	public int compare(SMCData o1, SMCData o2) {
		int l1 = o1.getSmcLevel().getLevelid();
		int l2 = o2.getSmcLevel().getLevelid();
		return l1 < l2 ? 1 : (l1 == l2 ? 0 : -1);
	}
}
