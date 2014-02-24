/**
 * 
 */
package com.jeeyoh.notification.service;

import com.jeeyoh.model.user.UserModel;

/**
 * @author Sarvesh
 *
 */
public interface IMessagingEventPublisher
{
	//public void sendWelcomeEmail();
	//public void sendChangePasswordEmail();
    //public void sendResetPasswordEmail();    
    //public void sendInvitationEmail();
   // public void sendConfirmationEmail();
	public void forgetPassword(UserModel user);
	public void sendConfirmationEmail(UserModel user, String confirmationCode);    
}
