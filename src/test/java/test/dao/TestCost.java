package test.dao;

import org.junit.Test;

import net.dao.CostDao;

public class TestCost {
	@Test
	public void testGetLine() {
		CostDao dao = new CostDao();
		int line = dao.allLine();
		System.out.println(line);
	}
}

class test{
	@Test
	public static void main(String[] args) {
		int page = 2;
		int size = 6;
		int a = (page-1)*size+1;
		System.out.println(a);
		int b = page*size;
		System.out.println(b);
		System.out.println(page);
		System.out.println(size);
	}
}
