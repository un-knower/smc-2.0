package cn.uway.smc.ui.province;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.DateUtil;
import cn.uway.commons.type.StringUtil;
import cn.uway.smc.businesses.BussinessMgr;
import cn.uway.smc.businesses.TaskMgr;
import cn.uway.smc.db.conn.DBUtil;
import cn.uway.smc.db.dao.SmcDataHistoryDAO;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.db.pojo.SMCDataHistory;
import cn.uway.smc.util.ConstDef;

public class JSTelProvince extends AbstractProvince
{
  private static String DB_URL = "DB_URL";
  private static String DB_DRIVER = "DB_DRIVER";
  private static String DB_USERNAME = "DB_USERNAME";
  private static String DB_PASSWORD = "DB_PASSWORD";
  private String sql = "INSERT INTO SmsSendQueue.dbo.SmsSendQueue (SendDate ,SmsReceiver ,SmsContent ,SmsSentLabel ,AllDaySms) VALUES(?,?,?,?,?)";
  

  private static final Logger LOG = LoggerFactory.getLogger(JSTelProvince.class);

  public JSTelProvince()
  {
    init();
  }

  public int sendSMS(SMCData data)
  {
	String to_users= data.getToUsers();
	String content=data.getContent(); 
	String send_time=data.getSendTime(); 
	  
    String dbUrl = p.getProperty(DB_URL);
    String dbDriver = p.getProperty(DB_DRIVER);
    String dbUsername = p.getProperty(DB_USERNAME);
    String dbPassword = p.getProperty(DB_PASSWORD);
    
    String[] users = getPhonesByToUsers(to_users);
    int result = 0;
    Connection conn = null;
    PreparedStatement st = null;
    
    if(StringUtil.isNull(send_time)){
    	send_time=DateUtil.getDateString(new Date());
    }
    
    String cause= "";
    try
    {
      conn = DBUtil.getConnection(dbDriver.trim(), dbUrl.trim(), dbUsername.trim(), dbPassword.trim());

      for (String user : users)
      {
        st = conn.prepareStatement(this.sql);
        st.setString(1, send_time);
        st.setString(2, user);
        st.setString(3, content);
        st.setInt(4, 0);
        st.setInt(5, 1);
        st.executeUpdate();
      }

    }
    catch (Exception e)
    {
      result = -1;
      cause= e.getMessage();
      if(cause.length()>100)
    	  cause=cause.substring(0,100);
      LOG.error("插入库失败", e);
    }
    finally
    {
      DBUtil.close(null, st, conn);
    }
	BussinessMgr.sendAfter(TaskMgr.getInstance().getSys(), data,
			result, cause);

	// 入库
	LOG.debug(data+ "发送结果：" +( result==0 ));

    return result;
  }
	public static void storeHistorySMSMsg(SMCDataHistory history,
			String calledNumber, long id ) {
		try {
			history.setId(id);
			history.setToUsers(calledNumber);

			SmcDataHistoryDAO expressDataHistoryDAO = new SmcDataHistoryDAO();
			expressDataHistoryDAO.add(history);
		} catch (Throwable e) {
			LOG.error("短信息历史数据存储失败，原因：", e);
		}
	}

  public static String[] getPhonesByToUsers(String toUsers)
  {
    int index = "<PHONE>".length();
    int lastIndex = toUsers.lastIndexOf("</PHONE>");

    toUsers = toUsers.substring(index, lastIndex);

    String[] phones = toUsers.split(";");

    return phones;
  }

  public static void main(String[] args)
  {
    JSTelProvince pro = new JSTelProvince();
    String toUsers = "<PHONE>137342;1043233</PHONE> <EMAIL>fdsa@uway.cn;</EMAIL>";

   // pro.sendSMS("", "", 1, 1, 1, toUsers, 1, toUsers, "");
  }

  public void init()
  {
  }

  public boolean sendMessage(SMCData data)
  {
    int result = sendSMS(data);
    return result >= 0;
  }
}