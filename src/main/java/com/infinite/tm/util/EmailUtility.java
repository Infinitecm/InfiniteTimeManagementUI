package com.infinite.tm.util;

import java.util.Base64;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinite.tm.model.User;

public class EmailUtility {
	
	public static String SendEmail(String emailId, String subject, String text)
	{
		
	    Logger logger = LoggerFactory.getLogger(EmailUtility.class);
		
	      String from = "infinitetimsheet@gmail.com"; 
	      String password="infinite@1";
	  
	      //Get properties object    
        Properties props = new Properties();    
        props.put("mail.smtp.host", "smtp.gmail.com");    
        props.put("mail.smtp.socketFactory.port", "465");    
        props.put("mail.smtp.socketFactory.class",    
                  "javax.net.ssl.SSLSocketFactory");    
        props.put("mail.smtp.auth", "true");    
        props.put("mail.smtp.port", "465");    
        
        //get Session   
        Session session = Session.getInstance(props,new javax.mail.Authenticator() {    
         protected PasswordAuthentication getPasswordAuthentication() {    
         return new PasswordAuthentication(from,password);  
         }    
        });    

        //compose message    
        try {    
         MimeMessage message = new MimeMessage(session);    
         message.addRecipient(Message.RecipientType.TO,new InternetAddress(emailId));    
         message.setSubject(subject);    
         message.setContent(text, "text/html");    
         //send message  
         Transport.send(message);  
         logger.info("message sent successfully");

 		return "Check password in your mail";

        } catch (MessagingException e) {
      	  throw new RuntimeException(e);

        }    
	}
	
	public static String SendEmail(String toEmailId,String ccEmailId, String subject, String text)
	{
		
	    Logger logger = LoggerFactory.getLogger(EmailUtility.class);

		
	      String from = "infinitetimsheet@gmail.com"; 
	      String password="infinite@1";
	  
	      //Get properties object    
        Properties props = new Properties();    
        props.put("mail.smtp.host", "smtp.gmail.com");    
        props.put("mail.smtp.socketFactory.port", "465");    
        props.put("mail.smtp.socketFactory.class",    
                  "javax.net.ssl.SSLSocketFactory");    
        props.put("mail.smtp.auth", "true");    
        props.put("mail.smtp.port", "465");    
        
        //get Session   
        Session session = Session.getInstance(props,new javax.mail.Authenticator() {    
         protected PasswordAuthentication getPasswordAuthentication() {    
         return new PasswordAuthentication(from,password);  
         }    
        });    

        //compose message    
        try {    
         MimeMessage message = new MimeMessage(session);    
         message.addRecipient(Message.RecipientType.TO,new InternetAddress(toEmailId)); 
         message.addRecipient(Message.RecipientType.CC,new InternetAddress(ccEmailId));    

         message.setSubject(subject);    
         message.setContent(text, "text/html");    
         //send message  
         Transport.send(message);  
         logger.info("message sent successfully");

 		return "Check password in your mail";

        } catch (MessagingException e) {
      	  throw new RuntimeException(e);

        }    
	}

	public static void sendEmail(User userDetail) {
		try {
	
			StringBuilder message = new StringBuilder();
			 message.append("<div><p style='font-family:calibri;font-size:18;'> Hi " + userDetail.getFirstName() + " " + userDetail.getLastName() +",</p></div> "
					+ "<div><p style='font-family:calibri;font-size:18;'>\n\nYour request has been completed for access Time Management with requested USER Role : " 
					+ userDetail.getRole()+"</p></div>"
					+" <div><p style='font-family:calibri;font-size:18;'>You can login using your Email ID in to Time Management.\n\nPlease use <b>"+
					new String(Base64.getDecoder().decode(userDetail.getPassword().getBytes())) +
					"</b> as temporary password and make sure to update your password to keep your information secure.</p></div><br>");
			message.append(EmailUtility.thanks());
			String subject = "Time Management Access Information";
			EmailUtility.SendEmail(userDetail.getEmailId(), subject, message.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public static StringBuilder thanks()
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("<p><font color='#cb2027'><strong>Thanks,<br>Time Management Team</strong></font></p>");
		builder.append("<div><p><i>This is an auto-generated email from the system, so please do not reply to this email.</i>"
				+ "<i>For any questions, please contact your point of contact.</i></p></div>");
		return builder;
		
	}
	
	

}
