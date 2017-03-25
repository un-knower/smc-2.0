/**
 * SmcServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.uway.smc.ui.webservice.client;

public interface SmcServiceService extends javax.xml.rpc.Service {

	public java.lang.String getSmcServiceAddress();

	public cn.uway.smc.ui.webservice.client.SmcService_PortType getSmcService()
			throws javax.xml.rpc.ServiceException;

	public cn.uway.smc.ui.webservice.client.SmcService_PortType getSmcService(
			java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
