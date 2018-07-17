package t.sql.utils;

import java.lang.reflect.Field;
import java.util.List;

import t.sql.exception.TVerificationException;
import t.sql.interfaces.DTO;
import t.sql.validates.Verification;
/**
 * 校验实体的工具类
 * @author zhangj
 * @date   2018年6月14日 上午12:50:53 
 * @email  zhangjin0908@hotmail.com
 */
public class VerificationUtils {
	 /**
	  * 校验实体,如果实体异常将会抛出异常,以及注解的信息
	  * @param dto
	  */
	 public static void check(DTO dto) {
		try {
			Field[] fields =  dto.getClass().getDeclaredFields();
			for(Field field : fields) {
				Verification v = field.getAnnotation(Verification.class);
				if(v != null) {
					field.setAccessible(true);
					Object value =field.get(dto);
					if(v.notNull()) {
						if(value == null || "".equals(value)) {
							throw new TVerificationException(String.format("Data value is [%s]-%s",value,v.message()));
						}
					}else if(!"".equals(v.blacklistRegular()) && value instanceof CharSequence) {
						List<String> l = StringUtils.findRegular((CharSequence)value, v.blacklistRegular());
						if(l.size() > 0) {
							throw new TVerificationException(String.format("Data value is [%s]-%s",value,v.message()));
						}
					}else if(!"".equals(v.blacklistRegular()) && value instanceof CharSequence) {
						List<String> l = StringUtils.findRegular((CharSequence)value, v.blacklistRegular());
						if(l.size() == 0) {
							throw new TVerificationException(String.format("Data value is [%s]-%s",value,v.message()));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new TVerificationException(e);
		}
	 }
}
