package com.base.service;

import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;

import com.util.array.ArrayUtil;
import com.util.date.DateUtil;
import com.util.exception.DataErrorsException;
import com.util.spring.SpringUtil;
import com.util.string.StringUtil;
import com.util.mybatis.RowBounds;
import com.util.pager.Pager;
/**
 * 映射类，把Controller层跟Service通过反射调用,简单的【增删查改】不需要自己写Controller
 * @author Administrator
 *
 */
public final class ServiceMapping {

	private static final Log log = LogFactory.getLog(ServiceMapping.class);
	
	/**
	 * 插入数据
	 * @param map 浏览器请求的数据
	 * @param modelPackage 对应model类的包名
	 * @return 
	 */
	public static Object insertMapping(Map<String, String> map,
			String modelPackage) {
		return execute(map, modelPackage, "insert", Object.class);
	}

	/**
	 * 根据ID删除
	 * @param map 浏览器请求的数据
	 * @param modelPackage 对应model类的包名
	 * @return 
	 */
	public static Object deleteByIdMapping(Map<String, String> map,
			String modelPackage) {
		return execute(map, modelPackage, "deleteById", Integer.class);
	}
	
	/**
	 * 根据model帮助类删除
	 * @param map 浏览器请求的数据
	 * @param modelPackage 对应model类的包名
	 * @return 
	 */
	public static Object deleteByExampleMapping(Map<String, String> map,
			String modelPackage) {
		return execute(map, modelPackage, "deleteByExample", Object.class);
	}
	
	/**
	 * 根据id删除
	 * @param map 浏览器请求的数据
	 * @param modelPackage 对应model类的包名
	 * @return 
	 */
	public static Object updateByIdMapping(Map<String, String> map,
			String modelPackage) {
		return execute(map, modelPackage, "updateById", Object.class);
	}

	/**
	 * 根据model帮助类更新
	 * @param map 浏览器请求的数据
	 * @param modelPackage 对应model类的包名
	 * @return 
	 */
	public static Object updateByExampleMapping(Map<String, String> map,
			String modelPackage) {
		return execute(map, modelPackage, "updateByExample", Object.class);
	}
	
	/**
	 * 根据id查询数据
	 * @param map 浏览器请求的数据
	 * @param modelPackage 对应model类的包名
	 * @return 
	 */
	public static Object selectByIdMapping(Map<String, String> map,
			String modelPackage) {
		return execute(map, modelPackage, "selectById", Integer.class);
	}
	
	/**
	 * 根据model查询数据
	 * @param map 浏览器请求的数据
	 * @param modelPackage 对应model类的包名
	 * @return 
	 */
	public static Object selectByModelMapping(Map<String, String> map,
			String modelPackage) {
		return execute(map, modelPackage, "selectByModel", Object.class);
	}
	
	/**
	 * 根据model帮助类查询数据
	 * @param map 浏览器请求的数据
	 * @param modelPackage 对应model类的包名
	 * @return 
	 */
	public static Object selectByExampleMapping(Map<String, String> map,
			String modelPackage) {
		return execute(map, modelPackage, "selectByExample", Object.class);
	}
	
	/**
	 * 根据model帮助类查询数据，支持分页
	 * @param map 浏览器请求的数据
	 * @param modelPackage 对应model类的包名
	 * @return 
	 */
	public static Object selectByExampleMapping(Map<String, String> map,
			String modelPackage,Integer offset, Integer pageSize) {
		return execute(map, modelPackage, "selectByExample", Object.class, offset, pageSize);
	}
	
	/**
	 * 根据model帮助类得到总条数
	 * @param map 浏览器请求的数据
	 * @param modelPackage 对应model类的包名
	 * @return 
	 */
	public static Object countByExampleMapping(Map<String, String> map,
			String modelPackage) {
		return execute(map, modelPackage, "countByExample", Object.class);
	}
	
	/**
	 * 通过反射执行操作
	 * @param map 浏览器请求的数据
	 * @param modelPackage 对应model类的包名
	 * @param serviceMethodName 对应service层的方法名
	 * @param serviceParameterClass 方法名参数类
	 * @return
	 */
	private static Object execute(Map<String, String> map, String modelPackage,
			String serviceMethodName, Class<?> serviceParameterClass) {
		return execute(map, modelPackage, serviceMethodName, serviceParameterClass, null, null);
	}

