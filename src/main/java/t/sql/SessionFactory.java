package t.sql;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 用来创建session的工厂方法
 * @author zhangj
 * @date   2018-05-20 09:11:59
 * @email  zhangjin0908@hotmail.com
 */
public interface SessionFactory {
	/**
	 * 获取当前线程的session,用户不需要手动管理此session对象
	 * @return 
	 * @throws SQLException 
	 */
	Session getCurrentSession() ;
	
	/**
	 * 获取Java数据库的Connection连接池,注意: 需要手动来进行关闭,这个不会自动管理Connetcion
	 * @return  返回数据库的Connection
	 * @throws SQLException 
	 */
	Connection openConnection() ;
}
