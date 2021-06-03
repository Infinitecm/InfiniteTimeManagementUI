package com.infinite.tm;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.infinite.tm.model.AccountInformation;
import com.infinite.tm.model.HolidayList;
import com.infinite.tm.model.TimeMgmtDates;
import com.infinite.tm.model.User;
import com.infinite.tm.model.repository.AccountInformationRepository;
import com.infinite.tm.model.repository.HolidayListRepository;
import com.infinite.tm.model.repository.TimeMgmtTimeSheetEntryRepository;
import com.infinite.tm.model.repository.TimeMgmtUserHierarchyRepository;
import com.infinite.tm.model.repository.UserRepository;

@Component
public class DnSeeder implements CommandLineRunner {
	
	@Autowired
	TimeMgmtUserHierarchyRepository userHierarchyRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TimeMgmtTimeSheetEntryRepository timeMgmtTimeSheetEntryRepository;
	
	@Autowired
	HolidayListRepository holidayListRepository;	
	
	@Autowired
	AccountInformationRepository accountInformationRepository;


	@Value("${crypto-key}")
	private String secretKey;
	
	
	
	@Override
	public void run(String... args) throws Exception 
	{
		//jan
		TimeMgmtDates newYear = new TimeMgmtDates("01/01/2020", "New Year");
		TimeMgmtDates makarSankranti = new TimeMgmtDates("01/15/2020", "Makar Sankranti");
		TimeMgmtDates pongal = new TimeMgmtDates("01/15/2020", "Pongal");
			//2021
		TimeMgmtDates newYear_21 = new TimeMgmtDates("01/01/2021", "New Year");
		TimeMgmtDates makarSankranti_21 = new TimeMgmtDates("01/14/2021", "Makar Sankranti");
		TimeMgmtDates pongal_21 = new TimeMgmtDates("01/14/2021", "Pongal");
		TimeMgmtDates republicDay_21 = new TimeMgmtDates("01/26/2021", "Republic Day");

				
		//feb
		TimeMgmtDates usPresidentDay = new TimeMgmtDates("02/17/2020", "US President's Day");
		TimeMgmtDates mahaShivratri = new TimeMgmtDates("02/21/2020", "Maha Shivratri");
			//2021
		TimeMgmtDates usPresidentDay_21 = new TimeMgmtDates("02/17/2021", "US President's Day");

		
		//march
		TimeMgmtDates holi = new TimeMgmtDates("03/10/2020", "Holi");
		TimeMgmtDates teluguNewYear = new TimeMgmtDates("03/25/2020", "Telugu New Year");
		TimeMgmtDates ugadi = new TimeMgmtDates("03/25/2020", "Ugadi");
			//2021
		TimeMgmtDates mahaShivratri_21 = new TimeMgmtDates("03/21/2021", "Maha Shivratri");
		TimeMgmtDates holi_21 = new TimeMgmtDates("03/29/2021", "Holi");


		
		//april
		TimeMgmtDates goodFriday = new TimeMgmtDates("04/10/2020", "Good Friday");
		TimeMgmtDates tamilNewYear = new TimeMgmtDates("04/14/2020", "Tamil New Year");
			//2021
		TimeMgmtDates goodFriday_21 = new TimeMgmtDates("04/02/2021", "Good Friday");
		TimeMgmtDates ugadi_21 = new TimeMgmtDates("04/13/2021", "Ugadi");
		TimeMgmtDates tamilNewYear_21 = new TimeMgmtDates("04/14/2021", "Tamil New Year");
		TimeMgmtDates telguNewYear_21 = new TimeMgmtDates("04/13/2021", "Telugu New Year");




		//may
		TimeMgmtDates mayDay = new TimeMgmtDates("05/01/2020", "May Day");
			//2021
		TimeMgmtDates mayDay_21 = new TimeMgmtDates("05/01/2021", "May Day");
		TimeMgmtDates memorialDay_21 = new TimeMgmtDates("05/25/2021", "Memorial Day");


		
		//june
		
		//july
		TimeMgmtDates USIndependenceDay = new TimeMgmtDates("07/03/2020", "US Independence Day");
		TimeMgmtDates Bakrid = new TimeMgmtDates("07/31/2020", "Bakrid");
			//2021
		TimeMgmtDates Bakrid_21 = new TimeMgmtDates("07/21/2021", "Bakrid");
		TimeMgmtDates USIndependenceDay_21 = new TimeMgmtDates("07/03/2021", "US Independence Day");


		
		//aug
		TimeMgmtDates rakhi = new TimeMgmtDates("08/03/2020", "Raksha Bandhan");
		TimeMgmtDates krishnaJayanti = new TimeMgmtDates("08/11/2020", "Krishna Jayanti");
			//2021
		TimeMgmtDates IndependenceDay_21 = new TimeMgmtDates("08/15/2021", "Independence Day");
		TimeMgmtDates JanMashtami_21 = new TimeMgmtDates("08/30/2021", "JanMashtami");



		//sep
		TimeMgmtDates usLabourDay = new TimeMgmtDates("09/07/2020", "US Labor Day");
			//2021
		TimeMgmtDates ganeshaChaturthi_21 = new TimeMgmtDates("09/10/2021", "Ganesha Chaturthi");
		TimeMgmtDates usLabourDay_21 = new TimeMgmtDates("09/07/2021", "US Labor Day");

 
		
		//oct
		TimeMgmtDates gandhiJayanti = new TimeMgmtDates("10/02/2020", "Gandhi Jayanti");
		TimeMgmtDates vijayaDashami = new TimeMgmtDates("10/26/2020", "Vijaya Dashami");
			//2021
		TimeMgmtDates gandhiJayanti_21 = new TimeMgmtDates("10/02/2021", "Gandhi Jayanti");
		TimeMgmtDates vijayaDashami_21 = new TimeMgmtDates("10/15/2021", "Vijaya Dashami");


		
		//nov
		TimeMgmtDates thanksGivingDay = new TimeMgmtDates("11/26/2020", "Thanks Giving Day");
		TimeMgmtDates thanksGivingDay1 = new TimeMgmtDates("11/27/2020", "Thanks Giving Day");
		TimeMgmtDates karthikaPurnima = new TimeMgmtDates("11/30/2020", "Karthika Purnima");
			//2021
		TimeMgmtDates kannadaRajyotsava_21 = new TimeMgmtDates("11/01/2021", "kannada Rajyotsava");
		TimeMgmtDates deepavali_21 = new TimeMgmtDates("11/04/2021", "Deepavali");
		TimeMgmtDates karthikaPurnima_21 = new TimeMgmtDates("11/19/2021", "Karthika Purnima");
		TimeMgmtDates GuruNanakJayanti_21 = new TimeMgmtDates("11/19/2021", "Guru Nanak Jayanti");
		TimeMgmtDates thanksGivingDay_21 = new TimeMgmtDates("11/26/2021", "Thanks Giving Day");
		TimeMgmtDates thanksGivingDay1_21 = new TimeMgmtDates("11/27/2021", "Thanks Giving Day");
		
		
		//dec
		TimeMgmtDates winterHoliday = new TimeMgmtDates("12/24/2020", "Winter Holiday");
		TimeMgmtDates christmas = new TimeMgmtDates("12/25/2020", "Christmas");
			//2021
		TimeMgmtDates winterHoliday_21 = new TimeMgmtDates("12/24/2021", "Winter Holiday");
		TimeMgmtDates christmas_21 = new TimeMgmtDates("12/25/2021", "Christmas");


		//holiday List
		
		HolidayList bangalore = new HolidayList("Bangalore", Arrays.asList(newYear,makarSankranti,
				mahaShivratri,holi,ugadi,goodFriday,mayDay,Bakrid,gandhiJayanti,vijayaDashami,christmas,newYear_21,makarSankranti_21,
				republicDay_21,mahaShivratri_21,goodFriday_21,ugadi_21,mayDay_21,Bakrid_21,IndependenceDay_21,ganeshaChaturthi_21,
				gandhiJayanti_21,vijayaDashami_21,kannadaRajyotsava_21,deepavali_21));
		
		HolidayList chennai = new HolidayList("Chennai", Arrays.asList(newYear,pongal,mahaShivratri,
				goodFriday,tamilNewYear,mayDay,Bakrid,krishnaJayanti,gandhiJayanti,vijayaDashami,christmas,newYear_21,pongal_21,
				republicDay_21,mahaShivratri_21,goodFriday_21,tamilNewYear_21,mayDay_21,Bakrid_21,IndependenceDay_21,JanMashtami_21,
				ganeshaChaturthi_21,gandhiJayanti_21,vijayaDashami_21,deepavali_21));
		
		HolidayList gurgaon = new HolidayList("Gurgaon", Arrays.asList(newYear,mahaShivratri,holi,goodFriday,
				mayDay,Bakrid,rakhi,krishnaJayanti,gandhiJayanti,vijayaDashami,christmas,newYear_21,republicDay_21,mahaShivratri_21,
				holi_21,goodFriday_21,mayDay_21,Bakrid_21,IndependenceDay_21,JanMashtami_21,ganeshaChaturthi_21,gandhiJayanti_21,
				vijayaDashami_21,deepavali_21,GuruNanakJayanti_21));
		
		HolidayList hyderabad = new HolidayList("Hyderabad", Arrays.asList(newYear,makarSankranti,mahaShivratri,
				teluguNewYear,goodFriday,mayDay,Bakrid,gandhiJayanti,vijayaDashami,karthikaPurnima,christmas,newYear_21,
				pongal_21,republicDay_21,mahaShivratri_21,goodFriday_21,telguNewYear_21,mayDay_21,Bakrid_21,IndependenceDay_21,
				ganeshaChaturthi_21,gandhiJayanti_21,vijayaDashami_21,deepavali_21,karthikaPurnima_21));
		
		HolidayList bangaloreUsBackOffice = new HolidayList("Bangalore US Back Office", Arrays.asList(newYear,
				usPresidentDay,mayDay,USIndependenceDay,usLabourDay,gandhiJayanti,vijayaDashami,thanksGivingDay,
				thanksGivingDay1,winterHoliday,christmas,newYear_21,usPresidentDay_21,memorialDay_21,USIndependenceDay_21,
				usLabourDay_21,thanksGivingDay_21,thanksGivingDay1_21,winterHoliday_21,christmas_21));
			
		//accounts details
		
		AccountInformation verizon = new AccountInformation("Verizon", Arrays.asList("VMAS","GENESYS","Tool Automation","TimeManagement",
				"Iron Mountain","Scripps"));
		AccountInformation Zyter = new AccountInformation("Zyter", Arrays.asList("PMS","IOS","Android"));
		AccountInformation Nokia = new AccountInformation("Nokia", Arrays.asList("Network","Communication"));
		
		List<AccountInformation> accounts = Arrays.asList(verizon,Zyter,Nokia);
		this.accountInformationRepository.saveAll(accounts);
		
		//admins
		User admin = new User("0000", "Admin", "", "admin@infinite.com", "admin",
				new String(Base64.getEncoder().encodeToString("admin@123".getBytes())), "01/02/20", "Infinite",accounts);
		
	
		
//		this.timeMgmtTimeSheetEntryRepository.deleteAll();
//		this.userHierarchyRepository.deleteAll();
//		this.userRepository.deleteAll();

		List<User> tmuser = Arrays.asList(admin);
		this.userRepository.saveAll(tmuser);
		List<HolidayList> holiday = Arrays.asList(bangalore,chennai,hyderabad,gurgaon,bangaloreUsBackOffice);
		this.holidayListRepository.saveAll(holiday);
		
		
	}

		
}
