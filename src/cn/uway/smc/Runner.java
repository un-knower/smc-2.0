package cn.uway.smc;

import static cn.uway.smc.util.ConstDef.LOGCFG_FILE_URL;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import cn.uway.ews.EWS;
import cn.uway.smc.businesses.AlarmStatisticsMgr;
import cn.uway.smc.businesses.AutoSenderMgr;
import cn.uway.smc.businesses.TaskMgr;
import cn.uway.smc.receiver.ReceiveMgr;
import cn.uway.smc.util.SysCfg;

/**
 * SMCServer服务类
 * 
 */
public class Runner {

	private final static Logger LOG = LoggerFactory.getLogger(Runner.class);

	private EWS ews = null;

	/** 每次发布请，修改版本号。 */
	public static final String APP_VERSION = "2.0.3.4";

	public Runner(int port, String webApp, String contextPath) {
		ews = new EWS(port, contextPath, webApp);
	}

	/** 启动Http Server */
	public boolean startServer() throws Exception {
		return ews.start();
	}

	/** 停止Http Server */
	public boolean stopServer() throws Exception {
		return ews.stop();
	}

	/**
	 * 主函数类
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try {
			LOG.info("【程序版本】{}", APP_VERSION);

			printSysInfo();

			SysCfg sysConf = SysCfg.getInstance();
			boolean b = initLogger(); // 初始化系统日志模块
			if (!b) {
				System.err.println("SMCenter启动失败,原因：系统日志模块加载失败.");
				return;
			}

			int port = sysConf.getWebPort();
			String context = "/";
			Server server = new Server(port);
			WebAppContext ctx = new WebAppContext("./web", context);
			server.addHandler(ctx);
			server.start();
			LoggerFactory.getLogger(Runner.class).info(
					"WEB端口已启动，端口：{}，上下文路径：{}", new Object[]{port, context});

		} catch (Exception e) {
			throw new Exception("WebAppContext 启动失败");
		}
		try {
			TaskMgr.getInstance().start();

			boolean isused = SysCfg.getInstance().getIsUsed();
			if (!isused) {
				// 启动短信息接收器
				ReceiveMgr.getInstance().start();
			}
			AutoSenderMgr.getInstance().start();
			AlarmStatisticsMgr.getInstance().start();
		} catch (Exception e) {
			LOG.error("启动失败,原因:{}", e);
		}
	}

	private static void printSysInfo() {
		Properties props = System.getProperties();
		Iterator<Entry<Object, Object>> it1 = props.entrySet().iterator();
		LOG.info("==============================================");
		while (it1.hasNext()) {
			Entry<Object, Object> entry = it1.next();
			LOG.info("【系统信息】{}：{}",
					new Object[]{entry.getKey(), entry.getValue()});
		}
		LOG.info("==============================================");
		Iterator<Entry<String, String>> it2 = System.getenv().entrySet()
				.iterator();
		while (it2.hasNext()) {
			Entry<String, String> entry = it2.next();
			LOG.info("【环境变量】{}：{}",
					new Object[]{entry.getKey(), entry.getValue()});
		}
		LOG.info("==============================================");
	}

	/**
	 * 加载系统日志功能
	 */
	private static boolean initLogger() {
		boolean b = true;
		ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
		LoggerContext loggerContext = (LoggerContext) loggerFactory;
		JoranConfigurator jc = new JoranConfigurator();
		try {
			File f = new File(LOGCFG_FILE_URL);
			jc.setContext(loggerContext);
			jc.doConfigure(f);
		} catch (JoranException e) {
			b = false;
		}
		return b;
	}

}
