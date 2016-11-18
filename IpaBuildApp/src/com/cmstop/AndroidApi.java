package com.cmstop;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class AndroidApi extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public AndroidApi() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	// buildApp(String buildId,String appName,String projectId,String
	// siteId_userId,String appType,String appurl,String appidentifier,String
	// appversion,String filepath)
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();

		String appName = request.getParameter("appname");
		String projectId = request.getParameter("projectid");
		String siteId = request.getParameter("siteid");
		String appType = request.getParameter("apptype");
		String apiurl = request.getParameter("appurl");
		String appidentifier = request.getParameter("appidentifier");
		String appversion = request.getParameter("appversion");
		String downloadPath = request.getParameter("filepath");
		String identity = request.getParameter("identifier");
		Logger log = Logger.getLogger(this.getClass());
		int result = 0;
		long bid = DBHelper.genId(appName + projectId + appType + apiurl
				+ downloadPath + appversion + siteId, appType);
		if (appName == null || projectId == null || siteId == null
				|| appType == null || apiurl == null || appidentifier == null
				|| appversion == null || downloadPath == null) {

		} else {
			System.out.println("result");

			result = DBHelper.buildApp(bid, appName, projectId, siteId,
					appType, apiurl, appidentifier, appversion, downloadPath,
					identity);
		}
		log.error("buildId " + bid + ",appName " + appName + ",projectId "
				+ projectId + ",siteId " + siteId + ",apptype " + appType
				+ ",apiurl" + apiurl + ",appidentifier" + appidentifier
				+ ",appversion " + appversion + ",downloadpath " + downloadPath);

		out.println("{\"status:\":\"" + result + "\",\"buildid:\",\"" + bid
				+ "\"}");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
