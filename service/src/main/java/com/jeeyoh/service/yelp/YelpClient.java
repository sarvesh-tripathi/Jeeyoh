package com.jeeyoh.service.yelp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.List;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.HmacSha1MessageSigner;
import oauth.signpost.signature.QueryStringSigningStrategy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jeeyoh.model.yelp.YelpBusiness;
import com.jeeyoh.model.yelp.YelpBusinessResponse;
import com.jeeyoh.model.yelp.YelpSearchResponse;
import com.jeeyoh.service.groupon.GrouponClient;

@Component("yelpClient")
public class YelpClient implements IYelpClient {
	private static Logger logger = LoggerFactory.getLogger(YelpClient.class);

	public final String CONSUMER_KEY = "6tQCXdb-LTRtgzW-5cfLNA"; 
    public final String CONSUMER_SECRET = "whVCSDPZ7vp3cl5O1wpl8s-RIX4"; 
    public final String TOKEN = "LdtW0aQ_KTbzZUFY-XFZjO6eXwPcuwk1"; 
    public final String TOKEN_SECRET = "ruhV8VhcSSp6vzYdEntqLFgSeVc"; 
    public final String YWSID = "iXUUomsyqBkdXaypz7539A"; 
    public final String ENCODING_SCHEME = "UTF-8"; 
    
	@Override
	public YelpSearchResponse search(String location, String offset) {
		String lat = "30.361471"; 
        String lng = "-87.164326"; 
        String category = "food"; 
        String radius = "40000"; 
        String limit = "20"; 
        //String offset = "0"; 
        String sort = "0"; 
        //String term = "food";
        //String location = "San Fransisco";
        String service = "http://api.yelp.com/v2/search"; 
        YelpSearchResponse searchResponse = null;

        try { 
                String query = String 
                                .format(service + "?location=%s&radius_filter=%s&limit=%s&offset=%s&sort=%s", 
                                                //URLEncoder.encode(term, ENCODING_SCHEME), 
                                                URLEncoder.encode(location, ENCODING_SCHEME), 
                                                URLEncoder.encode(radius, ENCODING_SCHEME), 
                                                URLEncoder.encode(limit, ENCODING_SCHEME), 
                                                URLEncoder.encode(offset, ENCODING_SCHEME), 
                                                URLEncoder.encode(sort, ENCODING_SCHEME)); 
                OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, 
                                CONSUMER_SECRET); 
                consumer.setTokenWithSecret(TOKEN, TOKEN_SECRET); 
                //consumer.setMessageSigner(new HmacSha1MessageSigner()); 
                consumer.setSendEmptyTokens(true); 
                consumer.setSigningStrategy(new QueryStringSigningStrategy());                       
                String signedQuery = consumer.sign(query);
                consumer.setMessageSigner(new HmacSha1MessageSigner()); 
                System.out.println(getClass().getName() + ">> " + "Signed query: " 
                                + signedQuery); 
                HttpGet request = new HttpGet(signedQuery); 
                HttpClient httpClient = new DefaultHttpClient(); 
                HttpResponse response = (HttpResponse) httpClient.execute(request); 

                HttpEntity entity = response.getEntity(); 
                String result = EntityUtils.toString(entity);                
                try { 
                	ObjectMapper mapper = new ObjectMapper();
                	mapper.enable(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                	searchResponse = mapper.readValue(result, YelpSearchResponse.class);
                   }
                   catch(Exception e) { 
                    e.printStackTrace(); 
                   }
        } catch (UnsupportedEncodingException e) { 
                // TODO Auto-generated catch block 
                e.printStackTrace(); 
        } catch (MalformedURLException e) { 
                // TODO Auto-generated catch block 
                e.printStackTrace(); 
        } catch (IOException e) { 
                // TODO Auto-generated catch block 
                e.printStackTrace(); 
        } catch (OAuthMessageSignerException e) { 
                // TODO Auto-generated catch block 
                e.printStackTrace(); 
        } catch (OAuthExpectationFailedException e) { 
                // TODO Auto-generated catch block 
                e.printStackTrace(); 
        } catch (OAuthCommunicationException e) { 
                // TODO Auto-generated catch block 
                e.printStackTrace(); 
        }
        return searchResponse;
	}

