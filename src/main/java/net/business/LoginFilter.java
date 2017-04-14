package net.business;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录检查过滤器：
 *	在所有的(不包括登录相关的请求)请求前，
 *	判断用户是否已登录，若已登录则请求继续，
 *	否则拒绝用户的请求，重定向到登录页。
 */
public class LoginFilter implements Filter {

	private String[] paths;
	
	public void destroy() {

	}

	public void doFilter(ServletRequest req, ServletResponse res, 
			FilterChain chain)
			throws IOException, ServletException {
		//Tomcat传入的参数是HTTP相关参数，
		//就是当前参数的子类型，可以强转。
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		//排除掉不需要校验的请求，一般在web.xml中配置
//		String[] paths = new String[]{
//			"/toLogin.do",
//			"/createImg.do",
//			"/login.do"
//		};
		for (String path : paths) {
			path.equals(request.getServletPath());
			chain.doFilter(req, res);
			return;
		}
		
		//从session中获取adminCode，
		//以此来判断用户是否已登录
		HttpSession session = request.getSession();
		Object adminCode = session.getAttribute("adminCode");
		if (adminCode == null) {
			//未登录，重定向到登录页
			response.sendRedirect(request.getContextPath()+ 
					"toLogin.do");
		} else {
			//已登录，请求继续。
			//参数chain按照doFilter过滤完后请求继续
			chain.doFilter(req, res);
		}

	}

	public void init(FilterConfig config) throws ServletException {
		String ignorePath = config.getInitParameter("ignorePath");
		paths = ignorePath.split(",");
	}

}
