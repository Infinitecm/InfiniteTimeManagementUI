package com.infinite.tm.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.mongodb.client.result.UpdateResult;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infinite.tm.model.AccountInformation;
import com.infinite.tm.model.HolidayList;
import com.infinite.tm.model.HolidaysVO;
import com.infinite.tm.model.LeadDetailData;
import com.infinite.tm.model.TimeMgmtDates;
import com.infinite.tm.model.TimeMgmtDays;
import com.infinite.tm.model.TimeMgmtReportData;
import com.infinite.tm.model.TimeMgmtSubmitDataVO;
import com.infinite.tm.model.TimeMgmtSubmittedDataVO;
import com.infinite.tm.model.TimeMgmtTimesheetData;
import com.infinite.tm.model.TimeMgmtTimesheetEntry;
import com.infinite.tm.model.TimeMgmtTimesheetWeekStatusVO;
import com.infinite.tm.model.TimeMgmtTimesheetWeeklyData;
import com.infinite.tm.model.TimeMgmtUserDataVO;
import com.infinite.tm.model.TimeMgmtUserHierarchy;
import com.infinite.tm.model.TimeMgmtWeekDataVO;
import com.infinite.tm.model.TimeMgmtWeekday;
import com.infinite.tm.model.TimeMgmtWeeks;
import com.infinite.tm.model.TimesheetDetail;
import com.infinite.tm.model.UserDetailData;
import com.infinite.tm.model.repository.HolidayListRepository;
import com.infinite.tm.model.repository.TimeMgmtTimeSheetEntryRepository;
import com.infinite.tm.util.AESEncryptDecryptUtil;
import com.infinite.tm.util.EmailUtility;

@RestController
@Component
@CrossOrigin(origins = "http://172.16.21.12:80")
@RequestMapping("/TM/TimesheetEntry")
public class TimesheetEntryController {

	@Autowired
	TimeMgmtTimeSheetEntryRepository timeMgmtTimeSheetEntryRepository;
	
	@Autowired
	HolidayListRepository holidayListRepository;
	

	@Autowired
	private MongoOperations mongoOperation;

	Logger logger = LoggerFactory.getLogger(TimesheetEntryController.class);
	
	private static final String DAYS = ".days";
	private static final String STATUS = ".status";
	private static final String WEEKNO = ".weekNo";
	private static final String EMAIL = "email";
	private static final String LEAD = "lead";
	private static final String MANAGER = "manager";
	private static final String USER = "user";
	private static final String MANAGER_EMAILID = "emailId";
	private static final String LOCATION = "location";
	private static final String WEEKS = "weeks.";
	
	@Value("${crypto-key}")
	private String secretkey;
	
	double sum = 0.0;
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

	int LastDate = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
	
	
	 @PostMapping("/getHolidaysForExcel")
	public List<HolidaysVO> getHolidaysForExcel(@RequestBody TimeMgmtWeekDataVO data) throws ParseException
	 {
	        List<HolidaysVO> holidays=new ArrayList<HolidaysVO>();
	        Date startDate=sdf.parse(data.getStartDate());
	        Date endDate=sdf.parse(data.getEndDate());
	        List<HolidayList> holidayList=holidayListRepository.findAll();
	        for(HolidayList holiday:holidayList) 
	        {
	            for(TimeMgmtDates dates:holiday.getDates()) 
	            {
	                Date date=sdf.parse(dates.getDate());
	             
	                SimpleDateFormat sdf = new SimpleDateFormat("EEE");
	                String day = sdf.format(date);
	                System.out.println(day+"=========================================day =--------------");
	                if(((date.equals(startDate)||date.after(startDate))&&(date.equals(endDate)||date.before(endDate)))) 
	                {
	                    System.out.println("adding=========================");
	                    HolidaysVO hday=new HolidaysVO(holiday.getLocation(), dates.getDate(), day,  dates.getReason());
	                    holidays.add(hday);
	                }
	             
	            }
	        }
	        return holidays;
	       
	   }
	
