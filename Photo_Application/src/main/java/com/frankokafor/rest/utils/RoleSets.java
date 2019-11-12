package com.frankokafor.rest.utils;

import org.springframework.stereotype.Component;

@Component
public class RoleSets {
	
	public final String ADMIN = "ADMIN";
	public final String STAFF = "STAFF";
	public final String HR = "HR";
	public final String SUPER_ADMIN = "SUPER_ADMIN";
	public final String DIRECTOR = "DIRECTOR";
	public final String PR_ADMIN = "Has control over staff and hr";
	public final String PR_STAFF = "Normal user";
	public final String PR_HR = "Have control over staffs";
	public final String PR_SUPER_ADMIN = "Creator of the system";
	public final String PR_DIRECTOR = "Has total control of the system";
}
