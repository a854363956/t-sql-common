package t.sql.validates;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 字符串的验证方式类,强烈推荐使用正则表达式来进行校验
 * @author zhangj
 * @date   2018年6月14日 上午12:33:07 
 * @email  zhangjin0908@hotmail.com
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Verification{
	/**
	 * 当前实体中是否可以为空
	 * @return  true表示不为空, false表示可以为空
	 */
	boolean notNull();
	/**
	 * 白名单正则表达式,表示如果匹配到此正则表达式就允许通过,否则抛出异常
	 * @return  允许通过的正则表达式
	 */
	String  whitelistRegular() default "";
	/**
	 * 黑名单正则表达式,如果匹配到此正则表达式就不允许通过交易,否则允许通过校验
	 * @return  不允许通过的正则表达式
	 */
	String  blacklistRegular() default "";
	/**
	 * 如果出现错误,抛出的异常提示信息
	 * @return
	 */
	String  message();
}
