package cn.uway.smc.ui.province;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.util.SysCfg;

public class ProvinceMgr {

	private static Logger LOG = LoggerFactory.getLogger(ProvinceMgr.class);

	private static ProvinceMgr instance = new ProvinceMgr();

	@SuppressWarnings("rawtypes")
	private static Map<String, Class> map = null;

	private  AbstractProvince provinceHandle = null;

	static {
		map = new HashMap<String, Class>();
		map.put("GD", GdProvince.class);
		map.put("HUB", HbProvince.class);
		map.put("BJ", BJProvince.class);
	    map.put("JSTEL", JSTelProvince.class);
	}

	public synchronized static ProvinceMgr getInstance() {
		if (instance == null)
			instance = new ProvinceMgr();

		return instance;
	}

	private ProvinceMgr() {
	}

	@SuppressWarnings("rawtypes")
	public AbstractProvince createProvince(String cityId) throws Exception {
		if (provinceHandle != null)
			return provinceHandle;

		Class handerClass = map.get(cityId);
		if (handerClass != null) {
			provinceHandle = (AbstractProvince) handerClass.newInstance();
		} else
			throw new Exception("创建个性化工厂类失败.");

		return provinceHandle;
	}

	public static void main(String[] args) {
		boolean isused = SysCfg.getInstance().getIsUsed();
		try {
			String cityId = SysCfg.getInstance().getSmcCityEn();

			AbstractProvince provinceMgr = ProvinceMgr.getInstance()
					.createProvince(cityId);

			SMCData data = new SMCData();

			if (isused) {
				provinceMgr.sendMessage(data);
			} else {
				// smcDataMgr.put(expressData);
			}

		} catch (Exception e1) {
			LOG.error(": 出现异常.  ", e1);

			// return -1;
		}
	}
}
