package com.infinite.tm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infinite.tm.model.AccountInformation;
import com.infinite.tm.model.LeadDetailData;
import com.infinite.tm.model.TimeMgmtData;
import com.infinite.tm.model.TimeMgmtManagerData;
import com.infinite.tm.model.TimeMgmtMemberData;
import com.infinite.tm.model.TimeMgmtTeamData;
import com.infinite.tm.model.TimeMgmtTimesheetEntry;
import com.infinite.tm.model.TimeMgmtUserHierarchy;
import com.infinite.tm.model.TimeMgmtWeeks;
import com.infinite.tm.model.User;
import com.infinite.tm.model.UserDetailData;
import com.infinite.tm.model.UserDetailVO;
import com.infinite.tm.model.repository.AccountInformationRepository;
import com.infinite.tm.model.repository.TimeMgmtTimeSheetEntryRepository;
import com.infinite.tm.model.repository.TimeMgmtUserHierarchyRepository;
import com.infinite.tm.model.repository.UserRepository;
import com.infinite.tm.util.AESEncryptDecryptUtil;
import com.infinite.tm.util.EmailUtility;
import com.infinite.tm.util.GeneratePassword;

@RestController
@CrossOrigin(origins="http://172.16.21.12:80")
@RequestMapping("/TM/Profile")
public class ProfileController {

	@Autowired
	TimeMgmtUserHierarchyRepository timeMgmtUserHierarchyRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TimeMgmtTimeSheetEntryRepository timeMgmtTimeSheetEntryRepository;
	
	@Autowired
	AccountInformationRepository accountInformationRepository;
	
	@Autowired
	private MongoOperations mongoOperation;
	
	
	private static final String MANAGER_EMAILID="emailId";
	private static final String TEAMLEAD_EMAILID="teams.emailId";
	private static final String USER_EMAILID="teams.members.emailId";
	private static final String ADMIN_EMAILID="admin@infinite.com";

	private static final String ADD="add";
	private static final String UPDATE="update";
	private static final String MANAGER="manager";
	private static final String ADMIN="admin";
	private static final String LEAD="lead";
	private static final String ACCOUNT="account";

	@Value("${crypto-key}")
	private String secretkey;
	
	Logger logger = LoggerFactory.getLogger(ProfileController.class);

