package t.sql.dynamic.proxy;

import java.lang.reflect.Proxy;

/***
 * 动态代理，用来动态创建接口的实现类
 * @author zhangj
 * @date 2018年9月27日 上午10:42:11
 * @email zhangjin0908@hotmail.com
 */
public class DynamicProxy {
	public static Object getInstance(Class<?> cls, t.sql.SessionFactory sessionFactory){
		 RealObjectProxy invocationHandler = new RealObjectProxy(sessionFactory);
        Object newProxyInstance = Proxy.newProxyInstance(
                cls.getClassLoader(),
                new Class[] { cls },
                invocationHandler);
        return (Object)newProxyInstance;
    }

}

