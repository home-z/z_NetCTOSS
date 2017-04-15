package test.dao;

import org.junit.Test;

import entity.AdminInfo;
import net.dao.AdminDao;


public class TestAdim {
	
	@Test
	public void checkLogin() {
		AdminDao dao = new AdminDao();
		String value = "admin";
		AdminInfo admin = dao.findUser(value);
		System.out.println(admin.getPassword());
	}
}