	@PostMapping("/addProfile")
	public String addUser(@RequestBody UserDetailVO userDetail)
	{
	    final String cryptoKey = secretkey;

	    JSONObject result =new JSONObject();
		TimeMgmtUserHierarchy timeMgmtUserHierarchy=new TimeMgmtUserHierarchy();
		TimeMgmtUserHierarchy timeMgmtUserHierarchy1=new TimeMgmtUserHierarchy();
		
		TimeMgmtTimesheetEntry timesheetEntry=new TimeMgmtTimesheetEntry();
		List<TimeMgmtWeeks> weeks = new ArrayList<>();

		List<LeadDetailData> leadDetailDataList=new ArrayList<>();
		List<UserDetailData> userDetailDataList=new ArrayList<>();

		User user= new User();
		Query query = new Query();
		Query query1 = new Query();

	  try {
		 List<AccountInformation> accountInfo = getAccountFormat(userDetail.getAccounts(), userDetail.getProjectList());
		  logger.info("AccountInformation"+accountInfo);
	  
		//add/update of manager
		if((userDetail.getIsToolAdmin().equalsIgnoreCase("Yes")) && (userDetail.getRole().equalsIgnoreCase(MANAGER))) 
		{
			query.addCriteria(Criteria.where(MANAGER_EMAILID).is(userDetail.getEmailId().toLowerCase().trim()));
			timeMgmtUserHierarchy=mongoOperation.findOne(query, TimeMgmtUserHierarchy.class);		
			
			if((userDetail.getAction().equalsIgnoreCase(UPDATE)))
			{
				logger.info("updating in hierarchy");

				if( timeMgmtUserHierarchy !=null) 
				{
					
				//updating in userHierarchy
					
				timeMgmtUserHierarchy.setFirstName(userDetail.getFirstName());
				timeMgmtUserHierarchy.setLastName(userDetail.getLastName());
				timeMgmtUserHierarchy.setEmpId(userDetail.getEmpId());
				timeMgmtUserHierarchy.setRole(userDetail.getRole());
				timeMgmtUserHierarchy.setRate(AESEncryptDecryptUtil.encrypt(userDetail.getRate(),cryptoKey));
				timeMgmtUserHierarchy.setVendor(userDetail.getVendor());
				timeMgmtUserHierarchy.setVendorContact(userDetail.getVendorContact());
				timeMgmtUserHierarchy.setAccounts(userDetail.getAccounts());
				timeMgmtUserHierarchy.setProjects(userDetail.getProjectList());
				timeMgmtUserHierarchy.setAccountInfo(accountInfo);
				timeMgmtUserHierarchy.setCreateDate(userDetail.getCreateDate());
				timeMgmtUserHierarchy.setLocation(userDetail.getLocation());
				timeMgmtUserHierarchy.setType(userDetail.getType());
				
				mongoOperation.save(timeMgmtUserHierarchy);
				logger.info("updated in hierarchy");
				
				
				//updating in TimeSheetEntry
				timesheetEntry = timeMgmtTimeSheetEntryRepository.findByEmail(userDetail.getEmailId().toLowerCase().trim());

				timesheetEntry.setFirstName(userDetail.getFirstName());
				timesheetEntry.setLastName(userDetail.getLastName());
				timesheetEntry.setRole(userDetail.getRole());
				timesheetEntry.setVendor(userDetail.getVendor());
				timesheetEntry.setAccounts(userDetail.getAccounts());
				timesheetEntry.setProjects(userDetail.getProjectList());
				timesheetEntry.setCreateDate(userDetail.getCreateDate());
				timesheetEntry.setLocation(userDetail.getLocation());
				timesheetEntry.setType(userDetail.getType());
				timesheetEntry.setAccountInfo(accountInfo);
				
				mongoOperation.save(timesheetEntry);
				logger.info("updated in timesheetEntry");
				
				
				//updating in user
				user=mongoOperation.findOne(query, User.class);
				System.out.println(user);
				if( user !=null)
				{
				user.setEmpId(userDetail.getEmpId());
				user.setFirstName(userDetail.getFirstName());
				user.setLastName(userDetail.getLastName());
				user.setRole(userDetail.getRole());
				user.setCreateDate(userDetail.getCreateDate());
				user.setLocation(userDetail.getLocation());
				user.setAccountInfo(accountInfo);
				mongoOperation.save(user);
				logger.info("updated in user");
				}
				logger.info("Manager updated successfully");

				result.put("status", "Manager updated successfully");
				result.put("statusCode", 200);
				}
				else
				{
					result.put("status", "Manager doesn't Exist");
					result.put("statusCode",409);
					
				}
				
			}
			else if(userDetail.getAction().equalsIgnoreCase(ADD))
			{
				logger.info(" the action is add ");

				if(timeMgmtUserHierarchy==null) {
					logger.info(" adding a new manager ");

					//adding in hierarchy
					
					TimeMgmtUserHierarchy userHierarchy = new TimeMgmtUserHierarchy
							(userDetail.getEmpId(), userDetail.getFirstName(), userDetail.getLastName(),
							userDetail.getEmailId().toLowerCase().trim(), userDetail.getRole(), AESEncryptDecryptUtil.encrypt(userDetail.getRate(),cryptoKey), userDetail.getVendor(), userDetail.getVendorContact(),
							userDetail.getCreateDate(), userDetail.getLocation(), userDetail.getType(), leadDetailDataList,userDetail.getAccounts(), userDetail.getProjectList(),accountInfo);
					
					mongoOperation.save(userHierarchy);
					logger.info("added in hierarchy");
					//adding in TimeSheetEntry
					TimeMgmtTimesheetEntry timesheet = new TimeMgmtTimesheetEntry(userDetail.getEmailId().toLowerCase().trim(), userDetail.getEmpId(),
							userDetail.getFirstName(), userDetail.getLastName(), "", "", userDetail.getLead(),userDetail.getManager(), userDetail.getVendor(), 
							userDetail.getRole(), weeks,userDetail.getAccounts(),userDetail.getProjectList(),accountInfo,"", "true", userDetail.getCreateDate(), 
							userDetail.getLocation(), userDetail.getType());
					
					mongoOperation.save(timesheet);

					//adding in user
					
					user.setEmpId(userDetail.getEmpId());
					user.setEmailId(userDetail.getEmailId().toLowerCase().trim());
					user.setFirstName(userDetail.getFirstName());
					user.setLastName(userDetail.getLastName());
					user.setRole(userDetail.getRole());
					//password generated and saved
					user.setPassword(GeneratePassword.newPassword());
					user.setCreateDate(userDetail.getCreateDate());
					user.setLocation(userDetail.getLocation());
					user.setAccountInfo(accountInfo);
					mongoOperation.save(user);
					EmailUtility.sendEmail(user); //mail send
					logger.info("added in user");
					logger.info("Manager added successfully");

					result.put("status", "Manager added Successfully");
					result.put("statusCode", 200);
				}
				else
				{
					result.put("status", "Manager already exist, please use Update Lead");
					result.put("statusCode",409);
				}
				
			}
			
			
		}
		//add/update of lead
		else if((userDetail.getIsLead().equalsIgnoreCase("Yes")) && (userDetail.getIsManager()!= null || userDetail.getIsManager()!=""))
		{
			
			query.addCriteria((Criteria.where(TEAMLEAD_EMAILID).is(userDetail.getEmailId().toLowerCase().trim())));
			timeMgmtUserHierarchy=mongoOperation.findOne(query, TimeMgmtUserHierarchy.class);
			System.out.println(timeMgmtUserHierarchy);
			//add
			if(userDetail.getAction().equalsIgnoreCase(ADD)) {
				if(timeMgmtUserHierarchy!=null) {
					result.put("status", "Lead already exist, please use Update Lead");
					result.put("statusCode", 1);
				}else {
					query1.addCriteria(Criteria.where(MANAGER_EMAILID).is(userDetail.getManager().toLowerCase().trim()));
					timeMgmtUserHierarchy1=mongoOperation.findOne(query1, TimeMgmtUserHierarchy.class);
					System.out.println(timeMgmtUserHierarchy1);
					
					if(timeMgmtUserHierarchy1!=null) {
						//adding in hierarchy
						logger.info("adding in hierarchy");

						LeadDetailData leadDetail=new LeadDetailData(userDetail.getEmpId(), userDetail.getFirstName(), userDetail.getLastName(),
								userDetail.getEmailId().toLowerCase().trim(), userDetail.getManager(), userDetail.getRole(),
								AESEncryptDecryptUtil.encrypt(userDetail.getRate(),cryptoKey), userDetail.getVendor(), userDetail.getVendorContact(),
								userDetail.getCreateDate(), userDetail.getLocation(), userDetail.getType(),userDetail.getBillable(),
								userDetail.getAccounts(),userDetail.getProjectList(),accountInfo, userDetailDataList);
						
						logger.info("added in lead detail");
						
						//adding in timeSheetEntry
						TimeMgmtTimesheetEntry timesheet = new TimeMgmtTimesheetEntry(userDetail.getEmailId().toLowerCase().trim(), userDetail.getEmpId(),
								userDetail.getFirstName(), userDetail.getLastName(), timeMgmtUserHierarchy1.getFirstName(), timeMgmtUserHierarchy1.getLastName(),
								userDetail.getManager().toLowerCase().trim(), userDetail.getManager().toLowerCase().trim(), userDetail.getVendor(), 
								userDetail.getRole(), weeks,userDetail.getAccounts(), userDetail.getProjectList(),accountInfo,userDetail.getBillable(),
								"true", userDetail.getCreateDate(), userDetail.getLocation(), userDetail.getType());
						
						mongoOperation.save(timesheet);
						logger.info("added in timesheetEntry");


						if((timeMgmtUserHierarchy1.getEmailId().trim()).equalsIgnoreCase((userDetail.getManager().toLowerCase().trim())))
						{
							logger.info(" in if condition");

							leadDetailDataList=timeMgmtUserHierarchy1.getTeams();
							leadDetailDataList.add(leadDetail);
							logger.info("added in hierarchy"); 

							
						}else {
							leadDetailDataList=timeMgmtUserHierarchy1.getTeams();
						}
						timeMgmtUserHierarchy1.setTeams(leadDetailDataList);
						mongoOperation.save(timeMgmtUserHierarchy1);
						logger.info("added in hierarchy");

						//adding in user
					
						user.setEmpId(userDetail.getEmpId());
						user.setEmailId(userDetail.getEmailId().toLowerCase().trim());
						user.setFirstName(userDetail.getFirstName());
						user.setLastName(userDetail.getLastName());
						user.setRole(userDetail.getRole());
						//password generated and saved
						user.setPassword(GeneratePassword.newPassword());
						user.setCreateDate(userDetail.getCreateDate());
						user.setLocation(userDetail.getLocation());
						user.setAccountInfo(accountInfo);
						mongoOperation.save(user);
						EmailUtility.sendEmail(user); //mail send
						logger.info("added in user table");
						logger.info("Lead added successfully");

						result.put("status", "Lead added Successfully");
						result.put("statusCode", 200);
					}
					else {
						result.put("status", "Add lead failed");
						result.put("statusCode", 409);
					}
				}
			}
			//update
			else if(userDetail.getAction().equalsIgnoreCase(UPDATE)) {
				if(timeMgmtUserHierarchy !=null)
			{
				
				query1.addCriteria((Criteria.where(MANAGER_EMAILID).is(userDetail.getManager().toLowerCase().trim()))); 
				timeMgmtUserHierarchy1=mongoOperation.findOne(query1, TimeMgmtUserHierarchy.class);
				System.out.println(timeMgmtUserHierarchy1);
				//updated in hierarchy
				if((timeMgmtUserHierarchy1.getEmailId().trim()).equalsIgnoreCase((userDetail.getManager().toLowerCase().trim())))
				{
					for(LeadDetailData leadDetails: timeMgmtUserHierarchy1.getTeams()) {
						if(leadDetails.getEmailId().trim().equalsIgnoreCase(userDetail.getEmailId().toLowerCase().trim())) {
							leadDetails.setEmpId(userDetail.getEmpId());
							leadDetails.setFirstName(userDetail.getFirstName());
							leadDetails.setLastName(userDetail.getLastName());
							leadDetails.setRole(userDetail.getRole());
							leadDetails.setRate(AESEncryptDecryptUtil.encrypt(userDetail.getRate(),cryptoKey));
							leadDetails.setLead(userDetail.getManager());
							leadDetails.setVendor(userDetail.getVendor());
							leadDetails.setVendorContact(userDetail.getVendorContact());
							leadDetails.setAccounts(userDetail.getAccounts());
							leadDetails.setProjects(userDetail.getProjectList());
							leadDetails.setAccountInfo(accountInfo);
							leadDetails.setLead(timeMgmtUserHierarchy1.getEmailId().toLowerCase().trim());
							leadDetails.setMembers(leadDetails.getMembers());
							leadDetails.setCreateDate(leadDetails.getCreateDate());
							leadDetails.setLocation(userDetail.getLocation());
							leadDetails.setType(userDetail.getType());
							leadDetails.setBillable(userDetail.getBillable());
							leadDetailDataList.add(leadDetails);
							logger.info("Lead deatail list  updated successfully");

						}else {
							leadDetailDataList.add(leadDetails);
						}
					}
					timeMgmtUserHierarchy1.setTeams(leadDetailDataList);
					mongoOperation.save(timeMgmtUserHierarchy1);
					
					//updating in timeSheetEntry
					
						timesheetEntry = timeMgmtTimeSheetEntryRepository.findByEmail(userDetail.getEmailId().toLowerCase().trim());
						timesheetEntry.setFirstName(userDetail.getFirstName());
						timesheetEntry.setLastName(userDetail.getLastName());
						timesheetEntry.setRole(userDetail.getRole());
						timesheetEntry.setVendor(userDetail.getVendor());
						timesheetEntry.setAccounts(userDetail.getAccounts());
						timesheetEntry.setProjects(userDetail.getProjectList());
						timesheetEntry.setAccountInfo(accountInfo);
						timesheetEntry.setCreateDate(userDetail.getCreateDate());
						timesheetEntry.setLocation(userDetail.getLocation());
						timesheetEntry.setType(userDetail.getType());
						timesheetEntry.setBillable(userDetail.getBillable());
					mongoOperation.save(timesheetEntry);

					logger.info("updated in TimeSheetEntry");
					

					//updated in user
					logger.info("updating in user table");
					user=userRepository.findByEmailId(userDetail.getEmailId().toLowerCase().trim());
					user.setEmpId(userDetail.getEmpId());
					user.setFirstName(userDetail.getFirstName());
					user.setLastName(userDetail.getLastName());
					user.setRole(userDetail.getRole());
					user.setCreateDate(userDetail.getCreateDate());
					user.setLocation(userDetail.getLocation());
					user.setAccountInfo(accountInfo);
					mongoOperation.save(user);
					logger.info("updated in user table");
					
					logger.info("Lead updated successfully");

					result.put("status", "Lead updated Successfully");
					result.put("statusCode", 200);
				}

			}
			
			else {result.put("status", "Lead doesn't exist, Please use Add lead");
			result.put("statusCode", 409);
				
			}
		
		}
		}
		/*add/update of user*/
		else if((userDetail.getLead()!=null || userDetail.getLead().trim()!="") &&(userDetail.getManager()!=null || userDetail.getManager().trim()!="") && (userDetail.getIsUser().equalsIgnoreCase("Yes"))) 
		{
			logger.info("checking for user");

			query.addCriteria((Criteria.where(USER_EMAILID).is(userDetail.getEmailId().toLowerCase().trim())));
			timeMgmtUserHierarchy=mongoOperation.findOne(query, TimeMgmtUserHierarchy.class);
			System.out.println(timeMgmtUserHierarchy);

			if(userDetail.getAction().equalsIgnoreCase(ADD)) 
			{
				if(timeMgmtUserHierarchy!=null) {
					result.put("status", "User already exist, please use Update User");
					result.put("statusCode", 409);
				}else 
				{
					query1.addCriteria((Criteria.where(MANAGER_EMAILID).is(userDetail.getManager().toLowerCase().trim()))
							.andOperator((Criteria.where(TEAMLEAD_EMAILID).is(userDetail.getLead().toLowerCase().trim()))));

					timeMgmtUserHierarchy1=mongoOperation.findOne(query1, TimeMgmtUserHierarchy.class);
					if(timeMgmtUserHierarchy1!=null) {
						//adding in the hierarchy
						logger.info("adding in hierarchy");
						UserDetailData userDetailData = new UserDetailData(userDetail.getEmpId(), userDetail.getFirstName(), userDetail.getLastName(),
								userDetail.getEmailId().toLowerCase().trim(), userDetail.getRole(), AESEncryptDecryptUtil.encrypt(userDetail.getRate(),cryptoKey),
								userDetail.getLead(), userDetail.getVendor(), userDetail.getVendorContact(), userDetail.getCreateDate(), userDetail.getLocation(),
								userDetail.getType(),userDetail.getBillable(),userDetail.getAccounts(),userDetail.getProjectList(),accountInfo);
						
						TimeMgmtTimesheetEntry timesheet = new TimeMgmtTimesheetEntry(userDetail.getEmailId().toLowerCase().trim(), userDetail.getEmpId(),
								userDetail.getFirstName(), userDetail.getLastName(), "", "",userDetail.getLead().toLowerCase().trim(),
								timeMgmtUserHierarchy1.getEmailId().toLowerCase().trim() , userDetail.getVendor(), userDetail.getRole(), weeks,userDetail.getAccounts(), 
								userDetail.getProjectList(),accountInfo,userDetail.getBillable(), "true", userDetail.getCreateDate(), userDetail.getLocation(), userDetail.getType());
						
												
						for(LeadDetailData leadDetailData:timeMgmtUserHierarchy1.getTeams()) {
							if(leadDetailData.getEmailId().trim().equalsIgnoreCase(userDetail.getLead().toLowerCase().trim())){	
								
								timesheet.setLeadFirstName(leadDetailData.getFirstName());
								timesheet.setLeadLastName(leadDetailData.getLastName());
								
								userDetailDataList=leadDetailData.getMembers();
								userDetailDataList.add(userDetailData);
							}else {
								userDetailDataList=leadDetailData.getMembers();
							}
							leadDetailData.setMembers(userDetailDataList);
							leadDetailDataList.add(leadDetailData);
						}
						timeMgmtUserHierarchy1.setTeams(leadDetailDataList);
						mongoOperation.save(timeMgmtUserHierarchy1);
						mongoOperation.save(timesheet);
						logger.info("added in hierarchy and timeSheetEntry");
						
						//adding in user
						
						user.setEmailId(userDetail.getEmailId().toLowerCase().trim());
						user.setFirstName(userDetail.getFirstName());
						user.setLastName(userDetail.getLastName());
						user.setRole(userDetail.getRole());
						//password generated and saved
						user.setPassword(GeneratePassword.newPassword());
						user.setCreateDate(userDetail.getCreateDate());
						user.setLocation(userDetail.getLocation());
						user.setAccountInfo(accountInfo);
						mongoOperation.save(user);
						EmailUtility.sendEmail(user); //mail send
						logger.info("Added in user table");
						logger.info("User Added successfully");
						result.put("status", "User Added successfully");
						result.put("statusCode", 200);
						
					}
				}
			}
			else if(userDetail.getAction().equalsIgnoreCase(UPDATE))
			{
				if(timeMgmtUserHierarchy !=null)
				{
			
				query.addCriteria((Criteria.where(MANAGER_EMAILID).is(userDetail.getManager().toLowerCase().trim()))); 
				timeMgmtUserHierarchy=mongoOperation.findOne(query, TimeMgmtUserHierarchy.class);
				
				//updating in hierarchy
				for(LeadDetailData leadDetails: timeMgmtUserHierarchy.getTeams()) {
					if(leadDetails.getEmailId().trim().equalsIgnoreCase(userDetail.getLead().toLowerCase().trim())){
						for(UserDetailData userDetails:leadDetails.getMembers()) {
							if(userDetails.getEmailId().trim().equalsIgnoreCase(userDetail.getEmailId().toLowerCase().trim())) {
								userDetails.setFirstName(userDetail.getFirstName());
								userDetails.setLastName(userDetail.getLastName());
								userDetails.setEmpId(userDetail.getEmpId());
								userDetails.setRole(userDetail.getRole());
								userDetails.setRate(AESEncryptDecryptUtil.encrypt(userDetail.getRate(),cryptoKey));
								userDetails.setLead(userDetail.getLead());
								userDetails.setVendor(userDetail.getVendor());
								userDetails.setVendorContact(userDetail.getVendorContact());
								userDetails.setAccounts(userDetail.getAccounts());
								userDetails.setProjects(userDetail.getProjectList());
								userDetails.setAccountInfo(accountInfo);
								userDetails.setCreateDate(userDetail.getCreateDate());
								userDetails.setLocation(userDetail.getLocation());
								userDetails.setType(userDetail.getType());
								userDetails.setBillable(userDetail.getBillable());
								userDetailDataList.add(userDetails);
							}else {
								userDetailDataList.add(userDetails);
							}
						}
						
						leadDetails.setMembers(userDetailDataList);
					}else {
						leadDetails.getMembers();
					}
					leadDetailDataList.add(leadDetails);
				}
				timeMgmtUserHierarchy.setTeams(leadDetailDataList);
				mongoOperation.save(timeMgmtUserHierarchy);
				logger.info("updated in hierarchy");
				
				//updating in timeSheetEntry

				timesheetEntry = timeMgmtTimeSheetEntryRepository.findByEmail(userDetail.getEmailId().toLowerCase().trim());
				
					timesheetEntry.setFirstName(userDetail.getFirstName());
					timesheetEntry.setLastName(userDetail.getLastName());
					timesheetEntry.setVendor(userDetail.getVendor());
					timesheetEntry.setAccounts(userDetail.getAccounts());
					timesheetEntry.setProjects(userDetail.getProjectList());
					timesheetEntry.setAccountInfo(accountInfo);
					timesheetEntry.setRole(userDetail.getRole());
					timesheetEntry.setCreateDate(userDetail.getCreateDate());
					timesheetEntry.setLocation(userDetail.getLocation());
					timesheetEntry.setType(userDetail.getType());
					timesheetEntry.setBillable(userDetail.getBillable());
					mongoOperation.save(timesheetEntry);

					
				logger.info("updated in TimeSheetEntry");
				
				
				//updating in user
				user=userRepository.findByEmailId(userDetail.getEmailId().toLowerCase().trim());
				user.setEmpId(userDetail.getEmpId());
				user.setFirstName(userDetail.getFirstName());
				user.setLastName(userDetail.getLastName());
				user.setRole(userDetail.getRole());
				user.setCreateDate(userDetail.getCreateDate());
				user.setLocation(userDetail.getLocation());
				user.setAccountInfo(accountInfo);
				mongoOperation.save(user);
				logger.info("updated in user table");
				
				logger.info("User updated successfully");

				result.put("status", "User updated successfully");
				result.put("statusCode", 200);
			}
			
			else {
				result.put("status", "User doesn't exist, Please use add User");
				result.put("statusCode", 409);
			}
			  
		}
		else
		{
			logger.info("can't add or updates");

			result.put("status", "can't add or update");
			result.put("statusCode", 409);
		}
		
	  }
	  
	  }
	  catch (Exception e) {
		  logger.info("Error in addUserDetail of TimeMgmtUserHierarchyController:: " + e.getMessage());
		  e.printStackTrace();	
		  }

		
		return result.toString();		
 
	}
	
	
	
	

