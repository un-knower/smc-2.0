/**
 * SmcService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.uway.smc.ui.webservice.client;

public interface SmcService_PortType extends java.rmi.Remote {

	public int deliver(java.lang.String username, java.lang.String password,
			int type, int src_id, int level, java.lang.String to_users,
			int send_way, java.lang.String content, java.lang.String send_time)
			throws java.rmi.RemoteException;

	public int deliverEmail(java.lang.String username,
			java.lang.String password, int src_id, int level,
			java.lang.String to_users, java.lang.String content,
			java.lang.String send_time, java.lang.String title,
			byte[] attachmentfile, java.lang.String attachment)
			throws java.rmi.RemoteException;

	public void main(java.lang.String[] args) throws java.rmi.RemoteException;
}
