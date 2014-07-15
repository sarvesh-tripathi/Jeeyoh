package com.jeeyoh.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestLoggingFilter implements Filter{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		long startTime = System.currentTimeMillis();
		logger.debug("Filter ==> Request ==> startTime ==> " + startTime);
		String url = null;
		if (request instanceof HttpServletRequest) {
			 url = ((HttpServletRequest)request).getRequestURL().toString();
		}
		logger.debug("In Filter ==> ==> "+url);
		
		// pass the request along the filter chain
        chain.doFilter(request, response);
        
        long endTime = System.currentTimeMillis();
		logger.debug("Filter ==> Response ==> Time ==> " + endTime + " total time taken ==> " + (endTime-startTime));

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