	@GetMapping("/userDetails/emailId/{emailId}/role/{role}")
	public TimeMgmtData getUserDetails(@PathVariable("emailId") String emailId,@PathVariable("role") String role )
	{
		Query query = new Query();
	    final String cryptoKey = secretkey;

		List<TimeMgmtManagerData> managerList=new ArrayList<>();
		List<TimeMgmtTeamData> tlList=new ArrayList<>();
		List<TimeMgmtMemberData> memberList=new ArrayList<>();

		if(role.equalsIgnoreCase(ADMIN)&& emailId.toLowerCase().trim().equalsIgnoreCase(ADMIN_EMAILID))
		{
			List<TimeMgmtUserHierarchy> timeMgmtUserHierarchy=this.timeMgmtUserHierarchyRepository.findAll();
			System.out.println(timeMgmtUserHierarchy);
			
			if(!timeMgmtUserHierarchy.isEmpty())
			{
				for(TimeMgmtUserHierarchy tm: timeMgmtUserHierarchy) {
					
					TimeMgmtManagerData managerData = new TimeMgmtManagerData(tm.getEmpId(), tm.getEmailId(), tm.getFirstName(),
							tm.getLastName(), tm.getRole(), tm.getCreateDate(), tm.getLocation(), tm.getType(),tm.getAccounts(), tm.getProjects());
					managerList.add(managerData);//adding manager in managerList
					logger.info("manager List added ");

					for(LeadDetailData leadDetailData : tm.getTeams())
					{
						 
						TimeMgmtTeamData teamsData = new TimeMgmtTeamData(leadDetailData.getEmpId(), leadDetailData.getEmailId(),
								leadDetailData.getFirstName(), leadDetailData.getLastName(), leadDetailData.getRole(), 
								AESEncryptDecryptUtil.decrypt(leadDetailData.getRate(),cryptoKey), leadDetailData.getVendor(),
								leadDetailData.getVendorContact(), leadDetailData.getCreateDate(), leadDetailData.getLead(),
								leadDetailData.getLocation(), leadDetailData.getType(),leadDetailData.getBillable(),
								leadDetailData.getAccounts(), leadDetailData.getProjects());
						tlList.add(teamsData);//adding lead in teamslist
						logger.info("lead list added");

						for(UserDetailData userDetailData : leadDetailData.getMembers())
						{
														
							TimeMgmtMemberData memberData = new TimeMgmtMemberData(userDetailData.getEmpId(), userDetailData.getEmailId(),
									userDetailData.getFirstName(), userDetailData.getLastName(), userDetailData.getRole(), 
									AESEncryptDecryptUtil.decrypt(userDetailData.getRate(),cryptoKey), userDetailData.getVendor(),
									userDetailData.getVendorContact(), userDetailData.getCreateDate(), userDetailData.getLead(),
									"", userDetailData.getLocation(), userDetailData.getType(),userDetailData.getBillable(),
									"",userDetailData.getAccounts(), userDetailData.getProjects());
							memberList.add(memberData);//adding in memberList
							logger.info("member list added");
						}
					}
				}
				TimeMgmtData timeMgmtData = new TimeMgmtData("200", "success", managerList, tlList, memberList);
				logger.info("success");
				return timeMgmtData;
			
			}
			TimeMgmtData timeMgmtData = new TimeMgmtData("409", "No Data Preseent", Arrays.asList(), Arrays.asList(), Arrays.asList());
			logger.info("No data available");
			return timeMgmtData;
		}
		else if(role.equalsIgnoreCase(MANAGER))
		{
			List<TimeMgmtUserHierarchy> timeMgmtUserHierarchy=timeMgmtUserHierarchyRepository.findByEmailId(emailId.toLowerCase().trim());
			System.out.println(timeMgmtUserHierarchy);
			if(!timeMgmtUserHierarchy.isEmpty())
			{
				for(TimeMgmtUserHierarchy tm: timeMgmtUserHierarchy) {
					TimeMgmtManagerData managerData = new TimeMgmtManagerData(tm.getEmpId(), tm.getEmailId(), tm.getFirstName(),
							tm.getLastName(), tm.getRole(), tm.getCreateDate(), tm.getLocation(), tm.getType(),tm.getAccounts(), tm.getProjects());
					managerList.add(managerData);//adding manager in managerList
					logger.info("manager List added ");

					for(LeadDetailData leadDetailData : tm.getTeams())
					{
						TimeMgmtTeamData teamsData = new TimeMgmtTeamData(leadDetailData.getEmpId(), leadDetailData.getEmailId(),
								leadDetailData.getFirstName(), leadDetailData.getLastName(), leadDetailData.getRole(), 
								AESEncryptDecryptUtil.decrypt(leadDetailData.getRate(),cryptoKey), leadDetailData.getVendor(),
								leadDetailData.getVendorContact(), leadDetailData.getCreateDate(), leadDetailData.getLead(),
								leadDetailData.getLocation(), leadDetailData.getType(),leadDetailData.getBillable(),
								leadDetailData.getAccounts(), leadDetailData.getProjects());
						
						tlList.add(teamsData);//adding lead in teamslist
						logger.info("lead list added");

						for(UserDetailData userDetailData : leadDetailData.getMembers())
						{
							TimeMgmtMemberData memberData = new TimeMgmtMemberData(userDetailData.getEmpId(), userDetailData.getEmailId(),
									userDetailData.getFirstName(), userDetailData.getLastName(), userDetailData.getRole(), 
									AESEncryptDecryptUtil.decrypt(userDetailData.getRate(),cryptoKey), userDetailData.getVendor(),
									userDetailData.getVendorContact(), userDetailData.getCreateDate(), userDetailData.getLead(),
									"", userDetailData.getLocation(), userDetailData.getType(), userDetailData.getBillable(),
									"",userDetailData.getAccounts(), userDetailData.getProjects());
							memberList.add(memberData);//adding in memberList
							logger.info("member list added");
						}
					}
				}
				TimeMgmtData timeMgmtData = new TimeMgmtData("200", "success", managerList, tlList, memberList);
				logger.info("success");
				return timeMgmtData;
					
			}
			TimeMgmtData timeMgmtData = new TimeMgmtData("409", "EmailId for Manager Role Doesn't Exit", Arrays.asList(), Arrays.asList(), Arrays.asList());
			logger.info("No data available");
			return timeMgmtData;
		}
		else if(role.equalsIgnoreCase(LEAD))
		{
			query.addCriteria(Criteria.where(TEAMLEAD_EMAILID).is(emailId.toLowerCase().trim()));
			List<TimeMgmtUserHierarchy> timeMgmtUserHierarchy=mongoOperation.find(query, TimeMgmtUserHierarchy.class);
			System.out.println(timeMgmtUserHierarchy);
			if(!timeMgmtUserHierarchy.isEmpty())
			{
			for(TimeMgmtUserHierarchy tm: timeMgmtUserHierarchy) 
			{
				TimeMgmtManagerData managerData = new TimeMgmtManagerData(tm.getEmpId(), tm.getEmailId(), tm.getFirstName(),
						tm.getLastName(), tm.getRole(), tm.getCreateDate(), tm.getLocation(), tm.getType(),tm.getAccounts(), tm.getProjects());
				managerList.add(managerData);//adding manager in managerList
				
				logger.info("manager List added ");

				for(LeadDetailData leadDetailData: tm.getTeams()) {
					logger.info("inside for");
	
					if(leadDetailData.getEmailId().trim().equalsIgnoreCase(emailId.toLowerCase().trim())) 
					{
						TimeMgmtTeamData teamsData = new TimeMgmtTeamData(leadDetailData.getEmpId(), leadDetailData.getEmailId(),
								leadDetailData.getFirstName(), leadDetailData.getLastName(), leadDetailData.getRole(), 
								AESEncryptDecryptUtil.decrypt(leadDetailData.getRate(),cryptoKey), leadDetailData.getVendor(),
								leadDetailData.getVendorContact(), leadDetailData.getCreateDate(), leadDetailData.getLead(),
								leadDetailData.getLocation(), leadDetailData.getType(), leadDetailData.getBillable(),
								leadDetailData.getAccounts(), leadDetailData.getProjects());
						tlList.add(teamsData);//adding lead in teamslist
						logger.info("lead list added");

						for(UserDetailData userDetailData : leadDetailData.getMembers())
						{
							TimeMgmtMemberData memberData = new TimeMgmtMemberData(userDetailData.getEmpId(), userDetailData.getEmailId(),
									userDetailData.getFirstName(), userDetailData.getLastName(), userDetailData.getRole(), 
									AESEncryptDecryptUtil.decrypt(userDetailData.getRate(),cryptoKey), userDetailData.getVendor(),
									userDetailData.getVendorContact(), userDetailData.getCreateDate(), userDetailData.getLead(),
									"", userDetailData.getLocation(), userDetailData.getType(), userDetailData.getBillable(),
									"",userDetailData.getAccounts(), userDetailData.getProjects());
							
							memberList.add(memberData);//adding in memberList
							logger.info("member list added");
						}
						
					}
				}
			}
			TimeMgmtData timeMgmtData = new TimeMgmtData("200","success", managerList, tlList, memberList);
			logger.info("success");
			return timeMgmtData;			
			}
		TimeMgmtData timeMgmtData = new TimeMgmtData("409","EmailId for Lead Role Doesn't Exit", Arrays.asList(), Arrays.asList(), Arrays.asList());
		logger.info("No data available");
		return timeMgmtData;
		}
	TimeMgmtData timeMgmtData = new TimeMgmtData("409","Wrong input Data", Arrays.asList(), Arrays.asList(), Arrays.asList());
	logger.info("No data available");
	return timeMgmtData;
	}
		
