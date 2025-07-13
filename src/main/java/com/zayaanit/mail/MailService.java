package com.zayaanit.mail;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.zayaanit.exception.CustomException;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.extern.slf4j.Slf4j;

/**
 * Zubayer Ahamed
 * @since Jul 13, 2025
 */
@Slf4j
@Service
public class MailService {

	@Autowired private Environment env;

	public void sendMail(MailReqDto reqDto) throws CustomException, AddressException, MessagingException, ParseErrorException, MethodInvocationException, ResourceNotFoundException, IOException {

		// Configure email settings from properties file
		Properties props = System.getProperties();
		props.setProperty("mail.debug", env.getProperty("mail.debug"));
		props.setProperty("mail.smtp.host", env.getProperty("mail.smtp.host"));
		props.setProperty("mail.smtp.port", env.getProperty("mail.smtp.port"));
		props.setProperty("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
		props.setProperty("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));
		props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

		// Login accout 
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(env.getProperty("mail.user"), env.getProperty("mail.passwd"));
			}
		});

		// Mime Message Settings
		MimeMessage message = new MimeMessage(session);
		message.setSubject(reqDto.getSubject());
		message.setFrom(new InternetAddress(reqDto.getFrom()));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(reqDto.getTo()));
		if(StringUtils.isNotBlank(reqDto.getReplyTo())) message.setReplyTo(InternetAddress.parse(reqDto.getReplyTo()));
		if(reqDto.getCc() != null && !reqDto.getCc().isEmpty()) {
			for(String c : reqDto.getCc()) {
				if(StringUtils.isBlank(c)) continue;
				message.setRecipient(Message.RecipientType.CC, new InternetAddress(c));
			}
		}
		if(reqDto.getBcc() != null && !reqDto.getBcc().isEmpty()) {
			for(String c : reqDto.getBcc()) {
				if(StringUtils.isBlank(c)) continue;
				message.setRecipient(Message.RecipientType.BCC, new InternetAddress(c));
			}
		}

		// Init Mail Body
		BodyPart mailBody = new MimeBodyPart();

		// Init Velocity engine
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.init();

		// create context and add data
		VelocityContext context = new VelocityContext();
		reqDto.getContextData().entrySet().stream().forEach(d -> {
			context.put(d.getKey(), d.getValue());
		});

		File templateFile = getTemplateFile(reqDto.getMailType());
		log.debug("Mail template : {}", templateFile.getAbsolutePath());
		InputStream isr = new BufferedInputStream(new FileInputStream(templateFile));
		Reader reader = new InputStreamReader(isr, StandardCharsets.UTF_8);

		/* now render the template into a StringWriter */
		StringWriter bodyWriter = new StringWriter();
		velocityEngine.evaluate(context, bodyWriter, "", reader);

		// Set template data to mail body
		mailBody.setContent(bodyWriter.toString(), "text/html");

		// Add Attachment 
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mailBody);
//		for(Map.Entry<String, String> attachment : getFiles().entrySet()){
//			BodyPart multipartBody = new MimeBodyPart();
//			DataSource source = new FileDataSource(attachment.getValue());
//			multipartBody.setDataHandler(new DataHandler(source));
//			multipartBody.setFileName(attachment.getKey());
//			multipart.addBodyPart(multipartBody);
//		}

		message.setContent(multipart);

		// Save mail file before send
//		File file = new File(EML_FILE);
//		if(!file.exists()) {
//			file.createNewFile();
//		}
		//message.writeTo(new FileOutputStream(file));

		// send mail
		Transport.send(message);

	}

	private Map<String, String> getFiles(){
		Map<String, String> map = new HashMap<>();
		map.put("attachment.csv", "src/main/resources/static/mail-attachment-template.csv");
		map.put("attachment.pdf", "src/main/resources/static/in_DUMMY088990_20200825-025718.pdf");
		return map;
	}

	private File getTemplateFile(MailType mailType) {
		File file = null;
		if(MailType.EVENT_REMINDER.equals(mailType)) {
			String serverTemplate = env.getProperty("mail.template.server");
			if(StringUtils.isBlank(serverTemplate)) serverTemplate = "src/main/resources/static/standard_event_reminder_email_template.vm";
			file = new File(serverTemplate);
		} else if(MailType.TASK_REMINDER.equals(mailType)) {
			String serverTemplate = env.getProperty("mail.template.service");
			if(StringUtils.isBlank(serverTemplate)) serverTemplate = "src/main/resources/static/standard_task_reminder_email_template.vm";
			file = new File(serverTemplate);
		} 
		return file;
	}

}
