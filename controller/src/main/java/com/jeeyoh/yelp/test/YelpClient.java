package com.jeeyoh.yelp.test;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Calendar;
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

import com.jeeyoh.model.yelp.YelpBusiness;
import com.jeeyoh.model.yelp.YelpBusinessResponse;
import com.jeeyoh.model.yelp.YelpSearchResponse;


public class YelpClient { 
        public final String CONSUMER_KEY = "6tQCXdb-LTRtgzW-5cfLNA"; 
        public final String CONSUMER_SECRET = "whVCSDPZ7vp3cl5O1wpl8s-RIX4"; 
        public final String TOKEN = "LdtW0aQ_KTbzZUFY-XFZjO6eXwPcuwk1"; 
        public final String TOKEN_SECRET = "ruhV8VhcSSp6vzYdEntqLFgSeVc"; 
        public final String YWSID = "iXUUomsyqBkdXaypz7539A"; 
        public final String ENCODING_SCHEME = "UTF-8"; 

        public void start() { 
                String lat = "30.361471"; 
                String lng = "-87.164326"; 
                String category = "food"; 
                String radius = "40000"; 
                String limit = "20"; 
                String offset = "20"; 
                String sort = "0"; 
                String term = "food";
                String location = "Indianapolis";
                //String id = "2908";
                String service = "http://api.yelp.com/v2/search"; 
                //String service = "http://api.yelp.com/v2/business/texas-gourmet-beef-sticks-arlington"; 

                try { 
                        String query = String 
                                        .format(service + "?location=%s&radius_filter=%s&limit=%s&offset=%s&sort=%s", 
                                                       // URLEncoder.encode(term, ENCODING_SCHEME), 
                                                        URLEncoder.encode(location, ENCODING_SCHEME), 
                                                        URLEncoder.encode(radius, ENCODING_SCHEME), 
                                                        URLEncoder.encode(limit, ENCODING_SCHEME), 
                                                        URLEncoder.encode(offset, ENCODING_SCHEME), 
                                                        URLEncoder.encode(sort, ENCODING_SCHEME));
                	//String query = String.format(service, id);
                        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, 
                                        CONSUMER_SECRET); 
                        consumer.setTokenWithSecret(TOKEN, TOKEN_SECRET); 
                        //consumer.setMessageSigner(new HmacSha1MessageSigner()); 
                        consumer.setSendEmptyTokens(true); 
                        consumer.setSigningStrategy(new QueryStringSigningStrategy());                       
                        String signedQuery = consumer.sign(query);
                        consumer.setMessageSigner(new HmacSha1MessageSigner()); 
                       
                        HttpGet request = new HttpGet(signedQuery); 
                        HttpClient httpClient = new DefaultHttpClient(); 
                        HttpResponse response = (HttpResponse) httpClient.execute(request); 

                        HttpEntity entity = response.getEntity(); 
                        String result = EntityUtils.toString(entity); 
                       
                        try {
                        	ObjectMapper mapper = new ObjectMapper();
                        	mapper.enable(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                        	YelpSearchResponse response1 = mapper.readValue(result, YelpSearchResponse.class);
                        	
                        	List<YelpBusiness> businesses = response1.getBusinesses();
                        	
                        } catch(Exception e) {
                        	e.printStackTrace();
                        }
                } catch (UnsupportedEncodingException e) { 
                        e.printStackTrace(); 
                } catch (MalformedURLException e) { 
                        e.printStackTrace(); 
                } catch (IOException e) { 
                        e.printStackTrace(); 
                } catch (OAuthMessageSignerException e) { 
                        e.printStackTrace(); 
                } catch (OAuthExpectationFailedException e) {
                        e.printStackTrace(); 
                } catch (OAuthCommunicationException e) { 
                        e.printStackTrace(); 
                }
        }
        public void business() {
    		
   		 String service = "http://api.yelp.com/v2/business/" + "pinos-prime-meat-market-new-york"; 
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
   	               
   	                HttpGet request = new HttpGet(signedQuery); 
   	                HttpClient httpClient = new DefaultHttpClient(); 
   	                HttpResponse response = (HttpResponse) httpClient.execute(request); 

   	                HttpEntity entity = response.getEntity(); 
   	                String result = EntityUtils.toString(entity); 
   	            
   	             try {
                 	ObjectMapper mapper = new ObjectMapper();
                 	mapper.enable(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                 	YelpBusinessResponse response1 = mapper.readValue(result, YelpBusinessResponse.class);
                 	
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
   	        
   	}
        
     public void test()
     {
    	 Calendar c = Calendar.getInstance();
    	 c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
    	 c.getTime(); // => Date of this coming Saturd
     }

        /** 
         * @param args 
         */ 
        public static void main(String[] args) { 
                // TODO Auto-generated method stub 
               new YelpClient().start(); 
        	   //new YelpClient().business(); 
        		//new YelpClient().test(); 
        }
} 


