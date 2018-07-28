package t.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import t.sql.exception.TSQLException;
import t.sql.interfaces.DTO;
import t.sql.query.Query;
import t.sql.query.QueryImp;
import t.sql.transaction.Transaction;
import t.sql.utils.SqlUtils;
import t.sql.utils.VerificationUtils;
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
		VerificationUtils.check(data);
		sqlUtils.toUpdateSqlDto(data, connection);
	}

	@Override
	public void delete(DTO data) {
		VerificationUtils.check(data);
		sqlUtils.toDeleteSqlDto(data, connection);
	}
	@Override
	public void create(DTO data) {
		VerificationUtils.check(data);
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
		for(DTO dto :datas) {
			VerificationUtils.check(dto);
		}
		sqlUtils.toUpdateSqlDtoJDBCBatch(datas, connection);
	}
	
	@Override
	public void createBatch(Collection<DTO> datas) {
		for(DTO dto :datas) {
			VerificationUtils.check(dto);
		}
		sqlUtils.toCreateSqlDtoJDBCBatch(datas, connection);
	}

	@Override
	public int nativeDMLSQL(String sql, Object... objs) {
		PreparedStatement ps = null;
		try {
			ps =connection.prepareStatement(sql);
			for(int i=0;i<objs.length;i++) {
				ps.setObject(i+1, objs[i]);
			}
			return ps.executeUpdate();
		} catch (Exception e) {
			throw new TSQLException(e);
		}finally {
			if(ps!=null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public void deleteBatch(Collection<DTO> datas) {
		sqlUtils.toDeleteSqlDtoJDBCBatch(datas, connection);
	}

}
