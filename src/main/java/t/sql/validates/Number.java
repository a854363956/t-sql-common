package t.sql.validates;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/***
 * 数字校验器  注意: 数字校验器,目前只支持Long类型以及Integer类型
 * @author zhangj
 * @date 2018年7月28日 上午10:01:53
 * @email zhangjin0908@hotmail.com
 */
@Retention(RUNTIME)
@Target({ FIELD })
public @interface Number {
	/**
	 * 如果出现错误,抛出的异常提示信息
	 * @return  返回要抛出的信息
	 */
	Text  message();
	/**
	 * 当前实体中是否可以为空
	 * @return  true表示不为空, false表示可以为空
	 */
	boolean notNull();
	/**
	 * 当前允许的最小值,默认情况下,这个值允许的最小值是long类型的最小值
	 * @return
	 */
	double  min() default -9223372036854775808L;
	/**
	 * 当前允许的最大值,默认情况下,这个值是long类型的最大值
	 * @return
	 */
	double  max() default 9223372036854775807L;
}

