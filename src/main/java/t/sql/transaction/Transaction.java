package t.sql.transaction;

import java.sql.Connection;

import t.sql.exception.TSQLException;
/**
 * 添加事物管理
 * @author zhangj
 * @date   2018年6月9日 下午7:49:07 
 * @email  zhangjin0908@hotmail.com
 */
public class Transaction {
	private Connection connection ;
	public Transaction(Connection connection) {
		try {
			connection.setAutoCommit(false);
			this.connection = connection;
		} catch (Exception e) {
			throw new TSQLException(e);
		}
	}
	public void commit() {
		try {
			if(connection.isClosed()) {
				throw new TSQLException("The connection is closed,Unable commit!");
			}else {
				connection.commit();
			}
		} catch (Exception e) {
			throw new TSQLException(e);
		}
	}
	public void rollback() {
		try {
			if(connection.isClosed()) {
				throw new TSQLException("The connection is closed,Unable rollback!");
			}else {
				connection.rollback();
			}
		} catch (Exception e) {
			throw new TSQLException(e);
		}
	}
}
