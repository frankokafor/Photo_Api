package com.frankokafor.rest.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class AppProperties {
	//the environment method is used to read property files in the application
	
	@Autowired
	private Environment environment;
	
	public String getTokenSecret() {
		return environment.getProperty("tokenSecret");
	}
}
