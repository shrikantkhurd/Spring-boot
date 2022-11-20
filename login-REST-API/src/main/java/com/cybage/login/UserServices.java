package com.cybage.login;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;

@Service
public class UserServices {

	@Autowired
	private UserRepository repo;


//	@Autowired
//	private PasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender mailSender;

	public void register(User user, String siteURL) throws UnsupportedEncodingException, MessagingException {
		// String encodedPassword = passwordEncoder.encode(user.getPassword());
		// user.setPassword(encodedPassword);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);
		user.setEnabled(false);
		//user.setEnabled("In Active");

		repo.save(user);

		sendVerificationEmail(user, siteURL);
	}

	private void sendVerificationEmail(User user, String siteURL)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = user.getEmail();
		String fromAddress = "admin@gmail.com";
		String senderName = "Event Management";
		String subject = "Please verify your registration";

		String content="<p> Dear " + user.getFirstName() +" "+user.getLastName() +",</p>";
	
		content += "<p> Please click the link below to verify your registration:</p>";
		
		String verifyURL= siteURL + "/verify?code=" + user.getVerificationCode();
		
		content +="<h4> <a href=\""+ verifyURL + "\">VERIFY</a></h4>";
		content += "<p> Thank you <br> Event Management</p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);


		helper.setText(content, true);

		mailSender.send(message);

	}
	public boolean verify(String verificationCode) {
		//public String verify(String verificationCode) {
	    User user = repo.findByVerificationCode(verificationCode);
	     
	    if (user == null || user.isEnabled()) {
	        return false;
	    } else {
	        user.setVerificationCode(null);
	        user.setEnabled(true);
	        repo.save(user);
	         
	        return true;
	    }
		/*
		 * if (user == null ) { user.setEnabled("In Active"); } else {
		 * user.setVerificationCode(null); user.setEnabled("Active"); repo.save(user);
		 * 
		 * return "Active";
		 * 
		 * } return verificationCode;
		 */
	     
	}

}
