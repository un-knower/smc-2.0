package cn.uway.smc.ui.webservice.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Date;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.DateUtil;
import cn.uway.smc.util.SysCfg;

public class Test5 {

	private final static Logger log = LoggerFactory.getLogger(Test5.class);

	public static void main(String[] args) throws ServiceException,
			RemoteException {
		String clientKey = "---------------key-----------------";
		log.debug(clientKey);
		SysCfg sysConf = SysCfg.getInstance();
		int port = sysConf.getWebPort();

		String url = "http://localhost:" + port + "/services/SmcService?wsdl";

		SmcServiceServiceLocator service = new SmcServiceServiceLocator();
		SmcService_PortType gsp;
		try {
			gsp = service.getSmcService(new URL(url));

			String username = SysCfg.getInstance().getSMCTestUsername();
			String password = SysCfg.getInstance().getSMCTestPassword();
			int type = 2;
			int src_id = SysCfg.getInstance().getSMCTestSrcid();
			int level = SysCfg.getInstance().getSMCTestLevelId();

			String phone = SysCfg.getInstance().getSMCTestPhone();
			String email = SysCfg.getInstance().getSMCTestEmail();
			String content = SysCfg.getInstance().getContent();
			int send_way = SysCfg.getInstance().getSendWay();
			// String releaseTime = SysCfg.getInstance().getReleaseTime();
			String to_users = "<PHONE>" + phone + "</PHONE><EMAIL>" + email
					+ "</EMAIL>";
			for (int i = 0; i < 200; i++) {
				log.debug("to_users " + to_users);
				log.debug("email " + email);

				Date d = new Date();
				String send_time = DateUtil.getDateString(new Date(
						d.getTime() + 0 * 1000));
				int result = gsp.deliver(username, password, type, src_id,
						level, to_users, send_way, send_time + " " + content
								+ "" + i, send_time);
				log.debug("调用输出结果：  " + result);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

}
