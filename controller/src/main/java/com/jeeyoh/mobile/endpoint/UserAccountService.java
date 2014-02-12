package com.jeeyoh.mobile.endpoint;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeeyoh.model.responce.LoginResponce;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.service.userservice.IUserService;


@Path("/userService")
public class UserAccountService {
	
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@Autowired
	IUserService userService;
	
	@Path("/test/{name}")
	@GET
	public Response  test(@PathParam("name") String name)
	{
		logger.debug("Enter in mobile app "+name);
		return Response.status(200).entity(name).build();
	}
	
	@Path("/login/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public LoginResponce  login(@PathParam("name") UserModel user)
	{
		logger.debug("Enter in mobile app "+user);
		LoginResponce loginResponce = userService.loginUser(user);
		return loginResponce;
	}
	@Path("/regiteration/{name}")
	@GET
	public Response  registration(@PathParam("name") String name)
	{
		logger.debug("Enter in mobile app "+name);
		return Response.status(200).entity(name).build();
	}

}
