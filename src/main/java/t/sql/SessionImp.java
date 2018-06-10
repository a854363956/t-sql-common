package t.sql;

import java.sql.Connection;
import java.util.Collection;

import t.sql.interfaces.DTO;
import t.sql.query.Query;
import t.sql.query.QueryImp;
import t.sql.transaction.Transaction;
import t.sql.utils.SqlUtils;
/**
 * Session的实现类
 * @author zhangj
 * @date   2018-05-20 19:37:43
 * @email  zhangjin0908@hotmail.com
 */
public class SessionImp implements Session{
	private Connection connection;
	private Transaction transaction;
	private SqlUtils sqlUtils;
	public SessionImp(Connection connection) {
		this.connection= connection;
		this.sqlUtils = new SqlUtils();
	}
	
	@Override
	public void update(DTO data) {
		sqlUtils.toUpdateSqlDto(data, connection);
	}

	@Override
	public void delete(DTO data) {
		sqlUtils.toDeleteSqlDto(data, connection);
	}
	@Override
	public void create(DTO data) {
		sqlUtils.toCreateSqlDto(data, connection);
	}

	@Override
	public <T> Query<T> createQuery(String sql,Class<?> clzz) {
		return new QueryImp<T>(sql,connection,clzz);
	}

	@Override
	public Transaction openTransaction() {
		if(transaction == null) {
			return new Transaction(connection);
		}else {
			return this.transaction;
		}
	}

	@Override
	public void updateBatch(Collection<DTO> datas) {
		sqlUtils.toUpdateSqlDtoJDBCBatch(datas, connection);
	}

	@Override
	public void createBatch(Collection<DTO> datas) {
		sqlUtils.toCreateSqlDtoJDBCBatch(datas, connection);
	}
}
