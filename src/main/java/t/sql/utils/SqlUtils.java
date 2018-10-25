package t.sql.utils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


import t.sql.exception.TSQLException;
import t.sql.interfaces.DTO;

public class SqlUtils {
	public int toUpdateSqlDto(DTO dto,Connection connection) {
		PreparedStatement ps  = null;
		try {
			// 注解属性和字段名称的对应
			Map<String,String> attributes = new java.util.HashMap<String,String>();
			
			Class<?> clzz = dto.getClass();
			Field[] fields =clzz.getDeclaredFields();
			String tableName=clzz.getDeclaredAnnotation(Table.class).name();
			if("".equals(tableName)) {
				tableName=clzz.getSimpleName();
			}
			StringBuffer where =getWhereById(fields);
			StringBuffer set=new StringBuffer(" set ");
			for(Field field :fields) {
				Column column =field.getDeclaredAnnotation(Column.class);
				if(column != null) {
					String name = column.name();
					// 如果等于空直接取字段名称，不等于空则添加属性和注解的对应关系
					if("".equals(name)) {
						name=field.getName();
					}else {
						attributes.put(name,field.getName());
					}
					set.append(name);
					set.append("=:");
					set.append(name);
					set.append(",");
				}
			}
			String _set = set.substring(0, set.length()-1);
			String sql = String.format("update %s %s %s", tableName,_set,where);
			String exSql = sql.replaceAll(":[a-zA-Z_]+", "?");
			List<String> l = StringUtils.findRegular(sql, ":[a-zA-Z_]+");
			ps =connection.prepareStatement(exSql);
			for(int i=0;i<l.size();i++) {
				String name=l.get(i).replace(":", "");
				String aKey = attributes.get(name);
				Field f = null;
				if(aKey == null) {
					f= clzz.getDeclaredField(name);
				}else {
					f= clzz.getDeclaredField(aKey);
				}
				f.setAccessible(true);
				Object val =f.get(dto);
				ps.setObject(i+1, val);
			}
			int i = ps.executeUpdate();
			if(i<1) {
				throw new TSQLException(String.format("No update data! id is %s", Arrays.toString(l.toArray())));
			}else if(i>1){
				throw new TSQLException(String.format("Overabundance of update data! id is %s", Arrays.toString(l.toArray())));
			}else {
				return i ;
			}
		} catch (Exception e) {
			throw new TSQLException(e);
		}finally {
			if(ps != null ) {
				try {
					ps.close();
				} catch (SQLException e) {
					throw new TSQLException(e);
				}
			}
		}
	}
	/**
	 * 执行更新的SQL语句
	 * @param dto
	 * @param connection
	 * @return
	 */
	public  int toDeleteSqlDto(DTO dto,Connection connection) {
		PreparedStatement ps  = null;
		try {
			Class<?> clzz = dto.getClass();
			Field[] fields =clzz.getDeclaredFields();
			String tableName=clzz.getDeclaredAnnotation(Table.class).name();
			if("".equals(tableName)) {
				tableName=clzz.getSimpleName();
			}
			StringBuffer where = getWhereById(fields);
			String sql =String.format("delete from %s %s", tableName,where);
			List<String> l = StringUtils.findRegular(sql, ":[A-z_]+");
			String exSql = sql.replaceAll(":[a-zA-Z_]+", "?");
		    ps =connection.prepareStatement(exSql);
			for(int i=0;i<l.size();i++) {
				String name=l.get(i).replace(":", "");
				Field f= clzz.getDeclaredField(name);
				f.setAccessible(true);
				Object val =f.get(dto);
				ps.setObject(i+1, val);
			}
			int i = ps.executeUpdate();
			if(i<1) {
				throw new TSQLException(String.format("No delete data! id is %s", Arrays.toString(l.toArray())));
			}else if(i>1){
				throw new TSQLException(String.format("Overabundance of deleted data! id is %s", Arrays.toString(l.toArray())));
			}else {
				return i ;
			}
		} catch (Exception e) {
			throw new TSQLException(e);
		}finally {
			if(ps != null ) {
				try {
					ps.close();
				} catch (SQLException e) {
					throw new TSQLException(e);
				}
			}
		}
		
	}
	public <T extends DTO> int[] toUpdateSqlDtoJDBCBatch(Collection<T> dtos,Connection connection) {
		PreparedStatement ps  = null;
		try {
			Class<?> clzz = dtos.iterator().next().getClass();
			Field[] fields =clzz.getDeclaredFields();
			String tableName=clzz.getDeclaredAnnotation(Table.class).name();
			if("".equals(tableName)) {
				tableName=clzz.getSimpleName();
			}
			
			StringBuffer set = new StringBuffer(" set ");
			for(Field field :fields) {
				Column column =field.getDeclaredAnnotation(Column.class);
				if(column != null) {
					String name = column.name();
					if(name.equals("")) {
						name=field.getName();
					}
					set.append(name);
					set.append("=:");
					set.append(name);
					set.append(",");
				}
			}
			StringBuffer where = getWhereById(fields);
			String _set = set.substring(0, set.length()-1);
			String sql = String.format("update %s %s %s", tableName,_set,where);
		
			List<String> l = StringUtils.findRegular(sql, ":[A-z_]+");
			String exSql = sql.toString().replaceAll(":[a-zA-Z_]+", "?");
			ps =connection.prepareStatement(exSql);
			for(DTO dto :dtos) {
				for(int i=0;i<l.size();i++) {
					String name=l.get(i).replace(":", "");
					Field f= clzz.getDeclaredField(name);
					f.setAccessible(true);
					Object val =f.get(dto);
					ps.setObject(i+1, val);
				}
				ps.addBatch();
			}
			return ps.executeBatch();			
		} catch (Exception e) {
			throw new TSQLException(e);
		}finally {
			if(ps != null ) {
				try {
					ps.close();
				} catch (SQLException e) {
					throw new TSQLException(e);
				}
			}
		}
	}
	public <T extends DTO> int[] toDeleteSqlDtoJDBCBatch(Collection<T> dtos,Connection connection) {
		PreparedStatement ps  = null;
		try {
			Class<?> clzz = dtos.iterator().next().getClass();
			Field[] fields =clzz.getDeclaredFields();
			String tableName=clzz.getDeclaredAnnotation(Table.class).name();
			if("".equals(tableName)) {
				tableName=clzz.getSimpleName();
			}
			String idName= null;
			for(Field field :fields) {
				Id id=field.getDeclaredAnnotation(Id.class);
				Column column =field.getDeclaredAnnotation(Column.class);
				if(column != null ) {
					if(id!=null) {
						field.setAccessible(true);
					}
					String name = column.name();
					if(name.equals("")) {
						name=field.getName();
					}
					idName = name;
					break;
				}
			}
			String sql =String.format(" delete  from %s where %s=?", tableName,idName);
			ps =connection.prepareStatement(sql);
			for( DTO  dto : dtos) {
				Object idValue=null;
				for(Field field :fields) {
					Id id=field.getDeclaredAnnotation(Id.class);
					if(id!=null) {
						field.setAccessible(true);
						idValue = field.get(dto);
						break;
					}
				}
				ps.setObject(1, idValue);
				ps.addBatch();
			}
			return ps.executeBatch();
		} catch (Exception e) {
			throw new TSQLException(e);
		} finally {
			if(ps != null ) {
				try {
					ps.close();
				} catch (SQLException e) {
					throw new TSQLException(e);
				}
			}
		}
		
		
		
	}
	public <T extends DTO> int[] toCreateSqlDtoJDBCBatch(Collection<T> dtos,Connection connection) {
		PreparedStatement ps  = null;
		try {
			Class<?> clzz = dtos.iterator().next().getClass();
			Field[] fields =clzz.getDeclaredFields();
			String tableName=clzz.getDeclaredAnnotation(Table.class).name();
			if("".equals(tableName)) {
				tableName=clzz.getSimpleName();
			}
			StringBuffer columnName=new StringBuffer();
			StringBuffer columnValue=new StringBuffer();
			
			
			for(Field field :fields) {
				Column column =field.getDeclaredAnnotation(Column.class);
				if(column != null ) {
					String name = column.name();
					if(name.equals("")) {
						name=field.getName();
					}
					columnName.append(name);
					columnName.append(",");
					columnValue.append(":");
					columnValue.append(name);
					columnValue.append(",");
				}
			}
			String _columnName=columnName.substring(0, columnName.length()-1);
			String _columnValue=columnValue.substring(0, columnValue.length()-1);
			StringBuffer sql = new StringBuffer();
			sql.append("insert into ");
			sql.append(tableName);
			sql.append("(");
			sql.append(_columnName);
			sql.append(") values (");
			sql.append(_columnValue);
			sql.append(")");
			List<String> l = StringUtils.findRegular(sql, ":[A-z_]+");
			String exSql = sql.toString().replaceAll(":[a-zA-Z_]+", "?");
			ps=connection.prepareStatement(exSql);
			for(DTO dto :dtos) {
				for(Field field :fields) {
					Id id=field.getDeclaredAnnotation(Id.class);
					if(id!=null) {
						field.setAccessible(true);
						// 清除之前的如果@Id注解的字段为空，那么就生成一个UUID的字段，改为如果Id为空则警告用户
						if(field.get(dto) == null) {
							throw new TSQLException(String.format("[%s] @Id Field name can not be empty.",field.getName()));
						}
						break;
					}
				}
				for(int i=0;i<l.size();i++) {
					String name=l.get(i).replace(":", "");
					Field f= clzz.getDeclaredField(name);
					f.setAccessible(true);
					Object val =f.get(dto);
					ps.setObject(i+1, val);
				}
				ps.addBatch();
			}
			return ps.executeBatch();			
		} catch (Exception e) {
			throw new TSQLException(e);
		} finally {
			if(ps != null ) {
				try {
					ps.close();
				} catch (SQLException e) {
					throw new TSQLException(e);
				}
			}
		}
	}
	
