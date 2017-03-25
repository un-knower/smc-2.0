package cn.uway.smc.receiver;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.smc.db.dao.SmcCfgSourceDAO;
import cn.uway.smc.util.SysCfg;

/**
 * 接收管理器 ReceiveMgr
 */
public class ReceiveMgr {

	private static final Logger LOG = LoggerFactory.getLogger(ReceiveMgr.class);

	private List<String> sourceList = null;

	private ReceiveMgr() {

	}

	public static ReceiveMgr getInstance() {
		return SMCSenderMgrContainer.instance;
	}

	private static class SMCSenderMgrContainer {

		private static ReceiveMgr instance = new ReceiveMgr();
	}

	private void init() throws Exception {
		this.sourceList = new SmcCfgSourceDAO().listId();
		if ((this.sourceList == null) || (this.sourceList.size() <= 0)) {
			String msg = "信息源为空，请配置信息源";
			throw new Exception(msg);
		}
	}

	public void start() throws Exception {
		LOG.debug("启动短信接收器");
		try {
			init();

			if (SysCfg.getInstance().getSmsProtocol() == 1) {
				ReceiveThread rt1 = new ReceiveThread(new ReceiveAgent());
				Thread rt = new Thread(rt1, "sgip协议接收线程");
				rt.start();
			} else {
				SMGPReceiver reiver = new SMGPReceiver();
				Thread r = new Thread(reiver, "电信smgp协议接收线程");
				r.start();
			}
		} catch (Exception e) {
			LOG.debug("短信接收线程出现异常,原因:{}", e);
		}
	}

	public List<String> getSourceList() {
		return this.sourceList;
	}

	public void setSourceList(List<String> sourceList) {
		this.sourceList = sourceList;
	}

	public static void main(String[] args) throws Exception {
		getInstance().start();
	}

}
