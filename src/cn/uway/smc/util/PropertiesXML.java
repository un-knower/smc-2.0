package cn.uway.smc.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.smc.exception.CfgException;

/**
 * 以键值对的方式，对XML中各节点进行读写。键是"xx.xx.xx"的形式，指明xml中的元素路径。
 * 
 * @author ChenSijiang
 * @since 1.0
 */
public class PropertiesXML {

	private static Logger logger = LoggerFactory.getLogger(PropertiesXML.class);

	// 存放要进行操作的xml文档对象
	private Document document;

	// xml文件的位置
	private String xmlLocation;

	// 存放键值对，例如：<"system.web.debug",
	// "true">，表示xml中system元素下的web元素下的debug元素值为true
	private Map<String, String> propertiesMap = new HashMap<String, String>();

	/**
	 * 构造方法，在参数中指定要读取的xml文件的位置
	 * 
	 * @param xmlLocation
	 *            xml文件的位置
	 * @throws CfgException
	 *             装载xml时异常
	 */
	public PropertiesXML(String xmlLocation) throws CfgException {
		this.xmlLocation = xmlLocation;
		loadXML();
	}

	/**
	 * 设置一个属性，如果没有此属性，将会发生异常。成功的话，将把新值写入xml
	 * 
	 * @param name
	 *            属性名
	 * @param value
	 *            属性值
	 * @throws CfgException
	 *             属性不存在时
	 */
	public void setProperty(String name, String value) throws CfgException {
		Node selectedNode = select(propertyToXPath(name));
		if (selectedNode == null) {
			throw new CfgException("此属性不存在:" + name);
		} else {
			selectedNode.setText(value);
			write();
			propertiesMap.put(name, value);
		}
	}

	/**
	 * 根据属性名，获取到属性值，如果属性不存在，将返回null
	 * 
	 * @param name
	 *            属性名
	 * @return 属性值，如果属性不存在，将返回null
	 */
	public String getProperty(String name) {
		if (propertiesMap.containsKey(name)) {
			return propertiesMap.get(name);
		}

		String xpath = propertyToXPath(name);
		Node selectedNode = select(xpath);

		if (selectedNode == null) {
			return null;
		}
		String value = selectedNode.getText();
		propertiesMap.put(name, value);

		return value;
	}

	/**
	 * 获取多个同名节点（同一级）的值。
	 * 
	 * @param name
	 *            属性名
	 * @return 如果一个也没有，返回长度为0的list
	 */
	@SuppressWarnings("unchecked")
	public List<String> getPropertyes(String name) {
		List<String> tmp = new ArrayList<String>();

		String xpath = propertyToXPath(name);

		List<Element> es = document.selectNodes(xpath);
		if (es == null || es.size() == 0) {
			return tmp;
		}

		for (Element e : es) {
			tmp.add(e.getTextTrim());
		}

		return tmp;
	}

	/**
	 * 获取指定元素（不能是Attribute）的所有子元素.
	 * 
	 * @param propertyName
	 *            属性名
	 * @return 指定元素的所有子元素，如果没有，返回null.
	 */
	@SuppressWarnings("unchecked")
	public List<Element> getChildElementsByPropertyName(String propertyName) {
		String xpath = propertyToXPath(propertyName);

		Node selectedNode = select(xpath);
		if (selectedNode == null) {
			return null;
		}

		return ((Element) selectedNode).elements();
	}

	/**
	 * 根据xpath路径，返回一个Node对象，不限于是xml的节点还是属性。如果不存在，返回null
	 * 
	 * @param xpath
	 *            xpath路径
	 * @return 所选择的Node对象，如果不存在，返回null
	 */
	private Node select(String xpath) {
		Node selectedNode = document.selectSingleNode(xpath);
		if (selectedNode == null) {
			selectedNode = document.selectSingleNode(changeToAttribute(xpath));
			if (selectedNode == null) {
				return null;
			}
		}
		return selectedNode;
	}

	/**
	 * 加载xml文件
	 * 
	 * @throws CfgException
	 *             文件未找到，或者无权限
	 */
	private void loadXML() throws CfgException {
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(new FileInputStream(xmlLocation));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CfgException("载入xml文件时发生异常", e);
		}
	}

	/**
	 * 将属性名转换成xpath路径
	 * 
	 * @param property
	 *            属性名
	 * @return xpath路径
	 */
	private String propertyToXPath(String property) {
		String xpath = property.replace('.', '/');
		return "/" + xpath;
	}

	/**
	 * 将表示选择元素的xpath，转换成选择属性的xpath，比如"/system/alarm/sender/email/caption"，将会被转换成
	 * "/system/alarm/sender/email/@caption"
	 * 
	 * @param xpath
	 *            xpath路径
	 * @return 表示属性的xpath路径
	 */
	private String changeToAttribute(String xpath) {
		int index = xpath.lastIndexOf('/');
		return new StringBuilder(xpath).insert(++index, "@").toString();
	}

	/**
	 * 将xml文档的修改，写入文件。
	 * 
	 * @throws CfgException
	 *             写入失败
	 */
	private void write() throws CfgException {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");

		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		XMLWriter writer = null;
		try {
			fos = new FileOutputStream(xmlLocation);
			osw = new OutputStreamWriter(fos, "utf-8");

			writer = new XMLWriter(osw, format);
			writer.write(document);
		} catch (Exception e) {
			logger.error("写入xml时出现异常");
			throw new CfgException(e.getMessage(), e);
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
				if (osw != null) {
					osw.flush();
					osw.close();
				}
				if (fos != null) {
					fos.flush();
					fos.close();
				}
			} catch (Exception e) {
			}
		}
	}
}
