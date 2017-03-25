package cn.uway.smc.businesses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.smc.db.dao.SmcCfgForbidPhoneDAO;

/**
 * 管理禁止对用户发送短信，以及恢复对用户发送短信息
 * 
 * @author liuwx
 */
public class ForbidphoneMgr {

	private final static Logger log = LoggerFactory
			.getLogger(ForbidphoneMgr.class);

	/**
	 * Map<电话号码，ForbidPhone对象>
	 */
	public static Map<String, List<Integer>> FORBIDMAP = new java.util.concurrent.ConcurrentHashMap<String, List<Integer>>();

	private ForbidphoneMgr() {
		init();
	}

	private static class SMCSenderMgrContainer {

		private static ForbidphoneMgr instance = new ForbidphoneMgr();
	}

	public static ForbidphoneMgr getInstance() {
		return SMCSenderMgrContainer.instance;
	}

	/**
	 * 添加一个禁用号码
	 * 
	 * @param phone
	 */
	public synchronized void add(ForbidPhone phone) {
		if (!FORBIDMAP.containsKey(phone.getPhone())) {
			List<Integer> list = new ArrayList<Integer>();
			list.add(phone.getBusinessId());
			FORBIDMAP.put(phone.getPhone(), list);
		} else {
			List<Integer> ls = FORBIDMAP.get(phone.getPhone());
			if (!ls.contains(phone.getBusinessId()))
				ls.add(phone.getBusinessId());
		}
	}

	/**
	 * 添加一组禁用业务编号
	 * 
	 * @param phone
	 *            电话号码
	 * @param list
	 *            格式:List<业务编号>
	 */
	public synchronized void addList(String phone, List<String> list) {
		for (String id : list) {
			ForbidPhone fPhone = new ForbidPhone(phone, Integer.parseInt(id));
			add(fPhone);
		}
	}

	/**
	 * @param phone
	 *            电话号码
	 * @param busIdlist
	 *            格式:List<业务编号>
	 * @return 一组禁用业务编号
	 */
	public synchronized List<ForbidPhone> builderList(String phone,
			List<String> busIdlist) {
		List<ForbidPhone> ls = new ArrayList<ForbidPhone>();
		for (String id : busIdlist) {
			ForbidPhone p = new ForbidPhone(phone, Integer.valueOf(id));
			ls.add(p);
		}
		return ls;
	}

	/**
	 * 移除一个应用号码
	 * 
	 * @param phone
	 *            禁用号码对象
	 */
	public synchronized void remove(ForbidPhone phone) {
		if (FORBIDMAP.containsKey(phone.getPhone())) {
			List<Integer> ls = FORBIDMAP.get(phone.getPhone());
			ls.remove(phone.getBusinessId());
			if (ls.size() == 0)
				remove(phone.getPhone());
		}
	}

	/**
	 * 移除一个禁用号码所具有的业务
	 * 
	 * @param phone
	 *            电话号码
	 */
	public synchronized void remove(String phone) {
		if (FORBIDMAP.containsKey(phone)) {
			FORBIDMAP.remove(phone);
		}
	}

	/**
	 * 初始化,从数据库中加载被禁用的手机号码以及业务号码
	 */
	private void init() {
		SmcCfgForbidPhoneDAO dao = new SmcCfgForbidPhoneDAO();
		try {
			FORBIDMAP = dao.map();
		} catch (Exception e) {
			log.error("加载禁用接受电话号码出现错误");
		}
	}

}
