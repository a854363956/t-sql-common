package test.t.sql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import t.sql.Session;
import t.sql.SessionFactory;
import t.sql.SessionFactoryImp;
import t.sql.interfaces.DTO;
import t.sql.query.Query;

public class TestSession {
	private SessionFactory sf;

   @Before
	public void onInit() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://localhost:3306/t_base");
		config.setUsername("root");
		config.setPassword("");
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("autoCommit", "true");

		HikariDataSource ds = new HikariDataSource(config);
		sf = new SessionFactoryImp(ds);
	}
   @Test
   public void testProxy() {
	   TestInterface o = (TestInterface) t.sql.dynamic.proxy.DynamicProxy.getInstance(TestInterface.class,sf);
	   System.out.println(o.test().toString());
	   System.out.println(o.getCurrentSession().toString());
   }
	@Test
	public void testList() throws Exception {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("ABCD","123");
		m.put(null,"1234");
		list.add(m);
		System.out.println(t.sql.utils.ListUtils.toLowerCaseMapKey(list));
	}
	@Test
	public void testCreate() {
		test.t.sql.dto.Test t = new test.t.sql.dto.Test();
		t.setId("id");
		t.setName("name");
		t.setValue("value");
		t.setCommen("commen");
		sf.getCurrentSession().create(t);
	}

	@Test
	public void testDelete() {
		test.t.sql.dto.Test t = new test.t.sql.dto.Test();
		t.setId("id");
		t.setName("name");
		t.setValue("value");
		t.setCommen("commen");
		sf.getCurrentSession().create(t);
		t.setCommen("test");
		sf.getCurrentSession().delete(t);
	}


	@Test
	public void testBigUpdate() throws SQLException {
		test.t.sql.dto.Test t = new test.t.sql.dto.Test();
		t.setId("id");
		t.setName("name");
		t.setValue("value");
		t.setCommen("commen");
		sf.getCurrentSession().create(t);
		t.setCommen("123");
		List<DTO> dtos = new ArrayList<DTO>();
		dtos.add(t);
		sf.getCurrentSession().updateBatch(dtos);
		;
	}

	@Test
	public void testUpdate() {
		test.t.sql.dto.Test t = new test.t.sql.dto.Test();
		t.setId("id");
		t.setName("name");
		t.setValue("value");
		t.setCommen("commen");
		sf.getCurrentSession().create(t);
		t.setCommen("test");
		sf.getCurrentSession().update(t);
	}


	@Test
	public void test() {

	}

	@Test
	public void testQuery() {
		Session session = sf.getCurrentSession();
		Query<Map<String, Object>> q = session.createQuery("select * from test where id=:id", HashMap.class);
		q.setTimeOut(20);
		q.setParameter("id", "4b0621afba0844c79b02072e9f1e990e");
		List<Map<String, Object>> datas = q.list();
		System.out.println(datas.size());
	}
}
