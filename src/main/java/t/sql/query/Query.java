package t.sql.query;

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
	public void setParameter(String name,Object val) throws TSQLException;
	/**
	 * 设置JDBC的超时时间 单位毫秒
	 * @param time  jdbc的超时时间,秒单位
	 */
	public void setTimeOut(int time);
	public void setTime(String name,Date time);
	public void setTimestamp(String name,Date time);
	public void setText(String name,String val);
	public void setShort(String name, short val);
	public void setLong(String name,long val);
	public void setFloat(String name,float val);
	public void setInteger(String name,int val);
	/**
	 * 此方法用来查询单个返回值,如果查询里面返回值为空,那么将返回为null
	 * @return
	 */
	public T uniqueResult();
}
