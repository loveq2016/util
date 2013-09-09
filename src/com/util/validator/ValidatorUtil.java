package com.util.validator;

import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;


public class ValidatorUtil {

	
	/**
	 * 验证 javabean中所有属性值是否正确
	 * @param t 要验证的javabean对象
	 * @return true表示验证通过，false表示验证有错误
	 */
	public static <T> boolean validateAll(T t) {
		return validateAll(t, null);
	}
	
	/**
	 * 验证 javabean中所有属性值是否正确，不正确把错误信息返回到errorMap中
	 * @param t  要验证的javabean对象
	 * @param errorMap 如果有错误，把错误信息以key-value的显示放在此map中
	 * @return true表示验证通过，false表示验证有错误
	 */
	public static <T> boolean validateAll(T t,Map<String, String> errorMap) {
		boolean flag = true;
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<T>> set =  validator.validate(t);
		if (!set.isEmpty()) {
			flag = false;
		}
		if (errorMap != null) {
			for (ConstraintViolation<T> constraintViolation : set) {
				errorMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
			}
		}
		return flag;
	}
	
	
	/**
	 * 验证 javabean中指定属性值是否正确
	 * @param t 要验证的javabean对象
	 * @param propertys  指定要验证的属性，必须是javabean中的某个属性
	 * @return true表示验证通过，false表示验证有错误
	 */
	public static <T> boolean validate(T t,String[] propertys) {
		return validate(t, propertys, null);
	}
	
	
	/**
	 * 验证 javabean中指定属性值是否正确，不正确把错误信息返回到errorMap中
	 * @param t 要验证的javabean对象
	 * @param propertys 指定要验证的属性，必须是javabean中的某个属性
	 * @param errorMap  如果有错误，把错误信息以key-value的显示放在此map中
	 * @return true表示验证通过，false表示验证有错误
	 */
	public static <T> boolean validate(T t,String[] propertys,Map<String, String> errorMap) {
		boolean flag = true;
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		for (String string : propertys) {
			Set<ConstraintViolation<T>> set =  validator.validateProperty(t, string);
			if (!set.isEmpty()) {
				flag = false;
			}
			if (errorMap != null) {
				for (ConstraintViolation<T> constraintViolation : set) {
					errorMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
				}
			} else {
				if (flag == false) {
					return flag;
				}
			} 
		}
		return flag;
	}
}
