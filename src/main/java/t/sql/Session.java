package t.sql;


import java.util.Collection;

import t.sql.interfaces.DTO;
import t.sql.query.Query;
import t.sql.transaction.TransactionObject;
import t.sql.transaction.TransactionVoid;

/**
 * 用来处理当前sql查询的工具类
 * @author zhangj
 * @date  2018-05-20 09:17:14 
 * @email zhangjin0908@hotmail.com
 */
public interface Session {
	/**
	 * 根据DTO来进行更新数据
	 * @param data
	 */
	void update(DTO data);
	/**
	 * 根据DTO来进行删除数据
	 * @param data
	 */
	void delete(DTO data);
	/**
	 * 根据DTO来进行创建数据
	 * @param data
	 */
	void create(DTO data);
	/**
	 *批量更新
	 */
	<T extends DTO> void updateBatch(Collection<T> datas);
	/**
	 * 批量创建数据
	 * @param datas 要创建的数据,必须实现DTO的接口
	 */
	<T extends DTO> void createBatch(Collection<T> datas);
	/**
	 * 批量删除数据
	 * @param datas 要删除的数据,必须实现DTO的接口
	 */
	<T extends DTO> void deleteBatch(Collection<T> datas);
	/**
	 * 创建查询对象
	 * @param sql  要查询的sql
	 * @param clzz 要转换的实体
	 * @return 
	 */
	<T> Query<T> createQuery(String sql,Class<?> clzz);
	/**
	 * 开启事务管理,并在事务提供的接口里进行操作
	 * @return 可以返回自定义数据
	 */
	<T> T transactionObject(TransactionObject<T> t);
	/**
	 * 开启事务管理,并在事务提供的接口里进行操作 无任何返回值
	 */
	void transactionVoid(TransactionVoid t);
	/***
	 * 执行DMLSQL
	 * @param sql  要执行的SQL语句
	 * @param obj  要替换的参数
	 * @return
	 */
	int nativeDMLSQL(String sql,Object... obj);

}
