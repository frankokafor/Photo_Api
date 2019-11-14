package com.frankokafor.rest.events;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class EmailEventListener implements ApplicationListener{

	public void onApplicationEvent(ApplicationEvent event) {
		System.out.println(event.toString());
	}

}
