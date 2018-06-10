package t.sql;


import javax.sql.DataSource;

import t.sql.exception.TSQLException;

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
		} catch (Exception e) {
			throw new TSQLException(e);
		}
	}

}
