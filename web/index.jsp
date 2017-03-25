<%@ page contentType="text/html; charset=utf-8" language="java"  isELIgnored="false"%>
<%@ taglib prefix="n" uri="/WEB-INF/n.tld"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
<title>登陆</title>

<!-- CSS -->
<link href="/page/style/css/transdmin.css" rel="stylesheet" type="text/css" media="screen" />
<!--[if IE 6]><link rel="stylesheet" type="text/css" media="screen" href="style/css/ie6.css" /><![endif]-->
<!--[if IE 7]><link rel="stylesheet" type="text/css" media="screen" href="style/css/ie7.css" /><![endif]-->

<!-- JavaScripts-->
<script type="text/javascript" src="/page/style/js/jquery.js"></script>
<script type="text/javascript" src="/page/style/js/jNice.js"></script>
<script type="text/javascript">
	

        
        function getValidate(){
         
        
        
        }
        
</script>

</head>

<body>
	<div id="wrapper" style="width:931px;height:800px;">
    	<!-- h1 tag stays for the logo, you can use the a tag for linking the index page -->
    	<h1><a href="#"><span>Transdmin Light</span></a></h1>
        
        <!-- You can name the links with lowercase, they will be transformed to uppercase by CSS, we prefered to name them with uppercase to have the same effect with disabled stylesheet -->
 
        <!-- // #end mainNav -->
        
        <div id="containerHolder">
			<div id="container" style="width:920px;height:500px;">
        		<div id="sidebar">
                	
                    <!-- // .sideNav -->
                </div>    
                <!-- // #sidebar -->
                
                <!-- h2 stays for breadcrumbs -->
                 <center> <h2>登陆</h2></center>
                
                <div id="main" style="width:700px;height:400px;margin-top:100px;">
                       	<form action="auth.do" method="post" class="jNice">
                      	<fieldset>
                        	<p><label>用户名称:</label><input name="userName" type="text" class="text-long" /></p>
                        	<p><label>密码:</label><input name="password" type="password" class="text-long" /></p>                           
                          <input type="submit" value="进入" />
                        </fieldset>
                   
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
