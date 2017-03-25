package cn.uway.smc.ui.province;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.soap.SOAPException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.message.SOAPHeaderElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.DateUtil;
import cn.uway.commons.type.StringUtil;
import cn.uway.smc.db.conn.DBUtil;
import cn.uway.smc.db.dao.SmcDataHistoryDAO;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.db.pojo.SMCDataHistory;
import cn.uway.smc.util.ConstDef;

public class GdProvince extends AbstractProvince {

	private static String SERVICE_URL = null;

	private static String AP_NAME = null;

	private static String AP_PWD = null;

	public GdProvince() {
		init();
	}

	private final static Logger LOG = LoggerFactory.getLogger(GdProvince.class);

	public static int sendSMS(String username, String password, int type,
			int src_id, int level, String to_users, int send_way,
			String content, String send_time) {
		LOG.debug("连接DotNet WebService短信接口发送短信.");

		int failCount = 0;
		String[] phones = getPhonesByToUsers(to_users);
		if (phones.length <= 0) {
			LOG.warn("phones is null, phones:" + to_users);
			return -5;
		}

		Service service = new Service();
		Call call = null;
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(SERVICE_URL));
		} catch (Exception e) {
			LOG.error("连接.Net WebService 服务创建异常。原因：", e);
			return -1;
		}
		call.setOperationName(new QName("http://tempuri.org/", "SendSMS"));
		call.addParameter("KeyNO", org.apache.axis.encoding.XMLType.XSD_LONG,
				ParameterMode.IN);
		call.addParameter("PHONE", org.apache.axis.encoding.XMLType.XSD_STRING,
				ParameterMode.IN);
		call.addParameter("SMSContent",
				org.apache.axis.encoding.XMLType.XSD_STRING, ParameterMode.IN);
		call.setUseSOAPAction(true);
		call.setSOAPActionURI("http://tempuri.org/SendSMS");
		call.setReturnType(XMLType.XSD_BOOLEAN);// 返回的数据类型

		for (int i = 0; i < phones.length; i++) {
			String user = phones[i];

			// 由于需要认证，故需要设置调用的用户名和密码。
			SOAPHeaderElement soapHeaderElement = new SOAPHeaderElement(
					"http://tempuri.org/", "CredentialSoapHeader");
			soapHeaderElement.setNamespaceURI("http://tempuri.org/");
			try {
				soapHeaderElement.addChildElement("SystemID").setValue(AP_NAME);
				soapHeaderElement.addChildElement("PassWord").setValue(AP_PWD);
				call.addHeader(soapHeaderElement);
			} catch (SOAPException e) {
				LOG.error("消息头权限验证失败，原因: ", e);
				continue;
			}

			String messageid = DateUtil
					.getDateString_yyyyMMddHHmmss(new Date())
					+ (Math.random() * 100000);
			long keyNO = Long.parseLong(messageid);
			boolean res = true;
			try {
				res = (Boolean) call.invoke(new Object[]{messageid, user,
						content});
				if (res)
					failCount++;
				LOG.debug("编号:" + keyNO + ", 短息发送号码给用户:" + user + ", 返回值: "
						+ (res ? "成功" : "失败"));
			} catch (RemoteException e) {
				LOG.debug("编号:" + keyNO + ", 短息发送号码给用户:[" + user + "]异常。原因：", e);
			}

			j_GetSMSMsg(keyNO);

			SMCDataHistory history = new SMCDataHistory();
			Date tmpDate = new Date();
			try {
				history.setOccurTime(tmpDate);
				history.setContent(content);
				history.setId(DBUtil.getSeq(ConstDef.SEQ_EXPRESS));
				history.setSrcId(src_id);
				if (!res)
					history.setCause("调用DotNet WebService 接口发送短信出现异常.");
				history.setLevelId(level);
				history.setOccurTime(tmpDate);
				history.setStampTime(tmpDate);
				history.setSendTime(send_time);
				history.setSendTimeExclude(null);
				history.setSendWay(send_way);
				history.setSentResult(res ? 0 : -1);
				SmcDataHistoryDAO dao = new SmcDataHistoryDAO();
				dao.add(history);
			} catch (Exception e) {
				LOG.error("编号:" + keyNO + ", 编号消息记录到历史库失败. 原因：", e);
			}
		}

		LOG.debug("调用DotNet短信webservice接口 ,发送状态: " + failCount);
		return failCount > 0 ? 0 : -1;
	}

	/**
	 * @Explain 解析手机号码
	 * @Date 2012-4-16 下午4:03:01
	 * @author tianjing
	 * @method getAllUserList
	 * @param 输入参数
	 * @return String[]
	 * @Exception 抛出的异常
	 */
	public static String[] getPhonesByToUsers(String toUsers) {
		// <PHONE>137342;1043233</PHONE> <EMAIL>fdsa@uway.cn;</EMAIL>
		int index = ("<PHONE>").length();
		int lastIndex = toUsers.lastIndexOf(("</PHONE>"));

		toUsers = toUsers.substring(index, lastIndex);

		String[] phones = toUsers.split(";");
		List<String> result = new ArrayList<String>();

		for (String sub : phones) {
			if (StringUtil.isNotNull(sub))
				result.add(sub);
		}

		return result.toArray(new String[result.size()]);
	}

	public static int j_GetSMSMsg(long keynum) {
		Service service = new Service();
		Call call;
		try {
			call = (Call) service.createCall();

			call.setTargetEndpointAddress(new java.net.URL(SERVICE_URL));
			call.setOperationName(new QName("http://tempuri.org/",
					"J_GetSMSMsg"));
			call.addParameter("KeyNO",
					org.apache.axis.encoding.XMLType.XSD_LONG, ParameterMode.IN);
			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
			call.setUseSOAPAction(true);
			call.setSOAPActionURI("http://tempuri.org/J_GetSMSMsg");
			try {
				// 由于需要认证，故需要设置调用的用户名和密码。
				SOAPHeaderElement soapHeaderElement = new SOAPHeaderElement(
						"http://tempuri.org/", "CredentialSoapHeader");
				soapHeaderElement.setNamespaceURI("http://tempuri.org/");

				soapHeaderElement.addChildElement("SystemID").setValue(AP_NAME);
				soapHeaderElement.addChildElement("PassWord").setValue(AP_PWD);
				call.addHeader(soapHeaderElement);
			} catch (SOAPException e) {
				LOG.debug("消息头权限验证失败，原因: " + e);
				throw e;
			}
			String key = String.valueOf(keynum);
			String retStr = (String) call.invoke(new Object[]{key});

			LOG.debug("编号: " + keynum + ", 查询短信发送情况: " + retStr);
		} catch (Exception e) {
			LOG.error("调用DotNet 短信webservice接口出现异常", e);
		}

		return 0;
	}

	public static void main(String[] args) {
		// Random ran1 = new Random();
		// Random ran2 = new Random(12345);
		// // 创建了两个类Random的对象。
		// System.out.println("The 1st set of random numbers:");
		// System.out.println("\t Integer:" + ran1.nextInt());
		// System.out.println("\t Long:" + ran1.nextLong());
		// System.out.println("\t Float:" + ran1.nextFloat());
		// System.out.println("\t Double:" + ran1.nextDouble());
		// System.out.println("\t Gaussian:" + ran1.nextGaussian());
		// // 产生各种类型的随机数
		// System.out.print("The 2nd set of random numbers:");
		// for (int i = 0; i < 5; i++)
		// {
		// System.out.println(ran2.nextInt() + " ");
		// if ( i == 2 )
		// System.out.println();
		// // 产生同种类型的不同的随机数。
		// System.out.println();
		// }
		// int i = (int) (Math.random() * 1000);
		// System.out.println(i);

		String toUsers = "<PHONE>137342;1043233</PHONE> <EMAIL>fdsa@uway.cn;</EMAIL>";
		int index = ("<PHONE>").length();
		int lastIndex = toUsers.lastIndexOf(("</PHONE>"));

		toUsers = toUsers.substring(index, lastIndex);

		String[] phones = toUsers.split(";");
		for (int i = 0; i < phones.length; i++) {
			System.out.println(phones[i]);
		}

	}

	public void init() {
		SERVICE_URL = p.getProperty("endPort");
		AP_NAME = p.getProperty("username");
		AP_PWD = p.getProperty("password");
	}

	@Override
	public boolean sendMessage(SMCData data) {
		int result = sendSMS(data.getUsername(), data.getPassword(),
				data.getType(), data.getSrcid(), data.getLevelid(),
				data.getToUsers(), data.getSendWay(), data.getContent(),
				data.getSendTime());
		return result >= 0;
	}

}
