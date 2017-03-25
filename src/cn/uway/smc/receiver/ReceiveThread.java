package cn.uway.smc.receiver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spApi.Bind;
import spApi.BindResp;
import spApi.Deliver;
import spApi.DeliverResp;
import spApi.Report;
import spApi.ReportResp;
import spApi.SGIP_Command;
import spApi.Unbind;
import spApi.UnbindResp;
import spApi.Userrpt;
import spApi.UserrptResp;
import cn.uway.smc.businesses.AutoSenderMgr;
import cn.uway.smc.db.dao.SmcCfgSysDAO;
import cn.uway.smc.db.pojo.SMCCfgSys;
import cn.uway.smc.db.pojo.SMCData;

/**
 * 联通sgip协议 ReceiveThread
 */
public class ReceiveThread extends AbstractReceiver implements Runnable {

	private static Logger log = LoggerFactory.getLogger(ReceiveThread.class);

	String sourceNumber = null;

	String sqNumber = null;

	String messageContent = null;

	Connection connect = null;

	String strSQL = null;

	ReceiveAgent rat = null;

	boolean runReceiveFlag = true;

//	ServerSocket serversocket = null;

	public ReceiveThread(ReceiveAgent rt) {
		this.rat = rt;
	}

	private int read() {
		int r = -1;
		if ((this.rat.so == null) || (!this.rat.connectrecflag)) {
			this.rat.listenReceive();
			log.debug("  restart the receive server ......... ");
		}
		if (this.rat.so != null) {
			r = this.rat.readPack();
			log.debug(" ReceiveThread readPack   ============= ");
		}
		return r;
	}

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		this.rat.listenReceive();
		while (this.runReceiveFlag) {
			try {
				log.debug(" receive  is running ============= ");

				read();

				if (!this.rat.connectrecflag) {
					this.rat.listenReceive();
					this.rat.connectrecflag = true;
					this.rat.readPack();
				}

				try {
					Thread.currentThread().sleep(3000l);
				} catch (InterruptedException localInterruptedException1) {

				}

				if (this.rat.islogin) {
					log.debug("rat.intTemp:" + this.rat.result);
					if (this.rat.result == 1) {
						String content = this.rat.messageContent.replaceAll(
								"；", ";").replaceAll("＃", "#");
						log.debug("TYPE: DELIVER SPNumber : "
								+ this.rat.spNumber + " SourceNumber :"
								+ this.rat.sourceNumber + " Report_ID : "
								+ this.rat.reportID + " MessageContent : "
								+ content);

						SMCData express = new SMCData();
						express.setId(KeyGenerate.generateKey());
						express.setLevelid(1);
						express.setIsReceiveGw(1);
						express.setContent(null);
						String phone = this.rat.sourceNumber.trim();
						if (!phone.startsWith("86"))
							phone = "86" + phone;
						express.setToUsers(phone);
						express.setSendWay(1);
						express.setSubject(content);
						express.setSpNumber(this.rat.spNumber);

						AutoSenderMgr.getInstance().put(express);
						log.debug(" put the receive message to dateSet ,id=   "
								+ express.getId());
					}

					if (this.rat.result == 2) {
						log.debug("rat.TYPE:{};", "REPORT");
						log.debug("rat.SPNumber:{};", this.rat.spNumber);
						log.debug("rat.MoRecordID:{};", this.rat.moRecordID);
						log.debug("rat.Report_ID:{};",
								Integer.valueOf(this.rat.reportID));
						log.debug("rat.ErrorCode:{};",
								Integer.valueOf(this.rat.errorCode));
						this.rat.close();
					}

					log.debug(" do after .... ");
				} else {
					log.info("非法用户尝试进行请求，危险*****************************");
				}
			} catch (Exception e) {
				log.error("接收联通上行消息异常:" + e);
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e1) {
				}
			}
		}
	}
}

class ReceiveAgent {

	private static Logger log = LoggerFactory.getLogger(ReceiveAgent.class);

