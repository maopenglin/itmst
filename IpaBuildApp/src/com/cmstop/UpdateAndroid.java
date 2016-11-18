package com.cmstop;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class UpdateAndroid extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UpdateAndroid() {
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
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
		
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		String buildId=request.getParameter("id");
		String sucess=request.getParameter("status");
		Logger log = Logger.getLogger(this.getClass());
		log.info("buildId "+buildId +"    status:"+sucess);
		if(null==sucess||buildId==null){
			out.println("{\"status\":\"0\"}");
			
		}else{
		if(Integer.valueOf(sucess)>2||Integer.valueOf(sucess)<0){
			out.println("{\"status\":\"0\"}");
		}else{
			
			int result=DBHelper.updateApp(Long.valueOf(buildId), Integer.valueOf(sucess));
			out.println("{\"status\":\""+result+"\"}");
		}
		}
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
