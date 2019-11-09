package com.frankokafor.rest.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContext implements ApplicationContextAware {

	private static ApplicationContext CONTEXT;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		CONTEXT = applicationContext;

	}

	public static Object getBean(String beanName) {
		return CONTEXT.getBean(beanName);
	}

	/*
	 * this class helps us get class bean from anywhere in the application
	 * 
	 */
}
