<%@ page contentType="text/html; charset=utf-8" language="java"  isELIgnored="false"%>
<%@ taglib prefix="n" uri="/WEB-INF/n.tld" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<title>系统配置</title>

<!-- CSS -->
<link href="style/css/transdmin.css" rel="stylesheet" type="text/css" media="screen" />
<!--[if IE 6]><link rel="stylesheet" type="text/css" media="screen" href="style/css/ie6.css" /><![endif]-->
<!--[if IE 7]><link rel="stylesheet" type="text/css" media="screen" href="style/css/ie7.css" /><![endif]-->

<!-- JavaScripts-->
<script type="text/javascript" src="style/js/jquery.js"></script>
<script type="text/javascript" src="style/js/jNice.js"></script>



</head>

<body>
	<div id="wrapper">
    	<!-- h1 tag stays for the logo, you can use the a tag for linking the index page -->
    	<h1><a href="#"><span>Transdmin Light</span></a></h1>
        
        <!-- You can name the links with lowercase, they will be transformed to uppercase by CSS, we prefered to name them with uppercase to have the same effect with disabled stylesheet -->
        <ul id="mainNav">
        	<li><a href="#">主页</a></li>
        	<li><a href="/page/shortInfo.do?action=queryList">数据管理</a></li> <!-- Use the "active" class for the active menu item  -->
        	<li><a href="/page/shortInfoHistory.do?action=queryList">历史数据查询</a></li>
        	<li><a href="/page/infoSourceCfg.do?action=queryList">业务配置</a></li>
        	<li><a href="/page/config.do?action=queryList" class="active">系统配置</a></li>
        	<li class="logout"><a href="/auth.do?exit="exit">退出</a></li>
        </ul>
        <!-- // #end mainNav -->
        
        <div id="containerHolder">
			<div id="container">
        		<div id="sidebar">
                	<ul class="sideNav">        
                    	<li><a href="/page/config.do?action=queryList" class="active">系统配置</a></li>
                    	<li><a href="/page/user.do?action=queryList">用户管理</a></li>
                  </ul>
                    <!-- // .sideNav -->
                </div>    
                <!-- // #sidebar -->
                
                <!-- h2 stays for breadcrumbs -->                
                <div id="main">
                	
					<h3>>>系统配置信息</h3>
					 <form action="/page/config.do?action=updatePage" method="post" class="jNice">
					 
					 	<table cellpadding="0" cellspacing="0">
              <c:forEach items="${requestScope.result.data.datas}" var="x">
		            <c:if test="${x!=null}"> 
		            		 <input type="hidden" name="pageId" id="pageId" value="${x.id}">
		            		  <input type="hidden" name="mailPwd" id="mailPwd" value="${x.mailPwd}">
		            		   <input type="hidden" name="smsUserPwd" id="smsUserPwd" value="${x.smsUserPwd}">
		            		   <input type="hidden" name="serverReceivePwd" id="serverReceivePwd" value="${x.serverReceivePwd}">
                    			<tr>
                    				<td>编号:</td>
                    				<td>${x.id}</td>
                    		  </tr>
                    		  <tr>
                    				<td>邮件服务器:</td>
                    				<td>${x.smtpHost}</td>
                    		  </tr>
                    		  <tr>
                    				<td>邮件账号:</td>
                    				<td>${x.mailUser}</td>
                    		  </tr>
                    		  <tr>
                    				<td>邮件账号密码:</td>
                    				<td>******</td>
                    		  </tr>
                    		  <tr>
                    				<td>节点编号:</td>
                    				<td>${x.nodeId}</td>
                    		  </tr>
                    		  <tr>
                    				<td>计费类型:</td>
                    				<td>${x.feeType}</td>
                    		  </tr>
                    		  <tr>
                    				<td>代收标志:</td>
                    				<td>${x.agentFlag}</td>
                    		  </tr>
                    		  <tr>
                    				<td>引起MT消息的原因:</td>
                    				<td>${x.moRelateToMtFlag}</td>
                    		  </tr>
                    		  <tr>
                    				<td>优先级:</td>
                    				<td>${x.priority}</td>
                    		  </tr>
                    		  <tr>
                    				<td>状态报告标记:</td>
                    				<td>${x.reportFlag}</td>
                    		  </tr>
                    		  <tr>
                    				<td>GSM协议类型(PID):</td>
                    				<td>${x.tpPid}</td>
                    		  </tr>
                    		  <tr>
                    				<td>GSM协议类型(UDHI):</td>
                    				<td>${x.tpUdhi}</td>
                    		  </tr>
                    		  <tr>
                    				<td>短消息编码格式:</td>
                    				<td>${x.messagecoding}</td>
                    		  </tr>
                    		  <tr>
                    				<td>信息类型:</td>
                    				<td>${x.messageType eq 1? "短信":(x.messageType eq 2?"邮件":"短信和邮件")}</td>
                    		  </tr>
                    		  <tr>
                    				<td>SP的接入号:</td>
                    				<td>${x.spNumber}</td>
                    		  </tr>
                    		  <tr>
                    				<td>付费号码:</td>
                    				<td>${x.chargeNumber}</td>
                    		  </tr>
                    		  <tr>
                    				<td>企业代码:</td>
                    				<td>${x.corpId}</td>
                    		  </tr>
                    		  <tr>
                    				<td>业务代码:</td>
                    				<td>${x.serviceType}</td>
                    		  </tr>
                    		  <tr>
                    				<td>短消息收费值:</td>
                    				<td>${x.feeValue}</td>
                    		  </tr>
                    		  <tr>
                    				<td>赠送用户话费:</td>
                    				<td>${x.givenValue}</td>
                    		  </tr>
                    		  <tr>
                    				<td>短消息发送时间:</td>
                    				<td>${x.scheduleTime}</td>
                    		  </tr>
                    		  <tr>
                    				<td>短消息终止时间:</td>
                    				<td>${x.expireTime}</td>
                    		  </tr>
                    		   <tr>
                    				<td>短消息服务器IP:</td>
                    				<td>${x.serverIp}</td>
                    		  </tr>
                    		   <tr>
                    				<td>短消息服务器端口:</td>
                    				<td>${x.serverPort}</td>
                    		  </tr>
                    		  
                    		   <tr>
                    				<td>短信发送用户名:</td>
                    				<td>${x.smsUserName}</td>
                    		  </tr>
                    		   <tr>
                    				<td>短信发送密码:</td>
                    				<td>******</td>
                    		  </tr>
                    		  
                    		  <tr>
                    				<td>短消息服务接收端口:</td>
                    				<td>${x.serverReceivePort}</td>
                    		  </tr>
                    		  
                    		   <tr>
                    				<td>短消息服务器接收用户名:</td>
                    				<td>${x.serverReceiveUserName}</td>
                    		  </tr>
                    		  <tr>
                    				<td>短消息服务接收密码:</td>
                    				<td>******</td>
                    		  </tr>
                    		  
                    		  <tr>
                    				<td>一天最大发送条数:</td>
                    				<td>${x.securityMaxSendCountDay}</td>
                    		  </tr>
                    		  <tr>
                    				<td>一小时最大发送条数:</td>
                    				<td>${x.securityMaxSendCountHour}</td>
                    		  </tr>
                    		  <tr>
                    				<td>外部表的驱动:</td>
                    				<td>${x.extTableDriver}</td>
                    		  </tr>
                    		  <tr>
                    				<td>外部表链接:</td>
                    				<td>${x.extTableUrl}</td>
                    		  </tr>
                    		  <tr>
                    				<td style="width:180px;">外部表用户名:</td>
                    				<td>${x.extTableUser}</td>
                    		  </tr>
                    		  <tr>
                    				<td>外部表密码:</td>
                    				<td>${x.extTablePwd}</td>
                    		  </tr>
                    		  <tr>
                    				<td>描述:</td>
                    				<td>${x.description}</td>
                    		  </tr>                    		 
                   		  	                    		  	
                    		  <tr>
                    		
                    				<td>记录是否生效:</td>                    			
                    			  <td> ${x.takeEffect eq 1 ?"有效":"无效"}</td>
                    				
                    		  </tr>
             </c:if>
          </c:forEach>
            </table>
                <div align="center">								
									<n:nav pageCount="${requestScope.result.wparam.pageCount}"
										pageIndex="${requestScope.result.wparam.pageIndex}" navigationUrl="/page/config.do"
										params="${requestScope.result.wparam.params}"></n:nav>
								</div>
            
                          <input class="button-submit" type="submit"  value="修改" />
                          <input class="button-submit" type="button" value="返回" onclick="window.history.back(-1)"/>
					</form>
                  
                    
                </div>
                <!-- // #main -->
                
                <div class="clear"></div>
            </div>
            <!-- // #container -->
        </div>	
        <!-- // #containerHolder -->
        
         <%@ include file="/page/footer.html"%>
    </div>
    <!-- // #wrapper -->
</body>
</html>