	/**
	 * 通过反射执行操作，支持分页
	 * @param map 浏览器请求的数据
	 * @param modelPackage 对应model类的包名
	 * @param serviceMethodName 对应service层的方法名
	 * @param serviceParameterClass 方法名参数类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Object execute(Map<String, String> map, String modelPackage,
			String serviceMethodName, Class<?> serviceParameterClass, Integer offset, Integer pageSize) {
		try {

			String modelName = StringUtil.firstLetterToLowerCase(modelPackage
					.substring(modelPackage.lastIndexOf(".") + 1,
							modelPackage.length()));
			Object service = SpringUtil.getBean(modelName + "Service");
			Class<?> serciceClass = service.getClass().getSuperclass();
			
			Method serviceMethod = null;

			if ("deleteById".equals(serviceMethodName) || "selectById".equals(serviceMethodName)) {
				serviceMethod = serciceClass.getDeclaredMethod(serviceMethodName, serviceParameterClass);
				if (map != null && !map.isEmpty()) {
					for (Entry<String, String> entry : map.entrySet()) {
						return serviceMethod.invoke(service,
								Integer.valueOf(entry.getValue()));
					}
				}
			} else if ("selectByExample".equals(serviceMethodName) || "deleteByExample".equals(serviceMethodName) || "countByExample".equals(serviceMethodName)) {
				Class<?> clas = Class.forName(modelPackage);
				Object object = clas.newInstance();
				
				Class<?> clas2 = Class.forName(modelPackage+"Example");
				Object object2 = clas2.newInstance();
				
				if (!"deleteByExample".equals(serviceMethodName)) {
					String isCache = map.get("isCache");
					if ("true".equalsIgnoreCase(isCache)) {
						Method isCacheMethod = clas2.getSuperclass().getDeclaredMethod("setCache", boolean.class);
						isCacheMethod.invoke(object2, true);
					}
					
					String ob = map.get("ob");
					if (StringUtil.isNotEmpty(ob)) {
						Method isCacheMethod = clas2.getSuperclass().getDeclaredMethod("setOrderByClause", String.class);
						isCacheMethod.invoke(object2, ob);
					}
					
					String gb = map.get("gb");
					if (StringUtil.isNotEmpty(gb)) {
						Method setGroupBy = clas2.getSuperclass().getDeclaredMethod("setGroupBy", String.class);
						setGroupBy.invoke(object2, gb);
					}
				}
				Method method = clas2.getDeclaredMethod("createCriteria");
				Object criteria = method.invoke(object2);
				
				if (map != null && !map.isEmpty()) {
					String where = map.get("where");
					log.debug("  where条件是"+where);
					if (StringUtil.isNotEmpty(where)) {
						ObjectMapper om = new ObjectMapper();
						Map<String, String> whereMap = om.readValue(where, Map.class);
						for (Entry<String, String> entry : whereMap.entrySet()) {
							setExampleData(object, entry, criteria);
						}
					}
				}
				if (offset == null || pageSize == null) {
					serviceMethod = serciceClass.getDeclaredMethod(serviceMethodName, serviceParameterClass);
					return serviceMethod.invoke(service, object2);
				} else {
					serviceMethod = serciceClass.getDeclaredMethod(serviceMethodName, serviceParameterClass,org.apache.ibatis.session.RowBounds.class);
					Method countMethod = serciceClass.getDeclaredMethod("countByExample", serviceParameterClass);
					
					Map<String, Object> returnData = new HashMap<String, Object>();
					returnData.put("pager", new Pager(Integer.valueOf(String.valueOf(countMethod.invoke(service, object2))), offset, pageSize));
					returnData.put("items", serviceMethod.invoke(service, object2, new RowBounds(offset, pageSize)));
					return returnData;
				}
				
			} else if ("updateByExample".equals(serviceMethodName)) {
				Class<?> clas = Class.forName(modelPackage);
				Object object = clas.newInstance();
				
				Class<?> clas2 = Class.forName(modelPackage+"Example");
				Object object2 = clas2.newInstance();
				
				Method method = clas2.getDeclaredMethod("createCriteria");
				Object criteria = method.invoke(object2);
				
				if (map != null && !map.isEmpty()) {
					for (Entry<String, String> entry : map.entrySet()) {
						setBeanData(clas, object, entry);
					}
					String where = map.get("where");
					if (StringUtil.isNotEmpty(where)) {
						log.warn(serviceMethodName+"where条件是"+where);
						ObjectMapper om = new ObjectMapper();
						Map<String, String> whereMap = om.readValue(where, Map.class);
						for (Entry<String, String> entry : whereMap.entrySet()) {
							setExampleData(object, entry, criteria);
						}
					}
					serviceMethod = serciceClass.getDeclaredMethod(serviceMethodName, serviceParameterClass,Object.class);
					return serviceMethod.invoke(service, object, object2);
				}
			} else  {
				Class<?> clas = Class.forName(modelPackage);
				Object object = clas.newInstance();
				if (map != null && !map.isEmpty()) {
					for (Entry<String, String> entry : map.entrySet()) {
						setBeanData(clas, object, entry);
					}
				}
				serviceMethod = serciceClass.getDeclaredMethod(serviceMethodName, serviceParameterClass);
				return serviceMethod.invoke(service, object);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataErrorsException(e);
		}
		return null;
	}


	/**
	 * 把数据设置到model对应的帮助类中   支持 like, eq 等查询
	 * @param object
	 * @param entry
	 * @param method
	 * @param criteria
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void setExampleData(Object object,
			Entry<String, String> entry,Object criteria) throws Exception {
		String type = object.getClass().getDeclaredField(entry.getKey()).getType().toString();	
		Method method = null;
		
		String value = entry.getValue();
		String[] array = null;
		if (StringUtil.isNotEmpty(value)) {
			if (value.lastIndexOf(",") != -1) {
				array = new String[2];
				array[0] = value.substring(0, value.lastIndexOf(","));
				array[1] = value.substring(value.lastIndexOf(",")+1, value.length());
			} else {
				array = new String[1];
				array[0] = value;
			}
		}
		if (ArrayUtil.isEmpty(array) || StringUtil.isEmpty(array[0])) {
			log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+"为空, 查询条件不合格，无法添加到查询条件中");
			return ;
		}
		if (type.equals("class java.lang.String")) {
			if (array.length == 1) {
				method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"EqualTo", String.class);
				method.invoke(criteria,array[0]);
			} else if (array.length == 2) {
				if ("noteq".equalsIgnoreCase(array[1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotEqualTo", String.class);
					method.invoke(criteria,array[0]);
				} else if ("like".equalsIgnoreCase(array[1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"Like", String.class);
					method.invoke(criteria,"%"+array[0]+"%");
				} else if ("notLike".equalsIgnoreCase(array[1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotLike", String.class);
					method.invoke(criteria,"%"+array[0]+"%");
				} else if ("in".equalsIgnoreCase(array[1])) {
					List list = null;
					array = array[0].split("&");
					if (ArrayUtil.isNotEmpty(array)) {
						list = new ArrayList();
						for (String string : array) {
							list.add(string);
						}
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"In", List.class);
						method.invoke(criteria,list);
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" In 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("notIn".equalsIgnoreCase(array[array.length-1])) {
					List list = null;
					array = array[0].split("&");
					if (ArrayUtil.isNotEmpty(array)) {
						list = new ArrayList();
						for (String string : array) {
							list.add(string);
						}
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotIn", List.class);
						method.invoke(criteria,list);
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" NotIn 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("bw".equalsIgnoreCase(array[array.length-1]) || "between".equalsIgnoreCase(array[array.length-1])) {
					array = array[0].split("&");
					if (array != null && array.length >= 2) {
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"Between", String.class, String.class);
						method.invoke(criteria,array[0],array[1]);		
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" Between 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("notbw".equalsIgnoreCase(array[array.length-1]) || "notBetween".equalsIgnoreCase(array[array.length-1])) {
					array = array[0].split("&");
					if (array != null && array.length >= 2) {
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotBetween", String.class, String.class);
						method.invoke(criteria,array[0],array[1]);		
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" NotBetween 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("gt".equalsIgnoreCase(array[array.length-1]) || "greaterThan".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"GreaterThan", String.class);
					method.invoke(criteria,array[0]);		
				} else if ("gteq".equalsIgnoreCase(array[array.length-1]) || "gtOrEq".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"GreaterThanOrEqualTo", String.class);
					method.invoke(criteria,array[0]);
				} else if ("lt".equalsIgnoreCase(array[array.length-1]) || "lessThan".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"LessThan", String.class);
					method.invoke(criteria,array[0]);
				} else if ("lteq".equalsIgnoreCase(array[array.length-1]) || "ltOrEq".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"LessThanOrEqualTo", String.class);
					method.invoke(criteria,array[0]);
				} else {
					log.warn("找不到执行的方法");
				}
			}
		} else if (type.equals("class java.lang.Integer")) {
			if (array.length == 1) {
				method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"EqualTo", Integer.class);
				method.invoke(criteria,Integer.valueOf(array[0]));
			} else if (array.length == 2) {
				if ("noteq".equalsIgnoreCase(array[1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotEqualTo", Integer.class);
					method.invoke(criteria,Integer.valueOf(array[0]));
				} else if ("in".equalsIgnoreCase(array[1])) {
					List list = null;
					array = array[0].split("&");
					if (ArrayUtil.isNotEmpty(array)) {
						list = new ArrayList();
						for (String string : array) {
							list.add(string);
						}
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"In", List.class);
						method.invoke(criteria,list);
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" In 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("notIn".equalsIgnoreCase(array[array.length-1])) {
					List list = null;
					array = array[0].split("&");
					if (ArrayUtil.isNotEmpty(array)) {
						list = new ArrayList();
						for (String string : array) {
							list.add(string);
						}
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotIn", List.class);
						method.invoke(criteria,list);
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" NotIn 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("bw".equalsIgnoreCase(array[array.length-1]) || "between".equalsIgnoreCase(array[array.length-1])) {
					array = array[0].split("&");
					if (array != null && array.length >= 2) {
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"Between", Integer.class, Integer.class);
						method.invoke(criteria,Integer.valueOf(array[0]),Integer.valueOf(array[1]));		
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" Between 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("notbw".equalsIgnoreCase(array[array.length-1]) || "notBetween".equalsIgnoreCase(array[array.length-1])) {
					array = array[0].split("&");
					if (array != null && array.length >= 2) {
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotBetween", Integer.class, Integer.class);
						method.invoke(criteria,Integer.valueOf(array[0]),Integer.valueOf(array[1]));		
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" NotBetween 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("gt".equalsIgnoreCase(array[array.length-1]) || "greaterThan".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"GreaterThan", Integer.class);
					method.invoke(criteria,Integer.valueOf(array[0]));		
				} else if ("gteq".equalsIgnoreCase(array[array.length-1]) || "gtOrEq".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"GreaterThanOrEqualTo", Integer.class);
					method.invoke(criteria,Integer.valueOf(array[0]));
				} else if ("lt".equalsIgnoreCase(array[array.length-1]) || "lessThan".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"LessThan", Integer.class);
					method.invoke(criteria,Integer.valueOf(array[0]));
				} else if ("lteq".equalsIgnoreCase(array[array.length-1]) || "ltOrEq".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"LessThanOrEqualTo", Integer.class);
					method.invoke(criteria,Integer.valueOf(array[0]));
				} else {
					log.warn("找不到执行的方法");
				}
			}
		} else if (type.equals("class java.util.Date")) {
			if (array.length == 1) {
				method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"EqualTo", Date.class);
				method.invoke(criteria,DateUtil.parseDateSecondFormat(array[0]));
			} else if (array.length == 2) {
				if ("noteq".equalsIgnoreCase(array[1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotEqualTo", Date.class);
					method.invoke(criteria,DateUtil.parseDateSecondFormat(array[0]));
				} else if ("in".equalsIgnoreCase(array[1])) {
					List list = null;
					array = array[0].split("&");
					if (ArrayUtil.isNotEmpty(array)) {
						list = new ArrayList();
						for (String string : array) {
							list.add(string);
						}
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"In", List.class);
						method.invoke(criteria,list);
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" In 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("notIn".equalsIgnoreCase(array[array.length-1])) {
					List list = null;
					array = array[0].split("&");
					if (ArrayUtil.isNotEmpty(array)) {
						list = new ArrayList();
						for (String string : array) {
							list.add(string);
						}
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotIn", List.class);
						method.invoke(criteria,list);
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" NotIn 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("bw".equalsIgnoreCase(array[array.length-1]) || "between".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"Between", Date.class, Date.class);
					array = array[0].split("&");
					if (array != null && array.length >= 2) {
						method.invoke(criteria,DateUtil.parseDateSecondFormat(array[0]),DateUtil.parseDateSecondFormat(array[1]));		
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" Between 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("notbw".equalsIgnoreCase(array[array.length-1]) || "notBetween".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotBetween", Date.class, Date.class);
					array = array[0].split("&");
					if (array != null && array.length >= 2) {
						method.invoke(criteria,DateUtil.parseDateSecondFormat(array[0]),DateUtil.parseDateSecondFormat(array[1]));		
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" NotBetween 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("gt".equalsIgnoreCase(array[array.length-1]) || "greaterThan".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"GreaterThan", Date.class);
					method.invoke(criteria,DateUtil.parseDateSecondFormat(array[0]));		
				} else if ("gteq".equalsIgnoreCase(array[array.length-1]) || "gtOrEq".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"GreaterThanOrEqualTo", Date.class);
					method.invoke(criteria,DateUtil.parseDateSecondFormat(array[0]));
				} else if ("lt".equalsIgnoreCase(array[array.length-1]) || "lessThan".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"LessThan", Date.class);
					method.invoke(criteria,DateUtil.parseDateSecondFormat(array[0]));
				} else if ("lteq".equalsIgnoreCase(array[array.length-1]) || "ltOrEq".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"LessThanOrEqualTo", Date.class);
					method.invoke(criteria,DateUtil.parseDateSecondFormat(array[0]));
				} else {
					log.warn("找不到执行的方法");
				}
			}
		} else if (type.equals("class java.lang.Short")) {
			if (array.length == 1) {
				method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"EqualTo", Short.class);
				method.invoke(criteria,Short.valueOf(array[0]));
			} else if (array.length == 2) {
				if ("noteq".equalsIgnoreCase(array[1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotEqualTo", Short.class);
					method.invoke(criteria,Short.valueOf(array[0]));
				} else if ("in".equalsIgnoreCase(array[1])) {
					List list = null;
					array = array[0].split("&");
					if (ArrayUtil.isNotEmpty(array)) {
						list = new ArrayList();
						for (String string : array) {
							list.add(string);
						}
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"In", List.class);
						method.invoke(criteria,list);
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" In 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("notIn".equalsIgnoreCase(array[array.length-1])) {
					List list = null;
					array = array[0].split("&");
					if (ArrayUtil.isNotEmpty(array)) {
						list = new ArrayList();
						for (String string : array) {
							list.add(string);
						}
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotIn", List.class);
						method.invoke(criteria,list);
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" NotIn 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("bw".equalsIgnoreCase(array[array.length-1]) || "between".equalsIgnoreCase(array[array.length-1])) {
					array = array[0].split("&");
					if (array != null && array.length >= 2) {
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"Between", Short.class, Short.class);
						method.invoke(criteria,Short.valueOf(array[0]),Short.valueOf(array[1]));		
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" Between 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("notbw".equalsIgnoreCase(array[array.length-1]) || "notBetween".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotBetween", Short.class, Short.class);
					if (StringUtil.isNotEmpty(array[0])) {
						array = array[0].split("&");
						if (array != null && array.length >= 2) {
							method.invoke(criteria,Short.valueOf(array[0]),Short.valueOf(array[1]));		
						} else {
							log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" NotBetween 查询条件不合格，无法添加到查询条件中");
						}
					}
				} else if ("gt".equalsIgnoreCase(array[array.length-1]) || "greaterThan".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"GreaterThan", Short.class);
					method.invoke(criteria,Short.valueOf(array[0]));		
				} else if ("gteq".equalsIgnoreCase(array[array.length-1]) || "gtOrEq".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"GreaterThanOrEqualTo", Short.class);
					method.invoke(criteria,Short.valueOf(array[0]));
				} else if ("lt".equalsIgnoreCase(array[array.length-1]) || "lessThan".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"LessThan", Short.class);
					method.invoke(criteria,Short.valueOf(array[0]));
				} else if ("lteq".equalsIgnoreCase(array[array.length-1]) || "ltOrEq".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"LessThanOrEqualTo", Short.class);
					method.invoke(criteria,Short.valueOf(array[0]));
				} else {
					log.warn("找不到执行的方法");
				}
			}
		} else if (type.equals("class java.lang.Double")) {
			if (array.length == 1) {
				method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"EqualTo", Double.class);
				method.invoke(criteria,Double.valueOf(array[0]));
			} else if (array.length == 2) {
				if ("noteq".equalsIgnoreCase(array[1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotEqualTo", Double.class);
					method.invoke(criteria,Double.valueOf(array[0]));
				} else if ("in".equalsIgnoreCase(array[1])) {
					List list = null;
					array = array[0].split("&");
					if (ArrayUtil.isNotEmpty(array)) {
						list = new ArrayList();
						for (String string : array) {
							list.add(string);
						}
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"In", List.class);
						method.invoke(criteria,list);
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" In 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("notIn".equalsIgnoreCase(array[array.length-1])) {
					List list = null;
					if (StringUtil.isNotEmpty(array[0])) {
						array = array[0].split("&");
						if (ArrayUtil.isNotEmpty(array)) {
							list = new ArrayList();
							for (String string : array) {
								list.add(string);
							}
							method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotIn", List.class);
							method.invoke(criteria,list);
						} else {
							log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" NotIn 查询条件不合格，无法添加到查询条件中");
						}
					}
				} else if ("bw".equalsIgnoreCase(array[array.length-1]) || "between".equalsIgnoreCase(array[array.length-1])) {
					array = array[0].split("&");
					if (array != null && array.length >= 2) {
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"Between", Double.class, Double.class);
						method.invoke(criteria,Double.valueOf(array[0]),Double.valueOf(array[1]));		
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" Between 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("notbw".equalsIgnoreCase(array[array.length-1]) || "notBetween".equalsIgnoreCase(array[array.length-1])) {
					array = array[0].split("&");
					if (array != null && array.length >= 2) {
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotBetween", Double.class, Double.class);
						method.invoke(criteria,Double.valueOf(array[0]),Double.valueOf(array[1]));		
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" NotBetween 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("gt".equalsIgnoreCase(array[array.length-1]) || "greaterThan".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"GreaterThan", Double.class);
					method.invoke(criteria,Double.valueOf(array[0]));		
				} else if ("gteq".equalsIgnoreCase(array[array.length-1]) || "gtOrEq".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"GreaterThanOrEqualTo", Double.class);
					method.invoke(criteria,Double.valueOf(array[0]));
				} else if ("lt".equalsIgnoreCase(array[array.length-1]) || "lessThan".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"LessThan", Double.class);
					method.invoke(criteria,Double.valueOf(array[0]));
				} else if ("lteq".equalsIgnoreCase(array[array.length-1]) || "ltOrEq".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"LessThanOrEqualTo", Double.class);
					method.invoke(criteria,Double.valueOf(array[0]));
				} else {
					log.warn("找不到执行的方法");
				}
			}
		}  else if (type.equals("class java.lang.Float")) {
			if (array.length == 1) {
				method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"EqualTo", Float.class);
				method.invoke(criteria,Float.valueOf(array[0]));
			} else if (array.length == 2) {
				if ("noteq".equalsIgnoreCase(array[1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotEqualTo", Float.class);
					method.invoke(criteria,Float.valueOf(array[0]));
				} else if ("in".equalsIgnoreCase(array[1])) {
					List list = null;
					array = array[0].split("&");
					if (ArrayUtil.isNotEmpty(array)) {
						list = new ArrayList();
						for (String string : array) {
							list.add(string);
						}
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"In", List.class);
						method.invoke(criteria,list);
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" In 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("notIn".equalsIgnoreCase(array[array.length-1])) {
					List list = null;
					array = array[0].split("&");
					if (ArrayUtil.isNotEmpty(array)) {
						list = new ArrayList();
						for (String string : array) {
							list.add(string);
						}
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotIn", List.class);
						method.invoke(criteria,list);
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" NotIn 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("bw".equalsIgnoreCase(array[array.length-1]) || "between".equalsIgnoreCase(array[array.length-1])) {
					array = array[0].split("&");
					if (array != null && array.length >= 2) {
						method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"Between", Float.class, Float.class);
						method.invoke(criteria,Float.valueOf(array[0]),Float.valueOf(array[1]));		
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" Between 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("notbw".equalsIgnoreCase(array[array.length-1]) || "notBetween".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"NotBetween", Float.class, Float.class);
					array = array[0].split("&");
					if (array != null && array.length >= 2) {
						method.invoke(criteria,Float.valueOf(array[0]),Float.valueOf(array[1]));		
					} else {
						log.warn(StringUtil.firstLetterToUpperCase(entry.getKey())+" NotBetween 查询条件不合格，无法添加到查询条件中");
					}
				} else if ("gt".equalsIgnoreCase(array[array.length-1]) || "greaterThan".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"GreaterThan", Float.class);
					method.invoke(criteria,Float.valueOf(array[0]));		
				} else if ("gteq".equalsIgnoreCase(array[array.length-1]) || "gtOrEq".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"GreaterThanOrEqualTo", Float.class);
					method.invoke(criteria,Float.valueOf(array[0]));
				} else if ("lt".equalsIgnoreCase(array[array.length-1]) || "lessThan".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"LessThan", Float.class);
					method.invoke(criteria,Float.valueOf(array[0]));
				} else if ("lteq".equalsIgnoreCase(array[array.length-1]) || "ltOrEq".equalsIgnoreCase(array[array.length-1])) {
					method = criteria.getClass().getDeclaredMethod("and"+StringUtil.firstLetterToUpperCase(entry.getKey())+"LessThanOrEqualTo", Float.class);
					method.invoke(criteria,Float.valueOf(array[0]));
				} else {
					log.warn("找不到执行的方法");
				}
			}
		}
	}
	
	/**
	 * 把浏览器请求的数据设置到javabean中
	 * @param clas
	 * @param object
	 * @param entry
	 * @throws Exception
	 */
	private static void setBeanData(Class<?> clas, Object object,
			Entry<String, String> entry) throws Exception {
		if ("where".equals(entry.getKey())) {
			return;
		}
		String type = object.getClass().getDeclaredField(entry.getKey()).getType().toString();
		if (type.equals("class java.lang.String")) {
			Method method = clas.getDeclaredMethod("set" + StringUtil.firstLetterToUpperCase(entry.getKey()),String.class);
			method.invoke(object, entry.getValue());
			return ;
		} else {
			if (StringUtil.isEmpty(entry.getValue())) {
				log.warn(entry.getKey() + " 值为空，无法把值设置到javabean中");
				return ;
			}
		}
		if (type.equals("class java.lang.String")) {
			Method method = clas.getDeclaredMethod("set" + StringUtil.firstLetterToUpperCase(entry.getKey()),String.class);
			method.invoke(object, entry.getValue());
		} else if (type.equals("class java.lang.Integer")) {
			Method method = clas.getDeclaredMethod("set" + StringUtil.firstLetterToUpperCase(entry.getKey()),Integer.class);
			method.invoke(object, Integer.valueOf(entry.getValue()));
		} else if (type.equals("class java.util.Date")) {
			Method method = clas.getDeclaredMethod("set" + StringUtil.firstLetterToUpperCase(entry.getKey()),Date.class);
			method.invoke(object,DateUtil.parseDateSecondFormat(entry.getValue()));
		} else if (type.equals("class java.lang.Short")) {
			Method method = clas.getDeclaredMethod("set" + StringUtil.firstLetterToUpperCase(entry.getKey()),Short.class);
			method.invoke(object, Short.valueOf(entry.getValue()));
		} else if (type.equals("class java.lang.Double")) {
			Method method = clas.getDeclaredMethod("set" + StringUtil.firstLetterToUpperCase(entry.getKey()),Double.class);
			method.invoke(object, Double.valueOf(entry.getValue()));
		} else if (type.equals("class java.lang.Boolean")) {
			Method method = clas.getDeclaredMethod("set" + StringUtil.firstLetterToUpperCase(entry.getKey()),Boolean.class);
			method.invoke(object, Boolean.valueOf(entry.getValue()));
		} else if (type.equals("class java.lang.Byte")) {
			Method method = clas.getDeclaredMethod("set" + StringUtil.firstLetterToUpperCase(entry.getKey()),Byte.class);
			method.invoke(object, Byte.valueOf(entry.getValue()));
		} else if (type.equals("class java.lang.Float")) {
			Method method = clas.getDeclaredMethod("set" + StringUtil.firstLetterToUpperCase(entry.getKey()),Float.class);
			method.invoke(object, Float.valueOf(entry.getValue()));
		}
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator =  factory.getValidator();
		Set<ConstraintViolation<Object>> set = validator.validate(object);
		StringBuilder sb =  null;
		if (set != null && !set.isEmpty()) {
			sb = new StringBuilder();
			for (ConstraintViolation<Object> constraintViolation : set) {
				sb.append("属性"+constraintViolation.getPropertyPath()+"数据错误,"+constraintViolation.getMessage()+"\n");
			}
			throw new DataErrorsException(sb.toString());
		}
		
	}

}
