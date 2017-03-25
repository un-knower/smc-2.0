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
        	<li><a href="/page/infoSourceCfg.do?action=queryList" class="active">业务配置</a></li>
        	<li><a href="/page/config.do?action=queryList" >系统配置</a></li>
        	<li class="logout"><a href="/auth.do?exit="exit">退出</a></li>
        </ul>
        <!-- // #end mainNav -->
        
        <div id="containerHolder">
			<div id="container">
        		<div id="sidebar">
                	<ul class="sideNav">
                		  <li><a href="/page/infoSourceCfg.do?action=queryList">短消息来源配置</a></li>                   
                    	<li><a href="/page/infoStrategyCfg.do?action=queryList">短消息发送策略配置</a></li>
                    	<li><a href="/page/infoLevelCfg.do?action=queryList">短消息级别查询</a></li>
                    	<li><a href="/page/infoToUserCfg.do?action=queryList" class="active">短消息接收用户配置</a></li>
                    	<li><a href="/page/infoUserGroupCfg.do?action=queryList">短消息接收用户分组配置</a></li>                  	
                  </ul>
                    <!-- // .sideNav -->
                </div>    
                <!-- // #sidebar -->
                
                <!-- h2 stays for breadcrumbs -->                
                <div id="main">
                	
					<h3>>>短消息接收用户信息</h3>
					 <form action="" method="post" class="jNice">
					 
					 	<table cellpadding="0" cellspacing="0">
                    			<tr>
                    				<td>编号:</td>
                    				<td>${requestScope.result.data.id}</td>
                    		  </tr>
                    		  <tr>
                    				<td>用户名:</td>
                    				<td>${requestScope.result.data.name}</td>
                    		  </tr>
                    		  <tr>
                    				<td>用户分组编号:</td>
                    				<td>${requestScope.result.data.groupId}</td>
                    		  </tr>
                    		  <tr>
                    				<td>手机号:</td>
                    				<td>${requestScope.result.data.cellphone}</td>
                    		  </tr>
                    		   <tr>
                    				<td>邮箱:</td>
                    				<td>${requestScope.result.data.email}</td>
                    		  </tr>
                    		   <tr>
                    				<td>描述:</td>
                    				<td>${requestScope.result.data.description}</td>
                    		  </tr>
                    		  
            </table>
              
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
