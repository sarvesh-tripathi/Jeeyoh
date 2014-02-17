package com.jeeyoh.mobile.endpoint;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeeyoh.model.response.UserRegistrationResponse;
import com.jeeyoh.persistence.domain.User;

@Path("/searchService")
public class SearchService {
	
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserRegistrationResponse search(User user)
    {
        
        return null;
    }

}
