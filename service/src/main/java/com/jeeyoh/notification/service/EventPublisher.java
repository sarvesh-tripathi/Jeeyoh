/**
 * 
 */
package com.jeeyoh.notification.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.notification.JeeyohMessageType;

/**
 * @author Sarvesh
 * 
 */
@Component("eventPublisher")
public class EventPublisher implements IMessagingEventPublisher
{
    final static Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    @Autowired
    IJeeyohMessagePublisher jeeyohMessagePublisher;

    @Value("${mail.sender.email}")
    private String fromEmailId;

    @Value("${app.jeeyoh.server.url}")
    private String serverURL;
    
    @Value("${app.jeeyoh.welcome.subject}")
    private String welcomeEmailSubject;
    
    
    @Value("${app.jeeyoh.registration.confirmation.subject}")
    private String confirmationSubject;
    
    
    
    public void sendWelcomeEmail(UserModel user)
    {
        HashMap<String, Object> messageArgumentsMap = new HashMap<String, Object>();
        messageArgumentsMap.put("userFirstName", user.getFirstName());
        messageArgumentsMap.put("to", user.getEmailId());
        messageArgumentsMap.put("from", fromEmailId);
        messageArgumentsMap.put("subject", welcomeEmailSubject);
        jeeyohMessagePublisher.publish(this.getClass().getSimpleName(), JeeyohMessageType.WELCOME_EMAIL,
                IJeeyohMessagePublisher.INSTANCE_NO_REF, messageArgumentsMap);
    }
    /*
    public void sendChangePasswordEmail()
    {
        HashMap<String, Object> messageArgumentsMap = new HashMap<String, Object>();
        //messageArgumentsMap.put("userFullName", user.getFullName());
        //messageArgumentsMap.put("newPassword", user.getNewPassword());
        //messageArgumentsMap.put("to", user.getEmail());
        messageArgumentsMap.put("from", fromEmailId);
        messageArgumentsMap.put("subject", changePasswordEmailSubject);
        storisticMessagePublisher.publish(this.getClass().getSimpleName(), JeeyohMessageType.RESET_PASSWORD,
                IJeeyohMessagePublisher.INSTANCE_NO_REF, messageArgumentsMap);
    }
*/
    /*public void sendResetPasswordEmail()
    {
        HashMap<String, Object> messageArgumentsMap = new HashMap<String, Object>();
        messageArgumentsMap.put("userFirstName", user.getFullName());
        messageArgumentsMap.put("newPassword", user.getTempPassword());
        messageArgumentsMap.put("to", user.getEmail());
        messageArgumentsMap.put("from", fromEmailId);
        messageArgumentsMap.put("serverURL", serverURL);
        messageArgumentsMap.put("subject", resetPasswordEmailSubject);
        storisticMessagePublisher.publish(this.getClass().getSimpleName(), user, JeeyohMessageType.RESET_PASSWORD,
                IJeeyohMessagePublisher.INSTANCE_NO_REF, messageArgumentsMap);
    }*/
    
    /*@Override
    public void sendInvitationEmail()
    {
        HashMap<String, Object> messageArgumentsMap = new HashMap<String, Object>();
        messageArgumentsMap.put("userName", InvitedUserName);
        messageArgumentsMap.put("userEmail", emailId);
        messageArgumentsMap.put("invitationCode", invitationCode);
        messageArgumentsMap.put("serverURL", serverURL);
        messageArgumentsMap.put("imageURL", imageURL);
        messageArgumentsMap.put("inviteFriendName", inviteFriendName); 
        messageArgumentsMap.put("to", emailId);
        messageArgumentsMap.put("from", fromEmailId);
        messageArgumentsMap.put("subject", inviteFriendEmailSubject.replace("<invitationFrom>", InvitedUserName));
        storisticMessagePublisher.publish(this.getClass().getSimpleName(), null, JeeyohMessageType.INVITE_FRIEND,
                IJeeyohMessagePublisher.INSTANCE_NO_REF, messageArgumentsMap);
        
    }*/
    
    public void sendConfirmationEmail(UserModel user, String confirmationCode)
    {        
        HashMap<String, Object> messageArgumentsMap = new HashMap<String, Object>();
        String encUserName = user.getFirstName();
        messageArgumentsMap.put("userName", user.getFirstName());
        String encEmail = user.getEmailId();  
        String encConfirmationCode = null;
        try
        {
            encUserName = URLEncoder.encode(encUserName, "UTF-8");
            encEmail = URLEncoder.encode(encEmail, "UTF-8");
            encConfirmationCode = URLEncoder.encode(confirmationCode, "UTF-8");
            
        }
        catch(UnsupportedEncodingException e)
        {
            logger.error("email and name can not be encoded");
        }
        //encUserName = encUserName.replaceAll("\\+", "%20");        
        messageArgumentsMap.put("encUserName", encUserName);
        messageArgumentsMap.put("encEmail", encEmail);
        messageArgumentsMap.put("serverURL", serverURL);
        messageArgumentsMap.put("confirmationCode", encConfirmationCode);
        
        messageArgumentsMap.put("to", user.getEmailId());
        messageArgumentsMap.put("from", fromEmailId);
        messageArgumentsMap.put("subject", confirmationSubject);
        jeeyohMessagePublisher.publish(this.getClass().getSimpleName(), JeeyohMessageType.REGISTRATION_CONFIRMATION,
                IJeeyohMessagePublisher.INSTANCE_NO_REF, messageArgumentsMap);
    }



	@Override
	public void forgetPassword(UserModel user) {
		// TODO Auto-generated method stub
		 HashMap<String, Object> messageArgumentsMap = new HashMap<String, Object>();
	        messageArgumentsMap.put("userFirstName", user.getFirstName());
	        messageArgumentsMap.put("password", user.getPassword());
	        messageArgumentsMap.put("to", user.getEmailId());
	        messageArgumentsMap.put("from", fromEmailId);
	        //messageArgumentsMap.put("serverURL", serverURL);
	        messageArgumentsMap.put("subject", "Forget Password");
	        jeeyohMessagePublisher.publish(this.getClass().getSimpleName(), JeeyohMessageType.FORGET_PASSWORD,
	                IJeeyohMessagePublisher.INSTANCE_NO_REF, messageArgumentsMap);
	        
	      /*  jeeyohMessagePublisher.publish(this.getClass().getSimpleName(), user, JeeyohMessageType.RESET_PASSWORD,
	                IJeeyohMessagePublisher.INSTANCE_NO_REF, messageArgumentsMap);*/
	}

}
