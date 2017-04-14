<%@page pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Wiki－NetCTOSS</title>
         <!-- 
        	当前：/netctoss/findCost.do
        	目标：/netctoss/styles/global.css
        -->
        <link type="text/css" rel="stylesheet" media="all" href="styles/global.css" />
        <link type="text/css" rel="stylesheet" media="all" href="styles/global_color.css" />
        <script language="javascript" type="text/javascript">
            //排序按钮的点击事件
            function sort(btnObj) {
                if (btnObj.className == "sort_desc")
                    btnObj.className = "sort_asc";
                else
                    btnObj.className = "sort_desc";
            }

            //启用
            function startFee() {
                var r = window.confirm("确定要启用此资费吗？资费启用后将不能修改和删除。");
                document.getElementById("operate_result_info").style.display = "block";
            }
            //删除
            function deleteFee() {
                var r = window.confirm("确定要删除此资费吗？");
            }
        </script>        
    </head>
    <body>
        <!--Logo区域开始-->
        <div id="header">
            <img src="images/logo.png" alt="logo" class="left"/>
            <a href="toLogin.do">[退出]</a>            
        </div>
        <!--Logo区域结束-->
        <!--导航区域开始-->
        <div id="navi">                        
            <ul id="menu">
                <li><a href="index.html" class="index_off"></a></li>
                <li><a href="role/role_list.html" class="role_off"></a></li>
                <li><a href="admin/admin_list.html" class="admin_off"></a></li>
                <li><a href="fee/fee_list.html" class="fee_off"></a></li>
                <li><a href="account/account_list.html" class="account_off"></a></li>
                <li><a href="service/service_list.html" class="service_off"></a></li>
                <li><a href="bill/bill_list.html" class="bill_off"></a></li>
                <li><a href="report/report_list.html" class="report_off"></a></li>
                <li><a href="user/user_info.html" class="information_off"></a></li>
                <li><a href="user/user_modi_pwd.html" class="password_off"></a></li>
            </ul>            
        </div>
        <!--导航区域结束-->
        <!--主要区域开始-->
        <div id="main">
            <form action="" method="post">
                <!--排序-->
                <div class="search_add">
                    <div>
                        <!--<input type="button" value="月租" class="sort_asc" onclick="sort(this);" />
                        <input type="button" value="基费" class="sort_asc" onclick="sort(this);" />
                        <input type="button" value="时长" class="sort_asc" onclick="sort(this);" />-->
                    </div>
                    <input type="button" value="增加" class="btn_add" onclick="location.href='addCost.do';" />
                </div> 
                <!--启用操作的操作提示-->
                <div id="operate_result_info" class="operate_success">
                    <img src="images/close.png" onclick="this.parentNode.style.display='none';" />
                    删除成功！
                </div>    
                <!--数据区域：用表格展示数据-->     
                <div id="data">            
                    <table id="datalist">
                        <tr>
                            <th>资费ID</th>
                            <th class="width100">资费名称</th>
                            <th>基本时长</th>
                            <th>基本费用</th>
                            <th>单位费用</th>
                            <th>创建时间</th>
                            <th>开通时间</th>
                            <th class="width50">状态</th>
                            <th class="width200"></th>
                        </tr> 
                        <c:forEach items="${data}" var="d">                   
                        <tr>
                            <td>${d.costId}</td>
                            <td><a href="fee_detail.html">${d.name }</a></td>
                            <td>${d.baseDuration } 小时</td>
                            <td>${d.baseCost } 元</td>
                            <td>${d.unitCost } 元/小时</td>
                            <td><fmt:formatDate value="${d.creatime }" pattern="yyyy/MM/dd"/></td>
                            <td>${d.startime }</td>
                            <td>
                            	<c:if test="${d.status ==0 }">启用</c:if>
                            	<c:if test="${d.status ==1 }">禁用</c:if>
                            </td>
                            <td>                                
                                <input type="button" value="启用" class="btn_start" onclick="startFee();" />
                                													<!-- 请求路径中传入了一个参数 -->
                                <input type="button" value="修改" class="btn_modify" onclick="location.href='toUpdateCost.do?id=${d.costId}';" />
                                <input type="button" value="删除" class="btn_delete" onclick="deleteFee();" />
                            </td>
                        </tr>
                        </c:forEach>  
<!--                         <tr> -->
<!--                             <td>2</td> -->
<!--                             <td><a href="fee_detail.html">包 40 小时</a></td> -->
<!--                             <td>40 小时</td> -->
<!--                             <td>40.50 元</td> -->
<!--                             <td>3.00 元/小时</td> -->
<!--                             <td>2013/01/21 00:00:00</td> -->
<!--                             <td>2013/01/23 00:00:00</td> -->
<!--                             <td>开通</td> -->
<!--                             <td>                                 -->
<!--                             </td> -->
<!--                         </tr> -->
                    </table>
                    <p>业务说明：<br />
                    1、创建资费时，状态为暂停，记载创建时间；<br />
                    2、暂停状态下，可修改，可删除；<br />
                    3、开通后，记载开通时间，且开通后不能修改、不能再停用、也不能删除；<br />
                    4、业务账号修改资费时，在下月底统一触发，修改其关联的资费ID（此触发动作由程序处理）
                    </p>
                </div>
                <!--分页-->
                <div id="pages">
                <!-- 当前是第一页，则不能点击上一页 -->
                  <c:if test="${page==1 }">
        	        <a href="">上一页</a>
        	      </c:if>
        	      <c:if test="${page!=1 }">	
        	      	<a href="findCost.do?page=${page-1 }">上一页</a>
        	      </c:if>
        	      
        	       <!-- 
                 	begin：循环起始位置；
                 	end：循环终止位置；
                 -->
        	      <c:forEach begin="1" end="${total }" var="i">
        	      	<c:if test="${i==page }">
        	      	<!-- 若循环到了当前页，则将页码高亮显示 -->
                    	<a href="findCost.do?page${i }" class="current_page">${i }</a>
                    </c:if>
                    <!-- 若不是当前页，则去掉高亮的样式 -->
                    <c:if test="${i!=page }">
                    	<a href="findCost.do?page${i }">${i }</a>
                    </c:if>
                  </c:forEach>
                  
                  <!-- 当前数据是最后一条，则不能点下一页 -->
                  <c:if test="${page==total }">
                    <a href="">下一页</a><p>已经是最后一页</p>
                  </c:if>
                  <c:if test="${page!=total }">
                  	<a href="findCost.do?page=${page+1 }">下一页</a>
                  </c:if>
                </div>
            </form>
        </div>
        <!--主要区域结束-->
        <div id="footer">
            <p>[源自北美的技术，最优秀的师资，最真实的企业环境，最适用的实战项目]</p>
            <p>版权所有(C)加拿大达内IT培训集团公司 </p>
        </div>
    </body>
</html>
