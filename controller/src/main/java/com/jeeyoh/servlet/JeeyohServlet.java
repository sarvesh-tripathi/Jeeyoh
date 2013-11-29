package com.jeeyoh.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

public class JeeyohServlet extends DispatcherServlet {
	private static final long serialVersionUID = 6329331078227751697L;
    protected void doDispatch(HttpServletRequest request,
            HttpServletResponse response)
            throws Exception
    { 
        super.doDispatch(request, response);
    }
}