	@Scheduled(cron = "0 0 0 1 * *")
	public void monthlyAutoApprove()
	{
		try {

			List<TimeMgmtTimesheetEntry> members = timeMgmtTimeSheetEntryRepository.findAll();
			if (members != null) {

				for (TimeMgmtTimesheetEntry member : members) {
					List<TimeMgmtWeeks> weeks = member.getWeeks();
					System.out.println(weeks.size() + " ---weeks");
					if (weeks != null) {
						for (TimeMgmtWeeks week : weeks) {

							if (week.getStatus() == 3) {
								week.setStatus(4);
								timeMgmtTimeSheetEntryRepository.save(member);
								autoApprove(member.getLead(), member.getEmail(),
										member.getFirstName() + " " + member.getLastName(),
										member.getFirstName() + " " + member.getLastName(), member.getEmpId(),
										week.getWeekNo(), week.getStartDate(), week.getEndDate());
								System.out.println(week.getEndDate() + " " + member.getFirstName() + " saved");

							}
						}
					}

				}
			}

		} catch (Exception e) {
			System.out.print(e);
		}

	}

	
	@SuppressWarnings("deprecation")
	public void getReport(TimeMgmtTimesheetEntry user, TimeMgmtTimesheetEntry member, String project, Date startDate,
			Date endDate, String uAccount, List<TimeMgmtReportData> reports) throws ParseException {
		final String cryptoKey = secretkey;
		Query query2 = new Query();
		query2.addCriteria(Criteria.where(MANAGER_EMAILID).regex(user.getEmail(), "i"));
		TimeMgmtUserHierarchy userHierarchy = mongoOperation.findOne(query2, TimeMgmtUserHierarchy.class);

		String rate = null;
		if (member.getRole().equalsIgnoreCase(LEAD)) {
			for (LeadDetailData lead : userHierarchy.getTeams()) {
				if (lead.getEmailId().equalsIgnoreCase(member.getEmail())) {
					rate = AESEncryptDecryptUtil.decrypt(lead.getRate(), cryptoKey);

				}
			}

		} else if (member.getRole().equalsIgnoreCase(USER)) {
			for (LeadDetailData lead : userHierarchy.getTeams()) {
				if (lead.getEmailId().equalsIgnoreCase(member.getLead())) {

					for (UserDetailData teamMember : lead.getMembers()) {
						if (teamMember.getEmailId().equalsIgnoreCase(member.getEmail())) {
							rate = AESEncryptDecryptUtil.decrypt(teamMember.getRate(), cryptoKey);

						}
					}
				}
			}
		}

		Query query3 = new Query();

		logger.info("getholidays");
		query3.addCriteria(Criteria.where(LOCATION).regex(member.getLocation(), "i"));
		HolidayList holidayList = mongoOperation.findOne(query3, HolidayList.class);

		double totalHours = 0;
		TimeMgmtReportData report = new TimeMgmtReportData();

		List<TimeMgmtWeekday> hours = new ArrayList<TimeMgmtWeekday>();
		int noOfLeaves = 0;
		int noOfHolidays = 0;
		int totalNoOfDays = 0;
		int noOfWorkingDays = 0;
		List<TimeMgmtWeeks> weeks = member.getWeeks();
		int currentWeek=0;
		int aWeeks=0;
		if (weeks != null) {
			List<Double> weekHours = new ArrayList<Double>();

			for (TimeMgmtWeeks week : weeks) {
				if (week != null) {
					boolean flag = false;

					if (week.getStatus() == 4 || week.getStatus()==2) {
						List<String> leaves = week.getLeave();
						TimeMgmtDays days = week.getDays();
						double weekHour = 0;

						for (TimeMgmtWeekday monday : days.getMonday()) {
							Date date = sdf.parse(monday.getDate());
							if (monday.getProjectName().equalsIgnoreCase(project)
									&& ((date.equals(startDate) || date.after(startDate))
											&& (date.equals(endDate) || date.before(endDate)))) {
								TimeMgmtWeekday day = new TimeMgmtWeekday(monday.getProjectName(), monday.getDate(),
										"Mon", Double.toString(monday.getHours()));
								for (TimeMgmtDates holiday : holidayList.getDates()) {
									if (sdf.parse(holiday.getDate()).equals(date)) {
										day.setHoursPerDay("H");
										noOfHolidays++;
									}
								}

								if (leaves != null) {
									for (String leave : leaves) {
										Date monLeave = sdf.parse(leave);
										if (monLeave.equals(date)) {
											day.setHoursPerDay("L");
											noOfLeaves++;
										}
									}
								}
								totalNoOfDays++;
								hours.add(day);
								flag = true;
								weekHour += monday.getHours();
								totalHours += monday.getHours();
								if(week.getStatus()==4) {
									if(currentWeek!=week.getWeekNo()) {
										aWeeks++;
										currentWeek=week.getWeekNo();
									}
								}

							}

						}
						for (TimeMgmtWeekday tuesday : days.getTuesday()) {
							Date date = sdf.parse(tuesday.getDate());

							if (tuesday.getProjectName().equalsIgnoreCase(project)
									&& ((date.equals(startDate) || date.after(startDate))
											&& (date.equals(endDate) || date.before(endDate)))) {
								TimeMgmtWeekday day = new TimeMgmtWeekday(tuesday.getProjectName(), tuesday.getDate(),
										"Tue", Double.toString(tuesday.getHours()));
								for (TimeMgmtDates holiday : holidayList.getDates()) {
									if (sdf.parse(holiday.getDate()).equals(date)) {
										day.setHoursPerDay("H");
										noOfHolidays++;
									}
								}

								if (leaves != null) {
									for (String leave : leaves) {
										Date tueLeave = sdf.parse(leave);
										if (tueLeave.equals(date)) {
											day.setHoursPerDay("L");
											noOfLeaves++;
										}
									}
								}
								totalNoOfDays++;
								hours.add(day);
								flag = true;
								weekHour += tuesday.getHours();
								totalHours += tuesday.getHours();
								if(week.getStatus()==4) {
									if(currentWeek!=week.getWeekNo()) {
										aWeeks++;
										currentWeek=week.getWeekNo();
									}
								}
							}

						}
						for (TimeMgmtWeekday wednesday : days.getWednesday()) {

							Date date = sdf.parse(wednesday.getDate());
							if (wednesday.getProjectName().equalsIgnoreCase(project)
									&& ((date.equals(startDate) || date.after(startDate))
											&& (date.equals(endDate) || date.before(endDate)))) {
								TimeMgmtWeekday day = new TimeMgmtWeekday(wednesday.getProjectName(),
										wednesday.getDate(), "Wed", Double.toString(wednesday.getHours()));
								for (TimeMgmtDates holiday : holidayList.getDates()) {
									if (sdf.parse(holiday.getDate()).equals(date)) {
										day.setHoursPerDay("H");
										noOfHolidays++;
									}
								}

								if (leaves != null) {
									for (String leave : leaves) {
										Date wedLeave = sdf.parse(leave);
										if (wedLeave.equals(date)) {
											day.setHoursPerDay("L");
											noOfLeaves++;
										}
									}
								}
								totalNoOfDays++;
								hours.add(day);
								flag = true;
								weekHour += wednesday.getHours();
								totalHours += wednesday.getHours();
								if(week.getStatus()==4) {
									if(currentWeek!=week.getWeekNo()) {
										aWeeks++;
										currentWeek=week.getWeekNo();
									}
								}
							}

						}
						for (TimeMgmtWeekday thursday : days.getThursday()) {

							Date date = sdf.parse(thursday.getDate());
							if (thursday.getProjectName().equalsIgnoreCase(project)
									&& ((date.equals(startDate) || date.after(startDate))
											&& (date.equals(endDate) || date.before(endDate)))) {
								TimeMgmtWeekday day = new TimeMgmtWeekday(thursday.getProjectName(), thursday.getDate(),
										"Thu", Double.toString(thursday.getHours()));
								for (TimeMgmtDates holiday : holidayList.getDates()) {
									if (sdf.parse(holiday.getDate()).equals(date)) {
										day.setHoursPerDay("H");
										noOfHolidays++;
									}
								}

								if(leaves != null) {
								for (String leave : leaves) {
									Date thurLeave = sdf.parse(leave);
									if (thurLeave.equals(date)) {
										day.setHoursPerDay("L");
										noOfLeaves++;
									}
								}
								}
								totalNoOfDays++;
								hours.add(day);
								flag = true;
								weekHour += thursday.getHours();
								totalHours += thursday.getHours();
								if(week.getStatus()==4) {
									if(currentWeek!=week.getWeekNo()) {
										aWeeks++;
										currentWeek=week.getWeekNo();
									}
								}
							}

						}
						for (TimeMgmtWeekday friday : days.getFriday()) {

							Date date = sdf.parse(friday.getDate());

							if (friday.getProjectName().equalsIgnoreCase(project)
									&& ((date.equals(startDate) || date.after(startDate))
											&& (date.equals(endDate) || date.before(endDate)))) {
								TimeMgmtWeekday day = new TimeMgmtWeekday(friday.getProjectName(), friday.getDate(),
										"Fri", Double.toString(friday.getHours()));
								for (TimeMgmtDates holiday : holidayList.getDates()) {
									if (sdf.parse(holiday.getDate()).equals(date)) {
										day.setHoursPerDay("H");
										noOfHolidays++;
									}
								}

								if (leaves != null) {
									for (String leave : leaves) {
										Date friLeave = sdf.parse(leave);
										if (friLeave.equals(date)) {
											day.setHoursPerDay("L");
											noOfLeaves++;
										}
									}
								}
								totalNoOfDays++;
								hours.add(day);
								flag = true;
								weekHour += friday.getHours();
								totalHours += friday.getHours();
								if(week.getStatus()==4) {
									if(currentWeek!=week.getWeekNo()) {
										aWeeks++;
										currentWeek=week.getWeekNo();
									}
								}
							}

						}

						if (flag) {

							weekHours.add(weekHour);
						}
					}
				}
			}
			noOfWorkingDays = totalNoOfDays - (noOfHolidays + noOfLeaves);
			report.setNoOfWorkingDays(noOfWorkingDays);

			report.setWeeks(weekHours);

			report.setIsBillable(member.getBillable());
			report.setAccount(uAccount);
			report.setBillRate(rate);
			report.setEmpId(member.getEmpId());
			report.setAmount(Double.toString(totalHours * Integer.parseInt(rate)));
			report.setEmpName(member.getFirstName() + " " + member.getLastName());
			report.setHours(hours);
			report.setLocation(member.getLocation());
			report.setProject(project);
			report.setTotalHours(totalHours);
			report.setNoOfLeaves(noOfLeaves);
//			boolean leapYear=checkYear(startDate.getYear()+1900);
			
			if (startDate.getMonth() + 1 == 2) {
				
				if (!report.getHours().isEmpty() && (report.getWeeks().size() == 5 || report.getWeeks().size() == 4 && aWeeks>=3)) {
					reports.add(report);
					System.out.println("printing weeks"+aWeeks);
				}
				System.out.println("this is february month=");
				

			}
//			else if (startDate.getMonth() + 1 == 2&&!(leapYear)) {
//				
//				if (!report.getHours().isEmpty() && (report.getWeeks().size() == 5 || report.getWeeks().size() == 4 && aWeeks>=3)) {
//					reports.add(report);
//					System.out.println("printing weeks"+aWeeks);
//				}
//				System.out.println("this is february month=");
//				
//
//			}
			else if (!report.getHours().isEmpty() && report.getWeeks().size() == 5&&aWeeks>=4) {
				reports.add(report);
				System.out.println("printing weeks"+aWeeks);
			}

			report = null;
		}

	}
//	 boolean checkYear(int year)
//    {
//        if (year % 400 == 0)
//            return true;
//     
//     
//        if (year % 100 == 0)
//            return false;
//     
//  
//        if (year % 4 == 0)
//            return true;
//        return false;
//    }

