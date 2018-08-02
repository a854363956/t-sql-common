package t.sql.query;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import t.sql.exception.TSQLException;
/**
 * 查询对象
 * @author zhangj
 * @date   2018年5月20日 上午9:47:13 
 * @email  zhangjin0908@hotmail.com
 * @param <T>
 */
public interface Query<T> {
	/**
	 * 调用此方法将返回一个真实的查询结果集
	 * @return 执行查询的sql返回一个list的结果集
	 */
	public List<T> list();
	/**
	 * 设置JDBC的超时时间 单位毫秒
	 * @param time  jdbc的超时时间,秒单位
	 */
	public void setTimeOut(int time);
	/**
	 * 设置参数
	 * @param name 参数名称
	 * @param val  参数值
	 * @throws TSQLException
	 */
	public void setParameter(String name,Object val) throws TSQLException;
	
	/**
	 * 设置List参数
	 * @param name  参数名称
	 * @param vals  符合参数的List结果集
	 * @throws TSQLException
	 */
	public void setListParameter(String name,Collection<T> vals) throws TSQLException;
	/**
	 * 设置当前时间
	 * @param name 参数名称
	 * @param time 时间参数
	 */
	public void setTime(String name,Date time);
	/**
	 * 设置当前时间
	 * @param name 参数名称
	 * @param time 时间参数
	 */
	public void setTimestamp(String name,Date time);
	/**
	 * 设置文本内容
	 * @param name 参数名称
	 * @param val  要设置的文本内容
	 */
	public void setText(String name,String val);
	/**
	 * 设置短整数
	 * @param name 参数名称
	 * @param val  要设置的浮点值的名称
	 */
	public void setShort(String name, short val);
	/**
	 * 设置的长整型
	 * @param name 参数名称 
	 * @param val  整数类型
	 */
	public void setLong(String name,long val);
	/**
	 * 要设置的浮点值
	 * @param name 参数名称
	 * @param val  浮点值
	 */
	public void setFloat(String name,float val);
	/**
	 * 要设置的Int类型
	 * @param name 参数的名称
	 * @param val  整数类型
	 */
	public void setInteger(String name,int val);
	/**
	 * 此方法用来查询单个返回值,如果查询里面返回值为空,那么将返回为null
	 * @return
	 */
	public T uniqueResult();
}
