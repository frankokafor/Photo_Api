package com.frankokafor.rest.events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import com.frankokafor.rest.models.UserEntity;
import com.frankokafor.rest.service.implimentation.EmailServiceImpl;

public class EmailEvent extends ApplicationEvent {
	
	private static final long serialVersionUID = 1L;
	@Autowired
	private EmailServiceImpl service;
	
	
	public EmailEvent(Object source) {
		super(source);
	}
	
	public Boolean sendMail2(String name, String email, String token) {
			return service.sendPasswordEmail(name, email, token);
	}
	
	public void sendMail(UserEntity user) {
		service.sendText(user);
	}

}