	// 定义所有公用变量
	String sourceNumber = null; // 源手机号码

	String messageContent = null; // 短消息内容

	String spNumber = null; //

	int Data_Coding; // 数据编码

	// 临时变量，接收消息类型如：-1,0,1,2,3
	// -1表示错误，0表示BIND和UNBIND消息，1表示上行消息（接收），2表示状态报告，3表示用户状态报告
	int result = 0;

	byte[] temp = new byte[160]; // 临时数组，接收到的短消息内容

	ServerSocket serversocket = null;

	Socket so = null;

	OutputStream output = null;

	InputStream input = null;

	SGIP_Command sgipCmd = null;

	Deliver deliver = null;

	DeliverResp deliverresp = null;

	Report report = null;

	ReportResp reportresp = null;

	Userrpt userrpt = null;

	UserrptResp userrptresp = null;

	Bind active = null;

	Unbind unbind = null;

	BindResp resp = null;

	UnbindResp unresp = null;

	SGIP_Command command = null;

	int seq1 = 0;

	int seq2 = 0;

	int seq3 = 0;

	String moRecordID = "0";

	int reportID = 0;

	int errorCode = 0;

	String loginName = "";

	String loginPwd = "";

	boolean islogin = false;

	int nodeID = 0;

	int sockServerPort = 0;

	boolean connectrecflag = true;

	private SMCCfgSys syscfg = null;

