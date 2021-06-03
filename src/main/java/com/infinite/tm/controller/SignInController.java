package com.infinite.tm.controller;

import java.util.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.infinite.tm.model.ChangePassword;
import com.infinite.tm.model.TimeMgmtUserLogginData;
import com.infinite.tm.model.User;
import com.infinite.tm.model.repository.TimeMgmtUserHierarchyRepository;
import com.infinite.tm.model.repository.UserRepository;
import com.infinite.tm.util.EmailUtility;
import com.infinite.tm.util.GeneratePassword;

@RestController
@CrossOrigin(origins="http://localhost:80")
@RequestMapping("/TM")
public final class SignInController {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	TimeMgmtUserHierarchyRepository timeMgmtUserHierarchyRepository;
	
//	@GetMapping("/all")
//	public List<TimeMgmtUserHierarchy> getAll()
//	{
//		List<TimeMgmtUserHierarchy> users = this.timeMgmtUserHierarchyRepository.findAll();
//		return users;
//	}
	
	
	@PostMapping("/signin")
	public TimeMgmtUserLogginData signin(@RequestBody User customer)
	{


		 Logger logger = LoggerFactory.getLogger(SignInController.class);
		 TimeMgmtUserLogginData logginData = new TimeMgmtUserLogginData();
		 
		 User customerData = userRepository.findByEmailId(customer.getEmailId().toLowerCase().trim());
		 
		if (customerData!=null) {
			if(customerData.getPassword().equals(customer.getPassword()))
			{
		         logger.info("loggedin");
				 TimeMgmtUserLogginData logginData_ = new TimeMgmtUserLogginData(customerData.getEmpId(),
						 customerData.getFirstName(), customerData.getLastName(), customerData.getEmailId(), 
						 customerData.getRole(), customerData.getCreateDate(), customerData.getLocation(),
						 customerData.getAccountInfo(), "Sucess", 200);
		         logger.info("success");

		         return logginData_;
			}
			else
			{
				logginData.setStatus("Password don't Match");
				logginData.setStatusCode(409);
				 
		         return logginData;
			}
		}
		logginData.setStatus("Wrong Email-Id");
		logginData.setStatusCode(500);
		 
         return logginData;
		
	}
	
	@PostMapping("/changePassword")
	public String changePassword(@RequestBody ChangePassword customer ){
		JSONObject result =new JSONObject();
		 Logger logger = LoggerFactory.getLogger(SignInController.class);
		 
		User customerData = userRepository.findByEmailId(customer.getEmailId().toLowerCase().trim());
		if (customerData!=null)
		{
			if(customerData.getPassword().equals(customer.getOldPassword()))
			{
				if(customer.getConfirmPassword().equalsIgnoreCase(customer.getNewPassword()))
				{
					customerData.setPassword(customer.getNewPassword());
					userRepository.save(customerData);
					logger.info("password changed");
					 result.put("status", "Password Changed");
					 result.put("StatusCode", 200);
			         return result.toString();
				}
				else
				{
					logger.info("password doesn't match");
					result.put("status", "Password don't Match");
			         result.put("StatusCode", 409);
			         return result.toString();		
				}
			
			}
		}
		
		result.put("status", "user Doesn't Exits");
        result.put("StatusCode", 500);
        return result.toString();	
        
	}
	
	@PostMapping("/forgetPassword/Email/{emailId}")
	public String NewPassword(@PathVariable("emailId") String emailId )
	{
		 Logger logger = LoggerFactory.getLogger(SignInController.class);
			JSONObject result =new JSONObject();

		User user = userRepository.findByEmailId(emailId.toLowerCase().trim());
		if(user!=null)
		{
			user.setPassword(new String(Base64.getEncoder().encodeToString(GeneratePassword.newPassword().getBytes())));
			userRepository.save(user);
			logger.info("password reset");
			
			String subject= "Time Managment: RESET PASSWORD";
			StringBuilder text = new StringBuilder();
			text.append("<div><p style='font-family:calibri;font-size:18;'>Hi <b>"+user.getFirstName()+" "+user.getLastName()+"</b>,</p></div>");

			 text.append("<div><p style='font-family:calibri;font-size:18;'> Your request for reset Password is processed.<br> Try the new password <b>"+
			new String(Base64.getDecoder().decode(user.getPassword().getBytes()))+"</b> to login.<br>");
			 text.append(EmailUtility.thanks());
			String mail=  EmailUtility.SendEmail(emailId,subject,text.toString());
			result.put("status", "Sucess");
	         result.put("object", mail);
	         result.put("StatusCode", 200);
	         return result.toString();
		}
			result.put("status", "User Doesn't Exits");
			result.put("StatusCode", 500);
			return result.toString();	
	}
	
	

}
	

