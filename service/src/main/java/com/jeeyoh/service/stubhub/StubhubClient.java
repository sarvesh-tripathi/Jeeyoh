package com.jeeyoh.service.stubhub;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.jeeyoh.model.stubhub.StubHubEvents;

@Component("stubhubClient")
public class StubhubClient implements IStubhubClient {
	
	

	private String stubhubURL = "http://www.stubhub.com/listingCatalog/select/?qt=fetchAllEvents&version=2.2&indent=on&wt=json&fl=description+event_date+event_date_local+event_time_local+geography_parent+venue_name+city+state+genreUrlPath+urlpath+leaf+channel+totalTickets+zip+latitude+longitude+timezone+timezone_id+venue_config_name+currency_code+event_config_template_id+event_con+fig_template+keywords_en_US+maxPrice+minPrice+maxSeatsTogether+minSeatsTogether+title+ancestorGenreDescriptions+image_url+genre_parent_name";
	//private String stubhubURL = "http://www.stubhub.com/listingCatalog/select/?q=stubhubDocumentType:event&version=2.2&indent=on&wt=json&fl=description+event_date+event_date_local+event_time_local+geography_parent+venue_name+city+state+genreUrlPath+urlpath+leaf+channel+totalTickets+zip+latitude+longitude+timezone+timezone_id+venue_config_name+currency_code+event_config_template_id+event_con+fig_template+keywords_en_US+maxPrice+minPrice+maxSeatsTogether+minSeatsTogether+title+ancestorGenreDescriptions+image_url+genre_parent_name&start=0&rows=50000";
	
	private HttpClient client = new DefaultHttpClient();
	@Override
	public StubHubEvents getStubHubEvents() {
		// TODO Auto-generated method stub
		
		
		HttpGet request = new HttpGet(stubhubURL);
		HttpResponse response = null;
		StubHubEvents stub = null;
		try {
			
			response = client.execute(request);
			int responseCode = response.getStatusLine().getStatusCode();
			ObjectMapper mapper = new ObjectMapper();
			stub = mapper.readValue(response.getEntity().getContent(), StubHubEvents.class);
			
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stub;
	}

}