	@PostMapping("/showReport")
	public List<TimeMgmtReportData> showReport(@RequestBody TimeMgmtWeekDataVO monthData) throws ParseException {
		logger.info("inside showReport");

		List<TimeMgmtReportData> reports = new ArrayList<TimeMgmtReportData>();

		Date startDate = sdf.parse(monthData.getStartDate());
		Date endDate = sdf.parse(monthData.getEndDate());
		Query query1 = new Query();
		query1.addCriteria(Criteria.where(EMAIL).regex(monthData.getEmail(), "i"));
		TimeMgmtTimesheetEntry user = mongoOperation.findOne(query1, TimeMgmtTimesheetEntry.class);
		List<String> projects = user.getProjects();

		List<String> accounts = user.getAccounts();
		Query query = new Query();
		try {

			logger.info(monthData.getAccount() + monthData.getProject());

			if (monthData.getRole().equalsIgnoreCase(LEAD)) {

				query.addCriteria(Criteria.where(LEAD).regex(monthData.getEmail(), "i"));

			} else if (monthData.getRole().equalsIgnoreCase(MANAGER)) {

				query.addCriteria(Criteria.where(MANAGER).regex(monthData.getEmail(), "i"));
			}
			List<TimeMgmtTimesheetEntry> members = mongoOperation.find(query, TimeMgmtTimesheetEntry.class);

			if (monthData.getAccount().trim().equalsIgnoreCase("all")
					&& monthData.getProject().trim().equalsIgnoreCase("all")) {
				logger.info("all---all 1");

				if (members != null) {

					for (String account : accounts) {
						for (String project : projects) {
							logger.info(project);
							for (TimeMgmtTimesheetEntry member : members) {

								for (String uAccount : member.getAccounts()) {
									if (account.equalsIgnoreCase(uAccount)) {
										getReport(user, member, project, startDate, endDate, uAccount, reports);
									}

								}
							}

						}
					}
				}

			} else if (monthData.getAccount().trim().equalsIgnoreCase("all")
					&& !monthData.getProject().trim().equalsIgnoreCase("all")) {
				logger.info("all-----project 2");
				if (members != null) {
					for (String account : accounts) {
						String project = monthData.getProject();

						for (TimeMgmtTimesheetEntry member : members) {
							for (String uAccount : member.getAccounts()) {
								if (account.equalsIgnoreCase(uAccount)) {
									getReport(user, member, project, startDate, endDate, member.getAccounts().get(0),
											reports);
								}
							}

						}

					}
				}
			} else if (!(monthData.getAccount().trim().equalsIgnoreCase("all"))
					&& (monthData.getProject().trim().equalsIgnoreCase("all"))) {
				logger.info("account------all3");
				List<AccountInformation> accountInfo = user.getAccountInfo();

				List<String> accountInfoProjects = null;
				for (AccountInformation uaccountInfo : accountInfo) {

					if (uaccountInfo.getAccount().equalsIgnoreCase(monthData.getAccount())) {

						accountInfoProjects = uaccountInfo.getProjects();
					}

				}
				for (String project : accountInfoProjects) {
					for (TimeMgmtTimesheetEntry member : members) {
						for (String uAccount : member.getAccounts()) {
							if (monthData.getAccount().equalsIgnoreCase(uAccount)) {
								getReport(user, member, project, startDate, endDate, uAccount, reports);

							}
						}

					}

				}

			} else if (!monthData.getAccount().trim().equalsIgnoreCase("all")
					&& !monthData.getAccount().trim().equalsIgnoreCase("all")) {
				logger.info("account-----project 4");
				if (members != null) {
					String account = monthData.getAccount();
					String project = monthData.getProject();
					for (TimeMgmtTimesheetEntry member : members) {

						for (String uAccount : member.getAccounts()) {

							if (account.equalsIgnoreCase(uAccount)) {
								getReport(user, member, project, startDate, endDate, uAccount, reports);

							}
						}

					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return reports;

	}
	@PostMapping("/timesheetDetail")
	public TimeMgmtTimesheetData getTimesheetDetails(@RequestBody TimesheetDetail timesheetDetail) {
		Query query = new Query();
		Query query1 = new Query();

		TimeMgmtTimesheetEntry weeklyData = new TimeMgmtTimesheetEntry();
		TimeMgmtTimesheetData finalData = new TimeMgmtTimesheetData();
		List<TimeMgmtTimesheetWeeklyData> weeklyDataList = new ArrayList<>();
		TimeMgmtUserDataVO userData = new TimeMgmtUserDataVO();

		try {

			// validating depending on role
			logger.info("email:" + timesheetDetail.getEmail() + " leadMail:" + timesheetDetail.getLeadEmail());

			if (timesheetDetail.getRole().equalsIgnoreCase(LEAD)) {
				query.addCriteria(Criteria.where(EMAIL).regex(timesheetDetail.getEmail(), "i")
						.andOperator(Criteria.where(LEAD).regex(timesheetDetail.getLeadEmail(), "i")));
				logger.info("lead is accessing user");
			} else if (timesheetDetail.getRole().equalsIgnoreCase(MANAGER)) {
				query.addCriteria(Criteria.where(EMAIL).regex(timesheetDetail.getEmail(), "i")
						.andOperator(Criteria.where(MANAGER).regex(timesheetDetail.getLeadEmail(), "i")));
				logger.info("manager is accessing user/lead");
			} else if (timesheetDetail.getRole().equalsIgnoreCase(MANAGER)) {
				query.addCriteria(Criteria.where(EMAIL).regex(timesheetDetail.getEmail(), "i"));
				logger.info("manager");
			} else if (timesheetDetail.getRole().equalsIgnoreCase(LEAD)) {
				query.addCriteria(Criteria.where(EMAIL).regex(timesheetDetail.getEmail(), "i"));
				logger.info("lead");
			} else if (timesheetDetail.getRole().equalsIgnoreCase(USER)) {
				query.addCriteria(Criteria.where(EMAIL).regex(timesheetDetail.getEmail(), "i"));
				logger.info("user");
			}

			weeklyData = mongoOperation.findOne(query, TimeMgmtTimesheetEntry.class);
			List<TimeMgmtDates> holiday = getHolidays(weeklyData.getLocation(), timesheetDetail.getStartDate(),
					timesheetDetail.getEndDate());

			if ((timesheetDetail.getEmail() != "" || timesheetDetail.getEmail() != null)
					&& timesheetDetail.getWeekNo() != 0) {
				if (weeklyData.getEmail().equalsIgnoreCase(timesheetDetail.getEmail())) {
					logger.info("setting data for the first input");
					if(weeklyData.getProjects().size()==1)
					{
						TimeMgmtTimesheetWeeklyData weekdata = new TimeMgmtTimesheetWeeklyData(weeklyData.getProjects().get(0),
								8.0, 8.0, 8.0,8.0,8.0, 1);
						weeklyDataList.add(weekdata);
						
					}
					else
					{
						for (String project : weeklyData.getProjects()) {
							TimeMgmtTimesheetWeeklyData weekdata = new TimeMgmtTimesheetWeeklyData(project, 0.0, 0.0, 0.0,
								0.0, 0.0, 1);
							weeklyDataList.add(weekdata);
					}
					}
					userData = new TimeMgmtUserDataVO(weeklyData.getEmail(), weeklyData.getFirstName(),
							weeklyData.getLastName(), weeklyData.getLead(), weeklyData.getManager(),
							weeklyData.getVendor(), weeklyData.getRole(), weeklyData.getAccounts(),
							weeklyData.getLeadFirstName(), weeklyData.getLeadLastName(), "");

				}

				for (TimeMgmtWeeks data : weeklyData.getWeeks()) {
					logger.info("retriving data form the db");

					if ( data.getWeekNo() == timesheetDetail.getWeekNo()
							&& data.getStartDate().equalsIgnoreCase(timesheetDetail.getStartDate())
							&& data.getEndDate().equalsIgnoreCase(timesheetDetail.getEndDate())) {
						logger.info("End Date" + data.getEndDate());
						logger.info("Start Date" + data.getStartDate());
						logger.info("Week No" + data.getWeekNo());
						for (TimeMgmtTimesheetWeeklyData weekList : weeklyDataList) {
							for (TimeMgmtWeekday monday : data.getDays().getMonday()) {
								if (weekList.getProject().equalsIgnoreCase(monday.getProjectName())) {
									weekList.setMonday(monday.getHours());
								}
							}
							for (TimeMgmtWeekday tuesday : data.getDays().getTuesday()) {
								if (weekList.getProject().equalsIgnoreCase(tuesday.getProjectName())) {
									weekList.setTuesday(tuesday.getHours());
								}
							}
							for (TimeMgmtWeekday wednesday : data.getDays().getWednesday()) {
								if (weekList.getProject().equalsIgnoreCase(wednesday.getProjectName())) {
									weekList.setWednesday(wednesday.getHours());
								}
							}
							for (TimeMgmtWeekday thursday : data.getDays().getThursday()) {
								if (weekList.getProject().equalsIgnoreCase(thursday.getProjectName())) {
									weekList.setThursday(thursday.getHours());
								}
							}
							for (TimeMgmtWeekday friday : data.getDays().getFriday()) {
								if (weekList.getProject().equalsIgnoreCase(friday.getProjectName())) {
									weekList.setFriday(friday.getHours());
								}
							}
							weekList.setStatus(data.getStatus());
						}

						if (data.getLeave() != null) {
							for (int i = 0; i < data.getLeave().size(); i++) {
								TimeMgmtDates leave = new TimeMgmtDates(data.getLeave().get(i), "Leave");
								holiday.add(leave);
							}
						}

						query1.addCriteria(Criteria.where(EMAIL).is(data.getSubmittedBy().toLowerCase().trim()));
						TimeMgmtTimesheetEntry submittedBy = mongoOperation.findOne(query1,
								TimeMgmtTimesheetEntry.class);
						userData.setSubmittedBy(submittedBy.getFirstName() + " " + submittedBy.getLastName());

					}
				}

				finalData.setProjects(weeklyDataList);
				finalData.setUserData(userData);
				finalData.setHoliday(holiday);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info("Exitting getTimesheetDetails");
		return finalData;

	}

	@PostMapping("/submitTimesheetDetails")
	public String submitTimesheetDetails(@RequestBody TimeMgmtSubmitDataVO submitData) {
		Query query = new Query();
		JSONObject tmResponse = new JSONObject();
		boolean weekExists = false;
		int weekIndex = 0;
		Update sUpdate = new Update();
		try {
			System.out.print(submitData.getEmail());
			query.addCriteria(Criteria.where(EMAIL).regex(submitData.getEmail(), "i"));
			logger.info("query");
			TimeMgmtTimesheetEntry tmdata = mongoOperation.findOne(query, TimeMgmtTimesheetEntry.class);
			System.out.print(tmdata);
			if (submitData.getWeekNo() != 0 && submitData.getStatus() != 0 && submitData.getDays() != null) 
			{
				UpdateResult resultFromDb = null ;
				weekIndex = tmdata.getWeeks().size();
				for (int j = 0; j < tmdata.getWeeks().size(); j++) {
					TimeMgmtWeeks weekdata = tmdata.getWeeks().get(j);
					if (submitData.getWeekNo() == weekdata.getWeekNo()
							&& submitData.getStartDate().equalsIgnoreCase(weekdata.getStartDate())
							&& submitData.getEndDate().equalsIgnoreCase(weekdata.getEndDate())) {
						logger.info("Update the existing week data" + j);
						sUpdate.set(WEEKS + j + WEEKNO, submitData.getWeekNo());
						sUpdate.set(WEEKS + j + STATUS, submitData.getStatus()); 
						sUpdate.set(WEEKS + j + ".submittedBy", submitData.getSubmittedBy());
						sUpdate.set(WEEKS + j + ".submittedByRole", submitData.getSubmittedByRole());
						sUpdate.set(WEEKS + j + ".startDate", submitData.getStartDate());
						sUpdate.set(WEEKS + j + ".endDate", submitData.getEndDate());
						sUpdate.set(WEEKS + j + DAYS, submitData.getDays());
						sUpdate.set(WEEKS + j + ".leave", submitData.getLeave());
						weekExists = true;
					}
				}
				if (!weekExists) {
					sUpdate.set(WEEKS + weekIndex + WEEKNO, submitData.getWeekNo());
					sUpdate.set(WEEKS + weekIndex + STATUS, submitData.getStatus());
					sUpdate.set(WEEKS + weekIndex + ".submittedBy", submitData.getSubmittedBy());
					sUpdate.set(WEEKS + weekIndex + ".submittedByRole", submitData.getSubmittedByRole());
					sUpdate.set(WEEKS + weekIndex + ".startDate", submitData.getStartDate());
					sUpdate.set(WEEKS + weekIndex + ".endDate", submitData.getEndDate());
					sUpdate.set(WEEKS + weekIndex + DAYS, submitData.getDays());
					sUpdate.set(WEEKS + weekIndex + ".leave", submitData.getLeave());

					
				}

				resultFromDb = mongoOperation.upsert(query, sUpdate, TimeMgmtTimesheetEntry.class);
				logger.info("ExecutionResult Updated Successfully :: " + resultFromDb);

			
				if (resultFromDb.wasAcknowledged()) {
					if (submitData.getStatus() == 2) {
						tmResponse.put("RESULT", "Timesheet Saved Successfully");
						tmResponse.put("VALUE", true);
					} else if (submitData.getStatus() == 3) {
						tmResponse.put("RESULT", "Timesheet Submitted Successfully");
						tmResponse.put("VALUE", true);
					}
					
					tmdata = mongoOperation.findOne(query, TimeMgmtTimesheetEntry.class);
					for (TimeMgmtWeeks tmweeks : tmdata.getWeeks()) {
						if (submitData.getWeekNo() == tmweeks.getWeekNo()
								&& submitData.getStartDate().equalsIgnoreCase(tmweeks.getStartDate())
								&& submitData.getEndDate().equalsIgnoreCase(tmweeks.getEndDate()))
						{
							logger.info("Status after updating db" + tmweeks.getStatus());
							if (submitData.getStatus() == 3) {
								sendSubmissionEmailNotification(submitData.getEmail(), submitData.getWeekNo(),
										submitData.getStartDate(), submitData.getEndDate());
							}
						}
					}
				} else {
					tmResponse.put("RESULT", "Timesheet updation failed");
					tmResponse.put("VALUE", false);
				}
			}

		} catch (Exception ex) {
			logger.error("Exception caught in submitTimeSheetDetails()" + ex);
			tmResponse.put("RESULT", "Oops!... Something Went Wrong");
			tmResponse.put("VALUE", false);
		}
		return tmResponse.toString();
	}

	@GetMapping("/weekStatus/{email}")
	public List<TimeMgmtTimesheetWeekStatusVO> getWeekStatus(@PathVariable("email") String email) {

		logger.info("Inside getWeekStatus in TimeMgmtTimesheetRepositoryImpl");
		List<TimeMgmtTimesheetWeekStatusVO> weekStatusList = new ArrayList<>();
		System.out.print(email);
		Query query = new Query();

		try {
			query.addCriteria(Criteria.where(EMAIL).regex(email, "i"));

			TimeMgmtTimesheetEntry weeklyData = mongoOperation.findOne(query, TimeMgmtTimesheetEntry.class);

			for (TimeMgmtWeeks data : weeklyData.getWeeks()) {

					TimeMgmtTimesheetWeekStatusVO statusVO = new TimeMgmtTimesheetWeekStatusVO();
					statusVO.setStartDate(data.getStartDate());
					statusVO.setStatus(data.getStatus());
					statusVO.setEndDate(data.getEndDate());
					statusVO.setWeekNo(data.getWeekNo());
					weekStatusList.add(statusVO);
				} 
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		logger.info("Exitting getWeekStatus in TimeMgmtTimesheetRepositoryImpl");
		return weekStatusList;
	}

	@GetMapping("/approveTimesheetDetails")
	public String approveTimesheetDetails(@RequestParam String email, @RequestParam Integer weekNo,
			@RequestParam String startDate, @RequestParam String endDate, @RequestParam String approverRole) {
		Query query = new Query();
		String msg;
		System.out.print(email);
		query.addCriteria(Criteria.where(EMAIL).regex(email, "i"));
		TimeMgmtTimesheetEntry data = mongoOperation.findOne(query, TimeMgmtTimesheetEntry.class);
		for (TimeMgmtWeeks tmweeks : data.getWeeks()) {
			if (weekNo == tmweeks.getWeekNo() && startDate.equalsIgnoreCase(tmweeks.getStartDate())
					&& endDate.equalsIgnoreCase(tmweeks.getEndDate())) {
				if (tmweeks.getStatus() == 3) {
					tmweeks.setStatus(4);
					timeMgmtTimeSheetEntryRepository.save(data);
					logger.info("Status after updating db " + tmweeks.getStatus());
					msg = "Thank You for Approving the TimeSheet of " + data.getFirstName() + " " + data.getLastName();
					approveEmailNotification(email, weekNo, startDate, endDate, approverRole);// mail send
					return msg;
				} else if (tmweeks.getStatus() == 4) {
					msg = "The  TimeSheet of " + data.getFirstName() + " " + data.getLastName() + " already Approved";
					return msg;

				}
			}
		}

		msg = "Opp's Something Went Wrong";
		return msg;

	}

	@GetMapping("/rejectTimesheetDetails")
	public String rejectTimesheetDetails(@RequestParam String email, @RequestParam Integer weekNo,
			@RequestParam String startDate, @RequestParam String endDate) {
		Query query = new Query();
		System.out.print(email);
		String msg;

		query.addCriteria(Criteria.where(EMAIL).regex(email, "i"));

		TimeMgmtTimesheetEntry data = mongoOperation.findOne(query, TimeMgmtTimesheetEntry.class);
		for (TimeMgmtWeeks tmweeks : data.getWeeks()) {
			if (weekNo == tmweeks.getWeekNo() && startDate.equalsIgnoreCase(tmweeks.getStartDate())
					&& endDate.equalsIgnoreCase(tmweeks.getEndDate())) {
				if (tmweeks.getStatus() == 3) {
					tmweeks.setStatus(5);
					tmweeks.setLeave(null);
					timeMgmtTimeSheetEntryRepository.save(data);
					logger.info("Status after updating db " + tmweeks.getStatus());
					msg = "You have Rejected the TimeSheet of " + data.getFirstName() + " " + data.getLastName();
					rejectEmailNotification(email, weekNo, startDate, endDate);
					return msg;
				} else if (tmweeks.getStatus() == 4) {
					msg = "The  TimeSheet of " + data.getFirstName() + " " + data.getLastName() + " already Approved";
					return msg;
				} else if (tmweeks.getStatus() == 5) {
					msg = "The  TimeSheet of " + data.getFirstName() + " " + data.getLastName() + " already Rejected";
					return msg;
				}
			}

		}
		msg = "Opps... Something Went Wrong";
		return msg;
	}

	@GetMapping("/revokeTimesheetDetails")
	public String revokeTimesheetDetails(@RequestParam String email, @RequestParam Integer weekNo,
			@RequestParam String startDate, @RequestParam String endDate) {
		Query query = new Query();
		System.out.print(email);
		String msg;
		query.addCriteria(Criteria.where(EMAIL).regex(email, "i"));

		TimeMgmtTimesheetEntry data = mongoOperation.findOne(query, TimeMgmtTimesheetEntry.class);
		for (TimeMgmtWeeks tmweeks : data.getWeeks()) {
			if (weekNo == tmweeks.getWeekNo() && startDate.equalsIgnoreCase(tmweeks.getStartDate())
					&& endDate.equalsIgnoreCase(tmweeks.getEndDate()) && tmweeks.getStatus() == 4) {

				tmweeks.setStatus(6);
				tmweeks.setLeave(null);
				timeMgmtTimeSheetEntryRepository.save(data);
				logger.info("Status after updating db " + tmweeks.getStatus());
				msg = "You have Revoked the TimeSheet of " + data.getFirstName() + " " + data.getLastName();
//				revokeEmailNotification(email, weekNo, startDate, endDate);
				return msg;
			}
		}
		msg = "Opps... Something Went Wrong";
		return msg;
	}

	@PostMapping("/selectedWeek")
	public List<TimeMgmtSubmittedDataVO> getMembers(@RequestBody TimeMgmtWeekDataVO weekData) {
		List<TimeMgmtTimesheetEntry> timesheetEntry = new ArrayList<>();
		List<TimeMgmtSubmittedDataVO> members = new ArrayList<>();
		TimeMgmtTimesheetEntry timeSheetEntry1 = new TimeMgmtTimesheetEntry();
		TimeMgmtSubmittedDataVO member = null;

		Query query = new Query();

		try {
			logger.info("in selectedWeek");

			if (weekData.getRole().equalsIgnoreCase(LEAD)) {

				query.addCriteria(Criteria.where(LEAD).regex(weekData.getEmail(), "i"));

			} else if (weekData.getRole().equalsIgnoreCase(MANAGER)) {

				query.addCriteria(Criteria.where(MANAGER).regex(weekData.getEmail(), "i"));
			}
			timesheetEntry = mongoOperation.find(query, TimeMgmtTimesheetEntry.class);

			System.out.println("all members" + timesheetEntry);
			System.out.println(timesheetEntry.size());
			if (timesheetEntry != null) {

				for (TimeMgmtTimesheetEntry data : timesheetEntry) {

					List<TimeMgmtWeeks> weeks = data.getWeeks();
					List<TimeMgmtWeeks> weeks1 = new ArrayList<TimeMgmtWeeks>();
					for (TimeMgmtWeeks week : weeks) {
						if (week != null) {
							SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
							Date gStartDate = sdf.parse(weekData.getStartDate());
							Date gEndDate = sdf.parse(weekData.getEndDate());
							Date uStartDate = sdf.parse(week.getStartDate());
							Date uEndDate = sdf.parse(week.getEndDate());

							if ((uStartDate.equals(gStartDate) || uStartDate.after(gStartDate))
									& (uEndDate.equals(gEndDate) || uEndDate.before(gEndDate))
									& week.getStatus() == 3) {
								week.setDays(null);
								timeSheetEntry1 = mongoOperation.findById(week.getSubmittedBy(),
										TimeMgmtTimesheetEntry.class);

								week.setSubmittedBy(
										timeSheetEntry1.getFirstName() + " " + timeSheetEntry1.getLastName());

								weeks1.add(week);

								System.out.println(data.getEmail());
								System.out.println(week.getEndDate() + " " + week.getStartDate());

								member = new TimeMgmtSubmittedDataVO(data.getFirstName(), data.getLastName(),
										data.getEmail(), data.getRole(), weeks1);

							}

						}
					}
					members.add(member);
					member = null;

					//
				}
			}
		}

		catch (Exception e) {
			e.getStackTrace();

		}
		List<TimeMgmtSubmittedDataVO> users = new ArrayList<TimeMgmtSubmittedDataVO>();
		for (TimeMgmtSubmittedDataVO user : members) {
			if (user != null) {
				users.add(user);
			}
		}
		return users;
	}

	@PostMapping("/approveAll")
	public String approveAll(@RequestBody List<TimesheetDetail> members) {

		TimeMgmtTimesheetEntry timeSheetEntry = new TimeMgmtTimesheetEntry();
		String msg = null;
		int memberCount = 0;

		try {
			for (TimesheetDetail member : members) {

				Query query = new Query();
				query.addCriteria(Criteria.where(EMAIL).regex(member.getEmail(), "i"));
				timeSheetEntry = mongoOperation.findOne(query, TimeMgmtTimesheetEntry.class);
				if (timeSheetEntry != null) {

					int weekNo = member.getWeekNo();

					for (TimeMgmtWeeks tmweeks : timeSheetEntry.getWeeks()) {
						if (tmweeks != null) {
							SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
							Date gStartDate = sdf.parse(member.getStartDate());
							Date gEndDate = sdf.parse(member.getEndDate());
							Date uStartDate = sdf.parse(tmweeks.getStartDate());
							Date uEndDate = sdf.parse(tmweeks.getEndDate());

							if (weekNo == tmweeks.getWeekNo() && uStartDate.equals(gStartDate)
									&& uEndDate.equals(gEndDate) && tmweeks.getStatus() == 3) {
								memberCount++;
								tmweeks.setStatus(4);
								timeMgmtTimeSheetEntryRepository.save(timeSheetEntry);
								logger.info("Status after updating db " + tmweeks.getStatus());
								approveEmailNotification(member.getEmail(), weekNo, member.getStartDate(),
										member.getEndDate(), member.getApproverRole());

							} else {
								logger.info("status not changed");

							}
						}

					}
				}
			}

			if (memberCount == members.size()) {
				msg = "Thank You for Approving the TimeSheet of all ";

			} else {
				msg = "Opp's Something Went Wrong";
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return msg;
	}

	@PostMapping("/rejectAll")
	public String rejectAll(@RequestBody List<TimesheetDetail> members) {
		TimeMgmtTimesheetEntry data = new TimeMgmtTimesheetEntry();
		String msg = null;

		int MemberCount = 0;

		try {
			for (TimesheetDetail member : members) {
				System.out.println("hi " + member.getEmail());
				Query query = new Query();
				query.addCriteria(Criteria.where(EMAIL).regex(member.getEmail(), "i"));
				data = mongoOperation.findOne(query, TimeMgmtTimesheetEntry.class);

				if (data != null) {

					int weekNo = member.getWeekNo();

					for (TimeMgmtWeeks tmweeks : data.getWeeks())

					{
						SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
						Date gStartDate = sdf.parse(member.getStartDate());
						Date gEndDate = sdf.parse(member.getEndDate());
						Date uStartDate = sdf.parse(tmweeks.getStartDate());
						Date uEndDate = sdf.parse(tmweeks.getEndDate());
						if (weekNo == tmweeks.getWeekNo() && uStartDate.equals(gStartDate) && uEndDate.equals(gEndDate)
								&& tmweeks.getStatus() == 3) {
							MemberCount++;

							tmweeks.setStatus(5);
							tmweeks.setLeave(null);
							timeMgmtTimeSheetEntryRepository.save(data);
							logger.info("Status after updating db " + tmweeks.getStatus());

							rejectEmailNotification(member.getEmail(), weekNo, member.getStartDate(),
									member.getEndDate());

						} else {
							logger.info("status not changed");

						}

					}
				}
			}
			if (MemberCount == members.size()) {
				msg = "You have Rejected the TimeSheet of all selected members.";
			} else {
				msg = "Opp's Something Went Wrong";
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return msg;

	}

//	@Scheduled(cron="*/10 * * * * *")
	@Scheduled(cron = "59 59 10 * * MON")
	public void SubmitReminder() {
		Calendar calendar = Calendar.getInstance();
		int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
		
		List<TimeMgmtTimesheetEntry> users = timeMgmtTimeSheetEntryRepository.findAll();
		for (TimeMgmtTimesheetEntry user : users) {
			boolean c = false;

			logger.info("checking status for " + user.getFirstName() + " " + user.getLastName());
			
			String subject = "TimeSheet Submission Reminder";
			StringBuilder builder = new StringBuilder();
			builder.append("<div><p style='font-family:calibri;font-size:18;'>Hi <b>" + user.getFirstName() + " "
					+ user.getLastName() + "</b>,</p></div>");

			builder.append(
					"<div><p style='font-family:calibri;font-size:18;'>This is a Gentle reminder for the submission of TimeSheet.<br><br>");

			List<TimeMgmtWeeks> weeks = user.getWeeks();
			for (TimeMgmtWeeks week : weeks) {
				if ((week.getWeekNo() == (weekOfYear - 1)) && (week.getStatus() == 2)) {
					builder.append("Please enter the timesheet for the week ending with <b><strong>" + week.getEndDate()
							+ "</strong></b>");
					c = true;
				}
			}
			builder.append("<br><br>Please Ignore this mail if already filled. <br></p></div>");

			builder.append(EmailUtility.thanks());

			if (c) {
				logger.info("SubmitReminder send to " + user.getFirstName() + " " + user.getLastName());
				EmailUtility.SendEmail(user.getEmail(), subject, builder.toString());
			}

		}

	}

	@Scheduled(cron = "59 59 16 * * FRI")
//	@Scheduled(cron="*/10 * * * * *")
	public void approvalReminder() {
		Calendar c = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
		String date = sdf.format(c.getTime());

		List<TimeMgmtTimesheetEntry> users = timeMgmtTimeSheetEntryRepository.findAll();
		for (TimeMgmtTimesheetEntry user : users) {
			if (user.getRole().equalsIgnoreCase(LEAD) || user.getRole().equalsIgnoreCase(MANAGER)) {
				logger.info("checking status for " + user.getFirstName() + " " + user.getLastName());
				
				String subject = "TimeSheet Approval Reminder";
				StringBuilder builder = new StringBuilder();
				builder.append("<div><p style='font-family:calibri;font-size:18;'>Hi <b>" + user.getFirstName() + " "
						+ user.getLastName() + "</b>,</p></div>");

				builder.append(
						"<div><p style='font-family:calibri;font-size:18;'>This is a Gentle reminder for the Approval of TimeSheet.<br><br>");
				builder.append("Please Approve the timesheet for the week ending with  <b>" + date + "</b>.");

				builder.append("<br><br>Please Ignore this mail if already approved. <br></p></div>");

				builder.append(EmailUtility.thanks());

				EmailUtility.SendEmail(user.getEmail(), subject, builder.toString());

			}
		}

	}
	
	@Scheduled(cron = "0 0 10 28-31 * ?")
//	@Scheduled(cron="*/10 * * * * *")
	public void lastDayOfMonthReminder() 
	{ 
		
		logger.info(" inside lastDayOfMonthReminder---1");
		Calendar c = Calendar.getInstance();
	    LocalDate currentdate = LocalDate.now();
	      
	    if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) 
	    {
			logger.info(" inside lastDayOfMonthReminder----2");
			List<TimeMgmtTimesheetEntry> users = timeMgmtTimeSheetEntryRepository.findAll();
			for (TimeMgmtTimesheetEntry user : users) 
			{
				if (user.getRole().equalsIgnoreCase(LEAD) || user.getRole().equalsIgnoreCase(USER)) {

				logger.info("checking status for " + user.getFirstName() + " " + user.getLastName());
				
				String subject = "TimeSheet Submission Reminder";
				StringBuilder builder = new StringBuilder();
				builder.append("<div><p style='font-family:calibri;font-size:18;'>Hi <b>" + user.getFirstName() + " "
						+ user.getLastName() + "</b>,</p></div>");

				builder.append(
						"<div><p style='font-family:calibri;font-size:18;'>This is a Gentle reminder for the submission of TimeSheet.<br><br>");

			
						builder.append("Please enter the timesheet for the "+currentdate.getMonth()+" Month. <b><strong></strong></b>");
						
				builder.append("<br><br>Please Ignore this mail if already filled. <br></p></div>");

				builder.append(EmailUtility.thanks());

				
					logger.info("SubmitReminder send to " + user.getFirstName() + " " + user.getLastName());
					EmailUtility.SendEmail(user.getEmail(), subject, builder.toString());

			}
		}
	 }
	}
	
	public void autoApprove(String leadEmail, String userEmail, String leadName, String userName, String userEmpId,
			int weekNo, String startDate, String endDate) {
		logger.info("inside Auto  approveEmailNotification");

		String TmLink = "<a href=\"http://172.16.21.12:80/#/login\">";

		String subject = "Auto Approval of TimeSheet - " + userName + "- (" + userEmpId + ")";

		StringBuilder builder = new StringBuilder();
		StringBuilder table = table(userEmail, weekNo, startDate, endDate);

		builder.append(
				"<img data-imagetype=\"External\" src=\"E:\\sts\\TM\\src\\main\\java\\com\\infinite\\tm\" border=\"0\" id=\"x__x0000_i1025\"><br><hr>");
		builder.append("<div><p style='font-family:calibri;font-size:18;'>Hi <b>" + leadName + "</b>,</p></div>");
		builder.append("<div><p style='font-family:calibri;font-size:18;'>The TimeSheet request from <b>" + userName
				+ "- " + userEmpId + "</b> for the week <b>" + startDate + " to " + endDate
				+ "</b> is pending for your approval. The timesheet has been submitted for <b>" + sum + " hours</b>.");

		builder.append(table);

		builder.append(
				"Since the same was not approved at your end, and as per the process defined by Time Managment, system has auto approved it on your behalf.<br>");
		builder.append("<br>Please " + TmLink + "click here</a> to login to the application.</p></div>");

		builder.append(EmailUtility.thanks());

		EmailUtility.SendEmail(leadEmail, userEmail, subject, builder.toString());
	}

	public List<TimeMgmtDates> getHolidays(String location, String startDate, String endDate) {
		Query query = new Query();
		List<TimeMgmtDates> holidays = new ArrayList<>();
		try {

			logger.info("inside getholidays");
			query.addCriteria(Criteria.where(LOCATION).regex(location, "i"));
			HolidayList holidayList = mongoOperation.findOne(query, HolidayList.class);
			logger.info("location: " + holidayList.getLocation() + "\n dates: " + holidayList.getDates());
			for (TimeMgmtDates date : holidayList.getDates()) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				Date startDate_ = sdf.parse(startDate);
				Date endDate_ = sdf.parse(endDate);
				Date cDate = sdf.parse(date.getDate());
				if ((cDate.after(startDate_) || cDate.equals(startDate_))
						&& (cDate.equals(endDate_) || cDate.before(endDate_))) {
					holidays.add(date);
					System.out.println(
							date.getReason() + " " + " added" + ((cDate.after(startDate_) || cDate.equals(startDate_))
									&& (cDate.equals(endDate_) || cDate.before(endDate_))));

				}
				System.out.println(date.getDate() + " " + date.getReason());

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return holidays;

	}

	public StringBuilder table(String email, int weekNo, String startDate, String endDate) {
		Query query = new Query();
		double totalPHr = 0.0;
		double totalHr[] = { 0.0, 0.0, 0.0, 0.0, 0.0 };
		sum = 0.0;
		System.out.print(email);
		query.addCriteria(Criteria.where(EMAIL).regex(email, "i"));
		TimeMgmtTimesheetEntry data = mongoOperation.findOne(query, TimeMgmtTimesheetEntry.class);

		StringBuilder builder = new StringBuilder();

		for (TimeMgmtWeeks weekData : data.getWeeks()) {
			if (weekNo == weekData.getWeekNo() && startDate.equalsIgnoreCase(weekData.getStartDate())
					&& endDate.equalsIgnoreCase(weekData.getEndDate())) {

				builder.append(
						"<div><table border='1' cellpadding='0' cellspacing='0' style='border: 1px solid #bfbfbf; width: 100%; margin-top: 20px; border-collapse: collapse;'>");

				builder.append("<tr style='background: #336ab6; color: #eee; font-family:calibri; font-size:18; '>");
				// header
				builder.append("<th style='padding:5px; width: 130px; height:35px; '> Projects </th>");
				builder.append("<th style='padding:5px; width: 130px; height:35px; '> Monday <br>"
						+ (weekData.getDays().getMonday()).get(0).getDate() + "</th>");
				builder.append("<th style='padding:5px; width: 130px; height:35px; '> Tuesday <br>"
						+ (weekData.getDays().getTuesday()).get(0).getDate() + "</th>");
				builder.append("<th style='padding:5px; width: 130px; height:35px; '> Wednesday <br>"
						+ (weekData.getDays().getWednesday()).get(0).getDate() + "</th>");
				builder.append("<th style='padding:5px; width: 130px; height:35px; '> Thursday <br>"
						+ (weekData.getDays().getThursday()).get(0).getDate() + "</th>");
				builder.append("<th style='padding:5px; width: 130px; height:35px; '> Friday <br>"
						+ (weekData.getDays().getFriday()).get(0).getDate() + "</th>");
				builder.append("<th style='padding:5px; width: 130px; height:35px; '> Total Project Hours </th></tr>");
				logger.info("loop count:" + data.getProjects().size());
				for (int i = 0; i < data.getProjects().size(); i++) {
					builder.append("<tr style='background: #fff; color: black; font-family:calibri; font-size:18; '>");
					builder.append(
							"<td style='padding:5px; text-align:center;'>" + data.getProjects().get(i) + "</td>");
					logger.info(data.getProjects().get(i));
					totalPHr = 0;
					double hr = (weekData.getDays().getMonday()).get(i).getHours();
					builder.append("<td style='padding:5px; text-align:center;'>" + hr + "</td>");
					logger.info("hour:" + hr);
					totalPHr = totalPHr + hr;
					totalHr[0] = totalHr[0] + hr;
					hr = (weekData.getDays().getTuesday()).get(i).getHours();
					logger.info("hour:" + hr);
					builder.append("<td style='padding:5px; text-align:center;'>" + hr + "</td>");
					totalPHr = totalPHr + hr;
					totalHr[1] = totalHr[1] + hr;
					hr = (weekData.getDays().getWednesday()).get(i).getHours();
					builder.append("<td style='padding:5px; text-align:center;'>" + hr + "</td>");
					logger.info("hour:" + hr);
					totalPHr = totalPHr + hr;
					totalHr[2] = totalHr[2] + hr;
					hr = (weekData.getDays().getThursday()).get(i).getHours();
					builder.append("<td style='padding:5px; text-align:center;'>" + hr + "</td>");
					logger.info("hour:" + hr);
					totalPHr = totalPHr + hr;
					totalHr[3] = totalHr[3] + hr;
					hr = (weekData.getDays().getFriday()).get(i).getHours();
					builder.append("<td style='padding:5px; text-align:center;'>" + hr + "</td>");
					logger.info("hour:" + hr);
					totalPHr = totalPHr + hr;
					totalHr[4] = totalHr[4] + hr;
					builder.append("<td style='padding:5px; text-align:center;'>" + totalPHr + "</td></tr>");
					logger.info("total project hours:" + totalPHr);

				}

				// row for total hours
				builder.append("<tr style='background: #fff; color: black; font-family:calibri; font-size:18; '>");
				builder.append("<td style='padding:5px; text-align:center;'>Total Hours</td>");
				builder.append("<td style='padding:5px; text-align:center;'>" + totalHr[0] + "</td>");
				builder.append("<td style='padding:5px; text-align:center;'>" + totalHr[1] + "</td>");
				builder.append("<td style='padding:5px; text-align:center;'>" + totalHr[2] + "</td>");
				builder.append("<td style='padding:5px; text-align:center;'>" + totalHr[3] + "</td>");
				builder.append("<td style='padding:5px; text-align:center;'>" + totalHr[4] + "</td>");
				sum = +totalHr[0] + totalHr[1] + totalHr[2] + totalHr[3] + totalHr[4];
				builder.append("<td style='padding:5px; text-align:center;'>" + sum + "</td></tr></table><div><br>");

				List<TimeMgmtDates> holiday = getHolidays(data.getLocation(), startDate, endDate);
				if (holiday != null) {
					builder.append("<p style='font-family:calibri;font-size:18'>");
					for (TimeMgmtDates tm : holiday) {
						builder.append("<font color='#1fad15'><strong>" + tm.getReason() + "</strong></font> on "
								+ tm.getDate() + "<br>");
					}
					builder.append("</p><br>");
				}
			}

		}
		return builder;

	}

	public void sendSubmissionEmailNotification(String email, int weekNo, String startDate, String endDate) {
		Query query = new Query();

		System.out.print(email);
		query.addCriteria(Criteria.where(EMAIL).regex(email, "i"));
		TimeMgmtTimesheetEntry data = mongoOperation.findOne(query, TimeMgmtTimesheetEntry.class);
		logger.info("inside sendSubmissionEmailNotification");
		String subject = "TM Submission - " + data.getFirstName() + " " + data.getLastName() + "- (" + startDate + " - "
				+ endDate + ")";

		String approveLink = "<a href=\"http://172.16.21.12:80/TM/TimesheetEntry/approveTimesheetDetails?email=" + email
				+ "&weekNo=" + weekNo + "&startDate=" + startDate + "&endDate=" + endDate + "&approverRole=lead\">";
		String rejectLink = "<a href=\"http://172.16.21.12:80/TM/TimesheetEntry/rejectTimesheetDetails?email=" + email
				+ "&weekNo=" + weekNo + "&startDate=" + startDate + "&endDate=" + endDate + "&approverRole=lead\">";

		StringBuilder builder = new StringBuilder();
		StringBuilder table = table(email, weekNo, startDate, endDate);

		builder.append(
				"<img data-imagetype=\"External\" src=\"E:\\sts\\TM\\src\\main\\java\\com\\infinite\\tm\" border=\"0\" id=\"x__x0000_i1025\"><br><hr>");
		builder.append("<div><p style='font-family:calibri;font-size:18;'>Hi <b>" + data.getLeadFirstName() + " "
				+ data.getLeadLastName() + "</b>,</p></div>");

		builder.append("<div><p style='font-family:calibri;font-size:18;'>Please find the timesheet of <b>"
				+ data.getFirstName() + " " + data.getLastName() + " - " + data.getEmpId()
				+ "</b> for the week starting from <b>" + startDate + "</b> to <b>" + endDate + "</b> for " + sum
				+ " hours.</p></div>");

		builder.append(table);

		builder.append(
				"<p style='font-family:calibri;font-size:18;'>Please send your reply for the timesheet request by clicking on "
						+ approveLink + "Approve</a> or " + rejectLink + "Reject</a>. <p>");
		builder.append(EmailUtility.thanks());

		logger.info("html text:  " + builder.toString());

		EmailUtility.SendEmail(data.getLead(), subject, builder.toString());

	}

	public void approveEmailNotification(String email, int weekNo, String startDate, String endDate,
			String approverRole) {
		Query query = new Query();

		System.out.print(email);
		query.addCriteria(Criteria.where(EMAIL).regex(email, "i"));
		TimeMgmtTimesheetEntry data = mongoOperation.findOne(query, TimeMgmtTimesheetEntry.class);
		logger.info("inside approveEmailNotification");
		String subject = "TM Approval  - " + data.getFirstName() + " " + data.getLastName() + "- (" + startDate + " - "
				+ endDate + ")";

		StringBuilder builder = new StringBuilder();
		StringBuilder table = table(email, weekNo, startDate, endDate);
		builder.append(
				"<img data-imagetype=\"External\" src=\"E:\\sts\\TM\\src\\main\\java\\com\\infinite\\tm\" border=\"0\" id=\"x__x0000_i1025\"><br><hr>");
		builder.append("<div><p style='font-family:calibri;font-size:18;'>Hi <b>" + data.getFirstName() + " "
				+ data.getLastName() + "</b>,</p></div>");

		builder.append(
				"<div><p style='font-family:calibri;font-size:18;'>Please find the approved timesheet of :</p></div>");

		builder.append(
				"<div><table border='1' cellpadding='0' cellspacing='0' style='border: 1px solid #bfbfbf; width: 100%; margin-top: 20px; border-collapse: collapse;'>");

		builder.append("<tr style='background: #336ab6; color: #eee; font-family:calibri; font-size:18; '>");
		// header
		builder.append("<th style='padding:5px; width: 130px; height:35px; '> FirstName</th>");
		builder.append("<th style='padding:5px; width: 130px; height:35px; '> LastName </th>");
		builder.append("<th style='padding:5px; width: 130px; height:35px; '> StartDate </th>");
		builder.append("<th style='padding:5px; width: 130px; height:35px; '> EndDate </th>");
		builder.append("<th style='padding:5px; width: 130px; height:35px; '> Approved By</th>");
		builder.append("<th style='padding:5px; width: 130px; height:35px; '> Approved Hours </th></tr>");
		builder.append("<tr style='background: #fff; color: black; font-family:calibri; font-size:18; '>");
		builder.append("<td style='padding:5px; text-align:center;'>" + data.getFirstName() + "</td>");
		builder.append("<td style='padding:5px; text-align:center;'>" + data.getLastName() + "</td>");
		builder.append("<td style='padding:5px; text-align:center;'>" + startDate + "</td>");
		builder.append("<td style='padding:5px; text-align:center;'>" + endDate + "</td>");
		if (LEAD.equalsIgnoreCase(approverRole)) {
			builder.append("<td style='padding:5px; text-align:center;'>" + data.getLeadFirstName() + " "
					+ data.getLeadLastName() + "</td>");
			logger.info("Approved By: " + data.getLeadFirstName() + " " + data.getLeadLastName());
		} else if (MANAGER.equalsIgnoreCase(approverRole)) {
			Query query1 = new Query();
			String mail = data.getManager();
			query1.addCriteria(Criteria.where(EMAIL).regex(mail, "i"));
			TimeMgmtTimesheetEntry manager = mongoOperation.findOne(query1, TimeMgmtTimesheetEntry.class);
			builder.append("<td style='padding:5px; text-align:center;'>" + manager.getFirstName() + " "
					+ manager.getLastName() + "</td>");
			logger.info("Approved By: " + manager.getFirstName() + "" + manager.getLastName());

		}

		builder.append("<td style='padding:5px; text-align:center;'>" + sum + "</td>");

		builder.append(table);

		builder.append(EmailUtility.thanks());

		EmailUtility.SendEmail(email, subject, builder.toString());

	}

	public void rejectEmailNotification(String email, int weekNo, String startDate, String endDate) {
		Query query = new Query();

		System.out.print(email);
		query.addCriteria(Criteria.where(EMAIL).regex(email, "i"));
		TimeMgmtTimesheetEntry data = mongoOperation.findOne(query, TimeMgmtTimesheetEntry.class);
		logger.info("inside approveEmailNotification");
		String subject = "Time Management Rejection Notification";

		StringBuilder builder = new StringBuilder();
		StringBuilder table = table(email, weekNo, startDate, endDate);
		builder.append(
				"<img data-imagetype=\"External\" src=\"E:\\sts\\TM\\src\\main\\java\\com\\infinite\\tm\" border=\"0\" id=\"x__x0000_i1025\"><br><hr>");
		builder.append("<div><p style='font-family:calibri;font-size:18;'>Hi <b>" + data.getFirstName() + " "
				+ data.getLastName() + "</b>,</p></div>");

		builder.append("<div><p style='font-family:calibri;font-size:18;'>Your timesheet for the week starting from "
				+ startDate + " to " + endDate + " has been rejected.</p></div>");

		builder.append(table);
		builder.append("Please contact your lead.");
		builder.append(EmailUtility.thanks());

		EmailUtility.SendEmail(email, subject, builder.toString());
	}

}
