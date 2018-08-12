package t.sql.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
/***
 * 用来查询数据的接口标识
 * @author zhangj
 * @date 2018年8月12日 上午10:15:48
 * @email zhangjin0908@hotmail.com
 */
public @interface Query {
	/**
	 * 返回当前要执行的sql语句
	 * @return
	 */
	String sql();
}

