package com.scsb.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class CompatibilityFilter
 */
/*@WebFilter("/CompatibilityFilter")*/
public class CompatibilityFilter implements Filter {

	/*使用filter 在web.xml
	 *
	<filter>  
	    <filter-name>CompatibilityFilter</filter-name>  
	    <filter-class>com.scsb.vaadin.servlet.CompatibilityFilter</filter-class>  
	</filter>  
	<filter-mapping>  
	    <filter-name>CompatibilityFilter</filter-name>  
	    <url-pattern>/Servlet/*</url-pattern>  
	</filter-mapping>
	 * */
	
    /**
     * Default constructor. 
     */
    public CompatibilityFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse) response;
		res.addHeader("X-UA-Compatible", "IE=EmulateIE8");
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
