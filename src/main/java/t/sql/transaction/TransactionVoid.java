package t.sql.transaction;

import java.sql.SQLException;

import t.sql.Session;

/***
 * 
 * @author zhangj
 * @date 2018年8月12日 下午2:07:10
 * @email zhangjin0908@hotmail.com
 */
public interface TransactionVoid {
	void execute(Session session) throws SQLException;
}

