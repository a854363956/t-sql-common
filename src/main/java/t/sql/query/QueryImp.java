package t.sql.query;

import java.lang.reflect.Field;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import t.sql.exception.TSQLException;
import t.sql.interfaces.DTO;
import t.sql.utils.StringUtils;

enum ParamesType {
	TIME, TIMESTAMP, LONG, TEXT, SHORT, INTEGER, FLOAT, OBJECT, LIST
}

class Parames {
	private String name;
	private Object value;
	private ParamesType type;

	public Parames(String name, Object value, ParamesType type) {
		super();
		this.name = name;
		this.value = value;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public ParamesType getType() {
		return type;
	}

	public void setType(ParamesType type) {
		this.type = type;
	}

}

@SuppressWarnings({ "unchecked" })
public class QueryImp<T> implements Query<T> {
	// 当前执行的sql
	private String sql;
	// 默认JDBC超时时间为10秒
	private int timeOut = 10;
	// 要替换的参数
	private List<Parames> parames = new ArrayList<Parames>();
	private Connection connection;
	private Class<?> clzz;
	public QueryImp(String sql, Connection connection,Class<?> clzz) {
		this.sql = sql;
		this.connection = connection;
		this.clzz =clzz;
	}
	@Override
	public List<T> list() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(getPrepareStatementSql());
			setPreparedStatementParames(ps);
			ps.setQueryTimeout(timeOut);
			rs = ps.executeQuery();
			ResultSetMetaData rsm = rs.getMetaData();
			int count = rsm.getColumnCount();
			List<T> result = new ArrayList<T>();
			while(rs.next()) {
				String[] labels = new String[count + 1];
				Object[] values = new Object[count + 1];
				for (int i = 0; i < count; i++) {
					String label = rsm.getColumnLabel(i+1);
					Object value = rs.getObject(i+1);
					labels[i] = label;
					values[i] = value;
				}
				T t= convert(labels, values);
				result.add(t);
			}
			return result;
		} catch (Exception e) {
			throw new TSQLException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e2) {
				throw new TSQLException(e2);
			}

		}
	}

	@Override
	public void setParameter(String name, Object val) throws TSQLException {
		Parames p = new Parames(name, val, ParamesType.OBJECT);
		parames.add(p);
	}

	@Override
	public void setTimeOut(int time) {
		this.timeOut = time;
	}

	@Override
	public void setTime(String name, Date time) {
		Parames p = new Parames(name, time, ParamesType.TIME);
		parames.add(p);
	}

	@Override
	public void setTimestamp(String name, Date time) {
		Parames p = new Parames(name, time, ParamesType.TIMESTAMP);
		parames.add(p);
	}

	@Override
	public void setText(String name, String val) {
		Parames p = new Parames(name, val, ParamesType.TEXT);
		parames.add(p);
	}

	@Override
	public void setShort(String name, short val) {
		Parames p = new Parames(name, val, ParamesType.SHORT);
		parames.add(p);
	}

	@Override
	public void setLong(String name, long val) {
		Parames p = new Parames(name, val, ParamesType.LONG);
		parames.add(p);
	}

	@Override
	public void setFloat(String name, float val) {
		Parames p = new Parames(name, val, ParamesType.FLOAT);
		parames.add(p);
	}

	@Override
	public void setInteger(String name, int val) {
		Parames p = new Parames(name, val, ParamesType.INTEGER);
		parames.add(p);
	}

	private String prepareStatementSql = null;

	// 获取当前PrepareStatement执行的字符串
	private String getPrepareStatementSql() {
		String sql = getSql();
		if (prepareStatementSql == null) {
			List<String> re = StringUtils.findRegular(sql, ":[a-zA-Z_]+");
			for (String f : re) {
				boolean isList = false;
				Object  val = null;
				for(Parames p:parames) {
					if((":"+p.getName()).equals(f)) {
						isList = true;
						val = p.getValue();
						break;
					}
				}
				if(isList == true) {
					StringBuffer slist = new StringBuffer("(");
					for(Object o : ((Collection<Object>)val)) {
						slist.append("?,");
					}
					String _qList = slist.substring(0, slist.length()-1)+")";
					sql =  sql.replace(f, _qList);
				}else {
					sql = sql.replace(f, "?");
				}
				
			}
		}
		return sql;
	}

	private String getSql() {
		return sql;
	}

	@Override
	public T uniqueResult() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(getPrepareStatementSql());
			setPreparedStatementParames(ps);
			ps.setQueryTimeout(timeOut);
			rs = ps.executeQuery();
			ResultSetMetaData rsm = rs.getMetaData();
			int count = rsm.getColumnCount();
			if (!rs.next()) {
				return null;
			}
			String[] labels = new String[count + 1];
			Object[] values = new Object[count + 1];
			for (int i = 0; i < count; i++) {
				String label = rsm.getColumnLabel(i);
				Object value = rs.getObject(i);
				labels[i] = label;
				values[i] = value;
			}
			return convert(labels, values);
		} catch (Exception e) {
			throw new TSQLException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e2) {
				throw new TSQLException(e2);
			}

		}
	}

	public Class<?> getTClass() {
        return this.clzz;
	}
	@SuppressWarnings("rawtypes")
	private T convert(String[] labels, Object[] values) throws InstantiationException, IllegalAccessException {
		Class<?> tClass  = getTClass();
		Object ob = tClass.newInstance();
		if(ob instanceof Map) {
			Map m =(Map)ob;
			for(int i=0;i<labels.length;i++) {
				// 修复在查询出List<Map<String,Object>> 类型的时候map中含有null类型
				if(labels[i] != null ) {
					m.put(labels[i], values[i]);
				}else {
					continue;
				}
			}
			return (T)m;
		}else if(ob instanceof DTO) {
			Field[] fields =tClass.getDeclaredFields();
			for(int i=0;i<labels.length;i++) {
				for(Field f :fields) {
					Column column =f.getDeclaredAnnotation(Column.class);
					String name ="";
					if("".equals(column.name())) {
						name =  f.getName();
					}else {
						name=column.name();
					}
					
					if(name.equals(labels[i])) {
						f.setAccessible(true);
						f.set(ob, values[i]);
					}
				}
			}
			return (T) ob;
		}else {
			throw new TSQLException(String.format("Cannot be converted to %s", ob.getClass().getName()));
		}
		
	}

	private void setPreparedStatementParames(PreparedStatement ps) throws SQLException {
		checkParameters();
		List<String> l = StringUtils.findRegular(getSql(), ":[a-zA-Z_]+");
		int index = 0;
		for (int i = 0; i < l.size(); i++) {
			String name = l.get(i).replace(":", "");
			for (Parames p : parames) {
				if (p.getName().equals(name)) {
					if (p.getType() == ParamesType.OBJECT) {
						ps.setObject(index + 1, p.getValue());
						index++;
					} else if (p.getType() == ParamesType.FLOAT) {
						if (p.getValue() instanceof Float) {
							ps.setFloat(index + 1, (Float) p.getValue());
							index++;
						} else {
							throw new TSQLException(
									String.format("Incorrect data type! Parameter fields[%s]", name));
						}
					} else if (p.getType() == ParamesType.INTEGER) {
						if (p.getValue() instanceof Integer) {
							ps.setInt(index + 1, (Integer) p.getValue());
							index++;
						} else {
							throw new TSQLException(
									String.format("Incorrect data type! Parameter fields[%s]", name));
						}
					} else if (p.getType() == ParamesType.SHORT) {
						if (p.getValue() instanceof Short) {
							ps.setShort(index + 1, (Short) p.getValue());
							index++;
						} else {
							throw new TSQLException(
									String.format("Incorrect data type! Parameter fields[%s]", name));
						}
					} else if (p.getType() == ParamesType.TEXT) {
						if (p.getValue() instanceof String) {
							ps.setString(index + 1, (String) p.getValue());
							index++;
						} else {
							throw new TSQLException(
									String.format("Incorrect data type! Parameter fields[%s]", name));
						}
					} else if (p.getType() == ParamesType.LONG) {
						if (p.getValue() instanceof Long) {
							ps.setLong(index + 1, (Long) p.getValue());
							index++;
						} else {
							throw new TSQLException(
									String.format("Incorrect data type! Parameter fields[%s]", name));
						}
					} else if (p.getType() == ParamesType.TIMESTAMP) {
						if (p.getValue() instanceof Date) {
							java.sql.Timestamp timestamp = new Timestamp(((Date) p.getValue()).getTime());
							ps.setTimestamp(index + 1, timestamp);
							index++;
						} else {
							throw new TSQLException(
									String.format("Incorrect data type! Parameter fields[%s]", name));
						}
					} else if (p.getType() == ParamesType.TIME) {
						if (p.getValue() instanceof Date) {
							java.sql.Time time = new Time(((Date) p.getValue()).getTime());
							ps.setTime(index + 1, time);
							index++;
						} else {
							throw new TSQLException(
									String.format("Incorrect data type! Parameter fields[%s]", name));
						}
					}else if(p.getType() == ParamesType.LIST) {
						if(p.getValue() == null ) {
							throw new TSQLException(
									String.format(" Parameter[%s] value cannot be empty ", name));
						}
						Object[] parameter= ((Collection<Object>)p.getValue()).toArray();
						for(Object o :parameter) {
							ps.setObject(index+1,o);
							index++;
						}
					    
					}
				}
			}
		}
	}

	private void checkParameters() {
		for (Parames ps : parames) {
			List<String> l = StringUtils.findRegular(getSql(), String.format(":%s", ps.getName()));
			if (l.size() == 0) {
				throw new TSQLException(String.format("Invalid parameters [:%s]", ps.getName()));
			}
		}
	}
	@Override
	public <E>  void setListParameter(String name, Collection<E> vals) throws TSQLException {
		Parames p = new Parames(name, vals, ParamesType.LIST);
		parames.add(p);
	}

}
