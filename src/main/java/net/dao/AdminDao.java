package net.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.AdminInfo;
import util.DBUtil;

public class AdminDao {
	/**
	 * 登录
	 * @param admin
	 * @return
	 */
	public AdminInfo findUser(String adminstration){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select *  from admin_info where admin_code = ? ";
			PreparedStatement ps = conn.prepareStatement(sql);
			//报索引列无效异常的原因是因为 admin 值为null 随后发现有值也报列无效 原因在于'?'的单引号
			ps.setString(1, adminstration);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				AdminInfo user = new AdminInfo();
				//将查询出来的值赋值给表中的列 (将值映射到实体类中)
				user.setAdminId(rs.getInt("admin_id"));
				user.setAdminCode(rs.getString("admin_code"));
				user.setPassword(rs.getString("password"));
				return user;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询管理员失败");
		} finally {
			if (conn != null) {
				DBUtil.close(conn);
			}
		}
	}
}
