package cn.uway.smc.ui.webservice.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.smc.util.SysCfg;

public class Test {

	private final static Logger log = LoggerFactory.getLogger(Test.class);

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

			String send_time = SysCfg.getInstance().getSMCTestSend_time();
			String to_users = "<PHONE>" + phone + "</PHONE><EMAIL>" + email
					+ "</EMAIL>";
			for (int i = 0; i < 1; i++) {
				log.debug("to_users " + to_users);
				log.debug("email " + email);

				int result = gsp.deliver(username, password, type, src_id,
						level, to_users, send_way, content, send_time);
				log.debug("调用输出结果：  " + result);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
