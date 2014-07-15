package com.jeeyoh.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jeeyoh.formbean.UserFormBean;
import com.jeeyoh.model.enums.ServiceAPIStatus;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.UserRegistrationResponse;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.notification.service.IMessagingEventPublisher;
import com.jeeyoh.service.userservice.IUserService;
import com.jeeyoh.validator.IFormValidator;

@Controller
public class RegistrationController {

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	IMessagingEventPublisher eventPublisher;

	@Autowired
	private IUserService userService;

	@Autowired
	IFormValidator formValidator;

	/**
	 * Renders Registration page to the user
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView signUp(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("RegistrationController ==> signUp ==> ");
		ModelAndView modelAndView = new ModelAndView("registration", "userFormBean", new UserFormBean());
		return modelAndView;
	}



	/**
	 * temporary success page only for testing purpose
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/successPage", method = RequestMethod.GET)
	public ModelAndView successPage(HttpServletRequest request, HttpServletResponse response)
	{

		ModelAndView modelAndView = new ModelAndView("successPage");
		return modelAndView;
	}
	
	
	/**
	 * temporary success page only for testing purpose
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/successEmailConfirmation", method = RequestMethod.GET)
	public ModelAndView successEmailConfirmation(HttpServletRequest request, HttpServletResponse response)
	{

		ModelAndView modelAndView = new ModelAndView("successEmailConfirmation");
		return modelAndView;
	}

	/**
	 * temporary failure page only for testing purpose
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/failurePage", method = RequestMethod.GET)
	public ModelAndView failurePage(HttpServletRequest request, HttpServletResponse response)
	{

		ModelAndView modelAndView = new ModelAndView("failurePage");
		return modelAndView;
	}

	/**
	 * Invoked when the user opens the errorPage404 page
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/errorPage404", method = RequestMethod.GET)
	public ModelAndView errorPage404(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("RegistrationController ==> home ==> ");
		ModelAndView modelAndView = new ModelAndView("errorPage404");
		return modelAndView;
	}

	/**
	 * Invoked when the user opens the errorPage404 page
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/errorPage500", method = RequestMethod.GET)
	public ModelAndView errorPage500(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("RegistrationController ==> home ==> ");
		ModelAndView modelAndView = new ModelAndView("errorPage500");
		return modelAndView;
	}

	/**
	 * Register user to download Jeeyoh App
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public ModelAndView registerUser(@ModelAttribute("userFormBean") UserFormBean userFormBean,  BindingResult result)
	{
		// Check if email already exist in Database
		String email = userFormBean.getEmail();
		BaseResponse emailExistResponse = userService.emailNotRegistered(email);
		if (emailExistResponse != null && emailExistResponse.getStatus().equals(ServiceAPIStatus.OK.getStatus()))
		{
			userFormBean.setEmailRegister(false);
		}
		else
		{
			userFormBean.setEmailRegister(true);
		}

		formValidator.signUpvalidate(userFormBean, result);

		ModelAndView modelAndView = null;
		if (result.hasErrors())
		{
			modelAndView = new ModelAndView("registration", "userFormBean", userFormBean);
		}
		else
		{
			UserModel user = new UserModel();
			if (userFormBean != null)
			{
				user.setFirstName(userFormBean.getFirstName());
				user.setLastName(userFormBean.getLastName());
				user.setEmailId(userFormBean.getEmail());
			}

			UserRegistrationResponse registerResponse = userService.registerBetaListUser(user);
			if (registerResponse != null && registerResponse.getStatus().equals(ServiceAPIStatus.OK.getStatus()))
			{
				
				String confirmationCode = registerResponse.getConfirmationId();
				eventPublisher.sendConfirmationEmail(user, confirmationCode);
				modelAndView = new ModelAndView("redirect:successPage");
			}
			else
			{
				modelAndView = new ModelAndView("redirect:failurePage");
			}
		}
		return modelAndView;
	}


	/**
	 * This method helps to confirm user's email address
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/confirmEmail", method = RequestMethod.GET)
	public ModelAndView confirmEmail(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView =null;
		String confirmationCode = request.getParameter("confirmationCode");
		String email = request.getParameter("emailId");
		String userName = request.getParameter("userName");

		UserModel user = new UserModel();
		try {
			user.setEmailId(URLDecoder.decode(email, "UTF-8"));
			user.setFirstName(URLDecoder.decode(userName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		logger.debug("confirmEmail == " + confirmationCode + " : "+ email +" : "+userName);
		if (confirmationCode != null && !confirmationCode.isEmpty())
		{
			BaseResponse confirmResponse = userService.confirmEmail(email, confirmationCode);
			if (confirmResponse != null && confirmResponse.getStatus() == ServiceAPIStatus.OK.getStatus())
			{
				eventPublisher.sendWelcomeEmail(user);
				modelAndView = new ModelAndView("redirect:successEmailConfirmation");
			}
			else
			{

			}

		}
		else
		{

		}
		return modelAndView;
	}

}
