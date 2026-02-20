package com.org.java.app.util;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailSender {

	@Autowired
	private JavaMailSender mailSender;

	public void sendPdf(String to, String subject, String bodyHtml, byte[] pdfBytes, String filename)
			throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(bodyHtml, true);
		helper.addAttachment(filename, new ByteArrayResource(pdfBytes), "application/pdf");
		mailSender.send(message);
	}
}



