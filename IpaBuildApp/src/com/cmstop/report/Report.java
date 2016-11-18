package com.cmstop.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

import com.cmstop.BuildEntity;
import com.cmstop.DBHelper;
import com.cmstop.ReportEntity;

public class Report extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Report() {
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

		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String buildId=request.getParameter("buildid");
		if(null==buildId){
			buildId="0";
			
		}
		String projectId=request.getParameter("projectid");
		if(null==projectId){
			projectId="0";
			
		}
		String createDate=request.getParameter("createtime");
		String endDate=request.getParameter("endtime");
		if(null!=createDate){
			   if(null==endDate){
				   endDate=createDate;
			   }
			
		}
		String status=request.getParameter("status");
		
		String siteId=request.getParameter("siteid");
		
		String identifier=request.getParameter("platform");
		
		String apptype=request.getParameter("apptype");
		if(null==siteId){
			siteId="0";
			
		}
		int pageNO=0;
		   if(null!=request.getParameter("page")){
			   pageNO=Integer.valueOf(request.getParameter("page"));
		}
		 int pageSize=10;
		   if(null!=request.getParameter("size")){
			   pageSize=Integer.valueOf(request.getParameter("size"));
			   
		   }
		   if(pageSize>50){
			   pageSize=50;
		   }
		ReportEntity report=new ReportEntity();
		
		ObjectMapper objectMapper = new ObjectMapper();
        try {
          objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
          report= DBHelper.select(Long.valueOf(buildId), Integer.valueOf(projectId), Integer.valueOf(siteId),pageNO,pageSize,createDate,endDate,status,apptype,identifier);
         // System.out.println("data size:"+report.datas.size());
         } catch (IOException e) {
           
        }
        out.print(objectMapper.writeValueAsString(report)); 
		out.flush();
		out.close();
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

		doGet(request, response);
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
