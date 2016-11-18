package com.cmstop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.omg.CORBA.UserException;

public class CSRFile extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6776291694007254180L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		response.setContentType("text/json;charset=utf-8");
//		response.setCharacterEncoding("utf-8");
//		PrintWriter out = response.getWriter();
//		out.print("中文");
//		out.flush();
		//request.getRequestDispatcher("/WEB-INF/error.json").forward(request,
				//response);
		
		doPost(request, response);
		
		
		
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
	public String getPath() {

		String a = getServletContext().getRealPath("/");

		String[] ar = a.split("/");
		StringBuffer buf = new StringBuffer();
		// buf.append("/");
		for (int i = 0; i < ar.length - 3; i++) {
			buf.append(ar[i] + "/");

		}
		return buf.toString()+"iosKey/";
	}

	public String getRootPath() {

		String a = getServletContext().getRealPath("/");

		String[] ar = a.split("/");
		StringBuffer buf = new StringBuffer();
		// buf.append("/");
		for (int i = ar.length - 3; i < ar.length - 2; i++) {
			buf.append(ar[i] + "/");

		}
		return buf.toString();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String encoding = "UTF-8";
		request.setCharacterEncoding(encoding);
		response.setContentType("text/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		
		Properties prop2 = new Properties();
		
		Process process = null;
		Runtime rt = Runtime.getRuntime();
		Logger log = Logger.getLogger(this.getClass());
		String userAppStrongEmail = request.getParameter("appStoreEmail");
		try {
			String xp = XSSCheckFilter.class.getClassLoader()
					.getResource("").toURI().getPath();
			FileInputStream fis2 = new FileInputStream(new File(xp
					+ "ip.properties"));
			prop2.load(fis2);
			String keyRootPath=prop2.getProperty("outputPath")+"/keySrc/";
			File f=new File(keyRootPath);
			if(!f.exists()){
				
				f.mkdir();
			}
			String keyPath=keyRootPath;
			if (userAppStrongEmail == null || userAppStrongEmail.equals("")) {

				// out.print("{\"stauts\":\"0\",\"message\":\"email not empty\"}");
				throw new Exception("email error");
			}
			String pkName=""+keyPath+""+userAppStrongEmail+".key";
			String cmd = "openssl genrsa  -out "+pkName+" 2048";
			log.info(cmd);
			process = rt.exec(cmd);

			int iretCode = process.waitFor();
             cmd="chmod 0600 "+pkName+"";
             log.info(cmd);
             process = rt.exec(cmd);
             process.waitFor();
             
              
              
             
              cmd="security import "+pkName+" -k ~/Library/Keychains/login.keychain";
              String []  cmd23={"/bin/sh","-c",cmd};
              System.out.println("private key  : "+cmd);
             process = rt.exec(cmd23);
             
             BufferedReader bufferedReader2 = new BufferedReader(
 					new InputStreamReader(process.getInputStream(),"utf-8"));
 			String line2;
 			StringBuffer buffer2=new StringBuffer();
 			while ((line2 = bufferedReader2.readLine()) != null) {
 				buffer2.append(line2);
 			}
 			System.out.println("import private key :"+buffer2.toString());
 			
            log.info(cmd);
           process.waitFor();
             
            String csr= userAppStrongEmail.substring(0, userAppStrongEmail.indexOf("@"));
            String c="openssl req -new  -key "+pkName+"  -out "+keyPath+csr+".certSigningRequest -subj \"/emailAddress="+userAppStrongEmail+"/CN="+userAppStrongEmail+"/C=CN/ST=BJ/L=BeiJing\"";
            String []  cmd2={"/bin/sh","-c",c};
			 log.info(cmd2);
             process = rt.exec(cmd2);
             
             BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				String line;
				boolean buildOk=false;
				while ((line = bufferedReader.readLine()) != null) {
					
					log.info(line);
					System.out.println(line);
					
				}
				process.waitFor();
				out.print("{\"status\":\"0\",\"\":\"keySrc/" + csr
						+ ".certSigningRequest\"}");
		} catch (Exception e) {
			out.print("{\"status\":\"1\",\"message\":\"" + e.getMessage()
					+ "\"}");
		} finally {
			out.close();
		}
	}
	
	  
}
