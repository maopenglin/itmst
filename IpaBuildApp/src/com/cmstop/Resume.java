package com.cmstop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Resume extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Resume() {
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
	public String getPath() {

		String a = getServletContext().getRealPath("/");

		String[] ar = a.split("/");
		StringBuffer buf = new StringBuffer();
		// buf.append("/");
		for (int i = 0; i < ar.length - 3; i++) {
			buf.append(ar[i] + "/");

		}
		return buf.toString()+"iossrcdir/";
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		String encoding = "UTF-8";
		request.setCharacterEncoding(encoding);
		
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		
		out.println("<HTML>");
		out.println("<HEAD><TITLE>打包服务器自动化</TITLE></HEAD>");
		out.println("<BODY>");
		String path=this.getPath();
		
		Process process = null;
		
		 File file = new File(path);
		 File[] array = file.listFiles();   
         //out.print(path +" count:"+array.length);
	        for(int i=0;i<array.length;i++){   
	              if(array[i].isDirectory()){
	            	  String newPath=path+array[i].getName();
	            	  File tfile = new File(newPath);
	         		  File[] ta = tfile.listFiles();   
	         		  //out.println("new path "+newPath);
	         		  for(int s=0;s<ta.length;s++){
	         			 //out.println("tas  \n"+ta[s].getAbsolutePath());
	         			    String binPath=ta[s].getAbsolutePath();
	         			   File bfile = new File(binPath);
	 	         		  File[] bf = bfile.listFiles();   
	 	         		 // for(int y=0;y<bf.length;y++){
	 	         			 String execPath=ta[s].getAbsolutePath()+"/ios.bin";
	 	         			File efile = new File(execPath);
	 	         			if(efile.exists()){
	 	         				out.print(execPath+"\n");
	 	         			 // if(ta[s].isFile()&&ta[s].getName().equals("ios.bin")){
	 	         				     // out.print("<h4>"+bf[y].getAbsolutePath()+"</h4>");
	 	         				    try {
	 	                               FileInputStream fis = new FileInputStream(execPath);
	 	                               ObjectInputStream ois = new ObjectInputStream(fis);
	 	                               Been  cat = (Been) ois.readObject();
	 	                              out.print("<h3> name:"+cat.getkAppName()+"</h3>");
	 	                             String cmd = "rm -rf "+execPath.replace("ios.bin", "")+cat.downloadPath+"/*";
	 	                             if(cmd.indexOf("iossrcdir/")>0){
	 	                            	
	 	                            	 String []  cmd23={"/bin/sh","-c",cmd};
	 	                            	process =  Runtime.getRuntime().exec(cmd23);
	 	                            	int iretCode = process.waitFor();
	 	                            	SingletonIpa.getInstance().getThread().execute(new IpaRunable(cat,getServletContext().getRealPath("/")));
	 	                            	//out.print("<h3>"+cat.getkAppZhCnName()+"开始打包</h3>");
	 	                             }
	 	            				
	 	            				//process =  Runtime.getRuntime().exec(cmd);
	 	            				//int iretCode = process.waitFor();
	 	                              
	 	                               ois.close();
	 	                       } catch (Exception ex) {
	 	                              out.print(ex.getMessage());
	 	                       }
	 	         			}  
	 	         			 // }
	 	         			  
	 	         		 // }
	         			   
	         		  }
	              }
	              
	            	
	            
	        }
		
		
		out.println("</BODY>");
		out.println("</HTML>");
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
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
