package net.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.ThrowStatement;

import com.sun.org.apache.regexp.internal.recompile;

import entity.AdminInfo;
import entity.Cost;
import util.DBUtil;

public class CostDao {
	/**
	 * 提前要封装的代码alt+shift+m
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Cost createCost(ResultSet rs) throws SQLException {
		Cost c = new Cost();
		c.setCostId(rs.getInt("cost_id"));
		c.setName(rs.getString("name"));
		c.setBaseDuration(rs.getInt("base_duration"));
		c.setBaseCost(rs.getDouble("base_cost"));
		c.setUnitCost(rs.getDouble("unit_cost"));
		c.setStatus(rs.getString("status"));
		c.setDescr(rs.getString("descr"));
		c.setCreatime(rs.getTimestamp("creatime"));
		c.setStartime(rs.getTimestamp("startime"));
		c.setCostType(rs.getString("cost_type"));
		return c;
	}
	
	/**
	 * 
	 * @return 返回Cost
	 */
	public List<Cost> findAll() {
		//TODO 将被查询页数的方法替换
//		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = null;
	try {
		conn = DBUtil.getConnection();
		//按cost_id默认排序
		String sql  = "select * from cost order by cost_id";
			//ps预编译
			PreparedStatement ps = conn.prepareStatement(sql);
			//执行sql查询语句 ，没有参数直接执行，返回的结果集对象rsultset
			ResultSet result = ps.executeQuery();
			List<Cost> list = new ArrayList<Cost>();
			//移动光标到下一条结果
			while(result.next()) {
				Cost cost =  createCost(result);
				list.add(cost);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询数据失败");
		} finally {
			if (conn != null) {
				DBUtil.close(conn);
			}
		}
	}
	
	/**
	 * 状态默认为暂停态1；
	 * 创建时间默认为系统时间；
	 * 开通时间默认为null；
	 */
	public void save(Cost cost) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			//业务中后俩个参数是默认值 系统的默认时间 默认开通时间为null
			String sql ="insert into cost values(" + "cost_seq.nextval"+",?,?,?,?,1,?,sysdate,null,?)";
			
			//编译
			PreparedStatement ps = conn.prepareStatement(sql);
			//赋值
			ps.setString(1, cost.getName());
			//jdbc中setInt/setDouble不允许传入null；
			//但当前业务中这些字段是允许为null；
			//将方法换成ps.setObject
			ps.setObject(2, cost.getBaseDuration());
			ps.setDouble(3, cost.getBaseCost());
			ps.setObject(4, cost.getUnitCost());
			ps.setString(5, cost.getDescr());
			ps.setString(6, cost.getCostType());
			//执行
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("添加资费信息失败",e);
		} finally {
			DBUtil.close(conn);
		}
	}
	
	public Cost toOpenData(Integer id) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from cost where cost_id=?");
			ps.setInt(1, id);
			ResultSet rs =  ps.executeQuery();
		   if (rs.next()) {
			return createCost(rs);
		}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询失败", e);
		} finally {
			if (conn != null) {
				DBUtil.close(conn);
			}
		}
		return null;
	}
	
	
	public void toUpDate(Cost c) {
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			//TODO 若有异常 可尝试去掉占位符的单引号
			String sql ="UPDATE cost SET name='?',base_duration='?',base_cost='?',unit_cost='?',cost_type='?' "
					+ ",descr='?' "
					+ " where cost_id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, c.getName());
			ps.setInt(2, c.getBaseDuration());
			ps.setDouble(3, c.getBaseCost());
			ps.setDouble(4, c.getUnitCost());
			ps.setString(5, c.getCostType());
			ps.setString(6, c.getDescr());
			ps.setInt(7, c.getCostId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("更新资费失败", e);
		} finally {
			if (con != null) {
				DBUtil.close(con);
			}
		}
	}
	
	
	
	/**
	 * 
	 * @param id
	 */
	public void upDateCost(String id) {
		//TODO 删除资费
//		Connection con = null;
//		try {
//			con = DBUtil.getConnection();
//			String sql = "up";
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	
	}
	
	/**
	 * 
	 * @return 行数量
	 */
	public int allLine() {
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			String sql = "select count(*) from cost";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				// 第一个列是 1，第二个列是 2，……
				return rs.getInt(1);
			}
			} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询总行数失败");
		} finally {
			if (con != null) {
				DBUtil.close(con);
			}
		}
		return 0;
	}
	
	/**
	 * 查询某一页的资费数据
	 * @param page 页数
	 * @param size 每页显示的行数
	 * @return
	 */
	public List<Cost> findPage(int page,int size) {
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			String sql = "select * from ( "
					+ " select c.*,rownum r from ( "
					+ " select * from cost order by cost_id "
					+ " ) "
					+ " c) where r between ? and ?";
			PreparedStatement ps = con.prepareStatement(sql);
			//算出起始行和结束行
			//第一?是起始行：上一页（page-1）最后一行+1 （就是当前页的第一行） 
			ps.setInt(1, (page-1)*size+1);
			//第二?是终止行(相对的)：当前页*每页显示条数
			ps.setInt(2, page*size);
			ResultSet rs = ps.executeQuery();
			List<Cost> costList = new ArrayList<Cost>();
			while(rs.next()) {
				Cost cost = createCost(rs);
				costList.add(cost);
			}
			return costList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("分页查询资费失败",e);
		} finally {
				DBUtil.close(con);
		}
	}
	
	
	
	
	
	
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		CostDao dao = new CostDao();
		//查询测试
//		Cost cost = dao.findById(4);
//		String name = cost.getName();
//		System.out.println(name);
//		List<Cost> list = dao.findAll();
//		if (list != null) {
//			for (Cost value : list) {
//				System.out.println(value.getName());
//			}
//		}
		
		Cost cost = new Cost();
		cost.setName("包月套餐");
//		cost.setBaseDuration(50);//包月没有基本费用
		cost.setBaseCost(600.0);
//		cost.setUnitCost(0.5);包月没有超时费用
		cost.setDescr("包月很爽");
		cost.setCostType("1");
		dao.save(cost);
		System.out.println("save over");
	}
}
