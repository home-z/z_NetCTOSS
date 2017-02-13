package net.business;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Cost;
import net.dao.CostDao;

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
			toOpenUpDate(req, res);
		}else {
			throw new RuntimeException("没有这个页面");
		}
	}

	/**
	 * 
	 * @param req 请求
	 * @param res 响应
	 * @throws ServletException 
	 * @throws IOException
	 */
	protected void findCost(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException {
		CostDao dao = new CostDao();
		List<Cost> list = dao.findAll();
		req.setAttribute("data", list);
		req.getRequestDispatcher("WEB-INF/cost/find.jsp").forward(req, res);
	}
	
	/*
	 * 打开增加资费资费页面 没有数据交互也要写个servlet 》》》——《《《
	 */
	protected void addCost(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/cost/addCost.jsp").forward(req, res);
	}
	
	/**
	 * 
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
		System.out.println(unitCost+"jbfy"+baseCost);
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
	protected void toOpenUpDate(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException {
		
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
	 */
	protected void upDateCost(HttpServletRequest req,HttpServletResponse res) {
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
	}
	
}
