package util;

import java.io.IOException;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

public class DBUtil {
	private  static BasicDataSource ds;
	
	static {
		try {
		Properties op = new Properties();
			op.load(DBUtil.class.getClassLoader().getResourceAsStream("source/db.properties"));
			
			ds = new BasicDataSource();
			ds.setDriverClassName(op.getProperty("jdbc.driverclass"));
			ds.setUrl(op.getProperty("jdbc.url"));
			ds.setUsername(op.getProperty("jdbc.user"));
			ds.setPassword(op.getProperty("jdbc.password"));
			ds.setMaxActive(Integer.parseInt(op.getProperty("dbcp.maxActive")));
			//TODO 数据库连接池 无setInitialSize方法 原因是jdbcjar版本不对
			ds.setInitialSize(Integer.parseInt(op.getProperty("dbcp.initSize")));
		} catch (IOException e) {
			throw new RuntimeException("读取属性文件出错");
		}
	}
	
	public static Connection getConnection() throws SQLException {
			return ds.getConnection();
	}
	
	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("归还连接失败");
			}
		}
	}
	
	/**
	 * 测试
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		Connection conn = getConnection();
		System.out.println(conn.getClass().getName());
	}
	
}
