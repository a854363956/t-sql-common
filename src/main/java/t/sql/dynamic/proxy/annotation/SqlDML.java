package t.sql.dynamic.proxy.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;

import t.sql.dynamic.proxy.enums.SqlType;

@Retention(RUNTIME)
@Target(METHOD)
/***
 * 进行SQLMDL操作的注解
 * @author zhangj
 * @date 2018年9月27日 上午10:48:15
 * @email zhangjin0908@hotmail.com
 */
public @interface SqlDML {
	/**
	 * 要执行的SQL语句
	 * @return
	 */
	String  sql();
	/**
	 * sql的类型，默认情况下sql的类型是查询
	 * @return
	 */
	SqlType type() default SqlType.SELECT;
	/**
	 * sql 查询超时时间，不表示更新和删除的超时时间
	 * @return
	 */
	int    timeOut() default 10;
	
	/**
	 * 当前执行后的返回类型
	 * @return
	 */
	Class<?>   returnType() default HashMap.class;
}