	public ReceiveAgent() {
		super();
		try {
			syscfg = new SmcCfgSysDAO().list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (syscfg != null) {
			sockServerPort = syscfg.getServerReceivePort();// ;
			nodeID = syscfg.getNodeId();
		}

		log.debug("sockServerPort  " + sockServerPort + "  nodeID  " + nodeID);
	}

	// 初始化SOCKET服务器并监听消息
	public void listenReceive() {
		close();
		command = new SGIP_Command();
		try {
			serversocket = new ServerSocket(sockServerPort);
			so = serversocket.accept();
			input = new DataInputStream(so.getInputStream());
			output = new DataOutputStream(so.getOutputStream());
			connectrecflag = true;
		} catch (Exception e) {
			log.error("初始化SOCKET服务器时异常！" + e);
			connectrecflag = false;
		}
	}

	// 对消息进行解包
	public int readPack() {
		try {
			log.debug("read..................before ");
			log.debug("read  inputstream bytes size : " + input.available());
			// if ( input.available() == 0 )
			// return -99;
			sgipCmd = command.read(input);
			log.debug("read..................end ");

			int commandId = command.getCommandID();
			log.debug("RecAgent ID:{}", commandId);
			switch (commandId) {
				case SGIP_Command.ID_SGIP_BIND : {// 为BIND消息
					active = (Bind) sgipCmd;
					log.debug("BIND...");
					int result = active.readbody();// 解包
					int loginType = active.GetLoginType();
					log.debug("result:{};loginType:{};", result, loginType);
					log.debug("flag:" + active.GetFlag() + ";seq1:"
							+ active.getSeqno_1() + ";seq2:"
							+ active.getSeqno_2() + ";seq3:"
							+ active.getSeqno_3() + ";");
					loginName = active.GetLoginName();
					if (loginName == null)
						loginName = "";// 得到登陆名
					loginPwd = active.GetLoginPassword();
					if (loginPwd == null)
						loginPwd = "";// 得到登陆密码

					if (loginName.trim().equals(
							syscfg.getServerReceiveUserName())// "wypt_S"
							&& loginPwd.trim().equals(
									syscfg.getServerReceivePwd())) {// 判断名和密码是否正确，如果正确，则返回一个BINDRESP的应答消息，消息值为0
						resp = new BindResp(nodeID, 0);
						resp.write(output);
						result = 0;
						islogin = true;
						break;
					} else {// 如果登陆名和密码不对，则返回一个应答包，值为1
						log.debug("not login");
						resp = new BindResp(nodeID, 1);
						resp.write(output);
						result = -1;
						islogin = false;
						break;
					}
				}
				case SGIP_Command.ID_SGIP_UNBIND : {// 为UNBIND消息,返回应答包即可
					log.debug("UNBIND...");
					unbind = (Unbind) sgipCmd;
					unresp = new UnbindResp(nodeID);
					unresp.write(output);
					result = 0;
					break;
				}
				case SGIP_Command.ID_SGIP_DELIVER : {// 为DELIVER消息，即用户上行的消息
					log.debug("DELIVER...");
					deliver = (Deliver) sgipCmd;
					deliver.readbody();
					sourceNumber = deliver.getUserNumber();// 得到用户的手机号码
					spNumber = deliver.getSPNumber();// 得到用户发送给对方的号码
					Data_Coding = deliver.getMessageCoding();// 得到消息编码
					temp = deliver.getMessageContent();// 得到消息内容
					// 如果SP号有86，则去掉
					if (spNumber.length() > 2 && spNumber.startsWith("86")) {
						spNumber = spNumber.substring(2);
					}
					spNumber = spNumber.trim();
					log.debug("data_code:" + Data_Coding);
					if (Data_Coding == 0) {// 如果编码为0，则用8859解码
						messageContent = new String(temp, "iso-8859-1");
					} else if (Data_Coding == 8) {// 如编码为8，则用UNICODE解码
						messageContent = new String(temp, "iso-10646-ucs-2");
					}
					log.debug("receivedContent:" + messageContent);
					deliverresp = new DeliverResp(nodeID, 0);// 返回成功接收DELIVER消息的应答包
					deliverresp.SetResult(0);
					deliverresp.write(output);
					result = 1;
					break;
				}
				case SGIP_Command.ID_SGIP_REPORT : {// 为状态报告
					log.debug("REPORT");
					report = (Report) sgipCmd;
					int result = report.readbody();
					reportID = report.getState();// 得到报告状态
					log.debug("Report_result:{};", result);
					errorCode = report.getErrorCode();// 如果失败，则可以得到错误码
					seq1 = report.getSeq_1();
					seq2 = report.getSeq_2();
					seq3 = report.getSeq_3();
					moRecordID = Integer.toHexString(seq1)
							+ Integer.toHexString(seq2)
							+ Integer.toHexString(seq3);// 组合成消息ID

					reportresp = new ReportResp(nodeID, 0);// 返回成功接收REPORT的消息应答包
					reportresp.SetResult(0);
					reportresp.write(output);
					result = 2;
					break;
				}
				case SGIP_Command.ID_SGIP_USERRPT : {// 暂不用
					userrpt = (Userrpt) sgipCmd;
					userrpt.readbody();
					userrptresp = new UserrptResp(nodeID, 0);
					userrptresp.SetResult(12);
					userrptresp.write(output);
					result = 3;
					break;
				}
			}
		} catch (Exception e) {
			log.error("短信接收,读取信息包异常:" + e);
			connectrecflag = false;
		}
		return result;
	}

	/**
	 * 断开与服务器的连接
	 */
	private void exitGW() {
		try {
			unbind = new Unbind(nodeID);
			unbind.write(output);
		} catch (Exception e) {
			log.error("断开网关异常:exitSMS()" + e.getMessage());
		}
	}

	/**
	 * 从网关断开连接，先发送UNBIND包，如返回值为0，则断开网关成功，否则失败
	 * 
	 * @return
	 */
	public boolean disconnect() {
		int result = 1;
		try {
			exitGW();
			result = readPack();
		} catch (Exception e) {
			log.error("断开网关异常:" + e.getMessage());
		}
		return result == 0;
	}

	// 关闭SOKCET服务
	public void close() {
		try {
			// disconnect();// add
			if (so != null) {
				so.close();
			}
			if (serversocket != null) {
				serversocket.close();
			}
			so = null;
			serversocket = null;
		} catch (Exception e) {
			log.error("关闭接收代理socket时异常！");
		}
	}
}
