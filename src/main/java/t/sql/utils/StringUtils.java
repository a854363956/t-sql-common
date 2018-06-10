package t.sql.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用来处理字符串的工具类
 * @author zhangj
 * @date   2018-05-19 23:17:53
 * @emial  zhangjin0908@hotmail.com
 */
public class StringUtils {
	/**
	 * 根据正则表达式查询指定的字符串
	 * @param txt        要查询的字符串
	 * @param regular    正则表达式
	 * @return
	 */
	static public List<String> findRegular(CharSequence txt,String regular){
		 Pattern p = Pattern.compile(regular);
		 Matcher m = p.matcher(txt);
		 List<String> result = new ArrayList<String>();
		 while(m.find()){
			 result.add(m.group());
		 }
		 return result;
	}
	/**
	 * 获取唯一ID
	 * @return
	 */
	static public String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
