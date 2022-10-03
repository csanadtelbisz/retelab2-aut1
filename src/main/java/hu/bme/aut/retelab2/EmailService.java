package hu.bme.aut.retelab2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Component
public class EmailService {

	private final String EMAIL_FROM = "hello.springles@outlook.com";

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine thymeleafTemplateEngine;

	public void sendSimpleMessage(String to, String subject, String text) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(EMAIL_FROM);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);
			emailSender.send(message);
		} catch (MailException exception) {
			exception.printStackTrace();
		}
	}

	public void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel) {
		Context thymeleafContext = new Context();
		thymeleafContext.setVariables(templateModel);
		String htmlBody = thymeleafTemplateEngine.process("email.html", thymeleafContext);
		sendHtmlMessage(to, subject, htmlBody);
	}

	private void sendHtmlMessage(String to, String subject, String htmlBody) {
		MimeMessage message = emailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(EMAIL_FROM);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(htmlBody, true);
			emailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	@Primary
	@Bean
	public ITemplateResolver thymeleafTemplateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("template/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML");
		templateResolver.setCharacterEncoding("UTF-8");
		return templateResolver;
	}

	@Primary
	@Bean
	public SpringTemplateEngine thymeleafTemplateEngine(ITemplateResolver templateResolver) {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		templateEngine.setTemplateEngineMessageSource(new ResourceBundleMessageSource());
		return templateEngine;
	}
}
