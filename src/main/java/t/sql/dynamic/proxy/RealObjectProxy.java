package t.sql.dynamic.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import t.sql.dynamic.proxy.enums.SqlType;

/***
 * 真实被代理的对象
 * @author zhangj
 * @date 2018年9月27日 上午10:44:38
 * @email zhangjin0908@hotmail.com
 */
public class RealObjectProxy implements InvocationHandler{
	private t.sql.SessionFactory sessionFactory;
	
	public RealObjectProxy(t.sql.SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 如果方法名称为获取当前Session
		if(method.getName().equals("getCurrentSession")) {
			return sessionFactory.getCurrentSession();
		}
		
		
		t.sql.dynamic.proxy.annotation.SqlDML sqlDml = method.getAnnotation(t.sql.dynamic.proxy.annotation.SqlDML.class);
		if(sqlDml == null) {
			throw new t.sql.exception.TSQLException("@SqlDML annotations are not used in current methods.");
		}else {
			String sql = sqlDml.sql();
			SqlType type =sqlDml.type();
			Parameter[] parameter = method.getParameters();
			Class<?>  clazz = sqlDml.returnType();
			int timeOut = sqlDml.timeOut();
			if(type == SqlType.SELECT) {
				t.sql.query.Query query = sessionFactory.getCurrentSession().createQuery(sql,clazz);
				query.setTimeOut(timeOut);
				for(int i=0;i<parameter.length;i++) {
					Parameter parame = parameter[i];
					Object    value  = args[i];
					query.setParameter(parame.getName(),value);
				}
				if(java.util.Collection.class.isAssignableFrom(method.getReturnType())) {
					return query.list();
				}else {
					return query.uniqueResult();
				}
			}else if(type == SqlType.INSERT || type == SqlType.UPDATE || type == SqlType.DELETE) {
				return sessionFactory.getCurrentSession().nativeDMLSQL(sql,args);
			}
			throw new t.sql.exception.TSQLException("Invalid SQl type ["+type+"]");
		}
	}
	

}