	public int toCreateSqlDto(DTO dto,Connection connection) {
		PreparedStatement ps  = null;
		try {
			Class<?> clzz = dto.getClass();
			Field[] fields =clzz.getDeclaredFields();
			String tableName=clzz.getDeclaredAnnotation(Table.class).name();
			if("".equals(tableName)) {
				tableName=clzz.getSimpleName();
			}
			StringBuffer columnName=new StringBuffer();
			StringBuffer columnValue=new StringBuffer();
			for(Field field :fields) {
				Id id=field.getDeclaredAnnotation(Id.class);
				Column column =field.getDeclaredAnnotation(Column.class);
				if(column != null) {
					if(id!=null) {
						field.setAccessible(true);
						// 如果为空,才会设置Id字段的值
						if(field.get(dto) == null) {
							field.set(dto, StringUtils.getUUID());
						}
					}
					String name = column.name();
					if(name.equals("")) {
						name=field.getName();
					}
					columnName.append(name);
					columnName.append(",");
					columnValue.append(":");
					columnValue.append(name);
					columnValue.append(",");
				}
			}
			String _columnName=columnName.substring(0, columnName.length()-1);
			String _columnValue=columnValue.substring(0, columnValue.length()-1);
			StringBuffer sql = new StringBuffer();
			sql.append("insert into ");
			sql.append(tableName);
			sql.append("(");
			sql.append(_columnName);
			sql.append(") values (");
			sql.append(_columnValue);
			sql.append(")");
			List<String> l = StringUtils.findRegular(sql, ":[A-z_]+");
			String exSql = sql.toString().replaceAll(":[a-zA-Z_]+", "?");
			ps =connection.prepareStatement(exSql);
			for(int i=0;i<l.size();i++) {
				String name=l.get(i).replace(":", "");
				Field f= clzz.getDeclaredField(name);
				f.setAccessible(true);
				Object val =f.get(dto);
				ps.setObject(i+1, val);
			}
			return ps.executeUpdate();
		} catch (Exception e) {
			throw new TSQLException(e);
		}finally {
			if(ps != null ) {
				try {
					ps.close();
				} catch (SQLException e) {
					throw new TSQLException(e);
				}
			}
		}
		
	}
	private StringBuffer getWhereById(Field[] fields) {
		List<String> ids=new ArrayList<String>();
		for(Field field :fields) {
			Id id=field.getDeclaredAnnotation(Id.class);
			Column column =field.getDeclaredAnnotation(Column.class);
			if(id!=null && column!=null) {
				String name = column.name();
				if(name.equals("")) {
					name=field.getName();
				}
				ids.add(name);
			}
		}
		StringBuffer where =new StringBuffer();
		for(int i=0;i<ids.size();i++) {
			if(i==0) {
				where.append("where ");
				where.append(ids.get(i));
				where.append("=:");
				where .append(ids.get(i));
			}else {
				where.append("and ");
				where.append(ids.get(i));
				where.append("=:");
				where.append(ids.get(i));
			}
		}
		if(where.toString().equals("") ) {
			throw new TSQLException("[@Id] notes do not exist entities.");
		}
		
		return where;
	}
}
