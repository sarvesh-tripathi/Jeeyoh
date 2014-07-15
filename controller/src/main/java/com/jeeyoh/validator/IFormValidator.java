package com.jeeyoh.validator;

import org.springframework.validation.Errors;

public interface IFormValidator
{
    public void signUpvalidate(Object target, Errors errors);
}
