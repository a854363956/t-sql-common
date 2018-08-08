package t.sql.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 用来对List进行操作的通用工具类 
 * @author zhangj
 * @date 2018年8月8日 下午10:35:35
 * @email zhangjin0908@hotmail.com
 */
public class ListUtils {
	
	/**
	 * 将当前List<Map<String,Object>> 中Map的key转换成纯小写  
	 * @param obj  要转换的数据类型
	 * @return     返回修改后的数据
	 */
	public static List<Map<String,Object>> toLowerCaseMapKey(List<Map<String,Object>> obj){
		List<Map<String,Object>> result = new java.util.ArrayList<Map<String,Object>>();
		obj.forEach((m) ->{
			 Map<String,Object> map = new HashMap<String,Object>();
			 m.forEach((k,v)->{
				 if(k != null) {
					 map.put(k.toLowerCase(),v);
				 }
			 });
			 result.add(map);
		});
		return result;
	}
}
 