	@GetMapping("/accountDetails")
	public List<AccountInformation> getAccountDetails()
	{
		List<AccountInformation> accounts =this.accountInformationRepository.findAll();
		return accounts;
		
	}
	
	
	public List<AccountInformation> getAccountFormat( List<String> accountList, List<String> projectList)
	{
		
		logger.info("inside AccountFormat"+accountList.size());
		List<AccountInformation> accountInfo = new ArrayList<>();
		try
		{
		for(int i=0;i<accountList.size();i++)
		{
			Query query = new Query();
			query.addCriteria(Criteria.where(ACCOUNT).regex(accountList.get(i),"i"));
			AccountInformation account = mongoOperation.findOne(query, AccountInformation.class);
			List<String> project = new ArrayList<>();

			for(int j=0;j<projectList.size();j++)
			{						
				for (int k=0;k<account.getProjects().size();k++)
				{
				    if (account.getProjects().get(k).equalsIgnoreCase(projectList.get(j)))
				          project.add(account.getProjects().get(k));
				}

			}

			accountInfo.add(new AccountInformation(accountList.get(i), project));
			
		}
		logger.info("out of accountFormat");

		}
		catch (Exception e) {
			logger.info("inside catch of accountFormat "+ e);
		}
		return accountInfo;

	}
	
	
	
}
