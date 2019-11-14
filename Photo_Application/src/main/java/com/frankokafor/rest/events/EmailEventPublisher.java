package com.frankokafor.rest.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import com.frankokafor.rest.models.UserEntity;

public class EmailEventPublisher implements ApplicationEventPublisherAware{
	
	@Autowired
    private ApplicationEventPublisher publisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
		
	}
	
	public void sendEmailPublishEvent(UserEntity user) {
	//	EmailEvent event = new EmailEvent(user).sendMail(user);
	}

}
