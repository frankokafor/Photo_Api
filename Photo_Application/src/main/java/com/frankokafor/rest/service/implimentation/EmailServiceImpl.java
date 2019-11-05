package com.frankokafor.rest.service.implimentation;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.frankokafor.rest.model.response.EmailMessages;
import com.frankokafor.rest.service.EmailService;
import com.frankokafor.rest.shared.object.UserDataTransferObject;

@Service
public class EmailServiceImpl implements EmailService{
	@Autowired
	private JavaMailSender sender;
	@Autowired
	private EmailMessages msg;

	@Override
	public void sendText(UserDataTransferObject user) {
		try {
		String path = msg.TEXT_BODY.replace("$tokenValue", user.getEmailVerificationToken()); 
		MimeMessage mime = sender.createMimeMessage();
		MimeMessageHelper mimeHelp = new MimeMessageHelper(mime,true);
			mimeHelp.setFrom(msg.FROM);
			mimeHelp.setTo(user.getEmail());
			mimeHelp.setSubject(msg.SUBJECT);
			mimeHelp.setText(path);
			mimeHelp.setSentDate(new Date());
			sender.send(mime);
			System.out.println("mail sent");
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		
	}

}
