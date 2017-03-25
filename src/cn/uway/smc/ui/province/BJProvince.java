package cn.uway.smc.ui.province;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.commons.type.DateUtil;
import cn.uway.commons.type.StringUtil;
import cn.uway.smc.businesses.BussinessMgr;
import cn.uway.smc.businesses.TaskMgr;
import cn.uway.smc.db.pojo.SMCData;

public class BJProvince extends AbstractProvince {

	private static Logger LOG = LoggerFactory.getLogger(BJProvince.class);

	public BJProvince() {

	}

	public boolean sendMessage(SMCData data) {
		endPort = p.getProperty("endPort");
		endPort = endPort.trim();
		String xml = "<?xml version=\"1.0\" encoding=\"GBK\"?>";
		xml = xml + "<CoreSMS>";
		xml = xml + "<OperID>" + p.getProperty("username") + "</OperID>";
		xml = xml + "<OperPass>" + p.getProperty("password") + "</OperPass>";
		xml = xml + "<Action>Submit</Action>";
		xml = xml + "<Category>0</Category>";
		xml = xml + "<Body>";
		xml = xml + "<SendTime>" + this.getSendTime(data.getSendTime())
				+ "</SendTime>";
		xml = xml + "<AppendID>1234</AppendID>";
		xml = xml + "<Message>";
		xml = xml + "<DesMobile>" + getSendSmsUsers(data.getToUsers())
				+ "</DesMobile>";
		xml = xml + "<Content>" + data.getContent() + "</Content>";
		xml = xml + "</Message>";
		xml = xml + "</Body>";
		xml = xml + "</CoreSMS>";
		String res = "";
		LOG.debug("调用发送开始\n" + xml);

		// desmobile, smsid
		Map<String, String> resultMap = new HashMap<String, String>();

		res = poststh(xml);
		LOG.debug("调用发送结束:" + res);
		int result = -1;
		String cause = "";
		boolean rFlag = false;
		try {
			// 解析相应的xml
			rFlag = returnCode(xml, res, resultMap);
			LOG.info("发送短信状态" + (rFlag ? "成功" : "失败"));
			if (!rFlag) {
				for (int i = 0; i < 2; i++) {
					res = poststh(xml);
					rFlag = returnCode(xml, res, resultMap);
					LOG.info("第{}次尝试,{}", i + 1, (rFlag ? "成功" : "失败"));
					Thread.sleep(1000);
					if (rFlag)
						break;
				}
			}

			if (rFlag)
				result = 0;
			else {
				cause = "发送短信失败";
			}
		} catch (Exception e) {
			LOG.error("发送短信异常", e);
		} finally {
			cause = cause + getResultDesc(resultMap);
			if (result != 0)
				result = -4;
			BussinessMgr.sendAfter(TaskMgr.getInstance().getSys(), data,
					result, cause);
			// 入库
			LOG.debug("发送结果：" + rFlag + "\n" + res);
		}
		return rFlag;
	}

