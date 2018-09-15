package t.sql.utils;

import java.lang.reflect.Field;
import java.util.List;

import t.sql.exception.TVerificationException;
import t.sql.interfaces.DTO;
import t.sql.validates.Number;
import t.sql.validates.Text;
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
				field.setAccessible(true);
				Object value =field.get(dto);
				Text v = field.getAnnotation(Text.class);
				Number nv = field.getAnnotation(Number.class);
				if(v != null ) {
					// 检查字符串是否符合校验
					checkStringVerification(v,value);
				}
				if(nv != null) {
					// 检查数值类型是否符合校验
					checkNumberVerification(nv,value);
				}
				
			}
		} catch (Exception e) {
			throw new TVerificationException(e);
		}
	 }
	 
	 private static void checkNumberVerification(Number nv ,Object value) {
		 if(nv != null) {
			 if(nv.notNull()) {
				 if(value == null || "".equals(value)) {
						throw new TVerificationException(String.format("Data value is [%s]-%s",value,"value can not be empty"));
				  }
			 }else if(value instanceof Integer ) {
				 Integer val = (Integer) value;
				 if(val< nv.min()) {
					throw new TVerificationException(String.format("Data value is [%s]-%s",value,nv.message()));
				 }else if(val > nv.max()){
					throw new TVerificationException(String.format("Data value is [%s]-%s",value,nv.message()));
				 }
			 }else if(value instanceof Long) {
				 Long val = (Long) value;
				 if(val< nv.min()) {
					throw new TVerificationException(String.format("Data value is [%s]-%s",value,nv.message()));
				 }else if(val > nv.max()){
					throw new TVerificationException(String.format("Data value is [%s]-%s",value,nv.message()));
				 }
			 }
		 }
	 }
	 
	 
	 private static void checkStringVerification(Text sv,Object value) {
		 if(sv != null) {
				if(sv.notNull()) {
					if(value == null || "".equals(value)) {
						throw new TVerificationException(String.format("Data value is [%s]-%s",value,"value can not be empty"));
					}
				}else if(!"".equals(sv.blacklistRegular()) && value instanceof CharSequence) {
					List<String> l = StringUtils.findRegular((CharSequence)value, sv.blacklistRegular());
					if(l.size() > 0) {
						throw new TVerificationException(String.format("Data value is [%s]-%s",value,sv.message()));
					}
				}else if(!"".equals(sv.blacklistRegular()) && value instanceof CharSequence) {
					List<String> l = StringUtils.findRegular((CharSequence)value, sv.blacklistRegular());
					if(l.size() == 0) {
						throw new TVerificationException(String.format("Data value is [%s]-%s",value,sv.message()));
					}
				}
			}
	 }
}
