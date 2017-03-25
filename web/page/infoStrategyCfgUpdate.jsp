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
<title>业务配置</title>

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
             	
        	   var srcid = document.getElementById("srcid");        	  
             var index = srcid.selectedIndex;
             var srcidValue = srcid.options[index].text;     
         	    if(srcidValue =="请选择消息源"){
                alert("请选择消息源.");
                return false;
              }   
              
             var levelid = document.getElementById("levelid");        	  
             var index = levelid.selectedIndex;
             var levelidValue = levelid.options[index].text;     
         	    if(levelidValue =="请选择消息级别"){
                alert("请选择消息级别.");
                return false;
              } 
             var id = document.getElementById("id");        	  
             var index = id.selectedIndex;
             var idValue = id.options[index].text;     
         	    if(idValue =="请选择用户分组"){
                alert("请选择用户分组.");
                return false;
              } 
              var ttl = document.getElementById("ttl").value;       
				        if(ttl.search("^-?\\d+$")!=0){
							        alert("存活时间必须是整数!");
							        return false;
							    }
              var endoOffsetTime = document.getElementById("endoOffsetTime").value;  
              if(endoOffsetTime.search("^-?\\d+$")!=0){
							        alert("发送偏移时间必须是整数!");
							        return false;
							    }

              var sendTimes = document.getElementById("sendTimes").value;  
				       if(sendTimes.search("^-?\\d+$")!=0){
							        alert("发送次数必须是整数!");
							        return false;
							    }
              var sendInterval = document.getElementById("sendInterval").value;  	
				          if(sendInterval.search("^-?\\d+$")!=0){
							        alert("时间间隔必须是整数!");
							        return false;
							    }			        				        				        
        
             var sendWay = document.getElementById("sendWay");        	  
             var index = sendWay.selectedIndex;
             var sendWayValue = sendWay.options[index].text;     
         	    if(sendWayValue =="请选择发送方式"){
                alert("请选择发送方式.");
                return false;
              }   
             var resendWhenFail = document.getElementById("resendWhenFail");        	  
             var index = resendWhenFail.selectedIndex;
             var resendWhenFailValue = resendWhenFail.options[index].text;     
         	    if(resendWhenFailValue =="请选择是否重发"){
                alert("请选择是否重发.");
                return false;
              }               
             var takeEffect = document.getElementById("takeEffect");        	  
             var index = takeEffect.selectedIndex;
             var takeEffectValue = takeEffect.options[index].text;     
         	    if(takeEffectValue =="请选择是否生效"){
                alert("请选择是否生效.");
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
	<li><a href="/page/infoSourceCfg.do?action=queryList" class="active">业务配置</a></li>
	<li><a href="/page/config.do?action=queryList">系统配置</a></li>
	<li class="logout"><a href="/auth.do?exit="exit">退出</a></li>
</ul>
<!-- // #end mainNav -->

<div id="containerHolder">
<div id="container">
<div id="sidebar">
<ul class="sideNav">
	<li><a href="/page/infoSourceCfg.do?action=queryList">短消息来源配置</a></li>
	<li><a href="/page/infoStrategyCfg.do?action=queryList" class="active">短消息发送策略配置</a></li>
	<li><a href="/page/infoLevelCfg.do?action=queryList">短消息级别查询</a></li>
	<li><a href="/page/infoToUserCfg.do?action=queryList">短消息接收用户配置</a></li>
	<li><a href="/page/infoUserGroupCfg.do?action=queryList">短消息接收用户分组配置</a></li>
</ul>
<!-- // .sideNav --></div>
<!-- // #sidebar --> <!-- h2 stays for breadcrumbs -->

<div id="main">

<form action="/page/infoStrategyCfg.do?action=save" method ="post" class="jNice" onSubmit="return getValidate()">
<h3>>>修改策略</h3>
<fieldset><!-- <p><label>编号:</label><input name="id" type="text" class="text-long" /></p>                    		                        
                        	 --> 
<input type="hidden" id="ids" name="id"  value="${requestScope.strategy.id}"/>
<p><label>消息源:</label> <select id="srcid" name="srcid">
	<option>请选择消息源</option>
	<c:forEach items="${requestScope.sources}" var="task">
		<option value="${task.srcid}"   ${task.srcid  eq requestScope.strategy.srcid ? "selected='selected'" : "" } >${task.name}</option>
	</c:forEach>
</select></p>
<p><label>消息级别:</label> <select id="levelid" name="levelid">
	<option>请选择消息级别</option>
	<c:forEach items="${requestScope.levels}" var="task">
		<option value="${task.levelid}"  ${task.levelid  eq requestScope.strategy.levelid ? "selected='selected'" : "" } >${task.name}</option>
	</c:forEach>
</select></p>

<p><label>用户分组</label> <select id="id" name="id">
	<option>请选择用户分组</option>
	<c:forEach items="${requestScope.groups}" var="task">
		<option value="${task.id}"  ${task.id eq requestScope.strategy.id ? "selected='selected'" : "" }>${task.name}</option>
	</c:forEach>

</select></p>
<p><label>存活时间:</label><input id="ttl" name="ttl" type="text"  value="${requestScope.strategy.ttl}"
	class="text-long" /> <span> 单位:分钟，默认为120分钟</span></p>

<p><label>发送偏移时间:</label><input id="endoOffsetTime" name="endoOffsetTime" type="text" value="${requestScope.strategy.endoOffsetTime}"
	class="text-long" /> <span> 整数</span></p>

<p><label>发送次数:</label><input id="sendTimes" name="sendTimes" type="text"  value="${requestScope.strategy.sendTimes}"
	class="text-long" /> <span> 整数</span></p>
<p><label>时间间隔</label><input id="sendInterval" name="sendInterval" type="text"  value="${requestScope.strategy.sendInterval}"
	class="text-long" /> <span> 单位 :分钟</span></p>
<p><label>发送方式</label> <select id="sendWay" name="sendWay">
	<option>请选择发送方式</option>
	<option value="1"  ${requestScope.strategy.sendWay eq 1 ? "selected='selected'" : ""}>短信</option>
	<option value="2" ${requestScope.strategy.sendWay eq 2 ? "selected='selected'" : ""}>邮件</option>
	<option value="3" ${requestScope.strategy.sendWay eq 3 ? "selected='selected'" : ""}>短信与邮件</option>
</select></p>
<p><label>发送失败是否重发</label> <select id="resendWhenFail" name="resendWhenFail">
	<option>请选择是否重发</option>
	<option value="0" ${requestScope.strategy.resendWhenFail eq 0 ? "selected='selected'" : ""}>否</option>
	<option value="1" ${requestScope.strategy.resendWhenFail eq 1 ? "selected='selected'" : ""} >是</option>
</select></p>

<p><label>是否生效</label> <select id="takeEffect" name="takeEffect">
	<option>请选择是否生效</option>
	<option value="0" ${requestScope.strategy.takeEffect eq 0 ? "selected='selected'" : "" }>否</option>
	<option value="1" ${requestScope.strategy.takeEffect eq 1 ? "selected='selected'" : "" }>是</option>
</select></p>

<input class="button-submit" type="submit" value="提交" /> <input
	class="button-submit" type="reset" value="重置" /> <input
	class="button-submit" type="button" value="返回"
	onclick="window.history.back(-1)" /></fieldset>
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
