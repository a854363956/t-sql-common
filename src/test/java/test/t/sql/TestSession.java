package test.t.sql;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
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
import t.sql.transaction.Transaction;
import t.sql.utils.StringUtils;

public class TestSession {
	private SessionFactory sf;

	@Before
	public void onInit() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://localhost:3306/test_t_sql");
		config.setUsername("test_t_sql");
		config.setPassword("test_t_sql");
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("autoCommit", "true");

		HikariDataSource ds = new HikariDataSource(config);
		sf = new SessionFactoryImp(ds);
	}
	@Test
	public void testGoogle() throws Exception {
	
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

	@Test // 152秒
	public void testBigCreate() {
		Date d = new Date();
		List<DTO> datas = new ArrayList<DTO>();
		for (int i = 0; i < 10000; i++) {
			test.t.sql.dto.Test t = new test.t.sql.dto.Test();
			t.setName("name");
			t.setValue("value");
			t.setCommen("commen");
			datas.add(t);
		}
		Session session = sf.getCurrentSession();
		Transaction t = session.openTransaction();
		session.createBatch(datas);
		t.commit();
		System.out.println((new Date().getTime() - d.getTime()));
	}

	@Test
	public void testBigUpdate() {
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

	@SuppressWarnings("unchecked")
	@Test
	public void testDeleteBatch() {
		Session session = sf.getCurrentSession();
		Transaction t = session.openTransaction();
		Query<test.t.sql.dto.Test> q = session.createQuery("select * from test", test.t.sql.dto.Test.class);
		List<?> list = q.list();
		session.deleteBatch((List<DTO>) list);
		t.commit();
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
