package com.jeeyoh.formbean;

import javax.validation.constraints.NotNull;


import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;


public class UserFormBean
{    
    private String firstName;
    private String lastName;
    @NotNull
    @NotEmpty
    @Email
    private String email;
    private boolean isConfirmed;
    
    //Added to check email exist using DB service API
    private boolean emailRegister;

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public boolean isEmailRegister()
    {
        return emailRegister;
    }

    public void setEmailRegister(boolean emailRegister)
    {
        this.emailRegister = emailRegister;
    }

}
