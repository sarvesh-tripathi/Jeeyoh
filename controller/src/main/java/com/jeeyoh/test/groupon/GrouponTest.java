package com.jeeyoh.test.groupon;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//import com.jeeyoh.domain.Groupondivision;
import com.jeeyoh.model.groupon.DealsModel;
import com.jeeyoh.model.groupon.DivisionModel;
import com.jeeyoh.model.groupon.DivisionResponseModel;
import com.jeeyoh.model.groupon.ResponseModel;
import com.jeeyoh.service.groupon.GrouponClient;
import com.jeeyoh.service.groupon.GrouponService;

public class GrouponTest {

	public static ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
			"applicationContext.xml");
	public static GrouponClient grouponClient = applicationContext.getBean(
			"grouponClient", GrouponClient.class);
	public static GrouponService grouponService = applicationContext.getBean(
			"grouponService", GrouponService.class);
	
	public static void main(String[] args) {
		//getDeals();
		//getDivisions();
		//getDealsByCountry();
	}
	
	public static void getDeals() {
		
		ResponseModel response = grouponClient.getDeals();
		if(response != null)
		{
			List<DealsModel> deals = response.getDeals();
			if(deals != null)
			{
				System.out.println("deals size = " + deals.size());
				try {
					ObjectMapper mapper = new ObjectMapper();
					System.out.println(mapper.writeValueAsString(response));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				//grouponService.addDeals(deals);
			}
		}
		/*
		String response = grouponClient.getDeals();
		System.out.println("deals response = " + response);
		*/
	}
	
	public static void getDivisions() {
		DivisionResponseModel response = grouponClient.getDivisions();
		if(response != null) {
			List<DivisionModel> divisions = response.getDivisions();
			if(divisions != null)
			{
				System.out.println("number of divisions:= " + divisions.size());
				
				try {
					ObjectMapper mapper = new ObjectMapper();
					System.out.println(mapper.writeValueAsString(response));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				//grouponService.addDivisions(divisions);			
			}
		}
	}
	/*
	public static void getDealsByCountry() {
		String country = "USA";
		List<Groupondivision> divisions = grouponService.getDivisionsByCountry(country);
		if(divisions != null)
		{
			for(Groupondivision division : divisions) {
				if(division != null) {
					ResponseModel response = grouponClient.getDealsByDivision(division.getDivisionid());
					if(response != null)
					{
						List<DealsModel> deals = response.getDeals();
						if(deals != null)
						{
							System.out.println("deals size = " + deals.size());
							try {
								ObjectMapper mapper = new ObjectMapper();
								System.out.println(mapper.writeValueAsString(response));
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							grouponService.addDeals(deals);
						}
					}
				}
			}
		}		
	}*/
}
