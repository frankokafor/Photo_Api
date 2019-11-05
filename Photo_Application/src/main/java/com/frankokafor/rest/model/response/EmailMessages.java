package com.frankokafor.rest.model.response;

import org.springframework.stereotype.Component;

@Component
public class EmailMessages {
	public final String FROM = "frankebukaokafor@gmail.com";
	public final String SUBJECT = "Final step to complete your registration";
	public final String HTML_BODY = "<h1>welcom to frank application</h1>"
							+"<p>final step to complete your registration</p>"
							+"click the link below"
							+"<a href='http://localhost:8081/users/email-verification/email.html?token=$tokenValue'>";
	
	public final String TEXT_BODY =  "welcom to frank application"
			+"final step to complete your registration"
			+"copy the link below to your browser"
			+"http://localhost:8081/users/email-verification/email.html?token=$tokenValue";
	
	
	
}
