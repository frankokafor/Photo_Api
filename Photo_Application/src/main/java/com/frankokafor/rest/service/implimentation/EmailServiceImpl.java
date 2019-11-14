package com.frankokafor.rest.service.implimentation;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.frankokafor.rest.model.response.EmailMessages;
import com.frankokafor.rest.models.UserEntity;
import com.frankokafor.rest.service.EmailService;
import com.frankokafor.rest.shared.object.UserDataTransferObject;

@Service
public class EmailServiceImpl implements EmailService {
	@Autowired
	private JavaMailSender sender;
	@Autowired
	private EmailMessages msg;

	@Async
	@Override
	public void sendText(UserEntity user) {
		try {
			String link = " http://localhost:8080/EmailVerificationService/email.html?token="
					+ user.getEmailVerificationToken();
			MimeMessage mime = sender.createMimeMessage();
			MimeMessageHelper mimeHelp = new MimeMessageHelper(mime, true);
			mimeHelp.setFrom(msg.FROM);
			mimeHelp.setTo(user.getEmail());
			mimeHelp.setSubject(msg.SUBJECT_EMAIL_VERIFICATION);
			mimeHelp.setText(msg.TEXT_BODY_EMAIL_VERIFICATION + link);
			mimeHelp.setSentDate(new Date());
			sender.send(mime);
		} catch (Exception e) {
			new ResponseEntity<>("wrong email address",HttpStatus.BAD_REQUEST);
		}
	}
	
	@Override
	public Boolean sendPasswordEmail(String name, String email, String token){
		boolean value = false;
		try {
			String fName = "HELLO " + name + "\n";
			String link = " http://localhost:8080/EmailVerificationService/password-reset.html?token=" + token;
			MimeMessage mime = sender.createMimeMessage();
			MimeMessageHelper mimeHelp = new MimeMessageHelper(mime, true);
			mimeHelp.setFrom(msg.FROM);
			mimeHelp.setTo(email);
			mimeHelp.setSubject(msg.SUBJECT_PASSWORD_RESET);
			mimeHelp.setText(fName + msg.TEXT_BODY_PASSWORD_RESET + link);
			mimeHelp.setSentDate(new Date());
			sender.send(mime);
			if (mime != null && (mime.getMessageID() != null && !mime.getMessageID().isEmpty())) {
				value = true;
			}
			return value;
		} catch (Exception e) {
			return value;
		}
	}
}
