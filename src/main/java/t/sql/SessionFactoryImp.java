package t.sql;


import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class SessionFactoryImp implements SessionFactory{
	private DataSource ds;
	public SessionFactoryImp(DataSource ds) {
		this.ds =ds;
	}
	public static final ThreadLocal<Session> session = new ThreadLocal<Session>();
	
	@Override
	public Session getCurrentSession() {
		try {
			if(session.get()==null) {
				session.set(new SessionImp(ds.getConnection()));
			}
			return session.get();
		} catch (SQLException e) {
			throw new t.sql.exception.TSQLException(e);
		}
		
	}

	@Override
	public Connection openConnection()  {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			throw new t.sql.exception.TSQLException(e);
		}
	}

}
