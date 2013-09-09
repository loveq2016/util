package com.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class SpringUtil implements BeanFactoryAware {
	private static BeanFactory beanFactory = null;

	//private static SpringUtil servlocator = null;

	public void setBeanFactory(BeanFactory factory) throws BeansException {
		beanFactory = factory;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	/**
	 * 创建读取Bean服务类实例(从spring.xml中加载) <bean id="serviceLocator"
	 * class="com.huawei.poi.common.util.ServiceLocator" scope="singleton" />
	 *//*
	public static SpringUtil getInstance() {
		if (servlocator == null)
			servlocator = (SpringUtil) beanFactory.getBean("springUtil");
		return servlocator;
	}*/

	/**
	 * 根据提供的bean名称得到相应的服务类
	 * @param servName bean名称
	 */
	public static Object getBean(String beanName) {
		return beanFactory.getBean(beanName);
	}

	/**
	 * 根据提供的bean名称得到对应于指定类型的服务类
	 * @param servName  bean名称
	 * @param clazz 返回的bean类型,若类型不匹配,将抛出异常
	 */
	public static <T> T getBean(String beanName, Class<T> clazz) {
		return beanFactory.getBean(beanName, clazz);
	}
}
