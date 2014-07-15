package com.jeeyoh.validator;

import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.jeeyoh.formbean.UserFormBean;

@Component("FormValidator")
public class FormValidator implements Validator, IFormValidator
{
    private static Logger logger = LogManager.getLogger(FormValidator.class);

    private final static Pattern EMAIL_PATTERN = Pattern
            .compile(".+@.+\\.[a-z]+");

    @Override
    public boolean supports(Class clazz)
    {
        // just validate the UserFormBean instances
        // If we want to use multiple classes using the below lines
        // return UserFormBean.class.equals(clazz) ||
        // Another.class.equals(clazz);
        return UserFormBean.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        logger.debug("Inside Val;idate method");
    }

    private boolean isEmail(String value)
    {
        return EMAIL_PATTERN.matcher(value).matches();
    }

    @Override
    public void signUpvalidate(Object target, Errors errors)
    {
        logger.debug("signUpvalidate method called");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName",
                "error.firstname.empty");
        /*ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password",
                "error.password.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "verifyPassword",
                "error.verifypassword.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "age",
        "error.age.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender",
        "error.gender.empty");*/
        

        UserFormBean user = (UserFormBean) target;

        
       /* String password = user.getPassword();
        String verifyPassword = user.getVerifyPassword();

        if (password != null && verifyPassword != null)
        {
            if (!(password.equals(verifyPassword)))
            {
                logger.debug("Inside not matches");
                errors.rejectValue("verifyPassword", "notmatch.password");
            }
        }*/
        String email = user.getEmail();
        if (email == null || email.isEmpty() || email.trim().isEmpty())
        {
            errors.rejectValue("email", "error.email.empty");
        }
        else if (!isEmail(email))
        {
            errors.rejectValue("email", "error.email.invalid");
        }
        else if(user.isEmailRegister())
        {
            logger.debug("inside email alreadyRegister::");
            errors.rejectValue("email", "error.email.alreadyRegister");
        }
        
       /* String age = user.getAge();
        if(age != null && !age.isEmpty())
        {
            int userAge = 0;
            try
            {
                userAge = Integer.parseInt(age);
                if(userAge < 13)
                {
                    errors.rejectValue("age", "error.age.minimum");
                }
            }
            catch(Exception e)
            {
                errors.rejectValue("age", "error.age.notnumeric");
            }
            
        }*/

    }

}