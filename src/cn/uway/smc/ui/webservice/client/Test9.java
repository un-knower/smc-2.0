package cn.uway.smc.ui.webservice.client;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.smc.util.SysCfg;

public class Test9 {

	private final static Logger log = LoggerFactory.getLogger(Test9.class);

	public static void main(String[] args) {

		try {

			String clientKey = "---------------key-----------------";
			log.debug(clientKey);
			SysCfg sysConf = SysCfg.getInstance();
			int port = sysConf.getWebPort();

			String endpoint = "http://localhost:" + port
					+ "/services/SmcService?wsdl";

			// 调用过程
			Service service = new Service();

			Call call = (Call) service.createCall();

			call.setTargetEndpointAddress(new java.net.URL(endpoint));

			call.setOperationName("deliver");// WSDL里面描述的操作名称

			call.addParameter("username",
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);// 操作的参数
			call.addParameter("password",
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);// 操作的参数
			call.addParameter("type", org.apache.axis.encoding.XMLType.XSD_INT,
					javax.xml.rpc.ParameterMode.IN);// 操作的参数

			call.addParameter("src_id",
					org.apache.axis.encoding.XMLType.XSD_INT,
					javax.xml.rpc.ParameterMode.IN);// 操作的参数
			call.addParameter("level",
					org.apache.axis.encoding.XMLType.XSD_INT,
					javax.xml.rpc.ParameterMode.IN);// 操作的参数
			call.addParameter("to_users",
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);// 操作的参数
			call.addParameter("send_way",
					org.apache.axis.encoding.XMLType.XSD_INT,
					javax.xml.rpc.ParameterMode.IN);// 操作的参数
			call.addParameter("content",
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);// 操作的参数
			call.addParameter("send_time",
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);// 操作的参数

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

				call.setReturnType(org.apache.axis.encoding.XMLType.XSD_INT);// 设置返回类型

				call.setUseSOAPAction(true);

				// 给方法传递参数，并且调用方法
				Object[] obj = new Object[]{username, password, type, src_id,
						level, to_users, send_way, content, send_time};
				int result = (Integer) call.invoke(obj);

				System.out.println("Result is : " + result);
				log.debug("调用输出结果：  " + result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}