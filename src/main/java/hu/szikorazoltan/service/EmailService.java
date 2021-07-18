package hu.szikorazoltan.service;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.mail.username}")
	private String MESSAGE_FROM;

	@Value("${base.url}")
	private String BASE_URL;

	private JavaMailSender javaMailSender;

	@Autowired
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void sendMessage(String email, String username, String code) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		try {
			helper.setFrom(MESSAGE_FROM);
			helper.setTo(email);
			helper.setSubject(BASE_URL + " - Sikeres regisztráció");

			String activateLink = "<a href=\"" + BASE_URL + "/api/activation/" + code + "\" target=\"_blank\">LINK</a>";
			String htmlBody = "<h1>Kedves " + username +"!</h1>"
					+ "<h3>Köszönjük, hogy regisztrált a(z) " + email + " email címmel.</h3>"
					+ "<p>A regisztráció megerősítéséhez kattintson az alábbi linkre.<br/> "
					+ "Aktiváló link: "+ activateLink + "</p>"
					+ "<h4>További szép napot!</h4>";

			helper.setText(htmlBody, true);
			javaMailSender.send(message);
		} catch (Exception e) {
			log.error("Hiba e-mail küldéskor az alábbi címre: " + email + " " + e);
		}
	}
}
