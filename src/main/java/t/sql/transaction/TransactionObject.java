package t.sql.transaction;

import java.sql.SQLException;

/***
 * 执行事务的基础接口
 * @author zhangj
 * @date 2018年8月12日 下午1:29:09
 * @email zhangjin0908@hotmail.com
 */
@FunctionalInterface  
public interface TransactionObject<T> {
	T execute() throws SQLException;
}

