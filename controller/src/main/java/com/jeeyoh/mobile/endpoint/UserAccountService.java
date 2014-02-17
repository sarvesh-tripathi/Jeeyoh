package com.jeeyoh.mobile.endpoint;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.LoginResponse;
import com.jeeyoh.model.response.UserRegistrationResponse;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.notification.service.IMessagingEventPublisher;
import com.jeeyoh.service.userservice.IUserService;
import com.jeeyoh.utils.Utils;
import com.sun.jersey.api.core.InjectParam;


@Path("/userService")
public class UserAccountService {
	
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@InjectParam
	IUserService userService;
	
	@InjectParam
    IMessagingEventPublisher eventPublisher;
	
	@Path("/test/{name}")
	@GET
	public Response  test(@PathParam("name") String name)
	{
		logger.debug("Enter in mobile app "+name);
		return Response.status(200).entity(name).build();
	}
	
	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public LoginResponse login(UserModel user)
	{
		logger.debug("Enter in mobile app "+user);
		String password = user.getPassword();
		user.setPassword(Utils.MD5(password));		
		LoginResponse loginResponce = userService.loginUser(user);
		return loginResponce;
	}
	@Path("/regiteration")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    public UserRegistrationResponse registration(UserModel user)
	{
		logger.debug("Enter in mobile app "+user.getFirstName());
		UserRegistrationResponse userRegistrationResponse = null;
		String confirmationCode = null;
		if(user != null)
		{
			logger.debug("Enter in mobile app 2222");
			userRegistrationResponse = userService.registerUser(user);
			if(userRegistrationResponse != null)
			{
				confirmationCode = userRegistrationResponse.getConfirmationId();
				logger.debug("confirmation code "+confirmationCode);
			    eventPublisher.sendConfirmationEmail(user,confirmationCode);
			}
		}
		return userRegistrationResponse;
	}
	
	@Path("/logout")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public BaseResponse logout(UserModel user)
	{
		BaseResponse  baseResponce = userService.logoutUser(user);
		return baseResponce;
		
	}
	
	/*
	 * 
	 */
	
	@Path("/chnagePassword")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public BaseResponse chnagePassword(UserModel user)
	{
		BaseResponse baseResponse = userService.changePassword(user);
		return baseResponse;
	}
	@GET
    @Path("/confirmEmail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse confirmEmail(@QueryParam("confirmationCode") String confirmationCode)
    {
		BaseResponse baseResponse = userService.confirmUser(confirmationCode);		
		return baseResponse;
		
    }
	

}
