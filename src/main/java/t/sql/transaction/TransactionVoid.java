package t.sql.transaction;

import java.sql.SQLException;

import t.sql.Session;

/***
 * 执行事务无返回值的接口
 * @author zhangj
 * @date 2018年8月12日 下午2:07:10
 * @email zhangjin0908@hotmail.com
 */
@FunctionalInterface
public interface TransactionVoid {
	void execute(Session session) throws SQLException;
}

