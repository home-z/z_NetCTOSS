package net.business;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.AdminInfo;
import entity.Cost;
import net.dao.AdminDao;
import net.dao.CostDao;
import util.ImageUtil;

@SuppressWarnings("serial")
public class MainServlet extends HttpServlet{

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String path = req.getServletPath();
		if ("/findCost.do".equals(path)) {
			//find zi fei
			findCost(req, res);
		} else if (path.equals("/addCost.do")) {
			addCost(req, res);
		} else if (path.equals("/save.do")) {
			save(req, res);
		} else if (path.equals("/toUpdateCost.do")) {
			toOpen(req, res);
		} else if (path.equals("/upDate.do")) {
			upDateCost(req, res);
		} else if (path.equals("/values.do")) {
			upDateCost(req, res);
		} else if (path.equals("/toLogin.do")) {
			toLogin(req, res);
		} else if(path.equals("/toIndex.do")) {
			toIndex(req, res);
		} else if (path.equals("/login.do")) {
			checkLogin(req,res);
		} else if(path.equals("/sigOut.do")) {
			back(req, res);
		} else if (path.equals("/newImg.do")) {
			createImg(req, res);
		} else {
			throw new RuntimeException("没有这个页面");
		}
		
	}

	/**
	 *  登录页面
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public void checkLogin(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		//获取jsp页面传过来的值
		String user = req.getParameter("adminName");
		String pwd = req.getParameter("password");
		String code = req.getParameter("code");
		//创建session
		HttpSession session = req.getSession();
		//获得名为imgCode的session值
		String imgCode = (String)session.getAttribute("imgCode");
		//判断验证码是否存和合法--忽略大小写
		if (code == null || code.equals("") || !code.equalsIgnoreCase(imgCode)) {
			req.setAttribute("codeError", "验证码错误");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
			return;//验证码错误则不再往下执行
		}
		AdminDao dao = new AdminDao();
		AdminInfo admin = dao.findUser(user);
		if (admin == null) {
			//转发并提示错误信息
			req.setAttribute("nullValue", "账号不存在");
			//当前:projectName/save.do
			//目标:projectName/WEB-INF/cost/find.jsp
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
		} else if (!pwd.equals(admin.getPassword())) {
			//密码错误
			req.setAttribute("error", "密码错误");
			//相对路径不以/开头
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
		} else {
			//校验通过
			//将用户名存入cookie
			Cookie cookie = new Cookie("userName",user);//一条cookie记录
			res.addCookie(cookie);
			//将用户存入session，方便后面的业务判断
			session.setAttribute("userName", user);
			//当前：/netctoss/login.do
			//目标：/netctoss/toIndex.do
			//登录后到首页
			res.sendRedirect("toIndex.do");
			
			System.out.println("请求的主机名:"+req.getServerName());
			System.out.println("项目的根目录:"+req.getContextPath());
		}
	}
	
	/**
	 * 生成验证码
	 * @param req
	 * @param res
	 * @throws IOException 
	 */
	public void createImg(HttpServletRequest req,HttpServletResponse res) throws IOException {
		//创建随机图片和验证码
	Object[] images = ImageUtil.createImage();
	//创建session 并将验证码放入sessiong 
	HttpSession session = req.getSession();
	session.setAttribute("imgCode", images[0]);
	//将图片输出给浏览器
	BufferedImage bi = (BufferedImage)images[1];
	//指定格式
	res.setContentType("image/png");
	//输出流的目标就是浏览器
	OutputStream os = res.getOutputStream();
	ImageIO.write(bi, "png", os);
	os.close();
	
	}
	/**
	 * 通过转发到登录界面
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	public void toLogin(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
	}
	
	/**
	 * 登出
	 * @param req
	 * @param res
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void back(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/info.jsp").forward(req, res);
	}
	/**
	 * 跳转到主页
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	public void toIndex(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/main/index.jsp").forward(req, res);
	}
	
	/**
	 * 
	 * @param req 请求
	 * @param res 响应
	 * @throws ServletException 
	 * @throws IOException
	 */
	protected void findCost(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException {
		//获取请求参数
		String page = req.getParameter("page");
		//思路：总页数是根据查出来的总行数除以自定义的每页显示的行数得到的结果
		//这是动态的，会根据你所在的页数得出你距离最后一页的页数
		if (page == null || page.equals("")) 
			page = "1";
		//获取预置在web.xml context中的数据 即获取常量
		//Context对象有两个作用：1可以从配置文件读取固定的数据，
		//2可以在程序运行时动态的存取数据 一对多 一个context对应多个servlet
		//getInitParameter("")是读取固定数据的，...Attribute(arg0)动态存取数据
		//Config对象只能从配置文件读参数读固定值 一对一 一个config对应一个servlet
		String length = getServletContext().getInitParameter("pageSize");
		CostDao dao = new CostDao();
		//查询资费
		List<Cost> list = dao.findPage(new Integer(page),new Integer(length));
		//查询总行数
		int line = dao.allLine();
		//算出总页数
		int total = line/new Integer(length);
		//取余运算：line除以length 的余数不为0的话...
		if (line%new Integer(length) != 0) {
		//每页6条数据除以总行数的出页数,若所得结果有余数总页数+1
			total++;
		}
		req.setAttribute("data", list);//页面数据
		req.setAttribute("total", total);//总页数
		req.setAttribute("page", page);//当前页
		//当前：/netctoss/findCost.do
		//目标：/netctoss/WEB-INF/cost/find.jsp
		req.getRequestDispatcher("WEB-INF/cost/find.jsp").forward(req, res);
	}
	
	/*
	 * 打开增加资费资费页面 没有数据交互也要写个servlet 》》》——《《《
	 */
	protected void addCost(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/cost/addCost.jsp").forward(req, res);
	}
	
	/**
	 *  新增自费项目并保存
	 * @param req
	 * @param res
	 * @throws IOException 
	 */
	protected void save(HttpServletRequest req,HttpServletResponse res) throws IOException {
		req.setCharacterEncoding("utf8");
		
		String name = req.getParameter("name");
		String costType = req.getParameter("radFeeType");
		String baseDuration = req.getParameter("baseDuration");
		String baseCost = req.getParameter("baseCost");
		String unitCost = req.getParameter("unitCost");
		String descr = req.getParameter("descr");
		Cost cost = new Cost();
		cost.setName(name);
		cost.setDescr(descr);
		cost.setCostType(costType);
		//将字符串类型转为业务所需的类型
		if (baseCost != null && !baseCost.equals("")) {
			cost.setBaseCost(new Double(baseCost));
			//TODO Double和Integer的包装类静态方法valueof会将已有的变为null 原因在于下面用的是ifelse
		}  if (unitCost != null && !unitCost.equals("")) {
			cost.setUnitCost(Double.valueOf(unitCost));
		}  if (baseDuration != null && !baseDuration.equals("")) {
			cost.setBaseDuration(Integer.valueOf(baseDuration));
		}
		//调用dao组件
		CostDao dao = new CostDao();
		dao.save(cost);
		
		//添加新数据后，重定向到查询，得到添加数据后的网页
		//当前:project/save.do
		//目标:project/WEB-INF/cost/find.jsp
		res.sendRedirect("findCost.do");
	}
	
	/**
	 * 打开修改资费页面，并将原有数据传入
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void toOpen(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException {
		CostDao dao = new CostDao();
		String id = req.getParameter("id");
		Cost cost = dao.toOpenData(Integer.valueOf(id));
		req.setAttribute("val", cost);
		req.getRequestDispatcher("WEB-INF/cost/upDate.jsp").forward(req, res);
	}
	
	/**
	 * 修改资费讯息后保存
	 * @param req
	 * @param res
	 * @throws IOException 
	 */
	protected void upDateCost(HttpServletRequest req,HttpServletResponse res) throws IOException {
		String costId = req.getParameter("costId");
		String name = req.getParameter("name");
		String costType = req.getParameter("radFeeType");
		String baseDuration = req.getParameter("baseDuration");
		String baseCost = req.getParameter("baseCost");
		String unitCost = req.getParameter("unitCost");
		String descr = req.getParameter("descr");
		//本身为字符串的内容在此不作判空。严格来说那是前端干的，而同时本人非全栈开发者...忽略这个已发现的bug
		Cost c = new Cost();
		c.setCostId(Integer.valueOf(costId));
		c.setName(name);
		c.setCostType(costType);
		c.setDescr(descr);
		if (baseDuration != null && !baseDuration.equals("")) {
			c.setBaseDuration(Integer.valueOf(baseDuration));
		}
		if (baseCost != null && !baseCost.equals("")) {
			c.setBaseCost(Double.valueOf(baseCost));
		}
		if (unitCost != null && !unitCost.equals("")) {
			c.setUnitCost(Double.valueOf(unitCost));
		}
		
		CostDao dao = new CostDao();
		dao.toUpDate(c);
		//
		res.sendRedirect("findCost.do");
		
	}
	
}