	public String getResultDesc(Map<String, String> resultMap) {

		if (resultMap == null || resultMap.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, String>> set = resultMap.entrySet();
		for (Entry<String, String> entry : set) {
			sb.append(entry.getKey()).append(":").append(entry.getValue())
					.append(";");
		}
		return sb.toString();
	}

	/**
	 * <pre>
	 * 
	 * 
	 * public void common(Document doc, String DesMobile, String SMSID) {
	 * 
	 * 	Node mobileNode = doc
	 * 			.selectSingleNode(&quot;CoreSMS/Body/Code/Message/DesMobile&quot;);
	 * 	if (mobileNode != null) {
	 * 		DesMobile = mobileNode.getText();
	 * 	}
	 * 	Node idNode = doc.selectSingleNode(&quot;CoreSMS/Body/Code/Message/SMSID&quot;);
	 * 	if (idNode != null) {
	 * 		SMSID = idNode.getText();
	 * 	}
	 * }
	 * </pre>
	 */

	@SuppressWarnings("unchecked")
	public boolean returnCode(String request, String response,
			Map<String, String> resultMap) {
		if (StringUtil.isNull(response))
			return false;
		SAXReader reader = new SAXReader();
		try {

			Document doc = null;
			byte[] bytes = response.getBytes();
			InputStream in = new ByteArrayInputStream(bytes);
			Reader strInStream = null;
			try {
				strInStream = new InputStreamReader(in, "GBK");

				doc = reader.read(strInStream);

			} catch (UnsupportedEncodingException e1) {
				LOG.error("读取xml异常", e1);
				return false;
			} finally {
				try {
					IOUtils.closeQuietly(in);
					IOUtils.closeQuietly(strInStream);

				} catch (Exception e) {
				}
			}
			if (doc == null)
				return false;

			Node node = doc.selectSingleNode("CoreSMS/Body/Code");
			String code = null;
			if (node != null) {
				code = node.getText();
			}
			/* code : 0 正常 ， 其他不正常 */
			if ("0".equals(code)) {
				List<Element> messageList = doc
						.selectNodes("CoreSMS/Body/Message");
				if (messageList != null) {
					for (Element e : messageList) {
						List<Element> oneMessage = e.elements();
						String mobile = "";
						String smsid = "";
						for (Element field : oneMessage) {
							String name = field.getName();
							String value = field.getText();

							if ("DesMobile".equalsIgnoreCase(name)) {
								mobile = value;
							}
							if ("SMSID".equalsIgnoreCase(name)) {
								smsid = value;
							}
						}
						if (!resultMap.containsKey(mobile))
							resultMap.put(mobile, smsid);
					}
				}
				return true;
			}

		} catch (DocumentException e) {
			LOG.error("响应信息失败.", e);
		}

		return false;
	}

	/**
	 * 处理xml
	 * 
	 * @param content
	 * @return
	 */
	public String poststh(String content) {// content为你要拼写的XML文档字符串
		URL url = null;
		StringBuilder sb = new StringBuilder();
		try {
			url = new URL(endPort);
		} catch (MalformedURLException e1) {
			LOG.error("连接地址失败{}.{}", endPort, e1);
			return null;
		}
		URLConnection uc = null;
		OutputStreamWriter out = null;
		BufferedReader rd = null;
		try {
			uc = url.openConnection();
			uc.setDoOutput(true); // ....必须设置为'true'.
			uc.setRequestProperty("Content-Type", "text/xml"); // 记住这行不能少否则会出错
			out = new OutputStreamWriter(uc.getOutputStream(), "GBK");
			out.write(content);
			out.flush();
			out.close();

			rd = new BufferedReader(new InputStreamReader(uc.getInputStream(),
					"GBK"));
			String responseStr;
			// String index = "<CoreSMS>";

			while ((responseStr = rd.readLine()) != null) {
				sb.append(responseStr).append("\n");
			}
			// result1 = result1 + sb.substring(sb.indexOf(index));

		} catch (IOException e) {
			LOG.error("发送信息失败{}.{}", endPort, e);
		} finally {
			try {
				IOUtils.closeQuietly(out);
				IOUtils.closeQuietly(rd);
			} catch (Exception e) {
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		// Sms_BJ test = new Sms_BJ();
		// test.message();

		String now = "2013-03-03 01:02:03";
		try {
			System.out.println(DateUtil.getDateString_yyyyMMddHHmmss(new Date(
					DateUtil.getDate(now).getTime())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BJProvince ss = new BJProvince();
		String request = "";
		String response = "<?xml version= \"1.0\" encoding= \"utf-8\"?><CoreSMS><OperID>好样</OperID><OperPass>**********</OperPass><Action>Submit</Action><Category>1</Category><Body><Code>0</Code><Message><DesMobile>13311229868</DesMobile><SMSID>W20130324165623517105</SMSID></Message></Body></CoreSMS>";
		Map<String, String> resultMap = new HashMap<String, String>();
		// ss.test();
		ss.returnCode(request, response, resultMap);

		String aa = "aa<PHONE>4556</PHONE>";
		aa = aa.substring(aa.indexOf("<PHONE>") + 1);
		System.out.println(aa);

	}

}