	@Override
	public void searchBusiness() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public YelpSearchResponse search(String location) {
		long total = 0;
		String lat = "30.361471"; 
        String lng = "-87.164326"; 
        String category = "food"; 
        String radius = "40000"; 
        String limit = "20"; 
        String offset = "0"; 
        String sort = "0"; 
              
        String service = "http://api.yelp.com/v2/search"; 
        YelpSearchResponse searchResponse = null;
        try { 
                String query = String 
                                .format(service + "?location=%s&radius_filter=%s&limit=%s&offset=%s&sort=%s", 
                                                //URLEncoder.encode(term, ENCODING_SCHEME), 
                                                URLEncoder.encode(location, ENCODING_SCHEME), 
                                                URLEncoder.encode(radius, ENCODING_SCHEME), 
                                                URLEncoder.encode(limit, ENCODING_SCHEME), 
                                                URLEncoder.encode(offset, ENCODING_SCHEME), 
                                                URLEncoder.encode(sort, ENCODING_SCHEME)); 
                OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, 
                                CONSUMER_SECRET); 
                consumer.setTokenWithSecret(TOKEN, TOKEN_SECRET); 
                //consumer.setMessageSigner(new HmacSha1MessageSigner()); 
                consumer.setSendEmptyTokens(true); 
                consumer.setSigningStrategy(new QueryStringSigningStrategy());                       
                String signedQuery = consumer.sign(query);
                consumer.setMessageSigner(new HmacSha1MessageSigner()); 
                System.out.println(getClass().getName() + ">> " + "Signed query: " 
                                + signedQuery); 
                HttpGet request = new HttpGet(signedQuery); 
                HttpClient httpClient = new DefaultHttpClient(); 
                HttpResponse response = (HttpResponse) httpClient.execute(request); 

                HttpEntity entity = response.getEntity(); 
                String result = EntityUtils.toString(entity);                
                try { 
                	ObjectMapper mapper = new ObjectMapper();
                	mapper.enable(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                	searchResponse = mapper.readValue(result, YelpSearchResponse.class);
                	total = searchResponse.getTotal();
                } catch(Exception e) { 
                	e.printStackTrace(); 
                }
        } catch (UnsupportedEncodingException e) { 
                // TODO Auto-generated catch block 
                e.printStackTrace(); 
        } catch (MalformedURLException e) { 
                // TODO Auto-generated catch block 
                e.printStackTrace(); 
        } catch (IOException e) { 
                // TODO Auto-generated catch block 
                e.printStackTrace(); 
        } catch (OAuthMessageSignerException e) { 
                // TODO Auto-generated catch block 
                e.printStackTrace(); 
        } catch (OAuthExpectationFailedException e) { 
                // TODO Auto-generated catch block 
                e.printStackTrace(); 
        } catch (OAuthCommunicationException e) { 
                // TODO Auto-generated catch block 
                e.printStackTrace(); 
        }
        return searchResponse;
	}

	@Override
	public YelpBusinessResponse business(String businessId) {
		
		 String service = "http://api.yelp.com/v2/business/" + businessId; 
		 logger.debug("Business :: "+service);
		 YelpBusinessResponse searchResponse = null;
	        try { 
	                
	                OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, 
	                                CONSUMER_SECRET); 
	                consumer.setTokenWithSecret(TOKEN, TOKEN_SECRET); 
	                //consumer.setMessageSigner(new HmacSha1MessageSigner()); 
	                consumer.setSendEmptyTokens(true); 
	                consumer.setSigningStrategy(new QueryStringSigningStrategy());                       
	                String signedQuery = consumer.sign(service);
	                consumer.setMessageSigner(new HmacSha1MessageSigner()); 
	                System.out.println(getClass().getName() + ">> " + "Signed query: " 
	                                + signedQuery); 
	                HttpGet request = new HttpGet(signedQuery); 
	                HttpClient httpClient = new DefaultHttpClient(); 
	                HttpResponse response = (HttpResponse) httpClient.execute(request); 

	                HttpEntity entity = response.getEntity(); 
	                String result = EntityUtils.toString(entity);                
	                try { 
	                	ObjectMapper mapper = new ObjectMapper();
	                	mapper.enable(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
	                	searchResponse = mapper.readValue(result, YelpBusinessResponse.class);
	                	} catch(Exception e) { 
	                	e.printStackTrace(); 
	                }
	        } catch (UnsupportedEncodingException e) { 
	                // TODO Auto-generated catch block 
	                e.printStackTrace(); 
	        } catch (MalformedURLException e) { 
	                // TODO Auto-generated catch block 
	                e.printStackTrace(); 
	        } catch (IOException e) { 
	                // TODO Auto-generated catch block 
	                e.printStackTrace(); 
	        } catch (OAuthMessageSignerException e) { 
	                // TODO Auto-generated catch block 
	                e.printStackTrace(); 
	        } catch (OAuthExpectationFailedException e) { 
	                // TODO Auto-generated catch block 
	                e.printStackTrace(); 
	        } catch (OAuthCommunicationException e) { 
	                // TODO Auto-generated catch block 
	                e.printStackTrace(); 
	        }
	        return searchResponse;
	}
	
}
