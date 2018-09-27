package test.t.sql;

import java.util.List;
import java.util.Map;

import t.sql.Session;
import t.sql.dynamic.proxy.annotation.SqlDML;

/***
 * 
 * @author zhangj
 * @date 2018年9月27日 上午11:25:11
 * @email zhangjin0908@hotmail.com
 */
public interface TestInterface {
	
	@SqlDML(sql="select * from t_base_user")
	 List<Map<String,Object>> test();
	
	 Session getCurrentSession();
	 
}

