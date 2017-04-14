<%@page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html >
	<head>
		<meta content="text/html; charset=utf-8"/>
 		<title>达内－NetCTOSS</title>
        <link type="text/css" rel="stylesheet" media="all" href="styles/global.css" />
        <link type="text/css" rel="stylesheet" media="all" href="styles/global_color.css" /> 
    </head>
    <body class="index">
        <div class="login_box">
        	<form action="login.do" method="post">
            <table>
                <tr>
                    <td class="login_info">账号：</td>
                    <td colspan="2"><input name="adminName" value="" type="text" class="width150" /></td>
                    <td class="login_error_info"><span class="required">${nullValue }</span></td>
                </tr>
                <tr>
                    <td class="login_info">密码：</td>
                    <td colspan="2"><input name="password" value="" type="password" class="width150" /></td>
                    <td><span class="required">${error }</span></td>
                </tr>
                <tr>
                    <td class="login_info">验证码：</td>
                    <td class="width70"><input name="code" type="text" class="width70" /></td>
                    <!-- onclick事件:将当前的src值传入一个js函数，每点击验证码图片一次，
                    		每触发一次向后台请求一次（得到一张新图片） -->
                    <td><img src="newImg.do" onclick="this.src='newImg.do?x='+Math.random();" alt="验证码" title="点击更换" /></td>  
                    <td><span class="required"></span></td>              
                </tr>            
                <tr>
                    <td></td>
                    <td class="login_button" colspan="2">
                        <a href="javascript:document.forms[0].submit();"><img src="images/login_btn.png" /></a>
                    </td>    
                    <td><span class="required">${codeError }</span></td>                
                </tr>
            </table>
          </form>
        </div>
    </body>
</html>
