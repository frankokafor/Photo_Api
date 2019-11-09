package com.frankokafor.rest.model.response;

import org.springframework.stereotype.Component;

@Component
public class EmailMessages {
	public final String FROM = "frankebukaokafor@gmail.com";
	public final String SUBJECT_EMAIL_VERIFICATION = "Final step to complete your registration";
	public final String SUBJECT_PASSWORD_RESET = "Resquest To Reset Your Password";
	public final String HTML_BODY_EMAIL_VERIFICATION = "<h1>welcom to frank application</h1>"
			+ "<p>final step to complete your registration</p>" + "click the link below"
			+ "<a href='http://localhost:8080/EmailVerificationService/email.html?token=$tokenValue'/>";

	public final String TEXT_BODY_EMAIL_VERIFICATION = "Please Verify Your Email Address. "
			+ " Welcom to frank application" + " final step to complete your registration"
			+ " copy the link below to your browser";

	public final String TEXT_BODY_PASSWORD_RESET = " Please Verify That This Is You. "
			+ " a request has been made to our server to reset your password"
			+ " please if this request wasnt made by you kindly ignore this message"
			+ " otherwise copy the link below to your browser to reset your password";

}
