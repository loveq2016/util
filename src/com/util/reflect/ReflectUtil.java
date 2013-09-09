package com.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 这是一个用于方法反射的工具类 这将运用JDK的反射机制
 * 
 * @author strive
 * 
 */
public class ReflectUtil {

	protected static final Log log = LogFactory.getLog(ReflectUtil.class);

	/**
	 * 反转一个有返回值的无参方法
	 * 
	 * @param object
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
	public static Object excuteMethod(Object object, String methodName)
			throws Exception {
		Class c = object.getClass();
		Method m = c.getMethod(methodName);
		return m.invoke(object);
	}

	/**
	 * 反转一个没有返回值的有一个参数的方法
	 * 
	 * @param object
	 * @param methodName
	 * @param parameter
	 * @throws Exception
	 */
	public static void excuteMethod(Object object, String methodName,
			Object parameter) throws Exception {
		Class c = object.getClass();
		Method m = c.getDeclaredMethod(methodName, parameter.getClass());
		m.invoke(object, parameter);
	}

	/**
	 * 执行一个参数为boolean类型的方法
	 * 
	 * @param object
	 * @param methodName
	 * @param parameter
	 * @throws Exception
	 */
	public static void excuteBoolMethod(Object object, String methodName,
			boolean parameter) throws Exception {
		Class c = object.getClass();
		Method m = c.getDeclaredMethod(methodName, boolean.class);
		m.invoke(object, parameter);
	}

	/**
	 * 获得一个属性的set方法名
	 * 
	 * @param property
	 * @return
	 */
	public static String returnSetMethodName(String property) {
		return "set" + Character.toUpperCase(property.charAt(0))
				+ property.substring(1);
	}

	/**
	 * 获得一个属性的get方法名
	 * 
	 * @param property
	 * @return
	 */
	public static String returnGetMethodName(String property) {
		return "get" + Character.toUpperCase(property.charAt(0))
				+ property.substring(1);
	}

	private static Object operate(Object obj, String fieldName,
			Object fieldVal, String type) {
		Object ret = null;
		try {
			Class classType = obj.getClass();

			Field[] fields = classType.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if (!field.getName().equals(fieldName)) {
					continue;
				}
				String firstLetter = fieldName.substring(0, 1).toUpperCase();
				if ("set".equals(type)) {
					String setMethodName = "set" + firstLetter
							+ fieldName.substring(1);
					Method setMethod = classType.getMethod(setMethodName,
							new Class[] { field.getType() });
					ret = setMethod.invoke(obj, new Object[] { fieldVal });
				}
				if ("get".equals(type)) {
					String getMethodName = "get" + firstLetter
							+ fieldName.substring(1);
					Method getMethod = classType.getMethod(getMethodName,
							new Class[0]);
					ret = getMethod.invoke(obj, new Object[0]);
				}
				return ret;
			}

		} catch (Exception e) {
			log.error("reflect error:" + fieldName, e);
		}
		return ret;
	}

	public static Object getVal(Object obj, String fieldName) {
		return operate(obj, fieldName, null, "get");
	}

	public static void setVal(Object obj, String fieldName, Object fieldVal) {
		operate(obj, fieldName, fieldVal, "set");
	}

	private static Method getDeclaredMethod(Object object, String methodName,
			Class<?>[] parameterTypes) {
		for (Class superClass = object.getClass(); superClass != Object.class;) {
			try {
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException localNoSuchMethodException) {
				superClass = superClass.getSuperclass();
			}

		}

		return null;
	}

	private static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers())) {
			field.setAccessible(true);
		}
	}

	private static Field getDeclaredField(Object object, String filedName) {
		for (Class superClass = object.getClass(); superClass != Object.class;) {
			try {
				return superClass.getDeclaredField(filedName);
			} catch (NoSuchFieldException localNoSuchFieldException) {
				superClass = superClass.getSuperclass();
			}

		}

		return null;
	}

	public static Object invokeMethod(Object object, String methodName,
			Class<?>[] parameterTypes, Object[] parameters)
			throws InvocationTargetException {
		Method method = getDeclaredMethod(object, methodName, parameterTypes);

		if (method == null) {
			throw new IllegalArgumentException("Could not find method ["
					+ methodName + "] on target [" + object + "]");
		}

		method.setAccessible(true);
		try {
			return method.invoke(object, parameters);
		} catch (IllegalAccessException localIllegalAccessException) {
		}

		return null;
	}

	public static void setFieldValue(Object object, String fieldName,
			Object value) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + object + "]");
		}
		makeAccessible(field);
		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static Object getFieldValue(Object object, String fieldName) {
		Field field = getDeclaredField(object, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + object + "]");
		}
		makeAccessible(field);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return result;
	}
}