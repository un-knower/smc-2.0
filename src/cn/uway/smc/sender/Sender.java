package cn.uway.smc.sender;

import java.util.List;

import cn.uway.smc.db.pojo.SMCCfgSys;
import cn.uway.smc.db.pojo.SMCData;
import cn.uway.smc.pack.Package;

public interface Sender {

	public boolean sendAll();

	/**
	 * 消息发送方法
	 * 
	 * @param smsPack
	 *            :封装消息的数据包
	 * @return int:0表示成功，其它都表示失败
	 */
	public int send(Package data);

	/** 返回此消息的消息编号 */
	public String getMessageId();

	// 关闭连接，释放资源
	public void close();

	/** 构建消息包 **/
	public List<Package> builderPackage(SMCData smcExpressData, SMCCfgSys sys);
}
