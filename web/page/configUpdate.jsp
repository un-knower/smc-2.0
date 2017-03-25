<%@ page contentType="text/html; charset=utf-8" language="java"
	isELIgnored="false"%>
<%@ taglib prefix="n" uri="/WEB-INF/n.tld"%>
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
<link href="style/css/transdmin.css" rel="stylesheet" type="text/css"
	media="screen" />
<!--[if IE 6]><link rel="stylesheet" type="text/css" media="screen" href="style/css/ie6.css" /><![endif]-->
<!--[if IE 7]><link rel="stylesheet" type="text/css" media="screen" href="style/css/ie7.css" /><![endif]-->

<!-- JavaScripts--> <script type="text/javascript"
	src="style/js/jquery.js"></script> <script type="text/javascript"
	src="style/js/jNice.js"></script>
	
<script type="text/javascript">
	  function getValidate(){
	  	
         	   var smtpHost = document.getElementById("smtpHost").value;  
         	    if(smtpHost ==""){
                alert("邮件服务器不可为空.");
                return false;
              }
                  var mailUser = document.getElementById("mailUser").value;  
         	    if(mailUser ==""){
                alert("邮件账号不可为空.");
                return false;
              }
                                var mailPwd = document.getElementById("mailPwd").value;  
         	    if(mailPwd ==""){
                alert("邮件账号密码不可为空.");
                return false;
              }
                               var nodeId = document.getElementById("nodeId").value;  
         	    if(nodeId ==""){
                alert("节点编号不可为空.");
                return false;
              }
                              var feeType = document.getElementById("feeType").value;  
         	    if(feeType ==""){
                alert("计费类型不可为空.");
                return false;
              }
                              var agentFlag = document.getElementById("agentFlag").value;  
         	    if(agentFlag ==""){
                alert("代收标志不可为空.");
                return false;
              }
                             var moRelateToMtFlag = document.getElementById("moRelateToMtFlag").value;  
         	    if(moRelateToMtFlag ==""){
                alert("引起MT消息的原因不可为空.");
                return false;
              }
                              var priority = document.getElementById("priority").value;  
         	    if(priority ==""){
                alert("优先级不可为空.");
                return false;
              }
                            var reportFlag = document.getElementById("reportFlag").value;  
         	    if(reportFlag ==""){
                alert("状态报告标记不可为空.");
                return false;
              }
                            var tpPid = document.getElementById("tpPid").value;  
         	    if(tpPid ==""){
                alert("GSM协议类型不可为空.");
                return false;
              }
                            var tpUdhi = document.getElementById("tpUdhi").value;  
         	    if(tpUdhi ==""){
                alert("GSM协议类型不可为空.");
                return false;
              }
                            var messagecoding = document.getElementById("messagecoding").value;  
         	    if(messagecoding ==""){
                alert("短消息的编码格式不可为空.");
                return false;
              }                       
              
             var messageType = document.getElementById("messageType");
             var index = messageType.selectedIndex;
             var messageTypevalue = messageType.options[index].text;                         
         	    if(messageTypevalue =="请选择信息类型"){
                alert("请选择信息类型.");
                return false;
              }  
                            var spNumber = document.getElementById("spNumber").value;  
         	    if(spNumber ==""){
                alert("SP的接入号不可为空.");
                return false;
              }  
                             var corpId = document.getElementById("corpId").value;  
         	    if(corpId ==""){
                alert("企业代码不可为空.");
                return false;
              } 
                             var serviceType = document.getElementById("serviceType").value;  
         	    if(serviceType ==""){
                alert("业务代码不可为空.");
                return false;
              }  
                             var feeValue = document.getElementById("feeValue").value;  
         	    if(feeValue ==""){
                alert("该条短消息的收费值不可为空.");
                return false;
              }   
                            var givenValue = document.getElementById("givenValue").value;  
         	    if(givenValue ==""){
                alert("赠送用户的话费不可为空.");
                return false;
              }                                                        
  
                            var serverIp = document.getElementById("serverIp").value;  
         	    if(serverIp ==""){
                alert("短消息服务器IP不可为空.");
                return false;
              }   
                            var serverPort = document.getElementById("serverPort").value;  
         	    if(serverPort ==""){
                alert("短消息服务器端口不可为空.");
                return false;
              }    
         	    
                var serverReceivePort = document.getElementById("serverReceivePort").value;  
         	    if(serverReceivePort ==""){
                alert("短消息服务接收端口不可为空.");
                return false;
              }      
                var smsUserName = document.getElementById("smsUserName").value;  
         	    if(smsUserName ==""){
                alert("短信用户名不可为空.");
                return false;
              
              }    
                          var smsUserPwd = document.getElementById("smsUserPwd").value;  
         	    if(smsUserPwd ==""){
                alert("短信密码不可为空.");
                return false;
              }
                           var securityMaxSendCountDay = document.getElementById("securityMaxSendCountDay").value;  
         	    if(securityMaxSendCountDay ==""){
                alert("一天发送短信最大条数不可为空.");
                return false;
              }    
                           var securityMaxSendCountHour = document.getElementById("securityMaxSendCountHour").value;  
         	    if(securityMaxSendCountHour ==""){
                alert("一小时发送短信最大条数不可为空.");
                return false;
              }       
                           var extTableDriver = document.getElementById("extTableDriver").value;  
         	    if(extTableDriver ==""){
                alert("外部表驱动不可为空.");
                return false;
              }  
                           var extTableUrl = document.getElementById("extTableUrl").value;  
         	    if(extTableUrl ==""){
                alert("外部表链接不可为空.");
                return false;
              }  
                           var extTableUser = document.getElementById("extTableUser").value;  
         	    if(extTableUser ==""){
                alert("外部表用户不可为空.");
                return false;
              }   
                           var extTablePwd = document.getElementById("extTablePwd").value;  
         	    if(extTablePwd ==""){
                alert("外部表用户密码不可为空.");
                return false;
              }  
                           var descriptions = document.getElementById("description").value;  
         	    if(descriptions ==""){
                alert("描述不可为空.");
                return false;
              } 
              
             var takeEffect = document.getElementById("takeEffect");
             var index = takeEffect.selectedIndex;
             var takeEffectvalue = takeEffect.options[index].text;                         
         	    if(takeEffectvalue =="请选择记录是否生效"){
                alert("请选择记录是否生效.");
                return false;
              }               
                                                
                                   
    }


