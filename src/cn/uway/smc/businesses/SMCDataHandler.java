package cn.uway.smc.businesses;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.DateUtil;
import cn.uway.commons.type.StringUtil;
import cn.uway.smc.db.pojo.SMCCfgStrategy;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.util.ConstDef;
import cn.uway.smc.util.TimeIntervalMgr;

/**
 * 快递短信息处理
 * 
 */
public class SMCDataHandler implements Runnable {

	private final static Logger LOG = LoggerFactory
			.getLogger(SMCDataHandler.class);

	private SMCData smcData;

	private SMCCfgStrategy strategy;

	public SMCDataHandler(SMCData SMCExpressData) {
		this.smcData = SMCExpressData;
	}

	public void hander() {

		long begin = new Date().getTime();
		try {
			strategy = smcData.getStrategy();
			if (strategy == null) {
				LOG.error("id:{}消息的发送策略为空，不能发送.", smcData.getId());
				return;
			}
			Date now = TimeIntervalMgr.getInstance().getDateValue(new Date());
			LOG.debug(smcData
					+ "  当前时间:  "
					+ DateUtil.getDateString(now)
					+ "  发送时间:  "
					+ (StringUtil.isNull(smcData.getSendTime())
							? "立即发送"
							: smcData.getSendTime()));

			// 判断是否准备好，如果没有准备好，则继续此消息添加到快递消息队列中
			boolean b = BussinessMgr.readyToSend(now, smcData);

			LOG.debug(smcData + " 是否准备好 :  " + b);
			if (b) {
				boolean result = SystemMgr.getInstance().sender(smcData);
				String msg = smcData + ",当前发送结果 " + (result ? "成功" : "失败")
						+ ",suctimes=" + smcData.getSentOkTimes();
				LOG.debug(msg);

			}
		} catch (Exception e) {
			String emailUserNotExist = "javax.mail.SendFailedException: Invalid Addresses";
			String emailservererror = "Could not connect to SMTP host";
		
			String message = e.getMessage();
			if (message.contains(emailUserNotExist)) {
				String msg = smcData + ",当前发送结果 失败" + ",邮件地址不存在. "
						+ smcData.getToUsers();
				LOG.error(msg);

				BussinessMgr.sendAfter(TaskMgr.getInstance().getSys(), smcData,
						ConstDef.EMAILADDRESS_ERROR,
						"邮件地址不存在. " + smcData.getToUsers());
				TaskMgr.getInstance().delActiveTask(smcData.getId());
			} else if (message.contains(emailservererror)) {
				// TODO
			}
			
			LOG.error("--------------- error: "+e.getMessage());

		} finally {
			TaskMgr.getInstance().delActiveTask(smcData.getId());
		}
		long end = new Date().getTime();
		LOG.debug(smcData + "  共消耗时间" + (end - begin) / 1000 + " s ");
	}

	public void run() {
		hander();
	}

	public static void main(String[] args) {

		System.out.println(DateUtil.getDateString(new Date()));
	}
}
