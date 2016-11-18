package com.cmstop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XSSCheckFilter implements Filter {
	public String getRemortIP(HttpServletRequest request) {
		if (request.getHeader("HTTP_X_REAL_IP") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("HTTP_X_REAL_IP");
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		response.setCharacterEncoding("UTF-8");
		boolean find = false;
		try {
			String path = XSSCheckFilter.class.getClassLoader().getResource("")
					.toURI().getPath();
			FileInputStream fis = new FileInputStream(new File(path
					+ "ip.properties"));
			Properties prop = new Properties();
			prop.load(fis);
			String ips = prop.getProperty("ip").trim();
			
			String cip = getRemortIP(request);
			
			String[] aip = ips.split(",");
       
			for (int i = 0; i < aip.length; i++) {
//System.out.println(aip[i]+"\t"+cip);
				if (aip[i].trim().equals(cip))
				{
					find = true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	   //System.out.println(find+"\t"+request.getQueryString());
		if (request.getQueryString() != null
				&& request.getQueryString().length() > 2) {
			 filterChain.doFilter(req, resp);
			//request.getRequestDispatcher("/WEB-INF/error.json").forward(request,
				//	response);
		}else{
			 filterChain.doFilter(req, resp);
		}
		
		
	}

	public void destroy() {
	}

	public void init(FilterConfig config) throws ServletException {

	}
}
