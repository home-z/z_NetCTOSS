package net.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.sun.java_cup.internal.runtime.virtual_parse_stack;
import com.sun.org.apache.regexp.internal.recompile;

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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
			String sql ="UPDATE cost SET name='?',cost_type='?', "
					+ "base_duration='?',base_cost='?',unit_cost='?',descr='?' "
					+ " where cost_id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, c.getName());
			ps.setString(2, c.getCostType());
			ps.setInt(3, c.getBaseDuration());
			ps.setDouble(4, c.getBaseCost());
			ps.setDouble(5, c.getUnitCost());
			ps.setString(6, c.getDescr());
			ps.setInt(7, c.getCostId());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("更新资费失败", e);
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
