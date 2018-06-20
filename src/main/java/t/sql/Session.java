package t.sql;


import java.util.Collection;

import t.sql.interfaces.DTO;
import t.sql.query.Query;
import t.sql.transaction.Transaction;

/**
 * 用来处理当前sql查询的工具类
 * @author zhangj
 * @date  2018-05-20 09:17:14 
 * @email zhangjin0908@hotmail.com
 */
public interface Session {
	void update(DTO data);
	void delete(DTO data);
	void create(DTO data);
	/**
	 *批量更新
	 */
	void updateBatch(Collection<DTO> datas);
	/**
	 * 批量创建数据
	 * @param datas 要创建的数据,必须实现DTO的接口
	 */
	void createBatch(Collection<DTO> datas);
	/**
	 * 创建查询对象
	 * @param sql  要查询的sql
	 * @param clzz 要转换的实体
	 * @return 
	 */
	<T> Query<T> createQuery(String sql,Class<?> clzz);
	/**
	 * 开启事务管理
	 * @return
	 */
	Transaction openTransaction();
	/***
	 * 执行DMLSQL
	 * @param sql  要执行的SQL语句
	 * @param obj  要替换的参数
	 * @return
	 */
	int nativeDMLSQL(String sql,Object... obj);
}