</script>
	
</head>

<body>
<div id="wrapper"><!-- h1 tag stays for the logo, you can use the a tag for linking the index page -->
<h1><a href="#"><span>Transdmin Light</span></a></h1>

<!-- You can name the links with lowercase, they will be transformed to uppercase by CSS, we prefered to name them with uppercase to have the same effect with disabled stylesheet -->
<ul id="mainNav">
	<li><a href="#">主页</a></li>
	<li><a href="/page/shortInfo.do?action=queryList">数据管理</a></li>
	<!-- Use the "active" class for the active menu item  -->
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
<!-- // .sideNav --></div>
<!-- // #sidebar --> <!-- h2 stays for breadcrumbs -->
<div id="main">

<h3>>>系统配置信息</h3>
<form action="/page/config.do?action=update" method="post" class="jNice" onSubmit="return getValidate()">

<table cellpadding="0" cellspacing="0" >
  <input name="id" type="hidden" value="${requestScope.result.data.id}">
	<tr>
		<td>编号:</td>
		<td>${requestScope.result.data.id}</td>
	</tr>
	<tr>
		<td>邮件服务器:</td>
		<td><input id="smtpHost" name="smtpHost" type="text"
			class="text-long" value="${requestScope.result.data.smtpHost}" /></td>
	</tr>
	<tr>
		<td>邮件账号:</td>
		<td><input id="mailUser" name="mailUser" type="text"
			class="text-long" value="${requestScope.result.data.mailUser}" /></td>
	</tr>
	<tr>
		<td>邮件账号密码:</td>
		<td><input id="mailPwd" name="mailPwd" type="password"
			class="text-long" value="${requestScope.result.data.mailPwd}" /></td>
	</tr>
	<tr>
		<td>节点编号:</td>
		<td><input id="nodeId" name="nodeId" type="text"
			class="text-long" value="${requestScope.result.data.nodeId}" /></td>
	</tr>
	<tr>
		<td>计费类型:</td>
		<td><input id="feeType" name="feeType" type="text"
			class="text-long" value="${requestScope.result.data.feeType}" /></td>
	</tr>
	<tr>
		<td>代收标志:</td>
		<td><input id="agentFlag" name="agentFlag" type="text"
			class="text-long" value="${requestScope.result.data.agentFlag}" /></td>
	</tr>
	<tr>
		<td>引起MT消息的原因:</td>
		<td><input id="moRelateToMtFlag" name="moRelateToMtFlag"
			type="text" class="text-long"
			value="${requestScope.result.data.moRelateToMtFlag}" /></td>
	</tr>
	<tr>
		<td>优先级:</td>
		<td><input id="priority" name="priority" type="text"
			class="text-long" value="${requestScope.result.data.priority}" /></td>
	</tr>
	<tr>
		<td>状态报告标记:</td>
		<td><input id="reportFlag" name="reportFlag" type="text"
			class="text-long" value="${requestScope.result.data.reportFlag}" /></td>
	</tr>
	<tr>
		<td>GSM协议类型:</td>
		<td><input id="tpPid" name="tpPid" type="text" class="text-long"
			value="${requestScope.result.data.tpPid}" /></td>
	</tr>
	<tr>
		<td>GSM协议类型:</td>
		<td><input id="tpUdhi" name="tpUdhi" type="text"
			class="text-long" value="${requestScope.result.data.tpUdhi}" /></td>
	</tr>
	<tr>
		<td>短消息的编码格式:</td>
		<td><input id="messagecoding" name="messagecoding" type="text"
			class="text-long" value="${requestScope.result.data.messagecoding}" /></td>
	</tr>
	<tr>
		<td>信息类型:</td>
		<td><select id="messageType" name="messageType">
			<option value="">请选择信息类型</option>
			<option value="1" ${requestScope.result.data.messageType eq 1 ? "selected='selected'":""} >短信</option>
			<option value="2" ${requestScope.result.data.messageType eq 2 ? "selected='selected'":""}>邮件</option>
			<option value="3" ${requestScope.result.data.messageType eq 3 ? "selected='selected'":""}>短信和邮件</option>
		</select></td>

	<tr>
		<td>SP的接入号:</td>
		<td><input id="spNumber" name="spNumber" type="text"
			class="text-long" value="${requestScope.result.data.spNumber}" /></td>
	</tr>
	<tr>
		<td>付费号码:</td>
		<td><input id="chargeNumber" name="chargeNumber" type="text"
			class="text-long" value="${requestScope.result.data.chargeNumber}" /></td>
	</tr>
	<tr>
		<td>企业代码:</td>
		<td><input id="corpId" name="corpId" type="text"
			class="text-long" value="${requestScope.result.data.corpId}" /></td>
	</tr>
	<tr>
		<td>业务代码:</td>
		<td><input id="serviceType" name="serviceType" type="text"
			class="text-long" value="${requestScope.result.data.serviceType}" /></td>
	</tr>
	<tr>
		<td>该条短消息的收费值:</td>
		<td><input id="feeValue" name="feeValue" type="text"
			class="text-long" value="${requestScope.result.data.feeValue}" /></td>
	</tr>
	<tr>
		<td>赠送用户的话费:</td>
		<td><input id="givenValue" name="givenValue" type="text"
			class="text-long" value="${requestScope.result.data.givenValue}" /></td>
	</tr>
	<tr>
		<td>短消息终止时间:</td>
		<td><input id="expireTime" name="expireTime" type="text"
			class="text-long" value="${requestScope.result.data.expireTime}" /></td>
	</tr>
	<tr>
		<td>短消息发送时间:</td>
		<td><input id="scheduleTime" name="scheduleTime" type="text"
			class="text-long" value="${requestScope.result.data.scheduleTime}" /></td>
	</tr>
	<tr>
		<td>短消息服务器IP:</td>
		<td><input id="serverIp" name="serverIp" type="text"
			class="text-long" value="${requestScope.result.data.serverIp}" /></td>
	</tr>
	<tr>
		<td>短消息服务器端口:</td>
		<td><input id="serverPort" name="serverPort" type="text"
			class="text-long" value="${requestScope.result.data.serverPort}" /></td>
	</tr>
	
	<tr>
		<td>短信发送用户名:</td>
		<td><input id="smsUserName" name="smsUserName" type="text"
			class="text-long" value="${requestScope.result.data.smsUserName}" /></td>
	</tr>
	<tr>
		<td>短信发送密码:</td>
		<td><input id="smsUserPwd" name="smsUserPwd" type="password"
			class="text-long" value="${requestScope.result.data.smsUserPwd}" /></td>
	</tr>
	
		
	<tr>
		<td>短消息服务器接收端口:</td>
		<td><input id="serverReceivePort" name="serverReceivePort" type="text"
			class="text-long" value="${requestScope.result.data.serverReceivePort}" /></td>
	</tr>
		<tr>
		<td>短消息服务器接收用户名:</td>
		<td><input id="serverReceiveUserName" name="serverReceiveUserName" type="text"
			class="text-long" value="${requestScope.result.data.serverReceiveUserName}" /></td>
	</tr>
	
	<tr>
      <td>短消息服务接收密码:</td>
      <td><input id="serverReceivePwd" name="serverReceivePwd" type="password"
			class="text-long" value="${requestScope.result.data.serverReceivePwd}" /></td>
    </tr>
	
	<tr>
		<td>一天最大发送条数:</td>
		<td><input id="securityMaxSendCountDay"
			name="securityMaxSendCountDay" type="text" class="text-long"
			value="${requestScope.result.data.securityMaxSendCountDay}" /></td>
	</tr>
	<tr>
		<td>一小时最大发送条数:</td>
		<td><input id="securityMaxSendCountHour"
			name="securityMaxSendCountHour" type="text" class="text-long"
			value="${requestScope.result.data.securityMaxSendCountHour}" /></td>
	</tr>
	<tr>
		<td>外部表的驱动:</td>
		<td><input id="extTableDriver" name="extTableDriver" type="text"
			class="text-long" value="${requestScope.result.data.extTableDriver}" /></td>
	</tr>
	<tr>
		<td>外部表链接:</td>
		<td><input id="extTableUrl" name="extTableUrl" type="text"
			class="text-long" value="${requestScope.result.data.extTableUrl}" /></td>
	</tr>
	<tr>
		<td>外部表用户名:</td>
		<td><input id="extTableUser" name="extTableUser" type="text"
			class="text-long" value="${requestScope.result.data.extTableUser}" /></td>
	</tr>
	<tr>
		<td>外部表密码:</td>
		<td><input id="extTablePwd" name="extTablePwd" type="text"
			class="text-long" value="${requestScope.result.data.extTablePwd}" /></td>
	</tr>
	<tr>
		<td>描述:</td>
		<td><input id="description" name="description" type="text"
			class="text-long" value="${requestScope.result.data.description}" /></td>
	</tr>
	<tr>
		<td>记录是否生效:</td>
		<td><select id="takeEffect" name="takeEffect">
			<option value="">请选择记录是否生效</option>
			<option value="1" ${requestScope.result.data.takeEffect eq 1 ? "selected='selected'":""}>生效</option>
			<option value="0" ${requestScope.result.data.takeEffect eq 0 ? "selected='selected'":""}>失效</option>
		</select></td>
	</tr>

</table>
<input class="button-submit" type="submit" value="提交" /> 
<input class="button-submit" type="button" value="返回" onclick="window.history.back(-1)" />
	</form>


</div>
<!-- // #main -->

<div class="clear"></div>
</div>
<!-- // #container --></div>
<!-- // #containerHolder --> <%@ include file="/page/footer.html"%>
</div>
<!-- // #wrapper -->
</body>
</html>
