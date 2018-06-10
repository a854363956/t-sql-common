package t.sql;

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
	 */
	Session getCurrentSession();
}
